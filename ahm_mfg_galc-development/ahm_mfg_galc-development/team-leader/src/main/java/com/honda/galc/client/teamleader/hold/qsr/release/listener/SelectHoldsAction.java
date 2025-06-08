package com.honda.galc.client.teamleader.hold.qsr.release.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrAction;
import com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel;
import com.honda.galc.client.teamleader.hold.qsr.release.vinseq.ReleaseBySeqInputPanel;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.HoldResult;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SelectHoldsAction</code> is ... .
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
public class SelectHoldsAction extends QsrAction<ReleasePanel> implements ActionListener {

	public SelectHoldsAction(ReleasePanel parentPanel) {
		super(parentPanel);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void executeActionPerformed(ActionEvent e) {
		
		getView().getProductPanel().clearSelection();
		Map<Object, BaseProduct> productMap = new HashMap<Object, BaseProduct>();
		List<HoldResult> holdResults = new ArrayList<HoldResult>();
		
		Division divison = getView().getDivision();
		ProductType productType = this.getView().getProductType();
		Qsr qsr = (Qsr) getView().getInputPanel().getQsrComboBox().getSelectedItem();
		
		if(getView().getInputPanel() instanceof ReleaseBySeqInputPanel) {
			
			ReleaseBySeqInputPanel inputPanel = (ReleaseBySeqInputPanel)getView().getInputPanel();
			
			HoldAccessType holdAccessType = (HoldAccessType)inputPanel.getHoldTypeComboBox().getSelectedItem();
			
			String productId = inputPanel.getProductInput().getText();
			
			String startSeq = inputPanel.getStartVinSeqInput().getText();
			startSeq = StringUtils.isEmpty(startSeq)?startSeq:StringUtils.leftPad(startSeq, 6, "0");
			
			String endSeq = inputPanel.getEndVinSeqInput().getText();
			endSeq = StringUtils.isEmpty(endSeq)?endSeq:StringUtils.leftPad(endSeq, 6, "0");
			
			Date startTime = inputPanel.getStartTime();
			
			Date endTime = inputPanel.getEndTime();
			
			Timestamp start = startTime!= null?new Timestamp(startTime.getTime()):null;
			Timestamp end = endTime!= null?new Timestamp(endTime.getTime()):null;
			
			int qsrId = qsr != null?qsr.getId():0;
			
			if(qsr == null && StringUtils.isEmpty(productId) && StringUtils.isEmpty(startSeq) && StringUtils.isEmpty(endSeq) && startTime == null && endTime==null){
				return;
			}
			List<String> yearsList = filterWildCard(CommonUtil.objectArrayToStringList(inputPanel.getModelYearListElement().getComponent().getSelectedValues()));
			List<String> codesList = filterWildCard(CommonUtil.objectArrayToStringList(inputPanel.getModelCodeListElement().getComponent().getSelectedValues()));
			List<String> typesList = filterWildCard(CommonUtil.objectArrayToStringList(inputPanel.getModelTypeListElement().getComponent().getSelectedValues()));
				
			if(productType.equals(ProductType.FRAME)) {
					String holdAccessTypeId = holdAccessType != null?holdAccessType.getId().getTypeId():"";
					long count = getHoldResultDao().findCountByRange(productId,start, end, startSeq, endSeq,holdAccessTypeId , qsrId);
					if(handleResultSetSize(count)){
						holdResults = getHoldResultDao().findAllByRange(productId,start, end, startSeq, endSeq, holdAccessTypeId, qsrId);
						if(holdResults.size() > 0) {
							List<Frame> productList = ServiceFactory.getDao(FrameDao.class).findProductsBySpecCode(getProductIds(holdResults),yearsList, codesList, typesList);
							for (Frame product : productList) {
								productMap.put(product.getId(), product);
							}
						}
					}else {
						return;
					}
			}else {
				if(!StringUtils.isEmpty(productId)) {
					if(qsrId == 0) {
						holdResults = getHoldResultDao().findAllByProductId(productId.trim());
					}else {
						holdResults = getHoldResultDao().findAllByProductIdAndQsr(productId.trim(),qsrId);
					}
				}else {
					holdResults = getHoldResultDao().findAllByQsrId(qsr.getId());
				}
				List<BaseProduct> productList = (List<BaseProduct>) getProductDao(getView().getProductType()).findProducts(getProductIds(holdResults), 0, holdResults.size());
				for (BaseProduct product : productList) {
					productMap.put(product.getId(), product);
				}
			}	
		} else if (divison == null || qsr == null || qsr.getId() == null) { 
			return;
		} else {	
			holdResults = getHoldResultDao().findAllByQsrId(qsr.getId());
			List<BaseProduct> productList = (List<BaseProduct>) getProductDao(getView().getProductType()).findAllByQsrId(qsr.getId());
			for (BaseProduct product : productList) {
				productMap.put(product.getId(), product);
			}
		}
		
		if(Config.isDisableProductIdCheck(productType.toString())){
			for(HoldResult holdResult: holdResults) {
				String productId = holdResult.getId().getProductId();
				if (productMap.containsKey(productId)) continue;
				BaseProduct product = ProductTypeUtil.createProduct(productType.toString(), productId);
				productMap.put(productId, product);
			}
		}
		
		holdResults = filterHoldResults(productMap.keySet(),holdResults);
		List<Map<String, Object>> data = this.prepareReleaseRecords(new ArrayList<BaseProduct>(productMap.values()), holdResults);
		
		boolean allHoldsReleased = true;
		for (HoldResult holdResult : holdResults) {
			if (holdResult.getReleaseFlag() != 1) {
				allHoldsReleased = false;
				break;
			}	
		}
		getView().getProductPanel().reloadData(data);
		
		if (getView().getProductPanel().getTable().getRowCount() == 0) {
			String msg = "No Results found for select criteria.";
			this.getMainWindow().setMessage(msg);
		}

		if (qsr != null) {
			if (!isNoActiveHolds(qsr.getId())) return;
			if (holdResults.isEmpty() ) {
				int retCode = JOptionPane.showConfirmDialog(getView(), "There are no holds for this QSR\nDo you want to complete this QSR", "No Holds on QSR", JOptionPane.OK_CANCEL_OPTION);
				if (retCode == JOptionPane.OK_OPTION) completeQsr(qsr, "No holds on qsr");
			} else if (allHoldsReleased) {
				int retCode = JOptionPane.showConfirmDialog(getView(), "All holds for this QSR are released.\nDo you want to complete this QSR ?", "All Holds Released", JOptionPane.OK_CANCEL_OPTION);
				if (retCode == JOptionPane.OK_OPTION) completeQsr(qsr, "All holds released");
			}
		} else {
			Map<Integer,Qsr> unreleasedQsrMap = new HashMap<Integer,Qsr>();
			
			Set<Integer> qsrIds = holdResults.stream().map(HoldResult::getQsrId).collect(Collectors.toSet());
			for (Integer qsrId : qsrIds) {
				Qsr holdResultQsr = getQsrDao().findByKey(qsrId);
				if (	holdResultQsr != null &&
						holdResultQsr.getStatus() == QsrStatus.ACTIVE.getIntValue() &&
						!unreleasedQsrMap.containsKey(holdResultQsr.getId()) &&
						isNoActiveHolds(qsrId)) {
					unreleasedQsrMap.put(qsrId, holdResultQsr);
				}
			}
			
			int completeAllQsrs = JOptionPane.NO_OPTION;
			String warnMsg = "There are no active holds for QSR";
			
			if (unreleasedQsrMap.isEmpty()) {
				return;
			} else if (unreleasedQsrMap.entrySet().size() > 1) {
				StringBuilder msg = new StringBuilder(warnMsg + "s:\n");
				for (Qsr unreleasedQsr : unreleasedQsrMap.values()) {
					msg.append(unreleasedQsr.getName() + "-" + unreleasedQsr.getDescription() + "\n");
				}
				msg.append("\nWould you like to complete all of them?");
				completeAllQsrs = JOptionPane.showConfirmDialog(null, this.getScrollPane(msg.toString()), "Inactive QSRs", JOptionPane.YES_NO_CANCEL_OPTION);
			} 
			
			StringBuilder confirmMsg = new StringBuilder();
			for (Qsr unreleasedQsr : unreleasedQsrMap.values()) {
				if (completeAllQsrs == JOptionPane.NO_OPTION) {
					StringBuilder msg = new StringBuilder(warnMsg + ":\n");
					msg.append(unreleasedQsr.getName() + "-" + unreleasedQsr.getDescription() + "\n");
					msg.append("\nWould you like to complete it?");
					int buttonOption = unreleasedQsrMap.values().size() > 1 ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION;
					int completeCurrentQsr = JOptionPane.showConfirmDialog(getView(), this.getScrollPane(msg.toString()), "Inactive QSR", buttonOption);
					if (completeCurrentQsr == JOptionPane.NO_OPTION || completeCurrentQsr == JOptionPane.CLOSED_OPTION) continue;
					if (completeCurrentQsr == JOptionPane.CANCEL_OPTION) break;
				}
				completeQsr(unreleasedQsrMap.get(unreleasedQsr.getId()), "QSR Completed", completeAllQsrs == JOptionPane.NO_OPTION);
				getView().getInputPanel().getQsrComboBox().removeItem(unreleasedQsr);
				confirmMsg.append(unreleasedQsr.getName() + "-" + unreleasedQsr.getDescription() + " - " + getQsrStatusLabel(unreleasedQsr.getStatus()) + "\n");
			}
			if (completeAllQsrs == JOptionPane.YES_OPTION && confirmMsg.length() > 0) {
				JOptionPane.showMessageDialog(null, this.getScrollPane(confirmMsg.toString()), "QSRs Completed", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}


	private boolean isNoActiveHolds(int qsrId) {
		return (getHoldResultDao().unreleasedCountByQsr(qsrId) > 0) ? false : true;
	}

	protected void completeQsr(Qsr qsr, String comment) {
		completeQsr(qsr, comment, true);
	}
			
	protected void completeQsr(Qsr qsr, String comment, Boolean showDialog) {
		Division division = getView().getDivision();
		ProductType productType = getView().getProductType();

		List<HoldParm> holdParams = getHoldParamDao().findAllByQsrId(qsr.getId());
		if (holdParams != null && holdParams.size() > 0) {
			for (HoldParm hp : holdParams) {
				if (hp.getReleaseFlag() != 1) {
					String msg = "This QSR is associated to active Die Hold. \nPlease disactivate Die Hold first and try again.";
					JOptionPane.showMessageDialog(getView(), msg, "Active Die Hold", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		}

		qsr.setComment(comment);
		qsr.setStatus(QsrStatus.COMPLETED.getIntValue());
		qsr.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		getQsrDao().update(qsr);

		Qsr qsrResult = getQsrDao().findByKey(qsr.getId());
		StringBuilder sb = new StringBuilder();
		sb.append("Request processed succesfully, QSR Request Code : ").append(qsrResult.getName());
		sb.append("\nQSR Status - ").append(getQsrStatusLabel(qsrResult.getStatus()));
		if (showDialog) JOptionPane.showMessageDialog(getView(), sb.toString());
		
		if(	getView().getInputPanel() instanceof ReleaseBySeqInputPanel && 
			getView().getInputPanel().getQsrComboBox().getSelectedItem() == null) return;

		getView().getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
		getView().getInputPanel().getDepartmentComboBox().setSelectedItem(division);
		getView().getInputPanel().getProductTypeComboBox().setSelectedItem(productType);
		getView().getInputPanel().getQsrComboBox().setSelectedItem(qsrResult);
	}
	
	protected String getQsrStatusLabel(Integer qsrStatus) {
		QsrStatus status = QsrStatus.getByIntValue(qsrStatus);
		if (status != null) return status.getLabel();
		return "";
	}

	// === get/set === //
	@SuppressWarnings("unchecked")
	protected DiecastDao<? extends DieCast> getProductDao(BaseProduct entity) {
		String productName = entity.getClass().getSimpleName().toUpperCase();
		ProductDao<? extends BaseProduct> dao = ProductTypeUtil.getProductDao(productName);
		return (DiecastDao<? extends DieCast>) dao;
	}
	

	private List<HoldResult> filterHoldResults(Set<Object> productIdSet,List<HoldResult> holdResults) {
		List<HoldResult> filteredHoldResults = new ArrayList<HoldResult>();
			
				for(HoldResult holdResult:holdResults) {
					if(productIdSet.contains(holdResult.getId().getProductId())) {
						filteredHoldResults.add(holdResult);
					}
			}
		
		return filteredHoldResults;
	}
	
	private List<String> getProductIds(List<HoldResult> holdResults){
		List<String> productIds = new ArrayList<String>();
		
		for(HoldResult holdResult:holdResults) {
			productIds.add(holdResult.getId().getProductId());
		}
		return productIds;
	}
	
	private List<String> filterWildCard(List<String> mtocList){
		return mtocList.contains("*")?new ArrayList<String>():mtocList;
	}
	
	private boolean handleResultSetSize(long count) {
		int maxSize = Config.getProperty().getMaxResultsetSize();
		
		if (maxSize > 0) {
			if (count > maxSize) {
				String msg = "Resultset: %s exceeds max size : %s, please select additional criteria.";
				msg = String.format(msg, count, maxSize);
				this.getMainWindow().setErrorMessage(msg);
				return false;
			}
		}
		
		return true;
	}
}