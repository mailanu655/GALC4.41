package com.honda.galc.client.collector;

import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

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

@PropertyBean(componentId ="Default_CollectorClient")
public interface CollectorClientPropertyBean extends SystemPropertyBean{
	
	/**
	 * specify how to show a result
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getRenders();
	
	/**
	 * Product Name
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "PRODUCT ID:")
	public String getProductIdLabel();
	
	/**
	 * Product Spec Label
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "YMTO:")
	public String getProductSpecLabel();
	
	
	/**
	 * Product Max Id Length
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "17")
	public int getMaxProductIdLength();

	/**
	 * Render type. Initially support html is supported.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "html")
	public String getRenderType();
	
	/**
	 * template file for rendering out
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRenderTemplate();
	
	/**
	 * properties for rendering out
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getRenderProperties();


	/**
	 * Action scripts maps
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getActions();

	/**
	 * Collector client controller class
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.collector.CollectorClientController")
	public String getControllerClass();

	/**
	 * Indicate if PLC data is through OPC Ei
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUseOpcEi();

	/**
	 * Show product Id on the client UI
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowProductId();

	/**
	 * The client will automatically send request to server and update the client content
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoUpdate();

	/**
	 * Interval in milliseconds between services call to server when doing auto-update
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getUpdateInterval();

	/**
	 * Control if the Cancel button should be used
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isEnableCancel();
	
	
	@PropertyBeanAttribute
	public Map<String, String> getDataInputMap();
}
