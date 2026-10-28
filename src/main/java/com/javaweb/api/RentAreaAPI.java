package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.RentAreaCreateRequest;
import com.javaweb.Beans.RentAreaDTO;
import com.javaweb.service.RentAreaService;

@RestController
public class RentAreaAPI {
	@Autowired
	private RentAreaService rentAreaService;
	
	@GetMapping("/api/building/{buildingId}/rent-areas")
	public ResponseEntity<List<RentAreaDTO>> getRentAreaByBuildingId(@PathVariable("buildingId") Long buildingId) {
		if (buildingId == null || buildingId <= 0) {
			return ResponseEntity.badRequest().build();
		}
		
		List<RentAreaDTO> dtos = rentAreaService.getRentAreaByBuildingId(buildingId);
		
		if (dtos == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dtos);
	}
	
	@PostMapping("/api/building/{buildingId}/rent-areas")
	public ResponseEntity<RentAreaDTO> createRentArea(@PathVariable("buildingId") Long buildingId, @RequestBody RentAreaCreateRequest request) {
		RentAreaDTO dto = rentAreaService.createRentArea(buildingId, request);
		return ResponseEntity.status(201).body(dto);
	}
	
	@DeleteMapping("/api/building/{buildingId}/rent-areas/{rentAreaId}")
	public ResponseEntity<Void> deleteRentArea(
			@PathVariable("buildingId") Long buildingId,
	        @PathVariable("rentAreaId") Long rentAreaId
	        ) 
	{
		rentAreaService.deleteRentArea(buildingId, rentAreaId);
		return ResponseEntity.noContent().build();
	}
}
