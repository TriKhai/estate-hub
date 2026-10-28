package com.javaweb.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.BuildingCreateRequest;
import com.javaweb.Beans.BuildingCreateResponse;
import com.javaweb.Beans.BuildingDTO;
import com.javaweb.Beans.BuildingFullCreateRequest;
import com.javaweb.Beans.BuildingUpdateRequest;
import com.javaweb.Beans.StaffAssignmentRequest;
import com.javaweb.Beans.StaffDTO;
import com.javaweb.customException.FieldRequiredException;
import com.javaweb.service.BuildingAssignmentService;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {

	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private BuildingAssignmentService buildingAssignmentService;
	
	// SOLID
// 	@GetMapping(value = "/api/building")
//	public List<BuildingDTO> getBuilding(
//			@RequestParam(name="name", required = false) String name,
//			@RequestParam(name="districtId", required = false) Long districtid,
//			@RequestParam(name="minPrice", required = false) Integer minPrice,
//			@RequestParam(name="minPrice", required = false) Integer maxPrice
//			) {
//		
// 		List<BuildingDTO> result = buildingService.getAllBuilding(name, districtid, type);
// 		return result;
//		
//	}


	@GetMapping(value = "/api/building")
	public ResponseEntity<List<BuildingDTO>> getBuilding(@RequestParam(name="name", required = false) String name,
			@RequestParam(name="districtId", required = false) Long districtid,
			@RequestParam(name="minPrice", required = false) Integer minPrice,
			@RequestParam(name="maxPrice", required = false) Integer maxPrice,
			@RequestParam(name="page", defaultValue = "1") Integer page,
			@RequestParam(name="size", defaultValue = "10") Integer size,
			@RequestParam(name="sortBy", defaultValue = "id") String sortBy,
			@RequestParam(name="sortDir", defaultValue = "asc") String sortDir) {
		
		if ((districtid != null && districtid <= 0)
		        || (minPrice != null && minPrice < 0)
		        || (maxPrice != null && maxPrice < 0)
		        || (minPrice != null && maxPrice != null && minPrice > maxPrice)) {
		    return ResponseEntity.badRequest().build();
		}
		
		if (page < 1 || size < 1 || size > 100) {
			return ResponseEntity.badRequest().build();
		}
		
		boolean validSortBy = "id".equals(sortBy) 
				|| "name".equals(sortBy)
				|| "rentPrice".equals(sortBy);
		boolean validSortDir = "asc".equals(sortDir)
		        || "desc".equals(sortDir);
		if (!validSortBy || !validSortDir) {
			return ResponseEntity.badRequest().build();
		}
		
		List<BuildingDTO> buildings = buildingService.getAllBuilding(name, districtid, minPrice, maxPrice, page, size, sortBy, sortDir);

		
		return ResponseEntity.ok(buildings);
	}
	
 	@GetMapping(value = "/api/building/{id}")
	public ResponseEntity<BuildingDTO> getBuildingById(@PathVariable("id") Long id) {
		if (id <= 0) {
			return ResponseEntity.badRequest().build();
		}
		BuildingDTO building = buildingService.getBuildingById(id);
		if (building == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(building);
	}
 	
 	
 	@GetMapping(value = "/api/building/search-by-price")
	public ResponseEntity<List<BuildingDTO>> getBuildingsByRentPrice(@RequestParam(name="minPrice") Integer minPrice, 
															@RequestParam(name="maxPrice") Integer maxPrice) {
//		if (minPrice > maxPrice || minPrice == null || maxPrice == null) { => Sai 
 		if (minPrice == null || maxPrice == null || minPrice < 0 || maxPrice < 0 || minPrice > maxPrice) {
			return ResponseEntity.badRequest().build(); // 400
		}
		List<BuildingDTO> buildings = buildingService.getBuildingsByRentPrice(minPrice, maxPrice);
		if (buildings == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(buildings);
	}
 	
 	@PostMapping("/api/building")
 	public ResponseEntity<BuildingCreateResponse> createBuilding(@RequestBody BuildingCreateRequest request) {
		Long id = buildingService.createBuilding(request);
		
		BuildingCreateResponse response = new BuildingCreateResponse();
		response.setId(id);
		return ResponseEntity.status(201).body(response);
 	}
 	
 	@PutMapping("/api/building/{id}")
 	public ResponseEntity<Void> updateBuilding(
 			 @PathVariable("id") Long id,
 			 @RequestBody BuildingUpdateRequest request
 	) {
 		buildingService.updateBuilding(id, request);
 		return ResponseEntity.noContent().build();
 	}
 	
 	@DeleteMapping("/api/building/{id}")
 	public ResponseEntity<Void> deleteBuilding(@PathVariable("id") Long id) {
 		buildingService.deleteBuilding(id);
 		return ResponseEntity.noContent().build();
 	}
 	
 	@PostMapping("/api/building/full")
 	public ResponseEntity<BuildingCreateResponse> createBuildingFull(@RequestBody BuildingFullCreateRequest request) {
 		Long id = buildingService.createBuildingFull(request);
 		BuildingCreateResponse response = new BuildingCreateResponse();
 		response.setId(id);
 		return ResponseEntity.status(201).body(response);
 	}
 	
 	// assigned building
 	@GetMapping("/api/building/{buildingId}/staffs")
 	public ResponseEntity<List<StaffDTO>> getAssignedStaff(
 	        @PathVariable("buildingId") Long buildingId) {

 	    return ResponseEntity.ok(
 	        buildingAssignmentService.getAssignedStaff(buildingId)
 	    );
 	}
 	
 	@PutMapping("/api/building/{buildingId}/staffs")
 	public ResponseEntity<Void> replaceAssignedStaff(
 	        @PathVariable("buildingId") Long buildingId,
 	        @RequestBody StaffAssignmentRequest request) {

 	    buildingAssignmentService.replaceAssignedStaff(buildingId, request);
 	    return ResponseEntity.noContent().build();
 	}
}
