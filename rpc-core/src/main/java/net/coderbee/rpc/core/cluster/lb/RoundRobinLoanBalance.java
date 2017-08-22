package net.coderbee.rpc.core.cluster.lb;

import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.extension.SpiMeta;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 采用轮训策略的负责均衡策略实现。
 * 采用轮训策略的负责均衡策略实现。
 *
 * @author coderbee on 2017/7/3.
 */
@SpiMeta(name = "roundRobin")
public class RoundRobinLoanBalance<T> extends AbstractLoadBalance<T> {
	private AtomicInteger index = new AtomicInteger();

	@Override
	public Refer<T> select(RpcRequest request) {
		int index = round();
		if (refers != null) {
			List<Refer<T>> ref = refers;
			Refer<T> refer = ref.get(index % ref.size());
			return refer;
		}

		return null;
	}

	private int round() {
		int i = index.getAndIncrement();
		return 0x7FFFFFFF & i;
	}
}
