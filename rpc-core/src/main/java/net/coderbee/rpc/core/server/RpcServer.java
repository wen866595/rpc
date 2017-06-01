package net.coderbee.rpc.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.coderbee.rpc.core.MethodInvoker;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

	private MethodInvoker methodInvoker;

	private String serverAddress;
	private ServiceRegistry serviceRegistry;

	public RpcServer(MethodInvoker methodInvoker, String serverAddress) {
		this(methodInvoker, serverAddress, null);
	}

	public RpcServer(MethodInvoker methodInvoker, String serverAddress, ServiceRegistry serviceRegistry) {
		if (methodInvoker == null) {
			throw new NullPointerException("methodInvoker must be not null");
		}

		if (serverAddress == null && serviceRegistry == null) {
			throw new IllegalArgumentException("serverAddress, serviceRegistry 不能同时为空");
		}

		this.methodInvoker = methodInvoker;
		this.serverAddress = serverAddress;
		this.serviceRegistry = serviceRegistry;
	}

	public void start() throws Exception {
		NioEventLoopGroup bootGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bootGroup, workGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline()
									.addLast(new RpcDecoder(RpcRequest.class))
									.addLast(new RpcEncoder(RpcResponse.class))
									.addLast(new RpcHandler(methodInvoker));
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			String[] array = serverAddress.split(":");
			String host = array[0];
			int port = Integer.parseInt(array[1]);

			ChannelFuture future = bootstrap.bind(host, port).sync();
			LOGGER.debug("server started on port {}", port);

			if (serviceRegistry != null) {
				serviceRegistry.register(serverAddress);
			}

			future.channel().closeFuture().sync();

		} finally {
			workGroup.shutdownGracefully();
			bootGroup.shutdownGracefully();
		}
	}

}
