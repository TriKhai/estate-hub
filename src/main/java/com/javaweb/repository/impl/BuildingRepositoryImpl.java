package com.javaweb.repository.impl;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;


import com.javaweb.customException.BuildingInUseException;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionUtils;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	
	// Kết nối database
//	static final String DB_URL = "jdbc:mysql://localhost:xxx/xxx";
//	static final String USER = "";
//	static final String PASS = "";
	
	// ResultSet => Entity
	private BuildingEntity mapRow(ResultSet resultSet) throws SQLException {
		BuildingEntity building = new BuildingEntity();
		building.setId(resultSet.getLong("id"));
		building.setName(resultSet.getString("name"));
		building.setStreet(resultSet.getString("street"));
		building.setWard(resultSet.getString("ward"));
		int basement = resultSet.getInt("numberofbasement"); 	
		building.setNumberOfBasement(resultSet.wasNull() ? null : basement);
		building.setRentPrice(resultSet.getInt("rentPrice"));
		return building;
	}

	@Override
	public BuildingEntity findById(Long id) {
		// Dau ? la vi tri truyen gia tri, khong noi id truc tiep vao SQL.
		String sql = "SELECT id, name, street, ward, numberofbasement, rentPrice FROM building WHERE id = ?";

		// try-with-resources tu dong dong statement va connection.
		try (Connection connection = ConnectionUtils.getConnection();
			 PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, id); // Tham so JDBC bat dau tu 1.

			try (ResultSet resultSet = statement.executeQuery()) {
				if (!resultSet.next()) {
					return null; // Truy van thanh cong, nhung khong co toa nha nay.
				}

				return mapRow(resultSet);
			}
		} catch (SQLException exception) {
			// Loi database khac voi truong hop khong tim thay du lieu.
			throw new IllegalStateException("Khong the truy van toa nha", exception);
		}
	}

	
	@Override
	public List<BuildingEntity> findAll(String name, Long districtId, Integer minPrice, Integer maxPrice, Integer size, long offset, String sortBy, String sortDir) {
		String sortColumn;
		switch (sortBy) {
		case "id":
			sortColumn = "id";
			break;
		case "name":
			sortColumn = "name";
			break;
		case "rentPrice":
			sortColumn = "rentPrice";
			break;
		default:
			throw new IllegalArgumentException("Cot sap xep khong hop le");
		}
		
		String direction;
		if ("asc".equals(sortDir)) {
			direction = "ASC";
		} else if ("desc".equals(sortDir)) {
			direction = "DESC";
		} else {
			throw new IllegalArgumentException("Chieu sap xep khong hop le");
		}
		
		
		StringBuilder sql = new StringBuilder("SELECT id, name, street, ward, numberofbasement, rentprice FROM building b WHERE 1=1 "); 
		boolean hasName = name != null && !name.trim().isEmpty();
		
		if (hasName) {
			sql.append(" AND name LIKE ?");
		}
		
		if (districtId != null) {
			sql.append(" AND districtid = ?");
		}
		
		if (minPrice != null) {
			sql.append(" AND rentprice >= ?");
		}
		
		if (maxPrice != null) {
			sql.append(" AND rentprice <= ?");
		}
		
//		sql.append(" ORDER BY id ASC LIMIT ? OFFSET ?");
		sql.append(" ORDER BY ")
		   .append(sortColumn)
		   .append(" ")
		   .append(direction);
		
		if (!"id".equals(sortColumn)) {
		    sql.append(", id ASC");
		}
		sql.append(" LIMIT ? OFFSET ?");
		
		List<BuildingEntity> buildings = new ArrayList<>();
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql.toString())
			) {
			int parameterIndex = 1;
			if (hasName) {
				statement.setString(parameterIndex++, "%" + name.trim() + "%");
			}
			
			if (districtId != null) {
				statement.setLong(parameterIndex++, districtId);
			}
			
			if (minPrice != null) {
				statement.setInt(parameterIndex++, minPrice);
			}
			
			if (maxPrice != null) {
				statement.setInt(parameterIndex++, maxPrice);
			}
			
			statement.setInt(parameterIndex++, size);
			statement.setLong(parameterIndex++, offset);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					buildings.add(mapRow(resultSet));
				}
				return buildings;
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Khong the truy cap danh sach toa nha", exception);
		}
	}


	@Override
	public List<BuildingEntity> findByRentPrice(Integer minPrice, Integer maxPrice) {
		String sql = "SELECT id, name, street, ward, numberofbasement, rentprice FROM building WHERE rentprice >= ? AND rentprice <= ? ORDER BY rentprice ASC, id ASC";
		List<BuildingEntity> buildings = new ArrayList<BuildingEntity>();
		try (Connection connection = ConnectionUtils.getConnection();
				
			PreparedStatement statement = connection.prepareStatement(sql)){
			statement.setInt(1, minPrice);
			statement.setInt(2, maxPrice);
			
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					buildings.add(mapRow(resultSet));
				}
				return buildings;
			}
			
		} catch (SQLException exception) {
			throw new IllegalStateException("Khong the truy cap danh sach toa nha", exception);
		}
	}

	@Override
	public Long insert(Connection connection, BuildingEntity building) throws SQLException {
		String sql = "INSERT INTO building (name, street, ward, districtid, numberofbasement, rentprice) VALUES (?, ?, ?, ?, ?, ?)";
		try (
				PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			)
		{
			statement.setString(1, building.getName());
			statement.setString(2, building.getStreet());
			statement.setString(3, building.getWard());
			statement.setLong(4, building.getDistrictId());
			
			if (building.getNumberOfBasement() == null) {
				statement.setNull(5, java.sql.Types.INTEGER);
			} else {
				statement.setInt(5, building.getNumberOfBasement());
			}
			statement.setInt(6, building.getRentPrice());
			int rows  = statement.executeUpdate();
			
			if (rows  != 1) {
			    throw new IllegalStateException("Khong them duoc dung mot toa nha");
			}
			
			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next() ) {
					return keys.getLong(1);
				}
					
				throw new IllegalStateException("Khong lay duoc ID toa nha vua them");
			}
		} catch (SQLException exception) {
		    throw new IllegalStateException("Loi database khi them toa nha", exception);
		}
	}

	@Override
	public Long insert(BuildingEntity building) {
		try (Connection connection = ConnectionUtils.getConnection()){
			return insert(connection, building);
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database", exception);
		}
	}


	@Override
	public boolean existsDistrictById(Long districtId) {
		String sql = "SELECT 1 FROM district WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql))
		{
			statement.setLong(1, districtId);
			try (ResultSet resultSet = statement.executeQuery()) {
			    return resultSet.next();
			}
			
		} catch (SQLException exception) {
			throw new IllegalStateException("Loi database kiem tra district", exception);
		}
	}

	@Override
	public int update(BuildingEntity building) {
		String sql = "UPDATE building "
				+ "SET name = ?, "
				+ "street = ?, "
				+ "ward = ?, "
				+ "districtid = ?, "
				+ "numberofbasement = ?, "
				+ "rentprice = ? "
				+ "WHERE id = ?";
		
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql)
			)
		{
			statement.setString(1, building.getName());
			statement.setString(2, building.getStreet());
			statement.setString(3, building.getWard());
			statement.setLong(4, building.getDistrictId());
			if (building.getNumberOfBasement() == null) {
				statement.setNull(5, java.sql.Types.INTEGER);
			} else {
				statement.setInt(5, building.getNumberOfBasement());
			}
			statement.setInt(6, building.getRentPrice());
			statement.setLong(7, building.getId());
			
			
			return statement.executeUpdate();
		} catch (SQLException exception) {
		    throw new IllegalStateException("Loi database khi cap nhat toa nha", exception);
		}
	}
	
	@Override
	public int deleteById(Long id) {
		String sql = "DELETE FROM building WHERE id = ?";
		try (
				Connection connection = ConnectionUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);
			) {
			statement.setLong(1, id);
			return statement.executeUpdate();
		} catch (SQLException exception) {
			if (exception.getErrorCode() == 1451) {
			    throw new BuildingInUseException(
			        "Toa nha dang co du lieu lien quan, khong the xoa",
			        exception
			    );
			}

			throw new IllegalStateException("Loi database khi xoa toa nha", exception);
		}
	}

	@Override
	public boolean existsById(Connection connection, Long buildingId) throws SQLException {
		String sql = "SELECT 1 FROM building WHERE id = ?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setLong(1, buildingId);
			ResultSet rs = statement.executeQuery();
			return rs.next();
		} 
	}

	

}
