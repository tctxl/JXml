package com.jeffrey.jxml.exception;

public class NullRequestEntityException extends Exception {
	public NullRequestEntityException() {
		super("RequestEntity为空，请确认是否加载了RequestEntity！");
	}
}
