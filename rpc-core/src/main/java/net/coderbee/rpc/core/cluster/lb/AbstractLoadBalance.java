package net.coderbee.rpc.core.cluster.lb;

import net.coderbee.rpc.core.Refer;
import net.coderbee.rpc.core.cluster.LoadBalance;

import java.util.List;

/**
 * @author coderbee on 2017/7/3.
 */
public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {
	protected List<Refer<T>> refers = null;

	@Override
	public void onRefresh(List<Refer<T>> referers) {
		this.refers = referers;
	}

}
