package net.coderbee.rpc.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.coderbee.util.ReflectUtil;

/**
 * @author coderbee on 2017/9/12.
 */
public class DefaultCaller<T> implements Caller<T> {
	private Map<String, Method> methodMap = new HashMap<>();

	private T ref;
	private Class<T> interfaceClass;
	private URL serviceUrl;

	public DefaultCaller(Class<T> interfaceClass, T ref, URL serviceUrl) {
		this.interfaceClass = interfaceClass;
		this.ref = ref;
		this.serviceUrl = serviceUrl;
		initMethodMap();
	}

	private void initMethodMap() {
		Method[] methods = interfaceClass.getDeclaredMethods();
		for(Method method : methods) {
			String desc = ReflectUtil.getMethodDesc(method);
			methodMap.put(desc, method);
		}
	}

	@Override
	public Class<T> getInterface() {
		return interfaceClass;
	}

	@Override
	public RpcResponse invoke(RpcRequest request) throws RpcException, IOException {
		RpcResponse response = new RpcResponse();

		String methodDesc = ReflectUtil.getMethodDesc(request.getMethodName(), request.getParameterTypeDesc());
		Method method = methodMap.get(methodDesc);
		if (method == null) {
			RpcException exception = new RpcException("Server method " + methodDesc + " not exists");
			response.setError(exception);
			return response;
		}

		try {
			Object value = method.invoke(ref, request.getParameters());
			response.setResult(value);
		} catch (Exception ex) {
			response.setError(ex);
		}

		return response;
	}
}
