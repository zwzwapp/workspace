package com.main.inventory;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

public interface InventoryService {

}

@Service
class InventoryServiceImpl implements InventoryService{
	
	private MongoTemplate mongoTemplate;
	private ReactiveInventoryRepository inventoryRepository;
	private ReactiveStoreRepository storeRepository;
	
	public InventoryServiceImpl(MongoTemplate mongoTemplate,
								ReactiveInventoryRepository inventoryRepository,
								ReactiveStoreRepository storeRepository){
		this.mongoTemplate = mongoTemplate;
		this.inventoryRepository = inventoryRepository;
		this.storeRepository = storeRepository;
	}
}
