package com.main.comment;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public Single<List<Comment>> findByItemId(@PathVariable String itemId){
		return this.commentService
					.findByItemId(itemId)
					.toList()
					.toSingle();					
	}
}
