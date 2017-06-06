package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.RpcResponse;

import java.util.concurrent.*;

/**
 * Created by coderbee on 2017/6/6.
 */
class NettyRpcResponse implements Future<RpcResponse> {
	private CountDownLatch latch = new CountDownLatch(1);
	private volatile RpcResponse rpcResponse;

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
		return this.rpcResponse;
	}

	@Override
	public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		latch.await();
		return this.rpcResponse;
	}

	public void onSuccess(RpcResponse response) {
		this.rpcResponse = response;
		latch.countDown();
	}
}
