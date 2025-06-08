package com.honda.galc.client.qi.repairentry;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.qi.base.AbstractQiDefectController;
import com.honda.galc.client.qi.base.AbstractQiDefectProcessModel;
import com.honda.galc.client.qi.base.AbstractQiProcessView;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.ElevatedLoginDialog;
import com.honda.galc.client.ui.ElevatedLoginResult;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.MediaUtils;
import com.honda.galc.client.utils.MicroserviceUtils;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.qi.QiDefectResultImageDao;
import com.honda.galc.dao.qi.QiRepairResultImageDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.qi.QiDefectCategory;
import com.honda.galc.entity.qi.QiDefectDevice;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultImage;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiRepairResultImage;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.SelectionMode;
import javafx.stage.Stage;

public abstract class AbstractRepairEntryController<M extends AbstractQiDefectProcessModel, V extends AbstractQiProcessView<?, ?>> extends AbstractQiDefectController<RepairEntryModel, AbstractRepairEntryView<?,?>> implements EventHandler<ActionEvent> {

	protected List<QiRepairResultDto> mainDefectList;
	protected static final String NO_PROBLEM_FOUND = "NO PROBLEM FOUND";
	protected static final String REAL_PROBLEM_DEF_CATG = "REAL PROBLEM";
	protected static final String INFORMATIONAL_DEF_CATG = "INFORMATIONAL";
	protected static final String REASON_FOR_CHANGE_DELELE = "Actual Problem deleted at Repair Stataion";

	protected long actualProblemDefectResultId;
	protected boolean assignActualProblemFlag;
	protected Date sessionTimestamp;

	public static boolean isNewDefect = false;
	
	private volatile boolean noProblemFound  = false;
	
	private String invalidColumnNames = "";
	
	private List<Field> preCheckFields = new ArrayList<Field>();
	protected volatile boolean mainDefectsOnly = true;

	public AbstractRepairEntryController(RepairEntryModel model, AbstractRepairEntryView<?, ?> view){
		super(model, view);
	}

	/*
	 * This is executed after product is inputed and validated
	 * This method will do the initial data preparation and view preparation
	 * every process controller will be invoked no matter if this is the current controller
	 */
	@Override
	protected void prepareExecute(){
		resetState();
		getView().prepare();
	}
	
	public void resetState()  {
		actualProblemDefectResultId = 0;
		assignActualProblemFlag = false;
		isNewDefect = false;
		noProblemFound = false;
		mainDefectsOnly = false;
		getView().getTreeTablePane().getSelectionModel().clearSelection();
	}
	
	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) event.getSource();

			//check current tracking status again
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return;
			}
			
			//Add the Elevated User Function to Screens
			String elevatedSecurityGroup = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProcessPointId()).getElevatedSecurityGroup();
			if(elevatedSecurityGroup != "" && !elevatedSecurityGroup.isEmpty()) {
				boolean isPasswordRequired = PropertyService.getPropertyBean(ProductCheckPropertyBean.class, getProcessPointId()).isElevatedUserPasswordRequired();
				String reason = "Assembly sequence number out of sequence - Elevated user sign-on required to continue processing";

				ElevatedLoginResult elevatedLoginResult = ElevatedLoginDialog.login(ClientMainFx.getInstance().getStage(getApplicationId()), reason, isPasswordRequired, elevatedSecurityGroup);
				
				if (elevatedLoginResult.isSuccessful()) {
					Logger.getLogger().info("Elevated User (" + elevatedLoginResult.getUserId() + ") logged in successfully");
				} else {
					Logger.getLogger().info("Elevated User ("  + elevatedLoginResult.getUserId() + ") failed to log in due to "
							+ elevatedLoginResult.getMessage());
					return;
				}
			}			

			if (getView().getDefectActualProblemBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				markDefectAsActualProblem();
			} else if (getView().getDeleteActualProblemBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				boolean isOk = MessageDialog.confirm(getView().getStage(), "You are about to delete the selected defect. Do you wish to continue?");
				if (!isOk)
					return;
				deleteActualProblem();
			} else if (getView().getSetActualProblemToNotFixedBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				setActualProblemToNotFixed();
			} else if (getView().getScrapBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				scrapSelectedDefect();
			} else if (getView().getAddRepairMethodBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				noProblemFound =false;
				addRepairMethods();
			} else if (getView().getNoProblemFoundBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				boolean isOk = MessageDialog.confirm(getView().getStage(), "You are about to mark defect as No Problem Found. Do you wish to continue?");
				if (!isOk)
					return;
				//check current tracking status again
				if (getModel().isPreviousLineInvalid()) {
					publishProductPreviousLineInvalidEvent();
					return;
				}
				markNoProblemFound();
			} else if (getView().getAddNewDefectBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				addNewDefect();
			} else if (getView().getAssignActualProblemBtn().getText().equalsIgnoreCase(loggedButton.getText())) {
				assignActualProblemToDefect();
			}
			else if (getView().getUpdateRepairAreaButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				clearMessage();
				
				QiProgressBar qiProgressBar = null;
				RepairAreaMgmtDialog repairAreaMgmtDialog = null;
				try {
					qiProgressBar = QiProgressBar.getInstance("Loading Repair Area Management Screen.","Loading Repair Area Management Screen.",
							getModel().getProductId(),getView().getStage(),true);
					qiProgressBar.showMe();
					repairAreaMgmtDialog = new RepairAreaMgmtDialog("Repair Area Management",
							getModel(), getApplicationId(), 
							new TreeItem<QiRepairResultDto>(getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue()));
				} finally {
					if(qiProgressBar != null)  {
						qiProgressBar.closeMe();
					}
				}
				repairAreaMgmtDialog.showAndWait();
				loadInitData();
			} else if (getView().getUploadImageButton().getText().equalsIgnoreCase(loggedButton.getText())) {
				clearMessage();
				QiProgressBar qiProgressBar = QiProgressBar.getInstance("Uploading file.","Uploading media file ...", "", getView().getStage(), true);;			
				try {
					if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
						File sourceFile = MediaUtils.browseMediaFile();
						if (sourceFile != null) {
							qiProgressBar.showMe();
							uploadImage(sourceFile);
							getView().getTreeTablePane().getSelectionModel().clearSelection();
							disableAllButtons();
							loadInitialData();
						}
					} 
				}
				finally {
					if(qiProgressBar != null)  {
						qiProgressBar.closeMe();
					}
				}
			}
		}
	}
	
	@Subscribe
	public void processEvent(SessionEvent event) {
		try {
			String applicationId = event.getApplicationId();
			if (applicationId == null || getApplicationId().equals(applicationId)) {
				switch(event.getEventType()) {
				case SESSION_START:
					this.sessionTimestamp = event.getEventTime();
					break;
				case SESSION_END:
					this.sessionTimestamp = null;
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}
	
	protected void saveImage(QiRepairResultDto dto, String url) {
		if(dto.getRepairId() == 0) {
			QiDefectResultImage entity = new QiDefectResultImage(dto.getDefectResultId(), url);
			entity.setApplicationId(getCurrentWorkingProcessPointId());
			entity.setCreateUser(this.getApplicationContext().getUserId());
			entity = ServiceFactory.getDao(QiDefectResultImageDao.class).save(entity);
			dto.getDefectResultImages().add(entity);
		} else {
			QiRepairResultImage entity = new QiRepairResultImage(dto.getRepairId(), url);
			entity.setApplicationId(getCurrentWorkingProcessPointId());
			entity.setCreateUser(this.getApplicationContext().getUserId());
			entity = ServiceFactory.getDao(QiRepairResultImageDao.class).save(entity);
			dto.getRepairResultImages().add(entity);
		}
	}

	protected void addRowSelectionListner() {
		getView().getTreeTablePane().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<TreeItem<QiRepairResultDto>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<QiRepairResultDto>> observable,
					TreeItem<QiRepairResultDto> oldValue, TreeItem<QiRepairResultDto> newValue) {
				TreeItem<QiRepairResultDto> selectedTreeItem = getView().getTreeTablePane().getSelectionModel()
						.getSelectedItem();

				// call to Enable/Disable buttons on selections
				if(selectedTreeItem!=null)
				{
					Logger.getLogger().check("Selected defect with description:"+selectedTreeItem.getValue().getDefectDesc());
					if(selectedTreeItem.getValue().getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
						enableAssignActualProblemButtonAfterScrap(); //Enable the "Assign The Actual Product to Defect" Button
					}else if((selectedTreeItem.getParent() != null &&  selectedTreeItem.getParent().getValue().getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId())
							|| getView().isProductScraped) {
						getView().getRepairOptionsPane().setDisable(true); //Child defect of the scrap product is for review only
					}else {
						if(getView().getScrapBtn().isDisable() && getView().getScrapBtn().isVisible() 
								&& !getView().isProductScraped && selectedTreeItem.getValue().getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId()) {
							getView().getScrapBtn().setDisable(false); //enable Scrap button after it was disabled
						}
						enableDisableButtonsOnSelection(selectedTreeItem); //This method only apply for not scrapped products
					}																	
				}	
			}
		});
	}
	
	protected String getProcessPointIdColumnData(QiRepairResultDto qiRepairResult) {
		QiDefectDevice qiDefectDevice = getModel().getDefectDevice(qiRepairResult.getDefectResultId());
		return qiDefectDevice == null ? qiRepairResult.getApplicationId() : qiRepairResult.getApplicationId() + " - " + qiDefectDevice.getDeviceId();
	}
	
	protected String getProcessPointName(String processPointId) {
		ProcessPoint processPoint = getModel().findProcessPoint(processPointId);
		return processPoint == null ? " " : processPoint.getProcessPointName();
	}

	/**
	 * Row change listener for Multi-Select Repairs
	 */
	protected void addRowSelectionListnerForMultiSelect() {
		getView().getTreeTablePane().getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem<QiRepairResultDto>>() {

			@Override
			public void onChanged(Change<? extends TreeItem<QiRepairResultDto>> arg0) {
				ObservableList<TreeItem<QiRepairResultDto>> selectedItems = FXCollections.observableArrayList();
				selectedItems.addAll(getView().getTreeTablePane().getSelectionModel().getSelectedItems());
				selectedItems.removeIf(item -> item == null); //Remove null items		
				if(selectedItems != null && selectedItems.size() > 1) {
					enableDisableButtonsOnSelectionForMultiSelected(selectedItems);
				}else if(selectedItems != null && selectedItems.size() == 1){//Single selected
					TreeItem<QiRepairResultDto> selectedTreeItem = selectedItems.get(0);
					if(selectedTreeItem != null) {
						Logger.getLogger().check("Selected defect with description:"+selectedTreeItem.getValue().getDefectDesc());
						if(selectedTreeItem.getValue().getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
							enableAssignActualProblemButtonAfterScrap(); //Enable the "Assign The Actual Product to Defect" Button
						}else if((selectedTreeItem.getParent() != null &&  selectedTreeItem.getParent().getValue().getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId())
								|| getView().isProductScraped) {
							getView().getRepairOptionsPane().setDisable(true); //Child defect of the scrap product is for review only
						}else {
							if(getView().getScrapBtn().isDisable() && getView().getScrapBtn().isVisible() 
									&& !getView().isProductScraped && selectedTreeItem.getValue().getCurrentDefectStatus() == DefectStatus.NOT_FIXED.getId()) {
								getView().getScrapBtn().setDisable(false); //enable Scrap button after it was disabled when doing multi-select
							}
							enableDisableButtonsOnSelection(selectedTreeItem); //This method only apply for not scrapped products
						}							
					}					
				}else
					disableAllButtons();				
			}			
		});		
	}
	
	/**
	 * Method will  be used to enable/disable repair options based on selected item.
	 * 
	 * @param selectedTreeItem
	 */
	protected void enableDisableButtonsOnSelectionForMultiSelected(ObservableList<TreeItem<QiRepairResultDto>> selectedTreeItems) {
		if (selectedTreeItems != null) {
			Logger.getLogger().info("Multi-Select Repairs...");
			//Disable "Assign the Actual Problem to Defect", "Set Actual Problem To Not Fixed", "Delete Actual Problem", "Scrap" buttons
			getView().getAssignActualProblemBtn().setDisable(true);
			getView().getDeleteActualProblemBtn().setDisable(true);
			getView().getSetActualProblemToNotFixedBtn().setDisable(true);
			getView().getScrapBtn().setDisable(true);
			getView().getUpdateRepairAreaButton().setDisable(true);
			
			boolean mainDefectFlag = selectedTreeItems.stream().anyMatch(item -> item.getValue().getRepairId() == 0);

			boolean fixedFlag = selectedTreeItems.stream().anyMatch(item -> item.getValue().getCurrentDefectStatus() == DefectStatus.FIXED.getId());

			boolean repairMethodAssigned = false;			
			
			boolean disableRepairMethodtn = false;
			boolean disableNoProblemFoundBtn = false; 
			boolean disableDefectActualProblemBtn = false;
			for(TreeItem<QiRepairResultDto> selectedTreeItem : selectedTreeItems) {
				Logger.getLogger().check("Multi-Select Repairs selected defect with description:"+selectedTreeItem.getValue().getDefectDesc());
				if(selectedTreeItem != null && selectedTreeItem.getValue() != null) {
					if (!mainDefectFlag && !fixedFlag) {
						repairMethodAssigned = getModel().isRepairMethodAssigned(selectedTreeItem.getValue().getRepairId());
					}

					String defectCategory = selectedTreeItem.getValue().getDefectCategoryName();
					
					if(fixedFlag) {
						disableRepairMethodtn = true;
						disableNoProblemFoundBtn = true;
						disableDefectActualProblemBtn = true;
						break;
					}

					if(mainDefectFlag && (!selectedTreeItem.getChildren().isEmpty() || QiDefectCategory.SYMPTOM_DEF_CATG.equalsIgnoreCase(defectCategory))) { 
						disableRepairMethodtn = true; 
					}						
					
					if((mainDefectFlag && !selectedTreeItem.getChildren().isEmpty()) || (!mainDefectFlag && !repairMethodAssigned)){
						disableNoProblemFoundBtn = true;
					}

					if (!mainDefectFlag 
							|| (!REAL_PROBLEM_DEF_CATG.equalsIgnoreCase(defectCategory) && !INFORMATIONAL_DEF_CATG.equalsIgnoreCase(defectCategory))
							|| isDefectMarkedAsActualProblem(selectedTreeItem)) {
						disableDefectActualProblemBtn = true;
					}
					if(mainDefectFlag && !fixedFlag)  {
						getView().getAssignActualProblemBtn().setDisable(false);						
					}
					
				}
			}
				
			getView().getAddRepairMethodBtn().setDisable(disableRepairMethodtn);
			getView().getNoProblemFoundBtn().setDisable(disableNoProblemFoundBtn);
			getView().getDefectActualProblemBtn().setDisable(disableDefectActualProblemBtn);				
		}
	}
	
	/**
	 * This method will select and scroll up to latest assign actual problem.
	 */
	protected void selectLatestActualProblem() {
		ObservableList<TreeItem<QiRepairResultDto>> mainDefectList = getView().getTreeTablePane().getRoot().getChildren();
		for (TreeItem<QiRepairResultDto> mainTreeItem : mainDefectList) {
			if (actualProblemDefectResultId == mainTreeItem.getValue().getDefectResultId()) {
				if (mainTreeItem.getChildren() != null)  {
					if(mainTreeItem.getChildren().size() > 0) {
							getView().getTreeTablePane().getSelectionModel().select(mainTreeItem.getChildren().get(0));
					}
					getView().getTreeTablePane().scrollTo(getView().getTreeTablePane().getSelectionModel().getSelectedIndex() - 1);
					mainTreeItem.setExpanded(true);
					enableDisableButtonsOnSelection(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
					actualProblemDefectResultId = 0;
					assignActualProblemFlag = false;
					break;
				}
			}
		}
	}

	/**
	 * Method will  be used to enable/disable repair options based on selected item.
	 * 
	 * @param selectedTreeItem
	 */
	protected void enableDisableButtonsOnSelection(TreeItem<QiRepairResultDto> selectedTreeItem) {
		if (selectedTreeItem != null) {
			boolean mainDefectFlag = selectedTreeItem.getValue().getRepairId() == 0 ? true : false;
			boolean fixedFlag = selectedTreeItem.getValue().getCurrentDefectStatus() == DefectStatus.FIXED.getId() ? true : false;
			boolean currentSessionFlag = sessionTimestamp.compareTo(selectedTreeItem.getValue().getActualTimestamp()) <= 0;
			boolean createUserFlag = getApplicationContext().getUserId().equalsIgnoreCase(selectedTreeItem.getValue().getCreateUser());
			boolean updateUserFlag = getApplicationContext().getUserId().equalsIgnoreCase(selectedTreeItem.getValue().getUpdateUser());

			boolean repairMethodAssigned = false;

			String defectCategory = selectedTreeItem.getValue().getDefectCategoryName();
			
			if (fixedFlag || !REAL_PROBLEM_DEF_CATG.equalsIgnoreCase(defectCategory)) {
				getView().getScrapBtn().setDisable(true);
			}

			if (!mainDefectFlag && !fixedFlag) {
				repairMethodAssigned = getModel().isRepairMethodAssigned(selectedTreeItem.getValue().getRepairId());
			}

			if ((mainDefectFlag && fixedFlag) || (mainDefectFlag && !selectedTreeItem.getChildren().isEmpty())
					|| (mainDefectFlag && QiDefectCategory.SYMPTOM_DEF_CATG.equalsIgnoreCase(defectCategory))) {
				getView().getAddRepairMethodBtn().setDisable(true);
			} else {
				getView().getAddRepairMethodBtn().setDisable(false);
			}
			if (mainDefectFlag || fixedFlag || !currentSessionFlag && !createUserFlag && !updateUserFlag) {
				getView().getDeleteActualProblemBtn().setDisable(true);
			} else {
				getView().getDeleteActualProblemBtn().setDisable(false);
			}
			if (!mainDefectFlag && fixedFlag && !NO_PROBLEM_FOUND.equals(selectedTreeItem.getValue().getDefectTypeName())) {
				getView().getSetActualProblemToNotFixedBtn().setDisable(false);
			} else {
				getView().getSetActualProblemToNotFixedBtn().setDisable(true);
			}
			if (!mainDefectFlag && fixedFlag && NO_PROBLEM_FOUND.equals(selectedTreeItem.getValue().getDefectTypeName())) {
				getView().getDeleteActualProblemBtn().setDisable(false);
			}
			if (mainDefectFlag && !fixedFlag) {
				getView().getAssignActualProblemBtn().setDisable(false);
			} else {
				getView().getAssignActualProblemBtn().setDisable(true);
			}

			if (mainDefectFlag && !fixedFlag && selectedTreeItem.getChildren().isEmpty()) {
				getView().getNoProblemFoundBtn().setDisable(false);
			} else if (!mainDefectFlag && !fixedFlag && repairMethodAssigned) {
				//if the actual problem has repair method assigned but is not completely fixed
				//enable "No Problem Found" button
				getView().getNoProblemFoundBtn().setDisable(false);
			} else {
				getView().getNoProblemFoundBtn().setDisable(true);
			}	

			if (mainDefectFlag && !fixedFlag
					&& (REAL_PROBLEM_DEF_CATG.equalsIgnoreCase(defectCategory) || INFORMATIONAL_DEF_CATG.equalsIgnoreCase(defectCategory))
					&& !isDefectMarkedAsActualProblem(selectedTreeItem)) {
				getView().getDefectActualProblemBtn().setDisable(false);
			} else {
				getView().getDefectActualProblemBtn().setDisable(true);
			}

			getView().getUpdateRepairAreaButton().setDisable(false);	
			getView().getUploadImageButton().setDisable(!MicroserviceUtils.getInstance().isServiceAvailable());	
		}
	}

	/**
	 * This comparator is used to sort defect data based on their fixed status
	 * and alphabetical order..
	 */
	protected Comparator<QiRepairResultDto> defectSortingComparator = new Comparator<QiRepairResultDto>() {
		@Override
		public int compare(QiRepairResultDto object1, QiRepairResultDto object2) {
			int compareResult = object1.getCurrentDefectStatus() - object2.getCurrentDefectStatus();
			if (compareResult == 0) {
				return object2.getActualTimestamp().compareTo(object1.getActualTimestamp());
			}
			return compareResult;
		}
	};

	/**
	 * Method will be used to disable all the buttons.
	 */
	public void disableAllButtons() {
		getView().getDeleteActualProblemBtn().setDisable(true);
		getView().getSetActualProblemToNotFixedBtn().setDisable(true);
		getView().getAddRepairMethodBtn().setDisable(true);
		getView().getAssignActualProblemBtn().setDisable(true);
		getView().getNoProblemFoundBtn().setDisable(true);
		getView().getDefectActualProblemBtn().setDisable(true);
		getView().getScrapBtn().setVisible(getScrapStationConfiguration());
		getView().getUpdateRepairAreaButton().setDisable(true);
		getView().getUploadImageButton().setDisable(true);
		if(getView().getAddNewDefectBtn().isDisable() && !getView().isProductScraped)
			getView().addNewDefectButton.setDisable(false);
		getView().getScrapBtn().setDisable(true);
	}
	
	/**
	 * Enable the Assign the Actual Problem to Defect button after the product has been scrapped
	 */
	public void enableAssignActualProblemButtonAfterScrap() {
		//Enable the "Assign the Actual Problem to Defect" button after scrapped the product 
		//and the "Assign the Actual Problem After Scrap" value is "Yes"
		if(getAssignActualProblemAfterScrapConfiguration())	{
			getView().getRepairOptionsPane().setDisable(false);
			getView().getAssignActualProblemBtn().setDisable(false);		
			getView().getDefectActualProblemBtn().setDisable(true);
			getView().getAddRepairMethodBtn().setDisable(true);
			getView().getNoProblemFoundBtn().setDisable(true);
			getView().getDeleteActualProblemBtn().setDisable(true);
			getView().getSetActualProblemToNotFixedBtn().setDisable(true);
			getView().getScrapBtn().setDisable(true);
			getView().getAddNewDefectBtn().setDisable(true);	
			getView().getUploadImageButton().setDisable(true);	
		}		
	}

	/**
	 * This method will be used to read station configuration for scrap feature.
	 * <br>
	 * If no configuration present then it will read default value.
	 * 
	 */
	private boolean getScrapStationConfiguration() {
		boolean isScrapBtnNeedToBeVisible;
		QiStationConfiguration entryStation = getModel().findEntryStationConfigById("Scrap");
		if (entryStation != null) {
			isScrapBtnNeedToBeVisible = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isScrapBtnNeedToBeVisible = QiEntryStationConfigurationSettings.SCRAP.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isScrapBtnNeedToBeVisible;
	}
	
	/**
	 * This method will be used to read station configuration for "Assign Actual Problem After Scrap" feature.
	 * <br>
	 * If no configuration present then it will read default value.
	 * 
	 */
	public boolean getAssignActualProblemAfterScrapConfiguration() {
		boolean isAssignActualProblemAfterScrap;
		QiStationConfiguration entryStation = getModel().findEntryStationConfigById("Assign Actual Problem After Scrap");
		if (entryStation != null) {
			isAssignActualProblemAfterScrap = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isAssignActualProblemAfterScrap = QiEntryStationConfigurationSettings.ASSIGN_ACTUAL_PROBLEM_AFTER_SCRAP.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isAssignActualProblemAfterScrap;
	}
	/**
	 * This method will be used to read station configuration for "Multi-Select Repairs" feature.
	 * <br>
	 * If no configuration present then it will read default value.
	 * 
	 */
	public boolean getMultiSelectReapirsConfiguration() {
		boolean isMultiSelectRepairs;
		QiStationConfiguration entryStation = getModel().findEntryStationConfigById("Multi-Select Repairs");
		if (entryStation != null) {
			isMultiSelectRepairs = "Yes".equalsIgnoreCase(entryStation.getPropertyValue()) ? true : false;
		} else {
			isMultiSelectRepairs = QiEntryStationConfigurationSettings.MULTI_SELECT_REPAIRS.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isMultiSelectRepairs;
	}
	/**
	 * This method is used to get the property value of "MULTI_SELECT_REPAIRS_FIELDS_PRE_CHECK"
	 * @return
	 */
	public String getMultiSelectRepairsFieldsPreCheck() {
		return getModel().getProperty().getMultiSelectRepairsFieldsPreCheck();
	}
	
	/**
	 * This method is used to get the Field name according to the database column name
	 * @return
	 */
	public List<Field> getMultiSelectRepairsPreCheckMethods(){
		String[] preCheckColumns = StringUtils.split(getMultiSelectRepairsFieldsPreCheck(), Delimiter.COMMA);
		List<Field> correspondingFields = new ArrayList<Field>();
		StringBuilder invalid_columns = new StringBuilder("");  //this is actually invalid names, i.e names declared in pre-check, but not found in Dto
		Field[] allFields = QiRepairResultDto.class.getDeclaredFields();
		if(allFields != null && allFields.length > 0 && preCheckColumns !=null && preCheckColumns.length > 0) {
			for(String columnName: preCheckColumns) {
				String outputName = "";
				boolean validName = false;
				for (Field field : allFields) {		
					DtoTag dtoTag = field.getAnnotation(DtoTag.class);
					if(dtoTag != null) {
						if(!dtoTag.outputName().equals(DtoTag.DEF_UNNASSIGNED)) 
							outputName = dtoTag.outputName();
						if(!dtoTag.name().equals(DtoTag.DEF_UNNASSIGNED)) 
							outputName = dtoTag.name();
						
						if(!outputName.equals("") && outputName.equals(columnName.trim())){	
							correspondingFields.add(field);
							validName = true;
							break;
						}
					}					
				}
				if(!validName) {
					invalid_columns.append(columnName).append(",");
				}
			}
		}		
		if(invalid_columns.toString().compareTo("") != 0) {
			this.setInvalidColumnNames(invalid_columns.toString().substring(0, invalid_columns.length()-1));
			return null;
		}else {
			this.setPreCheckFields(correspondingFields);
			return correspondingFields;
		}
	}		
	
	public String getInvalidColumnNames() {
		return invalidColumnNames;
	}

	public void setInvalidColumnNames(String invalidColumnNames) {
		this.invalidColumnNames = invalidColumnNames;
	}
	
	
	public List<Field> getPreCheckFields() {
		return preCheckFields;
	}

	public void setPreCheckFields(List<Field> preCheckFields) {
		this.preCheckFields = preCheckFields;
	}

	public List<QiRepairResultDto> getMainDefectList() {
		return mainDefectList;
	}

	public void setMainDefectList(List<QiRepairResultDto> mainDefectList) {
		this.mainDefectList = mainDefectList;
	}

	/**
	 * This method will be used to check if product has been scraped.
	 * 
	 * @param defectList
	 */
	protected boolean isProductScrapped(List<QiRepairResultDto> defectList) {
		boolean isProductScraped = false;
		for (QiRepairResultDto dto : defectList) {
			if (DefectStatus.NON_REPAIRABLE.getId() == dto.getCurrentDefectStatus()) {
				isProductScraped = true;
				break;
			}
		}
		return isProductScraped;
	}

	/**
	 * This method is used to set repair time stamp for fixed defects.
	 * 
	 * @param childDefectList
	 */
	protected void setRepairTimeStamp(List<QiRepairResultDto> childDefectList) {
		for (QiRepairResultDto qiRepairResultDto : childDefectList) {
			if (qiRepairResultDto.getCurrentDefectStatus() == DefectStatus.FIXED.getId()) {
				Date repairTimestamp = getModel().findLatestRepairTimestamp(qiRepairResultDto.getRepairId());
				qiRepairResultDto.setRepairTimestamp(repairTimestamp);
			}
		}
	}

	/**
	 * This method will derive latest repair time from all the actual defects.
	 * 
	 * @param childDefectList
	 * @return latestRepairTimestamp
	 */
	protected Date getLatesRepairTimestampForMainDefect(List<QiRepairResultDto> childDefectList) {
		Date latestRepairTimestamp = null;
		if (isAllActualDefectFixed(childDefectList)) {
			for (QiRepairResultDto qiRepairResultDto : childDefectList) {
				Date repairTimestamp = qiRepairResultDto.getRepairTimestamp();

				if (latestRepairTimestamp == null) {
					latestRepairTimestamp = repairTimestamp;
				} else if (latestRepairTimestamp != null && repairTimestamp != null
						&& latestRepairTimestamp.before(repairTimestamp)) {

					latestRepairTimestamp = repairTimestamp;
				}
			}
		}
		return latestRepairTimestamp;
	}

	/**
	 * Method will check if all the actual defects are fixed.
	 * 
	 * @param childDefectList
	 * @return isAllActualDefectFixed
	 */
	private boolean isAllActualDefectFixed(List<QiRepairResultDto> childDefectList) {
		boolean isAllActualDefectFixed = true;
		for (QiRepairResultDto qiRepairResultDto : childDefectList) {
			if (qiRepairResultDto.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
				isAllActualDefectFixed = false;
				break;
			}
		}
		return isAllActualDefectFixed;
	}

	/**
	 * Method will be used to check if main defect has been already marked as
	 * actual problem.
	 * 
	 * @param selectedTreeItem
	 * @return
	 */
	public boolean isDefectMarkedAsActualProblem(TreeItem<QiRepairResultDto> selectedTreeItem) {
		boolean isDefectMarkedAsActualProblem = false;

		QiRepairResultDto mainDefect = selectedTreeItem.getValue();
		List<QiRepairResultDto> actualDefectList = selectedTreeItem.getValue().getChildRepairResultList();

		if (actualDefectList != null && !actualDefectList.isEmpty()) {
			for (QiRepairResultDto actualProblem : actualDefectList) {
				if (mainDefect.getDefectResultId() == actualProblem.getDefectResultId()
						&& mainDefect.getProductId().equalsIgnoreCase(actualProblem.getProductId())
						&& mainDefect.getInspectionPartName().equalsIgnoreCase(actualProblem.getInspectionPartName())) {
					isDefectMarkedAsActualProblem = true;
					break;
				}
			}
		}
		return isDefectMarkedAsActualProblem;
	}

	/**
	 * This method will select and scroll up to given defect result ID in defect table.
	 * 
	 * @param defectResultId
	 */
	protected void expandAndSelectRow(long defectResultId) {
		ObservableList<TreeItem<QiRepairResultDto>> mainDefectList = getView().getTreeTablePane().getRoot().getChildren();
		for (TreeItem<QiRepairResultDto> mainTreeItem : mainDefectList) {
			if (defectResultId == mainTreeItem.getValue().getDefectResultId()) {
				getView().getTreeTablePane().getSelectionModel().select(mainTreeItem);
				getView().getTreeTablePane().scrollTo(getView().getTreeTablePane().getSelectionModel().getSelectedIndex());
				mainTreeItem.setExpanded(true);
				break;
			}
		}
	}

	/**
	 * This method will select and scroll up to latest assign actual problem.
	 * @param defectResultId
	 */
	protected void selectLatestChildDefect(long defectResultId) {
		ObservableList<TreeItem<QiRepairResultDto>> mainDefectList = getView().getTreeTablePane().getRoot().getChildren();
		for (TreeItem<QiRepairResultDto> mainTreeItem : mainDefectList) {
			if (defectResultId == mainTreeItem.getValue().getDefectResultId()) {
				if (mainTreeItem.getChildren() != null && !mainTreeItem.getChildren().isEmpty()) {
					getView().getTreeTablePane().getSelectionModel().select(mainTreeItem.getChildren().get(0));
					mainTreeItem.setExpanded(true);
					enableDisableButtonsOnSelection(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
					break;
				}
			}
		}
	}

	protected void selectActualProblem(long defectResultId, long repairId) {
		ObservableList<TreeItem<QiRepairResultDto>> mainDefectList = getView().getTreeTablePane().getRoot().getChildren();
		for (TreeItem<QiRepairResultDto> mainTreeItem : mainDefectList) {
			if (defectResultId == mainTreeItem.getValue().getDefectResultId()) {
				if (mainTreeItem.getChildren() != null) {
					for (TreeItem<QiRepairResultDto> childTreeItem : mainTreeItem.getChildren()) {
						if (repairId == childTreeItem.getValue().getRepairId()) {
							getView().getTreeTablePane().getSelectionModel().select(childTreeItem);
							mainTreeItem.setExpanded(true);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * This method will be used to add new defect. It will navigate to main
	 * defect entry screen to perform this action.
	 */
	protected void addNewDefect() {
		String defectFromRepair = QiEntryStationConfigurationSettings.IN_REPAIR_ENTRY_PROMPT_FOR_IS_THIS_CAUSED_DURING_REPAIR.getDefaultPropertyValue();
		QiStationConfiguration configEntry =  ServiceFactory.getDao(QiStationConfigurationDao.class)
				.findValueByProcessPointAndPropKey(getProcessPointId(), QiEntryStationConfigurationSettings.IN_REPAIR_ENTRY_PROMPT_FOR_IS_THIS_CAUSED_DURING_REPAIR.getSettingsName());
		if(null != configEntry && !StringUtils.isBlank(configEntry.getPropertyValue())) {
			defectFromRepair = configEntry.getPropertyValue();
		}
		
		boolean isRepairRelated = false;
		String dialogResponse = "";

		if("On".equalsIgnoreCase(defectFromRepair)) {

			dialogResponse = MessageDialog.confirmWithCancel(getView().getStage(), "Is this defect caused during Repair?", true);


			if(!FXOptionPane.Response.CANCEL.name().equalsIgnoreCase(dialogResponse)){

				if(FXOptionPane.Response.YES.name().equalsIgnoreCase(dialogResponse)){  
					isRepairRelated = true;
		}	
			Map<String, Object> repairDetails = new HashMap<String, Object>();
			repairDetails.put("isNewDefect", true);
			repairDetails.put("Repair_Related", isRepairRelated);
			EventBusUtil.publish(new ProductEvent(repairDetails, ProductEventType.PRODUCT_REPAIR_DEFECT));
			}
		}
		else {
			Map<String, Object> repairDetails = new HashMap<String, Object>();
			repairDetails.put("isNewDefect", true);
			repairDetails.put("Repair_Related", false);
			EventBusUtil.publish(new ProductEvent(repairDetails, ProductEventType.PRODUCT_REPAIR_DEFECT));
		}
	}
	

	/**
	 * This method will be used to assign new defect as the actual problem to
	 * main defect.<br>
	 * It will navigate to main defect entry screen to perform this action.
	 */

	protected void assignActualProblemToDefect() {
		QiProgressBar qiProgressBar = null;
		try {
			qiProgressBar = getQiProgressBar("Assigning defect as Actual Problem.","Assigning defect as Actual Problem.");
			qiProgressBar.showMe();
			
			List<TreeItem<QiRepairResultDto>> qiRepairList;
			if (getView().getTreeTablePane().getSelectionModel().getSelectionMode().equals(SelectionMode.SINGLE)) {
				qiRepairList = Arrays.asList(getView().getTreeTablePane().getSelectionModel().getSelectedItem());
			} else {
				qiRepairList = getView().getTreeTablePane().getSelectionModel().getSelectedItems();
			}

			if(qiRepairList == null || qiRepairList.isEmpty())  return;
			List<QiRepairResultDto> allRepairItems = new ArrayList<>();
			Map<String, Object> repairDetails = new HashMap<String, Object>();
			for(TreeItem<QiRepairResultDto> selectedItem : qiRepairList)  {
				if(selectedItem == null) continue;
				QiRepairResultDto qiRepairResultDto = selectedItem.getValue();
				allRepairItems.add(qiRepairResultDto);
			}
			if(!allRepairItems.isEmpty())  {
				actualProblemDefectResultId = allRepairItems.get(0).getDefectResultId();
				repairDetails.put("selectedDefect", allRepairItems.get(0));
				assignActualProblemFlag = true;
				repairDetails.put("isNewDefect", false);
				repairDetails.put("Repair_Related", false);
				repairDetails.put("all_repair_items", allRepairItems);
				EventBusUtil.publish(new ProductEvent(repairDetails, ProductEventType.PRODUCT_REPAIR_DEFECT));
			}
				
		}
		finally  {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}
	
	/**
	 * This method will be used to scrap selected defects.
	 */
	protected void scrapSelectedDefect() {
		if (!getView().getTreeTablePane().getSelectionModel().isEmpty()) {
			QiRepairResultDto selectedDefect = getView().getTreeTablePane().getSelectionModel().getSelectedItem().getValue();
			AbstractScrapDialog dialog = new ScrapDialog(getModel(), selectedDefect, getView().getTreeTablePane(), getApplicationId(), getView().getUserId());
		dialog.showDialog();
		if (dialog.isUnitScraped()) {
			loadInitialData();
		}
		}else {
			getLogger().error("Please select the Defect you want to scrap");
		}
	}

	protected void rollbackNoProblemFoundDefect(QiDefectResult mainDefect, boolean updateReportable) {
		// updating status to 'not fixed' for fixed defect and update status to original status REPORTABLE.
		mainDefect.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
		mainDefect.setUpdateUser(getModel().getUserId());
		if (updateReportable) {
			mainDefect.setReportable((short)QiReportable.REPORTABLE.getId());
		}
		getModel().updateDefectResult(mainDefect);
	}

	protected void updateInitialDefectNoProblemFound(QiDefectResult mainDefect, boolean updateReportable) {
		// updating status to 'fixed' for initial defect
		mainDefect.setCurrentDefectStatus((short) DefectStatus.FIXED.getId());
		mainDefect.setUpdateUser(getModel().getUserId());
		if (updateReportable) {
			mainDefect.setReportable((short)QiReportable.NON_REPORTABLE_BY_NO_PROBLEM_FOUND.getId());
		}
		getModel().updateDefectResult(mainDefect);
		}
	
	public QiImage getImageByImagename(String imageName) {
		return getModel().getImageByImageName(imageName);
	}

	/**
	 * This method will be used to assign repair methods to actual defects.
	 */
	protected abstract void addRepairMethods();

	protected abstract void markDefectAsActualProblem();
	public abstract List<QiRepairResultDto> markDefectAsActualProblem(boolean reload);

	protected abstract void deleteActualProblem();

	protected abstract void setActualProblemToNotFixed();

	protected abstract void loadInitialData();

	protected abstract void markNoProblemFound();

	protected abstract void loadInitData();
	
	protected abstract void uploadImage(File imageFile);

	public  boolean isNoProblemFound() {
		return noProblemFound;
	}

	public void setNoProblemFound(boolean noProblemFound) {
		this.noProblemFound = noProblemFound;
	}
	public boolean isMainDefectsOnly() {
		return mainDefectsOnly;
	}

	public void setMainDefectsOnly(boolean mainDefectsOnly) {
		this.mainDefectsOnly = mainDefectsOnly;
	}

	public void reload() {}
}
