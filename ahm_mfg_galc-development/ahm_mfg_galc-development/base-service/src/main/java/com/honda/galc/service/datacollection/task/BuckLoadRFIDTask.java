package com.honda.galc.service.datacollection.task;

import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>BuckLoadRFIDTask</h3> <h3>The class is the custom data collection task
 * for 2SD Buck Load On. The task can save RFID to GALC DB</h3> <h4></h4> <h4>
 * Change History</h4>
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
 * @author Hale Xie Sept 1st, 2014
 */
public class BuckLoadRFIDTask extends CollectorTask {

	/**
	 * Instantiates a new buck load rfid task.
	 * 
	 * @param context
	 *            the data collection context
	 * @param processPointId
	 *            the process point id
	 */
	public BuckLoadRFIDTask(HeadlessDataCollectionContext context,
			String processPointId) {
		super(context, processPointId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.honda.galc.service.datacollection.task.CollectorTask#execute()
	 */
	@Override
	public void execute() {
		BuckLoadRFIDPropertyBean property = PropertyService.getPropertyBean(
				BuckLoadRFIDPropertyBean.class, processPointId);
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		id.setPartName(property.getRfidPartName());
		
		//ProductOnService Put the product in Product ID tag. So we have to get the product id from the base product.
		BaseProduct product = (BaseProduct) context.get(property
				.getProductIdTag());
		id.setProductId(product.getProductId());
		installedPart.setId(id);
		//Save the RFID in the part serial number column.
		installedPart.setPartSerialNumber((String) context.get(property
				.getRfidTagName()));
		installedPart.setInstalledPartStatus(InstalledPartStatus.OK);
		installedPart.setPartId(property.getRfidPartId());
		installedPart.setAssociateNo(processPointId);
		installedPart.setProcessPointId(processPointId);
		InstalledPartDao dao = ServiceFactory.getDao(InstalledPartDao.class);
		dao.save(installedPart);
	}

}
