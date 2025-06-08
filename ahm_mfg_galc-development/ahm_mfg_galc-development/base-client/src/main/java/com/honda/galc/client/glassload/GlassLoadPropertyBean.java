package com.honda.galc.client.glassload;

import java.awt.Color;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean()
public interface GlassLoadPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "175,250,255")
	public Color getBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getForegroundColor();

	@PropertyBeanAttribute(defaultValue = "75,100,230")
	public Color getAlternateBackgroundColor();

	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getAlternateForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getPrevHighlightForegroundColor();

	@PropertyBeanAttribute(defaultValue = "255,255,0")
	public Color getPrevHighlightBackgroundColor();

	@PropertyBeanAttribute(defaultValue = "255,255,255")
	public Color getCurrentHighlightBackgroundColor();

	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getCurrentHighlightForegroundColor();

	@PropertyBeanAttribute(defaultValue = "255,0,0")
	public Color getStragglerHighlightBackgroundColor();

	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getStragglerHighlightForegroundColor();

	@PropertyBeanAttribute(defaultValue = "16")
	public int getFontSize();

	@PropertyBeanAttribute(defaultValue = "13")
	public int getHeaderFontSize();

	@PropertyBeanAttribute(defaultValue = "30")
	public int getItemHeight();

	/**
	 * place the highlighted bar in the predefined position from the top
	 * of the display window. if the value = -1, place in the middle of the 
	 * display window.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "3")
	public int getHighlightPosition();

	@PropertyBeanAttribute(defaultValue = "29")
	public int getProcessedProductNumber();
	
	@PropertyBeanAttribute(defaultValue = "WT01")
	public String getAfOnTrackingStatus();
	
	@PropertyBeanAttribute(defaultValue = "15")
	public int getHeartbeatInterval();
	
	public String getAuthorizationGroup();

}
