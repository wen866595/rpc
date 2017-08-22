package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.Caller;
import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.extension.Spi;

import java.util.List;

/**
 * 表示一个服务代理。
 *
 * @author coderbee on 2017/6/14.
 */
@Spi
public interface Cluster<T> extends Caller {

	void init();

	void onRefresh(List<Refer<T>> referers);

	void setLoadBalance(LoadBalance<T> loadBalance);

}
