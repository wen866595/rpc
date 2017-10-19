package net.coderbee.rpc.core;

import java.io.Serializable;
import java.util.Map;

/**
 * RPC 响应体。
 *
 * Created by coderbee on 2017/5/20.
 */
@SuppressWarnings("serial")
public class RpcResponse implements Serializable {
	private String requestId;
	private Throwable error;
	private Object result;
	private Map<String ,String > attachments;
	private byte version;

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

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}
}
