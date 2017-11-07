package net.coderbee.rpc.core.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.transport.AbstractPoolClient;
import net.coderbee.rpc.core.transport.Channel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author coderbee on 2017/6/4.
 */
public class NettyClient extends AbstractPoolClient {
	private static Logger logger = LoggerFactory.getLogger(NettyClient.class);

	private ConcurrentMap<String, NettyRpcResponse> requestMap = new ConcurrentHashMap<>();

	private Bootstrap bootstrap;

	public NettyClient(URL serviceUrl) {
		super(serviceUrl);
	}

	@Override
	protected PooledObjectFactory createPoolableObjectFactory() {
		return new NettyChannelFactory(this);
	}

	@Override
	public RpcResponse send(RpcRequest request) throws RpcException {
		return request(request);
	}

	public void register(String requestId, NettyRpcResponse response) {
		requestMap.put(requestId, response);
	}

	public void removeCallback(String requestId) {
		requestMap.remove(requestId);
	}

	@Override
	public boolean open() {
		if (state.isAliveState()) {
			return true;
		}

		initChannelPool();

		initClientBootstrap();

		state = ChannelState.ALIVE;

		return state.isAliveState();
	}

	private void initClientBootstrap() {
		NioEventLoopGroup group = new NioEventLoopGroup();
		bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline()
								.addLast(new RpcEncoder(serializer, RpcRequest.class))
								.addLast(new RpcDecoder(serializer, RpcResponse.class))
								.addLast(new SimpleChannelInboundHandler<RpcResponse>() {
									@Override
									protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
										String requestId = msg.getRequestId();
										logger.info("get response, requestId:" + requestId);
										NettyRpcResponse respFuture = requestMap.remove(requestId);
										if (respFuture != null) {
											respFuture.onSuccess(msg);
										} else {
											logger.info("requestId " + requestId + " has been removed");
										}
									}
								});
					}
				}).option(ChannelOption.SO_KEEPALIVE, true);
	}

	@Override
	public void close() {
		// TODO
		if (state.isCloseState()) {
			return;
		}

		pool.close();

		requestMap.clear();

		state = ChannelState.CLOSE;
	}

	@Override
	public void close(int timeout) {

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
	public RpcResponse request(RpcRequest request) {
		Channel channel = null;
		try {
			channel = borrowObject();
			return channel.send(request);
		} catch (Exception e) {
			logger.error("", e);
			throw new RpcException(e);
		} finally {
			returnObject(channel);
		}
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}
}
