package com.honda.galc.client.codebroadcast;

import java.awt.Color;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

@PropertyBean(componentId ="Default_CodeBroadcast")
public interface CodeBroadcastPropertyBean extends IProperty{

	/**
	 * Argument required from a broadcast in order to broadcast job codes.
	 */
	@PropertyBeanAttribute(defaultValue = "BROADCAST_CODES")
	public String getArgumentBroadcastCodes();

	/**
	 * Argument required from a broadcast in order to request trigger data.
	 */
	@PropertyBeanAttribute(defaultValue = "REQUEST_TRIGGER")
	public String getArgumentRequestTrigger();

	/**
	 * Error code indicating a NG condition.
	 */
	@PropertyBeanAttribute(defaultValue = "NG")
	public String getCodeNg();

	/**
	 * Error code indicating an OK condition.
	 */
	@PropertyBeanAttribute(defaultValue = "OK")
	public String getCodeOk();

	/**
	 * List of characters which are the last character
	 * to be displayed as part of a color code.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public char[] getColorCodeTerminatingCharacters();

	/**
	 * Color of codes which have been confirmed.
	 */
	@PropertyBeanAttribute(defaultValue = "0,255,0")
	public Color getColorConfirmed();

	/**
	 * Neutral color for components.
	 */
	@PropertyBeanAttribute(defaultValue = "238,238,238")
	public Color getColorNeutral();

	/**
	 * Color of the background when a new lot product is displayed.
	 */
	@PropertyBeanAttribute(defaultValue = "-1,-1,-1")
	public Color getColorNewLot();

	/**
	 * Color of codes which have not been confirmed.
	 */
	@PropertyBeanAttribute(defaultValue = "255,255,0")
	public Color getColorUnconfirmed();

	/**
	 * Number of columns to be used for displaying codes in the code panel.
	 */
	@PropertyBeanAttribute(defaultValue = "4")
	public int getColumnCountCodePanel();

	/**
	 * Number of columns to be used for displaying special tags in the special tag panel.
	 */
	@PropertyBeanAttribute(defaultValue = "2")
	public int getColumnCountSpecialTagPanel();

	/**
	 * The type of CodeBroadcastController to be used.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getControllerClass();

	/**
	 * The message to be displayed to the user when the DEVICE_DATA_ERROR_PRODUCT_ID is received from the DeviceDataRoutingService.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDeviceDataErrorMessage();

	/**
	 * The product id which indicates an error when received from the DeviceDataRoutingService.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getDeviceDataErrorProductId();

	/**
	 * The email notification level. (NONE, ERROR, WARNING, MESSAGE)<br>
	 * NONE: no emails will be sent<br>
	 * ERROR: emails will be sent for error messages<br>
	 * WARNING: emails will be sent for error and warning messages<br>
	 * MESSAGE: emails will be sent for all messages (error, warning and standard)
	 */
	@PropertyBeanAttribute(defaultValue = "NONE")
	public String getEmailNotificationLevel();

	/**
	 * The code to be broadcast when a color code needs to be broadcast but is unavailable.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getExclusionCode();

	/**
	 * The font size to be used by the CodeBroadcastPanel.
	 */
	@PropertyBeanAttribute(defaultValue = "24")
	public int getFontSize();

	/**
	 * The function key to be used as a shortcut for clicking the confirm button.
	 */
	@PropertyBeanAttribute(defaultValue = "F2")
	public String getFunctionKeyConfirm();

	/**
	 * The function key to be used as a shortcut for clicking the product select button.
	 */
	@PropertyBeanAttribute(defaultValue = "F3")
	public String getFunctionKeyProductSelect();

	/**
	 * The function key to be used as a shortcut for clicking the refresh button.
	 */
	@PropertyBeanAttribute(defaultValue = "F5")
	public String getFunctionKeyRefresh();

	/**
	 * The csv list of error codes which should display in a popup when they occur.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getPopupErrorCodes();

	/**
	 * Label for the Code panel.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLabelCodePanel();

	/**
	 * Label for the Color Tag selection.
	 */
	@PropertyBeanAttribute(defaultValue = "COLOR TAG")
	public String getLabelColorTag();

	/**
	 * Label for the Color Tag code panel.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLabelColorTagCodePanel();

	/**
	 * Label for the Special Color selection.
	 */
	@PropertyBeanAttribute(defaultValue = "SPECIAL COLOR")
	public String getLabelSpecialColor();

	/**
	 * Label for the Special Color code panel.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getLabelSpecialColorCodePanel();

	/**
	 * Label for the Special Tag selection.
	 */
	@PropertyBeanAttribute(defaultValue = "SPECIAL TAG")
	public String getLabelSpecialTag();

	/**
	 * The csv list of codes under the color code drop-down which are confirmed by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationColorCodes();

	/**
	 * The FORMID under which display print attributes are saved.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getStationDisplayFormId();

	/**
	 * The csv list of build attributes which are displayed but not confirmed by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationDisplayCodes();

	/**
	 * The csv list of codes which are confirmed by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationJobCodes();

	/**
	 * The csv list of codes which are confirmed by the user in the special tag by color code panel.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationJobCodesByColorCode();

	/**
	 * The csv list of codes which are confirmed by the user in the special tag panel.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationJobCodesBySpecialTag();

	/**
	 * The csv list of codes under the special color code drop-down which are confirmed by the user.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getStationSpecialColorCodes();

	/**
	 * The name of the station.
	 */
	@PropertyBeanAttribute
	public String getStationName();

	/**
	 * The process point used for identifying stragglers.
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getStragglerPpDelayedAt();

	/**
	 * Size of the text fields.
	 */
	@PropertyBeanAttribute(defaultValue = "16")
	public int getTextFieldSize();

	/**
	 * Time interval (in seconds) after which the product entry screen will automatically refresh.<br>
	 * Does not apply to the special tag screen.
	 */
	@PropertyBeanAttribute(defaultValue = "-1")
	public int getTimeoutInterval();

	/**
	 * Flag indicating whether an active product on the product entry screen may be replaced with a different product.
	 */
	@PropertyBeanAttribute(propertyKey = "ALLOW_PRODUCT_REPLACEMENT", defaultValue = "false")
	public boolean isAllowProductReplacement();

	/**
	 * Flag indicating that codes relating to received device data will be automatically confirmed.
	 */
	@PropertyBeanAttribute(propertyKey = "AUTO_CONFIRM_DEVICE_DATA", defaultValue = "false")
	public boolean isAutoConfirmDeviceData();

	/**
	 * Flag indicating that the color code should be broadcast along with the job codes iff it is available.
	 */
	@PropertyBeanAttribute(propertyKey = "BROADCAST_COLOR_CODE", defaultValue = "false")
	public boolean isBroadcastColorCode();

	/**
	 * Flag indicating that an "Are you sure?" popup should appear when the confirm button is pressed.
	 */
	@PropertyBeanAttribute(propertyKey = "CONFIRM_CONFIRMATION", defaultValue = "false")
	public boolean isConfirmConfirmation();

	/**
	 * Flag indicating whether the screen is for code display only.
	 */
	@PropertyBeanAttribute(propertyKey = "DISPLAY_ONLY", defaultValue = "false")
	public boolean isDisplayOnly();

	/**
	 * Flag indicating whether the Refresh button is available to the user.
	 */
	@PropertyBeanAttribute(propertyKey = "ENABLE_REFRESH_BUTTON", defaultValue = "true")
	public boolean isEnableRefreshButton();

	/**
	 * Flag indicating whether results of code broadcasts should be disregarded.
	 */
	@PropertyBeanAttribute(propertyKey = "IGNORE_BROADCAST_RESULTS", defaultValue = "false")
	public boolean isIgnoreBroadcastResults();

	/**
	 * Flag indicating whether the error state check should be ignored.
	 */
	@PropertyBeanAttribute(propertyKey = "IGNORE_ERROR_STATE_CHECK", defaultValue = "false")
	public boolean isIgnoreErrorStateCheck();

	/**
	 * Flag indicating that the product panel should show the previous product for which codes were broadcast.
	 */
	@PropertyBeanAttribute(propertyKey = "SHOW_PREVIOUS_PRODUCT", defaultValue = "false")
	public boolean isShowPreviousProduct();

}
