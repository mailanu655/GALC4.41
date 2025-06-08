package com.honda.galc.client.dc.processor;

import java.util.HashMap;

import com.honda.galc.client.dc.mvc.DataCollectionController;
import com.honda.galc.device.dataformat.InputData;
import com.honda.galc.dto.BroadcastContext;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.service.BroadcastMQService;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>BroadcastMQProcessor Class description</h3>
 * <p>
 * BroadcastMQProcessor description
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
 * @author Larry Karpov<br>
 *         May 12, 2015
 * 
 * 
 */

public class BroadcastMQProcessor extends OperationProcessor {

	public BroadcastMQProcessor(DataCollectionController controller,
			MCOperationRevision operation) {
		super(controller, operation);
	}

	public boolean execute(InputData data) {
		getLogger().info("BroadcastMQProcessor");
		BroadcastContext context = createBroadcastContext();
		if (populateBroadcastContext(context, data) != null) {
			return broadcast(context);
		} else {
			return false;
		}
	}

	protected boolean broadcast(BroadcastContext context) {
		BroadcastMQService service = ServiceFactory
				.getService(BroadcastMQService.class);
		try {
			service.broadcast(context);
		} catch (Exception e) {
			getLogger().error(e);
			return false;
		}
		return true;
	}

	protected BroadcastContext createBroadcastContext() {
		BroadcastContext context = new BroadcastContext();
		context.setExtraAttribs(new HashMap<String, Object>());
		return context;
	}
	
	protected BroadcastContext populateBroadcastContext(BroadcastContext context, InputData data) {
		return null;
	}
}
