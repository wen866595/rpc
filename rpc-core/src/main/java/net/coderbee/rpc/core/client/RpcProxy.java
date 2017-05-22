package net.coderbee.rpc.core.client;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcProxy {
	private String serverAddress;
	private ServiceDiscovery serviceDiscovery;

	public RpcProxy(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public RpcProxy(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	public <T> T create(Class<?> interfaceClass) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new
				InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				RpcRequest request = new RpcRequest();
				request.setRequestId(UUID.randomUUID().toString());
				request.setClassName(method.getDeclaringClass().getName());
				request.setMethodName(method.getName());
				request.setParameterTypes(method.getParameterTypes());
				request.setParameters(args);

				if (serviceDiscovery != null) {
					serverAddress = serviceDiscovery.discover();
				}

				String[] split = serverAddress.split(":");
				String host = split[0];
				int port = Integer.parseInt(split[1]);

				RpcClient client = new RpcClient(host, port);
				RpcResponse response = client.send(request);
				if (response.getError() != null) {
					throw response.getError();
				} else {
					return response.getResult();
				}
			}
		});
	}

}
