package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.customException.RentTypeAlreadyAssignedException;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.repository.entity.RentTypeEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class RentTypeRepositoryImpl implements RentTypeRepository{
	
	public RentTypeEntity mapRow(ResultSet resultSet) throws SQLException {
		RentTypeEntity entity = new RentTypeEntity();
		entity.setId(resultSet.getLong("id"));
		entity.setCode(resultSet.getString("code"));
		entity.setName(resultSet.getString("name"));
		
		return entity;
	}

	@Override
	public List<RentTypeEntity> findAll() {
		String sql = "SELECT id, code, name FROM renttype ORDER BY id ASC";
		List<RentTypeEntity> renttypes = new ArrayList<RentTypeEntity>();
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			)
		{
			
			try (ResultSet resultSet = statement.executeQuery()) {
			    while (resultSet.next()) {
			        renttypes.add(mapRow(resultSet));
			    }
			}
			return renttypes;
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database renttype", exception);
		}
	}

	@Override
	public List<RentTypeEntity> findByBuildingId(Long buildingId) {
		String sql = "SELECT rt.id, rt.code, rt.name FROM renttype rt JOIN buildingrenttype brt ON brt.renttypeid = rt.id WHERE brt.buildingid = ? ORDER BY rt.id ASC";
		
		List<RentTypeEntity> renttypes = new ArrayList<RentTypeEntity>();
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			)
		{
			statement.setLong(1, buildingId);
			try (ResultSet resultSet = statement.executeQuery()) {
			    while (resultSet.next()) {
			        renttypes.add(mapRow(resultSet));
			    }
			}
			return renttypes;
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database renttype", exception);
		}
	}

	@Override
	public boolean existsById(Long rentTypeId) {
		String sql = "SELECT 1 FROM renttype WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				){
			statement.setLong(1, rentTypeId);
			try (ResultSet rs = statement.executeQuery()) {
				return rs.next();
			}	
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public boolean existsAssignment(Long buildingId, Long rentTypeId) {
		String sql = "SELECT 1 FROM buildingrenttype WHERE buildingid = ? AND renttypeid = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			){
			statement.setLong(1, buildingId);
			statement.setLong(2, rentTypeId);
			try(ResultSet rs = statement.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}
	
	@Override
	public int assignToBuilding(Connection connection, Long buildingId, Long rentTypeId) {
		String sql = "INSERT INTO buildingrenttype (buildingid, renttypeid) "
	               + "VALUES (?, ?)";
		try (
			PreparedStatement statement = connection.prepareStatement(sql);
		)
		{
			statement.setLong(1, buildingId);
			statement.setLong(2, rentTypeId);
			return statement.executeUpdate();
			
		} catch (SQLException exception) {
			if (exception.getErrorCode() == 1062) {
	            throw new RentTypeAlreadyAssignedException(
	                "Loai thue da duoc gan cho toa nha",
	                exception
	            );
	        }
			throw new IllegalStateException(
		            "Loi database khi gan loai thue cho toa nha",
		            exception
		        );
		}
	}

	@Override
	public int assignToBuilding(Long buildingId, Long rentTypeId) {
		try (
				Connection connection = ConnectionUtils.getConnection();
				)
		{
			return assignToBuilding(connection, buildingId, rentTypeId);	
		} catch (SQLException exception) {
			if (exception.getErrorCode() == 1062) {
	            throw new RentTypeAlreadyAssignedException(
	                "Loai thue da duoc gan cho toa nha",
	                exception
	            );
	        }
			throw new IllegalStateException(
		            "Loi database khi gan loai thue cho toa nha",
		            exception
		    );
		}
	}

	@Override
	public int removeFromBuilding(Long buildingId, Long rentTypeId) {
		String sql = "DELETE FROM buildingrenttype WHERE buildingid = ? AND renttypeid = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			)
		{
			statement.setLong(1, buildingId);
			statement.setLong(2, rentTypeId);
			
			return statement.executeUpdate();
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	
}
