package net.coderbee.rpc.core.transport.netty;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.transport.Client;
import net.coderbee.rpc.core.transport.EndPointFactory;

/**
 * @author coderbee on 2017/6/11.
 */
public class NettyEndPointFactory implements EndPointFactory {
	@Override
	public Client createClient(URL url) {
		return new NettyClient(url);
	}

	@Override
	public void safeReleaseResources(Client client, URL url) {
		client.close();
	}
}
