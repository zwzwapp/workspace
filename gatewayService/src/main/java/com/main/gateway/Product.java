package com.main.gateway;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String title;
	
	private String brand;
	
	private List<Price> prices;

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
	
	
}