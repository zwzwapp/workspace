package com.main.merchandising;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.merchandising.domain.Item;

import reactor.core.publisher.Mono;

@RestController
public class MerchandisingRest {
	
	private MerchandisingService merchandisingService;
	
	public MerchandisingRest(MerchandisingService merchandisingService){
		this.merchandisingService = merchandisingService;
	}
	
	@RequestMapping("/hello")
	public Mono<String> hello(){
		return Mono.just("hello merchandising service");
	}
	
	@RequestMapping("/brand/{brand}")
	public List<Item> findByBrand(@PathVariable String brand){
		return this.merchandisingService.findByBrand(brand).collectList().block();
	}
	
	@RequestMapping("/id/{id}")
	public Item findById(@PathVariable String id){
		return this.merchandisingService.findOne(id).block();
	}
		
}
