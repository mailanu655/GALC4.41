package com.honda.galc.client.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>AudioPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AudioPropertyBean description </p>
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
 * <TD>Feb 22, 2013</TD>
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
 * @since Feb 22, 2013
 */
@PropertyBean(componentId ="Default_Sounds")
public interface AudioPropertyBean extends IProperty{
	String getSoundOk();
	String getSoundNg();
	/*
	 * Used along getSoundScrap() to enable a specific Scrap sound
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean getSoundScrapEnabled();
	
	/*
	 * Used along getEnabledScrapSound() to define the specific Scrap sound
	 */
	@PropertyBeanAttribute(defaultValue = "resource/com/honda/galc/client/sounds/WarningHorn.wav")
	String getSoundScrap();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundOkProductId();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundDestination();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundChanged();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundConnected();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundDisConnected();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundNoAction();
	
	@PropertyBeanAttribute(defaultValue = "0")
	int getNgSoundRepeatCount();
	
	@PropertyBeanAttribute(defaultValue = "0")
	int getChangedSoundRepeatCount();
	
	@PropertyBeanAttribute(defaultValue = "0")
	int getDestinationSoundRepeatCount();
	
	@PropertyBeanAttribute(defaultValue = "")
	String getSoundWarn();
	
	@PropertyBeanAttribute(defaultValue = "N")
	String getLightAlarm();
	
	@PropertyBeanAttribute(defaultValue = "N")
	String getSoundAlarmProductStateClient();
	
	@PropertyBeanAttribute(defaultValue = "resource/com/honda/galc/client/sounds/BoxingBell.wav")
	String getSoundAlarm();
	
	@PropertyBeanAttribute(defaultValue = "0")
	int getAlarmSoundRepeatCount();
}
