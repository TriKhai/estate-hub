package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.CustomerAssignmentRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class CustomerAssignmentRepositoryImpl implements CustomerAssignmentRepository{

	public StaffEntity mapRow(ResultSet rs) throws SQLException {
		StaffEntity entity = new StaffEntity();
		entity.setFullName(rs.getString("fullname"));
		entity.setId(rs.getLong("id"));
		return entity;
	}
	
	@Override
	public List<StaffEntity> findByCustomerId(Long customerId) {
		String sql = "SELECT DISTINCT u.id, u.fullname FROM assignmentcustomer ac JOIN `user` u ON u.id = ac.staffid WHERE ac.customerid = ? ORDER BY u.id ASC";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			){
			statement.setLong(1, customerId);
			List<StaffEntity> entities = new ArrayList<StaffEntity>();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					entities.add(mapRow(resultSet));
				}
				return entities;
			}
			
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public int deleteByCustomerId(Connection connection, Long customerId) throws SQLException {
		String sql = "DELETE FROM assignmentcustomer WHERE customerid = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setLong(1, customerId);
	        return statement.executeUpdate();
	    }
	}

	@Override
	public int insert(Connection connection, Long customerId, Long staffId) throws SQLException {
		String sql = "INSERT INTO assignmentcustomer (customerid, staffid) VALUES (?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setLong(1, customerId);
	        statement.setLong(2, staffId);
	        return statement.executeUpdate();
	    }
	}

}
