package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.Beans.BuildingCreateResponse;
import com.javaweb.Beans.CustomerCreateRequest;
import com.javaweb.Beans.CustomerCreateResponse;
import com.javaweb.Beans.CustomerDTO;
import com.javaweb.Beans.CustomerUpdateRequest;
import com.javaweb.Beans.StaffAssignmentRequest;
import com.javaweb.Beans.StaffDTO;
import com.javaweb.service.CustomerAssignmentService;
import com.javaweb.service.CustomerService;

@RestController
public class CustomerAPI {
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerAssignmentService customerAssignmentService;
	
	@GetMapping("/api/customers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomer(
	        @RequestParam(value = "name", required = false) String name,
	        @RequestParam(value = "phone", required = false) String phone,
	        @RequestParam(value = "page", defaultValue = "1") Integer page,
	        @RequestParam(value = "size", defaultValue = "10") Integer size) {
		
		
		List<CustomerDTO> dtos = customerService.getAllCustomer(name, phone, page, size);
		return ResponseEntity.ok(dtos);
	}
	
	@GetMapping("/api/customers/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") Long id) {
		CustomerDTO dto = customerService.getCustomerById(id);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping("/api/customers")
	public ResponseEntity<CustomerCreateResponse> createCustomer(@RequestBody CustomerCreateRequest request) {
		Long id = customerService.createCustomer(request);

		CustomerCreateResponse response = new CustomerCreateResponse();
		response.setId(id);
		return ResponseEntity.status(201).body(response);
	}
	
	@PutMapping("/api/customers/{id}")
	public ResponseEntity<Void> updateCustomer(
			@PathVariable("id") Long id, 
			@RequestBody CustomerUpdateRequest request
		)
	{
		customerService.updateCustomer(id, request);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/api/customers/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
		customerService.deleteCustomer(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/api/customers/{customerId}/staffs")
	public ResponseEntity<List<StaffDTO>> getAssignedStaff(@PathVariable("customerId") Long customerId) {
		return ResponseEntity.ok(
				customerAssignmentService.getAssignedStaff(customerId)
	 	    );
	}
	
	@PutMapping("/api/customers/{customerId}/staffs")
	public ResponseEntity<Void> replaceAssignedStaff(
 	        @PathVariable("customerId") Long customerId,
 	        @RequestBody StaffAssignmentRequest request) {

		customerAssignmentService.replaceAssignedStaff(customerId, request);
 	    return ResponseEntity.noContent().build();
 	}
}
