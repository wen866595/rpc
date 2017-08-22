package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.*;

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
	public <T> Exporter<T> exporter(Caller invoker) {
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
