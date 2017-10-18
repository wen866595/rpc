package net.coderbee.rpc.core.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.serialize.Serializer;
import net.coderbee.rpc.core.transport.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author coderbee on 2017/9/12.
 */
public class NettyServerChannel extends SimpleChannelInboundHandler<RpcRequest> {
	private static Logger logger = LoggerFactory.getLogger(NettyServerChannel.class);

	private Server server;
	private URL url;

	private Serializer serializer;
	private Channel channel;

	public NettyServerChannel(Server server, URL url) {
		this.server = server;
		this.url = url;
		serializer = ExtensionLoader.getSpi(Serializer.class, "hessian");
	}

	public boolean open() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap().group(group).channel(NioServerSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline()
								.addLast(new RpcEncoder(serializer, RpcResponse.class))
								.addLast(new RpcDecoder(serializer, RpcRequest.class))
								.addLast(NettyServerChannel.this);
					}
				}).option(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture future = bootstrap.bind(url.getHost(), url.getPort());
		try {
			future.sync();
			channel = future.channel();
		} catch (InterruptedException e) {
			logger.error("bind to " + url.getHost() + ":" + url.getPort() + " failed .", e);
			return false;
		}
		return true;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
		logger.info("get requestId:" + request.getRequestId());
		RpcResponse response = server.call(request);
		ctx.writeAndFlush(response);
		logger.info("finish call, requestId:" + request.getRequestId());
	}

	public void close() {
		if (channel != null) {
			try {
				channel.closeFuture().sync();
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

}
