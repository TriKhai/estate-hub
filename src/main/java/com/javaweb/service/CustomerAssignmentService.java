package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.StaffAssignmentRequest;
import com.javaweb.Beans.StaffDTO;

public interface CustomerAssignmentService {
	List<StaffDTO> getAssignedStaff(Long customerId);

	void replaceAssignedStaff(
	    Long customerId,
	    StaffAssignmentRequest request
	);
}