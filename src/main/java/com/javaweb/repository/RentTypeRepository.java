package com.javaweb.repository;

import java.sql.Connection;
import java.util.List;

import com.javaweb.repository.entity.RentTypeEntity;

public interface RentTypeRepository {
	List<RentTypeEntity> findAll();
	List<RentTypeEntity> findByBuildingId(Long buildingId);
	boolean existsById(Long rentTypeId);
	boolean existsAssignment(Long buildingId, Long rentTypeId);
	
	int assignToBuilding(Long buildingId, Long rentTypeId);
	int assignToBuilding(Connection connection, Long buildingId, Long rentTypeId);
	
	int removeFromBuilding(Long buildingId, Long rentTypeId);
}
