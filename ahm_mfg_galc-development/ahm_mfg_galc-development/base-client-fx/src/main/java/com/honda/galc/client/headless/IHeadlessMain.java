package com.honda.galc.client.headless;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.entity.conf.Application;

/**
 * @author Subu Kathiresan
 * Oct 31, 2011
 */
public interface IHeadlessMain {
	
	public void initialize(ApplicationContext appContext, Application application);
}
