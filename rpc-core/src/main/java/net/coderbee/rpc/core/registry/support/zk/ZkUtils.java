package net.coderbee.rpc.core.registry.support.zk;

import net.coderbee.rpc.core.Constant;
import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;

/**
 * @author coderbee on 2017/6/28.
 */
public class ZkUtils {

	public static String toNodeTypePath(URL url, ZkNodeType nodeType) {
		String type;
		if (nodeType == ZkNodeType.server) {
			type = "service";
		} else if (nodeType == ZkNodeType.client) {
			type = "client";
		} else {
			throw new RpcException("unknown ZkNodeType:" + nodeType);
		}
		return toServicePath(url) + Constant.PATH_SEPARATOR + type;
	}

	public static String toNodePath(URL url, ZkNodeType nodeType) {
		String typePath = toNodeTypePath(url, nodeType);
		return typePath + Constant.PATH_SEPARATOR + url.getHostPortString();
	}

	public static String toServicePath(URL url) {
		return Constant.PATH_SEPARATOR + url.getProtocol() + Constant.PATH_SEPARATOR + url.getPath();
	}

}
