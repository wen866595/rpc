package net.coderbee.rpc.core.cluster;

import net.coderbee.rpc.core.URL;

/**
 * @author coderbee on 2017/6/14.
 */
public class ClusterSupport<T> {
	private Class<T> interfaceType;
	private URL registryUrl;
	private Cluster<T> cluster;

	public ClusterSupport(Class<T> interfaceType, URL registryUrl) {
		this.interfaceType = interfaceType;
		this.registryUrl = registryUrl;
	}

	public void init() {

	}

	public Cluster<T> getCluster() {
		return cluster;
	}

}
