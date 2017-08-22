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
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.serialize.Serializer;
import net.coderbee.rpc.core.serialize.impl.Hessian2Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcServer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

	private MethodInvoker methodInvoker;
	private Serializer serializer;

	private URL serviceUrl;
	private Registry registry;

	public RpcServer(MethodInvoker methodInvoker, URL serviceUrl) {
		this(methodInvoker, serviceUrl, null);
	}

	public RpcServer(MethodInvoker methodInvoker, URL serviceUrl, Registry registry) {
		if (methodInvoker == null) {
			throw new NullPointerException("methodInvoker must be not null");
		}

		if (serviceUrl == null && registry == null) {
			throw new IllegalArgumentException("serverAddress, serviceRegistry 不能同时为空");
		}

		this.methodInvoker = methodInvoker;
		this.serializer = new Hessian2Serializer();
		this.serviceUrl = serviceUrl;
		this.registry = registry;
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
									.addLast(new RpcDecoder(serializer, RpcRequest.class))
									.addLast(new RpcEncoder(serializer, RpcResponse.class))
									.addLast(new RpcHandler(methodInvoker));
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture future = bootstrap.bind(serviceUrl.getHost(), serviceUrl.getPort()).sync();
			LOGGER.debug("server started on port {}", serviceUrl.getPort());
			System.out.println("server started on port " + serviceUrl.getPort());

			if (registry != null) {
				registry.register(serviceUrl);
				System.out.println("registered serviceUrl:" + serviceUrl.toFullUrlString());
			}

			future.channel().closeFuture().sync();

		} finally {
			workGroup.shutdownGracefully();
			bootGroup.shutdownGracefully();
		}

	}

}
