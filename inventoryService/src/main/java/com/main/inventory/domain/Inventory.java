package com.main.inventory.domain;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@Document(collection = "inventory")
public class Inventory {
	
	@Id
	private String sku;
	
	private String itemId;
	
	private String name;
	
	private Integer currentStock;
	
	private Integer lowStock;
	
	private boolean alertNotifyLowStock;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
	public LocalDateTime lastUpdate;
	
	private Store store;
	
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(Integer currentStock) {
		this.currentStock = currentStock;
	}

	public Integer getLowStock() {
		return lowStock;
	}

	public void setLowStock(Integer lowStock) {
		this.lowStock = lowStock;
	}

	public boolean isAlertNotifyLowStock() {
		return alertNotifyLowStock;
	}

	public void setAlertNotifyLowStock(boolean alertNotifyLowStock) {
		this.alertNotifyLowStock = alertNotifyLowStock;
	}

	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}
	
}
