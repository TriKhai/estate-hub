package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.RentAreaCreateRequest;
import com.javaweb.Beans.RentAreaDTO;
import com.javaweb.repository.entity.RentAreaEntity;

public interface RentAreaService {
	List<RentAreaDTO> getRentAreaByBuildingId(Long buildingId); 
	RentAreaDTO createRentArea(Long buildingId, RentAreaCreateRequest request);
	void deleteRentArea(Long buildingId, Long rentId);
}
