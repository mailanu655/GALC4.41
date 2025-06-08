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
public class BlockBuildResultHistoryTest {

	Timestamp now = null;
	BlockBuildResultHistoryId blockHistId;
	BlockBuildResultHistory blockHist;
	String toString = "";
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		blockHistId = new BlockBuildResultHistoryId();
		blockHistId.setBlockId("5BA0HC1112240467V");
		blockHistId.setPartName("BM1_MOD2_PROCESS_PALLET");
		blockHistId.setActualTimestamp(null);
		
		blockHist = new BlockBuildResultHistory();
		blockHist.setId(blockHistId);
		blockHist.setResultValue("1");
		blockHist.setInstalledPartStatus(1);
		blockHist.setAssociateNo("MC0BL12421");
		blockHist.setUpdateTimestamp(null);
		blockHist.setProcessPointId("MC0BL12421");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSetGetOkStatus() {
		blockHist.setInstalledPartStatus(1);
		assertTrue(blockHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(blockHist.getInstalledPartStatus().equals(1));
	}
	
	@Test
	public void testSetGetNgStatus() {
		blockHist.setInstalledPartStatus(0);
		assertTrue(blockHist.getInstalledPartStatus() instanceof Integer);
		assertTrue(blockHist.getInstalledPartStatus().equals(0));
	}
	
	@Test
	public void testSetGetNullStatus() {
		blockHist.setInstalledPartStatus(null);
		assertNull(blockHist.getInstalledPartStatus());
	}

	@Test
	public void testToStringOkStatus() {
		blockHist.setInstalledPartStatus(1);
		this.toString = "BlockBuildResultHistory "
				+ "[BlockBuildResultHistoryId [blockId=5BA0HC1112240467V, partName=BM1_MOD2_PROCESS_PALLET, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=1, "
				+ "associateNo=MC0BL12421, "
				+ "processPointId=MC0BL12421]";
		assertTrue(blockHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNgStatus() {
		blockHist.setInstalledPartStatus(0);
		this.toString = "BlockBuildResultHistory "
				+ "[BlockBuildResultHistoryId [blockId=5BA0HC1112240467V, partName=BM1_MOD2_PROCESS_PALLET, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=0, "
				+ "associateNo=MC0BL12421, "
				+ "processPointId=MC0BL12421]";
		assertTrue(blockHist.toString().equals(toString));
	}
	
	@Test
	public void testToStringNullStatus() {
		blockHist.setInstalledPartStatus(null);
		this.toString = "BlockBuildResultHistory "
				+ "[BlockBuildResultHistoryId [blockId=5BA0HC1112240467V, partName=BM1_MOD2_PROCESS_PALLET, actualTimestamp=null], "
				+ "resultValue=1, "
				+ "installedPartStatus=null, "
				+ "associateNo=MC0BL12421, "
				+ "processPointId=MC0BL12421]";
		assertTrue(blockHist.toString().equals(toString));
	}
	
	@Test
	public void testHashCodeOkStatus() {
		blockHist.setInstalledPartStatus(1);
		assertTrue(blockHist.hashCode() == -750828893);
	}
	
	@Test
	public void testHashCodeNgStatus() {
		blockHist.setInstalledPartStatus(0);
		assertTrue(blockHist.hashCode() == -750829854);
	}
	
	@Test
	public void testHashCodeNullStatus() {
		blockHist.setInstalledPartStatus(null);
		assertTrue(blockHist.hashCode() == -750829854);
	}
	
	
	@Test
	public void testEqualsNullStatusSameObject() {
		blockHist.setInstalledPartStatus(null);
		BlockBuildResultHistory blockHistCopy = blockHist;
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testEqualsOkStatusSameObject() {
		blockHist.setInstalledPartStatus(1);
		BlockBuildResultHistory blockHistCopy = blockHist;
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testEqualsNgStatusSameObject() {
		blockHist.setInstalledPartStatus(0);
		BlockBuildResultHistory blockHistCopy = blockHist;
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	
	@Test
	public void testEqualsNullStatus() {
		blockHist.setInstalledPartStatus(null);
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testEqualsOkStatus() {
		blockHist.setInstalledPartStatus(1);
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testEqualsNgStatus() {
		blockHist.setInstalledPartStatus(0);
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		assertTrue(blockHist.equals(blockHistCopy));
	}
	
	
	
	@Test
	public void testNotEqualsNullStatus() {
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		blockHist.setInstalledPartStatus(null);
		blockHistCopy.setInstalledPartStatus(1);
		assertFalse(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testNotEqualsOkStatus() {
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		blockHist.setInstalledPartStatus(1);
		blockHistCopy.setInstalledPartStatus(0);
		assertFalse(blockHist.equals(blockHistCopy));
	}
	
	@Test
	public void testNotEqualsNgStatus() {
		BlockBuildResultHistory blockHistCopy = (BlockBuildResultHistory) blockHist.deepCopy();
		blockHist.setInstalledPartStatus(0);
		blockHistCopy.setInstalledPartStatus(null);
		assertFalse(blockHist.equals(blockHistCopy));
	}
}
