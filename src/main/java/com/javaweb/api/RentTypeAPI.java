package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.RentTypeDTO;
import com.javaweb.repository.RentTypeRepository;
import com.javaweb.service.RentTypeService;

@RestController
public class RentTypeAPI {

	@Autowired
	private RentTypeService rentTypeService;
	
	@GetMapping("/api/rent-types")
	public ResponseEntity<List<RentTypeDTO>> getAllRentTypes() {
		return ResponseEntity.ok(
		        rentTypeService.getAllRentTypes()
		    );
	}
	
	@GetMapping("/api/building/{buildingId}/rent-types")
	public ResponseEntity<List<RentTypeDTO>> getRentTypesByBuildingId(@PathVariable("buildingId") Long buildingId) {

		 return ResponseEntity.ok(
			        rentTypeService.getRentTypesByBuildingId(buildingId)
			    );
	}
	
	@PostMapping("/api/building/{buildingId}/rent-types/{rentTypeId}")
	public ResponseEntity<Void> assignRentType(
	        @PathVariable("buildingId") Long buildingId,
	        @PathVariable("rentTypeId") Long rentTypeId) {

	    rentTypeService.assignRentType(buildingId, rentTypeId);
	    return ResponseEntity.status(201).build();
	}
	
	@DeleteMapping("/api/building/{buildingId}/rent-types/{rentTypeId}")
	public ResponseEntity<Void> removeRentType(
	        @PathVariable("buildingId") Long buildingId,
	        @PathVariable("rentTypeId") Long rentTypeId) {

	    rentTypeService.removeRentType(buildingId, rentTypeId);
	    return ResponseEntity.noContent().build();
	}
}
