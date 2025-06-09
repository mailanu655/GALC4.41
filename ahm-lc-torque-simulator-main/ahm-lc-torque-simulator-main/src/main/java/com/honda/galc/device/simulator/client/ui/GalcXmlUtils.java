package com.honda.galc.device.simulator.client.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * <h3>Class description</h3>
 * This is a utility class for loading and parsing XML documents. 
 * It can load properties defined in XML format.
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
 * <TD>July 05, 2007</TD>
 * <TD>1.0</TD>
 * <TD>@GY 20070705</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcXmlUtils {
	public static final String PROPERTIES_NODE = "properties";
	public static final String PROPERTY_ENTRY_NODE = "entry";
	public static final String PROPERTY_KEY = "key";
	private Properties properties;

/**
 * Load a Document object from a XML file. It validates the XML file and uses 
 * GalcXmlErrorHandler to replace the default handler.
 * 
 * @param uriString The URI string of the XML file.
 * @return The document object parsed from the file.
 * @throws ParserConfigurationException
 * @throws SAXException
 * @throws IOException
 */
	public Document loadDocument(String uriString) 
		throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new GalcXmlErrorHandler());
		builder.setEntityResolver(new ClasspathResolver());
		
		InputStream in = getClass().getClassLoader().getResourceAsStream(uriString);
		if (in == null) {
			throw new IOException(String.format("Unable to load resource %s!!!",uriString));
		}

		Document doc = builder.parse(in);
		return doc;
    }

/**
 * Load properties from a XML property file.
 * @param uriString The URI string of the XML property file.
 * @return Properties loaded from the file.
 * @throws ParserConfigurationException 
 * @throws SAXException
 * @throws IOException
 */
	public Properties loadXmlProperties(String uriString) 
    	throws ParserConfigurationException, SAXException, IOException {
    	setProperties(new Properties());
 		processPropertyNode(loadDocument(uriString), null);
        return getProperties();
    }

	public void processPropertyNode(Node aNode, String key) {
		String aKey = null;

		switch (aNode.getNodeType()) {
			case Node.ELEMENT_NODE: 
				aKey = (aNode.getNodeName().equals(PROPERTY_ENTRY_NODE) && aNode.hasAttributes()) ? 
						 aNode.getAttributes().getNamedItem(PROPERTY_KEY).getNodeValue() : null;
				break;
			case Node.TEXT_NODE:
				if(key != null) getProperties().setProperty(key, aNode.getNodeValue());
				break;
			default:
				break;
		}
		
		NodeList aNodeList = aNode.getChildNodes();
		for(int i=0; i<aNodeList.getLength(); i++) {
			processPropertyNode(aNodeList.item(i), aKey);
		}
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public List<Node> getChildNodesByName(Node node, String name)
	{
		if(name == null) return null;
		NodeList childNodes = node.getChildNodes();
		List<Node> result = new ArrayList<Node>();
		for(int i = 0; i < childNodes.getLength(); i++)
		{
			if(name.equals(childNodes.item(i).getNodeName()))
				result.add(childNodes.item(i));
		}
		return result;
		
	}
}

