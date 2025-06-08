package com.honda.test.service.gts;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsPbs1MoveDecisionRule01ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
	@BeforeClass
	public static void  loadConfig() {
		loadPbsConfig();
	}
	
	@Test
	public void test01AddCarriers() {
		testCarrierFrameAssociation(1001,"2HGFC3A50KH750429");
		testCarrierFrameAssociation(1002,"2HGFC3A52KH750450");
		testCarrierFrameAssociation(1081,"2HGFC2F82KH507590");
		
		getGtsService().addCarrierByUser("tA", 1, "1001");
		
		TestUtils.sleep(300);
		
		getGtsService().addCarrierByUser("tA", 2, "1002");
		
		issueIndicatorChange("CB-B2AP9A", 1);
		issueIndicatorChange("CS-T2", 1);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tA");
		assertEquals(2,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
		assertEquals("1002",lcs.get(1).getCarrierId());
	}
	
	
	@Test
	public void test02Move_tA_tD() {
		
		toggleGate("EXIT-tA"); // on
		toggleGate("ENTRY-tD"); // on
		toggleGate("ENTRY-tY"); // on
		
		issueIndicatorChange("CP-tA", 1);
		
		TestUtils.sleep(300);
		
		
		assertMove("tA", "tD", GtsMoveStatus.CREATED);
		
		issueIndicatorChange("MP-tA-tD", 1);
		
		// check move request is Started
		assertMove("tA", "tD",GtsMoveStatus.STARTED);
		
		issueIndicatorChange("CP-tA", 0);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tA");
		assertEquals(1,lcs.size());
		assertEquals("1002",lcs.get(0).getCarrierId());
		
		lcs = findLaneCarriers("tD");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
		issueIndicatorChange("MP-tA-tD", 0);
		assertMove("tA", "tD",GtsMoveStatus.FINISHED);
		
		
	}
	
	@Test
	public void test03Move_tA_tY() {
		
		toggleGate("ENTRY-tD"); // off
		
		issueIndicatorChange("CP-tA", 1);
		
		TestUtils.sleep(300);
		
		assertMove("tA", "tY", GtsMoveStatus.CREATED);
		
		issueIndicatorChange("MP-tA-tY", 1);
		
		// check move request is Started
		assertMove("tA", "tY",GtsMoveStatus.STARTED);
		
		issueIndicatorChange("CP-tA", 0);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tA");
		assertEquals(0,lcs.size());
		
		lcs = findLaneCarriers("tY");
		assertEquals(1,lcs.size());
		assertEquals("1002",lcs.get(0).getCarrierId());
		
		issueIndicatorChange("MP-tA-tY", 0);
		assertMove("tA", "tY",GtsMoveStatus.FINISHED);
		
		
	}
	

}
