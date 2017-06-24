package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.serialize.Serializer;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.netty.NettyClient;

import java.io.IOException;

/**
 * Created by coderbee on 2017/5/31.
 */
public class NettyHessianProtocol implements Protocol {


	@Override
	public <T> Exporter<T> exporter(Caller invoker) {
		return null;
	}

	@Override
	public <T> Refer<T> refer(Class<T> clazz, URL url) {
		return new NettyHessianRefer<T>(clazz, url);
	}

	@Override
	public void destroy() {

	}

	static class NettyHessianRefer<T> implements Refer<T> {
		private Class<T> clazz;
		private URL serviceUrl;
		private Serializer serializer;
		private Client client;

		public NettyHessianRefer(Class<T> clazz, URL serviceUrl) {
			this.clazz = clazz;
			this.serviceUrl = serviceUrl;

			client = new NettyClient(serviceUrl);
		}

		@Override
		public Class<T> getInterface() {
			return clazz;
		}

		@Override
		public Object invoke(RpcRequest request) throws RpcException, IOException {
			return client.request(request);
		}
	}

}
