package net.coderbee.rpc.core.config;

import net.coderbee.rpc.core.Exporter;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.cluster.Cluster;
import net.coderbee.rpc.core.cluster.ClusterSupport;
import net.coderbee.rpc.core.extension.Spi;

import java.util.List;

/**
 * @author coderbee on 2017/6/14.
 */
@Spi
public interface ConfigerHandler {

	<T> ClusterSupport<T> createClusterSupport(Class<T> interfaceClass, URL registryUrl);

	<T> T refer(Class<T> interfaceClass, Cluster<T> cluster, String proxyType);

	<T> Exporter<T> export(Class<T> interfaceClass, T ref, URL registryUrl);

	<T> void unExport(List<Exporter<T>> exporters, URL registryUrl);
}
