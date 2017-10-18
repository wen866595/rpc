package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.Caller;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.EndPointFactory;
import net.coderbee.rpc.core.transport.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author coderbee on 2017/6/11.
 */
public class NettyEndPointFactory implements EndPointFactory {
	private Map<String, Server> ipPort2server = new HashMap<>();
	private ConcurrentMap<Server, Set<String>> server2urls = new ConcurrentHashMap<>();

	@Override
	public Client createClient(URL url) {
		return new NettyClient(url);
	}

	@Override
	public void safeReleaseResources(Client client, URL url) {
		client.close();
	}

	public Server createServer(URL serviceUrl, Caller caller) {
		synchronized (ipPort2server) {
			String hostPort = serviceUrl.getHostPortString();
//			serviceUrl.getProtocol();

			Server server = ipPort2server.get(hostPort);
			if (server != null) {
				// 检查能不能共用服务

				return server;
			}

			URL copy = serviceUrl.copy();
			copy.setPath("");

			server = new NettyServer(serviceUrl, caller);
			ipPort2server.put(hostPort, server);

			return server;
		}
	}

}
