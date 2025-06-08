package com.honda.galc.client.qics.property;

import java.awt.Color;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>QicsPicturePropertyBean Class description</h3>
 * <p> QicsPicturePropertyBean description </p>
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
 * Jul 12, 2011
 *
 *
 */
@PropertyBean(componentId = "prop_QicsPicture")
public interface DefaultQicsPicturePropertyBean extends IProperty{
	
	@PropertyBeanAttribute(propertyKey= "DEFECT_COLOR_OS",defaultValue = "0,255,0")
	public Color getDefectColorOS();
	
	@PropertyBeanAttribute(propertyKey= "DEFECT_COLOR_REPAIR",defaultValue = "0,255,0")
	public Color getDefectColorRepair();
	
	@PropertyBeanAttribute(propertyKey= "DEFECT_COLOR_SCRAP",defaultValue = "0,255,0")
	public Color getDefectColorScrap();
	
	@PropertyBeanAttribute(propertyKey= "DEFECT_FONT_SIZE",defaultValue = "20")
	public int getDefectFontSize();
	
	@PropertyBeanAttribute(propertyKey= "DEFECT_FONT_STYLE",defaultValue = "0")
	public int getDefectFontStyle();
	
	@PropertyBeanAttribute(propertyKey= "HEMMING_COLOR_OS",defaultValue = "0,255,0")
	public Color getHemmingColorOS();
	
	@PropertyBeanAttribute(propertyKey= "HEMMING_COLOR_REPAIR",defaultValue = "0,255,0")
	public Color getHemmingColorRepair();
	
	@PropertyBeanAttribute(propertyKey= "HEMMING_COLOR_SCRAP",defaultValue = "0,255,0")
	public Color getHemmingColorScrap();
	
	@PropertyBeanAttribute(propertyKey= "HEMMING_DRAW",defaultValue = "true")
	public boolean isHemmingDraw();
	
	@PropertyBeanAttribute(propertyKey= "X_POINT_COLOR_OS",defaultValue = "255,0,0")
	public Color getXPointColorOS();
	
	@PropertyBeanAttribute(propertyKey= "X_POINT_COLOR_REPAIR",defaultValue = "255,0,0")
	public Color getXPointColorRepair();
	
	@PropertyBeanAttribute(propertyKey= "X_POINT_COLOR_SCRAP",defaultValue = "255,0,0")
	public Color getXPointColorScrap();
	
	@PropertyBeanAttribute(propertyKey= "X_POINT_FONT_SIZE",defaultValue = "20")
	public int getXPointFontSize();
	
	@PropertyBeanAttribute(propertyKey= "X_POINT_FONT_STYLE",defaultValue = "1")
	public int getXPointFontStyle();
	
	@PropertyBeanAttribute(propertyKey= "PLUS_CURSOR_NAME")
	public String getPlusCursorURL();
	
	@PropertyBeanAttribute(propertyKey= "MINUS_CURSOR_NAME")
	public String getMinusCursorURL();
	
	@PropertyBeanAttribute(propertyKey= "M_CURSOR_HOT_SPO_X",defaultValue = "2")
	public int getMCursorHotSpotX();
	
	@PropertyBeanAttribute(propertyKey= "M_CURSOR_HOT_SPO_Y",defaultValue = "2")
	public int getMCursorHotSpotY();
	
	@PropertyBeanAttribute(propertyKey= "P_CURSOR_HOT_SPO_X",defaultValue = "2")
	public int getPCursorHotSpotX();
	
	@PropertyBeanAttribute(propertyKey= "P_CURSOR_HOT_SPO_Y",defaultValue = "2")
	public int getPCursorHotSpotY();
	
	@PropertyBeanAttribute(propertyKey= "DISPLAY_IMAGE_SECTION",defaultValue = "TRUE")
	public boolean isDisplayImageSection();
	
}
