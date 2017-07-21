package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.extension.Scope;
import net.coderbee.rpc.core.extension.Spi;

import java.util.List;

/**
 * 负载均衡策略
 *
 * @author coderbee on 2017/7/3.
 */
@Spi(scope = Scope.PROTOTYPE)
public interface LoadBalance<T> {

	void onRefresh(List<Refer<T>> referers);

	Refer<T> select(RpcRequest request);

}
