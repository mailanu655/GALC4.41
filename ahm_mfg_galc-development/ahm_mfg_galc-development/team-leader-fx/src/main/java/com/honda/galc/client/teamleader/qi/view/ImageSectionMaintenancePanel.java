package com.honda.galc.client.teamleader.qi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.teamleader.qi.controller.ImageSectionMaintenanceController;
import com.honda.galc.client.teamleader.qi.enumtype.QiRegionalScreenName;
import com.honda.galc.client.teamleader.qi.model.ImageMaintenanceModel;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.util.ObjectComparator;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageSectionMaintenancePanel</code> is the Panel class for Image Section Maintenance.
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
 * <TD>25/07/2016</TD>
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
public class ImageSectionMaintenancePanel extends QiAbstractTabbedView<ImageMaintenanceModel, ImageSectionMaintenanceController>{
	
	private LoggedButton drawBtn;
	private LoggedButton saveBtn;
	private LoggedButton deleteBtn;
	private LoggedButton moveBtn;
	private LoggedButton resetBtn;
	
	private LoggedButton downArrowBtn;
	private LoggedButton upArrowBtn;
	private LoggedButton copyFromBtn;
	
	private LoggedButton polygonReshapingBtn;
	private LoggedButton copyBtn;
	private LoggedButton pasteBtn;
	private LoggedButton undoBtn;
	private LoggedButton clearBtn;
	private LoggedButton zoomInBtn;
	private LoggedButton zoomOutBtn;
	private LoggedButton flipHorizontalBtn;
	private LoggedButton flipVerticalBtn;
	private LoggedButton printBtn;
	
	private CheckBox showGrid;
	private CheckBox showAllImageSections;
	
	private List<Line> gridLines;
	private List<Polygon> imageSections;
	
	protected UpperCaseFieldBean fullPartNameFilterTextField;
	protected UpperCaseFieldBean imageNameFilterTextField;
	
	protected ObjectTablePane<QiImageSectionDto> imageNameTablePane;
	protected ObjectTablePane<QiImageSectionDto> partNameTablePane;
	protected ObjectTablePane<QiImageSectionDto> assignedPartNameTablePane;
	
	ScrollPane scrollPane;
	AnchorPane anchorPane;
	Canvas canvas;
	
	List<Short> list;
	
	public double screenWidth;
	public double screenHeight;
	private boolean onTabSelection = false;
	private double imageSize;
	
	public ImageSectionMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW,mainWindow);
	}
	
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		
		screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
		screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
		imageSize = 500;
		
		VBox subContainer = new VBox();
		HBox hBox = new HBox();
		list = new ArrayList<Short>();
		list.add((short)1);
		list.add((short)2);
		
		setImageAndCanvas();
		subContainer.getChildren().addAll(createTable(), createButtonContainer(), createDrawingAndAssignedPartTable());
		
		hBox.getChildren().addAll(scrollPane, subContainer);
		hBox.setPadding(new Insets(05));
		hBox.setAlignment(Pos.CENTER_LEFT);
		this.setCenter(hBox);
	}

	private void setImageAndCanvas() {
		scrollPane = new ScrollPane();
		anchorPane = new AnchorPane();
		canvas = new Canvas(imageSize, imageSize);
		gridLines = new ArrayList<Line>();
		imageSections = new ArrayList<Polygon>();
		scrollPane.setContent(anchorPane);
		anchorPane.getChildren().add(canvas);
        scrollPane.setMaxSize(imageSize + 15, imageSize + 15);
        scrollPane.setMinSize(imageSize + 15, imageSize + 15);
        addImageBorder();
	}
	
	private void addImageBorder() {
		Line rightBorderLine = new Line(imageSize + 1, 0, imageSize + 1, imageSize + 1);
		Line bottomBorderLine = new Line(0, imageSize + 1, imageSize + 1, imageSize + 1);
		rightBorderLine.setStrokeWidth(0.3);
		bottomBorderLine.setStrokeWidth(0.3);
		anchorPane.getChildren().add(rightBorderLine);
		anchorPane.getChildren().add(bottomBorderLine);
	}

	private void createDrawingTools() {
		polygonReshapingBtn = createBtn("wykobi_polygon_definition.png", "Polygon Reshaping", "Polygon_Reshaping", getController(), 30, 25, "drawing-tools");
		copyBtn = createBtn("copy.png", "Copy", "Copy", getController(), 20, 15, "drawing-tools");
		pasteBtn = createBtn("paste.jpg", "Paste", "Paste", getController(), 30, 25, "drawing-tools");
		undoBtn = createBtn("undo.jpg", "Undo", "Undo", getController(), 30, 25, "drawing-tools");
		clearBtn = createBtn("clear.jpg", "Clear", "Clear", getController(), 30, 25, "drawing-tools");
		zoomInBtn = createBtn("zoomin.png", "Zoom In", "Zoom_In", getController(), 30, 25, "drawing-tools");
		zoomOutBtn = createBtn("zoomout.png", "Zoom Out", "Zoom_Out", getController(), 30, 25, "drawing-tools");
		flipHorizontalBtn = createBtn("flipLR.png", "Flip Horizontal", "Flip_Horizontal", getController(), 30, 25, "drawing-tools");
		flipVerticalBtn = createBtn("flipUD.png", "Flip Vertical", "Flip_Vertical", getController(), 30, 25, "drawing-tools");
		printBtn = createBtn("print.jpg", "Print", "Print", getController(), 30, 25, "drawing-tools");
		moveBtn  = createBtn("move.png", "Move", "Move", getController(), 30, 25, "drawing-tools");
		if (!isFullAccess()) {
			polygonReshapingBtn.setDisable(true);
			copyBtn.setDisable(true);
			pasteBtn.setDisable(true);
			undoBtn.setDisable(true);
			clearBtn.setDisable(true);
			flipHorizontalBtn.setDisable(true);
			flipVerticalBtn.setDisable(true);
		}
		
		showGrid = createCheckBox("Show Grid", getController(), "checkBox-ImageSection");
		showAllImageSections = createCheckBox("Show All Image Sections", getController(), "checkBox-ImageSection");
		showGrid.setStyle("-fx-font-size: 8pt; -fx-font-weight: bold;");
		showAllImageSections.setStyle("-fx-font-size: 8pt; -fx-font-weight: bold;");
	}
	
	private void createImageSerialNumberColumn() {
		LoggedTableColumn<QiImageSectionDto, Integer> imageColumn=new LoggedTableColumn<QiImageSectionDto, Integer>();
		createSerialNumber(imageColumn);
		imageNameTablePane.getTable().getColumns().add(0, imageColumn);
		imageNameTablePane.getTable().getColumns().get(0).setText("#");
		imageNameTablePane.getTable().getColumns().get(0).setResizable(true);
		imageNameTablePane.getTable().getColumns().get(0).setPrefWidth(30);
	}
	
	private void createPartSerialNumberColumn() {
		LoggedTableColumn<QiImageSectionDto, Integer> partColumn=new LoggedTableColumn<QiImageSectionDto, Integer>();
		createSerialNumber(partColumn);
		partNameTablePane.getTable().getColumns().add(0, partColumn);
		partNameTablePane.getTable().getColumns().get(0).setText("#");
		partNameTablePane.getTable().getColumns().get(0).setResizable(true);
		partNameTablePane.getTable().getColumns().get(0).setPrefWidth(30);
	}
	
	private LoggedLabel getFilterLable(String id, String text){
		LoggedLabel label = UiFactory.createLabel(id, text);
		label.getStyleClass().add("display-label");
		return label;
	}
	
	private VBox createDrawing(){
		VBox drawing = new VBox();
		HBox checkBox = new HBox();
		VBox drawingTools = new VBox();
		HBox drawingTools1 = new HBox();
		HBox drawingTools2 = new HBox();
		HBox drawingTools3 = new HBox();
		
		createDrawingTools();
		
		checkBox.getChildren().addAll(showGrid, showAllImageSections);
		checkBox.setSpacing(10);
		
		drawingTools1.getChildren().addAll(copyBtn, pasteBtn, undoBtn, clearBtn);
		drawingTools1.setSpacing(10);
		
		drawingTools2.getChildren().addAll(polygonReshapingBtn, flipHorizontalBtn, flipVerticalBtn, printBtn);
		drawingTools2.setSpacing(10);
		
		drawingTools3.getChildren().addAll(zoomInBtn, zoomOutBtn, moveBtn);
		drawingTools3.setSpacing(10);
		drawingTools3.setAlignment(Pos.CENTER_LEFT);
		
		drawingTools.getChildren().addAll(drawingTools1, drawingTools2, drawingTools3);
		drawingTools.setSpacing(10);
		drawingTools.setPadding(new Insets(5));
		drawingTools.setAlignment(Pos.CENTER);
		
		drawing.getChildren().addAll(checkBox, drawingTools);
		drawing.setPadding(new Insets(5));
		drawing.setAlignment(Pos.CENTER);
		return drawing;
	}
	
	private HBox createDrawingAndAssignedPartTable(){
		HBox tableContainer = new HBox();
		
		assignedPartNameTablePane = createAssignedPartNameTablePane();
		
		tableContainer.getChildren().addAll(createTitiledPane("Drawing Tools", createDrawing()), createTitiledPane("Assigned Part Name", assignedPartNameTablePane));
		tableContainer.setAlignment(Pos.CENTER);
		tableContainer.setSpacing(5);
		tableContainer.setPadding(new Insets(5));
		return tableContainer;
	}
	
	private VBox createImageContainer(){
		VBox imageContainer = new VBox();
		HBox imageFilterContainer = new HBox();
		
		LoggedLabel imageNameFilterLabel = getFilterLable("imageNameFilterLabel", "Image Name Filter");
		imageNameFilterLabel.setStyle(getLabelStyle());
		imageNameFilterTextField = createFilterTextField("imageNameFilterTextField", 10 , getController());
		HBox radioButtonContainer = createFilterRadioButtons(getController(),20);
		radioButtonContainer.setAlignment(Pos.CENTER);
		imageFilterContainer.getChildren().addAll(imageNameFilterLabel, imageNameFilterTextField);
		
		imageNameTablePane = createImageNameTablePane();
		createImageSerialNumberColumn();
		
		imageContainer.getChildren().addAll(imageFilterContainer, radioButtonContainer, imageNameTablePane);
		imageContainer.setSpacing(screenWidth*0.003);
		imageContainer.setPadding(new Insets(5));
		return imageContainer;
	}
	
	private VBox createPartContainer(){
		HBox partFilterContainer = new HBox();
		VBox partContainer = new VBox();
		
		LoggedLabel fullPartNameFilterLabel = getFilterLable("fullPartNameFilterLabel", "Part Name Filter");
		fullPartNameFilterLabel.setStyle(getLabelStyle());
		fullPartNameFilterTextField = createFilterTextField("fullPartNameFilterTextField", 10, getController());
		partFilterContainer.getChildren().addAll(fullPartNameFilterLabel, fullPartNameFilterTextField);
		
		partNameTablePane = createPartNameTablePane();
		createPartSerialNumberColumn();
		
		partContainer.getChildren().addAll(partFilterContainer, partNameTablePane);
		partContainer.setSpacing(screenWidth*0.003);
		partContainer.setPadding(new Insets(5));
		return partContainer;
	}
	
	private HBox createTable(){
		HBox tableContainer = new HBox();
		
		tableContainer.getChildren().addAll(createTitiledPane("Loaded Images", createImageContainer()), createTitiledPane("QICS Full Part Name", createPartContainer()));
		tableContainer.setAlignment(Pos.CENTER);
		return tableContainer;
	}
	
	private HBox createButtonContainer(){
		HBox tableContainer = new HBox();
		tableContainer.getChildren().addAll(createActionButtonContainer(), createArrowButtonContainer());
		tableContainer.setAlignment(Pos.CENTER);
		tableContainer.setSpacing(05);
		tableContainer.setPadding(new Insets(5));
		return tableContainer;
	}
	
	private HBox createActionButtonContainer() {
		HBox buttonContainer = new HBox();
		
		drawBtn = createImageBtn(QiConstant.DRAW, getController(),getLabelStyle());
		saveBtn = createImageBtn(QiConstant.SAVE, getController(),getLabelStyle());
		deleteBtn = createImageBtn(QiConstant.DELETE, getController(),getLabelStyle());
		resetBtn = createImageBtn("Reset", getController(),getLabelStyle());
		
		if (!isFullAccess()) {
			drawBtn.setDisable(true);
			saveBtn.setDisable(true);
			deleteBtn.setDisable(true);
		}
		
		buttonContainer.getChildren().addAll(drawBtn, saveBtn, deleteBtn, resetBtn);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setSpacing(5);
		buttonContainer.setPrefWidth((screenWidth - imageSize)/1.6);
		return buttonContainer;
	}
	
	private HBox createArrowButtonContainer() {
		HBox buttonContainer = new HBox();
		downArrowBtn = createBtn("down.jpg", "Down", "Down", getController(),(int)(0.015 * screenWidth),(int)(0.010 * screenWidth),"imageSection-btn");
		upArrowBtn = createBtn("up.jpg", "Up", "Up", getController(), (int)(0.015 * screenWidth), (int)(0.010 * screenWidth),"imageSection-btn");
		copyFromBtn =  createImageBtn("Copy From", getController(),getLabelStyle());
		
		buttonContainer.getChildren().addAll(copyFromBtn, downArrowBtn, upArrowBtn);
		buttonContainer.setSpacing(0.003 * screenWidth);
		buttonContainer.setAlignment(Pos.CENTER);
		buttonContainer.setPrefWidth((screenWidth - imageSize)/2);
		return buttonContainer;
	}
	
	/**
	 * This method is used to create a Table Pane.
	 * @return
	 */
	private ObjectTablePane<QiImageSectionDto> createPartNameTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("QICS Full Part Name", "fullPartDesc");
		
		Double[] columnWidth = new Double[] {
				0.26
			}; 
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("partNameTablePane");
		return panel;
	}
	
	private ObjectTablePane<QiImageSectionDto> createAssignedPartNameTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Sec Id", "imageSectionId")
								.put("QICS Full Part Name","fullPartDesc");
		
		Double[] columnWidth = new Double[] {
				0.1,0.20
			}; 
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("assignedPartNameTablePane");
		return panel;
	}
	
	private ObjectTablePane<QiImageSectionDto> createImageNameTablePane(){
		ColumnMappingList columnMappingList = 
				ColumnMappingList.with("Image Name", "imageName");
		
		Double[] columnWidth = new Double[] {
				0.26
			}; 
		ObjectTablePane<QiImageSectionDto> panel = new ObjectTablePane<QiImageSectionDto>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("imageNameTablePane");
		return panel;
	}
	
	private TitledPane createTitiledPane(String title,Node content) {
		TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setPrefSize((screenWidth - imageSize + 15)/2, screenHeight-120);
		return titledPane;
	}
	
	public void onTabSelected() {
		
		onTabSelection=true;
		getPartNameTablePane().setDisable(false);
		getFullPartNameFilterTextField().setDisable(false);
		QiImageSectionDto imageSectionDto = getImageNameTablePane().getSelectedItem();
		QiImageSectionDto partName = getPartNameTablePane().getSelectedItem();
		getController().activate();
		getController().clearDisplayMessage();
		List<QiImageSectionDto> imageList = getModel().findImageByFilter(StringUtils.trim(imageNameFilterTextField.getText()), getSelectedRadioButtonValue());
		if(imageList.size() > 0) {
			imageNameTablePane.setData(imageList);
			if(imageSectionDto != null && imageList.contains(imageSectionDto)) {
				imageNameTablePane.getTable().getSelectionModel().select(imageSectionDto);
			}
		}
		
		List<QiImageSectionDto> partLocationList = getModel().getPartLocCombByFilter(StringUtils.trim(fullPartNameFilterTextField.getText()), list);
		Collections.sort(partLocationList, getPartNameTableComparator());
		if(partLocationList.size() > 0) {
			partNameTablePane.setData(partLocationList);
			if(partName != null && partLocationList.contains(partName)) {
				partNameTablePane.getTable().getSelectionModel().select(partName);
			}
		}
		onTabSelection=false;
	}

	@Override
	public ViewId getViewId() {
		return null;
	}

	@Override
	public void reload() {
		List<QiImageSectionDto> partLocationList = getModel().getPartLocCombByFilter("", list);
		Collections.sort(partLocationList, getPartNameTableComparator());
		partNameTablePane.setData(partLocationList);
		imageNameTablePane.setData(getModel().findImageByFilter("", getSelectedRadioButtonValue()));
	}

	public void reloadPart(String filter) {
		List<QiImageSectionDto> partLocationList = getModel().getPartLocCombByFilter(filter.trim(), list);
		Collections.sort(partLocationList, getPartNameTableComparator());
		partNameTablePane.setData(partLocationList);
	}
	
	private ObjectComparator<QiImageSectionDto> getPartNameTableComparator() {
		return new ObjectComparator<QiImageSectionDto>("inspectionPartName", "inspectionPartLocationName", "inspectionPartLocation2Name",
					"inspectionPart2Name", "inspectionPart2LocationName", "inspectionPart2Location2Name", "inspectionPart3Name");
	}
	
	public void reloadImage(String filter) {
		imageNameTablePane.setData(getModel().findImageByFilter(filter.trim(), getSelectedRadioButtonValue()));
	}
	
	private List<Short> getSelectedRadioButtonValue() {
		List<Short> statusList = new ArrayList<Short>();
		if(getAllRadioBtn().isSelected()) {
			statusList.add((short)1);
			statusList.add((short)0);
		} else {
			if(getActiveRadioBtn().isSelected())
				statusList.add((short)1);
			else
				statusList.add((short)0);
			statusList.add((short)2);
		}
		return statusList;
	}
	
	public List<Node> getPrintTablePages(PageLayout layout, ImageView imageView, boolean printPartNameTable, ObservableList<QiImageSectionDto> imageSectionList) {
		LinkedList<Node> printPageList = new LinkedList<Node>();
		VBox imageNode = new VBox();
		imageNode.getChildren().addAll(imageView);
		printPageList.add(imageNode);

		if (printPartNameTable) {
			double totalHeight = layout.getTopMargin() + layout.getBottomMargin();
			VBox sectionTable = new VBox();
			sectionTable.setPadding(new Insets(20, 10, 20, 20));
			HBox tableRow;
			Label sequenceNoLabel;
			Text fullPartName;
			for (QiImageSectionDto record : imageSectionList) {
				fullPartName = new Text(10, 50, "");
				fullPartName.setTextAlignment(TextAlignment.JUSTIFY);
				fullPartName.setWrappingWidth(layout.getPrintableWidth() - (layout.getLeftMargin() + layout.getRightMargin()));
				fullPartName.setText(record.getFullPartDesc());
				fullPartName.setStyle("-fx-border-color: black;");
				fullPartName.setTextAlignment(TextAlignment.JUSTIFY);
				
				if (totalHeight + fullPartName.getLayoutBounds().getHeight() >= layout.getPrintableHeight()) {
					printPageList.add(sectionTable);
					sectionTable = new VBox();
					sectionTable.setPadding(new Insets(20, 10, 20, 20));
					totalHeight = layout.getTopMargin() + layout.getBottomMargin();
				}
				
				sequenceNoLabel = new Label(" " + record.getSequenceNo());
				sequenceNoLabel.setPrefWidth(30.0);
				sequenceNoLabel.setMinHeight(fullPartName.getLayoutBounds().getHeight());
				sequenceNoLabel.setStyle("-fx-border-color: black; -fx-border-width: 0.5");
				
				tableRow = new HBox();
				tableRow.setSpacing(10);
				tableRow.setPadding(new Insets(0, 10, 0, 0));
				tableRow.setStyle("-fx-border-color: black;");
				tableRow.getChildren().addAll(sequenceNoLabel, fullPartName);
				
				sectionTable.getChildren().add(tableRow);
				totalHeight += (fullPartName.getLayoutBounds().getHeight() + 2);
			}
			printPageList.add(sectionTable);
		}
		return printPageList;
	}
	
	@Override
	public void start() {
	}
	
	public void disableOrEnableRadioButton(boolean isDisable){
		getAllRadioBtn().setDisable(isDisable);
		getActiveRadioBtn().setDisable(isDisable);
		getInactiveRadioBtn().setDisable(isDisable);
	}

	public String getScreenName() {
		return QiRegionalScreenName.IMAGE_SECTION_MAINTENANCE.getScreenName();
	}
	
	public ObjectTablePane<QiImageSectionDto> getPartNameTablePane() {
		return partNameTablePane;
	}
	
	public UpperCaseFieldBean getFullPartNameFilterTextField() {
		return fullPartNameFilterTextField;
	}
	
	public ObjectTablePane<QiImageSectionDto> getImageNameTablePane() {
		return imageNameTablePane;
	}
	
	public UpperCaseFieldBean getImageNameFilterTextField() {
		return imageNameFilterTextField;
	}
	
	public String getPartFilterTextData(){
		return StringUtils.trimToEmpty(fullPartNameFilterTextField.getText());
	}

	public void setFullPartNameFilterTextField(UpperCaseFieldBean fullPartNameFilterTextField) {
		this.fullPartNameFilterTextField = fullPartNameFilterTextField;
	}
	
	public String getImageFilterTextData(){
		return StringUtils.trimToEmpty(imageNameFilterTextField.getText());
	}

	public void setImageNameFilterTextField(UpperCaseFieldBean imageNameFilterTextField) {
		this.imageNameFilterTextField = imageNameFilterTextField;
	}

	public LoggedButton getDrawBtn() {
		return drawBtn;
	}

	public LoggedButton getSaveBtn() {
		return saveBtn;
	}

	public LoggedButton getDeleteBtn() {
		return deleteBtn;
	}

	public LoggedButton getPolygonReshaping() {
		return polygonReshapingBtn;
	}

	public LoggedButton getCopy() {
		return copyBtn;
	}

	public LoggedButton getPaste() {
		return pasteBtn;
	}

	public LoggedButton getUndo() {
		return undoBtn;
	}

	public LoggedButton getClear() {
		return clearBtn;
	}

	public LoggedButton getZoomIn() {
		return zoomInBtn;
	}

	public LoggedButton getZoomOut() {
		return zoomOutBtn;
	}

	public LoggedButton getFlipHorizontal() {
		return flipHorizontalBtn;
	}

	public LoggedButton getFlipVertical() {
		return flipVerticalBtn;
	}

	public LoggedButton getPrint() {
		return printBtn;
	}

	public LoggedButton getMoveBtn() {
		return moveBtn;
	}

	public CheckBox getShowGrid() {
		return showGrid;
	}

	public CheckBox getShowAllImageSections() {
		return showAllImageSections;
	}

	public ScrollPane getScrollPane() {
		return scrollPane;
	}

	public AnchorPane getAnchorPane() {
		return anchorPane;
	}

	public List<Line> getGridLines() {
		return gridLines;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public double getScreenWidth() {
		return screenWidth;
	}

	public double getScreenHeight() {
		return screenHeight;
	}

	public List<Polygon> getImageSections() {
		return imageSections;
	}

	public LoggedButton getDownArrowBtn() {
		return downArrowBtn;
	}

	public LoggedButton getUpArrowBtn() {
		return upArrowBtn;
	}
	
	public LoggedButton getCopyFromBtn() {
		return copyFromBtn;
	}

	public LoggedButton getResetBtn() {
		return resetBtn;
	}

	public double getImageSize() {
        return imageSize;
    }

	public ObjectTablePane<QiImageSectionDto> getAssignedPartNameTablePane() {
		return assignedPartNameTablePane;
	}

	public void setDownArrowBtn(LoggedButton downArrowBtn) {
		this.downArrowBtn = downArrowBtn;
	}

	public void setUpArrowBtn(LoggedButton upArrowBtn) {
		this.upArrowBtn = upArrowBtn;
	}

	public void setAssignedPartNameTablePane(
			ObjectTablePane<QiImageSectionDto> assignedPartNameTablePane) {
		this.assignedPartNameTablePane = assignedPartNameTablePane;
	}

	public void setImageSections(List<Polygon> imageSections) {
		this.imageSections = imageSections;
	}

	public void setScreenWidth(double screenWidth) {
		this.screenWidth = screenWidth;
	}

	public void setScreenHeight(double screenHeight) {
		this.screenHeight = screenHeight;
	}

	public void setGridLines(List<Line> gridLines) {
		this.gridLines = gridLines;
	}

	public void setScrollPane(ScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public void setAnchorPane(AnchorPane anchorPane) {
		this.anchorPane = anchorPane;
	}

	public void setDrawBtn(LoggedButton drawBtn) {
		this.drawBtn = drawBtn;
	}

	public void setSaveBtn(LoggedButton saveBtn) {
		this.saveBtn = saveBtn;
	}

	public void setDeleteBtn(LoggedButton deleteBtn) {
		this.deleteBtn = deleteBtn;
	}

	public void setPolygonReshaping(LoggedButton polygonReshaping) {
		this.polygonReshapingBtn = polygonReshaping;
	}

	public void setCopy(LoggedButton copy) {
		this.copyBtn = copy;
	}

	public void setPaste(LoggedButton paste) {
		this.pasteBtn = paste;
	}

	public void setUndo(LoggedButton undo) {
		this.undoBtn = undo;
	}

	public void setClear(LoggedButton clear) {
		this.clearBtn = clear;
	}

	public void setZoomIn(LoggedButton zoomIn) {
		this.zoomInBtn = zoomIn;
	}

	public void setZoomOut(LoggedButton zoomOut) {
		this.zoomOutBtn = zoomOut;
	}

	public void setFlipHorizontal(LoggedButton flipHorizontal) {
		this.flipHorizontalBtn = flipHorizontal;
	}

	public void setFlipVertical(LoggedButton flipVertical) {
		this.flipVerticalBtn = flipVertical;
	}

	public void setPrint(LoggedButton print) {
		this.printBtn = print;
	}

	public void setShowGrid(CheckBox showGrid) {
		this.showGrid = showGrid;
	}

	public void setShowAllImageSections(CheckBox showAllImageSections) {
		this.showAllImageSections = showAllImageSections;
	}

	public boolean isOnTabSelection() {
		return onTabSelection;
	}
	
	public  String  getLabelStyle() {
		return String.format("-fx-font-size: %dpx;", (int)(0.011 * screenWidth));
	}
}
