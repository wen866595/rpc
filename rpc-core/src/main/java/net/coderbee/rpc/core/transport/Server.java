package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;

import java.io.IOException;

/**
 * @author coderbee on 2017/9/12.
 */
public interface Server extends EndPoint {

	boolean isBound();

	RpcResponse call(RpcRequest request) throws RpcException, IOException;

}
