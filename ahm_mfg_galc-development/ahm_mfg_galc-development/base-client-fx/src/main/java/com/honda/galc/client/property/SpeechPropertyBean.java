package com.honda.galc.client.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>SpeachPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> SpeachPropertyBean description </p>
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
 * <TD>Apr 18, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Apr 18, 2012
 */
 
@PropertyBean(componentId ="Default_Speech")
public interface SpeechPropertyBean extends IProperty {
	
	/**
	 * voice name
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "kevin")
	String getVoiceName();
	
	/**
	 * voice style may include "business", "casual", "robotic", "breathy"
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "business")
	String getVoiceStyle();
	
	/**
	 * Voice pitch float value example 5.0
	 * use default if not provided
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getPitch();
	
	/**
	 * Pitch Shift float value example: .007
	 * use default if not provided
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getPitchShift();
	
	/**
	 * Pitch Range float value example: 0.01
	 * use default if not provided
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getPitchRange();
	
	/**
	 * get Locale Language
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String getLanguage();
	
	/**
	 * get Locale County
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String getCountry();
	
	/**
	 * words per minute - float value 
	 * @return
	 */
	@PropertyBeanAttribute( defaultValue = "")
	public String getRate();
	
	/**
	 * Gender of this voice.
	 * values:MALE, FEMALE, NEUTRAL,DONT_CARE
	 * @return
	 */
//	@PropertyBeanAttribute( defaultValue = "")
//	public String getGender();
	
}
