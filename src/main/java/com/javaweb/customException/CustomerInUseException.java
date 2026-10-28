package com.javaweb.customException;

public class CustomerInUseException extends RuntimeException{
	public CustomerInUseException(String message, Throwable cause) {
		super(message, cause);
	}
}
