package com.main.merchandising;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.merchandising.domain.Item;

import rx.Observable;
import rx.Single;

@RestController
public class MerchandisingRest {
	
	private MerchandisingService merchandisingService;
	
	public MerchandisingRest(MerchandisingService merchandisingService){
		this.merchandisingService = merchandisingService;
	}
	
	@RequestMapping("/hello")
	public Single<String> hello(){
		return Single.just("hello merchandising service");
	}
	
	@RequestMapping("/hello-list")
	public Single<List<String>> helloList(){
		return Observable.just("string 1", "string 2").toList().toSingle();
	}
	
	@RequestMapping("/brand/{brand}")
	public Single<List<Item>> findByBrand(@PathVariable String brand){
		return this.merchandisingService.findByBrand(brand).toList().toSingle();
	}
	
	@RequestMapping("/id/{id}")
	public Single<Item> findById(@PathVariable String id){
		return this.merchandisingService.findOne(id);
	}
		
}
