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
public class HeadBuildResultHistoryTest {

	Timestamp now = null;
	HeadBuildResultHistoryId headHistId;
	HeadBuildResultHistory headHist;
	String toString = "";
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		headHistId = new HeadBuildResultHistoryId();
		headHistId.setHeadId("5BA0HC0C22BAE413C");
		headHistId.setPartName("MC MOD 3 STATUS");
		headHistId.setActualTimestamp(null);
		
		headHist = new HeadBuildResultHistory();
		headHist.setId(headHistId);
		headHist.setResultValue("1");
		headHist.setInstalledPartStatus(1);
		headHist.setAssociateNo("MC0HD12221");
		headHist.setUpdateTimestamp(null);
		headHist.setProcessPointId("MC0HD12221");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSetGetOkStatus() {
		headHist.setInstalledPartStatus(1);
		assertTrue(headHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(headHist.getInstalledPartStatus().equals(1));
	}
	
	@Test
	public void testSetGetNgStatus() {
		headHist.setInstalledPartStatus(0);
		assertTrue(headHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(headHist.getInstalledPartStatus().equals(0));
	}
	
	@Test
	public void testSetGetNullStatus() {
		headHist.setInstalledPartStatus(null);
		assertNull(headHist.getInstalledPartStatus());
	}

	@Test
	public void testToStringOkStatus() {
		headHist.setInstalledPartStatus(1);
		this.toString = "HeadBuildResultHistory [HeadBuildResultHistoryId [headId=5BA0HC0C22BAE413C, partName=MC MOD 3 STATUS, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=1, "
				+ "associateNo=MC0HD12221, "
				+ "processPointId=MC0HD12221]";
		assertTrue(headHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNgStatus() {
		headHist.setInstalledPartStatus(0);
		this.toString = "HeadBuildResultHistory [HeadBuildResultHistoryId [headId=5BA0HC0C22BAE413C, partName=MC MOD 3 STATUS, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=0, "
				+ "associateNo=MC0HD12221, "
				+ "processPointId=MC0HD12221]";
		assertTrue(headHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNullStatus() {
		headHist.setInstalledPartStatus(null);
		this.toString = "HeadBuildResultHistory [HeadBuildResultHistoryId [headId=5BA0HC0C22BAE413C, partName=MC MOD 3 STATUS, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=null, "
				+ "associateNo=MC0HD12221, "
				+ "processPointId=MC0HD12221]";
		assertTrue(headHist.toString().equals(toString));
	}
	
	@Test
	public void testHashCodeOkStatus() {
		headHist.setInstalledPartStatus(1);
		assertTrue(headHist.hashCode() == 940465076);
	}
	
	@Test
	public void testHashCodeNgStatus() {
		headHist.setInstalledPartStatus(0);
		assertTrue(headHist.hashCode() == 940464115);
	}
	
	@Test
	public void testHashCodeNullStatus() {
		headHist.setInstalledPartStatus(null);
		assertTrue(headHist.hashCode() == 940464115);
	}
	
	
	@Test
	public void testEqualsNullStatusSameObject() {
		headHist.setInstalledPartStatus(null);
		HeadBuildResultHistory headHistCopy = headHist;
		assertTrue(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testEqualsOkStatusSameObject() {
		headHist.setInstalledPartStatus(1);
		HeadBuildResultHistory headHistCopy = headHist;
		assertTrue(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testEqualsNgStatusSameObject() {
		headHist.setInstalledPartStatus(0);
		HeadBuildResultHistory headHistCopy = headHist;
		assertTrue(headHist.equals(headHistCopy));
	}
	
	
	@Test
	public void testEqualsNullStatus() {
		headHist.setInstalledPartStatus(null);
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		assertTrue(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testEqualsOkStatus() {
		headHist.setInstalledPartStatus(1);
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		assertTrue(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testEqualsNgStatus() {
		headHist.setInstalledPartStatus(0);
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		assertTrue(headHist.equals(headHistCopy));
	}
	
	
	
	@Test
	public void testNotEqualsNullStatus() {
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		headHist.setInstalledPartStatus(null);
		headHistCopy.setInstalledPartStatus(1);
		assertFalse(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testNotEqualsOkStatus() {
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		headHist.setInstalledPartStatus(1);
		headHistCopy.setInstalledPartStatus(0);
		assertFalse(headHist.equals(headHistCopy));
	}
	
	@Test
	public void testNotEqualsNgStatus() {
		HeadBuildResultHistory headHistCopy = (HeadBuildResultHistory) headHist.deepCopy();
		headHist.setInstalledPartStatus(0);
		headHistCopy.setInstalledPartStatus(null);
		assertFalse(headHist.equals(headHistCopy));
	}
}
