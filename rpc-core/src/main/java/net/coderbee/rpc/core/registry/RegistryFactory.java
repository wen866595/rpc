package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.Spi;

/**
 * @author coderbee on 2017/6/24.
 */
@Spi
public interface RegistryFactory {

	Registry getRegistry(URL url);

}
