package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>ClassicViewPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClassicViewPropertyBean description </p>
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
@PropertyBean(componentId ="Classic_DataCollectionPanel")
public interface ClassicViewPropertyBean extends CommonViewPropertyBean, ViewProperty{
	int getTorqueInfoAreaHeight();
	
	/**
	 * Flag to enable Cancel button
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isEnableCancel();

	/**
	 * enable skip part button
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isEnableSkipPart();

	/**
	 * enable skip product button
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isEnableSkipProduct();
	
	/**
	 * enable next product button
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "true")
	boolean isEnableNextProduct();
}
