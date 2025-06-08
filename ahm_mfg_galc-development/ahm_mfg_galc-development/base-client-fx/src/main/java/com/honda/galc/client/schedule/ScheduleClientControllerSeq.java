package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * 
 * <h3>Schedule Client Controller Seq Class description</h3>
 * <p> ScheduleClientControllerSeq description </p>
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
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 *
 */

public class ScheduleClientControllerSeq extends ScheduleClientController {

	private BuildAttributeCache buildAttributeCache;
	private String lineSeparator = System.getProperty("line.separator"); 
	private ScheduleMainPanel panel;
	
	public ScheduleClientControllerSeq(ScheduleMainPanel panel) {
		super(panel);
		this.panel = panel;
	}

	@Override
	public void retrievePreProductionLots(){
		clearCurrentProductId();
		String planCode = panel.getDropDownStringValue();
		if(!StringUtil.isNullOrEmpty(planCode)) {
			List<PreProductionLot> preProductionLots = getDao(PreProductionLotDao.class).findAllByPlanCode(planCode);
			
			List<PreProductionLot> processedLots = new SortedArrayList<PreProductionLot>("getSequence");
			List<PreProductionLot> upcomingLots = new SortedArrayList<PreProductionLot>("getSequence");
			List<PreProductionLot> onHoldLots = new SortedArrayList<PreProductionLot>("getProductionLot");
			List<PreProductionLot> currentLots = new SortedArrayList<PreProductionLot>("getSequence");
			
			for(PreProductionLot lot : preProductionLots) {
	
				if(lot.getHoldStatus() == 0) {
					onHoldLots.add(lot);
				} else {
					if(lot.getSendStatus() == PreProductionLotSendStatus.WAITING 
							|| lot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS|| lot.getSendStatus() == PreProductionLotSendStatus.SENT) {
						upcomingLots.add(lot);
					} else if (lot.getSendStatus() == PreProductionLotSendStatus.DONE) {
						processedLots.add(lot);
					}
				}
			}
		
			
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
			}
			else{
				retrieveProcessedProducts();
				if(properties.isAutoLoadExpectedProductId()){
					retreiveExpectedProduct(currentLots, upcomingLots);
				}
			}
			
			publishDataChanged(prepareData(currentLots,currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
			publishDataChanged(prepareData(upcomingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
			publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);
			
			lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestampByPlanCode(panel.getDropDownStringValue());
		}
	}
	

	
	protected void setCurrentLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		if(checkScheduleChanged()) return;

		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();

		PreProductionLotSeqUtils lotUtils = new PreProductionLotSeqUtils(tablePane.getTable().getItems(), getProperties().isMoveByKdLot(),
				getProperties().lockFirstLot(), scheduleMainPanel);

		List<PreProductionLot> changedLots = lotUtils.setCurrentLot(selectedRows.get(0), selectedRows.get(selectedRows.size() - 1));
		
		//confirm log change
		MultiValueObject<PreProductionLot> selectedRow = tablePane.getTable().getItems().get(selectedRows.get(0));
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
		retrievePreProductionLots();
	}

	
	private boolean confirmSetCurrent(List<PreProductionLot> changedLots, String currentLot) {
		StringBuilder sb = new StringBuilder("The following lot");
		sb.append(changedLots.size() > 1 ? "s " : " ");
		sb.append("will be set to Complete.  This action can not be undone.").append(lineSeparator);
		for(PreProductionLot lot : changedLots)
			sb.append(lot.getProductionLot()).append(lineSeparator);
		
		sb.append(lineSeparator).append("You are about to set Lot ");
		sb.append(currentLot).append(" as Current.  Do you want to continue?");
		
		return MessageDialog.confirm(ClientMainFx.getInstance().getStage(), sb.toString());
	}

	private void generateShippingInfo(List<PreProductionLot> unProcessedRowsBeforeSelectedRow) throws Exception {
		for(PreProductionLot preLot : unProcessedRowsBeforeSelectedRow){
			if(preLot.getSendStatus() == PreProductionLotSendStatus.WAITING|| preLot.getSendStatus() == PreProductionLotSendStatus.SENT){
				ServiceFactory.getDao(SubProductShippingDao.class).createSubProductShipping(preLot, 
						getProductType(), getOnProerties().getSequenceInterval(), getSubIds(preLot));
			}
				
		}
		
	}
	
	private String[] getSubIds(PreProductionLot preProductionLot) throws Exception {
		 String subIdStr = getBuildAttributesCache().findAttributeValue(preProductionLot.getProductSpecCode(),BuildAttributeTag.SUB_IDS, getProductType());
		 
		 if(StringUtils.isEmpty(subIdStr)){
			 logger.error("Invalid Build Attribute SUB_IDS.");
			 throw new Exception("Sub Id build attribue configuration error.");
		 }
		
		 String[] subIds = subIdStr.split(Delimiter.COMMA);
		return subIds;
	}
	
	private BuildAttributeCache getBuildAttributesCache() {
		if(buildAttributeCache == null){
			buildAttributeCache = new BuildAttributeCache();
			buildAttributeCache.loadAttribute(BuildAttributeTag.SUB_IDS);
	
		}
		return buildAttributeCache;
	}

	protected void completeLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		PreProductionLotSeqUtils lotUtils = new  PreProductionLotSeqUtils(tablePane.getTable().getItems(), getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> changedLots = lotUtils.completeLots();   

		logger.info("complete the lots. number of changed lots: " + changedLots.size() + 
				" start lot: " + changedLots.get(0).getProductionLot() + 
				" end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateCompleteLots(changedLots);
		retrievePreProductionLots();

	}

	public void moveUp(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		performUp(tablePane, selectedRows, false);
	}
	
	public void performUp (ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane, List<Integer> selectedRows, boolean isCut) {
		ArrayList<Integer> selectedRowsNew=new ArrayList<Integer>();
		List<PreProductionLot> changedLots=new ArrayList<PreProductionLot>();
		final boolean upcomingPanelEnabled = ((scheduleMainPanel.getUpcomingLotTblPane() != null) && ((Boolean) upcomingPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		final boolean currentPanelEnabled = ((scheduleMainPanel.getCurrentLotPanel() != null) && ((Boolean) currentPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		
		if(upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())) {
			List<MultiValueObject<PreProductionLot>> inProgressLots = FXCollections.observableArrayList();
			//The table pane is either Current or Upcoming
			//Get current panel items
			List<MultiValueObject<PreProductionLot>>  currentLots = (currentPanelEnabled ? scheduleMainPanel.getCurrentLotPanel().getLotPanel().getTable().getItems() : null);
			//Get upcoming panel items
			List<MultiValueObject<PreProductionLot>>  upcomingLots = (upcomingPanelEnabled ? scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems() : null);
				//Table pane is upcoming lots, Edit the selected row indexes
			int factor = (currentPanelEnabled ? currentLots.size() : 0);
			for (Integer index: selectedRows) {
				index += factor;
				selectedRowsNew.add(index);
			}
			//Combining current and upcoming
			if (currentPanelEnabled) inProgressLots.addAll(currentLots);
			if (upcomingPanelEnabled) inProgressLots.addAll(upcomingLots);
			
			PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(inProgressLots, getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
			
			//Check for Cut or Move Up Event
			if(isCut){
				
				changedLots=cutMoveUp(selectedRowsNew, lotUtil);
			}
			else{
				changedLots = 
						lotUtil.moveUp(selectedRowsNew.get(0), selectedRowsNew.get(selectedRowsNew.size() -1),(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
			}
			
		
		getDao(PreProductionLotDao.class).updateAll(changedLots);
	
		lastUpdateTimeStamp = findLastUpdateTimestamp();
		
		retrievePreProductionLots();
		}
	}
	
	public List<PreProductionLot> cutMoveUp (ArrayList<Integer> selectedRows, PreProductionLotSeqUtils lotUtil) {
		
		List<PreProductionLot> changedLots=new ArrayList<PreProductionLot>();
		Map<String,PreProductionLot> changedLotMap=new HashMap<String,PreProductionLot>();
		
		int startRow=selectedRows.get(0);
		int endRow=selectedRows.get(selectedRows.size() - 1);
		
		//Move Up above the row selected to paste
		for(int i=0; i< (getModel().getCutRowIndices().get(0)-getModel().getPasteRowIndex());i++){
					
		List<PreProductionLot> movedLots=
				lotUtil.moveUp(startRow, endRow,(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
		startRow--;
		endRow--;
			for(PreProductionLot prodLot:movedLots){
						changedLotMap.put(prodLot.getId(), prodLot);
			}
				
		}
		logger.info("Changed Lots after Cut/Paste:  "+changedLotMap.values().toString());
		changedLots.addAll(changedLotMap.values());
			
		return changedLots;
	}
	
	public void moveDown(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		performDown(tablePane,selectedRows,false);
	}
	
	public void performDown (ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane, List<Integer> selectedRows, boolean isCut){
	
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		ArrayList<Integer> selectedRowsNew=new ArrayList<Integer>();
		final boolean upcomingPanelEnabled = ((scheduleMainPanel.getUpcomingLotTblPane() != null) && ((Boolean) upcomingPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		final boolean currentPanelEnabled = ((scheduleMainPanel.getCurrentLotPanel() != null) && ((Boolean) currentPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		
		//check if pane is upcoming or current
		if((upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())) || 
				(currentPanelEnabled && tablePane.equals(scheduleMainPanel.getCurrentLotPanel().getLotPanel()))) {
			//The table pane is either Current or Upcoming
			//Get current panel items
			List<MultiValueObject<PreProductionLot>> inProgressLots = FXCollections.observableArrayList();
			List<MultiValueObject<PreProductionLot>>  currentLots = (currentPanelEnabled ? scheduleMainPanel.getCurrentLotPanel().getLotPanel().getTable().getItems() : null);
			//Get upcoming panel items
			List<MultiValueObject<PreProductionLot>>  upcomingLots = (upcomingPanelEnabled ? scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems() : null);
			
			if(upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())){
				
				int factor = (currentPanelEnabled ? currentLots.size() : 0);
				for (Integer index: (getModel().isOnCut()?getModel().getCutRowIndices():selectedRows)) {
					index += factor;
					selectedRowsNew.add(index);
				}
				
			}
			else{
				selectedRowsNew.addAll(selectedRows);
			}
			//Combining current and upcoming
			if (currentPanelEnabled) inProgressLots.addAll(currentLots);
			if (upcomingPanelEnabled) inProgressLots.addAll(upcomingLots);
			
			PreProductionLotSeqUtils lotUtil = new PreProductionLotSeqUtils(inProgressLots, getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		
			// check for Cut or Move Down Event
			if(isCut){
			
				changedLots=cutMoveDown(selectedRowsNew, lotUtil);
			}
			else{
				changedLots = 
					lotUtil.moveDown(selectedRowsNew.get(0), selectedRowsNew.get(selectedRowsNew.size() -1),(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
			}
		
			getDao(PreProductionLotDao.class).updateAll(changedLots);
			lastUpdateTimeStamp = findLastUpdateTimestamp();
			retrievePreProductionLots();
		}
	}
	
	public List<PreProductionLot> cutMoveDown (ArrayList<Integer> selectedRows, PreProductionLotSeqUtils lotUtil) {
			
		List<PreProductionLot> changedLots=new ArrayList<PreProductionLot>();
		Map<String,PreProductionLot> changedLotMap=new HashMap<String,PreProductionLot>();
	
		int startRow=selectedRows.get(0);
		int endRow=selectedRows.get(selectedRows.size() - 1);
		
		//Move Down below the row selected to paste
		for(int i=0; i< (getModel().getPasteRowIndex()-getModel().getCutRowIndices().get(getModel().getCutRowIndices().size()-1));i++){
				
			List<PreProductionLot> movedLots=
						lotUtil.moveDown(startRow,endRow,(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
			startRow++;
			endRow++;
			for(PreProductionLot prodLot:movedLots){
					changedLotMap.put(prodLot.getId(), prodLot);
			}
			
		}
	logger.info("Changed Lots after Cut/Paste:  "+changedLotMap.values().toString());
	changedLots.addAll(changedLotMap.values());
			
	return changedLots;
		 
	}
	
	protected void updateTablePane(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane,
			List<MultiValueObject<PreProductionLot>> selectedLots, String[] methods){
		
		tablePane.setData(tablePane.getTable().getItems());
		if(tablePane == scheduleMainPanel.getUpcomingLotTblPane()){
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct();
			}
		}
		tablePane.selectList(selectedLots);
	}
	

	public void productLoaded(String productId, String productionLot, String planCode, int stampedCount) {
		
		clearCurrentProductId();
		
		if(!StringUtils.isEmpty(panel.getDropDownStringValue()) && !panel.getDropDownStringValue().contains(StringUtils.trim(planCode))){
			Logger.getLogger().warn("Invalid Plan Code ", planCode);
			return;
		}
		
		Logger.getLogger().info("Product Loaded:", productionLot, " planCode:", planCode, " stampedCount:" + stampedCount);
		updateLots(productionLot, stampedCount);
		
		if(!StringUtils.isEmpty(productId)) setCurrentProductId(productId);
		
		completeScheduleClient();
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
		retrievePreProductionLots();
	}

	@Override
	protected void realeaseAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		if(selectedLots.isEmpty()) return;
		
		double sequence = getDao(PreProductionLotDao.class).findMaxSequence(panel.getDropDownStringValue()).doubleValue();
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
			    " end lt: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);
		retrievePreProductionLots();
	}

}
 
