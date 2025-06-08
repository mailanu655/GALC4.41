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
public class GtsPbs1MoveDecisionRule04ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		testCarrierFrameAssociation(1083,"2HGFC2F84KH507591");
		testCarrierFrameAssociation(1084,"2HGFC2F84KH507607");
		testCarrierFrameAssociation(1085,"2HGFC2F83KH507582");
		testCarrierFrameAssociation(1086,"2HGFC2F83KH507596");
		testCarrierFrameAssociation(1087,"2HGFC2F83KH507601");
		
		
		addLaneCarrier("qH","1001",1);
		addLaneCarrier("qE","1002",1);
		
		issueIndicatorChange("CP-qH", 1);
		issueIndicatorChange("CP-qE", 1);
		
		issueIndicatorChange("CB-B34P11", 1);
		issueIndicatorChange("CS-I1", 1);
		issueIndicatorChange("CS-R3", 0);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("qH");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
	}
	
	
	@Test
	public void test02Move_qE_wA() {
		// Only qE->wA possible
		
		// Lane-qH      ---- 1001 -> EXIT-qH
		// Lane-qE      ---- 1002 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		// -> 1001 move qE-wA
		
		toggleGate(true,"EXIT-qE","ENTRY-wA"); // on
		
		issueIndicatorChange("CS-R3", 1);
		
		assertMove("qE", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qE", "wA");
		
		issueIndicatorChange("CS-R3", 1);
		
		toggleGate("ENTRY-wA",false); // on
		toggleGate("EXIT-qE",false); // on
	}
	
	@Test
	public void test03Move_qH_wA() {
		// One qH->wA possible lane move possible only and qE, qH same lot
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1002 -> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		// -> 1001 move qH-wA
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("qH", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qH", "wA");
		
		toggleGate("ENTRY-wA",false); // on
		toggleGate("EXIT-qH",false); // on
	}
	
	@Test
	public void test04Move_qH_wA() {
		// only qH->wA possible and qE Empty
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- ---- -> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		// -> 1001 move qH-wA
		removeLaneCarrier("qE","1002",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("qH", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qH", "wA");
		
		toggleGate("ENTRY-wA",false); // on
		toggleGate("EXIT-qH",false); // on
	}
	
	@Test
	public void test05Move_qE_wA_Empty_Carrier() {
		// only qH->wA possible and qE Empty
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1030 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		addLaneCarrier("qE","1030",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("qE", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qE", "wA");
		
		toggleGate("ENTRY-wA",false); // on
		toggleGate("EXIT-qE",false); // on
	}
	
	@Test
	public void test06Move_qH_wA_Empty_Carrier() {
		// only qH->wA possible and qE Empty
		
		// Lane-qH      ---- 1030 +> EXIT-qH
		// Lane-qE      ---- 1001 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		removeLaneCarrier("qH","1001",1);
		removeLaneCarrier("qE","1030",1);
		addLaneCarrier("qH","1030",1);
		addLaneCarrier("qE","1001",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("qH", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qH", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}
	
	@Test
	public void test07Move_qE_wA_Small_Lot() {
		// only qH->wA possible and qE Empty
		
		// Lane-qH      ---- 1081 +> EXIT-qH
		// Lane-qE      ---- 1001 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		removeLaneCarrier("qH","1030",1);
		addLaneCarrier("qH","1081",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("qE", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qE", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}
	
	@Test
	public void test08Move_qH_wA_Small_Lot() {
		// qH-wA , qE-wA both possible - small lot lane move first
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1081 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		removeLaneCarrier("qH","1081",1);
		removeLaneCarrier("qE","1001",1);
		addLaneCarrier("qH","1001",1);
		addLaneCarrier("qE","1081",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(100);
		
		assertMove("qH", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qH", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}
	
	@Test
	public void test09Move_qE_wA_Same_Lot() {
		// qH-wA , qE-wA both possible - same_lot alternate move
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1002 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		removeLaneCarrier("qE","1081",1);
		addLaneCarrier("qE","1002",1);
		
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(100);
		
		assertMove("qE", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qE", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}
	
	@Test
	public void test10Move_qE_wA_Same_Lot_first_qE() {
		// qH-wA , qE-wA both possible - same_lot alternate move
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1002 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(100);
		
		assertMove("qE", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qE", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}

	@Test
	public void test11Move_qH_wA_Same_Lot() {
		// qH-wA , qE-wA both possible - same_lot alternate move
		
		// Lane-qH      ---- 1001 +> EXIT-qH
		// Lane-qE      ---- 1002 +> EXIT-qE
		// Lane-wA ---- ---- ---- <+ ENTRY-wA on
				
		toggleGate("EXIT-qH",true); // on
		toggleGate("EXIT-qE",true); // on
		toggleGate("ENTRY-wA",true); // on
		
		TestUtils.sleep(100);
		
		assertMove("qH", "wA", GtsMoveStatus.CREATED);
		resetMoveRequest("qH", "wA");
		
		toggleGate("ENTRY-wA",false); // off
		toggleGate("EXIT-qH",false); // off
		toggleGate("EXIT-qE",false); // off
		
	}

}
