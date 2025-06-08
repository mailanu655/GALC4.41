package com.honda.galc.client.datacollection.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
/**
 * 
 * <h3>ViewManagerPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ViewManagerPropertyBean description </p>
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
 * @author Paul Chou
 * Apr 23, 2010
 *
 */
 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
@PropertyBean(componentId ="Default_LotControl_ViewManager")
public interface ViewManagerPropertyBean extends CommonViewPropertyBean, IProperty {
	/**
	 * Show part build attribute on screen
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isShowBuildAttribute();
	
	/**
	 * Height of the main window
	 * @return
	 */
	int getMainWindowHeight();
	
	/**
	 * Width of the main window
	 * @return
	 */
	int getMainWindowWidth();
	
	/**
	 * View Manger class
	 * @return
	 */
	String getViewManager();
	
	/**
	 * Number of seconds to delay before refresh screen
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0" )
	int getScreenRefreshingDelay();
	
	/**
	 * Log level to control log context shown in the client info 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "trace")
	public String getClientInfoLogLevel();
	
	
	/**
	 * Get the Number of engines their log will be kept in client log 
	 * 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "2" )
	public int getClientInfoLogSize();
	
	/**
	 * Log level to control log context shown in the client info 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "1500")
	public int getClientInfoLogMaxEntries();
	
	/**
	 * Define Color Map for different Model
	 * for example: RNWAA4 ORANGE
	 *              RNWAA5 CYAN
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getModelColorMap();
	
	/**
	 * Define Color Map for part mark indexed by product spec
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getPartMarkColorMap();
	
	/**
	 * Attribute to specify log attribute to be filter out in client info log
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getClientLogFilter();
	
	/**
	 * Security group group name to authorize a user 
	 * @return
	 */
	@PropertyBeanAttribute
	public String getAuthorizationGroup();
	/**
	 * Flag indicate if user login required on button actions
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToSkipProduct();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToSkipPart();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToReject();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToRefresh();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToCancel();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToDisableExpected();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isNeedAuthorizedUserToDeleteCacheData();
	
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isNeedAuthorizedUserToChangeAudioConfig();
	
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNeedAuthorizedUserToRemake();
	
	/**
	 * Flag indicating that the expected stamping sequence should be used
	 * to automatically enter the product rather than user entry.
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isUseExpectedStampingSequence();
	
	/**
	 * Number of seconds delay for part mark 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "0")
	int getPartMarkDelay();
	
	/**
	 * OUT_OF_SEQ_BUTTON{YES} = YES, OUT_OF_SEQ_BUTTON{NO} = NO
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getOutOfSeqButton();
	/**
	 * Map the Skip button to say NEXT BOLT instead of SKIP
	 * Map the Refresh button to say REPAIR instead of REFRESH
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getMaxAttemptsButton();

	/**
	 * Map the Refresh button to say REPAIR instead of REFRESH
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getDifferentProductButton();
	/**
	 * Map the Skip button to say NEXT BOLT instead of SKIP. 
	 * Map the Refresh button to say REPAIR instead of REFRESH
	 * ex: PRODUCT_CHECK_BUTTON {SKIP} = NEXTBOLT, PRODUCT_CHECK_BUTTON {REFRESH} = REPAIR
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getProductCheckButton();
	/**
	 * Map the Skip button to say NEXT BOLT instead of SKIP
	 * Map the Refresh button to say REPAIR instead of REFRESH
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getAlreadyAssignedButton();

	/**
	 * Map the Refresh button to say REPAIR instead of REFRESH
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getDifferentMbpnAssignedButton();
	
	/**
	 * Map the Refresh button to say CANCEL instead of REFRESH
	 * TEST_TORQUE_BUTTON {REFRESH} = CANCEL
	 * @return
	 */
	@PropertyBeanAttribute
	public Map<String, String> getTestTorqueButton();
	
	@PropertyBeanAttribute(defaultValue = "#ff0000")
	public String getMaxAttemptsBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getMaxAttemptsForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#ffff00")
	public String getDifferentProductBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getDifferentProductForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#0000ff")
	public String getOutOfSequenceBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getOutOfSequenceForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getDefaultForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#ff0000")
	public String getDefaultBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#0000ff")
	public String getProductCheckBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#ffff00")
	public String getAlreadyAssignedBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#ffff00")
	public String getDifferentMbpnAssignedBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#d6d6c2")
	public String getTestTorqueBackColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getProductCheckForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getAlreadyAssignedForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getDifferentMbpnAssignedForeColor();
	
	@PropertyBeanAttribute(defaultValue = "#000000")
	public String getTestTorqueForeColor();
	
	/**
	 * Used to map actions to special scan keywords.<br>
	 * For example, the value of SCAN_MAP{REFRESH} tells the data collection screen to refresh when it is scanned. 
	 */
	@PropertyBeanAttribute
	public Map<String, String> getScanMap();
	
	/**
	 * If enabled, causes "Unexpected Product Scan" dialog to be displayed when a product is scanned into the part field.
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isUnexpectedProductScanCheckEnabled();
}
