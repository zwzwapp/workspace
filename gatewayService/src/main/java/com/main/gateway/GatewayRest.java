package com.main.gateway;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.gateway.domain.Comment;
import com.main.gateway.domain.Product;

import rx.Observable;
import rx.Single;

@RestController
public class GatewayRest {

	private MerchandisingClient merchandisingClient;
	private CommentClient commentClient;
	
	public GatewayRest(MerchandisingClient merchandisingClient,
						CommentClient commentClient){
		this.merchandisingClient = merchandisingClient;
		this.commentClient = commentClient;
	}
	
	@RequestMapping("/api/product")
	public Single<String> helloProduct(){
		return this.merchandisingClient.hello();
	}
		
	@RequestMapping("/api/product/id/{id}")	
	public Single<Product> findProductById(@PathVariable String id){
		Single<Product> product =  this.merchandisingClient.findById(id)
					.map(summary -> {
						Product p = new Product();
						p.setId(summary.getId());
						p.setTitle(summary.getTitle());
						p.setBrand(summary.getBrand());
						p.setPrices(summary.getPrices());				
						return p;
					});
		
		Single<List<Comment>> comments = this.commentClient.findByItemId(id);
		
		return Single.zip(product, comments, (p, c) -> {
			p.setComments(c);
			return p;
		});			
	}
	
	@RequestMapping("/api/product/brand/{brand}")
	public Single<List<Product>> findProductByBrand(@PathVariable String brand){
		return this.merchandisingClient.findByBrand(brand)
					.toObservable()
					.flatMap( s -> Observable.from(s))
					.map(summary -> {						
						Product product = new Product();
						product.setId(summary.getId());
						product.setTitle(summary.getTitle());
						product.setBrand(summary.getBrand());
						product.setPrices(summary.getPrices());				
						return product;
					})
					.toList()
					.toSingle();
	}
}
