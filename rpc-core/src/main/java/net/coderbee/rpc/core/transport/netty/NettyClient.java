package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.transport.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.ExecutionException;

/**
 * @author coderbee on 2017/6/4.
 */
public class NettyClient implements Client {
	private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

	private URL serviceUrl;
	private NettyChannel channel;

	public NettyClient(URL serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Override
	public boolean open() {
		channel = new NettyChannel(serviceUrl);
		return channel.open();
	}

	@Override
	public void close() {
		channel.close();
	}

	@Override
	public InetAddress getLoaclAddress() {
		return null;
	}

	@Override
	public InetAddress getRemoteAddress() {
		return null;
	}

	@Override
	public RpcResponse request(RpcRequest request) {
		try {
			return channel.send(request);
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			logger.error("", e);
			throw new RpcException(e);
		}
	}

}
