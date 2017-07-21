package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.Scope;
import net.coderbee.rpc.core.extension.Spi;

import javax.xml.ws.Response;

/**
 * 高可用策略
 *
 * @author coderbee on 2017/7/3.
 */
@Spi(scope = Scope.PROTOTYPE)
public interface HaStrategy<T> {

	void setUrl(URL url);

	Response call(RpcRequest request, LoadBalance<T> loadBalance);

}
