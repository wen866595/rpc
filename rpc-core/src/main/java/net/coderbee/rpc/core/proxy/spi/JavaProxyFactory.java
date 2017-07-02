package net.coderbee.rpc.core.proxy.spi;

import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author coderbee on 2017/6/13.
 */
@SpiMeta(name = "default")
public class JavaProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProxy(Class<T> type, InvocationHandler invocationHandler) {
		ClassLoader classLoader = JavaProxyFactory.class.getClassLoader();
		return (T) Proxy.newProxyInstance(classLoader, new Class[]{type}, invocationHandler);
	}

}
