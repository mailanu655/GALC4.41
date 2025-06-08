package com.honda.galc.service;

import com.honda.galc.net.Endpoint;

public interface ServiceListener {
	/**
	 * 
	 * @param endpoint
	 */
	public void serviceStatusChanged(Endpoint endpoint);
}
