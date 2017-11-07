package net.coderbee.rpc.core.transport.netty;

import io.netty.channel.ChannelFuture;
import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.transport.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * 利用 Netty 进行网络传输的通道。
 *
 * @author coderbee on 2017/6/6.
 */
class NettyChannel implements Channel {
	private static Logger logger = LoggerFactory.getLogger(NettyChannel.class);

	private volatile ChannelState state = ChannelState.UNINIT;

	private NettyClient nettyClient;
	private URL serviceUrl;
	private io.netty.channel.Channel channel;

	public NettyChannel(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
		this.serviceUrl = nettyClient.getUrl();
		System.out.println("new NettyChannel, serviceUrl: " + nettyClient.getUrl());
	}

	public boolean open() {
		try {
			ChannelFuture future = nettyClient.getBootstrap().connect(serviceUrl.getHost(), serviceUrl.getPort()).sync();
			channel = future.channel();
		} catch (InterruptedException e) {
			logger.error("connect to " + serviceUrl.getHost() + ":" + serviceUrl.getPort() + " failed .", e);
		}
		return true;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return channel.localAddress();
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return nettyClient.getRemoteAddress();
	}

	@Override
	public RpcResponse send(RpcRequest request) throws RpcException {
		NettyRpcResponse nettyRpcResponse = new NettyRpcResponse();

		nettyClient.register(request.getRequestId(), nettyRpcResponse);

		ChannelFuture future = channel.writeAndFlush(request);

		return nettyRpcResponse;
		// 写超时。写是同步的，异步等待响应。
//		boolean result = future.awaitUninterruptibly(200, TimeUnit.MILLISECONDS);
//		if (result && future.isSuccess()) {
////		if (future.isSuccess()) {
//			return nettyRpcResponse;
//		}
//
//		nettyClient.removeCallback(request.getRequestId());
//
//		throw new RpcException("write request timeout or failed .");
	}

	public void close() {
		close(0);
	}

	@Override
	public void close(int timeout) {
		state = ChannelState.CLOSE;

		if (channel != null) {
			try {
				channel.closeFuture().await(timeout);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
		return state.isAliveState();
	}

	@Override
	public URL getUrl() {
		return serviceUrl;
	}
}