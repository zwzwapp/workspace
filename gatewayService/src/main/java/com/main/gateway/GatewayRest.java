package com.main.gateway;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rx.Single;

@RestController
public class GatewayRest {

	private MerchandisingClient merchandisingClient;
	
	public GatewayRest(MerchandisingClient merchandisingClient){
		this.merchandisingClient = merchandisingClient;
	}
	
	@RequestMapping("/api/product")
	public Single<String> helloProduct(){
		return this.merchandisingClient.hello();
	}
	
	@RequestMapping("/api/product/id/{id}")
	public Single<Object> findProductById(@PathVariable String id){
		return this.merchandisingClient.findById(id);
	}
	
	@RequestMapping("/api/product/brand/{brand}")
	public Single<List<Product>> findProductByBrand(@PathVariable String brand){
		return this.merchandisingClient.findByBrand(brand)									
					.toList()
					.toSingle();
	}
}
