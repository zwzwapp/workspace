package com.main.comment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.comment.domain.Comment;

import rx.Single;

@RestController
public class CommentRest {
	private static final Logger logger = LoggerFactory.getLogger(CommentRest.class);
	public CommentService commentService;
	
	public CommentRest(CommentService commentService){
		this.commentService = commentService;
	}
	
	@GetMapping("/hello")
	public Single<String> hello(){
		return Single.just("hello comment service");
	}
	
	@GetMapping("/item-id/{itemId}")
	public Single<List<Comment>> findByItemId(@PathVariable String itemId, 
													@RequestParam(defaultValue = "0") int start,
													@RequestParam(defaultValue = "10") int pageSize){
		return this.commentService
					.findByItemId(itemId, start, pageSize)
					.cache()
					.toList()
					.doOnCompleted(() -> logger.info("finding by item id : "+ itemId))
					.toSingle();					
	}
	
	@GetMapping("/rating/{itemId}")
	public Single<Double> findRatingByItemId(@PathVariable String itemId){
		return this.commentService.findRatingByItemId(itemId)
					.doOnSuccess(i -> logger.info("find rating by item id : "+ itemId));
	}
	
	@GetMapping("/username/{username}")
	public Single<List<Comment>> findByUsername(@PathVariable String username, 
													@RequestParam(defaultValue = "0") int start,
													@RequestParam(defaultValue = "10") int pageSize){
		return this.commentService
				.findByUsername(username, start, pageSize)
				.cache()
				.toList()
				.doOnCompleted(() -> logger.info("finding by username : "+ username))
				.toSingle();		
	}
		
	@GetMapping("/user-email/{userEmail}")
	public Single<List<Comment>> findByUserEmail(@PathVariable String userEmail, 
													@RequestParam(defaultValue = "0") int start,
													@RequestParam(defaultValue = "10") int pageSize){
		return this.commentService
				.findByUserEmail(userEmail, start, pageSize)				
				.cache()
				.toList()
				.doOnCompleted(() -> logger.info("finding by userEmail : "+ userEmail))
				.toSingle();		
	}
	
	@GetMapping("/ip-address/{ipAddress}")
	public Single<List<Comment>> findByIpAddress(@PathVariable String ipAddress, 
													@RequestParam(defaultValue = "0") int start,
													@RequestParam(defaultValue = "10") int pageSize){
		return this.commentService
				.findByIpAddress(ipAddress, start, pageSize)				
				.cache()
				.toList()
				.doOnCompleted(() -> logger.info("finding by ip address : "+ ipAddress))
				.toSingle();		
	}
}
