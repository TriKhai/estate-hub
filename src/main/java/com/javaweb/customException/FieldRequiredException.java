package com.javaweb.customException;

//public class FieldRequiredException extends Exception { => khi dùng thì có throws FieldRequiredException
public class FieldRequiredException extends RuntimeException {

	public FieldRequiredException(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
}
