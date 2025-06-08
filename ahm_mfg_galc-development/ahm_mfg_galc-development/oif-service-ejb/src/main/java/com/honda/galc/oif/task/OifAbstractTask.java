package com.honda.galc.oif.task;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.system.oif.svc.common.IOifRunHistory;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;

/**
 * 
 * <h3>OifAbstractTask Class description</h3>
 * <p> OifAbstractTask description </p>
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
 * @author Jeffray Huang<br>
 * May 31, 2012
 *
 *
 */
/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class OifAbstractTask extends PropertyHelper implements IOifRunHistory{
	
	protected Logger logger;

	public OifAbstractTask(String name) {
		super(name);
		logger = Logger.getLogger(name);
	}
	
	public String getName() {
		return getComponentId();
	}

	@Override
	public void execute(Object[] args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OifErrorsCollector getOifErrorsCollector() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setOifErrorsCollector(OifErrorsCollector oifErrorsCollector) {
		// TODO Auto-generated method stub
		
	}

}
