package com.javaweb.customException;

public class RentTypeNotFoundException extends RuntimeException {
	public RentTypeNotFoundException(String message) {
		super(message);
	}
}
