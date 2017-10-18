package net.coderbee.rpc.core.transport;

public interface MessageHandler {

    Object handle(Channel channel, Object message);

}
