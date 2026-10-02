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
import com.javaweb.customException.FieldRequiredException;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
	
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
}
