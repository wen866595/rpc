package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

/**
 * @author coderbee on 2017/6/24.
 */
public interface Registry extends DiscoveryService, RegistryService {

	URL getUrl();

}
