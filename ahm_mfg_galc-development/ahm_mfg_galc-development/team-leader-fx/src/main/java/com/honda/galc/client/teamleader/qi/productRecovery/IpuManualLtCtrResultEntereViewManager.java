package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ApplicationContext;
/**
 * 
 * <h3>IpuManualLtCtrResultEntereViewManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> IpuManualLtCtrResultEntereViewManager description </p>
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
 * @author L&T infotech
 * Aug 18, 2017
 *
 */
/**
 * 
  * @author L&T infotech
 * Aug 18, 2017
 */
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;

public class IpuManualLtCtrResultEntereViewManager extends ManualLtCtrResultEnterViewManagerBase{
	public IpuManualLtCtrResultEntereViewManager(ApplicationContext appContext,Application application, ProductType currentProductType) {
		super(appContext, application, currentProductType);
	}
}
