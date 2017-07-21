package net.coderbee.rpc.core.config;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.cluster.ClusterSupport;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.util.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author coderbee on 2017/6/21.
 */
public class RefererConfig<T> extends AbstractConfig {

	// 具体到方法的配置
	protected List<MethodConfig> methods;
	// 点对点直连服务提供地址
	private String directUrl;

	private RegistryConfig registryConfig;
	private URL registryUrl;

	private ProtocolConfig protocolConfig;
	private Class<T> interfaceClass;
	private T ref;

	private AtomicBoolean isInit = new AtomicBoolean(false);

	private ClusterSupport<T> clusterSupport;

	public T getRef() {
		if (ref == null) {
			initRef();
		}
		return ref;
	}

	private synchronized void initRef() {
		if (isInit.get()) {
			return;
		}

		if (protocolConfig == null) {
			throw new RpcException("配置错误，协议没有设置");
		}

		ConfigerHandler configerHandler = ExtensionLoader.getSpi(ConfigerHandler.class, Constant.defaultValue);

		Map<String, String> params = new HashMap<String, String>();

		collectMethodParams(params, methods);

		String localHost = NetUtil.getLocalHost();
		URL refUrl = new URL(protocolConfig.getName(), localHost, 0, interfaceClass.getName());

		clusterSupport = createClusterSupport(configerHandler, refUrl, registryUrl);

		ref = configerHandler.refer(interfaceClass, clusterSupport.getCluster(), refUrl.getParameter(URLParamType
				.proxy.getName()));

		isInit.set(true);
	}

	private ClusterSupport<T> createClusterSupport(ConfigerHandler configerHandler, URL refUrl, URL registryUrl) {
		registryUrl.setParameter(URLParamType.embed.getName(), refUrl.toFullUrlString());
		return configerHandler.createClusterSupport(interfaceClass, registryUrl);
	}

	public String getDirectUrl() {
		return directUrl;
	}

	public void setDirectUrl(String directUrl) {
		this.directUrl = directUrl;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public List<MethodConfig> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodConfig> methods) {
		this.methods = methods;
	}

	public ProtocolConfig getProtocolConfig() {
		return protocolConfig;
	}

	public void setProtocolConfig(ProtocolConfig protocolConfig) {
		this.protocolConfig = protocolConfig;
	}

	public ClusterSupport<T> getClusterSupport() {
		return clusterSupport;
	}

	public void setClusterSupport(ClusterSupport<T> clusterSupport) {
		this.clusterSupport = clusterSupport;
	}

	public RegistryConfig getRegistryConfig() {
		return registryConfig;
	}

	public void setRegistryConfig(RegistryConfig registryConfig) {
		this.registryConfig = registryConfig;
		this.registryUrl = registryConfig.toURL();
	}
}
