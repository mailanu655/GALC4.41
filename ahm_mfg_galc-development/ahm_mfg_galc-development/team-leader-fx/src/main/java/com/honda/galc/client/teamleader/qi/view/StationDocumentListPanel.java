package com.honda.galc.client.teamleader.qi.view;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.StationDocumentListController;
import com.honda.galc.client.teamleader.qi.model.StationDocumentListModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDocument;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

/**
 * 
 * <h3>StationDocumentListPanel Class description</h3>
 * <p>
 * StationDocumentListPanel description
 * </p>
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
 *
 * </TABLE>
 * 
 * @author Justin Jiang<br>
 *         February 20, 2020
 *
 */

public class StationDocumentListPanel
		extends QiAbstractTabbedView<StationDocumentListModel, StationDocumentListController> {

	private ObjectTablePane<QiDocument> assignedDocumentTablePane;
	private String terminal;

	public StationDocumentListPanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
		terminal = super.getMainWindow().getClientHostName();
	}

	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		VBox mainVBox = new VBox();
		setPadding(new Insets(10, 10, 10, 10));
		mainVBox.getChildren().addAll(createLabelHBox(),
				createTitiledPane("Assigned Documents", createAssignedDocumentMigPane()));
		this.setTop(mainVBox);
	}

	private HBox createLabelHBox() {
		HBox labelHBox = new HBox();
		LoggedLabel label = UiFactory.createLabel("label",
				"Please click the document in the table to open browser to view/download.", "-fx-font: 14 arial;",
				TextAlignment.LEFT);
		labelHBox.setPadding(new Insets(20, 20, 20, 20));
		labelHBox.getChildren().addAll(label);
		return labelHBox;
	}

	private TitledPane createTitiledPane(String title, Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double titlePaneWidth = primaryScreenBounds.getWidth();
		titledPane.setPrefSize(titlePaneWidth, 350);
		return titledPane;
	}

	private MigPane createAssignedDocumentMigPane() {
		MigPane pane = new MigPane("insets 5 5 5 5", "[center,grow,fill]", "");

		assignedDocumentTablePane = createAssignedDocumentObjectTablePane();
		assignedDocumentTablePane.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		LoggedTableColumn<QiDocument, Integer> column = new LoggedTableColumn<QiDocument, Integer>();

		createSerialNumber(column);
		assignedDocumentTablePane.getTable().getColumns().add(0, column);
		assignedDocumentTablePane.getTable().getColumns().get(0).setText("#");
		assignedDocumentTablePane.getTable().getColumns().get(0).setResizable(true);
		assignedDocumentTablePane.getTable().getColumns().get(0).setMaxWidth(100);
		assignedDocumentTablePane.getTable().getColumns().get(0).setMinWidth(20);

		pane.add(assignedDocumentTablePane, "span,wrap");
		pane.setId("mig-pane");
		return pane;
	}

	private ObjectTablePane<QiDocument> createAssignedDocumentObjectTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Document Name", "documentName")
				.put("Document Link", "documentLink").put("Description", "description");
		ObjectTablePane<QiDocument> panel = new ObjectTablePane<QiDocument>(columnMappingList);
		panel.setStyle("-fx-padding: 5px 0px 5px 0px; -fx-min-height: 125px");
		panel.setPrefWidth(0.95 * Screen.getPrimary().getVisualBounds().getWidth());
		panel.setConstrainedResize(false);
		panel.getTable().getColumns().get(0).setPrefWidth(panel.getPrefWidth() * 0.2);
		panel.getTable().getColumns().get(1).setPrefWidth(panel.getPrefWidth() * 0.41);
		panel.getTable().getColumns().get(2).setPrefWidth(panel.getPrefWidth() * 0.40);
		return panel;
	}

	public void onTabSelected() {
		getController().clearDisplayMessage();
		reload();
	}

	public String getScreenName() {
		return "Station Document List";
	}

	@Override
	public void reload() {
		assignedDocumentTablePane.setData(getModel().findAssignedDocumentByTerminal(terminal));
	}

	@Override
	public void start() {
	}

	public ObjectTablePane<QiDocument> getAssignedDocumentTablePane() {
		return assignedDocumentTablePane;
	}

	public String getTerminal() {
		return terminal;
	}
}
