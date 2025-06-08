package com.honda.galc.client.datacollection.observer;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.dao.conf.DeviceFormatDao;
import com.honda.galc.dao.product.PartSpecDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;

/**
 * 
 * <h3>LotControlSubProductPersistenceManager Class description</h3>
 * <p> LotControlSubProductPersistenceManager description </p>
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
 * Nov 30, 2011
 *
 *
 */
public class LotControlIpuPersistenceManager extends LotControlSubProductPersistenceManager {
	
	List<InstalledPart> parts = new ArrayList<InstalledPart>();
	protected DeviceFormatDao deviceFormatDao = null;
	protected PartSpecDao partSpecDao = null;
	
	public LotControlIpuPersistenceManager(ClientContext context) {
		super(context);
	}

	public void addParts(List<InstalledPart> dataOfLineParts) {
		parts.addAll(dataOfLineParts);
	}

	public TrackingService getTrackingService() {
		return ServiceFactory.getService(TrackingService.class);
	}
	
	@Override
	public IExpectedProductManager getExpectedProductManger() {
		if(expectedProductManger == null)
			expectedProductManger = new IpuExpectedProductManager(context);

		return expectedProductManger;
	}


}
