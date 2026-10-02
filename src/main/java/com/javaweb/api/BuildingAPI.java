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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.BuildingDTO;
import com.javaweb.customException.FieldRequiredException;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;
	
	// SOLID
 	@GetMapping(value = "/api/building")
	public List<BuildingDTO> getBuilding(
			@RequestParam(name="name", required = false) String name,
			@RequestParam(name="districtId", required = false) String districtid,
			@RequestParam(name="typeOf", required = false) List<String> type
			) {
		
 		List<BuildingDTO> result = buildingService.getAllBuilding(name, districtid, type);
 		return result;
		
	}
 	
 	
}
