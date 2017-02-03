package com.main.merchandising;

import org.springframework.stereotype.Service;

import com.main.merchandising.domain.Item;

import rx.Observable;
import rx.Single;

public interface MerchandisingService {
	
	public Observable<Item> findByBrand(String brand);
	
	public Single<Item> findOne(String id);

}

@Service
class MerchandisingServiceImpl implements MerchandisingService{

	private ReactiveItemRepository itemRepository;
	
	public MerchandisingServiceImpl(ReactiveItemRepository itemRepository){
		this.itemRepository = itemRepository;
	}
	
	@Override
	public Observable<Item> findByBrand(String brand) {				
		return this.itemRepository.findByBrand(brand);							
	}

	@Override
	public Single<Item> findOne(String id) {		
		return this.itemRepository.findOne(id).toSingle();									
	}
	
}
