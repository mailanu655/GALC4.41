package com.honda.galc.client.schedule;

import java.awt.Color;
import java.util.Map;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;
/**
 * <h3>Class description</h3>
 * Default properties used by schedule client.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
/**
 *
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */

@PropertyBean(componentId = "DEFAULT_SCHEDULE_CLIENT")
public interface DefaultScheduleClientProperty extends SystemPropertyBean {

	public static final String COLUMN_HEADINGS = "COLUMN_HEADINGS";
	public static final String METHOD_NAMES = "METHOD_NAMES";
	public static final String POPUP_MENU_ITEMS = "POPUP_MENU_ITEMS";
	public static final String ROW_HEIGHT = "ROW_HEIGHT";
	public static final String ROW_COUNT = "ROW_COUNT";
	public static final String FONT = "FONT";
	public static final String PANEL_NAME = "PANEL_NAME";
	public static final String PANEL_SIZE = "PANEL_SIZE";
	public static final String DRAG_AND_DROP_ENABLED = "DRAG_AND_DROP";
	public static final String PANEL_HIGHT = "PANEL_HIGHT";
	public static final String IS_MOVE_BY_KD_LOT = "IS_MOVE_BY_KD_LOT";
	public static final String IS_PROCESSED_PRODUCT_OR_LOT = "IS_PROCESSED_PRODUCT_OR_LOT";
	public static final String HIGHLIGHT_COLUMN = "HIGHLIGHT_COLUMN";
	public static final String HIGHLIGHT_COLOR = "HIGHLIGHT_COLOR";
	public static final String HIGHLIGHT_VALUE = "HIGHLIGHT_VALUE";
	public static final String PROCESS_PRODUCT = "PROCESS_PRODUCT";
	public static final String RESET = "RESET";
	public static final String SHOW_CURRENT_PRODUCT_LOT = "SHOW_CURRENT_PRODUCT_LOT";
	public static final String CHANGE_LOT_SIZE = "CHANGE_LOT_SIZE";
	public static final String CHECK_DUPLICATE_EXPECTED_PRODUCT_ID = "CHECK_DUPLICATE_EXPECTED_PRODUCT_ID";
	public static final String PROCESS_MULTI_PRODUCT = "PROCESS_MULTI_PRODUCT";
	public static final String IS_MULTIPLE_ACTIVE_LOTS ="IS_MULTIPLE_ACTIVE_LOTS";


	//  Processed panel
	@PropertyBeanAttribute(defaultValue = "Processed Product")
	public String getProcessedPanelName();

	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isProcessedProductOrLot();

	@PropertyBeanAttribute(defaultValue = "getProductionLot,getKdLot,getLotSize,getProductSpecCode,getSendStatus,getSentTimestamp,getStampedCount,getNextProductionLot")
	public String[] getProcessedMethodNames();

	@PropertyBeanAttribute(defaultValue = "Production Lot,KD Lot,Lot Size,Spec,Sent,Sent Timestamp,Stamped Count,Next Production Lot")
	public String[] getProcessedColumnHeadings();

	@PropertyBeanAttribute(defaultValue = "20")
	public int getProcessedRowHeight();

	@PropertyBeanAttribute(defaultValue = "10")
	public int getProcessedRowCount();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProcessedMenuItems();

	/**
	 * defines the security groups for the menu items of processed lot/product panel
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getProcessedMenuUserGroups();

	@PropertyBeanAttribute(defaultValue = "Dialog")
	public String getProcessedFontName();

	@PropertyBeanAttribute(defaultValue = "1")
	public int getProcessedFontStyle();

	@PropertyBeanAttribute(defaultValue = "12")
	public int getProcessedFontSize();

	@PropertyBeanAttribute(defaultValue = "0")
	public int getProcessedPanelSize();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getProcessedDragAndDropEnabled();

	@PropertyBeanAttribute(defaultValue = "20")
	public int getCurrentRowHeight();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getCurrentMenuItems();

	/**
	 * defines the security groups for the menu items of the current lot panel
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getCurrentMenuUserGroups();

	@PropertyBeanAttribute(defaultValue = "Dialog")
	public String getCurrentFontName();

	@PropertyBeanAttribute(defaultValue = "1")
	public int getCurrentFontStyle();

	@PropertyBeanAttribute(defaultValue = "12")
	public int getCurrentFontSize();

	@PropertyBeanAttribute(defaultValue = "150")
	public int getCurrentPanelPreferedHight();

	@PropertyBeanAttribute(defaultValue = "getProductionLot,getKdLot,getLotSize,getProductSpecCode,getSendStatus,getSentTimestamp,getStampedCount,getNextProductionLot")
	public String[] getCurrentMethodNames();

	@PropertyBeanAttribute(defaultValue = "Production Lot,KD Lot,Lot Size,Spec,Sent,Sent Timestamp,Stamped Count,Next Production Lot")
	public String[] getCurrentColumnHeadings();

//	Upcoming panel
	@PropertyBeanAttribute(defaultValue = "Upcoming Lot")
	public String getUpcomingPanelName();

	@PropertyBeanAttribute(defaultValue = "getProductionLot,getKdLot,getLotSize,getProductSpecCode,getSendStatus,getSentTimestamp,getStampedCount,getNextProductionLot")
	public String[] getUpcomingMethodNames();

	@PropertyBeanAttribute(defaultValue = "Production Lot,KD Lot,Lot Size,Spec,Sent,Sent Timestamp,Stamped Count,Next Production Lot")
	public String[] getUpcomingColumnHeadings();

	@PropertyBeanAttribute(defaultValue = "20")
	public int getUpcomingRowHeight();

	@PropertyBeanAttribute(defaultValue = "Hold,Move Up,Move Down,Select Last Lot")
	public String[] getUpcomingMenuItems();

	/**
	 * defines the security groups for the menu items of upcoming lot panel
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getUpcomingMenuUserGroups();

	@PropertyBeanAttribute(defaultValue = "Dialog")
	public String getUpcomingFontName();

	@PropertyBeanAttribute(defaultValue = "1")
	public int getUpcomingFontStyle();

	@PropertyBeanAttribute(defaultValue = "12")
	public int getUpcomingFontSize();

	@PropertyBeanAttribute(defaultValue = "700")
	public int getUpcomingPanelSize();

	@PropertyBeanAttribute(defaultValue = "-1")
	public int getUpcomingHighlightColumn();

	@PropertyBeanAttribute(defaultValue = "")
	public String getUpcomingHighlightValue();

	@PropertyBeanAttribute(defaultValue = "0,255,255")
	public Color getUpcomingHighlightColor();


	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getUpcomingDragAndDropEnabled();

//	On hold panel
	@PropertyBeanAttribute(defaultValue = "On Hold Lot")
	public String getOnHoldPanelName();

	@PropertyBeanAttribute(defaultValue = "getProductionLot,getKdLot,getLotSize,getProductSpecCode,getSendStatus,getSentTimestamp,getStampedCount,getNextProductionLot")
	public String[] getOnHoldMethodNames();

	@PropertyBeanAttribute(defaultValue = "Production Lot,KD Lot,Lot Size,Spec,Sent,Sent Timestamp,Stamped Count,Next Production Lot")
	public String[] getOnHoldColumnHeadings();

	@PropertyBeanAttribute(defaultValue = "20")
	public int getOnHoldRowHeight();

	@PropertyBeanAttribute(defaultValue = "Release")
	public String[] getOnHoldMenuItems();

	/**
	 * defines the security groups for the menu items of the on hold lot panel
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,String> getOnHoldMenuUserGroups();

	@PropertyBeanAttribute(defaultValue = "Dialog")
	public String getOnHoldFontName();

	@PropertyBeanAttribute(defaultValue = "1")
	public int getOnHoldFontStyle();

	@PropertyBeanAttribute(defaultValue = "12")
	public int getOnHoldFontSize();

	@PropertyBeanAttribute(defaultValue = "0")
	public int getOnHoldPanelSize();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean getOnHoldDragAndDropEnabled();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPlanCode();

	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowCurrentProductLot();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHideOnHoldPanel();

	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isChangeLotSize();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUpdateReplicatedSchedule();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getTargetProcessLocation();

	@PropertyBeanAttribute(defaultValue = "true")
	public boolean getUseNextProductionLot();

	@PropertyBeanAttribute(defaultValue = "Go WeldOn")
	public String[] getProcessedProductStampingSeqMenuItems();

	@PropertyBeanAttribute(defaultValue = "Processed Product Stamping Products")
	public String getProcessedProductStampingSeqPanelName();

	@PropertyBeanAttribute(defaultValue = "getProductId,getSendStatus")
	public String[] getProcessedProductStampingSeqMethodNames();

	@PropertyBeanAttribute(defaultValue = "Product Id,Sent Status")
	public String[] getProcessedProductStampingSeqColumnHeadings();

	@PropertyBeanAttribute(defaultValue = "Upcoming Product Stamping Products")
	public String getUpcomingProductStampingSeqPanelName();

	@PropertyBeanAttribute(defaultValue = "getProductId,getProductionLot,getStampingSequenceNumber")
	public String[] getUpcomingProductMethodNames();

	@PropertyBeanAttribute(defaultValue = "Product Id,Production Lot,Seq No")
	public String[] getUpcomingProductColumnHeadings();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getUpcomingProductStampingSeqMenuItems();

	@PropertyBeanAttribute(defaultValue = "350")
	public int getUpcomingProductPanelSize();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProductStampingInfoEnabled();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isAutoRefreshEnabled();

	@PropertyBeanAttribute(defaultValue = "60000")
	public int getAutoRefreshInterval();

	@PropertyBeanAttribute(defaultValue = "0")
	public int getScheduleDisplayOffset();

	// defines the product type to be used for add lot dialog window
	@PropertyBeanAttribute(defaultValue = "")
	public String getAddLotDialogProductType();

	//defines the maximum KD lot size for add lot dialog window
	@PropertyBeanAttribute(defaultValue = "120")
	public int getAddLotDialogMaxKdLotSize();

	//defines if product stamping sequence records needs to generated when a service lot is created.
	@PropertyBeanAttribute(defaultValue="false")
	boolean isProductStampingSeqGenerationEnabled();

	/**
	 * determines how many lots for which products will
	 * be shown in the "Upcoming Products" panel
	 *
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public int getUpcomingProductsMaxLots();

	@PropertyBeanAttribute(defaultValue ="SERVICE_LOT_COMBINATION")
	public String getServiceLotCombination();

	@PropertyBeanAttribute(defaultValue ="false")
	boolean isAssignMbpnToProdLot();
	
	@PropertyBeanAttribute(defaultValue = "9")
	public int getServiceLotCombinationNumber();
	
	@PropertyBeanAttribute(defaultValue ="false")
	boolean isMultipleActiveLots();
	
}


