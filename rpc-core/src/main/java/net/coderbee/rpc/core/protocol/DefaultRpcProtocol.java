package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.serialize.Serializer;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.netty.NettyClient;
import net.coderbee.rpc.core.transport.netty.NettyServer;

import java.io.IOException;
import java.net.InetAddress;

/**
 * @author coderbee on 2017/8/19.
 */
@SpiMeta(name = "rpc")
public class DefaultRpcProtocol implements Protocol {

	@Override
	public <T> Exporter<T> exporter(Caller<T> caller, URL serviceUrl) {
		return new DefaultRpcExporter<>(caller, serviceUrl);
	}

	@Override
	public <T> Refer<T> refer(Class<T> clazz, URL url) {
		return new DefaultRpcRefer<>(clazz, url);
	}

	@Override
	public void destroy() {

	}

	class DefaultRpcRefer<T> implements Refer<T> {
		private Class<T> clazz;
		private URL serviceUrl;
		private Serializer serializer;
		private Client client;

		public DefaultRpcRefer(Class<T> clazz, URL serviceUrl) {
			this.clazz = clazz;
			this.serviceUrl = serviceUrl;

			client = new NettyClient(serviceUrl);
			client.open();
		}

		@Override
		public URL getUrl() {
			return serviceUrl;
		}

		@Override
		public void destroy() {
			client.close();
		}

		@Override
		public Class<T> getInterface() {
			return clazz;
		}

		@Override
		public RpcResponse invoke(RpcRequest request) throws RpcException, IOException {
			return client.request(request);
		}
	}

	class DefaultRpcExporter<T> implements Exporter<T> {
		private Caller<T> caller;
		private URL serviceUrl;
		private NettyServer nettyServer;

		DefaultRpcExporter(Caller<T> caller, URL serviceUrl) {
			this.caller = caller;
			this.serviceUrl = serviceUrl;

			nettyServer = new NettyServer(serviceUrl, caller);
		}

		@Override
		public Caller getInvoker() {
			return caller;
		}

		@Override
		public void unexport() {

		}

		@Override
		public boolean open() {
			return nettyServer.open();
		}

		@Override
		public void close() {
			nettyServer.close();
		}

		@Override
		public InetAddress getLoaclAddress() {
			return nettyServer.getLoaclAddress();
		}

		@Override
		public InetAddress getRemoteAddress() {
			return nettyServer.getRemoteAddress();
		}
	}

}
