package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.DataValidationDialog;

import com.honda.galc.client.teamleader.qi.view.QiAbstractTabbedView;
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiPlantPanel;
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
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;
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
 * <code>QiSitePanelController</code> is the controller class for Site Panel
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

public class QiSitePanelController<V extends QiAbstractTabbedView<?,?>> extends QiAbstractPanelController<QiResponsibilityAssignmentModel, QiSitePanel<V>,V>
implements EventHandler<ActionEvent>  {

	public QiSitePanelController(QiResponsibilityAssignmentModel model, QiSitePanel<V> panel,V view) {
		super();
		setModel(model);
		setPanel(panel);
		setView(view);
	}

	@Override
	public void initListeners() {
		
		boolean setUpdateEnablerOn = getView() instanceof QiRegionalResponsibilityAssignmentView;
		
		getPanel().getSiteNameText().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.SITE_NAME_TEXT_BOX_LEN));
		getPanel().getDescriptionTextArea().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(QiConstant.SITE_DESC_TEXT_AREA_LEN));
		setTextFieldListener(getPanel().getSiteNameText());
		addFieldListener(getPanel().getSiteNameText(), true, setUpdateEnablerOn, true);
		
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
					saveSite(actionEvent);
				else if (QiConstant.UPDATE.equalsIgnoreCase(loggedButton.getText()))
					updateSite();
				else if (QiConstant.CANCEL.equalsIgnoreCase(loggedButton.getText())) {
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
				} else if ("Create Plant".equalsIgnoreCase(loggedButton.getText()))
					openPlantDialog(actionEvent);
			} else
				displayUnauthorizedOperationMessage();
		}
	}

	/** This function will open a plant dialog to create plant 
	 * 
	 * @param actionEvent
	 */
	private void openPlantDialog(ActionEvent actionEvent) {
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			QiResponsibilityPanelDialog<QiPlantPanel<V>,V> dialog = new QiResponsibilityPanelDialog<QiPlantPanel<V>,V>("Create Plant", getView().getMainWindow(), getView(), getModel(), QiConstant.PLANT_NODE,getApplicationId());
			dialog.show();
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
			if (!getSite(StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1])).isActive()) {
				dialog.getPanel().getActiveRadioBtn().setDisable(true);
				dialog.getPanel().getInactiveRadioBtn().setSelected(true);
			}
		}
		else{
			setErrorMessage("Please select Site in the Hierarchy.");
		}
	}


	/** This function will update the site information
	 * 
	 * @param actionEvent
	 */
	private void updateSite() {
		
		if (null != getTree().getSelectionModel().getSelectedItem()) {
			clearDisplayMessage();
			if(checkMandatoryFields(false)){
				List<String> allAssignedSitesList = getModel().findAllAssignedSites();
				String siteName = StringUtils.trimToNull(getPanel().getSiteNameText().getText());
				QiSite site = getSite(StringUtils.trimToEmpty(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1]));
				if (site == null) {
					displayErrorMessage(QiConstant.CONCURRENT_UPDATE_MSG, QiConstant.CONCURRENT_UPDATE_MSG);
					return;
				}
				int wasActive=site.getActive();
				int isActive=getStatus();
				QiSite newSite = (QiSite)site.deepCopy();
				newSite.setSite(siteName);
				newSite.setSiteDesc(getPanel().getDescriptionTextArea().getText());
				newSite.setActive(getStatus());
				if(allAssignedSitesList.contains(siteName))
					if(isActive == 0){
						MessageDialog.showError(getView().getStage(), QiConstant.CANNOT_BE_INACTIVATED_MSG);
						return;
					}
						
				if (site.equals(newSite))
					displayErrorMessage("No change detected.", "No change detected.");
				else {
					if (!siteName.equals(site.getSite().trim()) && isSiteAlreadyExist(siteName)){
						displayErrorMessage("This site already exist.", "This site already exist.");
					}
					else{
						ReasonForChangeDialog dialog = new ReasonForChangeDialog(getApplicationId());
						if (dialog.showReasonForChangeDialog(null)) {
							try {
									if(wasActive==1 && isActive==0){
										String retValue=isLocalSiteImpacted(site.getSite());
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
									getModel().updateSite(siteName, site.getSite(), newSite.getSiteDesc(), newSite.getActive(), getUserId());
									// call to prepare and insert audit data
									AuditLoggerUtil.logAuditInfo(site, newSite, dialog.getReasonForChangeTextArea().getText(), getView().getScreenName(),getUserId());
									if (!site.getSite().trim().equals(siteName)) {
										ArrayList<QiPlant> allPlants = new ArrayList<QiPlant>();
										allPlants.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getPlantListBySite(site));

										if (!allPlants.isEmpty())
											for (QiPlant plant : allPlants) {
												ArrayList<QiDepartment> deptList = new ArrayList<QiDepartment>();
												deptList.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getDepartmentListByPlant(plant));

												getModel().updateSiteForPlant(getUserId(), siteName, plant.getId());

												if (!deptList.isEmpty())
													for (QiDepartment dept : deptList) {
														ArrayList<QiResponsibleLevel> respList = new ArrayList<QiResponsibleLevel>();
														respList.addAll(((QiRegionalResponsibilityAssignmentView)getView()).getController().getResponsibleLevelListByDepartment(dept));

														getModel().updateSiteForDept(getUserId(), siteName, dept.getId());

														if (!respList.isEmpty())
															for (QiResponsibleLevel resp : respList) {
																getModel().updateSiteForResponsibleLevel(siteName, getUserId(), Integer.valueOf(resp.getId().toString()));
															}
													}
											}
									}
									updateTree(siteName, site.getActive());
									setEntity(getModel().find(getEntity()));
									EventBusUtil.publish(new StatusMessageEvent("Site details updated successfully.", StatusMessageEventType.INFO));
								}
							catch (Exception e) {
								displayErrorMessage("Updation of " + siteName + "failed.", "Updation of " + siteName + " failed.");
							}
						} else {
							return;
						}
					}
				}
			}
		}
		else{
			setErrorMessage("Please select Site in the Hierarchy.");
		}
	}

	/** This method will create site.
	 * 
	 * @param actionEvent
	 */
	private void saveSite(ActionEvent actionEvent) {
		if (checkMandatoryFields(false)) {
			clearDisplayMessage();
			if (isSiteAlreadyExist(StringUtils.trimToNull(getPanel().getSiteNameText().getText().trim()))) {
				displayErrorMessageForDialog("This site already exist.");
			}
			else{
				QiSite newSite = new QiSite();
				newSite.setSite(StringUtils.trimToNull(getPanel().getSiteNameText().getText()));
				newSite.setSiteDesc(getPanel().getDescriptionTextArea().getText());
				newSite.setCompany(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("- ")[1]);
				newSite.setActive(getStatus());
				newSite.setCreateUser(getUserId());
				try {
					getModel().saveSite(newSite);
					updateTree(newSite.getSite().trim());
					LoggedButton loggedButton = (LoggedButton) actionEvent.getSource();
					Stage stage = (Stage) loggedButton.getScene().getWindow();
					stage.close();
					EventBusUtil.publish(new StatusMessageEvent("Site saved successfully.", StatusMessageEventType.INFO));
				} catch (Exception e) {
					displayErrorMessageForDialog("Problem occured while saving" + newSite.getSite() + " site.", "Problem occured while saving" + newSite.getSite() + " site.");
				}
			}
		}
	}

	/**
	 * This method is used to populate fields on the site panel on initial
	 * load on selection of site node on QiResponsibilityAssignmentView screen.
	 * 
	 */
	public void populateData() {
		try {
			QiSite site = getSite(getTree().getSelectionModel().getSelectedItem().getValue().toString().split("-")[1]);
			setEntity(site);
			getPanel().getSiteNameText().settext(site.getSite());
			getPanel().getDescriptionTextArea().setText(site.getSiteDesc());
			if (site.isActive())
				getPanel().getActiveRadioBtn().setSelected(true);
			else
				getPanel().getInactiveRadioBtn().setSelected(true);

			QiCompany parentCompany = getModel().findCompanyById(getTree().getSelectionModel().getSelectedItem().getParent().getValue().split("- ")[1].toString());
			if (parentCompany.getActive() == 0) {
				getPanel().getActiveRadioBtn().setDisable(true);
			}
			
			getPanel().getPanelButton().setDisable(true);
			
			
		} catch (Exception e) {
			displayErrorMessage("Error occured while displaying site.", "Error occured while displaying site.");
		}
	}

	/** This will return a QiSite object.
	 * 
	 * @param temp
	 * @return
	 */
	private QiSite getSite(String temp) {
		QiSite site = null;
		try {
			site = getModel().findSiteById(StringUtils.trimToEmpty(temp));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return site;
	}

	/** This method will reflect the save operation performed for
	 *  site on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param siteName
	 */
	private void updateTree(String siteName) {
		TreeItem<String> siteNode=null;
		if(getStatus() ==  0)
			siteNode= new TreeItem<String>(QiConstant.SITE_NODE + "- " + siteName,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
		else if(getStatus() ==  1)
			siteNode= new TreeItem<String>(QiConstant.SITE_NODE + "- " + siteName,  new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
		getTree().getSelectionModel().getSelectedItem().getChildren().add(siteNode);
		getTree().getSelectionModel().select(siteNode);
		getTree().scrollTo(getTree().getSelectionModel().getSelectedIndex()-10);
	}

	/** This method will reflect the update operation performed for
	 *  site on tree view displayed on the QiResponsibilityAssignmentView screen.
	 * 
	 * @param siteName
	 */
	private void updateTree(String siteName, int status) {
		getTree().getSelectionModel().getSelectedItem().setValue(QiConstant.SITE_NODE + "- " + siteName);
		if(getStatus()  ==  0 && getSite(siteName).getActive() != status){
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png")));
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivatePlantBySite(siteName);
			((QiRegionalResponsibilityAssignmentView)getView()).getController().inactivateChilds(getTree().getSelectionModel().getSelectedItem());
		}
		else
			getTree().getSelectionModel().getSelectedItem().setGraphic(new ImageView(new Image("/resource/com/honda/galc/client/images/qi/green.png")));
	}

	/** This method will return true only if all the mandatory fields have input.
	 * 
	 */
	@Override
	protected boolean checkMandatoryFields(boolean isPanel) {
		if (getPanel().getSiteNameText().getText().isEmpty()) {
			if(isPanel)
				setErrorMessage("Please enter site name.");
			else
				displayErrorMessageForDialog("Please enter site name.");
			return false;
		}
		return true;
	}


	/** Check whether the site already exist in database.
	 * 
	 * @param siteName
	 * @return
	 */
	private boolean isSiteAlreadyExist(String siteName) {
		if(getSite(siteName) != null)
			return true;
		return false;
	}
	
	/**
	 * Displays local site validation and returns true if any site is impacted
	 */
	public String isLocalSiteImpacted(String siteName) {
		List<String> siteList=new ArrayList<String>();
		siteList.add(siteName);
		Map<String,String> siteMap=PropertyService.getPropertyBean(QiPropertyBean.class).getSitesForValidation();
		if(siteMap==null || siteMap.isEmpty()){
			if(!(MessageDialog.confirm(getView().getStage(),"Local Site Data will not be validated as local site(s) are not configured. Do you still want to continue?")))
				return QiConstant.NO_LOCAL_SITES_CONFIGURED;
			else
				return "";
		}
		DataValidationDialog dataValidationDialog=new DataValidationDialog("Local Validation",getApplicationId(),getView().getScreenName()+"-Site",siteList);
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
