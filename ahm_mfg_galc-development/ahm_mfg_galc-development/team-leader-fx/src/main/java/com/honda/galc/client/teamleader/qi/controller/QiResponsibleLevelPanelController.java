package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
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
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView;
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
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.util.AuditLoggerUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiResponsibleLevelPanelController</code> is the controller class for Responsible level Panel.
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
 * <TD>30/11/2016</TD>
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
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class QiResponsibleLevelPanelController<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanelController<QiResponsibilityAssignmentModel, QiResponsibleLevelPanel<V>, V> implements EventHandler<ActionEvent>  {
	public QiResponsibleLevelPanelController(QiResponsibilityAssignmentModel model, QiResponsibleLevelPanel<V> panel,V view) {
		super();
		setModel(model);
		setPanel(panel);
		setView(view);
	}

	@Override
	public void initListeners() {
		
		boolean setUpdateEnablerOn = getView() instanceof QiLocalResponsibilityAssignmentView;
		
		getPanel().getResponsibleLevelNameText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.RESPONSIBLE_LEVEL_NAME_TEXT_BOX_LEN));
		getPanel().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.RESPONSIBLE_LEVEL_DESC_TEXT_AREA_LEN));
		setTextFieldListener(getPanel().getResponsibleLevelNameText());
		addFieldListener(getPanel().getResponsibleLevelNameText(), true, setUpdateEnablerOn, false);
		getPanel().getController().addResLvlCmbBoxListener();
		if(setUpdateEnablerOn){
			addStatusChangeListener();
			addDescAreaChangeListener();
			addUpperResponsibleLevelComboBoxListener();
		}
		
	}


	@Override
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if(isFullAccess()){
				LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

				if (QiConstant.SAVE.equalsIgnoreCase(loggedButton.getText()))
					saveResponsibleLevel(actionEvent);
				else if (QiConstant.UPDATE.equalsIgnoreCase(loggedButton.getText()))
					updateResponsibleLevel();
				else if (QiConstant.CANCEL.equalsIgnoreCase(loggedButton.getText())) {
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				}

				else if ("Create Responsible Level".equalsIgnoreCase(loggedButton.getText()))
					openResponsibleLevelDialog(actionEvent);

			}
			else
				displayUnauthorizedOperationMessage();
		}
	}
	
	/**
	 * This method is event listener for responsibleLevelComboBox
	 */
	private void addUpperResponsibleLevelComboBoxListener() {
		getPanel().getUpperResponsibleLevelComboBoxComboBox().getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<String>() {
					public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
						getPanel().getPanelButton().setDisable(false);
						if (newValue != null)
							Logger.getLogger().check("Upper Responsible Level :: " + newValue + " selected");

						if(!getPanel().isDialogPanel()){
						QiDepartmentId id = new QiDepartmentId();

						id = getSitePlantDeptForRespLevel(id);

						List<QiResponsibleLevel> qiResponsibleLevellists = getModel().findAllRespLevels(
								id.getSite().trim(), id.getPlant().trim(), id.getDepartment().trim(), newValue);
						if (!qiResponsibleLevellists.isEmpty()) {
							getPanel().getHigherUpperResponsibleLevelComboBox().getItems().clear();
							getPanel().getHigherUpperResponsibleLevelComboBox().getItems().add(" ");
							for (QiResponsibleLevel responsibleLevel : qiResponsibleLevellists)
								if (responsibleLevel.getUpperResponsibleLevelId() != 0 && responsibleLevel.getLevel()==2) {
									getPanel().getHigherUpperResponsibleLevelComboBox().getItems()
											.add(getModel()
													.findResponsibleLevelById(
															responsibleLevel.getUpperResponsibleLevelId())
													.getResponsibleLevelName());
									getPanel().getHigherUpperResponsibleLevelComboBox().setVisible(true);
									getPanel().getHigherUpperResponsibleLevellabel().setVisible(true);
									getPanel().getHigherUpperResponsibleLevelComboBox().setDisable(false);
								}
						}
					}
					}
				});

	}

	/** This method will be used to display a Responsible Level dialog.
	 * 
	 * @param actionEvent
	 */
	private void openResponsibleLevelDialog(ActionEvent actionEvent) {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			QiResponsibilityPanelDialog<QiResponsibleLevelPanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiResponsibleLevelPanel<V>,V>("Create Responsible Level", getView().getMainWindow(),getView(), getModel(), QiConstant.RESPONSIBLE_LEVEL3_NODE,getApplicationId());
			dialog.show(); 
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
			dialog.getPanel().getController().addResponsibleLevelComboBoxListener();
			if(!getSelectedResponsibleLevel().isActive()){
				dialog.getPanel().getActiveRadioBtn().setDisable(true);
				dialog.getPanel().getInactiveRadioBtn().setSelected(true);
			}
			if(!getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0].equals(QiConstant.RESPONSIBLE_LEVEL3_NODE.trim())
					|| !getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE.trim())){
				dialog.getPanel().getUpperResponsibleLevelComboBoxComboBox().getSelectionModel().
				select(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1].trim());
				dialog.getPanel().getResponsibleLevelComboBox().getSelectionModel().select(0);
			}
		}
		else{
			displayErrorMessage("Please select Upper Responsible Level or Department.", "Please select Upper Responsible Level or Department.");
		}
	}


	/**  This method is used to update the responsible level details. 
	 * 
	 */
	private void updateResponsibleLevel() {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			clearDisplayMessage();
			
			if (getPanel().getResponsibleLevelNameText().getText().contains("- ")) {
				displayErrorMessage("Illegal character combination '- ' not allowed", "Illegal character combination '- ' not allowed");
				return;
			}
			
			QiResponsibleLevel responsibleLevel = getSelectedResponsibleLevel();
			if (responsibleLevel == null) {
				displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
				return;
			}

			responsibleLevel = getFormattedResponsibleLevel(responsibleLevel);
			String responsibleLevelName = StringUtils.trimToNull(getPanel().getResponsibleLevelNameText().getText());
			if (checkMandatoryFields(true)) {
				if (getPanel().getInactiveRadioBtn().isSelected() && getSelectedResponsibleLevel().isActive()) {
					// Check as a level1
					long countAssignedToLDC = getModel()
							.countAssignedToLocalDefectCombinationByRespId(responsibleLevel.getResponsibleLevelId());
					long countAssignedToStation = getModel()
							.countAssignedToStationByRespId(responsibleLevel.getResponsibleLevelId());
					if (countAssignedToLDC > 0 || countAssignedToStation > 0) {
						MessageDialog.showError(getView().getStage(), QiConstant.CANNOT_BE_INACTIVATED_MSG);
						return;
					}
					// check as level 2 or level 3
					long localDefectCount = getModel().countAssignedToLocalDefectCombinationByRespLevelAndId(
							responsibleLevel.getResponsibleLevelId(), responsibleLevel.getLevel());
					long assignedToStationCount = getModel().countAssignedToStationByRespLevelAndId(
							responsibleLevel.getResponsibleLevelId(), responsibleLevel.getLevel());
					if (localDefectCount > 0 || assignedToStationCount > 0) {
						StringBuilder errorMsg = new StringBuilder("Responsible Level being updated affects ");
						errorMsg.append(localDefectCount + " local(s) and ").append("\n").append(assignedToStationCount)
								.append(" station(s). ").append("Hence, Inactivation of this plant is not allowed.");
						MessageDialog.showError(getView().getStage(), errorMsg.toString());
						return;
					}
				}
				ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
				int upperResponbleLevelId = 0;
				QiResponsibleLevel newResponsibleLevel = (QiResponsibleLevel) responsibleLevel.deepCopy();
				newResponsibleLevel.setResponsibleLevelName(responsibleLevelName);
				newResponsibleLevel.setLevel(responsibleLevel.getLevel());
				newResponsibleLevel.setResponsibleLevelDescription(
						StringUtils.trimToNull(getPanel().getDescriptionTextArea().getText()));
				String upperResponsibleLevelName = StringUtils
						.trimToEmpty(getPanel().getUpperResponsibleLevelComboBoxComboBox().getValue());
				if (upperResponsibleLevelName.isEmpty() || upperResponsibleLevelName.equals(" ")) {
					newResponsibleLevel.setUpperResponsibleLevelId(0);
				} else {
					short level = newResponsibleLevel.getLevel();
					List<QiResponsibleLevel> upperResponsibleLevelList = getModel().findAllRespLevels(
							responsibleLevel.getSite(), responsibleLevel.getPlant(), responsibleLevel.getDepartment(),
							upperResponsibleLevelName);
					QiResponsibleLevel tempResponsibleLevel = new QiResponsibleLevel();
					for (QiResponsibleLevel upperResponsibleLevel : upperResponsibleLevelList) {
						if (upperResponsibleLevel.getLevel() == (short) (level + 1)) {
							tempResponsibleLevel = upperResponsibleLevel;
							upperResponbleLevelId = upperResponsibleLevel.getResponsibleLevelId();
							if (level == 1) {
								String higherUpperResponsibleLevelName = StringUtils
										.trimToEmpty(getPanel().getHigherUpperResponsibleLevelComboBox().getValue());

								if (StringUtils.isEmpty(higherUpperResponsibleLevelName.trim())
										&& upperResponsibleLevel.getUpperResponsibleLevelId() == 0)
									break;

								if (upperResponsibleLevel.getUpperResponsibleLevelId() != 0) {
									if (StringUtils.equals(higherUpperResponsibleLevelName,
											getModel()
													.findResponsibleLevelById(
															upperResponsibleLevel.getUpperResponsibleLevelId())
													.getResponsibleLevelName().toString().trim())) {
										break;
									}
								}
							}
						}
					}
					newResponsibleLevel.setUpperResponsibleLevelId(upperResponbleLevelId);
					if (!tempResponsibleLevel.isActive())
						getPanel().getInactiveRadioBtn().setSelected(true);
				}
				newResponsibleLevel.setActive(getStatus());

				if (responsibleLevel.equals(newResponsibleLevel))
					displayErrorMessage("No change detected.", "No change detected.");
				else {
					newResponsibleLevel.setUpdateUser(getUserId());
					if (dialog.showReasonForChangeDialog(null)) {
						if ((!responsibleLevelName.equals(responsibleLevel.getResponsibleLevelName().trim()) && (newResponsibleLevel.getUpperResponsibleLevelId()!=responsibleLevel.getUpperResponsibleLevelId()))
								&& checkForExistingResponsibleLevel(responsibleLevelName,
										newResponsibleLevel.getUpperResponsibleLevelId())) {
							displayErrorMessage("This responsible level already exist.",
									"This responsible level already exist.");
						} else {
							try {
								if (isUpdated(getEntity())) {
									return;
								}
								getModel().updateResponsibleLevel(
										StringUtils.trimToNull(getPanel().getResponsibleLevelNameText().getText()),
										StringUtils.trimToNull(getPanel().getDescriptionTextArea().getText()),
										newResponsibleLevel.getUpperResponsibleLevelId(),
										newResponsibleLevel.getResponsibleLevelId(), getStatus(), getUserId());
								// call to prepare and insert audit data
								AuditLoggerUtil.logAuditInfo(responsibleLevel, newResponsibleLevel,
										dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),
										getUserId());
								if (responsibleLevel.getUpperResponsibleLevelId()
										.equals(newResponsibleLevel.getUpperResponsibleLevelId()))
									updateTree(responsibleLevelName, newResponsibleLevel.getLevel(),
											responsibleLevel.getActive(), false);
								else
									updateTree(responsibleLevelName, newResponsibleLevel.getLevel(),
											responsibleLevel.getActive(), true);
								setEntity(getModel().find(getEntity()));
								EventBusUtil.publish(
										new StatusMessageEvent("Responsible level details updated successfully.",
												StatusMessageEventType.INFO));
							} catch (Exception e) {
								displayErrorMessage("Updation of " + responsibleLevelName + "failed.",
										"Updation of " + responsibleLevelName + " failed.");
							}
						}
					} else {
						return;
					}
				}
			}
		} else {
			setErrorMessage("Please select Responsible Level in the Hierarchy.");
		}
	}

	/** This method will remove trailing white spaces from old responsible level name.
	 * @param responsilbleLevel
	 * @return
	 */
	private QiResponsibleLevel getFormattedResponsibleLevel(QiResponsibleLevel responsilbleLevel) {
		responsilbleLevel.setResponsibleLevelName(responsilbleLevel.getResponsibleLevelName().trim());
		return responsilbleLevel;
	}

	/**  This method is used to create responsible level. 
	 * 
	 */
	private void saveResponsibleLevel(ActionEvent actionEvent) {

		if (getPanel().getResponsibleLevelNameText().getText().contains("- ")) {
			displayErrorMessageForDialog("Illegal character combination '- ' not allowed");
			return;
		}

		QiDepartmentId id=new QiDepartmentId();
		String checkNode = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0]);
		QiResponsibleLevel newResponsibleLevel = new QiResponsibleLevel();
		if(checkNode.equals("Department")){
			id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1].trim()); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1].trim());
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1].trim());

			newResponsibleLevel.setSite(id.getSite());
			newResponsibleLevel.setPlant(id.getPlant());
			newResponsibleLevel.setDepartment(id.getDepartment());
		}
		else if (checkNode.equals("Responsible Level 1") || checkNode.equals("Responsible Level 2") || checkNode.equals("Responsible Level 3")) {
			QiResponsibleLevel responsibleLevel = getSelectedResponsibleLevel();
			id.setSite(responsibleLevel.getSite());
			id.setPlant(responsibleLevel.getPlant());
			id.setDepartment(responsibleLevel.getDepartment());

			newResponsibleLevel.setSite(id.getSite());
			newResponsibleLevel.setPlant(id.getPlant());
			newResponsibleLevel.setDepartment(id.getDepartment());
		}
		if (checkMandatoryFields(false)) {
			clearDisplayMessage();
			String levelName = StringUtils.trimToNull(getPanel().getResponsibleLevelNameText().getText());
			short level = checkResponsibleLevel();
			
			String upperResponsibleLevelName = StringUtils.trimToEmpty(getPanel().getUpperResponsibleLevelComboBoxComboBox().getValue());	
			
			if(StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 3")){
				newResponsibleLevel.setUpperResponsibleLevelId(0);
			}else if(StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 1") 
					|| StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 2")){
				
				if(upperResponsibleLevelName.isEmpty() || upperResponsibleLevelName.equals(" ")){
					newResponsibleLevel.setUpperResponsibleLevelId(0);
				}else{
					QiResponsibleLevel upperResponsibleLevel = getModel().findBySitePlantDepartmentLevelNameAndLevel(id.getSite(),
							id.getPlant(), id.getDepartment(), upperResponsibleLevelName, (short) (level + 1));
					newResponsibleLevel.setUpperResponsibleLevelId(upperResponsibleLevel.getResponsibleLevelId());
					if(!upperResponsibleLevel.isActive())
						getPanel().getInactiveRadioBtn().setSelected(true);}
			}
			
			QiResponsibleLevel checkResponsibleLevel = getModel().findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(id.getSite(),
						id.getPlant(), id.getDepartment(), levelName , level, newResponsibleLevel.getUpperResponsibleLevelId());

			
			if (checkResponsibleLevel !=null ) {
				EventBusUtil.publish(new StatusMessageEvent("This responsible level already exist.", StatusMessageEventType.DIALOG_ERROR));
			}else{
				newResponsibleLevel.setResponsibleLevelName(StringUtils.trimToNull(getPanel().getResponsibleLevelNameText().getText()));
				newResponsibleLevel.setResponsibleLevelDescription(StringUtils.trimToNull(getPanel().getDescriptionTextArea().getText()));
				newResponsibleLevel.setLevel(level);
				newResponsibleLevel.setRowtype((short) 0);

				newResponsibleLevel.setActive(getStatus());
				newResponsibleLevel.setCreateUser(getUserId());

				try {
					getModel().saveResponsibleLevel(newResponsibleLevel);
					updateTree(newResponsibleLevel.getResponsibleLevelName(),level,	newResponsibleLevel.getUpperResponsibleLevelId()>0 
							?getModel().findResponsibleLevelById(newResponsibleLevel.getUpperResponsibleLevelId()).getResponsibleLevelName().trim():
							"Department");
					EventBusUtil.publish(new StatusMessageEvent("Responsible level saved successfully", StatusMessageEventType.INFO));
					LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				} catch (Exception e) {
					displayErrorMessage("Problem occured while saving" + newResponsibleLevel.getResponsibleLevelName() + " responsible level.", "Problem occured while saving" + newResponsibleLevel.getResponsibleLevelName() + " responsible level.");
				}
			}
		}
	}

	/**  
	 * This method is used to fetch responsible level. 
	 */
	private short checkResponsibleLevel(){
		short level=0 ;
		if(StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 1")){
			level = 1;
		}
		else if(StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 2")){
			level = 2;
		}
		else if(StringUtils.trimToEmpty((String)getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).equals("Level 3")){
			level = 3;
		}
		return level;
	}

	/**
	 * This method is used to check the mandatory fields on the Responsible Level panel.
	 * The method will return true only if all mandatory fields have certain input.
	 * 
	 */
	@Override
	protected boolean checkMandatoryFields(boolean isPanel) {
		if(isPanel){
			if(getPanel().getResponsibleLevelComboBox()!=null  && (StringUtils.trimToEmpty((String) getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).isEmpty())) {
				return false;
			} else if (getPanel().getResponsibleLevelNameText().getText().isEmpty()) {
				setErrorMessage("Please enter responsible level name.");
				return false;
			}
		}
		else{
			if (StringUtils.trimToEmpty((String) getPanel().getResponsibleLevelComboBox().getSelectionModel().getSelectedItem()).isEmpty()) {
				displayErrorMessageForDialog("Please  select responsible level.");
				return false;
			} else if (getPanel().getResponsibleLevelNameText().getText().isEmpty()) {
				displayErrorMessageForDialog("Please enter responsible level name.");
				return false;
			}
		}
		return true;
	}

	/**
	 * This method is used to populate fields on the Responsible Level panel on initial
	 * load on selection of Responsible Level node on QiResponsibilityAssignmentView screen.
	 * 
	 */
	@Override
	public void populateData() {

		getPanel().getPanelButton().setDisable(true);
		
		QiResponsibleLevel responsibleLevel=getSelectedResponsibleLevel();
		setEntity(responsibleLevel);
		getPanel().getResponsibleLevelNameText().settext(responsibleLevel.getResponsibleLevelName());
		getPanel().getDescriptionTextArea().setText(responsibleLevel.getResponsibleLevelDescription());
		loadUpperResposibleLevelComboBox(responsibleLevel.getSite() , responsibleLevel.getPlant() , responsibleLevel.getDepartment()  ,responsibleLevel.getLevel());
		if (responsibleLevel.getLevel() == 3) {
			getPanel().getUpperResponsibleLevelComboBoxComboBox().setDisable(true);
		}else{
			if (responsibleLevel.getUpperResponsibleLevelId() != 0) {
				QiResponsibleLevel upperResponsibleLevel = getModel().findResponsibleLevelById(responsibleLevel.getUpperResponsibleLevelId());
				getPanel().getUpperResponsibleLevelComboBoxComboBox().getSelectionModel().select(upperResponsibleLevel.getResponsibleLevelName());
			}
		}
		if(responsibleLevel.isActive())
			getPanel().getActiveRadioBtn().setSelected(true);
		else
			getPanel().getInactiveRadioBtn().setSelected(true);
		if (responsibleLevel.getUpperResponsibleLevelId() > 0) {
			QiResponsibleLevel upperResponsibleLevel = (QiResponsibleLevel) getModel().findResponsibleLevelById(responsibleLevel.getUpperResponsibleLevelId());
			if (upperResponsibleLevel.getActive() == 0)
				getPanel().getActiveRadioBtn().setDisable(true);
		}
		else{
			QiDepartment dept=getModel().findDepartmentById(new QiDepartmentId(responsibleLevel.getDepartment(),responsibleLevel.getSite(),responsibleLevel.getPlant()));
			if (dept.getActive() == 0) {
				getPanel().getActiveRadioBtn().setDisable(true);
			}
		}
	}


	/**  This method is used to create QiResponsibleLevel Object for selected responsible level name. 
	 * 
	 */
	public QiResponsibleLevel getSelectedResponsibleLevel() {
		String responsibleLevelByNode = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0]);
		String responsibleLevelParentNode = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[0]);
		String responsibleLevelName = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1]);
		QiDepartmentId id= new QiDepartmentId();
		short level =0;
		int layer = 0;

		if(responsibleLevelByNode.equals("Responsible Level 3") && responsibleLevelParentNode.equals("Department")){
			id.setSite( getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
			level = 3;
		}

		else if(responsibleLevelByNode.equals("Responsible Level 2")){
			if(getTree().getSelectionModel().getSelectedItem().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1]);
				layer = 0;
			}
			else if(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
				layer = 1;
			}
			else{
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
				layer = 2;
			}
			level=2;
		}
		else if(responsibleLevelByNode.equals("Responsible Level 1")){
			if(getTree().getSelectionModel().getSelectedItem().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1]);
				layer = 0;
			}
			else if(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
				layer = 1;
			}
			else if(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
				layer = 2;
			}
			else{
				id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
				id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
				id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
				layer = 3;
			}
			
			level=1;
		}

		String upperResponsibleLevelValue = StringUtils.trimToEmpty(
				getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
		int upperResponsibleLevelId = 0;
		if (!responsibleLevelByNode.equals("Responsible Level 3") && !responsibleLevelParentNode.equals("Department")) {

			List<QiResponsibleLevel> qiResponsibleLevellists = getModel().findAllRespLevels(id.getSite().trim(),
					id.getPlant().trim(), id.getDepartment().trim(), upperResponsibleLevelValue);

			if (!qiResponsibleLevellists.isEmpty()) {
				for (QiResponsibleLevel upperResponsibleLevel : qiResponsibleLevellists) {
					if(layer==1) upperResponsibleLevelId = upperResponsibleLevel.getResponsibleLevelId();
					if (layer == 2) {
						if (upperResponsibleLevel.getUpperResponsibleLevelId() != 0)
							continue;
						upperResponsibleLevelId = upperResponsibleLevel.getResponsibleLevelId();
					} else {
						if (upperResponsibleLevel.getUpperResponsibleLevelId() == 0)
							continue;
						QiResponsibleLevel higherResponsibleLevel = upperResponsibleLevel;
						higherResponsibleLevel = getModel()
								.findResponsibleLevelById(upperResponsibleLevel.getUpperResponsibleLevelId());
						String higherUpperLevelValue = StringUtils.trimToEmpty(getTree().getSelectionModel()
								.getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
						if (StringUtils.equals(higherResponsibleLevel.getResponsibleLevelName(),
								higherUpperLevelValue)) {
							upperResponsibleLevelId = upperResponsibleLevel.getResponsibleLevelId();
							break;
						}

					}
					
				}
			}

		}

		QiResponsibleLevel responsibleLevel = null;
		try {
			responsibleLevel = getModel().findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(id.getSite().trim(), id.getPlant().trim(), id.getDepartment().trim(),
					responsibleLevelName, level,upperResponsibleLevelId);
		} catch (Exception e) {
			displayErrorMessage("Failed to get responible level data.", "Failed to get responible level data.");
		}
		return responsibleLevel;
	}

	/**
	 * This method will reflect the create operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 * @param level
	 * @param parentNode
	 */
	private void updateTree(String node,short level, String parentNode) {
		TreeItem<String> responsibleLevelNode=null;
		if(level == 3){
			if(getStatus() ==  0)
				responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + node, 
						new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			else
				responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + node, 
						new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
			getTree().getSelectionModel().getSelectedItem().getChildren().add(responsibleLevelNode);
			getTree().getSelectionModel().select(responsibleLevelNode);
			getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
		}

		else if(level == 2){

			addLevel2NodeOnCreate(node, parentNode);
		}

		if(level == 1){
			addLevel1NodeOnCreate(node, parentNode);
		}
	}

	/** This method create the Responsible level 2 node at appropriate position on creation.
	 * 
	 * @param node
	 * @param parentNode
	 */
	private void addLevel2NodeOnCreate(String node, String parentNode) {
		TreeItem<String> responsibleLevelNode;
		if(getStatus() ==  0 )
			responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + node, 
					new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else
			responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + node,
					new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
		if(parentNode.equals("Department") && 
				getTree().getSelectionModel().getSelectedItem().getValue().split("- ")[0].trim().equals(QiConstant.DEPARTMENT_NODE.trim())){
			getTree().getSelectionModel().getSelectedItem().getChildren().add(responsibleLevelNode);
		}
		else if(parentNode.equals("Department") && 
				getTree().getSelectionModel().getSelectedItem().getValue().split("- ")[0].trim().equals(QiConstant.RESPONSIBLE_LEVEL3_NODE.trim())){
			getTree().getSelectionModel().getSelectedItem().getParent().getChildren().add(responsibleLevelNode);
		}
		else{
			if(parentNode.equals(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1].trim())){
				getTree().getSelectionModel().getSelectedItem().getChildren().add(responsibleLevelNode);
			}
			else{
				ObservableList<TreeItem<String>> responsibleLevel3List = getTree().getSelectionModel().getSelectedItem().getParent().getChildren();
				parentNode = QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + parentNode;
				for (TreeItem<String> treeItem : responsibleLevel3List) {
					if (treeItem.getValue().trim().equals(parentNode)) {
						treeItem.getChildren().addAll(responsibleLevelNode);
						break;
					}
				}
			}
		}
		getTree().getSelectionModel().select(responsibleLevelNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
	}

	/** This method create the Responsible level 1 node at appropriate position on creation.
	 * 
	 * @param node
	 * @param parentNode
	 */
	private void addLevel1NodeOnCreate(String node, String parentNode) {
		TreeItem<String> responsibleLevelNode;
		if(getStatus() ==  0)
			responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + node, 
					new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else
			responsibleLevelNode= new TreeItem<String>(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + node, 
					new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));

		if(parentNode.equals("Department")){
			if (getTree().getSelectionModel().getSelectedItem().getValue().contains("Department"))
				getTree().getSelectionModel().getSelectedItem().getChildren().add(responsibleLevelNode);
			else if (getTree().getSelectionModel().getSelectedItem().getParent().getValue().contains("Department"))
				getTree().getSelectionModel().getSelectedItem().getParent().getChildren().add(responsibleLevelNode);
			else
				getTree().getSelectionModel().getSelectedItem().getParent().getParent().getChildren().add(responsibleLevelNode);
		}
		else{
			if(parentNode.equals(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1].trim().trim())){
				getTree().getSelectionModel().getSelectedItem().getChildren().add(responsibleLevelNode);
			}
			else {
				TreeItem<String> department = null;
				if (getTree().getSelectionModel().getSelectedItem().getParent().getValue().contains("Department")){
					department = getTree().getSelectionModel().getSelectedItem().getParent();
				}
				else {
					department = getTree().getSelectionModel().getSelectedItem().getParent().getParent();
				}
				ObservableList<TreeItem<String>> responsibleLevel3List = department.getChildren();
				ObservableList<TreeItem<String>> responsibleLevel2List;
				String level2Parent = QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + parentNode;
				for (TreeItem<String> treeItemResponsibleLevel3 : responsibleLevel3List) {
					if (treeItemResponsibleLevel3.getValue().trim().equals(level2Parent)) {
						treeItemResponsibleLevel3.getChildren().addAll(responsibleLevelNode);
						break;
					}
					responsibleLevel2List = treeItemResponsibleLevel3.getChildren();
					for (TreeItem<String> treeItemResponsibleLevel2 : responsibleLevel2List) {
						if (treeItemResponsibleLevel2.getValue().trim().equals(level2Parent)) {
							treeItemResponsibleLevel2.getChildren().addAll(responsibleLevelNode);
							break;
						}
					}
				}
			}
		}
		getTree().getSelectionModel().select(responsibleLevelNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
	}

	/**
	 * This method will reflect the update operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 * @param level
	 * @param status
	 */
	private void updateTree(String node,short level, int status, boolean isParentChanged) {
		if(level == 3){
			getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + node);
		}
		else if(level == 2){
			if(isParentChanged) {
				updatePositionOfLevel2NodeForUpdate(node);
			}
			else{
				getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + node);
			}

		}

		else if(level == 1){
			if(isParentChanged) {
				updatePositionOfLevel1NodeForUpdate(node);
			}
			else{
				getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + node);
			}

		}
		if(getStatus()  ==  0 ){
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			((QiLocalResponsibilityAssignmentController) getView().getController()).inactivateResponsibleLevelByUpperResponsibleLevel(Integer.valueOf(getSelectedResponsibleLevel().getId().toString()));
			((QiLocalResponsibilityAssignmentController) getView().getController()).inactivateChilds(getTree().getSelectionModel().getSelectedItem());
		}
		else if(getStatus() ==  1  && status != getStatus())
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
	}

	/** This method shift the position of Responsible level 1 node on update.
	 * 
	 * @param node
	 */
	@SuppressWarnings("unchecked")
	private void updatePositionOfLevel1NodeForUpdate(String node) {
		String updatedUpperResponsibleLevelName = StringUtils.trimToEmpty((String)getPanel().getUpperResponsibleLevelComboBoxComboBox().getSelectionModel().getSelectedItem());
		String oldUpperResponsibleLevelName = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
		String responsibleLevelParentNode = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[0]);
		if(updatedUpperResponsibleLevelName.equals(oldUpperResponsibleLevelName)){
			getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + node);}

		else{
			getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + node);
			TreeItem<String> department = null;
			boolean dept3=false;
			boolean dept2=false;
			if (responsibleLevelParentNode.equals(QiConstant.DEPARTMENT_NODE.trim())) {
				department = getTree().getSelectionModel().getSelectedItem().getParent();
			} else if (responsibleLevelParentNode.equals(QiConstant.RESPONSIBLE_LEVEL2_NODE.trim())
					&& getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue()
					.toString().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)) {
				department = getTree().getSelectionModel().getSelectedItem().getParent().getParent();
				dept2=true;
			} else {
				department = getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent();
				dept3=true;
			}

			if (!updatedUpperResponsibleLevelName.isEmpty()) {
				ObservableList<TreeItem<String>> allNodeUnderDept = department.getChildren();
				ObservableList<TreeItem<String>> responsibleLevel2List;
				TreeItem<String> selectedNode = getTree().getSelectionModel().getSelectedItem();
				String level2Parent = QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + updatedUpperResponsibleLevelName;
				for (TreeItem<String> nodeUnderDept : allNodeUnderDept) {
					responsibleLevel2List = nodeUnderDept.getChildren();

					if (nodeUnderDept.getValue().trim().equals(level2Parent)) {
						selectedNode.getParent().getChildren().remove(selectedNode);
						nodeUnderDept.getChildren().addAll(selectedNode);
						getTree().getSelectionModel().select(selectedNode);
						break;
					}
					for (TreeItem<String> treeItemResponsibleLevel2 : responsibleLevel2List) {
						if (treeItemResponsibleLevel2.getValue().trim().equals(level2Parent)) {
							selectedNode.getParent().getChildren().remove(selectedNode);
							treeItemResponsibleLevel2.getChildren().addAll(selectedNode);
							getTree().getSelectionModel().select(selectedNode);
							break;
						}
					}
				} 
			}

			else{
				if(dept2){
					department = getTree().getSelectionModel().getSelectedItem().getParent().getParent();
				}
				else if(dept3){
					department = getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent();
				}

				TreeItem<String> selectedNode = getTree().getSelectionModel().getSelectedItem();
				selectedNode.getParent().getChildren().remove(selectedNode);
				department.getChildren().addAll(selectedNode);
				getTree().getSelectionModel().select(selectedNode);
			}
		}
	}

	/**  This method shift the position of Responsible level 2 node on update.
	 * 
	 * @param node
	 */
	private void updatePositionOfLevel2NodeForUpdate(String node) {
		String updatedUpperResponsibleLevelName = StringUtils.trimToEmpty(getPanel().getUpperResponsibleLevelComboBoxComboBox().getValue());
		String oldUpperResponsibleLevelName = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
		String responsibleLevelParentNode = StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[0]);
		if(updatedUpperResponsibleLevelName.equals(oldUpperResponsibleLevelName)){
			getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + node);}

		else{
			getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + node);
			TreeItem<String>	department = null;

			if (!updatedUpperResponsibleLevelName.isEmpty()) {
				if (responsibleLevelParentNode.equals(StringUtils.trimToEmpty(QiConstant.DEPARTMENT_NODE.trim()))) {
					department = getTree().getSelectionModel().getSelectedItem().getParent();
				} else {
					department = getTree().getSelectionModel().getSelectedItem().getParent().getParent();
				}
				ObservableList<TreeItem<String>> responsibleLevel3List = department.getChildren();
				updatedUpperResponsibleLevelName = QiConstant.RESPONSIBLE_LEVEL3_NODE + "- "
						+ updatedUpperResponsibleLevelName;
				for (TreeItem<String> treeItem : responsibleLevel3List) {

					if (treeItem.getValue().trim().equals(updatedUpperResponsibleLevelName)) {
						TreeItem<String> selectedNode = getTree().getSelectionModel().getSelectedItem();
						selectedNode.getParent().getChildren().remove(selectedNode);
						treeItem.getChildren().addAll(selectedNode);
						getTree().getSelectionModel().select(selectedNode);
						break;
					}
				} 
			}

			else {
				department = getTree().getSelectionModel().getSelectedItem().getParent().getParent();

				TreeItem<String> selectedNode = getTree().getSelectionModel().getSelectedItem();
				selectedNode.getParent().getChildren().remove(selectedNode);
				department.getChildren().addAll(selectedNode);
				getTree().getSelectionModel().select(selectedNode);
			}
		}
	}

	/**
	 * This method is event listener for responsibleLevelComboBox
	 */
	private void addResponsibleLevelComboBoxListener() {
		getPanel().getResponsibleLevelComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
				
				if(newValue != null)
					Logger.getLogger().check("Responsible Level :: " + newValue + " selected");

				short level = checkResponsibleLevel();
				QiDepartmentId id = new QiDepartmentId();
				if ( level == 3){
					getPanel().getUpperResponsibleLevelComboBoxComboBox().setDisable(true);
				}
				else if ( level == 1 || level == 2 ){
					if(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE))
						getPanel().getUpperResponsibleLevelComboBoxComboBox().setDisable(true);		
					else
						getPanel().getUpperResponsibleLevelComboBoxComboBox().setDisable(false);		

					id=getSitePlantDeptForRespLevel(id);
					loadUpperResposibleLevelComboBox(id.getSite().trim() , id.getPlant().trim() , id.getDepartment().trim() , level);
				}
			}
		});
		
	}
	
	private void addResLvlCmbBoxListener(){
		
		if(getPanel() != null && getPanel().getResponsibleLevelComboBox() != null){
			getPanel().getResponsibleLevelComboBox().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
					
					if(newValue != null)
						Logger.getLogger().check("Responsible Level :: " + newValue + " selected");
				}
			});
		}
	}


	/**
	 * @param id
	 */
	private QiDepartmentId getSitePlantDeptForRespLevel(QiDepartmentId id) {
		if(getTree().getSelectionModel().getSelectedItem().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
			id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1]);
		}
		else if(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
			id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getValue().toString().split("- ")[1]);
		}
		else if(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().split("- ")[0].equals(QiConstant.DEPARTMENT_NODE)){
			id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getValue().toString().split("- ")[1]);
		}
		else{
			id.setSite(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]); 
			id.setPlant(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getParent().getValue().toString().split("- ")[1]);
			id.setDepartment(getTree().getSelectionModel().getSelectedItem().getParent().getParent().getParent().getValue().toString().split("- ")[1]);

		}
		return id;
	}

	/**
	 * This method is used to load upperResponsibleLevelComboBox
	 */
	private void loadUpperResposibleLevelComboBox(String site, String plant,
			String department, short level) {
		getPanel().getUpperResponsibleLevelComboBoxComboBox().getItems().clear();
		getPanel().getUpperResponsibleLevelComboBoxComboBox().setPromptText("Select");
		List<QiResponsibleLevel> responsibleLevelsBySitePlantDeptAndLevelList = getModel().findAllResponsibleLevelBySitePlantDepartmentAndLevel(site, 
				plant, department,(short) (level + 1));
		getPanel().getUpperResponsibleLevelComboBoxComboBox().getItems().add(" ");
		for (QiResponsibleLevel responsibleLevelObj : responsibleLevelsBySitePlantDeptAndLevelList) {
			if(!getPanel().getUpperResponsibleLevelComboBoxComboBox().getItems().contains(responsibleLevelObj.getResponsibleLevelName()))
			getPanel().getUpperResponsibleLevelComboBoxComboBox().getItems().add(responsibleLevelObj.getResponsibleLevelName());			
		
		}
	}

	/**  Check whether the responsible level already exist in database.
	 * 
	 * @param responsibleLevelName
	 * @return
	 */
	private boolean checkForExistingResponsibleLevel(String responsibleLevelName, int newResponsibleLevelId) {
		QiResponsibleLevel responsibleLevel = getSelectedResponsibleLevel();
		QiResponsibleLevel checkResponsibleLevel;
		
			checkResponsibleLevel  = getModel().findBySitePlantDepartmentLevelNameLevelAndUpperResponsibleLevelId(responsibleLevel.getSite(),
				responsibleLevel.getPlant(),  responsibleLevel.getDepartment(), responsibleLevelName , responsibleLevel.getLevel(), newResponsibleLevelId);
		
		if (checkResponsibleLevel !=null ) {
			return true;
		}
		return false;
	}
	

	/** This method will return main views treeview after casting.
	 * 
	 * @return mainView Tree 
	 */
	public TreeView<String> getTree() {
		if(getView() instanceof QiRegionalResponsibilityAssignmentView){
			return ((QiRegionalResponsibilityAssignmentView)getView()).getTree();
		}
		else{
			return ((QiLocalResponsibilityAssignmentView)getView()).getTree();
		}
			
	}
	
}
