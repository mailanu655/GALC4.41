package com.honda.galc.client.teamleader.qi.controller;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.teamleader.qi.view.QiCompanyPanel;
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiResponsibilityPanelDialog;
import com.honda.galc.client.teamleader.qi.view.QiSitePanel;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiCompanyPanelController</code> is the controller class for Company Panel
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

public class QiCompanyPanelController<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanelController<QiResponsibilityAssignmentModel, QiCompanyPanel<V>, V> implements EventHandler<ActionEvent>  {

	public QiCompanyPanelController(QiResponsibilityAssignmentModel model, QiCompanyPanel<V> panel, V view) {
		super();
		setModel(model);
		setPanel(panel);
		setView(view);
	}

	@Override
	public void handle(ActionEvent actionEvent) {

		if (actionEvent.getSource() instanceof LoggedButton) {
			if (isFullAccess()) {
				LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();

				if (QiConstant.UPDATE.equalsIgnoreCase(loggedButton.getText()))
					updateCompany();
				else if (QiConstant.SAVE.equalsIgnoreCase(loggedButton.getText()))
					saveCompany(actionEvent);
				else if (QiConstant.CANCEL.equalsIgnoreCase(loggedButton.getText())) {
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				} else if ("Create Company".equalsIgnoreCase(loggedButton.getText()))
					openCompanyDialog(actionEvent);
				else if ("Create Site".equalsIgnoreCase(loggedButton.getText()))
					openSiteDialog(actionEvent);
			}
			else 
				displayUnauthorizedOperationMessage();
		}
	}						

	/** This method will be used to display a Company dialog.
	 * 
	 * @param actionEvent
	 */
	private void openCompanyDialog(ActionEvent actionEvent) {
		QiResponsibilityPanelDialog<QiCompanyPanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiCompanyPanel<V>,V>(
				"Create Company", getView().getMainWindow(), getView(), getModel(), QiConstant.COMPANY_NODE,getApplicationId());
		dialog.show();
		dialog.getPanel().getController().setPanel(dialog.getPanel());
		dialog.getPanel().getController().initListeners();
	}

	/** This method will be used to display a Site dialog.
	 * 
	 * @param actionEvent
	 */
	private void openSiteDialog(ActionEvent actionEvent) {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			QiResponsibilityPanelDialog<QiSitePanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiSitePanel<V>,V>("Create Site", getView().getMainWindow(), getView(), getModel(), QiConstant.SITE_NODE,getApplicationId());
			dialog.show();
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
			if (!getCompany(StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1])).isActive()) {
				dialog.getPanel().getActiveRadioBtn().setDisable(true);
				dialog.getPanel().getInactiveRadioBtn().setSelected(true);
			}
		}
		else{
			setErrorMessage("Please select Company in the Hierarchy.");
		}
	}

	/**  This method is used to update the company details. 
	 * 
	 */
	private void updateCompany() {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			clearDisplayMessage();
			String companyName = StringUtils.trimToNull(getPanel().getCompanyNameText().getText());
			if(checkMandatoryFields(true)){
				QiCompany company = getCompany(StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1]));
				if (company == null) {
					displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
					return;
				}				
				QiCompany newCompany = new QiCompany();
				newCompany.setCompany(companyName);
				newCompany.setCompanyDesc(getPanel().getDescriptionTextArea().getText());
				newCompany.setActive(getStatus());
				newCompany.setUpdateUser(getUserId());
				List<String> assignedSiteList = getModel().findAllAssignedSites();
				
				List<QiSite> siteList = getModel().findAllSiteByCompany(companyName);
				
				for(QiSite site : siteList) {
					if(assignedSiteList.contains(site.getSite())) {
						if(getStatus() == 0){
							MessageDialog.showError(getView().getStage(), QiConstant.CANNOT_BE_INACTIVATED_MSG);
							return;
						}
					}
				}
				
				if (company.equals(newCompany))
					displayErrorMessage("No change detected.", "No change detected.");
				else {
					if(!companyName.equals(company.getCompany().trim()) && isCompanyAlreadyExist(companyName)){
						displayErrorMessage("This company already exist.", "This company already exist.");
					}
					else{					
						ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
						if (dialog.showReasonForChangeDialog(null)) {
							try {
								if (isUpdated(getEntity())) {
									return;
								}
								getModel().updateCompany(companyName, company.getCompany(), newCompany.getCompanyDesc(), newCompany.getActive(), getUserId());
								// call to prepare and insert audit data
								AuditLoggerUtil.logAuditInfo(company, newCompany, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());

								if(!company.getCompany().trim().equals(newCompany.getCompany().trim())){
									getModel().updateCompanyForSite(companyName, getUserId(), company.getCompany());
								}
								updateTree(companyName, company.getActive());
								setEntity(getModel().find(getEntity()));
								EventBusUtil.publish(new StatusMessageEvent("Company details updated successfully.", StatusMessageEventType.INFO));
							} catch (Exception e) {
								displayErrorMessage("Updation of " + companyName + "failed.", "Updation of " + companyName + " failed.");
							}
						} else {
							return;
						}
					}
				}
			}
		}
		else{
			setErrorMessage("Please select Company in the Hierarchy.");
		}
	}

	/**  Check whether the company already exist in database.
	 * 
	 * @param companyName
	 * @return
	 */
	private boolean isCompanyAlreadyExist(String companyName) {
		if(getCompany(companyName) != null)
			return true;
		return false;
	}

	/**
	 * This method is used to save the new company. It will check is company
	 * with same name exist in the database prior saving the company, else it
	 * will display message to user.
	 * 
	 * @param actionEvent
	 */
	private void saveCompany(ActionEvent actionEvent) {
		if(checkMandatoryFields(false)){
			clearDisplayMessage();
			if(isCompanyAlreadyExist(getPanel().getCompanyNameText().getText().trim())){
				displayErrorMessageForDialog("This company already exist.");
			}
			else{
				QiCompany newCompany = new QiCompany();
				newCompany.setCompany(getPanel().getCompanyNameText().getText());
				newCompany.setCompanyDesc(getPanel().getDescriptionTextArea().getText());
				newCompany.setActive(getStatus());
				newCompany.setCreateUser(getUserId());
				try {
					getModel().saveCompany(newCompany);
					updateTree(newCompany.getCompany());
					LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
					EventBusUtil.publish(new StatusMessageEvent("Company saved successfully.", StatusMessageEventType.INFO));
				} catch (Exception e) {
					displayErrorMessageForDialog("Problem occured while saving" + newCompany.getCompany() + " company.",
							"Problem occured while saving" + newCompany.getCompany() + " company.");
				}
			}
		}
	}

	@Override
	public void initListeners() {
		
		boolean setUpdateEnablerOn = getView() instanceof QiRegionalResponsibilityAssignmentView;
		
		getPanel().getCompanyNameText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.COMPANY_NAME_TEXT_BOX_LEN));
		setTextFieldListener(getPanel().getCompanyNameText());
		addFieldListener(getPanel().getCompanyNameText(), true, setUpdateEnablerOn, true);
		getPanel().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.COMPANY_DESC_TEXT_AREA_LEN));

		if(setUpdateEnablerOn){
			addStatusChangeListener();
			addDescAreaChangeListener();
		}
		
	}

	/**
	 * This method is used to populate fields on the company panel on initial
	 * load on selection of company node on QiResponsibilityAssignmentView screen.
	 * 
	 */
	public void populateData() {
		QiCompany company = getCompany(StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1]));
		setEntity(company);
		getPanel().getCompanyNameText().settext(company.getCompany());
		getPanel().getDescriptionTextArea().setText(company.getCompanyDesc());
		if (company.isActive())
			getPanel().getActiveRadioBtn().setSelected(true);
		else
			getPanel().getInactiveRadioBtn().setSelected(true);
		
			getPanel().getPanelButton().setDisable(true);
			
	}

	/**
	 * This method is used to check the mandatory fields on the company panel.
	 * The method will return true only if all mandatory fields have certain input.
	 * 
	 */
	@Override
	protected boolean checkMandatoryFields(boolean isPanel) {
		if (getPanel().getCompanyNameText().getText() == null || getPanel().getCompanyNameText().getText().isEmpty()) {
			if(isPanel)
				setErrorMessage("Please enter company name.");
			else
				displayErrorMessageForDialog("Please enter company name.");
			return false;
		}
		return true;
	}

	/**
	 * This method will reflect the create operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param node
	 * @param operation
	 */
	private void updateTree(String node) {
		TreeItem<String> companyNode=null;
		if(getStatus() ==  0)
			companyNode= new TreeItem<String>(QiConstant.COMPANY_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else
			companyNode= new TreeItem<String>(QiConstant.COMPANY_NODE + "- " + node,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
		getTree().getRoot().getChildren().add(companyNode);
		getTree().getSelectionModel().select(companyNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex());
	}

	/**
	 * This method will reflect the update operation performed for
	 * company on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param companyName
	 * @param status
	 */
	private void updateTree(String companyName, int status) {
		getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.COMPANY_NODE + "- " + companyName);
		if (getStatus() == 0 && status != getStatus()){
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateSiteByCompany(companyName);
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateChilds(getTree().getSelectionModel().getSelectedItem());
		}
		else if(getStatus() ==  1 &&  status != getStatus())
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
	}

	/** This method will return the entity of QiCompany based on the companyName provided as argument.
	 * 
	 * @param companyName
	 * @return
	 */
	private QiCompany getCompany(String companyName) {
		QiCompany company = null;
		try {
			company = getModel().findCompanyById(companyName);
		} catch (Exception e) {
			setErrorMessage("Failed to get Company details.");
		}
		return company;
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
