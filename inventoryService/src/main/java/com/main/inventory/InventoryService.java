package com.main.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import rx.Single;

public interface InventoryService {

	public Single<Integer> findOnlyCurrentStockBySku(String sku);
}

@Service
class InventoryServiceImpl implements InventoryService{	
	private Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);
	
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

	@Override
	public Single<Integer> findOnlyCurrentStockBySku(String sku) {
		
		return Single.just(this.inventoryRepository.findCurrentStockBySku(sku))
					.map(i -> i.getCurrentStock())
					.onErrorResumeNext(i -> {
						logger.error("error : "+ i.getMessage());
						return Single.just(0);
					});
	}
}
