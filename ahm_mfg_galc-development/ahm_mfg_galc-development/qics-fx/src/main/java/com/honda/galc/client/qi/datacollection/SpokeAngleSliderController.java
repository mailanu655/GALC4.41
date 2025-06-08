package com.honda.galc.client.qi.datacollection;

import static com.honda.galc.client.product.action.ProductActionId.CANCEL_DIRECT_PASS;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.qi.base.AbstractQiProcessController;
import com.honda.galc.client.qi.base.QiProcessModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class SpokeAngleSliderController extends AbstractQiProcessController<QiProcessModel, SpokeAngleSliderPanel> implements EventHandler<ActionEvent> {

	public SpokeAngleSliderController(QiProcessModel model, SpokeAngleSliderPanel view) {
		super(model, view);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initializeListeners() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initEventHandlers() {
		// TODO Auto-generated method stub
	}	
	
	public ProductActionId[] getProductActionIds(){
		return new ProductActionId[]{CANCEL_DIRECT_PASS};
	}
} 
