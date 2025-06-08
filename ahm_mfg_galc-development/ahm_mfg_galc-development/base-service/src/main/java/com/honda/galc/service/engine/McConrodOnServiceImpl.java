package com.honda.galc.service.engine;

import com.honda.galc.dao.product.ConrodDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Conrod;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>McConrodOnServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> McConrodOnServiceImpl description </p>
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

public class McConrodOnServiceImpl extends MachiningOn<Conrod> implements McConrodOnService{
	
	public McConrodOnServiceImpl() {
		context.setProductType(ProductType.CONROD);
	}

	protected ConrodDao getDao() {
		return ServiceFactory.getDao(ConrodDao.class);
	}

	@Override
	protected String getMaskProperty() {
		return PropertyService.getProperty("prop_PartSerialMask", "CR_HMA");
	}

}
