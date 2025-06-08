package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>ScheduleClientSeqController Class description</h3>
 * <p> ScheduleClientSeqController description </p>
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
 * @author Paul Chou<br>
 * Jun 25, 2014
 *
 *
 */

public class ScheduleClientControllerSeq extends ScheduleClientController {

	private String lineSeparator = System.getProperty("line.separator");  

	public ScheduleClientControllerSeq(ScheduleMainPanel panel) {
		super(panel);
	}

	@Override
	public void retrievePreProductionLots(){
		clearCurrentProductId();
		String planCode = properties.getPlanCode();
		
		upcomingLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllUpcomingLotsByPlanCode(planCode));
		processedLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllPreviousLotsByPlanCode(planCode, properties.getProcessedRowCount()));
		
		Collections.reverse(processedLots);

		List<PreProductionLot> onHoldLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllOnHoldLotsByPlanCode(planCode));
		List<PreProductionLot> currentLots = new SortedArrayList<PreProductionLot>("getSequence");
		
		Iterator<PreProductionLot> iterator = upcomingLots.iterator();
		PreProductionLot currentLot = null; 
		PreProductionLot previousLot = upcomingLots.size() == 0 ? null : upcomingLots.get(0);
		
		if(getProperties().isMoveByKdLot()) {

			while(iterator.hasNext() && previousLot != null){
				currentLot = iterator.next();
				if(currentLot.isSameKdLot(previousLot)){
					currentLots.add(currentLot);
					iterator.remove();
					previousLot = currentLot;

				} else 
					previousLot = null;
			}
		} else if(previousLot != null){
			currentLots.add(previousLot);
			upcomingLots.remove(0);
		}
		
		if(!isProcessedProductOrLot()){
			publishDataChanged(prepareData(processedLots,processedPanelProperties),SchedulingEventType.PROCESSED_ORDER_CHANGED);
		} else {
			retrieveProcessedProducts();
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct(currentLots, upcomingLots);
			}
		}
		
		publishDataChanged(prepareData(currentLots,currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
		publishDataChanged(prepareData(upcomingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
		publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);
		
		lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestampByPlanCode(properties.getPlanCode());
	}
	

	
	protected void setCurrentLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		if(checkScheduleChanged()) return;

		int[] selectedRows = tablePane.getTable().getSelectedRows();

		PreProductionLotSeqUtils lotUtils = new PreProductionLotSeqUtils(tablePane.getItems(), getProperties().isMoveByKdLot(),
				getProperties().lockFirstLot(), scheduleMainPanel);

		List<PreProductionLot> changedLots = lotUtils.setCurrentLot(selectedRows[0], selectedRows[selectedRows.length - 1]);
		
		//confirm log change
		MultiValueObject<PreProductionLot> selectedRow = tablePane.getItems().get(selectedRows[0]);
		if(!confirmSetCurrent(changedLots, selectedRow.getKeyObject().getProductionLot())) return;
		
		logger.info("set the lots to current. number of changed lots: " + changedLots.size() + " Lots: " + changedLots);
		
		if(getOnProerties().isCreateShippingSchedule()) {
			try {
				generateShippingInfo(changedLots);
			} catch (Exception e) {
				logger.error(e, "Exception to create shipping information.");
			}
		}
		
		updateCompleteLots(changedLots);

		tablePane.clearSelection();
		refreshLots();
	}

	
	private boolean confirmSetCurrent(List<PreProductionLot> changedLots, String currentLot) {
		StringBuilder sb = new StringBuilder("The following lot");
		sb.append(changedLots.size() > 1 ? "s " : " ");
		sb.append("will be set to Complete.  This action can not be undone.").append(lineSeparator);
		for(PreProductionLot lot : changedLots)
			sb.append(lot.getProductionLot()).append(lineSeparator);
		
		sb.append(lineSeparator).append("You are about to set Lot ");
		sb.append(currentLot).append(" as Current.  Do you want to continue?");
		
		return MessageDialog.confirm(scheduleMainPanel, sb.toString());
	}

	private void generateShippingInfo(List<PreProductionLot> unProcessedRowsBeforeSelectedRow) throws Exception {
		for(PreProductionLot preLot : unProcessedRowsBeforeSelectedRow){
			if(preLot.getSendStatus() == PreProductionLotSendStatus.WAITING
					|| preLot.getSendStatus() == PreProductionLotSendStatus.SENT){
				ServiceFactory.getDao(SubProductShippingDao.class).createSubProductShipping(preLot, 
						getProductType(), getOnProerties().getSequenceInterval(), getSubIds(preLot));
			}
				
		}
		
	}
	
	private String[] getSubIds(PreProductionLot preProductionLot) throws Exception {
		 String subIdStr = getBuildAttributeCache().findAttributeValue(preProductionLot.getProductSpecCode(),BuildAttributeTag.SUB_IDS, getProductType());
		 
		 if(StringUtils.isEmpty(subIdStr)){
			 logger.error("Invalid Build Attribute SUB_IDS.");
			 throw new Exception("Sub Id build attribue configuration error.");
		 }
		
		 String[] subIds = subIdStr.split(Delimiter.COMMA);
		return subIds;
	}

	protected void completeLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		PreProductionLotSeqUtils lotUtils = new  PreProductionLotSeqUtils(tablePane.getItems(), getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> changedLots = lotUtils.completeLots();   

		logger.info("complete the lots. number of changed lots: " + changedLots.size() + 
				" start lot: " + changedLots.get(0).getProductionLot() + 
				" end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateCompleteLots(changedLots);
		refreshLots();

	}
	
	protected void finishLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		PreProductionLot lot = tablePane.getSelectedItem().getKeyObject();
		if(lot != null) {
			String productionLot = lot.getProductionLot();
			int status = PreProductionLotSendStatus.DONE.getId();
			getDao(PreProductionLotDao.class).updateSendStatus(productionLot, status);
			logger.info("Completed the lot number: " + productionLot);
		}   
		refreshLots();
	}


	
	public void moveUp(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		int[] selectedRows = tablePane.getTable().getSelectedRows();
		PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(tablePane.getItems(), getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> selectedRowsList = lotUtil.getSelectedRows(selectedRows[0], selectedRows[selectedRows.length -1]);
		List<PreProductionLot> changedLots = lotUtil.moveUp(selectedRows[0], selectedRows[selectedRows.length -1],(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
		updateReplicatedLots(changedLots);	
		getDao(PreProductionLotDao.class).updateAll(changedLots);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
		updateTablePane(tablePane,lotUtil.getDataList(tablePane.getItems(),selectedRowsList));

	}

	
	public void moveDown(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		int[] selectedRows = tablePane.getTable().getSelectedRows();
		PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(tablePane.getItems(), getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> selectedRowsList = lotUtil.getSelectedRows(selectedRows[0], selectedRows[selectedRows.length -1]);
		List<PreProductionLot> changedLots = lotUtil.moveDown(selectedRows[0], selectedRows[selectedRows.length -1],(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
		updateReplicatedLots(changedLots);
		getDao(PreProductionLotDao.class).updateAll(changedLots);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
		
		updateTablePane(tablePane,lotUtil.getDataList(tablePane.getItems(),selectedRowsList));
	}

	protected void updateTablePane(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane,
			List<MultiValueObject<PreProductionLot>> selectedLots, String[] methods){
		
		tablePane.reloadData(tablePane.getItems());
		if(tablePane == scheduleMainPanel.getUpcomingLotPanel()){
			scheduleMainPanel.assignUpcomingLotColors();
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct();
			}
		}
		tablePane.clearSelection();
		tablePane.select(selectedLots);
	}
	

	@Override
	public void execute(String productId, String productionLot, String planCode, int stampedCount) {
		
		if(!StringUtils.isEmpty(properties.getPlanCode()) && !properties.getPlanCode().contains(StringUtils.trim(planCode))){
			Logger.getLogger().warn("Invalid Plan Code ", planCode);
			return;
		}
		
		if(!isInitialized()){
			Logger.getLogger().info("Received product on event before system initialized: Production Lot:", productionLot, 
					" Plan Code:", planCode, "Product Id:", productId, "stampedCount:" + stampedCount);
			return;
		}
		
		clearCurrentProductId();
		
		Logger.getLogger().info("Product Loaded:", productionLot, " planCode:", planCode, " stampedCount:" + stampedCount);
		updateLots(productionLot, stampedCount);
		
		if(!StringUtils.isEmpty(productId)) setCurrentProductId(productId);
	}
	
	public ProductOnHlPropertyBean getOnProerties(){
		return PropertyService.getPropertyBean(ProductOnHlPropertyBean.class, scheduleMainPanel.getProcessPointId());
	}
	
	protected void updateCompleteLots(List<PreProductionLot> changedLots) { 
		
		for(PreProductionLot lot : changedLots){
			lot.setSendStatus(PreProductionLotSendStatus.DONE);
		
		ProductStampingSequenceDao pssDao = getDao(ProductStampingSequenceDao.class);
		List<ProductStampingSequence> products = pssDao.findAllByProductionLot(lot.getProductionLot());
			for(ProductStampingSequence product: products){
				if(product.getSendStatus() == ProductStampingSendStatus.WAITING.getId() || product.getSendStatus() == ProductStampingSendStatus.SENT.getId()){
					product.setSendStatus( ProductStampingSendStatus.STAMPED.getId());
					pssDao.save(product);
				}
			}
		
		}
		
		updateChangedLots(changedLots);
	}

	@Override
	protected void holdAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		if(selectedLots.isEmpty()) return;
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		
		for(MultiValueObject<PreProductionLot> lot : selectedLots) {
			lot.getKeyObject().setHoldStatus(0);
			changedLots.add(lot.getKeyObject());
		}
		
		logger.info("hold the lots. number of changed lots: " + changedLots.size() + 
			    " start lot: " + changedLots.get(0).getProductionLot() + 
			    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);
		refreshLots();
	}

	@Override
	protected void realeaseAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		if(selectedLots.isEmpty()) return;
		
		double sequence = getDao(PreProductionLotDao.class).findMaxSequence(properties.getPlanCode()).doubleValue();
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		for(MultiValueObject<PreProductionLot> lot : selectedLots) {
			lot.getKeyObject().setHoldStatus(1);
			if(lot.getKeyObject().getPlanCode().equals(getProperties().getSourcePlanCode())) 
				sequence += getOnProerties().getSequenceInterval();
			else {
				BigDecimal incrementValue= new BigDecimal("0.01");
				BigDecimal newSeqValue = new BigDecimal(String.valueOf(sequence)).add(incrementValue);
				sequence = newSeqValue.doubleValue();
			}
			lot.getKeyObject().setSequence(sequence);
			changedLots.add(lot.getKeyObject());
		}
		
		logger.info("release the lots. number of changed lots: " + changedLots.size() + 
			    " start lot: " + changedLots.get(0).getProductionLot() + 
			    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);
		refreshLots();
	}
	
	public void setNext(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		int[] selectedRows = tablePane.getTable().getSelectedRows();
		List<MultiValueObject<PreProductionLot>> selectedItems = tablePane.getSelectedItems();
		if(selectedRows[0] == 0) 
		{
			scheduleMainPanel.getMainWindow().setErrorMessage("Selected row is at the top of the list, can not move up.");
			return;
		}
		if(selectedItems.size()>1)
		{
			scheduleMainPanel.getMainWindow().setErrorMessage("Please select one row only");
			return;
		}
		PreProductionLotSeqUtils lotUtils = new PreProductionLotSeqUtils(tablePane.getItems(),getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> changedLots = lotUtils.moveTop(selectedRows[0]);
		updateChangedLots(changedLots);
		updateTablePane(tablePane,selectedItems);
		logger.info("Moved the lot:"+tablePane.getSelectedItems().get(0)+ " to top of list");
		refreshLots();
	}
}
 
