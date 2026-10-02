package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.BuildingDTO;

public interface BuildingService {
	List<BuildingDTO> getAllBuilding(String name, String districtId, List<String> type);
}
