package net.coderbee.util.concurrent;

/**
 * Created by coderbee on 2017/4/21.
 */
public abstract class TraceableRunnable extends TraceableTask
		implements Runnable {

	public void run() {
		super.initContext();

		run0();
	}

	protected abstract void run0();
}
