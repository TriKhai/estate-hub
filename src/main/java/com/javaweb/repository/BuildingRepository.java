package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingRepository {
	BuildingEntity findById(Long id); // Hàm nhận id tòa nhà => Trả về dữ liệu tòa nhà (Entity)
	List<BuildingEntity> findByRentPrice(Integer minPrice, Integer maxPrice);	
	List<BuildingEntity> findAll(String name, Long districtId, Integer minPrice, Integer maxPrice, Integer size, long offset, String sortBy, String sortDir);
	
	Long insert(BuildingEntity building);
	Long insert(Connection connection, BuildingEntity building) throws SQLException;
	
	boolean existsDistrictById(Long districtId);
	int update(BuildingEntity building);
	int deleteById(Long id);
	
	boolean existsById(Connection connection, Long buildingId) throws SQLException;
	        
	
}
