package net.coderbee.rpc.spring;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcService;
import net.coderbee.rpc.core.server.MethodInvoker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于 Spring bean 的方法调用处理类。
 * <p>
 * Created by coderbee on 2017/5/22.
 */
public class SpringBeanMethodInvoker implements MethodInvoker,
		ApplicationContextAware {

	private Map<String, Object> handlerMap = new HashMap<>();

	@Override
	public Object invoke(RpcRequest rpcRequest) throws Throwable {
		String className = rpcRequest.getClassName();

		Object bean = handlerMap.get(className);
		Method method = bean.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

		Object result = method.invoke(bean, rpcRequest.getParameters());
		return result;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
		if (serviceBeanMap != null && !serviceBeanMap.isEmpty()) {
			for (Object bean : serviceBeanMap.values()) {
				String name = bean.getClass().getAnnotation(RpcService.class).clazz().getName();
				handlerMap.put(name, bean);
			}
		}
	}
}
