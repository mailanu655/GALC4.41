package com.honda.galc.oif.task;

import java.io.IOException;
import java.util.Date;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.oif.task.ProductionResultHandlerGIV705;
import com.honda.galc.oif.task.ProcessingBodyHandlerGIV706;
import com.honda.galc.oif.task.ProductionProgressHandlerGIV707;

/**
 * 
 * <h3>ProductionProcessTask</h3>
 * <p>
 * ProductionProcessTask sends GIV705, GIV706, and GIV707 together
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
 * @author Kenneth Gibson<br>
 *         January 26, 2015
 * 
 */

public class SendGIV extends OifAbstractTask implements
		IEventTaskExecutable {

	Date date = new Date();
	String GIV705_COMPONENT_ID = getProperty("GIV705_COMPONENT_ID");
	String GIV706_COMPONENT_ID = getProperty("GIV706_COMPONENT_ID");
	String GIV707_COMPONENT_ID = getProperty("GIV707_COMPONENT_ID");
	
	public SendGIV(String componentId) throws IOException {
		super(componentId);
	}

	public void execute(Object[] args) {		
			try {
				exportRecords(args);
			} catch (IOException e) {
				String errorStr = "IOException raised when sending GIV files";
				logger.error(e, errorStr);
			}
		} 
	

	private void exportRecords(Object[] args) throws IOException {
		ProductionResultHandlerGIV705 	giv705 = new ProductionResultHandlerGIV705(GIV705_COMPONENT_ID);
		ProcessingBodyHandlerGIV706 giv706 = new ProcessingBodyHandlerGIV706(GIV706_COMPONENT_ID);
		ProductionProgressHandlerGIV707 giv707 = new ProductionProgressHandlerGIV707(GIV707_COMPONENT_ID);
		
		//send GIV 705 file
		giv705.execute(args);
		
		//send GIV 706 file
		giv706.execute(args);
		
		//send GIV 707 file
		giv707.execute(args);
		
	}
	
}


