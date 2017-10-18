package net.coderbee.rpc.core.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @author coderbee on 2017/9/3.
 */
public class ConfigUtil {

	public static Map<String, Integer> parseExport(String export) {
		Map<String, Integer> map = new HashMap<>();
		String[] split = export.split(",");
		for(String string : split) {
			String[] pp = string.split(":");
			map.put(pp[0], Integer.parseInt(pp[1]));
		}
		return map;
	}

}
