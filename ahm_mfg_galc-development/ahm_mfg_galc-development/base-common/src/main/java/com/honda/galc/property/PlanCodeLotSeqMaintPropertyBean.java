package com.honda.galc.property;



/**
 * 
 * @author Gangadhararao Gadde
 * @date Apr09, 2015
 */
@PropertyBean
public interface PlanCodeLotSeqMaintPropertyBean extends IProperty{

	@PropertyBeanAttribute(defaultValue ="")
	public String[] getPlanCodes();		

}
