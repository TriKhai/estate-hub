package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.StaffEntity;

public interface StaffRepository {
	List<StaffEntity> findAllActiveStaff();
	boolean existsActiveStaff(Connection connection, Long staffId) throws SQLException;
	        
}
