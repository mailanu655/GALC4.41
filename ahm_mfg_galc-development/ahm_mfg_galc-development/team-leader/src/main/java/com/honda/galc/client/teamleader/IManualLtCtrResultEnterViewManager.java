package com.honda.galc.client.teamleader;

import java.awt.event.ActionEvent;
import java.util.List;

import com.honda.galc.client.teamleader.model.PartResult;
/**
 * 
 * <h3>IManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IManualLtCtrResultEnterViewManager description </p>
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
 * Aug 18, 2010
 *
 */
public interface IManualLtCtrResultEnterViewManager {
	void confirmMeasurement();
	void subScreenClose(ActionEvent e);
	void confirmPartSn();
	void setHlStatus();
	void saveUpdate();
	void resetScreen();
	void addDefaultTorqueValues();
	void locateFocus();
	void subScreenOpen(final ManualLotControlRepairPanel view, List<PartResult> resultList);
	void confirmStringValue();
}
