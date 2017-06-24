package net.coderbee.rpc.core.proxy;

import net.coderbee.rpc.core.extension.Spi;

import java.lang.reflect.InvocationHandler;

/**
 * @author coderbee on 2017/6/13.
 */
@Spi
public interface ProxyFactory {

	<T> T getProxy(Class<T> type, InvocationHandler invocationHandler);

}
