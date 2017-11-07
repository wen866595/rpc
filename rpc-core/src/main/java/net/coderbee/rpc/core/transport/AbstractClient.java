package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.ChannelState;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.URLParamType;
import net.coderbee.rpc.core.extension.ExtensionLoader;
import net.coderbee.rpc.core.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author coderbee on 2017/11/5.
 */
public abstract class AbstractClient implements Client {
	protected static Logger logger = LoggerFactory.getLogger(AbstractClient.class);

	protected SocketAddress localAddress;
	protected SocketAddress remoteAddress;
	protected Serializer serializer;
	protected URL url;

	protected volatile ChannelState state = ChannelState.UNINIT;

	public AbstractClient(URL url) {
		this.url = url;
		this.serializer = ExtensionLoader.getSpi(Serializer.class, url.getParameter(URLParamType.serializer));

	}

	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(SocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	@Override
	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	public URL getUrl() {
		return url;
	}
}
