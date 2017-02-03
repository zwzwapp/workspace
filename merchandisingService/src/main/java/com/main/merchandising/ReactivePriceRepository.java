package com.main.merchandising;

import org.springframework.data.repository.reactive.RxJava1CrudRepository;

import com.main.merchandising.domain.Price;

import rx.Single;

public interface ReactivePriceRepository extends RxJava1CrudRepository<Price, String>{

	Single<Price> findByItemId(String itemId);
}
