package net.coderbee.rpc.core.config;

import io.netty.util.internal.ConcurrentSet;
import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.util.NetUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author coderbee on 2017/6/21.
 */
public class ServiceConfig<T> extends AbstractConfig {
	private static ConcurrentSet<Exporter<String>> exporterIds = new ConcurrentSet<>();
	private List<Exporter<T>> exporters = new CopyOnWriteArrayList<>();
	private Class<T> interfaceClass;
	private T ref;

	private URL registryUrl;

	private ProtocolConfig protocolConfig;

	private String group;
	private String version;

	private String export;
	private AtomicBoolean exported = new AtomicBoolean(false);

	public synchronized void export() {
		if (exported.get()) {
			logger.info("repeat export, ignored .");
			return;
		}

		Map<String, Integer> protocolPort = getProtocolPort();
		for (Map.Entry<String, Integer> entry : protocolPort.entrySet()) {
			int port = protocolPort.get(protocolConfig.getName());
			doExport(protocolConfig, port, registryUrl);
		}

		exported.set(true);
	}

	private void doExport(ProtocolConfig protocolConfig, int port, URL registryUrl) {
		Map<String, String> param = new HashMap<>();
		param.put(URLParamType.nodeType.getName(), URLParamType.nodeType.getValue());
		// TODO collect config param

		URL serviceUrl = new URL(protocolConfig.getName(), NetUtil.getLocalHost(), port,
				interfaceClass.getName(), param);
		if (isExists(serviceUrl)){
			throw new RpcException(serviceUrl.toFullUrlString() + " is exported .");
		}

		registryUrl.setParameter(URLParamType.embed.getName(), serviceUrl.toFullUrlString());

		ConfigerHandler configerHandler = ExtensionLoader.getSpi(ConfigerHandler.class, "default");
		Exporter<T> exporter = configerHandler.export(interfaceClass, ref, registryUrl);
		exporters.add(exporter);
	}

	private boolean isExists(URL serviceUrl) {
		return false;
	}

	private Map<String, Integer> getProtocolPort() {
		if (export == null || "".equals(export.trim())) {
			throw new RpcException("export is empty");
		}

		return ConfigUtil.parseExport(export);
	}

	public String getExport() {
		return export;
	}

	public void setExport(String export) {
		this.export = export;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public T getRef() {
		return ref;
	}

	public void setRef(T ref) {
		this.ref = ref;
	}

	public URL getRegistryUrl() {
		return registryUrl;
	}

	public void setRegistryUrl(URL registryUrl) {
		this.registryUrl = registryUrl;
	}

	public ProtocolConfig getProtocolConfig() {
		return protocolConfig;
	}

	public void setProtocolConfig(ProtocolConfig protocolConfig) {
		this.protocolConfig = protocolConfig;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
