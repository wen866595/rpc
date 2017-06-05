package net.coderbee.rpc.core;

import java.io.IOException;

/**
 * 方法调用器
 *
 * @author coderbee
 */
public interface Invoker<T> {

	Class<T> getInterface();

	Object invoke(RpcRequest request) throws RpcException, IOException;

}
