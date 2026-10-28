package com.javaweb.controllerAdvice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.javaweb.Beans.ErrorResponseDTO;
import com.javaweb.customException.BuildingInUseException;
import com.javaweb.customException.BuildingNotFoundException;
import com.javaweb.customException.CustomerInUseException;
import com.javaweb.customException.CustomerNotFoundException;
import com.javaweb.customException.FieldRequiredException;
import com.javaweb.customException.InvalidBuildingException;
import com.javaweb.customException.InvalidCustomerException;
import com.javaweb.customException.InvalidRentAreaException;
import com.javaweb.customException.RentAreaNotFoundException;
import com.javaweb.customException.RentTypeAlreadyAssignedException;
import com.javaweb.customException.RentTypeNotFoundException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidBuildingException.class)
	public ResponseEntity<Object> handleInvalidBuilding(InvalidBuildingException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
		error.setError(ex.getMessage());
		error.setDetail(new ArrayList<String>());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<Object> handleArithmeticException(
			ArithmeticException ex, WebRequest request) {
		ErrorResponseDTO errDTO = new ErrorResponseDTO();
		errDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("So nguyen kh chia dc cho 0.");
		errDTO.setDetail(details);
		return new ResponseEntity<>(errDTO, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// custom
	@ExceptionHandler(FieldRequiredException.class)
	public ResponseEntity<Object> handleFieldRequiredException(
			FieldRequiredException ex, WebRequest request) {
		ErrorResponseDTO errDTO = new ErrorResponseDTO();
		errDTO.setError(ex.getMessage());
		List<String> details = new ArrayList<>();
		details.add("Check lai name hoac numberOfBasement (dang null)");
		errDTO.setDetail(details);
		return new ResponseEntity<>(errDTO, HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(BuildingNotFoundException.class)
	public ResponseEntity<Object> handleBuildingNotFound(BuildingNotFoundException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
		error.setError(ex.getMessage());
		error.setDetail(new ArrayList<String>());
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BuildingInUseException.class)
	public ResponseEntity<Object> handleBuildingInUse(BuildingInUseException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	// Rent Area
	@ExceptionHandler(InvalidRentAreaException.class)
	public ResponseEntity<Object> handleInvalidRentArea(InvalidRentAreaException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
		error.setError(ex.getMessage());
		error.setDetail(new ArrayList<String>());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RentAreaNotFoundException.class)
	public ResponseEntity<Object> handleRentAreaNotFound(
	        RentAreaNotFoundException ex) {

	    ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	// Rent Type
	@ExceptionHandler(RentTypeAlreadyAssignedException.class)
	public ResponseEntity<Object> handleRentTypeAlreadyAssigned(
			RentTypeAlreadyAssignedException ex) {

	    ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(RentTypeNotFoundException.class)
	public ResponseEntity<Object> handleRentTypeNotFound(
			RentTypeNotFoundException ex) {

	    ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	// Customer
	@ExceptionHandler(InvalidCustomerException.class)
	public ResponseEntity<Object> handleInvalidCustomer(InvalidCustomerException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
		error.setError(ex.getMessage());
		error.setDetail(new ArrayList<String>());
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // 400
	}
	
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<Object> handleCustomerNotFound(
			CustomerNotFoundException ex) {

	    ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // 404
	}
	
	@ExceptionHandler(CustomerInUseException.class)
	public ResponseEntity<Object> handleCustomerInUse(CustomerInUseException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO();
	    error.setError(ex.getMessage());
	    error.setDetail(new ArrayList<String>());

	    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
}
