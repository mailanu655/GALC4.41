package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;


/**
 * <h3>Class description</h3>
 * Properties for image data collection
 * 
 * <h4>Description</h4>
 * <p>
 * </p>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Sep. 12, 2017</TD>
 * <TD>1.0</TD>
 * <TD>20170912</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Dylan Yang
 */
@PropertyBean(componentId ="Image_Data_Collection")
public interface ImageViewPropertyBean extends CommonViewPropertyBean, ViewProperty{

	/**
	 * Number of torques displayed in a line. Adjust this based on screen resolution and 
	 * other torque field settings such as width, height, font and etc.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "4")
	int getTorquesPerLine();
	
	/**
	 * 
	 * 
	 * 
	 */
	
	@PropertyBeanAttribute(defaultValue = "100")
	int getTorqueFieldHeight();
	
	/**
	 * 
	 */
	
	@PropertyBeanAttribute(defaultValue = "600")
	int getTorqueFieldWidth();
	
	/**
	 * Flag to enable Cancel button
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isEnableCancel();

	/**
	 * enable skip part button
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isEnableSkipPart();

	/**
	 * enable skip product button
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isEnableSkipProduct();
	
	/**
	 * enable next product button
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isEnableNextProduct();
	
	/**
	 * Enable Default view look and feel 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseDefaultViewLook();
}
