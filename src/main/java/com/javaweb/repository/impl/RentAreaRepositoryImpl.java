package com.javaweb.repository.impl;

import java.nio.channels.IllegalSelectorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

//	public RentAreaEntity mapTo(ResultSet resultset) throws SQLException {
//		RentAreaEntity entity = new RentAreaEntity();
////		entity.setValue(resultSet.get);
//	}
	
	@Override
	public List<RentAreaEntity> findByBuildingId(Long buildingId) {
		String sql = "SELECT id, buildingid, value "
				+ "FROM rentarea "
				+ "WHERE buildingid = ? "
				+ "ORDER BY value ASC, id ASC ";
		List<RentAreaEntity> entities = new ArrayList<RentAreaEntity>();
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
		) {
			
			if (buildingId == null) {
				return null;
			}
			statement.setLong(1, buildingId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					RentAreaEntity entity = new RentAreaEntity();
					entity.setBuildingId(resultSet.getLong("buildingId"));
					entity.setId(resultSet.getLong("id"));
					entity.setValue(resultSet.getInt("value"));
					entities.add(entity);
				}
				return entities;
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi ket noi database", exception);
		}
	}

	@Override
	public Long insert(Connection connection, RentAreaEntity entity) throws SQLException {
		String sql = "INSERT INTO rentarea (buildingid, value) VALUES (?, ?)";
		try (
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			) {
			
			statement.setLong(1, entity.getBuildingId());
			statement.setInt(2, entity.getValue());
			
			int affectedRows = statement.executeUpdate();
			if (affectedRows != 1) {
			    throw new IllegalStateException("Khong them duoc gia thue");
			}
			
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next() ) {
					return keys.getLong(1);
				}
				
				throw new IllegalStateException("Khong lay duoc ID gia thue vua them");
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi ket noi database khi them rent area", exception);
		}
	}
	
	@Override
	public Long insert(RentAreaEntity entity) {
		try (
			Connection connection = ConnectionUtils.getConnection();	
		) {
			return insert(connection, entity);
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi ket noi database khi them rent area", exception);
		}
	}

	@Override
	public int deleteByBuildingIdAndId(Long buildingId, Long rentAreaId) {
		String sql = "DELETE FROM rentarea WHERE buildingId = ? AND id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			
			statement.setLong(1, buildingId);
			statement.setLong(2, rentAreaId);
			
			return statement.executeUpdate();	
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database khi xoa rent area", exception);
		}
	}

	
	
}
