package net.coderbee.util.concurrent;

import java.util.concurrent.Callable;

/**
 * Created by coderbee on 2017/4/21.
 */
public abstract class TraceableCallable<T> extends TraceableTask
		implements Callable<T> {

	public T call() {
		super.initContext();

		return call0();
	}

	protected abstract T call0();
}
