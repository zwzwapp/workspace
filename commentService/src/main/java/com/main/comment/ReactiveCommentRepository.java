package com.main.comment;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.main.comment.domain.Comment;

public interface ReactiveCommentRepository extends MongoRepository<Comment, String>{
	
	List<Comment> findByItemId(String itemId);
	
	@Query(value = "{'itemId' : ?0}", fields = "{'rating' : 1}")
	List<Comment> findRatingByItemId(String itemId);

	List<Comment> findByUsername(String username);
	
	List<Comment> findByUserEmail(String userEmail);
	
	List<Comment> findByIpAddress(String ipAddress);
}
