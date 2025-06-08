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
public class GtsPbs1MoveDecisionRule02ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
	@BeforeClass
	public static void  loadConfig() {
		loadPbsConfig();
	}

	@Test
	public void test01AddCarriers() {
		testCarrierFrameAssociation(1001,"2HGFC3A50KH750429"); // HCM 01AF201811020220
		testCarrierFrameAssociation(1002,"2HGFC3A52KH750450");
		testCarrierFrameAssociation(1003,"2HGFC3A50KH750446");
		
		testCarrierFrameAssociation(1081,"2HGFC2F82KH507590"); // HCM 01AF201811020270
		testCarrierFrameAssociation(1082,"2HGFC2F84KH507588");
		
		getGtsService().addCarrierByUser("tC", 1, "1001");
		
		TestUtils.sleep(300);
		
		issueIndicatorChange("CB-B2AP9A", 1);
		issueIndicatorChange("CS-T2", 1);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tC");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
	}
	
	
	@Test
	public void test02Move_tC_tH() {
		
		// Lane-tC                          ---- 1001 +> EXIT-tC
		// Lane-tD                               1002 -> EXIT-tD
		// Lane-tH ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-tH on
				
		// -> 1001 move tC-tH
		
		toggleGate("EXIT-tC"); // on
		toggleGate("ENTRY-tH"); // on
		
		issueIndicatorChange("CP-tC", 1);
		
		TestUtils.sleep(300);
		
		
		assertMove("tC", "tH", GtsMoveStatus.CREATED);
		
		issueIndicatorChange("MP-tC-tH", 1);
		
		// check move request is Started
		assertMove("tC", "tH",GtsMoveStatus.STARTED);
		
		issueIndicatorChange("CP-tC", 0);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("tC");
		assertEquals(0,lcs.size());
		
		lcs = findLaneCarriers("tH");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
		issueIndicatorChange("MP-tC-tH", 0);
		assertMove("tC", "tH",GtsMoveStatus.FINISHED);
		
		
	}
	

}
