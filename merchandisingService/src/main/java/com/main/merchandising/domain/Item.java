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

	@NotNull
	@TextIndexed
	private String category;
	
	private String description;
	
	private String tags;
			
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
