/**
 * 
 */
package com.honda.galc.client.device.ipuqatester.model;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

/**
 * @author Subu Kathiresan
 * @date Feb 3, 2012
 */
public class UnitInTestTest {

	private String TEST_FILE_PATH = "datafiles" + File.separator + "IpuQaTest.xml";
	
	private UnitInTest unitInTest = null;
	
	public UnitInTestTest() {}

	@Test
	public void xmlToObject() {
		XStream xs = new XStream();
		xs.processAnnotations(UnitInTest.class);
		xs.processAnnotations(Process.class);
		xs.processAnnotations(com.honda.galc.client.device.ipuqatester.model.Test.class);
		xs.processAnnotations(TestParam.class);
		xs.processAnnotations(TestAttrib.class);

		try {
			unitInTest = (UnitInTest) xs.fromXML(new FileInputStream(TEST_FILE_PATH));
			assertEquals("SV31105100086", unitInTest.getProductId());
			assertEquals("5, 0, 0, 6", unitInTest.getProcesses().get(0).getSoftwareVersion());
			assertEquals(true, unitInTest.isTotalStatusPass());
			assertEquals(11, unitInTest.getProcesses().size());
			assertEquals(1, unitInTest.getProcesses().get(0).getTests().size());
			assertEquals(3, unitInTest.getProcesses().get(0).getTests().get(0).getTestParams().size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void objectToXml() {
		XStream xs = new XStream();
		xs.processAnnotations(UnitInTest.class);
		xs.processAnnotations(Process.class);
		xs.processAnnotations(com.honda.galc.client.device.ipuqatester.model.Test.class);
		xs.processAnnotations(TestParam.class);
		xs.processAnnotations(TestAttrib.class);

		UnitInTest unitInTestDummy = (UnitInTest) xs.fromXML(xs.toXML(unitInTest));
		assertEquals(unitInTest, unitInTestDummy);
	}
}
