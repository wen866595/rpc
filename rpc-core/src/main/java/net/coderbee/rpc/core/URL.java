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

	private static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, Constant.charset);
		} catch (UnsupportedEncodingException ignored) {
			return str;
		}
	}

	private static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, Constant.charset);
		} catch (UnsupportedEncodingException ignored) {
			return str;
		}
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

		Map<String, String> patameters = new HashMap<>();
		String path;
		int pathi = string.indexOf('?');
		if (pathi != -1) {
			path = string.substring(0, pathi);
			string = string.substring(pathi + 1);

			if (!"".equals(string)) {
				String[] split = string.split("&");
				for (String kvs : split) {
					String[] kv = kvs.split("=");
					String paramName = urlDecode(kv[0]);
					String paramValue = urlDecode(kv[1]);
					patameters.put(paramName, paramValue);
				}
			}
		} else {
			path = string;
		}


		return new URL(protocol, host, port, path, patameters);
	}

	public String toServicePath() {
		return protocol + Constant.PATH_SEPARATOR + path + Constant.PATH_SEPARATOR;
	}

	public String toFullUrlString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append(protocol).append("://").append(host).append(':').append(port).append('/').append(path);
		if (parameters.size() > 0) {
			boolean first = true;
			for (Map.Entry<String, String> param : parameters.entrySet()) {
				if (first) {
					sb.append('?');
					first = false;
				} else {
					sb.append('&');
				}
				sb.append(urlEncode(param.getKey())).append('=').append(urlEncode(param.getValue()));
			}
		}

		return sb.toString();
	}

	public String getHostPortString() {
		return host + ":" + port;
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

	public String getUri() {
		return protocol + "://" + host + ":" + port + Constant.PATH_SEPARATOR + path;
	}
}
