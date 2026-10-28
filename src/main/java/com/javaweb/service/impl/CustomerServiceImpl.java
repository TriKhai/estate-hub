package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.Beans.CustomerCreateRequest;
import com.javaweb.Beans.CustomerDTO;
import com.javaweb.Beans.CustomerUpdateRequest;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.CustomerNotFoundException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.customException.InvalidCustomerException;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.entity.CustomerEntity;
import com.javaweb.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public CustomerDTO toDTO(CustomerEntity entity) {
		CustomerDTO dto = new CustomerDTO();
		dto.setFullname(entity.getFullname());
		dto.setId(entity.getId());
		dto.setEmail(entity.getEmail());
		dto.setPhone(entity.getPhone());
		return dto;
	}

	@Override
	public List<CustomerDTO> getAllCustomer(String name, String phone, Integer page, Integer size) {	
		if (page == null || size == null
		        || page < 1 || size < 1 || size > 100) {
		    throw new InvalidCustomerException("Page hoặc size không hợp lệ");
		}
		Long offset = (page - 1L)*size;
		List<CustomerDTO> dtos = new ArrayList<CustomerDTO>();
		List<CustomerEntity> entities = customerRepository.findAll(name, phone, size, offset);
		
		for (CustomerEntity entity: entities) {
			dtos.add(toDTO(entity));
		}
		return dtos;
	}

	@Override
	public CustomerDTO getCustomerById(Long id) {
		
		if (id == null || id <= 0) {
			throw new InvalidCustomerException("id khach hang khong hop le: " + id);
		}
		
		CustomerEntity entity = customerRepository.findById(id);
		if (entity == null) {
		    throw new CustomerNotFoundException(
		        "Không tìm thấy khách hàng có ID " + id
		    );
		}
		return toDTO(entity);
	}

	@Override
	public Long createCustomer(CustomerCreateRequest request) {
		if (request == null) {
			throw new InvalidCustomerException("Thieu thong tin khach hang");
		}
		
		String fullname = request.getFullname();
		String phone = request.getPhone();
		String email = request.getEmail();

		if (fullname == null || fullname.trim().isEmpty()) {
		    throw new InvalidCustomerException("Tên không được để trống");
		}
		fullname = fullname.trim();

		if (fullname.length() > 255) {
		    throw new InvalidCustomerException("Tên tối đa 255 ký tự");
		}

		if (phone == null || !phone.trim().matches("[0-9]{10,15}")) {
		    throw new InvalidCustomerException(
		        "Số điện thoại phải gồm 10–15 chữ số"
		    );
		}
		phone = phone.trim();

		// Email không bắt buộc: bỏ trống thì lưu null.
		email = email == null ? null : email.trim();

		if (email != null && email.isEmpty()) {
		    email = null;
		}

		if (email != null
		        && (email.length() > 255
		            || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))) {
		    throw new InvalidCustomerException("Email không hợp lệ");
		}
		
		CustomerEntity entity = new CustomerEntity();
		entity.setEmail(email);
		entity.setFullname(fullname);
		entity.setPhone(phone);
		
		return customerRepository.insert(entity);
	}

	@Override
	public void updateCustomer(Long id, CustomerUpdateRequest request) {
		if (request == null) {
			throw new InvalidCustomerException("Thieu thong tin khach hang");
		}
		
		String fullname = request.getFullname();
		String phone = request.getPhone();
		String email = request.getEmail();

		if (fullname == null || fullname.trim().isEmpty()) {
		    throw new InvalidCustomerException("Tên không được để trống");
		}
		fullname = fullname.trim();

		if (fullname.length() > 255) {
		    throw new InvalidCustomerException("Tên tối đa 255 ký tự");
		}

		if (phone == null || !phone.trim().matches("[0-9]{10,15}")) {
		    throw new InvalidCustomerException(
		        "Số điện thoại phải gồm 10–15 chữ số"
		    );
		}
		phone = phone.trim();

		// Email không bắt buộc: bỏ trống thì lưu null.
		email = email == null ? null : email.trim();

		if (email != null && email.isEmpty()) {
		    email = null;
		}

		if (email != null
		        && (email.length() > 255
		            || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))) {
		    throw new InvalidCustomerException("Email không hợp lệ");
		}
		
		CustomerEntity entity = new CustomerEntity();
		entity.setEmail(email);
		entity.setFullname(fullname);
		entity.setPhone(phone);
		entity.setId(id);
		
		int result = customerRepository.update(entity);

		if (result == 0 && customerRepository.findById(id) == null) {
		    throw new CustomerNotFoundException(
		        "Khong tim thay khach hang co ID " + id
		    );
		}
	}

	@Override
	public void deleteCustomer(Long id) {
		if (id == null || id <= 0) {
			throw new InvalidCustomerException("Id khach hang khong duoc de trong");
		}
		
		int result = customerRepository.delete(id);

		if (result == 0 && customerRepository.findById(id) == null) {
		    throw new CustomerNotFoundException(
		        "Khong tim thay khach hang co ID " + id
		    );
		}
		
	}

}
