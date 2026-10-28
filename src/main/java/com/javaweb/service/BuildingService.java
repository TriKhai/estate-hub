package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.BuildingCreateRequest;
import com.javaweb.Beans.BuildingDTO;
import com.javaweb.Beans.BuildingFullCreateRequest;
import com.javaweb.Beans.BuildingUpdateRequest;

public interface BuildingService {
	BuildingDTO getBuildingById(Long id);
	List<BuildingDTO> getBuildingsByRentPrice(Integer minPrice, Integer maxPrice);
	List<BuildingDTO> getAllBuilding(String name, Long districtId, Integer minPrice, Integer maxPrice, Integer page, Integer size, String sortBy, String sortDir);
	Long createBuilding(BuildingCreateRequest request);
	void updateBuilding(Long id, BuildingUpdateRequest request);
	void deleteBuilding(Long id);
	
	Long createBuildingFull(BuildingFullCreateRequest request);
}
