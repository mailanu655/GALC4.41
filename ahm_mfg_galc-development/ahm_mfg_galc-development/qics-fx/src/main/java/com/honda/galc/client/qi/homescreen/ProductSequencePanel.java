package com.honda.galc.client.qi.homescreen;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.product.ProductSequence;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class ProductSequencePanel {

	private ProductSequenceController controller;
	private HomeScreenModel model;
	private double width;
	private double height;
	private HomeScreenView parentView;
	private ObjectTablePane<ProductSequence> myTable;
	LoggedButton refreshButton ;
	LoggedButton selectButton ;
	
	public ProductSequencePanel(HomeScreenView hsView, HomeScreenModel hsModel) {
		this(hsView, hsModel,Screen.getPrimary().getVisualBounds());
	}
	
	
	public ProductSequencePanel(HomeScreenView hsView, HomeScreenModel hsModel, Rectangle2D screenBounds) {
		width = screenBounds.getWidth();
		height = screenBounds.getHeight();
		controller=new ProductSequenceController(this,hsModel,hsView);
		controller.setMyPanel(this);
		this.model=hsModel;
		parentView = hsView;
	}
	
	
	
	public MigPane getProductSequencePanel()  {
		MigPane pane = new MigPane("insets 10 5 0 5", "[center,grow,fill]", "");
		VBox myVBox=new VBox();
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "id.productId").put("Received", "referenceTimestamp").put("Seq No.", "sequenceNumber", Integer.class).put("Product Type","productType");
		Double[] columnWidth = new Double[] {0.12,0.12,0.07,0.07}; 
		
		myTable = new ObjectTablePane<ProductSequence>(columnMappingList,columnWidth);
		myTable.setStyle(parentView.getTableFontStyleSmallBold());
		myTable.setMaxWidth((int)(0.41 * width));
		myTable.setMaxHeight((int)(0.43 * height));
		
		LoggedTableColumn<ProductSequence, Integer> column = new LoggedTableColumn<ProductSequence, Integer>();
		parentView.createSerialNumber(column);
		myTable.getTable().getColumns().add(0, column);
		myTable.getTable().getColumns().get(0).setText("#");
		myTable.getTable().getColumns().get(0).setResizable(true);
		myTable.getTable().getColumns().get(0).setMaxWidth((int)(0.021 * width));
		myTable.getTable().getColumns().get(0).setMinWidth(5);
		myTable.setEditable(false);
		myTable.setConstrainedResize(false);
		myTable.setPadding(new Insets(0,0,5,0));
		
		HBox myHBox = new HBox(300);
		myHBox.setAlignment(Pos.BASELINE_CENTER);
		myHBox.getChildren().addAll(createRefreshButton(), createSelectButton());
		myVBox.getChildren().addAll(myTable, myHBox);
		pane.add(myVBox, "left, span, wrap");
		pane.setId("ProductSequencePane");
		return pane;		
	}

	public ObjectTablePane<ProductSequence> getMyTable() {
		return myTable;
	}

	public void setMyTable(ObjectTablePane<ProductSequence> myTable) {
		this.myTable = myTable;
	}
	
	public void reload()  {
		getController().reload();
	}


	private LoggedButton createRefreshButton() {
		refreshButton = UiFactory.createButton(QiConstant.REFRESH, "refreshBtn");
		refreshButton.setPrefWidth(width * 0.06);
		refreshButton.setPrefHeight(width * 0.01);
		refreshButton.setPadding(new Insets(5,0,0,0));
		refreshButton.setOnAction(getController());
		return refreshButton;
	}
	
	private LoggedButton createSelectButton() {
		selectButton = UiFactory.createButton(QiConstant.SELECT, "selectBtn");
		selectButton.setPrefWidth(width * 0.06);
		selectButton.setPrefHeight(width * 0.01);
		selectButton.setPadding(new Insets(5,0,0,0));
		selectButton.setOnAction(getController());
		selectButton.setDisable(true);
		return selectButton;
	}
	

	public ProductSequenceController getController() {
		return controller;
	}



	public void setController(ProductSequenceController controller) {
		this.controller = controller;
	}



	public LoggedButton getRefreshButton() {
		return refreshButton;
	}



	public void setRefreshButton(LoggedButton refreshButton) {
		this.refreshButton = refreshButton;
	}


	public HomeScreenModel getModel() {
		return model;
	}


	public LoggedButton getSelectButton() {
		return selectButton;
	}
}