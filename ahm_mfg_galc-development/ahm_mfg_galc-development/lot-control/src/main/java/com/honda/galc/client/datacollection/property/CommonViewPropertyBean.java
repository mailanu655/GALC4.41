package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * <h3>CommonViewPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CommonViewPropertyBean description </p>
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
 * @author Paul Chou
 * Mar 17, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
@PropertyBean(componentId ="Default_CommonViewProperties")
public interface CommonViewPropertyBean extends CommonPropertyBean{
	
	/**
	 * Text displays on product spec label
	 * for example: YMTO for Engine and MTOC for Frame
	 * @return
	 */
	@PropertyBeanAttribute
	public String getProdSpecLabel();

	/**
	 * Product entity attribute name for productSpec text field
	 * currently only support ProductSpecCode, KdLotNumber and ProductionLot
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "ProductSpec")
	public String getProdSpecText();
	
	
	/**
	 * Text displays on expected product Id label
	 * for example: Next expected Ein:
	 * @return
	 */
	@PropertyBeanAttribute
	public String getExpectProductIdLabel();
	
	/**
	 * Text displays on Product Id label
	 * for example: VIN from Frame, EIN for Engine
	 * @return
	 */
	@PropertyBeanAttribute
	public String getProductIdLabel();

	
	/**
	 * Indicate if torque value is shown as decimal value, for example 12.05, 
	 * or text OK/NG 
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isShowTorqueAsValue();
	
	/**
	 * Flag to indicate if to put product on hold when spec change
	 * default false
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "false")
	boolean isHoldOnSpecChange();
	/**
	 * Flag to indicate if need associate to confirm product put on hold by spec change
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isConfirmSpecCheck();
	
	
	
}
