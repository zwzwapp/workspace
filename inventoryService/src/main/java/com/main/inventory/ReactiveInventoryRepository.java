package com.main.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.main.inventory.domain.Inventory;

public interface ReactiveInventoryRepository extends MongoRepository<Inventory, String>{
	
	@Query(value = "{'sku' : ?0}", fields = "{'currentStock' : 1}")
	Inventory findCurrentStockBySku(String sku);

	Inventory findBySku(String sku);
}
