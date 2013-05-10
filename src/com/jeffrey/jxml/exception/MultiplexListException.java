package com.jeffrey.jxml.exception;

public class MultiplexListException extends Exception{

	public MultiplexListException(String genericClass, String childName) {
		super(String.format("Çë¼ì²é[%s]£¬[%s]·ºÐÍÇ¶Ì×ÓÐÎó£¡", childName , genericClass));
	}

	public MultiplexListException() {
	}
}
