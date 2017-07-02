package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

/**
 * 注册中心：用于实现服务注册和发现。
 *
 * @author coderbee on 2017/6/24.
 */
public interface Registry extends DiscoveryService, RegistryService {

	URL getUrl();

}
