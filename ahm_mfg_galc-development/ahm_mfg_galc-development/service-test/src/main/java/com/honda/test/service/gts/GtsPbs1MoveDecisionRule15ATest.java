package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.product.HoldResult;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsPbs1MoveDecisionRule15ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
	@BeforeClass
	public static void  loadConfig() {
		loadPbsConfig();
	}

	@Test
	public void test01AddCarriers() {
		
		HoldResult holdResult= new HoldResult("2HGFC2F86KH507592", HoldResultType.HOLD_NOW.getId());
		getDao(HoldResultDao.class).save(holdResult);
		
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
		testCarrierFrameAssociation(1089,"2HGFC2F86KH507592",GtsProductStatus.HOLD);
		
		issueIndicatorChange("CPLF-pV", 1);
		
		issueIndicatorChange("CB-B3P12", 1);
		issueIndicatorChange("CB-B9P12", 1);
		issueIndicatorChange("CS-PB2", 1);
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test02Move_pV_pQ() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane 
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                            1201 ^+ EXIT-pV  on
				
		addLaneCarrier("pV","1201",1); // empty carrier
		
		toggleGate(true,"ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03Move_pV_pK() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane 
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1201^- EXIT-pV  on
				
		toggleGate(false,"ENTRY-pL","EXIT-pQ"); // off
		toggleGate(true,"ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test04Move_pV_pF() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane 
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1201^- EXIT-pV  on
				
		toggleGate(false,"ENTRY-pK"); // off
		toggleGate(true,"ENTRY-pF","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test05Move_pV_pE() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane 
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1201^- EXIT-pV  on
				
		toggleGate(false,"ENTRY-pF"); // off
		toggleGate(true,"ENTRY-pE","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test06Move_pV_pE() {
		
		//released carriers
    	// 1. lane with the last body that has the same lot ( pE, pF, pK, pL)
      	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1002 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1001^- EXIT-pV  on
				
		removeLaneCarrier("pV","1201",1); // empty carrier
		addLaneCarrier("pV","1001",1); // 
		addLaneCarrier("pE","1002",1); // 
		
		toggleGate(false,"ENTRY-pF"); // off
		toggleGate(true,"ENTRY-pE","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test07Move_pV_pF() {
		
		//released carriers
    	// 1. lane with the last body that has the same lot ( pE, pF, pK, pL)
      	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1002 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1001^- EXIT-pV  on
				
		removeLaneCarrier("pE","1002",1); 
		addLaneCarrier("pF","1002",1); // 
		
		toggleGate(false,"ENTRY-pE"); // off
		toggleGate(true,"ENTRY-pF","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test08Move_pV_pK() {
		
		//released carriers
    	// 1. lane with the last body that has the same lot ( pE, pF, pK, pL)
      	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1002 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1001^- EXIT-pV  on
				
		removeLaneCarrier("pF","1002",1); 
		addLaneCarrier("pK","1002",1); // 
		
		toggleGate(false,"ENTRY-pF"); // off
		toggleGate(true,"ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test09Move_pV_pQ() {
		
		//released carriers
    	// 1. lane with the last body that has the same lot ( pE, pF, pK, pL)
      	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1002 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1001^- EXIT-pV  on
				
		removeLaneCarrier("pK","1002",1); 
		addLaneCarrier("pL","1002",1); // 
		
		toggleGate(false,"ENTRY-pK"); // off
		toggleGate(true,"ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test10Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// 2. goes to empty lane pE,pF,pG,pK,pL
		   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1001^- EXIT-pV  on
		// Lane-PC 1002 ---- ---- ---- ---- ---- ---- ----
		
		removeLaneCarrier("pL","1002",1); 
		addLaneCarrier("pC","1002",1); // 
		
		toggleGate(false,"ENTRY-pL","EXIT-pQ"); // off
		toggleGate(true,"ENTRY-pE","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test11Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// 2. goes to empty lane pE,pF,pG,pK,pL
		   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1001^- EXIT-pV  on
		// Lane-PC 1002 ---- ---- ---- ---- ---- ---- ----
				
		toggleGate(false,"ENTRY-pE"); // off
		toggleGate(true,"ENTRY-pF","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test12Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// 2. goes to empty lane pE,pF,pG,pK,pL
		   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1001^- EXIT-pV  on
		// Lane-PC 1002 ---- ---- ---- ---- ---- ---- ----
				
		toggleGate(false,"ENTRY-pF"); // off
		toggleGate(true,"ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test13Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// 2. goes to empty lane pE,pF,pG,pK,pL
		   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE off
		//                                             1001^- EXIT-pV  on
		// Lane-PC 1002 ---- ---- ---- ---- ---- ---- ----
				
		toggleGate(false,"ENTRY-pK"); // off
		toggleGate(true,"ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test14Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		//3. goes the lane with lowest lot number (at the end of lane)(pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE off
		//                                             1081^- EXIT-pV  on
		// Lane-PC 1082 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pC","1002",1); 
		addLaneCarrier("pC","1082",1); // 
		removeLaneCarrier("pV","1001",1); 
		addLaneCarrier("pV","1081",1); // 
		addLaneCarrier("pE","1001",1); // 
		
		toggleGate(false,"ENTRY-pL","EXIT-pQ"); // off
		toggleGate(true,"ENTRY-pE","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test15Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		//3. goes the lane with lowest lot number (at the end of lane)(pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1081^- EXIT-pV  on
		// Lane-PC 1082 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pE","1001",1); // 
		addLaneCarrier("pF","1001",1); // 
		
		toggleGate(false,"ENTRY-pE"); // off
		toggleGate(true,"ENTRY-pF","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test16Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		//3. goes the lane with lowest lot number (at the end of lane)(pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1081^- EXIT-pV  on
		// Lane-PC 1082 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pF","1001",1); // 
		addLaneCarrier("pK","1001",1); // 
		
		toggleGate(false,"ENTRY-pF"); // off
		toggleGate(true,"ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test17Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		//3. goes the lane with lowest lot number (at the end of lane)(pE, pF, pK, pL in order), if the lowest lot number is less than the moving lot number	
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1081^- EXIT-pV  on
		// Lane-PC 1082 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pK","1001",1); // 
		addLaneCarrier("pL","1001",1); // 
		
		toggleGate(false,"ENTRY-pK"); // off
		toggleGate(true,"ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test18Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1086 1087 ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1084 1085 ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1082 1083 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1081 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1021^- EXIT-pV  on
		// Lane-PC 1020 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pC","1082",1); // 
		removeLaneCarrier("pV","1081",1); // 
		removeLaneCarrier("pL","1001",1); //
		
		addLaneCarrier("pC","1020",1); // 
		addLaneCarrier("pV","1021",1); //
		addLaneCarrier("pE","1081",1); //
		addLaneCarrier("pF","1082",1); //
		addLaneCarrier("pF","1083",2); //
		addLaneCarrier("pK","1084",1); //
		addLaneCarrier("pK","1085",2); //
		addLaneCarrier("pL","1086",1); //
		addLaneCarrier("pL","1087",2); //
		
		
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test19Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1086 1087 ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1084 1085 ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1081 1083 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1021^- EXIT-pV  on
		// Lane-PC 1020 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pF","1083",2); //
		addLaneCarrier("pE","1083",2); //
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test20Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1086 1087 ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1084 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1082 1085 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1081 1083 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1021^- EXIT-pV  on
		// Lane-PC 1020 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pK","1085",2); //
		addLaneCarrier("pF","1085",2); //
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test21Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL 1086 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1084 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF 1082 1085 ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE 1081 1083 ---- ---- ---- ---- ---- ---- <- ENTRY-pE on
		//                                             1021^- EXIT-pV  on
		// Lane-PC 1020 ---- ---- ---- ---- ---- ---- ----
				
		removeLaneCarrier("pL","1087",2); //
		addLaneCarrier("pK","1087",2); //
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test22Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible    
        
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1086 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1084 1087 ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF 1082 1085 ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE 1081 1083 ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1021^- EXIT-pV  on
		// Lane-PC 1020 ---- ---- ---- ---- ---- ---- ----
		toggleGate(false,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL"); // off		
		toggleGate(true,"ENTRY-pM","ENTRY-pN","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
		removeLaneCarrier("pC","1020",1); //
		removeLaneCarrier("pE","1083",2); //
		removeLaneCarrier("pE","1081",1); //
		removeLaneCarrier("pF","1085",2); //
		removeLaneCarrier("pF","1082",1); //
		removeLaneCarrier("pK","1087",2); //
		removeLaneCarrier("pK","1084",1); //
		removeLaneCarrier("pL","1086",1); //
		
	}
	
	@Test
	public void test23Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers

    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1020 ---- ---- ---- ---- ---- ---- ----
		
		addLaneCarrier("pG","1020",1); //
		addLaneCarrier("pV","1021",1); //
		addLaneCarrier("pF","1081",1); //
		addLaneCarrier("pK","1082",1); //
		
		
		toggleGate(false,"EXIT-pQ"); // off		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test24Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers

    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1020 ---- ---- ---- ---- ---- ---- ----
		
		removeLaneCarrier("pF","1081",1); //
		addLaneCarrier("pE","1081",1); //
		
		
		toggleGate(false,"EXIT-pQ"); // off		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test25Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers

    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1020 ---- ---- ---- ---- ---- ---- ----
		
		removeLaneCarrier("pK","1082",1); //
		addLaneCarrier("pF","1082",1); //
		
		
		toggleGate(false,"EXIT-pQ"); // off		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test26Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers

    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK 1083 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF 1082 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1020 ---- ---- ---- ---- ---- ---- ----
		
		addLaneCarrier("pK","1083",1); //
		
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test27Delayed_Lot_Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1084 ---- ---- ---- ---- ---- ---- ----
		
		removeLaneCarrier("pE","1081",1); //
		removeLaneCarrier("pF","1082",1); //
		removeLaneCarrier("pK","1083",1); //
		removeLaneCarrier("pG","1020",1); //
		addLaneCarrier("pG","1084",1); //
		
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test28Delayed_Lot_Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1084 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pL","EXIT-pQ"); // on
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test29Delayed_Lot_Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1084 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pK"); //off
		toggleGate(true,"ENTRY-pE","ENTRY-pF","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	  
	@Test
	public void test30Delayed_Lot_Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane
    	   
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ off
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1084 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pF"); //off
		toggleGate(true,"ENTRY-pE","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test31Upcoming_Lot_Move_pV_pE() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane (does not meet condition)
		// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
    	// with fewest carriers
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1001 ---- ---- ---- ---- ---- ---- ----
		
		removeLaneCarrier("pG","1084",1); //
		addLaneCarrier("pG","1001",1); //
		
		toggleGate(true,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pE", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pE");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test32Upcoming_Lot_Move_pV_pF() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane (does not meet condition)
		// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
    	// with fewest carriers
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL on
		//                                                 ^- EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pE on
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1001 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pE");
		toggleGate(true,"ENTRY-pF","ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pF", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pF");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test33Upcoming_Lot_Move_pV_pK() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane (does not meet condition)
		// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
    	// with fewest carriers
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF on
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1001 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pE","ENTRY-pF");
		toggleGate(true,"ENTRY-pK","ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pK", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pK");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test34Upcoming_Lot_Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane (does not meet condition)
		// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
    	// with fewest carriers
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK on
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1001 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pE","ENTRY-pF","ENTRY-pK");
		toggleGate(true,"ENTRY-pL","EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test35Upcoming_Lot_Move_pV_pQ() {
		
		//released carriers
    	// has same lot in storage pC,pD,pE,pF,pK,pL,pM,pN
		// no move possible empty lanes
		// no smaller lot at end of lane pE,pF,pK,pL
		//4. lane pE,pF,pK,pL have bigger lot,  goes to the lane with greatest empty spaces - (does not meet condition)
		//5. Storage lot body goes to the lane pM, pN, if move is possible (does not meet condition)   
		// NOW check the current lot
       	// Current lot cars go to the lane with the fewest carriers (does not meet condition)
		// Delayed lot cars go to the farthest available lane (does not meet condition)
		// All others go to priority lanes (pE,pF,pK,pL first , pM,pN second) 
    	// with fewest carriers
           
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		//                                                 ^+ EXIT-pQ on
		// Lane-pK ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pK off
		// Lane-pF ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pF off
		// Lane-pE ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pE off
		//                                             1021^+ EXIT-pV  on
		// Lane-PG 1001 ---- ---- ---- ---- ---- ---- ----
		
		toggleGate(false,"ENTRY-pE","ENTRY-pF","ENTRY-pK","ENTRY-pL");
		toggleGate(true,"EXIT-pQ","EXIT-pV"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pV", "pQ", GtsMoveStatus.CREATED);
		resetMoveRequest("pV", "pQ");

		issueIndicatorChange("CS-PB1", 0);
		
	}

}
