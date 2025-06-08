package com.honda.galc.property;

import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;


@PropertyBean(componentId ="DEFAULT_QI_PRODUCT_CLIENT")
public interface DefaultQiProductPropertyBean extends ProductPropertyBean,ApplicationPropertyBean,SystemPropertyBean{

	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.QiProductIdProcessor")
	String getProductIdProcessor();
	
	@PropertyBeanAttribute(defaultValue = "com.honda.galc.client.product.pane.QiProductIdlePane")
	String[] getProductIdlePane();
	
	/**
	 * Hide process Change Page as it is not required for NAQ
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getProcessInfoDialog();
	
	/**
	 * Hide Main Menu Page as it is not required for NAQ
	 */
	@PropertyBeanAttribute(defaultValue = "")
	String getMainMenuDialog();
	
	/**
	 * If true, partition will be shown in Idle screen 
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isProductWidgetDisplayed();
	
	/**
	 * If true, the tracking service will get called after VIN scan
	 */
	@PropertyBeanAttribute(defaultValue = "false")
	boolean isAutoTracking();
	
	/**
	 * If true the associate is not required to supply a password at login. (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="LOGIN_WITHOUT_PASSWORD",defaultValue = "true")
	public boolean isLoginWithoutPassword();
	
	/**
	 * If set to true, on click of switch user menu item the client will restart and launch the login page
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	boolean isRestartOnSwitchUserOrLogout();
	
	/**
	 *  When set to true the Mass Awareness Message widget will be displayed on at the bottom of the screen. (VIOS)
	 */
	@PropertyBeanAttribute(propertyKey="MASS_AWARENESS_MESSAGE_ENABLED",defaultValue = "true")
	public boolean isMassAwarenessMessageEnabled();
	
	@PropertyBeanAttribute(defaultValue="true")
	boolean isAutoLogin();
	
	/**
	 * If true, display Manual Product Entry Dialog instead of Product Id Label
	 * on VIOS Input Pane
	 */
	@PropertyBeanAttribute(defaultValue = "true")
	public boolean isManualProductEntryEnabled();
	
	/**
	 * max products to retrieve in product search
	 */
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getMaxProductsFetch();

}
