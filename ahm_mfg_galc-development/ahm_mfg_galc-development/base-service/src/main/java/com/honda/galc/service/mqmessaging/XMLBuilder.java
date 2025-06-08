package com.honda.galc.service.mqmessaging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import com.honda.galc.data.DataContainer;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>XMLBuilder Class description</h3>
 * <p>
 * XMLBuilder description
 * </p>
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
 * @author Deshane Joseph<br>
 *         Apr 12, 2013
 * 
 * 
 */

public class XMLBuilder {
	private DataContainer dc;

	public XMLBuilder(DataContainer dc) {
		this.dc = dc;
	}

	public String buildXMLString() {

		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		StringWriter stringWriter = new StringWriter();
		XMLEventWriter eventWriter;
		try {
			eventWriter = outputFactory.createXMLEventWriter(stringWriter);

			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");

			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);

			StartElement configStartElement = eventFactory.createStartElement(
					"", "", "AUTO_INSTALL");
			eventWriter.add(configStartElement);
			eventWriter.add(end);

			createNode(eventWriter, "AUTO_VIN", (String) dc.get("PRODUCT_ID"));
			createNode(eventWriter, "AUTO_MODEL_ID",
					(String) dc.get("AUTO_MODEL_ID"));
			createNode(eventWriter, "AUTO_MFR_COLOR_CD",
					(String) dc.get("AUTO_MFR_COLOR_CD"));
			createNode(eventWriter, "AUTO_MODEL_TYPE_CD",
					(String) dc.get("AUTO_MODEL_TYPE_CD"));
			createNode(eventWriter, "TELEMATICS_TCU_SRL_NUM",
					(String) dc.get("PART_SERIAL_NUMBER"));
			String siteCode = null;
			if (PropertyService.getSiteName().equals("HMA"))
				siteCode = "B";
			else
				siteCode = "";
			createNode(eventWriter, "AUTO_MFR_PLANT_ID", siteCode);

			eventWriter.add(eventFactory.createEndElement("", "",
					"AUTO_INSTALL"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();

		} catch (XMLStreamException e) {

			e.printStackTrace();
		}

		return stringWriter.toString();

	}

	private void createNode(XMLEventWriter eventWriter, String name,
			String value) throws XMLStreamException {

		XMLEventFactory eventFactory = XMLEventFactory.newInstance();
		XMLEvent end = eventFactory.createDTD("\n");
		XMLEvent tab = eventFactory.createDTD("\t");

		StartElement sElement = eventFactory.createStartElement("", "", name);
		eventWriter.add(tab);
		eventWriter.add(sElement);

		Characters characters = eventFactory.createCharacters(value);
		eventWriter.add(characters);

		EndElement eElement = eventFactory.createEndElement("", "", name);
		eventWriter.add(eElement);
		eventWriter.add(end);

	}

}
