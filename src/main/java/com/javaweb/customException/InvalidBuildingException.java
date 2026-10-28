package com.javaweb.customException;

public class InvalidBuildingException extends RuntimeException {
    public InvalidBuildingException(String message) {
        super(message);
    }
}
