package com.honda.galc.service.datacollection.work;

import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;

/**
 * 
 * <h3>PersistenceOnWork</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PersistenceOnWork description </p>
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
 * <TD>P.Chou</TD>
 * <TD>May 26, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since May 26, 2015
 */
public class PersistenceOnWork extends PersistenceWork{

	public PersistenceOnWork(HeadlessDataCollectionContext context,
			ProductDataCollectorBase collector) {
		super(context, collector);
		
	}
	
	protected void doTracking() {
		if(getProperty().isAutoTracking())
			collector.track();
	}

}
