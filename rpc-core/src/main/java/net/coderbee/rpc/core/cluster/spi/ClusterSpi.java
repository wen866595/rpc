package net.coderbee.rpc.core.cluster.spi;

import net.coderbee.rpc.core.*;
import net.coderbee.rpc.core.cluster.Cluster;
import net.coderbee.rpc.core.cluster.LoadBalance;
import net.coderbee.rpc.core.extension.SpiMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author coderbee on 2017/7/3.
 */
@SpiMeta(name = "default")
public class ClusterSpi<T> implements Cluster<T> {
	private AtomicBoolean available = new AtomicBoolean(false);

	private List<Refer<T>> refers;
	private LoadBalance<T> loadBalance;

	@Override
	public Class getInterface() {
		if (refers == null || refers.isEmpty()) {
			return null;
		}

		return refers.get(0).getInterface();
	}

	@Override
	public RpcResponse invoke(RpcRequest request) throws RpcException, IOException {
		if (refers == null || refers.isEmpty()) {
			throw new RpcException("no service available");
		}

		Refer<T> selectedRefer = loadBalance.select(request);
		return selectedRefer.invoke(request);
	}

	@Override
	public void init() {
		onRefresh(refers);
		available.set(true);
	}

	/**
	 * 刷新引用列表。对于在新引用列表里不存在(没法正常提供服务)的引用进行延迟销毁。
	 *
	 * @param refers 新的引用列表
	 */
	public synchronized void onRefresh(List<Refer<T>> refers) {
		if (refers == null || refers.isEmpty()) {
			return;
		}

		// 更新负载均衡
		loadBalance.onRefresh(refers);

		List<Refer<T>> oldRefers = this.refers;
		this.refers = refers;

		if (oldRefers == null) {
			return;
		}
		List<Refer<T>> delayDestroyRefers = new ArrayList<>();
		for (Refer<T> refer : oldRefers) {
			if (refers.contains(refer)) {
				continue;
			}
			delayDestroyRefers.add(refer);
		}

		Supports.delayDestroyRefers(delayDestroyRefers);
	}

	public LoadBalance<T> getLoadBalance() {
		return loadBalance;
	}

	public void setLoadBalance(LoadBalance<T> loadBalance) {
		this.loadBalance = loadBalance;
	}
}
