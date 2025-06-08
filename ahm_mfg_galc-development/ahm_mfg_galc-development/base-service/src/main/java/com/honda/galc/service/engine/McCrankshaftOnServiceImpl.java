package com.honda.galc.service.engine;

import com.honda.galc.dao.product.CrankshaftDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Crankshaft;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>McCrankshaftOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> McCrankshaftOnServiceImpl description </p>
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
 * <TD>Kamlesh Maharjan</TD>
 * <TD>Oct 15, 2021</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
*/

public class McCrankshaftOnServiceImpl extends MachiningOn<Crankshaft> implements McCrankshaftOnService{
	
	public McCrankshaftOnServiceImpl() {
		context.setProductType(ProductType.CRANKSHAFT);
	}

	protected CrankshaftDao getDao() {
		return ServiceFactory.getDao(CrankshaftDao.class);
	}

	@Override
	protected String getMaskProperty() {
		return PropertyService.getProperty("prop_PartSerialMask", "CS_HMA");
	}

}
