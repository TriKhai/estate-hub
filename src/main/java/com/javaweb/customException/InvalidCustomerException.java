package com.javaweb.customException;

public class InvalidCustomerException extends RuntimeException {
	public InvalidCustomerException(String message) {
		super(message);
	}
}
