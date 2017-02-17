package com.main.gateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.gateway.domain.AggreCount;
import com.main.gateway.domain.Comment;
import com.main.gateway.domain.Inventory;
import com.main.gateway.domain.Product;
import com.main.gateway.domain.Summary;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

@CrossOrigin
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
			product.setItemId(s.getId());
			product.setTitle(s.getTitle());
			product.setDescription(s.getDescription());
			product.setBrand(s.getBrand());
			product.setCategory(s.getCategory());
			product.setMainImageUrl(s.getMainImageUrl());
			product.setImageUrls(s.getImageUrls());
			product.setSkus(s.getSkus());
			product.setTags(s.getTags());
								
			product.setStartPrice(s.getPrices().stream().min((p1, p2) -> Double.compare(p1.getRetailPrice(), p2.getRetailPrice())).get().getRetailPrice());
			product.setMaxPrice(s.getPrices().stream().max((p1, p2) -> Double.compare(p1.getRetailPrice(), p2.getRetailPrice())).get().getRetailPrice());
			product.setSaleStartPrice(s.getPrices().stream().min((p1, p2) -> Double.compare(p1.getSalePrice(), p2.getSalePrice())).get().getSalePrice());
			product.setSaleMaxPrice(s.getPrices().stream().max((p1, p2) -> Double.compare(p1.getSalePrice(), p2.getSalePrice())).get().getSalePrice());
			
			product.setPropertys(s.getPropertys());
			product.setVariants(s.getVariants());
			
			product.setComments(c);
			product.setRating(r);		
			return product;
		})
		.flatMap(this::loadInventorys)	
		.doOnSuccess(i -> logger.info("find product by id : "+ id))
		.doOnError(e -> logger.info("error : "+ e.getMessage()))		
		.onErrorResumeNext(i -> Single.just(new Product()));						
	}
	
//	@RequestMapping("/api/product/brand/{brand}")
//	public Single<List<Product>> findProductByBrand(@PathVariable String brand,
//														@RequestParam(defaultValue="0") int start, 
//														@RequestParam(defaultValue="25") int pageSize){
//						
//		return this.merchandisingClient.findByBrand(brand)
//					.flatMapObservable(s -> Observable.from(s))
//					.map(s -> {
//						Product product = new Product();
//						product.setSummary(s);
//						return product;
//					})
//					.flatMap(this::loadRating)
//					.toList()
//					.subscribeOn(Schedulers.io())
//					.doOnCompleted(() -> logger.info("find product by brand : "+ brand))
//					.toSingle();		
//	}
	
	@RequestMapping("/api/product/category/{category}")
	public Single<List<Product>> findProductByCategory(@PathVariable String category,
														@RequestParam(defaultValue = "0") int start, 
														@RequestParam(defaultValue = "25") int pageSize){
		return this.merchandisingClient.findByCategoryRegex(category)				
				.flatMapObservable(s -> Observable.from(s))				
				.map(s -> {
					Product product = new Product();
					product.setItemId(s.getId());
					product.setTitle(s.getTitle());
					product.setDescription(s.getDescription());
					product.setBrand(s.getBrand());
					product.setCategory(s.getCategory());
					product.setMainImageUrl(s.getMainImageUrl());
					product.setImageUrls(s.getImageUrls());
					product.setSkus(s.getSkus());
					product.setTags(s.getTags());
										
					product.setStartPrice(s.getPrices().stream().min((p1, p2) -> Double.compare(p1.getRetailPrice(), p2.getRetailPrice())).get().getRetailPrice());
					product.setMaxPrice(s.getPrices().stream().max((p1, p2) -> Double.compare(p1.getRetailPrice(), p2.getRetailPrice())).get().getRetailPrice());
					product.setSaleStartPrice(s.getPrices().stream().min((p1, p2) -> Double.compare(p1.getSalePrice(), p2.getSalePrice())).get().getSalePrice());
					product.setSaleMaxPrice(s.getPrices().stream().max((p1, p2) -> Double.compare(p1.getSalePrice(), p2.getSalePrice())).get().getSalePrice());
					return product;
				})										
				.flatMap(this::loadRating)				
				.toList()
				.subscribeOn(Schedulers.io())
				.doOnCompleted(() -> logger.info("find product by category : "+ category))
				.toSingle();	
	}
	
	@GetMapping("/api/product/aggregation")
	public Single<List<AggreCount>> aggregationByBrand(){
		return this.merchandisingClient.aggregationByBrand()
										.subscribeOn(Schedulers.io())
										.doOnSuccess(i -> logger.info("aggregation by brand"));
	}
	
	public Observable<Product> loadRating(Product product){
		Single<Double> rating = this.commentClient.findRatingByItemId(product.getItemId());							
		return rating.zipWith(Single.just(product), (r, pr) -> {
			pr.setRating(r);
			return pr;
		})
		.toObservable();
	}
			
	public Single<Product> loadInventorys(Product product){
		Single<Product> pro = Single.defer(() -> Single.just(product)).subscribeOn(Schedulers.io());
		Single<List<Inventory>> inventorys = Observable.from(product.getSkus())																														
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
