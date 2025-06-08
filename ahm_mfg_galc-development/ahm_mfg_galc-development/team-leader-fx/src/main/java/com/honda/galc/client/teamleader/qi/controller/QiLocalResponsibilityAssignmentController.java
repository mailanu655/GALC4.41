package com.honda.galc.client.teamleader.qi.controller;

import java.util.ArrayList;
import java.util.Iterator;

import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.teamleader.qi.view.QiCompanyPanel;
import com.honda.galc.client.teamleader.qi.view.QiDepartmentPanel;
import com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView;
import com.honda.galc.client.teamleader.qi.view.QiPlantPanel;
import com.honda.galc.client.teamleader.qi.view.QiResponsibleLevelPanel;
import com.honda.galc.client.teamleader.qi.view.QiSitePanel;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.qi.QiCompany;
import com.honda.galc.entity.qi.QiDepartment;
import com.honda.galc.entity.qi.QiPlant;
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
 * <code>QiResponsibilityAssignmentController</code> is the controller class for QiResponsibilityAssignmentView Panel i.e. Hierarchy Screen.
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
/**
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
public class QiLocalResponsibilityAssignmentController extends AbstractQiController<QiResponsibilityAssignmentModel, QiLocalResponsibilityAssignmentView> implements EventHandler<ActionEvent>{

	private QiResponsibilityAssignmentModel model;
	private boolean itemSelected = false;
	Image activeIcon = new Image("/resource/com/honda/galc/client/images/qi/green.png");
	Image inactiveIcon = new Image("/resource/com/honda/galc/client/images/qi/red.png");

	private ChangeListener<TreeItem<String>> treeNodeChangeListener = new ChangeListener<TreeItem<String>>() {

		public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
			
			if(newValue != null && newValue.getValue() != null){
				Logger.getLogger().check("Local Responsiblity Tree node :: " + newValue.getValue() + " selected");
			}
				
			
			clearDisplayMessage();
			getView().getContentPane().getChildren().clear();

			if(newValue!= null){
				String[] selectedNode = newValue.getValue().toString().split("-");
				if (selectedNode[0].equalsIgnoreCase(QiConstant.COMPANY_NODE)) {
					new QiCompanyPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].equalsIgnoreCase(QiConstant.SITE_NODE)) {
					new QiSitePanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].equalsIgnoreCase(QiConstant.PLANT_NODE)) {
					new QiPlantPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].equalsIgnoreCase(QiConstant.DEPARTMENT_NODE)) {
					new QiDepartmentPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].contains(QiConstant.RESPONSIBLE_LEVEL3_NODE)) {
					new QiResponsibleLevelPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].contains(QiConstant.RESPONSIBLE_LEVEL2_NODE)) {
					new QiResponsibleLevelPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}

				else if (selectedNode[0].contains(QiConstant.RESPONSIBLE_LEVEL1_NODE)) {
					new QiResponsibleLevelPanel<QiLocalResponsibilityAssignmentView>(model, getView().getMainWindow(), getView());
				}
			}
		}
	};

	public QiLocalResponsibilityAssignmentController(QiResponsibilityAssignmentModel model, QiLocalResponsibilityAssignmentView view) {
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
		addStatusComboBoxListener();
	}

	/**
	 * This is a listener for tree, will check and display appropriate panel on
	 * particular node selection.
	 * 
	 */
	private void addTreeActionListener() {
		getView().getTree().getSelectionModel().selectedItemProperty().addListener(treeNodeChangeListener );
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
						allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, root, company,false,true);
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
			ArrayList<QiResponsibleLevel> allResponsibleLevels2, ArrayList<QiResponsibleLevel> allResponsibleLevels1, TreeItem<String> root, QiCompany company,
			boolean showActiveOnly, boolean showBoth) {
		TreeItem<String> companyNode = null;
		if (company.isActive())
			companyNode = addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), activeIcon);
		else
			companyNode =  addNode(QiConstant.COMPANY_NODE + "- " + company.getCompany(), inactiveIcon);
		
		if (companyNode != null && !allSites.isEmpty()) {
			for (QiSite site : allSites) {
				if (site.getCompany().contentEquals(company.getCompany())) {
					getSiteHierarchy(activeIcon, inactiveIcon,allPlants, allDepartments, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, companyNode, site, showActiveOnly,showBoth);
				}
			}
			if(!showActiveOnly && company.isActive() && companyNode.getChildren().size() > 0 ) {
				root.getChildren().add(companyNode);
			} else if(!showActiveOnly && !company.isActive()) {
				root.getChildren().add(companyNode);
			} else if(showActiveOnly && company.isActive()) {
				root.getChildren().add(companyNode);
			} else if(showActiveOnly && !company.isActive() && companyNode.getChildren().size() > 0 ) {
				root.getChildren().add(companyNode);
			}
		}
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
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, TreeItem<String> companyNode, QiSite site,
			boolean showActiveOnly, boolean showBoth) {
		TreeItem<String> siteNode = null;
		if (site.isActive())
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), activeIcon);
		else
			siteNode = addNode(QiConstant.SITE_NODE + "- " + site.getSite(), inactiveIcon);
		
		if (siteNode != null && !allPlants.isEmpty()) {	
			for (QiPlant plant : allPlants) {
				getPlantHierarchy(activeIcon, inactiveIcon, allDepartments, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, site, siteNode, plant, showActiveOnly, showBoth);
			}
			if(showBoth) {
				companyNode.getChildren().add(siteNode);
			}else if(!showActiveOnly && site.isActive() && siteNode.getChildren().size() > 0 ) {
				companyNode.getChildren().add(siteNode);
			}else if(!showActiveOnly && !site.isActive()) {
				companyNode.getChildren().add(siteNode);
			} else if(showActiveOnly && site.isActive()) {
				companyNode.getChildren().add(siteNode);
			} else if(showActiveOnly && !site.isActive() && siteNode.getChildren().size() > 0 ) {
				companyNode.getChildren().add(siteNode);
			}
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
			ArrayList<QiResponsibleLevel> allResponsibleLevels1, QiSite site, TreeItem<String> siteNode, QiPlant plant,
			boolean showActiveOnly, boolean showBoth) {
		if (plant.getId().getSite().contentEquals(site.getSite())) {
			TreeItem<String> plantNode = null;
			if (plant.isActive())
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), activeIcon);
			else
				plantNode = addNode(QiConstant.PLANT_NODE + "- " + plant.getId().getPlant(), inactiveIcon);
			
			if (plantNode != null && !allDepartments.isEmpty()) {
				for (QiDepartment dept : allDepartments) {
					getDepartmentHierarchy(activeIcon, inactiveIcon, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, plant, plantNode, dept, showActiveOnly, showBoth);	
				}
				if(showBoth) {
					siteNode.getChildren().add(plantNode);
				}else if(!showActiveOnly && !plant.isActive()) {
					siteNode.getChildren().add(plantNode);
				} else if(showActiveOnly && plant.isActive()) {
					siteNode.getChildren().add(plantNode);
				} else if(!showActiveOnly && plant.isActive() && plantNode.getChildren().size() > 0) {
					siteNode.getChildren().add(plantNode);
				} else if(showActiveOnly && !plant.isActive() && plantNode.getChildren().size() > 0) {
					siteNode.getChildren().add(plantNode);
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
			QiDepartment dept, boolean showActiveOnly, boolean showBoth) {
		if (dept.getId().getPlant().contentEquals(plant.getId().getPlant()) && dept.getId().getSite().contentEquals(plant.getId().getSite())) {
			TreeItem<String> deptNode = null;
			if (dept.isActive())
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(),activeIcon);
			else
				deptNode = addNode(QiConstant.DEPARTMENT_NODE + "- " + dept.getId().getDepartment(), inactiveIcon);

			if (deptNode != null && !allResponsibleLevels3.isEmpty()) {					
				for (QiResponsibleLevel responsibleLevel3 : allResponsibleLevels3) {
					getResponsibleLevel3Hierarchy(activeIcon, inactiveIcon, allResponsibleLevels2, allResponsibleLevels1, dept, deptNode, responsibleLevel3, showActiveOnly, showBoth);
				}			
			}
			if (deptNode != null && !allResponsibleLevels2.isEmpty()) {
				for (QiResponsibleLevel responsibleLevel2 : allResponsibleLevels2) {
					if (responsibleLevel2.getDepartment().contentEquals(dept.getId().getDepartment()) && responsibleLevel2.getSite().contentEquals(dept.getId().getSite())
							&& responsibleLevel2.getPlant().contentEquals(dept.getId().getPlant()) && (responsibleLevel2.getLevel() == 2)
							&& (responsibleLevel2.getUpperResponsibleLevelId() == 0)) {
						TreeItem<String> orphanResponsibleLevel2 = null;
						if (responsibleLevel2.isActive()) {
							orphanResponsibleLevel2 = addNode(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), activeIcon);
						} else { 
							orphanResponsibleLevel2 = addNode(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), inactiveIcon);
						}
						if (orphanResponsibleLevel2 != null) {
							getResposibleLevel1(activeIcon, inactiveIcon, allResponsibleLevels1, dept, responsibleLevel2,	orphanResponsibleLevel2, showActiveOnly, showBoth);
							if(showBoth) {
								deptNode.getChildren().add(orphanResponsibleLevel2);
							}else if(!showActiveOnly && !responsibleLevel2.isActive()) {
								deptNode.getChildren().add(orphanResponsibleLevel2);
							} else if(showActiveOnly && responsibleLevel2.isActive()) {
								deptNode.getChildren().add(orphanResponsibleLevel2);
							} else if(!showActiveOnly && responsibleLevel2.isActive() && orphanResponsibleLevel2.getChildren().size() > 0) {
								deptNode.getChildren().add(orphanResponsibleLevel2);
							} else if(showActiveOnly && !responsibleLevel2.isActive() && orphanResponsibleLevel2.getChildren().size() > 0) {
								deptNode.getChildren().add(orphanResponsibleLevel2);
							}

						}
					}
				}
			}
			for (QiResponsibleLevel responsibleLevel1 : allResponsibleLevels1) {
				if (responsibleLevel1.getDepartment().contentEquals(dept.getId().getDepartment())
						&& responsibleLevel1.getSite().contentEquals(dept.getId().getSite()) && responsibleLevel1.getPlant().contentEquals(dept.getId().getPlant())
						&& (responsibleLevel1.getLevel() == 1) && (responsibleLevel1.getUpperResponsibleLevelId() == 0)) {
					TreeItem<String> orphanResponsibleLevel1Node = null;
					if (responsibleLevel1.isActive()) {
						orphanResponsibleLevel1Node = addNode(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), activeIcon);
					} else {
						orphanResponsibleLevel1Node = addNode(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), inactiveIcon);
					}
					if (orphanResponsibleLevel1Node != null) {
						if(showBoth) {
							deptNode.getChildren().add(orphanResponsibleLevel1Node);
						}else if(!showActiveOnly && !responsibleLevel1.isActive()) {
							deptNode.getChildren().add(orphanResponsibleLevel1Node);
						} else if(showActiveOnly && responsibleLevel1.isActive()) {
							deptNode.getChildren().add(orphanResponsibleLevel1Node);
						}
					}
				}                
			}
			if(showBoth) {
				plantNode.getChildren().add(deptNode);
			}else if(!showActiveOnly && !dept.isActive()) {
				plantNode.getChildren().add(deptNode);
			} else if(showActiveOnly && dept.isActive()) {
				plantNode.getChildren().add(deptNode);
			} else if(!showActiveOnly && dept.isActive() && deptNode.getChildren().size() > 0) {
				plantNode.getChildren().add(deptNode);
			} else if(showActiveOnly && !dept.isActive() && deptNode.getChildren().size() > 0) {
				plantNode.getChildren().add(deptNode);
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
			QiResponsibleLevel responsibleLevel2, TreeItem<String> orphanResponsibleLevel2,
			boolean showActiveOnly, boolean showBoth) {
		if(!allResponsibleLevels1.isEmpty())
			for (QiResponsibleLevel responsibleLevel1 : allResponsibleLevels1) {
				if (responsibleLevel1.getDepartment().contentEquals(dept.getId().getDepartment())
						&& responsibleLevel1.getSite().contentEquals(dept.getId().getSite())
						&& responsibleLevel1.getPlant().contentEquals(dept.getId().getPlant()) && (responsibleLevel1.getLevel() == 1)
						&& (responsibleLevel2.getResponsibleLevelId().intValue() == responsibleLevel1.getUpperResponsibleLevelId().intValue())) {
					TreeItem<String> responsibleLevel1Node = null;
					if (responsibleLevel1.isActive())
						responsibleLevel1Node = addNode(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), activeIcon);
					else
						responsibleLevel1Node = addNode(QiConstant.RESPONSIBLE_LEVEL1_NODE + "- " + responsibleLevel1.getResponsibleLevelName(), inactiveIcon);

					if (responsibleLevel1Node != null) {
						if(showBoth) {
							orphanResponsibleLevel2.getChildren().add(responsibleLevel1Node);
						}else if(!showActiveOnly && !responsibleLevel1.isActive()) {
							orphanResponsibleLevel2.getChildren().add(responsibleLevel1Node);
						} else if(showActiveOnly && responsibleLevel1.isActive()) {
							orphanResponsibleLevel2.getChildren().add(responsibleLevel1Node);
						}
					}
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
			QiDepartment dept, TreeItem<String> deptNode, QiResponsibleLevel responsibleLevel3,
			boolean showActiveOnly, boolean showBoth) {
		if (responsibleLevel3.getDepartment().contentEquals(dept.getId().getDepartment()) && responsibleLevel3.getSite().contentEquals(dept.getId().getSite())
				&& responsibleLevel3.getPlant().contentEquals(dept.getId().getPlant()) && responsibleLevel3.getLevel() == 3) {
			TreeItem<String> responsibleLevel3Node = null;

			if (responsibleLevel3.isActive())
				responsibleLevel3Node=addNode(QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + responsibleLevel3.getResponsibleLevelName(), activeIcon);
			else
				responsibleLevel3Node=addNode(QiConstant.RESPONSIBLE_LEVEL3_NODE + "- " + responsibleLevel3.getResponsibleLevelName(), inactiveIcon);
			
			if (responsibleLevel3Node != null && !allResponsibleLevels2.isEmpty()) {
				for (QiResponsibleLevel responsibleLevel2 : allResponsibleLevels2) {
					if (responsibleLevel2.getDepartment().contentEquals(dept.getId().getDepartment()) 
							&& responsibleLevel2.getSite().contentEquals(dept.getId().getSite())
							&& responsibleLevel2.getPlant().contentEquals(dept.getId().getPlant()) 
							&& (responsibleLevel2.getLevel() == 2)
							&& (responsibleLevel3.getResponsibleLevelId().intValue() == responsibleLevel2.getUpperResponsibleLevelId().intValue())) {
						TreeItem<String> responsibleLevel2Node = null;
						if (responsibleLevel2.isActive()) {
							responsibleLevel2Node = addNode(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), activeIcon);
						}else { 
							responsibleLevel2Node = addNode(QiConstant.RESPONSIBLE_LEVEL2_NODE + "- " + responsibleLevel2.getResponsibleLevelName(), inactiveIcon);
						}
						if (responsibleLevel2Node != null) {
							getResposibleLevel1(activeIcon, inactiveIcon, allResponsibleLevels1, dept, responsibleLevel2,	responsibleLevel2Node, showActiveOnly, showBoth);
							if(showBoth) {
								responsibleLevel3Node.getChildren().add(responsibleLevel2Node);
							}else if(!showActiveOnly && !responsibleLevel2.isActive()) {
								responsibleLevel3Node.getChildren().add(responsibleLevel2Node);
							} else if(showActiveOnly && responsibleLevel2.isActive()) {
								responsibleLevel3Node.getChildren().add(responsibleLevel2Node);
							} else if(!showActiveOnly && responsibleLevel2.isActive() && responsibleLevel2Node.getChildren().size() > 0) {
								responsibleLevel3Node.getChildren().add(responsibleLevel2Node);
							} else if(showActiveOnly && !responsibleLevel2.isActive() && responsibleLevel2Node.getChildren().size() > 0) {
								responsibleLevel3Node.getChildren().add(responsibleLevel2Node);
							}
						}
					}
				}
				if(showBoth) {
					deptNode.getChildren().add(responsibleLevel3Node);
				}else if(!showActiveOnly && !responsibleLevel3.isActive()) {
					deptNode.getChildren().add(responsibleLevel3Node);
				} else if(showActiveOnly && responsibleLevel3.isActive()) {
					deptNode.getChildren().add(responsibleLevel3Node);
				} else if(!showActiveOnly && responsibleLevel3.isActive() && responsibleLevel3Node.getChildren().size() > 0) {
					deptNode.getChildren().add(responsibleLevel3Node);
				} else if(showActiveOnly && !responsibleLevel3.isActive() && responsibleLevel3Node.getChildren().size() > 0) {
					deptNode.getChildren().add(responsibleLevel3Node);
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
	
	private void filterTreeHierarchy(boolean showActiveOnly, boolean showBoth) {
		TreeView<String> tree = getView().getTree();
		TreeItem<String> root = tree.getRoot();
		root.getChildren().clear();
		
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
					getCompanyHierarchy(activeIcon, inactiveIcon, allSites, allPlants, allDepartments, allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, root, company, showActiveOnly, showBoth);
			}
		}
		setExpanded(root);
		tree.setShowRoot(false);
	}
	
	private void addStatusComboBoxListener() {
		getView().getStatusComboBox().valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				clearDisplayMessage();
				itemSelected=true;
				Logger.getLogger().info("Status ComboBox changed from " + oldValue + " to " + newValue);
				if (newValue !=null && newValue.equals("Active")) {
					EventBusUtil.publish(new StatusMessageEvent("Active status selected", StatusMessageEventType.INFO));
					filterTreeHierarchy(true, false); // Show only active nodes
				} else if (newValue !=null && newValue.equals("Non-Active")) {
					EventBusUtil.publish(new StatusMessageEvent("Non-Active status selected", StatusMessageEventType.INFO));
					filterTreeHierarchy(false, false); // Show only inactive nodes
				}
			}
		});
	}
	
	public void refresh(TreeView<String> tree) {
		getView().getStatusComboBox().setValue(null);
		itemSelected=false;
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
						allResponsibleLevels3, allResponsibleLevels2, allResponsibleLevels1, tree.getRoot(), company,false,true);
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
