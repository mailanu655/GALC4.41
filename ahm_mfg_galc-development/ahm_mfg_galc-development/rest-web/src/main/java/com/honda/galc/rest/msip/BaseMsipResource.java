package com.honda.galc.rest.msip;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.rest.BaseRestResource;

/**
 * @author Subu Kathiresan
 * @date Jun 15, 2017
 */
public abstract class BaseMsipResource extends BaseRestResource {
	
	protected static final String SERVICE_SUFFIX = "Handler";
	protected static final String DTO_SUFFIX = "Dto";
	protected static final String METHOD_NAME = "execute";
	protected static final String METHOD_PREFIX = "fetch";
	
	protected Object getHandler(String interfaceName) {
		return ApplicationContextProvider.getBean(getPrefix(interfaceName) + SERVICE_SUFFIX);
	}
}
