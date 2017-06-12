package net.coderbee.rpc.core.serialize;

import net.coderbee.rpc.core.extension.Spi;

import java.io.IOException;

/**
 * Created by coderbee on 2017/5/21.
 */
@Spi
public interface Serializer {

	byte[] serialize(Object obj) throws IOException;

	<T> Object deSerialize(byte[] data, Class<T> type) throws IOException;

}
