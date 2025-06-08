package com.honda.galc.client.ui;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>ApplicationMainPanel Class description</h3>
 * <p> ApplicationMainPanel description </p>
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
 * Dec 7, 2010
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
public abstract class ApplicationMainPanel extends MainPanel {

	
	private static final long serialVersionUID = 1L;
	
	private String DEFAULT_DELIMITER = ",;";
	
	protected MainWindow window;
	
	public ApplicationMainPanel() {
		
	}
	
	public ApplicationMainPanel(MainWindow window) {
		this.window = window;
		if(window != null)
			PropertyService.getComponentProperties(getApplicationId());
	}
	
	public MainWindow getMainWindow() {
		
		return window;
		
	}

	public void setMainWindow(MainWindow window) {
		this.window = window;
	}
	
	public String getApplicationId() {
		return window.getApplication().getApplicationId();
	}
	
	public String getProperty(String propertyName) {
		
		String property = PropertyService.getProperty(getApplicationId(), propertyName);
		if(property == null) throw new PropertyException("Property : " + propertyName + " is not configured for " + getApplicationId());
		return property;
	}
	
	public String getProperty(String propertyName,String defaultValue) {
		
		return PropertyService.getProperty(getApplicationId(), propertyName,defaultValue);
		
	}
	
	public Boolean getPropertyBoolean(String propertyName,boolean defaultValue) {
		
		return PropertyService.getPropertyBoolean(getApplicationId(), propertyName,defaultValue);
		
	}
	
	public Integer getPropertyInt(String propertyName) {
		
		return PropertyService.getPropertyInt(getApplicationId(), propertyName);
		
	}
	
	public Integer getPropertyInt(String propertyName,int defaultValue) {
		
		return PropertyService.getPropertyInt(getApplicationId(), propertyName,defaultValue);
		
	}
	
	public List<ComponentProperty> getProperties(){
		
		return PropertyService.getComponentProperty(getApplicationId());
		
	}
	
	public void refreshProperties() {
		
		PropertyService.refreshComponentProperties(getApplicationId());
		
	}
	
	public String[] getPropertyArray(String propertyName) {
		
		String property = getProperty(propertyName);
		
		if(StringUtils.isEmpty(property)) return new String[0];
		
		return StringUtils.split(property, DEFAULT_DELIMITER);
		
	}
	
	public String getProcessPointId() {
		return window.getApplicationContext().getProcessPointId();
	}
	
	public ApplicationPropertyBean getApplicationPropertyBean() {
		return getApplicationPropertyBean(getApplicationId());
	}
	
	public ApplicationPropertyBean getApplicationPropertyBean(String applicationId) {
		return PropertyService.getPropertyBean(ApplicationPropertyBean.class, applicationId);
	}
	
	public String getApplicationProductTypeName() {
		return getApplicationPropertyBean().getProductType();
	}
	
	public String getApplictionProductTypeName(String applicationId) {
		return getApplicationPropertyBean(applicationId).getProductType();
	}
	
	public Logger getLogger() {
		return getMainWindow().getLogger();
	}
	
	public void panelRealized() {
		
	}
}
