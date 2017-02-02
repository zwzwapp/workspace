package com.main.merchandising;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;

import com.main.merchandising.domain.Item;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MerchandisingService {
	
	public Flux<Item> findByBrand(String brand);
	
	public Mono<Item> findOne(String id);

}

@Service
class MerchandisingServiceImpl implements MerchandisingService{

	private ReactiveMongoTemplate mongoTemplate;
	private ReactiveItemRepository itemRepository;
	
	public MerchandisingServiceImpl(ReactiveMongoTemplate mongoTemplate,
										ReactiveItemRepository itemRepository){
		this.mongoTemplate = mongoTemplate;
		this.itemRepository = itemRepository;
	}
	
	@Override
	public Flux<Item> findByBrand(String brand) {		
		return this.itemRepository.findByBrand(brand)
							.log();
	}

	@Override
	public Mono<Item> findOne(String id) {		
		return this.itemRepository.findOne(id)
									.log();
	}
	
}
