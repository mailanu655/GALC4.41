package com.honda.galc.client.dc.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.KeypadEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.pdda.UnitImageDao;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class OperationInfoWidgetFX extends OperationView {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	private static final String emptyImageUrl = "/resource/images/common/empty.png";

	
	private OperationInfoFXBase operationInfoDelegate;
	private Node dataCollectionPanel = null;

	public OperationInfoWidgetFX(OperationProcessor processor) {
		super(processor);
		cleanup();
	}
	
	private void cleanup() {
		for (Object  o : EventBusUtil.findListenersOfType(this.getClass())   ) {
			if ( o != this ) {
				EventBusUtil.unregister(o);
			}
		}
	}

	public void initComponents() {
		String ProcessPointID = getProcessor().getController().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getProcessor().getController().getProductModel().getProductId();
		boolean isTrainingMode = getProcessor().getController().getProductModel().isTrainingMode();

		int maintID = getProcessor().getController().getModel().getCurrentOperation().getApprovedUnitMaintenanceId();
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,ProcessPointID);
		String width = property.getOperationInfoWidth();
		if (property.isDisplaySingleImage()) {
			setCenter(getSinglePDDAImage(property.isDisplayUnitSingleImage(),maintID));
		} else {
			operationInfoDelegate = new OperationInfoFXBase(Integer.parseInt(width), maintID,ProcessPointID,ProductID,isTrainingMode);
			this.setCenter(operationInfoDelegate);
		}
	}
	
	@Override
	public Node getDataCollectionPanel() {
		if (dataCollectionPanel == null) {
			if (getOperation().getType().equals(OperationType.INSTRUCTION) ||
					getOperation().getType().equals(OperationType.GALC_INSTRUCTION) ||
					getOperation().getType().equals(OperationType.GALC_AUTO_COMPLETE) ||
					getOperation().getType().equals(OperationType.GALC_MADE_FROM)) {
				dataCollectionPanel = new InstructionViewWidget(getProcessor());
			} else {
				dataCollectionPanel = new LotControlOperationView(getProcessor());
			}
			this.setRight(dataCollectionPanel);
		}
		return dataCollectionPanel;
	}

	private GridPane getSinglePDDAImage(boolean isDisplayUnitSingleImage,int maintID)  
	{
		List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();
		String ProcessPointID = getProcessor().getController().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getProcessor().getController().getProductModel().getProductId();

		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,ProcessPointID);
    	double width = property.getOperationSingleImageWidth();
		
		UnitImageDao installedPartDao = ServiceFactory.getService(UnitImageDao.class);
		if (isDisplayUnitSingleImage) pddaimage = installedPartDao.findAllUnitImages(maintID);
		else pddaimage = installedPartDao.getUnitMainImage(ProductID,ProcessPointID);
    	
		GridPane imageGrid = new GridPane();
		Image image;
		if ( null != pddaimage ) image = new Image(new ByteArrayInputStream(pddaimage.get(0).getImage()));
		else image = new Image(emptyImageUrl);

		ImageView iv = new ImageView();
		iv.setPreserveRatio(true);
		iv.setSmooth(true); 
		iv.setCache(true);
		iv.setFitWidth(width);
		iv.setImage(image);
		HBox hbox = new HBox(); 
		HBox.setMargin(iv, new Insets(1));
		hbox.getChildren().add(iv);
		imageGrid.add(hbox,0, 0); 
		imageGrid.setAlignment(Pos.CENTER);
		Logger.getLogger().check("Unit single image has been loaded.");

		return imageGrid;
	}

	
	@Subscribe
	public void handle(KeypadEvent event) {
		
		if (operationInfoDelegate == null) {
			return;
		}
		
		switch (event.getEventType()) {
			case KEY_LEFT:
				if (!event.isNavigatorSelected()) {
				  operationInfoDelegate.showNext();
				}
				break;

			case KEY_RIGHT:
				if (!event.isNavigatorSelected()) {
				  operationInfoDelegate.showPrevious();
				}
				break;

			case KEY_TOGGLEPANE:
				if (event.isNavigatorSelected()) {
					operationInfoDelegate.zoomOut();
				} else {
					operationInfoDelegate.zoomIn(); 
				}
				break;

			case KEY_COMPLETE:
				break;
		default:
			break;
		}
	}
}