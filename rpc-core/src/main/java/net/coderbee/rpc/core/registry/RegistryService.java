package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

import java.util.List;

/**
 * 服务注册
 *
 * @author coderbee on 2017/6/24.
 */
public interface RegistryService {

	/**
	 * 在注册中心注册 url
	 *
	 * @param url 目标 url
	 */
	void register(URL url);

	/**
	 * 在注册中心取消注册 url
	 *
	 * @param url 目标 url
	 */
	void unregister(URL url);

	/**
	 * 返回可用的 url
	 *
	 * @return 用的 url 列表
	 */
	List<URL> availableServiceUrls();

}
