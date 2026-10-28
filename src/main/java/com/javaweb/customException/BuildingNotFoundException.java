package com.javaweb.customException;

public class BuildingNotFoundException extends RuntimeException {
	public BuildingNotFoundException(String message) {
		super(message);
	}
}
