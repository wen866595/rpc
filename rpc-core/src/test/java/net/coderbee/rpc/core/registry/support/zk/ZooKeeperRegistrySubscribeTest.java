package net.coderbee.rpc.core.registry.support.zk;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.NotifyListener;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author coderbee on 2017/7/8.
 */
public class ZooKeeperRegistrySubscribeTest {

	@Test
	public void test() {
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, "zookerper");
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "/");
		Registry zookeeperRegistry = registryFactory.getRegistry(registryUrl);

		URL clientUrl = new URL("rpc", "192.168.0.0.1", 0, "net.coderbee.rpc.demo.HelloService");

		ExecutorService pool = Executors.newFixedThreadPool(2);
		pool.submit(() -> {
			System.out.println("start to subscribe");
			zookeeperRegistry.subscribe(clientUrl, new NotifyListener() {
				@Override
				public void notify(URL registryUrl, List<URL> urls) {
					System.out.println("registryUrl:" + registryUrl + ", urls:" + urls);
				}
			});
		});

		System.out.println("hold");
	}

}
