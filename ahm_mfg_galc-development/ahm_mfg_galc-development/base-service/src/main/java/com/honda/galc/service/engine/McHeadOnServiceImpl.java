package com.honda.galc.service.engine;

import com.honda.galc.dao.product.HeadDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.Head;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.DiecastUtil;

/**
 * 
 * <h3>McHeadOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> McHeadOn description </p>
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
public class McHeadOnServiceImpl extends MachiningOn<Head> implements McHeadOnService{
	
	public McHeadOnServiceImpl() {
		super();
		context.setProductType(ProductType.HEAD);
	}

	@Override
	public boolean isInputValid() {
		if(dcNumber.equals(mcNumber)){
			//Head from Anna or other plants
			isHomeProduct = false;
			getLogger().info("DCH:", dcNumber, "MCH:", mcNumber, ", head from other plant - dc & mc same number.");
			return validateDcMcNumberForHeadFromOtherPlants(mcNumber);
		} 
		
		return super.isInputValid();
		
	}

	private boolean validateDcMcNumberForHeadFromOtherPlants(String mcNumber) {
		boolean result = DiecastUtil.validateProductMcNumber(context.getProductType().name(), mcNumber, context, null);
		context.put(TagNames.VALID_DC_NUMBER.name(), result);
		context.put(TagNames.VALID_MC_NUMBER.name(), result);
		
		return result;
	}
	
	protected HeadDao getDao() {
		return ServiceFactory.getDao(HeadDao.class);
	}

	@Override
	protected String getMaskProperty() {
		return PropertyService.getProperty("prop_PartSerialMask", "MCH");
	}

}
