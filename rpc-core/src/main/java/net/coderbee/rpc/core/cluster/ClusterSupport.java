package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.Protocol;
import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.cluster.spi.ClusterSpi;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.NotifyListener;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author coderbee on 2017/6/14.
 */
public class ClusterSupport<T> implements NotifyListener {
	private static ConcurrentMap<String, Protocol> protocols = new ConcurrentHashMap<>();
	private Protocol protocol;

	private Class<T> interfaceType;
	private URL registryUrl;
	private Registry registry;
	private Cluster<T> cluster;

	public ClusterSupport(Class<T> interfaceType, URL registryUrl) {
		this.interfaceType = interfaceType;
		this.registryUrl = registryUrl;

		try {
			String urlStr = URLDecoder.decode(registryUrl.getParameter(URLParamType.embed.name()), "UTF-8");
			protocol = getProtocol(URL.build(urlStr).getProtocol());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private Protocol getProtocol(String protocolName) {
		Protocol protocol = protocols.get(protocolName);
		if (protocol == null) {
			protocol = ExtensionLoader.getSpi(Protocol.class, protocolName);
			protocols.putIfAbsent(protocolName, protocol); // 避免覆盖
			protocol = protocols.get(protocolName); // 新创建的可能没有 put 进去
		}
		return protocol;
	}

	public void init() {
		cluster = new ClusterSpi<>();

		LoadBalance loadBalance = ExtensionLoader.getSpi(LoadBalance.class, "roundRobin");
		cluster.setLoadBalance(loadBalance);

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
		List<Refer<T>> refers = new ArrayList<>(urls.size());
		for (URL url : urls) {
			System.out.println("notify url: " + url.toFullUrlString());
			Refer<T> refer = protocol.refer(interfaceType, url);
			refers.add(refer);
		}
		cluster.onRefresh(refers);
	}
}
