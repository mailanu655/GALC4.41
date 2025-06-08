package com.honda.galc.client.common.component;

import com.honda.galc.client.datacollection.ClientContext;

public class LotCtrInformation {
	private static volatile LotCtrInformation INSTANCE;

	private LotCtrInformation(ClientContext context) 
	{
		super();
		
	}

	public static LotCtrInformation getInstance(ClientContext context) {
		synchronized(LotCtrInformation.class) {
			if (INSTANCE == null)
				INSTANCE = new LotCtrInformation(context);
		}
		return INSTANCE;
	}

}
