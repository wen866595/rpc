package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

import java.util.List;

/**
 * @author coderbee on 2017/6/24.
 */
public interface NotifyListener {

	void notify(URL registryUrl, List<URL> urls);

}
