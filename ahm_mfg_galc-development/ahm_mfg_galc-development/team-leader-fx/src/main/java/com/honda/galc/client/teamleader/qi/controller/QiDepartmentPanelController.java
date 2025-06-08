package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.teamleader.qi.view.QiDepartmentPanel;
import com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiResponsibilityPanelDialog;
import com.honda.galc.client.teamleader.qi.view.QiResponsibleLevelPanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiLocalDefectCombination;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.entity.qi.QiReportingTarget;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiDepartmentPanelController</code> is the controller class for Department Panel.
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
 * <TD>22/10/2016</TD>
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

public class QiDepartmentPanelController<V extends QiAbstractTabbedView<?,?>>  extends QiAbstractPanelController<QiResponsibilityAssignmentModel, QiDepartmentPanel<V>, V> implements EventHandler<ActionEvent>  {

	public QiDepartmentPanelController(QiResponsibilityAssignmentModel model, QiDepartmentPanel<V> panel,V view) {
		super();
		setModel(model);
		setPanel(panel);
		setView(view);
	}

	@Override
	public void initListeners() {

		boolean setUpdateEnablerOn = getView() instanceof QiRegionalResponsibilityAssignmentView;
		
		getPanel().getDepartmentAbbrText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.DEPARTMENT_ABBR_NAME_TEXT_BOX_LEN));
		getPanel().getDepartmentNameText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.DEPARTMENT_NAME_TEXT_BOX_LEN));
		getPanel().getPddaDeptCodeText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.DEPARTMENT_PDDA_CODE_TEXT_BOX_LEN));
		getPanel().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.DEPARTMENT_DESC_TEXT_AREA_LEN));
		setTextFieldListener(getPanel().getDepartmentNameText());
		addFieldListener(getPanel().getDepartmentNameText(), true, setUpdateEnablerOn, true);
		setTextFieldListener(getPanel().getDepartmentAbbrText());
		addFieldListener(getPanel().getDepartmentAbbrText(), true, setUpdateEnablerOn, true);
		setTextFieldListener(getPanel().getPddaDeptCodeText());
		addFieldListener(getPanel().getPddaDeptCodeText(), true, setUpdateEnablerOn, true);
		
		if(setUpdateEnablerOn){
			addStatusChangeListener();
			addDescAreaChangeListener();
		}
		
	}


	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if (isFullAccess()) {
				LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

				if (QiConstant.SAVE.equalsIgnoreCase(loggedButton.getText()))
					saveDepartment(actionEvent);
				else if (QiConstant.UPDATE.equalsIgnoreCase(loggedButton.getText()))
					updateDepartment();
				else if (QiConstant.CANCEL.equalsIgnoreCase(loggedButton.getText())) {
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				} else if ("Create Responsible Level".equalsIgnoreCase(loggedButton.getText()))
					openResponsibleLevelDialog(actionEvent);

			} else
				displayUnauthorizedOperationMessage();
		}
	}

	/** This method will be used to display a Responsible Level dialog.
	 * 
	 * @param actionEvent
	 */
	private void openResponsibleLevelDialog(ActionEvent actionEvent) {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			QiResponsibilityPanelDialog<QiResponsibleLevelPanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiResponsibleLevelPanel<V>,V>("Create Responsible Level", getView().getMainWindow(), getView(), getModel(), QiConstant.RESPONSIBLE_LEVEL3_NODE,getApplicationId());
			dialog.show();
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
			QiDepartment department=getQiDepartment();
			if(department.getActive()==0){
				dialog.getPanel().getActiveRadioBtn().setDisable(true);
				dialog.getPanel().getInactiveRadioBtn().setSelected(true);
			}
		}
		else{
			setErrorMessage("Please select Department in the Hierarchy.");
		}
	}

	/**  This method will be used to update the department details. 
	 * 
	 */
	private void updateDepartment() {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			clearDisplayMessage();
			QiDepartment existingDepartment = getQiDepartment();
			if (existingDepartment == null) {
				displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
				return;
			}
			existingDepartment = getFormatedDepartment(existingDepartment);
			if(checkMandatoryFields(true)){
				List<String> allAssignedDeptsList = getModel().findAllAssignedDepts();
				if(getPanel().getInactiveRadioBtn().isSelected() && getQiDepartment().isActive()){
					List<QiReportingTarget> reportingTargetList=getModel().findAllByDepartmentAndDate(getPanel().getDepartmentAbbrText().getText());
					if(allAssignedDeptsList.contains(getPanel().getDepartmentAbbrText().getText())){
						MessageDialog.showError(getView().getStage(), QiConstant.CANNOT_BE_INACTIVATED_MSG);
						return;
					}
				
					if(reportingTargetList.size()>0){
						StringBuilder errorMsg=new StringBuilder(" Target(s) assigned to this department for current or future date." +
								"Hence, Inactivation is not allowed.");
						MessageDialog.showError(getView().getStage(),errorMsg.toString());
						return ;
					}
					
					List<QiLocalDefectCombination> localDefectList = getModel().findAllBySitePlantAndDepartment(existingDepartment.getId().getSite(), 
							existingDepartment.getId().getPlant(), getPanel().getDepartmentAbbrText().getText());
					if(localDefectList.size() > 0) {
						StringBuilder errorMsg = new StringBuilder("Department being updated affects ");
						errorMsg.append(localDefectList.size()+" local (s). Hence, Inactivation of this plant is not allowed.");
						MessageDialog.showError(getView().getStage(),errorMsg.toString());
						return ;
					}
				}
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				String departmentAbbr = getPanel().getDepartmentAbbrText().getText();
				QiDepartment newDepartment =(QiDepartment)existingDepartment.deepCopy();
				newDepartment.setDepartmentName(getPanel().getDepartmentNameText().getText().trim());
				newDepartment.getId().setDepartment(departmentAbbr);
				newDepartment.setPddaDept(getPanel().getDepartmentCodeText().getText());
				newDepartment.setActive(getStatus());
				newDepartment.setDepartmentDescription(getPanel().getDescriptionTextArea().getText());
				if (existingDepartment.equals(newDepartment))
					displayErrorMessage("No change detected.","No change detected.");
				else {
					if (dialog.showReasonForChangeDialog(null)) {
						if (!departmentAbbr.equals(existingDepartment.getId().getDepartment().trim()) && checkForExistingDepartment(newDepartment))
							displayErrorMessage(" This department already exist.","This department already exist.");
						else{
							try {
								if (isUpdated(getEntity())) {
									return;
								}
								getModel().updateDepartment(existingDepartment.getId(), newDepartment);

								AuditLoggerUtil.logAuditInfo(existingDepartment, newDepartment, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
								if (!existingDepartment.getId().equals(newDepartment.getId())) {
									ArrayList<QiResponsibleLevel> respList = new ArrayList<QiResponsibleLevel>();
									respList.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getResponsibleLevelListByDepartment(existingDepartment));

									if(!respList.isEmpty())
										for (QiResponsibleLevel resp : respList) {
											getModel().updateDeptForResponsibleLevel(newDepartment.getId().getDepartment().trim(),
													getUserId(), Integer.valueOf(resp.getId().toString()));
										}
								}
								updateTree(departmentAbbr, existingDepartment.getActive());
								setEntity(getModel().find(getEntity()));
								EventBusUtil.publish(new StatusMessageEvent("Department details updated successfully.", StatusMessageEventType.INFO));
								existingDepartment= newDepartment;
							} catch (Exception e) {
								displayErrorMessage("Updation of " + departmentAbbr+ "failed.", "Updation of " + departmentAbbr+ " failed.");
							}
						}
					} else {
						return;
					}
				}
			} 
		}
		else{
			setErrorMessage("Please select Department in the Hierarchy.");
		}
	}

	/**  This method will be used to create new department. 
	 * 
	 */
	private void saveDepartment(ActionEvent actionEvent) {
		if (checkMandatoryFields(false)) {
			clearDisplayMessage();
			String site = getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1];
			String department = StringUtils.trimToNull(getPanel().getDepartmentAbbrText().getText());
			String plant = getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1];
			QiDepartment newDept = new QiDepartment();
			newDept.setId(new QiDepartmentId(department, site, plant));
			QiDepartment checkDepartment = getModel().findDepartmentById(newDept.getId());
			if (checkDepartment !=null ) {
				displayErrorMessageForDialog("This department already exist.");
			}
			else{
				newDept.setDepartmentName(StringUtils.trimToNull(getPanel().getDepartmentNameText().getText()));
				newDept.setPddaDept(getPanel().getPddaDeptCodeText().getText());
				newDept.setActive(getStatus());
				newDept.setCreateUser(getUserId());
				newDept.setDepartmentDescription(getPanel().getDescriptionTextArea().getText());
				try {
					getModel().saveDepartment(newDept);
					updateTree(newDept.getId().getDepartment());
					LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
					EventBusUtil.publish(new StatusMessageEvent("Department saved successfully.", StatusMessageEventType.INFO));
				} catch (Exception e) {
					displayErrorMessageForDialog("Problem occured while saving " + newDept.getDepartmentName() + " department.");
				}
			}
		} 
	}

	/**
	 * This method will reflect the create operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 */
	private void updateTree(String node) {
		TreeItem<String> deptNode=null;
		if(getStatus() ==  0)
			deptNode= new TreeItem<String>(QiConstant.DEPARTMENT_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else
			deptNode= new TreeItem<String>(QiConstant.DEPARTMENT_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
		getTree().getSelectionModel().getSelectedItem().getChildren().add(deptNode);
		getTree().getSelectionModel().select(deptNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
	}

	/**
	 * This method will reflect the update operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 * @param status
	 */
	private void updateTree(String node, int status) {
		
		getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.DEPARTMENT_NODE + "- " + node);
		QiDepartment department = getQiDepartment();
		
		if(getStatus()  ==  0 && status != getStatus()){
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateResponsibleLevelByDepartment(department.getId());
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateChilds(getTree().getSelectionModel().getSelectedItem());
		}
		else if(getStatus() ==  1 && status != getStatus())
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
	}

	@Override
	public boolean checkMandatoryFields(boolean isPanel) {
		if (isPanel) {
			if (getPanel().getDepartmentAbbrText().getText().isEmpty()) {
				setErrorMessage("Please enter department abbr.");
				return false;
			} else if (getPanel().getDepartmentNameText().getText().isEmpty()) {
				setErrorMessage("Please enter department name.");
				return false;
			}  
		} else {
			if (getPanel().getDepartmentAbbrText().getText().isEmpty()) {
				displayErrorMessageForDialog("Please enter department abbr.");
				return false;
			} else if (getPanel().getDepartmentNameText().getText().isEmpty()) {
				displayErrorMessageForDialog("Please enter department name.");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method is used to populate fields on the site panel on initial
	 * load on selection of site node on QiResponsibilityAssignmentView screen.
	 * 
	 */
	public void populateData() {
		QiDepartment department = getQiDepartment();
        setEntity(department);
		getPanel().getDepartmentAbbrText().settext(department.getId().getDepartment().trim());
		getPanel().getDepartmentNameText().settext(department.getDepartmentName().trim());
		getPanel().getPddaDeptCodeText().settext(department.getPddaDept().trim());
		getPanel().getDescriptionTextArea().setText(department.getDepartmentDescription().trim());
		if(department.isActive())
			getPanel().getActiveRadioBtn().setSelected(true);
		else
			getPanel().getInactiveRadioBtn().setSelected(true);
		QiPlant parentPlant= getModel().findPlantById(new QiPlantId
				(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().split("- ") [1].toString(),
						getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ") [1].toString()));
		if(!parentPlant.isActive()){
			getPanel().getActiveRadioBtn().setDisable(true);
		}
		
		getPanel().getPanelButton().setDisable(true);

	}

	/** This method will return department object.
	 * @return
	 */
	private QiDepartment getQiDepartment() {
		String dept=StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1]);
		String plant=StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("-")[1]);
		String site=StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("-")[1]);
		QiDepartment department= getModel().findDepartmentById(new QiDepartmentId(dept,site,plant));
		return department;
	}

	/** This method will check whether the department already exist or not.
	 * 
	 * @param newDepartment
	 * @return
	 */
	private boolean checkForExistingDepartment(QiDepartment newDepartment) {
		newDepartment.getId().setDepartment(getPanel().getDepartmentAbbrText().getText());
		QiDepartment checkDepartment = getModel().findDepartmentById(newDepartment.getId());
		if (checkDepartment != null) {
			return true;
		} else {
			return false;
		}
	}

	/** Will return formated department object after removing trailing white spaces from pdda code and dept abbr.
	 * @param dept
	 * @return
	 */
	private QiDepartment getFormatedDepartment(QiDepartment dept){
		dept.setPddaDept(dept.getPddaDept().trim());
		QiDepartmentId id=dept.getId();
		id.setDepartment(dept.getId().getDepartment().trim());
		dept.setId(id);
		return dept;

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
