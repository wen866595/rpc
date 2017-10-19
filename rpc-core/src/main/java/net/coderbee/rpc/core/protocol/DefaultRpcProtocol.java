package net.coderbee.rpc.core.protocol;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.Protocol;
import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.server.Provider;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.EndPointFactory;
import net.coderbee.rpc.core.transport.ProviderMessageRouter;
import net.coderbee.rpc.core.transport.Server;
import net.coderbee.rpc.core.transport.netty.NettyClient;

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
		public InetAddress getLoaclAddress() {
			return server.getLoaclAddress();
		}

		@Override
		public InetAddress getRemoteAddress() {
			return server.getRemoteAddress();
		}

		@Override
		public Provider<T> getInvoker() {
			return provider;
		}
	}

}
