package com.honda.galc.client.teamleader.hold.qsr.put.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.HoldPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.HoldResultId;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectDescriptionId;
import com.honda.galc.entity.qics.DefectType;
import com.honda.galc.entity.qics.InspectionPartLocation;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ScrapAction</code> is ... .
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
public class MassScrapAction extends BaseListener<HoldPanel> implements ActionListener {

	private static final String SCRAP = "SCRAP";
	private MassScrapDialog dialog;

	public MassScrapAction(MassScrapDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
	}

	@Override
	protected void executeActionPerformed(ActionEvent ae) {

		int productCount = getDialog().getProducts().size();

		StringBuilder msg = new StringBuilder();
		msg.append("Are you sure you want scrap ").append(productCount).append(productCount == 1 ? " Product" : " Products");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Scrap Products", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		Qsr qsr = assembleQsr();

		try {
			Division division = (Division) getView().getInputPanel().getDepartmentComboBox().getSelectedItem();

			ProcessPoint processPoint = (ProcessPoint) getDialog().getProcessPointPanel().getProcessPointComboBox().getComponent().getSelectedItem();

			DefectDescription defectDescriptionInput = assembleDefectDescriptionInput();

			ProductType productType = getView().getProductType();
			getQsrDao().massScrapProducts(productType, qsr, assembleScrapResults(), defectDescriptionInput, processPoint);
			Logger.getLogger(getDialog().getParentPanel().getApplicationId()).info("Product(s) scrapped. User " + getDialog().getAssociateIdInput().getText());
			msg = new StringBuilder();
			msg.append("Request processed succesfully");
			msg.append("\n" + getDialog().getProducts().size()).append(" Products scrapped");
			JOptionPane.showMessageDialog(getView(), msg);

			getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
			getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
		} finally {
			getDialog().dispose();
		}
	}

	protected List<HoldResult> assembleScrapResults() {

		List<HoldResult> holdResults = new ArrayList<HoldResult>();


		Division division = getDialog().getParentPanel().getDivision();
		DailyDepartmentSchedule schedule = getDailyDepartmentScheduleDao().find(division.getDivisionId(), new Timestamp(System.currentTimeMillis()));
		Date productionDate = null;
		if (schedule != null) {
			productionDate = schedule.getId().getProductionDate();
		}

		for (BaseProduct bp : getDialog().getProducts()) {
			HoldResult holdResult = new HoldResult();
			holdResult.setId(new HoldResultId(bp.getProductId(), 0));
			holdResult.setHoldReason((String)getDialog().getReasonInput().getSelectedItem());
			holdResult.setHoldAssociateNo(getDialog().getAssociateIdInput().getText());
			holdResult.setHoldAssociateName(getDialog().getAssociateNameInput().getText());
			holdResult.setHoldAssociatePhone(getDialog().getPhoneInput().getText());
			holdResult.setProductionDate(productionDate);

			holdResult.setReleaseAssociateNo(getDialog().getAssociateIdInput().getText());
			holdResult.setReleaseAssociateName(getDialog().getAssociateNameInput().getText());
			holdResult.setReleaseAssociatePhone(getDialog().getPhoneInput().getText());
			holdResult.setReleaseFlag((short) 1);
			holdResult.setReleaseReason((String)getDialog().getReasonInput().getSelectedItem());

			holdResults.add(holdResult);
		}
		return holdResults;
	}


	protected DefectDescription assembleDefectDescriptionInput() {
		InspectionPartLocation location = getDialog().getDefectSelectionPanel().getLocationPane().getSelectedItem();
		DefectType defect = getDialog().getDefectSelectionPanel().getDefectPane().getSelectedItem();
		DefectDescription defectDescription = new DefectDescription();
		defectDescription.setId(new DefectDescriptionId());
		if (location == null) return defectDescription;
		defectDescription.getId().setInspectionPartLocationName(location.getInspectionPartLocationName());
		defectDescription.getId().setDefectTypeName(defect.getDefectTypeName());
		return defectDescription;
	}

	public MassScrapDialog getDialog() {
		return dialog;
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
	
	protected Qsr assembleQsr() {
		Division division = getDialog().getParentPanel().getDivision();
		ProductType productType = getView().getProductType();

		Qsr qsr = new Qsr();
		qsr.setProcessLocation(division.getDivisionId());
		qsr.setProductType(productType.name());
		qsr.setDescription(SCRAP);
		qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
		String comment = getDialog().getCommentInput().getText();
		if (StringUtils.isNotBlank(comment)) {
			qsr.setComment(comment);
		}
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return qsr;
	}
	
}
