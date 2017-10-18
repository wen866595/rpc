package net.coderbee.rpc.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC 请求体。
 *
 * @author coderbee on 2017/5/20.
 */
public class RpcRequest implements Serializable {
	private String requestId;
	private String className;
	private String methodName;
	private Class<?>[] parameterTypes;
	private String parameterTypeDesc;

	public String getParameterTypeDesc() {
		return parameterTypeDesc;
	}

	public void setParameterTypeDesc(String parameterTypeDesc) {
		this.parameterTypeDesc = parameterTypeDesc;
	}

	private Object[] parameters;
	private Map<String , String > attachments = new HashMap<>();
	private byte version;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public void setAttachment(String name, String value) {
		attachments.put(name, value);
	}

	public String getAttachment(String name) {
		return attachments.get(name);
	}

	public String getAttachment(URLParamType paramType) {
		String s = attachments.get(paramType.getName());
		if (s == null) {
			s = paramType.getValue();
		}
		return s;
	}

	public Map<String, String> getAttachments() {
		return attachments;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}
}
