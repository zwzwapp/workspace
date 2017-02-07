package com.main.merchandising;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.merchandising.domain.Summary;

import rx.Single;

@RestController
public class MerchandisingRest {
	private Logger logger = LoggerFactory.getLogger(MerchandisingRest.class);
	
	private MerchandisingService merchandisingService;
	
	public MerchandisingRest(MerchandisingService merchandisingService){
		this.merchandisingService = merchandisingService;
	}
	
	@RequestMapping("/hello")
	public String hello(){		
		return "hello merchandising service";		
	}
		
	@GetMapping("/id/{id}")
	public Single<Summary> findByItemId(@PathVariable String id){
		return this.merchandisingService.findById(id)
					.doOnSuccess(i -> logger.info("find by item id : "+ id));									
	}
	
	@GetMapping("/brand/{brand}")
	public Single<List<Summary>> findByBrand(@PathVariable String brand){
		return this.merchandisingService.findByBrand(brand)
					.doOnCompleted(() -> logger.info("find by brand : "+ brand))
					.toList()
					.toSingle();										
	}
}
