package com.honda.galc.client.schedule;

import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.entity.product.PreProductionLot;

/**
 * <h3>Class description</h3>
 * Schedule Client Table PreProductionLot Class Description
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

public class ScheduleClientTablePreProductionLot extends ScheduleClientTable<PreProductionLot> {

	public ScheduleClientTablePreProductionLot(ScheduleClientController controller,
			Map<String, Object> properties) {
		super(controller,properties);
	}
	
	/*
	 * Customized Listener for Pre Production Lot Tables
	 */
	@Override
	protected void addListeners() {
		
		super.addListeners();
		getTable()
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						new ChangeListener<MultiValueObject<PreProductionLot>>() {

							@Override
							public void changed(
									ObservableValue<? extends MultiValueObject<PreProductionLot>> arg0,
									MultiValueObject<PreProductionLot> arg1,
									MultiValueObject<PreProductionLot> arg2) {
								
								controller
										.selectionChanged(ScheduleClientTablePreProductionLot.this);

							}

						});
	}
	
}
