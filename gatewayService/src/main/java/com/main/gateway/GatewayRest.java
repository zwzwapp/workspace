package com.main.gateway;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.gateway.domain.Comment;
import com.main.gateway.domain.Inventory;
import com.main.gateway.domain.Product;

import rx.Observable;
import rx.Single;

@RestController
public class GatewayRest {

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
					.flatMapObservable(this::loadComments)						
					.flatMap(this::loadInventorys)
					.toSingle();				
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
	
	public Observable<Product> loadComments(Product product){
		
		Single<List<Comment>> comments = this.commentClient.findByItemId(product.getSummary().getId());
		return Observable.zip(Observable.just(product), comments.toObservable(), (p, c) -> {
			
			p.setComments(c);
			return p;
		});		
	}
	
	public Observable<Product> loadInventorys(Product product){
		Observable<List<Inventory>> inventorys = Observable.from(product.getSummary().getVariants())
											.map(v -> v.getId())											
											.flatMap(sku -> {												
												return this.inventoryClient.findOnlyCurrentStockBySku(sku).toObservable();												
											})
											.toList();
				
		return Observable.zip(Observable.just(product), inventorys, (p, i) -> {
			
			p.setInventorys(i);
			return p;
		});																															
	}	
}
