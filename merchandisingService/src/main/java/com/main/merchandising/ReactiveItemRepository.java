package com.main.merchandising;

import org.springframework.data.repository.reactive.RxJava1CrudRepository;
import org.springframework.stereotype.Repository;

import com.main.merchandising.domain.Item;

import rx.Observable;

@Repository
public interface ReactiveItemRepository extends RxJava1CrudRepository<Item, String>{

	Observable<Item> findByBrand(String brand);
}
