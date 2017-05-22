package net.coderbee.rpc.core.server;

import net.coderbee.rpc.core.RpcRequest;

/**
 * rpc 服务端方法调用。
 *
 * Created by coderbee on 2017/5/22.
 */
public interface MethodInvoker {

	Object invoke(RpcRequest rpcRequest) throws Throwable;

}
