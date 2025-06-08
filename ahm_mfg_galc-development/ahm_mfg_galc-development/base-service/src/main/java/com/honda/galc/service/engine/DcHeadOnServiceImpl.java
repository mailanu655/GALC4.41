package com.honda.galc.service.engine;

import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>DcHeadOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DcHeadOn description </p>
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
 * <TD>Apr 27, 2012</TD>
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
 * @since Apr 27, 2012
 */
public class DcHeadOnServiceImpl extends DiecastOn<Head> implements DcHeadOnService{
	public DcHeadOnServiceImpl() {
		context.setProductType(ProductType.HEAD);
	}

	@Override
	public void saveDiecast(Head head) {
		head.setHeadId(head.getDcSerialNumber());
		ServiceFactory.getDao(HeadDao.class).save(head);
		
	}
	
}
