package com.honda.galc.client.teamleader.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>ReprintPropertyBean Class description</h3>
 * <p> ReprintPropertyBean description </p>
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
 * @author Jeffray Huang<br>
 * Apr 19, 2011
 *
 *
 */
@PropertyBean
public interface ReprintPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPrintFormList();
	
	@PropertyBeanAttribute
	public String getDefaultProductSelectionPanel();
	
	
	@PropertyBeanAttribute(defaultValue ="")
	public Map<String,String> getPanelMap();
	
	@PropertyBeanAttribute(defaultValue ="")
	public String getProductType();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getAeLineIdList();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getAllowMultipleCopies();
	
	@PropertyBeanAttribute(defaultValue ="")
	public String getApplicableProcessPoint();
	
	@PropertyBeanAttribute(defaultValue ="")
	public String getAdditionalProcessPoints();	
}
