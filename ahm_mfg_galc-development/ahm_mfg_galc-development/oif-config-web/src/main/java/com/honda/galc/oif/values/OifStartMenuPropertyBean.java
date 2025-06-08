package com.honda.galc.oif.values;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;


@PropertyBean
public interface OifStartMenuPropertyBean extends IProperty {
	public Map<String, Boolean> tsReqFlags(Class<?> clazz);
	public Map<String, String> warnings();
}
