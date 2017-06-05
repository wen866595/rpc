package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;

/**
 * Created by coderbee on 2017/6/4.
 */
public interface Client extends EndPoint {

	RpcResponse request(RpcRequest request);

}
