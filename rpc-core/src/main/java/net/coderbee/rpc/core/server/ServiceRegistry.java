package net.coderbee.rpc.core.server;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.URL;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册类。
 *
 * Created by coderbee on 2017/5/20.
 */
public class ServiceRegistry {
	private static final Logger logger = LoggerFactory.getLogger(ServiceRegistry.class);

	private CountDownLatch latch = new CountDownLatch(1);

	private String registryAddress;

	public ServiceRegistry(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public void register(URL serviceUrl) {
		if (serviceUrl != null) {
			ZooKeeper zk = connectServer();
			if (zk != null) {
				createNode(zk, serviceUrl);
			}
		}
	}

	private ZooKeeper connectServer() {
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
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

	private void createNode(ZooKeeper zk, URL serviceUrl) {
		try {
			byte[] bytes = serviceUrl.toString().getBytes();
			String path = zk.create(Constant.ZK_DATA_PATH, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
			logger.info("create zookeeper node ({} => {}, path:{})", path, serviceUrl.toString(), path);
		} catch (InterruptedException | KeeperException e) {
			logger.error("", e);
		}
	}

}
