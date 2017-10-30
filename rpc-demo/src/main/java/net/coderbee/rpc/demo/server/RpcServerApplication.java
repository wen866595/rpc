package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.config.ProtocolConfig;
import net.coderbee.rpc.core.config.ServiceConfig;
import net.coderbee.rpc.demo.HelloService;
import net.coderbee.rpc.demo.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by coderbee on 2017/5/21.
 */
public class RpcServerApplication {
	private static Logger logger = LoggerFactory.getLogger(RpcServerApplication.class);

	public static void main(String[] args) throws InterruptedException {
		String protocolPort = "rpc:9999";

		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");

		ServiceConfig<HelloService> helloServiceConfig = new ServiceConfig<>();
		HelloService helloService = new HelloServiceImpl();
		helloServiceConfig.setInterfaceClass(HelloService.class);
		helloServiceConfig.setRef(helloService);

		helloServiceConfig.setRegistryUrl(registryUrl);

		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("rpc");
		helloServiceConfig.setProtocolConfig(protocolConfig);

		helloServiceConfig.setExport(protocolPort);
		helloServiceConfig.export();

		ServiceConfig<UserService> userServiceConfig = new ServiceConfig<>();
		UserService userService = new UserServiceImpl();
		userServiceConfig.setInterfaceClass(UserService.class);
		userServiceConfig.setRef(userService);

		userServiceConfig.setRegistryUrl(registryUrl);
		userServiceConfig.setProtocolConfig(protocolConfig);

		userServiceConfig.setExport(protocolPort);
		userServiceConfig.export();

		logger.info("rpc server started .");

		Thread.sleep(1000 * 60 * 30);
	}

}
