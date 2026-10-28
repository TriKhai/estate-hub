package com.javaweb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.javaweb.repository.entity.CustomerEntity;

public interface CustomerRepository {
	List<CustomerEntity> findAll(String name, String phone, Integer size, Long offset);
	CustomerEntity findById(Long id);
	Long insert(CustomerEntity entity);
	int update(CustomerEntity entity);
	int delete(Long id);
	boolean existsById(Connection connection, Long customerId) throws SQLException;
}
