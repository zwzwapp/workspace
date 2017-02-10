package com.main.gateway.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
	
	private Summary summary;
	
	private List<Comment> comments;
			
	private double rating;
		
	private List<Inventory> inventorys;

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public List<Inventory> getInventorys() {
		return inventorys;
	}

	public void setInventorys(List<Inventory> inventorys) {
		this.inventorys = inventorys;
	}

	
}
