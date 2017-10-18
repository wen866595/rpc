package net.coderbee.rpc.core;

import net.coderbee.rpc.core.*;

import java.io.IOException;

/**
 * @author coderbee on 2017/9/12.
 */
public class DefaultCaller<T> implements Caller<T> {
	private T ref;
	private Class<T> interfaceClass;
	private URL serviceUrl;

	public DefaultCaller(Class<T> interfaceClass, T ref, URL serviceUrl) {
		this.interfaceClass = interfaceClass;
		this.ref = ref;
		this.serviceUrl = serviceUrl;
	}

	@Override
	public Class<T> getInterface() {
		return interfaceClass;
	}

	@Override
	public RpcResponse invoke(RpcRequest request) throws RpcException, IOException {
		return null;
	}
}
