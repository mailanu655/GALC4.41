package com.honda.galc.qics.mobile.client.events;

import java.util.List;

import com.honda.galc.qics.mobile.shared.entity.DefectResult;

public class DefectResultsReceivedEvent extends  AbstractEvent<DefectResultsReceivedEvent, List<DefectResult>>{
	

	public DefectResultsReceivedEvent(List<DefectResult> defectResultList) {
		super( defectResultList );
	}

}
