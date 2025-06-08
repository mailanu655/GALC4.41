package com.honda.galc.property;

/**
 * 
 * <h3>ApplicationPropertyBean Class description</h3>
 * <p> ApplicationPropertyBean description </p>
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
 * Mar 12, 2012
 *
 *
 */
public interface ApplicationPropertyBean extends SystemPropertyBean{

	/**
	 * flag indicates whether to utilize LotControl rules 
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isRequiresFsm();

	@PropertyBeanAttribute(defaultValue = "")
	public String getMainPanelClass();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean hasStatusMessagePanel();

	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAllowWindowResize();

	/**
	 * If set to true the simple Login dialog will be displayed requesting username and password otherwise the touchscreen login dialog will be displayed (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="MANUAL_LOGIN",defaultValue = "true")
	public boolean isManualLogin();

	/**
	 * Specifies the name of the proximity card reader name.   (VIOS) 
	 */
	@PropertyBeanAttribute(propertyKey="PROXIMITYCARD_READERNAME",defaultValue = "OMNIKEY CardMan 5x25-CL 0")
	public String getProximityCardReaderName();

	/**
	 * If true the associate is not required to supply a password at login. (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="LOGIN_WITHOUT_PASSWORD",defaultValue = "false")
	public boolean isLoginWithoutPassword();


	/**
	 * This flag is set to true the client will expect to authenticate via badge scanner otherwise the user will enter the userid via 
	 * the keyboard (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="SCANNER_ACTIVE",defaultValue = "false")
	public boolean isScannerActive();

	/**
	 * A list of possible user id prefixes (e.g ha,vfc, vnc, vcc etc), used during authentication via badge scan.
	 * Badges only contain the numeric portion of the userid whereas LDAP contains the a prefix and number. During the authentication process
	 * each variation of prefix and badge number are tried until a match is found or the list of prefixes are exhausted.
	 */
	@PropertyBeanAttribute(propertyKey="ASSOCIATE_PREFIXES", defaultValue = "")
	public String[] getAssociatePrefixes();

	@PropertyBeanAttribute(propertyKey="CARD_ID_MAX_LENGTH", defaultValue = "8")
	public Integer getCardIdMaxLength();

	/**
	 * Specifies the time in minutes that a client can be inactive (i.e. no keyboard, mouse or device activity) before the associate is logged out of GALC. A
	 * value of -1 means never timeout. (VIOS) 
	 */
	@PropertyBeanAttribute(propertyKey="GALC_CLIENT_TIMEOUT",defaultValue = "-1")
	public String getGalcClientTimeout();

	/**
	 * Specifies the JavaFX CSS stylesheet to apply to the GALC application.  CSS files are located in /CLIENT_RESOURCE/resource/css (VIOS) 
	 */
	@PropertyBeanAttribute(propertyKey="JAVAFX_CSS_FILE",defaultValue = "default")
	public String getClientCssFile();
	
	/**
	 * Specifies on click of Alt +Tab,the task icon will be shown 
	 */ 
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isStageStyleDecorated();
	
	/**
	 * Specifies product kind for a Station, such as AUTOMOBILE, MOTORCYCLE, POWER_SPORTS, AIRCRAFT, ATV 
	 */ 
	@PropertyBeanAttribute (defaultValue = "AUTOMOBILE")
	String getProductKind();

	/**
	 * Specifies maximum list of users which will be shown in associate drop down for switch user 
	 */ 
	@PropertyBeanAttribute (defaultValue = "25")
	int getMaxRecentUsers();
	
	/**
	 * Specifies Number of days to fetch Recent Users
	 */ 
	@PropertyBeanAttribute (defaultValue = "10")
	int getDateRangeForRecentUsers();
	
	/**
	 * If set to true, on click of switch user menu item the client will restart and launch the login page
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isRestartOnSwitchUserOrLogout();


	
	/**
	 * Enable/Disable Switch User option in System Menu
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isSwitchUserSystemMenuEnabled();
	/**
	 * Enable/Disable Switch Mode option in System Menu
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isSwitchModeSystemMenuEnabled();
	/**
	 * Enable/Disable LogOut option in System Menu
	 */
	@PropertyBeanAttribute (defaultValue = "true")
	boolean isLogoutSystemMenuEnabled();

	/**
	 *  Specifies the font size of text in the Menu
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMenuFontSize();
	
	/**
	 * Enable/Disable substring of associate id to get the number part of associate id
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isSubstringRequiredForAssociateId();
	
	/**
	 * Enable/Disable check defect entered by Legacy QICS
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isQicsDefectEnteredCheck();
	

	/**
	 * The start index from which associate id will be substring
	 */
	@PropertyBeanAttribute (defaultValue = "17")
	int getStartIndex();

	/**
	 * The end index from which associate id will be substring
	 */
	@PropertyBeanAttribute (defaultValue = "22")
	int getLength();
	
	/**
	 * Specifies Lazy Load Display Rows Number of tableview, 0 - normal load 
	 */
	@PropertyBeanAttribute (defaultValue = "50")
	int getLazyLoadDisplayRows();

	/**
	 * Flag to support mapping car number from scanner
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isMapCardNumber();

	/**
	 * Flag/Checker to decide if broadcast from AFOffVinProcesor
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isUseBroadcastCheckPoint();

	/**
	 * Flag to support multiple engine source for QICs defect
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isQicsEngineSource();
	
	/**
	 * Property to determine which tabs will show up for searching
	 * for products.
	 */
	@PropertyBeanAttribute(defaultValue = "PRODUCT_SCAN_PANE")
	public String[] getSearchPanels();
	
	/**
	 * get the list of process locations for different MBPNs
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getMbpnProcessLocations();
	
	/**
	 * Flag to indicate whether the current client is a modal or not.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isModal();
	
	/**
	 * Flag to indicate whether the current client is the parent of 
	 * some modal client (mainly used to bypass exclusive lock for devices).
	 * 
	 * If above property is set to true for some client, this property should 
	 * be true for the default client that the modal client launches from.
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isModalParent();
}
