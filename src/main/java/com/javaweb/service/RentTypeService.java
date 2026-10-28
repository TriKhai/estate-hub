package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.RentTypeDTO;

public interface RentTypeService {
	List<RentTypeDTO> getAllRentTypes();
	List<RentTypeDTO> getRentTypesByBuildingId(Long buildingId); 
	void assignRentType(Long buildingId, Long rentTypeId);
	void removeRentType(Long buildingId, Long rentTypeId);
}
