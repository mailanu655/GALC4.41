package com.honda.galc.system.oif.svc.common;

public interface IOifRunHistory extends IEventTaskExecutable {

	public OifErrorsCollector getOifErrorsCollector();
	
	public void setOifErrorsCollector(OifErrorsCollector oifErrorsCollector);
	
}
