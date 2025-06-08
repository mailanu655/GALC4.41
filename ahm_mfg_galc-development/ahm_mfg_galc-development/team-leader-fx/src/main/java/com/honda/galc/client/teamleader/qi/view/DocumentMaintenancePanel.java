package com.honda.galc.client.teamleader.qi.view;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.DocumentMaintenanceController;
import com.honda.galc.client.teamleader.qi.model.DocumentMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.qi.QiDocument;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>DocumentMaintenancePanel Class description</h3>
 * <p>
 * DocumentMaintenancePanel description
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

public class DocumentMaintenancePanel
		extends QiAbstractTabbedView<DocumentMaintenanceModel, DocumentMaintenanceController> {

	private LoggedTextField documentFilterTextField;
	private ObjectTablePane<QiDocument> documentTablePane;

	public DocumentMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}

	@Override
	public void initView() {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		double width = primaryScreenBounds.getWidth() / 2;
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		LoggedLabel documentFilterLabel = UiFactory.createLabel("documentFilterLabel", "Filter");
		documentFilterLabel.getStyleClass().add("display-label");
		HBox filterContainer = new HBox();
		HBox mainFilterContainer = new HBox();
		documentFilterTextField = createLoggedFilterTextField("filterTextField", 25, getController());
		filterContainer.getChildren().addAll(documentFilterLabel, documentFilterTextField);
		filterContainer.setSpacing(10);
		filterContainer.setPadding(new Insets(10));
		filterContainer.setAlignment(Pos.CENTER_RIGHT);
		filterContainer.setPrefWidth(width);

		mainFilterContainer.getChildren().addAll(filterContainer);
		mainFilterContainer.setAlignment(Pos.CENTER_RIGHT);
		documentTablePane = createDocumentTablePane();
		LoggedTableColumn<QiDocument, Integer> column = new LoggedTableColumn<QiDocument, Integer>();
		createSerialNumber(column);
		setDocumentTablePane(column);
		this.setTop(mainFilterContainer);
		this.setCenter(documentTablePane);
	}

	private void setDocumentTablePane(LoggedTableColumn<QiDocument, Integer> column) {
		documentTablePane.getTable().getColumns().add(0, column);
		documentTablePane.getTable().getColumns().get(0).setText("#");
		documentTablePane.getTable().getColumns().get(0).setResizable(true);
		documentTablePane.getTable().getColumns().get(0).setMaxWidth(45);
		documentTablePane.getTable().getColumns().get(0).setMinWidth(1);
	}

	private ObjectTablePane<QiDocument> createDocumentTablePane() {
		ColumnMappingList columnMappingList = ColumnMappingList.with("Document Name", "documentName")
				.put("Document Link", "documentLink").put("Created By", "createUser")
				.put("Create TimeStamp", "createDate").put("Updated By", "updateUser")
				.put("Update TimeStamp", "updateDate");

		Double[] columnWidth = new Double[] { 0.25, 0.45, 0.06, 0.07, 0.06, 0.08 };
		ObjectTablePane<QiDocument> panel = new ObjectTablePane<QiDocument>(columnMappingList, columnWidth);
		panel.setConstrainedResize(false);
		panel.getTable().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		return panel;
	}

	public void onTabSelected() {
		reload(StringUtils.trim(documentFilterTextField.getText()));
	}

	@Override
	public void reload() {
		documentTablePane.setData(getModel().findDocumentsByFilter(""));
	}

	public void reload(String filter) {
		documentTablePane.setData(getModel().findDocumentsByFilter(filter));
	}

	@Override
	public void start() {
	}

	public ObjectTablePane<QiDocument> getDocumentTablePane() {
		return documentTablePane;
	}

	public String getFilterTextData() {
		return StringUtils.trimToEmpty(documentFilterTextField.getText());
	}

	public String getScreenName() {
		return "Document Maintenance";
	}
}
