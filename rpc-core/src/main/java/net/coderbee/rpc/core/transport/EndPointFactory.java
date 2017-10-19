package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.extension.Spi;

/**
 * @author coderbee on 2017/6/11.
 */
@Spi
public interface EndPointFactory {

	Server createServer(URL serviceUrl, MessageHandler caller);

	Client createClient(URL url);

	void safeReleaseResources(Client client, URL url);

}
