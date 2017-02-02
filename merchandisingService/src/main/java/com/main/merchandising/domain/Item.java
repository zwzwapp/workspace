package com.main.merchandising.domain;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "item")
public class Item {

	private String id;
	
	@NotNull
	@TextIndexed
	private String title;
	
	@NotNull
	@TextIndexed
	private String brand;

	public Item(){}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
		
}
