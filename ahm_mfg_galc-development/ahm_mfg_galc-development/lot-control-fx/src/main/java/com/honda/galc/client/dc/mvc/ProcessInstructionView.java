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
public class ProcessInstructionView extends AbstractProcessView<ProcessInstructionModel, ProcessInstructionController> {
	
	public ProcessInstructionView(MainWindow window) {
		super(ViewId.PROCESS_INSTRUCTION_VIEW, window);
	}

	@Override
	public void start() {
		loadProcessInstructionView();
	}

	@Override
	public void reload() {}

	@Override
	public void initView() {}
	
	
	private void loadProcessInstructionView() {
		HBox hbox = new HBox(10);
		
		Node view1 = getOpView(ViewId.SAFETY_QUALITY_IMAGE_LIST_WIDGET);
		Node view2 = getOpView(ViewId.OPERATION_LIST_WIDGETFX);
		
		HBox.setHgrow(view1,Priority.SOMETIMES);
		HBox.setHgrow(view2,Priority.ALWAYS);
		
		hbox.getChildren().addAll(view1,view2);
        
	    setCenter(hbox);
		showCCPView();
	}
	
	private void showCCPView() {
		String ProcessPointID = getProductModel().getProcessPointId();
		String ProductID = getProductModel().getProductId();
		if (getProductModel().displayCCP()){
			UnitImageDao unitImage = ServiceFactory.getService(UnitImageDao.class);
	    	List<PddaUnitImage> pddaimage = new ArrayList<PddaUnitImage>();
	    	pddaimage = unitImage.getUnitCCPImages(ProductID,ProcessPointID);
    		CCPSheetImageView.showImage(ClientMainFx.getInstance().getStage(),pddaimage,getProductModel());
    		if (!getProductModel().isTrainingMode()) getProductModel().broadcastDataContainer("3");
		}
	}
	
	private Node getOpView(ViewId viewId) {
		try {
			Class<?> viewClass = Class.forName(viewId.getViewClass());
			if(viewClass != null){
				return (Node)ReflectionUtils.createInstance(viewClass, new Object[]{getModel()});
			}else {
				setErrorMessage("View class does not exisit: " + viewId.getViewClass());
			}
		} catch(Exception ex) {
			Logger.getLogger().error(ex, "Problem creating Process Instruction View");
		}
		return null;
	}
}
