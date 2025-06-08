package com.honda.galc.client.teamleader.qi.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.honda.galc.client.teamleader.qi.model.ImageMaintenanceModel;
import com.honda.galc.client.teamleader.qi.view.ImageSectionCopyFromDialog;
import com.honda.galc.client.teamleader.qi.view.ImageSectionMaintenancePanel;
import com.honda.galc.client.teamleader.qi.view.ImageSectionPrintDialog;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ImageSectionSelection;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.entity.qi.QiImage;
import com.honda.galc.entity.qi.QiImageSection;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiImageSectionPointId;
import com.honda.galc.util.AuditLoggerUtil;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ImageSectionMaintenanceController</code> is the controller class for Image Section Maintenance Panel.
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
public class ImageSectionMaintenanceController extends AbstractQiController<ImageMaintenanceModel, ImageSectionMaintenancePanel> implements EventHandler<ActionEvent> {
	
	private boolean drawStatus = false, dragStatus = false;
	private List<Double> drawnXPointsList;
	private List<Double> drawnYPointsList;
	private int zoomCounter = 0;
	private DoubleProperty zoomWidth;
	private DoubleProperty zoomHeight;
	private GraphicsContext graphicsContext;
	SelectionModel selectionModel;
	private Polygon copyOrFlipPolygon;
	private boolean updatePolygon = false, eventHandler = false, polygonReshape = false, polygonDraw = false;
	private double zoomFactor = 1.5;
	private Map<Integer,Double> xCoordinate;
	private Map<Integer,Double> yCoordinate;
	ObservableList<Anchor> observableAnchorsList;
	private Map<String, Node> polygonMap;
	private ObservableList<QiImageSectionDto> printImageSectionList;
	boolean assignedPartTableUpdated = false, newPolygonCreated = false, polygonCopy = false, displayErrorMessage = false;
	private List<QiImageSectionDto> assignedOriginalSectionList;
	boolean movePolygon = false, copyFromImage = false;
	
	public ImageSectionMaintenanceController(ImageMaintenanceModel model,ImageSectionMaintenancePanel view) {
		super(model, view);
	}
	
	public void setCanvas(Canvas canvas, Image img){
		graphicsContext.drawImage(img, 0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	@Override
	public void initEventHandlers() {
		/**
		 *  This method is used to add listener on main panel table.
		 */
		initSelection();
		addPartNameTableListener();
		addAssignedPartDefectTableListner();
		addImageNameTableListener();
		initializeListAndMap();
		addZoomListener();
		addDrawListener();
		disableDrawingTools();
		initGraphicContext();
		removeAllImageSections();
		clearAnchorAndGraphics();
	}

	private void initSelection() {
		selectionModel = new SelectionModel();
	}
	
	private void addZoomListener(){
		if (zoomCounter == 0) {
			getView().getZoomOut().setDisable(true);
		}
		zoomWidth = new SimpleDoubleProperty(getView().getImageSize());
		zoomHeight = new SimpleDoubleProperty(getView().getImageSize());
		InvalidationListener listener = new InvalidationListener() {
			public void invalidated(Observable arg0) {
				getView().getAnchorPane().setPrefWidth(zoomWidth.get() * 1);
				getView().getAnchorPane().setPrefHeight(zoomHeight.get() * 1);
				getView().getScrollPane().requestLayout();
			}
		};
		zoomWidth.addListener(listener);
		zoomHeight.addListener(listener);
		getView().getScrollPane().setPannable(true);
	}
	
	private void initGraphicContext(){
		graphicsContext = getView().getCanvas().getGraphicsContext2D();
		graphicsContext.setStroke(Color.web(getModel().getLineColor()));
		graphicsContext.setLineWidth(getModel().getLineWidth());
	}
	
	private void addDrawListener(){
		getView().getCanvas().setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				dragStatus = false;
			}
		});
		getView().getCanvas().setOnMouseDragged(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				dragStatus = true;
			}
		});
		getView().getCanvas().setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				if(!dragStatus){
					draw(e);
				}
			}
		});
	}
	
	private void initializeListAndMap(){
		drawnXPointsList = new ArrayList<Double>();
		drawnYPointsList = new ArrayList<Double>();
		xCoordinate = new HashMap<Integer,Double>();
		yCoordinate = new HashMap<Integer,Double>();
		polygonMap = new HashMap<String, Node>();
		printImageSectionList = FXCollections.observableArrayList();
		assignedOriginalSectionList = new ArrayList<QiImageSectionDto>();
	}
	
	private void disableDrawingTools() {
		getView().getDrawBtn().setDisable(true);
		getView().getUndo().setDisable(true);
		getView().getPolygonReshaping().setDisable(true);
		getView().getCopy().setDisable(true);
		getView().getPaste().setDisable(true);
		getView().getFlipHorizontal().setDisable(true);
		getView().getFlipVertical().setDisable(true);
		getView().getPrint().setDisable(true);
		getView().getClear().setDisable(true);
		getView().getZoomIn().setDisable(true);
		getView().getSaveBtn().setDisable(true);
		getView().getDeleteBtn().setDisable(true);
		getView().getUpArrowBtn().setDisable(true);
		getView().getDownArrowBtn().setDisable(true);
		getView().getMoveBtn().setDisable(true);
		getView().getCopyFromBtn().setDisable(true);
		getView().getResetBtn().setDisable(true);
	}
	
	private double distance(double x1,double y1,double x2,double y2){
		return Math.sqrt(Math.pow((x2-x1), 2)+Math.pow((y2-y1), 2));
	}
	
	private void draw(MouseEvent e) {
		if(drawStatus){
			graphicsContext.setStroke(Color.web(getModel().getLineColor()));
			if (drawnXPointsList.isEmpty() && drawnYPointsList.isEmpty()) {
				graphicsContext.strokeLine(e.getX(), e.getY(), e.getX(), e.getY());
				addPointsToLists(e.getX(), e.getY());
				getView().getDrawBtn().setDisable(true);
				getView().getUndo().setDisable(false);
				getView().getClear().setDisable(false);
				getView().getShowAllImageSections().setDisable(true);
				getView().getCopyFromBtn().setDisable(true);
			} else {
				if(distance(drawnXPointsList.get(0),drawnYPointsList.get(0),e.getX(),e.getY())<5){
					graphicsContext.strokeLine(drawnXPointsList.get(drawnXPointsList.size()-1), drawnYPointsList.get(drawnYPointsList.size()-1), drawnXPointsList.get(0), drawnYPointsList.get(0));
					addPointsToLists(drawnXPointsList.get(0), drawnYPointsList.get(0));
					drawStatus = false;
					polygonDraw = true;
					getView().getDrawBtn().setDisable(true);
					if(isFullAccess())
					getView().getSaveBtn().setDisable(false);
				}else{
					graphicsContext.strokeLine(drawnXPointsList.get(drawnXPointsList.size()-1), drawnYPointsList.get(drawnYPointsList.size()-1), e.getX(), e.getY());
					addPointsToLists(e.getX(), e.getY());
				}
			}
		}
	}
	
	private void addPointsToLists(double x, double y) {
		drawnXPointsList.add(x);
		drawnYPointsList.add(y);
	}
	
	private void addPartNameTableListener() {
		getView().getPartNameTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			public void changed(
					ObservableValue<? extends QiImageSectionDto> arg0,
					QiImageSectionDto arg1,
					QiImageSectionDto arg2) {
				
				clearDisplayMessage();
				if (isFullAccess() && !assignedPartTableUpdated && !newPolygonCreated && !polygonReshape && !movePolygon) {
					getView().getDrawBtn().setDisable(false);
				}
				if(selectionModel.selection.size() == 0 && !newPolygonCreated){
					retrieveImageSectionOrEnableDrawing();
					refreshShowAllImageSection();
					removeControlAnchors();
					getView().getSaveBtn().setDisable(true);
					getView().getAssignedPartNameTablePane().getTable().getItems().clear();
				}
				if(!newPolygonCreated)
				disableOrEnableDrawingTools();
				if(movePolygon && newPolygonCreated)
					getView().getDownArrowBtn().setDisable(false);
					
				getView().getPrint().setDisable(false);
			}
		});
	}
	
	private void addAssignedPartDefectTableListner() {

		clearDisplayMessage();
		getView().getAssignedPartNameTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			public void changed(
					ObservableValue<? extends QiImageSectionDto> arg0,
					QiImageSectionDto arg1, QiImageSectionDto arg2) {
				if(getView().isOnTabSelection())
					return;
					clearDisplayMessage();
					if(null != arg2){
						selectionModel.clear();
						if(arg2.getImageSectionId() == 0)
							selectionModel.add(polygonMap.get("new"));
						else
							selectionModel.add(polygonMap.get(String.valueOf(arg2.getImageSectionId())));
					}
					
					if(!updatePolygon && !newPolygonCreated && !assignedPartTableUpdated){
						getView().getPolygonReshaping().setDisable(false);
			            getView().getCopy().setDisable(false);
			            getView().getFlipHorizontal().setDisable(false);
			            getView().getFlipVertical().setDisable(false);
			            getView().getPrint().setDisable(false);
			            getView().getMoveBtn().setDisable(false);
					}
			}
		});
	}
	
	private void addImageNameTableListener() {
		getView().getImageNameTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<QiImageSectionDto>() {
			public void changed(ObservableValue<? extends QiImageSectionDto> arg0,
					QiImageSectionDto arg1, QiImageSectionDto arg2) {
				QiImageSectionDto qiImageSectionDto = getView().getImageNameTablePane().getSelectedItem();
				if(qiImageSectionDto!=null){
					clearDisplayMessage();
					setCanvasAndImage();
					resetZoom();
					retrieveImageSectionOrEnableDrawing();
					refreshShowAllImageSection();
					removeControlAnchors();
					selectionModel.clear();
					disableOrEnableDrawingTools();
					getView().getPrint().setDisable(false);
					getView().getSaveBtn().setDisable(true);
					getView().getDownArrowBtn().setDisable(true);
					getView().getUpArrowBtn().setDisable(true);
					getView().getAssignedPartNameTablePane().getTable().getItems().clear();
					enableOrDisableFilterAndCheckBox(false);
					enableCopyFromButtom();
					newPolygonCreated = false;
					polygonCopy = false;
					polygonDraw = false;
					assignedPartTableUpdated = false;
					getView().getScrollPane().setHvalue(0);
					getView().getScrollPane().setVvalue(0);
				}
			}
		});
	}
	
	private void enableCopyFromButtom() {
		if(getView().getImageNameTablePane().getSelectedItem() != null) {
			String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
			if(imageName != null && getModel().showAllImageSection(imageName).size() == 0 ) {
				getView().getCopyFromBtn().setDisable(false);
			} else 
				getView().getCopyFromBtn().setDisable(true);
		}
	}

	private void enableOrDisableFilterAndCheckBox(boolean isDisable) {
		getView().getShowAllImageSections().setDisable(isDisable);
		getView().getImageNameFilterTextField().setDisable(isDisable);
		getView().disableOrEnableRadioButton(isDisable);
		getView().getImageNameTablePane().setDisable(isDisable);
	}
	
	private void retrieveImageSectionOrEnableDrawing() {
		if(getView().getPartNameTablePane().getSelectedItem()!=null && getView().getImageNameTablePane().getSelectedItem()!=null){
			int partLocationId = getView().getPartNameTablePane().getSelectedItem().getPartLocationId();
			String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
			List<QiImageSectionPoint> polygonPoints = getModel().findPolygonPoints(partLocationId, imageName);
			removeAllImageSections();
			getView().getAnchorPane().getChildren().remove(copyOrFlipPolygon);
			drawStatus = false;
			updatePolygon = false;
			dragStatus = false;
			setCanvasAndImage();
			getView().getUndo().setDisable(true);
			getView().getClear().setDisable(true);
			drawnXPointsList.clear();
			drawnYPointsList.clear();
			if(!polygonPoints.isEmpty()){
				getPolygonList(polygonPoints, false);
				if(getView().getImageSections()!=null)
					getView().getAnchorPane().getChildren().addAll(getView().getImageSections());
				if(isFullAccess())
					getView().getDeleteBtn().setDisable(false);
				if(!eventHandler) {
					new RubberBandSelection(getView().getAnchorPane());
					eventHandler = true;
				}
			}else{
				if (isFullAccess()) {
					getView().getDrawBtn().setDisable(false);
				}
				getView().getDeleteBtn().setDisable(true);
			}
		}
	}

	private void setCanvasAndImage() {
		graphicsContext.clearRect(0, 0, getView().getCanvas().getWidth(), getView().getCanvas().getHeight());
		String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
		QiImage qiImage=getModel().findQiImageByImageName(imageName);
		Image img = new Image(new ByteArrayInputStream(qiImage.getImageData()));
		setCanvas(getView().getCanvas(), img);
	}
	/**
	 * This is an implemented method of EventHandler interface. Called whenever an ActionEvent is triggered.
	 * Selecting button is an ActionEvent. So respective method is called based on which action event is triggered.
	 */
	public void handle(ActionEvent actionEvent) {
		if(actionEvent.getSource() instanceof LoggedButton) {
			if(actionEvent.getSource().equals(getView().getDrawBtn())) drawBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getSaveBtn())) saveBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getDeleteBtn())) deleteBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getPolygonReshaping())) polygonReshapingAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getCopy())) copyAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getPaste())) pasteAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getUndo())) undoAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getClear())) clearAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getZoomIn())) zoomInAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getZoomOut())) zoomOutAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getFlipHorizontal())) flipHorizontalAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getFlipVertical())) flipVerticalAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getPrint())) printAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getUpArrowBtn())) upBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getDownArrowBtn())) downBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getMoveBtn())) moveBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getCopyFromBtn())) copyFromBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getResetBtn())) resetBtnAction(actionEvent);
			else if(actionEvent.getSource().equals(getView().getCopyFromBtn())) copyFromBtnAction(actionEvent);
		}else if(actionEvent.getSource() instanceof CheckBox) {
			CheckBox checkBox = (CheckBox) actionEvent.getSource();
			if("Show Grid".equals(checkBox.getText())) showGridAction(actionEvent);
			else if("Show All Image Sections".equals(checkBox.getText())) showAllImageSectionsAction(actionEvent);
		}else if(actionEvent.getSource() instanceof UpperCaseFieldBean){
			UpperCaseFieldBean upperCaseField = (UpperCaseFieldBean) actionEvent.getSource();
			if("imageNameFilterTextField".equals(upperCaseField.getId()))
			getView().reloadImage(getView().getImageNameFilterTextField().getText());
			getView().reloadPart(getView().getFullPartNameFilterTextField().getText());
		}else if(actionEvent.getSource() instanceof LoggedRadioButton)
			getView().reloadImage(getView().getImageNameFilterTextField().getText());
	}
	
	public void drawBtnAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null && getView().getPartNameTablePane().getSelectedItem()!=null){
			clearDisplayMessage();
			drawStatus = true;
			selectionModel.clear();
			disableOrEnableDrawingTools();
		}else{
			displayErrorMessage("Image/Part Not Selected");
			return;
		}
	}
	
	public void saveBtnAction(ActionEvent event) {

		if (getView().getImageNameTablePane().getSelectedItem() != null) {
			List<QiImageSectionPoint> imageSectionPointList = new ArrayList<QiImageSectionPoint>();
			String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();

			if(copyFromImage) {
				performCopyFromOperation();
			} else if (assignedPartTableUpdated) {
				List<QiImageSectionDto> assignedDataSectionList = getView().getAssignedPartNameTablePane().getTable().getItems();
				if (newPolygonCreated) {
					int imageSectionId = getModel().getMaxSectionId();
					imageSectionId++;
					boolean savePoint = true;
					for (QiImageSectionDto imageSection : assignedDataSectionList) {
						QiImageSection qiImageSection = new QiImageSection();
						qiImageSection.setImageName(imageName);
						qiImageSection.setPartLocationId(imageSection.getPartLocationId());
						qiImageSection.setCreateUser(getUserId());
						try {
							qiImageSection.setImageSectionId(imageSectionId);
							getModel().saveImageSection(qiImageSection);
							if (savePoint) {
								saveOrUpdateImageSectionPoints(imageSectionPointList, imageSectionId);
								getModel().saveImageSectionPoint(imageSectionPointList);
								savePoint = false;
							}

						} catch (Exception e) {
							handleException("An error occurred at saveBtnAction method ", "Failed to save Image Section", e);
						}
					}
					newPolygonCreated = false;
					polygonCopy = false;

				} else {
					performSaveAction(assignedDataSectionList);
					performDeleteAction(assignedDataSectionList);
				}
				refreshToolsAndDrawing();
				enableOrDisableFilterAndCheckBox(false);
				getView().getZoomIn().setDisable(false);
				enableCopyFromButtom();

			} else {
				int imageSectionId;
				if (updatePolygon) {
					if (selectionModel.selection != null && !selectionModel.selection.isEmpty()) {
						clearDisplayMessage();
						ReasonForChangeDialog reasonForChangeDialog = new ReasonForChangeDialog(getApplicationId());
						if (reasonForChangeDialog
								.showReasonForChangeDialog("Are you sure you want to update the Image Section?")) {
							imageSectionId = Integer.parseInt(selectionModel.selection.iterator().next().getId());
							try {
								List<QiImageSectionPoint> oldPolygonPoints = getModel().findPolygonPoints(imageSectionId);
								saveOrUpdateImageSectionPoints(imageSectionPointList, imageSectionId);
								getModel().updateImageSectionPoint(imageSectionPointList);
								performAuditLog(oldPolygonPoints, imageSectionPointList, reasonForChangeDialog.getReasonForChangeText(),
										imageName + " " + imageSectionId);
								updatePolygon = false;
								dragStatus = false;
								enableOrDisableFilterAndCheckBox(false);
							} catch (Exception e) {
								handleException("An error occurred at saveBtnAction method ", "Failed to update Image Section", e);
							}
						} else
							return;
					} else {
						displayErrorMessage("Image Section Not Selected");
						return;
					}

				} else {
					if (getView().getPartNameTablePane().getSelectedItem() != null) {
						int partLocationId = getView().getPartNameTablePane().getSelectedItem().getPartLocationId();
						QiImageSection qiImageSection = new QiImageSection();
						qiImageSection.setImageName(imageName);
						qiImageSection.setPartLocationId(partLocationId);
						qiImageSection.setCreateUser(getUserId());
						try {
							imageSectionId = getModel().getMaxSectionId();
							imageSectionId++;
							qiImageSection.setImageSectionId(imageSectionId);
							getModel().saveImageSection(qiImageSection);
							saveOrUpdateImageSectionPoints(imageSectionPointList, imageSectionId);
							getModel().saveImageSectionPoint(imageSectionPointList);
							newPolygonCreated = false;
							polygonDraw = false;
							enableOrDisableFilterAndCheckBox(false);
							enableCopyFromButtom();
						} catch (Exception e) {
							handleException("An error occurred at saveBtnAction method ", "Failed to save Image Section", e);
						}
					} else {
						displayErrorMessage("Part Not Selected");
						return;
					}
				}
				refreshToolsAndDrawing();
			}
		} else {
			displayErrorMessage("Image Not Selected");
			return;
		}
		movePolygon = false;
	}
	
	private void refreshToolsAndDrawing() {
		getView().getSaveBtn().setDisable(true);
		getView().getUndo().setDisable(true);
		getView().getClear().setDisable(true);
		getView().getPrint().setDisable(false);
		if(isFullAccess()) {
			getView().getDrawBtn().setDisable(false);
			getView().getDeleteBtn().setDisable(false);
		}
		
		drawnXPointsList.clear();
		drawnYPointsList.clear();
		selectionModel.clear();
		retrieveImageSectionOrEnableDrawing();
		refreshShowAllImageSection();
		removeControlAnchors();
		assignedPartTableUpdated = false;
		getView().getAssignedPartNameTablePane().getTable().getItems().clear();
	}
	
	private void performSaveAction(List<QiImageSectionDto> assignedDataSectionList){
		
		List<QiImageSection> qiImageSectionList = new ArrayList<QiImageSection>();
		String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
		
		for(QiImageSectionDto section : assignedDataSectionList){
			if(!assignedOriginalSectionList.contains(section)){
				QiImageSection qiImageSection = new QiImageSection();
				qiImageSection.setImageName(imageName);
				qiImageSection.setImageSectionId(section.getImageSectionId());				
				qiImageSection.setPartLocationId(section.getPartLocationId());
				qiImageSection.setCreateUser(getUserId());
				qiImageSectionList.add(qiImageSection);
			}
		}
		
		try {
			if (qiImageSectionList.size() > 0){
				getModel().saveAllImageSections(qiImageSectionList);
			}
				
		}catch(Exception e) {
			handleException("An error occurred at saveBtnAction method ", "Failed to update Image Section", e);
		}
	}
	
	private void performDeleteAction(List<QiImageSectionDto> assignedDataSectionList) {
		
		List<QiImageSection> qiImageSectionDeleteList = new ArrayList<QiImageSection>();
		List<QiImageSectionDto> imageSectionDtoDeleteList = new ArrayList<QiImageSectionDto>();
		String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
        String sectionID = "";
		boolean showErrorMsg = false;
		List<String> imageSectionIdList = new ArrayList<String>();
		
		for(QiImageSectionDto section : assignedOriginalSectionList) {
			if(!assignedDataSectionList.contains(section)) {
				imageSectionIdList.add(section.getImageSectionId()+","+section.getPartLocationId()+","+imageName);
				imageSectionDtoDeleteList.add(section);
			}
		}
		String returnValue=isLocalSiteImpacted(imageSectionIdList,getView().getStage());
		if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
			return;
		}
		else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
			displayErrorMessage("Image Section cannot be deleted as it impacts other local site(s).");
			return;
		}
		else
		{
			for(QiImageSectionDto section : imageSectionDtoDeleteList) {
				QiImageSection qiImageSection = new QiImageSection();
				qiImageSection.setImageName(imageName);
				qiImageSection.setImageSectionId(section.getImageSectionId());				
				qiImageSection.setPartLocationId(section.getPartLocationId());
				List<Integer> partLocationIds = new ArrayList<Integer>();
				partLocationIds.add(section.getPartLocationId());
				qiImageSectionDeleteList.add(qiImageSection);
					
		}
		}
		
		
		
		try {
			if (qiImageSectionDeleteList.size() > 0) {
				for(QiImageSection deleteSection : qiImageSectionDeleteList) {
					if(getModel().getImageSectionCount(deleteSection.getImageSectionId()) > 1){
						getModel().deleteImageSectionById(deleteSection);
					}else {
						getModel().deleteImageSectionPoint(deleteSection.getImageSectionId());
						getModel().deleteImageSection(deleteSection);
						refreshShowAllImageSection();
					}
				}
			}
				
		}catch(Exception e) {
			handleException("An error occurred at saveBtnAction method ", "Failed to update Image Section", e);
		}
		if(showErrorMsg) {
			MessageDialog.showError(getView().getStage(), "Image Section Id - " + sectionID + " cannot be Updated as it is being used by Entry Screen", "Error Occured!");
		}
	}

	private void saveOrUpdateImageSectionPoints(List<QiImageSectionPoint> imageSectionPointList, int imageSectionId) {
		QiImageSectionPoint imageSectionPoint;
		QiImageSectionPointId qImageSectionPointId;
		for (int i = 0; i<drawnXPointsList.size()-1 ; i++) {
			imageSectionPoint=new QiImageSectionPoint();
			qImageSectionPointId = new QiImageSectionPointId();
			qImageSectionPointId.setPointSequenceNo(i);
			qImageSectionPointId.setImageSectionId(imageSectionId);
			imageSectionPoint.setId(qImageSectionPointId);
			imageSectionPoint.setPointX( (int) Math.round(drawnXPointsList.get(i)));
			imageSectionPoint.setPointY((int) Math.round(drawnYPointsList.get(i)));
			if(updatePolygon)
				imageSectionPoint.setUpdateUser(getUserId());
			else
				imageSectionPoint.setCreateUser(getUserId());
			imageSectionPointList.add(imageSectionPoint);
		}
	}
	
	public void deleteBtnAction(ActionEvent event) {
		if (getView().getImageNameTablePane().getSelectedItem() != null) {
			if (selectionModel.selection != null && !selectionModel.selection.isEmpty()) {
				if (getView().getAssignedPartNameTablePane().getSelectedItem() != null) {
					int imageSectionId = Integer.parseInt(selectionModel.selection.iterator().next().getId());
					String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
					List<String> imageSectionIdList=new ArrayList<String>();
					imageSectionIdList.add(imageSectionId+","+getPartLocationId(imageSectionId)+","+imageName);
					String returnValue=isLocalSiteImpacted(imageSectionIdList,getView().getStage());
					if(returnValue.equals(QiConstant.NO_LOCAL_SITES_CONFIGURED)){
						return;
					}
					else if(returnValue.equals(QiConstant.LOCAL_SITES_IMPACTED)){
						displayErrorMessage("Image Section cannot be deleted as it impacts other local site(s).");
						return;
					}
					else{
						ReasonForChangeDialog reasonForChangeDialog = new ReasonForChangeDialog(getApplicationId());
						if (reasonForChangeDialog.showReasonForChangeDialog("Are you sure you want to delete the Image Section?")) {
							clearDisplayMessage();
							try {
								QiImageSectionDto selectedDto = getView().getAssignedPartNameTablePane().getTable().getSelectionModel().getSelectedItem();
								String primaryKey = getView().getImageNameTablePane().getSelectedItem().getImageName() + " " + imageSectionId;
								getModel().deleteImageSectionPoint(imageSectionId);
								getModel().deleteImageSection(imageSectionId);
								QiImageSection imageSection = new QiImageSection();
								imageSection.setImageSectionId(imageSectionId);
								imageSection.setPartLocationId(selectedDto.getPartLocationId());
								AuditLoggerUtil.logAuditInfo(imageSection, null, reasonForChangeDialog.getReasonForChangeText(), getView().getScreenName(), primaryKey,getUserId());
								refreshToolsAndDrawing();
								enableCopyFromButtom();
							} catch (Exception e) {
								handleException("An error occurred at deleteImage method ",
										"Failed to Delete Image Section", e);
							}
						} else
							return;
					} 
				} else {
					displayErrorMessage("Assigned Part Name Not Selected");
					return;
				}
			} else {
				displayErrorMessage("Image Section Not Selected");
				return;
			}
		} else {
			displayErrorMessage("Image/Part Not Selected");
			return;
		}
	}
	
	private int getPartLocationId(int imageSectionId) {
		for(QiImageSectionDto imageSection : assignedOriginalSectionList) {
			if(imageSectionId == imageSection.getImageSectionId()) {
				return imageSection.getPartLocationId();
			}
		}
		return 0;
	}
	
	public void polygonReshapingAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null ){
			if(selectionModel.selection!=null && !selectionModel.selection.isEmpty() && getView().getAssignedPartNameTablePane().getSelectedItem()!=null){
				clearDisplayMessage();
				for (Node node :  selectionModel.selection) {
					if(node instanceof Polygon)
				        getView().getAnchorPane().getChildren().addAll(createControlAnchorsFor((Polygon)node));
			    }
				polygonReshape = true;
				updatePolygon=true;
				getView().getDrawBtn().setDisable(true);
				getView().getSaveBtn().setDisable(true);
				disableDynamicDrawingTools();
				enableOrDisableFilterAndCheckBox(true);
				disableUpAndDownBtn();
				getView().getClear().setDisable(false);
			}else{
				displayErrorMessage("Image Section or Assigned Part Name Not Selected");
				return;
			}
		}else{
			displayErrorMessage("Image Not Selected");
			return;
		}
	}
	
	public void copyAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null){
			if(selectionModel.selection!=null && !selectionModel.selection.isEmpty() && getView().getAssignedPartNameTablePane().getSelectedItem()!=null){
				clearDisplayMessage();
				int imageSectionId = Integer.parseInt(selectionModel.selection.iterator().next().getId());
				List<QiImageSectionPoint> polygonPoints = getModel().findPolygonPoints(imageSectionId);
				if(!polygonPoints.isEmpty()){
					List<Double> xPointsList = new ArrayList<Double>();
					List<Double> yPointsList = new ArrayList<Double>();
					List<Double> allPointsList = new ArrayList<Double>();
					createPointsListForCopyOrFlip(polygonPoints, xPointsList, yPointsList);
					double minXValue = Collections.min(xPointsList);
					double minYValue = Collections.min(yPointsList);
					for(int i=0; i<polygonPoints.size(); i++){
						allPointsList.add(xPointsList.get(i)-minXValue);
						drawnXPointsList.add(xPointsList.get(i)-minXValue);
						allPointsList.add(yPointsList.get(i)-minYValue);
						drawnYPointsList.add(yPointsList.get(i)-minYValue);
					}
					drawnXPointsList.add(xPointsList.get(0)-minXValue);
					drawnYPointsList.add(yPointsList.get(0)-minYValue);
					copyOrFlipPolygon.getPoints().setAll(allPointsList);
				}
				disableDynamicDrawingTools();
				enableOrDisableFilterAndCheckBox(true);
				disableUpAndDownBtn();
				getView().getDrawBtn().setDisable(true);
				getView().getPaste().setDisable(false);
				getView().getClear().setDisable(false);
				newPolygonCreated = true;
				polygonCopy = true;
			}else{
				displayErrorMessage("Image Section or Assigned Part Name Not Selected");
				return;
			}
		}else{
			displayErrorMessage("Image Not Selected");
			return;
		}
	}

	private void disableDynamicDrawingTools() {
		getView().getPolygonReshaping().setDisable(true);
		getView().getCopy().setDisable(true);
		getView().getFlipHorizontal().setDisable(true);
		getView().getFlipVertical().setDisable(true);
		getView().getPrint().setDisable(true);
		getView().getDeleteBtn().setDisable(true);
		getView().getMoveBtn().setDisable(true);
	}
	
	public void pasteAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null){
			clearDisplayMessage();
			copyOrFlipPolygon.setFill(null);
			copyOrFlipPolygon.setStroke(Color.web(getModel().getLineColor()));
			copyOrFlipPolygon.setStrokeWidth(getModel().getLineWidth());
			copyOrFlipPolygon.setId("new");
			getView().getAnchorPane().getChildren().add(copyOrFlipPolygon);
			getView().getPaste().setDisable(true);
			getView().getMoveBtn().setDisable(false);
			polygonCopy = false;
		}else{
			displayErrorMessage("Image/Part Not Selected");
			return;
		}
	}
	
	public void undoAction(ActionEvent event) {
		clearDisplayMessage();
		try {
			setCanvasAndImage();
			getView().getSaveBtn().setDisable(true);
			if (drawnXPointsList.size() == drawnYPointsList.size()) {
				for (int i = 0; i < drawnXPointsList.size() - 1; i++) {
					if (i == 0) {
						graphicsContext.strokeLine(drawnXPointsList.get(i), drawnYPointsList.get(i), drawnXPointsList.get(i), drawnYPointsList.get(i));
					} else {
						graphicsContext.strokeLine(drawnXPointsList.get(i-1), drawnYPointsList.get(i-1), drawnXPointsList.get(i), drawnYPointsList.get(i));
					}
				}
			}
			if ((!drawnXPointsList.isEmpty()) && (!drawnYPointsList.isEmpty()) && drawnXPointsList.size()>1) {
				removePointsFromList();
				drawStatus = true;
			}else{
				removePointsFromList();
				drawStatus = false;
				getView().getUndo().setDisable(true);
				enableOrDisableFilterAndCheckBox(false);
				if(isFullAccess())
					getView().getDrawBtn().setDisable(false);
				enableCopyFromButtom();
			}
			polygonDraw = false;
		} catch (Exception e) {
			handleException("An error occured in undo action ", "Failed to Undo Image Section", e);
		}
	}
	
	private void removePointsFromList() {
		drawnXPointsList.remove(drawnXPointsList.size() - 1);
		drawnYPointsList.remove(drawnYPointsList.size() - 1);
	}
	
	public void clearAction(ActionEvent event) {
		clearDisplayMessage();
		setCanvasAndImage();
		clearAllOperation();
	}
	
	private void clearAllOperation() {
		drawnXPointsList.clear();
		drawnYPointsList.clear();
		drawStatus = false;
		polygonCopy = false;
		polygonDraw = false;
		movePolygon= false;
		newPolygonCreated = false;
		assignedPartTableUpdated = false;
		getView().getUndo().setDisable(true);
		getView().getSaveBtn().setDisable(true);
		getView().getClear().setDisable(true);
		getView().getPaste().setDisable(true);
		getView().getZoomIn().setDisable(false);
		if(isFullAccess()) {
			getView().getDrawBtn().setDisable(false);
		}
		if(updatePolygon) {
			retrieveImageSectionOrEnableDrawing();
			refreshShowAllImageSection();
			removeControlAnchors();
			updatePolygon = false;
			dragStatus = false;
			polygonReshape = false;
		}
		selectionModel.clear();
		getView().getAnchorPane().getChildren().remove(copyOrFlipPolygon);
		getView().getAssignedPartNameTablePane().getTable().getItems().clear();
		enableOrDisableFilterAndCheckBox(false);
		enableCopyFromButtom();
	}
	
	public void zoomInAction(ActionEvent event) {
		zoom(zoomFactor);
		if (zoomCounter == 7) {
			getView().getZoomIn().setDisable(true);
			getView().getZoomOut().setDisable(false);
		}
		setZoomInCoordinates();
		event.consume();
	}
	
	private void zoom(double zoomFactor) {
		clearDisplayMessage();
		zoomWidth.set(zoomWidth.get() * zoomFactor);
		zoomHeight.set(zoomHeight.get() * zoomFactor);
		getView().getAnchorPane().setScaleX(getView().getAnchorPane().getScaleX() * zoomFactor);
		getView().getAnchorPane().setScaleY(getView().getAnchorPane().getScaleY() * zoomFactor);
		zoomCounter = zoomFactor>1 ? ++zoomCounter: --zoomCounter;
		if (zoomCounter > 0 && zoomCounter < 7) {
			getView().getZoomIn().setDisable(false);
			getView().getZoomOut().setDisable(false);
		}
	}
	
	private void resetZoom() {
		for(int i=0; i<zoomCounter; i++){
			getView().getAnchorPane().setScaleX(getView().getAnchorPane().getScaleX()/zoomFactor);
			getView().getAnchorPane().setScaleY(getView().getAnchorPane().getScaleY()/zoomFactor);
		}
		zoomWidth.set(getView().getImageSize());
		zoomHeight.set(getView().getImageSize());
		zoomCounter = 0;
		xCoordinate.clear();
		yCoordinate.clear();
		getView().getAnchorPane().setTranslateX(0.0);
		getView().getAnchorPane().setTranslateY(0.0);
		getView().getZoomIn().setDisable(false);
		getView().getZoomOut().setDisable(true);
	}
	
	public void zoomOutAction(ActionEvent event) {
		zoom(1/zoomFactor);
		if (zoomCounter == 0) {
			getView().getZoomIn().setDisable(false);
			getView().getZoomOut().setDisable(true);
		}
		setZoomOutCoordinates();
		event.consume();
	}
	
	private void setZoomInCoordinates() {
		double translateX = (zoomWidth.get() - (zoomWidth.get()/zoomFactor)) + getView().getAnchorPane().getTranslateX();
		double translateY = (zoomHeight.get() - (zoomHeight.get() / zoomFactor)) + getView().getAnchorPane().getTranslateY();
		getView().getAnchorPane().setTranslateX(((translateX / 2) + getView().getAnchorPane().getTranslateX()) * zoomFactor);
		getView().getAnchorPane().setTranslateY(((translateY / 2) + getView().getAnchorPane().getTranslateY()) * zoomFactor);

		if (!xCoordinate.containsKey(zoomCounter)) {
			xCoordinate.put(zoomCounter, getView().getAnchorPane().getTranslateX());
		}
		if (!yCoordinate.containsKey(zoomCounter)) {
			yCoordinate.put(zoomCounter, getView().getAnchorPane().getTranslateY());
		}
	}

	private void setZoomOutCoordinates() {
		if (zoomCounter == 0) {
			getView().getAnchorPane().setTranslateX(0);
			getView().getAnchorPane().setTranslateY(0);
		} else {
			getView().getAnchorPane().setTranslateX(xCoordinate.get(zoomCounter));
			getView().getAnchorPane().setTranslateY(yCoordinate.get(zoomCounter));
		}
	}
	
	public void flipHorizontalAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null){
			if(selectionModel.selection!=null && !selectionModel.selection.isEmpty() && getView().getAssignedPartNameTablePane().getSelectedItem()!=null){
				clearDisplayMessage();
				int imageSectionId = Integer.parseInt(selectionModel.selection.iterator().next().getId());
				List<QiImageSectionPoint> polygonPoints = getModel().findPolygonPoints(imageSectionId);
				if(!polygonPoints.isEmpty()){
					List<Double> xPointsList = new ArrayList<Double>();
					List<Double> yPointsList = new ArrayList<Double>();
					List<Double> allPointsList = new ArrayList<Double>();
					createPointsListForCopyOrFlip(polygonPoints, xPointsList, yPointsList);
					for(int i=0; i<polygonPoints.size(); i++){
						allPointsList.add(getView().getCanvas().getWidth()-xPointsList.get(i));
						drawnXPointsList.add(getView().getCanvas().getWidth()-xPointsList.get(i));
						allPointsList.add(yPointsList.get(i));
						drawnYPointsList.add(yPointsList.get(i));
					}
					drawnXPointsList.add(getView().getCanvas().getWidth()-xPointsList.get(0));
					drawnYPointsList.add(yPointsList.get(0));
					copyOrFlipPolygon.getPoints().setAll(allPointsList);
					copyOrFlipPolygon.setFill(null);
					copyOrFlipPolygon.setStroke(Color.web(getModel().getLineColor()));
					copyOrFlipPolygon.setStrokeWidth(getModel().getLineWidth());
					copyOrFlipPolygon.setId("new");
					getView().getAnchorPane().getChildren().add(copyOrFlipPolygon);
					getView().getDrawBtn().setDisable(true);
					getView().getClear().setDisable(false);
					disableDynamicDrawingTools();
					enableOrDisableFilterAndCheckBox(true);
					disableUpAndDownBtn();
					getView().getMoveBtn().setDisable(false);
					newPolygonCreated = true;
				}
			}else{
				displayErrorMessage("Image Section or Assigned Part Name Not Selected");
				return;
			}
		}else{
			displayErrorMessage("Image Not Selected");
			return;
		}
	}

	public void flipVerticalAction(ActionEvent event) {
		if(getView().getImageNameTablePane().getSelectedItem()!=null){
			if(selectionModel.selection!=null && !selectionModel.selection.isEmpty() && getView().getAssignedPartNameTablePane().getSelectedItem()!=null){
				clearDisplayMessage();
				int imageSectionId = Integer.parseInt(selectionModel.selection.iterator().next().getId());
				List<QiImageSectionPoint> polygonPoints = getModel().findPolygonPoints(imageSectionId);
				if(!polygonPoints.isEmpty()){
					List<Double> xPointsList = new ArrayList<Double>();
					List<Double> yPointsList = new ArrayList<Double>();
					List<Double> allPointsList = new ArrayList<Double>();
					createPointsListForCopyOrFlip(polygonPoints, xPointsList, yPointsList);
					for(int i=0; i<polygonPoints.size(); i++){
						allPointsList.add(xPointsList.get(i));
						drawnXPointsList.add(xPointsList.get(i));
						allPointsList.add(getView().getCanvas().getHeight()-yPointsList.get(i));
						drawnYPointsList.add(getView().getCanvas().getHeight()-yPointsList.get(i));
					}
					drawnXPointsList.add(xPointsList.get(0));
					drawnYPointsList.add(getView().getCanvas().getHeight()-yPointsList.get(0));
					copyOrFlipPolygon.getPoints().setAll(allPointsList);
					copyOrFlipPolygon.setFill(null);
					copyOrFlipPolygon.setStroke(Color.web(getModel().getLineColor()));
					copyOrFlipPolygon.setStrokeWidth(getModel().getLineWidth());
					copyOrFlipPolygon.setId("new");
					getView().getAnchorPane().getChildren().add(copyOrFlipPolygon);
					getView().getDrawBtn().setDisable(true);
					getView().getClear().setDisable(false);
					disableDynamicDrawingTools();
					enableOrDisableFilterAndCheckBox(true);
					disableUpAndDownBtn();
					newPolygonCreated = true;
					getView().getMoveBtn().setDisable(false);
				}
			}else{
				displayErrorMessage("Image Section or Assigned Part Name Not Selected");
				return;
			}
		}else{
			displayErrorMessage("Image Not Selected");
			return;
		}
	}
	
	private void createPointsListForCopyOrFlip(List<QiImageSectionPoint> polygonPoints, List<Double> xPointsList, List<Double> yPointsList) {
		copyOrFlipPolygon = new Polygon();
		drawnXPointsList = new ArrayList<Double>();
		drawnYPointsList = new ArrayList<Double>();
		for(int i = 0; i<polygonPoints.size(); i++){
			xPointsList.add((double) polygonPoints.get(i).getPointX());
			yPointsList.add((double) polygonPoints.get(i).getPointY());
		}
	}
	
	public void printAction(ActionEvent event) {
		WritableImage snapImage = null;
		ImageView imageView = null;
		ImageSectionPrintDialog printDialog = new ImageSectionPrintDialog(getApplicationId());
		String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
		QiImage qiImage=getModel().findQiImageByImageName(imageName);
		printImageSectionList.clear();
		if(qiImage!=null){
			clearDisplayMessage();
			Image img = new Image(new ByteArrayInputStream(qiImage.getImageData()));
			if (printDialog.showPrintOptionDialog()) {
				if (printDialog.getImgBtn().isSelected()) {
					if(getView().getShowGrid().isSelected()) {
						removeAllImageSections();
						snapImage = getView().getAnchorPane().snapshot(new SnapshotParameters(), null);
						imageView = new ImageView(snapImage);
						refreshShowAllImageSection();
					}else
						imageView = new ImageView(img);
				} else if (printDialog.getImgSectionBtn().isSelected()) {
					if (!getView().getShowAllImageSections().isSelected()) {
						removeAllImageSections();
						drawAllImageSections(false);
						getView().getAnchorPane().getChildren().addAll(getView().getImageSections());
						snapImage = getView().getAnchorPane().snapshot(new SnapshotParameters(), null);
						refreshShowAllImageSection();
					} else
						snapImage = getView().getAnchorPane().snapshot(new SnapshotParameters(), null);
					imageView = new ImageView(snapImage);
				} else if (printDialog.getImgSectionPartNameBtn().isSelected()) {
					removeAllImageSections();
					drawAllImageSections(true);
					getView().getAnchorPane().getChildren().addAll(getView().getImageSections());
					snapImage = getView().getAnchorPane().snapshot(new SnapshotParameters(), null);
					setCanvasAndImage();
					refreshShowAllImageSection();
					imageView = new ImageView(snapImage);
				}
				Printer printer = Printer.getDefaultPrinter();
				PrinterJob printJob = PrinterJob.createPrinterJob(printer);
				PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
				
				imageView.setPreserveRatio(true);
				imageView.setFitWidth(pageLayout.getPrintableWidth());
				printJob.getJobSettings().setPageLayout(pageLayout);
				
				final List<Node> pages = getView().getPrintTablePages(pageLayout, imageView, printDialog.getImgSectionPartNameBtn().isSelected(), printImageSectionList);
				final boolean print = printJob.showPrintDialog(null);
				if (print) {
					for(Node node : pages)
						printJob.printPage(node);
					printJob.endJob();
				}
			}
		}else{
			displayErrorMessage("Image Not Selected");
			return;
		}
	}
	
	private void drawGridLines() {
		double gridFactorHeight = getView().getImageSize()/ (QiConstant.NUMBER_OF_GRIDLINES);
        double gridFactorWidth = getView().getImageSize()/ (QiConstant.NUMBER_OF_GRIDLINES);
        for (int i = 0; i <= QiConstant.NUMBER_OF_GRIDLINES; i++) {
            getView().getGridLines().add(new Line(i * gridFactorWidth, 0, i * gridFactorWidth, getView().getImageSize()));
            getView().getGridLines().add(new Line(0, i * gridFactorHeight, getView().getImageSize(), i* gridFactorHeight));
        }
	}
	
	public void showGridAction(ActionEvent event) {
		clearDisplayMessage();
		CheckBox showGrid = getView().getShowGrid();
		try {
			if(showGrid.isSelected()){
				drawGridLines();
				getView().getAnchorPane().getChildren().addAll(getView().getGridLines());
			} else
				getView().getAnchorPane().getChildren().removeAll(getView().getGridLines());
		} catch (Exception e) {
			handleException("An error occured in during show action ", "Failed to Show Grid Lines", e);
		}
	}
	
	private void drawAllImageSections(boolean isPrintSectionNo){
		if(getView().getImageNameTablePane().getSelectedItem()!=null){
			String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
			List<QiImageSectionPoint> polygonPoints = getModel().showAllImageSection(imageName);
			if(!polygonPoints.isEmpty()){
				getPolygonList(polygonPoints, isPrintSectionNo);
			}
		}
	}

	private void getPolygonList(List<QiImageSectionPoint> polygonPoints, boolean isPrintSectionNo) {
		int imageSectionSerialNo = 0;
		List<Double> allPointsList = new ArrayList<Double>();
		Polygon polygon = new Polygon();
		for(int i = 0; i<polygonPoints.size(); i++){
			int imageSectionId = polygonPoints.get(i).getImageSectionId();
			if((i+1)<polygonPoints.size() && imageSectionId == polygonPoints.get(i+1).getImageSectionId()){
				allPointsList.add((double) polygonPoints.get(i).getPointX());
				allPointsList.add((double) polygonPoints.get(i).getPointY());
			}else{
				allPointsList.add((double) polygonPoints.get(i).getPointX());
				allPointsList.add((double) polygonPoints.get(i).getPointY());
				polygon.getPoints().setAll(allPointsList);
				polygon.setFill(null);
				polygon.setStroke(Color.web(getModel().getLineColor()));
				polygon.setStrokeWidth(getModel().getLineWidth());
				polygon.setId(imageSectionId+"");
				getView().getImageSections().add(polygon);
				if(copyFromImage) {
					QiImageSectionDto imageSection = new QiImageSectionDto();
					imageSection.setImageSectionId(imageSectionId);
					assignedOriginalSectionList.add(imageSection);
				}
				if(isPrintSectionNo){
					imageSectionSerialNo++;
					double x,y = 0;
					graphicsContext.setStroke(Color.BLUE);
					int pos = getMaxCordinate(allPointsList);
					 if(allPointsList.get(0)>getView().getImageSize()/2)
											x = allPointsList.get(pos-1)-1;
										else
											x = allPointsList.get(pos-1)+1;
					 if(allPointsList.get(pos)>(getView().getImageSize())/2)
											y = allPointsList.get(pos)-1;
										else
											y = allPointsList.get(pos)+1;
					
					graphicsContext.strokeText(String.valueOf(imageSectionSerialNo), x, y);
					graphicsContext.setFont(new Font("default", 15));
					
					List<QiImageSectionDto> partNameList = getModel().findPartNameByImageSectionId(imageSectionId);
					for(QiImageSectionDto partName : partNameList) {
						QiImageSectionDto printTableDto = new QiImageSectionDto();
						printTableDto.setSequenceNo(imageSectionSerialNo);
						printTableDto.setInspectionPartName(partName.getFullPartDesc());
						printImageSectionList.add(printTableDto);
					}
				}
				allPointsList = new ArrayList<Double>();
				polygon = new Polygon();
			}
		}
	}
	
	
	private int getMaxCordinate(List<Double> allPointsList) {
		double max = 0;
		int pos = 1;
		for(int i=1; i<allPointsList.size(); i+=2){
			if(max < allPointsList.get(i)) {
				max = allPointsList.get(i);
				pos = i;
			}
		}
		return pos;
	}
	
	private void removeAllImageSections(){
		if(getView().getImageSections()!=null){
			getView().getAnchorPane().getChildren().removeAll(getView().getImageSections());
			getView().getImageSections().clear();
		}
	}
	
	private void clearAnchorAndGraphics() {
		getView().getAnchorPane().getChildren().remove(copyOrFlipPolygon);
		graphicsContext.clearRect(0, 0, getView().getCanvas().getWidth(), getView().getCanvas().getHeight());
		getView().getAssignedPartNameTablePane().getTable().getItems().clear();
	}
	
	private void refreshShowAllImageSection(){
		CheckBox showAllImageSections = getView().getShowAllImageSections();
		removeAllImageSections();
		try {
			if(showAllImageSections.isSelected()){
				getView().getImageSections().clear();
				drawAllImageSections(false);
				if(getView().getImageSections()!=null)
					getView().getAnchorPane().getChildren().addAll(getView().getImageSections());
				if(!eventHandler) {
					new RubberBandSelection(getView().getAnchorPane());
					eventHandler = true;
				}
			}else{
				removeAllImageSections();
				retrieveImageSectionOrEnableDrawing();
			}
		} catch (Exception e) {
			handleException("An error occured during show all image section action ", "Failed to Show All Image Sections", e);
		}
	}
	
	public void showAllImageSectionsAction(ActionEvent event) {
		clearDisplayMessage();
		disableDrawingTools();
		clearAllOperation();
		refreshShowAllImageSection();
	}
	
	public void upBtnAction(ActionEvent event) {
		if (!getView().getAssignedPartNameTablePane().getSelectedItems().isEmpty()) {
			clearDisplayMessage();

			if (getView().getAssignedPartNameTablePane().getTable().getItems().size() == 1) {
				QiImageSectionDto selectedDto = getView().getAssignedPartNameTablePane().getTable().getSelectionModel().getSelectedItem();
				getView().getAssignedPartNameTablePane().getTable().getItems().removeAll(selectedDto);
				if (selectedDto.getImageSectionId() == 0) {
					QiImageSectionDto newPolygon = new QiImageSectionDto();
					newPolygon.setImageSectionId(0);
					newPolygon.setPartLocationId(0);
					getView().getAssignedPartNameTablePane().getTable().getItems().add(newPolygon);
					getView().getAssignedPartNameTablePane().getTable().getSelectionModel().select(0);
				}

			} else {
				getView().getAssignedPartNameTablePane().getTable().getItems().removeAll(getView().getAssignedPartNameTablePane().getSelectedItems());
			}

			assignedPartTableUpdated = true;
			disableDrawingTools();
			enableOrDisableFilterAndCheckBox(true);
			getView().getClear().setDisable(false);
			if(isFullAccess())
				getView().getSaveBtn().setDisable(false);

			if (getView().getAssignedPartNameTablePane().getTable().getItems().size() > 0) {
				getView().getDownArrowBtn().setDisable(false);
				getView().getUpArrowBtn().setDisable(false);
			} else
				getView().getUpArrowBtn().setDisable(true);

		}else {
				displayErrorMessage("Assigned Part Name Not Selected");
				return;				
			}
	}
	
	public void downBtnAction(ActionEvent event) {

		if(!getView().getAssignedPartNameTablePane().getSelectedItems().isEmpty() && !getView().getPartNameTablePane().getSelectedItems().isEmpty()) {
			clearDisplayMessage();
			QiImageSectionDto selectedDto = getView().getAssignedPartNameTablePane().getTable().getSelectionModel().getSelectedItem();
			List<QiImageSectionDto> selectedPartName = getView().getPartNameTablePane().getSelectedItems();
			List<QiImageSectionDto> assignedList = new ArrayList<QiImageSectionDto>();
			
			for (QiImageSectionDto imageSection : selectedPartName) {
				if (!isRecordAvailable(imageSection, selectedDto.getImageSectionId())) {
					if (selectedDto.getImageSectionId() == 0) {
						if (selectedDto.getPartLocationId() == 0) {
							getView().getAssignedPartNameTablePane().getTable().getItems().removeAll(selectedDto);
						}
						
						QiImageSectionDto newSection = new QiImageSectionDto();
						newSection.setImageSectionId(0);
						newSection.setPartLocationId(imageSection.getPartLocationId());
						newSection.setInspectionPart2Location2Name(imageSection.getFullPartDesc());
						getView().getAssignedPartNameTablePane().getTable().getItems().add(newSection);
						getView().getAssignedPartNameTablePane().getTable().getSelectionModel().select(newSection);
						newPolygonCreated = true;
						
					} else {
						imageSection.setImageSectionId(selectedDto.getImageSectionId());
						getView().getAssignedPartNameTablePane().getTable().getItems().add(imageSection);
					}
					assignedPartTableUpdated = true;
				}
			}
			
        	if(assignedPartTableUpdated) {
        		disableDrawingTools();
	        	getView().getClear().setDisable(false);
	        	getView().getDownArrowBtn().setDisable(false);
	        	getView().getUpArrowBtn().setDisable(false);
				getView().getAssignedPartNameTablePane().getTable().getItems().addAll(assignedList);
				enableOrDisableFilterAndCheckBox(true);
				if(isFullAccess())
					getView().getSaveBtn().setDisable(false);
        	}
		}else {
			displayErrorMessage("Part Name or Assigned Part Name Not Selected");
			return;				
		}
	}
	
	private void moveBtnAction(ActionEvent event) {
		if (selectionModel.selection.size() == 1) {
			Node moveNode = selectionModel.selection.iterator().next();
			movePolygon = true;
			MouseGestures mg = new MouseGestures(getView().getAnchorPane(), moveNode);
			mg.addEventHandler();
			
			getView().getDrawBtn().setDisable(true);
			getView().getSaveBtn().setDisable(true);
			disableDynamicDrawingTools();
			enableOrDisableFilterAndCheckBox(true);
			disableUpAndDownBtn();
			getView().getClear().setDisable(false);
		} else {
			displayErrorMessage("Image Section or Assigned Part Name Not Selected");
			return;
		}
	}
	
	private void resetBtnAction(ActionEvent event) {
		clearDisplayMessage();
		removeAllImageSections();
		copyFromImage = false;
		assignedOriginalSectionList.clear();
		getView().getSaveBtn().setDisable(true);
		if(isFullAccess()) {
			getView().getDrawBtn().setDisable(false);
		}
		
		getView().getResetBtn().setDisable(true);
		getView().getCopyFromBtn().setDisable(false);
		getView().getPartNameTablePane().setDisable(false);
		getView().getFullPartNameFilterTextField().setDisable(false);
		enableOrDisableFilterAndCheckBox(false);
	}
	
	private void copyFromBtnAction(ActionEvent event) {
			List<String> imageNameList = getModel().findAllImageNameBySection();
			if(!imageNameList.isEmpty()) {
				ImageSectionCopyFromDialog dialog = new ImageSectionCopyFromDialog(getApplicationId(), imageNameList ,getModel());
				if(dialog.showCopyFromDialog()) {
					copyFromImage = true;
					String imageName = dialog.getImageNameComboBox().getControl().getSelectionModel().getSelectedItem();
					if(imageName != null) {
						List<QiImageSectionPoint> polygonPoints = getModel().showAllImageSection(imageName);
						if(!polygonPoints.isEmpty()){
							getPolygonList(polygonPoints, false);
						}
						if(getView().getImageSections()!=null)
							getView().getAnchorPane().getChildren().addAll(getView().getImageSections());
						
						if(isFullAccess()) {
							getView().getSaveBtn().setDisable(false);
						}
						getView().getDrawBtn().setDisable(true);
						getView().getResetBtn().setDisable(false);
						getView().getCopyFromBtn().setDisable(true);
						disableDynamicDrawingTools();
						enableOrDisableFilterAndCheckBox(true);
						getView().getPartNameTablePane().setDisable(true);
						getView().getFullPartNameFilterTextField().setDisable(true);
					}
				}
			}
		}
	
	private void performCopyFromOperation() {
		
		if (getView().getImageNameTablePane().getSelectedItem() != null) {
			List<QiImageSectionPoint> imageSectionPointList = new ArrayList<QiImageSectionPoint>();
			List<QiImageSection> qiImageSectionList = new ArrayList<QiImageSection>();
			String imageName = getView().getImageNameTablePane().getSelectedItem().getImageName();
			int imageSectionId = getModel().getMaxSectionId();

			for (QiImageSectionDto section : assignedOriginalSectionList) {

				List<QiImageSectionDto> imageSectionDto = getModel().findPartNameByImageSectionId(section.getImageSectionId());
				List<QiImageSectionPoint> imageSectionPoints = getModel().findPolygonPoints(section.getImageSectionId());
				imageSectionId++;
				
				for (QiImageSectionDto imageSectionPart : imageSectionDto) {
					QiImageSection qiImageSection = new QiImageSection();
					qiImageSection.setImageName(imageName);
					qiImageSection.setPartLocationId(imageSectionPart.getPartLocationId());
					qiImageSection.setCreateUser(getUserId());
					qiImageSection.setImageSectionId(imageSectionId);
					qiImageSectionList.add(qiImageSection);
				}

				for (QiImageSectionPoint sectionPoint : imageSectionPoints) {
					sectionPoint.setImageSectionId(imageSectionId);
					sectionPoint.setCreateUser(getUserId());
					imageSectionPointList.add(sectionPoint);
				}
			}

			try {
				getModel().saveAllImageSections(qiImageSectionList);
				getModel().saveImageSectionPoint(imageSectionPointList);
				copyFromImage = false;
				assignedOriginalSectionList.clear();
				refreshToolsAndDrawing();
				enableOrDisableFilterAndCheckBox(false);
				getView().getPartNameTablePane().setDisable(false);
				getView().getFullPartNameFilterTextField().setDisable(false);
				getView().getResetBtn().setDisable(true);
				getView().getDownArrowBtn().setDisable(true);
				getView().getUpArrowBtn().setDisable(true);
				getView().getDeleteBtn().setDisable(true);
				
			} catch (Exception e) {
				handleException("An error occurred at copyFromBtnAction method ", "Failed to save Image Section", e);
			}

		} else {
			displayErrorMessage("Image Not Selected");
			return;
		}
	}
	
	private boolean isRecordAvailable( QiImageSectionDto assigneeDto, int sectionID){
		for(QiImageSectionDto data : getView().getAssignedPartNameTablePane().getTable().getItems()){
			int partlocationID = data.getPartLocationId();
			int assigneePartLocationID = assigneeDto.getPartLocationId();
			if(partlocationID == assigneePartLocationID && sectionID == (int)data.getImageSectionId()){
				return true;
			}
		}
		return false;
	
	}
	
	private void disableUpAndDownBtn() {
		getView().getUpArrowBtn().setDisable(true);
		getView().getDownArrowBtn().setDisable(true);
	}

	@Override
	public void addContextMenuItems() {
	}
	
	private ObservableList<Anchor> createControlAnchorsFor(final Polygon node) {
		observableAnchorsList = FXCollections.observableArrayList();
		for (int i = 0; i < node.getPoints().size(); i += 2) {
			final int idx = i;

			DoubleProperty xProperty = new SimpleDoubleProperty(node.getPoints().get(i));
			DoubleProperty yProperty = new SimpleDoubleProperty(node.getPoints().get(i + 1));

			xProperty.addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
					node.getPoints().set(idx, (Double) x);
				}
			});

			yProperty.addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
					node.getPoints().set(idx + 1, (Double) y);
				}
			});
			observableAnchorsList.add(new Anchor(xProperty, yProperty, i, node));
		}
		return observableAnchorsList;
	}
	
	private void removeControlAnchors() {			
		List<Node> tempList = new ArrayList<Node>();
		for(Node node : getView().getAnchorPane().getChildren()) {
			if(node instanceof Circle)
				tempList.add(node);
		}
		getView().getAnchorPane().getChildren().removeAll(tempList);
		if (observableAnchorsList != null) 
			observableAnchorsList.clear();
		polygonReshape=false;
	}
	
	private void disableOrEnableDrawingTools() {
		getView().getPaste().setDisable(true);
		if(!(selectionModel.selection.size() > 0 && getView().getSaveBtn().isDisable())) {
			disableDynamicDrawingTools();
        }
	}
	
	private class SelectionModel {
        Set<Node> selection = new HashSet<Node>();
        
		public void add(Node node) {
			if (node instanceof Polygon) {
				((Polygon) node).setStroke(Color.BLUE);
			} else {
				node.setStyle("-fx-effect: dropshadow(three-pass-box, blue, 2, 2, 0, 0);");
			}
			selection.add(node);
		}

		public void remove(Node node) {
			if (node instanceof Polygon) {
				((Polygon) node).setStroke(Color.web(getModel().getLineColor()));
			} else
				node.setStyle("-fx-effect: null");
			selection.remove(node);
		}

        public void clear() {
            while( !selection.isEmpty()) {
                remove(selection.iterator().next());
            }
        }
    }
	
	private class RubberBandSelection {
        Rectangle rect;
        Pane group;

        public RubberBandSelection(Pane group) {
            this.group = group;
            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
            if(!eventHandler) {
	            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
	            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
            }
        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	if(movePolygon || copyFromImage)
            		return;
            	
            	if(polygonReshape || drawStatus || dragStatus)
                	return;
            	rect.setX(event.getX());
                rect.setY(event.getY());
                rect.setWidth(0);
                rect.setHeight(0);
                group.getChildren().add( rect);
                event.consume();
            }
        };

        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	
            	if(polygonReshape || drawStatus || dragStatus || polygonDraw || assignedPartTableUpdated || movePolygon || copyFromImage)
                	return;
            	if(displayErrorMessage){
            		displayErrorMessage = false;
            		return;
            	}
                selectionModel.clear();
                ImageSectionSelection selectionSection = new ImageSectionSelection(rect);
                boolean firstNode = true;
                boolean newPolygonfound = false;
                for (Node node : group.getChildren()) {
					if (node instanceof Polygon) {
						if (selectionSection.checkIntersects((Shape) node)) {
							if(firstNode){
				                polygonMap.clear();
							}
							if (polygonCopy || newPolygonCreated) {
								if (node.getId() == "new") {
									newPolygonfound = true;
									polygonMap.put(node.getId(), node);
									selectionModel.add(node);
									updateAssignedPartNameTable(node, firstNode);
									firstNode = false;
								}
							} else {
								polygonMap.put(node.getId(), node);
								selectionModel.add(node);
								updateAssignedPartNameTable(node, firstNode);
								firstNode = false;
							}
						}
					}
				}
                
                if(!newPolygonfound && polygonCopy) {
                	displayErrorMessage("Please paste copied Image Section and save or perform clear operation");
					return;
                }
                
                if(!newPolygonfound && newPolygonCreated) {
					displayErrorMessage("Please select new Image Section and save or perform clear operation");
					return;
				}
                
	            clearDisplayMessage();
                disableOrEnableDrawingTools();
                if(polygonCopy){
                	getView().getPaste().setDisable(false);
                }else if(isFullAccess() && !newPolygonCreated && selectionModel.selection.size() > 0) {
                	getView().getDeleteBtn().setDisable(false);
                }
				if (polygonMap.size() == 1) {
					getView().getAssignedPartNameTablePane().getTable().getSelectionModel().select(0);
				}
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);
                group.getChildren().remove(rect);
                event.consume();
            }
        };
        
        private void updateAssignedPartNameTable(Node node, boolean firstNode){
        	if(firstNode){
        		assignedOriginalSectionList.clear();
        		getView().getAssignedPartNameTablePane().getTable().getItems().clear();
        	}
        	
        	if(node.getId()!="new"){
        		List<QiImageSectionDto> imageSectionDto = getModel().findPartNameByImageSectionId(Integer.valueOf(node.getId()));
            	assignedOriginalSectionList.addAll(imageSectionDto);
            	getView().getAssignedPartNameTablePane().getTable().getItems().addAll(imageSectionDto);
            	getView().getDownArrowBtn().setDisable(false);
    			getView().getUpArrowBtn().setDisable(false);
    			if(isFullAccess())
    				getView().getDeleteBtn().setDisable(false);
    		} else {
    			QiImageSectionDto newPolygon = new QiImageSectionDto();
    			newPolygon.setImageSectionId(0);
    			newPolygon.setPartLocationId(0);
    			getView().getAssignedPartNameTablePane().getTable().getItems().add(newPolygon);
    			getView().getDownArrowBtn().setDisable(false);
    		}
        }
    }
	
	class Anchor extends Circle {
		private final DoubleProperty x, y;
		private double transferX, transferY;
        private int position;
        private Polygon polygon;

		Anchor(DoubleProperty x, DoubleProperty y, int pos, Polygon polygon) {
			super(x.get(), y.get(), 2);
			setFill(Color.GOLD.deriveColor(1, 1, 1, 0.5));
			setStroke(Color.GOLD);
			setStrokeWidth(2);
			setStrokeType(StrokeType.OUTSIDE);
			this.x = x;
			this.y = y;
			this.position = pos;
            this.polygon = polygon;
			x.bind(centerXProperty());
			y.bind(centerYProperty());
			enableDrag();
		}

		private void enableDrag() {
			final Delta dragDelta = new Delta();
			setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					getView().getScrollPane().setPannable(false);
					dragDelta.x = getCenterX() - mouseEvent.getX();
					dragDelta.y = getCenterY() - mouseEvent.getY();
					getView().getAnchorPane().setCursor(Cursor.MOVE);
				}
			});
			setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					getView().getScrollPane().setPannable(true);
					getView().getAnchorPane().setCursor(Cursor.HAND);
                    drawnXPointsList.clear();
                    drawnYPointsList.clear();
                    
                    for(int i =0; i< polygon.getPoints().size(); i+=2) {
                           if(i==position) {
                                  drawnXPointsList.add(transferX);
                                  drawnYPointsList.add(transferY);
                           }else {
                                  drawnXPointsList.add(polygon.getPoints().get(i));
                                  drawnYPointsList.add(polygon.getPoints().get(i+1));
                           }
                    }
                    drawnXPointsList.add(polygon.getPoints().get(0));
                    drawnYPointsList.add(polygon.getPoints().get(1));
                    updatePolygon = true;
                    
                    getView().getClear().setDisable(false);
                    if(isFullAccess())
                    	getView().getSaveBtn().setDisable(false);
				}
			});
			setOnMouseDragged(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					double newX = mouseEvent.getX() + dragDelta.x;
					double newY = mouseEvent.getY() + dragDelta.y;
					if((newX > 0 && newX < getView().getCanvas().getWidth()) && (newY > 0 && newY < getView().getCanvas().getHeight())) {
						setCenterX(newX);
						setCenterY(newY);
						transferX = newX;					
						transferY = newY;
					}
				}
			});
			setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						getView().getAnchorPane().setCursor(Cursor.HAND);
					}
				}
			});
			setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent mouseEvent) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						getView().getAnchorPane().setCursor(Cursor.DEFAULT);
					}
				}
			});
		}

		public class Delta {
			double x, y;
		}
	}
	
	public class MouseGestures {

        double orgSceneX, orgSceneY;
        double orgTranslateX, orgTranslateY;
        List<Double> xPointList = new ArrayList<Double>();
        List<Double> yPointList = new ArrayList<Double>();
        Node moveNode;
        Pane group;
        
        public MouseGestures(Pane group, Node node) {
            this.group = group;
            this.moveNode = node;
        }
        
        public void addEventHandler() {
        	group.setOnMousePressed(circleOnMousePressedEventHandler);
        	group.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        	group.setOnMouseReleased(circleOnMouseReleasedEventHandler);
        	group.setOnMouseEntered(circleOnMouseEnteredEventHandler);
        	group.setOnMouseExited(circleOnMouseExitedEventHandler);
        }
        
        EventHandler<MouseEvent> circleOnMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if(!movePolygon)
					return;
				if (!mouseEvent.isPrimaryButtonDown()) {
					getView().getAnchorPane().setCursor(Cursor.HAND);
				}
			}
		};
		
		EventHandler<MouseEvent> circleOnMouseExitedEventHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent mouseEvent) {
				if(!movePolygon)
					return;
				if (!mouseEvent.isPrimaryButtonDown()) {
					getView().getAnchorPane().setCursor(Cursor.DEFAULT);
				}
			}
		};
        
        EventHandler<MouseEvent> circleOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
            	if(!movePolygon)
					return;
            	getView().getScrollPane().setPannable(true);
            	getView().getAnchorPane().setCursor(Cursor.DEFAULT);
            }
        };

        EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
            	if(!movePolygon)
					return;
            	
            	getView().getScrollPane().setPannable(false);
            	getView().getAnchorPane().setCursor(Cursor.MOVE);
                orgSceneX = t.getX();
                orgSceneY = t.getY();
                if (t.getSource() instanceof Circle) {
                    Circle p = ((Circle) (t.getSource()));
                    orgTranslateX = p.getCenterX();
                    orgTranslateY = p.getCenterY();
                } else {
                		orgTranslateX = moveNode.getTranslateX();
                        orgTranslateY = moveNode.getTranslateY();
                }
            }
        };
        
        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent t) {
            	
            	if(polygonReshape || drawStatus || !movePolygon)
                	return;
            	if(getView().getAssignedPartNameTablePane().getSelectedItem() == null) {
            		displayErrorMessage = true;
            		displayErrorMessage("Assigned Part Name Not Selected");
    				return;	
            	}
            	if(selectionModel.selection.size() > 1) {
            		displayErrorMessage = true;
            		displayErrorMessage("Select one Image Section");
    				return;	
            	}
            	getView().getAnchorPane().setCursor(Cursor.HAND);
                double offsetX = t.getX() - orgSceneX;
                double offsetY = t.getY() - orgSceneY;
                double newTranslateX = orgTranslateX + offsetX;
                double newTranslateY = orgTranslateY + offsetY;
                if (t.getSource() instanceof Circle) {
                    Circle p = ((Circle) (t.getSource()));
                    p.setCenterX(newTranslateX);
                    p.setCenterY(newTranslateY);
                } else if(movePolygon) {
                	getView().getScrollPane().setPannable(false);
					for (Node node : group.getChildren()) {
						if (node instanceof Polygon) {
							Polygon p = (Polygon) node;
							if (moveNode.getId().equals(p.getId())) {
								moveNode.setTranslateX(newTranslateX);
								moveNode.setTranslateY(newTranslateY);
								if (moveNode instanceof Polygon) {
									Polygon p1 = (Polygon) moveNode;
									for (int i = 0; i < p1.getPoints().size(); i += 2) {
										xPointList.add(p1.getPoints().get(i));
										yPointList.add(p1.getPoints().get(i + 1));
									}
									xPointList.add(p1.getPoints().get(0));
									yPointList.add(p1.getPoints().get(1));
									if (p1.getId() != "new") {
										updatePolygon = true;
										dragStatus = true;
									} else
										updatePolygon = false;
									
									if(isFullAccess()) {
										getView().getSaveBtn().setDisable(false);
									}
									getView().getDrawBtn().setDisable(true);
									getView().getClear().setDisable(false);
									disableDynamicDrawingTools();
									enableOrDisableFilterAndCheckBox(true);
									disableUpAndDownBtn();
								}
							}
						}

					}
                }
                double newX, newY;
                boolean insideImageMove = true;
                for (int i = 0; i < xPointList.size(); i++) {
                	
                	newX = xPointList.get(i)+newTranslateX;
                	newY = yPointList.get(i)+newTranslateY;
                	
                	if(newX < 0 || newX > getView().getCanvas().getWidth() || newY < 0 || newY > getView().getCanvas().getHeight()) {
                		insideImageMove = false;
                	}
				}
                
                if(insideImageMove) {
                	drawnXPointsList.clear();
                	drawnYPointsList.clear();
                	for (int i = 0; i < xPointList.size(); i++) {
    					drawnXPointsList.add(xPointList.get(i)+newTranslateX);
    					drawnYPointsList.add(yPointList.get(i)+newTranslateY);
    				}
                } else {
                	moveNode.setTranslateX(orgTranslateX);
					moveNode.setTranslateY(orgTranslateY);
                }
                xPointList.clear();
                yPointList.clear();
            }
        };
        
    }
	
	private void performAuditLog(List<QiImageSectionPoint> oldList, List<QiImageSectionPoint> newList, String reasonForChange,String primaryKey) {
        for(QiImageSectionPoint oldSection: oldList){
               for(QiImageSectionPoint newSection : newList){
                     if(oldSection.getId().getImageSectionId() == newSection.getId().getImageSectionId() && 
                                   oldSection.getId().getPointSequenceNo() == newSection.getId().getPointSequenceNo()){
                            if((oldSection.getPointX() != newSection.getPointX()) || (oldSection.getPointY() != newSection.getPointY())){
                                   AuditLoggerUtil.logAuditInfo(oldSection, newSection, reasonForChange, getView().getScreenName(),primaryKey,getUserId());
                            }
                     }
               }
               
        }
        
 }
}
