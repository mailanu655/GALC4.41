package com.honda.galc.service.engine;

import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Block;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>McBlockOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> McBlockOn description </p>
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
 * <TD>Jun 27, 2012</TD>
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
 * @since Jun 27, 2012
 */
public class McBlockOnServiceImpl extends MachiningOn<Block> implements McBlockOnService{
	
	public McBlockOnServiceImpl() {
		context.setProductType(ProductType.BLOCK);
	}

	protected BlockDao getDao() {
		return ServiceFactory.getDao(BlockDao.class);
	}

	@Override
	protected String getMaskProperty() {
		return PropertyService.getProperty("prop_PartSerialMask", "MCB");
	}

}
