package com.main.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.main.comment.domain.Comment;

import rx.Observable;
import rx.schedulers.Schedulers;

public interface CommentService {

	Observable<Comment> findByItemId(String itemId);
}

@Service
class CommentServiceImpl implements CommentService{
	private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	private ReactiveCommentRepository commentRepository;
		
	public CommentServiceImpl(ReactiveCommentRepository commentRepository){
		this.commentRepository = commentRepository;
	}
	
	@Override
	public Observable<Comment> findByItemId(String itemId) {
		return this.commentRepository.findByItemId(itemId)
					.subscribeOn(Schedulers.computation())
					.doOnNext(i -> logger.info("comment : "+ i.toString()));
	}
	
}
