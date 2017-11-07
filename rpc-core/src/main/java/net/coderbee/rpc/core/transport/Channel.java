package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;

import java.net.SocketAddress;

public interface Channel {

	SocketAddress getLocalAddress();

	SocketAddress getRemoteAddress();

    RpcResponse send(RpcRequest request) throws RpcException;

    boolean open();

    void close();

    void close(int timeout);

    boolean isClosed();

    boolean isAvailable();

    URL getUrl();

}
