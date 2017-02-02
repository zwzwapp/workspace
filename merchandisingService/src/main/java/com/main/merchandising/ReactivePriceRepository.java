package com.main.merchandising;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.main.merchandising.domain.Price;

import reactor.core.publisher.Flux;

public interface ReactivePriceRepository extends ReactiveCrudRepository<Price, String>{

	Flux<Price> findByItemId(String itemId);
}
