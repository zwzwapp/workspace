package com.main.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.main.gateway.domain.Summary;

import rx.Observable;
import rx.Single;

@FeignClient(name = "merchandising-service")
public interface MerchandisingClient {

	@RequestMapping("/hello")
	public Single<String> hello();

	@RequestMapping(method = RequestMethod.GET, value = "/id/{id}", consumes = "application/json")
	public Single<Summary> findById(@PathVariable("id") String id);
	
	@RequestMapping(method = RequestMethod.GET, value = "/brand/{brand}", consumes = "application/json")
	public Observable<Summary> findByBrand(@PathVariable("brand") String brand);
}
