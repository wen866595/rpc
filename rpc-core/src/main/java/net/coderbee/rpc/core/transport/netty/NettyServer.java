package net.coderbee.rpc.core.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.coderbee.rpc.core.RpcRequest;
import net.coderbee.rpc.core.RpcResponse;
import net.coderbee.rpc.core.URL;
import net.coderbee.rpc.core.codec.RpcDecoder;
import net.coderbee.rpc.core.codec.RpcEncoder;
import net.coderbee.rpc.core.server.RpcHandler;
import net.coderbee.rpc.core.transport.EndPoint;
import net.coderbee.rpc.core.transport.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyServer implements EndPoint {
    private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private URL url;
    private MessageHandler messageHandler;
    private AtomicBoolean open = new AtomicBoolean(false);

    public NettyServer(URL url, MessageHandler messageHandler) {
        this.url = url;
        this.messageHandler = messageHandler;
    }


    @Override
    public synchronized boolean open() {
        if (open.get()) {
            logger.warn(" is already open");
            return true;
        }

        open.set(true);

        return true;
    }

    private void initBootstrap() {
        NioEventLoopGroup bootGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bootGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcDecoder(serializer, RpcRequest.class))
                                    .addLast(new RpcEncoder(serializer, RpcResponse.class))
                                    .addLast(new RpcHandler(messageHandler));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(url.getHost(), url.getPort()).sync();
            logger.debug("server started on port {}", url.getPort());
            System.out.println("server started on port " + url.getPort());

            future.channel().closeFuture().sync();

        } finally {
            workGroup.shutdownGracefully();
            bootGroup.shutdownGracefully();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public InetAddress getLoaclAddress() {
        return null;
    }

    @Override
    public InetAddress getRemoteAddress() {
        return null;
    }
}
