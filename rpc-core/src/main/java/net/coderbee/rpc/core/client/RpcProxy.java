//package net.coderbee.rpc.core.client;
//
//import net.coderbee.rpc.core.RpcRequest;
//import net.coderbee.rpc.core.RpcResponse;
//import net.coderbee.rpc.core.URL;
//import net.coderbee.rpc.core.cluster.ClusterSupport;
//import net.coderbee.rpc.core.registry.Registry;
//import net.coderbee.rpc.core.transport.netty.NettyClient;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.util.UUID;
//
///**
// * Created by coderbee on 2017/5/20.
// */
//public class RpcProxy<T> {
//	private Registry registry;
//
//	public RpcProxy(Registry registry) {
//		this.registry = registry;
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T create(Class<?> interfaceClass) {
//		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new
//				InvocationHandler() {
//			@Override
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				RpcRequest request = new RpcRequest();
//				request.setRequestId(UUID.randomUUID().toString());
//				request.setClassName(method.getDeclaringClass().getName());
//				request.setMethodName(method.getName());
//				request.setParameterTypes(method.getParameterTypes());
//				request.setParameters(args);
//
//				nettyClient.open();
//				RpcResponse response = nettyClient.request(request);
//				if (response.getError() != null) {
//					throw response.getError();
//				} else {
//					return response.getResult();
//				}
//			}
//		});
//	}
//
//}
