package com.honda.galc.device.simulator.client.ui;

import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


/**
 * <h3>Class description</h3>
 * GalcController is responsible for managing common system
 * activities (including UI) and providing some useful utilities.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Jun 29, 2007</TD>
 * <TD>1.0</TD>
 * <TD>@GY 20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcController {
	                                     
	public static String PROPERTY_URI = "resource/com/honda/galc/device/simulator/client/ui/GalcWindowProperties.xml";
	private Properties properties;
	private GalcXmlUtils xmlUtils;
	
	public GalcController() {
		super();
		initialize();
	}
	
    public static void main(String[] args) {
    	GalcController controller = new GalcController();
    	controller.showMessageDialog(controller.getProperty("Window Title"));
	}

    private void initialize() {
    	xmlUtils = new GalcXmlUtils();
    	try {
			setProperties(xmlUtils.loadXmlProperties(PROPERTY_URI));
		} catch (ParserConfigurationException e) {
			showErrorDialog(e.getMessage() + "\nSystem default values will be used.");
			setProperties(new Properties());
		} catch (SAXException e) {
			showErrorDialog(e.getMessage() + "\nSystem default values will be used.");
			setProperties(new Properties());
		} catch (IOException e) {
			showErrorDialog(e.getMessage() + "\nSystem default values will be used.");
			setProperties(new Properties());
		} 
    }
    
/**
 * Display a message string in a popup window.
 * @param message The string to be displayed.
 */
    public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.PLAIN_MESSAGE);
	}

/**
 * Display a message string in a popup window as warning message.
 * @param message The string to be displayed.
 */
    public void showWarningDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

/**
 * Display a message string in a popup window as an error message.
 * @param message The message to be displayed.
 */
    public void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

/**
 * Display a message string in a popup window and allow users to choose
 * Yes or No options.
 * @param message The message to be displayed.
 * @return an integer indicating the option selected by the user. 
 */
    public int showConfirmDialog(String message) {
		return JOptionPane.showConfirmDialog(null, message, "Please confirm", 
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	}
	
/**
 * Get property value for the key.
 * @param key The key for which the property will be retrieved.
 * @return The property value string.
 */
    public String getProperty(String key) {
     	return getProperties().getProperty(key);
    }
        
/**
 * Get boolean property value for the key. See return for more
 * details.
 * @param key The key for which the property will be retrieved.
 * @return The boolean property value. It returns true if the 
 * string argument is not null and is equal, ignoring case, 
 * to the string "true". If the property cannot be found, it 
 * returns false.
 */
    public boolean getBooleanProperty(String key) {
     	return Boolean.parseBoolean(getProperty(key));
    }
            
/**
 * Get int property value for the key. If the key cannot be found,
 * the defaultValue will be used.
 * @param key The key for which property will be retrived.
 * @param defaultValue The default value if the required property 
 *                     cannot be found.
 * @return int value of the property.
 */
    public int getIntProperty(String key, int defaultValue) {
		int result = defaultValue;
		String aString = getProperty(key);
		if(aString != null) { 
			try {
				result = Integer.parseInt(aString.trim());
			} catch (NumberFormatException e) {
				result = defaultValue;
			}
		}
		return result;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public GalcXmlUtils getXmlUtils() {
		return xmlUtils;
	}

	public void setXmlUtils(GalcXmlUtils xmlUtils) {
		this.xmlUtils = xmlUtils;
	}

	public static String getPROPERTY_URI() {
		return PROPERTY_URI;
	}

	public static void setPROPERTY_URI(String property_uri) {
		PROPERTY_URI = property_uri;
	}
}
