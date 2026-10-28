package com.javaweb.customException;

public class RentAreaNotFoundException extends RuntimeException{
	public RentAreaNotFoundException(String message) {
		super(message);
	}
}
