package com.honda.galc.client.teamleader.hold.qsr.release.dialog;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.vinseq.ReleaseBySeqInputPanel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ReleaseAction</code> is ...
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
 * <TD>Jan 15, 2010</TD>
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
public class ReleaseAction extends QsrAction<ReleasePanel> implements ActionListener {

	private static final long serialVersionUID = 1L;

	private ReleaseDialog dialog;
	
	private String applicationId = getView().getApplicationId();
	
	private QsrMaintenancePropertyBean propertyBean=null;
	
	private List<BroadcastDestination> broadcastDestinations;

	public ReleaseAction(ReleaseDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
		propertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getView().getApplicationId());
	}

	@Override
	public void executeActionPerformed(ActionEvent e) {

		int productCount = getDialog().getHoldResults().size();
		StringBuilder msg = new StringBuilder();
		msg.append("Are you sure you want release ").append(productCount).append(productCount == 1 ? " Hold" : " Holds");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Release Holds", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		Qsr qsr = (Qsr) getDialog().getParentPanel().getInputPanel().getQsrComboBox().getSelectedItem();
		HoldResult releaseInput = assembleReleaseInput();

		List<HoldResult> holdResults = getDialog().getHoldResults();
		release(holdResults, releaseInput, qsr);
	}

	protected void release(List<HoldResult> holdResults, HoldResult releaseInput, Qsr qsr) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		boolean allHoldsReleased = getDialog().isQsrCompleted();
		boolean activeHoldParamExist = isActiveHoldParamExist(qsr);
		boolean updateQsr = false;
		
		if(qsr != null) {
			if (ReleaseDialog.APPROVED_TO_SHIP.equals(releaseInput.getReleaseReason())) {
				String approver = getDialog().getApproverInput().getText();
				qsr.setApproverName(approver);
				updateQsr = true;
			}
	
			String comment = getDialog().getCommentInput().getText();
			if (StringUtils.isNotBlank(comment)) {
				qsr.setComment(comment);
				updateQsr = true;
			}
	
			if (allHoldsReleased && !activeHoldParamExist) {
				qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
				updateQsr = true;
			}
		}

		for (HoldResult hr : holdResults) {
			hr.setReleaseAssociateNo(releaseInput.getReleaseAssociateNo());
			hr.setReleaseAssociateName(releaseInput.getReleaseAssociateName());
			hr.setReleaseAssociatePhone(releaseInput.getReleaseAssociatePhone());
			hr.setReleaseReason(releaseInput.getReleaseReason());
			hr.setReleaseFlag((short) 1);
			hr.setReleaseTimestamp(timestamp);
			hr.setUpdateTimestamp(timestamp);
		}

		try {
			Division division = getView().getDivision();
			ProductType productType = getView().getProductType();
			HoldAccessType holdType = getView().getHoldAccessType();

			int partitionSize = this.propertyBean.getReleasePartitionSize();
			List<List<HoldResult>> partitionedList = this.partitionHoldResults(holdResults, partitionSize);
			int currPartition = 0;
			try {
				this.getDialog().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				for (currPartition=0 ; currPartition < partitionedList.size(); currPartition++) {
					getQsrDao().updateHoldResults(productType, partitionedList.get(currPartition), null);
					Thread.sleep(2000);
				} 
			} catch (Exception e) {
				this.getLogger().error(e, e.getMessage());
				int count = 0;
				StringBuilder productIdList = new StringBuilder();
				for (int i = 0; i < currPartition; i++) {
					for (HoldResult holdResult  : partitionedList.get(i)) {
						productIdList.append("\t" + holdResult.getId().getProductId() + "\n");
						count++;
					}
				} 
				StringBuilder sb = new StringBuilder("Operation interrupted!\n" + count + " products processed sucessfully.\n");
				if (count > 0)  sb.append(productIdList);
				this.showScrollDialog(sb.toString());
			}
			
			if (updateQsr) {
				getQsrDao().updateHoldResults(productType, null, qsr);
			}
			
			if (propertyBean.isBroadcastEnabled()) {
				ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
				if (holdResults != null && holdResults.size() > 0) {
					loadBroadcastDestinations();
					for (HoldResult hr : holdResults) {
						for (BroadcastDestination destination : this.broadcastDestinations) {
							int seqNo = destination.getSequenceNumber();
							BaseProduct baseProduct = productDao.findBySn(hr.getId().getProductId());
							Logger.getLogger(applicationId).info("Broadcast Services started for " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")");
							invokeBroadcastService(baseProduct,seqNo ,hr.getReleaseReason());
						}
					}
				}
			}



			Logger.getLogger(getDialog().getParentPanel().getApplicationId()).info("Product(s) hold released. User " + getDialog().getAssociateIdInput().getText());
			getView().setCachedHoldResultInput(releaseInput);
			if(qsr != null) {
				Qsr qsrResult = getQsrDao().findByKey(qsr.getId());
				StringBuilder sb = new StringBuilder();
				sb.append("Request processed succesfully, QSR Request Code : ").append(qsrResult.getName());
				sb.append("\n").append(getDialog().getHoldResults().size());
				if (getDialog().getHoldResults().size() == 1) {
					sb.append(" Hold is released");
				} else {
					sb.append(" Holds are released");
				}
			
				
				QsrStatus status = QsrStatus.getByIntValue(qsrResult.getStatus());
				if (status != null) {
					sb.append("\nQSR Status - ").append(status.getLabel());
				}
			
				if (allHoldsReleased && activeHoldParamExist) {
					sb.append("\nActive Die Hold Exists");
				}
			

				JOptionPane.showMessageDialog(getView(), sb.toString());

				getView().setCachedQsr(qsrResult);
				getView().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
				getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
				getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
				getView().getInputPanel().getHoldTypeComboBox().setSelectedItem(holdType);
				getView().getInputPanel().getQsrComboBox().setSelectedItem(qsrResult);

			} else if (!(getView().getInputPanel() instanceof ReleaseBySeqInputPanel)) {
				getView().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
				getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
				getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
			}
			

		} finally {
			getDialog().dispose();
			getView().getInputPanel().getCommandButton().setEnabled(true);
			getView().getInputPanel().getCommandButton().doClick();
		}
	}

	protected HoldResult assembleReleaseInput() {
		HoldResult holdResult = new HoldResult();
		holdResult.setReleaseAssociateNo(getDialog().getAssociateIdInput().getText());
		holdResult.setReleaseAssociateName(getDialog().getAssociateNameInput().getText());
		holdResult.setReleaseAssociatePhone(getDialog().getPhoneInput().getText());
		holdResult.setReleaseReason((String) getDialog().getReasonInput().getSelectedItem());
		return holdResult;
	}

	protected boolean isActiveHoldParamExist(Qsr qsr) {
		if(qsr != null) {
			List<HoldParm> holdParams = getHoldParamDao().findAllByQsrId(qsr.getId());
			if (holdParams != null && holdParams.size() > 0) {
				for (HoldParm hp : holdParams) {
					if (hp.getReleaseFlag() != 1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// === get/set ===//
	public ReleaseDialog getDialog() {
		return dialog;
	}
	
	private void invokeBroadcastService(BaseProduct product, int seqNo, String aReason){
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT, product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		dc.put(DataContainerTag.RELEASE_COMMENT, aReason);
		dc.put(DataContainerTag.ASSOCIATE_NO, getMainWindow().getUserId());
		getService(BroadcastService.class).broadcast(applicationId, seqNo, dc);
	}
	
	//load all broad cast destinations 
	//(device has auto enabled false to prevent duplicate broadcast on DataCollection complete)
	private void loadBroadcastDestinations(){
		 this.broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(applicationId);
	}
	
}
