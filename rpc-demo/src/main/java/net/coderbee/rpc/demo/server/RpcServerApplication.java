package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.server.MethodInvoker;
import net.coderbee.rpc.core.server.RpcServer;
import net.coderbee.rpc.core.server.ServiceRegistry;
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
	                               ServiceRegistry serviceRegistry) {
		RpcServer rpcServer = new RpcServer(methodInvoker, "127.0.0.1:9999", serviceRegistry);

		return new RpcServerStarter(rpcServer);
	}

	@Bean
	public SpringBeanMethodInvoker initSpringBeanMethodInvoker() {
		return new SpringBeanMethodInvoker();
	}

	@Bean
	public ServiceRegistry getServiceRegistry() {
		String registryAddress = "127.0.0.1:2181";
		return new ServiceRegistry(registryAddress);
	}

}
