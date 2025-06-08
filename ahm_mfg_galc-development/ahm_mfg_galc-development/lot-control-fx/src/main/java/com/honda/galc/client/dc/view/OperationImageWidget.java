package com.honda.galc.client.dc.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import com.honda.galc.client.dc.mvc.ProcessImageModel;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.pdda.UnitImageDao;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class OperationImageWidget extends ProcessImageWidget<ProcessImageModel> {
	
	private static final String emptyImageUrl = "/resource/images/common/empty.png";

	public OperationImageWidget(ProcessImageModel model) {
		super(model);
	}

	public void initComponents() {
		this.setCenter(getMainPDDAImage());
		this.setId("operImgWidgetId");
		Logger.getLogger().check("OperationImageWidget has been loaded");
	}
	
	private GridPane getMainPDDAImage()  
	{
		List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();
    	
		String ProcessPointID = getModel().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getModel().getProductModel().getProductId();

		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,ProcessPointID);
    	double width = property.getOperationMainImageWidth();

		
		UnitImageDao installedPartDao = ServiceFactory.getService(UnitImageDao.class);
		pddaimage = installedPartDao.getUnitMainImage(ProductID,ProcessPointID);
    	
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
		Logger.getLogger().check("Unit main image has been loaded.");

		return imageGrid;
	}
}