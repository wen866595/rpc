package net.coderbee.rpc.demo.client;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.config.ConfigerHandler;
import net.coderbee.rpc.core.config.ProtocolConfig;
import net.coderbee.rpc.core.config.RefererConfig;
import net.coderbee.rpc.core.config.RegistryConfig;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import net.coderbee.rpc.demo.HelloService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by coderbee on 2017/5/21.
 */
@SpringBootApplication
public class RpcClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ConfigerHandler configerHandler, Registry registry) {
		return args -> {

			System.out.println("start to run command line .");

			RefererConfig<HelloService> refererConfig = new RefererConfig<>();
			refererConfig.setInterfaceClass(HelloService.class);

			ProtocolConfig protocolConfig = new ProtocolConfig();
			protocolConfig.setName("rpc");
			refererConfig.setProtocolConfig(protocolConfig);

			RegistryConfig registryConfig = new RegistryConfig();
			registryConfig.setName("zookeeper");
			registryConfig.setHost("127.0.0.1");
			registryConfig.setPort(2181);
			refererConfig.setRegistryConfig(registryConfig);

			HelloService helloService = refererConfig.getRef();

			System.out.println("create proxy done .");
			String result = helloService.hello("rpc demo");
			System.out.println("get result:" + result);

			String result2 = helloService.hello("rpc again");
			System.out.println("----" + result2);
		};
	}

	@Bean
	public ConfigerHandler initConfigerHandler() {
		return ExtensionLoader.getSpi(ConfigerHandler.class, "default");
	}

	@Bean
	public Registry initServiceDiscovery() {
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		return registryFactory.getRegistry(registryUrl);
	}
}
