package net.coderbee.rpc.core;

import net.coderbee.rpc.core.server.Provider;
import net.coderbee.rpc.core.transport.EndPoint;

/**
 * 服务导出
 *
 * @author coderbee
 */
public interface Exporter<T> extends EndPoint {

	Provider<T> getInvoker();

	void unexport();

}
