package net.coderbee.rpc.core.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty handler。
 * <p>
 * Created by coderbee on 2017/5/20.
 */
public class RpcHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private static final Logger logger = LoggerFactory.getLogger(RpcHandler.class);

	private MethodInvoker methodInvoker;

	public RpcHandler(MethodInvoker methodInvoker) {
		this.methodInvoker = methodInvoker;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.setRequestId(rpcRequest.getRequestId());
		try {
			Object result = handle(rpcRequest);
			rpcResponse.setResult(result);
		} catch (Throwable e) {
			rpcResponse.setError(e);
		}
		ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
	}

	private Object handle(RpcRequest rpcRequest) throws Throwable {
		String className = rpcRequest.getClassName();

		Object result = methodInvoker.invoke(rpcRequest);

		return result;
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("server caught exception", cause);
		ctx.close();
	}

}
