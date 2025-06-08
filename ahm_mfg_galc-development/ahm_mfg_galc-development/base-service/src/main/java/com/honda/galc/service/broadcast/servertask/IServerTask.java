package com.honda.galc.service.broadcast.servertask;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.honda.galc.data.DataContainer;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>IServerTask</code> is ... .
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
public interface IServerTask {
	
	@SuppressWarnings("serial")
	public final static Map<String,Class<? extends IServerTask>> SERVER_TASKS  =
		Collections.unmodifiableMap(
                new HashMap<String, Class<? extends IServerTask>>() {
                    {
                        put("GdpUpdateTask",GdpUpdateTask.class);
                        put("RemoveFromRepairAreaTask",RemoveFromRepairAreaTask.class);
                        put("TrpuUpdateTask",TrpuUpdateTask.class);
                        put("VinBomTask",VinBomTask.class);
						put("SpecCheckHoldTask",SpecCheckHoldTask.class);
						put("HoldToDefectTask",HoldToDefectTask.class);
                     }
                });
	

	public DataContainer execute(DataContainer dc);
}
