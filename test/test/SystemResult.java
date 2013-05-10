package test;

import java.util.List;


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

	public List<City> getData() {
		return data;
	}

	public void setData(List<City> data) {
		this.data = data;
	}


	private String functionName;
	private String msg;
	private ErrorInfo error;
	private List<City> data;

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
