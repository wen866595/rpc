package net.coderbee.rpc.core.registry.support;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.registry.NotifyListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author coderbee on 2017/6/24.
 */
public class DirectRegistry extends  AbstractRegistry {
	private ConcurrentMap<URL, Object> subscribedUrls = new ConcurrentHashMap<>();

	public DirectRegistry(URL registryUrl) {
		super(registryUrl);
	}

	@Override
	protected void doRegister(URL url) {
	}

	@Override
	protected void doUnregister(URL url) {
	}

	@Override
	protected void doSubscribe(URL url, NotifyListener listener) {
		subscribedUrls.putIfAbsent(url, null);
		listener.notify(url, discover(url));
	}

	@Override
	protected void doUnsubscribe(URL url, NotifyListener listener) {
		subscribedUrls.remove(url);

	}

	@Override
	public List<URL> discover(URL url) {
		return super.discover(url);
	}
}
