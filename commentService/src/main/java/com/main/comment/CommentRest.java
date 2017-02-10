package com.main.comment;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.main.comment.domain.Comment;

import rx.Single;

@RestController
public class CommentRest {

	public CommentService commentService;
	
	public CommentRest(CommentService commentService){
		this.commentService = commentService;
	}
	
	@GetMapping("/item-id/{itemId}")
	public Single<List<Comment>> findByItemId(@PathVariable String itemId, 
													@RequestParam(defaultValue = "0") int start,
													@RequestParam(defaultValue = "10") int pageSize){
		return this.commentService
					.findByItemId(itemId, start, pageSize)
					.cache()
					.toList()
					.toSingle();					
	}
	
	@GetMapping("/rating/{itemId}")
	public Single<Double> findRatingByItemId(@PathVariable String itemId){
		return this.commentService.findRatingByItemId(itemId);
	}
		
}
