package net.coderbee.rpc.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.coderbee.rpc.core.serialize.Serializer;

import java.util.List;

/**
 * Created by coderbee on 2017/5/20.
 */
public class RpcDecoder extends ByteToMessageDecoder {
	private Serializer serializer;
	private Class<?> genericClass;

	public RpcDecoder(Serializer serializer, Class<?> genericClass) {
		this.serializer = serializer;
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
		if (buf.readableBytes() < 4) {
			return;
		}

		buf.markReaderIndex();
		int dataLength = buf.readInt();
		if (dataLength < 0) {
			ctx.close();
		}

		if (buf.readableBytes() < dataLength) {
			buf.resetReaderIndex();
			return;
		}

		byte[] data = new byte[dataLength];
		buf.readBytes(data);

		Object obj = serializer.deSerialize(data, genericClass);
		list.add(obj);
	}
}
