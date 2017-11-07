package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.RpcException;
import net.coderbee.rpc.core.URL;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author coderbee on 2017/11/5.
 */
public abstract class AbstractPoolClient extends AbstractClient {
	protected GenericObjectPool<Channel> pool;
	protected PooledObjectFactory<Channel> factory;

	public AbstractPoolClient(URL url) {
		super(url);
	}

	protected void initChannelPool() {
		factory = createPoolableObjectFactory();
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(5);
		pool = new GenericObjectPool(factory, poolConfig);
	}

	protected abstract PooledObjectFactory createPoolableObjectFactory();

	protected void returnObject(Channel channel) {
		if (channel != null) {
			try {
				pool.returnObject(channel);
			} catch (Exception e) {
				logger.error(getClass().getSimpleName() + " return channel get error, url:" + url.getUri(), e);
			}
		}
	}

	protected Channel borrowObject() throws Exception {
		Channel channel = pool.borrowObject();
		if (channel != null || channel.isAvailable()) {
			return channel;
		}

		invalidateObject(channel);

		String errorMsg = this.getClass().getSimpleName() + " borrowObject Error: url=" + url.getUri();
		logger.error(errorMsg);
		throw new RpcException(errorMsg);
	}

	protected void invalidateObject(Channel channel) {
		if (channel != null) {
			try {
				pool.invalidateObject(channel);
			} catch (Exception e) {
				logger.error(getClass().getSimpleName() + " invalidate channel get error, url:" + url.getUri(), e);
			}
		}
	}

}
