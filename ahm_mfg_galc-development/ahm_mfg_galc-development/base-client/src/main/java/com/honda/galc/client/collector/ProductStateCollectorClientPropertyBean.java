package com.honda.galc.client.collector;

import java.awt.Color;

import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>CollectorClientPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorClientPropertyBean description </p>
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
 * <TD>Dec 2, 2011</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Dec 2, 2011
 */

@PropertyBean(componentId ="Default_ProductStateCollectorClient")
public interface ProductStateCollectorClientPropertyBean extends CollectorClientPropertyBean, AudioPropertyBean {
	
	/**
	 * Collector client controller class
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.collector.ProductStateCollectorClientController")
	public String getControllerClass();

	/**
	 * Gets the product state client id.
	 *
	 * @return the product state client id
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getProductStateClientId();
	
	/**
	 * Checks if is show ng product state.
	 *
	 * @return true, if is show ng product state
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowOnlyNgState();
	
	/**
	 * Gets the product state divider location.
	 *
	 * @return the product state divider location
	 */
	@PropertyBeanAttribute(defaultValue = "320")
	public int getProductStateDividerLocation();

	/**
	 * Checks if is product state.
	 *
	 * @return true, if is product state
	 */
	@PropertyBeanAttribute(defaultValue = "100")
	public int getMaxNumberOfRows();
	
	/**
	 * Gets the font size for the Product List Table.
	 *
	 * @return 
	 */
	@PropertyBeanAttribute(defaultValue = "12")
	public int getListFontSize();
	
	/**
	 * Gets the highlighted row for Product list.
	 *
	 * @return 
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getHighlightRow();
	
	/**
	 * Gets the highlighted row color for Product list.
	 *
	 * @return 
	 */
	@PropertyBeanAttribute(defaultValue = "200,200,200")
	public Color getHighlightRowColor();


	/**
	 * determine if check error message of last NG product need to be cleaned.
	 * 
	 * @return	boolean 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCheckLastNgProduct();
	
	/**
	 * Determine if client move to next product when NG status
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAutoRefreshNg();
  
	/**
	 * Determine if auto remove NG Product from Quality Assurance Collector productCheckList
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey = "AUTO_CLEANUP", defaultValue = "0")
	public int isAutoCleanUpEnabled();
	
}
