package com.honda.galc.client.teamlead.structure.delete;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.loader.dto.MeasurementDetailsDto;
import com.honda.galc.client.loader.dto.PartDetailsDto;
import com.honda.galc.client.loader.dto.StructureDeleteDTO;
import com.honda.galc.client.loader.dto.UnitDetailsDto;
import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.StructureDetailsDto;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Callback;


public class StructureDeletePanel extends AbstractTabbedView<StructureDeleteModel, StructureDeleteController>{
	private LabeledComboBox<String> productTypesComboBox;
	private LabeledComboBox<String> divisionComboBox;
	private LoggedComboBox<KeyValue<String, String>>  processPointComboBox;
	private LoggedRadioButton productIdRadioButton;
	private LoggedRadioButton LotNumberRadioButton;
	private UpperCaseFieldBean ProductIdTextField;
	private UpperCaseFieldBean lotNumberTextField;
	private LoggedButton searchBtn;
	private ObjectTablePane<StructureDetailsDto> structureDetailsTablePane;
	private double width;
	private TitledPane searchTitledPane;
	private TitledPane detailsTitledPane;
	private LoggedButton viewBtn;
	private LoggedButton deleteBtn;
	private LoggedButton createBtn;
	
	private HBox processPointHBox;
	private TreeTableView<StructureDeleteDTO> treeTableView;
	private TreeItem<StructureDeleteDTO> root;
	private boolean isAuthorized;
	private Hyperlink exportInCsvLink;
	private Button productIdBtn;
	private ProductTypeData productTypeData;
	
	
	
	public StructureDeletePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
	}
	
	@Override
	public void initView() {
		Rectangle2D parentBounds = Screen.getPrimary().getVisualBounds();
		double scrollPaneWidth = parentBounds.getWidth() - 5;
		double scrollPaneHeight = parentBounds.getHeight() - 141;
		width = parentBounds.getWidth()/2;
		isAuthorized = isAuthorized();
		String cssPath = "/resource/css/structure-delete.css";
		this.getStylesheets().add(cssPath);
		HBox searchDetailsHBox = new HBox();
		

		VBox structureVBox = new VBox();
		structureDetailsTablePane = createStructureDetailTablePane();
		searchDetailsHBox.getChildren().addAll(createTitiledPane("Search Details", createSearchDetailsPanel(), 350, scrollPaneHeight-100, 12, false), 
				createTitiledPane("Structure Details", structureDetailsTablePane, scrollPaneWidth, scrollPaneHeight-100, 12, false));
		
		treeTableView = createDetailsTree();
		searchTitledPane = new TitledPane("Search Panel", searchDetailsHBox);
		searchTitledPane.setFont(Font.font("", FontWeight.BOLD, 16));
		detailsTitledPane = createTitiledPane("Unit Details Panel", treeTableView, scrollPaneWidth, scrollPaneHeight, 16,true);
		
		structureVBox.getChildren().addAll(searchTitledPane, detailsTitledPane);
		detailsTitledPane.setExpanded(false);
		this.setCenter(structureVBox);
	}
	
	@Override
	public void onTabSelected() {
		getController().loadComboBox();
	}

	@Override
	public void reload() {
		
	}
	
	public void reload(String productId, String productionLot) {
		String product = StringUtils.isEmpty(productId) ? "Lot: " +  productionLot : "VIN: "  + productId;
		root = new TreeItem<StructureDeleteDTO>(new StructureDeleteDTO(product, "","","","",""));
		root.setExpanded(true);
		for (Entry<String, UnitDetailsDto> unit : getController().getDetailsMap().entrySet()) {
			UnitDetailsDto unitDetailsDto = unit.getValue();
			TreeItem<StructureDeleteDTO> unitTreeItem = new TreeItem<StructureDeleteDTO>(
					new StructureDeleteDTO(unitDetailsDto.getProcessPointName() + " - " + unitDetailsDto.getProcessPoint(), String.valueOf(unitDetailsDto.getProcSeqNo()), 
							unitDetailsDto.getOpeartionName(), unitDetailsDto.getOpType(), String.valueOf(unitDetailsDto.getOpSeqNo()), unitDetailsDto.getAsmProcNo()));
			
			boolean isPartHeader = true;
		    for (PartDetailsDto part : unit.getValue().getPartDetailsList()) {
		    	if(isPartHeader && unit.getValue().getPartDetailsList().size() > 0) {
		    		TreeItem<StructureDeleteDTO> partHeaderTreeItem = new TreeItem<StructureDeleteDTO>(
							new StructureDeleteDTO("", "Part Number", "Part Item Number", 
									"Part Sec Code", "Part Type", "Part Mask"), new Rectangle(15, 15, Color.CHOCOLATE));
		    		unitTreeItem.getChildren().add(partHeaderTreeItem);
					isPartHeader = false;
		    	}
		    	TreeItem<StructureDeleteDTO> partDetailsTreeItem = new TreeItem<StructureDeleteDTO>(
		    			new StructureDeleteDTO("", part.getPartNumber(), part.getPartItemNumber(), part.getPartSecCode(), part.getPartType(),part.getPartMask()));
		    	unitTreeItem.getChildren().add(partDetailsTreeItem);
		    	
		    	boolean isMeasHeader = true;
		    	for (MeasurementDetailsDto m : part.getMeasurementDetailsList()) {
		    		if(isMeasHeader && part.getMeasurementDetailsList().size() > 0) {
		    			TreeItem<StructureDeleteDTO> measHeaderTreeItem = new TreeItem<StructureDeleteDTO>(
		    					new StructureDeleteDTO("", "Measurement Seq No", "Min", "Max","P-Set","Tool"), new Rectangle(15, 15, Color.SKYBLUE));
				    	partDetailsTreeItem.getChildren().add(measHeaderTreeItem);
				    	isMeasHeader = false;
		    		}
		    		TreeItem<StructureDeleteDTO> measDetailsTreeItem = new TreeItem<StructureDeleteDTO>(
		    				new StructureDeleteDTO("", String.valueOf(m.getMeasurementSeqNo()), String.valueOf(m.getMin()), String.valueOf(m.getMax()),m.getPset(),m.getTool()));
		    		partDetailsTreeItem.getChildren().add(measDetailsTreeItem);
				}
			}
		    root.getChildren().add(unitTreeItem);
		}
		treeTableView.setRoot(root);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private TreeTableView<StructureDeleteDTO> createDetailsTree() {
		treeTableView = new TreeTableView<StructureDeleteDTO>();
		
		TreeTableColumn<StructureDeleteDTO, String> processPointColumn = 
	            new TreeTableColumn<StructureDeleteDTO, String>("Process Point");
		processPointColumn.setPrefWidth(300);
		processPointColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col1"));
		
		TreeTableColumn<StructureDeleteDTO, String> processSeqNoColumn = 
	            new TreeTableColumn<StructureDeleteDTO, String>("Process Seq No");
        processSeqNoColumn.setPrefWidth(150);
        processSeqNoColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col2"));
        
		TreeTableColumn<StructureDeleteDTO, String> operationNameColumn = 
	            new TreeTableColumn<StructureDeleteDTO, String>("Operation Name");
        operationNameColumn.setPrefWidth(250);
        operationNameColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col3"));
        
        TreeTableColumn<StructureDeleteDTO, String> opTypeColumn = 
	            new TreeTableColumn<StructureDeleteDTO, String>("Op Type");
        opTypeColumn.setPrefWidth(200);
        opTypeColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col4"));
        
        TreeTableColumn<StructureDeleteDTO, String> opSeqNoColumn = 
	            new TreeTableColumn<StructureDeleteDTO, String>("Op Seq No");
        opSeqNoColumn.setPrefWidth(200);
        opSeqNoColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col5"));
	        
        TreeTableColumn<StructureDeleteDTO, String> asmProNoColumn = 
            new TreeTableColumn<StructureDeleteDTO, String>("ASM Proc No");
        asmProNoColumn.setPrefWidth(200);
        asmProNoColumn.setCellValueFactory(new TreeItemPropertyValueFactory("col6"));
        
        treeTableView.getColumns().setAll(processPointColumn, processSeqNoColumn, operationNameColumn, opTypeColumn,  opSeqNoColumn, asmProNoColumn);
        
        treeTableView.setRowFactory(new Callback<TreeTableView<StructureDeleteDTO>, TreeTableRow<StructureDeleteDTO>>() {

          	 @Override
          	    public TreeTableRow<StructureDeleteDTO> call(TreeTableView<StructureDeleteDTO> tv) {
          	        return new TreeTableRow<StructureDeleteDTO>() {
          	            @Override
          	            public void updateItem(StructureDeleteDTO item, boolean empty) {
          	            	 super.updateItem(item, empty);
          	                 if (empty) {
          	                     setText("");
          	                 } else {
          	                     setText(item.getCol1());
          	                     setStyle("-fx-font-weight: normal ;");
          	                     if(item != null && (item.getCol2().equals("Part Number") || item.getCol2().equals("Measurement Seq No")))
          	                    	 setStyle("-fx-font-weight: bold;");
          	                 }
          	            }
          	        };
          	    }

          });
		return treeTableView;
	}

	public void setPddaDetails(String plantLocCode, String deptCode, String modelYearCode, 
			String prodSchQty, String lineNo, String vmc) {
		Label lblPlantLocCode = new Label("Plant Location Code: ");
		lblPlantLocCode.setPadding(new Insets(0,5,0,30));
		lblPlantLocCode.setStyle("-fx-font-weight: bold ;");
		
		Label lblDeptCode = new Label("Dept Code: ");
		lblDeptCode.setPadding(new Insets(0,5,0,30));
		lblDeptCode.setStyle("-fx-font-weight: bold ;");
		
		Label lblModelYearCode = new Label("Model Year: ");
		lblModelYearCode.setPadding(new Insets(0,5,0,30));
		lblModelYearCode.setStyle("-fx-font-weight: bold ;");
		
		Label lblProdSchQty = new Label("Production Rate: ");
		lblProdSchQty.setPadding(new Insets(0,5,0,30));
		lblProdSchQty.setStyle("-fx-font-weight: bold ;");
		
		Label lblLlineNo = new Label("Line Number: ");
		lblLlineNo.setPadding(new Insets(0,5,0,30));
		lblLlineNo.setStyle("-fx-font-weight: bold ;");
		
		Label lblVmc = new Label("VMC: ");
		lblVmc.setPadding(new Insets(0,5,0,30));
		lblVmc.setStyle("-fx-font-weight: bold ;");
		
		Text txtPlantLocCode = new Text(plantLocCode);
		Text txtDeptCode = new Text(deptCode);
		Text txtModelYearCode = new Text(modelYearCode);
		Text txtProdSchQty = new Text(prodSchQty);
		Text txtLineNo = new Text(lineNo);
		Text txtVmc = new Text(vmc);
		
		exportInCsvLink = new Hyperlink("Export In CSV");
		exportInCsvLink.setPadding(new Insets(0,0,0,width/2));
		exportInCsvLink.setAlignment(Pos.CENTER_RIGHT);
		exportInCsvLink.setMaxWidth(Double.MAX_VALUE);
		exportInCsvLink.setStyle("-fx-font-weight: bold ;");
		
		HBox pddaDetailsHeader = new HBox();
		pddaDetailsHeader.setMaxWidth(width * 2);
		pddaDetailsHeader.setMinWidth(width * 2);
		pddaDetailsHeader.getChildren().addAll(lblPlantLocCode, txtPlantLocCode, lblDeptCode, txtDeptCode, lblModelYearCode, txtModelYearCode, 
				lblProdSchQty, txtProdSchQty, lblLlineNo, txtLineNo, lblVmc, txtVmc, exportInCsvLink);
		detailsTitledPane.setGraphic(pddaDetailsHeader);
		detailsTitledPane.setText("");
	}
	
	private TitledPane createTitiledPane(String title,Node content, double width, double height, int font, boolean collapsible) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setFont(Font.font("", FontWeight.BOLD, font));
		titledPane.setContent(content);
		titledPane.setPrefSize(width,height);
		titledPane.setCollapsible(collapsible);
		return titledPane;
	}
	
	private ObjectTablePane<StructureDetailsDto> createStructureDetailTablePane(){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Product Id", "productId")
		.put("Production Lot","productionLot")
		.put("Spec Code", "productSpecCode")
		.put("Str Rev", "structureRevision")
		.put("Division", "division")
		.put("Production Date", "productionDate");
		
		Double[] columnWidth = new Double[] {
				0.10, 0.12,0.12,0.05,0.07,0.10,0.12
			};
		ObjectTablePane<StructureDetailsDto> panel = new ObjectTablePane<StructureDetailsDto>(columnMappingList,columnWidth);
		LoggedTableColumn<StructureDetailsDto, Boolean> buttonCol = new LoggedTableColumn<StructureDetailsDto, Boolean>();
		LoggedTableColumn<StructureDetailsDto, Boolean> buttonCol1 = new LoggedTableColumn<StructureDetailsDto, Boolean>();
		createSerialNumber(buttonCol1);
		panel.getTable().getColumns().add(0, buttonCol1);
		panel.getTable().getColumns().get(0).setText("S No");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(45);
		panel.getTable().getColumns().get(0).setMinWidth(45);
		
		createViewAndDeleteButton(buttonCol);
		panel.getTable().getColumns().add(7, buttonCol);
		panel.getTable().getColumns().get(7).setText("Action");
		panel.getTable().getColumns().get(7).setResizable(true);
		panel.getTable().getColumns().get(7).setMaxWidth(150);
		panel.getTable().getColumns().get(7).setMinWidth(150);
		
		
		panel.setConstrainedResize(false);
		return panel;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createViewAndDeleteButton(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn<StructureDetailsDto, Boolean>, LoggedTableCell<StructureDetailsDto,Boolean>>() { 
			public LoggedTableCell<StructureDetailsDto,Boolean> call(LoggedTableColumn<StructureDetailsDto,Boolean> p) {	
				return new LoggedTableCell<StructureDetailsDto,Boolean>() {
					@Override
					public void updateItem(Boolean item, boolean empty) {
						super.updateItem(item, empty);
						String index = empty ? null : getIndex() + StringUtils.EMPTY;
						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							StructureDetailsDto detailDto = getStructureDetailsTablePane().getTable().getItems().get(Integer.parseInt(index));
							HBox hb = new HBox();
							hb.setAlignment(Pos.CENTER);
							viewBtn = createBtn("View", getController());
							viewBtn.setPadding(new Insets(5));
							viewBtn.setId(index);
							viewBtn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 50px; -fx-font-size : 10pt;");
							if(isAuthorized) {

								if(detailDto.isStructureCreateFlag()) {
									createBtn = createBtn("Create", getController());
									hb.setSpacing(5);
									createBtn.setId(index);
									createBtn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 60px; -fx-font-size : 10pt;");
									hb.getChildren().addAll(createBtn);
								} else {
									deleteBtn = createBtn("Delete", getController());
									hb.setSpacing(5);
									deleteBtn.setId(index);
									deleteBtn.setStyle("-fx-pref-height: 5px; -fx-pref-width: 60px; -fx-font-size : 10pt;");
									hb.getChildren().addAll(viewBtn, deleteBtn);
								}
							} else {
								hb.getChildren().add(viewBtn);
							}
							
							setGraphic(hb);
						}
					}
				};
			}
		});
	}
	
	private boolean isAuthorized() {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		if(!StringUtils.isBlank(property.getDeleteAuthorizationGroup()))
		  return ClientMainFx.getInstance().getAccessControlManager().isAuthorized(property.getDeleteAuthorizationGroup().trim());
		
		return true;
	}
	
	private VBox createSearchDetailsPanel() {
		VBox searchDetails = new VBox();
		searchDetails.getChildren().addAll(createProductType(),createDivision(),  createProductAndLotRadio(), createProcessPointComboBox(), createSearchBtn());
		return searchDetails;
	}
	
	private HBox createProductType() {
		HBox productType = new HBox();
		productTypesComboBox =  createLabeledComboBox("Product Type", true, new Insets(3), true, true);
		productTypesComboBox.getControl().setMinHeight(30.0);
		productTypesComboBox.getControl().setMinWidth(200.0);
		productTypesComboBox.getControl().setPrefWidth(175);
		productTypesComboBox.getLabel().setPadding(new Insets(0,15,0,0));
		productTypesComboBox.getLabel().getStyleClass().add(Fonts.SS_DIALOG_BOLD(12));
		productType.getChildren().addAll(productTypesComboBox);
		
		
		return productType;
	}
	
	private HBox createDivision() {
		HBox division = new HBox();
		divisionComboBox = createLabeledComboBox("Division ", true, new Insets(3), true, true);
		divisionComboBox.getControl().setMinHeight(30.0);
		divisionComboBox.getControl().setMinWidth(200.0);
		divisionComboBox.getControl().setPrefWidth(175);
		divisionComboBox.getLabel().setPadding(new Insets(0,42,0,0));
		divisionComboBox.getLabel().getStyleClass().add(Fonts.SS_DIALOG_BOLD(12));
		division.getChildren().addAll(divisionComboBox);
		return division;
	}
	
	private HBox createProcessPointComboBox() {
		processPointHBox = new HBox();
		Label processPointLbl = new Label("Process Point *");
		processPointLbl.setPadding(new Insets(15,10,5,4));
		processPointLbl.setStyle("-fx-font-weight: bold ;");
		processPointComboBox = new LoggedComboBox<KeyValue<String, String>>("processPointComboBox");
		processPointComboBox.setMinHeight(30.0);
		processPointComboBox.setMaxHeight(30.0);
		processPointComboBox.setMinWidth(200.0);
		processPointComboBox.setPrefWidth(175);
		divisionComboBox.getLabel().getStyleClass().add(Fonts.SS_DIALOG_BOLD(12));
		processPointHBox.getChildren().addAll(processPointLbl, processPointComboBox);
		return processPointHBox;
	}
	
	private VBox createProductAndLotRadio() {
		VBox productAndLot = new VBox();
		ToggleGroup group = new ToggleGroup();
		HBox productIdHBox = new HBox();
		HBox productIdHBox1 = new HBox();
		HBox productIdHBox2 = new HBox();
		HBox productIdHBox3 = new HBox();
		productIdRadioButton = createRadioButton("", group, true, getController());
		
		productIdBtn = UiFactory.createButton("","-fx-font: 12 arial;",true);
		productIdBtn.setPrefSize(70, 25);
		ProductIdTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 16, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		ProductIdTextField.setPrefSize(195, 25);
		productIdHBox1.getChildren().add(productIdRadioButton);
		productIdHBox2.getChildren().add(productIdBtn);
		productIdHBox3.getChildren().add(ProductIdTextField);
		productIdHBox3.setPadding(new Insets(0, 0, 0, 10));
		productIdHBox.getChildren().addAll(productIdHBox1,productIdHBox2,productIdHBox3);
		productIdHBox.setPadding(new Insets(10, 0, 10, 0));
		
		HBox lotNoHBox = new HBox();
		LotNumberRadioButton = createRadioButton("Lot Number ", group, false, getController());
		lotNumberTextField = UiFactory.createUpperCaseFieldBean("filter-textfield", 16, Fonts.SS_DIALOG_PLAIN(12), TextFieldState.EDIT, Pos.BASELINE_LEFT);
		lotNoHBox.getChildren().addAll(LotNumberRadioButton, lotNumberTextField);
		lotNumberTextField.setDisable(true);
		productAndLot.getChildren().addAll(productIdHBox, lotNoHBox);
		
		productTypesComboBox.getControl().valueProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> ov,  String old_val, String new_val) { 
				if(productIdBtn != null){
					setProductTypeData(new_val);
					productIdBtn.setText(productTypeData.getProductIdLabel());
				}
			} 
		});
		
		productIdBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(final ActionEvent arg0) {
				ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(
						"Manual Product Entry Dialog",productTypeData,getMainWindow().getApplicationContext().getApplicationId());
				manualProductEntryDialog.showDialog();
				String productId = manualProductEntryDialog.getResultProductId();
				if (!StringUtils.isEmpty(productId)) {
					ProductIdTextField.setText(productId);
				}
			}

		});
		
		ProductIdTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	clearCompletePanel();
		    }
		});
		
		lotNumberTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	clearCompletePanel();
		    }
		});
		
		return productAndLot;
	}
	
	protected void clearCompletePanel() {
    	getController().clearPanel();
    	getRoot().setValue(new StructureDeleteDTO("", "", "", "", "", ""));
    	getRoot().getParent().setGraphic(null);
    	getStructureDetailsTablePane().setData(new ArrayList<StructureDetailsDto>());
	}

	public void setProductTypeData(String productType) {
		for (ProductTypeData type : getMainWindow().getApplicationContext().getProductTypeDataList()) {
			if (productType != null && type.getProductTypeName().equals(productType.trim())) {
				this.productTypeData = type;
				break;
			}
		}
	}
	
	private HBox createSearchBtn() {
		HBox searchBtnHBox = new HBox();
		searchBtn = createBtn("Search",getController());
		searchBtnHBox.getChildren().add(searchBtn);
		searchBtnHBox.setPrefSize(250,20);
		searchBtnHBox.setPadding(new Insets(10,0,0,20));
		HBox.setHgrow(searchBtnHBox, Priority.ALWAYS);
		searchBtnHBox.setAlignment(Pos.CENTER_RIGHT);
		return searchBtnHBox;
	}
	
	private LabeledComboBox<String> createLabeledComboBox(String label,boolean isHorizontal, Insets insets, boolean isLabelBold, boolean isMandaotry) {
		LabeledComboBox<String> comboBox = new LabeledComboBox<String>(label,isHorizontal,insets,isLabelBold,isMandaotry);
		return comboBox;
	}
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getScreenName() {
		return "Structure Maintenance Screen";
	}
	
	
    public LoggedRadioButton createRadioButton(String title, ToggleGroup group, boolean isSelected, EventHandler<ActionEvent> handler) {
		LoggedRadioButton radioButton = new LoggedRadioButton(title);
		radioButton.getStyleClass().add("radio-btn");
		radioButton.setToggleGroup(group);
		radioButton.setSelected(isSelected);
		radioButton.setOnAction(handler);
		return radioButton;
	}
    
    public LoggedButton createBtn(String text,EventHandler<ActionEvent> handler) {
		LoggedButton btn = UiFactory.createButton(text, text);
		btn.defaultButtonProperty().bind(btn.focusedProperty());
		btn.setOnAction(handler);
		btn.getStyleClass().add("table-button");
		return btn;
	}
    
	public ComboBox<String> getProductTypesComboBox() {
		return productTypesComboBox.getControl();
	}
	public void setProductTypesComboBox(LabeledComboBox<String> productTypesComboBox) {
		this.productTypesComboBox = productTypesComboBox;
	}
	public ComboBox<String> getDivisionComboBox() {
		return divisionComboBox.getControl();
	}
	public void setDivisionComboBox(LabeledComboBox<String> divisionComboBox) {
		this.divisionComboBox = divisionComboBox;
	}
	public LoggedComboBox<KeyValue<String, String>> getProcessPointComboBox() {
		return processPointComboBox;
	}
	public void setProcessPointComboBox(
			LoggedComboBox<KeyValue<String, String>> processPointComboBox) {
		this.processPointComboBox = processPointComboBox;
	}
	public LoggedRadioButton getProductIdRadioButton() {
		return productIdRadioButton;
	}
	public void setProductIdRadioButton(LoggedRadioButton productIdRadioButton) {
		this.productIdRadioButton = productIdRadioButton;
	}
	public LoggedRadioButton getLotNumberRadioButton() {
		return LotNumberRadioButton;
	}
	public void setLotNumberRadioButton(LoggedRadioButton lotNumberRadioButton) {
		LotNumberRadioButton = lotNumberRadioButton;
	}
	public UpperCaseFieldBean getProductIdTextField() {
		return ProductIdTextField;
	}
	public void setProductIdTextField(UpperCaseFieldBean productIdTextField) {
		ProductIdTextField = productIdTextField;
	}
	public UpperCaseFieldBean getLotNumberTextField() {
		return lotNumberTextField;
	}
	public void setLotNumberTextField(UpperCaseFieldBean lotNumberTextField) {
		this.lotNumberTextField = lotNumberTextField;
	}
	public LoggedButton getSearchBtn() {
		return searchBtn;
	}
	public void setSearchBtn(LoggedButton searchBtn) {
		this.searchBtn = searchBtn;
	}
	public ObjectTablePane<StructureDetailsDto> getStructureDetailsTablePane() {
		return structureDetailsTablePane;
	}
	public void setStructureDetailsTablePane(
			ObjectTablePane<StructureDetailsDto> structureDetailsTablePane) {
		this.structureDetailsTablePane = structureDetailsTablePane;
	}
	public TitledPane getSearchTitledPane() {
		return searchTitledPane;
	}
	public void setSearchTitledPane(TitledPane searchTitledPane) {
		this.searchTitledPane = searchTitledPane;
	}
	public TitledPane getDetailsTitledPane() {
		return detailsTitledPane;
	}
	public void setDetailsTitledPane(TitledPane detailsTitledPane) {
		this.detailsTitledPane = detailsTitledPane;
	}
	public LoggedButton getViewBtn() {
		return viewBtn;
	}
	public void setViewBtn(LoggedButton viewBtn) {
		this.viewBtn = viewBtn;
	}
	public LoggedButton getDeleteBtn() {
		return deleteBtn;
	}
	public void setDeleteBtn(LoggedButton deleteBtn) {
		this.deleteBtn = deleteBtn;
	}
	public TreeItem<StructureDeleteDTO> getRoot() {
		return root;
	}
	public void setRoot(TreeItem<StructureDeleteDTO> root) {
		this.root = root;
	}
	public HBox getProcessPointHBox() {
		return processPointHBox;
	}
	public void setProcessPointHBox(HBox processPointHBox) {
		this.processPointHBox = processPointHBox;
	}
	public Hyperlink getExportInCsvLink() {
		return exportInCsvLink;
	}
	public void setExportInCsvLink(Hyperlink exportInCsvLink) {
		this.exportInCsvLink = exportInCsvLink;
	}


	public ProductTypeData getProductTypeData() {
		return productTypeData;
	}

	public Button getProductIdBtn() {
		return productIdBtn;
	}

	public void setProductIdBtn(Button productIdBtn) {
		this.productIdBtn = productIdBtn;
	}

	public LoggedButton getCreateBtn() {
		return createBtn;
	}
	
	
	
	
	
}
