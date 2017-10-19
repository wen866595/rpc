package net.coderbee.rpc.core.config;

import java.util.List;

import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.Protocol;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.cluster.Cluster;
import net.coderbee.rpc.core.cluster.ClusterSupport;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.proxy.ProxyFactory;
import net.coderbee.rpc.core.proxy.RefererInvocationHandler;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import net.coderbee.rpc.core.server.Provider;

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
		String s = registryUrl.getParameter(URLParamType.embed.getName());
		URL serviceUrl = URL.build(s);

		// 1. 导出服务
		Protocol protocol = ExtensionLoader.getSpi(Protocol.class, serviceUrl.getProtocol());
		Provider<T> provider = new Provider<T>(ref, serviceUrl, interfaceClass);
		Exporter<T> exporter = protocol.exporter(provider, serviceUrl);

		// 2. 把服务注册到注册中心
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		Registry registry = registryFactory.getRegistry(registryUrl);
		registry.register(serviceUrl);

		return exporter;
	}

	@Override
	public <T> void unExport(List<Exporter<T>> exporters, URL registryUrl) {
		String s = registryUrl.getParameter(URLParamType.embed.getName());
		URL serviceUrl = URL.build(s);

		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, serviceUrl.getProtocol());
		Registry registry = registryFactory.getRegistry(registryUrl);

		registry.unregister(serviceUrl);
	}

}
