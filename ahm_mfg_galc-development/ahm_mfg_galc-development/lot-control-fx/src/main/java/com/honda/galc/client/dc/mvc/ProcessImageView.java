package com.honda.galc.client.dc.mvc;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.dc.view.CCPSheetImageView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.product.process.AbstractProcessView;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.pdda.UnitImageDao;
import com.honda.galc.dto.PddaUnitImage;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date June 11, 2014
 */
public class ProcessImageView extends AbstractProcessView<ProcessImageModel, ProcessImageController> {
	
	public ProcessImageView(MainWindow window) {
		super(ViewId.PROCESS_IMAGE_VIEW, window);
	}

	@Override
	public void start() {
		loadProcessImageView();
	}

	@Override
	public void reload() {}

	@Override
	public void initView() {}
	
	
	private void loadProcessImageView() {
		HBox hbox = new HBox(10);
		
		Node view = getOpView(ViewId.OPERATION_IMAGE_WIDGET);
		
		HBox.setHgrow(view,Priority.ALWAYS);
		
		hbox.getChildren().add(view);
        
	    setCenter(hbox);
		showCCPView();
	}
	
	private void showCCPView() {
		String ProcessPointID = getProductModel().getProcessPointId();
		String ProductID = getProductModel().getProductId();
		if (getProductModel().isDisplayCCP()){
			UnitImageDao unitImage = ServiceFactory.getService(UnitImageDao.class);
	    	List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();
	    	pddaimage = unitImage.getUnitCCPImages(ProductID,ProcessPointID);
    		CCPSheetImageView.showImage(ClientMainFx.getInstance().getStage(),pddaimage,getProductModel());
		}
	}
	
	private Node getOpView(ViewId viewId) {
		try {
			Class<?> viewClass = Class.forName(viewId.getViewClass());
			if(viewClass != null){
				return (Node)ReflectionUtils.createInstance(viewClass, new Object[]{getModel()});
			}else {
				setErrorMessage("View class does not exsit: " + viewId.getViewClass());
			}
		} catch(Exception ex) {
			Logger.getLogger().error(ex, "An error occurred while creating ProcessImageView");
		}
		return null;
	}
}
