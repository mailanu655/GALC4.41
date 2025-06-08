package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.QiResponsibleMappingAssignmentView;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPlant;
import com.honda.galc.entity.qi.QiResponsibilityMapping;
import com.honda.galc.entity.qi.QiResponsibilityMappingId;
import com.honda.galc.entity.qi.QiResponsibleLevel;
import com.honda.galc.entity.qi.QiSite;
import com.honda.galc.util.SortedArrayList;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**   
 * @author Bernard Leong
 * @since Jan 9, 2019
 * RGALCPROD-6055 - Enhance NA QICS to assign responsible dept for Engine defects based on source
 */
public class QiResponsibilityMappingController extends AbstractQiController<QiResponsibilityAssignmentModel, QiResponsibleMappingAssignmentView> implements EventHandler<ActionEvent>{
	public QiResponsibilityMappingController(QiResponsibilityAssignmentModel model,
			QiResponsibleMappingAssignmentView view) {
		super(model, view);
	}

	private ChangeListener<TreeItem<String>> treeNodeChangeListener = new ChangeListener<TreeItem<String>>() {

		public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
			enableDisableSaveButton();
		}
	};

	public void handle(ActionEvent event) {
		if (event.getSource().equals(getView().getSaveButton())) {
			TreeItem<String> defaultRespLevel = getView().getLeftTree().getSelectionModel().getSelectedItem();
			TreeItem<String> respLevel = getView().getRightTree().getSelectionModel().getSelectedItem();
			Object plantCode = getView().getPlantCombobox().getSelectionModel().getSelectedItem();
			String[] respValue = defaultRespLevel.getValue().split(" ");
			QiResponsibleLevel defaultResp = getRespLevel(defaultRespLevel, respValue);
			String[] altRespValue = respLevel.getValue().split(" ");
			QiResponsibleLevel altResp = getRespLevel(respLevel, altRespValue);
			if (defaultResp != null && altResp != null) {
				boolean saved = getModel().insertResponsibleMapping(defaultResp.getResponsibleLevelId().intValue(),
						altResp.getResponsibleLevelId().intValue(), plantCode.toString(), getUserId());
				if (saved) {
					EventBusUtil.publish(new StatusMessageEvent("Saved successfully", StatusMessageEventType.INFO));
					reloadTable();
				} else
					EventBusUtil
							.publish(new StatusMessageEvent("Default Resp " + defaultResp.getResponsibleLevelId() +
									" and Engine Source " + plantCode.toString() + " exist. No save", 
									StatusMessageEventType.WARNING));
			}
		}
		
		if (getView().getResplevelTablePane().getSelectedItem() != null && event.getSource() instanceof MenuItem) {
			MenuItem menuItem = (MenuItem) event.getSource();
			if (QiConstant.DELETE.equals(menuItem.getText())) {
				QiResponsibilityMappingId respLevel = getView().getResplevelTablePane().getSelectedItem().getId();
				getModel().deleteResponsibleMapping(respLevel.getDefaultRespLevelId(), respLevel.getPlantCode());
				EventBusUtil.publish(new StatusMessageEvent("Deleted successfully", StatusMessageEventType.INFO));
				reloadTable();
			}
		}
		
		if (event.getSource().equals(getView().getRefreshButton())) {
			reloadTable();
			getView().loadPlantCombobox();
			EventBusUtil.publish(new StatusMessageEvent("Refreshed successfully", StatusMessageEventType.INFO));
		}
	}

	private QiResponsibleLevel getRespLevel(TreeItem<String> defaultRespLevel, String[] respValue) {
		return getModel().getResponsibleLevelId(getSpecifiedNodeValue(defaultRespLevel,"Site"), 
				getSpecifiedNodeValue(defaultRespLevel,"Plant"), 
				getSpecifiedNodeValue(defaultRespLevel,"Department"), 
				respValue[3], respValue[5]);
	}

	private void enableDisableSaveButton() {
		if (isActiveRespLevelNode(getView().getLeftTree().getSelectionModel().getSelectedItem()) 
				&& isActiveRespLevelNode(getView().getRightTree().getSelectionModel().getSelectedItem()) 
					&& isValidPlantCode(getView().getPlantCombobox().getSelectionModel().getSelectedItem())) {
			getView().getSaveButton().setDisable(false);	
		} else {
			getView().getSaveButton().setDisable(true);
		}
	}
	
	private boolean isValidPlantCode(Object plantCode) {
		if (plantCode == null) return false;
		return !plantCode.toString().trim().equals("");
	}
	
	private boolean isActiveRespLevelNode(TreeItem<String> selectedItem) {
		if (selectedItem == null) return false;
		if (selectedItem.getValue().contains("Responsible Level")) {
			String[] respValue = selectedItem.getValue().split(" ");
			QiResponsibleLevel defaultResp = getRespLevel(selectedItem, respValue);
			if (defaultResp.isActive()) return true;
		}
		return false;
	}
	
	private String getSpecifiedNodeValue(TreeItem<String> selectedItem, String nodeName) {
		TreeItem<String> node = selectedItem;
		while (node != null) {
			String[] value = node.getValue().split(" ");
			if (nodeName.equalsIgnoreCase("Responsible Level") && node.getValue().contains("Responsible Level")) {
				return value[5];
			}
			if (nodeName.equalsIgnoreCase("Department") && node.getValue().contains("Department")) {
				return value[2];
			}
			if (nodeName.equalsIgnoreCase("Plant") && node.getValue().contains("Plant")) {
				return value[2];
			}
			if (nodeName.equalsIgnoreCase("Site") && node.getValue().contains("Site")) {
				return value[2];
			}
			node = node.getParent();
		}
		return "";
	}
	
	@Override
	public void addContextMenuItems() {
		if (getView().getResplevelTablePane().getSelectedItem() != null) {
			String[] menuItems = new String[] { QiConstant.DELETE};
			getView().getResplevelTablePane().createContextMenu(menuItems, this);
		}
	}

	@Override
	public void initEventHandlers() {
		addLeftTreeActionListener();
		addRightTreeActionListener();
		addPlantCodeComboBoxListener();
		addTableListener();
	}

	/**
	 * This is a listener for tree, will check and display appropriate panel on
	 * particular node selection.
	 * 
	 */
	private void addLeftTreeActionListener() {
		getView().getLeftTree().getSelectionModel().selectedItemProperty().addListener(treeNodeChangeListener );
	}
	
	private void addRightTreeActionListener() {
		getView().getRightTree().getSelectionModel().selectedItemProperty().addListener(treeNodeChangeListener );
	}
	
	private void addTreeActionListener(TreeView<String> tree) {
		getView().getTree(tree).getSelectionModel().selectedItemProperty().addListener(treeNodeChangeListener );
	}
	
	private void addTableListener() {
		getView().getResplevelTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiResponsibilityMapping>() {
			public void changed(ObservableValue<? extends QiResponsibilityMapping> arg0,
					QiResponsibilityMapping arg1, QiResponsibilityMapping arg2) {
				addContextMenuItems();
			}
		});
		getView().getResplevelTablePane().getTable().focusedProperty().addListener(new InvalidationListener() {
			public void invalidated(Observable arg0) {
				addContextMenuItems();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void addPlantCodeComboBoxListener() {
		getView().getPlantCombobox().valueProperty().addListener(plantCodeComboxBoxChangeListener);
	}

	private final ChangeListener<String> plantCodeComboxBoxChangeListener = new ChangeListener<String>() {
		@SuppressWarnings("rawtypes")
		public void changed(ObservableValue ov, String t, String t1) {enableDisableSaveButton();}
	};
	
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
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels3 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels2 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels1 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");

		allCompanies.addAll(getModel().findAllCompany());
		allSites.addAll(getModel().findAllSites());
		allPlants.addAll(getModel().findAllPlants());
		allDepartments.addAll(getModel().findAllDepartments());
		allResponsibleLevels3.addAll(getModel().findResponsibleLevelsByLevel((short) 3));
		allResponsibleLevels2.addAll(getModel().findResponsibleLevelsByLevel((short) 2));
		allResponsibleLevels1.addAll(getModel().findResponsibleLevelsByLevel((short) 1));

		
		TreeItem<String> root = new TreeItem<String>();

		if (null != allCompanies) {
			for (QiCompany company : allCompanies) {
				getCompanyHierarchy(activeIcon, inactiveIcon, allSites, allPlants, allDepartments,
						allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, root, company);
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
			ArrayList<QiPlant> allPlants, ArrayList<QiDepartment> allDepartments, ArrayList<QiResponsibleLevel> allResponsibleLevels3,
			ArrayList<QiResponsibleLevel> allResponsibleLevels2, ArrayList<QiResponsibleLevel> allResponsibleLevels1, TreeItem<String> root, QiCompany company) {
		TreeItem<String> companyNode = null;
		if (company.isActive())
			companyNode = addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), activeIcon);
		else
			companyNode =  addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), inactiveIcon);

		if(!allSites.isEmpty())
			for (QiSite site : allSites) {
				if (site.getCompany().contentEquals(company.getCompany())) {
					getSiteHierarchy(activeIcon, inactiveIcon, allPlants, allDepartments, allResponsibleLevels3,
							allResponsibleLevels2, allResponsibleLevels1, companyNode, site);
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
			ArrayList<QiResponsibleLevel> allResponsibleLevels3, ArrayList<QiResponsibleLevel> allResponsibleLevels2, 
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, TreeItem<String> companyNode, QiSite site) {
		TreeItem<String> siteNode = null;
		if (site.isActive())
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), activeIcon);
		else
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), inactiveIcon);

		companyNode.getChildren().add(siteNode);

		if(!allPlants.isEmpty())
			for (QiPlant plant : allPlants) {
				getPlantHierarchy(activeIcon, inactiveIcon, allDepartments, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, site, siteNode, plant);
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
			ArrayList<QiResponsibleLevel> allResponsibleLevels3, ArrayList<QiResponsibleLevel> allResponsibleLevels2,
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, QiSite site, TreeItem<String> siteNode, QiPlant plant) {
		if (plant.getId().getSite().contentEquals(site.getSite())) {
			TreeItem<String> plantNode = null;
			if (plant.isActive())
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), activeIcon);
			else
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), inactiveIcon);

			siteNode.getChildren().add(plantNode);

			if(!allDepartments.isEmpty()){
				for (QiDepartment dept : allDepartments) {
					getDepartmentHierarchy(activeIcon, inactiveIcon, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, plant, plantNode, dept);
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
	private void getDepartmentHierarchy(Image activeIcon, Image inactiveIcon,
			ArrayList<QiResponsibleLevel> allResponsibleLevels3, ArrayList<QiResponsibleLevel> allResponsibleLevels2,
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, QiPlant plant, TreeItem<String> plantNode,
			QiDepartment dept) {
		if (dept.getId().getPlant().contentEquals(plant.getId().getPlant()) && dept.getId().getSite().contentEquals(plant.getId().getSite())) {
			TreeItem<String> deptNode = null;
			if (dept.isActive())
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(),activeIcon);
			else
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(), inactiveIcon);

			plantNode.getChildren().add(deptNode);

			if(!allResponsibleLevels3.isEmpty())
				for (QiResponsibleLevel responsibleLevel3 : allResponsibleLevels3) {
					getResponsibleLevel3Hierarchy(activeIcon, inactiveIcon, allResponsibleLevels2, allResponsibleLevels1, dept, deptNode, responsibleLevel3);
				}

			if(!allResponsibleLevels2.isEmpty())
				for (QiResponsibleLevel responsibleLevel2 : allResponsibleLevels2) {
					if (responsibleLevel2.getDepartment().contentEquals(dept.getId().getDepartment()) && responsibleLevel2.getSite().contentEquals(dept.getId().getSite())
							&& responsibleLevel2.getPlant().contentEquals(dept.getId().getPlant()) && (responsibleLevel2.getLevel() == 2)
							&& (responsibleLevel2.getUpperResponsibleLevelId() == 0)) {
						TreeItem<String> orphanResponsibleLevel2 = null;
						if (responsibleLevel2.isActive())
							orphanResponsibleLevel2 = addNode(responsibleLevel2.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), activeIcon);
						else
							orphanResponsibleLevel2 = addNode(responsibleLevel2.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), inactiveIcon);

						getResposibleLevel1(activeIcon, inactiveIcon, allResponsibleLevels1, dept, responsibleLevel2,
								orphanResponsibleLevel2);

						deptNode.getChildren().add(orphanResponsibleLevel2);
					}
				}

			for (QiResponsibleLevel responsibleLevel1 : allResponsibleLevels1) {
				if (responsibleLevel1.getDepartment().contentEquals(dept.getId().getDepartment())
						&& responsibleLevel1.getSite().contentEquals(dept.getId().getSite()) && responsibleLevel1.getPlant().contentEquals(dept.getId().getPlant())
						&& (responsibleLevel1.getLevel() == 1) && (responsibleLevel1.getUpperResponsibleLevelId() == 0)) {
					TreeItem<String> orphanResponsibleLevel1Node = null;
					if (responsibleLevel1.isActive())
						orphanResponsibleLevel1Node = addNode(responsibleLevel1.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), activeIcon);
					else
						orphanResponsibleLevel1Node = addNode(responsibleLevel1.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), inactiveIcon);

					deptNode.getChildren().add(orphanResponsibleLevel1Node);
				}				
			}

		}
	}

	/**
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allResponsibleLevels1
	 * @param dept
	 * @param responsibleLevel2
	 * @param orphanResponsibleLevel2
	 */
	private void getResposibleLevel1(Image activeIcon, Image inactiveIcon,
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, QiDepartment dept,
			QiResponsibleLevel responsibleLevel2, TreeItem<String> orphanResponsibleLevel2) {
		if(!allResponsibleLevels1.isEmpty())
			for (QiResponsibleLevel responsibleLevel1 : allResponsibleLevels1) {
				if (responsibleLevel1.getDepartment().contentEquals(dept.getId().getDepartment())
						&& responsibleLevel1.getSite().contentEquals(dept.getId().getSite())
						&& responsibleLevel1.getPlant().contentEquals(dept.getId().getPlant()) && (responsibleLevel1.getLevel() == 1)
						&& (responsibleLevel2.getResponsibleLevelId().intValue() == responsibleLevel1.getUpperResponsibleLevelId().intValue())) {
					TreeItem<String> responsibleLevel1Node = null;
					if (responsibleLevel1.isActive())
						responsibleLevel1Node = addNode(responsibleLevel1.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), activeIcon);
					else
						responsibleLevel1Node = addNode(responsibleLevel1.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), inactiveIcon);

					orphanResponsibleLevel2.getChildren().add(responsibleLevel1Node);
				}
			}
	}

	/** This function will used to get Responsible level 3 hierarchy. 
	 * 
	 * @param activeIcon
	 * @param inactiveIcon
	 * @param allResponsibleLevels2
	 * @param allResponsibleLevels1
	 * @param dept
	 * @param deptNode
	 * @param responsibleLevel3
	 */
	private void getResponsibleLevel3Hierarchy(Image activeIcon, Image inactiveIcon,
			ArrayList<QiResponsibleLevel> allResponsibleLevels2, ArrayList<QiResponsibleLevel> allResponsibleLevels1,
			QiDepartment dept, TreeItem<String> deptNode, QiResponsibleLevel responsibleLevel3) {
		if (responsibleLevel3.getDepartment().contentEquals(dept.getId().getDepartment()) && responsibleLevel3.getSite().contentEquals(dept.getId().getSite())
				&& responsibleLevel3.getPlant().contentEquals(dept.getId().getPlant()) && responsibleLevel3.getLevel() == 3) {
			TreeItem<String> responsibleLevel3Node = null;

			if (responsibleLevel3.isActive())
				responsibleLevel3Node=addNode(responsibleLevel3.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + responsibleLevel3.getResponsibleLevelName(), activeIcon);
			else
				responsibleLevel3Node=addNode(responsibleLevel3.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + responsibleLevel3.getResponsibleLevelName(), inactiveIcon);

			deptNode.getChildren().add(responsibleLevel3Node);

			if(!allResponsibleLevels2.isEmpty())
				for (QiResponsibleLevel responsibleLevel2 : allResponsibleLevels2) {
					if (responsibleLevel2.getDepartment().contentEquals(dept.getId().getDepartment()) && responsibleLevel2.getSite().contentEquals(dept.getId().getSite())
							&& responsibleLevel2.getPlant().contentEquals(dept.getId().getPlant()) && (responsibleLevel2.getLevel() == 2)
							&& (responsibleLevel3.getResponsibleLevelId().intValue() == responsibleLevel2.getUpperResponsibleLevelId().intValue())) {
						TreeItem<String> responsibleLevel2Node = null;
						if (responsibleLevel2.isActive())
							responsibleLevel2Node = addNode(responsibleLevel2.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), activeIcon);
						else
							responsibleLevel2Node = addNode(responsibleLevel2.getResponsibleLevelId() + " " + QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), inactiveIcon);

						responsibleLevel3Node.getChildren().add(responsibleLevel2Node);

						getResposibleLevel1(activeIcon, inactiveIcon, allResponsibleLevels1, dept, responsibleLevel2,
								responsibleLevel2Node);
					}
				}
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


	public void reloadTable() {
		getView().getResplevelTablePane().setData(getModel().findAll());
	}
	
	public void refresh(TreeView<String> tree) {

		clearDisplayMessage();
		
		getView().getTree(tree).getSelectionModel().selectedItemProperty().removeListener(treeNodeChangeListener );

		TreeItem<String> selectedNode = tree.getSelectionModel().getSelectedItem();
		int selectedNodeIndex = tree.getSelectionModel().getSelectedIndex();
		
		tree.getRoot().getChildren().clear();	

		Image activeIcon = new Image("/resource/com/honda/galc/client/images/qi/green.png");
		Image inactiveIcon = new Image("/resource/com/honda/galc/client/images/qi/red.png");

		SortedArrayList<QiCompany> allCompanies = new SortedArrayList<QiCompany>("getCompany");
		SortedArrayList<QiSite> allSites = new SortedArrayList<QiSite>("getSite");
		SortedArrayList<QiPlant> allPlants = new SortedArrayList<QiPlant>("getPlant");
		SortedArrayList<QiDepartment> allDepartments = new SortedArrayList<QiDepartment>("getDepartment");
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels3 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels2 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");
		SortedArrayList<QiResponsibleLevel> allResponsibleLevels1 = new SortedArrayList<QiResponsibleLevel>("getResponsibleLevelName");

		allCompanies.addAll(getModel().findAllCompany());
		allSites.addAll(getModel().findAllSites());
		allPlants.addAll(getModel().findAllPlants());
		allDepartments.addAll(getModel().findAllDepartments());
		allResponsibleLevels3.addAll(getModel().findResponsibleLevelsByLevel((short) 3));
		allResponsibleLevels2.addAll(getModel().findResponsibleLevelsByLevel((short) 2));
		allResponsibleLevels1.addAll(getModel().findResponsibleLevelsByLevel((short) 1));

		if (null != allCompanies) {
			for (QiCompany company : allCompanies) {
				getCompanyHierarchy(activeIcon, inactiveIcon, allSites, allPlants, allDepartments,
						allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, tree.getRoot(), company);
			}
		} 
		
		setExpanded(tree.getRoot());
		tree.setShowRoot(false);

		addTreeActionListener(tree);
		
		if (null != selectedNode) {
			if(tree.getRoot().getChildren().contains(selectedNode))
				tree.getSelectionModel().select(selectedNode);
			else
				tree.getSelectionModel().select(selectedNodeIndex);
		}
	}
}
