package com.honda.galc.data.cache;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Aug 21, 2014
 */
public class ResultsCacheItem {
	
	String dispatcherUrl = "";
	String serviceName = "";
	String methodName = "save";
	Object[] parameters = null;
	
	public ResultsCacheItem(String dispatcherUrl, String serviceName, Object...parameters) {
		this.dispatcherUrl = dispatcherUrl;
		this.serviceName = serviceName;
		this.parameters = parameters;
	}
	
	public ResultsCacheItem(String dispatcherUrl, String serviceName, String methodName, Object...parameters) {
		this(dispatcherUrl, serviceName, parameters);
		this.methodName = methodName;
	}
	
	public String getDispatcherUrl() {
		return dispatcherUrl;
	}
	
	public void setDispatcherUrl(String dispatcherUrl) {
		this.dispatcherUrl = dispatcherUrl;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
	
	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}
	
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
