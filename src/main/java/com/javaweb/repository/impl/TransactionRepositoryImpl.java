package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.TransactionRepository;
import com.javaweb.repository.entity.TransactionEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

	
	public TransactionEntity mapRow(ResultSet rs) throws SQLException {
	    TransactionEntity entity = new TransactionEntity();

	    entity.setId(rs.getLong("id"));
	    entity.setCustomerId(rs.getLong("customerid"));

	    long typeId = rs.getLong("type");
	    entity.setTypeId(rs.wasNull() ? null : typeId);

	    entity.setNote(rs.getString("note"));
        entity.setTypeName(rs.getString("typename"));

	    Timestamp createdDate = rs.getTimestamp("createddate");
	    entity.setCreatedDate(
	        createdDate == null ? null : createdDate.toLocalDateTime()
	    );

	    return entity;
	}
	
	@Override
	public List<TransactionEntity> findByCustomerId(Long customerId) {
        String sql = "SELECT t.id, t.customerid, t.type, t.note, t.createddate, tt.name AS typename "
                + "FROM `transaction` t LEFT JOIN transactiontype tt ON tt.id = t.type "
                + "WHERE t.customerid = ? ORDER BY t.createddate DESC, t.id DESC";
		List<TransactionEntity> entities = new ArrayList<TransactionEntity>();
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
				){
			statement.setLong(1, customerId);

			try (ResultSet rs = statement.executeQuery()) {
			    while (rs.next()) {
			        entities.add(mapRow(rs));
			    }
			}
			return entities;
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

	@Override
	public Long insert(TransactionEntity entity) {
		String sql = "INSERT INTO `transaction` (customerid, type, note, createddate) "
				+ "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(
					    sql, Statement.RETURN_GENERATED_KEYS
					);
				){
			statement.setLong(1, entity.getCustomerId());
			statement.setLong(2, entity.getTypeId());
			statement.setString(3, entity.getNote());

			int rows = statement.executeUpdate();

			if (rows != 1) {
			    throw new IllegalStateException("Không thể thêm lịch sử chăm sóc");
			}

			try (ResultSet keys = statement.getGeneratedKeys()) {
			    if (keys.next()) {
			        return keys.getLong(1);
			    }

			    throw new IllegalStateException("Không lấy được ID lịch sử vừa thêm");
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}

}
