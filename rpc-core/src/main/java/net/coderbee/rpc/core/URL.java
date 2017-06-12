package net.coderbee.rpc.core;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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

	public static URL build(String string) {
		int pi = string.indexOf("://");
		String protocol = string.substring(0, pi);
		string = string.substring(pi + "://".length());

		int hi = string.indexOf(':');
		String host = string.substring(0, hi);
		string = string.substring(hi + 1);

		int porti = string.indexOf('/');
		int port = Integer.parseInt(string.substring(0, porti));
		string = string.substring(porti + 1);

		int pathi = string.indexOf('?');
		String path = string.substring(0, pathi);
		string = string.substring(pathi + 1);

		Map<String, String> patameters = new HashMap<>();
		if (!"".equals(string)) {
			String[] split = string.split("&");
			try {
				for (String kvs : split) {
					String[] kv = kvs.split("=");
					String paramName = URLDecoder.decode(kv[0], Constant.charset);
					String paramValue = URLDecoder.decode(kv[1], Constant.charset);
					patameters.put(paramName, paramValue);
				}
			} catch (UnsupportedEncodingException ignored) {
			}
		}

		return new URL(protocol, host, port, path, patameters);
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

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters == null ? new HashMap<>() : parameters;
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public String getParameter(String name, String defaultValue) {
		String value = parameters.get(name);
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}

	public void setParameter(String name, String value) {
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		parameters.put(name, value);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(protocol).append("://").append(host).append(':').append(port)
				.append('/').append(path).append('?');

//		parameters.entrySet().stream().collect()
		try {
			for (Map.Entry<String, String> kv : parameters.entrySet()) {
				String key = URLEncoder.encode(kv.getKey(), Constant.charset);
				String value = URLEncoder.encode(kv.getValue(), Constant.charset);
				sb.append(key).append('=').append(value).append('&');
			}
		} catch (UnsupportedEncodingException ignored) {
		}
		return sb.toString();
	}
}
