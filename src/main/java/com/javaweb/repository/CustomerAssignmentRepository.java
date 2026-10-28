package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.StaffEntity;

public interface CustomerAssignmentRepository {
	List<StaffEntity> findByCustomerId(Long customerId);
	int deleteByCustomerId(Connection connection, Long customerId) throws SQLException;
	int insert(Connection connection, Long customerId, Long staffId) throws SQLException;
}
