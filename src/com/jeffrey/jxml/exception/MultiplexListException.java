package com.jeffrey.jxml.exception;

public class MultiplexListException extends Exception{

	public MultiplexListException(String genericClass, String childName) {
		super(String.format("����[%s]��[%s]����Ƕ������", childName , genericClass));
	}

	public MultiplexListException() {
	}
}
