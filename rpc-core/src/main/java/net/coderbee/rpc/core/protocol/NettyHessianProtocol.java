package net.coderbee.rpc.core.protocol;

import net.coderbee.rpc.core.*;

/**
 * Created by coderbee on 2017/5/31.
 */
public class NettyHessianProtocol implements Protocol {



	@Override
	public Exporter exporter(Invoker invoker) {
		return null;
	}

	@Override
	public Refer refer(Class<?> clazz, URL url) {
		return null;
	}

	@Override
	public void destroy() {

	}

}
