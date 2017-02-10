package com.main.merchandising;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.main.merchandising.domain.Variant;

public interface ReactiveVariantRepository extends MongoRepository<Variant, String>{

}
