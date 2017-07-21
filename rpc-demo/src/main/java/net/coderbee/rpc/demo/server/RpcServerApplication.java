package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.MethodInvoker;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.registry.Registry;
import net.coderbee.rpc.core.registry.RegistryFactory;
import net.coderbee.rpc.core.server.RpcServer;
import net.coderbee.rpc.spring.SpringBeanMethodInvoker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by coderbee on 2017/5/21.
 */
@SpringBootApplication
public class RpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpcServerApplication.class, args);
	}

	@Bean
	public RpcServerStarter initRpcServer(MethodInvoker methodInvoker,
	                                      Registry registry) {
		URL url = new URL("rpc", "localhost", 9999, "net.coderbee.rpc.demo.HelloService");
		url.setParameter("serializer", "hessian");

		RpcServer rpcServer = new RpcServer(methodInvoker, url, registry);

		return new RpcServerStarter(rpcServer);
	}

	@Bean
	public SpringBeanMethodInvoker initSpringBeanMethodInvoker() {
		return new SpringBeanMethodInvoker();
	}

	@Bean
	public Registry getServiceRegistry() {
		URL registryUrl = new URL("zookeeper", "127.0.0.1", 2181, "");
		RegistryFactory registryFactory = ExtensionLoader.getSpi(RegistryFactory.class, registryUrl.getProtocol());
		return registryFactory.getRegistry(registryUrl);
	}

}
