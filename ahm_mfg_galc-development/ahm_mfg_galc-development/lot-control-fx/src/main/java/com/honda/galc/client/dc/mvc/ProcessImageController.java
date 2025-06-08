package com.honda.galc.client.dc.mvc;

import static com.honda.galc.client.product.action.ProductActionId.COMPLETE;

import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.process.AbstractProcessController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date June 11, 2014
 */
public class ProcessImageController extends AbstractProcessController<ProcessImageModel, ProcessImageView> {

	public ProcessImageController(ProcessImageModel model, ProcessImageView view) {
		super(model, view);
		setProcessType(ProcessType.PROCESS_INSTRUCTION);
		EventBusUtil.register(this);
		init();
	}
	
	protected void init() {}

	@Override
	public void initEventHandlers() {
	}

	@Override
	public void prepareExecute() {
		super.prepareExecute();
	}
	
	public boolean isProcessInstruction() {
		return true;
	}
	
	public ProductActionId[] getProductActionIds(){
		if (PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProcessPointId()).isDisplayCompleteButton() || 
			(getProductModel() != null && getProductModel().isTrainingMode() ) ) {
			return new ProductActionId[]{COMPLETE};
		}
		return new ProductActionId[]{};
	}
}
