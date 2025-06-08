package com.honda.galc.service.datacollection.task;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;

/**
 * <h3>The task is the parent class to generate additional broadcast data. The broadcast data will be store in the context using key: {@link TagNames#BROADCAST_DATA}</h3> 
 * <h4>Description</h4>
 * <p>
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
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @author Hale Xie
 * @created Nov 14, 2014
 */
public abstract class BroadcastDataTask extends CollectorTask implements
		IHlServiceTask {

	/**
	 * Instantiates a new broadcast data task.
	 *
	 * @param context the context
	 * @param processPointId the process point id
	 */
	public BroadcastDataTask(HeadlessDataCollectionContext context,
			String processPointId) {
		super(context, processPointId);
	}

	/* (non-Javadoc)
	 * @see com.honda.galc.service.datacollection.task.CollectorTask#execute()
	 */
	@Override
	public void execute() {
		DataContainer dc = (DataContainer) this.context
				.get(TagNames.BROADCAST_DATA.name());
		if (dc == null) {
			dc = new DefaultDataContainer();
			this.context.put(TagNames.BROADCAST_DATA.name(), dc);
		}
		prepareBroadcastData(dc);
	}

	/**
	 * Prepare the broadcast data.
	 *
	 * @param dc the data container that contains the additional broadcast data.
	 */
	protected abstract void prepareBroadcastData(DataContainer dc);
}
