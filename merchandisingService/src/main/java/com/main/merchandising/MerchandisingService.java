package com.main.merchandising;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.main.merchandising.domain.Item;
import com.main.merchandising.domain.Summary;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public interface MerchandisingService {
	
	public Single<Summary> findById(String id);
	
	public Observable<Summary> findByBrand(String brand);
}

@Service
class MerchandisingServiceImpl implements MerchandisingService{	
	private Logger logger = LoggerFactory.getLogger(MerchandisingServiceImpl.class);
			
	private MongoTemplate mongoTemplate;
	private ReactiveItemRepository itemRepository;
	private ReactivePriceRepository priceRepository;
	
	public MerchandisingServiceImpl(MongoTemplate mongoTemplate,
										ReactiveItemRepository itemRepository,
										ReactivePriceRepository priceRepository){
		this.mongoTemplate = mongoTemplate;
		this.itemRepository = itemRepository;
		this.priceRepository = priceRepository;
	}

	@Override
	public Single<Summary> findById(String id) {
		Aggregation agg = newAggregation(
				match(Criteria.where("_id").is(id)),								
				lookup("price", "_id", "itemId", "prices")				
				);
		AggregationResults<Summary> result = this.mongoTemplate.aggregate(agg, Item.class, Summary.class);
		return Single.just(result.getUniqueMappedResult())				
					.subscribeOn(Schedulers.newThread())
					.doOnSuccess(i -> logger.info("summary : "+ i.toString()));
	}

	@Override
	public Observable<Summary> findByBrand(String brand) {
		Aggregation agg = newAggregation(
				match(Criteria.where("brand").is(brand)),
				lookup("price", "_id", "itemId", "prices")
				);
		AggregationResults<Summary> result = this.mongoTemplate.aggregate(agg, Item.class, Summary.class);
		return Observable.fromIterable(result.getMappedResults())		
					.subscribeOn(Schedulers.newThread())
					.doOnNext(i -> logger.info("summary : "+ i.toString()));
	}
	
	
		
}
