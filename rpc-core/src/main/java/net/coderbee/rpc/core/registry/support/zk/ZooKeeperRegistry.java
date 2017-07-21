package net.coderbee.rpc.core.registry.support.zk;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.registry.NotifyListener;
import net.coderbee.rpc.core.registry.support.AbstractRegistry;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author coderbee on 2017/6/26.
 */
public class ZooKeeperRegistry extends AbstractRegistry {
	private ReentrantLock serverLock = new ReentrantLock();

	private URL registryUrl;
	private ZooKeeper zooKeeper;

	public ZooKeeperRegistry(URL registryUrl, ZooKeeper zooKeeper) {
		super(registryUrl);
		this.registryUrl = registryUrl;
		this.zooKeeper = zooKeeper;
	}

	@Override
	protected void doRegister(URL url) {
		serverLock.lock();
		try {
			createNode(url, ZkNodeType.server);
		} finally {
			serverLock.unlock();
		}
	}

	@Override
	protected void doUnregister(URL url) {
		String servicePath = ZkUtils.toNodePath(url, ZkNodeType.server);
		System.out.println("doUnregister servicePath:" + servicePath);
		serverLock.lock();
		try {
			Stat stat = zooKeeper.exists(servicePath, false);
			if (stat != null) {
				zooKeeper.delete(servicePath, stat.getVersion());
			}
		} catch (InterruptedException | KeeperException e) {
			logger.error("unregister url(" + url.toFullUrlString() + ") error", e);
		} finally {
			serverLock.unlock();
		}
	}

	@Override
	protected void doSubscribe(URL url, NotifyListener listener) {
		removeNode(url, ZkNodeType.client);
		createNode(url, ZkNodeType.client);

		String servicePath = ZkUtils.toNodeTypePath(url, ZkNodeType.server);
		System.out.println("subscribe " + servicePath);
		serverLock.lock();
		try {
			List<String> children = zooKeeper.getChildren(servicePath, (WatchedEvent event) -> {
				//if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
					doSubscribe(url, listener);
				//}
			});
			System.out.println("doSubscribe children:" + children);

			List<URL> registeredUrls = children.stream()
					.map(string -> {
						String[] split = string.split(":", 2);
						URL serviceUrl = new URL(url.getProtocol(), split[0], Integer.parseInt(split[1]), url.getPath());
						serviceUrl.setParameter(URLParamType.proxy.name(), "default");
						System.out.println("zk get service url:" + serviceUrl.toFullUrlString());
						return serviceUrl;
					})
					.collect(Collectors.toList());
			listener.notify(registryUrl, registeredUrls);

		} catch (KeeperException | InterruptedException e) {
			logger.error("", e);
		} finally {
			serverLock.unlock();
		}
	}

	@Override
	protected void doUnsubscribe(URL url, NotifyListener listener) {
		removeNode(url, ZkNodeType.client);
	}

	@Override
	public List<URL> discover(URL url) {
		String nodePath = ZkUtils.toNodeTypePath(url, ZkNodeType.server);
		System.out.println("discover nodePath:" + nodePath);

		serverLock.lock();
		try {
			List<String> list = zooKeeper.getChildren(nodePath, false);

			List<URL> results = convert2url(nodePath, list);
			return results;

		} catch (KeeperException | InterruptedException e) {
			logger.error("discover url(" + url.toFullUrlString() + ") error", e);
			throw new RpcException("discover url(" + url.toFullUrlString() + ") error", e);
		} finally {
			serverLock.unlock();
		}

	}

	private List<URL> convert2url(String nodePath, List<String> list) throws KeeperException, InterruptedException {
		List<URL> results = new ArrayList<>();
		for (String service : list) {
			String fullPath = nodePath + Constant.PATH_SEPARATOR + service;
			Stat stat = zooKeeper.exists(fullPath, false);
			if (stat != null) {
				byte[] data = zooKeeper.getData(fullPath, false, stat);
				String path = new String(data);
				URL url1 = URL.build(path);
				results.add(url1);
			}
		}
		return results;
	}

	private void removeNode(URL url, ZkNodeType nodeType) {
		String nodePath = ZkUtils.toNodePath(url, nodeType);
		try {
			Stat stat = zooKeeper.exists(nodePath, false);
			if (stat != null) {
				zooKeeper.delete(nodePath, stat.getVersion());
			}
		} catch (KeeperException | InterruptedException e) {
			logger.error("removeNode, url:" + url.toFullUrlString() + " error", e);
		}
	}

	private void createNode(URL url, ZkNodeType nodeType) {
		try {
			String nodeTypePath = ZkUtils.toNodeTypePath(url, nodeType);
			Stat exists = zooKeeper.exists(nodeTypePath, false);
			if (exists == null) {
				mkdirParent("", nodeTypePath);
			}

			String nodePath = ZkUtils.toNodePath(url, nodeType);
			System.out.println("create nodePath:" + nodePath);
			zooKeeper.create(nodePath, url.toFullUrlString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL);

		} catch (InterruptedException | KeeperException e) {
			logger.error("", e);
			throw new RpcException(e.getMessage());
		}
	}

	private void mkdirParent(String left, String right) throws KeeperException, InterruptedException {
		int i = right.indexOf(Constant.PATH_SEPARATOR, 1);
		if (i != -1) {
			if (!left.equals("")) {
				left = left + Constant.PATH_SEPARATOR + right.substring(0, i);
				mkdir(left);
			} else {
				left = right.substring(0, i);
				mkdir(left);
			}

			right = right.substring(i + 1);

			mkdirParent(left, right);

		} else if (!"".equals(right)) {
			String path = left + Constant.PATH_SEPARATOR + right;
			mkdir(path);
		}
	}

	private void mkdir(String path) throws KeeperException, InterruptedException {
		Stat exists = zooKeeper.exists(path, false);
		if (exists == null) {
			zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode
					.PERSISTENT);
		}
	}
}
