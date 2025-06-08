package com.honda.galc.client.dc.processor;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;

/**
 * Engine processor to update table GAL143TBX & GAL131TBX
 * @author Max MacKay
 * @date Oct. 16, 2015
 */
public class EngineLoadProcessor extends OperationProcessor implements IOperationProcessor {

	public EngineLoadProcessor(DataCollectionController controller, MCOperationRevision operation) {
		super(controller, operation);
	} // end of constructor 
	
	@Override
	public void destroy() {
	}
	
	public boolean execute(InputData data){
		try{
			
			PartSerialScanData scandata;
			scandata = (PartSerialScanData) data;
		
			Frame frame = (Frame) getController().getProductModel().getProduct();
			frame.setEngineSerialNo(scandata.getSerialNumber());
			
			EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
			
			Engine engine = engineDao.findByKey(scandata.getSerialNumber());
			engine.setVin(frame.getProductId());
			
			
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			
			frameDao.update(frame);  //Update 143TBX
			engineDao.update(engine);//Update 131TBX
			
			getLogger().info("Married VIN " + frame.getProductId() + " to Engine " + engine.getProductId());
			return true;
		}
		catch(Exception ex){
			getLogger().error(ex, "Failed to marry Engine to VIN.");
			return false;
		}
	} // end of execute method
}  //end of EngineNumberOperationProcessor class
