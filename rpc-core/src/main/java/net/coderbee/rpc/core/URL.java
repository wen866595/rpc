package net.coderbee.rpc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一资源定位符。用于定位服务。
 *
 * @author coderbee
 */
public class URL {
	private String protocol;

	private String host;

	private int port;

	private String path;

	private Map<String, String> parameters;

	public URL(String protocol, String host, int port, String path) {
		this(protocol, host, port, path, new HashMap<String, String>());
	}

	public URL(String protocol, String host, int port, String path, Map<String, String> parameters) {
		this.protocol = protocol;
		this.host = host;
		this.port = port;
		this.path = path;
		this.parameters = parameters;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters == null ? new HashMap<>() : parameters;
	}
}
