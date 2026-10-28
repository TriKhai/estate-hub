package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.TransactionTypeRepository;
import com.javaweb.repository.entity.TransactionTypeEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class TransactionTypeRepositoryImpl implements TransactionTypeRepository {

	
	public TransactionTypeEntity mapRow(ResultSet rs) throws SQLException {
		TransactionTypeEntity entity = new TransactionTypeEntity();
	    entity.setId(rs.getLong("id"));
	    entity.setName(rs.getString("name"));
	    entity.setCode(rs.getString("code"));
	    
	    return entity;
	}
	
	@Override
	public List<TransactionTypeEntity> findAll() {
		String sql = "SELECT id, name, code FROM transactiontype ORDER BY id ASC";
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			){
			
			List<TransactionTypeEntity> entities = new ArrayList<TransactionTypeEntity>();
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					TransactionTypeEntity entity = mapRow(rs);
					entities.add(entity);
				}
				
				return entities;
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public boolean existsById(Long id) {
		String sql = "SELECT 1 FROM transactiontype WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			){
			statement.setLong(1, id);
			try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
			
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}
	
}
