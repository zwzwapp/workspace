package com.main.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.gateway.domain.Comment;
import com.main.gateway.domain.Inventory;
import com.main.gateway.domain.Product;
import com.main.gateway.domain.Summary;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@RestController
public class GatewayRest {
	private Logger logger = LoggerFactory.getLogger(GatewayRest.class);
	
	private MerchandisingClient merchandisingClient;
	private CommentClient commentClient;
	private InventoryClient inventoryClient;
	
	public GatewayRest(MerchandisingClient merchandisingClient,
						CommentClient commentClient,
						InventoryClient inventoryClient){
		this.merchandisingClient = merchandisingClient;
		this.commentClient = commentClient;
		this.inventoryClient = inventoryClient;
	}
	
	@RequestMapping("/api/product")
	public Single<String> helloProduct(){
		return this.merchandisingClient.hello();
	}
		
	
	@RequestMapping("/api/product/id/{id}")	
	public Single<Product> findProductById(@PathVariable String id){
				
		Single<Summary> summary = this.merchandisingClient.findById(id).subscribeOn(Schedulers.io());
		Single<List<Comment>> comment = this.commentClient.findByItemId(id).subscribeOn(Schedulers.io());
		Single<Double> rating = this.commentClient.findRatingByItemId(id).subscribeOn(Schedulers.io());
																			
		return Single.zip(summary, comment, rating,  (s, c, r) -> {
			Product product = new Product();
			product.setSummary(s);
			product.setComments(c);
			product.setRating(r);		
			return product;
		})
		.flatMap(this::loadInventorys)	
		.doOnSuccess(i -> logger.info("find product by id : "+ id))
		.doOnError(e -> logger.info("error : "+ e.getMessage()))		
		.onErrorResumeNext(i -> Single.just(new Product()));						
	}
	
	@RequestMapping("/api/product/brand/{brand}")
	public Single<List<Product>> findProductByBrand(@PathVariable String brand){
						
		return this.merchandisingClient.findByBrand(brand)
					.flatMapObservable(s -> Observable.from(s))
					.map(s -> {
						Product product = new Product();
						product.setSummary(s);
						return product;
					})
					.flatMap(p -> {						
						Single<Double> rating = this.commentClient.findRatingByItemId(p.getSummary().getId());							
						return rating.zipWith(Single.just(p), (r, pr) -> {
							pr.setRating(r);
							return pr;
						})
						.toObservable();						
					})
					.toList()
					.toSingle();		
	}
		
	public Single<Product> loadInventorys(Product product){
		Single<Product> pro = Single.defer(() -> Single.just(product)).subscribeOn(Schedulers.io());
		Single<List<Inventory>> inventorys = Observable.from(product.getSummary().getVariants())
											.map(v -> v.getId())																				
											.flatMap(sku ->  this.inventoryClient.findOnlyCurrentStockBySku(sku).toObservable())
											.toList()
											.subscribeOn(Schedulers.io())
											.toSingle();		
				
		return Single.zip(pro, inventorys, (p, i) -> {			
			p.setInventorys(i);
			return p;
		});																															
	}	
}
