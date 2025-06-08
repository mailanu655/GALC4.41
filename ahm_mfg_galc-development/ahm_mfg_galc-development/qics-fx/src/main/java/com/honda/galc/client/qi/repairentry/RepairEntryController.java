
package com.honda.galc.client.qi.repairentry;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL;
import static com.honda.galc.client.product.action.ProductActionId.SUBMIT;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiResponsibilityMappingDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dto.qi.QiAppliedRepairMethodDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiExternalSystemDefectIdMap;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiRepairAreaSpace;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiRepairResultHist;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;

/**
 * <h3>Class description</h3>
 * <p>
 * <code>RepairEntryController</code>
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>L&T Infotech</TD>
 * <TD>17/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class RepairEntryController extends AbstractRepairEntryController<RepairEntryModel, AbstractRepairEntryView<?,?>>
implements EventHandler<ActionEvent> {

	public RepairEntryController(RepairEntryModel model, RepairEntryView view) {
		super(model, view);
		initProperties();
		EventBusUtil.register(this);
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
		if (assignActualProblemFlag)
			selectLatestActualProblem();
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

	protected void initProperties() {
		boolean isShowKickoutPane = QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)? true : false;
		QiStationConfiguration kickoutStation = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getSettingsName());
		if (null != kickoutStation && kickoutStation.getPropertyValue().equalsIgnoreCase(QiConstant.YES)) {
			isShowKickoutPane = true;
		}
		getModel().setShowKickoutPane(isShowKickoutPane);
	}

	protected void loadInitialData() {

		QiProgressBar qiProgressBar = null;	
		try {
			qiProgressBar = getQiProgressBar("Redirecting to Repair Entry Screen.",
				"Redirecting to Repair Entry Screen.");	
			qiProgressBar.showMe();
			getModel().setCurrentWorkingProcessPointId(getProcessPointId());
			setCurrentWorkingProcessPointId(getProcessPointId());
			getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
			setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
			// if multi_line station load station configuration of qics station associated with that line
			if(isMultiLine())  {
				setQicsStation();
			}

			List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();

			// filter defect results based on setting of previous defect visible

			List<QiStationPreviousDefect> previousDefectList = getModel().findAllByProcessPoint();
			List<QiRepairResultDto> configuredDefectList = new ArrayList<QiRepairResultDto>();
			StringBuilder entryDept = new StringBuilder(StringUtils.EMPTY);
			Map<String, String> configuredPreviousDefect = new HashMap<String, String>();
			for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ){
				configuredPreviousDefect.put(qiStationPreviousDefect.getId().getEntryDivisionId(), qiStationPreviousDefect.getOriginalDefectStatus() + "," + qiStationPreviousDefect.getCurrentDefectStatus());
				if(!StringUtils.isEmpty(entryDept.toString()))
					entryDept.append(",").append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
				else
					entryDept.append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
			}

			if(getModel().getProductId() != null && !StringUtils.isEmpty(entryDept.toString())) {
				defectList = getModel().findAllDefectsByProductIdEntryDepts(getModel().getProductId(), "(" + entryDept.toString() + ")");
			}
			if (!defectList.isEmpty()) {
				for(QiRepairResultDto qiDefectResult : defectList ){
					for(Map.Entry<String, String>  map:configuredPreviousDefect.entrySet()){
						if(map.getKey().toString().equalsIgnoreCase(qiDefectResult.getEntryDept())){
							String statusString = map.getValue();
							int index = statusString.indexOf(",");
							short originalStatus = new Short(statusString.substring(0, index)).shortValue();
							short currentStatus = new Short(statusString.substring(index + 1)).shortValue();
							if (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == (short)DefectStatus.ALL.getId()
									|| (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == qiDefectResult.getCurrentDefectStatus()) 
									|| (currentStatus == (short)DefectStatus.ALL.getId() && originalStatus == qiDefectResult.getOriginalDefectStatus())
									|| (originalStatus == qiDefectResult.getOriginalDefectStatus() && currentStatus == qiDefectResult.getCurrentDefectStatus())) {
								configuredDefectList.add(qiDefectResult);
							}
						}
					}
				}	
			}

			// sorting data based on defect status
			Collections.sort(configuredDefectList, defectSortingComparator);
			// fetch repair defect hierarchy
			for (QiRepairResultDto dto : configuredDefectList) {
				long defectResultId = dto.getDefectResultId();
				String kickoutProcessPointId = dto.getKickoutProcessPointId();
				if(!StringUtil.isNullOrEmpty(kickoutProcessPointId)) {
					ProcessPoint processPoint = getModel().findProcessPoint(kickoutProcessPointId);
					if(processPoint != null) {
						dto.setKickoutProcessPointName(getModel().findProcessPoint(kickoutProcessPointId).getProcessPointName());
					}
				}
				// call to load all repair result belongs to this defect
				List<QiRepairResultDto> childDefectList = getModel().findAllRepairEntryDefectsByDefectId(defectResultId);

				Collections.sort(childDefectList, defectSortingComparator);

				setRepairTimeStamp(childDefectList);
				dto.setChildRepairResultList(childDefectList);
				dto.setRepairTimestamp(getLatesRepairTimestampForMainDefect(childDefectList));
			}
			getView().getTreeTablePane().getRoot().getChildren().clear();
			if (getProductModel() != null && getProductModel().isTrainingMode()) {
				if(AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
					AbstractRepairEntryView.getParentCachedDefectList().addAll(configuredDefectList);
				}
				Collections.sort(AbstractRepairEntryView.getParentCachedDefectList(), defectSortingComparator);
				setTableContent(AbstractRepairEntryView.getParentCachedDefectList(), getView().getTreeTablePane().getRoot().getChildren());
				setMainDefectList(AbstractRepairEntryView.getParentCachedDefectList());
			}else{
				setTableContent(configuredDefectList, getView().getTreeTablePane().getRoot().getChildren());
				setMainDefectList(configuredDefectList);
			}
			boolean isProductScraped = isProductScrapped(configuredDefectList);
			getView().getRepairOptionsPane().setDisable(isProductScraped);
			getView().setProductScraped(isProductScraped);
			if (isProductScraped) {
					EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED, StatusMessageEventType.WARNING));
			}
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	@Override
	public void reload() {

		if(getModel() == null || getModel().getProductId() == null || StringUtils.isBlank(getModel().getProductId()))  return;

		QiProgressBar qiProgress = null;	
		try {
			qiProgress = getQiProgressBar("Redirecting to Repair Entry Screen.",
					"Redirecting to Repair Entry Screen.");	
			qiProgress.showMe();
			reloadTask();
		}
		finally  {
			if(qiProgress != null)  {
				qiProgress.closeMe();
			}
		}

	}

	public void reloadTask() {
				List<QiRepairResultDto> defectList = new ArrayList<QiRepairResultDto>();

				// filter defect results based on setting of previous defect visible

				List<QiStationPreviousDefect> previousDefectList = getModel().findAllByProcessPoint();
				List<QiRepairResultDto> configuredDefectList = new ArrayList<QiRepairResultDto>();
				StringBuilder entryDept = new StringBuilder(StringUtils.EMPTY);
				Map<String, String> configuredPreviousDefect = new HashMap<String, String>();
				for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ){
					configuredPreviousDefect.put(qiStationPreviousDefect.getId().getEntryDivisionId(), qiStationPreviousDefect.getOriginalDefectStatus() + "," + qiStationPreviousDefect.getCurrentDefectStatus());
					if(!StringUtils.isEmpty(entryDept.toString()))
						entryDept.append(",").append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
					else
						entryDept.append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
				}

				if(!StringUtils.isEmpty(entryDept.toString())) {
					defectList = getModel().findAllDefectsByProductIdEntryDepts(getModel().getProductId(), "(" + entryDept.toString() + ")");
				}
				if (!defectList.isEmpty()) {
					for(QiRepairResultDto qiDefectResult : defectList ){
						for(Map.Entry<String, String>  map:configuredPreviousDefect.entrySet()){
							if(map.getKey().toString().equalsIgnoreCase(qiDefectResult.getEntryDept())){
								String statusString = map.getValue();
								int index = statusString.indexOf(",");
								short originalStatus = new Short(statusString.substring(0, index)).shortValue();
								short currentStatus = new Short(statusString.substring(index + 1)).shortValue();
								if (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == (short)DefectStatus.ALL.getId()
										|| (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == qiDefectResult.getCurrentDefectStatus()) 
										|| (currentStatus == (short)DefectStatus.ALL.getId() && originalStatus == qiDefectResult.getOriginalDefectStatus())
										|| (originalStatus == qiDefectResult.getOriginalDefectStatus() && currentStatus == qiDefectResult.getCurrentDefectStatus())) {
									configuredDefectList.add(qiDefectResult);
								}
							}
						}
					}	
				}

				// sorting data based on defect status
				Collections.sort(configuredDefectList, defectSortingComparator);
				// fetch repair defect hierarchy
				for (QiRepairResultDto dto : configuredDefectList) {
					long defectResultId = dto.getDefectResultId();
					String kickoutProcessPointId = dto.getKickoutProcessPointId();
					if(!StringUtil.isNullOrEmpty(kickoutProcessPointId)) {
						ProcessPoint processPoint = getModel().findProcessPoint(kickoutProcessPointId);
						if(processPoint != null) {
							dto.setKickoutProcessPointName(getModel().findProcessPoint(kickoutProcessPointId).getProcessPointName());
						}
					}
					// call to load all repair result belongs to this defect
					List<QiRepairResultDto> childDefectList = getModel().findAllRepairEntryDefectsByDefectId(defectResultId);

					Collections.sort(childDefectList, defectSortingComparator);

					setRepairTimeStamp(childDefectList);
					dto.setChildRepairResultList(childDefectList);
					dto.setRepairTimestamp(getLatesRepairTimestampForMainDefect(childDefectList));
				}
				getView().getTreeTablePane().getRoot().getChildren().clear();
				if (getProductModel() != null && getProductModel().isTrainingMode()) {
					if(AbstractRepairEntryView.getParentCachedDefectList().isEmpty()){
						AbstractRepairEntryView.getParentCachedDefectList().addAll(configuredDefectList);
					}
					Collections.sort(AbstractRepairEntryView.getParentCachedDefectList(), defectSortingComparator);
					setTableContent(AbstractRepairEntryView.getParentCachedDefectList(), getView().getTreeTablePane().getRoot().getChildren());
					setMainDefectList(AbstractRepairEntryView.getParentCachedDefectList());
				}else{
					setTableContent(configuredDefectList, getView().getTreeTablePane().getRoot().getChildren());
					setMainDefectList(configuredDefectList);
				}
				boolean isProductScrapped = isProductScrapped(configuredDefectList);
				getView().getRepairOptionsPane().setDisable(isProductScrapped);
				getView().setProductScraped(isProductScrapped);
				if (isProductScrapped)
					EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED, StatusMessageEventType.WARNING));
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

		for (QiRepairResultDto repairResultDto : repairResultList) {

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

	protected void markDefectAsActualProblem()  {
		markDefectAsActualProblem(true);
	}
	/*
	 * This method will be used to mark defect as actual problem and create for Multi-Select or Single selected defect(s)
	 * entry in repair result table and will form a hierarchy.
	 */
	public List<QiRepairResultDto> markDefectAsActualProblem(boolean isReload) {

		QiProgressBar qiProgressBar = null;
	// check if any row selected
		try {
			List<QiRepairResultDto> repairedList = new ArrayList<QiRepairResultDto>();
			if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			qiProgressBar = getQiProgressBar("Marking defect as Actual Problem.",	"Marking defect as Actual Problem.");
			qiProgressBar.showMe();
			List<Long> selectedDefectResultIds = new ArrayList<Long>();
			if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
				if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
					ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
					if(selectedItems != null && selectedItems.size() > 0) {
						for(TreeItem<QiRepairResultDto> item : selectedItems) {
							if(item != null && item.getValue() != null) {
								QiRepairResultDto selectedDefect = item.getValue();
								repairedList.add(markDefectAsActualProblem(selectedDefect));
								selectedDefectResultIds.add(selectedDefect.getDefectResultId());
							}					
						}
					}
				}else {
					QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
					repairedList.add(markDefectAsActualProblem(selectedDefect));
					selectedDefectResultIds.add(selectedDefect.getDefectResultId());
				}
			}

			if(isReload)  {
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
			}
			return repairedList;
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	private QiRepairResultDto markDefectAsActualProblem(QiRepairResultDto selectedDefect) {
		QiRepairResultDto resultDto = null;
		//check if there is already a child defect, this will happen if a second repair method is being added (previous was not-completely fixed)
		//if there is an existing child defect, return reference to that, do not add new actual problem
		if(selectedDefect != null)  {
			List<QiRepairResultDto> existingActualProblems = selectedDefect.getChildRepairResultList();
			if(existingActualProblems != null && !existingActualProblems.isEmpty() && existingActualProblems.get(0) != null)  {
				return selectedDefect;
			}
		}
		if(!getProductModel().isTrainingMode()){
			QiDefectResult mainDefect = getModel().findMainDefectByDefectId(selectedDefect.getDefectResultId());
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
			qiRepairResult.setActualProblemSeq((short) (selectedDefect.getChildRepairResultList() != null
					? selectedDefect.getChildRepairResultList().size() + 1 : 1));
			if (isFrameQicsEngineSource()) {
				String respLevel = checkEngineSource(qiRepairResult);
				if (respLevel != null) qiRepairResult.setResponsibleLevel1(respLevel);
			}
			qiRepairResult.setDefectTransactionGroupId(mainDefect.getDefectTransactionGroupId());
			qiRepairResult = getModel().createRepairResult(qiRepairResult);
			resultDto = new QiRepairResultDto(mainDefect, qiRepairResult.getRepairId());
			resultDto.setDivisionName(selectedDefect.getDivisionName());
			resultDto.setActualTimestamp(new Timestamp(qiRepairResult.getActualTimestamp().getTime()));
			selectedDefect.getChildRepairResultList().add(resultDto);
		}
		else{
			QiDefectResult mainDefect = new QiDefectResult(selectedDefect);
			long maxRepairId = getModel().findMaxRepairId();
			resultDto = new QiRepairResultDto(mainDefect,maxRepairId+1);
			resultDto.setActualTimestamp(getDao(QiDefectResultDao.class).getDatabaseTimeStamp());
			resultDto.setDivisionName(selectedDefect.getDivisionName());
			List<QiRepairResultDto> childList = new ArrayList<QiRepairResultDto>();
			if(selectedDefect.getChildRepairResultList()==null)
				selectedDefect.setChildRepairResultList(childList);
			selectedDefect.getChildRepairResultList().add(resultDto);
		}
		return selectedDefect;
	}

	/**
	 * This method will be used to delete selected defect marked as actual
	 * problem.
	 */
	protected void deleteActualProblem() {

		// check if any row selected
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			QiProgressBar qiProgressBar = null;
			try {
				qiProgressBar = getQiProgressBar("Deleting defect marked as Actual Problem.",	"Deleting defect marked as Actual Problem.");
				qiProgressBar.showMe();
				QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
				int selectedIndex = getView().getTreeTablePane().getSelectionModel().getSelectedIndex();
				long repairId = selectedDefect.getRepairId();

				// check if repair method exist
				List<QiAppliedRepairMethodDto> appliedRepairMethodList = getModel().getAppliedRepairMethodData(repairId);
				if (appliedRepairMethodList != null && !appliedRepairMethodList.isEmpty()) {
					qiProgressBar.updateMessage("Actual Problem cannot be deleted.");
					MessageDialog.showError(ClientMainFx.getInstance().getStage(getCurrentWorkingProcessPointId()), "Actual Problem has associated repair methods hence cannot be deleted.");
					return;
				}

				long defectResultId = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getParent().getValue().getDefectResultId();
				List<QiRepairResultDto> actualDefectList = getModel().findAllRepairEntryDefectsByDefectId(defectResultId);

				boolean updateMainDefectResultStatus = false;
				if (actualDefectList.size() > 1 && isAnyActualDefectFix(actualDefectList) && getTotalNotFixedDefectCount(actualDefectList) == 1) {
					boolean isOk = MessageDialog.confirm(getView().getStage(),	"This deletion will change the main defect status to fixed. Do you wish to continue?");
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
					}
				} 
				if(actualDefectList.size()==1) {
					QiDefectResult qiDefectResult = getModel().findMainDefectByDefectId(defectResultId);
					rollbackNoProblemFoundDefect(qiDefectResult, true);
					// Updating status into historical tables.	
					
					QiDefectResultHist defectHist = new QiDefectResultHist(qiDefectResult);
					defectHist.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
					defectHist.setComment(QiConstant.EMPTY);
					ServiceFactory.getDao(QiDefectResultHistDao.class).insert(defectHist);

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

				} else {
					removeSelectedDefectFromCache(selectedDefect);
				}

				//replicate to old QICS
				if (!getProductModel().isTrainingMode() && updateMainDefectResultStatus) {
					if (PropertyService.getPropertyBean(QiPropertyBean.class, selectedDefect.getApplicationId()).isReplicateDefectRepairResult()) {
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

				// call to refresh defect table
				loadInitialData();
				getView().getTreeTablePane().getSelectionModel().select(selectedIndex - 1);
				getView().getTreeTablePane().getSelectionModel().getSelectedItem().setExpanded(true);
				enableDisableButtonsOnSelection(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
			}
			finally {
				if(qiProgressBar != null)  {
					qiProgressBar.closeMe();
				}
			}
		}
	}

	/**
	 * @param selectedDefect
	 */
	private void removeSelectedDefectFromCache(QiRepairResultDto selectedDefect) {
		QiRepairResultDto parentDto = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getParent().getValue();
		parentDto.getChildRepairResultList().remove(selectedDefect);
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
	 * This method will be used to change the defect status from fixed to not
	 * fixed.
	 */
	protected void setActualProblemToNotFixed() {

		// check if any row selected
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {

			QiProgressBar qiProgressBar = null;

			try {
				qiProgressBar = getQiProgressBar("Setting Defect status from Fixed to Not Fixed.",
						"Setting Defect status from Fixed to Not Fixed.");
				qiProgressBar.showMe();
				QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
				if(!getProductModel().isTrainingMode()){
					long repairId = selectedDefect.getRepairId();

					QiRepairResult qiRepairResult = getModel().findRepairResultById(repairId);

					qiRepairResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
					qiRepairResult.setUpdateUser(getModel().getUserId());

					// call to update repair entry defect status
					getModel().updateRepairResult(qiRepairResult);

					// call to update status of all the associated repair methods
					getModel().updateAllRepairMethodStatus(repairId, false);

					// updating status to 'not fixed' for initial defect
					long defectResultId = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getParent().getValue().getDefectResultId();
					getModel().updateMainDefectResultStatus(defectResultId,	DefectStatus.NOT_FIXED.getId());

					try {
						getModel().updateDefectStatus(selectedDefect.getDefectResultId());
						QiExternalSystemDefectIdMap extDefectMap = getModel()
								.findExternalSystemDefectIdMap(qiRepairResult.getDefectResultId());
						if (extDefectMap != null && extDefectMap.getId() != null && !extDefectMap.isExtSysRepairReqd())  {
							getModel().callExternalSystemService(qiRepairResult);
						}
					} catch (Exception e) {
						handleException("Exception trying to check external system info", "Unable to call lot control service", e);
					}	
					if (PropertyService.getPropertyBean(QiPropertyBean.class, selectedDefect.getApplicationId()).isReplicateDefectRepairResult()) {
						// delete defect from GAL222TBX
						getModel().deleteOldRepairResult(repairId);

						// update defect status in GAL125TBX
						getModel().updateOldDefectStatus(defectResultId, DefectStatus.OUTSTANDING.getId(), null, "");
					}
				}else{
					selectedDefect.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
				}

				// call to refresh defect table
				loadInitialData();
				disableAllButtons();
			} finally {
				if(qiProgressBar != null)  {
					qiProgressBar.closeMe();
				}
			}		
		}
	}

	/**
	 * This method will be used to mark defect as no problem found.
	 * 
	 */
	@Override
	protected void markNoProblemFound() {
		// check if any row selected
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {

			QiProgressBar qiProgressBar = null;
			try {
				qiProgressBar = getQiProgressBar("Marking Defect as No Problem Found.", "Marking Defect as No Problem Found.");
				qiProgressBar.showMe();

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

				// call to refresh defect table
				loadInitialData();
				disableAllButtons();
			} finally {
				if(qiProgressBar != null)  {
					qiProgressBar.closeMe();
				}
			}		
		}
	}

	private void markNoProblemFound(QiRepairResultDto selectedDefect) {
		if(!getProductModel().isTrainingMode()){
			QiDefectResult mainDefect = getModel().findMainDefectByDefectId(selectedDefect.getDefectResultId());
			mainDefect.setKickoutProcessPointName(selectedDefect.getKickoutProcessPointName());
			long repairId = selectedDefect.getRepairId();
			QiRepairResult repairResult = null;
			if (repairId > 0) {
				//update selected repair result
				QiRepairResult existingQiRepairResult = getModel().findRepairResultById(repairId);
				existingQiRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
				existingQiRepairResult.setDefectTypeName2("");
				existingQiRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
				existingQiRepairResult.setUpdateUser(getModel().getUserId());
				existingQiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
				existingQiRepairResult.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
				existingQiRepairResult.setTerminalName(getModel().getTerminalName());
				getModel().updateRepairResult(existingQiRepairResult);
				repairResult = existingQiRepairResult;

				//if the repair result has repair method applied but not completely fixed, 
				//create a record in QI_APPLIED_REPAIR_METHOD_TBX with REPAIR_METHOD="NO PROBLEM FOUND" and IS_COMPLETELY_FIXED=1
				getModel().createNoProblemFoundAppliedRepairMethod(repairId);

				List<QiRepairResult> repairResultList = RepairEntryModel.findAllRepairResultByDefectResultId(existingQiRepairResult.getDefectResultId());
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
					updateInitialDefectNoProblemFound(mainDefect, areAllRepairResultsNoProblemFound);
				}
			} else {

				updateInitialDefectNoProblemFound(mainDefect, true);

				// call to populate and save QiRepairResult
				QiRepairResult qiRepairResult = new QiRepairResult(mainDefect); 
				qiRepairResult.setCreateUser(getModel().getUserId());
				qiRepairResult.setActualProblemSeq((short) (selectedDefect.getChildRepairResultList() != null
						? selectedDefect.getChildRepairResultList().size() + 1 : 1));
				qiRepairResult.setDefectTypeName(NO_PROBLEM_FOUND);
				qiRepairResult.setDefectTypeName2("");
				qiRepairResult.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
				qiRepairResult.setProductionDate(getModel().getProductionDate());
				qiRepairResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
				qiRepairResult.setTerminalName(getModel().getTerminalName());
				if (isFrameQicsEngineSource()) {
					String respLevel = checkEngineSource(qiRepairResult);
					if (respLevel != null) qiRepairResult.setResponsibleLevel1(respLevel);
				}
				repairResult = getModel().createRepairResult(qiRepairResult);
			}

			try {
				getModel().updateDefectStatus(selectedDefect.getDefectResultId());
				QiExternalSystemDefectIdMap extDefectMap = getModel()
						.findExternalSystemDefectIdMap(repairResult.getDefectResultId());
				if (extDefectMap != null && extDefectMap.getId() != null && !extDefectMap.isExtSysRepairReqd())  {
					getModel().callExternalSystemService(repairResult);
				}
			} catch (Exception e) {
				handleException("Exception trying to check external system info", "Unable to call lot control service", e);
			}	

			QiDefectResultHist defectHist = new QiDefectResultHist(mainDefect);
			defectHist.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
			defectHist.setComment(NO_PROBLEM_FOUND);
			ServiceFactory.getDao(QiDefectResultHistDao.class).insert(defectHist);

			try {
				String message = getModel().addUnitToConfiguredRepairArea(selectedDefect.getDefectResultId());
				if(!StringUtils.isEmpty(message)) {
					EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
					loadInitData();
				}
			} catch (Exception e) {
				handleException("Exception occured in markNoProblemFound() method", "Failed to add unit to configured Repair Area", e);
			}

			// update defect_status to 1 repaired in GAL125TBX
			if (PropertyService.getPropertyBean(QiPropertyBean.class, selectedDefect.getApplicationId()).isReplicateDefectRepairResult()) {
				getModel().updateOldDefectStatus(mainDefect.getDefectResultId(), DefectStatus.REPAIRED.getId(), getModel().getProcessPointTimeStamp(), getModel().getUserId());
			}

			if (PropertyService.getPropertyBean(QiPropertyBean.class, getModel().getProcessPointId()).isUpdateNextProcess() &&
					!PropertyService.getPropertyBoolean(selectedDefect.getApplicationId(),"IS_NAQ_STATION", false)) {
				getModel().updateNextProcessTracking(getModel().getProduct());
			}
		}else{
			selectedDefect.setDefectTypeName(NO_PROBLEM_FOUND);
			selectedDefect.setDefectTypeName2("");
			selectedDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		}
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
			e.printStackTrace();
		}
	}

	/**
	 * This method will be used to assign repair methods to actual defects.
	 */
	protected void addRepairMethods() {
		QiRepairResultDto selectedItem = null;
		AddRepairMethodDialog dialog = null;

		QiProgressBar qiProgressBar = null;
		try {
			qiProgressBar = getQiProgressBar("Loading Repair Method Dialog.", "Loading Repair Method Dialog.");
			qiProgressBar.showMe();

			selectedItem = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
			boolean isFixedDefect = selectedItem.getCurrentDefectStatus() == DefectStatus.FIXED.getId();
			List<Long> repairIds = new ArrayList<Long>();
			//Find all selected defects
			List<QiRepairResultDto> allSelectedDefects = new ArrayList<QiRepairResultDto>();
			if(getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.MULTIPLE)) {
				ObservableList<TreeItem<QiRepairResultDto>> selectedItems = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
				if(selectedItems != null && selectedItems.size() > 0) {
					for(TreeItem<QiRepairResultDto> item : selectedItems) {
						if(item != null && item.getValue() != null) {
							allSelectedDefects.add(item.getValue());
							if(item.getValue().getRepairId() > 0)  {
								repairIds.add(item.getValue().getRepairId());
							}
						}					
					}
				}
			}
			else {
				allSelectedDefects.add(selectedItem);
				repairIds.add(selectedItem.getRepairId());
			}

			dialog = new AddRepairMethodDialog("Add Repair Method For Selected Actual Problem",
					getModel(), selectedItem, getView().getStage(), isFixedDefect,isNoProblemFound(), allSelectedDefects, sessionTimestamp, repairIds, this);
			dialog.getAddRepairMethodController().setProductModel(getProductModel());
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
		dialog.showDialog();
		// call to refresh defect table
		if (dialog.isCompletelyFixed()) { //actual problem is completely fixed
			loadInitialData();
			disableAllButtons();
			loadInitData();
			if (!dialog.isParentDefectFixed())  { //original defect is not fixed
				expandAndSelectRow(selectedItem.getDefectResultId());
			}
			else  {
				getView().getTreeTablePane().getSelectionModel().clearSelection();
			}
		} else { //actual problem is not completely fixed, refresh screen and select the actual problem to enable/disable buttons
			loadInitialData();
			selectActualProblem(selectedItem.getDefectResultId(), selectedItem.getRepairId());
		}
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

	private String checkEngineSource(QiRepairResult qiRepairResult) {
		List<QiResponsibilityMapping> listRespLevel = ServiceFactory.getDao(QiResponsibilityMappingDao.class).findAll();
		if (listRespLevel.isEmpty()) return null;
		QiResponsibleLevel respLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
				.findBySitePlantDepartmentAndLevelName(qiRepairResult.getResponsibleSite(),
						qiRepairResult.getResponsiblePlant(), qiRepairResult.getResponsibleDept(),
						qiRepairResult.getResponsibleLevel1());
		if (respLevel == null) return null;
		Product product = (Product)getModel().getProduct();
		Frame vehicle = (Frame) product;
		if (vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty()) return null;
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
		if (engine == null) return null;
		for (QiResponsibilityMapping each : listRespLevel) {
			if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel()) &&
					each.getPCode().equals(engine.getPlantCode())) {
				QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
						.findByResponsibleLevelId(each.getAlternateDefault());
				if (altRespLevel == null) break;
				return altRespLevel.getResponsibleLevelName();
			}
		}
		return null;
	}

	@Override
	protected void uploadImage(File sourceFile) {
		QiRepairResultDto dto = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
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

	private void setOnExit(Task<?> task, EventHandler<WorkerStateEvent> e) {
		task.setOnSucceeded(e);
		task.setOnFailed(e);
		task.setOnCancelled(e);
	}

}

