/**
 * 
 */
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;


import com.honda.galc.client.teamleader.fx.PropertiesParser;
import com.thoughtworks.xstream.XStream;


/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class TableMaintenanaceProperties {

	public static final String FILTERS_NODE = "filter";
	public static final String TABLE_CONFIG_NODE = "table";
	
	public static final String ID_ATTRIB_NAME = "id";
	public static final String SEPARATOR = ".";
	
	/**
	 * 
	 * @param id
	 * @param componentId
	 * @param applicationId
	 * @return
	 */
	public static GenericTableMaintFilter getFilter(String componentId, String id) 	{
		ArrayList<GenericTableMaintFilter> list = getFilters(componentId, FILTERS_NODE + SEPARATOR + id);
		for(GenericTableMaintFilter filter: list) {
			if (filter.getId().equals(id))
				return filter;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<GenericTableMaintFilter> getFilters(String componentId) {
		return getFilters(componentId, FILTERS_NODE);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<GenericTableMaintFilter> getFilters(String componentId, String targetPath) {
		ArrayList<GenericTableMaintFilter> filtersList = new ArrayList<GenericTableMaintFilter>();
		PropertiesParser parser = new PropertiesParser();
		StringBuilder filterXml = new StringBuilder();
		
		try 
		{
			filterXml = parser.convertPropertiesToXml(componentId, targetPath, FILTERS_NODE);
			
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(new StringReader(filterXml.toString()));
			List<Element> filterElements = doc.getRootElement().getChildren();
            
            for(Element element: filterElements)
            {
            	String id = element.getName();
            	element.setName(FILTERS_NODE);
            	
            	// add Id attribute
            	Attribute idAttrib = new Attribute(ID_ATTRIB_NAME, id); 
            	element.setAttribute(idAttrib);
            	
    			//De-Serialize filter properties xml
    			XStream xs = new XStream();
    			xs.processAnnotations(GenericTableMaintFilter.class);    			
            	XMLOutputter outputter = new XMLOutputter();           	
         		GenericTableMaintFilter filter = (GenericTableMaintFilter) xs.fromXML(outputter.outputString(element));
         		
    			filtersList.add(filter);
            }
		} 
		catch (Exception ex) 
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		Collections.sort(filtersList, new FiltersBySequenceNumber());
		return filtersList;	
	}
	
	@SuppressWarnings("unchecked")
	public static GenericTableMaintConfiguration getTableConfiguration(String componentId, String targetPath) {
		
		GenericTableMaintConfiguration config = null;
		PropertiesParser parser = new PropertiesParser();
		StringBuilder tableConfigXml = new StringBuilder();
		
		// TODO get all tables
		// TODO get all columns for parent table (that doesn't have any children)
		// TODO for each table get all children
		// TODO for each child get all columns
		// TODO wrap it up in Configuration 
		
		try 
		{
			tableConfigXml = parser.convertPropertiesToXml(componentId, targetPath, TABLE_CONFIG_NODE);
			
			SAXBuilder builder = new SAXBuilder(false);
			Document doc = builder.build(new StringReader(tableConfigXml.toString()));
			List<Element> tableConfigElements = doc.getRootElement().getChildren();
            
            for(Element element: tableConfigElements)
            {
            	String id = element.getName();
            	element.setName(TABLE_CONFIG_NODE);
            	
            	// add Id attribute
            	Attribute idAttrib = new Attribute(ID_ATTRIB_NAME, id); 
            	element.setAttribute(idAttrib);
            	
    			//De-Serialize filter properties xml
    			XStream xs = new XStream();
    			xs.processAnnotations(GenericTableMaintConfiguration.class);    			
            	XMLOutputter outputter = new XMLOutputter();           	
         		config = (GenericTableMaintConfiguration) xs.fromXML(outputter.outputString(element));
            }
		} 
		catch (Exception ex) 
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		
		return config;
	}
}
