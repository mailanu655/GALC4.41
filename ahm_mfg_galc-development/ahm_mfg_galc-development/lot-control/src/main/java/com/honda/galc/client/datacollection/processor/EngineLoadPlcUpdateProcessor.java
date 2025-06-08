/**
 * 
 */
package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;

import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.common.util.EngineLoadUtility;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;

/**
 * @author Subu Kathiresan
 * @date Jan 4, 2012
 */
public class EngineLoadPlcUpdateProcessor extends AbstractPlcCommunicationProcessor
	implements IDataCollectionTaskProcessor<ProductId> {

	public EngineLoadPlcUpdateProcessor(ClientContext context) {
		super(context);
	}

	public void init() {
	}

	public synchronized boolean execute(ProductId productId) {
		Logger.getLogger().info("EngineSerialPlcUpdateProcessor : Enter execute()");
		try {
			rule = getController().getState().getCurrentLotControlRule();
			EngineLoadUtility utility = new EngineLoadUtility();
			if (utility.updateEngineLoadPlc(getPlc(), getController().getState().getProductId())) {
				getController().getFsm().partSnOk(updateInstalledPart(productId));
				Logger.getLogger().info("EngineSerialPlcUpdateProcessor: Exit execute() ok");
				return true;
			} else {
				getController().getFsm().message(new Message("PLC Communication failure", "Failed to write AFB data to PLC", MessageType.ERROR));
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Logger.getLogger().error(ex.getMessage());
		} finally {
			if (finsPlcDevice != null)
				finsPlcDevice.unregisterListener(this);
		}
		Logger.getLogger().info("EngineSerialPlcUpdateProcessor: Exit execute() ng");
		return false;
	}

	/**
	 * updates the installed part attributes
	 * 
	 * @param productId
	 */
	private InstalledPart updateInstalledPart(ProductId productId) {
		InstalledPart installedPart = new InstalledPart();
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setMeasurements(new ArrayList<Measurement>());
		installedPart.setProductId(productId.getProductId());
		installedPart.setAssociateNo(context.getUserId());
		installedPart.setPartName(rule.getPartName().getPartName());
		installedPart.setPartId(rule.getParts().get(0).getId().getPartId());
		installedPart.setValidPartSerialNumber(true);
		installedPart.setPartIndex(getController().getState().getCurrentPartIndex());
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		return installedPart;
	}

}
