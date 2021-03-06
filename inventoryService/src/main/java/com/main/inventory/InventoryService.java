package com.main.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.main.inventory.domain.Inventory;

import rx.Single;

public interface InventoryService {

	public Single<Inventory> findOnlyCurrentStockBySku(String sku);
	
	public Single<Inventory> findBySku(String sku);
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
	public Single<Inventory> findOnlyCurrentStockBySku(String sku) {
		
		return Single.just(this.inventoryRepository.findCurrentStockBySku(sku))		
					.doOnSuccess(i -> logger.info("find only urrent stock by sku : "+ sku))
					.onErrorResumeNext(i -> {
						logger.error("error : "+ i.getMessage());
						return Single.just(new Inventory());
					});
	}

	@Override
	public Single<Inventory> findBySku(String sku) {
		return Single.just(this.inventoryRepository.findBySku(sku))
						.doOnSuccess(i -> logger.info("find by sku : "+ sku))
						.onErrorResumeNext(i -> {
							logger.error("error : "+ i.getMessage());
							return Single.just(new Inventory());
						});
	}
}
