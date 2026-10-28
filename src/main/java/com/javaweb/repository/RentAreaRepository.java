package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.RentAreaEntity;

public interface RentAreaRepository {
	List<RentAreaEntity> findByBuildingId(Long buildingId);
	Long insert(RentAreaEntity entity);
	Long insert(Connection connection, RentAreaEntity entity) throws SQLException;
	
	int deleteByBuildingIdAndId(Long buildingId, Long rentAreaId);
}
