package com.honda.galc.client.dc.mvc;

import static com.honda.galc.client.product.action.ProductActionId.COMPLETE;

import com.honda.galc.client.dc.property.DataCollectionPropertyBean;
import com.honda.galc.client.product.action.ProductActionId;
import com.honda.galc.client.product.process.AbstractProcessController;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date June 11, 2014
 */
public class ProcessInstructionController extends AbstractProcessController<ProcessInstructionModel, ProcessInstructionView> {

	private MCProductStructureDao productStructureDao;
	
	private MCProductStructureForProcessPointDao productStructureForProcessPointDao;
	
	public ProcessInstructionController(ProcessInstructionModel model, ProcessInstructionView view) {
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
	
	public MCProductStructureDao getMCProductStructureDao() {
		if(productStructureDao == null)
			productStructureDao = ServiceFactory.getDao(MCProductStructureDao.class);
		return productStructureDao;
	}
	
	public MCProductStructureForProcessPointDao getMCProductStructureForProcessPointDao() {
		if(productStructureForProcessPointDao == null)
			productStructureForProcessPointDao = ServiceFactory.getDao(MCProductStructureForProcessPointDao.class);
		return productStructureForProcessPointDao;
	}
	
	public ProductActionId[] getProductActionIds(){
		if (PropertyService.getPropertyBean(DataCollectionPropertyBean.class, getProcessPointId()).isDisplayCompleteButton() || 
			(getProductModel() != null && getProductModel().isTrainingMode() ) ) {
			return new ProductActionId[]{COMPLETE};
		}
		return new ProductActionId[]{};
	}
}
