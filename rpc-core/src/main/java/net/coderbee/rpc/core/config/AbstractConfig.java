package net.coderbee.rpc.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author coderbee on 2017/6/21.
 */
public abstract class AbstractConfig {
	protected static Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

	protected List<ProtocolConfig> protocolConfigs;

	protected void collectMethodParams(Map<String, String> params, List<MethodConfig> methods) {

	}



	public List<ProtocolConfig> getProtocolConfigs() {
		return protocolConfigs;
	}

	public void setProtocolConfigs(List<ProtocolConfig> protocolConfigs) {
		this.protocolConfigs = protocolConfigs;
	}

	public void setProtocolConfig(ProtocolConfig protocolConfig) {
		this.protocolConfigs = Collections.singletonList(protocolConfig);
	}

}
