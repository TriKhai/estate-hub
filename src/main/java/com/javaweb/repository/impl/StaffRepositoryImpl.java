package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.StaffRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class StaffRepositoryImpl implements StaffRepository {

	public StaffEntity mapRow(ResultSet rs) throws SQLException {
		StaffEntity entity = new StaffEntity();
		entity.setFullName(rs.getString("fullname"));
		entity.setId(rs.getLong("id"));
		return entity;
	}
	
	@Override
	public List<StaffEntity> findAllActiveStaff() {
		String sql = "SELECT DISTINCT u.id, u.fullname FROM user u JOIN user_role ur ON ur.userid = u.id JOIN role r ON r.id = ur.roleid WHERE u.status = 1 AND r.code = 'staff' ORDER BY u.id ASC";
		
		List<StaffEntity> entities = new ArrayList<StaffEntity>();
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				){
			
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
	public boolean existsActiveStaff(Connection connection, Long staffId) throws SQLException {
		String sql = "SELECT 1 FROM `user` u JOIN user_role ur ON ur.userid = u.id JOIN role r ON r.id = ur.roleid WHERE u.id = ? AND u.status = 1  AND r.code = 'staff' LIMIT 1";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, staffId);
			ResultSet rs = statement.executeQuery();
			return rs.next();
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

}
