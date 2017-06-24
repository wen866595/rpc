package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.Caller;

/**
 * 表示一个服务代理。
 *
 * @author coderbee on 2017/6/14.
 */
public interface Cluster<T> extends Caller {

	void init();

}
