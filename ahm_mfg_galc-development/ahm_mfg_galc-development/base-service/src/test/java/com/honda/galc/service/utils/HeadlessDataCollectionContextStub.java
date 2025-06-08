package com.honda.galc.service.utils;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;

public class HeadlessDataCollectionContextStub extends HeadlessDataCollectionContext{

	private static final long serialVersionUID = 1L;
	private ProcessPoint processPoint;

	@Override
	public ProcessPoint getProcessPoint() {
		if(processPoint == null) {
			processPoint = new ProcessPoint();
			processPoint.setProcessPointId("PP10409");
		}
		return processPoint;
	}

}
