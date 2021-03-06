package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.MethodInvoker;
import net.coderbee.rpc.core.URL;
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
		URL url = new URL("nettyHessian", "localhost", 9999, "test");
		url.setParameter("serializer", "hessian");

		RpcServer rpcServer = new RpcServer(methodInvoker, url, serviceRegistry);

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
