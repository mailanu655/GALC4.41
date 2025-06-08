package com.honda.galc.client.datacollection.observer;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.state.ProcessProduct;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.RfidData;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;

/**
 * <h3>Class description</h3>
 * 
 * <h4>Description</h4>
 * <p>
 * Write result to PLC.
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>July 17, 2013</TD>
 * <TD>1.0</TD>
 * <TD>20130717</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */

public class RfidDeviceManager extends LotControlDeviceManager implements IRfidDeviceManager {

	public RfidDeviceManager(ClientContext context) {
		super(context);
	}

	protected List<IDeviceData> getDeviceDataList() {
		List<IDeviceData> list = super.getDeviceDataList();
		list.add(new RfidData());
		return list;
	}

	public void sendResult(ProcessProduct state) {
		
		if(state.getProduct() == null || StringUtils.isEmpty(state.getProductId())) {
			return;
		}
		
		RfidData rfidData = new RfidData();
		String productSpec = state.getProductSpecCode();
		rfidData.setProductId(state.getProductId());
		rfidData.setModelYearCode(ProductSpec.extractModelYearCode(productSpec));
		rfidData.setModelCode(ProductSpec.extractModelCode(productSpec));
		rfidData.setModelTypeCode(ProductSpec.extractModelTypeCode(productSpec));
		rfidData.setModelOptionCode(ProductSpec.extractModelOptionCode(productSpec));
		
		String productionLot = state.getProduct().getProductionLot();
		rfidData.setProductionLot(productionLot);

		ProductionLot prodLot = ServiceFactory.getDao(ProductionLotDao.class).findByKey(productionLot);
		long count = ServiceFactory.getDao(ProductResultDao.class).findTotalProductProcessed(productionLot, context.getProcessPointId());
		rfidData.setLotSeqNo(String.format("%04d", (prodLot.getLotSize() - count + 1)));

		rfidData.setDcComplete(state.getErrorList().size() == 0 ? DataCollectionComplete.OK : DataCollectionComplete.NG);
		if(getEiDevice().containOutputDeviceData(RfidData.class)) {
			sendDeviceData(rfidData);
			Logger.getLogger().info("Finished sending RFID data.");
		}
	}
}
