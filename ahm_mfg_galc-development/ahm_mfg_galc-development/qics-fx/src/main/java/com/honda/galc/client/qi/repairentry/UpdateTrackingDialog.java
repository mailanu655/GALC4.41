package com.honda.galc.client.qi.repairentry;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.data.ProductSearchResult;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.qi.base.QiProcessModel;
import com.honda.galc.client.qi.homescreen.BulkProductHomeScreenController;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;


/**
 * 
 * <h3>UpdateTrackingDialog Class description</h3>
 * <p>
 * UpdateTrackingDialog is used to display the screen to update tracking status from Bulk Repair Home screen
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
 * @author Vivek Bettada<br>
 *        Oct 1, 2021
 * 
 */

public class UpdateTrackingDialog extends QiFxDialog<QiProcessModel> {

	private LoggedButton updateBtn;
	private LoggedButton cancelBtn;
	private ComboBox<Line> lineComboBox;
	private UpdateTrackingDialogController controller;
	private String productType = "";
	private ObjectTablePane<ProcessPoint> processPointPanel;
	LoggedTextArea commentArea;
	RepairEntryModel repairModel = null;
	List<Line> lineList;
	private double screenWidth;
	
	List<ProcessPoint> listOfTracking;
	String filterText = "";

	public UpdateTrackingDialog(String applicationId,List<Line> newLines, String newProductType, BulkProductHomeScreenController bulkHomeController, 
			RepairEntryModel newModel, List<ProductSearchResult> results) {
		super("Update Tracking Status", ClientMainFx.getInstance().getStage(applicationId),newModel);
		repairModel = newModel;
		productType = newProductType;
		if(newLines != null && !newLines.isEmpty())  {
			lineList = newLines;
		}
		else  {
			lineList = new ArrayList<Line>();
		}
		controller = new UpdateTrackingDialogController(repairModel, this, results, lineList, newProductType, bulkHomeController);
		this.setOnCloseRequest( new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
			    close(event);	
			}
		});
		initComponents();
		getController().initListeners();
	}

	private void initComponents(){
		this.getScene().getStylesheets().add(com.honda.galc.client.utils.QiConstant.CSS_PATH);
		VBox box = new VBox();
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		box.getChildren().addAll(getTrackingListPanel(), getButtonContainer());
		((BorderPane) this.getScene().getRoot()).setCenter(box);
		getLineComboBox().setItems(FXCollections.observableArrayList(lineList));
		getController().disableAndResetAll(true);
		if(lineList != null && !lineList.isEmpty())  {
			getLineComboBox().getSelectionModel().select(0);
		}
	}

	/**
	 * Get all filter on top panel
	 */
	private Node getTrackingListPanel() {
		
		LoggedLabel sourceLabel = UiFactory.createLabel("label_dest","Target Tracking Status",(int)(0.008 * screenWidth));
		sourceLabel.getStyleClass().add("display-label-blue");
		sourceLabel.setPadding(new Insets(0,10,0,0));
		
		LoggedLabel productTypeLbl = UiFactory.createLabel("label","Product Type: " + getProductType() , (int)(0.008 * screenWidth));
		
		LoggedLabel lineLbl = UiFactory.createLabel("label","Line", (int)(0.007 * screenWidth));
		LoggedLabel asterisk = UiFactory.createLabel("label", "*");
		lineComboBox  = new ComboBox<Line>();
		lineComboBox.setPrefHeight(27);
		lineComboBox.setMinHeight(27);
		lineComboBox.setMaxHeight(27);
		lineComboBox.setMinWidth(lineComboBox.getMinWidth() * 2.0);
		lineComboBox.setConverter(
				new StringConverter<Line>() {
					
					@Override
					public String toString(Line arg0) {
						return arg0.getDivisionName() + "-" + arg0.getLineName();
					}
					
					@Override
					public Line fromString(String arg0) {
						return null;
					}
				}
		);
		
		LoggedLabel listSizeLbl1 = UiFactory.createLabel("listSize1","No. of products: ", (int)(0.01 * screenWidth));
		LoggedLabel listSizeLbl2 = UiFactory.createLabel("listSize2","1", (int)(0.01 * screenWidth));
		listSizeLbl2.textProperty().bind(getController().getProductListSize());

		VBox commentVBox = new VBox();
		LoggedLabel commentLbl = UiFactory.createLabel("label","Reason for change", (int)(0.007 * screenWidth));
		LoggedLabel asterisk1 = UiFactory.createLabel("ast1", "*");
		HBox commentLabelBox = new HBox();
		commentLabelBox.getChildren().addAll(commentLbl, asterisk1);
		commentArea = new LoggedTextArea("");
		commentArea.setPromptText("Enter reason for change");
		commentArea.setPrefRowCount(2);
		commentVBox.getChildren().addAll(commentLabelBox, commentArea);
		
		HBox lineHBox = new HBox();
		lineHBox.getChildren().addAll(lineLbl,asterisk,lineComboBox);
		HBox.setMargin(lineComboBox, new Insets(0,0,0,20));
		VBox leftBox = new VBox();
		leftBox.getChildren().addAll(sourceLabel, productTypeLbl,lineHBox);
		leftBox.setSpacing(10);
		leftBox.setPadding(new Insets(20,10,0,10));
		VBox rightBox = new VBox();
		HBox listSizeHBox = new HBox();
		listSizeHBox.getChildren().addAll(listSizeLbl1, listSizeLbl2);
		listSizeLbl2.setAlignment(Pos.CENTER);
		rightBox.getChildren().addAll(listSizeHBox, commentVBox);
		rightBox.setSpacing(10);
		rightBox.setPadding(new Insets(20,10,0,10));
		HBox section1 = new HBox();
		section1.getChildren().addAll(leftBox, rightBox);
		section1.setAlignment(Pos.CENTER_LEFT);
		section1.setSpacing(100);
		HBox section2 = new HBox();
		section2.getChildren().addAll(getProcessPointTablePane());
		VBox bothSections = new VBox();
		bothSections.getChildren().addAll(section1, section2);
		return bothSections;
	}
	
	private Node getProcessPointTablePane() {
		ColumnMappingList columnMappingList;
		Double[] columnWidth;
		
		TitledPane destPanel = new TitledPane();

		destPanel.setPadding(new Insets(10));
		destPanel.setText("Tracking process points");
		destPanel.setCollapsible(false);
		destPanel.setPrefWidth(screenWidth/2.05);


		columnMappingList = ColumnMappingList.with("ProcessPoint", "processPointId").put("Name", "processPointName").put("Desc", "processPointDescription").put("LineId", "lineId").put("Name", "lineName");
		columnWidth = new Double[] {0.15,0.10,0.05,0.1,0.1}; 

		processPointPanel = new ObjectTablePane<ProcessPoint>(columnMappingList,columnWidth);
		processPointPanel.setConstrainedResize(false);
		processPointPanel.setPrefHeight(190);

		LoggedTableColumn<ProcessPoint, Integer> column = new LoggedTableColumn<ProcessPoint, Integer>();
		createSerialNumber(column);
		processPointPanel.getTable().getColumns().add(0, column);
		processPointPanel.getTable().getColumns().get(0).setText("#");
		processPointPanel.getTable().getColumns().get(0).setResizable(true);
		processPointPanel.getTable().getColumns().get(0).setMaxWidth(100);
		processPointPanel.getTable().getColumns().get(0).setMinWidth(5);
		processPointPanel.setSelectionMode(SelectionMode.SINGLE);

		destPanel.setContent(processPointPanel);
		return destPanel;
	}

	private Node getButtonContainer() {
		updateBtn = createBtn(com.honda.galc.client.utils.QiConstant.UPDATE, getController());
		cancelBtn = createBtn(com.honda.galc.client.utils.QiConstant.CLOSE, getController());

		HBox btnContainer = new HBox();
		btnContainer.setSpacing(6);
		btnContainer.setPadding(new Insets(0,0,20,0));
		btnContainer.setAlignment(Pos.CENTER);
		btnContainer.getChildren().addAll(updateBtn, cancelBtn);
		return btnContainer;
	}

	public UpdateTrackingDialogController getController() {
		return controller;
	}

	public void setController(UpdateTrackingDialogController controller) {
		this.controller = controller;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public ObjectTablePane<ProcessPoint> getProcessPointPanel() {
		return processPointPanel;
	}

	public void setProcessPointPanel(ObjectTablePane<ProcessPoint> processPointPanel) {
		this.processPointPanel = processPointPanel;
	}

	public LoggedButton getUpdateBtn() {
		return updateBtn;
	}

	public void setUpdateBtn(LoggedButton updateBtn) {
		this.updateBtn = updateBtn;
	}

	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(LoggedButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	public ComboBox<Line> getLineComboBox() {
		return lineComboBox;
	}

	public void setLineComboBox(ComboBox<Line> lineComboBox) {
		this.lineComboBox = lineComboBox;
	}

 	public LoggedTextArea getCommentArea() {
		return commentArea;
	}

	public void setCommentArea(LoggedTextArea commentArea) {
		this.commentArea = commentArea;
	}

	public void close(WindowEvent event) {
   	   if(getController().onClose())  {
   		   super.close();
   	   }
   	   else   {
   		   event.consume();
   	   }
  	}
}
