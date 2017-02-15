package com.main.merchandising;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.merchandising.domain.AggreCount;
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
		return this.merchandisingService.hello();		
	}
		
	@GetMapping("/id/{id}")
	public Single<Summary> findByItemId(@PathVariable String id){
		return this.merchandisingService.findById(id)					
					.doOnSuccess(i -> logger.info("find by item id : "+ id));										
	}
	
	@GetMapping("/brand/{brand}")
	public Single<List<Summary>> findByBrand(@PathVariable String brand,
												@RequestParam(defaultValue = "0") int start,
												@RequestParam(defaultValue = "25") int pageSize){
		return this.merchandisingService.findByBrand(brand, start, pageSize)
					.doOnCompleted(() -> logger.info("find by brand : "+ brand))
					.cache()
					.toList()
					.toSingle();										
	}
	
	@GetMapping("/category/{category}")
	public Single<List<Summary>> findByCategoryRegex(@PathVariable String category,
														@RequestParam(defaultValue = "0") int start,
														@RequestParam(defaultValue = "25") int pageSize){
		return this.merchandisingService.findByCategoryRegex(category, start, pageSize)
					.doOnCompleted(() -> logger.info("find by category regex : "+ category))
					.cache()
					.toList()
					.toSingle();
	}
	
	@GetMapping("/aggregation/brand")
	public Single<List<AggreCount>> aggregationByBrand(){
		return this.merchandisingService.aggregationByBrand()
					.doOnCompleted(() -> logger.info("aggregation by brand"))
					.toList()
					.toSingle();
	}
}
