package com.javaweb.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.BuildingCreateRequest;
import com.javaweb.Beans.BuildingDTO;
import com.javaweb.Beans.BuildingFullCreateRequest;
import com.javaweb.Beans.BuildingUpdateRequest;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.repository.entity.RentTypeEntity;
import com.javaweb.service.BuildingService;
import com.javaweb.utils.ConnectionUtils;

@Service
public class BuildingServiceImpl implements BuildingService {
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private RentAreaRepository rentAreaRepository;
	
	@Autowired
	private RentTypeRepository rentTypeRepository;

	private BuildingDTO toDTO(BuildingEntity entity) {
		BuildingDTO dto = new BuildingDTO();
		dto.setId(entity.getId());
		dto.setRentPrice(entity.getRentPrice());
		dto.setName(entity.getName());
		dto.setNumberOfBasement(entity.getNumberOfBasement());
		List<String> addressParts = new ArrayList<>();
		if (entity.getStreet() != null && !entity.getStreet().trim().isEmpty()) {
			addressParts.add(entity.getStreet());
		}
		if (entity.getWard() != null && !entity.getWard().trim().isEmpty()) {
			addressParts.add(entity.getWard());
		}
		dto.setAddress(String.join(", ", addressParts));
		return dto;
	}
	
	private void validateBuilding(BuildingCreateRequest request) {
		if (request == null) {
			throw new InvalidBuildingException("Thieu thong tin toa nha");
		}
		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new InvalidBuildingException("Ten toa nha khong duoc de trong");
		}
		if (request.getName().trim().length() > 255) {
			throw new InvalidBuildingException("Ten toa nha toi da 255 ky tu");
		}
		if (request.getDistrictId() == null || request.getDistrictId() <= 0) {
			throw new InvalidBuildingException("ID quan phai lon hon 0");
		}
		if (request.getRentPrice() == null || request.getRentPrice() < 0) {
			throw new InvalidBuildingException("Gia thue bat buoc va khong duoc am");
		}
		if (request.getNumberOfBasement() != null && request.getNumberOfBasement() < 0) {
			throw new InvalidBuildingException("So tang ham khong duoc am");
		}
		if (request.getStreet() != null && request.getStreet().length() > 255) {
			throw new InvalidBuildingException("Ten duong toi da 255 ky tu");
		}
		if (request.getWard() != null && request.getWard().length() > 255) {
			throw new InvalidBuildingException("Ten phuong toi da 255 ky tu");
		}

		// 2. ID hop le ve kieu/gia tri chua chac da ton tai trong database.
		if (!buildingRepository.existsDistrictById(request.getDistrictId())) {
			throw new InvalidBuildingException("Quan khong ton tai");
		}
	}

	// Chuyển request thành entity.
	private BuildingEntity toEntity(BuildingCreateRequest request) {
		BuildingEntity entity = new BuildingEntity();
		entity.setName(request.getName().trim());
		entity.setStreet(request.getStreet());
		entity.setWard(request.getWard());
		entity.setDistrictId(request.getDistrictId());
		entity.setNumberOfBasement(request.getNumberOfBasement());
		entity.setRentPrice(request.getRentPrice());
		return entity;
	}
	

	@Override
	public BuildingDTO getBuildingById(Long id) {
		BuildingEntity entity = buildingRepository.findById(id);
		if (entity == null) {
			return null;
		}
		
		return toDTO(entity);
	}
	
	@Override
	public List<BuildingDTO> getAllBuilding(String name, Long districtId, Integer minPrice, Integer maxPrice, Integer page, Integer size, String sortBy, String sortDir) {
		
		long offset = (page - 1L) * size; // pagination
		
		List<BuildingEntity> entities = buildingRepository.findAll(name, districtId, minPrice, maxPrice, size, offset, sortBy, sortDir);
		List<BuildingDTO> dtos = new ArrayList<BuildingDTO>();
		
		for (BuildingEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		
		return dtos;
	}

	@Override
	public List<BuildingDTO> getBuildingsByRentPrice(Integer minPrice, Integer maxPrice) {
		List<BuildingEntity> entities = buildingRepository.findByRentPrice(minPrice, maxPrice);
		List<BuildingDTO> dtos = new ArrayList<BuildingDTO>();
		for (BuildingEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		return dtos;
	}

	@Override
	public Long createBuilding(BuildingCreateRequest request) {
		// 1. Kiem tra du lieu truoc khi truy van database.
		if (request == null) {
			throw new InvalidBuildingException("Thieu thong tin toa nha");
		}
		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new InvalidBuildingException("Ten toa nha khong duoc de trong");
		}
		if (request.getName().trim().length() > 255) {
			throw new InvalidBuildingException("Ten toa nha toi da 255 ky tu");
		}
		if (request.getDistrictId() == null || request.getDistrictId() <= 0) {
			throw new InvalidBuildingException("ID quan phai lon hon 0");
		}
		if (request.getRentPrice() == null || request.getRentPrice() < 0) {
			throw new InvalidBuildingException("Gia thue bat buoc va khong duoc am");
		}
		if (request.getNumberOfBasement() != null && request.getNumberOfBasement() < 0) {
			throw new InvalidBuildingException("So tang ham khong duoc am");
		}
		if (request.getStreet() != null && request.getStreet().length() > 255) {
			throw new InvalidBuildingException("Ten duong toi da 255 ky tu");
		}
		if (request.getWard() != null && request.getWard().length() > 255) {
			throw new InvalidBuildingException("Ten phuong toi da 255 ky tu");
		}

		// 2. ID hop le ve kieu/gia tri chua chac da ton tai trong database.
		if (!buildingRepository.existsDistrictById(request.getDistrictId())) {
			throw new InvalidBuildingException("Quan khong ton tai");
		}

		// 3. Chuyen request sang entity de Repository luu.
		BuildingEntity entity = new BuildingEntity();
		entity.setName(request.getName().trim());
		entity.setStreet(request.getStreet());
		entity.setWard(request.getWard());
		entity.setDistrictId(request.getDistrictId());
		entity.setNumberOfBasement(request.getNumberOfBasement());
		entity.setRentPrice(request.getRentPrice());

		// 4. Repository tra ID moi; Service tra lai ID do cho API.
		return buildingRepository.insert(entity);
	}

	@Override
	public void updateBuilding(Long id, BuildingUpdateRequest request) {
		if (request == null) {
			throw new InvalidBuildingException("Thieu thong tin toa nha");
		}
		
		if (id == null || id <= 0) {
			throw new InvalidBuildingException("ID khong hop le");
		}
		
		if (request.getName() == null || request.getName().trim().isEmpty()) {
			throw new InvalidBuildingException("Ten toa nha khong duoc de trong");
		}
		if (request.getName().trim().length() > 255) {
			throw new InvalidBuildingException("Ten toa nha toi da 255 ky tu");
		}
		if (request.getDistrictId() == null || request.getDistrictId() <= 0) {
			throw new InvalidBuildingException("ID quan phai lon hon 0");
		}
		if (request.getRentPrice() == null || request.getRentPrice() < 0) {
			throw new InvalidBuildingException("Gia thue bat buoc va khong duoc am");
		}
		if (request.getNumberOfBasement() != null && request.getNumberOfBasement() < 0) {
			throw new InvalidBuildingException("So tang ham khong duoc am");
		}
		if (request.getStreet() != null && request.getStreet().length() > 255) {
			throw new InvalidBuildingException("Ten duong toi da 255 ky tu");
		}
		if (request.getWard() != null && request.getWard().length() > 255) {
			throw new InvalidBuildingException("Ten phuong toi da 255 ky tu");
		}
		
		if (!buildingRepository.existsDistrictById(request.getDistrictId())) {
		    throw new InvalidBuildingException("Quan khong ton tai");
		}
		
		if (buildingRepository.findById(id) == null) {
			throw new BuildingNotFoundException("Toa nha id" + id + " khong ton tai");
		}
		
		BuildingEntity entity = new BuildingEntity();
		entity.setName(request.getName().trim());
		entity.setStreet(request.getStreet());
		entity.setWard(request.getWard());
		entity.setDistrictId(request.getDistrictId());
		entity.setNumberOfBasement(request.getNumberOfBasement());
		entity.setRentPrice(request.getRentPrice());
		entity.setId(id);
		
		int result = buildingRepository.update(entity);
		

		if (result == 0 && buildingRepository.findById(id) == null) {
		    throw new BuildingNotFoundException(
		        "Khong tim thay toa nha co ID " + id
		    );
		}
		
	}

	@Override
	public void deleteBuilding(Long id) {
		if (id == null || id <= 0) {
			throw new InvalidBuildingException("ID khong hop le");
		}
		
		int result = buildingRepository.deleteById(id);
		if (result == 0) {
			throw new BuildingNotFoundException(
			        "Khong tim thay toa nha co ID " + id
			    );
		}
		
	}

	@Override
	public Long createBuildingFull(BuildingFullCreateRequest request) {
		
		if (request == null) {
			throw new InvalidBuildingException("Thieu thong tin toa nha");
		}
		
		BuildingCreateRequest buildingCreateRequest = new BuildingCreateRequest();
		buildingCreateRequest.setName(request.getName());
		buildingCreateRequest.setNumberOfBasement(request.getNumberOfBasement());
		buildingCreateRequest.setDistrictId(request.getDistrictId());
		buildingCreateRequest.setRentPrice(request.getRentPrice());
		buildingCreateRequest.setStreet(request.getStreet());
		buildingCreateRequest.setWard(request.getWard());

		validateBuilding(buildingCreateRequest);
		
		if (request.getRentAreas() == null || request.getRentTypeIds() == null) {
		    throw new InvalidBuildingException("Thieu danh sach dien tich hoac loai thue");
		}
		
		for (Integer area : request.getRentAreas()) {
		    if (area == null || area <= 0) {
		        throw new InvalidBuildingException("Dien tich phai lon hon 0");
		    }
		}
		
		Set<Long> seenIds = new HashSet<>();

		for (Long rentTypeId : request.getRentTypeIds()) {
		    if (rentTypeId == null || rentTypeId <= 0) {
		        throw new InvalidBuildingException("ID loai thue khong hop le");
		    }

		    if (!seenIds.add(rentTypeId)) {
		        throw new InvalidBuildingException("ID loai thue bi trung");
		    }

		    if (!rentTypeRepository.existsById(rentTypeId)) {
		        throw new InvalidBuildingException("Loai thue khong ton tai");
		    }
		}
		
		BuildingEntity buildingEntity = toEntity(buildingCreateRequest);
		
		try (
				Connection connection = ConnectionUtils.getConnection();
			){
			
			connection.setAutoCommit(false);
			
			try {
				
				Long buildingId = buildingRepository.insert(connection, buildingEntity);
				
				for (Integer area: request.getRentAreas()) {
					
					RentAreaEntity rentAreaEntity = new RentAreaEntity();
					rentAreaEntity.setBuildingId(buildingId);
					rentAreaEntity.setValue(area);
				
					rentAreaRepository.insert(connection, rentAreaEntity);
				}
				
				
				
				for (Long rentTypeId: request.getRentTypeIds()) {
					
					if ("TEST_ROLLBACK_11".equals(request.getName())) {
					    throw new IllegalStateException("Chu dong gay loi de test rollback");
					}
					
					int rows = rentTypeRepository.assignToBuilding(
			                connection, buildingId, rentTypeId
			        );
					
					if (rows != 1) {
		                throw new IllegalStateException("Khong gan duoc loai thue");
		            }
				}
				
				connection.commit();
				return buildingId;
				
			} catch (SQLException | RuntimeException exception) {
				try {
		            connection.rollback();
		        } catch (SQLException rollbackException) {
		            exception.addSuppressed(rollbackException);
		        }

		        throw new IllegalStateException(
		            "Khong the tao day du toa nha", exception
		        );
			}
			
		} catch (SQLException exception) {
		    throw new IllegalStateException("Loi ket noi database", exception);
		}
	}
	
}
