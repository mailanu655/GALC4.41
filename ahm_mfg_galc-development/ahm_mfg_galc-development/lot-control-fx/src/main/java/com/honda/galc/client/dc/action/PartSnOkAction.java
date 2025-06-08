package com.honda.galc.client.dc.action;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.client.dc.enumtype.DataCollectionResultEventType;
import com.honda.galc.client.dc.event.DataCollectionEvent;
import com.honda.galc.client.dc.event.DataCollectionResultEvent;
import com.honda.galc.client.dc.mvc.DataCollectionModel;
import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.exception.ServiceTimeoutException;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jun 19, 2014
 */
public class PartSnOkAction extends BaseDataCollectionAction<PartSerialScanData> {

	public void perform(DataCollectionModel model, DataCollectionEvent event) {
		try {
			MCOperationRevision operation = event.getOperation();
			InstalledPart installedPart = getInstalledPart(model, operation);
			PartSerialScanData partSerialScanData = (PartSerialScanData) event.getInputData();
			if (installedPart == null || !isInstalledPartValid(operation, installedPart)) {
				installedPart = createInstalledPart(model, partSerialScanData, event.getOperation(), InstalledPartStatus.OK);
			}

			if(isInstalledPartValid(operation, installedPart)) {
				savePart(installedPart, event.getOperation());
				//If the scanned part is In-house product, update the Last_passing_processPoint to OFF PP so that 
				//Inventory will be up to date
				PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class,model.getProductModel().getProcessPointId());
				if(property.isInHouseTrackingEnabled())
				{
					MbpnProduct inHouseProduct = getMbpnProductDao().findByKey(installedPart.getPartSerialNumber());
					if(inHouseProduct != null) {
						inHouseProduct.setLastPassingProcessPointId(property.getInHouseProductOffProcessPoint());
						getMbpnProductDao().save(inHouseProduct);
					}
				}
				notifyPartSnOk((PartSerialScanData) event.getInputData(), event.getOperation());

				markIfOpCompleted(model, event.getOperation());
			}
		} catch (ServiceTimeoutException e) {
			getLogger().error(e, "Unable to reach server. Please check network connection.");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "Unable to reach server. Please check network connection."));
		} catch (Exception e) {
			getLogger().error(e, "An Exception occured during Data Collection");
			EventBusUtil.publish(new DataCollectionResultEvent(DataCollectionResultEventType.DC_ERROR_REPORTED, "An Exception occured during Data Collection"));
		}
	}
	
	public String getCheckPointName() {
		return "";
	}


	
	public boolean dispatchReactions(List<CheckResult> checkResults, PartSerialScanData inputData) {

		// TODO Auto-generated method stub
		return false;
	}


}
