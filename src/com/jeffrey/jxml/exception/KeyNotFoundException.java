package com.jeffrey.jxml.exception;

public class KeyNotFoundException extends Exception{

	public KeyNotFoundException(String className,String key) {
		super(String.format("类[%s]中，键[%s]不存在",className, key));
	}
	
}
