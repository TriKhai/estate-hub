package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.StaffEntity;

public interface BuildingAssignmentRepository {
	List<StaffEntity> findByBuildingId(Long buildingId);
	
	int deleteByBuildingId(Connection connection, Long buildingId)
	        throws SQLException;

	int insert(Connection connection, Long buildingId, Long staffId)
	        throws SQLException;
}
