package net.coderbee.rpc.core.registry.support;

import io.netty.util.internal.ConcurrentSet;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.registry.NotifyListener;
import net.coderbee.rpc.core.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * 抽象的注册中心，主要用于记录日志。具体实现由子类提供。
 *
 * @author coderbee on 2017/6/24.
 */
public abstract class AbstractRegistry implements Registry {
	protected static Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);

	protected URL registryUrl;

	protected Set<URL> registedUrls = new ConcurrentSet<>();

	public AbstractRegistry(URL registryUrl) {
		this.registryUrl = registryUrl;
	}

	@Override
	public void register(URL url) {
		if (url == null) {
			logger.warn("url is null");
			return;
		}
		logger.info("register url({}) in register({})", url, registryUrl);

		doRegister(url);
		registedUrls.add(url);
	}

	@Override
	public void unregister(URL url) {
		if (url == null) {
			logger.warn("url is null");
			return;
		}
		logger.info("unregister url({}) in register({})", url, registryUrl);

		doUnregister(url);
		registedUrls.remove(url);
	}

	@Override
	public void subscribe(URL url, NotifyListener listener) {
		if (url == null || listener == null) {
			logger.warn("url or listener is null");
			return;
		}
		logger.info("listener({}) subscribe url({}) in register({})", listener, url, registryUrl);

		doSubscribe(url, listener);
	}

	@Override
	public void unsubscribe(URL url, NotifyListener listener) {
		if (url == null || listener == null) {
			logger.warn("url or listener is null");
			return;
		}
		logger.info("[{}] listener will unsubscribe url({}) in register({})", listener, url, registryUrl);

		doUnsubscribe(url, listener);
	}


	@Override
	public URL getUrl() {
		return registryUrl;
	}

	@Override
	public List<URL> availableServiceUrls() {
		return null;
	}

	@Override
	public List<URL> discover(URL url) {
		return null;
	}


	protected abstract void doRegister(URL url);

	protected abstract void doUnregister(URL url);

	protected abstract void doSubscribe(URL url, NotifyListener listener);

	protected abstract void doUnsubscribe(URL url, NotifyListener listener);

}
