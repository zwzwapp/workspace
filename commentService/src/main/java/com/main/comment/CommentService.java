package com.main.comment;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.main.comment.domain.Comment;

import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;

public interface CommentService {
	
	Single<Comment> saveComment(Comment comment);
	
	Observable<Comment> findByItemId(String itemId, int start, int pageSize);
	
	Single<Double> findRatingByItemId(String itemId);
	
	Observable<Comment> findByUsername(String username, int start, int pageSize);
	
	Observable<Comment> findByUserEmail(String userEmail, int start, int pageSize);
	
	Observable<Comment> findByIpAddress(String ipAddress, int start, int pageSize);
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
	public Observable<Comment> findByItemId(String itemId, int start, int pageSize) {
		
		return Observable.defer(() -> Observable.from(this.commentRepository.findByItemId(itemId)))
					.skip(start)
					.take(pageSize)
					.subscribeOn(Schedulers.computation())										
					.doOnCompleted(() -> logger.info("finding comments by item id start from " + start + " page size "+ pageSize))
					.doOnError(i -> logger.error(i.getMessage()))
					.onErrorResumeNext(Observable.from(new ArrayList<Comment>()));	
	}
	
	@Override
	public Single<Double> findRatingByItemId(String itemId){
					
		return Observable.defer(() -> Observable.from(this.commentRepository.findRatingByItemId(itemId)))					
					.map(comment -> comment.getRating())
					.toList()
					.map(list -> {
						DecimalFormat df = new DecimalFormat("#.##");
						Double result = list.stream().mapToInt(i -> i).average().getAsDouble();
						return Double.valueOf(df.format(result));
					})		
					.doOnCompleted(() -> logger.info("calculate the rating by item id : "+ itemId))
					.onErrorResumeNext(Observable.just(0.0))
					.toSingle();										
	}

	@Override
	public Observable<Comment> findByUsername(String username, int start, int pageSize) {
		return Observable.defer(() -> Observable.from(this.commentRepository.findByUsername(username)))
						.skip(start)
						.take(pageSize)
						.subscribeOn(Schedulers.io())
						.doOnCompleted(() -> logger.info("finding comments by given user name : "+ username))
						.doOnError(i -> logger.error(i.getMessage()))
						.onErrorResumeNext(Observable.from(new ArrayList<Comment>()));		
	}

	@Override
	public Observable<Comment> findByUserEmail(String userEmail, int start, int pageSize) {
		return Observable.defer(() -> Observable.from(this.commentRepository.findByUserEmail(userEmail)))
				.skip(start)
				.take(pageSize)
				.subscribeOn(Schedulers.io())
				.doOnCompleted(() -> logger.info("finding comments by given user email : "+ userEmail))
				.doOnError(i -> logger.error(i.getMessage()))
				.onErrorResumeNext(Observable.from(new ArrayList<Comment>()));		
	}

	@Override
	public Observable<Comment> findByIpAddress(String ipAddress, int start, int pageSize) {
		return Observable.defer(() -> Observable.from(this.commentRepository.findByIpAddress(ipAddress)))
				.skip(start)
				.take(pageSize)
				.subscribeOn(Schedulers.io())
				.doOnCompleted(() -> logger.info("finding comments by given ip address : "+ ipAddress))
				.doOnError(i -> logger.error(i.getMessage()))
				.onErrorResumeNext(Observable.from(new ArrayList<Comment>()));
	}
	
}
