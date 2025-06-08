package com.honda.galc.client.schedule;

import java.util.Map;

import javafx.collections.ListChangeListener;

/**
 * <h3>Class description</h3>
 * Schedule Client Table LastProductSelection Class Description
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 */

public class ScheduleClientTableLastProductSelection<T> extends ScheduleClientTable<T> {

	public ScheduleClientTableLastProductSelection(
			ScheduleClientController controller,
			Map<String, Object> lastProductSelectionProperties) {
		super(controller, lastProductSelectionProperties);
	}
	
	/*
	 * Customized Listener for Last Product Selection Table
	 */
	@Override
	protected void addListeners() {

		getTable().getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {
					@Override
					public void onChanged(Change<? extends Integer> change) {
						boolean disable = !(change.getList().size() > 0);
						controller.getLastLotSelectionScreen().getSendToOnButton().setDisable(disable);
						controller.getLastLotSelectionScreen().getChangeToSentButton().setDisable(disable);
					}

				});
	}

	

}
