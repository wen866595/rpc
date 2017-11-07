package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcResponse;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by coderbee on 2017/6/6.
 */
class NettyRpcResponse extends RpcResponse {
	private CountDownLatch latch = new CountDownLatch(1);
	private volatile RpcResponse rpcResponse;

	public void onSuccess(RpcResponse response) {
		this.rpcResponse = response;
		latch.countDown();
	}

	@Override
	public String getRequestId() {
		return this.rpcResponse.getRequestId();
	}

	@Override
	public void setRequestId(String requestId) {
		this.rpcResponse.setRequestId(requestId);
	}

	@Override
	public Throwable getError() {
		return this.rpcResponse.getError();
	}

	@Override
	public void setError(Throwable error) {
		this.rpcResponse.setError(error);
	}

	@Override
	public Object getResult() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			throw new RpcException("", e);
		}
		return this.rpcResponse.getResult();
	}

	@Override
	public void setResult(Object result) {
		this.rpcResponse.setResult(result);
	}

	@Override
	public byte getVersion() {
		return this.rpcResponse.getVersion();
	}

	@Override
	public void setVersion(byte version) {
		this.rpcResponse.setVersion(version);
	}

	@Override
	public Map<String, String> getAttachments() {
		return this.rpcResponse.getAttachments();
	}

	@Override
	public void setAttachments(Map<String, String> attachments) {
		this.rpcResponse.setAttachments(attachments);
	}
}
