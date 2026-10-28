package com.javaweb.customException;

public class InvalidRentAreaException extends RuntimeException{
	public InvalidRentAreaException(String message) {
		super(message);
	}

}
