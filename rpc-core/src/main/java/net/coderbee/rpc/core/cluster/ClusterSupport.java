package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.NotifyListener;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;

import java.util.List;

/**
 * @author coderbee on 2017/6/14.
 */
public class ClusterSupport<T> implements NotifyListener {
	private Class<T> interfaceType;
	private URL registryUrl;
	private Registry registry;
	private Cluster<T> cluster;

	public ClusterSupport(Class<T> interfaceType, URL registryUrl) {
		this.interfaceType = interfaceType;
		this.registryUrl = registryUrl;
	}

	public void init() {
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		registry = registryFactory.getRegistry(registryUrl);

		String refUrlString = registryUrl.getParameter(URLParamType.embed.getName());
		URL refUrl = URL.build(refUrlString);
		registry.subscribe(refUrl, this);
	}

	public Cluster<T> getCluster() {
		return cluster;
	}

	@Override
	public void notify(URL registryUrl, List<URL> urls) {

	}
}
