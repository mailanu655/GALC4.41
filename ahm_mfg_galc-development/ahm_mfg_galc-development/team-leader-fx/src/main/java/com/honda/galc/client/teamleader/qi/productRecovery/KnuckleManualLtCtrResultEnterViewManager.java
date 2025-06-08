package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
/**
 * 
 * <h3>KnuckleManualLtCtrResultEnterViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> KnuckleManualLtCtrResultEnterViewManager description </p>
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
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author L&T infotech
 * Aug 18, 2017
 */
public class KnuckleManualLtCtrResultEnterViewManager extends ManualLtCtrResultEnterViewManagerBase{
	public KnuckleManualLtCtrResultEnterViewManager(ApplicationContext appContext,Application application, ProductType currentProductType) {
		super(appContext, application, currentProductType);
	}
}
