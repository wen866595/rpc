package net.coderbee.rpc.core;

import java.io.Serializable;

/**
 * RPC 响应体。
 *
 * Created by coderbee on 2017/5/20.
 */
public class RpcResponse implements Serializable {
	private String requestId;
	private Throwable error;
	private Object result;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Throwable getError() {
		return error;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
