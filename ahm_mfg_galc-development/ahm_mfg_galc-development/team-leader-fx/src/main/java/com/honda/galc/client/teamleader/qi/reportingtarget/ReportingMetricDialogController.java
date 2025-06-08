package com.honda.galc.client.teamleader.qi.reportingtarget;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.controller.QiDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.qi.QiReportingMetric;
import com.honda.galc.entity.qi.QiReportingMetricId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>ReportingMetricDialogController</code>
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
public class ReportingMetricDialogController extends QiDialogController<ReportingTargetMaintenanceModel, ReportingMetricDialog> {

	private static final int METRIC_NAME_LENGTH = 10;

	public ReportingMetricDialogController(ReportingTargetMaintenanceModel model, ReportingMetricDialog dialog) {
		setModel(model);
		setDialog(dialog);
	}

	@Override
	public void handle(ActionEvent actionEvent) {

		if (actionEvent.getSource() instanceof LoggedButton) {
			LoggedButton button = (LoggedButton) actionEvent.getSource();
			if (button.getText().equalsIgnoreCase(QiConstant.SAVE)) {
				saveButtonAction();
			} else if (button.getText().equalsIgnoreCase(QiConstant.UPDATE)) {
				updateButtonAction();
			} else
				cancelButtonAction();
		}

	}

	/**
	 * This method is used to save {@link QiReportingMetric} object.
	 */
	private void saveButtonAction() {
		try {
			QiReportingMetric savedData = getModel().saveReportingMetric(prepareQiReportingMetricObject());
			getDialog().setQiReportingMetric(savedData);

			if (getDialog().getCopyToDeptRadioButton() != null && getDialog().getCopyToDeptRadioButton().isSelected()) {
				QiReportingMetric deptReportingMetric = new QiReportingMetric();
				deptReportingMetric.setId(new QiReportingMetricId(savedData.getId().getMetricName(), QiConstant.TARGET_DEPARTMENT));
				deptReportingMetric.setMetricDescription(savedData.getMetricDescription());
				deptReportingMetric.setMetricDataFormat(getDialog().getDeptMetricDataFormatComboBox().getControl().getSelectionModel().getSelectedItem());
				deptReportingMetric.setCreateUser(getUserId());
				getModel().saveReportingMetric(deptReportingMetric);
			}

			((Stage) getDialog().getSaveButton().getScene().getWindow()).close();
		} catch (Exception e) {
			handleException("An error occured in Metric save method ", "Failed To Save Metric" + (StringUtils.isBlank(e.getMessage()) ? "" : ":\n" + e.getMessage()), e);
		}
	}

	/**
	 * This method will be used to update {@link QiReportingMetric} object.
	 * 
	 */
	private void updateButtonAction() {
		try {
			QiReportingMetric initialObject = getDialog().getQiReportingMetric();
			QiReportingMetric clonedObject = (QiReportingMetric) initialObject.deepCopy();

			String metricName = getDialog().getMetricNameTextField().getText().trim();
			String metricDesc = getDialog().getMetricDescTextField().getText();
			String metricDataFormat = getDialog().getMetricDataFormatComboBox().getControl().getSelectionModel().getSelectedItem();

			if (metricName.equalsIgnoreCase(initialObject.getId().getMetricName())) {
				checkRespLevelFormat(initialObject.getId().getLevel(), metricName, metricDataFormat);
				initialObject.setMetricDescription(metricDesc != null ? metricDesc.trim() : null);
				initialObject.setMetricDataFormat(metricDataFormat);
				initialObject.setUpdateUser(getUserId());
				// call to update object
				getModel().updateMetricsData(initialObject);
				//call to log audit
				AuditLoggerUtil.logAuditInfo(clonedObject, initialObject, "Metric has been updated", getDialog().getScreenName(), getUserId());
				cascadeUpdateRespLevelMetric(clonedObject, initialObject);
			} else {
				// Here we are updating the primary key of the entity hence we need
				// to first create new entity then update the old references and
				// finally delete the entity.
				QiReportingMetric savedData = getModel().saveReportingMetric(prepareQiReportingMetricObject());
				getModel().updateAllTargetByMetricName(initialObject.getId().getMetricName(), savedData.getId().getMetricName(), getUserId());
				getModel().deleteMetricsData(initialObject);
				//call to log audit
				AuditLoggerUtil.logAuditInfo(initialObject, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getDialog().getScreenName(), getUserId());
				cascadeUpdateRespLevelMetric(initialObject, savedData);
				getDialog().setQiReportingMetric(savedData);
			}
			((Stage) getDialog().getSaveButton().getScene().getWindow()).close();
		} catch (Exception e) {
			handleException("An error occured in Metric update method ", "Failed To Update Metric" + (StringUtils.isBlank(e.getMessage()) ? "" : ":\n" + e.getMessage()), e);
		}
	}

	private boolean checkRespLevelFormat(String level, String metricName, String metricDataFormat) {
		final String parentLevel;
		if (QiConstant.TARGET_RESPONSIBLE_LEVEL_2.equals(level)) {
			parentLevel = QiConstant.TARGET_RESPONSIBLE_LEVEL_1;
		} else if (QiConstant.TARGET_RESPONSIBLE_LEVEL_3.equals(level)) {
			parentLevel = QiConstant.TARGET_RESPONSIBLE_LEVEL_2;
		} else {
			return true;
		}
		QiReportingMetric parentMetric = getModel().findByMetricId(new QiReportingMetricId(metricName, parentLevel));
		if (parentMetric == null) {
			throw new TaskException("No " + parentLevel + " metric exists.  Please set up the " + parentLevel + " metric first.");
		} else if (!parentMetric.getMetricDataFormat().equals(metricDataFormat)) {
			throw new TaskException("Mismatched metric data format.  " + level + " metric data format must match the " + parentLevel + " metric data format.");
		}
		return true;
	}

	private void cascadeUpdateRespLevelMetric(QiReportingMetric oldMetric, QiReportingMetric modifiedMetric) {
		if (!modifiedMetric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_1) && !modifiedMetric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_2))
			return;
		if (oldMetric.getId().getLevel().equals(QiConstant.TARGET_RESPONSIBLE_LEVEL_1)) {
			QiReportingMetric oldRespLevel2Metric = getModel().findByMetricId(new QiReportingMetricId(oldMetric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_2));
			if (oldRespLevel2Metric != null)
				cascadeUpdateRespLevel(modifiedMetric, oldRespLevel2Metric);
		}
		QiReportingMetric oldRespLevel3Metric = getModel().findByMetricId(new QiReportingMetricId(oldMetric.getId().getMetricName(), QiConstant.TARGET_RESPONSIBLE_LEVEL_3));
		if (oldRespLevel3Metric != null)
			cascadeUpdateRespLevel(modifiedMetric, oldRespLevel3Metric);
	}

	private void cascadeUpdateRespLevel(QiReportingMetric parentMetric, QiReportingMetric oldMetric) {
		QiReportingMetric modifiedMetric = new QiReportingMetric();
		modifiedMetric.setId(new QiReportingMetricId(parentMetric.getId().getMetricName(), oldMetric.getId().getLevel()));
		modifiedMetric.setMetricDescription(oldMetric.getMetricDescription());
		modifiedMetric.setMetricDataFormat(parentMetric.getMetricDataFormat());
		modifiedMetric.setCreateUser(getUserId());
		if (parentMetric.getId().getMetricName().equals(oldMetric.getId().getMetricName())) {
			getModel().updateMetricsData(modifiedMetric);
			AuditLoggerUtil.logAuditInfo(oldMetric, modifiedMetric, "Metric has been updated", getDialog().getScreenName(), getUserId());
		} else {
			getModel().saveReportingMetric(modifiedMetric);
			getModel().updateAllTargetByMetricName(oldMetric.getId().getMetricName(), modifiedMetric.getId().getMetricName(), getUserId());
			getModel().deleteMetricsData(oldMetric);
			AuditLoggerUtil.logAuditInfo(oldMetric, null, QiConstant.UPDATE_REASON_FOR_AUDIT, getDialog().getScreenName(), getUserId());
		}
	}

	/**
	 * When user clicks on close button in the popup screen closeBtnAction
	 * method gets called.
	 */
	private void cancelButtonAction() {
		LoggedButton cancelButton = getDialog().getCancelButton();
		try {
			Stage stage = (Stage) cancelButton.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured during cancel action ", "Failed to perform cancel action", e);
		}
	}

	/**
	 * This method will be used to prepare {@link QiReportingMetric} object.
	 * 
	 * @return qiReportingMetric
	 */
	private QiReportingMetric prepareQiReportingMetricObject() {
		QiReportingMetric qiReportingMetric = new QiReportingMetric();
		String metricName = getDialog().getMetricNameTextField().getControl().getText().trim();
		String level = getDialog().getLevel();
		String metricDataFormat = getDialog().getMetricDataFormatComboBox().getControl().getSelectionModel().getSelectedItem();
		if (level.equals(QiConstant.RESPONSIBLE_LEVEL_1)) {
			level = QiConstant.TARGET_RESPONSIBLE_LEVEL_1;
		} else if (level.equals(QiConstant.RESPONSIBLE_LEVEL_2)) {
			level = QiConstant.TARGET_RESPONSIBLE_LEVEL_2;
		} else if (level.equals(QiConstant.RESPONSIBLE_LEVEL_3)) {
			level = QiConstant.TARGET_RESPONSIBLE_LEVEL_3;
		}
		checkRespLevelFormat(level, metricName, metricDataFormat);
		qiReportingMetric.setId(new QiReportingMetricId(metricName, level));
		String metricDesc = getDialog().getMetricDescTextField().getText();
		qiReportingMetric.setMetricDescription(metricDesc != null ? metricDesc.trim() : null);
		qiReportingMetric.setMetricDataFormat(metricDataFormat);
		qiReportingMetric.setCreateUser(getUserId());

		return qiReportingMetric;
	}

	@Override
	public void initListeners() {
		metricNameTextFieldChangeListener();
		metricDescTextFieldChangeListener();
		metricFormatComboboxChangeListener();
	}

	/**
	 * This method gets called when the user enters any metric name in text
	 * field
	 * 
	 */
	private void metricNameTextFieldChangeListener() {
		getDialog().getMetricNameTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.length() > METRIC_NAME_LENGTH) {
					getDialog().getMetricNameTextField().setText(newValue.substring(0, METRIC_NAME_LENGTH).toUpperCase());
					getDialog().getMetricNameTextField().getControl().positionCaret(newValue.length());
				} else {
					getDialog().getMetricNameTextField().setText(newValue.toUpperCase());
				}
				enableSaveButton();
			}
		});
	}

	/**
	 * This method gets called when the user enters any metric description in text
	 * field
	 * 
	 */
	private void metricDescTextFieldChangeListener() {
		getDialog().getMetricDescTextField().getControl().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				enableSaveButton();
			}
		});
	}

	/**
	 * This method gets called when the user changes metric format.
	 * 
	 */
	private void metricFormatComboboxChangeListener() {
		getDialog().getMetricDataFormatComboBox().getControl().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!oldValue.equals(newValue))
					enableSaveButton();
			}
		});
	}

	/**
	 * This method checks the condition to enable the save button
	 */
	private void enableSaveButton() {
		if (StringUtils.isBlank(getDialog().getMetricNameTextField().getText())) {
			getDialog().getSaveButton().setDisable(true);
		} else
			getDialog().getSaveButton().setDisable(false);
	}

}
