package com.honda.galc.client.teamlead.vios.platform;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.AbstractViosDialog;
import com.honda.galc.client.teamlead.vios.ViosConstants;
import com.honda.galc.client.teamlead.vios.ViosMasterValidator;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCViosMasterPlatformDao;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import static com.honda.galc.service.ServiceFactory.getDao;
/**
 * <h3>ViosPlatformDialog Class description</h3>
 * <p>
 * Dialog for Vios Platform
 * </p>
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
 * @author Hemant Kumar<br>
 *        Aug 28, 2018
 */
public class ViosPlatformDialog extends AbstractViosDialog {
	
	private MCViosMasterPlatform platform;
	private LabeledTextField plantTextField;
	private LabeledTextField deptTextField;
	private LabeledTextField modelYearTextField;
	private LabeledTextField prodRateTextField;
	private LabeledTextField lineNoTextField;
	private LabeledTextField vmcTextField;
	private CheckBox activeCheckbox;
	private CheckBox discardYearCheckbox;
	
	private LoggedButton addButton;
	private LoggedButton updateButton;
	private LoggedButton cancelButton;

	public ViosPlatformDialog(Stage stage, String action, MCViosMasterPlatform platform) {
		super(action + " VIOS Platform", stage);
		this.platform = platform;
		if(action.equals(ViosConstants.ADD)) {
			updateButton.setDisable(true);
		} else if(action.equals(ViosConstants.UPDATE)) {
			addButton.setDisable(true);
			loadUpdateData();
		}
	}

	@Override
	public Node getMainContainer() {
		VBox mainBox = new VBox();
		mainBox.setAlignment(Pos.CENTER);
		mainBox.setPadding(new Insets(20));
		mainBox.setSpacing(20);
		
		plantTextField = createLabeledTextField(ViosConstants.PLANT);
		deptTextField = createLabeledTextField(ViosConstants.DEPARTMENT);
		modelYearTextField = createLabeledTextField(ViosConstants.MODEL_YEAR);
		prodRateTextField = createLabeledTextField(ViosConstants.PRODUCTION_RATE);
		lineNoTextField = createLabeledTextField(ViosConstants.LINE_NUMBER);
		vmcTextField = createLabeledTextField(ViosConstants.VEHICLE_MODEL_CODE);
		activeCheckbox = new CheckBox("Active");
		activeCheckbox.setStyle("-fx-font-weight: bold;-fx-font-size: 14px;");
		
		discardYearCheckbox = new CheckBox("DiscardYear");
		discardYearCheckbox.setStyle("-fx-font-weight: bold;-fx-font-size: 14px;");
		HBox activeHBox = new HBox();
		activeHBox.setAlignment(Pos.CENTER_RIGHT);
		
		activeHBox.getChildren().addAll(activeCheckbox, discardYearCheckbox);
		
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setPadding(new Insets(10));
		buttonBox.setSpacing(10);
		
		addButton = createBtn(ViosConstants.ADD);
		updateButton = createBtn(ViosConstants.UPDATE);
		cancelButton = createBtn(ViosConstants.CANCEL);
		
		buttonBox.getChildren().addAll(addButton, updateButton, cancelButton);
		
		mainBox.getChildren().addAll(activeHBox, plantTextField, deptTextField, modelYearTextField, 
				prodRateTextField, lineNoTextField, vmcTextField, buttonBox);
		return mainBox;
	}

	@Override
	public void initHandler() {
		buttonActionHandler();
		textFieldListener();
	}
	
	private void textFieldListener() {
		plantTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(1));
		deptTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(2));
		modelYearTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(6));
		modelYearTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	clearErrorMessage();
		        if(!newValue.matches(ViosConstants.DECIMAL_REGEX)) {
		        	modelYearTextField.setText(oldValue);
		        }
		    }
		});
		prodRateTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(6));
		prodRateTextField.getControl().textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	clearErrorMessage();
		        if(!newValue.matches(ViosConstants.DECIMAL_REGEX)) {
		        	prodRateTextField.setText(oldValue);
		        }
		    }
		});
		lineNoTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(1));
		vmcTextField.getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(1));
	}

	private void buttonActionHandler() {
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					MCViosMasterPlatform entity = getViosPlatformObj();
					String statusMsg = ViosMasterValidator.masterPlatformValidate(entity);
					if(StringUtils.isNotBlank(statusMsg)) {
						setErrorMessage(statusMsg);
						return ;
					}
					if(getDao(MCViosMasterPlatformDao.class).findByKey(entity.getViosPlatformId()) != null) {
						setErrorMessage("Platform already exists");
					}
					entity.setUserId(getUserId());
					getDao(MCViosMasterPlatformDao.class).insert(entity);
					Stage stage = (Stage) addButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while inserting MCViosMasterPlatform object"));
					setErrorMessage("Something went wrong while adding Platform!");
				}
			}
		});
		
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					MCViosMasterPlatform entity = getViosPlatformObj();
					
					entity.setUserId(getUserId());
					String statusMsg = ViosMasterValidator.masterPlatformValidate(entity);
					if(StringUtils.isNotBlank(statusMsg)) {
						setErrorMessage(statusMsg);
						return ;
					}
					if(!isPlatformUpdated(entity)) {
						if(!(platform.isActive() == entity.isActive() && platform.isDiscardYear() == entity.isDiscardYear())) {
							entity.setUserId(getUserId());
							getDao(MCViosMasterPlatformDao.class).save(entity);
							Stage stage = (Stage) updateButton.getScene().getWindow();
							stage.close();
						}
						setErrorMessage("There is no change to update");
						return;
					}
					if(getDao(MCViosMasterPlatformDao.class).findByKey(entity.getViosPlatformId()) != null) {
						setErrorMessage("Platform already exists");
						return;
					}
					platform.setUserId(getUserId());
					getDao(MCViosMasterPlatformDao.class).removeAndInsert(platform, entity);
					Stage stage = (Stage) updateButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					Logger.getLogger().error(e, new LogRecord("An exception occured while saving MCViosMasterPlatform object"));
					setErrorMessage("Something went wrong while updating Platform!");
				}
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelButton.getScene().getWindow();
				stage.close();
			}
		});
	}
	

	@Override
	public void loadData() {
		
	}
	
	private void loadUpdateData() {
		if(platform != null) {
			plantTextField.setText(platform.getPlantLocCode());
			deptTextField.setText(platform.getDeptCode());
			modelYearTextField.setText(platform.getModelYearDate().toPlainString());
			prodRateTextField.setText(platform.getProdSchQty().toPlainString());
			lineNoTextField.setText(platform.getProdAsmLineNo());
			vmcTextField.setText(platform.getVehicleModelCode());
			activeCheckbox.setSelected(platform.isActive());
			discardYearCheckbox.setSelected(platform.isDiscardYear());
		}
	}
	
	private MCViosMasterPlatform getViosPlatformObj() {
		@SuppressWarnings("unused")
		BigDecimal modelYearCode = null;
		@SuppressWarnings("unused")
		BigDecimal prodYear = null;
		boolean isActive = false;
		isActive  = activeCheckbox.isSelected();
		try {
			modelYearCode = new BigDecimal(modelYearTextField.getText());
			prodYear =	new BigDecimal(prodRateTextField.getText());
		} catch(NumberFormatException e){
			modelYearCode =  new BigDecimal(0.0);
			prodYear = new BigDecimal(0.0);
		}
				MCViosMasterPlatform entity = new MCViosMasterPlatform(plantTextField.getText(), 
				deptTextField.getText(), modelYearCode, prodYear, 
				lineNoTextField.getText(), vmcTextField.getText(), activeCheckbox.isSelected(), discardYearCheckbox.isSelected());
				entity.setUserId(getUserId());
		return entity;
	}
	
	private boolean isPlatformUpdated(MCViosMasterPlatform newPlatform) {
		if(platform != null) {
			return !(newPlatform.getPlantLocCode().equals(platform.getPlantLocCode())
					&& newPlatform.getDeptCode().equals(platform.getDeptCode())
					&& newPlatform.getModelYearDate().floatValue() == platform.getModelYearDate().floatValue()
					&& newPlatform.getProdSchQty().floatValue() == platform.getProdSchQty().floatValue()
					&& newPlatform.getProdAsmLineNo().equals(platform.getProdAsmLineNo())
					&& newPlatform.getVehicleModelCode().equals(platform.getVehicleModelCode()));
		}
		return false;
	}
}
