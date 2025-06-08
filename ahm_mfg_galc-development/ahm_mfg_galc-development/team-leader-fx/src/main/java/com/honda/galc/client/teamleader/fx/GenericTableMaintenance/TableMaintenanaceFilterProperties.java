/**
 * 
 */
package com.honda.galc.client.teamleader.fx.GenericTableMaintenance;

import java.io.StringReader;
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
public class TableMaintenanaceFilterProperties {

	public static final String FILTERS_NODE = "filter";
	
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
		Hashtable<String, GenericTableMaintFilter> list = getFilters(componentId, FILTERS_NODE + SEPARATOR + id);
		return list.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public static Hashtable<String, GenericTableMaintFilter> getFilters(String componentId) {
		return getFilters(componentId, FILTERS_NODE);
	}
	
	@SuppressWarnings("unchecked")
	public static Hashtable<String, GenericTableMaintFilter> getFilters(String componentId, String targetPath) {
		Hashtable<String, GenericTableMaintFilter> filterList = new Hashtable<String, GenericTableMaintFilter>();
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
         		
    			filterList.put(filter.getId(), filter);
            }
		} 
		catch (Exception ex) 
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		
		return filterList;	
	}
}
