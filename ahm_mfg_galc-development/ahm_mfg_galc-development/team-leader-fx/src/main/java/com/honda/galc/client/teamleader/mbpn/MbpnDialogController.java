package com.honda.galc.client.teamleader.mbpn;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.mvc.AbstractDialogController;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MbpnDialogController </code> is the Dialog Controller class for Mbpn.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @author L&T Infotech
 */

public class MbpnDialogController extends AbstractDialogController<MbpnModel,MbpnDialog>{ 

	private Mbpn mbpn;
	private static final String CREATE_MBPN = "createMbpn";
	private Mbpn mbpnCloned;

	public MbpnDialogController(MbpnModel model, MbpnDialog dialog, Mbpn mbpn) {
		super();
		setModel(model);
		setDialog(dialog);
		this.mbpn = mbpn;
		//added for Audit Logging
		this.mbpnCloned =(Mbpn) mbpn.deepCopy();
	}
	
	public void initListeners() {
		addComboBoxTextListener();
		getDialog().getMainNoCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(5));
		getDialog().getClassNoCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(3));
		getDialog().getProtoTypeCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(1));
		getDialog().getTypeNoCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(4));
		getDialog().getSupplementaryNoCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(2));
		getDialog().getTargetNoCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(2));
		getDialog().getHesColorCombobox().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(11));
		getDialog().getDescriptionTextField().addEventFilter(KeyEvent.KEY_TYPED , QiCommonUtil.restrictLengthOfTextFields(128));
		setComboBoxListener(getDialog().getMainNoCombobox());
		setComboBoxListener(getDialog().getClassNoCombobox());
		setComboBoxListener(getDialog().getProtoTypeCombobox());
		setComboBoxListener(getDialog().getTypeNoCombobox());
		setComboBoxListener(getDialog().getSupplementaryNoCombobox());
		setComboBoxListener(getDialog().getTargetNoCombobox());
		setComboBoxListener(getDialog().getHesColorCombobox());
	}
	
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource().equals(getDialog().getCreateBtn())) createBtnAction(event);
		else if(event.getSource().equals(getDialog().getUpdateBtn())) updateBtnAction(event);
		else if(event.getSource().equals(getDialog().getCancelBtn())) cancelBtnAction(event);
	}

	/**
	 * this method is used to create mbpn and productSpecCode
	 * @param event
	 */
	private void createBtnAction(ActionEvent event){

		/** Mandatory Check for Main No */
		if(StringUtils.isBlank(((String)getDialog().getMainNoCombobox().getSelectionModel().getSelectedItem()))){
			displayErrorMessage("Mandatory field is empty", "Please enter Main No");
			return;
		}
		/** Mandatory Check for Class No */
		else if(StringUtils.isBlank(((String)getDialog().getClassNoCombobox().getSelectionModel().getSelectedItem()))){
			displayErrorMessage("Mandatory field is empty", "Please enter Class No");
			return;
		}
		try{
			setValue(CREATE_MBPN,StringUtils.EMPTY);
		}
		catch (Exception e) {
			handleException("An error occured in createBtnAction method " , "Failed to create mbpn Data ", e);
		}
	}

	/**
	 * this method is used to update the existing mbpn and productSpecCode
	 * @param event
	 */
	private void updateBtnAction(ActionEvent event){

		String oldProductSpecCode = mbpn.getProductSpecCode();

		/** Mandatory Check for Main No */
		if(StringUtils.isBlank(((String)getDialog().getMainNoCombobox().getSelectionModel().getSelectedItem()))){
			displayErrorMessage("Mandatory field is empty", "Please enter Main No");
			return;
		}
		/** Mandatory Check for Class No */
		else if(StringUtils.isBlank(((String)getDialog().getClassNoCombobox().getSelectionModel().getSelectedItem()))){
			displayErrorMessage("Mandatory field is empty", "Please enter Class No");
			return;
		}

		/** Mandatory Check for Reason for Change */
		else if(QiCommonUtil.isMandatoryFieldEmpty(getDialog().getReasonForChangeTextArea())){
			displayErrorMessage("Mandatory field is empty", "Please enter Reason for Change");
			return;
		}
		try{
			setValue(StringUtils.EMPTY,oldProductSpecCode);
		}
		catch (Exception e) {
			handleException("An error occured in updateBtnAction method " , "Failed to update mbpn Data ", e);
		}
	}

	/**
	 * This method is used to set Value inside entity to create/update
	 * @param action
	 * @param oldProductSpecCode
	 */
	private void setValue(String action, String oldProductSpecCode) {
		String description = getDescriptionTextFieldValue();
		String oldDesc = mbpn.getDescription().trim();
		mbpn.setMainNo(getMainNumberValue());
		mbpn.setClassNo(getClassNumberValue());
		mbpn.setPrototypeCode(getProtoTypeValue());
		mbpn.setTypeNo(getTypeNumberValue());
		mbpn.setSupplementaryNo(getSupplementaryNumberValue());
		mbpn.setTargetNo(getTargetNumberValue());
		mbpn.setHesColor(getHesColorValue());
		mbpn.setDescription(StringUtils.trimToEmpty(description));
		mbpn.setMbpn(getMbpn());
		mbpn.setProductSpecCode(mbpn.getMbpn() + mbpn.getHesColor());
		
		if(action.isEmpty()){
			if(getModel().isProductSpecCodeExists(mbpn.getProductSpecCode()) && description.equals(oldDesc)){
				displayErrorMessage("Failed to update Product Spec Code as the Product Spec Code name " + mbpn.getProductSpecCode() + " already exists!", "MBPN " + mbpn.getProductSpecCode() + " already exists!");
				return;
			}
			getModel().updateMbpnData(mbpn, oldProductSpecCode);
			//call to prepare and insert audit data
			AuditLoggerUtil.logAuditInfo(mbpnCloned, mbpn, getDialog().getReasonForChangeTextArea().getText(), getDialog().getScreenName(),getModel().getUserId());
			Stage stage= (Stage) getDialog().getUpdateBtn().getScene().getWindow();
			stage.close();
		}
		else{
			if(getModel().isProductSpecCodeExists(mbpn.getProductSpecCode()))
				displayErrorMessage("Failed to add new Product Spec Code as the Product Spec Code name " + mbpn.getProductSpecCode() + " already exists!", "MBPN " + mbpn.getProductSpecCode() + " already exists!");
			else{
				getModel().createMbpnData(mbpn);
				Stage stage = (Stage) getDialog().getCreateBtn().getScene().getWindow();
				stage.close();
			}
		}
	}
	
	/**
	 * Gets the main number value.
	 *
	 * @return the main number value
	 */
	private String getMainNumberValue() {
		return getDialog().getMainNoCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getMainNoCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the class number value.
	 *
	 * @return the class number value
	 */
	private String getClassNumberValue() {
		return getDialog().getClassNoCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getClassNoCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the proto type value.
	 *
	 * @return the proto type value
	 */
	private String getProtoTypeValue() {
		return getDialog().getProtoTypeCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getProtoTypeCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the type number value.
	 *
	 * @return the type number value
	 */
	private String getTypeNumberValue() {
		return getDialog().getTypeNoCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getTypeNoCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the supplementary number value.
	 *
	 * @return the supplementary number value
	 */
	private String getSupplementaryNumberValue() {
		return getDialog().getSupplementaryNoCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getSupplementaryNoCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the target number value.
	 *
	 * @return the target number value
	 */
	private String getTargetNumberValue() {
		return getDialog().getTargetNoCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getTargetNoCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the hes color value.
	 *
	 * @return the hes color value
	 */
	private String getHesColorValue() {
		return getDialog().getHesColorCombobox().getSelectionModel().getSelectedItem() == null ? QiConstant.EMPTY :
			getDialog().getHesColorCombobox().getSelectionModel().getSelectedItem().toString().toUpperCase();
	}
	
	/**
	 * Gets the description text field value.
	 *
	 * @return the description text field value
	 */
	private String getDescriptionTextFieldValue() {
		return getDialog().getDescriptionTextField().getText() == null ? QiConstant.EMPTY :
			getDialog().getDescriptionTextField().getText().toString();
	}
	
	/**
	 * Gets the mbpn.
	 *
	 * @return the mbpn
	 */
	private String getMbpn() {
		return StringUtil.padRight(mbpn.getMainNo(), MbpnDef.MAIN_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getClassNo(), MbpnDef.CLASS_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getPrototypeCode(), MbpnDef.PROTOTYPE_CODE.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getTypeNo(),MbpnDef.TYPE_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getSupplementaryNo(), MbpnDef.SUPPLEMENTARY_NO.getLength(), ' ', false)
				+ StringUtil.padRight(mbpn.getTargetNo(), MbpnDef.TARGET_NO.getLength(), ' ', false)
				+ " "; //additional space to the end of MBPN
	}

	/**
	 * this method is used to close the popup screen.
	 */
	private void cancelBtnAction(ActionEvent event){
		LoggedButton cancelBtn = getDialog().getCancelBtn();
		try {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		} catch (Exception e) {
			handleException("An error occured in during cancel action ", "Failed to perform cancel action", e);
		}

	}

	/**
	 * This method converts combobox text to uppercase
	 */
	private void addComboBoxTextListener(){
		addComboBoxChangeListener(getDialog().getMainNoCombobox());
		addComboBoxChangeListener(getDialog().getClassNoCombobox());
		addComboBoxChangeListener(getDialog().getSupplementaryNoCombobox());
		addComboBoxChangeListener(getDialog().getHesColorCombobox());
		addComboBoxChangeListener(getDialog().getTypeNoCombobox());
		addComboBoxChangeListener(getDialog().getTargetNoCombobox());
		addComboBoxChangeListener(getDialog().getProtoTypeCombobox());
		setUpperCaseListener(getDialog().getDescriptionTextField());
		getDialog().getDescriptionTextField().textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				enableUpdateButton();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void addComboBoxChangeListener(LoggedComboBox<String> combobox){
		setUpperCaseListener(combobox);
		combobox.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!StringUtils.isEmpty(oldValue) && !oldValue.equals(newValue)) {
					enableUpdateButton();
				}
			}
		});
		combobox.valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				enableUpdateButton();
			}
		});
	}
	
	private void enableUpdateButton() {
		getDialog().getUpdateBtn().setDisable(!getDialog().getTitle().contains(QiConstant.UPDATE));
	}
}
