package com.javaweb.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.StaffAssignmentRequest;
import com.javaweb.Beans.StaffDTO;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.repository.BuildingAssignmentRepository;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.StaffRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.service.BuildingAssignmentService;
import com.javaweb.utils.ConnectionUtils;

@Service
public class BuildingAssignmentServiceImpl implements BuildingAssignmentService{
	
	@Autowired
	private BuildingAssignmentRepository buildingAssignmentRepository;
	
	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private StaffRepository staffRepository;

	@Override
	public List<StaffDTO> getAssignedStaff(Long buildingId) {
		if (buildingId == null || buildingId <= 0) {
			throw new InvalidBuildingException("id toa nha khong hop le! " + buildingId);
		}
		
		if (buildingRepository.findById(buildingId) == null) {
			throw new BuildingNotFoundException("khong tim thay toa nha voi id" + buildingId);
		}
		
		List<StaffEntity> entities = buildingAssignmentRepository.findByBuildingId(buildingId);
		List<StaffDTO> dtos = new ArrayList<StaffDTO>();
		for (StaffEntity entity: entities) {
			StaffDTO dto = new StaffDTO();
			dto.setFullName(entity.getFullName());
			dto.setId(entity.getId());
			dtos.add(dto);
		}
		
		return dtos;
	}

	@Override
	public void replaceAssignedStaff(Long buildingId, StaffAssignmentRequest request) {
		if (buildingId == null || buildingId <= 0) {
			throw new InvalidBuildingException("id toa nha khong hop le! " + buildingId);
		}
		
		if (request == null || request.getStaffIds() == null) {
		    throw new InvalidBuildingException("Thiếu danh sách nhân viên");
		}

		Set<Long> seenIds = new HashSet<>();

		for (Long staffId : request.getStaffIds()) {
		    if (staffId == null || staffId <= 0) {
		        throw new InvalidBuildingException("ID nhân viên không hợp lệ");
		    }

		    if (!seenIds.add(staffId)) {
		        throw new InvalidBuildingException("ID nhân viên bị trùng");
		    }
		}
		
		try (Connection connection = ConnectionUtils.getConnection()){
			connection.setAutoCommit(false);
			
			try {
				
				if (!buildingRepository.existsById(connection, buildingId)) {
				    throw new BuildingNotFoundException(
				        "Không tìm thấy tòa nhà có ID " + buildingId
				    );
				}
				
				for (Long staffId : request.getStaffIds()) {
				    if (!staffRepository.existsActiveStaff(connection, staffId)) {
				        throw new InvalidBuildingException(
				            "Nhân viên không tồn tại hoặc không được phép phân công: " + staffId
				        );
				    }
				}
				
				buildingAssignmentRepository.deleteByBuildingId(connection, buildingId);

				for (Long staffId : request.getStaffIds()) {
				    int rows = buildingAssignmentRepository.insert(
				        connection, buildingId, staffId
				    );

				    if (rows != 1) {
				        throw new IllegalStateException("Không thể thêm phân công");
				    }
				}
				
				connection.commit();
			} catch (SQLException | RuntimeException exception) {
				try {
		            connection.rollback();
		        } catch (SQLException rollbackException) {
		            exception.addSuppressed(rollbackException);
		        }

		        throw exception;
			}
		} catch (SQLException exception) {
			throw new IllegalStateException("Lỗi database khi phân công", exception);
		}
		
		
	}
	
}
