package com.honda.galc.client.teamleader.hold.qsr.release.scrap;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;


import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPart;
import com.honda.galc.entity.qics.InspectionPartLocation;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScanScrapAction</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class ScanScrapAction extends BaseListener<ReleasePanel> implements ActionListener {

	private ScanScrapDialog dialog;

	public ScanScrapAction(ScanScrapDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	@Override
	protected void executeActionPerformed(ActionEvent ae) {

		int productCount = getDialog().getHoldResults().size();

		StringBuilder msg = new StringBuilder();
		msg.append("Are you sure you want scrap ").append(productCount).append(productCount == 1 ? " Product" : " Products");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Scrap Products on Hold", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		try {
			Division division = (Division) getView().getInputPanel().getDepartmentComboBox().getSelectedItem();

			ProcessPoint processPoint = (ProcessPoint) getDialog().getProcessPointComboBoxComponent().getSelectedItem();

			HoldResult releaseInput = assembleReleaseInput();
			DefectDescription defectDescriptionInput = assembleDefectDescriptionInput();

			ProductType productType = getView().getProductType();
			getQsrDao().scrapHoldProducts(productType, new Qsr(), getDialog().getHoldResults(), releaseInput, defectDescriptionInput, processPoint);
			releaseQsr(getDialog().getHoldResults());
			Logger.getLogger(getDialog().getParentPanel().getApplicationId()).info("Product(s) scrapped. User " + getDialog().getAssociateIdInput().getText());
			msg = new StringBuilder();
			msg.append("\n" + getDialog().getHoldResults().size()).append(" Product scrapped");

			JOptionPane.showMessageDialog(getView(), msg);

			getView().setCachedHoldResultInput(releaseInput);
			getDialog().setSelectionToCache();

			getView().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
			getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
			getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
		} finally {
			getDialog().dispose();
		}
		return;
	}
	
	protected void releaseQsr(List<HoldResult> holdResults) {
		Set<Integer> qsrSet = new HashSet<Integer>();
		for (HoldResult hr : holdResults) {
			qsrSet.add(hr.getQsrId());
		}
		for (int qsrid : qsrSet) {
			if ( qsrid == 0 ) continue;
			Qsr qsr = getQsrDao().findByKey(qsrid);
			if (qsr != null) {
				if (!isActiveHoldParamExist(qsr) && isAllQsrCompleted(holdResults,qsrid)) {
					qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
					qsr.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
					getQsrDao().update(qsr);
				}
			}
		}
	}
	
	protected boolean isActiveHoldParamExist(Qsr qsr) {
		List<HoldParm> holdParams = getHoldParamDao().findAllByQsrId(qsr.getId());
		if (holdParams != null && holdParams.size() > 0) {
			for (HoldParm hp : holdParams) {
				if (hp.getReleaseFlag() != 1) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected boolean isAllQsrCompleted(List<HoldResult> holdResults,int qsrid) {
		boolean in = false;
		List<HoldResult> allList = getHoldResultDao().findAllByQsrId(qsrid);
		for (HoldResult result : allList) {
			if (result.getReleaseFlag() != 1) {
				for (HoldResult selectedResult : holdResults) {
					if (selectedResult.equals(result)) in = true; 
				}
				if (!in) return false;
			}		
		}	
		return true;
	}

	protected HoldResult assembleReleaseInput() {
		HoldResult holdResult = new HoldResult();
		holdResult.setReleaseAssociateNo(getDialog().getAssociateIdInput().getText());
		holdResult.setReleaseAssociateName(getDialog().getAssociateNameInput().getText());
		holdResult.setReleaseAssociatePhone(getDialog().getPhoneInput().getText());
		holdResult.setReleaseFlag((short) 1);
		holdResult.setReleaseReason("SCRAP");
		return holdResult;
	}

	protected DefectDescription assembleDefectDescriptionInput() {
		InspectionPartLocation location = getDialog().getDefectSelectionPanel().getLocationPane().getSelectedItem();
		DefectType defect = getDialog().getDefectSelectionPanel().getDefectPane().getSelectedItem();
		InspectionPart part = getDialog().getDefectSelectionPanel().getPartPane().getSelectedItem();
		InspectionPart otherPart = getDialog().getDefectSelectionPanel().getOtherPartPane().getSelectedItem();
		DefectDescription defectDescription = new DefectDescription();
		defectDescription.setId(new DefectDescriptionId());
		if (location == null) return defectDescription;
		defectDescription.getId().setInspectionPartLocationName(location.getInspectionPartLocationName());
		defectDescription.getId().setDefectTypeName(defect.getDefectTypeName());
		defectDescription.getId().setInspectionPartName(part.getInspectionPartName());
		defectDescription.getId().setSecondaryPartName(otherPart.getInspectionPartName());
		return defectDescription;
	}

	public ScanScrapDialog getDialog() {
		return dialog;
	}

}
