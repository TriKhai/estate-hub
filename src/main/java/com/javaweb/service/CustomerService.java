package com.javaweb.service;

import java.util.List;

import com.javaweb.Beans.CustomerCreateRequest;
import com.javaweb.Beans.CustomerDTO;
import com.javaweb.Beans.CustomerUpdateRequest;

public interface CustomerService {
	List<CustomerDTO> getAllCustomer(String name, String phone, Integer page, Integer size);
	CustomerDTO getCustomerById(Long id);
	Long createCustomer(CustomerCreateRequest request);
	void updateCustomer(Long id, CustomerUpdateRequest request);
	void deleteCustomer(Long id);
}
