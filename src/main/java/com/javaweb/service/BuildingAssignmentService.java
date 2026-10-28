package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.StaffAssignmentRequest;
import com.javaweb.Beans.StaffDTO;

public interface BuildingAssignmentService {
	List<StaffDTO> getAssignedStaff(Long buildingId);
	void replaceAssignedStaff(Long buildingId, StaffAssignmentRequest request);
}
