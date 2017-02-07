package com.main.comment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoRepositories
public class MongoConfiguration extends AbstractReactiveMongoConfiguration{
	
	@Value("${spring.data.mongodb.database}")
	private String databaseName;
	
	@Value("${spring.data.mongodb.uri}")
	private String databaseUri;
	
	 @Bean
	 public ObjectMapper objectMapper() {
		 return Jackson2ObjectMapperBuilder.json().build();
	 }
	 
	 @Bean
	 public ReactiveMongoDatabaseFactory mongoDatabaseFactory(){
		 return new SimpleReactiveMongoDatabaseFactory(mongoClient(), getDatabaseName());
	 }

	 @Bean
	 public ReactiveMongoTemplate reactiveMongoTemplate(){
		 return new ReactiveMongoTemplate(mongoDatabaseFactory());
	 }
	 
	 @Override
	 public MongoClient mongoClient() {		
		System.out.println("database url : "+ databaseUri);
		return MongoClients.create(databaseUri);
	 }

	 @Override
	 protected String getDatabaseName() {	
		return databaseName;
	 }
	
	
}
