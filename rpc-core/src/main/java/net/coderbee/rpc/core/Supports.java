package net.coderbee.rpc.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 支持类。
 *
 * @author coderbee on 2017/7/3.
 */
public class Supports {
	private static Logger logger = LoggerFactory.getLogger(Supports.class);

	private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

	public static <T> void delayDestroyRefers(List<Refer<T>> refers) {
		if (refers == null || refers.isEmpty()) {
			return;
		}

		scheduledExecutorService.schedule(() -> {
			for (Refer<T> refer : refers) {
				try {
					refer.destroy();
				} catch (Exception ex) {
					logger.error("destry refer " + refer.getUrl().getUri() + " error", ex);
				}
			}
		}, 1, TimeUnit.MINUTES);
	}

}
