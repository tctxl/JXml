package com.jeffrey.jxml.exception;

public class KeyNotFoundException extends Exception{

	public KeyNotFoundException(String className,String key) {
		super(String.format("��[%s]�У���[%s]������",className, key));
	}
	
}
