package com.main.gateway;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.main.gateway.domain.Comment;

import rx.Single;

@FeignClient(name = "comment-service")
public interface CommentClient {

	@RequestMapping(method = RequestMethod.GET, value = "/item-id/{itemId}")
	public Single<List<Comment>> findByItemId(@PathVariable("itemId") String itemId);
}
