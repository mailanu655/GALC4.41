package com.honda.galc.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.honda.galc.common.exception.DataConversionException;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * <h3>DataContainerXMLUtil Class description</h3>
 * <p> DataContainerXMLUtil description </p>
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
 * May 14, 2010
 *
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class DataContainerXMLUtil {

	private static final String END_DATACONTAINER_XML = "/dataContainer";
	private DataContainer dc = new DefaultDataContainer();
	private static final String KEY_VALUE_TAG = "keyValue";
	private static final String KEY_TAG = "key";
	private static final String VALUE_TAG = "value";
	private static final String VALUE_CLASS_TAG = "valueClass";
	private static final String DATA_TAG = "data";
	private static final String TAG_LIST = "list";
	private static final String TAG_LIST_ELEMENT = "listElement";
	private static final String TAG_INDEX = "listIndex"; 
	private static final String DATA_CONTAINER = "dataContainer";

	private boolean useKeyAsTags;

	public static DataContainer readFromXML(ByteArrayInputStream bis) {
		return new DataContainerXMLUtil().convertFromXML(bis);
	}

	public static DataContainer readDeviceDataFromXML(InputStream is) {
		return new DataContainerXMLUtil().convertDeviceDataFromXML(is); 
	}

	public DataContainerXMLUtil() {

	}
	public DataContainer convertDeviceDataFromXML(InputStream is) {
		return convertFromXML(readDeviceData(is));
	}

	public DataContainer convertFromXML(InputStream is) {

		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(is);

			processAttributes(doc);

			processContents(doc);

		} catch (ParserConfigurationException e1) {
			 Logger.getLogger().error(e1, "Could not convert the response for the data :"+ dc.toString());
			throw new DataConversionException("parsing xml ",e1);
		} catch (SAXException e) {
			 Logger.getLogger().error(e, "Could not convert the response for the data :"+ dc.toString());
			throw new DataConversionException("parsing xml ",e);
		} catch (IOException e) {
			 Logger.getLogger().error(e, "Could not convert the response for the data :"+ dc.toString());
			throw new DataConversionException("parsing xml ",e);
		} catch (Throwable t){
			 Logger.getLogger().error(t, "Could not convert the response for the data :"+ dc.toString());
			throw new DataConversionException("parsing xml ", t);
		}
		return dc;
	}

	private void processAttributes(Document doc) {

		NodeList elementsList = doc.getElementsByTagName(DATA_CONTAINER);
		if(elementsList == null || elementsList.getLength() <= 0) return;
		NamedNodeMap attributes = elementsList.item(0).getAttributes();
		useKeyAsTags = getAttributeBoolean(attributes, DataContainerTag.XML_USE_KEYS_AS_TAGS);
	}

	private boolean getAttributeBoolean(NamedNodeMap attributes, String name) {
		Node namedItem = attributes.getNamedItem(name);
		return namedItem == null ? false : Boolean.parseBoolean(namedItem.getNodeValue());
	}

	private void processContents(Document doc) {
		NodeList items = doc.getElementsByTagName(KEY_VALUE_TAG);
		if(items == null || items.getLength() == 0){
			useKeyAsTags = true;
			NodeList containerElements = doc.getElementsByTagName(DATA_CONTAINER);
			if(containerElements == null || containerElements.getLength() == 0) return;
			items = containerElements.item(0).getChildNodes();
		}

		if(items == null || items.getLength() == 0) return;
		for(int i = 0; i<items.getLength();i++) {
			Element item = (Element)items.item(i);

			String key;
			Element valueItem;

			if (useKeyAsTags) {
				key = item.getNodeName();
				addData(key, null, item.getFirstChild()== null ? "": item.getFirstChild().getNodeValue());
			} else {
				key = item.getElementsByTagName(KEY_TAG).item(0).getFirstChild().getNodeValue();
				valueItem = (Element) item.getElementsByTagName(VALUE_TAG).item(0);
				String valueClassName = valueItem.getElementsByTagName(VALUE_CLASS_TAG).item(0).getFirstChild().getNodeValue();
				Element dataItem = (Element)valueItem.getElementsByTagName(DATA_TAG).item(0);

				Object dataValue = "java.util.ArrayList".equals(valueClassName) ?
						convertList(dataItem) : getItemValue(dataItem);

						addData(key,valueClassName,dataValue);
			}

		}


	}

	private Object getItemValue(Element dataItem) {

		return (dataItem.getFirstChild() == null) ? "NULL" : dataItem.getFirstChild().getNodeValue();
	}


	private List<String> convertList(Element dataItem) {

		NodeList nodeList = dataItem.getElementsByTagName(TAG_LIST);
		Map<Integer,String> tagMap = new HashMap<Integer,String>();

		for(int i=0;i<nodeList.getLength();i++) {
			Element item = (Element)nodeList.item(i);
			Element listElement = (Element)item.getElementsByTagName(TAG_LIST_ELEMENT).item(0);
			int index = Integer.parseInt(listElement.getAttribute(TAG_INDEX));
			NodeList valueList = (NodeList)listElement.getElementsByTagName(VALUE_TAG);
			String dataValue = ((Element)valueList.item(0)).getElementsByTagName(DATA_TAG).item(0).getFirstChild().getNodeValue();
			tagMap.put(index, dataValue);
		}

		List<String> tagList = new ArrayList<String>();
		for(int i=0;i<tagMap.size();i++) {
			tagList.add(tagMap.get(i));
		}

		return tagList;
	}

	private ByteArrayInputStream readDeviceData(InputStream is) {

		InputStreamReader isreader = new InputStreamReader(is);
		BufferedReader breader = new BufferedReader(isreader,10);
		StringBuffer inputBuffer = new StringBuffer();
		String line = null;
		try {
			while ((line = breader.readLine()) != null) {
				inputBuffer.append(line).append("\n");
				Logger.getLogger().debug(line);
				if(line.indexOf(END_DATACONTAINER_XML) >= 0) break;
			}
		}catch(IOException e) {
			Logger.getLogger().error(e, "Could not read the datacontainer response for the data :"+ dc.toString()); 
		}

		return new ByteArrayInputStream(inputBuffer.toString().getBytes());
	}


	@SuppressWarnings("unchecked")
	public static void convertToXML(DataContainer dc, OutputStream os) {


		XMLStreamWriter writer;
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(os);
			writer.writeStartDocument();
			writer.writeCharacters(System.getProperty("line.separator"));
			writer.writeStartElement("dataContainer");
			Iterator iter = dc.keySet().iterator();
			while(iter.hasNext()) {
				String key = (String)iter.next();
				Object value = dc.get(key);
				if(value == null) value = "";
				addKeyValue(writer,key,value);
			}

			writer.writeEndElement();

			writer.writeCharacters(System.getProperty("line.separator"));

			writer.writeEndDocument();

			writer.flush();

		} catch (XMLStreamException e) {
			 Logger.getLogger().error(e, "Could not send the datacontainer request for the data :"+ dc.toString());
		} catch (FactoryConfigurationError e) {
			 Logger.getLogger().error(e, "Could not send the datacontainer request for the data :"+ dc.toString());
		}


	}
	@SuppressWarnings("unchecked")
	public static void outputXML(DataContainer dc, OutputStream os) {


		XMLStreamWriter writer;
		try {
			writer = XMLOutputFactory.newInstance().createXMLStreamWriter(os,"UTF-8");
			writer.writeStartDocument("UTF-8","1.0");
			writer.writeCharacters(System.getProperty("line.separator"));
			writer.writeStartElement("dataContainer");
			writer.writeAttribute(DataContainerTag.XML_USE_GENERIC_TYPE_NAMES, "true");
			writer.writeAttribute(DataContainerTag.XML_USE_SIMPLE_STRING_DATA_FORMAT, "true");
			writer.writeAttribute(DataContainerTag.XML_USE_KEYS_AS_TAGS, "true");
			Iterator iter = dc.keySet().iterator();
			while(iter.hasNext()) {
				String key = (String)iter.next();
				Object value = dc.get(key);
				if(value == null || value.toString().length() == 0)
					writer.writeEmptyElement(key);
				else {
					writer.writeStartElement(key);
					basicAddValue(writer,value);
					writer.writeEndElement();
				}
			}

			writer.writeEndElement();

			writer.writeCharacters(System.getProperty("line.separator"));

			writer.writeEndDocument();

			writer.flush();

		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String convertToXML(DataContainer dc) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		convertToXML(dc,stream);
		return stream.toString();
	}

	private static void addKeyValue(XMLStreamWriter writer, String key, Object value) throws XMLStreamException {

		writer.writeStartElement(KEY_VALUE_TAG);
		{
			writer.writeStartElement(KEY_TAG);
			writer.writeCharacters(key);
			writer.writeEndElement();

			addValue(writer,value);
		}	
		writer.writeEndElement();

	}

	private static void addValue(XMLStreamWriter writer,Object value) throws XMLStreamException{
		writer.writeStartElement(VALUE_TAG);
		{
			writer.writeStartElement(VALUE_CLASS_TAG);
			writer.writeCharacters(value.getClass().getName());
			writer.writeEndElement();

			writer.writeStartElement(DATA_TAG);
			basicAddValue(writer,value);
			writer.writeEndElement();
		}	
		writer.writeEndElement();
	}

	@SuppressWarnings("unchecked")
	private static void basicAddValue(XMLStreamWriter writer,Object value) throws XMLStreamException{

		if(!(value instanceof List)) {
			writer.writeCharacters(value.toString());
			return;
		}
		List<String> tagList = (List<String>) value;
		writer.writeStartElement(TAG_LIST);
		// write each list element
		int i = 0;
		for(String tagName : tagList) {
			writer.writeStartElement(TAG_LIST_ELEMENT);
			writer.writeAttribute(TAG_INDEX, Integer.toString(i));
			addValue(writer,tagName);
			writer.writeEndElement();
		}
		writer.writeEndElement();

	}

	private void addData(String key, String valueClassName,Object dataValue) {

		dc.put(key, dataValue);
	}

	public static String[] parseJasperXMLString(String xmlString) {
		List<String> params = new ArrayList<String>();

		try {
			Document doc = loadXMLFromString(xmlString);
			doc.getDocumentElement().normalize();
			NodeList listOfPersons = doc.getElementsByTagName("parameter");
			for (int s = 0; s < listOfPersons.getLength(); s++) {

				Node firstPersonNode = listOfPersons.item(s);

				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {

					Element firstPersonElement = (Element) firstPersonNode;
					params.add(firstPersonElement.getAttribute("name"));
				}

			}

		}  catch (Throwable t) {
			t.printStackTrace();
		}
		return params.toArray(new String[params.size()]);

	}

	private static Document loadXMLFromString(String xml) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		InputSource is = new InputSource(new StringReader(xml));
		try {
			return builder.parse(is);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
