package com.honda.galc.client.qi.defectentry;

import static com.honda.galc.client.product.action.ProductActionId.DIRECTPASS;
import static com.honda.galc.client.product.action.ProductActionId.KEYBOARD;
import static com.honda.galc.client.product.action.ProductActionId.SUBMIT;
import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.data.QiCommonDefectResult;
import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.enumtype.ProductEventType;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.qi.base.AbstractQiDefectController;
import com.honda.galc.client.qi.repairentry.AbstractRepairEntryController;
import com.honda.galc.client.qi.repairentry.AbstractRepairEntryView;
import com.honda.galc.client.qi.repairentry.RepairEntryController;
import com.honda.galc.client.qi.repairentry.RepairEntryModel;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.AbstractTileListViewBehaviour;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TileListView;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.ui.event.SessionEvent;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.QiInspectionUtils;
import com.honda.galc.client.utils.QiProgressBar;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.qi.QiDefectIqsScoreDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.dao.qi.QiDefectResultHistDao;
import com.honda.galc.dao.qi.QiExternalSystemDefectMapDao;
import com.honda.galc.dao.qi.QiResponsibleLevelDao;
import com.honda.galc.dao.qi.QiStationConfigurationDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.SubId;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.KickoutDto;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.dto.qi.QiStationResponsibilityDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.IqsAuditAction;
import com.honda.galc.entity.enumtype.IqsScore;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.enumtype.QiEntryStationDefaultStatus;
import com.honda.galc.entity.enumtype.QiReportable;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.entity.qi.QiDefectIqsScore;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiDefectResultHist;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiEntryModelGrouping;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiRepairResult;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationWriteUpDepartment;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.DefectResultId;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.entity.qics.StationResultId;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.HeadlessNaqService;
import com.honda.galc.service.QiClearParking;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.defect.ScrapService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <h3>DefectEntryController Class description</h3>
 * <p> DefectEntryController description </p>
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
 * @author L&T Infotech<br>
 * Nov 26, 2016
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class DefectEntryController extends AbstractQiDefectController<DefectEntryModel, DefectEntryView> implements EventHandler<ActionEvent>  {

	private static final String CHANGING_RESPONSIBILITY_MSG = "Are you sure you want to change responsibility?";
	private static final String DEFECT_SUCCESSFULLY_ACCEPTED = "Part Defect Comb successfully accepted";
	private static final String PRODUCT_SCRAP_ERROR_MSG = "You are about to scrap the selected product. Do you wish to continue?";
	private static final String SELECT_DEFECT_STATUS_ERROR_MSG = "Please select Defect Status";
	private static final String SELECT_RESPONSIBLE_LEVEL1_ERROR_MSG = "Please select Responsible Level 1";
	public static final String SELECT_RESPONSIBLE_LEVEL2_ERROR_MSG = "Please select Responsible Level 2 and level 3";
	public static final String RESPONSIBLE_LVL_MSG_ID = "responsible-level";
	private static final String SELECT_RESPONSIBLE_DEPT_ERROR_MSG = "Please select Responsible Department";
	private static final String SELECT_RESPONSIBLE_PLANT_ERROR_MSG = "Please select Responsible Plant";
	private static final String SELECT_RESPONSIBLE_SITE_ERROR_MSG = "Please select Responsible Site";
	private static final String SELECT_WRITE_UP_DEPT_ERROR_MSG = "Please select Write Up Department";
	private static final String SELECT_PART_DEFECT_ERROR_MSG = "Please select Part Defect Comb";
	private static final String DUPLICATE_PART_DEFECT_REJECT_ERROR_MSG = "Warning! Selected Part Defect Comb is already associated with the product.";
	private static final String DUPLICATE_PART_DEFECT_ACCEPT_MSG = "Selected Part Defect Comb is already associated with the product.Do you wish to continue?";
	private static final String PRODUCT_SHIPPED_MSG = "Product has already been shipped. Failed to Save Defect Results.";
	private static final String REASON_FOR_CHANGE_RESP_CHANGED_BY_ACTUAL_PROBLEM = "Responsibility is changed by Actual Problem";
	private static final String ENTRY_SCREEN_NOT_SETUP_MSG = "Entry Screen is not set up for this model at this station. Please contact QICS administrator.";
	
	private Map<String, Object> repairDefectDetails;
	private QiDefectResultDto selectedDefect;
	private QiStationConfiguration qiEntryStationConfigManagement;
	private boolean eventHandler;
	private double pointX;
	private double pointY;
	public boolean assignRealProblem;

	private volatile boolean selectValue = false;
	private String part1TextFilter;
	private FxDialog acceptDialog;
	private List<QiStationResponsibilityDto> assignedStationResponsibilities;
	private DefectEntryByTextController defectEntryByTextController;
	private DefectEntryByImageController defectEntryByImageController;
	ResponsibleLevelController respController = null;

	private int numOfEntryScreenCols = 2;
	private static final String SELECT_DEFECT_STATUS_WRITE_UP_DEPT_ERROR_MSG = "Please select Defect Status and Write Up Dept.";
	private boolean isRepairRelated = false;
	private boolean popupAccept = false; //is the pop-up accept enabled?
	private boolean acceptButton = true;  //is the accept button used at all? false=automatic_defect_entry
	private boolean isDefectEntered = false;
	private static final String EXISTING_DEFECTS = "Existing Defects";
	private static final String ACCEPT = "Accept";
	private static final String RESET = "Reset";
	private static final String RECENT_DEFECTS = "Recent Defects";
	private boolean isActualProblemToDefect = false;
	private boolean isShowL2L3 = false;
	private boolean isResponsibilityAccessible = false;
	private boolean isIqsInput = false;
	private boolean isIqsAuditActionInput = false;
	private boolean isShowKickoutPane = false;
	private boolean isPlayNgSoundForBadDFScan = false;

	private String selectedPartName;
	private List<String> defectEntryDefectStatusList;
	private volatile boolean isListInitialized = false;

	public DefectEntryController(DefectEntryModel model,
			DefectEntryView view) {
		super(model, view);
		eventHandler = false;
		EventBusUtil.register(this);
		defectEntryByTextController = new DefectEntryByTextController(this);
		defectEntryByImageController = new DefectEntryByImageController(this);
		respController = ResponsibleLevelController.getInstance(model, view, getCurrentWorkingProcessPointId());
		QiStationConfiguration hasAccept = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.SHOW_ACCEPT_BUTTON.getSettingsName());
		if (hasAccept != null && hasAccept.getPropertyValue().equalsIgnoreCase(QiConstant.NO)) {
			acceptButton = false;
		}
		QiStationConfiguration showPopup = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.DISABLE_ACCEPT_DEFECT.getSettingsName());
		if (null != showPopup && showPopup.getPropertyValue().equalsIgnoreCase(QiConstant.YES))  {
			popupAccept = true;
		}
		isShowL2L3 = QiEntryStationConfigurationSettings.SHOW_L2_L3.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		QiStationConfiguration showL2L3 = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.SHOW_L2_L3.getSettingsName());
		if (null != showL2L3 && showL2L3.getPropertyValue().equalsIgnoreCase(QiConstant.YES))  {
			isShowL2L3 = true;
		}
		isResponsibilityAccessible = QiEntryStationConfigurationSettings.RESPONSIBILITY.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		QiStationConfiguration respAccessible =  getModel().findStationConfiguration(QiEntryStationConfigurationSettings.RESPONSIBILITY.getSettingsName());
		if (null != respAccessible && respAccessible.getPropertyValue().equalsIgnoreCase(QiConstant.YES))  {
			isResponsibilityAccessible = true;
		}
		isShowKickoutPane = QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)? true : false;
		QiStationConfiguration kickoutStation = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.KICKOUT_LOCATION.getSettingsName());
		if (null != kickoutStation && kickoutStation.getPropertyValue().equalsIgnoreCase(QiConstant.YES)) {
			isShowKickoutPane = true;
		}

		QiStationConfiguration stationConfig = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.PLAY_NGSOUND_AT_DEFECT_ENTRY.getSettingsName());
		isPlayNgSoundForBadDFScan = stationConfig == null ? false : QiConstant.YES.equalsIgnoreCase(stationConfig.getPropertyValue());

		view.setTextFont(new SimpleObjectProperty<Font>(Font.getDefault()));
		view.setLabelFont(new SimpleObjectProperty<Font>(Font.getDefault()));
		view.setButtonFont(new SimpleObjectProperty<Font>(Font.getDefault()));
		view.setRadioButtonStyle(new SimpleStringProperty());
		view.setTextStyle(new SimpleStringProperty());
		view.setComboBoxStyle(new SimpleStringProperty());
		QiStationConfiguration iqsInput = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.IQS_INPUT.getSettingsName());
		isIqsInput = iqsInput != null && iqsInput.getPropertyValue().equalsIgnoreCase(QiConstant.YES);
		QiStationConfiguration iqsAuditActionInput = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.IQS_AUDIT_ACTION_INPUT.getSettingsName());
		isIqsAuditActionInput = iqsAuditActionInput != null && iqsAuditActionInput.getPropertyValue().equalsIgnoreCase(QiConstant.YES);
	}

	public String getPart1TextFilter() {
		return StringUtils.trimToEmpty(part1TextFilter);
	}
	@Override
	public void initializeListeners() {
		setEntryScreenListViewBehavior();
		addResponsibleComboBoxListeners();
		if(isShowL2L3())  {
			getRespController().addListener();
		}
		addTitledPaneListeners();
		addWriteUpDeptComboBoxListener();
		defectEntryByTextController.initializeListeners();
		defectEntryByImageController.initializeListeners();
		addHeightPropertyListener();
		addDefectStatusRadioButtonListener();
	}
	private void addHeightPropertyListener() {
		getView().heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String newTextStyle = String.format("-fx-padding: 0 2px 0 0; -fx-font-size: %dpx;", (int) Math.ceil(newValue.doubleValue() * 0.0165));
				String newRadioButtonStyle = String.format("-fx-background-radius: %dpx; -fx-padding: 4px", (int) Math.ceil(newValue.doubleValue() * 0.0305));
				String newComboBoxStyle = String.format("-fx-padding: 0 0px 0 0;-fx-font-size: %dpx;", (int) Math.ceil(newValue.doubleValue() * 0.0175));
				getView().getTextFont().set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.015));
				getView().getLabelFont().set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.021));
				getView().getButtonFont().set(Font.font("Dialog", FontWeight.BOLD, newValue.doubleValue() * 0.018));
				getView().getTextStyle().setValue(newTextStyle);
				getView().getRadioButtonStyle().setValue(newRadioButtonStyle);
				getView().getComboBoxStyle().setValue(newComboBoxStyle);
			}
		});
	}

	@Override
	public void initEventHandlers() { 
		disableAcceptButton();
		setExistingDefectCount();
	}
	/**
	 * This method is used to load data
	 * @param filter
	 */
	@SuppressWarnings("unchecked")
	public void loadInitialData(String filter) {
		QiProgressBar qiProgressBar = null;

		try {
			qiProgressBar = getQiProgressBar("Loading Defect Entry View","Loading Defect Entry View");
			qiProgressBar.showMe();
			setDefectEntered(false);
			setExistingDefectCount();
			publishLoadInitialDataProductEvent(filter);
			isListInitialized = false;
			getView().clearListSelections();
			getView().clearComponents();
			isListInitialized = true;
			List<String> partList = new ArrayList<String>();
			if(getModel().getProperty().isUpcStation()) {
				partList.add(getModel().getInspectionPartName());
				getModel().setCurrentWorkingEntryDept(getModel().getEntryDept());
			} else {
				partList.addAll(getModel().findAllByAllPartLocation(getCurrentWorkingEntryDept()));
			}
			getView().getPartSearchTextField().setSuggestionList(partList);
			assignedStationResponsibilities = new ArrayList<QiStationResponsibilityDto>(getModel().findAllAssignedStationResponsibilitiesByProcessPoint());
			getRespController().setAssignedStationResponsibilities(assignedStationResponsibilities);
			if(!assignedStationResponsibilities.isEmpty()) {
				getView().getSiteComboBox().getItems().addAll(QiCommonUtil.getUniqueArrayList(Lists.transform(assignedStationResponsibilities, new Function<QiStationResponsibilityDto, String>() {
					@Override
					public String apply(final QiStationResponsibilityDto entity) {
						return entity.getSite();
					}
				})));
			} else {
				getView().getSiteComboBox().getItems().addAll(getModel().findAllSite());
			}
			Collections.sort(getView().getSiteComboBox().getItems());
			List<String> writeUpDeptLists=getModel().findAllWriteUpDept();
			if(writeUpDeptLists.size()>0)
				getView().getWriteUpDeptComboBox().getItems().addAll(writeUpDeptLists);
			QiStationWriteUpDepartment defaultWriteUpDept=getModel().findDefaultWriteUpDeptByProcessPoint();
			if(defaultWriteUpDept!=null) {
				getView().getWriteUpDeptComboBox().getSelectionModel().select(defaultWriteUpDept.getId().getDivisionId());
				getView().getColorCodeLabel().setText(defaultWriteUpDept.getId().getColorCode());
			}
			if (getModel().getProductModel()!=null) {
				QiStationConfiguration setting = getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.ENTRY_SCREEN_COLUMN_NUMBER.getSettingsName());
				if(null!=setting) {
					numOfEntryScreenCols = Integer.parseInt(setting.getPropertyValue());	
				}
				List<QiDefectEntryDto> entryScreenList = null;
				if(getModel().getProperty().isUpcStation()){
					entryScreenList = getModel().findAllEntryScreenByProcessPoint(getModel().getInspectionPartName(), getModel().getEntryDept(), assignRealProblem);
				}else{
					if(!StringUtils.isEmpty(getModel().getProductSpecCode())){
						entryScreenList = getModel().findAllEntryScreenByProcessPoint(filter, getCurrentWorkingEntryDept(), assignRealProblem);
					}
				}

				if (entryScreenList == null || entryScreenList.size() == 0) {
					EventBusUtil.publish(new StatusMessageEvent(ENTRY_SCREEN_NOT_SETUP_MSG, StatusMessageEventType.ERROR));
				} 

				final List<QiDefectEntryDto> finalEntryScreenList = new ArrayList<QiDefectEntryDto>(entryScreenList);
				QiEntryModelGrouping entryModelGrouping = getModel().findByMtcModel();
				if(entryModelGrouping != null) {
					getView().getEntryModelLabel().setText(entryModelGrouping.getId().getEntryModel().toUpperCase());
				}
				getView().getEntryScreenListView().setItems(finalEntryScreenList);
				getView().getEntryScreenListView().selectFirst();
				defectEntryDefectStatusList = new ArrayList<>();
				//Check available/default defect configured from Station Config Screen.
				String[] availableDefectStatusList  = getAvailableDefectStatusList();
				if(availableDefectStatusList != null && availableDefectStatusList.length > 0)  {
					String defaultDefectStatus = getDefaultDefectStatus();
					for (String defectStatus: availableDefectStatusList){
						if(defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.REPAIRED.getName())){
							getView().getRepairedRadioBtn().setVisible(true);
							getView().getRepairedRadioBtn().setSelected(defaultDefectStatus.equalsIgnoreCase(defectStatus)?true:false);
							defectEntryDefectStatusList.add(QiEntryStationDefaultStatus.REPAIRED.getName());
						}else if(defectStatus.equalsIgnoreCase(QiEntryStationDefaultStatus.NOT_REPAIRED.getName())){
							getView().getNotRepairedRadioBtn().setVisible(true);
							getView().getNotRepairedRadioBtn().setSelected(defaultDefectStatus.equalsIgnoreCase(defectStatus) || isActualProblemToDefect?true:false);
							defectEntryDefectStatusList.add(QiEntryStationDefaultStatus.NOT_REPAIRED.getName());
						}else if(defectStatus.equalsIgnoreCase(QiConstant.NON_REPAIRABLE)){
							//Non-Repairable/Scrap defect status can't be set as default defect status from Station Config Screen.
							getView().getNonRepairableRadioBtn().setVisible(true);
							getView().getNonRepairableRadioBtn().setSelected(defaultDefectStatus.equalsIgnoreCase(defectStatus)?true:false);
							defectEntryDefectStatusList.add(QiConstant.NON_REPAIRABLE);
						}
					}
				}
			}
			clearMessage();
			checkMandatoryFields();
			getView().setLastDefectEnteredText(true);
			isProductScrapped(getModel().getProduct());
		} catch (Exception e) {
			handleException("An error occured in loadInitialData() method", "Failed to load data", e);
		}
		finally  {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		}
	}

	public boolean isProductScrapped(BaseProduct productToCheck) {
		boolean isProductScraped = false;
		List<QiDefectResult> resultList = getModel().findScrappedDefectsForProductId(productToCheck.getProductId());
		isProductScraped = resultList != null && !resultList.isEmpty();
		if (isProductScraped) {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED+ ": "+resultList.get(0).getPartDefectDescDetail(), StatusMessageEventType.WARNING));
			getView().isScrapedproduct= isProductScraped;
			getView().scrapMessage= resultList.get(0).getPartDefectDescDetail();
		}
		return isProductScraped;
	}

	public String[] getAvailableDefectStatusList()  {
		String  defectStatusValues = StringUtils.EMPTY;
		String[] availableDefectStatusList = null;
		qiEntryStationConfigManagement = getModel().findPropertyKeyValueByProcessPoint(QiConstant.ENTRY_STATION_AVAILABLE_DEFECT_STATUS);
		if(qiEntryStationConfigManagement != null){
			defectStatusValues = qiEntryStationConfigManagement.getPropertyValue();
			if(!StringUtils.isBlank(defectStatusValues))  {
				availableDefectStatusList= defectStatusValues.split("\\s*,\\s*");
			}
		}
		return availableDefectStatusList;
	}

	private void publishLoadInitialDataProductEvent(String filter) {
		ProductEvent event = new ProductEvent(StringUtils.EMPTY, getModel().getCachedDefectResultList().isEmpty()
				? ProductEventType.PRODUCT_DEFECT_VOID_ALL
						: ProductEventType.PRODUCT_APPLY_RECENT_DEFECT);
		if (!filter.isEmpty())
			event.addArgument(ObservableListChangeEventType.CHANGE_SELECTION, false);

		EventBusUtil.publish(event);
	}

	/**
	 * This method is used to create Product Panel Buttons
	 */
	public ProductActionId[] getProductActionIds(){
		if(!isDefectEntered && isCancelBtnDisable())  {
			return new ProductActionId[]{KEYBOARD,DIRECTPASS};			
		}
		else if(!isDefectEntered)  {
			return new ProductActionId[]{KEYBOARD,ProductActionId.CANCEL_DIRECT_PASS,DIRECTPASS};			
		}
		else  {
			return new ProductActionId[]{KEYBOARD,SUBMIT};
		}
	}

	/**
	 * This method is used to create Product Panel Buttons on click of Accept Button.
	 */
	public ProductActionId[] getProductActionIdsOnAccept(){
		if(!isDefectEntered && isCancelBtnDisable())  {
			return new ProductActionId[]{KEYBOARD,SUBMIT};
		}
		else if(!isDefectEntered)  {
			return new ProductActionId[]{KEYBOARD,ProductActionId.CANCEL_DONE,SUBMIT};			
		}
		else  {
			return new ProductActionId[]{KEYBOARD,SUBMIT};
		}
	}

	@Override
	public void reset() {
		clearMessages();
		setStateNotify(ProcessState.IDLE);
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
			if (EXISTING_DEFECTS.equals(loggedButton.getId())) openVoidUpdatePopup();
			else if (ACCEPT.equals(loggedButton.getText())) acceptDefect();
			else if (RESET.equals(loggedButton.getText())) fullyResetData();
			else if (RECENT_DEFECTS.equals(loggedButton.getText())) openRecentDefectPopup();
			else if(QiConstant.CLEAR_TEXT_SYMBOL.equals(loggedButton.getText())) clearPartSearchTxt();
		} else if (actionEvent.getSource() instanceof UpperCaseFieldBean) {
			if(actionEvent.getSource().equals(getView().getMenuSearchTextField())) reloadMenuTileListView();
			else if(actionEvent.getSource().equals(getView().getScanTextField())){
				getDefectEntryByTextController().preAcceptDefect(getView().getScanTextField().getText());
			}
		} else if(actionEvent.getSource() instanceof LoggedRadioButton)  {
			clearMessage();
			checkMandatoryFields();
		}
		else if (actionEvent.getSource() instanceof ContextMenu)
		{
			CustomMenuItem selectedCustomMenuItem= (CustomMenuItem)(actionEvent.getTarget());
			Label selectedLabel=(Label)selectedCustomMenuItem.getContent();
			this.selectedPartName = selectedLabel.getText();  
			loadInitialData(this.selectedPartName);
		}
	}

	public void clearMessage()  {
		super.clearMessage();
	}

	public void clearStatusOnly()  {
		super.clearStatusOnly();
	}

	public void clearById(String newId) {
		super.clearById(newId);
	}

	/**
	 * This method is used to open Existing Defects popup screen
	 */
	protected void openVoidUpdatePopup(){
		ExistingDefectDialog dialog = new ExistingDefectDialog(EXISTING_DEFECTS, getModel(), getProductModel(), getApplicationId(),
				getCurrentWorkingProcessPointId(), getModel().getQuantity(), getProductModel().isTrainingMode(),
				isRepairRelated);
		dialog.setDefectEntryStatusList(this.defectEntryDefectStatusList);
		Logger.getLogger().check("Existing Defects dialogbox populated");
		dialog.showDialog();
		getView().setLastDefectEnteredText(false);
		getView().getScanTextField().requestFocus();
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		if(entryScreen != null)
			defectEntryByImageController.drawCurrentSessionSymbol(entryScreen.getImageName());
		if(getModel().getCachedDefectResultList().isEmpty()) {
			EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_VOID_ALL));
		}
		setExistingDefectCount();
	}

	public void setExistingDefectCount()  {
		getView().getExistingDefectBtn().setGraphic(getExistDefButtonLabel());
		enableDisableExistingDefectButton();
	}
	
	public HBox getExistDefButtonLabel() {
		HBox coloredLabel = new HBox();

		int repCount = 0; 
		int notRepCount = 0;
		for (QiDefectResult defect : getModel().getCachedDefectResultList()) {
			if (defect.getDefectStatusType().isRepaired()) repCount++;
			else notRepCount++;
		}
		
		ArrayList<Label> elements =  new ArrayList<Label>();
		elements.add(new Label(EXISTING_DEFECTS + " ("));
		elements.add(new Label(Integer.toString(repCount)));
		elements.add(new Label(","));
		elements.add(new Label(Integer.toString(notRepCount)));
		elements.add(new Label(")"));
		
		for (int i = 0; i < elements.size(); i++) {
			if (i == 1) elements.get(i).setTextFill(Color.GREEN);
			else if (i == 3) elements.get(i).setTextFill(Color.RED);
			else elements.get(i).setTextFill(Color.BLACK);
			coloredLabel.getChildren().add(elements.get(i));
		}
		
		coloredLabel.setAlignment(Pos.CENTER);
		return coloredLabel;
	}
	
	/**
	 * This method is used to Accept a Part Defect from Defect Entry Screen.
	 */
	public void acceptDefect() {
		clearMessage();
		List<QiDefectResult> defectResultList = getModel().getCachedDefectResultList();
		QiDefectEntryDto selectedEntryScreen = getView().getEntryScreenListView().getSelectedItem();
		if(defectResultList != null && !defectResultList.isEmpty()) {
			QiDefectResult lastDefectEntered = defectResultList.get(defectResultList.size() - 1);
			if(lastDefectEntered.getOriginalDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()
					&& lastDefectEntered.getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
				MessageDialog.showError(QiConstant.POST_SCRAP_ACCEPT_DEFECT_ERROR_MSG);
				if(selectedEntryScreen.isImage())  {
					resetData();
				}
				return;
			}
		}
		String writeUpDept = StringUtils.trimToEmpty((String)getView().getWriteUpDeptComboBox().getSelectionModel().getSelectedItem());
		String respSite = StringUtils.trimToEmpty((String)getView().getSiteComboBox().getSelectionModel().getSelectedItem());
		String respPlant = StringUtils.trimToEmpty((String)getView().getPlantComboBox().getSelectionModel().getSelectedItem());
		String respDept = StringUtils.trimToEmpty((String)getView().getDepartmentComboBox().getSelectionModel().getSelectedItem());
		String entryScreen = selectedEntryScreen == null ? StringUtils.EMPTY : selectedEntryScreen.getEntryScreen();
		String respLevel1 = "";
		KeyValue<String, Integer> kv = getView().getLevel1ComboBox().getSelectionModel().getSelectedItem();
		if(kv != null && kv.getKey() != null)  {
			respLevel1 = kv.toString().trim();
		}


		if(getView().getDefectPanelByText().isVisible())
			selectedDefect = getView().getPartDefectListView().getSelectionModel().getSelectedItem();

		QiDefectResult qiDefectResult = getModel().getDefectResult();
		if(qiDefectResult!=null){
			selectedDefect = setPartDefectCombination(qiDefectResult);
			getModel().setDefectResult(null);
		}
		/**
		 * Mandatory check for mandatory fields
		 */
		if(selectedDefect == null) 
			displayErrorMessage(SELECT_PART_DEFECT_ERROR_MSG);
		else if(getView().getToggleGroup().getSelectedToggle() == null)
			displayErrorMessage(SELECT_DEFECT_STATUS_ERROR_MSG);
		else if(StringUtils.isBlank(writeUpDept)) 
			displayErrorMessage(SELECT_WRITE_UP_DEPT_ERROR_MSG);
		else if(StringUtils.isBlank(respSite))
			displayErrorWithId(SELECT_RESPONSIBLE_SITE_ERROR_MSG, RESPONSIBLE_LVL_MSG_ID);
		else if(StringUtils.isBlank(respPlant))
			displayErrorWithId(SELECT_RESPONSIBLE_PLANT_ERROR_MSG, RESPONSIBLE_LVL_MSG_ID);
		else if(StringUtils.isBlank(respDept))
			displayErrorWithId(SELECT_RESPONSIBLE_DEPT_ERROR_MSG, RESPONSIBLE_LVL_MSG_ID);
		else if(StringUtils.isBlank(respLevel1) || respLevel1.equals(QiConstant.ASSIGN_RESP)) 
			displayErrorWithId(SELECT_RESPONSIBLE_LEVEL1_ERROR_MSG, RESPONSIBLE_LVL_MSG_ID);
		else if(isShowL2L3() && !getRespController().isValidateResponsibleLevel())  {
			displayErrorWithId(SELECT_RESPONSIBLE_LEVEL2_ERROR_MSG, RESPONSIBLE_LVL_MSG_ID);			
		}
		else {
			try {
				if (isFrameQicsEngineSource()) {					
					List<QiResponsibilityMapping> listRespLevel = getResponsibleMapping();
					QiResponsibleLevel respLevel = getResponsibleLevel();
					Product product = (Product) getModel().getProduct();
					Frame vehicle = (Frame) product;
					if (!(vehicle.getEngineSerialNo() == null || vehicle.getEngineSerialNo().isEmpty())) {
						Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(vehicle.getEngineSerialNo());
						if (engine != null) {
							for (QiResponsibilityMapping each : listRespLevel) {
								if (respLevel.getResponsibleLevelId().equals(each.getDefaultRespLevel())
										&& each.getPCode().equals(engine.getPlantCode())) {
									QiResponsibleLevel altRespLevel = ServiceFactory.getDao(QiResponsibleLevelDao.class)
											.findByResponsibleLevelId(each.getAlternateDefault());
									if (altRespLevel == null)
										break;
									respLevel1 = altRespLevel.getResponsibleLevelName();
									break;
								}
							}
						}
					}
				}
				boolean isValidDefect = isValidDefectResult(writeUpDept, respSite, respPlant, respDept, respLevel1, entryScreen);
				if(isValidDefect){
					clearMessage();
					EventBusUtil.publish(new StatusMessageEvent(DEFECT_SUCCESSFULLY_ACCEPTED, StatusMessageEventType.INFO));
					EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_ACCEPT));
					getView().getExistingDefectBtn().setDisable(false);
					setExistingDefectCount();
					resetOptionalData();
					Logger.getLogger().check("Defect Accepted successfully");
				}
			} catch (Exception e) {
				handleException("An error occured in Accept Defect method", "Failed to Accept Defect", e);
			}
			if(getView().getdefectPanelByImage().isVisible() && getView().getAcceptBtn().isDisabled())  {
				setResponsibilityComboboxDisable(true);
			}
			if(selectedEntryScreen.isImage())  {
				resetData();
			} else {
				resetDefectStatusPanel();
			}

		}
	}
	
	/**
	 * This method is used to reset Defect Status panel selection to default value
	 */
	public void resetDefectStatusPanel() {
		QiStationConfiguration resetToDefaultProp = getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.RESET_TO_DEFAULT_DEFECT_STATUS.getSettingsName());
		
		if (resetToDefaultProp != null && resetToDefaultProp.getPropertyValue().equalsIgnoreCase(QiConstant.YES)) {
			String defaultDefectStatus = getDefaultDefectStatus();
			for (String defectStatus : getAvailableDefectStatusList()) {
				if (!defaultDefectStatus.equals(defectStatus)) continue;
				
				if (defaultDefectStatus.equals(QiConstant.REPAIRED))
					this.getView().getRepairedRadioBtn().setSelected(true);
				else if (defaultDefectStatus.equals(QiConstant.NOT_REPAIRED))
					this.getView().getNotRepairedRadioBtn().setSelected(true);
				else if (defaultDefectStatus.equals(QiConstant.NON_REPAIRABLE))
					this.getView().getNonRepairableRadioBtn().setSelected(true);
				return;
			}
			for (Toggle toggle : this.getView().getRepairedRadioBtn().getToggleGroup().getToggles()) {
				toggle.setSelected(false);
			}
		}
	}

	/**
	 * This method is used to validate Defect Result
	 * @param defectResult
	 * @return
	 * @throws Exception 
	 */
	private boolean isValidDefectResult(String writeUpDept, String respSite, String respPlant, String respDept, String respLevel1, String entryScreen) throws Exception {

		QiDefectResult defectResult = null;

		if(getModel().getProperty().isUpcStation()) {
			defectResult = setDefectResultObject(writeUpDept, respSite, respPlant, respDept, respLevel1, 
					selectedDefect,getModel().getProductList().get(0).getProductId(),getModel().getCurrentWorkingEntryDept(),entryScreen);
		} else {
			defectResult = setDefectResultObject(writeUpDept, respSite, respPlant, respDept, respLevel1, 
					selectedDefect,getModel().getProductId(),getCurrentWorkingEntryDept(),entryScreen);
		}
		defectResult.setDefectTransactionGroupId(getModel().getDefectTransactionGroupId(defectResult));
		if(getView().getdefectPanelByImage().isVisible()) {
			double resizePercent = (((getView().getScreenHeight()*0.67)-500)/(getView().getScreenHeight()*0.67))*100;
			defectResult.setImageName(selectedDefect.getImageName());
			defectResult.setPointX((int)(pointX-(pointX*resizePercent)/100));
			defectResult.setPointY((int)(pointY-(pointY*resizePercent)/100));
		}
		if(isShowKickoutPane()) {
			if(getView().getKickoutLocationPane().getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem() != null) {
				defectResult.setKickoutDivisionId(getView().getKickoutLocationPane().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem().getDivisionId());
				defectResult.setKickoutLineId(getView().getKickoutLocationPane().getLineComboBox().getControl().getSelectionModel().getSelectedItem().getLineId());
				defectResult.setKickoutProcessPointId(getView().getKickoutLocationPane().getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointId());
				defectResult.setKickoutProcessPointName(getView().getKickoutLocationPane().getProcessPointComboBox().getControl().getSelectionModel().getSelectedItem().getProcessPointName());
			}
		}
		if(getModel().getCachedDefectResultList().contains(defectResult)) {
			//Check configuration of Duplicate entry warning dialog for selected defect combination already exists for the product selected.
			checkAcceptDefect(defectResult);
			return false;
		} else if(getModel().checkDefectResultExist(defectResult)) {
			//Check configuration of Duplicate entry warning dialog for selected defect combination already exists for the product selected.
			checkAcceptDefect(defectResult);
			return false;
		} else {

			boolean isScrapReasonRequired = false;

			if (defectResult.getOriginalDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
				Stage stage = (isAcceptButton() && isPopupAccept()) ? acceptDialog : getView().getStage();
				QiStationConfiguration redirectConfigEntry = ServiceFactory.getDao(QiStationConfigurationDao.class)
						.findValueByProcessPointAndPropKey(getProcessPointId(),
								QiEntryStationConfigurationSettings.SCRAP_REASON.getSettingsName());

				String scrapReasonRequired = QiEntryStationConfigurationSettings.SCRAP_REASON.getDefaultPropertyValue();
				if(null !=redirectConfigEntry && !StringUtils.isBlank(redirectConfigEntry.getPropertyValue())) {
					scrapReasonRequired = redirectConfigEntry.getPropertyValue();
				}

				if(scrapReasonRequired.equalsIgnoreCase("Yes"))  {
					isScrapReasonRequired = true;
				}
				boolean isOk = MessageDialog.confirmWithOptionalComment(stage, PRODUCT_SCRAP_ERROR_MSG, "Scrap Reason", getModel().isScrapReasonRequired(), QiConstant.SCRAP_REASON, isScrapReasonRequired);
				if (!isOk)
					return false;
				if (getModel().isScrapReasonRequired() && StringUtils.isBlank(FXOptionPane.getCommentText())) {
					EventBusUtil.publish(new StatusMessageEvent(QiConstant.SCRAP_REASON, StatusMessageEventType.ERROR));
					return false;
				}
				defectResult.setComment(FXOptionPane.getCommentText());
				
				//Remove from the parking space when the product is scrapped
				QiClearParking clearParked = ServiceFactory.getService(QiClearParking.class);
				clearParked.removeVinFromQicsParking(getModel().getProductId()); 
				
			}
			QiStationConfiguration qiStationConfiguration =getModel().findPropertyKeyValueByProcessPoint(
					QiEntryStationConfigurationSettings.ADD_COMMENT_FOR_CHANGING_RESPONSIBILITY.getSettingsName()); 

			if (qiStationConfiguration != null) {
				if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
					final String status = addReasonForChangingResponsibility(defectResult);
					if (status.equals(QiConstant.ASSIGN)) {
						defectResult.setComment(FXOptionPane.getCommentText());
					} else if(status.equals(QiConstant.DEASSIGN)){
						return false;
					}
				}
			}
			if(getModel().getProperty().isUpcStation()) {
				for(MbpnProduct product : getModel().getProductList()) {
					QiDefectResult upcDefectResult = (QiDefectResult)defectResult.deepCopy();
					upcDefectResult.setProductId(product.getProductId());
					getModel().getCachedDefectResultList().add(upcDefectResult);
				}
			}else {
				getModel().getCachedDefectResultList().add(defectResult);
			}
			getView().setLastDefectEnteredText(true);
			return true;
		}
	}

	/**
	 * This method is used to set PDC in dto
	 */
	private QiDefectResultDto setPartDefectCombination(
			QiDefectResult qiDefectResult) {
		QiDefectResultDto selectedDefect;
		selectedDefect = new QiDefectResultDto();
		selectedDefect.setInspectionPartName(qiDefectResult.getInspectionPartName());
		selectedDefect.setInspectionPartLocationName(qiDefectResult.getInspectionPartLocationName());
		selectedDefect.setInspectionPartLocation2Name(qiDefectResult.getInspectionPartLocation2Name());
		selectedDefect.setInspectionPart3Name(qiDefectResult.getInspectionPart3Name());
		selectedDefect.setInspectionPart2Name(qiDefectResult.getInspectionPart2Name());
		selectedDefect.setInspectionPart2LocationName(qiDefectResult.getInspectionPart2LocationName());
		selectedDefect.setInspectionPart2Location2Name(qiDefectResult.getInspectionPart2Location2Name());
		selectedDefect.setDefectTypeName(qiDefectResult.getDefectTypeName());
		selectedDefect.setDefectTypeName2(qiDefectResult.getDefectTypeName2());
		selectedDefect.setImageName(qiDefectResult.getImageName());
		selectedDefect.setIqsCategory(qiDefectResult.getIqsCategoryName());
		selectedDefect.setIqsVersion(qiDefectResult.getIqsVersion());
		selectedDefect.setIqsQuestion(qiDefectResult.getIqsQuestion());
		selectedDefect.setIqsQuestionNo(qiDefectResult.getIqsQuestionNo());
		selectedDefect.setThemeName(qiDefectResult.getThemeName());
		selectedDefect.setReportable(qiDefectResult.getReportable());
		selectedDefect.setDefectCategoryName(qiDefectResult.getDefectCategoryName());
		selectedDefect.setEntryScreen(qiDefectResult.getEntryScreen());
		QiResponsibleLevel qiResponsibleLevel = getModel().findResponsibleLevelByNameAndDeptId(
				StringUtils.trimToEmpty(getView().getLevel1ComboBox().getSelectionModel().getSelectedItem().toString()),
				new QiDepartmentId(
						StringUtils.trimToEmpty(
								(String) getView().getDepartmentComboBox().getSelectionModel().getSelectedItem()),
						StringUtils.trimToEmpty(
								(String) getView().getSiteComboBox().getSelectionModel().getSelectedItem()),
						StringUtils.trimToEmpty(
								(String) getView().getPlantComboBox().getSelectionModel().getSelectedItem())));
		selectedDefect.setResponsibleLevelId(qiResponsibleLevel.getResponsibleLevelId());
		selectedDefect.setProcessNumber(qiDefectResult.getProcessNo());
		selectedDefect.setProcessName(qiDefectResult.getProcessName());
		selectedDefect.setUnitNumber(qiDefectResult.getUnitNo());
		selectedDefect.setUnitDesc(qiDefectResult.getUnitDesc());
		selectedDefect.setModelYear(qiDefectResult.getPddaModelYear());
		selectedDefect.setVehicleModelCode(qiDefectResult.getPddaVehicleModelCode());
		selectedDefect.setLocalTheme(qiDefectResult.getLocalTheme());
		selectedDefect.setRepairAreaName(qiDefectResult.getRepairArea());
		return selectedDefect;
	}

	/**
	 * This method is used to create Defect Result Object
	 * @param entryScreen 
	 */
	private QiDefectResult setDefectResultObject(String writeUpDept,
			String respSite, String respPlant, String respDept,
			String respLevel1, QiDefectResultDto selectedDefect,String productId,String entryDept, String entryScreen) throws Exception {
		/**
		 * Get responsibility Level 2 and Level 3 based on Level 1
		 */
		List<QiDefectResultDto> levelResultList = getModel().findLevel2andLevel3ByLevel1(respSite, respPlant, respDept, respLevel1);
		QiResponsibleLevel respLvl1 = null, respLvl2 = null, respLvl3 = null;
		String lvlName1 = respLevel1, lvlName2 = "", lvlName3 = "";
		if(isShowL2L3())  {
			respLvl1 = getRespController().findResponsibleLevel1();
			QiResponsibleLevel nextLvl = null;
			if(respLvl1 != null)  {
				lvlName1 = respLvl1.getResponsibleLevelName();
				if(respLvl1.getUpperResponsibleLevelId() > 0)  {
					nextLvl = getModel().findResponsibleLevelById(respLvl1.getUpperResponsibleLevelId());
					if(nextLvl != null && nextLvl.getLevel() == 2)  {
						respLvl2 = nextLvl;
						lvlName2 = nextLvl.getResponsibleLevelName();
					}
					else if(nextLvl != null && nextLvl.getLevel() == 3)  {
						lvlName3 = nextLvl.getResponsibleLevelName();
					}
				}
			}
			if(respLvl2 != null)  {
				if(respLvl2.getUpperResponsibleLevelId() > 0)  {
					respLvl3 = getModel().findResponsibleLevelById(respLvl2.getUpperResponsibleLevelId());
					if(respLvl3 != null)  {
						lvlName3 = respLvl3.getResponsibleLevelName();
					}
				}
			}
		}
		else  {
			lvlName1 = respLevel1;
			if(levelResultList != null && !levelResultList.isEmpty() && levelResultList.get(0) != null)  {
				lvlName2 = levelResultList.get(0).getLevelTwo();
				lvlName3 = levelResultList.get(0).getLevelThree();
			}
		}
		//just in case, L2/L3 got set to null, set to blank string
		if(StringUtils.isBlank(lvlName2))  {
			lvlName2 = "";
		}
		if(StringUtils.isBlank(lvlName3))  {
			lvlName3 = "";
		}
		
		/**
		 * Get IQS Score
		 */
		double iqsScore = isIqsInput ? ((IqsScore) getView().getIqsScoreComboBox().getSelectionModel().getSelectedItem()).getScore() : 0;
		int iqsAuditAction = isIqsInput && isIqsAuditActionInput ? ((IqsAuditAction) getView().getIqsAuditActionComboBox().getSelectionModel().getSelectedItem()).getId() : 0;
		
		/**
		 * Get BOM Main Part No for respective PDC
		 */
		List<String> bomMainPartNoList = getModel().findMainPartNoByInspectionPartName(selectedDefect.getInspectionPartName());
		String bomMainPartNo = !bomMainPartNoList.isEmpty() ? bomMainPartNoList.get(0) : StringUtils.EMPTY;

		LoggedRadioButton selectedRadioBtn = (LoggedRadioButton) getView().getToggleGroup().getSelectedToggle();
		/**
		 * Set Defect Result Object
		 */
		QiDefectResult defectResult = prepareDefectResultObject(writeUpDept, iqsScore, iqsAuditAction,
				respSite, respPlant, respDept, lvlName1, lvlName2, lvlName3, selectedDefect,
				bomMainPartNo, selectedRadioBtn,productId,entryDept,entryScreen);
		return defectResult;
	}
	/**
	 * This method is used to prepare Defect Result Object 
	 */
	private QiDefectResult prepareDefectResultObject(String writeUpDept, double iqsScore, int iqsAuditAction,
			String respSite, String respPlant, String respDept,
			String respLvl1, String respLvl2, String respLvl3,
			QiDefectResultDto selectedDefect, String bomMainPartNo,
			LoggedRadioButton selectedRadioBtn,String productId,
			String entryDept, String entryScreen)
	{
		QiDefectResult defectResult = new QiDefectResult();
		defectResult.setWriteUpDept(writeUpDept);
		defectResult.setIqsScore(iqsScore);
		defectResult.setIqsAuditAction((short) iqsAuditAction);
		defectResult.setResponsibleSite(respSite);
		defectResult.setResponsiblePlant(respPlant);
		defectResult.setResponsibleDept(respDept);
		defectResult.setResponsibleLevel1(respLvl1);
		defectResult.setResponsibleLevel2(respLvl2);
		defectResult.setResponsibleLevel3(respLvl3);
		defectResult.setInspectionPartName(selectedDefect.getInspectionPartName());
		defectResult.setInspectionPartLocationName(selectedDefect.getInspectionPartLocationName());
		defectResult.setInspectionPartLocation2Name(selectedDefect.getInspectionPartLocation2Name());
		defectResult.setInspectionPart2Name(selectedDefect.getInspectionPart2Name());
		defectResult.setInspectionPart2LocationName(selectedDefect.getInspectionPart2LocationName());
		defectResult.setInspectionPart2Location2Name(selectedDefect.getInspectionPart2Location2Name());
		defectResult.setInspectionPart3Name(selectedDefect.getInspectionPart3Name());
		defectResult.setDefectTypeName(selectedDefect.getDefectTypeName());
		defectResult.setDefectTypeName2(selectedDefect.getDefectTypeName2());
		defectResult.setApplicationId(getModel().getCurrentWorkingProcessPointId());
		defectResult.setProductId(productId);
		defectResult.setCreateUser(getView().getMainWindow().getUserId().toUpperCase());
		defectResult.setEntrySiteName(getModel().getEntrySiteName());
		defectResult.setEntryPlantName(getModel().getCurrentWorkingPlantName());
		defectResult.setEntryProdLineNo(getModel().getEntryProdLineNo());
		defectResult.setEntryDept(entryDept);
		defectResult.setProductSpecCode(getModel().getProductSpecCode());
		defectResult.setProductionLot(getModel().getProductionLot());
		defectResult.setBomMainPartNo(bomMainPartNo);
		defectResult.setDefectStatus(selectedRadioBtn.getText());
		defectResult.setProductType(getModel().getProductType());
		defectResult.setEngineFiringFlag(selectedDefect.getEngineFiringFlag());
		defectResult.setIqsVersion(selectedDefect.getIqsVersion());
		defectResult.setIqsCategoryName(selectedDefect.getIqsCategory());
		defectResult.setIqsQuestionNo(selectedDefect.getIqsQuestionNo());
		defectResult.setIqsQuestion(selectedDefect.getIqsQuestion());
		defectResult.setThemeName(selectedDefect.getThemeName());
		defectResult.setReportable(selectedDefect.getReportable());
		defectResult.setRepairArea(selectedDefect.getRepairAreaName());
		defectResult.setRepairMethodNamePlan(selectedDefect.getRepairMethod());
		defectResult.setRepairTimePlan(selectedDefect.getRepairMethodTime());
		defectResult.setEngineFiringFlag(selectedDefect.getEngineFiringFlag());
		if(selectedDefect.getReportable()==(short)0){
			qiEntryStationConfigManagement =getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.REPORTABLE.getSettingsName());
			//check if station is reportable
			if(qiEntryStationConfigManagement!=null)
				defectResult.setReportable(qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.NON_REPORTABLE_DEFECT_ENTRY_SCREEN.toString())?QiConstant.NON_REPORTABLE_DEFECT_ENTRY_SCREEN:(short)0 );
			else
				defectResult.setReportable((short)0 );

		}else{
			defectResult.setReportable(selectedDefect.getReportable());
		}
		defectResult.setPddaModelYear(selectedDefect.getModelYear());
		defectResult.setPddaVehicleModelCode(selectedDefect.getVehicleModelCode());
		defectResult.setProcessNo(selectedDefect.getProcessNumber());
		defectResult.setProcessName(selectedDefect.getProcessName());
		defectResult.setUnitNo(selectedDefect.getUnitNumber());
		defectResult.setUnitDesc(selectedDefect.getUnitDesc());
		defectResult.setLocalTheme(selectedDefect.getLocalTheme());
		defectResult.setDefectCategoryName(selectedDefect.getDefectCategoryName());

		if(isFrame() || isEngine()) {
			/**
			 * If Product Type is FRAME or ENGINE
			 */
			Product product = (Product)getModel().getProduct();
			if(product!=null) {
				defectResult.setAfOnSequenceNumber(product.getAfOnSequenceNumber()!=null ? product.getAfOnSequenceNumber().intValue() : 0);
				defectResult.setKdLotNumber(product.getKdLotNumber());
			}
		}
		defectResult.setTerminalName(getModel().getTerminalName());
		defectResult.setEntryScreen(entryScreen);
		defectResult.setGdpDefect((QiInspectionUtils.isGdpProcessPoint(getCurrentWorkingProcessPointId()) && defectResult.getCurrentDefectStatus()==(short)DefectStatus.NOT_FIXED.getId())?(short)1:(short)0); 
		defectResult.setTrpuDefect(QiInspectionUtils.isTrpuProcessPoint(getCurrentWorkingProcessPointId()) ? (short) 1 : (short) 0); 

		System.out.println(QiInspectionUtils.isGdpProcessPoint(getModel().getCurrentWorkingProcessPointId()));		
		System.out.println(QiInspectionUtils.isGdpProcessPoint(getModel().getCurrentWorkingProcessPointId()));
		System.out.println(QiInspectionUtils.isGlobalGdpWriteUpDept(defectResult.getWriteUpDept()));
		System.out.println(defectResult.getTrpuDefect() == (short) 1);
		
		if( QiInspectionUtils.isGdpProcessPoint(getModel().getCurrentWorkingProcessPointId())
			&& (defectResult.getTrpuDefect() == (short) 1) 
			&& QiInspectionUtils.isGlobalGdpEnabled() 
			&& QiInspectionUtils.isGlobalGdpWriteUpDept(defectResult.getWriteUpDept())) {
			defectResult.setGdpDefect((short) 1);
		}
		
		QiEntryModelGrouping entryModelGrouping = getModel().findByMtcModel();
		if (entryModelGrouping != null && entryModelGrouping.getId() != null) {
			defectResult.setEntryModel(entryModelGrouping.getId().getEntryModel());
		}

		defectResult.setDefaultResponsibleLevel1(selectedDefect.getDefaultResponsibeLevel1());

		return defectResult;
	}

	/**
	 * This method is used to execute action for Product Buttons: DONE & DIRECT PASS
	 */
	@Subscribe()
	public void onProductEvent(ProductEvent event) {
		QiProgressBar qiProgressBar  = null;
		try {
			if (event == null) {
				return;
			}
			
			switch(event.getEventType()) {
				case PRODUCT_DEFECT_DONE :
					boolean isEngineFiringFlagSet = false;
					List<QiDefectResult> savedQiDefectResultList = new ArrayList<QiDefectResult>();
					qiProgressBar = getQiProgressBar("Performing Done action.", "Performing Done action.");
					qiProgressBar.showMe();
					clearFilters();
					clearScrappedInfo();
					if (!getProductModel().isTrainingMode()) {
						try {
							if (!getModel().getCachedDefectResultList().isEmpty()) {

								if (getProductModel().isBulkProcess()) {
									qiProgressBar.updateMessage("procesing bulk defect result");
									savedQiDefectResultList = processBulkDefectResults(isEngineFiringFlagSet);

								} else {
								List<QiDefectResult> defectList = createDefectResults(getModel().getCachedDefectResultList(), isEngineFiringFlagSet);
								if (defectList == null) return;

									savedQiDefectResultList.addAll(defectList);

									if ((repairDefectDetails == null || repairDefectDetails.isEmpty())
										|| (repairDefectDetails != null && !repairDefectDetails.isEmpty() && (Boolean) repairDefectDetails.get("isNewDefect"))) {
										saveStationResult(getModel().getCachedDefectResultList());
										setDefectEntered(true);
									if(isEngineFiringFlagSet && getModel().getProductType().equalsIgnoreCase(ProductType.ENGINE.getProductName()))
											getModel().updateFiringFlag(true);

										//replicate defect result to GAL125TBX
									if (PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isReplicateDefectRepairResult()) {
											replicateDefectResult(savedQiDefectResultList);
										}

										getModel().getCachedDefectResultList().clear();

										if (getModel().findNotFixedDefectCountByProductId() == 0) {
											String configuredRepairAreaName = getModel().getConfiguredRepairAreaName();
											if(StringUtils.isBlank(configuredRepairAreaName) || QiConstant.CLEAR_REPAIR_AREA.equalsIgnoreCase(configuredRepairAreaName.trim()))  {
													getModel().deassignRepairAreaSpaceByProductId();
											}
											else {
												getModel().assignUnitToRepairArea(configuredRepairAreaName, savedQiDefectResultList.get(0).getDefectResultId());
											}
										}
									}
								}
						} else if(StringUtils.equalsIgnoreCase(String.valueOf(event.getTargetObject()), ViewId.IPP_TAG_ENTRY.getViewLabel())) {
								saveIPPStationResult();
							}
						} catch (Exception e) {
							handleException("An error occured in Product Done method", "Failed to save Defect", e);
						}
					} else {
						if (!getModel().getCachedDefectResultList().isEmpty()) {
						if ((repairDefectDetails == null || repairDefectDetails.isEmpty()) && getProductModel().isBulkProcess()) {
								//This is for add defect to product which status is null
								repairDefectDetails = new HashMap<String, Object>();
								repairDefectDetails.put("isNewDefect", true);
								repairDefectDetails.put("Repair_Related", false);
							}
							for (QiDefectResult defectResult : getModel().getCachedDefectResultList()) {
								if (getProductModel().isBulkProcess()) {
									List<BaseProduct> products = getProductModel().getProcessedProducts();
									for (BaseProduct product : products) {
										QiDefectResult multiDefectResult = (QiDefectResult) defectResult.deepCopy();
										multiDefectResult.setProductId(product.getProductId());
										createBulkRepairRelatedDefect(multiDefectResult, null);
									}
								} else {
									if (repairDefectDetails != null && !repairDefectDetails.isEmpty()) {
										createRepairRelatedDefect(defectResult, null);
									}

								}
							}
						}
						getModel().getCachedDefectResultList().clear();

					}
					break;
				
				case PRODUCT_DIRECT_PASSED :
					if(isProductShipped(getModel().getProductId()) && !getModel().getProperty().isAllowDefectForShippedVin()) {
						productShippedEvent();
					} else {
						qiProgressBar = getQiProgressBar("Performing Direct Pass action.", "Performing Direct Pass action.");
						qiProgressBar.showMe();
						doDirectPass();
					}
					break;
				
				case PRODUCT_INPUT_OK :
					setDefectEntered(false);
					clearFilters();
					getView().setViewStarted(false);
					break;
					
				default :	
			}
		} finally {
			if(qiProgressBar != null)  {
				qiProgressBar.closeMe();
			}
		} 
	}

	private void doDirectPass() {
		
		clearFilters();
		clearScrappedInfo();
		if (!getProductModel().isTrainingMode()) {

			//check if current tracking status is invalid previous line ID for this process point
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return;
			}

			try {
				saveStationResult(getModel().getCachedDefectResultList());
				if (isProductSequenceStation() && isDeleteProductSequence()) {
					deleteProductSequence();
				}
				if (getModel().getProperty().isUpcStation()) {

				} else {
				if (PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isReplicateDefectRepairResult() &&
						PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isReplicateDirectPassTo125()) { 
						//replicate direct pass result into GAL125TBX
						replicateDirectPassResult();
					}
				}
			} catch (Exception e) {
				handleException("An error occured in Product Direct Pass method", "Failed to Direct Pass", e);
			}
		}

	}
	
	private void clearScrappedInfo() {
		getView().setScrapedproduct(false);
		getView().setScrapMessage("");
		
	}

	/**
	 * @return 
	 * 
	 */
	private List<QiDefectResult> processBulkDefectResults(boolean isEngineFiringFlagSet) {
		List<QiDefectResult> savedQiDefectResultList = createBulkDefectResults(isEngineFiringFlagSet);

		if ((repairDefectDetails == null || repairDefectDetails.isEmpty())
				|| (repairDefectDetails != null && !repairDefectDetails.isEmpty() && (Boolean) repairDefectDetails.get("isNewDefect"))) {
			saveStationResult(getModel().getCachedDefectResultList());
			setDefectEntered(true);
			if(isEngineFiringFlagSet && getModel().getProductType().equalsIgnoreCase(ProductType.ENGINE.getProductName()))
				getModel().updateFiringFlag(true);

			//replicate defect result to GAL125TBX
			if (PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isReplicateDefectRepairResult()) {
				replicateDefectResult(savedQiDefectResultList);
			}

			getModel().getCachedDefectResultList().clear();

			if(getModel().findNotFixedDefectCountByProductId() == 0) {
				String configuredRepairAreaName = getModel().getConfiguredRepairAreaName();
				if(StringUtils.isBlank(configuredRepairAreaName) || QiConstant.CLEAR_REPAIR_AREA.equalsIgnoreCase(configuredRepairAreaName.trim()))  {
					getModel().deassignRepairAreaSpaceByProductId();										
				}
				else {
					getModel().assignUnitToRepairArea(configuredRepairAreaName, savedQiDefectResultList.get(0).getDefectResultId());
				}
			}
		}

		return savedQiDefectResultList;
	}

	/**
	 * @param isEngineFiringFlagSet
	 * @return
	 */
	private List<QiDefectResult> createBulkDefectResults(boolean isEngineFiringFlagSet) {
		List<BaseProduct> products = getProductModel().getProcessedProducts();
		List<QiDefectResult> cachedDefectResultList = new ArrayList<QiDefectResult>(getModel().getCachedDefectResultList());
		List<QiDefectResult> defectResultList = new ArrayList<QiDefectResult>();
		getModel().getCachedDefectResultList().clear();

		boolean isProduct = products.get(0) instanceof Product;
		for(BaseProduct product : products) {
			for(QiDefectResult defectResult : cachedDefectResultList) {
				QiDefectResult multiDefectResult = (QiDefectResult)defectResult.deepCopy();
				multiDefectResult.setProductId(product.getProductId());
				if(isProduct)  { //Frame, Engine, Mission, SubProduct
					Product p = (Product)product;
					multiDefectResult.setProductSpecCode(p.getProductSpecCode());
					if(isFrame())  {
					multiDefectResult.setAfOnSequenceNumber(p.getAfOnSequenceNumber() != null ? p.getAfOnSequenceNumber() : 0);
				}
				}
				getModel().getCachedDefectResultList().add(multiDefectResult);
			}
		}
		//the selected actual problem for each product
		defectResultList.addAll(createDefectResults(getModel().getCachedDefectResultList(), isEngineFiringFlagSet));
		return defectResultList;
	}

	private List<QiDefectResult> createDefectResults(List<QiDefectResult> defectResults, Boolean isEngineFiringFlagSet) {
		List<QiDefectResult> savedQiDefectResultList = new ArrayList<QiDefectResult>();
		boolean isFirstDefectResult = true;
		QiDefectResult previousDefectResult = null;
		Boolean isNewDefect = true;
		
		boolean isShippedVin = false;
		// Only to be check for product type : FRAME 
		// and for other product type shipping check is not required.
		if(getModel().getProductType()!=null && !getModel().getProductType().isEmpty()
				&& getModel().getProductType().equalsIgnoreCase(ProductType.FRAME.getProductName())) {
			// Check if the product is already shipped by time of finishing product
			if(isProductShipped(getModel().getProductId())){
				if (!getModel().getProperty().isAllowDefectForShippedVin()) {
					productShippedEvent();
					return null;
				} else {
					isShippedVin = true;
				}
			}
		}
		
		
		// get initial scan timestamp if this is VQ GDP Process Point and GLOBAL_DIRECT_PASS is TRUE
		Date oneHourAfterInitialScan = null;
		String[] vqGdpProcessPointIdArray = getModel().getProperty().getVqGdpProcessPointId();

		if (vqGdpProcessPointIdArray.length > 0) {
			List<String> vqGdpProcessPointIdList = Arrays.asList(vqGdpProcessPointIdArray);
			if (vqGdpProcessPointIdList.contains(getCurrentWorkingProcessPointId())
					&& QiInspectionUtils.isGdpProcessPoint(getCurrentWorkingProcessPointId())) {
				Timestamp initialVqGdpTimestamp = getModel().getInitialVqGdpTimestamp(getModel().getProductId(), vqGdpProcessPointIdList);
				
				if (initialVqGdpTimestamp != null) {
				    Calendar calendar = Calendar.getInstance();
				    calendar.setTime(initialVqGdpTimestamp);
				    calendar.add(Calendar.HOUR_OF_DAY, 1);
				    oneHourAfterInitialScan = calendar.getTime();
			    }
			}
		}
		
		for(QiDefectResult defectResult : defectResults) { //for each actual problem per product

			//check if current tracking status is invalid previous line ID for this process point
			if (getModel().isPreviousLineInvalid()) {
				publishProductPreviousLineInvalidEvent();
				return null;
			}
			
			if (!QiInspectionUtils.isGlobalGdpEnabled() && oneHourAfterInitialScan != null) {
				Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
				if (currentTimestamp.after(oneHourAfterInitialScan) && defectResult.getGdpDefect() == (short)1) {
					defectResult.setGdpDefect((short)0);
				}
			}

			if(defectResult.getEngineFiringFlag() == 1){
				isEngineFiringFlagSet =  true;
			}

			if (isShippedVin) {
				defectResult.setReportable((short)QiReportable.NON_REPORTABLE_AFTER_SHIP.getId());
			}

			// check if navigation came from repair entry
			if (repairDefectDetails != null && !repairDefectDetails.isEmpty()) {
				isNewDefect = (Boolean) repairDefectDetails.get("isNewDefect");
				if(!getProductModel().isBulkProcess()) {
					if (isFirstDefectResult) {
						defectResult = createRepairRelatedDefect(defectResult, null);
						previousDefectResult = defectResult;
						isFirstDefectResult = false;

					} else {
						defectResult = createRepairRelatedDefect(defectResult, previousDefectResult);
					}
				} else {
					if (isFirstDefectResult) {
						//create repair results for that product/actual problems
						defectResult = createBulkRepairRelatedDefect(defectResult, null);
						if(defectResult != null) previousDefectResult = defectResult;
						isFirstDefectResult = false;

					} else {
						if(previousDefectResult != null)  {
						defectResult.setDefectTransactionGroupId(previousDefectResult.getDefectTransactionGroupId());
						}
						defectResult = createBulkRepairRelatedDefect(defectResult, previousDefectResult);
					}
				}	
			} else {
				if (isFirstDefectResult) {
					if(isShowKickoutPane && defectResult.getKickoutProcessPointId() != null) {
						KickoutDto kickoutDto = getModel().createKickout(defectResult);
						defectResult.setKickoutId(kickoutDto.getKickoutId());
					}
					defectResult = getModel().createDefectResult(defectResult, null);
					previousDefectResult = defectResult;
					isFirstDefectResult = false;
				} else {
					defectResult.setDefectTransactionGroupId(previousDefectResult.getDefectTransactionGroupId());
					defectResult = getModel().createDefectResult(defectResult, previousDefectResult);
				}
				ProductSearchResult.addNewDefect(defectResult);

			}
			if(defectResult == null)  continue;
			createIqsScore(defectResult);
			
			if(defectResult.getOriginalDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
				//@KM : create actual problem along with main defect if new non repairable main defect
				if(isNewDefect) {
					createAcutalProblem(defectResult);
				}
				ScrapService scrapService = ServiceFactory.getService(ScrapService.class);
				scrapService.scrapProduct(createRequestDc(defectResult));
			}
			
			savedQiDefectResultList.add(defectResult);
			if(isProductSequenceStation() && isDeleteProductSequence())  {
				deleteProductSequence();
			}
		}
		return savedQiDefectResultList;
	}
	
	public void createAcutalProblem(QiDefectResult mainDefect) {
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
		qiRepairResult.setActualProblemSeq((short) 1);
		qiRepairResult = getModel().createRepairResult(qiRepairResult,null);

		QiRepairResultDto addedChildDefectDto = new QiRepairResultDto(mainDefect, qiRepairResult.getRepairId());
		addedChildDefectDto.setActualTimestamp(new Timestamp(qiRepairResult.getActualTimestamp().getTime()));
		ProductSearchResult.addChildDefect(addedChildDefectDto);
	}
	
	private DefaultDataContainer createRequestDc(QiDefectResult defectResult) {
		DefaultDataContainer requestDc = new DefaultDataContainer();
		requestDc.put(TagNames.PRODUCT_ID.name(), new ArrayList<String>(Arrays.asList(defectResult.getProductId().toString())));
		requestDc.put(TagNames.REASON.name(), defectResult.getDefectTypeName());
		requestDc.put(TagNames.APPLICATION_ID.name(), getCurrentWorkingProcessPointId());
		requestDc.put(TagNames.PROCESS_POINT_ID.name(), getCurrentWorkingProcessPointId());
		requestDc.put(TagNames.PRODUCT_TYPE.name(), getModel().getProductType());
		//we could use getModel().getUserId() as in the userid set for the defect and actual problem,
		//however this gets the ApplicationContext's userid; the user id should come from the current window's application context
		requestDc.put(TagNames.ASSOCIATE_ID.name(), getView().getMainWindow().getApplicationContext().getUserId());
		requestDc.put(TagNames.CURRENT_DATE.name(), getModel().getProductionDate());
		requestDc.put(TagNames.PROCESS_LOCATION.name(), defectResult.getInspectionPartLocationName());
		requestDc.put(TagNames.COMMENT.name(), defectResult.getComment());
		return requestDc;
	}
	
	
	private void createIqsScore(QiDefectResult defectResult) {
		if(!getProductModel().isTrainingMode() && defectResult.getIqsScore() > 0) {
			QiDefectIqsScore iqsScore = new QiDefectIqsScore(defectResult);
			ServiceFactory.getDao(QiDefectIqsScoreDao.class).save(iqsScore);
		}
	}
	
	public void clearFilters()  {
		part1TextFilter = "";
		getView().getPartSearchTextField().clear();
		this.selectedPartName = null;
	}

	/**
	 * checker to check if the product has been shipped or not.
	 * 
	 * @param productId
	 * @return	boolean 
	 */
	private boolean isProductShipped(String productId){
		ShippingStatus shippingStatus = ServiceFactory.getDao(ShippingStatusDao.class).findByKey(productId);
		if(shippingStatus==null || ShippingStatusEnum.S90A.equals(ShippingStatusEnum.getShippingStatusByStatus(shippingStatus.getStatus()))) return false;
		else return true;
	}
	private void productShippedEvent(){
		EventBusUtil.publishAndWait(new StatusMessageEvent("", StatusMessageEventType.CLEAR));
		EventBusUtil.publishAndWait(new StatusMessageEvent(PRODUCT_SHIPPED_MSG, StatusMessageEventType.ERROR));
		EventBusUtil.publishAndWait(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_SHIPPED));
	}

	public void startLoadData() {
		getModel().getCachedDefectResultList().clear();
		getView().getPartSearchTextField().setText(StringUtils.EMPTY);
		getView().getMenuSearchTextField().setText(StringUtils.EMPTY);
		getView().getPartSearchTextField().getSuggestionList().clear();
		getView().getDefect1TitledPane().setText("Primary Defect");
		getView().getEntryScreenListView().selectFirst();
		getModel().refreshCache();
		getModel().setCurrentWorkingProcessPointId(getProcessPointId());
		setCurrentWorkingProcessPointId(getProcessPointId());
		getModel().setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		setCurrentWorkingEntryDept(getApplicationContext().getEntryDept());
		// if multi_line station load station configuration of qics station associated with that line
		if(isMultiLine())  {
			setQicsStation();
		}
		part1TextFilter  = getPart1Name();
		getView().getPartSearchTextField().setText(part1TextFilter);
		loadInitialData(part1TextFilter);
		setResponsibilityComboboxDisable(true);
	}

	/**
	 * This method will be used to create defect encountered while fixing
	 * defects or assigning actual defect to main defect.
	 */
	private QiDefectResult createRepairRelatedDefect(QiDefectResult defectResult, QiDefectResult previousQiDefectResult) {
		Boolean isRepairRelated = (Boolean) repairDefectDetails.get("Repair_Related");
		defectResult.setIsRepairRelated((short) (isRepairRelated ? 1 : 0));
		Boolean isNewDefect = (Boolean) repairDefectDetails.get("isNewDefect");
		QiDefectResult qiDefectResult = null;
		Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
		if (isNewDefect) { //click "Add New Defects" on repair screen

			if(!getProductModel().isTrainingMode()) {
				if(isShowKickoutPane && defectResult.getKickoutProcessPointId() != null) {
					KickoutDto kickoutDto = getModel().createKickout(defectResult);
					defectResult.setKickoutId(kickoutDto.getKickoutId());
				}
				qiDefectResult = getModel().createDefectResult(defectResult, previousQiDefectResult);
				AbstractRepairEntryController.isNewDefect = true;
			} else {
				QiRepairResultDto repairDto = new QiRepairResultDto(defectResult,0);
				//Fix the defect that the defect list is empty after added a defect with same defect status  				
				repairDto.setActualTimestamp(timestamp);
				if(repairDto.getDivisionName() == null || repairDto.getDivisionName() == "") {
					//Set Dept Name 
					if(repairDto.getEntryDept() != null) {
						Division division = getProductModel().findDivision(repairDto.getEntryDept());
						if(division != null)
							repairDto.setDivisionName(division.getDivisionName());
					}
				}
				AbstractRepairEntryView.getParentCachedDefectList().add(repairDto);
			}
		} else {
			// click "Assign the Actual Problem to Defect" on repair screen
			List<QiRepairResultDto> allRepairItems = (List<QiRepairResultDto>) repairDefectDetails.get("all_repair_items");
			if(allRepairItems == null || allRepairItems.isEmpty()) return qiDefectResult;
			for(QiRepairResultDto selectedDefect : allRepairItems)  {
				defectResult.setDefectResultId(selectedDefect.getDefectResultId()); 
				if(!getProductModel().isTrainingMode()){
					QiRepairResult qiRepairResult = new QiRepairResult(defectResult);
					qiRepairResult.setCreateUser(getView().getMainWindow().getUserId().toUpperCase());
					qiRepairResult.setActualProblemSeq((short) (selectedDefect.getChildRepairResultList() != null
							? selectedDefect.getChildRepairResultList().size() + 1 : 1));
					qiRepairResult = getModel().createRepairResult(qiRepairResult, previousQiDefectResult);
					overrideOriginalDefect(defectResult, selectedDefect);
					qiDefectResult = new QiDefectResult(qiRepairResult);
				}
				else{
					long maxRepairId = getModel().findMaxRepairId();
					QiRepairResultDto qiRepairResultDto = new QiRepairResultDto(defectResult,maxRepairId+1);
					qiRepairResultDto.setActualTimestamp(timestamp);
					if(qiRepairResultDto.getDivisionName() == null || qiRepairResultDto.getDivisionName() == "") {
						//Set Dept Name 
						if(qiRepairResultDto.getEntryDept() != null) {
							Division division = getProductModel().findDivision(qiRepairResultDto.getEntryDept());
							if(division != null)
								qiRepairResultDto.setDivisionName(division.getDivisionName());
						}
					}
					List<QiRepairResultDto> childList = new ArrayList<QiRepairResultDto>();
					if(selectedDefect.getChildRepairResultList()==null)  {
						selectedDefect.setChildRepairResultList(childList);
					}
					selectedDefect.getChildRepairResultList().add(qiRepairResultDto);
					selectedDefect.setResponsibleDept(defectResult.getResponsibleDept());
					if(selectedDefect.getCurrentDefectStatus() != qiRepairResultDto.getCurrentDefectStatus()) {
						if(qiRepairResultDto.getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
							selectedDefect.setCurrentDefectStatus(qiRepairResultDto.getCurrentDefectStatus());
						}
						else if(qiRepairResultDto.getCurrentDefectStatus() == DefectStatus.FIXED.getId())  {
							boolean allFixed = true;
							for(QiRepairResultDto dto : selectedDefect.getChildRepairResultList())  {
								if(dto.getCurrentDefectStatus() != DefectStatus.FIXED.getId())  {
									allFixed = false;
								}
							}
							if(allFixed)  {
								selectedDefect.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
							}
						}
						else {
							selectedDefect.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
						}
					}
				}
			}
		}
		return qiDefectResult;
	}

	/**
	 * @param defectResult : selected actual problem for each product
	 * @param object
	 * 
	 * @return
	 */
	private QiDefectResult createBulkRepairRelatedDefect(QiDefectResult defectResult, QiDefectResult previousQiDefectResult) {
		Boolean isRepairRelated = (Boolean) repairDefectDetails.get("Repair_Related");
		defectResult.setIsRepairRelated((short) (isRepairRelated ? 1 : 0));
		Boolean isNewDefect = (Boolean) repairDefectDetails.get("isNewDefect");
		QiDefectResult qiDefectResult = null;

		Timestamp timestamp = getDao(QiDefectResultDao.class).getDatabaseTimeStamp();
		if (isNewDefect) { //click "Add New Defects" on repair screen
			if(!getProductModel().isTrainingMode()) {
				if(isShowKickoutPane && defectResult.getKickoutProcessPointId() != null) {
					KickoutDto kickoutDto = getModel().createKickout(defectResult);
					defectResult.setKickoutId(kickoutDto.getKickoutId());
				}
				qiDefectResult = getModel().createDefectResult(defectResult, previousQiDefectResult);
				QiRepairResultDto defectResultDto = new QiRepairResultDto(qiDefectResult, 0);
				defectResultDto.setActualTimestamp(new Timestamp(qiDefectResult.getActualTimestamp().getTime()));
				if(defectResultDto.getDivisionName() == null || defectResultDto.getDivisionName() == "") {
					//Set Dept Name 
					if(defectResultDto.getEntryDept() != null) {
						Division division = getProductModel().findDivision(defectResultDto.getEntryDept());
						if(division != null)
							defectResultDto.setDivisionName(division.getDivisionName());
					}
				}
				ProductSearchResult.addNewDefect(defectResultDto);
				RepairEntryController.isNewDefect = true;
			} else {				
				QiRepairResultDto parentDefectDto = new QiRepairResultDto(defectResult,0);				
				parentDefectDto.setActualTimestamp(timestamp);
				if(parentDefectDto.getDivisionName() == null || parentDefectDto.getDivisionName() == "") {
					//Set Dept Name 
					if(parentDefectDto.getEntryDept() != null) {
						Division division = getProductModel().findDivision(parentDefectDto.getEntryDept());
						if(division != null)
							parentDefectDto.setDivisionName(division.getDivisionName());
					}
				}
				//Create defect result Id 
				long maxDefectResultId = this.getModel().findMaxDefectResultId();
				int addedParentDefectCount = 1;
				Map<QiRepairResultDto, Integer> cachedDefects = this.getProductModel().getCachedDefectsForTraingMode();
				if(cachedDefects != null && cachedDefects.size() > 0) {
					for(Integer type : cachedDefects.values()) {
						if(type == QiConstant.ADD_PARENT_DEFECT_FOR_TM)
							addedParentDefectCount+=1;
					}
				}
				parentDefectDto.setDefectResultId(maxDefectResultId + addedParentDefectCount);			
				ProductSearchResult.addNewDefect(parentDefectDto);
				this.getProductModel().getCachedDefectsForTraingMode().put(parentDefectDto, QiConstant.ADD_PARENT_DEFECT_FOR_TM);
			}
		} else {
			// click "Assign the Actual Problem to Defect" on repair screen
			QiRepairResultDto selectedDefect = (QiRepairResultDto) repairDefectDetails.get("selectedDefect");
			List<QiRepairResultDto> allRepairDetails = new ArrayList<QiRepairResultDto>();
			List<QiRepairResultDto> addlRepairDetails = (List<QiRepairResultDto>)repairDefectDetails.get("additional_repair_details");
			if(addlRepairDetails != null && !addlRepairDetails.isEmpty())  {
				allRepairDetails.addAll(addlRepairDetails);
			}
			allRepairDetails.add(0, selectedDefect);			
			for(QiRepairResultDto repairItem : allRepairDetails)  { //for each original common defect
				//2023-01-18:VB:there could be multiple duplicate defects -- get a list
				//2023-07-17:VB:don't get duplicate defects if only single product
				List<QiRepairResultDto> duplicateList = Arrays.asList(repairItem);
				if(!getProductModel().isSingleProduct())  {
					List<QiRepairResultDto> currentDuplicateList = ProductSearchResult.getDefectResultList(new QiCommonDefectResult(repairItem), defectResult.getProductId());
					if(currentDuplicateList != null && !currentDuplicateList.isEmpty()) {
						//if duplicates are found - could be just 1 - replace duplicate list
						//otherwise, just use the selected defect
						duplicateList = currentDuplicateList;
					}
				}
				for(QiRepairResultDto currentSelectedDefect : duplicateList)  {
					if(currentSelectedDefect == null)  continue;
					defectResult.setDefectResultId(currentSelectedDefect.getDefectResultId());
					if(!getProductModel().isTrainingMode()){
						QiRepairResult qiRepairResult = new QiRepairResult(defectResult);
						qiRepairResult.setDefectTransactionGroupId(selectedDefect.getDefectTransactionGroupId());
						qiRepairResult.setCreateUser(getView().getMainWindow().getUserId().toUpperCase());
						qiRepairResult.setActualProblemSeq((short) (selectedDefect.getChildRepairResultList() != null
								? selectedDefect.getChildRepairResultList().size() + 1 : 1));
						qiRepairResult = getModel().createRepairResult(qiRepairResult, previousQiDefectResult);
						QiDefectResult updatedParentDefect = overrideOriginalDefect(defectResult, currentSelectedDefect);
						//Update main defect in cache
						ProductSearchResult.updateDefectResult(updatedParentDefect, 0);
						//Add new created child defect to cache
						qiDefectResult = new QiDefectResult(qiRepairResult);
						QiRepairResultDto childDefectDto = new QiRepairResultDto(qiDefectResult, qiRepairResult.getRepairId());
						childDefectDto.setActualTimestamp(new Timestamp(qiDefectResult.getActualTimestamp().getTime()));
						if(childDefectDto.getDivisionName() == null || childDefectDto.getDivisionName() == "") {
							//Set Dept Name 
							if(childDefectDto.getEntryDept() != null) {
								Division division = getProductModel().findDivision(childDefectDto.getEntryDept());
								if(division != null)
									childDefectDto.setDivisionName(division.getDivisionName());
							}
						}
						ProductSearchResult.addChildDefect(childDefectDto);
					}
					else{
						long maxRepairId = getModel().findMaxRepairId();
						int addedChildDefectCount = 1;
						Map<QiRepairResultDto, Integer> cachedDefects = this.getProductModel().getCachedDefectsForTraingMode();
						if(cachedDefects != null && cachedDefects.size() > 0) {
							for(Integer type : cachedDefects.values()) {
								if(type == QiConstant.ADD_CHILD_DEFECT_FOR_TM)
									addedChildDefectCount+=1;
							}
						}
						QiRepairResultDto childDefectDto = new QiRepairResultDto(defectResult,maxRepairId+addedChildDefectCount);
						childDefectDto.setDefectTransactionGroupId(selectedDefect.getDefectTransactionGroupId());
						childDefectDto.setActualTimestamp(timestamp);
						if(childDefectDto.getDivisionName() == null || childDefectDto.getDivisionName() == "") {
							//Set Dept Name 
							if(childDefectDto.getEntryDept() != null) {
								Division division = getProductModel().findDivision(childDefectDto.getEntryDept());
								if(division != null)
									childDefectDto.setDivisionName(division.getDivisionName());
							}
						}
						getProductModel().getCachedDefectsForTraingMode().put(childDefectDto, QiConstant.ADD_CHILD_DEFECT_FOR_TM);
						ProductSearchResult.addChildDefect(childDefectDto);
					}
				}
			}
		}
		return qiDefectResult;
	}

	/**
	 * This method Check station configuration for Actual Problem Responsibility
	 * override original Defect Responsibility.
	 */
	private QiDefectResult overrideOriginalDefect(QiDefectResult newQiDefectResult, QiRepairResultDto originalDefectDto) {
		boolean updateDefectResponsibility = false;
		qiEntryStationConfigManagement = getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.STATION_RESPONSIBILITY.getSettingsName());
		if (qiEntryStationConfigManagement != null) {
			if (qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES))
				updateDefectResponsibility = true;
		} else {
			updateDefectResponsibility = Boolean.getBoolean(QiEntryStationConfigurationSettings.STATION_RESPONSIBILITY.getDefaultPropertyValue());
		}

		boolean updateOriginalReportable = false;
		boolean updateOriginalResponsible = false;
		boolean updateOriginalStatus = false;
		QiDefectResult originalQiDefectResult = getModel().findDefectResultById(originalDefectDto.getDefectResultId());
		QiDefectResultHist qiDefectResultHist = null;

		if (originalQiDefectResult.getReportable() != 0 && newQiDefectResult.getReportable() == 0) {
			originalQiDefectResult.setReportable((short)QiReportable.REPORTABLE.getId());
			updateOriginalReportable = true;
		}

		if (updateDefectResponsibility) {
			String newResponsibleSite = newQiDefectResult.getResponsibleSite();
			String newResponsiblePlant = newQiDefectResult.getResponsiblePlant();
			String newResponsibleDept = newQiDefectResult.getResponsibleDept();
			String newResponsibleLevel1 = newQiDefectResult.getResponsibleLevel1();

			if (!originalQiDefectResult.getResponsibleSite().equals(newResponsibleSite) || 
					!originalQiDefectResult.getResponsiblePlant().equals(newResponsiblePlant) || 
					!originalQiDefectResult.getResponsibleDept().equals(newResponsibleDept) || 
					!originalQiDefectResult.getResponsibleLevel1().equals(newResponsibleLevel1)) {

				// create a defect result history record
				qiDefectResultHist = new QiDefectResultHist(originalQiDefectResult);
				qiDefectResultHist.setChangeUser(getModel().getUserId());

				// setting same responsibility as selected defect to the original defect
				originalQiDefectResult.setResponsibleSite(newResponsibleSite);
				originalQiDefectResult.setResponsiblePlant(newResponsiblePlant);
				originalQiDefectResult.setResponsibleDept(newResponsibleDept);
				originalQiDefectResult.setResponsibleLevel1(newResponsibleLevel1);

				// Get responsibility Level 2 and Level 3 based on Level 1
				List<QiDefectResultDto> levelResultList = getModel().findLevel2andLevel3ByLevel1(newQiDefectResult.getResponsibleSite(), newQiDefectResult.getResponsiblePlant(),
						newQiDefectResult.getResponsibleDept(), newQiDefectResult.getResponsibleLevel1());
				QiDefectResultDto levelResult = !levelResultList.isEmpty() ? levelResultList.get(0) : null;
				originalQiDefectResult.setResponsibleLevel2(levelResult != null ? levelResult.getLevelTwo() : null);
				originalQiDefectResult.setResponsibleLevel3(levelResult != null ? levelResult.getLevelThree() : null);
				originalQiDefectResult.setUpdateUser(getModel().getUserId());
				originalQiDefectResult.setReasonForChange(REASON_FOR_CHANGE_RESP_CHANGED_BY_ACTUAL_PROBLEM);

				updateOriginalResponsible = true;
			}
		}
		
		if(originalQiDefectResult.getCurrentDefectStatus() != newQiDefectResult.getCurrentDefectStatus()) {
			if(newQiDefectResult.getCurrentDefectStatus() == DefectStatus.NON_REPAIRABLE.getId()) {
				originalQiDefectResult.setCurrentDefectStatus(newQiDefectResult.getCurrentDefectStatus());
			}
			else if(newQiDefectResult.getCurrentDefectStatus() == DefectStatus.FIXED.getId())  {
				if(areAllActualProblemsFixed(originalQiDefectResult.getDefectResultId()))  {
					originalQiDefectResult.setCurrentDefectStatus((short)DefectStatus.FIXED.getId());
				}
			}
			else {
				originalQiDefectResult.setCurrentDefectStatus((short)DefectStatus.NOT_FIXED.getId());
			}
			if(qiDefectResultHist!=null) {
				qiDefectResultHist.setCurrentDefectStatus(newQiDefectResult.getCurrentDefectStatus());
			}else {
				qiDefectResultHist = new QiDefectResultHist(originalQiDefectResult);
				qiDefectResultHist.setChangeUser(getModel().getUserId());
				qiDefectResultHist.setCurrentDefectStatus(newQiDefectResult.getCurrentDefectStatus());
			}
			updateOriginalStatus= true;
		}

		if (updateOriginalReportable || updateOriginalResponsible || updateOriginalStatus) {
			// update original defect's responsibility or reportable
			originalQiDefectResult = getModel().updateDefectResult(originalQiDefectResult);
		}

		if (updateOriginalResponsible || updateOriginalStatus) {
			// save defect result history record
			getDao(QiDefectResultHistDao.class).insert(qiDefectResultHist);

			// update responsibility of old QICS defect result in GAL125TBX
			if (PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isReplicateDefectRepairResult() && updateOriginalResponsible) {
				ServiceFactory.getService(HeadlessNaqService.class).updateLegacyDefectResultResponsibility(originalQiDefectResult);
			}
		}
		return originalQiDefectResult;
	}

	public static boolean areAllActualProblemsFixed(long defectResultId)  {
		
		List<QiRepairResult> repairResultList = RepairEntryModel.findAllRepairResultByDefectResultId(defectResultId);
		boolean allFixed = true;
		if(repairResultList != null)  {
			for (QiRepairResult rr : repairResultList) {
				if (rr.getCurrentDefectStatus() != DefectStatus.FIXED.getId()) {
					allFixed = false;
					break;
				}
			}
		}
		return allFixed;
	}
	
	/**
	 * This method is used to reload Menu ListView
	 */
	private void reloadMenuTileListView() {
		try {
			String menu = StringUtils.trimToEmpty(getView().getMenuSearchTextField().getText());
			QiDefectEntryDto textEntryScreen = getView().getEntryScreenListView().getSelectedItem();
			String entryScreen = textEntryScreen!=null ? StringUtils.trimToEmpty(textEntryScreen.getEntryScreen()) : StringUtils.EMPTY;
			List<String> entryMenuList = getModel().findAllTextEntryMenuByFilter(entryScreen, menu, getPart1TextFilter());
			Collections.sort(entryMenuList);
			getView().getMenuTileListView().setItems(entryMenuList);
			getView().getPart1ListView().getItems().clear();
			getView().getPart2ListView().getItems().clear();
			getView().getDefectListView().getItems().clear();
			getView().getPartDefectListView().getItems().clear();
			getView().getMenuTileListView().selectFirst();
		} catch (Exception e) {
			handleException("An error occured in Reload Menu ListView method", "Failed to load Menu", e);
		}
	}

	/**
	 * This method is used to Enable/Disable Responsibility Combobox
	 */
	public void setResponsibilityComboboxDisable(boolean isDisabled) {

		//if trying to enable, but responsibility is not editable-->no op
		if(!isDisabled && !isResponsibilityAccessible)  return;

		getView().getSiteComboBox().setDisable(isDisabled);
		getView().getPlantComboBox().setDisable(isDisabled);
		getView().getDepartmentComboBox().setDisable(isDisabled);
		if(!isShowL2L3())  {
			getView().getLevel1ComboBox().setDisable(isDisabled);
		}
		else  {
			getRespController().disableComboBoxes(isDisabled);
		}
		if(isShowKickoutPane && !assignRealProblem) {
			getView().getKickoutLocationPane().getDepartmentComboBox().getControl().setDisable(isDisabled);
			getView().getKickoutLocationPane().getLineComboBox().getControl().setDisable(isDisabled);
			getView().getKickoutLocationPane().getProcessPointComboBox().getControl().setDisable(isDisabled);
		}
	}
	
	private void addDefectStatusRadioButtonListener() {
		getView().getToggleGroup().selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(newValue != null) {
					enableRecentDefectButton();
				} else {
					disableRecentDefectButton();
				}
			}
			
		}); 
	}
	
	/**
	 * This method is used to add Listeners on Responsibility ComboBox
	 */
	private void addResponsibleComboBoxListeners() {
		getView().getSiteComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,
					String old_val, String new_val) {
				getView().getPlantComboBox().getSelectionModel().select(null);
				getView().getDepartmentComboBox().getSelectionModel().select(null);
				getView().getLevel1ComboBox().getSelectionModel().select(null);
				getView().getPlantComboBox().getItems().clear();
				getView().getDepartmentComboBox().getItems().clear();
				getView().getLevel1ComboBox().getItems().clear();

				if(!assignedStationResponsibilities.isEmpty()) {
					getView().getPlantComboBox().getItems().addAll(getModel().getDefectEntryCacheUtil().getPlantListFromResponsibilities(assignedStationResponsibilities, new_val));
				} else {
					getView().getPlantComboBox().getItems().addAll(getModel().findAllPlantBySite(new_val));
				}
				Collections.sort(getView().getPlantComboBox().getItems());
				if(getView().getPlantComboBox().getItems().size()==1) {
					getView().getPlantComboBox().getSelectionModel().select(0);
				}
			}
		});

		getView().getPlantComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,
					String old_val, String new_val) {
				String site = (String)getView().getSiteComboBox().getSelectionModel().getSelectedItem();
				getView().getDepartmentComboBox().getSelectionModel().select(null);
				getView().getLevel1ComboBox().getSelectionModel().select(null);
				getView().getDepartmentComboBox().getItems().clear();
				getView().getLevel1ComboBox().getItems().clear();
				if(!assignedStationResponsibilities.isEmpty()) {
					getView().getDepartmentComboBox().getItems().addAll(
							getModel().getDefectEntryCacheUtil().getDeptListFromResponsibilities(assignedStationResponsibilities, site, new_val));
				} else {
					getView().getDepartmentComboBox().getItems().addAll(getModel().findAllDepartmentBySiteAndPlant(site, new_val));
				}
				Collections.sort(getView().getDepartmentComboBox().getItems());
				if(getView().getDepartmentComboBox().getItems().size()==1) {
					getView().getDepartmentComboBox().getSelectionModel().select(0);
				}
			}
		});

		getView().getDepartmentComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,
					String old_val, String new_val) {
				getView().getLevel1ComboBox().getSelectionModel().select(null);
				getView().getLevel1ComboBox().getItems().clear();

				if(new_val == null)  return;
				if(isShowL2L3())  {
					getRespController().loadRespComboBoxesWithAllValues(new_val);
					return;
				}

				String site = (String)getView().getSiteComboBox().getSelectionModel().getSelectedItem();
				String plant = (String)getView().getPlantComboBox().getSelectionModel().getSelectedItem();
				String dept = new_val;
				List<String> lvlNames = null;
				List<QiResponsibleLevel> responsibleLevel1List = null;
				List<KeyValue<String,Integer>> listOfL1 = null;
				if(assignedStationResponsibilities != null && !assignedStationResponsibilities.isEmpty()) {
					lvlNames = getModel().getDefectEntryCacheUtil().getLevel1ListFromResponsibilities(assignedStationResponsibilities, site, plant, dept);
					listOfL1 = ResponsibleLevelController.getUniqueListOfNames(lvlNames);
				} else {
					responsibleLevel1List = getModel().findAllBySitePlantDepartmentLevel(site, plant, dept,(short)1);
					listOfL1 = ResponsibleLevelController.getUniqueListOfResponsibleLevelNames(responsibleLevel1List);
				}
				if(listOfL1 != null && !listOfL1.isEmpty())  {
					Collections.sort(listOfL1,ResponsibleLevelController.getKVComparator());
					getView().getLevel1ComboBox().getItems().addAll(listOfL1);
					if(getView().getLevel1ComboBox().getItems().size()==1) {
						getView().getLevel1ComboBox().getSelectionModel().select(0);
					}
				}

			}
		});
	}
	/**
	 * This method is used to open Recent Defect PopUp screen
	 */
	private void openRecentDefectPopup(){

		RecentDefectDialog dialog = null;
		QiProgressBar QiProgressBar = null;
		try {
			QiProgressBar = getQiProgressBar("Opening Recent Defect Pop-up.","Opening Recent Defect Pop-up.");
			QiProgressBar.showMe();
			dialog = new RecentDefectDialog(getModel(), getApplicationId(), 
					((LoggedRadioButton) getView().getToggleGroup().getSelectedToggle()).getText(),
					StringUtils.trimToEmpty((String)getView().getWriteUpDeptComboBox().getSelectionModel().getSelectedItem()));

			Logger.getLogger().check("Recent Defects Dialogbox populated");
		} finally {
			if(QiProgressBar != null)  {
				QiProgressBar.closeMe();
			}
		}		
		dialog.showDialog();
		
		QiDefectResult defectResult = getModel().getDefectResult();
		if((defectResult ==null) || StringUtils.isEmpty(defectResult.getImageName()))  {
			getView().getScanTextField().requestFocus();
		}
		if(null!=defectResult)  {
			getView().reload(defectResult);
		}
		setExistingDefectCount();
		resetDefectStatusPanel();
	}
	/**
	 * This method is used to set Responsibility when defect is selected
	 */
	public void setResponsibilityOnDefectSelect(QiDefectResultDto selectedDefect) {
		QiResponsibleLevel level = getModel().findResponsibleLevelById(selectedDefect.getResponsibleLevelId());
		String site = level.getSite();
		String plant = level.getPlant();
		String dept = level.getDepartment();
		String level1 = level.getResponsibleLevelName();

		if(level!=null) {
			getView().getSiteComboBox().getSelectionModel().select(site);
			getView().getPlantComboBox().getSelectionModel().select(plant);
			getView().getDepartmentComboBox().getSelectionModel().select(dept);
			getView().getLevel1ComboBox().getSelectionModel().select(ResponsibleLevelController.getKeyValue(level1));

			//set default responsibility in case it gets overwritten later
			selectedDefect.setDefaultResponsibeLevel1(level);

			if(isShowL2L3())  {
				if(getRespController() != null)  {
					getRespController().setOriginalResponsibleLevel(level);
					getRespController().selectParentLevels(level);
				}
			}
		}
	}

	public Map<String, Object> getRepairDefectDetails() {
		return repairDefectDetails;
	}

	public void setRepairDefectDetails(Map<String, Object> repairDefectDetails) {
		this.repairDefectDetails = repairDefectDetails;
		getModel().getCachedDefectResultList().clear();
		if (repairDefectDetails != null && !(Boolean) repairDefectDetails.get("isNewDefect")) {
			isRepairRelated = true;
			getView().getRepairedRadioBtn().setDisable(!isShowRepairedForActualProblem());
			assignRealProblem = true;
			isActualProblemToDefect = true;
			QiRepairResultDto selectedDefect = (QiRepairResultDto) repairDefectDetails.get("selectedDefect");
			EventBusUtil.publish(new StatusMessageEvent("Current Defect Selected: " + selectedDefect.getDefectDesc(), StatusMessageEventType.INFO));
		} else {
			isRepairRelated = false;
			isActualProblemToDefect = false;
			getView().getRepairedRadioBtn().setDisable(false);
			assignRealProblem = false;
		}
		loadInitialData(StringUtils.EMPTY);
	}

	public boolean isShowRepairedForActualProblem()  {
		String showRepaired = QiEntryStationConfigurationSettings.SHOW_ACTUAL_PROBLEM_REPAIRED.getDefaultPropertyValue();
		QiStationConfiguration qiConfig = getModel().findStationConfiguration(QiEntryStationConfigurationSettings.SHOW_ACTUAL_PROBLEM_REPAIRED.getSettingsName());
		if(qiConfig != null)  {
			showRepaired = qiConfig.getPropertyValue();
		}
		if(StringUtils.isBlank(showRepaired))  {
			showRepaired = "";
		}
		if(showRepaired.equalsIgnoreCase(QiConstant.YES))  {
				return true;
		}
		else  {
				return false;
		}
	}
	
	/**
	 * This method will be invoke by repair entry screen to create defect.
	 */
	public void setProductEventDetails(Map<String, Object> productDetails) {
		setRepairDefectDetails(productDetails);
	}

	/**
	 * This method finds is user allowed to overwrite responsibility.
	 */
	public boolean isResponsibilityAccessible(){
		boolean isAccessible=QiEntryStationConfigurationSettings.RESPONSIBILITY.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		qiEntryStationConfigManagement= getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.RESPONSIBILITY.getSettingsName());
		if(qiEntryStationConfigManagement !=null){
			isAccessible=qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		}
		return isAccessible;
	}

	/**
	 * This method finds the default defect status as configured from Station Configuration Screen.
	 */
	private String getDefaultDefectStatus(){
		qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(QiConstant.ENTRY_STATION_DEFAULT_DEFECT_STATUS);
		String defaultDefectStatus=StringUtils.EMPTY;
		if(qiEntryStationConfigManagement!=null)
			defaultDefectStatus=qiEntryStationConfigManagement.getPropertyValue();
		return defaultDefectStatus; 
	}

	/**
	 * This method Check configuration for Duplicate entry warning dialog and accept/rejects the defect accordingly
	 */
	private void checkAcceptDefect(QiDefectResult defectResult){
		QiStationConfiguration qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.DUPLICATE.getSettingsName());
		String allowDuplicate=QiEntryStationConfigurationSettings.DUPLICATE.getDefaultPropertyValue();
		if(qiEntryStationConfigManagement!=null &&qiEntryStationConfigManagement.getPropertyValue()!=null){
			allowDuplicate=qiEntryStationConfigManagement.getPropertyValue();
		}
		QiStationConfiguration qiStationConfiguration =getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.ADD_COMMENT_FOR_CHANGING_RESPONSIBILITY.getSettingsName()); 

		if(!allowDuplicate.equalsIgnoreCase(QiEntryStationConfigurationSettings.DUPLICATE.getAvailablePropertyValue1())){
			if(allowDuplicate.equalsIgnoreCase(QiEntryStationConfigurationSettings.DUPLICATE.getAvailablePropertyValue2())){
				Stage stage = (!isAutoDefectEntry() && isPopupAccept()) ? acceptDialog : getView().getStage();
				boolean isOk = MessageDialog.confirm(stage,DUPLICATE_PART_DEFECT_ACCEPT_MSG);
				if(isOk) {
					if (qiStationConfiguration != null) {
						if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
							final String status = addReasonForChangingResponsibility(defectResult);
							if (status.equals(QiConstant.ASSIGN)) {
								defectResult.setComment(FXOptionPane.getCommentText());
							} else if(status.equals(QiConstant.DEASSIGN)){
								return;
							}
						}
					}
					getModel().getCachedDefectResultList().add(defectResult);
					getView().setLastDefectEnteredText(true);
					EventBusUtil.publish(new StatusMessageEvent(DEFECT_SUCCESSFULLY_ACCEPTED, StatusMessageEventType.INFO));
					EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_ACCEPT));
				}else{
					return;
				}
			}else{
				Stage stage = (isAcceptButton() && isPopupAccept()) ? acceptDialog : getView().getStage();
				MessageDialog.showError(stage,DUPLICATE_PART_DEFECT_REJECT_ERROR_MSG);
				return;
			}
		}else{
			if (qiStationConfiguration != null) {
				if (StringUtils.trimToEmpty(qiStationConfiguration.getPropertyValue()).equalsIgnoreCase(QiConstant.YES)) {
					final String status = addReasonForChangingResponsibility(defectResult);
					if (status.equals(QiConstant.ASSIGN)) {
						defectResult.setComment(FXOptionPane.getCommentText());
					} else if(status.equals(QiConstant.DEASSIGN)){
						return;
					}
				}
			}
			getModel().getCachedDefectResultList().add(defectResult);
			getView().setLastDefectEnteredText(true);
			EventBusUtil.publish(new StatusMessageEvent(DEFECT_SUCCESSFULLY_ACCEPTED, StatusMessageEventType.INFO));
			EventBusUtil.publish(new ProductEvent(StringUtils.EMPTY, ProductEventType.PRODUCT_DEFECT_ACCEPT));
		}
	}

	/**
	 * This method Check station configuration to Show all previous defects on image.
	 */
	public boolean checkShowDefectConfig() {
		qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.DEFECTS.getSettingsName());
		boolean isShowDefects=QiEntryStationConfigurationSettings.DEFECTS.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;;
		if(qiEntryStationConfigManagement!=null){
			isShowDefects=qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		}
		return isShowDefects;
	}
	/**
	 * This method is to load Recent Defect Data on Defect Entry screen.
	 * @param result
	 */
	public boolean loadRecentDefectData(QiDefectResult result){
		defectEntryByImageController.removeEllipse();
		if(StringUtils.isEmpty(result.getImageName())){
			if(isEntryScreenExist(getView().getEntryScreenListView(), result.getEntryScreen())) {
				if(!getModel().isPdcExistInCurrentSession(result)) {
					displayErrorMessage("Part Defect Combination does not exist");
					return false;
				}
				getView().getdefectPanelByImage().setVisible(false);
				getView().getDefectPanelByText().setVisible(true);
				getView().getLastDefectEnteredTextAreaForText().setVisible(true);
				getView().getLastDefectEnteredTextAreaForText().setText("Current Defect Selected: \n\""+result.getPartDefectDesc()+"\"");
			} else {
				displayErrorMessage("Entry Screen does not exist");
				return false;
			}
		}else{
			if(isEntryScreenExist(getView().getEntryScreenListView(), result.getEntryScreen())) {
				if(!getModel().isPdcExistInCurrentSession(result)) {
					displayErrorMessage("Part Defect Combination does not exist");
					return false;
				}
				double resizePercent = ((getView().getScreenHeight() * 0.67) - 500) / 5;
				pointX = (result.getPointX() + ((result.getPointX() * resizePercent) / 100));
				pointY = (result.getPointY() + ((result.getPointY() * resizePercent) / 100));
				getView().getDefectPanelByText().setVisible(false);
				QiImage qiImage = getModel().getImageByImageName(result.getImageName());
				getView().getdefectPanelByImage().toFront();
				getView().getdefectPanelByImage().setVisible(true);
				getView().getDefectPanelByText().setVisible(false);
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT1_CLEAR_ITEMS, null, ObservableListChangeEventType.CHANGE_SELECTION));
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT2_CLEAR_ITEMS, null, ObservableListChangeEventType.CHANGE_SELECTION));
				getView().getDefectImageView().setImage(new Image(new ByteArrayInputStream(qiImage.getImageData())));
				Ellipse ellipse = new Ellipse(3,3);
				ellipse.setFill(Color.BLUE);
				ellipse.setCenterX(pointX);
				ellipse.setCenterY(pointY);
				defectEntryByImageController.removeArc();
				defectEntryByImageController.removeAllImageSections();
				getView().getImagePane().getChildren().add(ellipse);
				getView().getLastDefectEnteredTextAreaForImage().setText("Current Defect Selected: \n\""+result.getPartDefectDesc()+"\"");
			} else {
				displayErrorMessage("Entry Screen does not exist");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method will select entry screen item based on given entry screen.
	 * 
	 * @param entryScreenView
	 * @param entryScreen
	 */
	private boolean isEntryScreenExist(TileListView<QiDefectEntryDto> entryScreenView, String entryScreen) {
		for (QiDefectEntryDto defectEntryDto : entryScreenView.getItems()) {
			if (entryScreen.equalsIgnoreCase(defectEntryDto.getEntryScreen())) {
				entryScreenView.select(defectEntryDto);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to reset data.
	 */
	private void resetData(){
		if(getView().getDefectPanelByText().isVisible()){
			if(getView().getMenuTileListView().getItems().isEmpty()){
				getView().getdefectPanelByImage().setVisible(false);
				getView().getDefectPanelByText().setVisible(false);
			}else {
				QiDefectEntryDto qiDefectEntryDto = getView().getEntryScreenListView().getSelectedItem();
				getView().getEntryScreenListView().clearSelection();
				getView().getEntryScreenListView().select(qiDefectEntryDto);
			}
		}else if(getView().getdefectPanelByImage().isVisible()){
			if(getView().getDefect1ListView().getItems().isEmpty()){
				getView().getdefectPanelByImage().setVisible(false);
				getView().getDefectPanelByText().setVisible(false);
			}else{
				String defect2 = StringUtils.EMPTY;
				String selectedDefect = getView().getDefect1ListView().getSelectionModel().getSelectedItem();
				defect2 = getView().getSecondarySelectedDefect();
				QiDefectEntryDto qiDefectEntryDto = getView().getEntryScreenListView().getSelectedItem();
				getView().getEntryScreenListView().clearSelection();
				getView().getEntryScreenListView().select(qiDefectEntryDto);
				getView().getDefect1TitledPane().setText(DefectEntryByImageController.PRIMARY_DEFECT);
				getView().getDefect1ListView().getSelectionModel().select(selectedDefect);
				getView().getDefect1ListView().scrollTo(selectedDefect);
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT2, defect2, ObservableListChangeEventType.CHANGE_SELECTION));
			}
		}
		getView().setLastDefectEnteredText(true);
		resetOptionalData();
		resetDefectStatusPanel();
		Logger.getLogger().check("Defect Entry Screen data was reset successfully");
	}

	/**
	 * This method is called after click the Reset Button
	 */
	private void fullyResetData(){
		//Reset the Primary Defect
		getView().getEntryScreenListView().clearSelection();
		if(getView().getEntryScreenListView().getItems() != null && getView().getEntryScreenListView().getItems().size() > 0)
			getView().getEntryScreenListView().selectFirst();
		//Clear the Current Defect detail or show the Last Entered Defect
		getView().setLastDefectEnteredText(true);
		resetDefectStatusPanel();

		resetOptionalData();
		Logger.getLogger().check("Defect Entry Screen data was fully reset successfully");
	}
	
	private void resetOptionalData() {
		if(isShowL2L3())  {
			getRespController().clearAll();
		}

		if(isIqsInput()) {
			getView().resetIqsScoreSelection();
		}
	}

	/**
	 * This method is used to save station result data
	 * @param defectResultList
	 * @return
	 */
	private StationResult saveStationResult(List<QiDefectResult> defectResultList) {
		if (getModel().getSchedule() == null) {
			return null;
		}
		StationResult stationResult = getStationResult();
		DailyDepartmentSchedule shiftFirstPeriod = getModel().findDailyDepartmentScheduleByCurrentSchedule();

		if (shiftFirstPeriod == null || shiftFirstPeriod.getStartTimestamp() == null) {
			return stationResult;
		}
		if (getModel().isProductProcessed(shiftFirstPeriod)) {
			stationResult = updateStationResult(defectResultList, stationResult, false);
			return stationResult;
		}
		// only update station result when the product has not been processed at this station after the beginning of this shift
		stationResult = updateStationResult(defectResultList, stationResult, true);
		return stationResult;
	}

	private void saveIPPStationResult() {
		StationResult stationResult = getStationResult();
		stationResult.updateStationResultCount(DefectStatus.IPP_ENTRY_TAG,false,1,true);
		stationResult.setLastProductId(getModel().getProductId());
		stationResult = getModel().saveStationResult(stationResult);
	}
	
	/**
	 * This method is used to update Station Result value
	 * @param defectResultList
	 * @param stationResult
	 * @param isDifferentProductId
	 * @return
	 */
	private StationResult updateStationResult(List<QiDefectResult> defectResultList, StationResult stationResult, boolean isDifferentProductId) {
		boolean isDifferent = isDifferentProductId;
		int quantity = 1;
		int count = 0;
		boolean isScannedOnce = true;
		if (getProductModel().isBulkProcess()) {
			quantity = getProductModel().getProcessedProducts().size();
			count = defectResultList.size()/quantity;
		}else if (!getModel().getProperty().isUpcStation()){
			count = defectResultList.size();
		}else if (getProductModel().isBulkProcess()) {
			quantity = ProductSearchResult.getProcessedProductsCount();
			count = defectResultList.size()/quantity;
		}else{
			quantity = Integer.parseInt(getModel().getQuantity());
			count = defectResultList.size()/quantity;
		}
		if(defectResultList.isEmpty()) {
			stationResult.updateStationResultCount(deriveDefectStatus(defectResultList),isDifferent,quantity,isScannedOnce);
		} else {
			for(int i = 0 ; i < count ; i++) {
				stationResult.updateStationResultCount(deriveDefectStatus(defectResultList),isDifferent,quantity,isScannedOnce);
				isDifferent = false;
				isScannedOnce = false;
			}
		}
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

	/**
	 * This method is used to derive Defect Status
	 * @param defectResultList
	 * @return
	 */
	private DefectStatus deriveDefectStatus(List<QiDefectResult> defectResultList) {
		DefectStatus defectStatus;
		if(defectResultList == null || defectResultList.isEmpty()) 
			defectStatus = DefectStatus.DIRECT_PASS;
		else {
			if(hasNonRepairableDefect(defectResultList))
				defectStatus = DefectStatus.NON_REPAIRABLE;
			else {
				defectStatus = hasNotRepairedDefect(defectResultList) ? 
						DefectStatus.NOT_REPAIRED:DefectStatus.REPAIRED;
			}
		}
		return defectStatus;
	}
	/**
	 * This method is used to check if the defect list has non repairable defect or not
	 * @param defectResultList
	 * @return
	 */
	private boolean hasNonRepairableDefect(List<QiDefectResult> defectResultList) {
		for(QiDefectResult defectResult : defectResultList) {
			if(defectResult.isDefectNotRepairable()) 
				return true;
		}
		return false;
	}
	/**
	 * This method is used to check if the defect list has not repaired defects or not
	 * @param defectResultList
	 * @return
	 */
	private boolean hasNotRepairedDefect(List<QiDefectResult> defectResultList) {
		for(QiDefectResult defectResult : defectResultList) {
			if(defectResult.isDefectNotRepaired()) 
				return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Subscribe()
	public void onDefectSelectEvent(ObservableListChangeEvent event) {
		if (event == null)
			return;
		if (event == null || !(event.getObject() instanceof DefectEntrySelection))  {
			return;
		}
		//following cast throws exception, so adding check above

		DefectEntrySelection selection = (DefectEntrySelection)event.getObject();
		if(event.getEventType().equals(ObservableListChangeEventType.CHANGE_SELECTION)) {
			selectValue = true;

			switch(selection){
			case DEFECT1:
				getView().getDefect1ListView().getSelectionModel().select((String)event.getValue());
				getView().getDefect1ListView().scrollTo((String)event.getValue());
				break;

			case DEFECT1_SET_ITEMS:
				getView().getDefect1ListView().setItems(FXCollections.observableArrayList((List<String>)event.getValue()));
				break;

			case PART1:
				getView().getPart1ListView().getSelectionModel().select((String)event.getValue());
				getView().getPart1ListView().scrollTo((String)event.getValue());
				break;

			case LOC:
				getView().getLocListView().getSelectionModel().select((String)event.getValue());
				getView().getLocListView().scrollTo((String)event.getValue());
				break;

			case PART2:
				getView().getPart2ListView().getSelectionModel().select((String)event.getValue());
				getView().getPart2ListView().scrollTo((String)event.getValue());
				break;

			case DEFECT:
				getView().getDefectListView().getSelectionModel().select((String)event.getValue());
				getView().getDefectListView().scrollTo((String)event.getValue());
				break;

			case PART1_SET_ITEMS:
				getView().getPart1ListView().setItems(FXCollections.observableArrayList((List<String>)event.getValue()));
				break;

			case LOC_SET_ITEMS:
				getView().getLocListView().setItems(FXCollections.observableArrayList((List<String>)event.getValue()));
				break;

			case PART2_SET_ITEMS:
				getView().getPart2ListView().setItems(FXCollections.observableArrayList((List<String>)event.getValue()));
				break;

			case DEFECT_SET_ITEMS:
				getView().getDefectListView().setItems(FXCollections.observableArrayList((List<String>)event.getValue()));
				break;

			case PART_DEFECT_SET_ITEMS:
				getView().getPartDefectListView().setItems(FXCollections.observableArrayList((List<QiDefectResultDto>)event.getValue()));
				break;

			case DEFECT1_CLEAR_ITEMS:
				getView().getDefect1ListView().getSelectionModel().clearSelection();
				getView().getDefect1ListView().getItems().clear();
				break;

			default:
				break;
			}
			selectValue = false;
		} else if(event.getEventType().equals(ObservableListChangeEventType.WARNING)) {
			if(!isAcceptButton()) {
				acceptDefect();
			}
			else  if(selection.equals(DefectEntrySelection.SHOW_CONFIRM_POPUP)){
				showConfirmDialog();
			}
		}
	}

	@Subscribe
	public void processEvent(SessionEvent event) {
		try {
			String applicationId = event.getApplicationId();
			if (applicationId == null || getApplicationId().equals(applicationId)) {
				switch(event.getEventType()) {
				case SESSION_END:
					getView().setViewStarted(false);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	/**
	 * This method is used to add Titled Pane Listeners
	 */
	private void addSearchableTextFieldOnTitledPane(final TitledPane titledPane, final String component, final boolean isImage) {

		final UpperCaseFieldBean filter = new UpperCaseFieldBean("filter");
		filter.setMinWidth(getView().getScreenWidth() * 0.025);
		filter.setMaxWidth(getView().getScreenWidth() * 0.025);
		filter.setStyle(String.format("-fx-font-size: %dpx;", (int)(0.009 * getView().getScreenWidth())));
		titledPane.setGraphic(filter);
		String titleText = titledPane.getText();
		filter.requestFocus();
		if(!titleText.equalsIgnoreCase(component) && !StringUtils.isBlank(titleText)) {
			filter.setText(titleText);
			filter.selectPositionCaret(titleText.length());
		}
		filter.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(!newValue) {
					titledPane.setGraphic(null);
				}
			}
		});
		filter.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				KeyEvent event;
				if(!StringUtils.isBlank(newValue)) {
					String charc = newValue.substring(newValue.length()-1, newValue.length());
					String filterVal = newValue;
					filter.setText(filterVal);
					if(StringUtils.isBlank(charc))  {
						charc = "Space";
					}
					event = new KeyEvent(KeyEvent.KEY_PRESSED, charc, filterVal, KeyCode.getKeyCode(charc), false, false, false, false);
					if(isImage) {
						refreshOnImageEntryTitledPaneFilter(event, component);
					} else {
						refreshOnTextEntryTitledPaneFilter(event, component);
					}
				} else {
					event = new KeyEvent(KeyEvent.KEY_PRESSED, StringUtils.EMPTY, StringUtils.EMPTY, KeyCode.ESCAPE, false, false, false, false);
					if(isImage) {
						refreshOnImageEntryTitledPaneFilter(event, component);
					} else {
						refreshOnTextEntryTitledPaneFilter(event, component);
					}
				}
			}
		});
		
		filter.setOnKeyPressed((ev) -> {
			if(ev.getCode() == KeyCode.ENTER || ev.getCode() == KeyCode.ESCAPE)  {
				getView().getPart1TitledPane().getParent().requestFocus();
			}
		});
	}

	private void addTitledPaneListeners() {
		getView().getPart1TitledPane().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addSearchableTextFieldOnTitledPane(getView().getPart1TitledPane(), DefectEntryByTextController.PART_1, false);
			}
		});

		getView().getPart2TitledPane().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addSearchableTextFieldOnTitledPane(getView().getPart2TitledPane(), DefectEntryByTextController.PART_2, false);
			}
		});

		getView().getDefectTitledPane().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addSearchableTextFieldOnTitledPane(getView().getDefectTitledPane(), DefectEntryByTextController.DEFECT, false);
			}
		});

		getView().getDefect1TitledPane().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				addSearchableTextFieldOnTitledPane(getView().getDefect1TitledPane(), DefectEntryByImageController.PRIMARY_DEFECT, true);
			}
		});
	}

	/**
	 * This method is used to refresh data based on TitledPane Filter for Text Entry Screen
	 * @param event
	 * @param component
	 */
	private void refreshOnTextEntryTitledPaneFilter(KeyEvent event, String component) {
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		String entryScreenName = entryScreen != null ? entryScreen.getEntryScreen() : StringUtils.EMPTY;
		String textEntryMenu = StringUtils.trimToEmpty(getView().getMenuTileListView().getSelectedItem());
		String part1 = StringUtils.trimToEmpty(getView().getPart1ListView().getSelectionModel().getSelectedItem());
		String loc = StringUtils.trimToEmpty(getView().getLocListView().getSelectionModel().getSelectedItem());
		String part2 = StringUtils.trimToEmpty(getView().getPart2ListView().getSelectionModel().getSelectedItem());
		String defect = StringUtils.trimToEmpty(getView().getDefectListView().getSelectionModel().getSelectedItem());
		String eventText = event.getText().toUpperCase();
		String defect1 = getView().parseDefect(defect, false);
		String defect2 = getView().parseDefect(defect, true);

		if(component.equalsIgnoreCase(DefectEntryByTextController.PART_1)) {		
			getView().getPart1TitledPane().setText(eventText);			

			if(StringUtils.isEmpty(textEntryMenu)) {
				return;
			}
			//Part1 is passed as empty due after event finish the selection in list is lost
			defectEntryByTextController.refreshTextFilterComponents(StringUtils.EMPTY, loc, part2, defect1, defect2);			

			if(event.getCode().equals(KeyCode.ESCAPE)) {
				getView().getPart1TitledPane().setText(DefectEntryByTextController.PART_1);
				getView().getPart1TitledPane().getParent().requestFocus();
			}
		} else if(component.equalsIgnoreCase(DefectEntryByTextController.LOC)) {
			getView().getLocTitledPane().setText(eventText);
			if(!StringUtils.isEmpty(textEntryMenu)) {
				getView().getLocListView().setItems(FXCollections.observableArrayList(getModel().findAllLocByEntryScreen(
						entryScreenName, textEntryMenu, part1, part2, defect1, defect2, eventText, getPart1TextFilter())));
			}
			if(event.getCode().equals(KeyCode.ESCAPE)) {
				if(!StringUtils.isEmpty(textEntryMenu)) {
					getView().getLocListView().setItems(FXCollections.observableArrayList(getModel().findAllLocByEntryScreen(
							entryScreenName, textEntryMenu, part1, part2, defect1, defect2, StringUtils.EMPTY, getPart1TextFilter())));
				}
				getView().getLocTitledPane().setText(DefectEntryByTextController.LOC);
				getView().getLocTitledPane().getParent().requestFocus();
			}
		} else if(component.equalsIgnoreCase(DefectEntryByTextController.PART_2)) {
			getView().getPart2TitledPane().setText(eventText);

			if(StringUtils.isEmpty(textEntryMenu)) {
				return;
			}
			//Part2 is passed as empty due after event finish the selection in list is lost
			defectEntryByTextController.refreshTextFilterComponents(part1, loc, StringUtils.EMPTY, defect1, defect2);			

			if(event.getCode().equals(KeyCode.ESCAPE)) {
				getView().getPart2TitledPane().setText(DefectEntryByTextController.PART_2);
				getView().getPart2TitledPane().getParent().requestFocus();
			}			
		} else if(component.equalsIgnoreCase(DefectEntryByTextController.DEFECT)) {
			getView().getDefectTitledPane().setText(eventText);
			if(!StringUtils.isEmpty(textEntryMenu)) {
				getView().getDefectListView().setItems(FXCollections.observableArrayList(getModel().findAllDefectByEntryScreen(
						entryScreenName, textEntryMenu, part1, loc, part2, eventText, getPart1TextFilter())));
			}
			if(event.getCode().equals(KeyCode.ESCAPE)) {
				if(!StringUtils.isEmpty(textEntryMenu)) {
					getView().getDefectListView().setItems(FXCollections.observableArrayList(getModel().findAllDefectByEntryScreen(
							entryScreenName, textEntryMenu, part1, loc, part2, StringUtils.EMPTY, getPart1TextFilter())));
				}
				getView().getDefectTitledPane().setText(DefectEntryByTextController.DEFECT);
				getView().getDefectTitledPane().getParent().requestFocus();
			}
		}
	}

	/**
	 * This method is used to refresh data based on TitledPane Filter for Image Entry Screen
	 * @param event
	 * @param component
	 */
	private void refreshOnImageEntryTitledPaneFilter(KeyEvent event, String component) {
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		String entryScreenName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getEntryScreen() : StringUtils.EMPTY);
		String imageName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getImageName() : StringUtils.EMPTY);
		int imageSectionId = 0;
		int partLocationId = 0;
		if(!defectEntryByImageController.getSelectionSet().isEmpty()) {
			imageSectionId = Integer.parseInt(defectEntryByImageController.getSelectionSet().iterator().next().getId());
			partLocationId = Integer.parseInt(defectEntryByImageController.getSelectionSet().iterator().next().getUserData().toString());  
		}
		String defect2 = getView().getSecondarySelectedDefect();
		String eventText = event.getText().toUpperCase();

		if(component.equalsIgnoreCase(DefectEntryByImageController.PRIMARY_DEFECT)) {
			getView().getDefect1TitledPane().setText(event.getText().toUpperCase());
			if(!StringUtils.isEmpty(entryScreenName)) {
				getView().getDefect1ListView().setItems(FXCollections.observableArrayList(getModel().findAllDefect1ByImageEntryScreen(
						entryScreenName, imageName, imageSectionId, defect2, partLocationId, eventText, assignRealProblem, getPart1TextFilter())));
			}
			if(event.getCode().equals(KeyCode.ESCAPE)) {
				if(!StringUtils.isEmpty(entryScreenName)) {
					getView().getDefect1ListView().setItems(FXCollections.observableArrayList(getModel().findAllDefect1ByImageEntryScreen(
							entryScreenName, imageName, imageSectionId, defect2, partLocationId, StringUtils.EMPTY, assignRealProblem, getPart1TextFilter())));
				}
				getView().getDefect1TitledPane().setText(DefectEntryByImageController.PRIMARY_DEFECT);
				getView().getDefect1TitledPane().getParent().requestFocus();
			}
		} 
	}
	/**
	 * This method is used to add WriteUp Dept Listener
	 */
	@SuppressWarnings("unchecked")
	private void addWriteUpDeptComboBoxListener(){
		getView().getWriteUpDeptComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@SuppressWarnings("rawtypes")
			@Override 
			public void changed(ObservableValue ov, String t, String t1) {
				clearMessage();
				checkMandatoryFields();
				String colorCode =getModel().findColorCodeByWriteupDeptAndProcessPointId(t1);
				getView().getColorCodeLabel().setText(StringUtils.trim(colorCode));
			}    
		});
	}
	
	private void enableRecentDefectButton() {
		getView().getRecentDefectBtn().setDisable(false);
		
	}
	
	private void disableRecentDefectButton() {
		getView().getRecentDefectBtn().setDisable(true);
	}

	public boolean isAutoDefectEntry()  {
		return (isAcceptButton() == false);
	}
	/**
	 * This method is used to disable Accept button if it is configured from Station Config Screen
	 */
	private void disableAcceptButton(){
		//renaming to show that DISABLE_ACCEPT amounts to showing the Accept popup
		if (isPopupAccept())  {
			getView().getAcceptBtn().setDisable(true);
		}
		else if (isAutoDefectEntry())  {
			getView().getAcceptBtn().setDisable(true);
		}
		else  {
			getView().getAcceptBtn().setDisable(false);
		}
	}

	/**
	 * confirmation dialog
	 * @param parent
	 * @return
	 */
	public void showConfirmDialog() {
		Point point = MouseInfo.getPointerInfo().getLocation();
		VBox vb = new VBox();

		final FxDialog dialog = new FxDialog(ACCEPT, getView().getStage(), vb);
		dialog.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		dialog.initStyle(StageStyle.TRANSPARENT);
		LoggedButton acceptButton = createPopUpButton(ACCEPT);
		acceptButton.defaultButtonProperty().bind(acceptButton.focusedProperty());
		acceptButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				acceptDefect();
				dialog.close();
			}
		});
		LoggedButton undoButton = createPopUpButton("Undo");
		undoButton.defaultButtonProperty().bind(undoButton.focusedProperty());
		undoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				resetData();
				dialog.close();
			}
		});
		BorderPane bp = new BorderPane();
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(acceptButton, undoButton);
		bp.setCenter(buttons);
		vb.setPadding(new Insets(10));
		buttons.setSpacing(20);
		vb.getChildren().addAll(bp);
		acceptDialog = dialog;
		dialog.showDialog(point.getX(),point.getY());
		Logger.getLogger().check("Confirm Dialogbox populated");
	}

	/**
	 * This method is used to create button for Accept/Undo Popup
	 * @param buttonText
	 * @return
	 */
	private LoggedButton createPopUpButton(String buttonText) {
		LoggedButton button = UiFactory.createButton(buttonText);
		button.getStyleClass().add("station-btn");
		button.setId(buttonText);
		button.setPrefWidth(getView().getScreenWidth() * 0.063);
		return button;
	}
	/**
	 * This method is used to set Entry Screen ListView Behavior
	 */
	private void setEntryScreenListViewBehavior() {
		getView().getEntryScreenListView().setBehaviour(new AbstractTileListViewBehaviour<QiDefectEntryDto>() {
			@Override
			public void setBehaviour(QiDefectEntryDto item) {
				double boxSize = getView().getScreenWidth() * 0.040;
				double size = (3 * boxSize) / numOfEntryScreenCols;
				if(item.isImage()) {
					getView().getEntryScreenListView().setNode(defectEntryByImageController.createImageEntryScreen(item, size));
				} else {
					getView().getEntryScreenListView().setNode(defectEntryByTextController.createTextEntryScreen(item, size));
				}
			}

			@Override
			public void addListener(QiDefectEntryDto item) {
				String processName = "loadDefectBy" + (item.isImage() ? "Image" : "Text");
				QiProgressBar qiProgress = QiProgressBar.getInstance(processName, processName, getProductModel().getProductId(), getView().getStage(), true);
   				try {
					qiProgress.showMe();
					if (item.isImage()) {
						loadDefectEntryByImage(item);
					} else {
						loadDefectByText(item);
					} 
				} finally {
					if(qiProgress != null)  {
						qiProgress.closeMe();
					}
				}
			}
		});
	}
	

	private void loadDefectByText(QiDefectEntryDto item) {

		if (getView().isScrapedproduct)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED + ": "+getView().scrapMessage, StatusMessageEventType.WARNING));
		}
		defectEntryByTextController.textEntryScreenListener();
	}

	private void loadDefectEntryByImage(QiDefectEntryDto item) {

		if (getView().isScrapedproduct)  {
			EventBusUtil.publish(new StatusMessageEvent(QiConstant.PRODUCT_ALREADY_SCRAPED + ": "+getView().scrapMessage, StatusMessageEventType.WARNING));
		}
		defectEntryByImageController.imageEntryScreenListener();
	}
	
	/**
	 * This method is used to add reason for change during responsibility change
	 * @param defectResult
	 * @return
	 */
	private String addReasonForChangingResponsibility(QiDefectResult defectResult) {

		if(!isResponsibleLevelChanged()) {
			Stage stage = (isAcceptButton() && isPopupAccept()) ? acceptDialog : getView().getStage();
			if(MessageDialog.confirmWithComment(stage, CHANGING_RESPONSIBILITY_MSG, "Reason For Change", true, "Please input comment"))
				return QiConstant.ASSIGN;
			else
				return QiConstant.DEASSIGN;

		}
		return QiConstant.OK;
	}

	/**
	 * This method is used to Check if the responsible level has changed based on plant, department, site, and level 
	 * and If isShowL2L3 levels are shown, validate against upper responsible levels
	 * @param String
	 * @return Boolean
	 */
	private Boolean isResponsibleLevelChanged() {
		String resPlant = StringUtils.trimToEmpty((String) getView().getPlantComboBox().getSelectionModel().getSelectedItem());
		String resDept = StringUtils.trimToEmpty((String) getView().getDepartmentComboBox().getSelectionModel().getSelectedItem());
		String resLevel = StringUtils.trimToEmpty((String) getView().getLevel1ComboBox().getSelectionModel().getSelectedItem().toString());
		String resSite = StringUtils.trimToEmpty((String) getView().getSiteComboBox().getSelectionModel().getSelectedItem().toString());
		
		QiResponsibleLevel responsibleLevel = getModel().findByResponsibleLevelId(selectedDefect.getResponsibleLevelId());
	    Boolean isLevelValid = responsibleLevel.getPlant().equalsIgnoreCase(resPlant)
	        && responsibleLevel.getDepartment().equalsIgnoreCase(resDept)
	        && responsibleLevel.getSite().equalsIgnoreCase(resSite)
	        && responsibleLevel.getResponsibleLevelName().equalsIgnoreCase(resLevel);
	    if(!isShowL2L3) {
	    	return isLevelValid;
	    }else{
			QiResponsibleLevel l2 = getModel().findResponsibleLevelById(responsibleLevel.getUpperResponsibleLevelId());
			QiResponsibleLevel l3 = getModel().findResponsibleLevelById(l2.getUpperResponsibleLevelId());
			String responsibleLevel2 = "";

			if (respController.getResponsiblePanel().getResponsibleLevel2ComboBox().getControl().getSelectionModel().getSelectedItem() != null) {
			    responsibleLevel2 = StringUtils.trimToEmpty(respController.getResponsiblePanel().getResponsibleLevel2ComboBox().getControl().getSelectionModel().getSelectedItem().toString());
			}

			String responsibleLevel3 = "";
			if (respController.getResponsiblePanel().getResponsibleLevel3ComboBox().getControl().getSelectionModel().getSelectedItem() != null) {
			    responsibleLevel3 = StringUtils.trimToEmpty(respController.getResponsiblePanel().getResponsibleLevel3ComboBox().getControl().getSelectionModel().getSelectedItem().toString());
			}
			
			if(l2!=null) {
				isLevelValid = isLevelValid && responsibleLevel2.equalsIgnoreCase(l2.getResponsibleLevelName());
			}
			if(l3!=null) {
				isLevelValid = isLevelValid && responsibleLevel3.equalsIgnoreCase(l3.getResponsibleLevelName());
			}
			return isLevelValid;
	    }
	}
	
	private String getPart1Name(){
		SubProduct subProd = getModel().findSubIdByProductId();
		if(getModel().getProductType().equalsIgnoreCase(ProductType.BUMPER.getProductName())){
			if(StringUtils.trimToEmpty(subProd.getSubId()).equals(SubId.FRONT.getId())){
				return  getModel().getProperty().getFrontBumperPartName();
			}else if (StringUtils.trimToEmpty(subProd.getSubId()).equals(SubId.REAR.getId())) {
				return  getModel().getProperty().getRearBumperPartName();
			}			
		}else if(getModel().getProductType().equalsIgnoreCase(ProductType.KNUCKLE.getProductName())){
			if(StringUtils.trimToEmpty(subProd.getSubId()).equals(SubId.LEFT.getId())){
				return getModel().getProperty().getLeftKnucklePartName();
			}else if(StringUtils.trimToEmpty(subProd.getSubId()).equals(SubId.RIGHT.getId())) {
				return getModel().getProperty().getRightKnucklePartName();
			}	
		}
		return StringUtils.EMPTY;
	}

	public DefectEntryByTextController getDefectEntryByTextController() {
		return defectEntryByTextController;
	}

	public DefectEntryByImageController getDefectEntryByImageController() {
		return defectEntryByImageController;
	}

	public boolean getSelectValue() {
		return selectValue;
	}

	public void setEventHandler(boolean eventHandler) {
		this.eventHandler = eventHandler;
	}

	public boolean getEventHandler() {
		return eventHandler;
	}

	public QiDefectResultDto getSelectedDefect() {
		return selectedDefect;
	}

	public void setSelectedDefect(QiDefectResultDto selectedDefect) {
		this.selectedDefect = selectedDefect;
	}

	public boolean isAssignRealProblem() {
		return assignRealProblem;
	}

	public boolean isDefectEntered() {
		return isDefectEntered;
	}

	public void setDefectEntered(boolean isDefectEntered) {
		this.isDefectEntered = isDefectEntered;
	}

	public double getPointX() {
		return pointX;
	}

	public void setPointX(double pointX) {
		this.pointX = pointX;
	}

	public double getPointY() {
		return pointY;
	}

	public void setPointY(double pointY) {
		this.pointY = pointY;
	}


	public void checkMandatoryFields() {
		String writeUpDept = StringUtils.trimToEmpty((String)getView().getWriteUpDeptComboBox().getSelectionModel().getSelectedItem());
		if(getView().getToggleGroup().getSelectedToggle() == null && StringUtils.isBlank(writeUpDept))
			displayErrorMessage(SELECT_DEFECT_STATUS_WRITE_UP_DEPT_ERROR_MSG);
		else if(getView().getToggleGroup().getSelectedToggle() == null)
			displayErrorMessage(SELECT_DEFECT_STATUS_ERROR_MSG);
		else if(StringUtils.isBlank(writeUpDept)) 
			displayErrorMessage(SELECT_WRITE_UP_DEPT_ERROR_MSG);		
	}	

	/*
	 * similar to legacy QICS process 
	 * save direct pass record into GAL125TBX with defect_status=2 and outstanding_flag=0
	 * generate new DEFECTRESULTID = max(DEFECTRESULTID) + 1, do not use GAL266TBX to generate new ID
	 * update station result (GAL260TBX)
	 * use existing recovery feature to back fill GAL215TBX
	 * do not update GAL143TBX, GAL176TBX
	 */
	public void replicateDirectPassResult() {
		DefectResult defectResult = new DefectResult();
		DefectResultId defectResultId = new DefectResultId();
		defectResultId.setProductId(getModel().getProductId());

		String applicationId = getModel().getCurrentWorkingProcessPointId();
		String oldApplicationId = getDao(QiExternalSystemDefectMapDao.class).findOldAppIdByAppId(applicationId);
		if (StringUtils.isEmpty(oldApplicationId)) {
			oldApplicationId = applicationId;
		} 
		defectResultId.setApplicationId(oldApplicationId);

		defectResultId.setInspectionPartName("");
		defectResultId.setInspectionPartLocationName("");
		defectResultId.setDefectTypeName("");
		defectResultId.setSecondaryPartName("");
		defectResultId.setTwoPartPairPart("");
		defectResultId.setTwoPartPairLocation("");

		defectResult.setId(defectResultId);
		defectResult.setNewDefect(true);
		defectResult.setAssociateNo(getModel().getUserId());
		defectResult.setOutstandingFlag(false);
		defectResult.setDefectStatus(DefectStatus.DIRECT_PASS);
		defectResult.setEntryDept(getCurrentWorkingEntryDept());
		defectResult.setNaqDefectResultId(0); //default NAQ_DEFECTRESULTID to 0 for direct pass 

		List<DefectResult> defectResults = new ArrayList<DefectResult>();
		defectResults.add(defectResult);
		saveDefects(null, defectResults, null, oldApplicationId.equals(applicationId)? null : oldApplicationId, true, false);
	}

	public void replicateDefectResult(List<QiDefectResult> savedQiDefectResultList) {

		String oldApplicationId = null;

		List<DefectResult> defectResults = new ArrayList<DefectResult>();

		for(QiDefectResult qiDefectResult : savedQiDefectResultList) {
			DefectResult defectResult = ServiceFactory.getService(HeadlessNaqService.class).setDefectData(qiDefectResult);
			defectResult.setActualTimestamp(new Timestamp(qiDefectResult.getActualTimestamp().getTime()));
			if(qiDefectResult.getOriginalDefectStatus()==DefectStatus.REPAIRED.getId())
				defectResult.setRepairTimestamp(new Timestamp(qiDefectResult.getActualTimestamp().getTime()));
			if (defectResult != null) {
				defectResults.add(defectResult);
				if (!defectResult.getId().getApplicationId().equals(qiDefectResult.getApplicationId())) {
					oldApplicationId = defectResult.getId().getApplicationId();
				}
			}
		}

		if (defectResults.size() > 0) {
			saveDefects(null, defectResults, null, oldApplicationId, true, 
					PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId()).isCreateRepairedDefectTo222());
		}
	}

	private void saveDefects(DefectStatus defectStatus, List<DefectResult> defectResults, ExceptionalOut exceptionalOut, 
			String oldApplicationId, boolean isReplicateDefectRepairResult, boolean isCreateRepairedDefectTo222) {

		DailyDepartmentSchedule schedule = getModel().getSchedule();
		getDao(DefectResultDao.class).saveAllDefectResults(
				getProductModel().getProduct(),
				defectStatus,
				oldApplicationId,
				defectResults,
				schedule,
				exceptionalOut,
				isReplicateDefectRepairResult,
				isCreateRepairedDefectTo222);
	}

	/**
	 * This method is used to disable Existing Defect button if there is no previous defect or defect in cache
	 */
	public void enableDisableExistingDefectButton(){
		getView().getExistingDefectBtn().setDisable(!getModel().hasExistingDefect());
	}

	/**
	 * This method is used to clear search text
	 */
	private void clearPartSearchTxt(){
		getView().getPartSearchTextField().clear();
		this.selectedPartName = null;
		loadInitialData(StringUtils.EMPTY);

	}

	/**
	 * This method is used to get the selected part name in Part Search autocomplete field
	 * @return
	 */
	protected String getSearchedPartName(){
		return this.selectedPartName; 
	}	

	/**
	 * This method is used to check if a search was triggered
	 * @return
	 */
	protected boolean isInSearchMode(){
		return StringUtils.isNotBlank(this.selectedPartName); 
	}

	/**
	 * This method is used to check if specific part name match with the part search on entry screen list 
	 * @param partName
	 * @return
	 */
	protected boolean matchWithCurrentPartSearch(String partName){
		return StringUtils.isNotBlank(this.selectedPartName) && this.selectedPartName.trim().equalsIgnoreCase(partName.trim());
	}

	public boolean isDeleteProductSequence()  {
		boolean val = PropertyService.getPropertyBoolean(getProcessPointId(), "IS_DELETE_PRODUCT_SEQUENCE", false);
		return val;
	}

	public boolean isProductSequenceStation()  {
		boolean isProductSequence = PropertyService.getPropertyBoolean(getProcessPointId(), "IS_PRODUCT_SEQUENCE_STATION", false);
		return isProductSequence;
	}

	public boolean isAcceptButton() {
		return acceptButton;
	}

	public void setAcceptButton(boolean isAcceptButton) {
		this.acceptButton = isAcceptButton;
	}

	/**
	 * @return the popupAccept
	 */
	public boolean isPopupAccept() {
		return popupAccept;
	}

	/**
	 * @param popupAccept the popupAccept to set
	 */
	public void setPopupAccept(boolean popupAccept) {
		this.popupAccept = popupAccept;
	}

	private void deleteProductSequence()  {
		String productId = getProductModel().getProductId();
		String rfidStation = PropertyService.getProperty(getCurrentWorkingProcessPointId(), QiConstant.RFID_STATION_PROP_KEY);
		if(StringUtils.isBlank(productId) || StringUtils.isBlank(rfidStation))  {
			return;
		}
		ProductSequenceId id = new ProductSequenceId();
		id.setProcessPointId(rfidStation);
		id.setProductId(productId);

		ProductSequence prodSeq = getDao(ProductSequenceDao.class).findByKey(id);

		if (prodSeq != null && prodSeq.getReferenceTimestamp() != null && !StringUtils.isBlank(rfidStation)) {
			if (PropertyService.getPropertyBean(QiPropertyBean.class, getCurrentWorkingProcessPointId())
					.isDeleteOnlyProcessedProduct()) {
				getDao(ProductSequenceDao.class).remove(prodSeq);
			} else {
				List<ProductSequence> prodSeqList = getDao(ProductSequenceDao.class).findAllForStationIdBeforeTimestamp(
						prodSeq.getReferenceTimestamp(), prodSeq.getId().getProcessPointId());
				for (ProductSequence listItem : prodSeqList) {
					getDao(ProductSequenceDao.class).remove(listItem);
				}
			}
		}
	}

	public static boolean isShowResponsibilityL2L3ForProcessPoint(String processPointId)  {
		if(StringUtils.isBlank(processPointId))  return false;
		QiStationConfiguration qiEntryStnSetting = ServiceFactory.getDao(QiStationConfigurationDao.class)
				.findValueByProcessPointAndPropKey(processPointId,
						QiEntryStationConfigurationSettings.SHOW_L2_L3.getSettingsName());
		boolean isShowL2L3 = false;
		String cfgValue = QiEntryStationConfigurationSettings.SHOW_L2_L3.getDefaultPropertyValue();
		if(!StringUtils.isBlank(cfgValue) && cfgValue.trim().equalsIgnoreCase("Yes"))  {
			isShowL2L3 = true;
		}
		if(null != qiEntryStnSetting && !StringUtils.isBlank(qiEntryStnSetting.getPropertyValue())) {
			cfgValue = qiEntryStnSetting.getPropertyValue();
		}

		if(cfgValue.trim().equalsIgnoreCase("Yes"))  {
			isShowL2L3 = true;
		}
		return isShowL2L3;
	}

	/**
	 * @return the respController
	 */
	public ResponsibleLevelController getRespController() {
		return respController;
	}

	/**
	 * @param respController the respController to set
	 */
	public void setRespController(ResponsibleLevelController respController) {
		this.respController = respController;
	}

	/**
	 * @return the isShowL2L3
	 */
	public boolean isShowL2L3() {
		return isShowL2L3;
	}

	/**
	 * @param isShowL2L3 the isShowL2L3 to set
	 */
	public void setShowL2L3(boolean isShowL2L3) {
		this.isShowL2L3 = isShowL2L3;
	}

	/**
	 * @param isResponsibilityAccessible the isResponsibilityAccessible to set
	 */
	public void setResponsibilityAccessible(boolean isResponsibilityAccessible) {
		this.isResponsibilityAccessible = isResponsibilityAccessible;
	}

	public boolean isIqsInput() {
		return isIqsInput;
	}

	public void setIqsInput(boolean isIqsInput) {
		this.isIqsInput = isIqsInput;
	}

	public boolean isIqsAuditActionInput() {
		return isIqsAuditActionInput;
	}

	public void setIqsAuditActionInput(boolean isIqsAuditActionInput) {
		this.isIqsAuditActionInput = isIqsAuditActionInput;
	}

	private List<QiResponsibilityMapping> getResponsibleMapping() {
		List<QiResponsibilityMapping> qiResponsibleMapping = getModel().findAll();
		return qiResponsibleMapping;
	}

	private QiResponsibleLevel getResponsibleLevel() {
		QiResponsibleLevel qiResponsibleLevel = getModel().findResponsibleLevelByNameAndDeptId(
				StringUtils.trimToEmpty(getView().getLevel1ComboBox().getSelectionModel().getSelectedItem().toString()),
				new QiDepartmentId(
						StringUtils.trimToEmpty(
								(String) getView().getDepartmentComboBox().getSelectionModel().getSelectedItem()),
						StringUtils.trimToEmpty(
								(String) getView().getSiteComboBox().getSelectionModel().getSelectedItem()),
						StringUtils.trimToEmpty(
								(String) getView().getPlantComboBox().getSelectionModel().getSelectedItem())));
		return qiResponsibleLevel;
	}


	public boolean isShowKickoutPane() {
		return isShowKickoutPane;
	}

	public void setShowKickoutPane(boolean isKickoutStation) {
		this.isShowKickoutPane = isKickoutStation;
	}

	public boolean isListInitialized() {
		return isListInitialized;
	}

	public void setListInitialized(boolean isListInitialized) {
		this.isListInitialized = isListInitialized;
	}
	
	public boolean isPlayNgSoundForBadDFScan(){
		return isPlayNgSoundForBadDFScan;
	}

	public boolean isFrame()  {
		if(getModel().getProductType().equalsIgnoreCase(ProductType.FRAME.getProductName()))  {
			return true;
		}
		else  {
			return false;
		}
	}
	
	public boolean isEngine()  {
		if(getModel().getProductType().equalsIgnoreCase(ProductType.ENGINE.getProductName())) {
			return true;
		}
		else  {
			return false;
		}
	}

}