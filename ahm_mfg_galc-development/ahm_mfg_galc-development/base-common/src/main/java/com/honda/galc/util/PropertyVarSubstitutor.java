package com.honda.galc.util;

import java.util.Properties;

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
