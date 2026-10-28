package com.javaweb.repository.impl;

import com.javaweb.repository.BuildingAssignmentRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public class BuildingAssignmentRepositoryImpl implements BuildingAssignmentRepository {

	@Override
	public List<StaffEntity> findByBuildingId(Long buildingId) {
		String sql = "SELECT DISTINCT u.id, u.fullname FROM assignmentbuilding ab JOIN user u ON u.id = ab.staffid WHERE ab.buildingid = ? ORDER BY u.id ASC";
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				) {
			
			List<StaffEntity> entities = new ArrayList<StaffEntity>();
			statement.setLong(1, buildingId);
			try (ResultSet rs = statement.executeQuery()) {
			    while (rs.next()) {
			        StaffEntity entity = new StaffEntity();
			        entity.setFullName(rs.getString("fullname"));
			        entity.setId(rs.getLong("id"));
			        entities.add(entity);
			    }

			}
			return entities;
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}	
	}

	@Override
	public int deleteByBuildingId(Connection connection, Long buildingId) throws SQLException {
		String sql = "DELETE FROM assignmentbuilding WHERE buildingid = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setLong(1, buildingId);
	        return statement.executeUpdate();
	    }
	}

	@Override
	public int insert(Connection connection, Long buildingId, Long staffId) throws SQLException {
		String sql = "INSERT INTO assignmentbuilding (buildingid, staffid) VALUES (?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, buildingId);
			statement.setLong(2, staffId);
			return statement.executeUpdate();
		}
	}
}
