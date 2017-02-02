package com.main.merchandising;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.main.merchandising.domain.Item;

import reactor.core.publisher.Flux;

@Repository
public interface ReactiveItemRepository extends ReactiveCrudRepository<Item, String>{

	Flux<Item> findByBrand(String brand);
}
