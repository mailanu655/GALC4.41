package com.honda.galc.client.teamleader.fx;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.service.property.PropertyHelper;



/*   
 * @author Gangadhararao Gadde , Subu kathiresan, Raj Salethaiyan
 * 
 *
 *
 */
public class PropertiesParser 
{
	public static final String SEPARATOR = ".";
	
	/**
	 * Default constructor
	 */
	public PropertiesParser(){}

	/**
	 * helper method which calls the overloaded convertPropertiesToXml
	 * 
	 * @param componentId
	 * @param nodeName
	 * @return
	 */
	public StringBuilder convertPropertiesToXml(String componentId, String nodeName) 
	{
		return convertPropertiesToXml(componentId, nodeName, nodeName);		
	}
	
	/**
	 * Converts the properties list in the targetPath to its
	 * xml representation
	 * 
	 * @param componentId
	 * @param targetpath
	 * @param nodeName
	 * @return
	 */
	public StringBuilder convertPropertiesToXml(String componentId, String targetPath, String nodeName) 
	{
		StringBuilder xmlString = new StringBuilder();
		TreeMap treeMap = null;
		PropertyHelper props = null;
		
		try
		{
			props = new PropertyHelper(componentId);
			treeMap = buildtargetMap(props, targetPath);
			
			addOpenTag(xmlString, nodeName);
			processChildren(nodeName, xmlString, treeMap);
			addCloseTag(xmlString, nodeName);
		}
		catch(Exception ex)
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		return xmlString;
	}

	/**
	 * Builds a TreeMap of all the properties that pertain to the
	 * selected target path.
	 * 
	 * @param props
	 * @param targetPath
	 * @return
	 */
	public TreeMap buildtargetMap(PropertyHelper props, String targetPath)
	{
		TreeMap<String, String> treeMap = new TreeMap<String, String>();

		try {			
			for(ComponentProperty key: props.getProperties())	{
				if (isValidKey(key.getPropertyKey(), targetPath)) {
					String val = key.getPropertyValue();

					// replace character entity references
					val = val.replace("&", "&amp;").replace(">", "&gt;").replace("<", "&lt;").replace("'", "&apos;").replace("\"", "&quot;");	
					treeMap.put(key.getPropertyKey(), val);
				}
			}
		}
		catch(Exception ex)
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		return treeMap;
	}
	
	/**
	 * Recursively parses all children of the target node and returns
	 * and xml string representing the collection of dot separated properties
	 * in the TreeMap.
	 * 
	 * @param xPath
	 * @param xmlString
	 * @param treeMap
	 * @return
	 */
	private String processChildren(String xPath, StringBuilder xmlString, TreeMap treeMap)
	{		
		Hashtable<String, String> children = null;
		
		try
		{
			children = getChildren(xPath, treeMap);
				
			for (String key: children.keySet())
			{
				String tag = children.get(key);
			
				// add opening tag
				addOpenTag(xmlString, tag);
			
				// add property value, if available
				if (treeMap.containsKey(key))
					xmlString.append(treeMap.get(key));
			
				// recursively process all children
				processChildren(key, xmlString, treeMap);
			
				// add closing tag
				addCloseTag(xmlString, tag);			
			}
		}
		catch(Exception ex)
		{
			// TODO add proper logging
			ex.printStackTrace();
		}
		
		return xmlString.toString();
	}

	/**
	 * Returns the unique children for a given node
	 * 
	 * @param xPath
	 * @param treeMap
	 */
	public Hashtable<String, String> getChildren(String xPath, TreeMap treeMap) 
	{
		Hashtable<String, String> children = new Hashtable<String, String>();
		
		// parse unique child nodes
		for (Object key : treeMap.keySet()) 
		{
			String sKey = (String) key;			
			StringTokenizer tokenizer = new StringTokenizer(sKey, SEPARATOR);
			String currentPath = "";
						
			while (tokenizer.hasMoreTokens()) 
			{
				String str = tokenizer.nextToken();	
				
				// add child, if not already added
				if (currentPath.equals(xPath)
					&& !children.containsKey(currentPath + SEPARATOR + str))
						children.put(currentPath + SEPARATOR + str, str);
						
				// adjust current path
				currentPath+= (currentPath == "") ? str: SEPARATOR + str;
			}
		}
		
		return children;
	}

	/**
	 * returns true if the key is a valid dot separated key for
	 * the provided target path
	 * 
	 * @param key
	 * @param targetPath
	 * @return
	 */
	public boolean isValidKey(String key, String targetPath)
	{
		return key.startsWith(targetPath) && (key.indexOf(SEPARATOR) > 0);
	}
	
	/**
	 * appends an open tag to the xml with the provided tag name
	 * 
	 * @param xmlString
	 * @param tag
	 */
	private void addOpenTag(StringBuilder xmlString, String tag)
	{
		xmlString.append("<" + tag + ">");
	}
	
	/**
	 * appends a close tag to the xml with the provided tag name
	 * 
	 * @param xmlString
	 * @param tag
	 */
	private void addCloseTag(StringBuilder xmlString, String tag)
	{
		xmlString.append("</" + tag + ">");
	}
}
