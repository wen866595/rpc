package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.config.ProtocolConfig;
import net.coderbee.rpc.core.config.ServiceConfig;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import net.coderbee.rpc.demo.HelloService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by coderbee on 2017/5/21.
 */
@SpringBootApplication
public class RpcServerApplication {
	private static Logger logger = LoggerFactory.getLogger(RpcServerApplication.class);

	public static void main(String[] args) throws InterruptedException {
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		Registry registry = registryFactory.getRegistry(registryUrl);

		ServiceConfig<HelloService> serviceConfig = new ServiceConfig<>();
		HelloService helloService = new HelloServiceImpl();
		serviceConfig.setInterfaceClass(HelloService.class);
		serviceConfig.setRef(helloService);

		serviceConfig.setRegistry(registry);

		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("rpc");
		serviceConfig.setProtocolConfig(protocolConfig);

		serviceConfig.setExport("rpc:9999");
		serviceConfig.export();

		logger.info("rpc server started .");

		Thread.sleep(1000 * 60 * 30);
	}

}
