package net.coderbee.rpc.demo.server;

import net.coderbee.rpc.core.server.RpcServer;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by coderbee on 2017/5/22.
 */
public class RpcServerStarter implements InitializingBean {

	private RpcServer rpcServer;

	public RpcServerStarter(RpcServer rpcServer) {
		this.rpcServer = rpcServer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("to start rpc server .");
		rpcServer.start();
	}

}
