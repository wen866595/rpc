package net.coderbee.rpc.core;

/**
 * Created by coderbee on 2017/5/20.
 */
public interface Constant {
	String charset = "utf-8";

	String ZK_REGISTRY_PATH = "/registry";
	String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/server";
	int ZK_SESSION_TIMEOUT = 5000;

	String defaultValue = "default";

	String PATH_SEPARATOR = "/";

}
