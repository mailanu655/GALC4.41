package com.honda.galc.client.ui.event;

import java.util.List;

import com.honda.galc.client.ui.IEvent;
import com.honda.galc.entity.qi.QiDefectResult;

public class ProductDefectUpdatedEvent implements IEvent {
	private List<QiDefectResult> defectResults;
	
	public ProductDefectUpdatedEvent(List<QiDefectResult> defectResults) {
		this.defectResults = defectResults;
	}
	
	public List<QiDefectResult> getDefectResults() {
		return this.defectResults;
	}
	
	public void setDefectResults(List<QiDefectResult> defectResults) {
		this.defectResults = defectResults;
	}
}
