package com.honda.galc.client.datacollection.property;

import java.util.Map;

import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * <h3>ViewProperty</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ViewProperty description </p>
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
 *  <TR>
 * <TD>Meghana Ghanekar</TD>
 * <TD>Jan 11, 2011</TD>
 * <TD>0.1</TD>
 * <TD>Added the methods related to FormFeed.</TD>
 * <TD>Added the methods related to FormFeed.</TD>
 * </TR>
 * </TABLE>
 *   
 * @author Paul Chou
 * Mar 17, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public interface ViewProperty {
	//--- view layout related properties
	//The ratio of torque field width showing torque status and torque value
	double getTorqueWidthRatio();

	int getMaxNumberOfPart();
	int getMaxNumberOfTorque();

	int getTorqueStartPositionX();
	int getTorqueStartPositionY();
	int getTorqueFieldGap();
	
	@PropertyBeanAttribute(defaultValue = "90")
	int getTorqueLabelHeight();
	
	@PropertyBeanAttribute(defaultValue = "102")
	int getTorqueLabelWidth();
	
	
	@PropertyBeanAttribute(defaultValue = "-1")
	int getPartStartPositionX();
	@PropertyBeanAttribute(defaultValue = "-1")
	int getPartStartPositionY();
	
	//Torque field width showing torque value
	int getTorqueFieldWidth();
	int getTorqueFieldHeight();
	
	@PropertyBeanAttribute(defaultValue = "18")
	int getProductIdLabelFontSize();
	
	@PropertyBeanAttribute(defaultValue = "36")
	int getProductIdTextFieldFontSize();
	
	@PropertyBeanAttribute(defaultValue = "25")
	int getAfOnSeqNumLabelFontSize();
	
	@PropertyBeanAttribute(defaultValue = "65")
	int getAfOnSeqNumFontSize();
	
	@PropertyBeanAttribute(defaultValue = "5")
	int getAfOnSeqNumDisplayLength();
	
	@PropertyBeanAttribute(defaultValue = "48")
	int getTorqueFontSize();
	
	@PropertyBeanAttribute(defaultValue = "18")
	int getPartLabelFontSize();
	
	@PropertyBeanAttribute(defaultValue = "36")
	int getPartSnFontSize();
	
	@PropertyBeanAttribute(defaultValue = "50")//Default 0
	int getSeparatorGap();
	
	@PropertyBeanAttribute(defaultValue = "25")
	int getProductCountLabelFontSize();
	
	@PropertyBeanAttribute(defaultValue = "30")
	int getProductCountValueFontSize();

	//Default value shows in torque text field when init/refresh client
	String getDefaultTorqueValue();

	//Button properties
	int getButtonPositionX();

	int getButtonPositionY();

	int getButtonGap();

	int getButtonWidth();

	int getButtonHeight();

	int getButtonFontSize();
	
	//Form Feed Button properties 
	int getFormfeedButtonPositionX();

	int getFormfeedButtonPositionY();

	int getFormfeedButtonWidth();

	int getFormfeedButtonHeight();

	int getFormfeedButtonFontSize();
	
	//Form Feed Spinner properties
	int getFormfeedSpinnerPositionX();

	int getFormfeedSpinnerPositionY();

	int getFormfeedSpinnerWidth();

	int getFormfeedSpinnerHeight();

	int getFormfeedSpinnerFontSize();
	
	String getPanelType();
	
	//Last Pid Label
	@PropertyBeanAttribute(defaultValue = "2")
	int getLastPidLabelX();
	
	@PropertyBeanAttribute(defaultValue = "0")
	int getLastPidLabelY();
	
	@PropertyBeanAttribute(defaultValue = "-999")
	int getLastPidLabelWidth();

	@PropertyBeanAttribute(defaultValue = "215")
	int getLastPidTextFieldX();
	
	@PropertyBeanAttribute(defaultValue = "586")
	int getLastPidTextFieldY();
	
	@PropertyBeanAttribute(defaultValue = "320")
	int getLastPidTextFieldWidth();
	
	@PropertyBeanAttribute(defaultValue = "51")
	int getLastPidTextFieldHeight();
	
	//--- Common View Properties
	
	public int getMaxPartTextLength();

	/**
	 * Product spec label name
	 * @return
	 */
	@PropertyBeanAttribute
	public String getProdSpecLabel();
	
	public String getAfOnSeqNumLabel();

	public String getExpectProductIdLabel();
	

	public String getProductIdLabel();
	
	@PropertyBeanAttribute(defaultValue = "COUNT")
	public String getProductCountLabel();
	
	@PropertyBeanAttribute()
	public int getMaxProductSnLength();
	
	//Show torque value (20.01) or status (OK/NG)
	boolean isShowTorqueAsValue();
	
	/**
	 * Flag to indicate if Measurement text field is editable
	 * true  - editable
	 * false - not editable
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isMeasurementEditable(); 
	
	@PropertyBeanAttribute(defaultValue = "true")
	boolean isMeasurementOnSkip();
	
	@PropertyBeanAttribute
	public Map<String, String> getButtonLabelMap();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMonitorSkippedProduct();
	
	@PropertyBeanAttribute(defaultValue = "210")
	public int getSkippedProductMonitorPanelWidth();
	
	@PropertyBeanAttribute(defaultValue = "116")
	public int getSkippedProductMonitorPanelHeight();
	
	@PropertyBeanAttribute(defaultValue = "18")
	public int getSkippedProductMonitorPanelFontSize();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowProductSubid();

	@PropertyBeanAttribute(defaultValue = "40")
	public int getStatusLabelWidth();

	@PropertyBeanAttribute(defaultValue = "1")
	int getGap();
	
	@PropertyBeanAttribute
	public Map<String, String> getSubidColorMap();
	
	/**
	 * Product Id Button or Label
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductIdButton();
	@PropertyBeanAttribute(defaultValue = "184")
	public int getProductIdButtonX();
	@PropertyBeanAttribute(defaultValue = "25")
	public int getProductIdButtonY();
	@PropertyBeanAttribute(defaultValue = "185")
	public int getProductIdButtonWidth();
	@PropertyBeanAttribute(defaultValue = "47")
	public int getProductIdButtonHeight();
	@PropertyBeanAttribute(defaultValue = "184")
	public int getProductIdLabelX();
	@PropertyBeanAttribute(defaultValue = "25")
	public int getProductIdLabelY();
	@PropertyBeanAttribute(defaultValue = "185")
	public int getProductIdLabelWidth();
	@PropertyBeanAttribute(defaultValue = "47")
	public int getProductIdLabelHeight();

	/**
	 * Product Id Text Field
	 */
	@PropertyBeanAttribute(defaultValue = "378")
	public int getProductIdTextFieldX();
	@PropertyBeanAttribute(defaultValue = "25")
	public int getProductIdTextFieldY();
	@PropertyBeanAttribute(defaultValue = "450")
	public int getProductIdTextFieldWidth();
	@PropertyBeanAttribute(defaultValue = "45")
	public int getProductIdTextFieldHeight();

	/**
	 * Af On Seq Num
	 */
	@PropertyBeanAttribute(defaultValue = "838")
	public int getAfOnSeqNumX();
	@PropertyBeanAttribute(defaultValue = "61")
	public int getAfOnSeqNumY();
	@PropertyBeanAttribute(defaultValue = "300")
	public int getAfOnSeqNumWidth();
	@PropertyBeanAttribute(defaultValue = "60")
	public int getAfOnSeqNumHeight();

	/**
	 * Product Count
	 */
	@PropertyBeanAttribute(defaultValue = "925")
	public int getProductCountNumX();
	@PropertyBeanAttribute(defaultValue = "61")
	public int getProductCountNumY();
	@PropertyBeanAttribute(defaultValue = "90")
	public int getProductCountNumWidth();
	@PropertyBeanAttribute(defaultValue = "60")
	public int getProductCountNumHeight();

	
	/**
	 * flag to show sub product id field
	 * default to false - existing client should work the same way as today
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowSubId();
	
	/**
	 * flag to show device tab on the system info panel
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowDevicesTab();
	
	// If the property is true,sets Last Product ID textbox as unfocusable
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isLastPidTextFieldUnfocusable();

	@PropertyBeanAttribute(defaultValue = "10")
	int getRepairPartsTableStartPositionX();
	
	@PropertyBeanAttribute(defaultValue = "50")
	int getRepairPartsTableStartPositionY();
	
	@PropertyBeanAttribute(defaultValue = "250")
	int getRepairPartsTableHeight();
	
	@PropertyBeanAttribute(defaultValue = "250")
	int getRepairPartsTableWidth();
	
	@PropertyBeanAttribute(defaultValue = "16")
	int getRepairPartsTableRowHeight();
	
	@PropertyBeanAttribute(defaultValue = "450")
	int getRemoveResultButtonStartPositionX();
	
	@PropertyBeanAttribute(defaultValue = "350")
	int getRemoveResultButtonStartPositionY();
	
	@PropertyBeanAttribute(defaultValue = "40")
	int getRemoveResultButtonHeight();
	
	@PropertyBeanAttribute(defaultValue = "150")
	int getRemoveResultButtonWidth();
	
	@PropertyBeanAttribute(defaultValue = "Remove Result")
	String getRemoveResultButtonLabel();
	
    @PropertyBeanAttribute(defaultValue = "false") 
    public boolean isNotAutoPopulateProductIdCellExit(); 

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isEnableSkipProductIdKey();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isEnablePreviousProductIdKey();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isEnableSkipPartKey();
 
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowProcessedCounter();

	@PropertyBeanAttribute(defaultValue = "2")
	public int getProcessedCounterPanelPositionX();

	@PropertyBeanAttribute(defaultValue = "122")
	public int getProcessedCounterPanelPositionY();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isEnableRefreshAfterProductScan();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowLastEngine();
	
	@PropertyBeanAttribute(defaultValue = "Lot Number")
	String getLotNumberLabel();
	

	/**
	 *  Flag to indicate to show Upcoming Products 
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isShowUpcomingProducts();

	/**
	 *  Flag to indicate to show Create Empty Carrier Button 
	 * 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isProcessEmptyEnabled();
	

	/* Last AF On Seq Number Text Field properties */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowLastAfOnSeqNumber();
	
	@PropertyBeanAttribute(defaultValue = "128")
	public int getLastAfOnSeqTextFieldX();
	
	@PropertyBeanAttribute(defaultValue = "84")
	public int getLastAfOnSeqTextFieldWidth();
	
	@PropertyBeanAttribute(defaultValue = "36")
	public int getLastAfOnSeqTextFieldFontSize();
	
	/* Last Af On Seq Number Label properties */
	@PropertyBeanAttribute(defaultValue = "130")
	public int getLastAfOnSeqLabelX();
	
	@PropertyBeanAttribute(defaultValue = "Af On Seq:")
	public String getLastAfOnSeqLabelText();
	
	@PropertyBeanAttribute(defaultValue = "14")
	public int getLastAfOnSeqLabelFontSize();
	
	/* Last MTO Text Field properties */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isShowLastMto();
	
	@PropertyBeanAttribute(defaultValue = "5")
	public int getLastMtoTextFieldX();
	
	@PropertyBeanAttribute(defaultValue = "120")
	public int getLastMtoTextFieldWidth();
	
	@PropertyBeanAttribute(defaultValue = "20")
	public int getLastMtoTextFieldFontSize();
	
	/* Last MTO label properties */
	@PropertyBeanAttribute(defaultValue = "7")
	public int getLastMtoLabelX();
	
	@PropertyBeanAttribute(defaultValue = "Last MTO:")
	public String getLastMtoLabelText();
	
	@PropertyBeanAttribute(defaultValue = "14")
	public int getLastMtoLabelFontSize();
}
