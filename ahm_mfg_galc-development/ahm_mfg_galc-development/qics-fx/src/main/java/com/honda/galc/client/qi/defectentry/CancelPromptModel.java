package com.honda.galc.client.qi.defectentry;

import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;

/**
 * 
 * <h3>ThemeMaintenanceModel Class description</h3>
 * <p> ThemeMaintenanceModel description </p>
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
 * @author L&T Infotech<br>
 * 
 *
 */ 
public class CancelPromptModel extends AbstractQiDefectProcessModel {

	public CancelPromptModel(){
		super();
	}
	
	private boolean isDefectEntered;

	public boolean isDefectEntered() {
		return isDefectEntered;
	}

	public void setDefectEntered(boolean isDefectEntered) {
		this.isDefectEntered = isDefectEntered;
	}
	
}
