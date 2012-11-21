package com.jeffrey.jxml.test;

public class ErrorInfo {
@Override
	public String toString() {
		return String.format("ErrorInfo\nstatus=%s", status);
	}

public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

private int status;

}
