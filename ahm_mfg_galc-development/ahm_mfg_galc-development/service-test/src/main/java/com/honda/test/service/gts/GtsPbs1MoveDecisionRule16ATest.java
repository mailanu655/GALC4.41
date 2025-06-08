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
public class GtsPbs1MoveDecisionRule16ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		issueIndicatorChange("CPLF-pW", 1);
		issueIndicatorChange("CP-pK", 1);
		issueIndicatorChange("CP-pF", 1);
		issueIndicatorChange("CP-pE", 1);
		
		issueIndicatorChange("CB-B4P12", 1);
		issueIndicatorChange("CS-PB2", 1);
		issueIndicatorChange("CS-PB1", 0);

	}
	
	@Test
	public void test02CurrentLot_Move_pW_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <- 1002  
		// Lane-pK EXIT-pK on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		addLaneCarrier("pG","1001",1);
		addLaneCarrier("pW","1002",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pW", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pW", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03CurrentLot_Move_pK_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pW","1002",1);
		addLaneCarrier("pK","1002",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pK", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pK", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test04CurrentLot_Move_pF_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pK","1002",1);
		addLaneCarrier("pF","1002",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pF", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pF", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test05CurrentLot_Move_pE_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pF","1002",1);
		addLaneCarrier("pE","1002",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pE", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pE", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test06OldestLot_Move_pW_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ 1020  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pE","1002",1);
		addLaneCarrier("pW","1020",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pW", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pW", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test07OldestLot_Move_pK_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ 1020 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pW","1020",1);
		addLaneCarrier("pK","1020",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pK", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pK", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test08OldestLot_Move_pF_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ 1020 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pK","1020",1);
		addLaneCarrier("pF","1020",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pF", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pF", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test09OldestLot_Move_pE_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ 1020 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pF","1020",1);
		addLaneCarrier("pE","1020",1);
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pE", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pE", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test10Empty_Carrier_Move_pW_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ 1201  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pE","1020",1);
		addLaneCarrier("pW","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pW", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pW", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test11Empty_Carrier_Move_pK_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pW","1201",1);
		addLaneCarrier("pK","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pK", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pK", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test12Empty_Carrier_Move_pF_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pK","1201",1);
		addLaneCarrier("pF","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pF", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pF", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test12Empty_Carrier_Move_pE_pS() {
		
	    // just check move possible
	      
		// Lane-pW EXIT-pW on <+ ----  
		// Lane-pK EXIT-pK on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pF EXIT-pF on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pE EXIT-pE on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pS ENTRY-pS on +> ---- 
		// Lane-pG ENTRY-pS on +> 1001 
				
		removeLaneCarrier("pF","1201",1);
		addLaneCarrier("pE","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-pW","EXIT-pK","EXIT-pF","EXIT-pE"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pE", "pS", GtsMoveStatus.CREATED);
		resetMoveRequest("pE", "pS");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
}
