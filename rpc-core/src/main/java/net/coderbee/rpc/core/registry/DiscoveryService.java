package net.coderbee.rpc.core.registry;

import net.coderbee.rpc.core.URL;

import java.util.List;

/**
 * 服务发现
 *
 * @author coderbee on 2017/6/24.
 */
public interface DiscoveryService {

	/**
	 * 订阅服务
	 *
	 * @param url      目标资源
	 * @param listener 监听者
	 */
	void subscribe(URL url, NotifyListener listener);

	/**
	 * 取消订阅
	 *
	 * @param url      目标资源
	 * @param listener 监听者
	 */
	void unsubscribe(URL url, NotifyListener listener);

	/**
	 * 发现指定 URL 的提供者者
	 *
	 * @param url 目标资源
	 * @return 实现了目标资源的服务提供者
	 */
	List<URL> discover(URL url);

}
