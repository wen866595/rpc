package net.coderbee.rpc.core.config;

import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.cluster.Cluster;
import net.coderbee.rpc.core.cluster.ClusterSupport;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.proxy.ProxyFactory;
import net.coderbee.rpc.core.proxy.RefererInvocationHandler;

import java.util.List;

/**
 * @author coderbee on 2017/6/21.
 */
@SpiMeta(name = "default")
public class SimpleConfigerHandler implements ConfigerHandler {
	@Override
	public <T> ClusterSupport createClusterSupport(Class<T> interfaceClass, URL registryUrl) {
		ClusterSupport<T> clusterSupport = new ClusterSupport<>(interfaceClass, registryUrl);
		clusterSupport.init();
		return clusterSupport;
	}

	@Override
	public <T> T refer(Class<T> interfaceClass, Cluster<T> cluster, String proxyType) {
		ProxyFactory proxyFactory = ExtensionLoader.getSpi(ProxyFactory.class, proxyType);
		return proxyFactory.getProxy(interfaceClass, new RefererInvocationHandler<>(interfaceClass, cluster));
	}

	@Override
	public <T> Exporter<T> export(Class<T> interfaceClass, T ref, URL registryUrl) {
		return null;
	}

	@Override
	public <T> void unExport(List<Exporter<T>> exporters, URL registryUrl) {

	}


}
