package com.main.merchandising;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.main.merchandising.domain.AggreCount;
import com.main.merchandising.domain.Item;
import com.main.merchandising.domain.Summary;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

public interface MerchandisingService {
	
	public String hello();
	
	public Single<Item> saveItem(Item item);
	
	public Single<Summary> findById(String id);
	
	public Observable<Summary> findByBrand(String brand, int start, int pageSize);
	
	public Observable<Summary> findByCategoryRegex(String category, int start, int pageSize);
	
	public Observable<AggreCount> aggregationByBrand();
}

@Service
class MerchandisingServiceImpl implements MerchandisingService{	
	private Logger logger = LoggerFactory.getLogger(MerchandisingServiceImpl.class);
			
	private MongoTemplate mongoTemplate;
	private ReactiveItemRepository itemRepository;
	private ReactivePriceRepository priceRepository;
	private ReactiveVariantRepository variantRepository;
	
	public MerchandisingServiceImpl(MongoTemplate mongoTemplate,
										ReactiveItemRepository itemRepository,
										ReactivePriceRepository priceRepository,
										ReactiveVariantRepository variantRepository){
		this.mongoTemplate = mongoTemplate;
		this.itemRepository = itemRepository;
		this.priceRepository = priceRepository;
		this.variantRepository = variantRepository;		
	}
	
	@Override
	public String hello(){
		logger.info("say hello");
		return "hello merchandising world"; 
	}
		
	@Override
	public Single<Item> saveItem(Item item) {		
		return Single.just(this.itemRepository.save(item))
						.doOnSuccess(i -> logger.info("saving new item"));						
	}
		
	@Override
	public Single<Summary> findById(String id) {
		Aggregation agg = newAggregation(
				match(Criteria.where("_id").is(id)),								
				lookup("price", "_id", "itemId", "prices"),
				lookup("variant", "_id", "itemId", "variants")
				);
		AggregationResults<Summary> result = this.mongoTemplate.aggregate(agg, Item.class, Summary.class);
		return Single.just(result.getUniqueMappedResult())				
					.subscribeOn(Schedulers.newThread())
					.doOnSuccess(i -> logger.info("summary : "+ i.toString()));
	}

	@Override
	public Observable<Summary> findByBrand(String brand, int start, int pageSize) {
		Aggregation agg = newAggregation(
				match(Criteria.where("brand").is(brand)),
				lookup("price", "_id", "itemId", "prices"),
				lookup("variant", "_id", "itemId", "variants")
				);
		AggregationResults<Summary> result = this.mongoTemplate.aggregate(agg, Item.class, Summary.class);		
		return Observable.from(result.getMappedResults())		
					.skip(start)
					.take(pageSize)
					.subscribeOn(Schedulers.newThread())
					.doOnCompleted(() -> logger.info("find by brand : " + brand))
					.doOnError(i -> logger.error("error : "+ i.getMessage()))
					.onErrorResumeNext(i -> Observable.from(new ArrayList<Summary>()));					
	}

	@Override
	public Observable<Summary> findByCategoryRegex(String category, int start, int pageSize) {
		Aggregation agg = newAggregation(
				match(Criteria.where("category").regex(category)),
				lookup("price", "_id", "itemId", "prices"),
				lookup("variant", "_id", "itemId", "variants")
				);
		AggregationResults<Summary> result = this.mongoTemplate.aggregate(agg, Item.class, Summary.class);
		return Observable.from(result.getMappedResults())
							.skip(start)
							.take(pageSize)
							.subscribeOn(Schedulers.newThread())
							.doOnCompleted(() -> logger.info("find by category regex : " + category))
							.doOnError(i -> logger.error("error : "+ i.getMessage()))
							.onErrorResumeNext(i -> Observable.from(new ArrayList<Summary>()));
	}

	@Override
	public Observable<AggreCount> aggregationByBrand() {
		Aggregation agg = newAggregation(
				project("brand"),
				group("brand").count().as("count"),
				project("count").and("name").previousOperation(),
				sort(Sort.Direction.DESC, "count")
				);
		AggregationResults<AggreCount> result = this.mongoTemplate.aggregate(agg, Item.class, AggreCount.class);
		return Observable.from(result.getMappedResults())
						.subscribeOn(Schedulers.io())
						.doOnCompleted(() -> logger.info("aggregation by brand"))
						.doOnError(e -> logger.error("error : "+ e.getMessage()))
						.onErrorResumeNext(i -> Observable.from(new ArrayList<AggreCount>()));
	}

	
		
}
