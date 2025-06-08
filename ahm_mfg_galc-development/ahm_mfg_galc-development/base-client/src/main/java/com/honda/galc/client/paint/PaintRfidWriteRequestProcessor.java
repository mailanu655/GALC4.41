/**
 * 
 */
package com.honda.galc.client.paint;


import java.util.List;

import com.honda.galc.client.device.plc.IPlcDataReadyEventProcessor;
import com.honda.galc.client.events.PaintRfidWriteRequest;
import com.honda.galc.client.headless.PlcDataReadyEventProcessorBase;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.enumtype.DeviceTagType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 29, 2012
 */
public class PaintRfidWriteRequestProcessor extends PlcDataReadyEventProcessorBase implements IPlcDataReadyEventProcessor<PaintRfidWriteRequest> {


	public synchronized boolean execute(PaintRfidWriteRequest deviceData) {

		try {
			String errorCode = "";
			String vin = "";
			String prodLotSubString = "";
			String mtoc = "";
			String lotSize = "";
			String model = "";
			String sequenceNumber = "";

			model = deviceData.getModel().trim();
			sequenceNumber = deviceData.getSequenceNumber().trim();
			getLogger().info("Received Model:"+model);
			getLogger().info("Received Sequence Number:"+sequenceNumber);


			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			List<Frame> list = frameDao.findByModelAndSeqNumber(model,sequenceNumber);

			if (list.size() == 0) {
				errorCode = "1";
				getLogger().info("Could not find any products for provided Model and Sequence Number combination");
			} else if (list.size() > 1) {
				errorCode = "2";
				getLogger().info("Found multiple products for provided Model and Sequence Number combination");
			} else if (list.size() == 1) {
				Frame frame = list.get(0);
				vin = frame.getProductId();
				if(frame.getProductionLot()!=null)
				{
					ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
					ProductionLot productionLot = productionLotDao.findByKey(frame.getProductionLot());
					if (productionLot!=null) {
						String prodLotKd = productionLot.getProdLotKd();					
						if (prodLotKd == null) {						
							prodLotSubString = productionLot.getProductionLot().substring(8);
							lotSize = String.valueOf(productionLot.getLotSize());

						} else {
							prodLotSubString = prodLotKd.substring(8);
							lotSize = String.valueOf(productionLotDao.findProdLotKdQty(productionLot));
						}
					}else
					{
						getLogger().info("Could not find the Production Lot for the product");
					}
				}else
				{
					getLogger().info("Could not find the Production Lot for the product");
				}
				mtoc = frame.getProductSpecCode();

			}

			getBean().put("vin", new StringBuilder(vin));
			getBean().put("errorCode", new StringBuilder(errorCode));
			getBean().put("prodLot", new StringBuilder(prodLotSubString));
			getBean().put("productSpecCode", new StringBuilder(mtoc));
			getBean().setProductSpecCode(mtoc);
			getBean().put("lotQty", new StringBuilder(lotSize));
			getBean().put("galcDataReady", new StringBuilder("1"),DeviceTagType.PLC_GALC_DATA_READY);
			getLogger().info("Paint Rfid Write Process Successful");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error processing Paint RFID Rewrite process");
			return false;
		} finally {
			getBean().put("eqDataReady", new StringBuilder("0"));
		}
	}



	public void postPlcWrite(boolean writeSucceeded) {
	}

	public void validate() {
	}
}
