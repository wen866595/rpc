package net.coderbee.rpc.core.transport;

import net.coderbee.rpc.core.URL;

import java.net.InetSocketAddress;

public interface Channel {

    InetSocketAddress getLocalAddress();

    InetSocketAddress getRemoteAddress();

    boolean open();

    void close();

    void close(int timeout);

    boolean isClosed();

    boolean isAvailable();

    URL getUrl();

}
