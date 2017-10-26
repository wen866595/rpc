package net.coderbee.rpc.demo.client;

import net.coderbee.rpc.core.config.ProtocolConfig;
import net.coderbee.rpc.core.config.RefererConfig;
import net.coderbee.rpc.core.config.RegistryConfig;
import net.coderbee.rpc.demo.HelloService;
import net.coderbee.rpc.demo.UserService;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by coderbee on 2017/5/21.
 */
public class RpcClientApplication {

	public static void main(String[] args) throws InterruptedException {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("rpc");

		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setName("zookeeper");
		registryConfig.setHost("127.0.0.1");
		registryConfig.setPort(2181);

		RefererConfig<HelloService> helloRefererConfig = new RefererConfig<>();
		helloRefererConfig.setInterfaceClass(HelloService.class);
		helloRefererConfig.setProtocolConfig(protocolConfig);
		helloRefererConfig.setRegistryConfig(registryConfig);
		HelloService helloService = helloRefererConfig.getRef();

		RefererConfig<UserService> userRefererConfig = new RefererConfig<>();
		userRefererConfig.setInterfaceClass(UserService.class);
		userRefererConfig.setProtocolConfig(protocolConfig);
		userRefererConfig.setRegistryConfig(registryConfig);
		UserService userService = userRefererConfig.getRef();

		userService.getNameById("xcode");

		helloService.hello("xcode");

		ExecutorService pool = Executors.newFixedThreadPool(12);

		int cnt = 10_000_000;
		CountDownLatch latch = new CountDownLatch(cnt);

		long start = System.currentTimeMillis();
		for (int i = 0; i < cnt; i++) {
			int finalI = i;
			pool.submit(() -> {
				String result = helloService.hello("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
				//System.out.println("get result:" + result);
				System.out.println(finalI);

//				String result2 = helloService.hello("rpc again");
//				System.out.println("----" + result2);
//
//				String coderbee = userService.getNameById("coderbee");
//				System.out.println(coderbee);
//
//				String xcode = userService.getNameById("xcode");
//				System.out.println(xcode);

				latch.countDown();
			});
		}

		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("used time millis:" + (end - start));
		pool.shutdown();
	}

}
