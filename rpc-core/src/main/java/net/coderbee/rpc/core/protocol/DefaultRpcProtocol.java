package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.server.Provider;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.EndPointFactory;
import net.coderbee.rpc.core.transport.ProviderMessageRouter;
import net.coderbee.rpc.core.transport.Server;
import net.coderbee.rpc.core.transport.netty.NettyClient;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author coderbee on 2017/8/19.
 */
@SpiMeta(name = "rpc")
public class DefaultRpcProtocol implements Protocol {
	private Map<String, Exporter<?>> protocolKey2exporter = new HashMap<String, Exporter<?>>();
	private Map<String, ProviderMessageRouter> ipport2router = new HashMap<String, ProviderMessageRouter>();

	@Override
	public <T> Exporter<T> exporter(Provider<T> caller, URL serviceUrl) {
		String protocolKey = serviceUrl.getProtocol() + Constant.PATH_SEPARATOR + serviceUrl.getHostPortString()
				+ Constant.PATH_SEPARATOR + serviceUrl.getPath();
		synchronized (protocolKey2exporter) {
			Exporter<?> exporter = protocolKey2exporter.get(protocolKey);
			if (exporter != null) {
				throw new RpcException("服务已经在：" + protocolKey);
			}

			DefaultRpcExporter<T> rpcExporter = new DefaultRpcExporter<>(caller, serviceUrl);
			rpcExporter.open();
			return rpcExporter;
		}
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
		private Server server;
		private Provider<T> provider;

		DefaultRpcExporter(Provider<T> provider, URL serviceUrl) {
			this.provider = provider;

			ProviderMessageRouter router = initRequestRouter(serviceUrl);

			EndPointFactory endPointFactory = ExtensionLoader.getSpi(EndPointFactory.class,
					serviceUrl.getParameter(URLParamType.endpointFactory));
			server = endPointFactory.createServer(serviceUrl, router);
		}

		private ProviderMessageRouter initRequestRouter(URL serviceUrl) {
			synchronized (ipport2router) {
				String ipport = serviceUrl.getHostPortString();
				ProviderMessageRouter router = ipport2router.get(ipport);

				if (router == null) {
					router = new ProviderMessageRouter(provider);
					ipport2router.put(ipport, router);
				} else {
					router.addProvider(provider);
				}

				return router;
			}
		}

		@Override
		public void unexport() {
		}

		@Override
		public boolean open() {
			return server.open();
		}

		@Override
		public void close() {
			server.close();
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
		public URL getUrl() {
			return null;
		}

		@Override
		public SocketAddress getLocalAddress() {
			return server.getLocalAddress();
		}

		@Override
		public SocketAddress getRemoteAddress() {
			return server.getRemoteAddress();
		}

		@Override
		public RpcResponse send(RpcRequest request) throws RpcException {
			return null;
		}

		@Override
		public Provider<T> getInvoker() {
			return provider;
		}
	}

}
