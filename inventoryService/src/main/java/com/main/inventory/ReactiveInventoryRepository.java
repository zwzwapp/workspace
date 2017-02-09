package com.main.inventory;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.inventory.domain.Inventory;

public interface ReactiveInventoryRepository extends MongoRepository<Inventory, String>{

}
