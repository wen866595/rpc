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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by coderbee on 2017/5/21.
 */
@SpringBootApplication
public class RpcServerApplication {
	private static Logger logger = LoggerFactory.getLogger(RpcServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RpcServerApplication.class, args);
	}

	public CommandLineRunner init(HelloService helloService, Registry registry) {
		return args -> {
			ServiceConfig<HelloService> serviceConfig = new ServiceConfig<>();
			serviceConfig.setInterfaceClass(HelloService.class);
			serviceConfig.setRef(helloService);

			serviceConfig.setRegistry(registry);

			ProtocolConfig protocolConfig = new ProtocolConfig();
			protocolConfig.setName("rpc");
			serviceConfig.setProtocolConfig(protocolConfig);

			serviceConfig.setExport("rpc:9999");
			serviceConfig.export();

			logger.info("rpc server started .");

		};
	}

//	@Bean
//	public RpcServerStarter initRpcServer(MethodInvoker methodInvoker,
//	                                      Registry registry) {
//		URL url = new URL("rpc", "localhost", 9999, "net.coderbee.rpc.demo.HelloService");
//		url.setParameter(URLParamType.serializer.getName(), URLParamType.serializer.getValue());
//		url.setParameter(URLParamType.proxy.getName(), URLParamType.proxy.getValue());
//
//		RpcServer rpcServer = new RpcServer(methodInvoker, url, registry);
//
//		System.out.println("inited rpc server .");
//		return new RpcServerStarter(rpcServer);
//	}
//
//	@Bean
//	public SpringBeanMethodInvoker initSpringBeanMethodInvoker() {
//		return new SpringBeanMethodInvoker();
//	}

	@Bean
	public Registry getServiceRegistry() {
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		return registryFactory.getRegistry(registryUrl);
	}

}
