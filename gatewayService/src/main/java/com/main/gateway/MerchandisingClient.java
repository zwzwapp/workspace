package com.main.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.main.gateway.domain.AggreCount;
import com.main.gateway.domain.Summary;

import rx.Single;

@FeignClient(name = "merchandising-service", fallback = MerchandisingClientFallback.class)
public interface MerchandisingClient {

	@RequestMapping("/hello")
	public Single<String> hello();

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}", consumes = "application/json")
	public Single<Summary> findById(@PathVariable("id") String id);
	
	@RequestMapping(method = RequestMethod.GET, value = "/brand/{brand}", consumes = "application/json")
	public Single<List<Summary>> findByBrand(@PathVariable("brand") String brand);
	
	@RequestMapping(method = RequestMethod.GET, value = "/category/{category}", consumes = "application/json")
	public Single<List<Summary>> findByCategoryRegex(@PathVariable("category") String category);
	
	@RequestMapping(method = RequestMethod.GET, value = "/aggregation/brand", consumes = "application/json")
	public Single<List<AggreCount>> aggregationByBrand();
}

@Component
class MerchandisingClientFallback implements MerchandisingClient{

	@Override
	public Single<String> hello() {
		return Single.just("hello merchandising service not working");
	}

	@Override
	public Single<Summary> findById(String id) {		
		return Single.just(new Summary());
	}

	@Override
	public Single<List<Summary>> findByBrand(String brand) {		
		return Single.just(new ArrayList<Summary>());
	}

	@Override
	public Single<List<Summary>> findByCategoryRegex(String category) {
		return Single.just(new ArrayList<Summary>());
	}

	@Override
	public Single<List<AggreCount>> aggregationByBrand() {
		return Single.just(new ArrayList<AggreCount>());
	}
	
}