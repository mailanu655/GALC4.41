package com.honda.galc.client.datacollection.view;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessPart;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>ClassicViewManager</h3>
 * <h4>
 * Classic view controller.
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
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
public class AntiTheftLabelViewManager extends ClassicViewManager{
	int iMaxVIN = Integer.parseInt(PropertyService.getProperty(context.getProcessPointId(),"MAX_PRODUCT_SN_LENGTH"));
	public AntiTheftLabelViewManager(ClientContext clientContext) {
		super(clientContext);
	}

	public void partSnNg(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		
		String strHiddenVIN = state.getCurrentInstallPart().getPartSerialNumber();
		if (null != strHiddenVIN &&strHiddenVIN.length()>iMaxVIN)
			strHiddenVIN =strHiddenVIN.substring((strHiddenVIN.length()-iMaxVIN), strHiddenVIN.length());
		renderFieldBeanNg(partSerialNumber, strHiddenVIN);
		partSerialNumber.requestFocus();
	}

	public void partSnOk(ProcessPart state) {
		UpperCaseFieldBean partSerialNumber = view.getPartSerialNumber(state.getCurrentPartIndex());
		String strHiddenVIN = state.getCurrentInstallPart().getPartSerialNumber();
		if (null != strHiddenVIN &&strHiddenVIN.length()>iMaxVIN)
			strHiddenVIN =strHiddenVIN.substring((strHiddenVIN.length()-iMaxVIN), strHiddenVIN.length());
		partSerialNumber.setText(strHiddenVIN);
		partSerialNumber.setColor(ViewControlUtil.VIEW_COLOR_OK);
		ViewControlUtil.refreshObject(partSerialNumber, null, ViewControlUtil.VIEW_COLOR_OK, false);
		moveTextFieldHighlight(partSerialNumber, ViewControlUtil.VIEW_COLOR_FONT, ViewControlUtil.VIEW_COLOR_OK);
		clearMessageArea(state);
	}
	
	private void delay(long time) {
		try {
			if(time == 0) return;
			Thread.sleep(time);
		} catch (Exception e) {
			Logger.getLogger().info(e, " exception on sleep.");
		}		
	}

	public void completePartSerialNumber(ProcessPart state) {
		//add delay for part mark only
		if(state.isLastPart())
			delay(Long.parseLong(PropertyService.getProperty(context.getProcessPointId(),"ANTI_THEFT_LABEL_DELAY"))*1000);
	}
}

