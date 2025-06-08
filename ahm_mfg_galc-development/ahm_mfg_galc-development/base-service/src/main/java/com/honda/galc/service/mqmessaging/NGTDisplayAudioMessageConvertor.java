package com.honda.galc.service.mqmessaging;

import java.io.StringWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.service.mq.IMqMessageConvertor;
/**
 * 
 * <h3>NGTDisplayAudioMessageConvertor Class description</h3>
 * <p>
 * NGTDisplayAudioMessageConvertor description
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
 * @author Haohua Xie<br>
 *         Feb 25, 2014
 * 
 * 
 */
public class NGTDisplayAudioMessageConvertor implements IMqMessageConvertor {

	protected void createNode(XMLEventWriter eventWriter, String name,
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

	protected Map<Object, Object> createOutData(DataContainer dc) {
		Collection<?> tags = (Collection<?>) dc.get(DataContainerTag.TAG_LIST);
		if (tags == null) {
			return dc;
		}
		//use LinkedHashMap to guarantee the output has the same order as date format definitions.
		Map<Object, Object> outDc = new LinkedHashMap<Object, Object>(tags.size());
		for (Object key : tags) {
			outDc.put(key, dc.get(key));
		}
		return outDc;
	}

	@Override
	public String convert(DataContainer dc) throws Exception {
		Map<Object, Object> outDc = createOutData(dc);
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
		StringWriter stringWriter = new StringWriter();
		XMLEventWriter eventWriter;
		try {
			eventWriter = outputFactory.createXMLEventWriter(stringWriter);

			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");

			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			eventWriter.add(end);
			StartElement configStartElement = eventFactory.createStartElement(
					"", "", "AUTO_INSTALL");
			eventWriter.add(configStartElement);
			eventWriter.flush();
			eventWriter.add(end);

			for (Map.Entry<?, ?> entry : outDc.entrySet()) {
				createNode(eventWriter, entry.getKey().toString(), entry
						.getValue().toString());
			}

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

}
