package net.coderbee.rpc.core.serialize;

import java.io.IOException;

/**
 * Created by coderbee on 2017/5/21.
 */
public interface DeSerializer {

	<T> T deSerialize(byte[] bytes, Class<T> type) throws IOException;

}
