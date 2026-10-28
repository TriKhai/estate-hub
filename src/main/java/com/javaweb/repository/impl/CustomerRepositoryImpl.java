package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.customException.CustomerInUseException;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.entity.CustomerEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	public CustomerEntity mapRow(ResultSet rs) throws SQLException {
		CustomerEntity entity = new CustomerEntity();
		
		entity.setId(rs.getLong("id"));
		entity.setFullname(rs.getString("fullname"));
		entity.setEmail(rs.getString("email"));
		entity.setPhone(rs.getString("phone"));
		
		return entity;
	}
	
	@Override
	public List<CustomerEntity> findAll(String name, String phone, Integer size, Long offset) {
		StringBuilder sql = new StringBuilder("SELECT id, fullname, phone, email FROM customer WHERE 1=1 "); 
		
		boolean hasName = name != null && !name.trim().isEmpty();
		boolean hasPhone = phone != null && !phone.trim().isEmpty();

		if (hasName) {
		    sql.append(" AND fullname LIKE ?");
		}

		if (hasPhone) {
		    sql.append(" AND phone LIKE ?");
		}

		sql.append(" ORDER BY id ASC LIMIT ? OFFSET ?");
		
		List<CustomerEntity> entities = new ArrayList<CustomerEntity>();
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql.toString());
			){
			int index = 1;

			if (hasName) {
			    statement.setString(index++, "%" + name.trim() + "%");
			}

			if (hasPhone) {
			    statement.setString(index++, "%" + phone.trim() + "%");
			}

			statement.setInt(index++, size);
			statement.setLong(index++, offset);
			
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
	public CustomerEntity findById(Long id) {
		
		String sql = "SELECT id, fullname, phone, email FROM customer WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			)
		{
			statement.setLong(1, id);
			
			try(ResultSet rs = statement.executeQuery()) {
				if (!rs.next()) {
					return null;
				}
				
				return mapRow(rs);
			} 
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public Long insert(CustomerEntity entity) {
		String sql = "INSERT INTO customer (fullname, phone, email) VALUES (?, ?, ?)";
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			){
			statement.setString(1, entity.getFullname());
			statement.setString(2, entity.getPhone());
			statement.setString(3, entity.getEmail());
			
			int rows  = statement.executeUpdate();
			
			if (rows  != 1) {
			    throw new IllegalStateException("Khong them duoc customer");
			}
			
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next() ) {
					return keys.getLong(1);
				}
					
				throw new IllegalStateException("Khong lay duoc ID toa nha vua them");
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi ket noi database", exception);
		}
	}

	@Override
	public int update(CustomerEntity entity) {
		String sql = "UPDATE customer SET fullname = ?, phone = ?, email = ? WHERE id = ?";
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			){
			statement.setString(1, entity.getFullname());
			statement.setString(2, entity.getPhone());
			statement.setString(3, entity.getEmail());
			statement.setLong(4, entity.getId());
			
			return statement.executeUpdate();
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM customer WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			)
		{
			statement.setLong(1, id);
			
			return statement.executeUpdate();
			
		} catch (SQLException exception) {
			if (exception.getErrorCode() == 1451) {
				throw new CustomerInUseException(
					    "Khách hàng đang có dữ liệu liên quan, không thể xóa",
					    exception
				);
			}

			throw new IllegalStateException("Loi database khi xoa toa nha", exception);
		}
	}

	@Override
	public boolean existsById(Connection connection, Long customerId) throws SQLException {
		String sql = "SELECT 1 FROM customer WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setLong(1, customerId);
			ResultSet rs = statement.executeQuery();
			return rs.next();
		}
	}

}
