package com.main.gateway.domain;

import java.util.List;

public class Product {
	
	private String id;
	
	private String title;
	
	private String brand;
	
	private List<Price> prices;
	
	private List<Comment> comments;
	
	private int rating;
	
	public Product getProductFromSummary(Summary summary){
		
		Product product = new Product();
		product.setId(summary.getId());
		product.setTitle(summary.getTitle());
		product.setBrand(summary.getBrand());
		product.setPrices(summary.getPrices());				
		return product;
	}
		
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

	public List<Price> getPrices() {
		return prices;
	}

	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
