package com.honda.galc.client.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * Refer to http://docs.oracle.com/javafx/2/api/javafx/scene/input/KeyCode.html
 * for permitted key codes The value should be in the format
 * Key,modifier1,modifier2....etc e.g A,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN The key
 * sequence will be typically be sent from a programmable remote control device
 * for that reason a long key sequence is used.
 * 
 * @author Suriya Sena<br>
 * 
 */

@PropertyBean
public interface KeypadPropertyBean extends IProperty {
	/**
	 * Key sequence for 2SD Remote Left Button
	 */
	@PropertyBeanAttribute(defaultValue = "Q,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getLeft();

	/** Key sequence for 2SD Remote Right Button */
	@PropertyBeanAttribute(defaultValue = "W,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getRight();

	/** Key sequence for 2SD Remote Complete Button */
	@PropertyBeanAttribute(defaultValue = "A,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getComplete();
	
	/** Key sequence for 2SD Remote Reject Button */
	@PropertyBeanAttribute(defaultValue = "R,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getReject();

	/** Key sequence for 2SD Remote Toggle Pane Button */
	@PropertyBeanAttribute(defaultValue = "Z,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getTogglePane();
	
	/** Key sequence for Scanning to Skip Task Button */
	@PropertyBeanAttribute(defaultValue = "T,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getSkipTask();
	
	/** Key sequence for Scanning to Skip Operation Button */
	@PropertyBeanAttribute(defaultValue = "O,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getSkipOperation();
	
	/** Key sequence for Scanning to Skip Product Button */
	@PropertyBeanAttribute(defaultValue = "P,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getSkipProduct();
	
	/** Key sequence for Scanning to Previous Task Button */
	@PropertyBeanAttribute(defaultValue = "L,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getPreviousTask();
	
	/** Key sequence for Scanning to Previous Operation Button */
	@PropertyBeanAttribute(defaultValue = "M,CONTROL_DOWN,ALT_DOWN,SHIFT_DOWN")
	public String getPreviousOperation();

}
