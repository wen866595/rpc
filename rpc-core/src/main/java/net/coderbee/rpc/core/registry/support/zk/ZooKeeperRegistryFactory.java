package net.coderbee.rpc.core.registry.support.zk;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.SpiMeta;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.support.AbstractRegistryFactory;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@SpiMeta(name = "zookeeper")
public class ZooKeeperRegistryFactory extends AbstractRegistryFactory {
	private static Logger logger = LoggerFactory.getLogger(ZooKeeperRegistryFactory.class);

	@Override
	protected Registry createRegistry(URL url) {
		logger.info("createRegistry for:" + url.toFullUrlString());

		CountDownLatch latch = new CountDownLatch(1);

		try {
			ZooKeeper zooKeeper = new ZooKeeper(url.getHostPortString(), 3000,
					(WatchedEvent event) -> {
						if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
							latch.countDown();
						}
					});

			latch.await();

			return new ZooKeeperRegistry(url, zooKeeper);

		} catch (IOException | InterruptedException e) {
			logger.error("connect to Zookeeper(" + url.toFullUrlString() + ") error", e);
			throw new RpcException("cannot create ZiikeeperRegistry", e);
		}
	}

}
