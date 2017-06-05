package net.coderbee.rpc.core.transport;

import java.net.InetAddress;

/**
 * Created by coderbee on 2017/6/4.
 */
public interface EndPoint {

	boolean open();

	void close();

	InetAddress getLoaclAddress();

	InetAddress getRemoteAddress();

}
