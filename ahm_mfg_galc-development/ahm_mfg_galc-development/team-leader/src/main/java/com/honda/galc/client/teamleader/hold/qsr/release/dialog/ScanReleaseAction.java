package com.honda.galc.client.teamleader.hold.qsr.release.dialog;

import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
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
 * <code>ScanReleaseAction</code> is ...
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
public class ScanReleaseAction extends QsrAction<ReleasePanel> implements ActionListener {

	private ScanReleaseDialog dialog;
	private List<BroadcastDestination> broadcastDestinations;
	private QsrMaintenancePropertyBean propertyBean=null;

	public ScanReleaseAction(ScanReleaseDialog dialog) {
		super(dialog.getParentPanel());
		this.dialog = dialog;
		this.propertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getView().getApplicationId());
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

		HoldResult releaseInput = assembleReleaseInput();

		List<HoldResult> holdResults = getDialog().getHoldResults();
		release(holdResults, releaseInput);
	}

	protected void release(List<HoldResult> holdResults, HoldResult releaseInput) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		Set<Integer> qsrSet = new HashSet<Integer>();
		for (HoldResult hr : holdResults) {
			hr.setReleaseAssociateNo(releaseInput.getReleaseAssociateNo());
			hr.setReleaseAssociateName(releaseInput.getReleaseAssociateName());
			hr.setReleaseAssociatePhone(releaseInput.getReleaseAssociatePhone());
			hr.setReleaseReason(releaseInput.getReleaseReason());
			hr.setReleaseFlag((short) 1);
			hr.setReleaseTimestamp(timestamp);
			hr.setUpdateTimestamp(timestamp);
			
			qsrSet.add(hr.getQsrId());
		}

		try {
			Division division = getView().getDivision();
			ProductType productType = getView().getProductType();

		
			int partitionSize = this.propertyBean.getReleasePartitionSize();
			List<List<HoldResult>> partitionedList = this.partitionHoldResults(holdResults, partitionSize);
			int currPartition = 0;
			try {
				this.getDialog().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				for (currPartition=0 ; currPartition < partitionedList.size(); currPartition++) {
					getQsrDao().updateHoldResults(productType, partitionedList.get(currPartition), null);
					Thread.sleep(2000);
				} 
			} catch (InterruptedException e) {
				e.printStackTrace();
				int count = 0;
				StringBuilder productIdList = new StringBuilder();
				for (int i = 0; i < currPartition; i++) {
					for (HoldResult holdResult : partitionedList.get(i)) {
						productIdList.append("\t" + holdResult.getId().getProductId() + "\n");
			            count++;
					}
				}
				StringBuilder sb = new StringBuilder("Operation interrupted!\n" + count + " products processed sucessfully.\n");
				if (count > 0) sb.append(productIdList);
				this.showScrollDialog(sb.toString());
			}

			for (int qsrid : qsrSet) {
				if ( qsrid == 0 ) continue;
				Qsr qsr = getQsrDao().findByKey(qsrid);
				if (qsr != null) {
					if (!isActiveHoldParamExist(qsr) && isAllQsrCompleted(holdResults,qsrid)) {
						qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
						qsr.setUpdateTimestamp(timestamp);
						getQsrDao().update(qsr);
					}
				}
			}
			
			if (propertyBean.isBroadcastEnabled()) {
				ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
				if (holdResults != null && holdResults.size() > 0) {
					loadBroadcastDestinations();
					for (HoldResult hr : holdResults) {
						for (BroadcastDestination destination : this.broadcastDestinations) {
							int seqNo = destination.getSequenceNumber();
							BaseProduct baseProduct = productDao.findBySn(hr.getId().getProductId());
							Logger.getLogger(getView().getApplicationId()).info("Broadcast Services started for " + destination.getDestinationId() + "(" + destination.getDestinationTypeName() + ")");
							invokeBroadcastService(baseProduct,seqNo ,hr.getReleaseReason());
						}
					}
				}
			}
			
			Logger.getLogger(getDialog().getParentPanel().getApplicationId()).info("Product(s) hold released. User " + getDialog().getAssociateIdInput().getText());
			StringBuilder sb = new StringBuilder();
			sb.append("Request processed succesfully");
			sb.append("\n").append(getDialog().getHoldResults().size());
			if (getDialog().getHoldResults().size() == 1) {
				sb.append(" Hold is released");
			} else {
				sb.append(" Holds are released");
			}

			JOptionPane.showMessageDialog(getView(), sb.toString());
			getView().setCachedHoldResultInput(releaseInput);
			
			List<Map<String, Object>> tableContent = getView().getProductPanel().getItems();
			for (HoldResult hr : holdResults) {
				int i;
				for (i = 0; i < tableContent.size(); i++) {
					HoldResult result = (HoldResult)tableContent.get(i).get("holdResult");
					if (result.equals(hr)) break;
				}
				tableContent.remove(i);
			}
			getView().getProductPanel().reloadData(tableContent);
			
		} finally {
			getDialog().dispose();
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

	private void invokeBroadcastService(BaseProduct product, int seqNo, String aReason){
		
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType().toString());
		dc.put(DataContainerTag.PRODUCT, product);
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		dc.put(DataContainerTag.RELEASE_COMMENT, aReason);
		dc.put(DataContainerTag.ASSOCIATE_NO, getMainWindow().getUserId());
		getService(BroadcastService.class).broadcast(getView().getApplicationId(), seqNo, dc);
	}
	
	//load all broad cast destinations 
	//(device has auto enabled false to prevent duplicate broadcast on DataCollection complete)
	private void loadBroadcastDestinations(){
		 this.broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class)
					.findAllByProcessPointId(getView().getApplicationId());
	}

	// === get/set ===//
	public ReleaseDialog getDialog() {
		return dialog;
	}
}
