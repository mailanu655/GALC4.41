package com.honda.galc.client.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>AudioPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AudioPropertyBean description </p>
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
 * <TD>Feb 22, 2013</TD>
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
 * @since Feb 22, 2013
 */
@PropertyBean(componentId ="Default_Sounds")
public interface AudioPropertyBean extends IProperty{
	String getSoundOk();
	String getSoundNg();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundChanged();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundConnected();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundDisConnected();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundNoAction();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundUrge();

	@PropertyBeanAttribute(defaultValue = "-1")
	int getNgSoundRepeatCount();
	
	/**
	 * Sound property for the process in which user id not trained
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundUserNotTrained();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundWarn();
}
