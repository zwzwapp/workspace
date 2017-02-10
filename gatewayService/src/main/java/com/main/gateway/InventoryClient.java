package com.main.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.main.gateway.domain.Inventory;

import rx.Single;

@FeignClient(name = "inventory-service", fallback = InventoryClientFallback.class)
public interface InventoryClient {

	@RequestMapping(method = RequestMethod.GET, value = "/current-stock/{sku}")
	public Single<Inventory> findOnlyCurrentStockBySku(@PathVariable("sku") String sku);
}

@Component
class InventoryClientFallback implements InventoryClient{

	@Override
	public Single<Inventory> findOnlyCurrentStockBySku(String sku) {		
		return Single.just(new Inventory());
	}
	
}