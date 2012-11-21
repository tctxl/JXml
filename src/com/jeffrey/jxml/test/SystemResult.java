package com.jeffrey.jxml.test;

import java.util.ArrayList;


public class SystemResult {

	@Override
	public String toString() {
		return String.format(
				"SystemResult\nfunctionName=%s,msg=%s,error=%s,data=%s",
				functionName, msg, error, data);
	}

	public ErrorInfo getError() {
		return error;
	}

	public void setError(ErrorInfo error) {
		this.error = error;
	}

	public ArrayList<City> getData() {
		return data;
	}

	public void setData(ArrayList<City> data) {
		this.data = data;
	}

	private String functionName;
	private String msg;
	private ErrorInfo error;
	private ArrayList<City> data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
}
