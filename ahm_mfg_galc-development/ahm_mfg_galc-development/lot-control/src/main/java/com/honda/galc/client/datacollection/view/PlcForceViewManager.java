package com.honda.galc.client.datacollection.view;

import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessTorque;
import com.honda.galc.entity.product.Measurement;
/**
 * <h3>ViewManager</h3>
 * <h4>
 * Common Data Collection view controller implementation.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Mar.5, 2019</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */

public class PlcForceViewManager extends ViewManager {

	public PlcForceViewManager(ClientContext clientContext) {
		super(clientContext);
	}
	

	@Override
	public void torqueNg(ProcessTorque state) {
		List<Measurement> measurements = state.getCurrentInstallPart().getMeasurements();
		for(int i = 0; i < state.getCurrentPartTorqueCount(); i++) {
			String mValue = (measurements.size() >= i) ? Double.toString(measurements.get(i).getMeasurementValue()) : "0.0";
			boolean mStatus = (measurements.size() >= i) ? (measurements.get(i).getMeasurementStatusId() ==1) : false;
			
			//viewProperty.isShowTorqueAsValue()
			ViewControlUtil.refreshObject(view.getTorqueValueTextField(i),
					mValue, mStatus? ViewControlUtil.VIEW_COLOR_OK : ViewControlUtil.VIEW_COLOR_NG, false);
			
			view.getTorqueValueTextField(i).setVisible(true);
			
		}
		
	}

}
