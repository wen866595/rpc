package net.coderbee.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author coderbee on 2017/6/24.
 */
public class NetUtil {

	public static String getLocalHost() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			return localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

}
