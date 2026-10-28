package com.javaweb.customException;

public class BuildingInUseException extends RuntimeException{
	public BuildingInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
