package com.jeffrey.jxml.exception;

public class GenericErrorException extends Exception{

	public GenericErrorException() {
		super("List����ָ�����Ͷ���");
	}
	public GenericErrorException(String detailMessage) {
		super(detailMessage);
	}
}
