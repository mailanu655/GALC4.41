package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * <h3>DataSyncProperty</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DataSyncProperty description </p>
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
 * Mar 25, 2010
 *
 */
@PropertyBean(componentId ="Default_LotControlDataSync")
public interface DataSyncProperty extends IProperty{
	
	/**
	 * Indicates back ground synchronize manager is manage local data to 
	 * synchronize into database when server is back online
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isAutoSync();

	/**
	 * Interval between two synchronization transactions
	 * @return
	 */
	int getOnlineSyncInterval();

	/**
	 * Maximum number of records to insert into database in one transaction
	 * @return
	 */
	int getSyncDataSize();
	
	/**
	 * Number of products to be display in one page
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "30")
	int getCacheDisplayPageSize();
	
}
