package net.coderbee.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.coderbee.rpc.core.serialize.Serializer;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcEncoder extends MessageToByteEncoder {
	private Serializer serializer;
	private Class<?> genericClass;

	public RpcEncoder(Serializer serializer, Class<?> genericClass) {
		this.serializer = serializer;
		this.genericClass = genericClass;
	}

	protected void encode(ChannelHandlerContext ctx, Object o, ByteBuf buf) throws Exception {
		if (genericClass.isInstance(o)) {
			byte[] data = serializer.serialize(o);
			buf.writeInt(data.length);
			buf.writeBytes(data);
		}
	}
}
