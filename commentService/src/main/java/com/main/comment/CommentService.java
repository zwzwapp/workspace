package com.main.comment;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.main.comment.domain.Comment;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

public interface CommentService {
	
	Single<Comment> saveComment(Comment comment);
	
	Observable<Comment> findByItemId(String itemId);
	
	Single<Double> findRatingByItemId(String itemId);
}

@Service
class CommentServiceImpl implements CommentService{
	private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	private ReactiveCommentRepository commentRepository;
		
	public CommentServiceImpl(ReactiveCommentRepository commentRepository){
		this.commentRepository = commentRepository;
	}
	
	@Override
	public Single<Comment> saveComment(Comment comment){		
		return Single.just(comment)
						.map(c -> {
							c.setRegisterDate(LocalDateTime.now());
							return c;
						})
						.doOnSuccess(c -> {
							logger.info("saving new comment : "+ comment.toString());
							this.commentRepository.save(c);
						});
	}
	
	@Override
	public Observable<Comment> findByItemId(String itemId) {
		return Observable.from(this.commentRepository.findByItemId(itemId))
					.take(10)
					.subscribeOn(Schedulers.computation())					
					.doOnNext(i -> logger.info("comment : "+ i.toString()));
	}
	
	@Override
	public Single<Double> findRatingByItemId(String itemId){
					
		return Observable.from(this.commentRepository.findRatingByItemId(itemId))					
					.map(comment -> comment.getRating())
					.toList()
					.map(list -> {
						DecimalFormat df = new DecimalFormat("#.##");
						Double result = list.stream().mapToInt(i -> i).average().getAsDouble();
						return Double.valueOf(df.format(result));
					})			
					.onErrorResumeNext(Observable.just(0.0))
					.toSingle();										
	}
	
}
