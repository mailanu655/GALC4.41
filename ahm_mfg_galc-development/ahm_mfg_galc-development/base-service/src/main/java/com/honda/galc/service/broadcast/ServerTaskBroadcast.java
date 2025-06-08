package com.honda.galc.service.broadcast;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.ApplicationContextProvider;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.service.broadcast.servertask.IServerTask;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ServerTaskBroadcast</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Nov 10, 2017
 */
public class ServerTaskBroadcast extends AbstractBroadcast {

	public ServerTaskBroadcast(BroadcastDestination destination, String processPointId, DataContainer dc) {
		super(destination, processPointId, dc);
	}

	@Override
	public DataContainer send(DataContainer dc) {

		String taskName = destination.getDestinationId();

		if (!IServerTask.SERVER_TASKS.containsKey(taskName)) {
			DataContainerUtil.error(logger, dc, "DestinationId / TaskName name can not be blank !");
			return dc;
		}		

		Object bean = null;
		try {
			bean = ApplicationContextProvider.getBean(taskName);
		} catch (Exception e) {
			DataContainerUtil.error(logger, dc, e, "Bean / Task " + taskName + " can not be acceseed !");
			return dc;
		}

		if (!(bean instanceof IServerTask)) {
			DataContainerUtil.error(logger, dc, "Invalid Task for " + taskName + ". Task must be instance of " + IServerTask.class.getSimpleName());
			return dc;
		}

		try {
			IServerTask serverTask = (IServerTask) bean;
			DataContainer retDc = serverTask.execute(dc);
			return retDc;
		} catch (Exception e) {
			DataContainerUtil.error(logger, dc, e, "Failed to execute " + taskName + " for :" + dc);
		}
		return dc;
	}
}
