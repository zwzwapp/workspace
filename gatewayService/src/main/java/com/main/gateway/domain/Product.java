package com.main.gateway.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Product {
	
	private String itemId;
	
	private String title;
	
	private String description;
	
	private String brand;
	
	private String category;
	
	private String mainImageUrl;
	
	private List<String> imageUrls;
	
	private List<String> skus;
	
	private String tags;
	
	private double startPrice;
	
	private double maxPrice;
	
	private Double rating;
	
	private double saleStartPrice;
	
	private double saleMaxPrice;
	
	private boolean moreColors;
	
	private List<Property> propertys;
	
	private List<Variant> variants;
		
	private List<Comment> comments;
					
	private List<Inventory> inventorys;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getMainImageUrl() {
		return mainImageUrl;
	}

	public void setMainImageUrl(String mainImageUrl) {
		this.mainImageUrl = mainImageUrl;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(double startPrice) {
		this.startPrice = startPrice;
	}

	public double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public double getSaleStartPrice() {
		return saleStartPrice;
	}

	public void setSaleStartPrice(double saleStartPrice) {
		this.saleStartPrice = saleStartPrice;
	}

	public double getSaleMaxPrice() {
		return saleMaxPrice;
	}

	public void setSaleMaxPrice(double saleMaxPrice) {
		this.saleMaxPrice = saleMaxPrice;
	}

	public boolean isMoreColors() {
		return moreColors;
	}

	public void setMoreColors(boolean moreColors) {
		this.moreColors = moreColors;
	}

	public List<Property> getPropertys() {
		return propertys;
	}

	public void setPropertys(List<Property> propertys) {
		this.propertys = propertys;
	}

	public List<Variant> getVariants() {
		return variants;
	}

	public void setVariants(List<Variant> variants) {
		this.variants = variants;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Inventory> getInventorys() {
		return inventorys;
	}

	public void setInventorys(List<Inventory> inventorys) {
		this.inventorys = inventorys;
	}

	
}
