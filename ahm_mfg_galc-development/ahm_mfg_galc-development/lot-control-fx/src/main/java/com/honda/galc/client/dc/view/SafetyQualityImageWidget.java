package com.honda.galc.client.dc.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.honda.galc.client.dc.processor.OperationProcessor;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class SafetyQualityImageWidget extends OperationView {

	private static final long serialVersionUID = 1L;
	//private PDDAPropertyBean property;

	public SafetyQualityImageWidget(OperationProcessor processor) {
		super(processor);
		
	}

	public void initComponents() {
		
		//get parameter from property
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		final double width = Double.parseDouble(property.getSafetyQualityImageWidth());

		// load the image
		 final Image image = new Image("resource/com/honda/galc/client/fx/testImage.JPG");
 
		 // simple displays ImageView the image as is
		 final ImageView iv = new ImageView();
		 iv.setFitWidth(500);
		 iv.setPreserveRatio(true);
		 iv.setSmooth(true); 
		 iv.setCache(true); 
		 iv.setImage(image);
		 
		 //Configuration control
		 if (null!=property.getSafetyQualityImageWidth()) iv.setFitWidth(width);
		 //if (null!=property.getSafetyQualityImageHeight()) iv.setFitHeight(Double.parseDouble(property.getSafetyQualityImageHeight()));

		 iv.setPreserveRatio(true);
		 iv.setSmooth(true); 
		 iv.setCache(true); 
		 iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	            //@Override
	            public void handle(MouseEvent me) {
	            	if (image.getWidth()!=iv.getFitWidth()) iv.setFitWidth(image.getWidth()); 
	            	else iv.setFitWidth(width);
	            }
	        });
		 
		 this.setCenter(iv);
	}

	protected void init() {
	}

}