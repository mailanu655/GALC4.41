package com.honda.galc.client.dc.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClickerControl;
import com.honda.galc.client.dc.common.ClientDeviceResolution;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.pdda.UnitDao;
import com.honda.galc.dao.pdda.UnitImageDao;
import com.honda.galc.data.cache.OperationInfoCache;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.dto.UnitOfOperationDetails;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class OperationInfoFXBase extends VBox  {
	
	private String fontsize="";
	private double width;
	private List<Image> imageList = new ArrayList<Image>();
	private Pagination pagination;
	private VBox vboxInner ;
	private VBox vboxInnerPrevious;
	private ScrollPane sp ;
	private GridPane gridPane;
	private int minUnitImageHeight;
	private boolean isZoomIn=false;
	private final Border boxBorder = new Border( new BorderStroke(javafx.scene.paint.Color.LIGHTGRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT));
	private final Border criticalBorder = new Border( new BorderStroke(javafx.scene.paint.Color.RED,BorderStrokeStyle.DASHED,CornerRadii.EMPTY,new BorderWidths(5)));
	
	
	public static final String id =  "OperationInfoFXBase";
	
	public OperationInfoFXBase(int width,int maintenanceId,String processPointID,String productID,boolean isTrainingMode) {
		init(width,maintenanceId,processPointID,productID,isTrainingMode);
 		setId(id);
    }


	private void init(int awidth, int maintenanceId,String processPointID,String productID,boolean isTrainingMode) {
		
		
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,processPointID);
		this.fontsize = property.getOperationInfoFontSize();
		
		width = Double.parseDouble(ClientDeviceResolution.getClientResolutionProperty("OPERATION_INFO_WIDTH_","width",String.valueOf(awidth)));
		this.fontsize = ClientDeviceResolution.getClientResolutionProperty("OPERATION_INFO_FONT_SIZE_","font",fontsize);
		
		this.minUnitImageHeight = property.getMinUnitImageHeight();
		String height = property.getOperationInfoHeight();
		String oddRow = String.format("-fx-font-size: %s;-fx-background-color: white;-fx-label-padding:4;",fontsize);
		String evenRow = String.format("-fx-font-size: %s;-fx-background-color: whitesmoke; -fx-label-padding: 4;",fontsize);
		String oddRowBold = String.format("-fx-font-size: %s;-fx-font-weight: bold; -fx-background-color: white;-fx-label-padding:4;",fontsize);
		String evenRowBold = String.format("-fx-font-size: %s;-fx-font-weight: bold;-fx-background-color: whitesmoke;-fx-label-padding:4;",fontsize);
        String critical = String.format("-fx-background-color: red;-fx-text-fill:white;-fx-alignment: CENTER; -fx-font-weight: bold;-fx-font-size:%s",fontsize);
        
		String displayRow;
		
		if (isTrainingMode) {
			displayRow = property.getOperationInfoDisplayRowForTraining();
		} else {
			displayRow = property.getOperationInfoDisplayRow();
		}
		
		if (StringUtils.isEmpty(displayRow)) {
			displayRow = "QualityPoint,SpecialControl,ReactionPlan,WorkingPoint,WorkingPointDetail,ImpactPoint,ControlMethod,TightTorque,AuxillaryMaterial";// 
		}
		
	//	displayRow = "OperationDesc,QualityPoint,SpecialControl,ReactionPlan,WorkingPoint,WorkingPointDetail,ImpactPoint,OperationDesc,ControlMethod,TightTorque,OperationalDesc";
		displayRow = displayRow.replaceAll("(OperationDesc,|,OperationDesc)","");
		displayRow = String.format("title1,OperationDesc,%s,Images",displayRow); // Always include OperationalDesc and include as second field
		
		
		String[] rowitems = displayRow.split(",");
		String cssStyle = "";
		String cssStyleTitle = "";
		
		setAlignment(Pos.TOP_CENTER);
		
		vboxInner = new VBox();
		sp = new ScrollPane();
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
        sp.setMaxHeight(Integer.parseInt(height));
        sp.setPrefHeight(Integer.parseInt(height));
        sp.setContent(vboxInner);
        
		gridPane = new GridPane();
		gridPane.setSnapToPixel(true);
		gridPane.setPrefWidth(width);
		gridPane.setPadding(new Insets(4));

		
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(15);
		column1.setHgrow(Priority.ALWAYS);
		ColumnConstraints column2 = new ColumnConstraints();

		column2.setHgrow(Priority.ALWAYS);   // setFillWidth true
		//column2.setPercentWidth(80);
		gridPane.getColumnConstraints().addAll(column1, column2);
		
		UnitOfOperationDetails unitOfOperation=null; 

		OperationInfoCache cache = OperationInfoCache.getInstance();
		
		Element item = cache.get(maintenanceId,processPointID,productID);
		
		if (item != null) {
			unitOfOperation = (UnitOfOperationDetails)item.getObjectValue();
		} else {
			unitOfOperation = ServiceFactory.getService(UnitDao.class).getUnitOfOperationDetails(maintenanceId,processPointID,productID);
			cache.put(maintenanceId,processPointID,productID,unitOfOperation);
		}
		
		if (null==unitOfOperation) {
			Label nodata = UiFactory.createLabel("nodata", "PDDA Instructions cannot be found for this model type.");
			nodata.setStyle("-fx-font-weight: bold; -fx-label-padding:20;");
			getChildren().add(nodata);
			return;
		}
		
		if (isCriticalOperation(unitOfOperation)) {
			gridPane.setBorder(criticalBorder);
		}
				
		for(int i = 0;i <rowitems.length; i++) {
			if (i%2 == 0) {
				cssStyle= evenRow;
				cssStyleTitle = evenRowBold;
		    } else {
				cssStyle= oddRow;
				cssStyleTitle = oddRowBold;
			}
				  
			Node leftLabel = UiFactory.createLabel("leftLabel");
			Node rightLabel = UiFactory.createLabel("rightLabel");
			
		 	if (rowitems[i].equals("title1") ) {
		 	
		 	    GridPane titleGrid = new GridPane();
				ColumnConstraints constaintTwenty = new ColumnConstraints();
				constaintTwenty.setPercentWidth(20);
							
				titleGrid.getColumnConstraints().addAll(constaintTwenty, constaintTwenty,constaintTwenty,constaintTwenty,constaintTwenty);					
				Label unitNoTitlelabel = getLabel("Unit Number",cssStyle);
				
				Label unitNolabel = getLabel(unitOfOperation.getUnitNo(),cssStyle);
				Label processNoTitlelabel = getLabel("Process Number",cssStyle);
				
				Label processNolabel = getLabel(unitOfOperation.getProcessNo(),cssStyle);
				processNolabel.setStyle("-fx-font-size:"+fontsize+";");
				processNolabel.setBorder(boxBorder);

				Label criticallabel = getLabel("",cssStyle);
				if (isCriticalOperation(unitOfOperation)) {
					criticallabel =getLabel(unitOfOperation.getUnitRank(),critical);
				}
				
				titleGrid.addRow(1, unitNoTitlelabel, unitNolabel,processNoTitlelabel ,processNolabel , criticallabel);
				titleGrid.setStyle(cssStyle);
				
				leftLabel =  titleGrid;
				GridPane.setColumnSpan(leftLabel, 2);
				GridPane.setHgrow(leftLabel, Priority.ALWAYS);
			 } else  if ( rowitems[i].equals("OperationDesc") ) {
				  leftLabel = getLabel(unitOfOperation.getUnitOperationDesc(), cssStyleTitle);
				  GridPane.setColumnSpan(leftLabel, 2);
				  GridPane.setHgrow(leftLabel, Priority.ALWAYS);
			 } else if ( rowitems[i].equals("UnitMaintDate") ) {
				  rightLabel = getLabel(unitOfOperation.getMaintDateVal(), cssStyle);
			 	  leftLabel = getLabel("Maint. Date",cssStyle);
			 } else if ( rowitems[i].equals("QualityPoint") ) {
				  rightLabel = getLabel(unitOfOperation.getQualityPoint(), cssStyle);
			 	  leftLabel = getLabel("Quality Point",cssStyle);
			 } else if ( rowitems[i].equals("SpecialControl") ) {
				  rightLabel = getLabel(unitOfOperation.getSpecialControl(), cssStyle);
			 	  leftLabel = getLabel("Special Control",cssStyle);
			 } else if ( rowitems[i].equals("ReactionPlan") ) {
				  rightLabel = getLabel(unitOfOperation.getReactionPlan(), cssStyle);
			 	  leftLabel = getLabel("Reaction Plan",cssStyle);
			 } else if ( rowitems[i].equals("WorkingPoint") ) {
				  rightLabel = getLabel(unitOfOperation.getWorkPtDescText(), cssStyle);
			 	  leftLabel = getLabel("Working Point",cssStyle);
			 } else if ( rowitems[i].equals("WorkingPointDetail") ) {
				  rightLabel = getLabel(unitOfOperation.getWorkPtDetail(), cssStyle);
			 	  leftLabel = getLabel("Working Point Detail",cssStyle);
			 } else if ( rowitems[i].equals("ImpactPoint") ) {
				  rightLabel = getLabel(unitOfOperation.getImpactPoint(), cssStyle);
			 	  leftLabel = getLabel("Impact Point",cssStyle);
			 } else if ( rowitems[i].equals("ControlMethod") ) {
				  rightLabel = getLabel(unitOfOperation.getControlMethod(), cssStyle);
			 	  leftLabel = getLabel("Control Method",cssStyle);
			 } else if ( rowitems[i].equals("TightTorque") ) {
				  GridPane torqueGrid = new GridPane();
				  ColumnConstraints constaintTwoThird = new ColumnConstraints();
				  constaintTwoThird.setPercentWidth(60);
				  ColumnConstraints constraintTwenty = new ColumnConstraints();
				  constraintTwenty.setPercentWidth(15);
				  torqueGrid.getColumnConstraints().addAll(constaintTwoThird, constraintTwenty,constraintTwenty);
				  
				  Label torqVal = getLabel(StringUtils.defaultIfBlank(unitOfOperation.getTrqCharVal(),"None"),cssStyle);
				  Label toolLab = getLabel("Tool",cssStyle);
			      Label toolVal = getLabel(StringUtils.defaultIfBlank(unitOfOperation.getTool(),"None"),cssStyle);
			      torqueGrid.addRow(1,torqVal, toolLab, toolVal);
			      rightLabel = torqueGrid;
			      leftLabel = getLabel("Tight Torque",cssStyle);
			  } else if ( rowitems[i].equals("AuxillaryMaterial") ) {
				  GridPane torqueGrid = new GridPane();
				  ColumnConstraints constaintTwoThird = new ColumnConstraints();
				  constaintTwoThird.setPercentWidth(60);
				  ColumnConstraints constraintTwenty = new ColumnConstraints();
				  constraintTwenty.setPercentWidth(20);
				  torqueGrid.getColumnConstraints().addAll(constaintTwoThird, constraintTwenty,constraintTwenty);
				  
				  Label auxMatVal = getLabel(StringUtils.defaultIfBlank(unitOfOperation.getAuxMatDescText(),"None"),cssStyle);
				  Label auxMSDSLab = getLabel("MSDS",cssStyle);
			      Label auxMSDSVal = getLabel(StringUtils.defaultIfBlank(unitOfOperation.getAuxMsdsNo(),"None"),cssStyle);
			      torqueGrid.addRow(1,auxMatVal, auxMSDSLab, auxMSDSVal);
			      rightLabel = torqueGrid;
			      leftLabel = getLabel("Aux Material",cssStyle);
			  } else if ( rowitems[i].equals("Images") ) {
				  leftLabel = getPDDAImage(maintenanceId);
				  leftLabel.setStyle(cssStyle);
				  GridPane.setColumnSpan(leftLabel, 2);
				  GridPane.setHalignment(leftLabel, HPos.CENTER);
			  } else {
				  Logger.getLogger().warn("Skipping unknown field title " + rowitems[i] );
				  continue;
			  }
				
  		 gridPane.addRow(i, leftLabel, rightLabel);
  		}
		
		vboxInner.getChildren().addAll(gridPane);
		getChildren().add(sp);
	}

	

	private boolean isCriticalOperation(UnitOfOperationDetails unitOfOperation ) {
	    return null != unitOfOperation.getUnitRank() &&unitOfOperation.getUnitRank().length() > 0; 
	}
		
	private GridPane getPDDAImage(int maintenanceId)  
	{
		List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();
        OperationInfoCache cache = OperationInfoCache.getInstance();
		
    	Element item = cache.get(maintenanceId);
    	
    	if (item != null) {
    		pddaimage = (List<PddaUnitImage>) item.getObjectValue();
    	} else {
		  try {
			UnitImageDao installedPartDao = ServiceFactory.getService(UnitImageDao.class);
			pddaimage = installedPartDao.findAllUnitImages(maintenanceId);
    		cache.put(maintenanceId,pddaimage);
		  } catch (NumberFormatException e) {
			e.printStackTrace();
		  }
    	}
    	
		final GridPane imageGrid = new GridPane();
		imageGrid.setPrefWidth(width);
		//Adding Bindings in Platform.runLater to fix unresponsive scrollbar layout
		Platform.runLater(new Runnable() {
			public void run() {
				imageGrid.maxHeightProperty().bind(Bindings.max(minUnitImageHeight, sp.heightProperty().subtract(gridPane.heightProperty())));
			}
		});

		if ( null != pddaimage ) {
			imageList = new ArrayList<Image>();
			for(int i = 0;i<pddaimage.size();i++) {
				final Image image = new Image(new ByteArrayInputStream(pddaimage.get(i).getImage()));
				imageList.add(image);
				ImageView iv = new ImageView();
				iv.setPreserveRatio(true);
				iv.setSmooth(true); 
				iv.setCache(true);
				//Setting Fit width based on number of images
	    		//Binding image height
	    		iv.fitWidthProperty().bind(imageGrid.prefWidthProperty().divide(pddaimage.size()));
	    		iv.fitHeightProperty().bind(imageGrid.maxHeightProperty());
	    		
				iv.setImage(image);
				iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
			        //@Override
			        public void handle(MouseEvent t) {
			        		zoomIn();
			        }
			    });

				HBox hbox = new HBox(); 
				HBox.setMargin(iv, new Insets(1));
				hbox.setBorder(boxBorder);
				hbox.getChildren().add(iv);
				imageGrid.add(hbox,i, 0); 
				Logger.getLogger().check("Unit Image " + pddaimage.get(i).getUnitImage().trim() + " Loaded");
			}
		}
		return imageGrid;
	}
	
	private Label getLabel(String title,String style) {
		Label label = UiFactory.createLabel("title", title);
	    label.setMaxWidth(Double.MAX_VALUE);
	    label.setMaxHeight(Double.MAX_VALUE);
		label.setWrapText(true);
	    label.setStyle(style);
		label.setBorder(boxBorder);
        return label;
	}
	
	public void showNext() {
		if (!zoomIn() && pagination != null) {
			pagination.setCurrentPageIndex(pagination.getCurrentPageIndex() +1);
		}
	}
	
	
	public void showPrevious() {
		if(!zoomIn() && pagination != null) {
		   pagination.setCurrentPageIndex(pagination.getCurrentPageIndex() -1);
		}
	}
	
	public void zoomOut() {
		getStyleClass().remove(ClickerControl.HIGHLIGHT_STYLE);
		getStyleClass().add(ClickerControl.NORMAL_STYLE);

		if (isZoomIn) {
		  vboxInner = vboxInnerPrevious;
		  sp.setContent(vboxInner);
		  isZoomIn = false;
		}
	}
 	
	public boolean zoomIn() {
		
		getStyleClass().add(ClickerControl.HIGHLIGHT_STYLE);
		getStyleClass().remove(ClickerControl.NORMAL_STYLE);
		
		if (!isZoomIn && imageList.size() > 0) {
			
			final Bounds bounds = sp.getViewportBounds();
			pagination = new Pagination(imageList.size());
			pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
			pagination.setMaxHeight(bounds.getHeight());
			pagination.setMaxWidth(bounds.getWidth());
			pagination.setStyle("-fx-page-information-visible:false;");

			pagination.setPageFactory(new Callback<Integer, Node>() {
				public Node call(Integer index) {
					ImageView imageView = new ImageView(imageList.get(index));
					imageView.setPreserveRatio(true);
					imageView.fitHeightProperty().set(bounds.getHeight());
					imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				        //@Override
				        public void handle(MouseEvent t) {
				        		zoomOut();
				        }
				    });

					return imageView;
				}
			});

			vboxInnerPrevious = vboxInner;
			vboxInner = new VBox();
			vboxInner.getChildren().add(pagination);
			sp.setContent(vboxInner);
			isZoomIn = true;
			return true;
		} else {
		   return false;
		}
	}
	
	
 
}