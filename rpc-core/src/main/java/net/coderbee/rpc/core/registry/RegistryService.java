package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

import java.util.List;

/**
 * @author coderbee on 2017/6/24.
 */
public interface RegistryService {

	void register(URL url);

	void unregister(URL url);

	List<URL> availableServiceUrls();

}
