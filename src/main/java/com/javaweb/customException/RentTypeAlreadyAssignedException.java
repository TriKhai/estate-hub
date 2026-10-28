package com.javaweb.customException;

public class RentTypeAlreadyAssignedException extends RuntimeException {
	public RentTypeAlreadyAssignedException(
            String message, Throwable cause) {
        super(message, cause);
    }

    public RentTypeAlreadyAssignedException(String message) {
        super(message);
    }
}
