package net.coderbee.rpc.core.registry.support;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coderbee on 2017/10/30.
 */
public abstract class AbstractRegistryFactory implements RegistryFactory {
	private Map<String , Registry> cache = new HashMap<>();

	@Override
	public Registry getRegistry(URL url) {
		String uri = url.getUri();
		synchronized (cache) {
			Registry registry = cache.get(uri);
			if (registry == null) {
				registry = createRegistry(url);
				cache.put(uri, registry);
			}
			return registry;
		}
	}

	abstract protected Registry createRegistry(URL url);
}
