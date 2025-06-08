package com.honda.galc.client.teamleader.qi.view;

import java.util.List;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.QiResponsibilityMappingController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiResponsibilityMapping;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class QiResponsibleMappingAssignmentView extends QiAbstractTabbedView<QiResponsibilityAssignmentModel, QiResponsibilityMappingController> {

	private TreeView<String> leftHierarchyTree, rightHierarchyTree;
	private BorderPane contentPane;
	private LoggedButton saveButton;
	private LoggedButton refreshButton;
	private LoggedComboBox<String> plantCombobox;
	private ObjectTablePane<QiResponsibilityMapping> respLevelTablePane;
	
	public QiResponsibleMappingAssignmentView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth()/2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
		VBox leftPane=new VBox();
		leftPane.setPrefWidth(500);
		HBox mainPanel = new HBox();
		
		VBox rightPane = new VBox();
		
		rightPane.setPrefWidth(500);
		
		saveButton = createBtn(QiConstant.SAVE, getController());
		saveButton.setDisable(true);
		
		refreshButton = createBtn(QiConstant.REFRESH, getController());
		
		HBox plantContainer = new HBox();
		HBox plantLabelBoxContainer = new HBox();
		setPlant(width, plantContainer, plantLabelBoxContainer);		
				
		BorderPane topLeftPane = new BorderPane();
		topLeftPane.setPadding(new Insets(10,10,10,10));
		topLeftPane.setRight(plantContainer);
		
		BorderPane topRightPane = new BorderPane();
		topRightPane.setPadding(new Insets(10,10,17,10));
		topRightPane.setCenter(refreshButton);
		topRightPane.setRight(saveButton);
		
		respLevelTablePane = createRespLevelTablePane();
		respLevelTablePane.setPadding(new Insets(86,0,0,0));
		
		rightPane.getChildren().addAll(getAlternateRespLabel(),topRightPane, createRightTreeContainer());
		leftPane.getChildren().addAll(getDefaultRespLabel(),topLeftPane,createLeftTreeContainer());
		
		mainPanel.getChildren().addAll(leftPane,rightPane,respLevelTablePane);
		mainPanel.setPadding(new Insets(10));
		this.setCenter(mainPanel);
	}

	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiResponsibilityMapping> createRespLevelTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("*Default Resp", "defaultRespLevel")
		.put("*Engine Source","pCode").put("Alternate Resp", "alternateDefault");
		
		Double[] columnWidth = new Double[] { 0.2, 0.07, 0.1 };
		ObjectTablePane<QiResponsibilityMapping> panel = new ObjectTablePane<QiResponsibilityMapping>(columnMappingList,columnWidth);
		panel.setConstrainedResize(true);
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMinWidth(0.1);
		panel.setPadding(new Insets(0));
		panel.setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}
	
	private LoggedLabel getDefaultRespLabel() {
		LoggedLabel defaultLabel = UiFactory.createLabel("defaultLabel", "*Default Responsibility");
		defaultLabel.getStyleClass().add("display-label-14");
		return defaultLabel;
	}	
	
	private LoggedLabel getAlternateRespLabel() {
		LoggedLabel alternateLabel = UiFactory.createLabel("alternateLabel", "Alternate Responsibility");
		alternateLabel.getStyleClass().add("display-label-14");
		return alternateLabel;
	}	

	private void setPlant(double width, HBox plantContainer, HBox plantLabelBoxContainer) {
		LoggedLabel plantLabel=UiFactory.createLabel("label","*Engine Source");
		plantLabel.getStyleClass().add("display-label");
		plantLabelBoxContainer.getChildren().addAll(plantLabel);
		plantCombobox = new LoggedComboBox<String>();
		plantCombobox.getStyleClass().add("combo-box-base");
		plantCombobox.setPrefWidth(width);	
		plantContainer.setAlignment(Pos.BASELINE_CENTER);
		plantContainer.setPadding(new Insets(10));
		plantContainer.setSpacing(10);
		plantContainer.getChildren().addAll(plantLabelBoxContainer,plantCombobox);
		loadPlantCombobox();
	}
	
	@SuppressWarnings("unchecked")
	public void loadPlantCombobox(){
		List<String> plantList = getModel().findAllEnginePlantCodes();
		getPlantCombobox().setItems(FXCollections.observableArrayList(plantList));
	}
	
	/**
	 * This method will be used to display tree on initial load on left/right side of
	 * the screen.
	 * 
	 * @return
	 */
	private VBox createLeftTreeContainer() {
		VBox treeContainer = new VBox();
		setLeftTree(getController().loadTree());
		treeContainer.getChildren().add(leftHierarchyTree);
		treeContainer.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
		leftHierarchyTree.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 120);
		return treeContainer;
	}
	
	private VBox createRightTreeContainer() {
		VBox treeContainer = new VBox();
		setRightTree(getController().loadTree());
		treeContainer.getChildren().add(rightHierarchyTree);
		treeContainer.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
		rightHierarchyTree.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 120);
		return treeContainer;
	}

	public void onTabSelected() {
		getController().clearDisplayMessage();
		getController().refresh(leftHierarchyTree);
		getController().refresh(rightHierarchyTree);
		getController().reloadTable();
	}

	public String getScreenName() {
		return "Responsibility Mapping";
	}

	@Override
	public void reload() {

	}

	@Override
	public void start() {

	}

	public TreeView<String> getTree(TreeView<String> tree) {
		if (tree.equals(leftHierarchyTree)) {
			return leftHierarchyTree;
		}
		return rightHierarchyTree;
	}
	
	public TreeView<String> getLeftTree() {
		return leftHierarchyTree;
	}
	
	public TreeView<String> getRightTree() {
		return rightHierarchyTree;
	}

	public void setLeftTree(TreeView<String> tree) {
		this.leftHierarchyTree = tree;
	}

	public void setRightTree(TreeView<String> tree) {
		this.rightHierarchyTree = tree;
	}
	
	public BorderPane getContentPane() {
		return contentPane;
	}

	public void setContentPane(BorderPane contentPane) {
		this.contentPane = contentPane;
	}

	public LoggedButton getSaveButton() {
		return saveButton;
	}
	
	public LoggedComboBox<String> getPlantCombobox() {
		return plantCombobox;
	}
	 
	public ObjectTablePane<QiResponsibilityMapping> getResplevelTablePane() {
		return respLevelTablePane;
	}
	
	public LoggedButton getRefreshButton() {
		return refreshButton;
	}
}
