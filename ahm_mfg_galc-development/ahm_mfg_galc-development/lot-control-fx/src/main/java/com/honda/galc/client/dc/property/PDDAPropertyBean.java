package com.honda.galc.client.dc.property;

import java.util.Map;

import com.honda.galc.constant.MfgInstructionLevelType;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>PDDAPropertyBean Class description</h3>
 * <p>
 * PDDAPropertyBean description
 * </p>
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
 * @author <br>
 *         Oct 01, 2013
 * 
 * 
 */
@PropertyBean(componentId = "DEFAULT_VIOS")
public interface PDDAPropertyBean extends IProperty {

	/**
	 * If set to true the PPE information will be shown at login. 
	 */
	@PropertyBeanAttribute(propertyKey = "SHOW_PPE", defaultValue = "true")
	public boolean isShowPPE();

	/**
	 * If set to true CCP (Critical Control Part) window will be shown at login and at each model change 
	 */
	@PropertyBeanAttribute(propertyKey = "SHOW_CCP", defaultValue = "true")
	public boolean isShowCCP();

	/**
	 * Define the width in pixels of the operation list window 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_WIDTH", defaultValue = "900")
	public String getOperationListWidth();

	/**
	 * Define the height in pixels of the operation list window 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_HEIGHT", defaultValue = "400")
	public String getOperationListHeight();

	/**
	 * Define the font size of the text in the operation list 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_FONTSIZE", defaultValue = "16 px")
	public String getOperationListFontSize();

	/** 
	 * Define the highlight color of the operation list colors are 
	 * defined in this document https://docs.oracle.com/javase/8/javafx/api/javafx/scene/doc-files/cssref.html 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_HIGHLIGHT_COLOR", defaultValue = "")
	public String getOperationListHighLightColor();

	/** 
	 * Define the highlight color of the operation list colors are 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_BACKGROUND_COLOR", defaultValue = "")
	public String getOperationListBackGroundColor();

	/** 
	 * Define the text color of the operation list 
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_TEXT_COLOR", defaultValue = "")
	public String getOperationListTextColor();

	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_ALTERODDROW_COLOR", defaultValue = "")
	public String getOperationListAlterOddRowColor();

	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_ALTEREVENROW_COLOR", defaultValue = "")
	public String getOperationListAlterEvenRowColor();

	/** 
	 * Define the column titles that may appear in the operation list
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONLIST_COLUMN_LIST", defaultValue = "unitSeqNo,unitNo,unitOperationDesc,unitBasePartNo")
	public String getOperationListColumnList();

	/** 
	 * Specifies the width of the operation info region in pixels
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_WIDTH", defaultValue = "80")
	public String getOperationInfoWidth();

	/** 
	 * Specifies the height of the operation info region in pixels
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_HEIGHT", defaultValue = "600")
	public String getOperationInfoHeight();

	/** 
	 * Specifies the font size of text in the operation info region
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_FONTSIZE", defaultValue = "16 px")
	public String getOperationInfoFontSize();

	/** 
	 * Specifies the size of scroll bar in the operation info region, for touch screens the scrollbar should be wide enough
	 * to be used by an associate wearing a glove
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_SCROLLBARSIZE", defaultValue = "16 px")
	public String getOperationInfoScrollBarSize();

	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_DISPLAYROW", defaultValue = "")
	public String getOperationInfoDisplayRow();

	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_TRAINING_DISPLAYROW", defaultValue = "")
	public String getOperationInfoDisplayRowForTraining();

	/** 
	 * Specifies the width in pixels of the safety quality image 
	 */
	@PropertyBeanAttribute(propertyKey = "SAFETYQUALITYIMAGE_WIDTH", defaultValue = "400")
	public String getSafetyQualityImageWidth();

	/** 
	 * Specifies the height in pixels of the safety quality image 
	 */
	@PropertyBeanAttribute(propertyKey = "SAFETYQUALITYIMAGE_HEIGHT", defaultValue = "400")
	public String getSafetyQualityImageHeight();

	/** 
	 * Specifies the width in pixels of the product schedule widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNIT_WIDTH", defaultValue = "490")
	public String getProductionScheduleUnitWidth();

	/** 
	 * Specifies the height in pixels of the product schedule widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNIT_HEIGHT", defaultValue = "100")
	public String getProductionScheduleUnitHeight();

	/** 
	 * Specifies the font size of text in the product schedule widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNIT_FONTSIZE", defaultValue = "80")
	public String getProductionScheduleUnitFontSize();

	/** 
	 * Specifies the number of rows in product schedule widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNIT_ROWS", defaultValue = "3")
	public String getProductionScheduleUnitRows();
	
	/** 
	 * Specifies the width in pixels of the production plan  widget  width
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTION_PLAN_WIDGET_WIDTH", defaultValue = "360")
	public String getProductionPlanWidgetWidth();
	
	/** 
	 * Specifies the width in pixels of the production plan  widget height
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTION_PLAN_WIDGET_HEIGHT", defaultValue = "100")
	public String getProductionPlanWidgetHeighth();
	
	/** 
	 * Specifies the width in pixels of the production plan  widget height
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTION_PLAN_WIDGET_TRACKING_PPT", defaultValue = "")
	public String getProductionPlanTrackingProcessPoint();
	
	/** 
	 * Specifies the font size of text in the product schedule widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTION_PLAN_WIDGET_FONTSIZE", defaultValue = "100")
	public String getProductionPlanWidgetFontSize();

	/** 
	 * Specifies the width in pixels of the production schedule lot widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULELOT_WIDTH", defaultValue = "280")
	public String getProductionScheduleLotWidth();

	/** 
	 * Specifies the height in pixels of the production schedule lot widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULELOT_HEIGHT", defaultValue = "136")
	public String getProductionScheduleLotHeight();

	/** 
	 * Specifies the font size of text in the production schedule lot widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULELOT_FONTSIZE", defaultValue = "12 px")
	public String getProductionScheduleLotFontSize();

	/** 
	 * Specifies the number of rows the production schedule lot widget 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULELOT_ROWS", defaultValue = "3")
	public String getProductionScheduleLotRows();

	/** 
	 * Specifies the width in pixels of the production schedule unit view 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNITVIEWWIDGET_WIDTH", defaultValue = "240")
	public String getProductionScheduleUnitViewWidgetWidth();

	/** 
	 * Specifies the height in pixels of the production schedule unit view 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNITVIEWWIDGET_HEIGHT", defaultValue = "250")
	public String getProductionScheduleUnitViewWidgetHeight();

	/** 
	 * Specifies the font size of text the production schedule unit view 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNITVIEWWIDGET_FONTSIZE", defaultValue = "16 px")
	public String getProductionScheduleUnitViewWidgetFontSize();

	/** 
	 * Specifies the number of rows in the the production schedule unit view 
	 */
	@PropertyBeanAttribute(propertyKey = "PRODUCTIONSCHEDULEUNITVIEWWIDGET_ROWS", defaultValue = "10")
	public String getProductionScheduleUnitViewWidgetRows();

	/**
	 *  Push Timer Widget 
	 *  Push Time  : The same as line speed. The vehicle will stay at station for PushTime interval, during that time all units of
	 *               work for that station must be completed. The Push time is configured the same for all stations and is currently
	 *               set to 55 minutes. (See Note 1) 
	 *               
	 *  Process Time : The sum of all unit of work times scheduled for a process, the process time will initally be set to <= Push time.
	 *                 
	 * ** Note 1 **
     * The next Push Time can be greater than the cycle time under the following circumstances
     *  1)  There is not enough time to complete the cycle within the current schedule, in which case the cycle must continue after the break
     *      in this case the break time is added to the push time.
     *  2)  The timer has started before the first work period, this can happen if the associate starts the terminal prior to beginning of the shift. 
     *      In this case the time to the start of the first period will be added to the push time.
	*/
	@PropertyBeanAttribute(defaultValue = "55")
	public int getPushTimerMins();	

	/**
	 * Defines the size of the push timer widget in pixels 
	 */
	@PropertyBeanAttribute(defaultValue = "95")
	public int getPushTimerWidgetSize();
	
	/**
	 * Defines the size of the push timer widget in pixels 
	 */
	@PropertyBeanAttribute(defaultValue = "95")
	public int getUnitProgressTimerWidgetSize();
	
	/**
	 * Defines the size of the push timer widget in pixels 
	 */
	@PropertyBeanAttribute(defaultValue = "95")
	public int getAccumulatedTimerWidgetSize();

	/**
	 * If the associate lags between TimerLimitOK and TimerLimitWarn minutes behind in their process the process timer color will be changed from Green to Yellow.    
	 */
	@PropertyBeanAttribute(defaultValue = "5")
	public int getPushTimerLimitOK();

	/**
	 * If the associate lags over TimerLimitWARN minutes behind in their process the process timer color will be changed from Yellow to Red.    
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	public int getPushTimerLimitWARN();

	/**
	 * This property specifies a comma separated list of dates and offsets  e.g  MM/d/yy HH:mm  [0-9]+, . The property is set by the oif job and the teamleader screen
	 * and is used to make adjustments of the push time at various times of the day.     
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getPushTimerAdjustment();

	/**
	 * Specifies the width of the unit part list widget in pixels 
	 */
	@PropertyBeanAttribute(propertyKey = "UNIT_PART_LIST_WIDTH", defaultValue = "600")
	public String getUnitPartListWidth();

	/**
	 * Specifies the height of the unit part list widget in pixels 
	 */
	@PropertyBeanAttribute(propertyKey = "UNIT_PART_LIST_HEIGHT", defaultValue = "400")
	public String getUnitPartListHeight();

	/**
	 * Specifies the font size of the unit part list widget 
	 */
	@PropertyBeanAttribute(propertyKey = "UNIT_PART_LIST_COLUMN_FONT",defaultValue = "16 px")
	public String getUnitPartListFontSize();

	/**
	 * Specifies the width of the Unit Navigator Widget in pixels 
	 */
	@PropertyBeanAttribute(defaultValue = "250")
	public int getUnitNavigatorWidth();

	/**
	 * Specifies the font size the Unit Navigator Widget  
	 */
	@PropertyBeanAttribute(defaultValue = "20 px")
	public String getUnitNavigatorFontSize();
	
	/**
	 * In simple mode the search option and fast travel map and scrollbar will not be shown, this option is suitable for process points where the 
	 * number of units shown on the unit navigator is small i.e less that 15.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUnitNavigatorSimpleMode();

	/**
	 * Specifies the font size the Unit Navigator Widget  
	 */
	@PropertyBeanAttribute(defaultValue = "90")
	public int getUnitNavigatorOperationWidth();
	
	@PropertyBeanAttribute(defaultValue = "12")
	public int getDcViewFontSize();
	
	/**
	 * Specifies the height of the torque box in pixels  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_TEXT_FIELD_HEIGHT",defaultValue = "48")
	public int getInputBoxHeight();
	
	/**
	 * Specifies the width of the part mask label in pixels  
	 */
	@PropertyBeanAttribute(defaultValue = "150")
	public int getPartMaskLabelWidth();
	
	/**
	 * Specifies the width of the part scan box in pixels  
	 */
	@PropertyBeanAttribute(defaultValue = "350")
	public int getPartScanBoxWidth();
		
	/**
	 * Specifies maximum number of torque measurements per column
	 */
	@PropertyBeanAttribute(defaultValue = "5")
	public int getTorqueBoxesPerColumn();
	
	/**
	 * Specifies the width of the torque box in pixels  
	 */
	@PropertyBeanAttribute(defaultValue = "60")
	public int getTorqueBoxWidth();
	
	/** Process Cycle Timer Line Speed, e.g BLOCK_A=42   */ 
	@PropertyBeanAttribute(defaultValue = "")
	public Map<String,Integer> getLineSpeed(Class<?> clasz);
	
	/** Process Cycle Timer Count Start Second   */ 
	@PropertyBeanAttribute(defaultValue = "3")
	public int getCountStartSecond();
	
	/** Specifies the Image Width of the Operation MainImage Widget   */ 
	@PropertyBeanAttribute(defaultValue = "600")
	public int getOperationMainImageWidth();
	
	/**
	 * Specifies the number of units in Navigator Widget 
	 */
	@PropertyBeanAttribute(defaultValue = "18")
	public int getMaxView();
	
	/**
	 * Level of Mfg Instructions to show 
	 */
	@PropertyBeanAttribute(defaultValue = "ALL")
	public MfgInstructionLevelType getMfgInstructionLevel();
	
	/**
	 * Possible resolutions  
	 */
	@PropertyBeanAttribute(propertyKey = "OPERATIONINFO_RESOLUTIONS", defaultValue = "1024*768")
	public String getPossibleResolutions();

	
	/**
	 * Right panel command button height
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_COMMAND_BUTTON_HEIGHT", defaultValue = "37")
	public double getCommandBtnHeight();

	/**
	 * Right panel command image height 
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_COMMAND_IMAGE_HEIGHT", defaultValue = "15")
	public double getCommandImageHeight();

	/**
	 * Right panel command button width  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_COMMAND_BUTTON_WIDTH", defaultValue = "30")
	public double getCommandBtnWidth();

	/**
	 * Right panel command image width  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_COMMAND_IMAGE_WIDTH", defaultValue = "15")
	public double getCommandImageWidth();

	/**
	 * Right panel Torque panel width  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_TORQUE_PANEL_WIDTH", defaultValue = "230")
	public double getTorquePanelWidth();

	/**
	 * Right panel Torque panel width  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_TORQUE_PANEL_FONT", defaultValue = "25")
	public int getTorquePanelFont();

	/**
	 * Right panel Torque panel width  
	 */
	@PropertyBeanAttribute(propertyKey = "DC_VIEW_TORQUE_PANEL_HEADING_FONT", defaultValue = "18")
	public int getTorquePanelHeadingFont();

	/** Specifies the Image Width of the Operation SingleImage Widget(Bumper use only)   */ 
	@PropertyBeanAttribute(defaultValue = "600")
	public int getOperationSingleImageWidth();
	
	/**
	 * Display Single PDDA Image with GALC Functionality
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDisplaySingleImage();

	/**
	 * Display Single UNIT PDDA Image with GALC Functionality
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isDisplayUnitSingleImage();
	
	/**
	 * Complete/Reject button font size
	 */
	@PropertyBeanAttribute(defaultValue = "16")
	public int getCompleteRejectButtonFont();
	
	/**
	 * Complete/Reject button font size
	 */
	@PropertyBeanAttribute(defaultValue = "150")
	public int getCompleteRejectButtonPrefWidth();
	
	/**
	 * Complete/Reject button font size
	 */
	@PropertyBeanAttribute(defaultValue = "40.0")
	public double getCompleteRejectNormalizedImageSize();
	
	/**
	 * Volt Meter Host  
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getVoltMeterHost();
	/**
	 * Volt Meter Port  
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getVoltMeterPort();
	/**
	 * Volt Meter Command Format  
	 */
	@PropertyBeanAttribute(defaultValue = "MEAS:VOLT:DC?,MEAS:COND?")
	public String getVoltMeterCommandFormat();
	/**
	 * Volt Meter Unit  
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getVoltMeterUnit();
	/**
	 * BroadCast to device
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isBroadcastToDevice();

	/**
	 * Minimum height to display unit images 
	 */
	@PropertyBeanAttribute(defaultValue = "150")
	public int getMinUnitImageHeight();
	
	
	/**
	 * get the delete authorization group.
	 *
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDeleteAuthorizationGroup();
	
	/**
	 * Specifies the Height of the timer text boxes in Advance Process Cycle Timer widget
	 */
	@PropertyBeanAttribute(propertyKey = "ADV_PRC_CYCLE_TIMER_TXT_HEIGHT", defaultValue = "60")
	public Integer getAdvPrcCycleTimerTxtHeight();
	
	/**
	 * Specifies the Width of the timer text boxes in Advance Process Cycle Timer widget
	 */
	@PropertyBeanAttribute(propertyKey = "ADV_PRC_CYCLE_TIMER_TXT_WIDTH", defaultValue = "60")
	public Integer getAdvPrcCycleTimerTxtWidth();
	
	/**
	 * Specifies the Font size of the timer text boxes in Advance Process Cycle Timer widget. It is in String format because, needs to mention 1=60,2=50 and so on.
	 * That means if the text box has one character, then the font size will be 60, if it is 2 chars, then the font size will be 50 and so on
	 */
	@PropertyBeanAttribute(propertyKey = "ADV_PRC_CYCLE_TIMER_FONT_SIZE", defaultValue = "60")
	public String getAdvPrcCycleTimerFontSize();

	@PropertyBeanAttribute(defaultValue = "INHOUSE_OFF_PP")
	public String getInHouseProductOffProcessPoint();
	

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isInHouseTrackingEnabled();
	
	
}

