package com.honda.galc.client.qi.defectentry;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.enumtype.ObservableListChangeEventType;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ObservableListChangeEvent;
import com.honda.galc.client.ui.event.ProgressEvent;
import com.honda.galc.client.util.DefectImageUtil;
import com.honda.galc.client.utils.ImageSectionSelection;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dto.qi.QiDefectEntryDto;
import com.honda.galc.dto.qi.QiDefectResultDto;
import com.honda.galc.dto.qi.QiImageSectionDto;
import com.honda.galc.dto.qi.QiMostFrequentDefectsDto;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.qi.QiImageSectionPoint;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationPreviousDefect;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
/**
 * <h3>DefectEntryByImageController</h3>
 * <p> DefectEntryByImageController: Controller file for Defect Entry by Image </p>
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
 * @author L&T Infotech<br>
 * July 14, 2017
 *
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class DefectEntryByImageController {
	
	private static final String SOLID_CIRCLE = "\u2022  ";
	public static final String PRIMARY_DEFECT = "Primary Defect";
	public static final String SECONDARY_DEFECT = "Secondary Defect";
	
	private Set<Node> selectionSet;
	private Map<String, Node> polygonMap;
	
	private DefectEntryController parentController;
	
	public DefectEntryByImageController(DefectEntryController parentController) {
		super();
		this.parentController = parentController;
		this.selectionSet = new HashSet<Node>();
		this.polygonMap = new HashMap<String, Node>();
	}
	
	/**
	 * This method is used to Initialize Listeners
	 */
	public void initializeListeners() {
		addDefect1ListViewListener();
	}
	
	/**
	 * This method is used to add Listener to Defect 1 ListView
	 */
	private void addDefect1ListViewListener() {
		getView().getDefect1ListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldValue, String newValue) {
				clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);
				QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
				String entryScreenName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getEntryScreen() : StringUtils.EMPTY);
				String imageName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getImageName() : StringUtils.EMPTY);
				int imageSectionId = 0;
				int partLocationId = 0;
				if(!selectionSet.isEmpty()) {
					imageSectionId = Integer.parseInt(selectionSet.iterator().next().getId());
					partLocationId = Integer.parseInt(selectionSet.iterator().next().getUserData().toString());  
				}
				String defect1 = getView().parseDefect(newValue, false);
				String defect2 = getView().parseDefect(newValue, true);
				if(!StringUtils.isBlank(newValue))
				  Logger.getLogger().check("Selected Defect:"+newValue);

				refreshImageFilterComponents(entryScreenName, imageName, imageSectionId, defect1, defect2,partLocationId);
			}
		});
	}

	/**
	 * This method is used to add Image Section Listener
	 */
	public void addImageSectionListener() {
		QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
		String entryScreenName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getEntryScreen() : StringUtils.EMPTY);
		String imageName = StringUtils.trimToEmpty(entryScreen!=null ? entryScreen.getImageName() : StringUtils.EMPTY);
		int imageSectionId = 0;
		int partLocationId = 0;
		if(!selectionSet.isEmpty()) {
			imageSectionId = Integer.parseInt(selectionSet.iterator().next().getId());
			partLocationId = Integer.parseInt(selectionSet.iterator().next().getUserData().toString());  
		}
		String defect1 = getView().getPrimarySelectedDefect();
		String defect2 = getView().getSecondarySelectedDefect();

		refreshImageFilterComponents(entryScreenName, imageName, imageSectionId, defect1, defect2, partLocationId);
	}
	/**
	 * This method is used to refresh Defect Entry by Image filter components 
	 */
	private void refreshImageFilterComponents(String newEntryScreenName, String newImageName, int newImageSectionId,
			String newDefect1, String newDefect2, int newPartLocationId) {
		
		String entryScreenName = newEntryScreenName, imageName = newImageName;
		String defect1 = newDefect1, defect2 = newDefect2;
		int imageSectionId = newImageSectionId, partLocationId = newPartLocationId;
		
		if(parentController.getSelectValue())   {
			return;
		}
		
		String defect1TitledFilter = getView().getDefect1TitledPane().getText();
		String defect1Filter = StringUtils.trimToEmpty(defect1TitledFilter.equalsIgnoreCase(PRIMARY_DEFECT) ? StringUtils.EMPTY : defect1TitledFilter);
		if(StringUtils.isEmpty(defect2)) {
			if(imageSectionId != 0 || partLocationId != 0)  {
				List<String> defect1List = getModel().findAllDefect1ByImageEntryScreen(entryScreenName, imageName, imageSectionId, defect2, partLocationId, defect1Filter, parentController.isAssignRealProblem(), getPart1TextFilter());
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT1_SET_ITEMS, defect1List, ObservableListChangeEventType.CHANGE_SELECTION));
			}
		}
		if(!StringUtils.isEmpty(defect1)) {
			if(getView().getDefect1ListView().getItems().contains(defect1))  {
				String defect = QiDefectResultDto.combineTypeNames(defect1, defect2);
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.DEFECT1, defect, ObservableListChangeEventType.CHANGE_SELECTION));
			}
		}

		if(imageSectionId==0) {
			removeAllImageSections();
			if(StringUtils.isEmpty(defect1) && StringUtils.isEmpty(defect2))  {
				drawAllImageSections(entryScreenName,imageName,defect1,defect2,Color.TRANSPARENT);
			} else  {
				drawAllImageSections(entryScreenName,imageName,defect1,defect2,getImageSectionColor());
			}
		} else {
			removeImageSectionsExceptSelected(imageSectionId);
			
			if(StringUtils.isEmpty(defect1) && StringUtils.isEmpty(defect2))  {
				drawAllImageSectionsExceptSelected(entryScreenName,imageName,defect1,defect2,Color.TRANSPARENT,imageSectionId);
			}
			else  {
				drawAllImageSectionsExceptSelected(entryScreenName,imageName,defect1,defect2,getImageSectionColor(),imageSectionId);
			}
		}
		List<QiDefectResultDto> selectedList = getModel().findAllFullPartDefectCombByFilter(entryScreenName, imageSectionId, defect1, defect2, partLocationId);
		if(!selectedList.isEmpty() && isAllImageComponentsSelected()) {
			parentController.setSelectedDefect(selectedList.get(0));
			parentController.getSelectedDefect().setEntryScreen(entryScreenName);
			parentController.setResponsibilityOnDefectSelect(parentController.getSelectedDefect());
			getView().getLastDefectEnteredTextAreaForImage().setText("Current Defect Selected: \n\""+parentController.getSelectedDefect().getPartDefectDesc()+"\"");
			parentController.setResponsibilityComboboxDisable(false);
			if(getView().getAcceptBtn().isDisabled()){
				EventBusUtil.publishAndWait(new ObservableListChangeEvent(DefectEntrySelection.SHOW_CONFIRM_POPUP, null, ObservableListChangeEventType.WARNING));
			}
		} else {
			parentController.setSelectedDefect(null);
			getView().setLastDefectEnteredText(true);
			parentController.setResponsibilityComboboxDisable(true);
		}
	}
	
	/**
	 * This method is used to get list of matching PDC
	 */
	protected List<QiDefectResultDto> getPart1ListViewByPartFilter(String entryScreen, String partFilter){		
		List<QiDefectResultDto> filteredDtoList = getModel().getDefectEntryCacheUtil().getImagePdcListByCombinedFilter(entryScreen, partFilter);
		return filteredDtoList;
	}
	
	/**
	 * This method is used to check whether all image components are selected
	 */
	private boolean isAllImageComponentsSelected() {
		int imageSectionId = 0;
		int partLocationId = 0;
		if(!selectionSet.isEmpty()) {
			imageSectionId = Integer.parseInt(selectionSet.iterator().next().getId());
			partLocationId = Integer.parseInt(selectionSet.iterator().next().getUserData().toString());  
		}
		String defect1 = getView().getPrimarySelectedDefect();

		if((imageSectionId != 0 && partLocationId != 0) && (null != defect1))  {
				return true;
		}
		return false;
	}

	/**
	 * This method is used to draw all image sections on Image
	 */
	private void drawAllImageSections(String entryScreen, String imageName, String defect1, String defect2, Paint color){
		List<QiImageSectionPoint> polygonPoints = new ArrayList<QiImageSectionPoint>(getModel().showAllImageSectionsByFilter(entryScreen, imageName, defect1, defect2, getPart1TextFilter()));
		if(!polygonPoints.isEmpty()){
			getPolygonList(reSeqPolygonListByArea(polygonPoints),color);
		}
		if(getView().getImageSections()!=null) {
			getView().getImagePane().getChildren().addAll(getView().getImageSections());
		}
	}
	
	/**drawAllImageSectionsByPartLocFilter
	 * This method is used to draw all image sections on Image
	 */
	private void drawAllImageSectionsByPartLocFilter(String entryScreen, String partLocFilter, Paint color){
		List<QiImageSectionPoint> polygonPoints = new ArrayList<QiImageSectionPoint>(getModel().showAllImageSectionsByPartLocFilter(entryScreen, partLocFilter));
		if(!polygonPoints.isEmpty()){
			getPolygonList(reSeqPolygonListByArea(polygonPoints),color);
		}
		if(getView().getImageSections()!=null) {
			getView().getImagePane().getChildren().addAll(getView().getImageSections());
		}
	}
	
	private float calculate(Point2D vertex[],int pointNum)  
    {  
		int i=0;  
        float temp=0;  
        for(;i<pointNum-1;i++)  
        {  
            temp+=(vertex[i].getX()-vertex[i+1].getX())*(vertex[i].getY()+vertex[i+1].getY());  
        }  
        temp+=(vertex[i].getX()-vertex[0].getX())*(vertex[i].getY()+vertex[0].getY());  
        return Math.abs(temp/2);  
    }
	
	private List<QiImageSectionPoint> reSeqPolygonListByArea(List<QiImageSectionPoint> polygonPoints) {
		
		Point2D[] vertex = new Point2D[1000];
		Map<Integer,Float> map = new TreeMap<Integer,Float>();
		int n = 0;
		for(int i = 0; i<polygonPoints.size(); i++){
			int imageSectionId = polygonPoints.get(i).getImageSectionId();
			if((i+1)<polygonPoints.size() && imageSectionId == polygonPoints.get(i+1).getImageSectionId()){
				vertex[n] = new Point2D((double) polygonPoints.get(i).getPointX(),(double) polygonPoints.get(i).getPointY());
				n++;
			}else {
				vertex[n] = new Point2D((double) polygonPoints.get(i).getPointX(),(double) polygonPoints.get(i).getPointY());
				map.put(imageSectionId,calculate(vertex,n));
				vertex = new Point2D[1000];
				n = 0;
			}
		}
		
	    List<Map.Entry<Integer,Float>> list = new ArrayList<Map.Entry<Integer,Float>>(map.entrySet());
	    Collections.sort(list,new Comparator<Map.Entry<Integer,Float>>() {
	        @Override
	        public int compare(Entry<Integer,Float> o1,
	                Entry<Integer,Float> o2) {
	            return (int) (o2.getValue() - o1.getValue());
	        }
	    });
	    
	    List<QiImageSectionPoint> newPolygonPoints = new ArrayList<QiImageSectionPoint>();
	    for (Map.Entry<Integer,Float> entry : list) {
			for(int i = 0; i<polygonPoints.size(); i++){
				if (entry.getKey()==polygonPoints.get(i).getImageSectionId()) newPolygonPoints.add(polygonPoints.get(i));
			}
	    }
	    
	    return newPolygonPoints;
	}
	
	/**
	 * This method is used to draw all image section on Image except specificed one
	 * @param entryScreen
	 * @param imageName
	 * @param defect1
	 * @param defect2
	 * @param color
	 * @param imageSectionId
	 */
	private void drawAllImageSectionsExceptSelected(String entryScreen, String imageName, String defect1, String defect2, Paint color, final int imageSectionId){
		List<QiImageSectionPoint> polygonPoints = new ArrayList<QiImageSectionPoint>(getModel().showAllImageSectionsByFilter(entryScreen, imageName, defect1, defect2, getPart1TextFilter()));
		polygonPoints.removeIf(new Predicate<QiImageSectionPoint>() {
			@Override
			public boolean test(QiImageSectionPoint point) {
				return point.getId().getImageSectionId() == imageSectionId;
			}
		});
		if(!polygonPoints.isEmpty()){
			getPolygonList(reSeqPolygonListByArea(polygonPoints),color);
		}
		if(getView().getImageSections()!=null) {
			for(Polygon polygon : getView().getImageSections()) {
				int secId = Integer.parseInt(polygon.getId());
				if(secId != imageSectionId) {
					getView().getImagePane().getChildren().add(polygon);
				}
			}
		}
	}
	/**
	 * This method is used to retrieve polygons
	 */
	private void getPolygonList(List<QiImageSectionPoint> polygonPoints, final Paint color) {
		List<Double> allPointsList = new ArrayList<Double>();
		double resizeWidthPercent = ((getView().getScreenHeight() * 0.67)-500)/5;
		double resizeHeightPercent = ((getView().getScreenHeight() * 0.67)-500)/5;
		for(int i = 0; i<polygonPoints.size(); i++){
			final Polygon polygon = new Polygon();
			int imageSectionId = polygonPoints.get(i).getImageSectionId();
			if((i+1)<polygonPoints.size() && imageSectionId == polygonPoints.get(i+1).getImageSectionId()){
				allPointsList.add((double) polygonPoints.get(i).getPointX()+((polygonPoints.get(i).getPointX()*resizeWidthPercent)/100));
				allPointsList.add((double) polygonPoints.get(i).getPointY()+((polygonPoints.get(i).getPointY()*resizeHeightPercent)/100));
			}else{
				allPointsList.add((double) polygonPoints.get(i).getPointX()+((polygonPoints.get(i).getPointX()*resizeWidthPercent)/100));
				allPointsList.add((double) polygonPoints.get(i).getPointY()+((polygonPoints.get(i).getPointY()*resizeHeightPercent)/100));
				polygon.getPoints().setAll(allPointsList);
				polygon.setFill(Color.TRANSPARENT);
				polygon.setStroke(color);
				polygon.setStrokeWidth(getModel().getLineWidth());
				polygon.setId(imageSectionId+"");
				getView().getImageSections().add(polygon);
				allPointsList = new ArrayList<Double>();				
			
				final String entryScreenName = StringUtils.trimToEmpty(getView().getEntryScreenListView().getSelectedItem().getEntryScreen());
				final String inspectionPartName = getModel().findPartLocationComb(entryScreenName, imageSectionId).getInspectionPartName();
				if(parentController.matchWithCurrentPartSearch(inspectionPartName)){
					polygon.setStroke(Color.RED);
				}
				polygon.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						getView().getImagePane().setCursor(Cursor.HAND);
						if(!selectionSet.contains(polygon))
							polygon.setStroke(Color.RED);
						List<Integer> imageSectionIdList = new ArrayList<Integer>();
						imageSectionIdList.add(Integer.parseInt(polygon.getId()));
						String defect1 = getView().getPrimarySelectedDefect();
						String defect2 = getView().getSecondarySelectedDefect();
						List<QiImageSectionDto> plcList = getModel().findAllPartLocationCombByImageSections(entryScreenName, imageSectionIdList,defect1,defect2, getPart1TextFilter());
						StringBuilder toolTipBuilder = new StringBuilder();
						for(QiImageSectionDto plc : plcList) {
							toolTipBuilder.append(SOLID_CIRCLE+plc.getFullPartDesc()+"\n");
						}
						Tooltip tooltip = new Tooltip(toolTipBuilder.toString());
						Tooltip.install(polygon, tooltip);
					}
				});
				polygon.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						getView().getImagePane().setCursor(Cursor.DEFAULT);						
						if(selectionSet.contains(polygon)){
							return;
						}						
						if(!parentController.matchWithCurrentPartSearch(inspectionPartName)){
							polygon.setStroke(color);
						}
					}
				});
			}
		}
	}
	
	/**
	 * This method is used to remove all Image Sections
	 */
	public void removeAllImageSections(){
		if(getView().getImageSections()!=null){			
			getView().getImagePane().getChildren().removeAll(getView().getImageSections());
			getView().getImageSections().clear();
		}
	}
	/**
	 * This method is used to remove all Image Sections except the specified one
	 */
	private void removeImageSectionsExceptSelected(final int imageSectionId){
		if(getView().getImageSections()!=null){
			getView().getImagePane().getChildren().removeIf(new Predicate<Node>() {
				@Override
				public boolean test(Node node) {
					if(node instanceof Polygon) {
						int secId = Integer.parseInt(node.getId());
						return (imageSectionId != secId);
					}
					return false;
				}
			});
			getView().getImageSections().removeIf(new Predicate<Polygon>() {
				@Override
				public boolean test(Polygon node) {
					int secId = Integer.parseInt(node.getId());
					return (imageSectionId != secId);
				}
			});
		}
	}
	/**
	 * This method is used to remove highlight symbol for mouse pointer
	 */
	public void removeEllipse() {
		ObservableList<Node> imagePaneChildren = getView().getImagePane().getChildren();
		List<Node> ellipseList = new ArrayList<Node>();
		for(Node node : imagePaneChildren) {
			if(node instanceof Ellipse) {
				ellipseList.add(node);
			}
		}
		getView().getImagePane().getChildren().removeAll(ellipseList);
	}

	/**
	 * This method is used to remove highlight symbol for mouse pointer
	 */
	public void removeArc() {
		ObservableList<Node> imagePaneChildren = getView().getImagePane().getChildren();
		List<Node> arcList = new ArrayList<Node>();
		for(Node node : imagePaneChildren) {
			if(node instanceof Arc) {
				arcList.add(node);
			}
		}
		getView().getImagePane().getChildren().removeAll(arcList);
	}
	/**
	 * This is a private class for Image Section Selection
	 * @author L&T Infotech
	 */
	private class PolygonSelection {
		Rectangle rect;
		Pane group;

		public PolygonSelection(Pane group) {
			this.group = group;
			rect = new Rectangle( 0,0,0,0);
			rect.setStroke(Color.BLUE);
			rect.setStrokeWidth(1);
			rect.setStrokeLineCap(StrokeLineCap.ROUND);
			rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
			if(!parentController.getEventHandler()) {
				group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
				group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
			}
		}
		/**
		 * Mouse pressed listener
		 */
		EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					parentController.setPointX(event.getX());
					parentController.setPointY(event.getY());
					rect.setX(event.getX());
					rect.setY(event.getY());
					rect.setWidth(0);
					rect.setHeight(0);
					group.getChildren().add(rect);
				}
				event.consume();
			}
		};
		/**
		 * Mouse released listener
		 */
		EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (event.getButton() == MouseButton.PRIMARY) {
					clearStatusAndResponsibleLevelMessage(DefectEntryController.RESPONSIBLE_LVL_MSG_ID);
					Logger.getLogger().check("Clicked on Image at X="+event.getX()+",Y="+event.getY());
					String defect1 = getView().getPrimarySelectedDefect();
					String defect2 = getView().getSecondarySelectedDefect();
					QiDefectEntryDto entryScreen = getView().getEntryScreenListView().getSelectedItem();
					String entryScreenName = StringUtils.trimToEmpty(entryScreen.getEntryScreen());
					for(Node node : selectionSet) {
						Polygon polygon = (Polygon)node;
						polygon.setFill(Color.TRANSPARENT);
						if(StringUtils.isEmpty(defect1) && StringUtils.isEmpty(defect2))  {
							polygon.setStroke(Color.TRANSPARENT);
						} else {
							polygon.setStroke(getImageSectionColor());
						}
					}
					selectionSet.clear();
					polygonMap.clear();
					float area = Float.MAX_VALUE;
					Polygon smallerPolygon = null;
					for (Node node : group.getChildren()) {
						Point2D localPoint = new Point2D(rect.getX(), rect.getY());
						ImageSectionSelection select = new ImageSectionSelection(rect);
						if (node instanceof Polygon) {
							if (select.checkIntersects((Shape) node) && node.contains(localPoint)) {
								float currentArea = calPloygonArea((Polygon)node); 
								if (  currentArea < area ){
									smallerPolygon = (Polygon)node;
									area = currentArea;
								}
							}
						}
					}
					if (smallerPolygon != null) {
						polygonMap.put(smallerPolygon.getId(), smallerPolygon);
						selectionSet.add(smallerPolygon);
					}

					List<QiImageSectionDto> plcList = new ArrayList<QiImageSectionDto>();
					int partLocationId = 0;

					if(selectionSet.size()==1){
						int imageSecId = Integer.parseInt(selectionSet.iterator().next().getId());
						List<Integer> imageSectionIdList = new ArrayList<Integer>();
						imageSectionIdList.add(imageSecId);
						plcList = getModel().findAllPartLocationCombByImageSections(entryScreenName, imageSectionIdList,defect1,defect2, getPart1TextFilter());
						if(plcList.size()==1){
							partLocationId = plcList.get(0).getPartLocationId();
							Polygon polygon = (Polygon)polygonMap.get(imageSecId+"");
							polygon.setUserData(Integer.valueOf(partLocationId));
						}
					}
					if((selectionSet.size() > 1) || plcList.size()>1){
						openImageSectionSelectionDialog(entryScreenName,defect1,defect2);
					}
					removeEllipse();
					for(Node node : selectionSet) {
						Polygon polygon = (Polygon)node;
						polygon.setStroke(Color.BLUE);
						polygon.setStrokeWidth(2);
						/**
						 * Point highlight on user click
						 */
						Ellipse ellipse = new Ellipse(3,3);
						ellipse.setFill(Color.BLUE);
						ellipse.setCenterX(parentController.getPointX());
						ellipse.setCenterY(parentController.getPointY());
						getView().getImagePane().getChildren().add(ellipse);
					}
					addImageSectionListener();
					rect.setX(0);
					rect.setY(0);
					rect.setWidth(0);
					rect.setHeight(0);
					group.getChildren().remove(rect);
					Logger.getLogger().check("Items populated in Defect List : " + StringUtils.join(getView().getDefect1ListView().getItems(), ','));
				}
				event.consume();				
			}
			/**
			 * This method is used to open Image Section Dialog
			 */
			private void openImageSectionSelectionDialog(String entryScreenName, String defect1, String defect2) {
				int partLocationId;
				ImageSectionPolygonSelectionDialog polygonSelectionDialog = new ImageSectionPolygonSelectionDialog(getModel(),polygonMap,getPart1TextFilter());
				if (polygonSelectionDialog.showPolygonSelectionDialog(entryScreenName, defect1, defect2)) {
					String imageSectionId = polygonSelectionDialog.getImageSectionTablePane().getSelectedItem().getImageSectionId().toString();
					partLocationId = polygonSelectionDialog.getImageSectionTablePane().getSelectedItem().getPartLocationId();
					selectionSet.clear();
					Polygon polygon = (Polygon)polygonMap.get(imageSectionId);
					polygon.setUserData(Integer.valueOf(partLocationId));
					selectionSet.add(polygon);
				}else
					selectionSet.clear();
			}
			private float calPloygonArea(Polygon polygon) {
				ObservableList<Double> points = polygon.getPoints();
				int pointNum = (int)points.size() / 2;
				Point2D vertex[] = new Point2D[pointNum];
				double x = 0;
				double y = 0;
				for (int i = 0;i<points.size();i++) {
					if (i % 2 == 0) x = points.get(i);
					else { 
						y = points.get(i);
						vertex[(i-1)/2] = new Point2D(x,y);
					}
				}
				return calculate(vertex,pointNum);
				
			}
		};
	}
	/**
	 * This method is used to add repaired/non repaired symbol
	 */
	private void addStatusSymbol(String imageName){
		List<QiStationPreviousDefect> previousDefectList = getModel().findAllByProcessPoint();
		List<QiDefectResult> configuredDefectList = new ArrayList<QiDefectResult>();
		StringBuilder entryDept = new StringBuilder(StringUtils.EMPTY);
		Map<String, String> configuredPreviousDefect = new HashMap<String, String>();
		for(QiStationPreviousDefect qiStationPreviousDefect : previousDefectList ){
			configuredPreviousDefect.put(qiStationPreviousDefect.getId().getEntryDivisionId(), qiStationPreviousDefect.getOriginalDefectStatus() + "," + qiStationPreviousDefect.getCurrentDefectStatus());
			if(!entryDept.toString().equalsIgnoreCase(StringUtils.EMPTY))
				entryDept.append(",").append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
			else
				entryDept.append("'").append(qiStationPreviousDefect.getId().getEntryDivisionId()).append("'");
		}
		List<QiDefectResult> defectResults = new ArrayList<QiDefectResult>();
		if(entryDept!=null && !entryDept.toString().equalsIgnoreCase(StringUtils.EMPTY))
			defectResults=getModel().findAllByImageNameAndEntryDept(imageName, entryDept.toString());
		for(QiDefectResult qiDefectResult : defectResults ){
			for(Map.Entry<String, String>  map:configuredPreviousDefect.entrySet()){
				if(map.getKey().toString().equalsIgnoreCase(qiDefectResult.getEntryDept())){
					String statusString = map.getValue();
					int index = statusString.indexOf(",");
					short originalStatus = new Short(statusString.substring(0, index)).shortValue();
					short currentStatus = new Short(statusString.substring(index + 1)).shortValue();
					if (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == (short)DefectStatus.ALL.getId()
							|| (originalStatus == (short)DefectStatus.ALL.getId() && currentStatus == qiDefectResult.getCurrentDefectStatus()) 
							|| (currentStatus == (short)DefectStatus.ALL.getId() && originalStatus == qiDefectResult.getOriginalDefectStatus())
							|| (originalStatus == qiDefectResult.getOriginalDefectStatus() && currentStatus == qiDefectResult.getCurrentDefectStatus())) {
						configuredDefectList.add(qiDefectResult);
					}
				}
			}
		}
		double resizePercent = ((getView().getScreenHeight()*0.67)-500)/5;
		for(QiDefectResult qiDefectResult :configuredDefectList){
			if(getModel().getProductId().equals(qiDefectResult.getProductId())){
				int xPoint = (int) (qiDefectResult.getPointX()+((qiDefectResult.getPointX()*resizePercent)/100));
				int yPoint = (int) (qiDefectResult.getPointY()+((qiDefectResult.getPointY()*resizePercent)/100));
				String defectStatus = qiDefectResult.getCurrentStatus();
				String colorCode = getModel().findColorCodeByWriteupDeptAndProcessPointId(qiDefectResult.getWriteUpDept());
				String partDefectCombDesc = qiDefectResult.getPartDefectDesc();
				DefectImageUtil.drawSymbol(xPoint,yPoint,defectStatus,colorCode,partDefectCombDesc, getView().getImagePane());
			}
		}
	}

	/**
	 * This method is used to draw symbols for defects in current session
	 */
	public void drawCurrentSessionSymbol(String imageName){
		removeArc();
		double resizePercent = ((getView().getScreenHeight()*0.67)-500)/5;
		for(QiDefectResult qiDefectResult :getModel().getCachedDefectResultList()){
			if(getModel().getProductId().equals(qiDefectResult.getProductId())
					&& qiDefectResult.getImageName().equals(imageName) 
					&& qiDefectResult.getPointX()!=0 && qiDefectResult.getPointY()!=0){
				int xPoint = (int) (qiDefectResult.getPointX()+((qiDefectResult.getPointX()*resizePercent)/100));
				int yPoint = (int) (qiDefectResult.getPointY()+((qiDefectResult.getPointY()*resizePercent)/100));
				String colorCode = getModel().findColorCodeByWriteupDeptAndProcessPointId(qiDefectResult.getWriteUpDept());
				String partDefectCombDesc = qiDefectResult.getPartDefectDesc();
				DefectImageUtil.drawCurrentSessionSymbol(xPoint, yPoint, colorCode, partDefectCombDesc, getView().getImagePane());
			}
		}
	}
	
	/**
	 * This method Check configuration for Highlight existing image sections.
	 */
	private Paint getImageSectionColor() {
		QiStationConfiguration qiEntryStationConfigManagement=getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.HIGHLIGHT.getSettingsName());
		boolean isHighlighted = QiEntryStationConfigurationSettings.HIGHLIGHT.getDefaultPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;;
		if(qiEntryStationConfigManagement!=null){
			isHighlighted = qiEntryStationConfigManagement.getPropertyValue().equalsIgnoreCase(QiConstant.YES)?true:false;
		}
		if(isHighlighted) 
			return Color.web(getModel().getLineColor());
		return Color.TRANSPARENT;
	}
	
	/**
	 * This method is used to create Image Entry Screen
	 */
	public Node createImageEntryScreen(QiDefectEntryDto item, double size) {
		ImageView imageView = new ImageView();
		String tempArray[] = item.getEntryScreen().split("\\(");
		String entryScreenName = tempArray[0];
		HBox pane = new HBox();
		if(item.getImageData()!=null)
			imageView.setImage(new Image(new ByteArrayInputStream(item.getImageData())));
		Tooltip tooltip = new Tooltip(entryScreenName);
		tooltip.setStyle("-fx-font-weight: bold;-fx-font-size: "+getModel().getThumbnailHoverFontSize()+";");
		Tooltip.install(imageView, tooltip);
		imageView.setFitHeight(size * 0.85);
		imageView.setFitWidth(size * 0.85);
		imageView.setStyle("-fx-border-color: grey;");
		pane.getChildren().add(imageView);
		pane.getStyleClass().add("tile-list-view-textfield");
		pane.setAlignment(Pos.CENTER);
		pane.setMinSize(size, size);
		pane.setMaxSize(size, size);
		pane.setRotate((double)item.getOrientationAngle());
		return pane;
	}
	
	/**
	 * This method is used to add Image Entry Screen ListView Listener
	 */
	public void imageEntryScreenListener() {
		QiDefectEntryDto newValue = getView().getEntryScreenListView().getSelectedItem();
		EventBusUtil.publish(new ProgressEvent(100,"Loading image defect screens."));
		clearMessage();
		getView().resetResponsibilityCombobox();
		getView().resetIqsScoreSelection();
		if(newValue!=null) {
			String entryModel = getView().getEntryModelLabel().getText();
			getModel().generatePartDefectCombCache(newValue, newValue.isImage(), parentController.assignRealProblem, isMbpnDefectEntryLimitToProductIdPart(), entryModel);
			getView().getImagePane().setRotate((double)newValue.getOrientationAngle());
			if(!parentController.getEventHandler()) {
				new PolygonSelection(getView().getImagePane());
				parentController.setEventHandler(true);
			}
			selectionSet.clear();
			getView().clearFreqList();
			getView().getdefectPanelByImage().toFront();
			getView().getdefectPanelByImage().setVisible(true);
			getView().getDefectPanelByText().setVisible(false);
			getView().setLastDefectEnteredText(true);
			getView().getDefect1ListView().getItems().clear();
			List<String> defectList = null;
			
			int imageSectionId = 0, partLocationId = 0;
			if(parentController.isInSearchMode())  {
				String partFilter = parentController.getSearchedPartName();
				List<QiDefectResultDto> filteredDtoList = getPart1ListViewByPartFilter(newValue.getEntryScreen(), partFilter);
				if(filteredDtoList != null && !filteredDtoList.isEmpty())  {
					QiDefectResultDto dto = filteredDtoList.get(0);
					imageSectionId = dto.getImageSectionId();
					partLocationId = dto.getPartLocationId();
				}
			}

			defectList = getModel().findAllDefect1ByImageEntryScreen(newValue.getEntryScreen(), newValue.getImageName(),
					imageSectionId, StringUtils.EMPTY, partLocationId, StringUtils.EMPTY,
					parentController.isAssignRealProblem(), getPart1TextFilter());

			List<String> mostFreqUsedList = null;
			List<String> mergedList = null;
			if(getFreqUsedRangeInHours() > 0 && getFreqUsedListSize() > 0)  {
				mostFreqUsedList = getMostRecentlyUsed(newValue.getEntryScreen());
				if(mostFreqUsedList != null && !mostFreqUsedList.isEmpty())  {
					mergedList = mergeDefectList(mostFreqUsedList, defectList);
				}
			}
			if(mergedList != null && !mergedList.isEmpty())  {
				getView().getDefect1ListView().setItems(FXCollections.observableArrayList(mergedList));				
				getView().getDefect1ListView().scrollTo(0);
				getView().setMostFreqList(mostFreqUsedList);
				getView().setMostFreqUsed(true);
			}
			else  {
				getView().getDefect1ListView().setItems(FXCollections.observableArrayList(defectList));				
				getView().getDefect1ListView().scrollTo(0);
				getView().setMostFreqList(new ArrayList<String>());
				getView().setMostFreqUsed(false);
			}
			
			
			if(null!=newValue.getImageData()){
				getView().getDefectImageView().setImage(new Image(new ByteArrayInputStream(newValue.getImageData())));
			}

			String defect1 = getView().getPrimarySelectedDefect();
			String defect2 = getView().getSecondarySelectedDefect();
			removeEllipse();
			removeAllImageSections();
			if(parentController.isInSearchMode())  {
				drawAllImageSectionsByPartLocFilter(newValue.getEntryScreen(), parentController.getSearchedPartName(), Color.BLUE);
			} 
			else  {
				drawAllImageSections(newValue.getEntryScreen(),newValue.getImageName(),defect1,defect2,Color.TRANSPARENT);
			}
			if(!StringUtils.isEmpty(newValue.getImageName())){
				List<Node> childList = getView().getImagePane().getChildren();
				List<Node> polygonList = new ArrayList<Node>();
				for(Node node :childList){
					if(node instanceof Circle || node instanceof Line){
						polygonList.add(node);
					}
				}
				if(!polygonList.isEmpty())
					getView().getImagePane().getChildren().removeAll(polygonList);
				if(parentController.checkShowDefectConfig())
					addStatusSymbol(newValue.getImageName());
			}
			parentController.checkMandatoryFields();
			drawCurrentSessionSymbol(newValue.getImageName());
		}
		else {
			getModel().setDefectResult(null);
			parentController.setSelectedDefect(null);
			getView().getdefectPanelByImage().setVisible(false);
			getView().getDefectPanelByText().setVisible(false);
		}
	}
	
	public List<String> mergeDefectList(List<String> mostFreqUsedList, List<String> defectList)  {
		List<String> mergedList = new ArrayList<>();
		Collections.sort(mostFreqUsedList);
		Collections.sort(defectList);
		if(mostFreqUsedList != null && !mostFreqUsedList.isEmpty())  {
			for(int i = 0;  i < mostFreqUsedList.size(); i++)  {
				defectList.remove(mostFreqUsedList.get(i));
			}
		}
		mergedList.addAll(mostFreqUsedList);
		mergedList.addAll(defectList);
		return mergedList;
	}
	
	public List<String> getMostRecentlyUsed(String entryScreen)  {
		List<String> defectTypeNameList = new ArrayList<String>();
		List<QiMostFrequentDefectsDto> recentDefectList = getModel().findMostFrequentDefectsByProcessPointEntryScreenDuration(entryScreen, getFreqUsedBeginTimestamp());
		if(recentDefectList == null || recentDefectList.isEmpty())  return defectTypeNameList;
		int maxListSize = getFreqUsedListSize();
		int frequentDefectListSize = recentDefectList.size();
		for(int i = 0; i < 11 && i < maxListSize && i < frequentDefectListSize ; i++)  {
			QiMostFrequentDefectsDto dto = recentDefectList.get(i);
			if(dto != null)  {
				String defectName = QiDefectResultDto.combineTypeNames(dto.getPrimaryDefect(), dto.getSecondaryDefect());
				defectTypeNameList.add(defectName);
			}
		}
		return defectTypeNameList;
	}
	
	public int getFreqUsedListSize()  {
		
		String maxListSz = "";
		int maxSize = 0;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.MAX_MOST_FREQ_USED_SZ.getSettingsName());
		if(qiStationConfigEntry != null && qiStationConfigEntry.getPropertyValue() != null)  {
			maxListSz = qiStationConfigEntry.getPropertyValue();
		}
		if(StringUtils.isBlank(maxListSz))  {
			maxListSz = QiEntryStationConfigurationSettings.MAX_MOST_FREQ_USED_SZ.getDefaultPropertyValue();				
		}
		try {
			if(!StringUtils.isBlank(maxListSz))  {
				maxSize = Integer.parseInt(maxListSz);
			}
		} catch (NumberFormatException e) {
			maxSize = 0;
		}

		return maxSize;
	}

	private boolean isMbpnDefectEntryLimitToProductIdPart() {
		boolean isMbpnDefectEntryLimitToProductIdPart;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(QiEntryStationConfigurationSettings.MBPN_DEFECT_ENTRY_LIMIT_TO_PRODUCT_ID_PART.getSettingsName());
		if (qiStationConfigEntry != null) {
			isMbpnDefectEntryLimitToProductIdPart = "Yes".equalsIgnoreCase(qiStationConfigEntry.getPropertyValue()) ? true : false;
		} else {
			isMbpnDefectEntryLimitToProductIdPart = QiEntryStationConfigurationSettings.MBPN_DEFECT_ENTRY_LIMIT_TO_PRODUCT_ID_PART.getDefaultPropertyValue()
					.equalsIgnoreCase("Yes") ? true : false;
		}
		return isMbpnDefectEntryLimitToProductIdPart;
	}
	
	
	private Date getFreqUsedBeginTimestamp()  {
		
		int hours = getFreqUsedRangeInHours();
		if(hours == 0)  return null;
		Calendar cal = new GregorianCalendar();
		Date actualTs = null;
		cal.add(Calendar.HOUR_OF_DAY, -hours);
		actualTs = new Date(cal.getTimeInMillis());
		return actualTs;
	}
	
	
	private int getFreqUsedRangeInHours()  {
		
		String mostFreqUsedDuration = "";
		int hours = 8;
		QiStationConfiguration qiStationConfigEntry = getModel().findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.MOST_FREQ_USED_DURATION.getSettingsName());
		if(qiStationConfigEntry != null && qiStationConfigEntry.getPropertyValue() != null)  {
			mostFreqUsedDuration = qiStationConfigEntry.getPropertyValue();
			if(StringUtils.isBlank(mostFreqUsedDuration))  {
				mostFreqUsedDuration = QiEntryStationConfigurationSettings.MOST_FREQ_USED_DURATION.getDefaultPropertyValue();				
			}
		}
		try {
			if(!StringUtils.isBlank(mostFreqUsedDuration))  {
				Pattern p = Pattern.compile("([0-9]+)([dh])");
				Matcher m = p.matcher(mostFreqUsedDuration);
				if(m.matches())  {
					hours = Integer.parseInt(m.group(1));
					if(m.group(2).equalsIgnoreCase("d"))  {
						hours *= 24;
					}
				}				 
			}
		} catch (NumberFormatException e) {
			hours = 0;
		}
		return hours;
	}
	
	private DefectEntryView getView() {
		return parentController.getView();
	}
	
	private DefectEntryModel getModel() {
		return parentController.getModel();
	}
	
	private void clearMessage() {
		parentController.clearMessage();
	}
	
	private void clearStatusAndResponsibleLevelMessage(String newId) {
		parentController.clearStatusOnly();
		parentController.clearById(newId);
	}
	
	
	private String getPart1TextFilter() {
		return parentController.getPart1TextFilter();
	}

	public Set<Node> getSelectionSet() {
		return selectionSet;
	}

	public Map<String, Node> getPolygonMap() {
		return polygonMap;
	}
}
