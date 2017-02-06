package com.main.merchandising.domain;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "price")
public class Price {

	private String id;
	
	@Indexed
	private String itemId;
	
	private double retailPrice;
	
	private double salePrice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	@Override
	public String toString() {
		return "Price [id=" + id + ", itemId=" + itemId + ", retailPrice=" + retailPrice + ", salePrice=" + salePrice
				+ "]";
	}
	
	
}
