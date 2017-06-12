package net.coderbee.rpc.core.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

/**
 * 利用 Netty 进行网络传输的通道。
 *
 * @author coderbee on 2017/6/6.
 */
class NettyChannel extends SimpleChannelInboundHandler<RpcResponse> {
	private static Logger logger = LoggerFactory.getLogger(NettyChannel.class);

	private Serializer serializer;
	private URL serviceUrl;
	private ConcurrentMap<String, NettyRpcResponse> requestMap = new ConcurrentHashMap<>();
	private Channel channel;

	public NettyChannel(URL serviceUrl) {
		this.serviceUrl = serviceUrl;
		serializer = ExtensionLoader.getSpi(Serializer.class, serviceUrl, URLParamType.serializer);
		System.out.println("new NettyChannel, serviceUrl: " + serviceUrl);
	}

	public boolean open() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline()
								.addLast(new RpcEncoder(serializer, RpcRequest.class))
								.addLast(new RpcDecoder(serializer, RpcResponse.class))
								.addLast(NettyChannel.this);
					}
				}).option(ChannelOption.SO_KEEPALIVE, true);
		System.out.println("start netty channel open .");
		try {
			ChannelFuture future = bootstrap.connect(serviceUrl.getHost(), serviceUrl.getPort()).sync();
			channel = future.channel();
		} catch (InterruptedException e) {
			logger.error("connect to " + serviceUrl.getHost() + ":" + serviceUrl.getPort() + " failed .", e);
		}
		System.out.println("netty channel open finished .");
		return true;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
		String requestId = rpcResponse.getRequestId();
		NettyRpcResponse respFuture = requestMap.get(requestId);
		respFuture.onSuccess(rpcResponse);
	}

	public RpcResponse send(RpcRequest request) throws ExecutionException, InterruptedException {
		NettyRpcResponse respFuture = new NettyRpcResponse();
		requestMap.put(request.getRequestId(), respFuture);

		channel.writeAndFlush(request);
		return respFuture.get();
	}

	public void close() {
		try {
			channel.closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}
}