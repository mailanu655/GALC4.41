package com.honda.galc.client.teamleader.qi.reportingtarget;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.teamleader.qi.controller.AbstractQiController;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.enumtype.MetricDataFormat;
import com.honda.galc.entity.product.ModelGrouping;
import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingMetricId;
import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Toggle;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>ReportingTargetMaintenanceController</code>
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
 * <TD>15/11/2016</TD>
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
public class ReportingTargetMaintenanceController extends AbstractQiController<ReportingTargetMaintenanceModel, ReportingTargetMaintenancePanel> implements EventHandler<ActionEvent> {

	private static final String METRIC_VALUE = "Metric Value";
	private static final String START_DATE_VALIDATION_MSG = "Start Date cannot be greater than End Date.";
	private static final String END_DATE_VALIDATION_MSG = "End Date cannot be less than Start Date.";
	private static final String METRIC_DELETION_ERROR = "Metric has associated targets hence cannot be deleted.";
	private static final String METRIC_DELETE_CONFIRMATION = "You are about to delete the selected Metric. Do you wish to continue?";
	private static final String TARGET_OVERRIDING_CONFIRMATION = "This action will update any existing Target. Do you wish to continue?";
	private static final String TARGET_DELETE_CONFIRMATION = "You are about to delete existing Target. Do you wish to continue?";
	private static final String PLEASE_ENTER_VALID_END_DATE = "Please enter valid End Date";
	private static final String PLEASE_ENTER_VALID_START_DATE = "Please enter valid Start Date";
	private static final String PLEASE_CREATE_METRIC_FIRST = "Please create Metric first";
	private static final String PLEASE_SELECT_RESPONSIBLE_MODEL_YEAR = "Please select Responsible Model Year";
	private static final String PLEASE_SELECT_RESPONSIBLE_MODEL = "Please select Responsible Model";
	private static final String PLEASE_SELECT_RESPONSIBLE_PRODUCT_TYPE = "Please select Responsible Product Type";
	private static final String PLEASE_SELECT_RESPONSIBLE_PLANT = "Please select Responsible Plant";
	private static final String TOTAL_METRIC_VALUE_ERROR_MSG = "Total Metric value must be 100";
	private static final String SETUP_LEVEL1_TARGET_ERROR_MSG = "Please set up Target for Level 1 Metric. Level 2 and 3 will be auto generated.";
	private static final String SETUP_LEVEL2_TARGET_ERROR_MSG = "Please set up Target for Level 2 Metric. Level 3 will be auto generated.";
	private static final String PERCENT_METRIC_VALUE_ERROR_MSG = "% metric value can't be more than 100";

	private static final String ALL = "All";
	private List<String> themeNameList;
	private List<String> temporaryTrackingList;
	private boolean isRefreshButton=false;
	private static final String DEPT_COMBOBOX_ERROR_MSG = "Please select Department";
	private static final String METRIC_VALUE_ERROR_MSG = "Please enter metric value";

	String higherLevel = null;
	boolean useCalculatedValue = false;
	String previousMetricName = null;

	private String site = null;
	private String plant = null;
	private String productType = null;
	private String modelGroup = null;
	private String modelYear = null;
	private String demandType = null;
	private String metricName = null;
	private String department = null;
	private String target = null;
	private java.sql.Date startDate = null;
	private java.sql.Date endDate = null;
	BigDecimal totalValue =BigDecimal.ZERO;
	private static ZoneId serverZoneId = null; 
	private static boolean autoUpdateChildTarget = false;

	public ReportingTargetMaintenanceController(ReportingTargetMaintenanceModel model, ReportingTargetMaintenancePanel view) {
		super(model, view);
		themeNameList = getModel().findAllActiveThemeNames();
		temporaryTrackingList = getModel().findAllActiveTemporaryTrackingNames();
	}

	@Override
	public void handle(ActionEvent actionEvent) {

		if (actionEvent.getSource().equals(getView().getPlantRadioButton())) {
			handlePlantRadioButtonSelction();
		} else if (actionEvent.getSource().equals(getView().getDepartmentRadioButton())) {
			handleDepartmentRadioButtonSelction();
		} else if (actionEvent.getSource().equals(getView().getThemeRadioButton())) {
			handleThemeRadioButtonSelction();
		} else if (actionEvent.getSource().equals(getView().getTempTrackingRadioButton())) {
			handleTempTrackingRadioButtonSelction();
		} else if (actionEvent.getSource().equals(getView().getDeptRespLevelRadioButton())) {
			handleDeptRespRadioButtonSelection();
		} else if (actionEvent.getSource().equals(getView().getResponsibleLevel1RadioButton())) {
			handleRespLevel1RadioButtonSelection();
		} else if (actionEvent.getSource().equals(getView().getResponsibleLevel2RadioButton())) {
			handleRespLevel2RadioButtonSelection();
		} else if (actionEvent.getSource().equals(getView().getResponsibleLevel3RadioButton())) {
			handleRespLevel3RadioButtonSelection();
		} else if (actionEvent.getSource().equals(getView().getCreateMenuItem())) {
			createMetricDetails(actionEvent);
		} else if (actionEvent.getSource().equals(getView().getUpdateMenuItem())) {
			updateMetricDetails(actionEvent);
		} else if (actionEvent.getSource().equals(getView().getDeleteMenuItem())) {
			deleteMetricDetails(actionEvent);
		} else if (actionEvent.getSource().equals(getView().getAddButton())) {
			addTargetDetails(actionEvent);
		} else if (actionEvent.getSource().equals(getView().getQueryButton())) {
			if (getSelectedTarget() != null) {
				reloadTableDataBySelectedFilters(true);
			} else {
				displayErrorMessage("Please select a target type to query.");
			}
		} else if (actionEvent.getSource().equals(getView().getDeleteButton())) {
			deleteTargetDetails();
		}
	}

	private void handleDeptRespRadioButtonSelection() {
		clearDepartmentComboBoxSelection();
		getView().handleDeptRespRadioButtonSelection(true);
		getView().getMetricComboBox().getControl().getItems().clear();
		getView().getReportingTargetTable().getTable().getItems().clear();
		getView().getAddButton().setText("Add");
	}

	/**
	 * This method will be used to create new Metric data.
	 * 
	 * @param actionEvent
	 */
	private void createMetricDetails(ActionEvent actionEvent) {
		try {
			clearDisplayMessage();
			if(getSelectedTarget()!=null){
				ReportingMetricDialog dialog = new ReportingMetricDialog(getModel(), getApplicationId(), getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem(),getSelectedTarget(), false);
				dialog.setScreenName(getView().getScreenName());
				dialog.showDialog();
				QiReportingMetric savedData=dialog.getQiReportingMetric();
				if(savedData!=null){
					getView().getMetricComboBox().getControl().getItems().add(savedData);
					getView().getMetricComboBox().getControl().getSelectionModel().select(savedData);
				}
				sortMetricComboboxData();
			}
			else
			{
				displayErrorMessage("Please select level to create metric");
				return;
			}
		} catch (Exception e) {
			handleException("An error occured in Metric create method ", "Failed To Create Metric", e);
		}
	}

	/**
	 * This method will be used to update selected metric object.
	 * 
	 * @param actionEvent
	 */
	private void updateMetricDetails(ActionEvent actionEvent) {

		try {
			if(getSelectedTarget()!=null){
				clearDisplayMessage();
				ReportingMetricDialog dialog = new ReportingMetricDialog(getModel(), getApplicationId(), getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem(),getSelectedTarget(), true);
				dialog.setScreenName(getView().getScreenName());
				dialog.showDialog();
				QiReportingMetric savedData=dialog.getQiReportingMetric();
				if(savedData!=null){
					getView().getMetricComboBox().getControl().getItems().remove(getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem());
					getView().getMetricComboBox().getControl().getItems().add(savedData);
					getView().getMetricComboBox().getControl().getSelectionModel().select(savedData);
				}

				sortMetricComboboxData();
				int selectedIndex = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedIndex();
				loadMetricComboBox();
				getView().getMetricComboBox().getControl().getSelectionModel().select(selectedIndex);
			}
		} catch (Exception e) {
			handleException("An error occured in Metric update method ", "Failed To update Metric", e);
		}
	}

	/**
	 * This method will be used to delete the metric data.
	 * 
	 * @param actionEvent
	 */
	private void deleteMetricDetails(ActionEvent actionEvent) {
		QiReportingMetric selectedItem = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem();
		deleteMetricDetails(selectedItem, false);
	}

	private boolean deleteMetricDetails(QiReportingMetric metric, boolean confirmed) {
		if (metric == null)
			return true;
		// confirm that the user wants to delete the metric
		if (confirmed || MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), METRIC_DELETE_CONFIRMATION)) {
			// cancel delete if the metric has corresponding targets
			List<QiReportingTarget> existingTargetData = getModel().findAllTargetsByMetricNameAndLevel(metric.getId().getMetricName(), metric.getId().getLevel());
			if (existingTargetData != null && !existingTargetData.isEmpty()) {
				MessageDialog.showError(ClientMainFx.getInstance().getStage(getApplicationId()), METRIC_DELETION_ERROR);
				return false;
			}
			// cascade delete for responsible levels
			if (metric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_1)) {
				if (!deleteMetricDetails(getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_2)), true))
					return false;
			} else if (metric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_2)) {
				if (!deleteMetricDetails(getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_3)), true))
					return false;
			}
			// delete the metric
			getModel().deleteMetricsData(metric);
			//call to log audit
			AuditLoggerUtil.logAuditInfo(metric, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getView().getScreenName(), getUserId());

			getView().getMetricComboBox().getControl().getSelectionModel().clearSelection();
			getView().getMetricComboBox().getControl().getItems().remove(metric);
			return true;
		}
		return false;
	}

	/**
	 * This method will be used to handle 'Plant' radio button click event and
	 * will add dynamic component in the middle panel.
	 * 
	 */
	private void handlePlantRadioButtonSelction() {
		clearDisplayMessage();
		getView().getTotalMetricValue().setVisible(false);

		LabeledTextField resultTextField = new LabeledTextField(METRIC_VALUE, true, TextFieldState.EDIT, new Insets(0, 10, 10, 10), 100, Pos.BASELINE_LEFT, false);
		resultTextField.getControl().setMaxHeight(25);

		String defaultMetricDataFormat="%";
		if (getView().getMetricComboBox().getControl().getSelectionModel() != null
				&& getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem() != null) {
			defaultMetricDataFormat = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem().getMetricDataFormat();
		}
		resultTextField.getControl().setPromptText(defaultMetricDataFormat);

		addMetricValueTextFieldChangeListener(resultTextField);

		getView().getDynamicContainer().getChildren().clear();
		getView().getDynamicContainer().getChildren().add(resultTextField);
		getView().getRespLevelRadioToggleGroup().selectToggle(null);
		plantDeptRadioButtonSelected();
	}

	/**
	 * This method will be used to handle 'Department' radio button click event
	 * and will add dynamic component in the middle panel.
	 * 
	 */
	private void handleDepartmentRadioButtonSelction( ) {
		addDynamicComponentBasedOnRadioBtnSelection(
				getModel().findAllDepartmentBySiteAndPlant(getView().getSiteTextField().getText(),
						getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem()));
		getView().getRespLevelRadioToggleGroup().selectToggle(null);
		plantDeptRadioButtonSelected();
	}

	/**
	 * This method will be used to handle 'Theme' radio button click event and
	 * will add dynamic component in the middle panel.
	 * 
	 * @param actionEvent
	 */
	private void handleThemeRadioButtonSelction( ) {
		getView().handleDeptRespRadioButtonSelection(false);
		addDynamicComponentBasedOnRadioBtnSelection(themeNameList);
		clearDepartmentComboBoxSelection();
		getView().getDepartmentComboBox().setVisible(true);
		clearFieldsOnRadioButtonSelection();
	}

	/**
	 * This method will be used to handle 'Temporary Tracking' radio button
	 * click event and will add dynamic component in the middle panel.
	 * 
	 * @param actionEvent
	 */
	private void handleTempTrackingRadioButtonSelction( ) {
		getView().handleDeptRespRadioButtonSelection(false);
		addDynamicComponentBasedOnRadioBtnSelection(temporaryTrackingList);
		clearDepartmentComboBoxSelection();
		getView().getDepartmentComboBox().setVisible(true);
		clearFieldsOnRadioButtonSelection();
	}

	/**
	 * This method will be used to handle 'Responsible Level1' radio button
	 * click event and will add dynamic component in the middle panel.
	 * 
	 * @param actionEvent
	 */
	private void handleRespLevel1RadioButtonSelection( ) {
		String[] selectedSiteFilterAndDeptArray = getSelectedSitePlantAndDepartment();

		List<String> responsibleLevel1List = getModel().findAllValidRespLevelBySitePlantDeptAndLevel(
				selectedSiteFilterAndDeptArray[0], selectedSiteFilterAndDeptArray[1], selectedSiteFilterAndDeptArray[2], (short) 1);
		addDynamicComponentBasedOnRadioBtnSelection(responsibleLevel1List);
		clearFieldsOnRadioButtonSelection();
	}

	/**
	 * This method will be used to handle 'Responsible Level2' radio button
	 * click event and will add dynamic component in the middle panel.
	 * 
	 * @param actionEvent
	 */
	private void handleRespLevel2RadioButtonSelection( ) {
		String[] selectedSiteFilterAndDeptArray = getSelectedSitePlantAndDepartment();

		List<String> responsibleLevel2List = getModel().findAllValidRespLevelBySitePlantDeptAndLevel(
				selectedSiteFilterAndDeptArray[0], selectedSiteFilterAndDeptArray[1], selectedSiteFilterAndDeptArray[2], (short) 2);
		addDynamicComponentBasedOnRadioBtnSelection(responsibleLevel2List);
		clearFieldsOnRadioButtonSelection();
	}

	/**
	 * This method will be used to handle 'Responsible Level3' radio button
	 * click event and will add dynamic component in the middle panel.
	 * 
	 * @param actionEvent
	 */
	private void handleRespLevel3RadioButtonSelection( ) {
		String[] selectedSiteFilterAndDeptArray = getSelectedSitePlantAndDepartment();

		List<String> responsibleLevel3List = getModel().findAllValidRespLevelBySitePlantDeptAndLevel(
				selectedSiteFilterAndDeptArray[0], selectedSiteFilterAndDeptArray[1], selectedSiteFilterAndDeptArray[2], (short) 3);
		addDynamicComponentBasedOnRadioBtnSelection(responsibleLevel3List);
		clearFieldsOnRadioButtonSelection();
	}

	@Override
	public void addContextMenuItems() {

	}

	/**
	 * This method will return array of selected 'Site', 'Plant' and department.
	 * 
	 * @return
	 */
	private String[] getSelectedSitePlantAndDepartment() {
		String[] selectedFilterArray = new String[3];
		selectedFilterArray[0] = getView().getSiteTextField().getText();
		selectedFilterArray[1] = getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem();
		selectedFilterArray[2] = getView().getDepartmentComboBox().getControl().getSelectionModel() != null
				? getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem() : "";

				return selectedFilterArray;
	}

	@Override
	public void initEventHandlers() {
		addPlantComboBoxListener();
		addProductTypeComboBoxListener();
		addModelComboBoxListener();
		addModelYearComboBoxListener();
		addDemandTypeComboBoxListener();
		addDepartmentComboBoxListener();
		addMetricComboBoxListener();
		addStartDateListener();
		addEndDateListener();
	}


	private void getSelectedData() {
		//get server ZoneId to offset the time between server and client
		if (serverZoneId == null) {
			serverZoneId = getModel().getServerZoneId(); //America/Los_Angeles, America/New_York
		}

		site = getView().getSiteTextField().getText();

		plant = getView().getPlantComboBox().getControl().getSelectionModel() != null
				? getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem() : null;

				productType = getView().getProductTypeComboBox().getControl().getSelectionModel() != null
						? getView().getProductTypeComboBox().getControl().getSelectionModel().getSelectedItem() : null;

						modelGroup = getView().getModelComboBox().getControl().getSelectionModel().getSelectedItem() != null
								? getView().getModelComboBox().getControl().getSelectionModel().getSelectedItem() : null;

								modelYear = getView().getModelYearComboBox().getControl().getSelectionModel().getSelectedItem() != null
										? getView().getModelYearComboBox().getControl().getSelectionModel().getSelectedItem() : null;

										demandType = getView().getDemandTypeComboBox().getControl().getSelectionModel() != null
												? getView().getDemandTypeComboBox().getControl().getSelectionModel().getSelectedItem() : null;

												metricName = (getView().getMetricComboBox().getControl().getSelectionModel() != null
														&& getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem() != null)
														? getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem()
																.getId().getMetricName()
																: null;

																department = (getView().getDepartmentComboBox().getControl().getSelectionModel() != null
																		&& getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem() != null)
																		? getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem()
																				: null;

																		target = getSelectedTarget();

																		if (getView().getStartDatePicker().getValue() != null && getView().getEndDatePicker().getValue() != null){
																			startDate = new java.sql.Date(java.util.Date.from(getView().getStartDatePicker().getValue().atStartOfDay(serverZoneId).toInstant()).getTime());
																			endDate = new java.sql.Date(java.util.Date.from(getView().getEndDatePicker().getValue().atStartOfDay(serverZoneId).toInstant()).getTime());
																		}
	}

	/**
	 * This method will be used to load target data based on given selected
	 * filters.
	 * 
	 * @return
	 */
	private List<QiReportingTarget> reloadTableDataBySelectedFilters(boolean refreshView) {

		getSelectedData();

		if (StringUtils.isBlank(site) || StringUtils.isBlank(plant) || StringUtils.isBlank(productType)
				|| StringUtils.isBlank(demandType) || StringUtils.isBlank(target)) {
			getView().getReportingTargetTable().getTable().getItems().clear();
			return null;
		}
		// loading table data
		List<QiReportingTarget> tableData = null;
		if (target.equals(QiConstant.TARGET_DEPARTMENT) || target.equals(QiConstant.TARGET_PLANT)) {
			tableData = getModel().findAllTargetByFilter(site, plant, productType, modelGroup, modelYear,
					demandType, target, metricName, startDate, endDate, null);
		} else {
			tableData = getModel().findAllTargetByFilter(site, plant, productType, modelGroup, modelYear,
					demandType, target, metricName, startDate, endDate, department);
		}
		if (refreshView) {
			getView().getReportingTargetTable().setData(tableData);
		}

		if(tableData!=null && tableData.size()>0){
			if(isFullAccess()){
				getView().getDeleteButton().setDisable(false);
				getView().getAddButton().setText("Update");
			}
		}
		else
		{
			getView().getDeleteButton().setDisable(true);
			getView().getAddButton().setText("Add");
		}

		return tableData;
	}

	private String getSelectedTarget() {
		String target=null;
		if (getView().getRadioToggleGroup().getSelectedToggle() != null) {
			target = ((LoggedRadioButton) getView().getRadioToggleGroup().getSelectedToggle()).getText();
		}

		if (target == null || target.equalsIgnoreCase(QiConstant.TARGET_DEPT_RESP_LEVEL)) {
			if (getView().getRespLevelRadioToggleGroup().getSelectedToggle() != null) {
				target = ((LoggedRadioButton) getView().getRespLevelRadioToggleGroup().getSelectedToggle()).getText();
			} else {
				target = null;
			}
		}
		return target;
	}

	private String getSelectedLevel() {
		String target=null;
		if (getView().getRadioToggleGroup().getSelectedToggle() != null) {
			target = ((LoggedRadioButton) getView().getRadioToggleGroup().getSelectedToggle()).getText();
		}

		if (target == null || target.equalsIgnoreCase(QiConstant.TARGET_DEPT_RESP_LEVEL)) {
			if (getView().getRespLevelRadioToggleGroup().getSelectedToggle() != null) {
				target = ((LoggedRadioButton) getView().getRespLevelRadioToggleGroup().getSelectedToggle()).getText();
				if(target.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL_1)){
					target = QiConstant.TARGET_RESPONSIBLE_LEVEL_1;
				} else if(target.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL_2)){
					target = QiConstant.TARGET_RESPONSIBLE_LEVEL_2;
				} else if(target.equalsIgnoreCase(QiConstant.RESPONSIBLE_LEVEL_3)){
					target = QiConstant.TARGET_RESPONSIBLE_LEVEL_3;
				}
			} else {
				target = null;
			}
		}
		return target;
	}

	/**
	 * This method Loads plant combo box.
	 * 
	 */
	private void loadPlantComboBox() {
		String site = getView().getSiteTextField().getText();
		List<String> plantList = getModel().findAllPlantBySite(site);

		getView().getPlantComboBox().getControl().getItems().clear();
		getView().getDepartmentComboBox().getControl().getItems().clear();

		if (plantList.size() > 0) {
			getView().getPlantComboBox().getControl().getItems().addAll(plantList);
			getView().getPlantComboBox().getControl().getSelectionModel().select(0);

			List<String> departmentList=getModel().findAllDepartmentBySiteAndPlant(site, plantList.get(0));
			if(departmentList!=null && !departmentList.isEmpty()){
				getView().getDepartmentComboBox().getControl().getItems().addAll(departmentList);
			}
		}
	}

	/**
	 * This method Loads product type combo box.
	 * 
	 */
	private void loadProductTypeComboBox() {

		List<String> productTypeList = getModel().findAllProductTypes();

		getView().getProductTypeComboBox().getControl().getItems().clear();
		getView().getProductTypeComboBox().getControl().getItems().addAll(productTypeList);
		getView().getProductTypeComboBox().getControl().getSelectionModel().select(productTypeList.get(0));

		// call to load model details based on product type
		getView().getModelComboBox().getControl().getItems().clear();
		getView().getModelYearComboBox().getControl().getItems().clear();

		loadAllModelCodes(productTypeList.get(0));
	}

	/**
	 * This method Loads Metric combo box.
	 * 
	 */
	private void loadMetricComboBox() {
		String currentMetricName = previousMetricName;

		getView().getMetricComboBox().getControl().getItems().clear();
		List<QiReportingMetric> metricList = getSelectedLevel()!=null?getModel().findAllByLevel(getSelectedLevel()):null;
		if (metricList != null && !metricList.isEmpty()) {
			getView().getMetricComboBox().getControl().getItems().addAll(metricList);
			sortMetricComboboxData();
			if (currentMetricName != null) {
				for (int i = 0; i < metricList.size(); i++) {
					QiReportingMetric tmp = metricList.get(i);
					if (tmp.getId().getMetricName().equals(currentMetricName)) {
						getView().getMetricComboBox().getControl().getSelectionModel().select(i);
						return;
					}
				}
				getView().getMetricComboBox().getControl().getSelectionModel().select(0);
			} else {
				getView().getMetricComboBox().getControl().getSelectionModel().select(0);

			}
		}
	}

	/**
	 * This method adds the plant combo box listener.
	 */
	private void addPlantComboBoxListener() {
		getView().getPlantComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
				clearRadioButtonFilterSelection();
				String site = getView().getSiteTextField().getText();
				getView().getDepartmentComboBox().getControl().getItems().clear();
				List<String> departmentList=getModel().findAllDepartmentBySiteAndPlant(site, new_val);
				if(departmentList!=null && !departmentList.isEmpty()){
					getView().getDepartmentComboBox().getControl().getItems().addAll(departmentList);
				}
			}
		});
	}


	/**
	 * This method adds the product type combo box listener.
	 */
	private void addProductTypeComboBoxListener() {
		getView().getProductTypeComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
				clearRadioButtonFilterSelection();
				getView().getModelComboBox().getControl().getItems().clear();
				getView().getModelYearComboBox().getControl().getItems().clear();

				loadAllModelCodes(new_val);
			}
		});
	}

	/**
	 * This method adds the Model combo box listener.
	 */
	private void addModelComboBoxListener() {
		getView().getModelComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
				getView().getModelYearComboBox().getControl().getItems().clear();
				String productType = getView().getProductTypeComboBox().getControl().getSelectionModel().getSelectedItem();

				loadAllModelYear(productType, new_val);
			}
		});
	}

	/**
	 * This method adds the Model Year combo box listener.
	 */
	private void addModelYearComboBoxListener() {
		getView().getModelYearComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val,String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
			}
		});
	}

	/**
	 * This method adds the Demand Type combo box listener.
	 */
	private void addDemandTypeComboBoxListener() {
		getView().getDemandTypeComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
			}
		});
	}

	/**
	 * This method will be used to load all the model codes based on product
	 * type.
	 * 
	 * @param productType
	 */
	private void loadAllModelCodes(String productType) {		
		List<String>  modelCodeList = getModel().findAllModelCodeByProductType(productType);
		if (!modelCodeList.isEmpty()) {
			Collections.sort(modelCodeList);
			modelCodeList.add(ALL);
			getView().getModelComboBox().getControl().getItems().clear();
			getView().getModelComboBox().getControl().getItems().addAll(modelCodeList);
			getView().getModelComboBox().getControl().getSelectionModel().select(0);

			loadAllModelYear(productType, modelCodeList.get(0));
		}
	}

	/**
	 * This method will be used to load all the model year based on model code.
	 * 
	 * @param productType
	 * @param modelCode
	 */
	private void loadAllModelYear(String productType, String modelCode) {
		List<String> modelYearList = new ArrayList<String>();
		List<ModelGrouping> assignedMtcModel=getModel().findAllModelGroupingsByModelGroup(modelCode);
		if (assignedMtcModel != null) {
			for (ModelGrouping modelGroupGrouping : assignedMtcModel) {
				if (!(ProductTypeUtil.isMbpnProduct(productType.trim()))) {
					List<QiMtcToEntryModelDto> mtcToModelGroupDtoList=getModel().findAllByFilterAndProductType(modelGroupGrouping.getId().getMtcModel(),productType.trim());
					if (mtcToModelGroupDtoList.size()>0 && !mtcToModelGroupDtoList.get(0).getModelYearDescription().isEmpty() 
							&& !modelYearList.contains(mtcToModelGroupDtoList.get(0).getModelYearDescription()))
						modelYearList.add(mtcToModelGroupDtoList.get(0).getModelYearDescription());
				} 
			}
		}
		Collections.sort(modelYearList);
		modelYearList.add(ALL);
		getView().getModelYearComboBox().getControl().getItems().clear();
		getView().getModelYearComboBox().getControl().getItems().addAll(modelYearList);
		getView().getModelYearComboBox().getControl().getSelectionModel().select(0);
	}



	/**
	 * This method adds the department combo box listener.
	 */
	private void addDepartmentComboBoxListener() {
		getView().getDepartmentComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String old_val, String new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();
				if(getView().getResponsibleLevel1RadioButton().isSelected() || getView().getResponsibleLevel2RadioButton().isSelected()
						|| getView().getResponsibleLevel3RadioButton().isSelected() || getView().getDeptRespLevelRadioButton().isSelected()){
					getView().getRespLevelRadioToggleGroup().selectToggle(null);
					if(getView().getResponsibleLevel1RadioButton().isSelected() || getView().getResponsibleLevel2RadioButton().isSelected()
							|| getView().getResponsibleLevel3RadioButton().isSelected() || getView().getDeptRespLevelRadioButton().isSelected()){
						getView().getDynamicContainer().getChildren().clear();
					}
					getView().getTotalMetricValue().clear();
					getView().getMetricComboBox().getControl().getItems().clear();
				}

			}
		});
	}

	/**
	 * This method adds the metric combo box listener.
	 */
	private void addMetricComboBoxListener() {
		getView().getMetricComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiReportingMetric>() {
			public void changed(ObservableValue<? extends QiReportingMetric> ov, QiReportingMetric old_val, QiReportingMetric new_val) {
				if(isRefreshButton){
					return;
				}
				clearMessage();

				if (new_val == null) {
					return;
				} else {
					previousMetricName = new_val.getId().getMetricName();
				}
				addPromptTextInDynamicTextField();
			}
		});
	}

	/**
	 * This method will be used to add dynamic component based on radio button
	 * selection.
	 * 
	 * @param resultList
	 */
	private void addDynamicComponentBasedOnRadioBtnSelection(List<String> resultList) {
		getView().getDynamicContainer().getChildren().clear();
		getView().getTotalMetricValue().setVisible(true);
		getView().getTotalMetricValue().clear();

		if (resultList != null && !resultList.isEmpty()) {

			String defaultMetricDataFormat="%";
			if (getView().getMetricComboBox().getControl().getSelectionModel() != null
					&& getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem() != null) {
				defaultMetricDataFormat = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem().getMetricDataFormat();
			}

			for (String label : resultList) {
				LabeledTextField resultTextField = new LabeledTextField(label, false, TextFieldState.EDIT, new Insets(0, 0, 0, 10), 100, Pos.BASELINE_CENTER, false);
				resultTextField.getControl().setMaxHeight(25);
				resultTextField.getControl().setPromptText(defaultMetricDataFormat);
				addMetricValueTextFieldChangeListener(resultTextField);
				getView().getDynamicContainer().getChildren().add(resultTextField);
			}
		}
	}

	/**
	 * This method gets called when the user enters any metric value in text
	 * field.
	 * 
	 */
	private void addMetricValueTextFieldChangeListener(final LabeledTextField resultTextField) {
		resultTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

				if (!newValue.matches("\\d{0,9}([\\.]\\d{0,4})?")) {
					resultTextField.setText(oldValue);
				} else {
					resultTextField.setText(newValue.equals(".") ? "0." : newValue);
					if (!resultTextField.getLabel().getText().equalsIgnoreCase(METRIC_VALUE)) {
						//non plant target
						calculateTotal();
					}
				}
			}
		});
	}

	private void calculateTotal() {
		try {
			ObservableList<Node> nodeList = getView().getDynamicContainer().getChildren();
			BigDecimal totalValue = BigDecimal.ZERO;
			for (Node node : nodeList) {
				if (node instanceof LabeledTextField) {
					LabeledTextField textField = (LabeledTextField) node;
					if (StringUtils.isNotEmpty(textField.getText())) {
						totalValue = totalValue.add(new BigDecimal(textField.getText()));
						getView().getTotalMetricValue().setText(totalValue.toString());
					}
				}
			}
		} catch (NumberFormatException e) {
			handleException("An error occured in calculateTotal() method", "Failed to convert Metric value", e);
		}
	}

	/**
	 * This method will be used to sort metric combobox data
	 */
	private void sortMetricComboboxData() {
		Collections.sort(getView().getMetricComboBox().getControl().getItems(), new Comparator<QiReportingMetric>() {

			@Override
			public int compare(QiReportingMetric object1, QiReportingMetric object2) {
				return object1.getId().getMetricName().compareTo(object2.getId().getMetricName());
			}
		});
	}

	/**
	 * This method will be used to add prompt text on all the dynamic text
	 * fields on metric combo box selection.
	 * 
	 */
	private void addPromptTextInDynamicTextField() {
		ObservableList<Node> nodeList = getView().getDynamicContainer().getChildren();
		String defaultMetricDataFormat="%";
		SingleSelectionModel<QiReportingMetric> selectionModel = getView().getMetricComboBox().getControl().getSelectionModel();
		if (nodeList != null && !nodeList.isEmpty() && selectionModel != null && selectionModel.getSelectedItem() != null
				&& selectionModel.getSelectedItem().getMetricDataFormat() != null) {
			defaultMetricDataFormat = selectionModel.getSelectedItem().getMetricDataFormat();
			for (Node node : nodeList) {
				if (node instanceof LabeledTextField) {
					LabeledTextField textField = (LabeledTextField) node;
					textField.getControl().setPromptText(defaultMetricDataFormat);
				}
			}
		}
	}

	/**
	 * This method will be used to create Metric Target details
	 * 
	 * @param actionEvent
	 */
	private void addTargetDetails(ActionEvent actionEvent) {
		clearMessage();

		if (!validateMetricTargetDetails()) {
			return;
		}

		//if it is Department level metric with format % and it has Plant level metric with format #.####
		//then the total Department level metric value must be 100 and populate calculated value column

		//if it is Resp Level 3 metric with format % and it has Department level metric with format #.#### or 
		//the Department level metric with format % and it has Plant level metric with format #.####
		//and if it has Resp Level 1 and 2 metric with format %, set up Resp Level 1 instead
		//otherwise, the total Resp level 3 metric value must be 100 and populate calculated value column

		//if it is Resp Level 2 metric with format % and it has Department level metric with format #.#### or 
		//its Department level metric with format % and its Plant level metric with format #.####
		//and if it has Resp Level 1 metric with format %, set up Resp Level 1 instead
		//otherwise, the total Resp level 2 metric value must be 100 and populate calculated value column
		//and create Resp Level 3 target if Level 3 metric is set up

		//if it is Resp Level 1 metric with format % and it has Department level metric with format #.#### or 
		//its Department level metric with format % and its Plant level metric with format #.####
		//then the total Resp level 1 metric value must be 100 and populate calculated value column
		//and create Resp Level 2 or 3 target if Level or Level 3 metric is set up

		final QiReportingMetric metric = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem();
		if (!validateMetricDetails(metric)) {
			return;
		}

		//get selected data from screen
		getSelectedData();

		// generate the targets
		String target = getSelectedTarget();
		if (target != null && MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), TARGET_OVERRIDING_CONFIRMATION)) {
//			System.out.println("\nStarting Add/Update: " + Calendar.getInstance().getTime());
			updateTargets(metric, null, null, null); // add/update the selected target and update lower level targets
			reloadTableDataBySelectedFilters(true); //refresh table view
			clearRadioButtonFilterSelection();
			getView().getDeleteButton().setDisable(true);
		}
//		System.out.println("\nEnding Add/Update: " + Calendar.getInstance().getTime());
	}

	/**
	 * Returns an error message describing the problem if the metric is not valid for add/update.<br>
	 * Returns null if there is no issue for the target.
	 */
	private boolean validateMetricDetails(QiReportingMetric metric) {

		switch (metric.getId().getLevel()) {
		case QiConstant.TARGET_PLANT: {
			return true;
		}
		case QiConstant.TARGET_DEPARTMENT: {
			// skip percentage check for DECIMAL format
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			//check if there is Plant level metric in format #.####
			QiReportingMetric plantLevelMetric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_PLANT));
			if (plantLevelMetric != null && plantLevelMetric.getMetricDataFormat().equals(MetricDataFormat.DECIMAL.getFormatDesc())) {
				higherLevel = QiConstant.TARGET_PLANT;
				useCalculatedValue = false; //reset flag
			}
			return check100Percent();
		}
		case QiConstant.TARGET_THEME: {
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			return check100Percent();
		}
		case QiConstant.TARGET_LOCAL_THEME: {
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			return check100Percent();
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_1: {
			// skip percentage check for DECIMAL format
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			return checkPlantDeptLevel(metric.getId().getMetricName());
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_2: {
			// skip percentage check for DECIMAL format
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			//check if there is level 1 metric in format %
			QiReportingMetric respLevel1Metric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_1));
			if (respLevel1Metric != null && respLevel1Metric.getMetricDataFormat().equals(MetricDataFormat.PERCENTAGE.getFormatDesc())) {
				//should set up Level 1 instead
				displayErrorMessage(SETUP_LEVEL1_TARGET_ERROR_MSG);
				return false;
			}
			return checkPlantDeptLevel(metric.getId().getMetricName());
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_3: {
			// skip percentage check for DECIMAL format
			if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
				return true;
			//check if there is level 1 metric in format %
			QiReportingMetric respLevel1Metric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_1));
			if (respLevel1Metric != null && respLevel1Metric.getMetricDataFormat().equals(MetricDataFormat.PERCENTAGE.getFormatDesc())) {
				//should set up Level 1 instead
				displayErrorMessage(SETUP_LEVEL1_TARGET_ERROR_MSG);
				return false;
			}
			//check if there is level 2 metric in format %
			if (getModel().hasLevel2Resp()) {
				QiReportingMetric respLevel2Metric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_2));
				if (respLevel2Metric != null && respLevel2Metric.getMetricDataFormat().equals(MetricDataFormat.PERCENTAGE.getFormatDesc())) {
					//should set up Level 2 instead
					displayErrorMessage(SETUP_LEVEL2_TARGET_ERROR_MSG);
					return false;
				}
			}
			return checkPlantDeptLevel(metric.getId().getMetricName());
		}
		default:
			return false;
		}
	}

	private boolean checkPlantDeptLevel(String metricName) {
		//check if there is Department level metric
		QiReportingMetric departmentLevelMetric = getModel().findByMetricId(new QiReportingMetricId(metricName, QiConstant.TARGET_DEPARTMENT));

		if (departmentLevelMetric != null) {
			//check if the Department level metric in format #.####
			if (departmentLevelMetric.getMetricDataFormat().equals(MetricDataFormat.DECIMAL.getFormatDesc())) {
				higherLevel = QiConstant.TARGET_DEPARTMENT;
				return check100Percent();
			} else {
				//check if there is Plant level metric
				QiReportingMetric plantLevelMetric = getModel().findByMetricId(new QiReportingMetricId(metricName, QiConstant.TARGET_PLANT));
				//check if the Plant level metric in format #.####
				if (plantLevelMetric != null && plantLevelMetric.getMetricDataFormat().equals(MetricDataFormat.DECIMAL.getFormatDesc())) {
					useCalculatedValue = true;
					higherLevel = QiConstant.TARGET_DEPARTMENT;
					return check100Percent();
				}
			}
		}
		return true;
	}

	private boolean check100Percent() {
		if (totalValue.compareTo(new BigDecimal(100)) != 0) {
			displayErrorMessage(TOTAL_METRIC_VALUE_ERROR_MSG);
			return false;
		}
		return true;
	}

	private void updateTargets(QiReportingMetric metric, List<QiReportingTarget> existingTargets, QiReportingTarget updateParent) {
		updateTargets(metric, existingTargets, updateParent, null);
	}

	private void updateTargets(QiReportingMetric metric, List<QiReportingTarget> existingTargets, List<QiReportingTarget> updateParents) {
		updateTargets(metric, existingTargets, null, updateParents);
	}

	private void updateTargets(QiReportingMetric metric, List<QiReportingTarget> existingTargets, QiReportingTarget updateParent, List<QiReportingTarget> updateParents) {
		// prepare update data
		List<QiReportingTarget> updateTargets = calculateUpdateTargets(metric, existingTargets, updateParent, updateParents);
		if (updateTargets != null && !updateTargets.isEmpty()) {
			// lookup existing data
			if (existingTargets == null) {
				getSelectedData();
			}
			// delete existing data
			deleteExistingTargetsByNativeQuery(updateTargets.get(0).getTarget());
			// save update data
			getModel().saveAllReportingTargetsByNativeQuery(updateTargets);
			
			//check property of auto update child target
			autoUpdateChildTarget = PropertyService.getPropertyBean(QiPropertyBean.class).isAutoUpdateChildTarget();
			if (autoUpdateChildTarget ||
					metric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_1) ||
					metric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_2)) {
				//for level 1 metric, level 2 and level 3 will be auto updated regardless
				//for level 2 metric, level 3 will be auto updated regardless
				updateChildTargets(metric, updateTargets, getLowerLevelMetric(metric));
			} 
		}
	}

	private List<QiReportingTarget> calculateUpdateTargets(QiReportingMetric metric, List<QiReportingTarget> existingTargets, QiReportingTarget updateParent, List<QiReportingTarget> updateParents) {
		if (metric == null)
			return null;

		final String targetLevel = metric.getId().getLevel();

		switch (targetLevel) {
		case QiConstant.TARGET_PLANT: {
			if (updateParent != null)
				throw new IllegalArgumentException(QiConstant.TARGET_PLANT + " target cannot have a parent");

			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				throw new IllegalArgumentException(QiConstant.TARGET_PLANT + " target cannot update from a parent");
			}
			return updateTargets;
		}
		case QiConstant.TARGET_DEPARTMENT: {
			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) { //if not children, get data from screen
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else { //calculate children
				if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
					return null; // cannot calculate auto-updates for a DEPARTMENT target in the DECIMAL format
				QiReportingMetric parentMetric = getModel().findByMetricId(new QiReportingMetricId(updateParent.getMetricName(), updateParent.getLevel()));
				if (parentMetric == null || MetricDataFormat.PERCENTAGE.getFormatDesc().equals(parentMetric.getMetricDataFormat()))
					return null; // cannot calculate auto-updates from a parent target in the PERCENTAGE format

				// calculate updated targets
				updateTargets = new ArrayList<QiReportingTarget>();
				for (QiReportingTarget existingTarget : existingTargets) {
					QiReportingTarget updateTarget = new QiReportingTarget(existingTarget);
					updateTarget.setTargetItem(existingTarget.getTargetItem());
					updateTarget.setLevel(existingTarget.getLevel());
					updateTarget.setTarget(existingTarget.getTarget());
					updateTarget.setMetricValue(existingTarget.getMetricValue());
					updateTarget.setCalculatedMetricValue(updateTarget.getMetricValue() * updateParent.getMetricValue() / 100);
					updateTargets.add(updateTarget);
				}
			}
			return updateTargets;
		}
		case QiConstant.TARGET_THEME: {
			if (updateParent != null)
				throw new IllegalArgumentException(QiConstant.TARGET_THEME + " target cannot have a parent");

			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				throw new IllegalArgumentException(QiConstant.TARGET_THEME + " target cannot update from a parent");
			}
			return updateTargets;
		}
		case QiConstant.TARGET_LOCAL_THEME: {
			if (updateParent != null)
				throw new IllegalArgumentException(QiConstant.TARGET_LOCAL_THEME + " target cannot have a parent");

			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				throw new IllegalArgumentException(QiConstant.TARGET_LOCAL_THEME + " target cannot update from a parent");
			}
			return updateTargets;
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_1: {
			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				if (MetricDataFormat.DECIMAL.getFormatDesc().equals(metric.getMetricDataFormat()))
					return null; // cannot calculate auto-updates for a RESPONSIBLE_LEVEL_1 target in the DECIMAL format
				QiReportingMetric parentMetric = getModel().findByMetricId(new QiReportingMetricId(updateParents.get(0).getMetricName(), updateParents.get(0).getLevel()));
				if (parentMetric == null)
					return null;

				boolean useCalculatedValue = false;
				if (MetricDataFormat.PERCENTAGE.getFormatDesc().equals(parentMetric.getMetricDataFormat())) {
					if (parentMetric.getId().getLevel().equals(QiConstant.TARGET_DEPARTMENT)) {
						QiReportingMetric plantLevelMetric = getModel().findByMetricId(new QiReportingMetricId(updateParents.get(0).getMetricName(), QiConstant.TARGET_PLANT));
						if (plantLevelMetric != null && MetricDataFormat.DECIMAL.getFormatDesc().equals(plantLevelMetric.getMetricDataFormat())) {
							useCalculatedValue = true;
						} else {
							return null; // cannot calculate auto-updates from a parent target in the PERCENTAGE format
						}
					} else {
						return null; // cannot calculate auto-updates from a parent target in the PERCENTAGE format
					}
				}

				// calculate updated targets
				updateTargets = new ArrayList<QiReportingTarget>();
				for (QiReportingTarget existingTarget : existingTargets) {
					QiReportingTarget parentTarget = null;
					for (QiReportingTarget parent : updateParents) {
						if (parent.getDepartment().equals(existingTarget.getDepartment())) {
							parentTarget = parent;
							break;
						}
					}
					QiReportingTarget updateTarget = new QiReportingTarget(existingTarget);
					updateTarget.setTargetItem(existingTarget.getTargetItem());
					updateTarget.setLevel(existingTarget.getLevel());
					updateTarget.setTarget(existingTarget.getTarget());
					updateTarget.setMetricValue(existingTarget.getMetricValue());
					updateTarget.setCalculatedMetricValue(updateTarget.getMetricValue() * (useCalculatedValue ? parentTarget.getCalculatedMetricValue() : parentTarget.getMetricValue()) / 100);
					updateTargets.add(updateTarget);
				}
			}
			return updateTargets;
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_2: {
			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				QiReportingMetric respLevel1Metric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_1));
				if (!metric.getMetricDataFormat().equals(respLevel1Metric.getMetricDataFormat()))
					return null; // cannot update from responsible level 1 of different format

				updateTargets = new ArrayList<QiReportingTarget>();
				Map<QiReportingTarget,Double> metricValueMap = new HashMap<QiReportingTarget,Double>();
				Map<QiReportingTarget,Double> calculatedMetricValueMap = new HashMap<QiReportingTarget,Double>();

				// calculate sums for the metric and calculated metric values
				for (QiReportingTarget parent : updateParents) {
					String targetItem = parent.getTargetItem().substring(0, parent.getTargetItem().lastIndexOf(QiConstant.SEPARATOR));
					if (!StringUtils.isBlank(targetItem)) {
						QiReportingTarget updateTarget = new QiReportingTarget(parent);
						updateTarget.setTargetItem(targetItem);
						updateTarget.setLevel(QiConstant.TARGET_RESPONSIBLE_LEVEL_2);
						updateTarget.setTarget(QiConstant.RESPONSIBLE_LEVEL_2);
						metricValueMap.put(updateTarget, metricValueMap.getOrDefault(updateTarget, 0.0) + parent.getMetricValue());
						calculatedMetricValueMap.put(updateTarget, calculatedMetricValueMap.getOrDefault(updateTarget, 0.0) + parent.getCalculatedMetricValue());
					}
				}

				// set the metric and calculated metric values
				for (QiReportingTarget updateTarget : metricValueMap.keySet()) {
					updateTarget.setMetricValue(metricValueMap.get(updateTarget));
					updateTarget.setCalculatedMetricValue(calculatedMetricValueMap.get(updateTarget));
					updateTargets.add(updateTarget);
				}
			}
			return updateTargets;
		}
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_3: {
			List<QiReportingTarget> updateTargets;
			if (existingTargets == null) {
				updateTargets = prepareTargetObjectDynamicallyBasedOnFilter(metric);
			} else {
				QiReportingMetric respLevel2Metric = getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_2));
				if (!metric.getMetricDataFormat().equals(respLevel2Metric.getMetricDataFormat()))
					return null; // cannot update from responsible level 2 of different format

				updateTargets = new ArrayList<QiReportingTarget>();
				Map<QiReportingTarget,Double> metricValueMap = new HashMap<QiReportingTarget,Double>();
				Map<QiReportingTarget,Double> calculatedMetricValueMap = new HashMap<QiReportingTarget,Double>();

				// calculate sums for the metric and calculated metric values
				for (QiReportingTarget parent : updateParents) {
					String targetItem = parent.getTargetItem().substring(0, parent.getTargetItem().indexOf(QiConstant.SEPARATOR));
					if (!StringUtils.isBlank(targetItem)) {
						QiReportingTarget updateTarget = new QiReportingTarget(parent);
						updateTarget.setTargetItem(targetItem);
						updateTarget.setLevel(QiConstant.TARGET_RESPONSIBLE_LEVEL_3);
						updateTarget.setTarget(QiConstant.RESPONSIBLE_LEVEL_3);
						metricValueMap.put(updateTarget, metricValueMap.getOrDefault(updateTarget, 0.0) + parent.getMetricValue());
						calculatedMetricValueMap.put(updateTarget, calculatedMetricValueMap.getOrDefault(updateTarget, 0.0) + parent.getCalculatedMetricValue());
					}
				}

				// set the metric and calculated metric values
				for (QiReportingTarget updateTarget : metricValueMap.keySet()) {
					updateTarget.setMetricValue(metricValueMap.get(updateTarget));
					updateTarget.setCalculatedMetricValue(calculatedMetricValueMap.get(updateTarget));
					updateTargets.add(updateTarget);
				}
			}
			return updateTargets;
		}
		default: {
			return null;
		}
		}
	}

	private void updateChildTargets(final QiReportingMetric parentMetric, final List<QiReportingTarget> parentTargets, final QiReportingMetric childMetric) {
		if (childMetric != null) {
			String parentLevel = parentMetric.getId().getLevel();
			if (parentLevel.equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_1)) {
				if (getModel().hasRespLevel(parentTargets.get(0).getSite(), parentTargets.get(0).getPlant(), parentTargets.get(0).getDepartment(), (short) 2)) {
					List<QiReportingTarget> children = findChildTargets(parentTargets.get(0), childMetric);
					updateTargets(childMetric, children, parentTargets);
				}
			} else if (parentLevel.equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_2)) {
				if (getModel().hasRespLevel(parentTargets.get(0).getSite(), parentTargets.get(0).getPlant(), parentTargets.get(0).getDepartment(), (short) 3)) {
					List<QiReportingTarget> children = findChildTargets(parentTargets.get(0), childMetric);
					updateTargets(childMetric, children, parentTargets);
				}
			} else if (parentLevel.equals(QiConstant.TARGET_DEPARTMENT)) { 
				List<QiReportingTarget> children = findChildTargetsByDateRange(parentTargets.get(0), childMetric);
				if (children != null && !children.isEmpty())
					updateTargets(childMetric, children, parentTargets);
				} else { // parent level is Plant
					List<QiReportingTarget> children = findChildTargetsByDateRange(parentTargets.get(0), childMetric);
					if (children != null && !children.isEmpty())
						updateTargets(childMetric, children, parentTargets.get(0));
				}
		}
	}

	private List<QiReportingTarget> findChildTargets(QiReportingTarget parentTarget, QiReportingMetric childMetric) {
		return getModel().findAllTargetByFilter(parentTarget.getSite(), parentTarget.getPlant(), parentTarget.getProductType(), parentTarget.getModelGroup(), parentTarget.getModelYearDescription(),
				parentTarget.getDemandType(), getTargetForLevel(childMetric.getId().getLevel()), parentTarget.getMetricName(), startDate, endDate, parentTarget.getDepartment());
	}
	
	private List<QiReportingTarget> findChildTargetsByDateRange(QiReportingTarget parentTarget, QiReportingMetric childMetric) {
		String level = getTargetForLevel(childMetric.getId().getLevel());
		String dept = parentTarget.getDepartment();
		if (level.equals(QiConstant.RESPONSIBLE_LEVEL_1) || level.equals(QiConstant.RESPONSIBLE_LEVEL_2) || level.equals(QiConstant.RESPONSIBLE_LEVEL_3)) { 
			dept = null; //get resp level for all depts
		}
		return getModel().findAllTargetByFilter(parentTarget.getSite(), parentTarget.getPlant(), parentTarget.getProductType(), parentTarget.getModelGroup(), parentTarget.getModelYearDescription(),
				parentTarget.getDemandType(), level, parentTarget.getMetricName(), startDate, endDate, dept);
	}

	private QiReportingMetric getHigherLevelMetric(QiReportingMetric metric) {
		String higherLevel = getHigherLevel(metric.getId().getLevel());
		if (higherLevel != null) {
			return getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), higherLevel));
		}
		return null;
	}

	private QiReportingMetric getLowerLevelMetric(QiReportingMetric metric) {
		String lowerLevelTarget = getLowerLevel(metric.getId().getLevel());
		if (lowerLevelTarget != null) {
			return getModel().findByMetricId(new QiReportingMetricId(metric.getId().getMetricName(), lowerLevelTarget));
		}
		return null;
	}

	private String getHigherLevel(String level) {
		switch (level) {
		case QiConstant.TARGET_DEPARTMENT:
			return QiConstant.TARGET_PLANT;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_1:
			return QiConstant.TARGET_DEPARTMENT;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_2:
			return QiConstant.TARGET_RESPONSIBLE_LEVEL_1;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_3:
			return QiConstant.TARGET_RESPONSIBLE_LEVEL_2;
		default:
			return null;
		}
	}

	private String getLowerLevel(String level) {
		switch (level) {
		case QiConstant.TARGET_PLANT:
			return QiConstant.TARGET_DEPARTMENT;
		case QiConstant.TARGET_DEPARTMENT:
			return QiConstant.TARGET_RESPONSIBLE_LEVEL_1;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_1:
			return QiConstant.TARGET_RESPONSIBLE_LEVEL_2;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_2:
			return QiConstant.TARGET_RESPONSIBLE_LEVEL_3;
		default:
			return null;
		}
	}

	private String getTargetForLevel(String level) {
		switch (level) {
		case QiConstant.TARGET_PLANT:
			return QiConstant.TARGET_PLANT;
		case QiConstant.TARGET_DEPARTMENT:
			return QiConstant.TARGET_DEPARTMENT;
		case QiConstant.TARGET_THEME:
			return QiConstant.TARGET_THEME;
		case QiConstant.TARGET_LOCAL_THEME:
			return QiConstant.TARGET_LOCAL_THEME;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_1:
			return QiConstant.RESPONSIBLE_LEVEL_1;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_2:
			return QiConstant.RESPONSIBLE_LEVEL_2;
		case QiConstant.TARGET_RESPONSIBLE_LEVEL_3:
			return QiConstant.RESPONSIBLE_LEVEL_3;
		default:
			return null;
		}
	}

	private void deleteExistingTargetsByNativeQuery(String target) {
		
		if (target.equals(QiConstant.TARGET_DEPARTMENT) || target.equals(QiConstant.TARGET_PLANT)) {
			department = null;
		}
		
		getModel().deleteAllReportingTargetsByNativeQuery(site, plant, productType,
				modelGroup, modelYear, demandType, target, metricName, startDate, endDate, department);
	}

	/**
	 * This method will be used to prepare {@link QiReportingTarget} object for
	 * multiple metric values based on different radio filter selection.
	 * 
	 * @param reportingTargetsToBeSaved
	 * @param target
	 */
	private List<QiReportingTarget> prepareTargetObjectDynamicallyBasedOnFilter(QiReportingMetric metric) {
		String target = getTargetForLevel(metric.getId().getLevel());
		List<QiReportingTarget> reportingTargetsToBeSaved = new ArrayList<QiReportingTarget>();
		ObservableList<Node> dynamicNodeList = getView().getDynamicContainer().getChildren();
		try {
			if (dynamicNodeList != null && !dynamicNodeList.isEmpty()) {
				for (Node node : dynamicNodeList) {
					if (node instanceof LabeledTextField) {
						LabeledTextField labeledTextField = (LabeledTextField) node;
						if (StringUtils.isNotBlank(labeledTextField.getText())) {

							String targetItem = labeledTextField.getLabel().getText();
							Double metricValue = Double.parseDouble(labeledTextField.getText());

							QiReportingTarget qiReportingTarget = prepareReportingTargetObject(target);
							qiReportingTarget.setTargetItem(targetItem);
							qiReportingTarget.setMetricValue(metricValue);
							if (target.equals(QiConstant.TARGET_DEPARTMENT)) {
								qiReportingTarget.setDepartment(targetItem);
							} else if (target.equals(QiConstant.TARGET_PLANT)) {
								qiReportingTarget.setDepartment(null);
							} 

							reportingTargetsToBeSaved.add(qiReportingTarget);
						}
					}
				}
			}
			return setEffectiveDateAndCalculatedValue(metric, reportingTargetsToBeSaved, getView().getStartDatePicker().getValue(), getView().getEndDatePicker().getValue());
		} catch (NumberFormatException e) {
			handleException("An error occured in prepareTargetObjectDynamicallyBasedOnFilter() method",	"Failed to convert Metric value", e);
			return null;
		}
	}

	/**
	 * For a given list of targets, returns a list populated by those items across the given date range.
	 * @param reportingTargetsToBeSaved
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private List<QiReportingTarget> setEffectiveDateAndCalculatedValue(QiReportingMetric metric, List<QiReportingTarget> reportingTargetsToBeSaved, LocalDate startDate, LocalDate endDate) {
		List<QiReportingTarget> finalListToBeSaved = new ArrayList<QiReportingTarget>();
		Long range = ChronoUnit.DAYS.between(startDate, endDate);

		// find parent targets for calculating metric value
		List<QiReportingTarget> parentTargets = null;
		if (MetricDataFormat.PERCENTAGE.getFormatDesc().equals(metric.getMetricDataFormat())) {
			QiReportingMetric higherLevelMetric = getHigherLevelMetric(metric);
			// if the metric for the higher level is in DECIMAL format, use that level as reference for calculation
			if (higherLevelMetric != null && (useCalculatedValue || MetricDataFormat.DECIMAL.getFormatDesc().equals(higherLevelMetric.getMetricDataFormat()))) {
				parentTargets = getModel().findAllTargetByFilter(this.site, this.plant, this.productType, this.modelGroup, this.modelYear,
						this.demandType, higherLevel, this.metricName, this.startDate, this.endDate,
						higherLevel.equals(QiConstant.TARGET_PLANT) ? null: this.department);
			}
		}

		// Outer loop for number of target items
		for (QiReportingTarget qiReportingTarget : reportingTargetsToBeSaved) {
			// Inner loop for number of days
			for (int daysCounter = 0; daysCounter <= range; daysCounter++) {
				QiReportingTarget clonedTarget = (QiReportingTarget) qiReportingTarget.deepCopy();
				
				java.sql.Date effectiveDate;
				if (getModel().isServerBeforeClient()) {
					effectiveDate = java.sql.Date.valueOf(startDate.plusDays(daysCounter));
				} else {
					effectiveDate = new java.sql.Date(java.util.Date.from(startDate.plusDays(daysCounter).atStartOfDay(serverZoneId).toInstant()).getTime());
				}
				clonedTarget.setEffectiveDate(effectiveDate);

				// calculate metric value if applicable
				if (parentTargets != null) {
					double parentMetricValue = 0;
					for (QiReportingTarget parentTarget : parentTargets) {
						if (parentTarget.getSite().equals(clonedTarget.getSite()) && parentTarget.getPlant().equals(clonedTarget.getPlant()) &&
								parentTarget.getProductType().equals(clonedTarget.getProductType()) && parentTarget.getModelGroup().equals(clonedTarget.getModelGroup()) &&
								parentTarget.getModelYearDescription().equals(clonedTarget.getModelYearDescription()) && parentTarget.getDemandType().equals(clonedTarget.getDemandType()) &&
								parentTarget.getMetricName().equals(clonedTarget.getMetricName()) && 
								DateUtils.isSameDay(parentTarget.getEffectiveDate(), clonedTarget.getEffectiveDate())) {
							if (useCalculatedValue) {
								parentMetricValue = parentTarget.getCalculatedMetricValue();
							} else {
								parentMetricValue = parentTarget.getMetricValue();
							}
							break;
						}
					}
					if (parentMetricValue != 0) {
						clonedTarget.setCalculatedMetricValue(clonedTarget.getMetricValue() * parentMetricValue / 100);
					}
				}

				finalListToBeSaved.add(clonedTarget);
			}
		}
		return finalListToBeSaved;
	}

	/**
	 * This method will be used to clear radio button filter and dynamic
	 * component selection.
	 * 
	 */
	private void clearRadioButtonFilterSelection() {
		if (getView().getRadioToggleGroup().getSelectedToggle() != null) {
			getView().getRadioToggleGroup().selectToggle(null);
		}
		if (getView().getRespLevelRadioToggleGroup().getSelectedToggle() != null) {
			getView().getRespLevelRadioToggleGroup().selectToggle(null);
		}
		getView().getDynamicContainer().getChildren().clear();

		getView().getTotalMetricValue().clear();
		getView().getMetricComboBox().getControl().getItems().clear();
	}

	/**
	 * This method will be used to prepare {@link QiReportingTarget} object.
	 * 
	 * @param target
	 * @return qiReportingTarget
	 */
	private QiReportingTarget prepareReportingTargetObject(String target) {

		QiReportingTarget qiReportingTarget = new QiReportingTarget();
		String modelYear = getView().getModelYearComboBox().getControl().getSelectionModel().getSelectedItem();
		String modelGroup = getView().getModelComboBox().getControl().getSelectionModel().getSelectedItem();

		qiReportingTarget.setSite(getView().getSiteTextField().getText());
		qiReportingTarget.setPlant(getView().getPlantComboBox().getControl().getSelectionModel().getSelectedItem());
		qiReportingTarget.setProductType(getView().getProductTypeComboBox().getControl().getSelectionModel().getSelectedItem());
		qiReportingTarget.setModelGroup(modelGroup);
		qiReportingTarget.setModelYearDescription(modelYear);
		qiReportingTarget.setDemandType(getView().getDemandTypeComboBox().getControl().getSelectionModel().getSelectedItem());
		qiReportingTarget.setMetricName(getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem().getId().getMetricName());
		qiReportingTarget.setLevel(getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem().getId().getLevel());
		qiReportingTarget.setTarget(target);
		qiReportingTarget.setCreateUser(getUserId());
		qiReportingTarget.setDepartment(getView().getDepartmentComboBox().getControl().getSelectionModel().getSelectedItem());
		qiReportingTarget.setSystem("QICS");
		return qiReportingTarget;
	}


	/**
	 * This method will be used to validate Metric Target details.
	 * 
	 * @return boolean
	 */
	private boolean validateMetricTargetDetails() {

		if (getView().getPlantComboBox().getControl().getSelectionModel().isEmpty()) {
			displayErrorMessage(PLEASE_SELECT_RESPONSIBLE_PLANT);
			return false;
		}
		if (getView().getProductTypeComboBox().getControl().getSelectionModel().isEmpty()) {
			displayErrorMessage(PLEASE_SELECT_RESPONSIBLE_PRODUCT_TYPE);
			return false;
		}
		if (getView().getModelComboBox().getControl().getSelectionModel().isEmpty()) {
			displayErrorMessage(PLEASE_SELECT_RESPONSIBLE_MODEL);
			return false;
		}
		if (getView().getModelYearComboBox().getControl().getSelectionModel().isEmpty()) {
			displayErrorMessage(PLEASE_SELECT_RESPONSIBLE_MODEL_YEAR);
			return false;
		}
		if (getView().getMetricComboBox().getControl().getSelectionModel().isEmpty()) {
			displayErrorMessage(PLEASE_CREATE_METRIC_FIRST);
			return false;
		}

		if (getView().getStartDatePicker().getValue() == null) {
			displayErrorMessage(PLEASE_ENTER_VALID_START_DATE);
			return false;
		}
		if (getView().getEndDatePicker().getValue() == null) {
			displayErrorMessage(PLEASE_ENTER_VALID_END_DATE);
			return false;
		}
		if(getView().getThemeRadioButton().isSelected() || getView().getTempTrackingRadioButton().isSelected() || getView().getDeptRespLevelRadioButton().isSelected()
				|| getView().getResponsibleLevel1RadioButton().isSelected()||getView().getResponsibleLevel2RadioButton().isSelected() ||
				getView().getResponsibleLevel3RadioButton().isSelected()){
			if (getView().getDepartmentComboBox().getControl().getSelectionModel().isEmpty()) {
				displayErrorMessage(DEPT_COMBOBOX_ERROR_MSG);
				return false;
			}
		}

		ObservableList<Node> nodeList = getView().getDynamicContainer().getChildren();
		totalValue = BigDecimal.ZERO;

		String metricDataFormat = getView().getMetricComboBox().getControl().getSelectionModel().getSelectedItem().getMetricDataFormat();

		for (Node node : nodeList) {
			if (node instanceof LabeledTextField) {
				LabeledTextField textField = (LabeledTextField) node;
				if (StringUtils.isNotEmpty(textField.getText())) {
					if (MetricDataFormat.PERCENTAGE.getFormatDesc().equals(metricDataFormat) && 
							new BigDecimal(textField.getText()).doubleValue() > 100) {
						displayErrorMessage(PERCENT_METRIC_VALUE_ERROR_MSG);
						return false;
					}

					totalValue = totalValue.add(new BigDecimal(textField.getText()));
				}
			}
		}

		if (totalValue.equals(BigDecimal.ZERO)) {
			displayErrorMessage(METRIC_VALUE_ERROR_MSG);
			return false;
		}

		return true;
	}

	/**
	 * This method will add end date validation
	 */
	private void addEndDateListener() {
		getView().getEndDatePicker().valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				LocalDate startDate = getView().getStartDatePicker().getValue();
				if (startDate != null && newValue != null && newValue.compareTo(startDate) < 0) {
					displayErrorMessage(END_DATE_VALIDATION_MSG);
					getView().getEndDatePicker().setValue(oldValue);
				} else {
					clearDisplayMessage();
				}
			}
		});
	}

	/**
	 * This method will add start date validation
	 */
	private void addStartDateListener() {
		getView().getStartDatePicker().valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				LocalDate endDate = getView().getEndDatePicker().getValue();
				if (endDate != null && newValue != null && newValue.compareTo(endDate) > 0) {
					displayErrorMessage(START_DATE_VALIDATION_MSG);
					getView().getStartDatePicker().setValue(oldValue);
				} else {
					clearDisplayMessage();
				}
			}
		});
	}

	/**
	 * This method will clear department combo box selection
	 */
	private void clearDepartmentComboBoxSelection() {
		if (getView().getDepartmentComboBox().getControl().getSelectionModel() != null) {
			getView().getDepartmentComboBox().getControl().getSelectionModel().clearSelection();
		}
	}

	/**
	 * This method will be used to delete target details.
	 */
	private void deleteTargetDetails(){
		List<QiReportingTarget> overiddenTargets = reloadTableDataBySelectedFilters(false);
		if (overiddenTargets != null && !overiddenTargets.isEmpty()) {
			if (!MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()),TARGET_DELETE_CONFIRMATION))
				return;
			deleteExistingTargetsByNativeQuery(target);
		}
		reloadTableDataBySelectedFilters(true);
		clearRadioButtonFilterSelection();
	}


	/**
	 * This method is used to refresh the data.
	 */
	public void refreshBtnAction() {
		isRefreshButton = true;

		getSelectedData();

		getView().getPlantComboBox().getControl().getItems().clear();
		getView().getProductTypeComboBox().getControl().getItems().clear();
		getView().getModelComboBox().getControl().getItems().clear();
		getView().getModelYearComboBox().getControl().getItems().clear();
		getView().getMetricComboBox().getControl().getItems().clear();

		List<String> plantList = getModel().findAllPlantBySite(getView().getSiteTextField().getText());

		loadPlantComboBox();
		loadProductTypeComboBox();
		loadMetricComboBox();
		if(null!=plant && plantList.contains(plant) ){
			//refresh plant drop down
			getView().getPlantComboBox().getControl().getSelectionModel().select(plant);
			if(productType!= null){
				//refresh product type drop down
				List<String> productTypeList = getModel().findAllProductTypes();
				getView().getProductTypeComboBox().getControl().getItems().addAll(productTypeList);
				getView().getProductTypeComboBox().getControl().getSelectionModel().select(productType);
				if(modelGroup!= null ){
					//refresh model group
					List<String> modelgroupList = getModel().findAllModelCodeByProductType(productType);
					modelgroupList.add(ALL);
					getView().getModelComboBox().getControl().getItems().clear();
					getView().getModelComboBox().getControl().getItems().addAll(modelgroupList);
					getView().getModelComboBox().getControl().getSelectionModel().select(modelGroup);
					if(modelGroup!= null){
						//refresh  model year
						loadAllModelYear(productType, modelGroup);
						getView().getModelYearComboBox().getControl().getSelectionModel().select(modelYear);
					}
				}
			}
		}

		if(demandType!=null){
			getView().getDemandTypeComboBox().getControl().getSelectionModel().select(demandType);
		}
		List <String> deptList=getModel().findAllDepartmentBySiteAndPlant(getView().getSiteTextField().getText(), plant);
		getView().getDepartmentComboBox().getControl().getItems().addAll(deptList);

		if(department!=null && deptList.contains(department)){
			getView().getDepartmentComboBox().getControl().getSelectionModel().select(department);
		}

		themeNameList = getModel().findAllActiveThemeNames();
		temporaryTrackingList = getModel().findAllActiveTemporaryTrackingNames();

		if (getView().getPlantRadioButton().isSelected()) {
			handlePlantRadioButtonSelction();
		} else if (getView().getDepartmentRadioButton().isSelected()) {
			handleDepartmentRadioButtonSelction();
		} else if (getView().getThemeRadioButton().isSelected()) {
			handleThemeRadioButtonSelction();
		} else if (getView().getTempTrackingRadioButton().isSelected()) {
			handleTempTrackingRadioButtonSelction();
		} 
		else if (getView().getDeptRespLevelRadioButton().isSelected()) {
			handleDeptRespRadioButtonSelection();
		}
		else if (getView().getResponsibleLevel1RadioButton().isSelected()) {
			handleRespLevel1RadioButtonSelection();
		} else if (getView().getResponsibleLevel2RadioButton().isSelected()) {
			handleRespLevel2RadioButtonSelection();
		} else if (getView().getResponsibleLevel3RadioButton().isSelected()) {
			handleRespLevel3RadioButtonSelection();
		} 

		//check if all regional metrics are set up
		List<String> missingRegionalMetricList = getModel().findMissingRegionalMetricList();
		if (missingRegionalMetricList != null && missingRegionalMetricList.size() > 0) {
			displayErrorMessage("Please create missing regional metrics: " + String.join(",", missingRegionalMetricList));
		}

		isRefreshButton = false;
	}

	/**
	 * This method is used to hide Department ComboBox
	 */
	private void plantDeptRadioButtonSelected() {
		getView().handleDeptRespRadioButtonSelection(false);
		clearFieldsOnRadioButtonSelection();
	}

	/**
	 * This method will be used clear selection.
	 */
	private void clearFieldsOnRadioButtonSelection() {
		getView().getDeleteButton().setDisable(true);

		loadMetricComboBox();

		getView().getReportingTargetTable().getTable().getItems().clear();
		getView().getAddButton().setText("Add");

		if (getView().getRespLevelRadioToggleGroup().getSelectedToggle() != null) {
			for (Toggle toggle : getView().getRadioToggleGroup().getToggles()) {
				if (((LoggedRadioButton)toggle).getText().equals(QiConstant.TARGET_DEPT_RESP_LEVEL)) {
					getView().getRadioToggleGroup().selectToggle(toggle);
				}
			}
		}
	}
}