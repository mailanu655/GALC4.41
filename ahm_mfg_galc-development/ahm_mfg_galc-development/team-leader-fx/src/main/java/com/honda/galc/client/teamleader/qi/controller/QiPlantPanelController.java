package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.DataValidationDialog;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.teamleader.qi.view.QiDepartmentPanel;
import com.honda.galc.client.teamleader.qi.view.QiResponsibilityPanelDialog;
import com.honda.galc.client.teamleader.qi.view.QiPlantPanel;
import com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiPlantPanelController</code> is the controller class for Plant Panel.
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
 * <TD>20/10/2016</TD>
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

public class QiPlantPanelController<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanelController<QiResponsibilityAssignmentModel, QiPlantPanel<V>, V> implements EventHandler<ActionEvent>  {


	public QiPlantPanelController(QiResponsibilityAssignmentModel model, QiPlantPanel<V> panel,V view) {
		super();
		setModel(model);
		setPanel(panel);
		setView(view);
	}

	@Override
	public void initListeners() {
		
		boolean setUpdateEnablerOn = getView() instanceof QiRegionalResponsibilityAssignmentView;
		
		getPanel().getPlantNameText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PLANT_NAME_TEXT_BOX_LEN));
		getPanel().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PLANT_DESC_TEXT_AREA_LEN));
		getPanel().getPddaPlantCodeText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PDDA_PLANT_CODE_TEXT_BOX_LEN));
		getPanel().getEntrySiteText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PLANT_ENTRY_SITE_TEXT_BOX_LEN));
		getPanel().getEntryPlantText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PLANT_ENTRY_PLANT_TEXT_BOX_LEN));
		getPanel().getProductLineNoText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PLANT_PRODUCT_LINE_NO_TEXT_BOX_LEN));
		getPanel().getPddaLineText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.PDDA_LINE_TEXT_BOX_LEN));
		setTextFieldListener(getPanel().getPlantNameText());
		setTextFieldListener(getPanel().getPddaPlantCodeText());
		setTextFieldListener(getPanel().getEntrySiteText());
		setTextFieldListener(getPanel().getEntryPlantText());
		setTextFieldListener(getPanel().getProductLineNoText());
		setTextFieldListener(getPanel().getPddaLineText());
		addFieldListener(getPanel().getPlantNameText(), true, setUpdateEnablerOn, true);
		addFieldListener(getPanel().getPddaPlantCodeText(), true, setUpdateEnablerOn, true);
		addFieldListener(getPanel().getEntrySiteText(), true, setUpdateEnablerOn, true);
		addFieldListener(getPanel().getEntryPlantText(), true, setUpdateEnablerOn, true);
		addFieldListener(getPanel().getProductLineNoText(),false, setUpdateEnablerOn, true);
		addFieldListener(getPanel().getPddaLineText(),true, setUpdateEnablerOn, true);
		loadProductKindComboBox();
		
		if(setUpdateEnablerOn){
			addStatusChangeListener();
			addDescAreaChangeListener();
			addCommonComboboxChangeListener(getPanel().getProudctKindComboBox());
		}
		
	}

	/** This method will populate the productKind Combo box on mouse click and handle the change events on the combo box.
	 * 
	 */
	private void loadProductKindComboBox() {

		getPanel().getProudctKindComboBox().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			}});

		getPanel().getProudctKindComboBox().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				try {
					List<String> list = getModel().findAllProductKindList();
					String sel= getPanel().getProudctKindComboBox().getValue();
					getPanel().getProudctKindComboBox().getItems().clear();
					List<String> list1=new ArrayList<String>();

					for(String productType:list){
						list1.add(StringUtils.trim(productType.toString()));
					}
					getPanel().getProudctKindComboBox().getItems().addAll(list1);
					getPanel().getProudctKindComboBox().setValue(sel);
				} catch (Exception e) {
					displayErrorMessage("Failed to load Product Kind ComboBox", "Failed to load Product Kind ComboBox");
				}
			}
		});
	}

	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (isFullAccess()) {
				LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

				if (QiConstant.SAVE.equalsIgnoreCase(loggedButton.getText()))
					savePlant(actionEvent);
				else if (QiConstant.CANCEL.equalsIgnoreCase(loggedButton.getText())) {
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				} else if (QiConstant.UPDATE.equalsIgnoreCase(loggedButton.getText()))
					updatePlant();
				else if ("Create Department".equalsIgnoreCase(loggedButton.getText()))
					openDepartmentDialog(actionEvent);

			} else
				displayUnauthorizedOperationMessage();
		}
	}

	/** This method will open Department dialog for creation of new department
	 * 
	 * @param actionEvent
	 */
	private void openDepartmentDialog(ActionEvent actionEvent) {
		if (null !=getTree().getSelectionModel().getSelectedItem()) {
			QiResponsibilityPanelDialog<QiDepartmentPanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiDepartmentPanel<V>,V>("Create Department",getView().getMainWindow(), getView(), getModel(), QiConstant.DEPARTMENT_NODE,getApplicationId());
			dialog.show();
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
			QiPlant plant=getQiPlant();
			if (plant.getActive() == 0) {
				dialog.getPanel().getActiveRadioBtn().setDisable(true);
				dialog.getPanel().getInactiveRadioBtn().setSelected(true);
			}
		}
		else{
			displayErrorMessage("Please select Plant.", "Please select Plant.");
		}
	}

	/** This method will be used to create new plant
	 * 
	 * @param actionEvent
	 */
	private void savePlant(ActionEvent actionEvent) {
		if(checkMandatoryFields(false)){
			clearDisplayMessage();
			QiPlant newPlant=new QiPlant();
			newPlant.setId(new QiPlantId(getTree().getSelectionModel().getSelectedItem().getValue().split("- ") [1],
					StringUtils.trimToNull(getPanel().getPlantNameText().getText())));
			newPlant.setPlantDesc(StringUtils.trimToNull(getPanel().getDescriptionTextArea().getText()));
			newPlant.setPddaPlantCode(StringUtils.trimToNull(getPanel().getPddaPlantCodeText().getText()));
			newPlant.setProductKind(StringUtils.trimToEmpty(getPanel().getProudctKindComboBox().getSelectionModel().getSelectedItem().toString()));
			newPlant.setEntrySite(StringUtils.trimToNull(getPanel().getEntrySiteText().getText()));
			newPlant.setEntryPlant(StringUtils.trimToNull(getPanel().getEntryPlantText().getText()));
			String productLine = StringUtils.trimToEmpty(getPanel().getProductLineNoText().getText());
			newPlant.setProductLineNo(Short.parseShort((StringUtils.defaultIfEmpty(productLine, "0"))));
			newPlant.setPddaLine(StringUtils.trimToNull(getPanel().getPddaLineText().getText()));
			newPlant.setActive(getStatus());
			newPlant.setCreateUser(getUserId());
			try {
				if (getModel().findPlantById(newPlant.getId()) != null) {
					EventBusUtil.publish(new StatusMessageEvent(newPlant.getId().getPlant() + " is already exist.", StatusMessageEventType.DIALOG_ERROR));
				}
				else{
					getModel().savePlant(newPlant);
					updateTree(newPlant.getId().getPlant());
					LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
					EventBusUtil.publish(new StatusMessageEvent("Plant saved successfully.", StatusMessageEventType.INFO));
				}
			} catch (Exception e) {
				displayErrorMessageForDialog("Problem occured while saving " + newPlant.getId().getPlant() + " plant.",
						"Problem occured while saving " + newPlant.getId().getPlant() + " plant.");
			}
		}
	}


	/** This method will be used to update the plant information
	 * 
	 * @param actionEvent
	 */
	private void updatePlant() {

		QiPlant plant=getQiPlant();
		if (plant == null) {
			displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
			return;
		}
		plant=getFormatedPlant(plant);
		
		int wasActive=plant.getActive();
		int isActive=getStatus();
		clearDisplayMessage();
		if(checkMandatoryFields(true)){
			List<String> allAssignedPlantsList = getModel().findAllAssignedPlants();
			if((wasActive==1 && isActive==0)){
				if(allAssignedPlantsList.contains(StringUtils.trim(getPanel().getPlantNameText().getText()))){
						MessageDialog.showError(getView().getStage(), QiConstant.CANNOT_BE_INACTIVATED_MSG);
						return;
				}
				List<QiLocalDefectCombination> localDefectList = getModel().findAllBySiteAndPlant(plant.getId().getSite(), getPanel().getPlantNameText().getText());
				if(localDefectList.size() > 0) {
					StringBuilder errorMsg = new StringBuilder("Plant being updated affects ");
					errorMsg.append(localDefectList.size()+" local (s). Hence, Inactivation of this plant is not allowed.");
					MessageDialog.showError(getView().getStage(),errorMsg.toString());
					return ;
				}
			}
			QiPlant newPlant=(QiPlant)plant.deepCopy();
			newPlant.getId().setPlant(StringUtils.trim(getPanel().getPlantNameText().getText()));
			newPlant.setPlantDesc(getPanel().getDescriptionTextArea().getText());
			newPlant.setPddaPlantCode(getPanel().getPddaPlantCodeText().getText());
			newPlant.setProductKind(getPanel().getProudctKindComboBox().getSelectionModel().getSelectedItem().toString());
			newPlant.setEntrySite(getPanel().getEntrySiteText().getText());
			newPlant.setEntryPlant(getPanel().getEntryPlantText().getText());
			String productLine =StringUtils.trimToEmpty(getPanel().getProductLineNoText().getText());
			newPlant.setProductLineNo(Short.parseShort(StringUtils.defaultIfEmpty(productLine, "0")));
			newPlant.setPddaLine(getPanel().getPddaLineText().getText());
			newPlant.setActive(getStatus());
			
			
			if (newPlant.equals(plant)) {
				displayErrorMessage("No change detected.", "No change detected.");
			} else {
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				if (dialog.showReasonForChangeDialog(null)) {
					if (!getPanel().getPlantNameText().getText().equals(plant.getId().getPlant().trim()) && checkForExistingPlant(newPlant))
						displayErrorMessage(newPlant.getId().getPlant() + " is already exist.", newPlant.getId().getPlant() + " is already exist.");
					else{
						try {
							if(wasActive==1 && isActive==0){
								String retValue=isLocalSiteImpacted(plant.getId().getPlant(),plant.getId().getSite());
								if(retValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED))
									return;
								else if(retValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
									displayErrorMessage("Local Site Impacted", "Inactivation of site impacts Local Site(s) data.");
									return;
								}
							}
							if (isUpdated(getEntity())) {
								return;
							}
							getModel().updatePlant(newPlant, plant.getId().getPlant());
							// call to prepare and insert audit data
							AuditLoggerUtil.logAuditInfo(plant, newPlant, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
							if (!plant.getId().equals(newPlant.getId())) {
								
								ArrayList<QiDepartment> deptList =new ArrayList<QiDepartment>();
								deptList.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getDepartmentListByPlant(plant));

								if(!deptList.isEmpty())
									for(QiDepartment dept:deptList){
										ArrayList<QiResponsibleLevel> respList = new ArrayList<QiResponsibleLevel>();
										respList.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getResponsibleLevelListByDepartment(dept));

										getModel().updatePlantForDept(getUserId(), newPlant.getId().getPlant(), dept.getId());

										if(!respList.isEmpty())
											for (QiResponsibleLevel resp : respList) {
												getModel().updatePlantForResponsibleLevel(newPlant.getId().getPlant(), getUserId(), Integer.valueOf(resp.getId().toString()));
											}
									}
							}
							EventBusUtil.publish(new StatusMessageEvent("Plant details updated successfully.", StatusMessageEventType.INFO));
							updateTree(newPlant.getId().getPlant(),plant.getActive());
							setEntity(getModel().find(getEntity()));
							plant=newPlant;
						} catch (Exception e) {
							displayErrorMessage("Updation of " + newPlant.getId().getPlant() + "failed.", "Updation of " + newPlant.getId().getPlant() + " failed.");
						}
					}
				} else {
					return;
				}
			}
		} 
	}


	/**
	 * This method will reflect the create operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 * @param operation
	 */
	private void updateTree(String node) {
		TreeItem<String> plantNode=null;
		if(getStatus() ==  0)
			plantNode= new TreeItem<String>(QiConstant.PLANT_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else if(getStatus() ==  1)
			plantNode= new TreeItem<String>(QiConstant.PLANT_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
		getTree().getSelectionModel().getSelectedItem().getChildren().add(plantNode);
		getTree().getSelectionModel().select(plantNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
	}

	/**
	 * This method will reflect the update operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param companyName
	 * @param status
	 */
	private void updateTree(String node, int status) {
		
		getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.PLANT_NODE + "- " + node);		
		QiPlant plant=getQiPlant();
		
		if (getStatus() == 0 && status != getStatus()) {
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateDepartmentByPlant(plant.getId());
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateChilds(getTree().getSelectionModel().getSelectedItem());
		}
		else if(getStatus() ==  1 && status != getStatus())
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
	}

	/**
	 * This method is used to populate fields on the plant panel on initial
	 * load on selection of tree node on QiResponsibilityAssignmentView screen.
	 * 
	 */
	public void populateData() {
		try {
			QiPlant plant=getQiPlant();
			setEntity(plant);
			getPanel().getPlantNameText().settext(plant.getId().getPlant());
			getPanel().getDescriptionTextArea().setText(plant.getPlantDesc());
			getPanel().getPddaPlantCodeText().settext(plant.getPddaPlantCode());
			getPanel().getProudctKindComboBox().setValue(plant.getProductKind().toString());
			getPanel().getEntrySiteText().setText(plant.getEntrySite());
			getPanel().getEntryPlantText().setText(plant.getEntryPlant());
			getPanel().getProductLineNoText().settext(new Integer(plant.getProductLineNo()).toString());
			getPanel().getPddaLineText().setText(plant.getPddaLine());
			if (plant.isActive())
				getPanel().getActiveRadioBtn().setSelected(true);
			else
				getPanel().getInactiveRadioBtn().setSelected(true);

			QiSite parentSite= getModel().findSiteById(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ") [1].toString());
			if(parentSite.getActive()==0){
				getPanel().getActiveRadioBtn().setDisable(true);
			}

			getPanel().getPanelButton().setDisable(true);
			
			
		} catch (Exception e) {
			displayErrorMessage("Failed to get plant details on tree node selection.", "Failed to load the Plant details.");
		}
	}

	/**
	 * This method is used to check the mandatory fields on the plant panel. The
	 * method will return true only if all mandatory fields have certain input.
	 * 
	 */
	@Override
	protected boolean checkMandatoryFields(boolean isPanel) {

		if (isPanel) {
			if (getPanel().getPlantNameText().getText() == null || getPanel().getPlantNameText().getText().isEmpty()) {
				setErrorMessage("Please enter plant name.");
				return false;
			}

			else if (getPanel().getProudctKindComboBox().getValue() == null || getPanel().getProudctKindComboBox().getValue().isEmpty()) {
				setErrorMessage("Please select product kind.");
				return false;
			}
		} else {
			if (getPanel().getPlantNameText().getText() == null || getPanel().getPlantNameText().getText().isEmpty()) {
				displayErrorMessageForDialog("Please enter plant name.");
				return false;
			}

			else if (getPanel().getProudctKindComboBox().getValue() == null || getPanel().getProudctKindComboBox().getValue().isEmpty()) {
				displayErrorMessageForDialog("Please select product kind.");
				return false;
			}
		}
		return true;
	}

	/** This method will return the entity of QiPlant based on the plant selected on screen.
	 * 
	 * @return
	 */
	private QiPlant getQiPlant() {
		return getModel().findPlantById(new QiPlantId(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ") [1],getTree().getSelectionModel().getSelectedItem().getValue().split("- ") [1]));
	}

	/**  Check whether the plant already exist in database.
	 * 
	 * @param newPlant
	 * @return boolean
	 */
	private boolean checkForExistingPlant(QiPlant newPlant) {
		if (getModel().findPlantById(newPlant.getId()) != null) {
			return true;
		} else {
			return false;
		}
	}

	/** This method will return plant object after removing trailing white spaces from pdda plant code and product kind.
	 * @param plant
	 * @return
	 */
	private QiPlant getFormatedPlant(QiPlant plant){
		plant.setPddaPlantCode(plant.getPddaPlantCode());
		plant.setProductKind(plant.getProductKind());
		plant.setEntrySite(plant.getEntrySite());
		plant.setEntryPlant(plant.getEntryPlant());
		return plant;

	}
	
	/**
	 * Displays local site validation and returns true if any site is impacted
	 */
	public String isLocalSiteImpacted(String plantName,String siteName) {
		List<String> plantlist=new ArrayList<String>();
		plantlist.add(plantName+","+siteName);
		Map<String,String> siteMap=PropertyService.getPropertyBean(QiPropertyBean.class).getSitesForValidation();
		if(siteMap==null || siteMap.isEmpty()){
			if(!(MessageDialog.confirm(getView().getStage(),"Local Site Data will not be validated as local site(s) are not configured. Do you still want to continue?")))
				return QiConstant.NO_LOCAL_SITES_CONFIGURED;
			else
				return "";
		}
		DataValidationDialog dataValidationDialog=new DataValidationDialog("Local Validation",getApplicationId(),getView().getScreenName()+"-Plant",plantlist);
		dataValidationDialog.showDialog();
		return dataValidationDialog.isLocalSiteImpact()?QiConstant.LOCAL_SITES_IMPACTED:"";
		
	}


	/** This method will return main views treeview after casting.
	 * 
	 * @return mainView Tree 
	 */
	private TreeView<String> getTree() {
		if(getView() instanceof QiRegionalResponsibilityAssignmentView){
			return ((QiRegionalResponsibilityAssignmentView)getView()).getTree();
		}
		else{
			return ((QiLocalResponsibilityAssignmentView)getView()).getTree();
		}

	}
}
