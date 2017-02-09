package com.main.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import rx.Single;

@RestController
public class InventoryRest {

	private InventoryService inventoryService;
	
	public InventoryRest(InventoryService inventoryService){
		this.inventoryService = inventoryService;
	}
	
	@GetMapping("/hello")
	public Single<String> hello(){
		return Single.just("hello inventory Service");
	}
	
	@GetMapping("/current-stock/{sku}")
	public Single<Integer> findCurrentStockBySku(@PathVariable String sku){
		return this.inventoryService.findOnlyCurrentStockBySku(sku);
	}
}
