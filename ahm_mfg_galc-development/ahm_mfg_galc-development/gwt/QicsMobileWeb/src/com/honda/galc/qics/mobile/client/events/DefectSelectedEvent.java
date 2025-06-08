package com.honda.galc.qics.mobile.client.events;

import com.smartgwt.mobile.client.data.Record;

public class DefectSelectedEvent extends  AbstractEvent<DefectSelectedEvent, Record>{
	
		
	public DefectSelectedEvent(Record selectedRecord) {
		super( selectedRecord );
	}

}
