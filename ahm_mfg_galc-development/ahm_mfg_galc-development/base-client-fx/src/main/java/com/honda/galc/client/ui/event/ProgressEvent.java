package com.honda.galc.client.ui.event;

import com.honda.galc.client.ui.IEvent;

/**
 * 
 * <h3>ProgressEvent</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProgressEvent description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Dec 28, 2010
 *
 */
public class ProgressEvent implements IEvent{
	private int progress;
	private String description;

	public ProgressEvent(int progress, String description) {
		super();
		this.progress = progress;
		this.description = description;
	}
	
	
	public ProgressEvent(int progress) {
		super();
		this.progress = progress;
	}


	//Getters & Setters
	public int getProgress() {
		return progress;
	}
	
	public double getPercentComplete() {
		return (double)progress / 100;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
