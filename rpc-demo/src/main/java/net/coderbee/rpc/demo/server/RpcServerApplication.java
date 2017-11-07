package net.coderbee.rpc.demo.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.config.ProtocolConfig;
import net.coderbee.rpc.core.config.ServiceConfig;
import net.coderbee.rpc.demo.HelloService;
import net.coderbee.rpc.demo.UserService;

/**
 * Created by coderbee on 2017/5/21.
 */
public class RpcServerApplication {
	private static Logger logger = LoggerFactory.getLogger(RpcServerApplication.class);

	public static void main(String[] args) throws InterruptedException {
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");

		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("rpc");

		String protocolPort = "rpc:9999";

		exportHelloService(registryUrl, protocolConfig, protocolPort);

		// exportHelloService(registryUrl, protocolConfig, "rpc:8020");

		// exportUserService(registryUrl, protocolConfig, "rpc:10020");

		logger.info("rpc server started .");

		Thread.sleep(1000 * 60 * 30);
	}

	private static void exportHelloService(URL registryUrl, ProtocolConfig protocolConfig, String protocolPort) {
		ServiceConfig<HelloService> helloServiceConfig = new ServiceConfig<>();
		HelloService helloService = new HelloServiceImpl();
		helloServiceConfig.setInterfaceClass(HelloService.class);
		helloServiceConfig.setRef(helloService);

		helloServiceConfig.setRegistryUrl(registryUrl);
		helloServiceConfig.setProtocolConfig(protocolConfig);

		helloServiceConfig.setExport(protocolPort);
		helloServiceConfig.export();
	}

	private static void exportUserService(URL registryUrl, ProtocolConfig protocolConfig, String protocolPort) {
		ServiceConfig<UserService> userServiceConfig = new ServiceConfig<>();
		UserService userService = new UserServiceImpl();
		userServiceConfig.setInterfaceClass(UserService.class);
		userServiceConfig.setRef(userService);

		userServiceConfig.setRegistryUrl(registryUrl);

		userServiceConfig.setProtocolConfig(protocolConfig);

		userServiceConfig.setExport(protocolPort);
		userServiceConfig.export();
	}

}
