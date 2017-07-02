package net.coderbee.rpc.core.registry.support.zk;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import org.junit.Test;

import java.util.List;

/**
 * @author coderbee on 2017/7/2.
 */
public class ZooKeeperRegistryTest {

	@Test
	public void test() {
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, "zookerper");
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "/");
		Registry zookeeperRegistry = registryFactory.getRegistry(registryUrl);

		URL serviceUrl = new URL("rpc", "localhost", 8081, "net.coderbee.rpc.demo.HelloService");
		URL serviceUrl2 = new URL("rpc", "localhost", 8082, "net.coderbee.rpc.demo.HelloService");

		zookeeperRegistry.register(serviceUrl);
		zookeeperRegistry.register(serviceUrl2);

		URL clientUrl = new URL("rpc", "192.168.0.0.1", 0, "net.coderbee.rpc.demo.HelloService");
		List<URL> serviceUrls = zookeeperRegistry.discover(clientUrl);
		for(URL su : serviceUrls) {
			System.out.println("server url: " + su.toFullUrlString());
		}

		zookeeperRegistry.unregister(serviceUrl);
		zookeeperRegistry.unregister(serviceUrl2);
	}

}
