package net.coderbee.rpc.core;

import java.io.IOException;

/**
 * 方法调用器
 *
 * @author coderbee
 */
public interface Caller<T> {

	Class<T> getInterface();

	RpcResponse invoke(RpcRequest request) throws RpcException, IOException;

}
