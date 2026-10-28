package com.javaweb.Beans;

import java.util.List;

public class BuildingFullCreateRequest {
	private String name;
	private String street;
	private String ward;
	private Long districtId;
	private Integer numberOfBasement;
	private Integer rentPrice;
	private List<Integer> rentAreas;
	private List<Long> rentTypeIds;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Integer getNumberOfBasement() {
		return numberOfBasement;
	}
	public void setNumberOfBasement(Integer numberOfBasement) {
		this.numberOfBasement = numberOfBasement;
	}
	public Integer getRentPrice() {
		return rentPrice;
	}
	public void setRentPrice(Integer rentPrice) {
		this.rentPrice = rentPrice;
	}
	public List<Integer> getRentAreas() {
		return rentAreas;
	}
	public void setRentAreas(List<Integer> rentAreas) {
		this.rentAreas = rentAreas;
	}
	public List<Long> getRentTypeIds() {
		return rentTypeIds;
	}
	public void setRentTypeIds(List<Long> rentTypeIds) {
		this.rentTypeIds = rentTypeIds;
	}
	
}
