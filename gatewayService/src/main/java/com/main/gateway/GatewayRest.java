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
				
		return this.merchandisingClient.findById(id)
					.map(summary -> {
						Product p = new Product();
						p.setSummary(summary);
						return p;
					})					
					.flatMap(this::loadRating)
					.doOnSuccess(i -> logger.info("load rating..."))
					.flatMap(this::loadComments)
					.doOnSuccess(i -> logger.info("load comments..."))
					.flatMap(this::loadInventorys)
					.doOnSuccess(i -> logger.info("load inventorys..."));					
	}
	
	/*@RequestMapping("/api/product/brand/{brand}")
	public Single<List<Product>> findProductByBrand(@PathVariable String brand){
		return this.merchandisingClient.findByBrand(brand)
					.flatMapObservable(s -> Observable.from(s))					
					.map(summary -> {						
						
						Product product = new Product();
						product.setId(summary.getId());
						product.setTitle(summary.getTitle());
						product.setBrand(summary.getBrand());
						product.setPrices(summary.getPrices());				
						return product;
					})
					.flatMap(this::loadComments)
					.toList()
					.toSingle();
	}*/
	
	public Single<Product> loadComments(Product product){
		
		Single<List<Comment>> comments = this.commentClient.findByItemId(product.getSummary().getId()).subscribeOn(Schedulers.io());
		Single<Product> pro = Single.just(product).subscribeOn(Schedulers.io());
		return Single.zip(pro, comments, (p, c) -> {
			
			p.setComments(c);
			return p;
		});		
	}
	
	public Single<Product> loadInventorys(Product product){
		Single<Product> pro = Single.defer(() -> Single.just(product)).subscribeOn(Schedulers.io());
		Single<List<Inventory>> inventorys = Observable.from(product.getSummary().getVariants())
											.map(v -> v.getId())																				
											.flatMap(sku -> {																								
												return this.inventoryClient.findOnlyCurrentStockBySku(sku).toObservable();												
											})
											.toList()
											.subscribeOn(Schedulers.io())
											.toSingle();		
				
		return Single.zip(pro, inventorys, (p, i) -> {
			
			p.setInventorys(i);
			return p;
		});																															
	}	
	
	public Single<Product> loadRating(Product product){
		Single<Product> pro = Single.defer(() -> Single.just(product)).subscribeOn(Schedulers.io());
		Single<Double> rating = Single.defer(() -> this.commentClient.findRatingByItemId(product.getSummary().getId()).subscribeOn(Schedulers.io()));
		
		return Single.zip(pro, rating, (p, r) -> {
			p.setRating(r);
			return p;
		});
	}
}
