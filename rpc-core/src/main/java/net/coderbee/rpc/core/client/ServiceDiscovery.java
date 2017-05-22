package net.coderbee.rpc.core.client;

import net.coderbee.rpc.core.Constant;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by coderbee on 2017/5/20.
 */
public class ServiceDiscovery {
	private static final Logger logger = LoggerFactory.getLogger(ServiceDiscovery.class);

	private CountDownLatch latch = new CountDownLatch(1);
	private volatile List<String> dataList = new ArrayList<>();

	private String registryAddress;

	public ServiceDiscovery(String registryAddress) {
		this.registryAddress = registryAddress;

		ZooKeeper zk = connectServer();
		if (zk != null) {
			watchNode(zk);
		}
	}

	private void watchNode(final ZooKeeper zk) {
		try {
			List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
				@Override
				public void process(WatchedEvent watchedEvent) {
					if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
						watchNode(zk);
					}
				}
			});

			List<String> dataList = new ArrayList<>();
			for (String node : nodeList) {
				byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(bytes));
			}

			logger.debug("node data: {}", dataList);
			System.out.println("node data: " + dataList);
			this.dataList = dataList;

		} catch (InterruptedException | KeeperException e) {
			logger.error("", e);
		}
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent watchedEvent) {
					if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});

			latch.await();

		} catch (IOException | InterruptedException e) {
			logger.error("", e);
		}
		return zk;
	}

	public String discover() {
		String urlPort = null;
		if (!dataList.isEmpty()){
			if (dataList.size() == 0) {
				urlPort = dataList.get(0);
			} else {
				int i = ThreadLocalRandom.current().nextInt(dataList.size());
				urlPort = dataList.get(i);
			}
		}

		return urlPort;
	}
}
