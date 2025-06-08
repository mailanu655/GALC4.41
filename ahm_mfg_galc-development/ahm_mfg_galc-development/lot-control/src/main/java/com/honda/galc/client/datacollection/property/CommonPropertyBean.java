package com.honda.galc.client.datacollection.property;

import java.util.List;
import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;
/**
 * 
 * <h3>CommonPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CommonPropertyBean description </p>
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
 * Mar 19, 2010
 *
 */

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
@PropertyBean(componentId ="Default_CommonProperties")
public interface CommonPropertyBean extends SystemPropertyBean {

	/**
	 * Maximum length of product Id
	 * Maximum product id length for engine is 12
	 * Maximum product id length for frame is 17
	 * @return
	 */
	//@PropertyBeanAttribute (defaultValue = "17") //Frame
	@PropertyBeanAttribute(defaultValue = "12") //Engine
	public abstract int getMaxProductSnLength();
	
	/**
	 * Maximum length of part serial number text field 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "23")
	public int getMaxPartTextLength();
	
	/**
	 * Indicate the heartbeat interval in milliseconds
	 */
	@PropertyBeanAttribute
	public String getHeartbeatInterval();
	
	/**
	 * Flag indicates next expected product id is display and checked 
	 * @return
	 */
	boolean isCheckExpectedProductId();
	
	/**
	 * Hide Product Skip button on the LC Product Entry screen if expected product check is disabled 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isHideSkipButtonWhenExpCheckDisabled();
	
	/**
	 * Flag indicates AF ON SEQ NO exist and checked
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isAfOnSeqNumExist();
	
	/**
	 * Indicates if auto processing of products is turned on
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isAutoProcessExpectedProduct();
	
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isTrimProductId();
	
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isPreviousLineCheckEnabled();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public Map<String, String> getFormId();
	
	/**
	 * Flag indicates Product count exist for lot control station.
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isProductLotCountExist();
	
	
}