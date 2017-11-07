package net.coderbee.rpc.core.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.serialize.Serializer;
import net.coderbee.rpc.core.server.RpcHandler;
import net.coderbee.rpc.core.transport.EndPoint;
import net.coderbee.rpc.core.transport.MessageHandler;
import net.coderbee.rpc.core.transport.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyServer implements Server, EndPoint {
	private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

	private URL url;
	private MessageHandler messageHandler;
	private AtomicBoolean open = new AtomicBoolean(false);

	private ChannelFuture future;

	NioEventLoopGroup bootGroup;
	NioEventLoopGroup workGroup;

	public NettyServer(URL url, MessageHandler messageHandler) {
		this.url = url;
		this.messageHandler = messageHandler;

		bootGroup = new NioEventLoopGroup();
		workGroup = new NioEventLoopGroup();
	}

	@Override
	public synchronized boolean open() {
		if (open.get()) {
			logger.warn("NettyServer is already open");
			return true;
		}

		System.out.println("start NettyServer open .");
		initBootstrap();

		open.set(true);

		return true;
	}

	private void initBootstrap() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bootGroup, workGroup).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						Serializer serializer = ExtensionLoader.getSpi(Serializer.class,
								url.getParameter(URLParamType.serializer));

						socketChannel.pipeline().addLast(new RpcDecoder(serializer, RpcRequest.class))
								.addLast(new RpcEncoder(serializer, RpcResponse.class))
								.addLast(new RpcHandler(messageHandler));
					}
				}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

		try {
			future = bootstrap.bind(url.getHost(), url.getPort()).sync();
			logger.info("server started on port {}", url.getPort());

		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}

	@Override
	public void close() {
		close(0);
	}

	@Override
	public void close(int timeout) {
		try {
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.error("", e);
		}

		workGroup.shutdownGracefully();
		bootGroup.shutdownGracefully();
	}

	@Override
	public boolean isClosed() {
		return false;
	}

	@Override
	public boolean isAvailable() {
		return false;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public SocketAddress getLocalAddress() {
		return null;
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return null;
	}

	@Override
	public RpcResponse send(RpcRequest request) throws RpcException {
		return null;
	}

	@Override
	public boolean isBound() {
		return false;
	}

	@Override
	public RpcResponse call(RpcRequest request) throws RpcException, IOException {
		throw new UnsupportedOperationException();
	}

}