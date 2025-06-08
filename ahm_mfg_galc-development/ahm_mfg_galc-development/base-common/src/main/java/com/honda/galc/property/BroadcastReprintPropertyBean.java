package com.honda.galc.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 13, 2016
 */
@PropertyBean
public interface BroadcastReprintPropertyBean extends IProperty {

	// specifies Form Feed devices
	@PropertyBeanAttribute(defaultValue="")
	public Map<String,String> getFormFeedDevice();

	// specifies  Form Feed device templates
	@PropertyBeanAttribute(defaultValue="")
	public Map<String,String> getFormFeedTemplate();

	//specifies default Line Name for AFON Seq Range search
	@PropertyBeanAttribute(defaultValue="")
	public String getDefaultSeqLine();
	
	//specifies security groups per for form type
	@PropertyBeanAttribute(defaultValue="")
	public Map<String,String> getFormAuthorizedGroups();
	
	/**
	 * Specify the list of additional destination ids should be displayed separated by comma, for
	 * instance "PRINTER1,PRINTER2"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getAdditionalDestinationIds();
	
	/**
	 * Specify the list of applicable product types should be displayed separated by comma, for
	 * instance "FRAME,ENGINE"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getApplicableProductTypes();
	
	/**
	 * Specify only the list of destination ids should be displayed separated by comma, for
	 * instance "PRINTER1,PRINTER2"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getFilterByDestinationIds();
	
	/**
	 * Specify only the list of process point ids should be displayed separated by comma, for
	 * instance "PROCESSPOINT1,PROCESSPOINT2"
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public String getFilterByProcessPointIds();
	
	/**
	 * Flag indicates whether tray selection combo-box is displayed and TRAY_NO tag is created.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isTraySelectionEnabled();
	
	/**
	 * Sets product table columns order for Search By VIN view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchByProductIdColumns(Class<?> clazz);
	
	/**
	 * Sets product table columns order for Search By Sequence Number view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchBySeqNumberColumns(Class<?> clazz);
	
	/**
	 * Sets product table columns order for Search By Sequence Number Range view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchBySeqNumRangeColumns(Class<?> clazz);
	
	/**
	 * Sets product table columns order for Search By Production Lot view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchByProdLotColumns(Class<?> clazz);
	
	/**
	 * Sets product table columns order for Search By Tracking Status view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchByTrackingStatusColumns(Class<?> clazz);
	
	/**
	 * Sets product table columns order for Search By Last Process Point view.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String, String> isSearchByLastProcPointColumns(Class<?> clazz);
	
	/**
	 * Specifies the list of printer tray values for devices with unique tray configurations 
	 * (e.g., 0,1,2 instead of the typical 1,2,3). 
	 * 
	 * Tray values are mapped against Broadcast Destination IDs 
	 * (e.g., TRAYS_BY_BROADCAST_ID_MAP{GalcAFOn} = 0,1,2,3,4)
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getTraysByBroadcastIdMap(Class<?> clazz);
	
	/**
	 * Specifies the column index to sort (index range: [0, column_length-1])
	 * (e.g., 1 which specifies the column index 1)
	 * 
	 * Column index values are mapped to Product Types
	 * (e.g., COLUMN_SORTING_STATUS{FRAME} = 2)
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue="")
	public Map<String,String> getColumnSortingStatus();
}
