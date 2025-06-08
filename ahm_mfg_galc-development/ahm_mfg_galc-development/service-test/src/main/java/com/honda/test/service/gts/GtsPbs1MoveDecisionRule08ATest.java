package com.honda.test.service.gts;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsPbs1MoveDecisionRule08ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
	@BeforeClass
	public static void  loadConfig() {
		loadPbsConfig();
	}
	
	@Test
	public void test01AddCarriers() {
		testCarrierFrameAssociation(1001,"2HGFC3A50KH750429"); // HCM 01AF201811020220
		testCarrierFrameAssociation(1002,"2HGFC3A52KH750450");
		testCarrierFrameAssociation(1003,"2HGFC3A50KH750446");
		testCarrierFrameAssociation(1004,"2HGFC3A51KH750441");
		testCarrierFrameAssociation(1005,"2HGFC3A52KH750433");
		
		testCarrierFrameAssociation(1020,"2HGFC4A82KH300863"); // HCM 01AF201811020240
		testCarrierFrameAssociation(1021,"2HGFC4A83KH300869"); // HCM 01AF201811020240
		testCarrierFrameAssociation(1022,"2HGFC4A84KH300864"); // HCM 01AF201811020240
		testCarrierFrameAssociation(1023,"2HGFC4A86KH300865"); // HCM 01AF201811020240
		
		testCarrierFrameAssociation(1081,"2HGFC2F82KH507590"); // HCM 01AF201811020270
		testCarrierFrameAssociation(1082,"2HGFC2F84KH507588");
		testCarrierFrameAssociation(1083,"2HGFC2F84KH507591");
		testCarrierFrameAssociation(1084,"2HGFC2F84KH507607");
		testCarrierFrameAssociation(1085,"2HGFC2F83KH507582");
		testCarrierFrameAssociation(1086,"2HGFC2F83KH507596");
		testCarrierFrameAssociation(1087,"2HGFC2F83KH507601");
		testCarrierFrameAssociation(1088,"2HGFC2F86KH507589");
		testCarrierFrameAssociation(1089,"2HGFC2F86KH507592");
		
		addLaneCarrier("pA","1002",1);
		addLaneCarrier("pH","1001",1);
		addLaneCarrier("pO","1003",1);
		
		
		issueIndicatorChange("CP-pA", 1);
		issueIndicatorChange("CP-pH", 1);
		
		issueIndicatorChange("CB-B1P12", 1);
		issueIndicatorChange("CS-PB1", 0);

	}
	
	
	@Test
	public void test02Move_pA_pI() {
		// pA-pI move possible 
		
		// Lane-pA ---- ---- ---- ---- 1002 +> EXIT-pA
		// Lane-pH ---- ---- ---- 1001 +> EXIT-pH
		// Lane-pB ---- ---- ---- <+ ENTRY-pB
		// Lane-pI ----           <+ ENTRY-pI
		// Lane-pO 1003 ----
		
				
		// -> 1002 move pA-pI
		
		toggleGate(true,"EXIT-pA","EXIT-pH","ENTRY-pB","ENTRY-pI"); // on
		
		issueIndicatorChange("CS-PB1", 1);
		
		TestUtils.sleep(200);
		
		assertMove("pA", "pI", GtsMoveStatus.CREATED);
		resetMoveRequest("pA", "pI");
		assertMove("pH", "pB", GtsMoveStatus.CREATED);
		resetMoveRequest("pH", "pB");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03Move_pH_pB() {
		// Only pH->pB possible
		
		// Lane-pA ---- ---- ---- ---- 1002 -> EXIT-pA
		// Lane-pH ---- ---- ---- 1001 +> EXIT-pH
		// Lane-pB ---- ---- ---- <+ ENTRY-pB
		// Lane-pI ----           <+ ENTRY-pI
		// Lane-pO 1003 ----
		
				
		// -> 1001 move pH-pB
		
		toggleGate(false,"EXIT-pA"); // on
		
		issueIndicatorChange("CS-PB1", 1);
		
		TestUtils.sleep(200);
		
		assertMove("pH", "pB", GtsMoveStatus.CREATED);
		resetMoveRequest("pH", "pB");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test04Move_pA_pB() {
		// Only pA->pB possible (lane pA size >3)
		
		// Lane-pA ---- 1005 1004 1003 1002 -> EXIT-pA
		// Lane-pH ---- ---- ---- 1001 +> EXIT-pH
		// Lane-pB ---- ---- ---- <+ ENTRY-pB
		// Lane-pI ----           <+ ENTRY-pI
		// Lane-pO 1003 ----
		
				
		// -> 1002 move pA-pB
		
		removeLaneCarrier("pO","1003",1);
		
		toggleGate(true,"EXIT-pA"); // on
		toggleGate(false,"ENTRY-pI","EXIT-pH"); // on
		
		
		issueIndicatorChange("CS-PB1", 1);
		
		TestUtils.sleep(200);
		
		assertMove("pA", "pB", GtsMoveStatus.CREATED);
		resetMoveRequest("pA", "pB");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test05Move_pH_pB_Alternate() {
		// pA->pB possible (lane pA size >3) and pH->pB possible 
		// move alternatively
		
		// Lane-pA ---- 1005 1004 1003 1002 -> EXIT-pA
		// Lane-pH ---- ---- ---- 1001 +> EXIT-pH
		// Lane-pB ---- ---- ---- <+ ENTRY-pB
		// Lane-pI ----           <+ ENTRY-pI
		// Lane-pO 1003 ----
		
				
		// -> move pH-pB
		
		toggleGate(true,"EXIT-pH"); // on
		
		issueIndicatorChange("CS-PB1", 1);
		
		TestUtils.sleep(200);
		
		assertMove("pH", "pB", GtsMoveStatus.CREATED);
		resetMoveRequest("pH", "pB");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test06Move_pA_pB_Alternate() {
		// pA->pB possible (lane pA size >3) and pH->pB possible 
		// move alternatively
		
		// Lane-pA ---- 1005 1004 1003 1002 -> EXIT-pA
		// Lane-pH ---- ---- ---- 1001 +> EXIT-pH
		// Lane-pB ---- ---- ---- <+ ENTRY-pB
		// Lane-pI ----           <+ ENTRY-pI
		// Lane-pO 1003 ----
		
				
		// -> move pA-pB
		
		issueIndicatorChange("CS-PB1", 1);
		
		TestUtils.sleep(200);
		
		assertMove("pA", "pB", GtsMoveStatus.CREATED);
		resetMoveRequest("pA", "pB");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
}
