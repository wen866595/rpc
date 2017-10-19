package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.Protocol;
import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.server.Provider;

/**
 * @author coderbee on 2017/7/23.
 */
public class ProtocolFilterDecorator implements Protocol {
	private Protocol protocol;

	public ProtocolFilterDecorator(Protocol protocol) {
		if (protocol == null) {
			new RpcException("protocol is null");
		}

		this.protocol = protocol;
	}

	@Override
	public <T> Exporter<T> exporter(Provider<T> invoker, URL serviceUrl) {
		return null;
	}

	@Override
	public <T> Refer<T> refer(Class<T> clazz, URL url) {
		return null;
	}

	@Override
	public void destroy() {
		protocol.destroy();
	}
}
