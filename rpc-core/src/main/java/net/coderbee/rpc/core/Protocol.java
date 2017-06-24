package net.coderbee.rpc.core;

/**
 * 调用协议定义
 *
 * @author coderbee
 */
public interface Protocol {

	/**
	 * 得到该协议的服务定义器。
	 *
	 * @param invoker 方法调用器
	 * @return 服务定义器
	 */
	<T> Exporter<T> exporter(Caller invoker);

	/**
	 * 返回该协议的服务引用器
	 *
	 * @param clazz 服务类型
	 * @param url   服务资源定义
	 * @return 引用器
	 */
	<T> Refer<T> refer(Class<T> clazz, URL url);

	/**
	 * 销毁服务。用于释放服务的资源。
	 */
	void destroy();

}
