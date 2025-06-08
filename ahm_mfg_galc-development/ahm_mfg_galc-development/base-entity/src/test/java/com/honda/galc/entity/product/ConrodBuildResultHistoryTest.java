/**
 * 
 */
package com.honda.galc.entity.product;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author VEC28594
 *
 */
public class ConrodBuildResultHistoryTest {

	Timestamp now = null;
	ConrodBuildResultHistoryId conrodHistId;
	ConrodBuildResultHistory conrodHist;
	String toString = "";
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		conrodHistId = new ConrodBuildResultHistoryId();
		conrodHistId.setConrodId("SIMCONROD0000001");
		conrodHistId.setPartName("TEST_PART");
		conrodHistId.setActualTimestamp(null);
		
		conrodHist = new ConrodBuildResultHistory();
		conrodHist.setId(conrodHistId);
		conrodHist.setResultValue("1");
		conrodHist.setInstalledPartStatus(1);
		conrodHist.setAssociateNo("SIM_USER");
		conrodHist.setUpdateTimestamp(null);
		conrodHist.setProcessPointId("SIMPROCPOINT");
	}

	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSetGetOkStatus() {
		conrodHist.setInstalledPartStatus(1);
		assertTrue(conrodHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(conrodHist.getInstalledPartStatus().equals(1));
	}
	
	@Test
	public void testSetGetNgStatus() {
		conrodHist.setInstalledPartStatus(0);
		assertTrue(conrodHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(conrodHist.getInstalledPartStatus().equals(0));
	}
	
	@Test
	public void testSetGetNullStatus() {
		conrodHist.setInstalledPartStatus(null);
		assertNull(conrodHist.getInstalledPartStatus());
	}

	@Test
	public void testToStringOkStatus() {
		conrodHist.setInstalledPartStatus(1);
		this.toString = "ConrodBuildResultHistory [ConrodBuildResultHistoryId [conrodId=SIMCONROD0000001, partName=TEST_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=1, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(conrodHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNgStatus() {
		conrodHist.setInstalledPartStatus(0);
		this.toString = "ConrodBuildResultHistory [ConrodBuildResultHistoryId [conrodId=SIMCONROD0000001, partName=TEST_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=0, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(conrodHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNullStatus() {
		conrodHist.setInstalledPartStatus(null);
		this.toString = "ConrodBuildResultHistory [ConrodBuildResultHistoryId [conrodId=SIMCONROD0000001, partName=TEST_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=null, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(conrodHist.toString().equals(toString));
	}
	
	@Test
	public void testHashCodeOkStatus() {
		conrodHist.setInstalledPartStatus(1);
		assertTrue(conrodHist.hashCode() == -1597309897);
	}
	
	@Test
	public void testHashCodeNgStatus() {
		conrodHist.setInstalledPartStatus(0);
		assertTrue(conrodHist.hashCode() == -1597310858);
	}
	
	@Test
	public void testHashCodeNullStatus() {
		conrodHist.setInstalledPartStatus(null);
		assertTrue(conrodHist.hashCode() == -1597310858);
	}
	
	
	@Test
	public void testEqualsNullStatusSameObject() {
		conrodHist.setInstalledPartStatus(null);
		ConrodBuildResultHistory conrodHistCopy = conrodHist;
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testEqualsOkStatusSameObject() {
		conrodHist.setInstalledPartStatus(1);
		ConrodBuildResultHistory conrodHistCopy = conrodHist;
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testEqualsNgStatusSameObject() {
		conrodHist.setInstalledPartStatus(0);
		ConrodBuildResultHistory conrodHistCopy = conrodHist;
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	
	@Test
	public void testEqualsNullStatus() {
		conrodHist.setInstalledPartStatus(null);
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testEqualsOkStatus() {
		conrodHist.setInstalledPartStatus(1);
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testEqualsNgStatus() {
		conrodHist.setInstalledPartStatus(0);
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		assertTrue(conrodHist.equals(conrodHistCopy));
	}
	
	
	
	@Test
	public void testNotEqualsNullStatus() {
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		conrodHist.setInstalledPartStatus(null);
		conrodHistCopy.setInstalledPartStatus(1);
		assertFalse(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testNotEqualsOkStatus() {
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		conrodHist.setInstalledPartStatus(1);
		conrodHistCopy.setInstalledPartStatus(0);
		assertFalse(conrodHist.equals(conrodHistCopy));
	}
	
	@Test
	public void testNotEqualsNgStatus() {
		ConrodBuildResultHistory conrodHistCopy = (ConrodBuildResultHistory) conrodHist.deepCopy();
		conrodHist.setInstalledPartStatus(0);
		conrodHistCopy.setInstalledPartStatus(null);
		assertFalse(conrodHist.equals(conrodHistCopy));
	}
}
