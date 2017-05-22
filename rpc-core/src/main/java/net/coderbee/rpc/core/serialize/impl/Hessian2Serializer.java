package net.coderbee.rpc.core.serialize.impl;

import com.caucho.hessian.io.Hessian2StreamingOutput;
import net.coderbee.rpc.core.serialize.Serializer;

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

}
