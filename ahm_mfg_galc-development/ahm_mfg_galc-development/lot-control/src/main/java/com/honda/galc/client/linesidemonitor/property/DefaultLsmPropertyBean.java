package com.honda.galc.client.linesidemonitor.property;

import java.awt.Color;
import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>DefaultLsmPropertyBean Class description</h3>
 * <p> DefaultLsmPropertyBean description </p>
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
 * Mar 16, 2011
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
@PropertyBean(componentId ="DEFAULT_LINE_SIDE_MONITOR")
public interface DefaultLsmPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.linesidemonitor.controller.LineSideMonitorController")
	public String getControllerClass();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.linesidemonitor.view.LineSideMonitorPanel")
	public String getMainPanelClass();
	
	@PropertyBeanAttribute(defaultValue = "0")
	public int getProcessedProductNumber();
	
	@PropertyBeanAttribute(defaultValue = "0")
	public int getUpcomingProductNumber();

	/**
	 * Number of rows to display in the table's scrollable viewport.<br>
	 * Does nothing when less than 0.
	 */
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getViewportRowCount();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isHaveCheckBoxColumn();
	
	/**
	 * Target process point - actual process point 
	 * if null, user manually mark the product processed
	 * @return
	 */
	public String getTargetProcessPoint();
	
	/**
	 * when subscribing to IProductPassedEvent, refresh process points 
	 * defines the valid process points to trigger refresh
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getRefreshProcessPoints();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isRefreshOnProductChange();
	
	public String getProductSequenceProcessPoint();
	
	@PropertyBeanAttribute(defaultValue = "200,200,200")
	public Color getBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "255,0,0")
	public Color getHighlightColor();
	
	@PropertyBeanAttribute(defaultValue = "255,255,255")
	public Color getHighlightForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getCutLotBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getCutLotForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getNewLotBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getNewLotForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getRebuildBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getRebuildForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getStragglerBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getStragglerForegroundColor();
	
	@PropertyBeanAttribute
	public String getStragglerPpDelayedAt();
	
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getBackgroundColorChangeColumnIndex();
	
	@PropertyBeanAttribute(defaultValue = "20")
	public int getFontSize();
	
	@PropertyBeanAttribute(defaultValue = "13")
	public int getHeaderFontSize();
	
	@PropertyBeanAttribute(defaultValue = "200,200,100")
	public Color getAlternateBackgroundColor();
	
	@PropertyBeanAttribute(defaultValue = "0,0,0")
	public Color getAlternateForegroundColor();
	
	@PropertyBeanAttribute(defaultValue = "30")
	public int getItemHeight();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isPollingLastProduct();
	
	/**
	 * The alignment of the table pane's data.<br>
	 * CENTER, LEFT, RIGHT, LEADING or TRAILING
	 */
	@PropertyBeanAttribute(defaultValue = "CENTER")
	public String getTablePaneAlignment();
	
	@Deprecated
	@PropertyBeanAttribute(defaultValue = "0.5")
	public double getTablePaneWeight();
	
	@Deprecated
	@PropertyBeanAttribute(defaultValue = "0.0")
	public double getInfoPaneWeight();
	
	@Deprecated
	@PropertyBeanAttribute(defaultValue = "0.5")
	public double getDataCollectionPaneWeight();
	
	/**
	 * The divider location used when the info is visible.
	 */
	@PropertyBeanAttribute(propertyKey ="INFO_DIVIDER_LOCATION",defaultValue = "-1")
	public int getInfoDividerLocation();
	
	/**
	 * Flag indicating whether the info divider cannot be adjusted by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInfoDividerLocationLocked();
	
	/**
	 * Flag indicating whether adjustments to the info divider location should be persisted.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInfoDividerLocationPersistent();
	
	/**
	 * The divider location used when data collection is allowed.
	 */
	@PropertyBeanAttribute(propertyKey ="DATA_COLLECTION_DIVIDER_LOCATION",defaultValue = "-1")
	public int getDataCollectionDividerLocation();
	
	/**
	 * Flag indicating whether the data collection divider cannot be adjusted by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDataCollectionDividerLocationLocked();
	
	/**
	 * Flag indicating whether adjustments to the data collection divider location should be persisted.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDataCollectionDividerLocationPersistent();
	
	/**
	 * Flag indicates that the data collection panel should be beside the table for wide screens.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isWidescreen();
	
	/**
	 * flag to indicate if the client is to subscribe the processed product event from other sources
	 * e.g. from a lot control client
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey ="SUBSCRIBE_PROCESSED_PRODUCT",defaultValue = "false")
	public boolean isSubscribingProcessedProduct();
	
	/**
	 * Flag indicates if supporting Product Id sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdFromPlc();
	
	/**
	 * Flag indicates if supporting Product Id auto refresh signal sent from PLC
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdRefreshFromPlc();
	
	/**
	 * Flag indicates if supporting Product Id highlight row signal sent from PLC
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdHighlightFromPlc();
	
	// in seconds
	@PropertyBeanAttribute(defaultValue = "10")
	public int getPollingLastProductFrequency();
	
	/**
	 * Product type - Engine or Frame
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "Engine")
	public String getProductType();
	
	/**
	 * place the highlighted bar in the predefined position from the top
	 * of the display window. if the value = -1, place in the middle of the 
	 * display window.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	public int getHighlightPosition();
	
	@PropertyBeanAttribute
	public Map<String,String>getHighlightRowOffset();
	
	@PropertyBeanAttribute
	public Map<String, String> getHighlightRowColor();

	/**
	 * define if calculating the product sequence list from "in process product" 176tbx
	 *  table or from "product sequence" table
	 *  default is from in process product table
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isSequenceFromInProcessProduct();
	
	/**
	 * Define if the info pane should be visible
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInfoVisible();
	
	/**
	 * List of column names which will not be displayed in the table<br>
	 * (but are still accessible to the program)
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getHiddenColumns();
	
	/**
	 * Define if the line side monitor has a data collection component
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAllowDataCollection();
	
	/**
	 * Define if cache mode should be enabled.<br>
	 * Cache mode causes the lsm to update much faster, but changes to existing data may not be reflected on screen.
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isAllowCacheMode();
	
	/**
	 * Define if the expected product should automatically be used as the product scan
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseExpectedAsScan();
	
	/**
	 * set the alarm on when the column change appears at the bottom of the data list
	 */
	@PropertyBeanAttribute(propertyKey ="ALARM_ON_OFFSET_BOTTOM",defaultValue = "false")
	public boolean isAlarmOnOffsetBottom();
	
	/**
	 * set the alarm on when the column change is the current product
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAlarmOnOffsetCurrent();
	
	/**
	 * set the alarm on when the column change is at defined offset to the current product
	 */
	@PropertyBeanAttribute(defaultValue = "-1000")
	public int getAlarmOffset();
	
	/**
	 * Used to map a color to an identifier.<br>
	 * When a ProductIdHighlight request is received, the corresponding product's row is highlighted with the color mapped to the identifier. 
	 */
	@PropertyBeanAttribute
	public Map<String, Color> getHighlightRowColor(Class<Color> clazz);
	
	/**
	 * Make the alarm sound configurable
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "playRepeatedNgSound")
	public String getAlarmSoundPlay();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProcessEmptyEnabled();
	
	
}
