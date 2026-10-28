package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.RentAreaCreateRequest;
import com.javaweb.Beans.RentAreaDTO;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.customException.InvalidRentAreaException;
import com.javaweb.customException.RentAreaNotFoundException;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.service.RentAreaService;

@Service
public class RentAreaServiceImpl implements RentAreaService {

	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	public RentAreaDTO toDTO(RentAreaEntity entity) {
		RentAreaDTO dto = new RentAreaDTO();
		dto.setBuildingId(entity.getBuildingId());
		dto.setId(entity.getId());
		dto.setValue(entity.getValue());
		return dto;
	}
	
	@Override
	public List<RentAreaDTO> getRentAreaByBuildingId(Long buildingId) {
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		
		List<RentAreaEntity> entities = rentAreaRepository.findByBuildingId(buildingId);
		List<RentAreaDTO> dtos = new ArrayList<RentAreaDTO>();
		
		for (RentAreaEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		
		return dtos;
	}

	@Override
	public RentAreaDTO createRentArea(Long buildingId, RentAreaCreateRequest request) {
		if (buildingId == null || buildingId <= 0) {
		    throw new InvalidBuildingException("ID toa nha khong hop le");
		}
		
		if (buildingId == null || buildingId <= 0) {
			throw new InvalidRentAreaException("building id khong hop le");
		}
		
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		
		if (request.getValue() == null || request.getValue() <= 0) {
			throw new InvalidRentAreaException("value khong hop le");
		}
		
		RentAreaEntity entity = new RentAreaEntity();
		entity.setBuildingId(buildingId);
		entity.setValue(request.getValue());
		
		Long id = rentAreaRepository.insert(entity);
		entity.setId(id);
		
		return toDTO(entity);
	}

	@Override
	public void deleteRentArea(Long buildingId, Long rentId) {
		
		if (rentId == null || rentId <= 0) {
			throw new InvalidRentAreaException("rent id khong hop le");
		}
		
		if (buildingId == null || buildingId <= 0) {
			throw new InvalidRentAreaException("building id khong hop le");
		}
		
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		
		
		
		int affectedRows = rentAreaRepository.deleteByBuildingIdAndId(buildingId, rentId);
		if (affectedRows == 0) {
			throw new RentAreaNotFoundException("khong tim thay rent area voi id: " + rentId + ", " + buildingId);
		}
		
	}	
}
