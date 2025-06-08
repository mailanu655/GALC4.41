package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;

/**
 * @author Subu Kathiresan
 * @date Jun 16, 2014
 */
public class LotControlOperationProcessor extends OperationProcessor implements IOperationProcessor{
	
	public LotControlOperationProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	@Override
	public void destroy() {
	}
	
	public boolean execute(InputData data) {
		return true;
	}
}
