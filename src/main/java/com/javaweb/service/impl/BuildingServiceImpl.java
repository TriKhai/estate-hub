package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.BuildingDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;
	
	@Override
	public List<BuildingDTO> getAllBuilding(String name, String districtId, List<String> type) {
		
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(name);
		List<BuildingDTO> result = new ArrayList<BuildingDTO>();
		
		for (BuildingEntity item: buildingEntities) {
			BuildingDTO building = new BuildingDTO();
			building.setName(item.getName());
			building.setAddress(item.getStreet() + ", " + item.getWard());
			building.setNumberOfBasement(item.getNumberOfBasement());
			
			result.add(building);
		}
		
		return result;
	}
	
}
