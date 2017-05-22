package net.coderbee.util.concurrent;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by coderbee on 2017/4/21.
 */
class TraceableTask {
	private Map<String, String> context;

	public TraceableTask() {
		context = MDC.getCopyOfContextMap();
	}

	protected void clearContext() {
		MDC.clear();
	}

	void initContext() {
		if (context == null || context.isEmpty()) {
			init();
		} else {
			MDC.setContextMap(context);
		}
	}

	private void init() {
		Map<String, String> context = new HashMap<String, String>();
		String traceID = UUID.randomUUID().toString().replace("-", "");
		context.put("trace_id", traceID);

		MDC.setContextMap(context);
	}

}
