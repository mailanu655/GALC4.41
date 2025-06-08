package com.honda.galc.client.teamleader.hold.qsr.release.defect.qics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;
import com.honda.galc.entity.qics.PartGroup;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>LocationSelectionListener</code> is ...
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
public class LocationSelectionListener extends BaseListener<TextDefectSelectionComponent> implements ListSelectionListener {

	public LocationSelectionListener(TextDefectSelectionComponent parentComponent) {
		super(parentComponent);
	}

	@Override
	protected void executeValueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel model = (ListSelectionModel) lse.getSource();
		if (model.isSelectionEmpty()) {
			getView().getDefectPane().removeData();
		} else {

			PartGroup partGroup = getView().getPartGroupPane().getSelectedItem();
			InspectionPart part = getView().getPartPane().getSelectedItem();
			InspectionPartLocation location = getView().getLocationPane().getSelectedItem();

			getView().getDefectPane().removeData();

			if (location == null || part == null || partGroup == null) {
				return;
			}

			List<DefectDescription> defectDescriptions = getDefectDescriptionDao().findAllBy(partGroup.getPartGroupName(), part.getInspectionPartName(), location.getInspectionPartLocationName());

			if (defectDescriptions == null) {
				defectDescriptions = new ArrayList<DefectDescription>();
			}

			getView().getDefectDescriptions().clear();
			getView().getDefectDescriptions().addAll(defectDescriptions);
			getView().getDefectPane().reloadData(getView().getDefects(partGroup, part, location));

			if (getView().getDefectPane().getTable().getRowCount() == 1) {
				getView().getDefectPane().getTable().getSelectionModel().setSelectionInterval(0, 0);
			}
		}
	}
}
