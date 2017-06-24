package net.coderbee.rpc.core.proxy;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.cluster.Cluster;
import net.coderbee.util.SystemUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author coderbee on 2017/6/14.
 */
public class RefererInvocationHandler<T> implements InvocationHandler {
	private Class<T> type;
	private Cluster cluster;

	public RefererInvocationHandler(Class<T> type, Cluster<T> cluster) {
		this.type = type;
		this.cluster = cluster;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		RpcRequest rpcRequest = new RpcRequest();
		rpcRequest.setRequestId(SystemUtil.randomUUID());
		rpcRequest.setClassName(type.getName());
		rpcRequest.setMethodName(method.getName());
		rpcRequest.setParameters(args);
		rpcRequest.setParameterTypes(method.getParameterTypes());

		RpcResponse rpcResponse = cluster.invoke(rpcRequest);

		return rpcResponse.getResult();
	}
}
