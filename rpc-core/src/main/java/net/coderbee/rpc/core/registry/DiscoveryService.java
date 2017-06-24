package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

import java.util.List;

/**
 * @author coderbee on 2017/6/24.
 */
public interface DiscoveryService {

	void subscribe(URL url, NotifyListener listener);

	void unsubscribe(URL url, NotifyListener listener);

	List<URL> discover(URL url);

}
