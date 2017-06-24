package net.coderbee.rpc.core.config;

/**
 * @author coderbee on 2017/6/21.
 */
public class MethodConfig extends AbstractConfig {
	/**
	 * 方法名
	 */
	private String name;

	/**
	 * 超时时间
	 */
	private long timeOut;

	/**
	 * 重试次数
	 */
	private int retryTimes;

	/**
	 * 并发数
	 */
	private int concurrentDegree;

	private Class<?>[] parameterTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}

	public int getConcurrentDegree() {
		return concurrentDegree;
	}

	public void setConcurrentDegree(int concurrentDegree) {
		this.concurrentDegree = concurrentDegree;
	}

	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

}
