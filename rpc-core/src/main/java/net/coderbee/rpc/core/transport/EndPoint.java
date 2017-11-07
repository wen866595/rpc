package net.coderbee.rpc.core.transport;

/**
 * Created by coderbee on 2017/6/4.
 */
public interface EndPoint extends Channel {

	boolean open();

	void close();

}
