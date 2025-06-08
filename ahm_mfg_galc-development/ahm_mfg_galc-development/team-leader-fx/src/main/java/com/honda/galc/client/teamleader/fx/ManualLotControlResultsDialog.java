 package com.honda.galc.client.teamleader.fx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotControlResultsController;
import com.honda.galc.client.teamlead.ltCtrRepair.ManualLotCtrRepairUtil;
import com.honda.galc.client.teamleader.model.PartResult;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.FxDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.StyleUtil;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.client.utils.UiUtils;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.OperationType;
import com.honda.galc.constant.PartType;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationPartRevision;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartHistory;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.util.CommonPartUtility;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class ManualLotControlResultsDialog extends FxDialog {
	
	private static final String PRODUCT_ID = "Product ID";
	private TextField productIdTextField;
	private TextField productTypeTextField;
	private TextField operationNameTextField;
	private TextArea partDesc;
	private TableView<InstalledPartHistory> partInformationTableView;
	private TableView<MeasurementAttempt> measurementInformationTableView;
	private Label messageLabel;
	protected volatile ArrayList<TextField> inputFields = new ArrayList<TextField>();
	protected volatile ArrayList<TextField> measRejectFields = new ArrayList<TextField>();
	protected volatile ArrayList<TextField> partRejectFields = new ArrayList<TextField>();
	protected volatile ArrayList<TextField> measSaveFields = new ArrayList<TextField>();
	protected volatile ArrayList<Button> skipRejectBtns = new ArrayList<Button>();
	protected volatile ArrayList<Button> measRejectbtns = new ArrayList<Button>();
	protected volatile ArrayList<Button> saveMeasBtns = new ArrayList<Button>();
	protected volatile ArrayList<Button> savePartBtns = new ArrayList<Button>();
	private BaseProduct baseProduct;
	private MCOperationRevision operation;
	private PartResult partResult;
	protected HashMap<String ,MCOperationPartRevision> saveBtnList = new HashMap<String ,MCOperationPartRevision>();
	private Image imageComplete = new Image("resource/images/common/confirm.png");
	private Image imageReject = new Image("resource/images/common/reject.png");
	
	//style
	final private String font_style="-fx-font: 14 arial;";
	final private String bold_font_style="-fx-font: 14 arial;-fx-font-weight: bold;";
	private boolean isFocused;
	
	ManualLotControlRepairActions action;
	
	private LoggedTextField partNameTextField;
	private LoggedTextField partSerialNumTextField;
	private LoggedTextField measurementValueTextField;
	private ObjectTablePane<Measurement> measurementDetailsTablePane;
	private List<Measurement> measurementData;
	private LoggedButton addMeasurementBtn;
	private LoggedButton deleteMeasurementBtn;
	private Button insertResultBtn;
	private Button cancelBtn;
	private int measurementSequenceNumber;

	private ManualLotControlResultsController controller;

	public ManualLotControlResultsDialog(String title, Stage owner, BaseProduct baseProduct,
			MCOperationRevision operation, PartResult partResult, ManualLotControlRepairActions action) {
		super(title, owner);
		this.partResult = partResult;
		this.operation = operation;
		this.baseProduct = baseProduct;
		this.action = action;
		this.messageLabel = UiFactory.createLabel("messageLabel", "");
		this.measurementSequenceNumber = 0;
		this.measurementData = new ArrayList<Measurement>();
		initComponents();
	}

	public void initComponents() {
		int rowIndex = 0;
		GridPane mainPane = createGridPane();
		
		BorderPane border = new BorderPane();
		List<ProductNumberDef> productNumberDefList = ProductNumberDef.getProductNumberDef(baseProduct.getProductType());
		String label = productNumberDefList.isEmpty() ? PRODUCT_ID : productNumberDefList.get(0).getName();
		
		productIdTextField = createTextField("productIdTextField", TextFieldState.READ_ONLY);
		mainPane.addRow(rowIndex++, UiFactory.createLabel(label,label, bold_font_style), productIdTextField);
		
		productTypeTextField = createTextField("productTypeTextField", TextFieldState.READ_ONLY);
		mainPane.addRow(rowIndex++, UiFactory.createLabel("productTypeLabel", "MTOC ",bold_font_style), productTypeTextField);
		
		operationNameTextField = createTextField("operationNameTextField", TextFieldState.READ_ONLY);
		if (!ManualLotControlRepairActions.isInsertResultsAction(action)) {
			mainPane.addRow(rowIndex++, UiFactory.createLabel("operationNameLabel", "Part/Operation Name ",bold_font_style), operationNameTextField);
		}
		partDesc = UiFactory.createTextArea();
		partDesc.setWrapText(true);
		partDesc.setPrefRowCount(4);
		partDesc.setPrefSize(400, 200);
		partDesc.setFocusTraversable(false);
		if(partResult != null && partResult.getInstalledPart() != null &&
				!(partResult.getInstalledPart().getInstalledPartReason().equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT))) {
			mainPane.addRow(rowIndex++, UiFactory.createLabel("partDescLabel", "Unit Description",bold_font_style), partDesc);
		}
		this.controller = new ManualLotControlResultsController(baseProduct, partResult, operation, this);
		if (action == ManualLotControlRepairActions.EDIT_RESULTS) {
			mainPane.add(createResultSection(), 0, rowIndex++, 2, 1);
		} else if(action == ManualLotControlRepairActions.SHOW_HISTORY){
			mainPane.add(createPartHistory(), 0, rowIndex++, 2, 1);
			mainPane.add(createMeasurementHistory(), 0, rowIndex++, 2, 1);
		} else if(ManualLotControlRepairActions.isInsertResultsAction(action)) {
			mainPane.add(createInsertResultsPane(), 0, rowIndex++, 2, 1);
		}
		ScrollPane scrollPane = new ScrollPane(mainPane);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		border.setCenter(scrollPane);
		border.setBottom(createErrorMessageSection());
		Scene scene = new Scene(border, Screen.getPrimary().getVisualBounds().getWidth() * 0.50, Screen.getPrimary().getVisualBounds().getHeight() * 0.9);
		this.setScene(scene);
		this.sizeToScene();
		controller.populateComponents(action);
		addListeners();
	}

	private LoggedTextField createTextField(String id, TextFieldState state) {
		LoggedTextField productIdTextField = UiFactory.createTextField("productIdTextField", 17,state);
		productIdTextField.setFocusTraversable(false);
		productIdTextField.requestFocus();
		productIdTextField.setStyle(font_style);
		return productIdTextField;
	}
	
	private VBox createErrorMessageSection() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(10, 10, 10, 10));

		messageLabel.setAlignment(Pos.TOP_LEFT);
		HBox.setHgrow(messageLabel, Priority.ALWAYS);
		messageLabel.setMaxWidth(Double.MAX_VALUE);
		messageLabel.setMinWidth(Region.USE_PREF_SIZE);
		messageLabel.setMinHeight(Region.USE_PREF_SIZE);
		hbox.getChildren().add(messageLabel);
		VBox messageBox = new VBox();
		messageBox.getChildren().add(hbox);
		return messageBox;
	}

	public void setErrorMessage(String errorMessage) {
		if (!StringUtils.isEmpty(errorMessage)) {
			messageLabel.setId("status-error-message");
			String style = "-fx-fill: black;   -fx-background-color: red;   -fx-font-weight: bold;   -fx-font-size: 20; "
					+ "-fx-border-color: black;-fx-border-insets: 1; -fx-border-width: 1;   -fx-border-style: solid;";
			messageLabel.setStyle(style);
		}

		messageLabel.setText(errorMessage);
	}

	public void setMessage(String message) {
		if (!StringUtils.isEmpty(message)) {
			messageLabel.setId("status-message");
			String style = "-fx-fill: black;  -fx-background-color: transparent;  -fx-font-weight: normal;  -fx-font-size: 20;"
					+ " -fx-border-color: black;  -fx-border-insets: 1;  -fx-border-width: 1; -fx-border-style: solid;";
			messageLabel.setStyle(style);
		}

		messageLabel.setText(message);
	}

	private VBox createResultSection() {
		isFocused=false;
		VBox vBox = new VBox(10);
		if (operation != null) {
			if(ManualLotCtrRepairUtil.isInstructionOrAutoCompleteOperation(operation)){
				addCompleteButton(vBox);
			}else{
				// Add part scan box
				for (MCOperationPartRevision part : operation.getParts()) {
					
					if (ManualLotCtrRepairUtil.hasScanPart(operation)) {
						if(PartType.MFG.equals(part.getPartType())){ 
							VBox partBox = new VBox(5);
							addPartScanBox(partBox, part);
							if(operation.getParts().size() >1)
								setBorder(partBox);
							vBox.getChildren().add(partBox);
						}
					}else if (ManualLotCtrRepairUtil.isMeasOnlyOperation(operation)) {
						if(PartType.MFG.equals(part.getPartType())) addMeasurementBoxes(vBox, part ,true);
					}
				}
			}
		} else {
			if (partResult.getLotControlRule() != null) {
				LotControlRule rule = partResult.getLotControlRule();
				List<PartSpec> parts = rule.getParts();
				for (PartSpec part : parts) {
				//part serial no. text field will not be shown if scan type is none
					if(rule.getSerialNumberScanType() != PartSerialNumberScanType.NONE){
						VBox lotControlPartBox = new VBox(5);
						addLotControlPartScanBox(lotControlPartBox, part);
						if(partResult.getLotControlRule().getParts().size() >1)
							setBorder(lotControlPartBox);
						vBox.getChildren().add(lotControlPartBox);
					}else{
						if (part != null && part.getMeasurementSpecs().size() > 0) {
							addLotControlMeasurementBoxes(vBox, part ,true);
						}
					}
				}
			} else if(partResult.getInstalledPart().getInstalledPartReason().equalsIgnoreCase(ApplicationConstants.MANUAL_INSERT)){
				addMeasurementTablePane(vBox, partResult.getInstalledPart());
			}
		}
		
		return vBox;
	}

	@SuppressWarnings("unchecked")
	private VBox createPartHistory() {

		VBox vbox = new VBox();
		vbox.setSpacing(3.0);
		vbox.setPadding(new Insets(10, 10, 10, 10));

		vbox.getChildren()
				.add(UiFactory.createLabel("partHistory", "Part Information", bold_font_style));
		partInformationTableView = UiFactory.createTableView(InstalledPartHistory.class);
		partInformationTableView.setEditable(false);

		TableColumn<InstalledPartHistory, String> serialCol = UiFactory.createTableColumn(InstalledPartHistory.class,
				String.class);
		serialCol.setText("Serial Number");
		serialCol.setCellValueFactory(
				new Callback<CellDataFeatures<InstalledPartHistory, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<InstalledPartHistory, String> p) {
						return new ReadOnlyObjectWrapper<String>(p.getValue().getPartSerialNumber());
					}
				});

		TableColumn<InstalledPartHistory, String> statusCol = UiFactory.createTableColumn(InstalledPartHistory.class,
				String.class);
		statusCol.setText("Part Status");
		statusCol.setCellValueFactory(
				new Callback<CellDataFeatures<InstalledPartHistory, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<InstalledPartHistory, String> p) {
						return new ReadOnlyObjectWrapper<String>(
								InstalledPartStatus.getType(p.getValue().getInstalledPartStatusId()).name());
					}
				});

		TableColumn<InstalledPartHistory, String> timestampCol = UiFactory.createTableColumn(InstalledPartHistory.class,
				String.class);
		timestampCol.setText("Timestamp");
		timestampCol.setCellValueFactory(
				new Callback<CellDataFeatures<InstalledPartHistory, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<InstalledPartHistory, String> p) {
						return new ReadOnlyObjectWrapper<String>(p.getValue().getId().getActualTimestamp().toString());
					}
				});

		TableColumn<InstalledPartHistory, String> associateCol = UiFactory.createTableColumn(InstalledPartHistory.class,
				String.class);
		associateCol.setText("Associate");
		associateCol.setCellValueFactory(
				new Callback<CellDataFeatures<InstalledPartHistory, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<InstalledPartHistory, String> p) {
						return new ReadOnlyObjectWrapper<String>(p.getValue().getAssociateNo());
					}
				});

		partInformationTableView.getColumns().clear();
		partInformationTableView.getColumns().addAll(serialCol, statusCol, timestampCol, associateCol);

		vbox.getChildren().add(partInformationTableView);

		return vbox;

	}

	@SuppressWarnings("unchecked")
	private VBox createMeasurementHistory() {

		VBox vbox = new VBox();
		vbox.setSpacing(3.0);
		vbox.setPadding(new Insets(10, 10, 10, 10));

		vbox.getChildren().add(
				UiFactory.createLabel("meaHistory", "Measurement Information", bold_font_style));
		measurementInformationTableView = UiFactory.createTableView(MeasurementAttempt.class);
		measurementInformationTableView.setEditable(false);

		TableColumn<MeasurementAttempt, Integer> meaSeqCol = UiFactory.createTableColumn(MeasurementAttempt.class,
				Integer.class);
		meaSeqCol.setText("Measurement Seq#");
		meaSeqCol.setCellValueFactory(
				new Callback<CellDataFeatures<MeasurementAttempt, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<MeasurementAttempt, Integer> p) {
						return new ReadOnlyObjectWrapper<Integer>(p.getValue().getId().getMeasurementSequenceNumber());
					}
				});

		TableColumn<MeasurementAttempt, String> meaResultCol = UiFactory.createTableColumn(MeasurementAttempt.class,
				String.class);
		meaResultCol.setText("Measurement Result");
		meaResultCol.setCellValueFactory(
				new Callback<CellDataFeatures<MeasurementAttempt, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<MeasurementAttempt, String> p) {
						return new ReadOnlyObjectWrapper<String>(
								(new Double(p.getValue().getMeasurementValue())).toString());
					}
				});

		TableColumn<MeasurementAttempt, String> meaStatusCol = UiFactory.createTableColumn(MeasurementAttempt.class,
				String.class);
		meaStatusCol.setText("Measurement Status");
		meaStatusCol.setCellValueFactory(
				new Callback<CellDataFeatures<MeasurementAttempt, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<MeasurementAttempt, String> p) {
						return new ReadOnlyObjectWrapper<String>(p.getValue().getMeasurementStatus().name());
					}
				});

		TableColumn<MeasurementAttempt, String> timestampCol = UiFactory.createTableColumn(MeasurementAttempt.class,
				String.class);
		timestampCol.setText("Timestamp");
		timestampCol.setCellValueFactory(
				new Callback<CellDataFeatures<MeasurementAttempt, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<MeasurementAttempt, String> p) {
						String timestamp = p.getValue().getActualTimestamp() == null ? ""
								: p.getValue().getActualTimestamp().toString();
						return new ReadOnlyObjectWrapper<String>(timestamp);
					}
				});

		measurementInformationTableView.getColumns().clear();
		measurementInformationTableView.getColumns().addAll(meaSeqCol, meaResultCol, meaStatusCol, timestampCol);

		vbox.getChildren().add(measurementInformationTableView);

		return vbox;
	}
	
	public void addCompleteButton(VBox vBox) {
		HBox hBox = new HBox(10);
		if (partResult.getBuildResult() != null && partResult.getBuildResult().isStatusOk()) {
			Button skipRejectBtn = UiFactory.createButton("Reject", true);
			skipRejectBtn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageReject),20));
			setSkipRejectPartAction(skipRejectBtn);
			skipRejectBtns.add(skipRejectBtn);
			hBox.getChildren().add(skipRejectBtn);
		}else{
			Button completeButton = UiFactory.createButton("Complete", true);
			saveInstruction(completeButton);
			completeButton.setGraphic(StyleUtil.normalizeImage(new ImageView(imageComplete),20));
			hBox.getChildren().add(completeButton);
		}
		hBox.setAlignment(Pos.BASELINE_CENTER);
		vBox.getChildren().add(hBox);
		vBox.setPadding(new Insets(7));
	}

	public void addPartScanBox(VBox vBox, MCOperationPartRevision part) {
		HBox hBox = new HBox(5);
		boolean isPartScaned =false ;
		String partMaskDisp = CommonPartUtility.parsePartMask(part.getPartMask());
		final TextField textFieldPart = UiFactory.createTextField(part.getId().getPartId(), font_style, TextFieldState.EDIT,
				350.0);
		textFieldPart.setPromptText("Enter part SN");
		Label labelPartMask = UiFactory.createLabel("lblPartMask", "  " + partMaskDisp, font_style,
				TextAlignment.RIGHT, 150);
		if ( partResult.getBuildResult()!= null  &&  partResult.getInstalledPart().getPartId().equalsIgnoreCase(part.getId().getPartId())
				 && partResult.getBuildResult().getPartSerialNumber().length() > 0  ) {
			setPromptText(partResult.getBuildResult().getPartSerialNumber(), textFieldPart, hBox);
			Button skipRejectBtn = UiFactory.createButton("", true);
			isPartScaned = true;
			partRejectFields.add(textFieldPart);
			skipRejectBtn.setId(part.getId().getPartId());
			setSkipRejectPartAction(skipRejectBtn);
			skipRejectBtns.add(skipRejectBtn);
			setBtnGraphics(skipRejectBtn , imageReject);
			textFieldPart.setDisable(true);
			hBox.getChildren().add(skipRejectBtn);
			
		} else {
			setPromptText(partMaskDisp, textFieldPart, hBox);
			Button savePartBtn = UiFactory.createButton("", true);
			savePartBtn.setId(part.getId().getPartId());
			savePartBtns.add(savePartBtn);
			if(partResult.getBuildResult()!= null && StringUtils.isNotBlank(partResult.getBuildResult().getPartSerialNumber()) ){
				savePartBtn.setDisable(true);
				textFieldPart.setDisable(true);
			}
			if(partResult.getBuildResult()== null && !isFocused){
				UiUtils.requestFocus(textFieldPart);
				isFocused=true;
			}
			savePartBtnAction(savePartBtn);
			setBtnGraphics(savePartBtn, imageComplete);
			textFieldPart.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					savePart(textFieldPart);
				}
			});
			hBox.getChildren().add(savePartBtn);
		}
		hBox.getChildren().add(UiFactory.createLabel("lblEmpty2", " ", font_style, 5));
		vBox.getChildren().add(labelPartMask);
		vBox.getChildren().add(hBox);

		if (operation != null && (operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS)
				|| operation.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL))) {
			addMeasurementBoxes(vBox, part, isPartScaned);
		}

	}

	public void addLotControlPartScanBox(VBox vBox, PartSpec part) {
		HBox hBox = new HBox(5);
		String partMaskDisp = part == null ? "*" : part.getPartSerialNumberMask();

		final TextField textFieldPart = UiFactory.createTextField(part ==null ? partResult.getPartName() : part.getId().getPartId(), font_style, TextFieldState.EDIT,
				350.0);
		textFieldPart.setPromptText("Enter part SN");

		Label labelPartMask = UiFactory.createLabel("lblPartMask", "  " + partMaskDisp, font_style,
				TextAlignment.RIGHT, 150);
		
		boolean isPartScanned  = false;
		//checks part's part id is same as installed part's id and validate part serial no. value with part mask 
		if (partResult.getBuildResult() != null &&  partResult.getInstalledPart() != null && part!=null 
				&& partResult.getInstalledPart().getPartId().equalsIgnoreCase(part.getId().getPartId())
				&& partResult.getBuildResult().getPartSerialNumber().length() > 0 &&   controller.validatePart(partResult.getBuildResult().getPartSerialNumber())) {
			setPromptText(partResult.getBuildResult().getPartSerialNumber(),textFieldPart,hBox);
			partRejectFields.add(textFieldPart);
			Button skipRejectBtn = UiFactory.createButton("", true);
			skipRejectBtn.setId(part ==null ? partResult.getPartName() : part.getId().getPartId());
			setSkipRejectPartAction(skipRejectBtn);
			skipRejectBtns.add(skipRejectBtn);
			textFieldPart.setDisable(true);
			isPartScanned = true;
			setBtnGraphics(skipRejectBtn , imageReject);
			hBox.getChildren().add(skipRejectBtn);
		} else {
			setPromptText(partMaskDisp,textFieldPart,hBox);
			
			Button savePartBtn = UiFactory.createButton("", true);
			setBtnGraphics(savePartBtn ,imageComplete);
			savePartBtn.setId(part ==null ? partResult.getPartName() : part.getId().getPartId());
			savePartBtnAction(savePartBtn);
			if(partResult.getBuildResult()== null && !isFocused){
				UiUtils.requestFocus(textFieldPart);
				isFocused=true;
			}
			textFieldPart.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					savePart(textFieldPart);
				}
			});
			savePartBtns.add(savePartBtn);
			if(partResult.getBuildResult()!= null && StringUtils.isNotBlank(partResult.getBuildResult().getPartSerialNumber()) ){
				savePartBtn.setDisable(true);
				textFieldPart.setDisable(true);
			}
			hBox.getChildren().add(savePartBtn);
		}
		hBox.getChildren().add(UiFactory.createLabel("lblEmpty2", " ", font_style, 5));
		vBox.getChildren().add(labelPartMask);
		vBox.getChildren().add(hBox);

		if (part != null && part.getMeasurementSpecs().size() > 0) {
			addLotControlMeasurementBoxes(vBox, part ,isPartScanned);
		}

	}
	private void setPromptText(String text, TextField textFieldPart, HBox hBox){
		textFieldPart.setPromptText(text);
		inputFields.add(textFieldPart);
		textFieldPart.setEditable(true);
		textFieldPart.setFocusTraversable(true);
		hBox.getChildren().add(UiFactory.createLabel("lblEmpty1", " ", font_style, 5));
		hBox.getChildren().add(textFieldPart);
	}

	public void addLotControlMeasurementBoxes(VBox vBox, PartSpec part , boolean ispartScanned) {

		HBox measRow = new HBox(10);
		GridPane measColumn = new GridPane();
		// Add Measurement boxes
		int row =  1;
		int column = 1;
		for (int i = 0; i < part.getMeasurementSpecs().size(); i++) {

			MeasurementSpec measurementSpec = part.getMeasurementSpecs().get(i);
			String minMax = new Double(measurementSpec.getMinimumLimit()).toString() + "/"
					+ new Double(measurementSpec.getMaximumLimit()).toString();
			int seq = measurementSpec.getId().getMeasurementSeqNum();
			Double existingMeasuremntValue = showRejectMeas(seq ,part.getId().getPartId());
			if(i % 3 ==0){
				column = column+1;
				row = 1;
			}
			if (existingMeasuremntValue != null) {
				Label labelMeasSpec = UiFactory.createLabel("lblMeasSpec", "  " + minMax, font_style,
						TextAlignment.RIGHT, 150);

				HBox hBox = new HBox(10);
				TextField textFieldMeasurement = UiFactory.createTextField(part ==null ? partResult.getPartName() : part.getId().getPartId() +"_" +seq , font_style,
						TextFieldState.READ_ONLY, 100, 30);
				textFieldMeasurement.setPromptText(String.valueOf(existingMeasuremntValue));
				measRejectFields.add(textFieldMeasurement);

				Button measRejectBtn = UiFactory.createButton("", true);
				setBtnGraphics(measRejectBtn ,imageReject);
				measRejectBtn.setId("reject" + i);
				setRejectMeasAction(measRejectBtn);
				measRejectbtns.add(measRejectBtn);
				textFieldMeasurement.setDisable(true);
				VBox measRejectContainer = new VBox(10);
				hBox.getChildren().add(UiFactory.createLabel("lblEmpty", " ", font_style, 5));
				hBox.getChildren().add(textFieldMeasurement );
				hBox.getChildren().add(measRejectBtn);
				measRejectContainer.getChildren().addAll(labelMeasSpec,hBox);
				measColumn.add(measRejectContainer , ++row , column);
				if(row!= 2)
					GridPane.setMargin(measRejectContainer, new Insets(5, 10, 5,  Screen.getPrimary().getVisualBounds().getWidth() * 0.06));
			} else {
				Label labelMeasSpec1 = UiFactory.createLabel("lblMeasSpec", "  " + minMax, font_style,
						TextAlignment.RIGHT, 150);
				HBox hBox1 = new HBox(10);
				final TextField textFieldMeasurement = UiFactory.createTextField(part ==null ? partResult.getPartName() : part.getId().getPartId() +"_" + seq, font_style,
						TextFieldState.EDIT, 100, 30);
				textFieldMeasurement.setPromptText(minMax);
				measSaveFields.add(textFieldMeasurement);
				if(ispartScanned && !isFocused){
					UiUtils.requestFocus(textFieldMeasurement);
					isFocused=true;
				}
				Button saveMeasBtn = UiFactory.createButton("", true);
				setBtnGraphics(saveMeasBtn ,imageComplete);
				saveMeasBtn.setPrefHeight(20);
				saveMeasBtn.setId("saveMeas" + i);
				saveMeasBtnAction(saveMeasBtn);
				
				textFieldMeasurement.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						setSaveMeasAction(textFieldMeasurement);
					}
				});
				saveMeasBtns.add(saveMeasBtn);
				VBox measSaveContainer = new VBox(10);
				hBox1.getChildren().add(UiFactory.createLabel("lblEmpty", " ", font_style, 5));
				hBox1.getChildren().add(textFieldMeasurement);
				hBox1.getChildren().add(saveMeasBtn);
				if(!ispartScanned){
					saveMeasBtn.setDisable(true);
					textFieldMeasurement.setDisable(true);
				}
				measSaveContainer.getChildren().addAll(labelMeasSpec1,hBox1);
				measColumn.add(measSaveContainer ,++row , column);
				if(row!= 2)
					GridPane.setMargin(measSaveContainer, new Insets(5, 10, 5,  Screen.getPrimary().getVisualBounds().getWidth() * 0.06));
			}
		}
		measRow.getChildren().add(measColumn);
		vBox.getChildren().add(measRow);
		
	}

	public void addMeasurementBoxes(VBox vBox, MCOperationPartRevision part , boolean ispartScanned) {
		HBox measRow = new HBox();
		GridPane measColumn = new GridPane();
		// Add Measurement boxes
		int row =  1;
		int column = 1;
		for (int i = 0; i < getMeasurementCount(part); i++) {
			MCOperationMeasurement measurementSpec = operation.getSelectedPart().getMeasurement(i);
			String minMax = new Double(measurementSpec.getMinLimit()).toString() + "/"
					+ new Double(measurementSpec.getMaxLimit());
			int seq = measurementSpec.getId().getMeasurementSeqNum();
			Double existingMeasurementValue = showRejectMeas(seq ,part.getId().getPartId() );
			if(i % 3 ==0){
				column= column+1;
				row =1;
			}
			if (existingMeasurementValue != null) {
				Label labelMeasSpec = UiFactory.createLabel("lblMeasSpec", "  " + minMax, font_style,
						TextAlignment.RIGHT, 150);
				HBox hBox = new HBox(5);
				TextField textFieldMeasurement = UiFactory.createTextField(part.getId().getPartId()+"_" + seq, font_style,
						TextFieldState.READ_ONLY, 100, 30);
				textFieldMeasurement.setPromptText(String.valueOf(existingMeasurementValue));
				textFieldMeasurement.setDisable(true);
				measRejectFields.add(textFieldMeasurement);
				Button measRejectBtn = UiFactory.createButton("", true);
				setBtnGraphics(measRejectBtn ,imageReject);
				measRejectBtn.setId("reject" + i);
				setRejectMeasAction(measRejectBtn);
				measRejectbtns.add(measRejectBtn);
				
				VBox measRejectContainer = new VBox(10);
				hBox.getChildren().add(UiFactory.createLabel("lblEmpty", " ", font_style, 5));
				hBox.getChildren().add(textFieldMeasurement);
				hBox.getChildren().add(measRejectBtn);
				measRejectContainer.getChildren().addAll(labelMeasSpec,hBox);
				measColumn.add(measRejectContainer,row++,column);
				if(row!= 2)
					GridPane.setMargin(measRejectContainer, new Insets(0,0,0,  Screen.getPrimary().getVisualBounds().getWidth() * 0.06));
			} else {
				Label labelMeasSpec1 = UiFactory.createLabel("lblMeasSpec", "  " + minMax, font_style,
						TextAlignment.RIGHT, 150);
				HBox hBox1 = new HBox(5);
				final TextField textFieldMeasurement = UiFactory.createTextField(part.getId().getPartId()+"_" + seq, font_style,
						TextFieldState.EDIT, 100, 30);
				textFieldMeasurement.setPromptText(minMax);
				measSaveFields.add(textFieldMeasurement);
				Button saveMeasBtn = UiFactory.createButton("", true);
				setBtnGraphics(saveMeasBtn , imageComplete);
				if(ispartScanned && !isFocused){
					UiUtils.requestFocus(textFieldMeasurement);
					isFocused=true;
				}
				saveMeasBtn.setId("saveMeas" + i);
				saveMeasBtnAction(saveMeasBtn);
				textFieldMeasurement.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						setSaveMeasAction(textFieldMeasurement);
					}
				});
				saveMeasBtns.add(saveMeasBtn);
				
				VBox measContainer = new VBox(10);
				hBox1.getChildren().add(UiFactory.createLabel("lblEmpty", " ", font_style, 5));
				hBox1.getChildren().add(textFieldMeasurement);
				hBox1.getChildren().add(saveMeasBtn);
				saveMeasBtn.setDisable(true);
				saveMeasBtn.setPrefSize(10, 10);
				textFieldMeasurement.setDisable(true);
				if(ispartScanned){
					saveMeasBtn.setDisable(false);
					textFieldMeasurement.setDisable(false);
				}
				measContainer.getChildren().addAll(labelMeasSpec1,hBox1);
				measColumn.add(measContainer,row++,column);
				if(row!= 2)
					GridPane.setMargin(measContainer, new Insets(5, 10, 5, Screen.getPrimary().getVisualBounds().getWidth() * 0.06));
			}
		}
		measRow.getChildren().add(measColumn);
		vBox.getChildren().add(measRow);
	}
	
	protected void setRejectMeasAction(Button measRejectBtn) {
		measRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				int index = measRejectbtns.indexOf(btn);
				TextField txtField = measRejectFields.get(index);
				String textfieldArr [] = txtField.getId().split("_");
				String partId = textfieldArr[0];
				int seq = Integer.parseInt(textfieldArr[1]);
				if (partResult.getBuildResult() != null && partResult.getBuildResult().getMeasurements().size() > 0) {
					if(operation !=null){
						MCOperationMeasurement opMeasurement = operation.getSelectedPart().getMeasurement(seq-1);
						for(Measurement m : partResult.getBuildResult().getMeasurements()){
							if(m.getId().getMeasurementSequenceNumber() == opMeasurement.getId().getMeasurementSeqNum()){
								controller.rejectMeasurement(m);
								break;
							}
						}
					}else{
						LotControlRule rule = partResult.getLotControlRule();
						MeasurementSpec selectedMeasSpec = null;
						if(rule != null){
							for(PartSpec  partSpec : rule.getParts()){
								if(partId.equalsIgnoreCase(partSpec.getId().getPartId())){
									selectedMeasSpec = partSpec.getMeasurementSpecs().get(seq-1);
									break;
								}
							}
							controller.rejectMeasurement( controller.getMeasurement(selectedMeasSpec));
						}
					}
				}
				reloadView();
			}
		});
	}
	
	protected void saveMeasBtnAction(Button saveBtn) {
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				int index = saveMeasBtns.indexOf(btn);
				TextField txtField = measSaveFields.get(index);
				setSaveMeasAction(txtField);
			}
		});
	}
	
	
	protected void setSaveMeasAction(TextField txtField) {
		String textfieldArr [] = txtField.getId().split("_");
		String partId = textfieldArr[0];
		int seq = Integer.parseInt(textfieldArr[1]);
		String measValue = txtField.getText();
		if(StringUtils.isEmpty(measValue)){
			this.setErrorMessage("Please enter measurement value ");
			return;
		}
		if (operation != null) {
			MCOperationMeasurement m = operation.getSelectedPart().getMeasurement(seq-1);
			controller.saveMeasurement(measValue, m);
		} else {
			LotControlRule rule = partResult.getLotControlRule();
			MeasurementSpec selectedMeasSpec = null;
			PartSpec selectedpartSpec = null;
			if(rule != null){
				for(PartSpec  partSpec : rule.getParts()){
					if(partId.equalsIgnoreCase(partSpec.getId().getPartId())){
						selectedpartSpec= partSpec;
						selectedMeasSpec = partSpec.getMeasurementSpecs().get(seq-1);
						break;
					}
				}
				controller.saveLotControlMeasurement(measValue, selectedMeasSpec, selectedpartSpec );
			}
		}
		reloadView();
	}

	protected void setSkipRejectPartAction(final Button skipRejectBtn) {
		skipRejectBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(ManualLotCtrRepairUtil.isInstructionOrAutoCompleteOperation(operation)){
					controller.rejectPart(operation.getId().getOperationName());
				}else{
					Button btn = ((Button) event.getSource());
					int index = skipRejectBtns.indexOf(btn);
					TextField txtField = partRejectFields.get(index);
					String partName = txtField.getText();
					if(operation !=null)
						operation.setSelectedPart(null);
					controller.rejectPart(partName);
				}
				reloadView();
			}
		});
	}

	protected void saveInstruction(Button saveBtn) {
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if(ManualLotCtrRepairUtil.isInstructionOrAutoCompleteOperation(operation)){
					controller.savePart("", null);
				}
				reloadView();
			}
		});
	}

	protected void savePartBtnAction(Button saveBtn) {
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Button btn = ((Button) event.getSource());
				int index = savePartBtns.indexOf(btn);
				TextField txtField = inputFields.get(index);
				savePart(txtField);
			}
		});
	}
	
	protected void savePart(TextField txtField) {
		String partName = txtField.getText();
		if(partName.isEmpty()){
			this.setErrorMessage("Please enter part serial no. value ");
			return ;
		}
		if(operation!= null){
			for(MCOperationPartRevision mcOperationPartRevision : operation.getParts()){
				if(mcOperationPartRevision.getId().getPartId().equalsIgnoreCase(txtField.getId())){
					operation.setSelectedPart(mcOperationPartRevision);
				}
			}
			controller.savePart(partName, null);										
		}else{
			LotControlRule rule = partResult.getLotControlRule();
			PartSpec selectedPartSpec = null;
			if(rule != null){
				for(PartSpec  partSpec : rule.getParts()){
					if(txtField.getId().equalsIgnoreCase(partSpec.getId().getPartId())){
						selectedPartSpec = partSpec;
						break;
					}
				}
				controller.saveLotControlPart(partName, null , selectedPartSpec);
			}
		}
		reloadView();
	}

	public int getMeasurementCount(MCOperationPartRevision part) {
		if (part != null && part.getMeasurements() != null) {
			return part.getMeasurements().size();
		} else {
			return 0;
		}
	}

	public TextField getProductIdTextField() {
		return productIdTextField;
	}

	public TextField getProductTypeTextField() {
		return productTypeTextField;
	}

	public TextField getOperationNameTextField() {
		return operationNameTextField;
	}

	public TextArea getPartDesc() {
		return partDesc;
	}

	public TableView<InstalledPartHistory> getPartInformationTableView() {
		return partInformationTableView;
	}

	public TableView<MeasurementAttempt> getMeasurementInformationTableView() {
		return measurementInformationTableView;
	}

	public ArrayList<TextField> getInputFields() {
		return inputFields;
	}

	public ArrayList<TextField> getMeasRejectFields() {
		return measRejectFields;
	}

	public ArrayList<TextField> getMeasSaveFields() {
		return measSaveFields;
	}

	void reloadView() {
		this.setScene(null);
		this.inputFields.clear();
		this.measRejectFields.clear();
		this.measSaveFields.clear();
		this.skipRejectBtns.clear();
		this.saveMeasBtns.clear();
		this.savePartBtns.clear();
		this.measRejectbtns.clear();
		this.partResult = controller.reloadPartResult();
		this.controller = null;
		initComponents();
		
	}

	private Double showRejectMeas(int mesSeq ,String partId) {
		
		if (partResult.getBuildResult() != null && partResult.getInstalledPart() != null && partResult.getInstalledPart().getPartId().equalsIgnoreCase(partId) && partResult.getBuildResult().getMeasurements().size() > 0) {
			for (Measurement meas : partResult.getBuildResult().getMeasurements()) {
				if (meas.getId().getMeasurementSequenceNumber() == mesSeq) {
					return meas.getMeasurementValue();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * This method sets graphics to button
	 * @param Button
	 * @param imageName
	 */
	private void setBtnGraphics(Button btn ,Image imageName) {
		btn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageName),20));
		btn.setStyle("-fx-border: 1px ; -fx-border-color: #8E8484;");
	}
	
	/**
	 * This method sets border
	 * @param VBox
	 */
	private void setBorder(VBox lotControlPartBox) {
		lotControlPartBox.setStyle("-fx-padding: 10;" + 
		    "-fx-border-style: solid ;" + 
		    "-fx-border-width: 1;" +
		    "-fx-border-insets: 5;" + 
		    "-fx-border-radius: 5;" +
		    "-fx-border-color: #D3D3D3;");
	}
	

	private VBox createInsertResultsPane() {
		VBox outerPane = new VBox();
		outerPane.setSpacing(20);
		outerPane.getChildren().addAll(createPartPanel(), createButtonPanel());
		handleButtonAction();
		return outerPane;
	}
	
	private void addListeners() {
		if(partSerialNumTextField != null) {
			partSerialNumTextField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(StringUtils.isNotBlank(newValue)) {
						clearErrorMessage();
					}
				}
			});
		}
		if(measurementDetailsTablePane != null) {
			measurementDetailsTablePane.getTable().getItems().addListener(new ListChangeListener<Measurement>() {
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends Measurement> c) {
					if(!measurementDetailsTablePane.getTable().getItems().isEmpty()) {
						clearErrorMessage();
					}
				}
			});
		}
	}
	
	private void handleButtonAction(){
		addMeasurementBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addMeasurementToTablePane();
            }
        });
		deleteMeasurementBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	deleteMeasurementFromTablePane();
            }
        });
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	cancelBtnAction(actionEvent);
            }
        });
		insertResultBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	saveResultsAction(actionEvent);
            }
        });
	}
	
	private GridPane createPartPanel() {
		int rowIndex = 0;
		GridPane mainPane = createGridPane();
		
		partNameTextField = createTextField("partName", TextFieldState.EDIT);
		mainPane.addRow(rowIndex++, UiFactory.createLabel("partName", "Part Name ",bold_font_style), partNameTextField);
		
		partSerialNumTextField = createTextField("partName", TextFieldState.EDIT);
		mainPane.addRow(rowIndex++, UiFactory.createLabel("partSerialNo", "Part Serial Number ",bold_font_style), partSerialNumTextField);
		
		mainPane.add(createMeasurementPanel(), 0, rowIndex++, 2, 1);
		measurementDetailsTablePane = createMeasurementTablePane(false);
		mainPane.add(measurementDetailsTablePane, 0, rowIndex++, 2, 1);
		return mainPane;
	}

	private GridPane createGridPane() {
		GridPane mainPane = new GridPane();
		mainPane.setAlignment(Pos.CENTER);
		mainPane.setVgap(10);
		mainPane.getColumnConstraints().add(new ColumnConstraints(150));
		mainPane.getColumnConstraints().add(new ColumnConstraints(410));
		return mainPane;
	}
	
	private GridPane createMeasurementPanel() {
		HBox measValueContainer = new HBox();
		measValueContainer.setAlignment(Pos.BASELINE_LEFT);
		measValueContainer.setSpacing(10);
		measurementValueTextField = UiFactory.createTextField("partName", 17,TextFieldState.EDIT);
		measurementValueTextField.setMinWidth(240);
		measurementValueTextField.setMaxWidth(240);
		measurementValueTextField.setFocusTraversable(false);
		measurementValueTextField.setStyle(font_style);
		measurementValueTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String value = ManualLotCtrRepairUtil.checkNumericInput(newValue);
				measurementValueTextField.setText(value);
			}
		});
		
		addMeasurementBtn = UiFactory.createButton("Add", true);
		deleteMeasurementBtn = UiFactory.createButton("Delete", true);
		measurementValueTextField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				addMeasurementBtn.fireEvent(event);
			}
		});
		addMeasurementBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	addMeasurementToTablePane();
            }
        });
		deleteMeasurementBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	Measurement meas = measurementDetailsTablePane.getSelectedItem();
            	measurementData.remove(meas);
            	measurementDetailsTablePane.getTable().getItems().remove(meas);
            }
        });
		measValueContainer.getChildren().addAll(measurementValueTextField, addMeasurementBtn, deleteMeasurementBtn);
		GridPane gridPane = createGridPane();
		gridPane.addRow(0, UiFactory.createLabel("measurementValue", "Measurement Value ",bold_font_style), measValueContainer);
		return gridPane;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ObjectTablePane<Measurement> createMeasurementTablePane(boolean isEditable){ 
		ColumnMappingList columnMappingList = ColumnMappingList.with("Measurement Value", "measurementValue");
		
		Double[] columnWidth = new Double[] {
				0.20
			};
		ObjectTablePane<Measurement> panel = new ObjectTablePane<Measurement>(columnMappingList,columnWidth);
		panel.setEditable(true);
		LoggedTableColumn<Measurement, Boolean> buttonCol = new LoggedTableColumn<Measurement, Boolean>();
		createSerialNumber(buttonCol);
		panel.getTable().getColumns().add(0, buttonCol);
		panel.getTable().getColumns().get(0).setText("Sr No");
		panel.getTable().getColumns().get(0).setResizable(true);
		panel.getTable().getColumns().get(0).setMaxWidth(80);
		panel.getTable().getColumns().get(0).setMinWidth(80);
		if(isEditable) {
			TableColumn titleCol = panel.getTable().getColumns().get(1);
			titleCol.setEditable(true);
			titleCol.setCellValueFactory(new PropertyValueFactory("measurementValue"));
			titleCol.setCellFactory(TextFieldTableCell.<Measurement, Double>forTableColumn(new DoubleStringConverter()));
			titleCol.setOnEditCommit(new EventHandler<CellEditEvent>() {
				@Override
				public void handle(CellEditEvent t) {
					((Measurement) t.getTableView().getItems().get(
						t.getTablePosition().getRow())
					).setMeasurementValue(Double.parseDouble(t.getNewValue().toString()));
				}
			});
		}
		panel.setMaxHeight(200);
		panel.setMinHeight(200);
		panel.setConstrainedResize(false);
		return panel;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void createSerialNumber(LoggedTableColumn rowIndex){
		rowIndex.setCellFactory( new Callback<LoggedTableColumn, LoggedTableCell>()	{
			public LoggedTableCell call(LoggedTableColumn p) {
				return new LoggedTableCell() {
					@Override
					public void updateItem( Object item, boolean empty ) {
						super.updateItem( item, empty );
						setText( empty ? null : getIndex() + 1 + "" );
					}
				};
			}
		});
	}
	
	public void clearErrorMessage() {
		messageLabel.setId("status-error-message");
		String style = "-fx-background-color: transparent;";
		messageLabel.setStyle(style);
		messageLabel.setText(StringUtils.EMPTY);
	}
	
	public void addMeasurementTablePane(VBox vBox, InstalledPart part) {
		List<Measurement> measurementList = part.getMeasurements();
		measurementDetailsTablePane = createMeasurementTablePane(true);
		measurementDetailsTablePane.setEditable(true);
		measurementDetailsTablePane.setData(measurementList);
		
		GridPane grid = createGridPane();
		partSerialNumTextField = createTextField("partName", TextFieldState.EDIT);
		partSerialNumTextField.setText(part.getPartSerialNumber());
		grid.addRow(0, UiFactory.createLabel("partSerialNo", "Part Serial Number ",bold_font_style), partSerialNumTextField);
		
		HBox buttonContainer = new HBox();
		
		final Button cancelBtn = UiFactory.createButton("Cancel", true);
		cancelBtn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageReject),20));
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Stage stage = (Stage) cancelBtn.getScene().getWindow();
				stage.close();
			}
		});
		
		final Button updateBtn = UiFactory.createButton("Update", true);
		updateBtn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageComplete),20));
		updateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				saveResultsAction(event);
			}
		});
		
		buttonContainer.setSpacing(10);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.getChildren().addAll(updateBtn, cancelBtn);
		
		vBox.getChildren().addAll(grid, createMeasurementPanel(), measurementDetailsTablePane, buttonContainer);
	}
	
	private HBox createButtonPanel() {
		HBox viewBtnHBox = new HBox();
		insertResultBtn = UiFactory.createButton("Insert", true);
		insertResultBtn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageComplete),20));
		cancelBtn = UiFactory.createButton("Cancel", true);
		cancelBtn.setGraphic(StyleUtil.normalizeImage(new ImageView(imageReject),20));
		viewBtnHBox.getChildren().addAll(insertResultBtn, cancelBtn);
		viewBtnHBox.setPadding(new Insets(10,0,10,0));
		viewBtnHBox.setSpacing(5);
		HBox.setHgrow(viewBtnHBox, Priority.ALWAYS);
		viewBtnHBox.setAlignment(Pos.BASELINE_CENTER);
		return viewBtnHBox;
	}
	
	private void saveResultsAction(ActionEvent event){
		clearErrorMessage();
		String partSrNo = StringUtils.trimToEmpty(partSerialNumTextField.getText());
		String partName = ManualLotControlRepairActions.isInsertResultsAction(action) 
								? StringUtils.trimToEmpty(partNameTextField.getText()) 
								: StringUtils.trimToEmpty(operationNameTextField.getText());
		List<Measurement> measurementList = new ArrayList<Measurement>(measurementDetailsTablePane.getTable().getItems());
		if(StringUtils.isBlank(partName)) {
			setErrorMessage("Please Enter Part Name");
			return;
		}
		if(StringUtils.isBlank(partSrNo) && measurementList.isEmpty()) {
			setErrorMessage("Please Enter Part Serial Number / Measurements");
			return;
		}
		if(ManualLotControlRepairActions.isInsertResultsAction(action) && controller.isPartNameExist(partName)) {
			setErrorMessage("Part Already Exists");
			return;
		}
		if(action == ManualLotControlRepairActions.EDIT_RESULTS) {
			controller.deleteMeasurements();
		}
		controller.savePart(partSrNo, measurementList);
		Stage stage = (Stage) productIdTextField.getScene().getWindow();
		stage.close();
	}
	
	private void cancelBtnAction(ActionEvent event){
		Stage stage = (Stage) cancelBtn.getScene().getWindow();
		stage.close();
	}
	
	private void addMeasurementToTablePane() {
		clearErrorMessage();
		if(action == ManualLotControlRepairActions.INSERT_RESULTS) {
			measurementSequenceNumber = measurementSequenceNumber + 1;
		} else if(action == ManualLotControlRepairActions.EDIT_RESULTS) {
			List<Measurement> measurementList = measurementDetailsTablePane.getTable().getItems();
			int maxSeqNo = 0;
			for(Measurement meas : measurementList) {
				int seqNo = meas.getId().getMeasurementSequenceNumber();
				if(seqNo > maxSeqNo) {
					maxSeqNo = seqNo;
				}
			}
			measurementSequenceNumber = maxSeqNo + 1;
		}
		Measurement measurement = controller.createMeasurement(measurementValueTextField.getText(), measurementSequenceNumber, null, 0);
		measurementData.add(measurement);
		measurementDetailsTablePane.getTable().getItems().add(measurement);
		measurementValueTextField.clear();
	}
	
	private void deleteMeasurementFromTablePane() {
		clearErrorMessage();
		Measurement selectedMeas = measurementDetailsTablePane.getSelectedItem();
		measurementData.remove(selectedMeas);
		measurementDetailsTablePane.getTable().getItems().remove(selectedMeas);
		measurementDetailsTablePane.getTable().getSelectionModel().selectFirst();
	}
	
	public LoggedTextField getPartNameTextField() {
		return partNameTextField;
	}
	
	public LoggedTextField getPartSerialNumTextField() {
		return partSerialNumTextField;
	}

	public ManualLotControlRepairActions getAction() {
		return action;
	}
}
