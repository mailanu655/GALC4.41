package com.honda.galc.client.ui;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;

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
 * @author Suriya Sena
 * 
 * Jan 23, 2014 JavaFx Migration
 */


public abstract class ApplicationMainPane extends MainPane {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_DELIMITER = ",;";
	
	private static final String resourcePath = "/resource/%s.fxml";

	protected MainWindow window;

	
	public ApplicationMainPane(MainWindow window) {
	   this(window,false);	
	}
	
	public ApplicationMainPane(MainWindow window, boolean isFXML) {
		super();
		this.window = window;
		if(window != null) {
			PropertyService.getComponentProperties(getApplicationId());
		}
		
		if (isFXML) {
			loadFXML();
		}
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
	
	public List<ComponentProperty> getPropertiesEx(){
		return PropertyService.getComponentProperty(getApplicationId());
	}
	
	public void refreshProperties() {
		PropertyService.refreshComponentProperties(getApplicationId());
	}
	
	public String[] getPropertyArray(String propertyName) {
		String property = getProperty(propertyName);
		
		if(StringUtils.isEmpty(property)) {
			return new String[0];
		}
		
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

	public void loadFXML() {
		
		URL resource = getViewURL();
		
		assert resource != null;
		
		FXMLLoader loader = new FXMLLoader(resource);
		
		
		loader.setController(this);
		try {
			Parent screen = (Parent)loader.load();
			this.getChildren().add(screen);
		}  catch (LoadException e) {
			if (e.getMessage().compareTo("Root value already specified.") == 0  ||
			    e.getMessage().compareTo("Controller value already specified.") == 0  
			    )
			{
				String message = String.format(
						"Error [%s] encountered when loading the FXML file [%s].\n\n" +
						"The scene definition must be defined as follows :\n" +
						"   MUST be contained within a root node\n" +
						"   MUST NOT define a controller attribute in fx:root.\n\n" +
					    "For Example :\n\n" + 
						"<fx:root type=\"javafx.scene.layout.BorderPane\" xmlns=\"http://javafx.com/javafx/8\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
						"  <center>\n" +
						"   content .... \n" +
						"  </center>\n" +
						"</fx:root>\n\n" +
						"Please refer to http://docs.oracle.com/javafx/2/fxml_get_started/custom_control.htm for further details\n",e.getMessage(),resource);
				MessageDialog.showScrollingInfo(null,message,10,50);
			} else {
				MessageDialog.showScrollingInfo(null,e.getMessage(),10,50);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String getViewPath() {
		String fxmlFilepath = String.format(resourcePath, this.getClass().getName().replace(".","/"));
		getLogger().debug(String.format("Based on the classname the FXML file is expected at in [%s]",fxmlFilepath));
		return fxmlFilepath;
	}

	private URL getViewURL() {
		return getClass().getResource(getViewPath());
	}
}
