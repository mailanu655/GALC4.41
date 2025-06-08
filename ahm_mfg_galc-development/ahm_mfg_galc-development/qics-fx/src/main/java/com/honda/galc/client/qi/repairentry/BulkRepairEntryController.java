package com.honda.galc.client.qi.repairentry;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.SUBMIT;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.ProductDefectUpdatedEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;

public class BulkRepairEntryController extends AbstractRepairEntryController<RepairEntryModel, AbstractRepairEntryView<?,?>>
implements EventHandler<ActionEvent> {
	public BulkRepairEntryController(RepairEntryModel model, BulkRepairEntryView view) {
		super(model, view);
		initProperties();
		loadData();
		EventBusUtil.register(this);
	}

	private void loadData() {
	}

	@Override
	public void initializeListeners() {
		if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
			addRowSelectionListnerForMultiSelect();
		}else {
		addRowSelectionListner();
	}

	}
	
	@Override
	public void initEventHandlers() {
		loadInitialData();
		loadInitData();
		disableAllButtons();
		if (assignActualProblemFlag) {
			selectLatestActualProblem();
		}
	}
	
	protected void initProperties() {
		boolean isShowKickoutPane = QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)? true : false;
		QiStationConfiguration kickoutStation = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getSettingsName());
		if (null != kickoutStation && kickoutStation.getPropertyValue().equalsIgnoreCase(QiConstant.YES)) {
			isShowKickoutPane = true;
		}
		getModel().setShowKickoutPane(isShowKickoutPane);
	}

	/**
	 * Populate data on repair area space on initial load.
	 *
	 */
	public void loadInitData() {
		final String PLANT_LBL = "Plant : ";
		final String ROW_LBL = "Row : ";
		final String SPACE_LBL = "Space : ";
		final String REPAIR_AREA_LBL = "Repair Area : ";

		try {
			QiRepairAreaSpace qiRepairAreaSpace = getModel().findRepairAreaSpaceByProductId(getModel().getProductId());
			if(qiRepairAreaSpace != null) {
				QiRepairArea qiRepairArea = getModel().findRepairAreaByName(qiRepairAreaSpace.getId().getRepairAreaName());
				getView().getPlantLabel().setText(PLANT_LBL + (qiRepairArea != null ? qiRepairArea.getPlantName() : ""));
				getView().getParkingRepairAreaLabel().setText(REPAIR_AREA_LBL + (qiRepairAreaSpace.getId().getRepairAreaName()));
				getView().getRowLabel().setText(ROW_LBL + (qiRepairAreaSpace.getId().getRepairArearRow()));
				getView().getSpaceLabel().setText(SPACE_LBL + (qiRepairAreaSpace.getId().getRepairArearSpace()));
			}
			else
			{
				getView().getPlantLabel().setText("");
				getView().getParkingRepairAreaLabel().setText("");
				getView().getRowLabel().setText("");
				getView().getSpaceLabel().setText("");
			}
		} catch (Exception e) {
			displayErrorMessage("Initial load failed.");
		}
	}

	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds() {
		if(isCancelBtnDisable())  {
			return new ProductActionId[]{SUBMIT};
		}
		return new ProductActionId[] { CANCEL , SUBMIT};
	}

	protected void loadInitialData() {
		getModel().setCurrentWorkingEntryDept(getProcessPointId());
		setCurrentWorkingProcessPointId(getProcessPointId());
		getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());

		// if multi_line station load station configuration of qics station associated with that line
		if(isMultiLine())  {
			setQicsStation();
		}
		List<QiRepairResultDto> configuredDefectList = ProductSearchResult.getDefectsProcessing();

		// sorting data based on defect status
		setMainDefectsOnly(true);
		Collections.sort(configuredDefectList, defectSortingComparator);
		List<QiRepairResultDto> distinctDefects = new ArrayList<>();
		List<QiCommonDefectResult> commonDefects = new ArrayList<>();
		// fetch repair defect hierarchy
		for (QiRepairResultDto dto : configuredDefectList) {
			if(!getProductModel().isSingleProduct())  {
				QiCommonDefectResult thisCommonDefect = ProductSearchResult.createQiCommonDefectResult(dto);
				if(!commonDefects.contains(thisCommonDefect))  {
					commonDefects.add(thisCommonDefect);
					distinctDefects.add(dto);
				}
			}
			else  {
				distinctDefects.add(dto);
			}
			long defectResultId = dto.getDefectResultId();
			// call to load all repair result belongs to this defect
			List<QiRepairResultDto> childDefectList = getModel().findAllRepairEntryDefectsByDefectId(defectResultId);
			if (getProductModel().isTrainingMode()) {
				childDefectList = updateChildrenDefectResultsForTrainingMode(defectResultId, childDefectList);
			}
			if(childDefectList != null && !childDefectList.isEmpty())  {
				setMainDefectsOnly(false);
			}
			Collections.sort(childDefectList, defectSortingComparator);

			setRepairTimeStamp(childDefectList);
			dto.setChildRepairResultList(childDefectList);
			dto.setRepairTimestamp(getLatesRepairTimestampForMainDefect(childDefectList));
		}

		getView().getTreeTablePane().getRoot().getChildren().clear();
		setTableContent(distinctDefects, getView().getTreeTablePane().getRoot().getChildren());
		setMainDefectList(configuredDefectList);
		boolean isProductScraped = isProductScrapped(configuredDefectList);
		boolean isFromScrappedTable = getProductModel().isProcessedFromScrappedTable();		
		getView().getRepairOptionsPane().setDisable(isProductScraped || isFromScrappedTable);
		getView().setProductScraped(isProductScraped || isFromScrappedTable);
		if (isProductScraped || isFromScrappedTable)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED, StatusMessageEventType.WARNING));
		}
	}
	
	/**
	 * This method is to ADD/UPDATE/DELETE the searched child defect list according to cached defect list
	 * @param defectResultId
	 * @param childrenDefectResults
	 * @return
	 */
	private List<QiRepairResultDto> updateChildrenDefectResultsForTrainingMode(
			long defectResultId, List<QiRepairResultDto> childrenDefectResults){
		Map<QiRepairResultDto, Integer> cachedDefectsForTM = this.getProductModel().getCachedDefectsForTraingMode();
		if(cachedDefectsForTM != null && cachedDefectsForTM.size() > 0) {	
			for(Map.Entry<QiRepairResultDto, Integer> entry : cachedDefectsForTM.entrySet()) {
				QiRepairResultDto cachedDefect = (QiRepairResultDto) entry.getKey();
				Integer type = entry.getValue();
				if((defectResultId == cachedDefect.getDefectResultId()) && 
						(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM 
						|| type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM 
						|| type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM)) {
					if(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM)
						childrenDefectResults.add(cachedDefect);
					else if(type == QiConstant.DELETE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);	
									break;
								}
							}
						}
					}else if (type == QiConstant.UPDATE_CHILD_DEFECT_FOR_TM) {
						if(childrenDefectResults != null && childrenDefectResults.size() > 0) {
							for(QiRepairResultDto defect : childrenDefectResults) {
								if(defect.getRepairId() == cachedDefect.getRepairId()) {
									childrenDefectResults.remove(defect);
									childrenDefectResults.add(cachedDefect);
									break;
								}
							}
						}
					}
				}
				
			}
		}
		
		return childrenDefectResults;
	}
	
	

	/**
	 * This method will be recursively used to set content on repair result tree
	 * structure table.
	 * 
	 * @param repairResultList
	 * @param treeItem
	 */
	protected void setTableContent(List<QiRepairResultDto> repairResultList,
			ObservableList<TreeItem<QiRepairResultDto>> treeItem) {

		List<BaseProduct> productList = getProductModel().getProcessedProducts();
		for (QiRepairResultDto repairResultDto : repairResultList) {
			boolean found = false;
			for(BaseProduct p : productList)  {
				if(p.getProductId().equalsIgnoreCase(repairResultDto.getProductId()))  {
					found = true;
					break;
				}
			}
			if(found) {
				TreeItem<QiRepairResultDto> childTreeItem = new TreeItem<QiRepairResultDto>(repairResultDto);
				treeItem.add(childTreeItem);
				// default expansion of defect hierarchy if status is not fixed
				if (repairResultDto.getCurrentDefectStatus() != DefectStatus.FIXED.getId()
						&& repairResultDto.getCurrentDefectStatus() != DefectStatus.NON_REPAIRABLE.getId()) {
					childTreeItem.setExpanded(true);
				}
				if (repairResultDto.getChildRepairResultList() != null
						&& !repairResultDto.getChildRepairResultList().isEmpty()) {
					setTableContent(repairResultDto.getChildRepairResultList(), childTreeItem.getChildren());
				}
			}
		}
	}

	@Override
	protected void markDefectAsActualProblem()  {
		markDefectAsActualProblem(true);
	}
	/**
	 * This method will be used to mark defect as actual problem and create one for Multi-Select or Single selected defect(s)
	 * entry in repair result table and will form a hierarchy.
	 */
	@Override
	public List<QiRepairResultDto> markDefectAsActualProblem(boolean reload) {
		List<Long> selectedDefectResultIds = new ArrayList<Long>();
		// check if any row selected
		List<QiRepairResultDto> repairedList = new ArrayList<QiRepairResultDto>();
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			List<Long>  processedDefects = new ArrayList<Long>();
			ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
			if(selectedItems != null && selectedItems.size() > 0) {
				for(TreeItem<QiRepairResultDto> item : selectedItems) {
					if(item != null && item.getValue() != null) {
						if(processedDefects != null && !processedDefects.isEmpty() && processedDefects.contains(item.getValue().getDefectResultId()))  continue;
						QiRepairResultDto selectedDefect = item.getValue();
						repairedList.addAll(markDefectAsActualProblem(selectedDefect, processedDefects));
						selectedDefectResultIds.add(selectedDefect.getDefectResultId());
					}					
				}
			}
		}
		if(reload)  {
			// call to refresh defect table
			loadInitialData();
			//Select all child defects 
			if(selectedDefectResultIds != null){
				if(selectedDefectResultIds.size() == 1) { //Single select
					selectLatestChildDefect(selectedDefectResultIds.get(0));
					enableDisableButtonsOnSelection(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
				}else if(selectedDefectResultIds.size() > 1) {
					for(long defectResultId : selectedDefectResultIds) { //Multi-Select
						selectLatestChildDefect(defectResultId);
					}
					this.disableAllButtons();
					if(!getView().getScrapBtn().isDisabled())
						getView().getScrapBtn().setDisable(true);
					//Enable Repair Method button
					getView().getAddRepairMethodBtn().setDisable(false);
				}
			}		
		}
		return repairedList;  //list of repaired main defects
	}
	
	/**
	 * This method will select and scroll up to latest assign actual problem for Multi-Select or Single select
	 * @param defectResultId
	 */
	@Override
	protected void selectLatestChildDefect(long defectResultId) {
		ObservableList<TreeItem<QiRepairResultDto>> mainDefectList = getView().getTreeTablePane().getRoot().getChildren();
		for (TreeItem<QiRepairResultDto> mainTreeItem : mainDefectList) {
			if (defectResultId == mainTreeItem.getValue().getDefectResultId()) {
				if (mainTreeItem.getChildren() != null) {
					getView().getTreeTablePane().getSelectionModel().select(mainTreeItem.getChildren().get(0));
					mainTreeItem.setExpanded(true);
					break;
				}
			}
		}
	}

	/**
	 * Add a child defect to the selected defect for all processed products	
	 * @param selectedDefect
	 */
	private List<QiRepairResultDto> markDefectAsActualProblem(QiRepairResultDto selectedDefect, List<Long> processedDefects)  {
		
		if(processedDefects != null && !processedDefects.isEmpty() && processedDefects.contains(selectedDefect.getDefectResultId()))  return null;
			
		QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(selectedDefect);
		List<QiRepairResultDto> toBeRepairedList = new ArrayList<>();
		List<QiRepairResultDto> repairedList = new ArrayList<>();
		if(getProductModel().isSingleProduct())  {
			toBeRepairedList.add(selectedDefect);
		}
		else  {
			toBeRepairedList.addAll(ProductSearchResult.getDefectsProcessing());
		}
		for(QiRepairResultDto currentDefect : toBeRepairedList) {
			QiCommonDefectResult currentCommonDefect = new QiCommonDefectResult(currentDefect);
			if(selectedCommonDefect.equals(currentCommonDefect)) {
				if(processedDefects == null)  {processedDefects = new ArrayList<Long>();}
				processedDefects.add(currentDefect.getDefectResultId());
				//check if there is already a child defect, this will happen if a second repair method is being added (previous was not-completely fixed)
				//if there is an existing child defect, return reference to that, do not add new actual problem
				QiRepairResultDto defectInCache = ProductSearchResult.getDefectResultByDefectId(currentDefect.getDefectResultId(), currentDefect.getProductId());
				if(defectInCache != null)  {
					List<QiRepairResultDto> existingActualProblems = defectInCache.getChildRepairResultList();
					if(existingActualProblems != null && !existingActualProblems.isEmpty() && existingActualProblems.get(0) != null)  {
						repairedList.add(defectInCache);
						continue;
					}
				}
				if(!getProductModel().isTrainingMode()){
					QiDefectResult mainDefect = getModel().findMainDefectByDefectId(currentDefect.getDefectResultId());
					// call to persist QiRepairResult
					QiRepairResult qiRepairResult = new QiRepairResult(mainDefect); 

					String site = getModel().getCurrentWorkingProcessPoint().getSiteName();
					String plant = getModel().getCurrentWorkingProcessPoint().getPlantName();
					short lineNo = getModel().getEntryProdLineNo();

					qiRepairResult.setEntrySiteName(site);
					qiRepairResult.setEntryPlantName(plant);
					qiRepairResult.setEntryProdLineNo(lineNo);
					qiRepairResult.setEntryDept(getModel().getCurrentWorkingEntryDept());
					qiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
					qiRepairResult.setTerminalName(getModel().getTerminalName());
					qiRepairResult.setCreateUser(getModel().getUserId());
					qiRepairResult.setActualProblemSeq((short) (currentDefect.getChildRepairResultList() != null
							? currentDefect.getChildRepairResultList().size() + 1 : 1));
					qiRepairResult = getModel().createRepairResult(qiRepairResult);

					QiRepairResultDto addedChildDefectDto = new QiRepairResultDto(mainDefect, qiRepairResult.getRepairId());
					addedChildDefectDto.setDivisionName(currentDefect.getDivisionName());
					addedChildDefectDto.setActualTimestamp(new Timestamp(qiRepairResult.getActualTimestamp().getTime()));
					repairedList.add(ProductSearchResult.addChildDefect(addedChildDefectDto));
				}
				else{
					QiDefectResult mainDefect = new QiDefectResult(currentDefect);
					QiRepairResultDto childDefectDto = new QiRepairResultDto(mainDefect, generateCachedRepairId());
					childDefectDto.setDefectResultId(currentDefect.getDefectResultId());	
					Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
					childDefectDto.setActualTimestamp(timestamp);
					childDefectDto.setDivisionName(currentDefect.getDivisionName());
					childDefectDto.setIsRepairRelated((short) 0);
					getProductModel().getCachedDefectsForTraingMode().put(childDefectDto, QiConstant.ADD_CHILD_DEFECT_FOR_TM);
					repairedList.add(ProductSearchResult.addChildDefect(childDefectDto));
				}
			}
		}
		return repairedList;
	}
	
	/**
	 * This method is to generate repair Id for training mode
	 * @return
	 */
	private long generateCachedRepairId() {
		long maxRepairId = getModel().findMaxRepairId();
		int addedChildDefectCount = 1;
		Map<QiRepairResultDto, Integer> cachedDefects = this.getProductModel().getCachedDefectsForTraingMode();
		if(cachedDefects != null && cachedDefects.size() > 0) {
			for(Integer type : cachedDefects.values()) {
				if(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM)
					addedChildDefectCount+=1;
			}
		}
		long repairId = maxRepairId + addedChildDefectCount;
		return repairId;
	}

	/**
	 * This method will be used to assign repair methods to actual defects.
	 */
	protected void addRepairMethods() {
		QiRepairResultDto selectedItem = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
		boolean isFixedDefect = selectedItem.getCurrentDefectStatus() == DefectStatus.FIXED.getId();
    	ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
		boolean mainDefectFlag = selectedItem.getRepairId() == 0 ? true : false;
		List<Long> repairIds = null;
		List<QiRepairResultDto> allSelectedDefects = new ArrayList<QiRepairResultDto>();
		
		//Find all selected defects
		if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
			if(selectedItems != null && selectedItems.size() > 0) {
				for(TreeItem<QiRepairResultDto> item : selectedItems) {
					if(item != null && item.getValue() != null) {
						allSelectedDefects.add(item.getValue());
					}					
				}
			 }
		}
		else {
			allSelectedDefects.add(selectedItem);
		}
		if(!mainDefectFlag){

			//get a list of repair ID and send to dialog
				repairIds = new ArrayList<Long>();
			if(getProductModel().isSingleProduct())  {
				for(QiRepairResultDto myItem : allSelectedDefects)  {
					repairIds.add(myItem.getRepairId());
				}
			}
			else  {
				ArrayList<Long> uniqueDefectIds = new ArrayList<>();
				if(allSelectedDefects != null && allSelectedDefects.size() > 0) {
					for(QiRepairResultDto item : allSelectedDefects) {
						List<QiRepairResultDto> defectResults = new ArrayList<QiRepairResultDto>();
						QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(item);
						//first, get A main defect that has a matching actual problem
						QiRepairResultDto repairedParentDefect = ProductSearchResult.getDefectResultByDefectId(item.getDefectResultId(), item.getProductId());
						//then, get all main defects across products that match the above main defect including duplicates
						defectResults = ProductSearchResult.getCommonDefects(repairedParentDefect);
				
						Iterator<QiRepairResultDto> iter = defectResults.iterator();
						while (iter.hasNext()) {
							QiRepairResultDto currentDefect = iter.next();
							if(uniqueDefectIds.contains(currentDefect.getDefectResultId()))  continue;
							uniqueDefectIds.add(currentDefect.getDefectResultId());
							QiRepairResultDto repairResult = ProductSearchResult.getChildDefect(currentDefect, selectedCommonDefect);
							if(repairResult != null && repairResult.getRepairId() > 0)  {
								repairIds.add(repairResult.getRepairId());
							}
						}
					}
				}					
			}
		}
	    
		BulkAddRepairMethodDialog dialog = new BulkAddRepairMethodDialog("Add Repair Method For Selected Actual Problem",
				getModel(), selectedItem, getApplicationId(), isFixedDefect, isNoProblemFound(), allSelectedDefects, sessionTimestamp, repairIds, this);
		dialog.getAddRepairMethodController().setProductModel(getProductModel());
		dialog.showDialog();
		// call to refresh defect table
		if (dialog.isCompletelyFixed()) { //actual problem is completely fixed
			loadInitialData();
			disableAllButtons();
			if (!dialog.isParentDefectFixed())  {//original defect is not fixed
				expandAndSelectRow(selectedItem.getDefectResultId());
			}
			else  {
				getView().getTreeTablePane().getSelectionModel().clearSelection();
			}
		} else if (dialog.isDirty()){ //actual problem is not completely fixed, refresh screen and select the actual problem to enable/disable buttons
			loadInitialData();
			selectActualProblem(selectedItem.getDefectResultId(), selectedItem.getRepairId());
		}
	}

	@Subscribe()
	private void onDefectEvent(ProductDefectUpdatedEvent defectUpdateEvent) {
		List<QiDefectResult> defectResults = (defectUpdateEvent.getDefectResults());
		for(QiDefectResult defectResult : defectResults) {
			QiRepairResultDto defectResultDto = new QiRepairResultDto(defectResult, 0);
			defectResultDto.setActualTimestamp(defectResult.getActualTimestamp());
			ProductSearchResult.updateDefectsProcessingMap(defectResultDto);
		}
	}

	/**
	 * This method will be used to scrap selected defects.
	 */

	@Override
	protected void scrapSelectedDefect() {
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
				 ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
				 if(selectedItems != null && selectedItems.size() > 0) {
					 for(TreeItem<QiRepairResultDto> item : selectedItems) {						
						 if(item != null && item.getValue() != null) {
							 AbstractScrapDialog dialog = new BulkScrapDialog(getModel(), item.getValue(), getView().getTreeTablePane(), getApplicationId(), getView().getUserId());
							dialog.showDialog();
							if (dialog.isUnitScraped()) {
								loadInitialData();
							}
						 }	break;					 
					 }
				 }
			}else {
				QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
				
				AbstractScrapDialog dialog = new BulkScrapDialog(getModel(), selectedDefect, getView().getTreeTablePane(), getModel().getCurrentWorkingProcessPointId(), getView().getUserId());
		dialog.showDialog();
		if (dialog.isUnitScraped()) {
			loadInitialData();
		}
	}
		} else {
		getLogger().error("Please select the Defect you want to scrap");
		}
		
	}

	/**
	 * This method will be used to delete selected defect marked as actual
	 * problem.
	 */
	protected void deleteActualProblem() {
		// check if any row selected
		if (getView().getTreeTablePane().getSelectionModel().isEmpty())  return;

		QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
		QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(selectedDefect);
		QiRepairResultDto selectedMainDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getParent().getValue();
		QiCommonDefectResult selectedCommonMain = new QiCommonDefectResult(selectedMainDefect);
		int selectedIndex = getView().getTreeTablePane().getSelectionModel().getSelectedIndex();

		List<BaseProduct> products = getProductModel().getProcessedProducts();
		for(BaseProduct product : products) {
			//*********
			List<QiRepairResultDto> allDuplicatesForProduct = new ArrayList<>();;
			//get all duplicate main defects
			if(getProductModel().isSingleProduct())  {
				//if single product, we want to touch only selected defect, leave duplicates alone
				allDuplicatesForProduct.add(ProductSearchResult.getActualParentDefect(selectedDefect, product.getProductId()));
			}
			else  {
					//if bulk, then get A parent defect having the common child defect, and get all duplicates
				QiRepairResultDto defectResultDto = ProductSearchResult.getParentProductDefect(selectedCommonDefect, selectedCommonMain, product.getProductId());
				allDuplicatesForProduct = ProductSearchResult.getDuplicateDefectsForProduct(defectResultDto, product.getProductId());
			}
			for(QiRepairResultDto mainDefect : allDuplicatesForProduct)  {
				//get the actual problem for this main defect, matching the selected common actual problem
				QiRepairResultDto childDefectDto = ProductSearchResult.getChildDefect(mainDefect, selectedCommonDefect);
			//******************
			
				if(childDefectDto == null)  continue;
				long repairId = childDefectDto.getRepairId();
	
				// check if repair method exist
				List<QiAppliedRepairMethodDto> appliedRepairMethodList = getModel().getAppliedRepairMethodData(repairId);
				if (appliedRepairMethodList != null && !appliedRepairMethodList.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					sb.append("Actual Problem (repairId:")
					.append(repairId).append(") has associated repair methods hence cannot be deleted");
					MessageDialog.showError(ClientMainFx.getInstance().getStage(getCurrentWorkingProcessPointId()), sb.toString());
					return;
				}
	
				long defectResultId = mainDefect.getDefectResultId();
				List<QiRepairResultDto> actualDefectList = getModel().findAllRepairEntryDefectsByDefectId(defectResultId);
				if (getProductModel().isTrainingMode()) {
					actualDefectList = updateChildrenDefectResultsForTrainingMode(defectResultId, actualDefectList);
				}
	
				boolean updateMainDefectResultStatus = false;
				if (actualDefectList.size() > 1 && isAnyActualDefectFix(actualDefectList) && getTotalNotFixedDefectCount(actualDefectList) == 1) {
					StringBuilder sb = new StringBuilder();
					sb.append("This delete operation will change the main defect (defect id:")
					.append(defectResultId).append(") status to fixed. Do you wish to continue?");
					boolean isOk = MessageDialog.confirm(getView().getStage(),	sb.toString());
					if (!isOk) {
						return;
					}
	
					updateMainDefectResultStatus = true;
	
					if(!getProductModel().isTrainingMode()){
						//check current tracking status again
						if (getModel().isPreviousLineInvalid()) {
							publishProductPreviousLineInvalidEvent();
							return;
						}
						getModel().updateMainDefectResultStatus(defectResultId, DefectStatus.FIXED.getId());
						mainDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						ProductSearchResult.updateDefectsProcessingMap(mainDefect);
					}else {//Update main defect status					
						mainDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
						mainDefect.setActualTimestamp(timestamp);
						getProductModel().getCachedDefectsForTraingMode().put(mainDefect, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);
						ProductSearchResult.updateDefectsProcessingMap(mainDefect);
					}
				} 
	
				//check current tracking status again
				if (getModel().isPreviousLineInvalid()) {
					publishProductPreviousLineInvalidEvent();
					return;
				}
	
				if(!getProductModel().isTrainingMode()) {
					QiRepairResult repairResult = getModel().findRepairResultById(repairId);
					QiRepairResultHist qiRepairResultHist = new QiRepairResultHist(repairResult);
					qiRepairResultHist.setChangeUser(getModel().getUserId());
					qiRepairResultHist.setDeleted((short)1);
					qiRepairResultHist.setReasonForChange(REASON_FOR_CHANGE_DELELE);
					getModel().createRepairResultHistory(qiRepairResultHist);
	
					getModel().deleteRepairEntryDefect(repairId);
					ProductSearchResult.removeChildDefect(repairId, product.getProductId());
	
				} else {
					getProductModel().getCachedDefectsForTraingMode().put(childDefectDto, QiConstant.DELETE_CHILD_DEFECT_FOR_TM);	
					ProductSearchResult.removeChildDefect(repairId, product.getProductId());				
				}
	
				//replicate to old QICS
				if (!getProductModel().isTrainingMode() && updateMainDefectResultStatus) {
					if (PropertyService.getPropertyBean(QiPropertyBean.class, mainDefect.getApplicationId()).isReplicateDefectRepairResult()) {
						QiRepairResult latestFixedRepairResult = getModel().findLatestFixedRepairResult(defectResultId);
						if (latestFixedRepairResult != null) {
							String repairAssociateNo = latestFixedRepairResult.getUpdateUser();
							if (StringUtils.isBlank(repairAssociateNo)) {
								repairAssociateNo = latestFixedRepairResult.getCreateUser();
							}
							getModel().updateOldDefectStatus(defectResultId, DefectStatus.REPAIRED.getId(), 
									latestFixedRepairResult.getActualTimestamp(), repairAssociateNo);
						}
					}
				}
			}
		}
		// call to refresh defect table
		loadInitialData();
		getView().getTreeTablePane().getSelectionModel().select(selectedIndex - 1);
		getView().getTreeTablePane().getSelectionModel().getSelectedItem().setExpanded(true);
		enableDisableButtonsOnSelection(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
	}

	/**
	 * This method will return true if any of the actual defect fix in given
	 * list else false.
	 * 
	 * @param actualDefectList
	 * @return isAnyActualDefectFix
	 */
	private boolean isAnyActualDefectFix(List<QiRepairResultDto> actualDefectList) {
		boolean isAnyActualDefectFix = false;
		for (QiRepairResultDto qiRepairResultDto : actualDefectList) {
			if (qiRepairResultDto.getCurrentDefectStatus() == DefectStatus.FIXED.getId()) {
				isAnyActualDefectFix = true;
				break;
			}
		}
		return isAnyActualDefectFix;
	}

	/**
	 * Method will return total not fixed actual defect count.
	 * 
	 * @param actualDefectList
	 * @return toatalNotFixedDefect
	 */
	private int getTotalNotFixedDefectCount(List<QiRepairResultDto> actualDefectList) {
		int toatalNotFixedDefect = 0;
		for (QiRepairResultDto qiRepairResultDto : actualDefectList) {
			if (qiRepairResultDto.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
				toatalNotFixedDefect = toatalNotFixedDefect + 1;
			}
		}
		return toatalNotFixedDefect;
	}

	/**
	 * This method will be used to change the defect status from fixed to not
	 * fixed.
	 */
	protected void setActualProblemToNotFixed() {
		// check if any row selected
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			List<BaseProduct> products = getProductModel().getProcessedProducts();
			QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
			QiRepairResultDto selectedMainDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getParent().getValue();
			QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(selectedDefect);
			QiCommonDefectResult selectedCommonMain = new QiCommonDefectResult(selectedMainDefect);
			for(BaseProduct product : products) {
				List<QiRepairResultDto> allDuplicatesForProduct = new ArrayList<>();;
				//get all duplicate main defects
				if(getProductModel().isSingleProduct())  {
					//if single product, we want to touch only selected defect, leave duplicates alone
					allDuplicatesForProduct.add(ProductSearchResult.getActualParentDefect(selectedDefect, product.getProductId()));
				}
				else  {
					//if bulk, then get A parent defect having the common child defect, and get all duplicates
					QiRepairResultDto defectResultDto = ProductSearchResult.getParentProductDefect(selectedCommonDefect, selectedCommonMain, product.getProductId());
					allDuplicatesForProduct = ProductSearchResult.getDuplicateDefectsForProduct(defectResultDto, product.getProductId());
				}
				for(QiRepairResultDto mainDefect : allDuplicatesForProduct)  {
					//get the actual problem for this main defect, matching the selected common actual problem
					QiRepairResultDto childDefectDto = ProductSearchResult.getChildDefect(mainDefect, selectedCommonDefect);
					if(!getProductModel().isTrainingMode()){	
						updateActualProblemToNotFixed(mainDefect, childDefectDto);
					}else{
						Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
						//Update child defect in cache
						childDefectDto.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
						childDefectDto.setIsCompletelyFixed((short) 0);
						childDefectDto.setActualTimestamp(timestamp);
						getProductModel().getCachedDefectsForTraingMode().put(childDefectDto, QiConstant.UPDATE_CHILD_DEFECT_FOR_TM);
						
						//Update main defect in cache
						mainDefect.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
						childDefectDto.setActualTimestamp(timestamp);
						ProductSearchResult.updateDefectsProcessingMap(mainDefect);
						getProductModel().getCachedDefectsForTraingMode().put(mainDefect, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);					
					}
				}
			}
			// call to refresh defect table
			loadInitialData();
			disableAllButtons();
		}
	}

	protected void updateActualProblemToNotFixed(QiRepairResultDto mainDefect, QiRepairResultDto actProb) {
	
		long repairId = actProb.getRepairId();
		long defectResultId = mainDefect.getDefectResultId();
	
		QiRepairResult qiRepairResult = getModel().findRepairResultById(repairId);
		qiRepairResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
		qiRepairResult.setUpdateUser(getModel().getUserId());
	
		// call to update repair entry defect status
		getModel().updateRepairResult(qiRepairResult);
	
		// call to update status of all the associated repair methods
		getModel().updateAllRepairMethodStatus(repairId, false);
	
		// updating status to 'not fixed' for initial defect
		getModel().updateMainDefectResultStatus(defectResultId,	DefectStatus.NOT_FIXED.getId());
		mainDefect.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
		ProductSearchResult.updateDefectsProcessingMap(mainDefect);
	
		if (PropertyService.getPropertyBean(QiPropertyBean.class, mainDefect.getApplicationId()).isReplicateDefectRepairResult()) {
			// delete defect from GAL222TBX
			getModel().deleteOldRepairResult(repairId);
	
			// update defect status in GAL125TBX
			getModel().updateOldDefectStatus(defectResultId, DefectStatus.OUTSTANDING.getId(), null, "");
		}
	}

	
	/**
	 * This method will be used to mark defect as no problem found. Used for Multi-Select and Single selected
	 * @throws  
	 * 
	 */
	@Override
	protected void markNoProblemFound() {
		// check if any row selected
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
				 ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
				 if(selectedItems != null && selectedItems.size() > 0) {
					 for(TreeItem<QiRepairResultDto> item : selectedItems) {
						 if(item != null && item.getValue() != null) {
							 markNoProblemFound(item.getValue());
						 }						 
					 }
				 }
			}else {
				QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
				markNoProblemFound(selectedDefect);
			}
		}
		
		// call to refresh defect table
		loadInitialData();
		disableAllButtons();
	}
	
	/**
	 * This method is to change the defect status to Fixed and defect type name to No Problem Found for all processed products
	 * @param selectedDefect
	 */
	private void markNoProblemFound(QiRepairResultDto selectedDefect) {
			List<BaseProduct> products = getProductModel().getProcessedProducts();
			QiCommonDefectResult commonDefectResult = new QiCommonDefectResult(selectedDefect);
			boolean isChild = ProductSearchResult.isChildDefect(selectedDefect);
		
			for(BaseProduct product : products) {
				QiRepairResultDto defectResultDto = new QiRepairResultDto();
				QiDefectResult mainDefect = new QiDefectResult();
				
				if(!getProductModel().isTrainingMode()){
					if(isChild) {
						QiRepairResultDto parentDefect = ProductSearchResult.getParentProductDefect(commonDefectResult, product.getProductId());
						defectResultDto = ProductSearchResult.getChildDefect(parentDefect,
								commonDefectResult);
						mainDefect = getModel().findMainDefectByDefectId(defectResultDto.getDefectResultId());

					} else {
						defectResultDto = ProductSearchResult.getDefectResult(commonDefectResult, product.getProductId());
						mainDefect = getModel().findMainDefectByDefectId(defectResultDto.getDefectResultId());
					}
					mainDefect.setKickoutProcessPointName(defectResultDto.getKickoutProcessPointName());

					if (isChild) {
						long repairId = defectResultDto.getRepairId();

						//update selected repair result
						QiRepairResult existingQiRepairResult = getModel().findRepairResultById(repairId);
						existingQiRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
						existingQiRepairResult.setDefectTypeName2("");
						existingQiRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						existingQiRepairResult.setUpdateUser(getModel().getUserId());
						existingQiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						existingQiRepairResult.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
						getModel().updateRepairResult(existingQiRepairResult);
						updateRepairResultDto(defectResultDto);
						
						//if the repair result has repair method applied but not completely fixed, 
						//create a record in QI_APPLIED_REPAIR_METHOD_TBX with REPAIR_METHOD="NO PROBLEM FOUND" and IS_COMPLETELY_FIXED=1
						getModel().createNoProblemFoundAppliedRepairMethod(repairId);
						
						List<QiRepairResult> repairResultList = getModel().findAllRepairResultByDefectResultId(existingQiRepairResult.getDefectResultId());
						boolean areAllRepairResultsFixed = true;
						boolean areAllRepairResultsNoProblemFound = true;
						for (QiRepairResult rr : repairResultList) {
							if (rr.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
								areAllRepairResultsFixed = false;
								break;
							}
							if (!rr.getDefectTypeName().equals(NO_PROBLEM_FOUND)) {
								areAllRepairResultsNoProblemFound = false;
								break;
							}
						}

						if (areAllRepairResultsFixed) {
							updateInitialDefectNoProblemFound(mainDefect, defectResultDto, areAllRepairResultsNoProblemFound);
						}
					} else {

						updateInitialDefectNoProblemFound(mainDefect, defectResultDto, true);

						// call to populate and save QiRepairResult
						QiRepairResult qiRepairResult = new QiRepairResult(mainDefect); 
						qiRepairResult.setCreateUser(getModel().getUserId());
						qiRepairResult.setActualProblemSeq((short) (defectResultDto.getChildRepairResultList() != null
								? defectResultDto.getChildRepairResultList().size() + 1 : 1));
						qiRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
						qiRepairResult.setDefectTypeName2("");
						qiRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						qiRepairResult.setProductionDate(getModel().getProductionDate());
						qiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						getModel().createRepairResult(qiRepairResult);
					}

					try {
						String message = getModel().addUnitToConfiguredRepairArea(defectResultDto.getDefectResultId());
						if(!StringUtils.isEmpty(message)) {
							EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
							loadInitData();
						}
					} catch (Exception e) {
						handleException("Exception occured in markNoProblemFound() method", "Failed to add unit to configured Repair Area", e);
					}

					// update defect_status to 1 repaired in GAL125TBX
					if (PropertyService.getPropertyBean(QiPropertyBean.class, defectResultDto.getApplicationId()).isReplicateDefectRepairResult()) {
						getModel().updateOldDefectStatus(mainDefect.getDefectResultId(), DefectStatus.REPAIRED.getId(), getModel().getProcessPointTimeStamp(), getModel().getUserId());
					}
					
					if (PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getProcessPointId()).isUpdateNextProcess() &&
                            !PropertyService.getPropertyBoolean(selectedDefect.getApplicationId(),"IS_NAQ_STATION", false)) {
						getModel().updateNextProcessTracking(product);
					}

				}else{
					Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
					if(isChild) {
						QiRepairResultDto parentDefect = ProductSearchResult.getParentProductDefect(commonDefectResult, product.getProductId());
						defectResultDto = ProductSearchResult.getChildDefect(parentDefect,
								commonDefectResult);
						
						//Update Child defect
						defectResultDto.setDefectTypeName(NO_PROBLEM_FOUND);
						defectResultDto.setDefectTypeName2("");
						defectResultDto.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						defectResultDto.setApplicationId(getModel().getCurrentWorkingProcessPointId());
						defectResultDto.setActualTimestamp(timestamp);
						ProductSearchResult.updateDefectsProcessingMap(defectResultDto);
						this.getProductModel().getCachedDefectsForTraingMode().put(defectResultDto, QiConstant.UPDATE_CHILD_DEFECT_FOR_TM);
				        //Update main defect
						List<QiRepairResultDto> allChildDefctList = this.getModel().findAllRepairEntryDefectsByDefectId(defectResultDto.getDefectResultId());
						allChildDefctList= updateChildrenDefectResultsForTrainingMode(defectResultDto.getDefectResultId(), allChildDefctList);
						boolean areAllChildDefectFixed = true;
						for (QiRepairResultDto dto : allChildDefctList) {
							if (dto.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
								areAllChildDefectFixed = false;
								break;
							}
						}
						if(areAllChildDefectFixed) { //If all child defect status is Fixed, Update Parent defect
							parentDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
							parentDefect.setActualTimestamp(timestamp);
							ProductSearchResult.updateDefectsProcessingMap(parentDefect);
							this.getProductModel().getCachedDefectsForTraingMode().put(parentDefect, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);
						}												
					} else {
						defectResultDto = ProductSearchResult.getDefectResult(commonDefectResult, product.getProductId());
						//Update main defect
						defectResultDto.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
						defectResultDto.setActualTimestamp(timestamp);
						ProductSearchResult.updateDefectsProcessingMap(defectResultDto);
						this.getProductModel().getCachedDefectsForTraingMode().put(defectResultDto, QiConstant.UPDATE_PARENT_DEFECT_FOR_TM);
						//Create a child id 
						QiDefectResult newChildDefect = new QiDefectResult(defectResultDto);
						QiRepairResultDto newChildDefectDto = new QiRepairResultDto(newChildDefect, generateCachedRepairId());
						newChildDefectDto.setDefectResultId(defectResultDto.getDefectResultId());
						newChildDefectDto.setDefectTypeName(NO_PROBLEM_FOUND);
						newChildDefectDto.setDefectTypeName2("");
						newChildDefectDto.setActualTimestamp(timestamp);
						newChildDefectDto.setDivisionName(defectResultDto.getDivisionName());
						getProductModel().getCachedDefectsForTraingMode().put(newChildDefectDto, QiConstant.ADD_CHILD_DEFECT_FOR_TM);
						ProductSearchResult.addChildDefect(newChildDefectDto);
					}					
				}
			}
		}
	
	/**
	 * This method will be used to assign new defect as the actual problem to
	 * main defect.<br>
	 * It will navigate to main defect entry screen to perform this action.
	 */
	@Override
	protected void assignActualProblemToDefect() {
		QiProgressBar qiProgressBar = null;
		try {
			qiProgressBar = getQiProgressBar("Assigning defect as Actual Problem.","Assigning defect as Actual Problem.");
			qiProgressBar.showMe();
			
			List<TreeItem<QiRepairResultDto>> qiRepairList = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
			if(qiRepairList == null || qiRepairList.isEmpty())  return;
			boolean firstItem = true;
			List<QiRepairResultDto> additionalRepairDetails = new ArrayList<>();
			Map<String, Object> repairDetails = new HashMap<String, Object>();
			for(TreeItem<QiRepairResultDto> selectedItem : qiRepairList)  {
				if(selectedItem == null) continue;
				QiRepairResultDto qiRepairResultDto = selectedItem.getValue();
				if(firstItem) {
					actualProblemDefectResultId = qiRepairResultDto.getDefectResultId();
					assignActualProblemFlag = true;
					repairDetails.put("isNewDefect", false);
					repairDetails.put("Repair_Related", false);
					repairDetails.put("selectedDefect", qiRepairResultDto);
					repairDetails.put("additional_repair_details", null);
					firstItem = false;
				}
				else  {
					additionalRepairDetails.add(qiRepairResultDto);
				}
			}
			if(!additionalRepairDetails.isEmpty())  {
				repairDetails.put("additional_repair_details", additionalRepairDetails);
			}
			EventBusUtil.publish(new ProductEvent(repairDetails, ProductEventType.PRODUCT_REPAIR_DEFECT));
		}
		finally  {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}
			
	protected void updateRepairResultDto(QiRepairResultDto repairResult) {
		repairResult.setDefectTypeName(NO_PROBLEM_FOUND);
		repairResult.setDefectTypeName2("");
		repairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		repairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		ProductSearchResult.updateDefectsProcessingMap(repairResult);

	}

	protected void updateInitialDefectNoProblemFound(QiDefectResult mainDefect, QiRepairResultDto defectResult, boolean updateReportable) {
		// updating status to 'fixed' for initial defect
		mainDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		mainDefect.setUpdateUser(getModel().getUserId());
		if (updateReportable) {
			mainDefect.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
		}
		getModel().updateDefectResult(mainDefect);
		defectResult.setActualTimestamp(new Timestamp(mainDefect.getActualTimestamp().getTime()));
		defectResult.setCurrentDefectStatus(mainDefect.getCurrentDefectStatus());
		ProductSearchResult.updateDefectsProcessingMap(defectResult);
		}
	
	@Override
	protected void uploadImage(File sourceFile) {
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			List<BaseProduct> products = getProductModel().getProcessedProducts();
			QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
			QiCommonDefectResult selectedCommonDefect = new QiCommonDefectResult(selectedDefect);
			QiRepairResultDto repairResultDto;
			
			for(BaseProduct product : products) {
				if(selectedDefect.getRepairId() > 0) {
					QiRepairResultDto parentDto = ProductSearchResult.getParentProductDefect(selectedCommonDefect, product.getProductId());
					repairResultDto = ProductSearchResult.getChildDefect(parentDto, selectedCommonDefect);
				} else {
					repairResultDto = ProductSearchResult.getDefectResult(selectedCommonDefect, product.getProductId());
				}
				
				if(!getProductModel().isTrainingMode()){	
					if(repairResultDto != null) {
						uploadAndSave(repairResultDto, sourceFile);
					}
				} else {
				}
			}
		}		
	}

	private void uploadAndSave(QiRepairResultDto dto, File sourceFile) {
		Logger.getLogger().info("Uploading media file to Microservice: " + sourceFile.getName());
		String url = MicroserviceUtils.getInstance().postFile(sourceFile);
		if(!StringUtils.isEmpty(url)) {
			saveImage(dto, url);
		} else {
			String msg = "Unable to upload media file: " + sourceFile.getName();
			Logger.getLogger().error(msg);
			displayErrorMessage(msg);
		}
	}
	
	/**
	 * This method is used to save station result data
	 * @param defectResultList
	 * @return
	 */
	private StationResult saveStationResult() {
		if (getModel().getSchedule() == null) {
			return null;
		}
		StationResult stationResult = getStationResult();
		DailyDepartmentSchedule shiftFirstPeriod = getModel().findDailyDepartmentScheduleByCurrentSchedule();

		if (shiftFirstPeriod == null || shiftFirstPeriod.getStartTimestamp() == null) {
			return stationResult;
		}
		if (getModel().isProductProcessed(shiftFirstPeriod)) {
			stationResult = updateStationResult(stationResult, false);
			return stationResult;
		}
		// only update station result when the product has not been processed at this station after the beginning of this shift
		stationResult = updateStationResult(stationResult, true);
		return stationResult;
	}

	/**
	 * This method is used to update Station Result value
	 * @param defectResultList
	 * @param stationResult
	 * @param isDifferentProductId
	 * @return
	 */
	private StationResult updateStationResult(StationResult stationResult, boolean isDifferentProductId) {
		int quantity = 1;
		stationResult.updateQiRepairStationCount(isDifferentProductId, quantity, true);
		stationResult.setLastProductId(getModel().getProductId());
		stationResult = getModel().saveStationResult(stationResult);
		return stationResult;
	}

	/**
	 * This method is used to get Station Result data
	 * @return
	 */
	private StationResult getStationResult() {
		StationResultId stationResultId = new StationResultId();

		stationResultId.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		stationResultId.setProductionDate(getModel().getProductionDate());
		stationResultId.setShift(getModel().getShift());

		StationResult stationResult = getModel().findStationResultById(stationResultId);

		if(stationResult != null) return stationResult;

		stationResult = new StationResult();
		stationResult.setId(stationResultId);
		stationResult.setFirstProductId(getModel().getProductId());

		return stationResult;
	}
	

	public long getDefectTransactionGroupCount(long defectTransactionGroupId) {
		return getModel().getDefectTransactionGroupCount(defectTransactionGroupId);

	}

	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		if (event != null && event.getEventType().equals(ProductEventType.PRODUCT_DEFECT_DONE) 
				&& event.getTargetObject()!=null && event.getTargetObject().toString().equals(ViewId.REPAIR_ENTRY.getViewLabel())){
			//check current tracking status again
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return;
			}

			if(!isNewDefect) {
				saveStationResult();
			}
			resetState();
		}
	}
}
