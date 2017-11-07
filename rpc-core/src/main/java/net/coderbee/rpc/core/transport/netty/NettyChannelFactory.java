package net.coderbee.rpc.core.transport.netty;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author coderbee on 2017/11/5.
 */
public class NettyChannelFactory extends BasePooledObjectFactory<NettyChannel> {
	private NettyClient nettyClient;

	public NettyChannelFactory(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}

	@Override
	public NettyChannel create() throws Exception {
		NettyChannel nettyChannel = new NettyChannel(nettyClient);
		nettyChannel.open();
		return nettyChannel;
	}

	@Override
	public PooledObject<NettyChannel> wrap(NettyChannel channel) {
		return new DefaultPooledObject(channel);
	}

}
