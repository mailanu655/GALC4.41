/**
 * 
 */
package com.honda.galc.client.device.ipuqatester;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author Subu Kathiresan
 *
 */
public class IpuQaTesterSAXContentHandler extends XMLFilterImpl {
	
	String replyAttribute = "";
	String replyValue = "";
	
	@Override
	public void startElement(String namaspaceURI, String localName, String qName, Attributes atts) {
		try {
			super.startElement(namaspaceURI, localName, qName, atts);
            if (atts.getValue(replyAttribute) != null) {
                replyValue = atts.getValue(replyAttribute);
            }
		} catch (SAXException e) {
			e.printStackTrace();
		}
    }
}
