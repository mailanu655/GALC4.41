package com.honda.galc.service;

import com.honda.galc.data.DataContainer;

public interface PaintOnLookUpService extends IService{
	public DataContainer vinLookUpBySeqNum(String seqNumber, String processPointId, String plantName);
	
	public DataContainer validateRfidAttributes(DataContainer dc, String applicationId);
}
