package net.coderbee.rpc.core;

/**
 * 服务导出
 *
 * @author coderbee
 */
public interface Exporter {

	Invoker getInvoker();

	void unexport();

}
