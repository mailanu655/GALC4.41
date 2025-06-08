package com.honda.galc.client.dc.processor;

import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.dto.BroadcastContext;
import com.honda.galc.dto.InstalledPartBroadcastContext;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.InstalledPart;

/**
 * 
 * <h3>BroadcastMQProcessor Class description</h3>
 * <p>
 * Operation Processor to broadcast installed part
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Alok Ghode<br>
 *         Jan 27, 2016
 * 
 * 
 */

public class InstalledPartBroadcastMQProcessor extends BroadcastMQProcessor {

	public InstalledPartBroadcastMQProcessor(DataCollectionController controller,
			MCOperationRevision operation) {
		super(controller, operation);
	}
	
	@Override
	public boolean execute(InputData data) {
		getLogger().info("Input data received: " + data);
		return false;
	}
	
	@Override
	protected BroadcastContext createBroadcastContext() {
		InstalledPartBroadcastContext context = null;
		//Fetching Installed Part
		InstalledPart installedPart = getController().getModel().getInstalledPartsMap().get(getOperation().getId().getOperationName());
		if (installedPart != null && StringUtils.isNotBlank(installedPart.getPartSerialNumber())) {
			//Setting installed part context if Part Serial Number is not blank
			context = new InstalledPartBroadcastContext();
			context.setProductId(getController().getProductModel().getProductId());
			context.setProductType(getController().getProductModel().getProduct().getProductType());
			context.setProcessPointId(getController().getProductModel().getProcessPointId());
			context.setInstalledPart(installedPart);
			context.setExtraAttribs(new HashMap<String, Object>());
			//Populating additional required parameters 
			populateBroadcastContext(context, null);
		}
		else {
			getLogger().warn("Install Part/SN not found for operation: " + getOperation().getId().getOperationName());
		}
		return context;
	}

	@Override
	public void completeOperation() {
		getLogger().info("Complete Operation: Executing Installed Part Broadcast");
		BroadcastContext context = createBroadcastContext();
		if (context != null) {
			//Broadcasting Installed Part 
			getLogger().info("Broadcasting installed part for operation: " + getOperation().getId().getOperationName());
			broadcast(context);
		}
		else {
				getLogger().error("Installed Part Broadcast is not able to create broadcast context");
		}
	}
}
