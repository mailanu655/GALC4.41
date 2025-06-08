package com.honda.galc.client.dc.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>DataCollectionPropertyBean Class description</h3>
 * <p> DataCollectionPropertyBean description </p>
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
 * Feb 25, 2014
 *
 *
 */
public interface DataCollectionPropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.dc.view.ProcessViewManager")
	public String getViewManager();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.dc.observer.DataCollectionAudioManager")
	public String getAudioManager();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.dc.observer.NonCachePersistenceManager")
	public String getPersistentManager();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.dc.observer.JobTorqueDeviceManager")
	public String getTorqueManager();
	
	@PropertyBeanAttribute(defaultValue="true")
	boolean isUnitNavigatorEnabled();
	
	@PropertyBeanAttribute(defaultValue="true")
	boolean isNavigatorClickable();
	
	@PropertyBeanAttribute(defaultValue="false")
	boolean isVisualOpsEnabled();
	
	@PropertyBeanAttribute(defaultValue="false")
	boolean isShowOpNameInUnitNavigator();
		
	@PropertyBeanAttribute(defaultValue= "5000")
	int lastMeasurementDisplayTime();
		
	/** This controls the presence of a COMPLETE button on the ProcessInstructionView, for developer testing. */
	@PropertyBeanAttribute(defaultValue="true")
	boolean isDisplayCompleteButton();
	
	/** This controls the presence of a SKIP button on the DataCollectionView */
	@PropertyBeanAttribute(defaultValue="true")
	boolean isDisplaySkipButton();
		
	/** this determines whether the Device status widget is
	 * shown at the bottom of the data collection panel or
	 * to the right of the panel
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isShowDeviceStatusAtBottom();
	
	/**
	 * get extra observers
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getObservers();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getFilterIds();
	
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMultiProcessPoints();
}
