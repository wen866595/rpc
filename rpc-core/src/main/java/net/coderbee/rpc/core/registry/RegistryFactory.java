package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.Spi;

/**
 * 注册中心工厂
 *
 * @author coderbee on 2017/6/24.
 */
@Spi
public interface RegistryFactory {

	/**
	 * 根据给定 url 创建注册中心
	 *
	 * @param url 表示注册中心的 url
	 * @return 注册中心实例
	 */
	Registry getRegistry(URL url);

}
