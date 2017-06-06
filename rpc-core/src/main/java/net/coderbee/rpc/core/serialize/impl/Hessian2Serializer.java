package net.coderbee.rpc.core.serialize.impl;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;
import net.coderbee.rpc.core.serialize.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by coderbee on 2017/5/21.
 */
public class Hessian2Serializer implements Serializer {

	@Override
	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Hessian2StreamingOutput output = new Hessian2StreamingOutput(buf);
		output.writeObject(obj);
		return buf.toByteArray();
	}

	@Override
	public <T> T deSerialize(byte[] bytes, Class<T> type) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(bytes);
		Hessian2StreamingInput hessian2Input = new Hessian2StreamingInput(buf);
		Object o = hessian2Input.readObject();
		return (T) o;
	}

}
