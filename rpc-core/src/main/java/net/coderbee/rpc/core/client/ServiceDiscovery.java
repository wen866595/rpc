package net.coderbee.rpc.core.client;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.URL;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
			List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, watchedEvent -> {
				if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
					watchNode(zk);
				}
			});

			List<String> dataList = new ArrayList<>();
			for (String node : nodeList) {
				byte[] bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + node, false, null);
				dataList.add(new String(bytes, Constant.charset));
			}

			logger.debug("node data: {}", dataList);
			System.out.println("node data: " + dataList);
			this.dataList = dataList;

		} catch (InterruptedException | UnsupportedEncodingException | KeeperException e) {
			logger.error("", e);
		}
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, watchedEvent -> {
				if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
					latch.countDown();
				}
			});

			latch.await();

		} catch (IOException | InterruptedException e) {
			logger.error("", e);
		}
		return zk;
	}

	public URL discover() {
		String urlString = null;
		if (!dataList.isEmpty()){
			if (dataList.size() == 0) {
				urlString = dataList.get(0);
			} else {
				int i = ThreadLocalRandom.current().nextInt(dataList.size());
				urlString = dataList.get(i);
			}
		}

		return URL.build(urlString);
	}
}
