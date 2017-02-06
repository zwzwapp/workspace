package com.main.merchandising;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.main.merchandising.domain.Item;

@Repository
public interface ReactiveItemRepository extends MongoRepository<Item, String>{
	
}
