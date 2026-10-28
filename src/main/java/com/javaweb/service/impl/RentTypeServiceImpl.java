package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.RentAreaCreateRequest;
import com.javaweb.Beans.RentAreaDTO;
import com.javaweb.Beans.RentTypeDTO;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.customException.RentTypeAlreadyAssignedException;
import com.javaweb.customException.RentTypeNotFoundException;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.repository.entity.RentTypeEntity;
import com.javaweb.service.RentAreaService;
import com.javaweb.service.RentTypeService;

@Service
public class RentTypeServiceImpl implements RentTypeService{
	
	@Autowired
	private RentTypeRepository rentTypeRepository;
	
	@Autowired
	private BuildingRepository buildingRepository;

	public RentTypeDTO toDTO(RentTypeEntity entity) {
		RentTypeDTO dto = new RentTypeDTO();
		dto.setId(entity.getId());
		dto.setCode(entity.getCode());
		dto.setName(entity.getName());
		
		return dto;
	}
	
	@Override
	public List<RentTypeDTO> getAllRentTypes() {
		List<RentTypeEntity> entities = rentTypeRepository.findAll();
		List<RentTypeDTO> dtos = new ArrayList<RentTypeDTO>();
		
		for (RentTypeEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		
		return dtos;
	}

	@Override
	public List<RentTypeDTO> getRentTypesByBuildingId(Long buildingId) {
		if (buildingId == null || buildingId <= 0) {
		    throw new InvalidBuildingException("ID toa nha khong hop le");
		}
		
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		List<RentTypeEntity> entities = rentTypeRepository.findByBuildingId(buildingId);
		List<RentTypeDTO> dtos = new ArrayList<RentTypeDTO>();
		
		for (RentTypeEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		
		return dtos;
	}

	@Override
	public void assignRentType(Long buildingId, Long rentTypeId) {
		if (buildingId == null || buildingId <= 0) {
		    throw new InvalidBuildingException("ID toa nha khong hop le");
		}
		
		if (rentTypeId == null || rentTypeId <= 0) {
		    throw new InvalidBuildingException("ID loai toa nha khong hop le");
		}
		
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		
		if (!rentTypeRepository.existsById(rentTypeId)) {
		    throw new RentTypeNotFoundException(
		        "Khong tim thay loai thue co ID " + rentTypeId
		    );
		}
		
		
		if (rentTypeRepository.existsAssignment(buildingId, rentTypeId)) {
		    throw new RentTypeAlreadyAssignedException(
		        "Loai thue da duoc gan cho toa nha"
		    );
		}
		
		int rows = rentTypeRepository.assignToBuilding(buildingId, rentTypeId);
		if (rows != 1) {
		    throw new IllegalStateException("Khong them duoc lien ket loai thue");
		}
	}

	@Override
	public void removeRentType(Long buildingId, Long rentTypeId) {
		if (buildingId == null || buildingId <= 0) {
		    throw new InvalidBuildingException("ID toa nha khong hop le");
		}
		
		if (rentTypeId == null || rentTypeId <= 0) {
		    throw new InvalidBuildingException("ID loai toa nha khong hop le");
		}
		
		if (buildingRepository.findById(buildingId) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + buildingId
		    );
		}
		
		if (!rentTypeRepository.existsById(rentTypeId)) {
		    throw new RentTypeNotFoundException(
		        "Khong tim thay loai thue co ID " + rentTypeId
		    );
		}
		
		int rows = rentTypeRepository.removeFromBuilding(buildingId, rentTypeId);

		if (rows == 0) {
		    throw new RentTypeNotFoundException(
		        "Loai thue chua duoc gan cho toa nha nay"
		    );
		}
		
	}

	
}
