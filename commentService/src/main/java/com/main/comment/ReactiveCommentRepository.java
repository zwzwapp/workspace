package com.main.comment;

import org.springframework.data.repository.reactive.RxJava1SortingRepository;

import com.main.comment.domain.Comment;

import rx.Observable;

public interface ReactiveCommentRepository extends RxJava1SortingRepository<Comment, String>{
	
	Observable<Comment> findByItemId(String itemId);
	
	

}
