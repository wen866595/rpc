package net.coderbee.rpc.core.registry.support;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;

/**
 * @author coderbee on 2017/6/25.
 */
public class DirectRegistryFactory implements RegistryFactory {
	@Override
	public Registry getRegistry(URL url) {
		return new DirectRegistry(url);
	}
}
