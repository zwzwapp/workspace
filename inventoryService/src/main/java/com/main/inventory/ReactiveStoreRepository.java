package com.main.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.inventory.domain.Store;

public interface ReactiveStoreRepository extends MongoRepository<Store, String>{

}
