package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.StaffDTO;
import com.javaweb.service.StaffService;

@RestController
public class StaffAPI {
	
	@Autowired
	private StaffService staffService;
	
	@GetMapping("/api/staffs")
	public ResponseEntity<List<StaffDTO>> getStaffs() {
		List<StaffDTO> dtos = staffService.getStaffs();
		return ResponseEntity.ok(dtos);
	}
}
