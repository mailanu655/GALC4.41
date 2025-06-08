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
public class GtsPbs1MoveDecisionRule09ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		getGtsService().addCarrierByUser("pB", 1, "1001");
		
		TestUtils.sleep(300);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("pB");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
		issueIndicatorChange("CS-PB1", 1);
		issueIndicatorChange("CB-B3P12", 1);
	}
	
	
	@Test
	public void test02Move_From_pB_Not_Possible_Exit_pB_Closed() {
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^- EXIT-pV  off
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD On
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1001 -> EXIT-pB off
   	
		// 1001 at lane pB, EXIT-pB off,
		// ENTRY-pE,ENTRY-pF,ENTRY-pK,ENTRY-pL,ENTRY-pM,ENTRY-pN off
		// no move
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		
		issueIndicatorChange("CP-pB", 1);
		
		TestUtils.sleep(300);
		
		
		assertMove("pB", "pC", GtsMoveStatus.FINISHED);
		assertMove("pB", "pD", GtsMoveStatus.FINISHED);
		assertMove("pB", "pV", GtsMoveStatus.FINISHED);
		
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		
		
	}
	
	@Test
	public void test03Move_Request_pB_pV_pE() {
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pD Off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1001 -> EXIT-pB on

		// 1001 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD,ENTRY-pF,ENTRY-pK,ENTRY-pL,ENTRY-pM, ENTRY-pN off
		// ENTRY-pE,EXIT-pV on
		// MR-pB-pV issued
		
		toggleGate("EXIT-pV"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pE"); // off

	}
	
	@Test
	public void test04Move_Request_pB_pV_pQ_pL() {
		
		// *****************Decision Logic**************
		// All others go to priority lanes (pE,pF,pK,pL first , pC,pD,pM,pN second) 
    	// with fewest carriers
		// *********************************************
       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pD Off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1001 -> EXIT-pB on

		// 1001 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD,ENTRY-pE,ENTRY-pF,ENTRY-pK,ENTRY-pM, ENTRY-pN off
		// EXIT-pV,EXIT-pQ,ENTRY-pL on
		// MR-pB-pV issued
		
		toggleGate("EXIT-pQ"); // on
		toggleGate("ENTRY-pL"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("EXIT-pQ"); // off
		toggleGate("ENTRY-pL"); // off
				
	}
	
	@Test
	public void test05Move_Request_pB_pC_Fewest_Carriers() {
		
		// *****************Decision Logic**************
		// All others go to priority lanes (pE,pF,pK,pL first , pC,pD,pM,pN second) 
    	// with fewest carriers
		// *********************************************
       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1001 -> EXIT-pB on

		// 1001 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD,ENTRY-pE,ENTRY-pF,ENTRY-pK,ENTRY-pL,ENTRY-pM, ENTRY-pN off
		// EXIT-pV,EXIT-pQ,ENTRY-pC,ENTRY-pD on
		// MR-pB-pC issued
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("EXIT-pQ"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
				
	}
	
	@Test
	public void test06Move_Request_pB_pV_Fewest_Carriers() {
		
		// *****************Decision Logic**************
		// All others go to priority lanes (pE,pF,pK,pL first , pC,pD,pM,pN second) 
    	// with fewest carriers
		// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1001 -> EXIT-pB on

		// 1001 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD,ENTRY-pE,ENTRY-pF,ENTRY-pK,ENTRY-pL,ENTRY-pM, ENTRY-pN off
		// EXIT-pV,EXIT-pQ,ENTRY-pC,ENTRY-pD on
		// MR-pB-pV issued (pM has farthest and smallest available spaces (7))
		
		getGtsService().addCarrierByUser("pC", 1, "1081");
		getGtsService().addCarrierByUser("pD", 1, "1082");
		getGtsService().addCarrierByUser("pN", 1, "1083");
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
		toggleGate("EXIT-pQ"); // off
				
	}
	
	@Test
	public void test07Move_Request_pB_pV_Empty_Carrier() {
		
		// *****************Decision Logic**************
		// Empty carrier, bad label body and non_released body goes to the farthest lane
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1091 -> EXIT-pB on
		// empty carrier 1091 at lane pB, EXIT-pB on,
		// MR-pB-pV issued - farthest lane
		
		removeLaneCarrier("pB", "1001", 1);
		getGtsService().addCarrierByUser("pB", 1, "1091");
		List<GtsLaneCarrier> lcs = findLaneCarriers("pB");
		assertEquals(1, lcs.size());
		assertEquals("1091", lcs.get(0).getCarrierId());
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");

		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
		toggleGate("EXIT-pQ"); // off
				
	}
	
	@Test
	public void test08Move_Request_pB_pD_Empty_Carrier() {
		
		// *****************Decision Logic**************
		// Empty carrier, bad label body and non_released body goes to the farthest lane
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1091 -> EXIT-pB on
		
		// empty carrier 1091 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD,ENTRY-pE,ENTRY-pF,ENTRY-pK,ENTRY-pL,ENTRY-pM, ENTRY-pN off
		// EXIT-pV,,ENTRY-pC,ENTRY-pD on
		//EXIT-pQ off
		// MR-pB-pD issued (EXIT-pQ off)
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("pB");
		assertEquals(1, lcs.size());
		assertEquals("1091", lcs.get(0).getCarrierId());
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pD", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pD");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
				
	}
	
	@Test
	public void test09Move_Request_pB_pC_Empty_Carrier() {
		
		// *****************Decision Logic**************
		// Empty carrier, bad label body and non_released body goes to the farthest lane
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pD off
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1091 -> EXIT-pB on
		
		// empty carrier 1091 at lane pB, EXIT-pB on,
		//EXIT-pQ , EXIT-pD off
		// MR-pB-pC issued (EXIT-pQ off)
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("pB");
		assertEquals(1, lcs.size());
		assertEquals("1091", lcs.get(0).getCarrierId());
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
				
	}
	
	@Test
	public void test10Move_Request_pB_pC_Same_Released_Last_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 1. lane with the last body that has the same lot (pC, pD, pE, pF, pK, pL)
      	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK 1086 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		//EXIT-pQ , EXIT-pD off
		// MR-pB-pC issued (EXIT-pQ off)
		
		removeLaneCarrier("pB", "1091", 1);
		getGtsService().addCarrierByUser("pB", 1, "1084");
		List<GtsLaneCarrier> lcs = findLaneCarriers("pB");
		assertEquals(1, lcs.size());
		assertEquals("1084", lcs.get(0).getCarrierId());
		
		getGtsService().addCarrierByUser("pL", 1, "1085");
		getGtsService().addCarrierByUser("pK", 1, "1086");
		getGtsService().addCarrierByUser("pF", 1, "1087");
		getGtsService().addCarrierByUser("pE", 1, "1088");
		
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("ENTRY-pL"); // on
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
		toggleGate("ENTRY-pL"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
		toggleGate("EXIT-pQ"); // off
				
	}
	
	@Test
	public void test11Move_Request_pB_pD_Same_Released_Last_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 1. lane with the last body that has the same lot (pC, pD, pE, pF, pK, pL)
      	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK 1086 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		//EXIT-pQ , EXIT-pD off
		// MR-pB-pD issued 
		
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("ENTRY-pL"); // on
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pD", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pD");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
		toggleGate("ENTRY-pL"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
		toggleGate("EXIT-pQ"); // off
				
	}
	
	@Test
	public void test12Move_Request_pB_pV_Same_Released_Last_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 1. lane with the last body that has the same lot (pC, pD, pE, pF, pK, pL)
      	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK 1086 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pD off
		// Lane-pC 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		//EXIT-pQ , EXIT-pD off
		// MR-pB-pV issued (EXIT-pQ off)
		
		toggleGate("ENTRY-pM"); // on
		toggleGate("ENTRY-pN"); // on
		toggleGate("ENTRY-pL"); // on
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pQ"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pM"); // off
		toggleGate("ENTRY-pN"); // off
		toggleGate("ENTRY-pL"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
		toggleGate("EXIT-pQ"); // off
				
	}
	
	@Test
	public void test13Move_Request_pB_pC_Storage_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 2. goes to empty lane pC,pD,pE,pF,pG,pK,pL if same released lot exists
		// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 1002 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 1001 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		// EXIT-pQ , EXIT-pD off
		// MR-pB-pC issued (move to first available empty lane pC)
		
		removeLaneCarrier("pC", "1081", 1);
		removeLaneCarrier("pD", "1082", 1);
		removeLaneCarrier("pK", "1086", 1);
		
		getGtsService().addCarrierByUser("pE", 2, "1001");
		getGtsService().addCarrierByUser("pF", 2, "1002");
		
		toggleGate("ENTRY-pC"); // on
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pV"); // off
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("EXIT-pV"); // on
		toggleGate("ENTRY-pC"); // off
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
				
	}
	
	@Test
	public void test14Move_Request_pB_pD_Storage_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 2. goes to empty lane pC,pD,pE,pF,pG,pK,pL if same released lot exists
		// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 1002 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 1001 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		// EXIT-pC off
		// MR-pB-pD issued (move to first available empty lane pD)
			
		toggleGate("ENTRY-pD"); // on
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pV"); // off
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pD", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pD");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("EXIT-pV"); // on
		toggleGate("ENTRY-pD"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
				
	}
	
	@Test
	public void test15Move_Request_pB_pV_Storage_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
    	// 2. goes to empty lane pC,pD,pE,pF,pG,pK,pL if same released lot exists
		// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 1002 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 1001 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		// EXIT-pC off
		// MR-pB-pV issued (move to first available empty lane pK)

		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
				
	}
	
	@Test
	public void test16Move_Request_pB_pV_Storage_Lot_Lowest_Lot_Number() {
		
		// *****************Decision Logic**************
		//released carriers
		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), 
		// if the lowest lot number is less than the moving lot number	
        // *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 1002 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 1001 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC off
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD off
		// MR-pB-pV issued (move to first lowest lot lane pE)
		
		getGtsService().addCarrierByUser("pK", 1, "1020");
	
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
				
	}
	
	@Test
	public void test17Move_Request_pB_pC_Storage_Lot_Lowest_Lot_Number() {
		
		// *****************Decision Logic**************
		//released carriers
		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), 
		// if the lowest lot number is less than the moving lot number	
        // *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1087 1002 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1088 1001 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC 1003 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1084 -> EXIT-pB on
		
		// same released lot 1084 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD off
		// MR-pB-pV issued (move to first lowest lot lane pE)
		
		getGtsService().addCarrierByUser("pC", 1, "1003");
	
		toggleGate("ENTRY-pK"); // on
		toggleGate("ENTRY-pF"); // on
		toggleGate("ENTRY-pE"); // on
		toggleGate("ENTRY-pC"); // on
		toggleGate("EXIT-pB"); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB"); // off
		toggleGate("ENTRY-pK"); // off
		toggleGate("ENTRY-pF"); // off
		toggleGate("ENTRY-pE"); // off
		toggleGate("ENTRY-pC"); // off
				
	}
	
	@Test
	public void test18Move_Request_pB_pC_Storage_Lot_greatest_empty_space() {
		
		// *****************Decision Logic**************
		//released carriers
		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), 
		// if the lowest lot number is less than the moving lot number	
        // *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC 1021 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1003 -> EXIT-pB on
		
		// same released lot 1003 at lane pB, EXIT-pB on,
		// ENTRY-pD off
		// MR-pB-pC issued (move to first lowest lot lane pC)
		
		removeLaneCarrier("pB", "1084", 1);
		removeLaneCarrier("pC", "1003", 1);
		removeLaneCarrier("pE", "1088", 1);
		removeLaneCarrier("pF", "1087", 1);
		
		getGtsService().addCarrierByUser("pB", 1, "1003");
		getGtsService().addCarrierByUser("pC", 1, "1021");
		getGtsService().addCarrierByUser("pM", 1, "1086");
		getGtsService().addCarrierByUser("pE", 2, "1088");
		getGtsService().addCarrierByUser("pF", 2, "1087");
		
		toggleGate(true,"ENTRY-pC","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test19Move_Request_pB_pV_Storage_Lot_greatest_empty_space() {
		
		// *****************Decision Logic**************
		//released carriers
		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), 
		// if the lowest lot number is less than the moving lot number	
        // *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC 1021 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1003 -> EXIT-pB on
		
		// released lot 1003 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD off
		// MR-pB-pV issued (move to first lowest lot lane pE)
			
		toggleGate(false,"ENTRY-pC","EXIT-pD");
		toggleGate(true,"EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test20Move_Request_pB_pV_Storage_Lot_greatest_empty_space1() {
		
		// *****************Decision Logic**************
		//released carriers
		//3. goes the lane with lowest lot number (at the end of lane)(pC, pD, pE, pF, pK, pL in order), 
		// if the lowest lot number is less than the moving lot number	
        // *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC 1021 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1003 -> EXIT-pB on
		
		// released lot 1003 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD off
		// MR-pB-pV issued (move to first lowest lot lane pM)
			
		toggleGate(false,"ENTRY-pC","EXIT-pD","ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL");
		toggleGate(true,"EXIT-pV","EXIT-pQ","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test21Move_Request_pB_pC_Current_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
		// Current lot cars go to the lane with the fewest carriers
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK 1020 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC 1021 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1003 -> EXIT-pB on
		
		// released lot 1003 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD on
		// MR-pB-pC issued (current lot )
		
		removeLaneCarrier("pK", "1020", 1);
		removeLaneCarrier("pC", "1021", 1);
		removeLaneCarrier("pB", "1003", 1);
		
		getGtsService().addCarrierByUser("pG", 1, "1020");
		getGtsService().addCarrierByUser("pB", 1, "1021");
		
			
		toggleGate(true,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");

		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test22Move_Request_pB_pV_Current_Lot() {
		
		// *****************Decision Logic**************
		//released carriers
		// Current lot cars go to the lane with the fewest carriers
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1021 -> EXIT-pB on
		// Lane-pG                     ---- ---- ---- 1020 
		
		// released lot 1003 at lane pB, EXIT-pB on,
		// ENTRY-pC,ENTRY-pD on
		// MR-pB-pC issued (current lot )
		
		toggleGate(false,"ENTRY-pC","ENTRY-pD");	
		toggleGate(true,"EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test23Move_Request_pB_pC_Delayed_Lot() {
		// *****************Decision Logic**************
		//released carriers
		// Current lot cars go to the lane with the fewest carriers
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                                 ^+ EXIT-pV  on
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1021 -> EXIT-pB on
		// Lane-pG                     ---- ---- ---- 1020 
			
		// MR-pB-pV issued (current lot )
		
		removeLaneCarrier("pE", "1001", 1);
		removeLaneCarrier("pF", "1002", 1);
		removeLaneCarrier("pB", "1021", 1);
		
		getGtsService().addCarrierByUser("pB", 1, "1001");
		
		toggleGate(true,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pV", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pV");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test24Move_Request_pB_pD_Delayed_Lot() {
		// *****************Decision Logic**************
		//released carriers
		// Current lot cars go to the lane with the fewest carriers
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  off
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD on
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1021 -> EXIT-pB on
		// Lane-pG                     ---- ---- ---- 1020 
			
		// MR-pB-pD issued (current lot )
		
		removeLaneCarrier("pE", "1001", 1);
		removeLaneCarrier("pF", "1002", 1);
		removeLaneCarrier("pB", "1021", 1);
		
		getGtsService().addCarrierByUser("pB", 1, "1001");
		
		toggleGate(false,"EXIT-pV");
		toggleGate(true,"ENTRY-pC","ENTRY-pD","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pD", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pD");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}
	
	@Test
	public void test25Move_Request_pB_pC_Delayed_Lot() {
		// *****************Decision Logic**************
		//released carriers
		// Current lot cars go to the lane with the fewest carriers
    	// *********************************************
       
		// Lane-pN 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1085 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ  on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1002 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1001 1088 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                                 ^+ EXIT-pV  off
		// Lane-pD ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pD off
		// Lane-pC ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pC on
		// Lane-pB                     ---- ---- ---- 1021 -> EXIT-pB on
		// Lane-pG                     ---- ---- ---- 1020 
			
		// MR-pB-pC issued (current lot )
		
		removeLaneCarrier("pE", "1001", 1);
		removeLaneCarrier("pF", "1002", 1);
		removeLaneCarrier("pB", "1021", 1);
		
		getGtsService().addCarrierByUser("pB", 1, "1001");
		
		toggleGate(false,"ENTRY-pD","EXIT-pV");
		toggleGate(true,"ENTRY-pC","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
		toggleGate("EXIT-pB",true); // on
		
		TestUtils.sleep(200);
		
		assertMove("pB", "pC", GtsMoveStatus.CREATED);
		resetMoveRequest("pB", "pC");
		
		toggleGate("EXIT-pB",false); // off
		toggleGate(false,"ENTRY-pC","ENTRY-pD","EXIT-pV","ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN");
				
	}

}
