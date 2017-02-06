package com.main.merchandising;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.merchandising.domain.Price;

public interface ReactivePriceRepository extends MongoRepository<Price, String>{
	
}
