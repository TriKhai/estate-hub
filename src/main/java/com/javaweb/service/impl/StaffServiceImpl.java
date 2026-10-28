package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.StaffDTO;
import com.javaweb.repository.StaffRepository;
import com.javaweb.repository.entity.StaffEntity;
import com.javaweb.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffRepository staffRepository;
	
	public StaffDTO mapTo(StaffEntity entity) {
		StaffDTO dto = new StaffDTO();
		dto.setFullName(entity.getFullName());
		dto.setId(entity.getId());
		return dto;
	}
	
	@Override
	public List<StaffDTO> getStaffs() {
		List<StaffEntity> entities = staffRepository.findAllActiveStaff();
		List<StaffDTO> dtos = new ArrayList<StaffDTO>();
		for (StaffEntity entity: entities) {
			StaffDTO dto = mapTo(entity);
			dtos.add(dto);
		}
		return dtos;
	}

}
