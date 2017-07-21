package net.coderbee.rpc.core.config;

import net.coderbee.rpc.core.URL;

/**
 * @author coderbee on 2017/7/16.
 */
public class RegistryConfig extends AbstractConfig {

	private String name;
	private String host;
	private int port;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public URL toURL() {
		URL url = new URL(name, host, port, "");
		return url;
	}
}
