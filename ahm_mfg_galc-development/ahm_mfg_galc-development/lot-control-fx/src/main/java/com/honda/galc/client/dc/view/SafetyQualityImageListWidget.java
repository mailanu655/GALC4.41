package com.honda.galc.client.dc.view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.honda.galc.client.dc.mvc.ProcessInstructionModel;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.exception.ReflectionException;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.pdda.UnitSafetyImageDao;
import com.honda.galc.dto.UnitOfOperation;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ReflectionUtils;

public class SafetyQualityImageListWidget extends ProcessInstructionWidget<ProcessInstructionModel> {

	private String mode = null;
	
	public SafetyQualityImageListWidget(ProcessInstructionModel model) {
		super(model);
	}

	public void initComponents() {
		this.setMode(PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString()));
		String ProcessPointID = getModel().getProductModel().getProcessPoint().getProcessPointId();
		String ProductID = getModel().getProductModel().getProductId();

		ScrollPane sp = new ScrollPane();
		VBox vboxInner = new VBox(10);

		vboxInner.setPadding(new Insets(5));
		VBox.setVgrow(vboxInner, Priority.ALWAYS);
		VBox.setVgrow(sp, Priority.ALWAYS);
		setViewportWidthHack(sp,125);
		sp.setStyle("-fx-font-size: 35px;-fx-background-color: transparent;");
        sp.setContent(vboxInner);
        sp.setFitToWidth(true);
        UnitSafetyImageDao pddaDao = ServiceFactory.getService(UnitSafetyImageDao.class);
    	List<UnitOfOperation> pddaimage = new ArrayList<UnitOfOperation>();
    	try {
			pddaimage = pddaDao.getUnitSafetyImageList(ProductID,ProcessPointID, getMode());
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (null != pddaimage) {
	    	for(int i = 0;i<pddaimage.size();i++) {
	    		final String maintenanceID = pddaimage.get(i).getPddaMaintenanceId();
	    		Button buttonUnit = UiFactory.createButton(pddaimage.get(i).getUnitNo(), "buttonUnit-" + i);
	    		buttonUnit.setStyle("-fx-font-size: 20;");
	    		buttonUnit.setOnAction(new EventHandler<ActionEvent>() {
		            //@Override
		            public void handle(ActionEvent e) {
		            	try {
							SafetyQualityImageView.showImage(null,maintenanceID);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
	    		});
	    		vboxInner.getChildren().add(buttonUnit);
	    	}
		}
		
		VBox vboxOuter = new VBox(10);
		vboxOuter.setAlignment(Pos.TOP_LEFT);
		Label label = UiFactory.createLabel("safetyQualityImage", "Safety Quality Image");
		label.setStyle("-fx-font-size: 15;");

   	    vboxOuter.getChildren().addAll(label, sp);
   	    vboxOuter.setMinWidth(Control.USE_PREF_SIZE);
		this.setCenter(vboxOuter);
	}
	
	
	public void setViewportWidthHack(ScrollPane sp, double val) {
		try { 
		   Double dval  = new Double(val);
		   ReflectionUtils.invoke(sp,"setPrefViewportWidth",dval );
		} catch (ReflectionException e) {
		    /* This will always fail on Java 7 */
		}
	}
	
	private String getMode() {
		return mode;
	}

	private void setMode(String mode) {
		this.mode = mode;
	} 
}