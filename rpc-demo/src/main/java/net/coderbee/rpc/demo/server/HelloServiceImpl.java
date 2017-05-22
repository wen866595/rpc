package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.RpcService;
import net.coderbee.rpc.demo.HelloService;
import org.springframework.stereotype.Component;

/**
 * Created by coderbee on 2017/5/20.
 */
@RpcService(clazz = HelloService.class)
@Component
public class HelloServiceImpl implements HelloService {

	public String hello(String name) {
		return "hellow " + name;
	}

}
