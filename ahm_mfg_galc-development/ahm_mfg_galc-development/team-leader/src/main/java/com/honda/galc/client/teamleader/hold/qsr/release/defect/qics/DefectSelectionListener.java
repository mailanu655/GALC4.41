package com.honda.galc.client.teamleader.hold.qsr.release.defect.qics;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DefectSelectionListener</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 26, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class DefectSelectionListener extends BaseListener<TextDefectSelectionComponent> implements ListSelectionListener {

	public DefectSelectionListener(TextDefectSelectionComponent parentComponent) {
		super(parentComponent);
	}

	@Override
	protected void executeValueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel model = (ListSelectionModel) lse.getSource();
		if (model.isSelectionEmpty()) {
			getView().getOtherPartPane().removeData();
		} else {
			PartGroup partGroup = getView().getPartGroupPane().getSelectedItem();
			InspectionPart part = getView().getPartPane().getSelectedItem();
			InspectionPartLocation location = getView().getLocationPane().getSelectedItem();
			DefectType defect = getView().getDefectPane().getSelectedItem();
			getView().getOtherPartPane().removeData();
			if (defect == null || location == null || part == null || partGroup == null) {
				return;
			}
			getView().getOtherPartPane().reloadData(getView().getSecondaryParts(partGroup, part, location, defect));
			if (getView().getOtherPartPane().getTable().getRowCount() == 1) {
				getView().getOtherPartPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
	}
}
