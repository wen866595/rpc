package net.coderbee.rpc.core;

/**
 * @author coderbee on 2017/6/12.
 */
public enum URLParamType {
	proxy("proxyType", "default"),
	serializer("serializer", "hessian"),
	endpointFactory("endpointFactory", "nettyHessian");

	private String name;
	private String value;
	private long longValue;
	private int intValue;
	private boolean boolValue;

	private URLParamType(String name, String value) {
		this.name = name;
		this.value = value;
	}

	private URLParamType(String name, long longValue) {
		this.name = name;
		this.value = String.valueOf(longValue);
		this.longValue = longValue;
	}

	private URLParamType(String name, int intValue) {
		this.name = name;
		this.value = String.valueOf(intValue);
		this.intValue = intValue;
	}

	private URLParamType(String name, boolean boolValue) {
		this.name = name;
		this.value = String.valueOf(boolValue);
		this.boolValue = boolValue;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public int getIntValue() {
		return intValue;
	}

	public long getLongValue() {
		return longValue;
	}

	public boolean getBooleanValue() {
		return boolValue;
	}

}
