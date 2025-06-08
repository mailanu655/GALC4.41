package com.honda.galc.client.teamleader.qi.reportingtarget;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.teamleader.qi.view.QiFxDialog;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.enumtype.MetricDataFormat;
import com.honda.galc.entity.qi.QiReportingMetric;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>ReportingMetricDialog</code>
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
public class ReportingMetricDialog extends QiFxDialog<ReportingTargetMaintenanceModel> {

	private ReportingMetricDialogController metricDialogController;
	private QiReportingMetric qiReportingMetric;
	private LabeledTextField metricNameTextField;
	private LabeledTextField metricDescTextField;
	private LabeledComboBox<String> metricDataFormatComboBox;
	private LoggedRadioButton copyToDeptRadioButton;
	private LabeledComboBox<String> deptMetricDataFormatComboBox;
	private LoggedButton saveButton;
	private LoggedButton cancelButton;
	
	private double screenWidth;
	private double screenHeight;
	
	private String level;
	private boolean isUpdateMode;

	public ReportingMetricDialog(ReportingTargetMaintenanceModel model, String applicationId,
			QiReportingMetric selectedMetric, String level, boolean isUpdateMode) {

		super("Metric Dialog - " + level, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		this.setMetricDialogController(new ReportingMetricDialogController(model, this));
		this.qiReportingMetric = selectedMetric;
		this.setLevel(level);
		this.setUpdateMode(isUpdateMode);
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		initComponents();
		// check if its a update mode
		if (isUpdateMode) {
			saveButton.setText(QiConstant.UPDATE);
			setComponentInitialData();
		}
		getMetricDialogController().initListeners();
	}

	/**
	 * This method will be used to set component initial values.
	 */
	private void setComponentInitialData() {
		metricNameTextField.setText(qiReportingMetric.getId().getMetricName());
		metricDescTextField.setText(qiReportingMetric.getMetricDescription());
		metricDataFormatComboBox.getControl().getSelectionModel().select(qiReportingMetric.getMetricDataFormat());
	}

	/**
	 * This method will be used to initialize all the components.
	 * 
	 */
	private void initComponents() {
		MigPane mainDialog = new MigPane();
		mainDialog.setPrefHeight(screenHeight * .4);
		mainDialog.setPrefWidth(screenWidth * .23);

		metricNameTextField = new LabeledTextField("Metric Name", true, TextFieldState.EDIT, new Insets(10), 100, Pos.BASELINE_LEFT, true);
		metricDescTextField = new LabeledTextField("Metric Desc", true, TextFieldState.EDIT, new Insets(10), 100, Pos.BASELINE_LEFT, false);
		metricDataFormatComboBox = createLabeledComboBox("metricDataFormatComboBox", "Metric Data Format", true, true, false);
		metricDataFormatComboBox.getControl().getItems().addAll(MetricDataFormat.PERCENTAGE.getFormatDesc(), MetricDataFormat.DECIMAL.getFormatDesc());
		metricDataFormatComboBox.setSelectedIndex(0);
		saveButton = createButton(QiConstant.SAVE);
		saveButton.setDisable(true);
		cancelButton = createButton(QiConstant.CANCEL);
		
		if (getLevel().equals(QiConstant.TARGET_PLANT) && !isUpdateMode()) {
			copyToDeptRadioButton = createRadioButton("Copy to Department", null, false, getMetricDialogController());
			deptMetricDataFormatComboBox = createLabeledComboBox("deptMetricDataFormatComboBox", "Department Metric Data Format", true, false, false);
			deptMetricDataFormatComboBox.getControl().getItems().addAll(MetricDataFormat.PERCENTAGE.getFormatDesc(), MetricDataFormat.DECIMAL.getFormatDesc());
			deptMetricDataFormatComboBox.setSelectedIndex(0);				
		}

		mainDialog.add(metricNameTextField, "wrap");
		mainDialog.add(metricDescTextField, "wrap");
		mainDialog.add(metricDataFormatComboBox, "width ::150, wrap 30");
		
		if (getLevel().equals(QiConstant.TARGET_PLANT) && !isUpdateMode()) {
			mainDialog.add(copyToDeptRadioButton, "wrap");
			mainDialog.add(deptMetricDataFormatComboBox, "width ::200, wrap 30" );
		}
		
		mainDialog.add(saveButton, "split 2, align center");
		mainDialog.add(cancelButton);

		((BorderPane) getScene().getRoot()).setCenter(mainDialog);
	}

	/**
	 * This method will be used to create LoggedButton object.
	 * 
	 * @param text
	 * @return
	 */
	private LoggedButton createButton(String text) {
		LoggedButton loggedButton = createBtn(text, getMetricDialogController());
		loggedButton.setPrefHeight(40);
		loggedButton.setPrefWidth(100);
		loggedButton.setStyle("-fx-font-size: 12pt; -fx-font-weight: bold;");
		return loggedButton;
	}

	/**
	 * This method will be used to create Labeled Combo Box
	 * 
	 * @param id
	 * @param labelName
	 * @param isHorizontal
	 * @param isMandatory
	 * @param isDisabled
	 * @return
	 */
	public LabeledComboBox<String> createLabeledComboBox(String id, String labelName, boolean isHorizontal, boolean isMandatory, boolean isDisabled) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(labelName, isHorizontal, new Insets(10), true, isMandatory);
		comboBox.setId(id);
		comboBox.getControl().setMinHeight(35);
		comboBox.getControl().getStyleClass().add("combo-box-base");
		comboBox.getControl().setDisable(isDisabled);
		
		return comboBox;
	}

	public ReportingMetricDialogController getMetricDialogController() {
		return metricDialogController;
	}

	public void setMetricDialogController(ReportingMetricDialogController metricDialogController) {
		this.metricDialogController = metricDialogController;
	}

	public LabeledTextField getMetricNameTextField() {
		return metricNameTextField;
	}

	public LabeledTextField getMetricDescTextField() {
		return metricDescTextField;
	}

	public LabeledComboBox<String> getMetricDataFormatComboBox() {
		return metricDataFormatComboBox;
	}

	public LoggedButton getSaveButton() {
		return saveButton;
	}

	public LoggedButton getCancelButton() {
		return cancelButton;
	}
	
	public String getLevel() {
		return level;
	}

	public QiReportingMetric getQiReportingMetric() {
		return qiReportingMetric;
	}

	public void setQiReportingMetric(QiReportingMetric qiReportingMetric) {
		this.qiReportingMetric = qiReportingMetric;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public boolean isUpdateMode() {
		return isUpdateMode;
	}

	public void setUpdateMode(boolean isUpdateMode) {
		this.isUpdateMode = isUpdateMode;
	}

	public LoggedRadioButton getCopyToDeptRadioButton() {
		return copyToDeptRadioButton;
	}

	public LabeledComboBox<String> getDeptMetricDataFormatComboBox() {
		return deptMetricDataFormatComboBox;
	}
}
