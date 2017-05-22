package net.coderbee.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse> {
	private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);
	private String host;
	private int port;

	private final Object lock = new Object();
	private CountDownLatch latch = new CountDownLatch(1);

	private RpcResponse response;

	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public RpcResponse send(RpcRequest request) throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline()
									.addLast(new RpcEncoder(RpcRequest.class))
									.addLast(new RpcDecoder(RpcResponse.class))
									.addLast(RpcClient.this);
						}
					}).option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = bootstrap.connect(host, port).sync();
			future.channel().writeAndFlush(request).sync();

			latch.await();

			if (response != null) {
				future.channel().closeFuture().sync();
			}

			return response;

		} finally {
			group.shutdownGracefully();
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse o) throws Exception {
		this.response = o;
		latch.countDown();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
