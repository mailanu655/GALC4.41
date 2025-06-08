package com.honda.galc.rest.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.rest.xml.XmlPayload;
import com.honda.galc.util.TimeTicks;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * @author Subu Kathiresan
 * @date Mar 18, 2015
 */
public class XmlPayloadTest {

	public static String TEST_OPERATION = "TEST OPERATION";
	public static String TEST_MESSAGE = "TEST MESSAGE";
	
	@Test
	public void convertPayloadToXmlTest() {
		
		XmlPayload xmlPayload = new XmlPayload();
		xmlPayload.addParam(new TimeTicks());
		
		MCOperationRevision operationRev = new MCOperationRevision();
		operationRev.setDescription(TEST_OPERATION);
		xmlPayload.addParam(operationRev);
		
		Frame frame = new Frame();
		frame.setProductId("SIM0PRODTRACK1501");
        frame.setStraightShipPercentage(10.0);
        frame.setShortVin("FB2E5002265");
        frame.setTrackingStatus("PRODTR_PP16");
        
		xmlPayload.addParam(frame);
		xmlPayload.addParam(new String(TEST_MESSAGE));

		String resultXml = getXStream().toXML(xmlPayload);
		assertEquals(resultXml, "<?xml version=\"1.0\" ?><com.honda.galc.rest.xml.XmlPayload><com.honda.galc.util.TimeTicks><__value>-1</__value></com.honda.galc.util.TimeTicks><com.honda.galc.entity.conf.MCOperationRevision><revisionId>0</revisionId><description>TEST OPERATION</description><check>0</check><deprecatedRevisionId>0</deprecatedRevisionId></com.honda.galc.entity.conf.MCOperationRevision><com.honda.galc.entity.product.Frame><trackingStatus>PRODTR_PP16</trackingStatus><operations></operations><autoHoldStatus>0</autoHoldStatus><productId>SIM0PRODTRACK1501</productId><straightShipPercentage>10.0</straightShipPercentage><shortVin>FB2E5002265</shortVin></com.honda.galc.entity.product.Frame><string>TEST MESSAGE</string></com.honda.galc.rest.xml.XmlPayload>");
	}
	
	@Test
	public void convertXmlToXmlPayloadTest() {

		XmlPayload xmlPayload = (XmlPayload) getXStream().fromXML("<?xml version=\"1.0\" ?><com.honda.galc.rest.xml.XmlPayload><com.honda.galc.util.TimeTicks><__value>-1</__value></com.honda.galc.util.TimeTicks><com.honda.galc.entity.conf.MCOperationRevision><revisionId>0</revisionId><description>TEST OPERATION</description><check>0</check><deprecatedRevisionId>0</deprecatedRevisionId></com.honda.galc.entity.conf.MCOperationRevision><com.honda.galc.entity.product.Frame><trackingStatus>PRODTR_PP16</trackingStatus><operations></operations><autoHoldStatus>0</autoHoldStatus><productId>SIM0PRODTRACK1501</productId><straightShipPercentage>10.0</straightShipPercentage><shortVin>FB2E5002265</shortVin></com.honda.galc.entity.product.Frame><string>TEST MESSAGE</string></com.honda.galc.rest.xml.XmlPayload>");
		
		TimeTicks timeTicks = (TimeTicks) xmlPayload.getRestParams().get(0);
		assertNotNull(timeTicks);
		
		MCOperationRevision operation = (MCOperationRevision) xmlPayload.getRestParams().get(1);
		assertEquals(operation.getDescription(), TEST_OPERATION);
		
		Frame frame = (Frame) xmlPayload.getRestParams().get(2);
		assertEquals(frame.getId(), "SIM0PRODTRACK1501");
		
		String testMessage = (String) xmlPayload.getRestParams().get(3);
		assertEquals(testMessage, TEST_MESSAGE);
	}
	
	public XStream getXStream() {
		XStream xStream = new XStream(new StaxDriver());
		xStream.registerConverter(new ReflectionConverter(xStream.getMapper(), xStream.getReflectionProvider()), XStream.PRIORITY_LOW); 
		xStream.autodetectAnnotations(Boolean.TRUE);
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.NO_REFERENCES);
		xStream.processAnnotations(XmlPayload.class);
		
		return xStream;
	}
}
