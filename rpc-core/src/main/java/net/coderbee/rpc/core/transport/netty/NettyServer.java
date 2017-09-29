package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.transport.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;

/**
 * @author coderbee on 2017/9/12.
 */
public class NettyServer implements Server {
	private InetAddress localAddress;
	private Collection<InetAddress> remoteAddresses;

	private URL serviceUrl;
	private Caller caller;
	private NettyServerChannel serverChannel;
	private boolean isOpenSucc;

	public NettyServer(URL serviceUrl, Caller caller) {
		this.serviceUrl = serviceUrl;
		this.caller = caller;
		serverChannel = new NettyServerChannel(this, serviceUrl);
	}

	@Override
	public boolean isBound() {
		return isOpenSucc;
	}

	@Override
	public RpcResponse call(RpcRequest request) throws RpcException, IOException {
		return caller.invoke(request);
	}

	@Override
	public boolean open() {
		isOpenSucc = serverChannel.open();
		return isOpenSucc;
	}

	@Override
	public void close() {
		serverChannel.close();
	}

	@Override
	public InetAddress getLoaclAddress() {
		return null;
	}

	@Override
	public InetAddress getRemoteAddress() {
		return null;
	}

}
