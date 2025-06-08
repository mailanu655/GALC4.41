package com.honda.galc.service.engine;

import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>DcMcHeadOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcMcHeadOn description </p>
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
 * <TD>A.Gawarla</TD>
 * <TD>Aug 25, 2015</TD>
 * <TD>1.42</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Ambica Gawarla
 * @since Aug 28, 2015
 */

public class DcMcHeadOnServiceImpl extends DcMcOn<Head> implements
		DcMcHeadOnService {

	public DcMcHeadOnServiceImpl(DiecastOn<Head> dcOnService, MachiningOn<Head> mcOnService) {
		super(dcOnService, mcOnService);
		context.setProductType(ProductType.HEAD);
	}

	public void saveDiecast(Head head) {
		head.setHeadId(head.getDcSerialNumber());
		ServiceFactory.getDao(HeadDao.class).save(head);
	}

	protected String getMaskProperty() {
		return PropertyService.getProperty("prop_PartSerialMask", "MCH");
	}

}
