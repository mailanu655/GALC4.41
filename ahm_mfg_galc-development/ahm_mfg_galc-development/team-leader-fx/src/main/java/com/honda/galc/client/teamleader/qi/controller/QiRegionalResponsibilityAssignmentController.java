package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.QiCompanyPanel;
import com.honda.galc.client.teamleader.qi.view.QiDepartmentPanel;
import com.honda.galc.client.teamleader.qi.view.QiPlantPanel;
import com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiResponsibilityPanelDialog;
import com.honda.galc.client.teamleader.qi.view.QiSitePanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiDepartmentId;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiPlantId;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.util.SortedArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiResponsibilityAssignmentController</code> is the controller class for QiRegionalResponsibilityAssignmentView Panel i.e. Hierarchy Screen.
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
 * <TD>14/10/2016</TD>
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

public class QiRegionalResponsibilityAssignmentController extends AbstractQiController<QiResponsibilityAssignmentModel, QiRegionalResponsibilityAssignmentView> implements EventHandler<ActionEvent>{

	private QiResponsibilityAssignmentModel model;
	
	private ChangeListener<TreeItem<String>> treeNodeChangeListener = new ChangeListener<TreeItem<String>>() {
		public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
			clearDisplayMessage();
			getView().getContentPane().getChildren().clear();
			
			if(newValue!= null){
			String[] selectedNode = newValue.getValue().toString().split("-");
			if (selectedNode[0].equalsIgnoreCase(QiConstant.COMPANY_NODE)) {
				new QiCompanyPanel<QiRegionalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
			}

			else if (selectedNode[0].equalsIgnoreCase(QiConstant.SITE_NODE)) {
				new QiSitePanel<QiRegionalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
			}

			else if (selectedNode[0].equalsIgnoreCase(QiConstant.PLANT_NODE)) {
				new QiPlantPanel<QiRegionalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
			}

			else if (selectedNode[0].equalsIgnoreCase(QiConstant.DEPARTMENT_NODE)) {
				new QiDepartmentPanel<QiRegionalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}
			}
		}
	};
	
	
	
	 public QiRegionalResponsibilityAssignmentController(QiResponsibilityAssignmentModel model, QiRegionalResponsibilityAssignmentView view) {
		super(model, view);
		this.model = model;
		
	}

	public void handle(ActionEvent event) {
		if (event.getSource().equals(getView().getRefreshButton())){
				refresh(getView().getTree());
				EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
			}
		
	}

	@Override
	public void addContextMenuItems() {
		
	}

	@Override
	public void initEventHandlers() {
		addTreeActionListener();
	}

	/**
	 * This is a listener for tree, will check and display appropriate panel on
	 * particular node selection.
	 * 
	 */
	private void addTreeActionListener() {
		getView().getTree().getSelectionModel().selectedItemProperty().addListener(treeNodeChangeListener);
	}
	
	/** Set all nodes in the tree to expanded state
	 * 
	 * @param item
	 */
	public void setExpanded(TreeItem<?> item) {
		if (item != null && !item.isLeaf()) {
			item.setExpanded(true);
			for (TreeItem<?> child : item.getChildren()) {
				setExpanded(child);
			}
		}
	}
	
	/** This method is used to display tree on initial load.
	 * 
	 * @return
	 */
	public TreeView<String> loadTree() {
		Image activeIcon = new Image("/resource/com/honda/galc/client/images/qi/green.png");
		Image inactiveIcon = new Image("/resource/com/honda/galc/client/images/qi/red.png");

		SortedArrayList<QiCompany> allCompanies = new SortedArrayList<QiCompany>("getCompany");
		SortedArrayList<QiSite> allSites = new SortedArrayList<QiSite>("getSite");
		SortedArrayList<QiPlant> allPlants = new SortedArrayList<QiPlant>("getPlant");
		SortedArrayList<QiDepartment> allDepartments = new SortedArrayList<QiDepartment>("getDepartment");

		allCompanies.addAll(getModel().findAllCompany());
		allSites.addAll(getModel().findAllSites());
		allPlants.addAll(getModel().findAllPlants());
		allDepartments.addAll(getModel().findAllDepartments());
		
		TreeItem<String> root = new TreeItem<String>();

		if (null != allCompanies) {
			for (QiCompany company : allCompanies) {
				getCompanyHierarchy(activeIcon, inactiveIcon, allSites, allPlants, allDepartments, root, company);
			}
		}
		setExpanded(root);
		TreeView<String> tree = new TreeView<String>(root);
		tree.setShowRoot(false);
		return tree;

	}

	/** This function will used to get company hierarchy. 
	 * 
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allSites
	 * @param allPlants
	 * @param allDepartments
	 * @param allResponsibleLevels3
	 * @param allResponsibleLevels2
	 * @param allResponsibleLevels1
	 * @param root
	 * @param company
	 */
	private void getCompanyHierarchy(Image activeIcon, Image inactiveIcon, ArrayList<QiSite> allSites,
			ArrayList<QiPlant> allPlants, ArrayList<QiDepartment> allDepartments,  TreeItem<String> root, QiCompany company) {
		TreeItem<String> companyNode = null;
		if (company.isActive())
			companyNode = addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), activeIcon);
		else
			companyNode =  addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), inactiveIcon);
		
		if(!allSites.isEmpty())
		for (QiSite site : allSites) {
			if (site.getCompany().contentEquals(company.getCompany())) {
				getSiteHierarchy(activeIcon, inactiveIcon, allPlants, allDepartments, companyNode, site);
			}
		}
		root.getChildren().add(companyNode);
	}

	/** This function will used to get site hierarchy. 
	 * 
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allPlants
	 * @param allDepartments
	 * @param allResponsibleLevels3
	 * @param allResponsibleLevels2
	 * @param allResponsibleLevels1
	 * @param companyNode
	 * @param site
	 */
	private void getSiteHierarchy(Image activeIcon, Image inactiveIcon, ArrayList<QiPlant> allPlants, ArrayList<QiDepartment> allDepartments,
			  TreeItem<String> companyNode, QiSite site) {
		TreeItem<String> siteNode = null;
		if (site.isActive())
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), activeIcon);
		else
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), inactiveIcon);
		
		companyNode.getChildren().add(siteNode);
		
		if(!allPlants.isEmpty())
		for (QiPlant plant : allPlants) {
			getPlantHierarchy(activeIcon, inactiveIcon, allDepartments, site, siteNode, plant);
		}
	}

	/** This function will used to get Plant hierarchy. 
	 * 
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allDepartments
	 * @param allResponsibleLevels3
	 * @param allResponsibleLevels2
	 * @param allResponsibleLevels1
	 * @param site
	 * @param siteNode
	 * @param plant
	 */
	private void getPlantHierarchy(Image activeIcon, Image inactiveIcon, ArrayList<QiDepartment> allDepartments, 
			QiSite site, TreeItem<String> siteNode, QiPlant plant) {
		if (plant.getId().getSite().contentEquals(site.getSite())) {
			TreeItem<String> plantNode = null;
			if (plant.isActive())
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), activeIcon);
			else
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), inactiveIcon);

			siteNode.getChildren().add(plantNode);

			if(!allDepartments.isEmpty()){
				for (QiDepartment dept : allDepartments) {
					getDepartmentHierarchy(activeIcon, inactiveIcon, plant, plantNode, dept);
				}
			}
		}
	}

	/** This function will used to get Department hierarchy. 
	 * 
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allResponsibleLevels3
	 * @param allResponsibleLevels2
	 * @param allResponsibleLevels1
	 * @param plant
	 * @param plantNode
	 * @param dept
	 */
	private void getDepartmentHierarchy(Image activeIcon, Image inactiveIcon, QiPlant plant, TreeItem<String> plantNode,
			QiDepartment dept) {
		if (dept.getId().getPlant().contentEquals(plant.getId().getPlant()) && dept.getId().getSite().contentEquals(plant.getId().getSite())) {
			TreeItem<String> deptNode = null;
			if (dept.isActive())
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(),activeIcon);
			else
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(), inactiveIcon);

			plantNode.getChildren().add(deptNode);
		}
	}

	/** Returns  a tree node with node and icon specified
	 * 
	 * @param nodeText
	 * @param icon
	 * @return
	 */
	private TreeItem<String> addNode(String nodeText, Image icon) {
		return new TreeItem<String>(nodeText, new ImageView(icon));
	}
	
	/** This method will set the tree hierarchy for supplied treeItem to in-activate state i.e. RED
	 * 
	 * @param item
	 */
	protected void inactivateChilds(TreeItem<?> item) {
		ImageView inactiveIcon = new ImageView(new Image("/resource/com/honda/galc/client/images/qi/red.png"));
		if (item != null) {
			item.setGraphic(inactiveIcon);
			if(!item.isLeaf()){
				for (TreeItem<?> child : item.getChildren()) {
					inactivateChilds(child);
				}
			}
		}
	}

	/**
	 * This method will in-activate all the available responsible levels for the provided department
	 * 
	 * @param department
	 * @return
	 */
	protected void inactivateResponsibleLevelByDepartment(QiDepartmentId id) {
		ArrayList<QiResponsibleLevel> allResponsibleLevels = new ArrayList<QiResponsibleLevel>();
		allResponsibleLevels.addAll(getModel().findAllResponsibleLevelBySitePlantDepartment(id.getSite(), id.getPlant(), id.getDepartment()));
				
		if (!allResponsibleLevels.isEmpty())
			for (QiResponsibleLevel responsibleLevel : allResponsibleLevels) {
					if (responsibleLevel.isActive()) {
						getModel().inactivateResponsibleLevel(responsibleLevel.getResponsibleLevelId());
					}
				}
	}

	/**
	 * This method will in-activate all the available departments for the provided plant
	 * 
	 * @param id
	 */
	protected void inactivateDepartmentByPlant(QiPlantId id) {
		ArrayList<QiDepartment> allDepartments = new ArrayList<QiDepartment>();
		allDepartments.addAll(getModel().findAllDepartmentBySiteAndPlant(id.getSite(), id.getPlant()));
		
		if (!allDepartments.isEmpty())
			for (QiDepartment dept : allDepartments) {
					if (dept.isActive()) {
						getModel().inactivatDepartment(dept.getId());
					}
					inactivateResponsibleLevelByDepartment(dept.getId());
				}
	}

	/**
	 * This method will in-activate all the available plants for the provided site
	 * 
	 * @param site
	 */
	protected void inactivatePlantBySite(String site) {
		ArrayList<QiPlant> allPlants = new ArrayList<QiPlant>();
		allPlants.addAll(getModel().findAllPlantBySite(site));

		if (!allPlants.isEmpty())
			for (QiPlant plant : allPlants) {
					if (plant.isActive()) {
						getModel().inactivatePlant(plant.getId());
					}
					inactivateDepartmentByPlant(plant.getId());
				}
	}

	/**
	 * This method will in-activate site for the provided company
	 * 
	 * @param company
	 */
	protected void inactivateSiteByCompany(String company) {
		ArrayList<QiSite> allSites = new ArrayList<QiSite>();
		allSites.addAll(getModel().findAllSiteByCompany(company));
		if (!allSites.isEmpty())
			for (QiSite site : allSites) {
					if (site.isActive()) {
						getModel().inactivateSite(site.getSite());
					}
					inactivatePlantBySite(site.getSite());
				}
	}

	/**
	 * This method will in-activate all the available responsible levels for the provided department
	 * 
	 * @param department
	 * @return
	 */
	protected void inactivateResponsibleLevelByUpperResponsibleLevel(int id) {
		ArrayList<QiResponsibleLevel> childResponsibleLevelHigher = new ArrayList<QiResponsibleLevel>();
		childResponsibleLevelHigher.addAll(getModel().findResponsibleLevelListByUpperResponsibleLevel(id));

		if (!childResponsibleLevelHigher.isEmpty())
			for (QiResponsibleLevel responsibleLevel : childResponsibleLevelHigher) {
				ArrayList<QiResponsibleLevel> childResponsibleLevelLower = new ArrayList<QiResponsibleLevel>();
				childResponsibleLevelLower.addAll(getModel().findResponsibleLevelListByUpperResponsibleLevel(Integer.valueOf(responsibleLevel.getId().toString())));

				if (!childResponsibleLevelLower.isEmpty())
					for (QiResponsibleLevel responsibleLevelLower : childResponsibleLevelLower) {
						if (responsibleLevelLower.isActive()) {
							getModel().inactivateResponsibleLevel(responsibleLevelLower.getResponsibleLevelId());
						}
					}

				if (responsibleLevel.isActive()) {
					getModel().inactivateResponsibleLevel(responsibleLevel.getResponsibleLevelId());
				}
			}	
	}
	
	
	/**
	 *  This method will open create company dialog if no company is present.
	 */
	public void createCompanyIfNonExisting(){
		if(getView().getTree().getRoot().getChildren().size()==0){
			QiResponsibilityPanelDialog<QiCompanyPanel<QiRegionalResponsibilityAssignmentView>, QiRegionalResponsibilityAssignmentView> dialog 
			= new QiResponsibilityPanelDialog<QiCompanyPanel<QiRegionalResponsibilityAssignmentView>, QiRegionalResponsibilityAssignmentView>(
					"Create Company", getView().getMainWindow(), getView(), getModel(), QiConstant.COMPANY_NODE,getApplicationId());
			dialog.show();
			dialog.getPanel().getController().setPanel(dialog.getPanel());
			dialog.getPanel().getController().initListeners();
		}
	}

	
	/**This method will return list of available sites for the company
	 *
	 * @param company
	 * @return
	 */
	protected List<QiSite> getSiteListByCompany(String company) {
		return getModel().findAllSiteByCompany(company);
	}
	
	
	/** This method will return list of available plants for the provided site
	 * 
	 * @param site
	 * @return
	 */
	protected List<QiPlant> getPlantListBySite(QiSite site) {
		return getModel().findAllPlantBySite(site.getSite().trim());
	}

	/** This method will return list of available departments for the provided plant
	 *
	 * @param plant
	 * @return
	 */
	protected List<QiDepartment> getDepartmentListByPlant(QiPlant plant) {
		return getModel().findAllDepartmentBySiteAndPlant(plant.getId().getSite().trim(),plant.getId().getPlant().trim());
	}

	/** This method will return list of available responsible levels for the provided department
	 * 
	 * @param department
	 * @return
	 */
	protected List<QiResponsibleLevel> getResponsibleLevelListByDepartment(QiDepartment department) {
		return getModel().findAllResponsibleLevelBySitePlantDepartment(department.getId().getSite().trim(),
				department.getId().getPlant().trim(), department.getId().getDepartment().trim());
	}

	

	public void refresh(TreeView<String> tree) {

		clearDisplayMessage();
		
		getView().getTree().getSelectionModel().selectedItemProperty().removeListener(treeNodeChangeListener );

		TreeItem<String> selectedNode = tree.getSelectionModel().getSelectedItem();
		int selectedNodeIndex = tree.getSelectionModel().getSelectedIndex();
		
		tree.getRoot().getChildren().clear();	

		Image activeIcon = new Image("/resource/com/honda/galc/client/images/qi/green.png");
		Image inactiveIcon = new Image("/resource/com/honda/galc/client/images/qi/red.png");

		SortedArrayList<QiCompany> allCompanies = new SortedArrayList<QiCompany>("getCompany");
		SortedArrayList<QiSite> allSites = new SortedArrayList<QiSite>("getSite");
		SortedArrayList<QiPlant> allPlants = new SortedArrayList<QiPlant>("getPlant");
		SortedArrayList<QiDepartment> allDepartments = new SortedArrayList<QiDepartment>("getDepartment");

		allCompanies.addAll(getModel().findAllCompany());
		allSites.addAll(getModel().findAllSites());
		allPlants.addAll(getModel().findAllPlants());
		allDepartments.addAll(getModel().findAllDepartments());

		if (null != allCompanies) {
			for (QiCompany company : allCompanies) {
				getCompanyHierarchy(activeIcon, inactiveIcon, allSites, allPlants, allDepartments, tree.getRoot(), company);
			}
		}
		
		setExpanded(tree.getRoot());
		tree.setShowRoot(false);

		addTreeActionListener();
		
		if (null != selectedNode) {
			if(tree.getRoot().getChildren().contains(selectedNode))
				tree.getSelectionModel().select(selectedNode);
			else
				tree.getSelectionModel().select(selectedNodeIndex);
		}

		
	}

	
}
