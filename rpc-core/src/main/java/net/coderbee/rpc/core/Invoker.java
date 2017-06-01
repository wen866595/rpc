package net.coderbee.rpc.core;

/**
 * 方法调用器
 *
 * @author coderbee
 */
public interface Invoker {

	Object invoke(RpcRequest request) throws RpcException;

}
