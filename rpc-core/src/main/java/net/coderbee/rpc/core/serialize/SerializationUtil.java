package net.coderbee.rpc.core.serialize;

import com.caucho.hessian.io.Hessian2StreamingInput;
import com.caucho.hessian.io.Hessian2StreamingOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by coderbee on 2017/5/20.
 */
public class SerializationUtil {
	public static Object deseriallize(byte[] data, Class<?> genericClass) throws IOException {
		ByteArrayInputStream buf = new ByteArrayInputStream(data);
		Hessian2StreamingInput hessian2Input = new Hessian2StreamingInput(buf);
		Object o = hessian2Input.readObject();
		return o;
	}

	public static byte[] serialize(Object o) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Hessian2StreamingOutput output = new Hessian2StreamingOutput(buf);
		output.writeObject(o);
		return buf.toByteArray();
	}
}
