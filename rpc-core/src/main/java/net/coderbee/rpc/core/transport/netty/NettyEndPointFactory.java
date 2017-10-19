package net.coderbee.rpc.core.transport.netty;

import java.util.HashMap;
import java.util.Map;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.EndPointFactory;
import net.coderbee.rpc.core.transport.MessageHandler;
import net.coderbee.rpc.core.transport.Server;

/**
 * @author coderbee on 2017/6/11.
 */
@SpiMeta(name = "nettyHessian")
public class NettyEndPointFactory implements EndPointFactory {
	private Map<String, Server> ipPort2server = new HashMap<>();

	@Override
	public Client createClient(URL url) {
		return new NettyClient(url);
	}

	@Override
	public void safeReleaseResources(Client client, URL url) {
		client.close();
	}

	public Server createServer(URL serviceUrl, MessageHandler messageHandler) {
		synchronized (ipPort2server) {
			String hostPort = serviceUrl.getHostPortString();
			// serviceUrl.getProtocol();

			Server server = ipPort2server.get(hostPort);
			if (server != null) {
				// 检查能不能共用服务

				return server;
			}

			URL copy = serviceUrl.copy();
			copy.setPath("");

			server = new NettyServer(copy, messageHandler);
			ipPort2server.put(hostPort, server);

			return server;
		}
	}

}
