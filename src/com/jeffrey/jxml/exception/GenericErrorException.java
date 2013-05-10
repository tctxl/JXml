package com.jeffrey.jxml.exception;

public class GenericErrorException extends Exception{

	public GenericErrorException() {
		super("List必须指定泛型对象");
	}
	public GenericErrorException(String detailMessage) {
		super(detailMessage);
	}
}
