/**
 * 
 */
package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * @author Subu Kathiresan
 * @date Jan 5, 2012
 */
@PropertyBean(componentId ="Default_EngineLoad")
public interface EngineLoadPropertyBean  extends IProperty {

	/**
	 * Comma separated list of valid previous lines for
	 * Engine load process point
	 * 
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String getValidEngineLoadPreviousLines();
	
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getEngineTrackingPpid();
	
	/**
	 * If true, helps to update engine MTO with right expected one for Commonization 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isEngineMtoUpdateEnabled();
	
	/**
	 * If true, Engine Creation dialog will be displayed whenever engine SN does not
	 * have an associated product record. 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isEngineCreationAllowed();
	
	/**
	 * Client will wait for a PLC signal to complete engine assignment
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isPlcPersist();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getEngLoadToPlcDeviceId();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getVinToPlcDeviceId();
	
	@PropertyBeanAttribute (defaultValue = "")
	public String getEngineSource();
	
	/**
	 * Part Name for Engine Load to create defect
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "ENGINE LOAD")
	public String getEngineLoadDefectPartName();
	
	/**
	 * If true, put VIN on hold if a new engine manifest was created
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateVinHoldOnNewEngManifest();
	
	/**
	 * If true, create NA QICS defect if a new engine manifest was created
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateDefectOnNewEngManifest();
	
	/**
	 * If true, put VIN on hold if engine is on hold
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateVinHoldOnEngineHold();
	
	/**
	 * If true, create NA QICS defect if engine is on hold
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateDefectOnEngineHold();
	
	/**
	 * If true, put VIN on hold if engine is on external hold
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateVinHoldOnExtEngineHold();
	
	/**
	 * If true, create NA QICS defect if engine is on external hold
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isCreateDefectOnExtEngineHold();
	
	
	@PropertyBeanAttribute (defaultValue = "3")
	public String getShortAfOnSeqLength();
	
	@PropertyBeanAttribute (defaultValue = "true")
	public boolean isAllowLastAfOnVinProcessing();
}
