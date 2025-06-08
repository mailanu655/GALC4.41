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
public class CrankshaftBuildResultHistoryTest {

	Timestamp now = null;
	CrankshaftBuildResultHistoryId crankHistId;
	CrankshaftBuildResultHistory crankHist;
	String toString = "";
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		crankHistId = new CrankshaftBuildResultHistoryId();
		crankHistId.setCrankshaftId("SIMCRANKSHAFT001");
		crankHistId.setPartName("SIM_PART");
		crankHistId.setActualTimestamp(null);
		
		crankHist = new CrankshaftBuildResultHistory();
		crankHist.setId(crankHistId);
		crankHist.setResultValue("1");
		crankHist.setInstalledPartStatus(1);
		crankHist.setAssociateNo("SIM_USER");
		crankHist.setUpdateTimestamp(null);
		crankHist.setProcessPointId("SIMPROCPOINT");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetGetOkStatus() {
		crankHist.setInstalledPartStatus(1);
		assertTrue(crankHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(crankHist.getInstalledPartStatus().equals(new Integer(1)));
	}
	
	@Test
	public void testSetGetNgStatus() {
		crankHist.setInstalledPartStatus(0);
		assertTrue(crankHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(crankHist.getInstalledPartStatus().equals(new Integer(0)));
	}
	
	@Test
	public void testSetGetNullStatus() {
		crankHist.setInstalledPartStatus(null);
		assertNull(crankHist.getInstalledPartStatus());
	}
	
	@Test
	public void testToStringOkStatus() {
		crankHist.setInstalledPartStatus(1);
		this.toString = "CrankshaftBuildResultHistory "
				+ "[CrankshaftBuildResultHistoryId [crankshaftId=SIMCRANKSHAFT001, partName=SIM_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=1, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(crankHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNgStatus() {
		crankHist.setInstalledPartStatus(0);
		this.toString = "CrankshaftBuildResultHistory "
				+ "[CrankshaftBuildResultHistoryId [crankshaftId=SIMCRANKSHAFT001, partName=SIM_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=0, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(crankHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNullStatus() {
		crankHist.setInstalledPartStatus(null);
		this.toString = "CrankshaftBuildResultHistory "
				+ "[CrankshaftBuildResultHistoryId [crankshaftId=SIMCRANKSHAFT001, partName=SIM_PART, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=null, "
				+ "associateNo=SIM_USER, "
				+ "processPointId=SIMPROCPOINT]";
		assertTrue(crankHist.toString().equals(toString));
	}
	
	@Test
	public void testHashCodeOkStatus() {
		crankHist.setInstalledPartStatus(1);
		assertTrue(crankHist.hashCode() == 1956171350);
	}
	
	@Test
	public void testHashCodeNgStatus() {
		crankHist.setInstalledPartStatus(0);
		assertTrue(crankHist.hashCode() == 1956170389);
	}
	
	@Test
	public void testHashCodeNullStatus() {
		crankHist.setInstalledPartStatus(null);
		assertTrue(crankHist.hashCode() == 1956170389);
	}
	
	
	@Test
	public void testEqualsNullStatusSameObject() {
		crankHist.setInstalledPartStatus(null);
		CrankshaftBuildResultHistory crankHistCopy = crankHist;
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testEqualsOkStatusSameObject() {
		crankHist.setInstalledPartStatus(1);
		CrankshaftBuildResultHistory crankHistCopy = crankHist;
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testEqualsNgStatusSameObject() {
		crankHist.setInstalledPartStatus(0);
		CrankshaftBuildResultHistory crankHistCopy = crankHist;
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	
	@Test
	public void testEqualsNullStatus() {
		crankHist.setInstalledPartStatus(null);
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testEqualsOkStatus() {
		crankHist.setInstalledPartStatus(1);
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testEqualsNgStatus() {
		crankHist.setInstalledPartStatus(0);
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		assertTrue(crankHist.equals(crankHistCopy));
	}
	
	
	
	@Test
	public void testNotEqualsNullStatus() {
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		crankHist.setInstalledPartStatus(null);
		crankHistCopy.setInstalledPartStatus(1);
		assertFalse(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testNotEqualsOkStatus() {
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		crankHist.setInstalledPartStatus(1);
		crankHistCopy.setInstalledPartStatus(0);
		assertFalse(crankHist.equals(crankHistCopy));
	}
	
	@Test
	public void testNotEqualsNgStatus() {
		CrankshaftBuildResultHistory crankHistCopy = (CrankshaftBuildResultHistory) crankHist.deepCopy();
		crankHist.setInstalledPartStatus(0);
		crankHistCopy.setInstalledPartStatus(null);
		assertFalse(crankHist.equals(crankHistCopy));
	}
}
