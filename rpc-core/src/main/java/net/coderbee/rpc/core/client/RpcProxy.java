package net.coderbee.rpc.core.client;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.transport.netty.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcProxy {
	private ServiceDiscovery serviceDiscovery;

	public RpcProxy(ServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}

	@SuppressWarnings("unchecked")
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

				URL serviceUrl = serviceDiscovery.discover();
				serviceUrl.setPath(interfaceClass.getName());

				NettyClient nettyClient = new NettyClient(serviceUrl);
				nettyClient.open();
				RpcResponse response = nettyClient.request(request);
				if (response.getError() != null) {
					throw response.getError();
				} else {
					return response.getResult();
				}
			}
		});
	}

}
