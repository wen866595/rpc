package net.coderbee.rpc.core.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.*;

/**
 * Created by coderbee on 2017/6/4.
 */
public class NettyClient implements Client {
	private static Logger logger = LoggerFactory.getLogger(NettyClient.class);


	private URL serviceUrl;
	private Serializer serializer;

	private String host;
	private int port;


	public NettyClient(URL serviceUrl, Serializer serializer) {
		this.serviceUrl = serviceUrl;
		this.serializer = serializer;

		host = serviceUrl.getHost();
		port = serviceUrl.getPort();
	}

	@Override
	public boolean open() {


		return true;
	}

	@Override
	public void close() {

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
		return null;
	}

	static class NettyRpcResponse extends RpcResponse implements Future<RpcResponse> {
		private CountDownLatch latch = new CountDownLatch(1);
		private RpcResponse rpcResponse;

		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return false;
		}

		@Override
		public boolean isCancelled() {
			return false;
		}

		@Override
		public boolean isDone() {
			return false;
		}

		@Override
		public RpcResponse get() throws InterruptedException, ExecutionException {
			latch.await();
			return rpcResponse;
		}

		@Override
		public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			latch.await();
			return rpcResponse;
		}

		public void onSuccess(RpcResponse response) {
			this.rpcResponse = rpcResponse;
			latch.countDown();
		}
	}

	static class NettyChannel extends SimpleChannelInboundHandler<RpcResponse> {
		private String host;
		private int port;
		private ConcurrentMap<String, NettyRpcResponse> requestMap = new ConcurrentHashMap();
		private Channel channel;

		public NettyChannel(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public boolean open() {
			NioEventLoopGroup group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline()
									.addLast(new RpcEncoder(RpcRequest.class))
									.addLast(new RpcDecoder(RpcResponse.class))
									.addLast(NettyChannel.this);
						}
					}).option(ChannelOption.SO_KEEPALIVE, true);
			try {
				ChannelFuture future = bootstrap.connect(host, port).sync();
				channel = future.channel();
			} catch (InterruptedException e) {
				logger.error("connect to " + host + ":" + port + " failed .", e);
			}

			return true;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
			String requestId = rpcResponse.getRequestId();
			NettyRpcResponse future = requestMap.get(requestId);
			future.onSuccess(rpcResponse);
		}

		public RpcResponse send(RpcRequest request) throws ExecutionException, InterruptedException {
			NettyRpcResponse respFuture = new NettyRpcResponse();
			requestMap.put(request.getRequestId(), respFuture);

			ChannelFuture future = channel.writeAndFlush(request);
			return respFuture.get();
		}
	}

}
