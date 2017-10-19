package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.server.Provider;

import java.util.HashMap;
import java.util.Map;

public class ProviderMessageRouter implements MessageHandler {
	private Map<String, Provider<?>> serviceKey2providers = new HashMap<>();

	public ProviderMessageRouter(Provider<?> provider) {
		addProvider(provider);
	}

	public synchronized <T> void addProvider(Provider<T> provider) {
		URL url = provider.getUrl();
		String serviceKey = url.getParameter(URLParamType.group) + Constant.PATH_SEPARATOR + url.getPath()
				+ Constant.PATH_SEPARATOR + url.getParameter(URLParamType.version);

		if (serviceKey2providers.containsKey(serviceKey)) {
			throw new RpcException("provider already exists:" + serviceKey);
		}

		serviceKey2providers.put(serviceKey, provider);
	}

	@Override
	public Object handle(Channel channel, Object message) {
		if (message == null) {
			throw new RpcException("channel or message is null");
		}

		RpcRequest request = (RpcRequest) message;
		String serviceKey = request.getAttachment(URLParamType.group) + Constant.PATH_SEPARATOR
				+ request.getClassName() + Constant.PATH_SEPARATOR + request.getAttachment(URLParamType.version);

		Provider<?> provider = serviceKey2providers.get(serviceKey);
		if (provider == null) {
			throw new RpcException("provider not exists serviceKey:" + serviceKey);
		}

		return provider.invoke(request);
	}

}
