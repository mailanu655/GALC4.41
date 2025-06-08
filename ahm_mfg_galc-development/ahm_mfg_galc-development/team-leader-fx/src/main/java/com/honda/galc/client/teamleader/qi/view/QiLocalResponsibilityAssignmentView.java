package com.honda.galc.client.teamleader.qi.view;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.QiLocalResponsibilityAssignmentController;
import com.honda.galc.client.teamleader.qi.model.QiResponsibilityAssignmentModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.utils.QiConstant;

import javafx.geometry.Insets;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.scene.control.ComboBox;

public class QiLocalResponsibilityAssignmentView  extends QiAbstractTabbedView<QiResponsibilityAssignmentModel, QiLocalResponsibilityAssignmentController> { 

	private TreeView<String> hierarchyTree;
	private BorderPane contentPane;
	private LoggedButton refreshButton;
	private ComboBox<String> statusComboBox;

	public QiLocalResponsibilityAssignmentView(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		SplitPane outerPane=new SplitPane();

		VBox rightPane = new VBox();
		rightPane.setPrefWidth(USE_COMPUTED_SIZE);

		refreshButton = createBtn(QiConstant.REFRESH, getController());

		statusComboBox = new ComboBox<>();
		statusComboBox.setPadding(new Insets(3, 3, 0, 0));
		statusComboBox.getItems().addAll("Active", "Non-Active");
		
		BorderPane topPane = new BorderPane();
		topPane.setPadding(new Insets(3, 3, 0, 0));
		topPane.setRight(refreshButton);
		
		BorderPane.setMargin(statusComboBox, new Insets(5, 5, 5, 5)); // Add some margin
	    topPane.setLeft(statusComboBox);
			
		rightPane.getChildren().addAll(topPane, createDisplayTabContainer());

		outerPane.getItems().addAll(createTreeContainer(), rightPane);
		outerPane.setDividerPositions(0.4f,0.6f);
		this.setCenter(outerPane);
	}

	/**
	 * This method will be used to display tree on initial load on left side of
	 * the screen.
	 * 
	 * @return
	 */
	private VBox createTreeContainer() {
		VBox treeContainer = new VBox();
		setTree(getController().loadTree());
		treeContainer.getChildren().add(hierarchyTree);
		treeContainer.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth() / 4);
		hierarchyTree.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight() - 120);
		return treeContainer;
	}

	/**
	 * This method will be used to create right side panel holder borderpane.
	 * 
	 * @return
	 */
	private BorderPane createDisplayTabContainer() {
		contentPane = new BorderPane();
		double width = Screen.getPrimary().getVisualBounds().getWidth();
		contentPane.setMinWidth(width / 2);
		return contentPane;
	}

	public void onTabSelected() {
		getController().clearDisplayMessage();
		getController().refresh(hierarchyTree);
	}

	public String getScreenName() {
		return "Local Responsibility";
	}

	@Override
	public void reload() {

	}

	@Override
	public void start() {

	}

	public TreeView<String> getTree() {
		return hierarchyTree;
	}

	public void setTree(TreeView<String> tree) {
		this.hierarchyTree = tree;
	}

	public BorderPane getContentPane() {
		return contentPane;
	}

	public void setContentPane(BorderPane contentPane) {
		this.contentPane = contentPane;
	}

	public LoggedButton getRefreshButton() {
		return refreshButton;
	}

	public ComboBox<String> getStatusComboBox() {
        return statusComboBox;
    }

}
