package net.coderbee.rpc.core.serialize.impl;

import com.caucho.hessian.io.Hessian2StreamingInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by coderbee on 2017/5/21.
 */
public class Hessian2DeSerializer implements DeSerializer {

	@Override
	public <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(bytes);
		Hessian2StreamingInput hessian2Input = new Hessian2StreamingInput(buf);
		Object o = hessian2Input.readObject();
		return (T) o;
	}

}
