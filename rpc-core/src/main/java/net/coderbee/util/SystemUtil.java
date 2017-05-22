package net.coderbee.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by coderbee on 2017/4/21.
 */
public class SystemUtil {
	// 系统 ID。
	private static String sysID = "learn";

	// 系统的实例 ID，用于区分不同机器的不同运行时实例
	private static String instanceID;

	static {
		SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		instanceID = getSysID() + "@" + yyyyMMddHHmmss.format(new Date());
	}

	public static String getSysID() {
		return sysID;
	}

	public static String getInstanceID() {
		return instanceID;
	}

	public static String randomUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
}
