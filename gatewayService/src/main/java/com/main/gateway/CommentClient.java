package com.main.gateway;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.main.gateway.domain.Comment;

import rx.Single;

@FeignClient(name = "comment-service", fallback = CommentClientFallback.class)
public interface CommentClient {

	@RequestMapping(method = RequestMethod.GET, value = "/item-id/{itemId}")
	public Single<List<Comment>> findByItemId(@PathVariable("itemId") String itemId);
	
	@RequestMapping(method = RequestMethod.GET, value = "/rating/{itemId}")
	public Single<Double> findRatingByItemId(@PathVariable("itemId") String itemId);
}

@Component
class CommentClientFallback implements CommentClient{

	@Override
	public Single<List<Comment>> findByItemId(String itemId) {		
		return Single.just(new ArrayList<Comment>());
	}

	@Override
	public Single<Double> findRatingByItemId(String itemId) {		
		return Single.just(0.0);
	}
	
}
