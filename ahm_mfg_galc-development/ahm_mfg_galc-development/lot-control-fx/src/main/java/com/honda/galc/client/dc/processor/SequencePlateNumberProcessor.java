package com.honda.galc.client.dc.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.service.ServiceFactory;

public class SequencePlateNumberProcessor extends LotControlOperationProcessor {


	
	
	public SequencePlateNumberProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	}

	@Override
	public void destroy() {
	}
	
	
		
	public boolean execute(InputData data) {
		//Save the next sequence plate number for the model
		PartSerialScanData inputData = (PartSerialScanData) data;
		//save AFONsequence number 
		saveData(inputData.getSerialNumber());
		return true;
	}

	
	
	/**
	 * Prepare data that will be stored into dataBase
	 */
	public void saveData(String nextSequencePlateNumber){
		//update AF Sequence number for next car to reference to
		FrameDao dao = ServiceFactory.getDao(FrameDao.class);
		Frame frame =(Frame) getController().getProductModel().getProduct();
		frame.setAfOnSequenceNumber(Integer.valueOf(nextSequencePlateNumber));
		dao.update(frame); 
		
		completeOperation(false);
		
	}
	
	
	
	
}
