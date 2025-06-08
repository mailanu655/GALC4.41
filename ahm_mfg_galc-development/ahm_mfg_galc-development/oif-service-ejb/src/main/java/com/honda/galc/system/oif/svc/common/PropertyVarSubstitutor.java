package com.honda.galc.system.oif.svc.common;

import java.util.Properties;

import com.honda.galc.util.AbstractVarSubstitutor;

public class PropertyVarSubstitutor extends AbstractVarSubstitutor {

	private Properties varSource;

	/**
	 * @param varSource
	 */
	public PropertyVarSubstitutor(Properties varSource) {
		super();
		this.varSource = varSource;
	}

	@Override
	protected Object getValue(String key) {
		return varSource.getProperty(key);
	}

}
