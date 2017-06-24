package net.coderbee.rpc.demo.client;

import net.coderbee.rpc.core.client.RpcProxy;
import net.coderbee.rpc.core.client.ServiceDiscovery;
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
	public CommandLineRunner commandLineRunner(RpcProxy rpcProxy) {
		return args -> {
			System.out.println("start to run command line .");
			HelloService helloService = (HelloService) rpcProxy.create(HelloService.class);
			System.out.println("create proxy done .");
			String result = helloService.hello("rpc demo");
			System.out.println(result);
			
			String result2 = helloService.hello("rpc again");
			System.out.println(result2);
		};
	}

	@Bean
	public RpcProxy initProxy(ServiceDiscovery serviceDiscovery) {
		return new RpcProxy(serviceDiscovery);
	}

	@Bean
	public ServiceDiscovery initServiceDiscovery() {
		String registryAddress = "127.0.0.1:2181";
		return new ServiceDiscovery(registryAddress);
	}
}
