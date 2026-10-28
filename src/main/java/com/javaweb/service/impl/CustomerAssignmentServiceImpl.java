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
import com.javaweb.customException.CustomerNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.customException.InvalidCustomerException;
import com.javaweb.repository.CustomerAssignmentRepository;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.StaffRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.service.CustomerAssignmentService;
import com.javaweb.utils.ConnectionUtils;

@Service
public class CustomerAssignmentServiceImpl implements CustomerAssignmentService {

	@Autowired
	private CustomerAssignmentRepository customerAssignmentRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private StaffRepository staffRepository;
	
	public StaffDTO toDTO(StaffEntity entity) {
		StaffDTO dto = new StaffDTO();
		dto.setFullName(entity.getFullName());
		dto.setId(entity.getId());
		return dto;
	}
	
	@Override
	public List<StaffDTO> getAssignedStaff(Long customerId) {
		if (customerId == null || customerId <= 0) {
			throw new InvalidCustomerException("id khach hang khong hop le" + customerId);
		}
		
		if (customerRepository.findById(customerId) == null) {
		    throw new CustomerNotFoundException(
		        "Không tìm thấy khách hàng có ID " + customerId
		    );
		}
		
		List<StaffEntity> entities = customerAssignmentRepository.findByCustomerId(customerId);
		List<StaffDTO> dtos = new ArrayList<StaffDTO>();
		for (StaffEntity entity: entities) {
			StaffDTO dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	public void replaceAssignedStaff(Long customerId, StaffAssignmentRequest request) {
		if (customerId == null || customerId <= 0) {
			throw new InvalidCustomerException("id khach hang khong hop le! " + customerId);
		}
		
		if (request == null || request.getStaffIds() == null) {
		    throw new InvalidCustomerException("Thiếu danh sách nhân viên");
		}

		Set<Long> seenIds = new HashSet<>();

		for (Long staffId : request.getStaffIds()) {
		    if (staffId == null || staffId <= 0) {
		        throw new InvalidCustomerException("ID nhân viên không hợp lệ");
		    }

		    if (!seenIds.add(staffId)) {
		        throw new InvalidCustomerException("ID nhân viên bị trùng");
		    }
		}
		
		try (Connection connection = ConnectionUtils.getConnection()){
			connection.setAutoCommit(false);
			
			try {
				
				if (!customerRepository.existsById(connection, customerId)) {
				    throw new CustomerNotFoundException(
				    		"Không tìm thấy khách hàng có ID " + customerId
				    );
				}
				
				for (Long staffId : request.getStaffIds()) {
				    if (!staffRepository.existsActiveStaff(connection, staffId)) {
				        throw new InvalidCustomerException(
				            "Nhân viên không tồn tại hoặc không được phép phân công: " + staffId
				        );
				    }
				}
				
				customerAssignmentRepository.deleteByCustomerId(connection, customerId);

				for (Long staffId : request.getStaffIds()) {
				    int rows = customerAssignmentRepository.insert(
				        connection, customerId, staffId
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
