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
public class GtsPbs1MoveDecisionRule17ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		issueIndicatorChange("CP-tE", 1);
		issueIndicatorChange("CPLF-tF", 1);
		
		issueIndicatorChange("CB-B1P9A", 1);
		issueIndicatorChange("CS-I1", 1);
		issueIndicatorChange("CS-R3", 0);
	}
	
	@Test
	public void test02Move_tE_tG() {
		
	    // Only one move possible
	      
		// Lane-tE                ---- ---- 1001 +> EXIT-tE on
		// Lane-tF                          ---- +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		addLaneCarrier("tE","1001",1);
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tE", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tE", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test03Move_tF_tG() {
		
	    // Only move tF->tG possible and lane tE is empty
	      
		// Lane-tE                ---- ---- ---- +> EXIT-tE on
		// Lane-tF                          1001 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tE","1001",1);
		addLaneCarrier("tF","1001",1);
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test04Move_tF_tG() {
		
	    // Only move tF->tG possible and lane tE is not empty (same Lot)
		// EXIT-tE off 
	      
		// Lane-tE                ---- ---- 1002 -> EXIT-tE off
		// Lane-tF                          1001 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		addLaneCarrier("tE","1002",1);
		
		toggleGate(false,"EXIT-tE");
		toggleGate(true,"EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test05Move_tF_tG() {
		
	    // Only move tF->tG possible and lane tE has bigger Lot)
		// EXIT-tE on 
	      
		// Lane-tE                ---- ---- 1020 -> EXIT-tE on
		// Lane-tF                          1001 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tE","1002",1);
		addLaneCarrier("tE","1020",1);
		
		issueIndicatorChange("CP-tE", 0);
		
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		issueIndicatorChange("CP-tE", 1);
		
		
	}

	@Test
	public void test06Empty_Carrier_Move_tE_tG() {
		
		 //Both lane possible, empty or bad read carriers priority       
	      
		// Lane-tE                ---- ---- 1201 -> EXIT-tE on
		// Lane-tF                          1001 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tE","1020",1);
		addLaneCarrier("tE","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tE", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tE", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test07Empty_Carrier_Move_tF_tG() {
		
		 //Both lane possible, empty or bad read carriers priority       
	      
		// Lane-tE                ---- ---- 1001 -> EXIT-tE on
		// Lane-tF                          1201 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tE","1201",1);
		removeLaneCarrier("tF","1001",1);
		
		addLaneCarrier("tE","1001",1);
		addLaneCarrier("tF","1201",1); // empty carrier
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test08Released_Carrier_Move_tE_tG() {
		
		 //Both lane possible, released carriers 
		 // small lot lane goes first
	      
		// Lane-tE                ---- ---- 1001 -> EXIT-tE on
		// Lane-tF                          1020 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tF","1201",1);
		addLaneCarrier("tF","1020",1); 
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tE", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tE", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test09Released_Carrier_Move_tF_tG() {
		
		 //Both lane possible, released carriers 
		 // small lot lane goes first
	      
		// Lane-tE                ---- ---- 1081 -> EXIT-tE on
		// Lane-tF                          1020 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tE","1001",1);
		addLaneCarrier("tE","1081",1); 
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test09Same_Lot_Alternative_Move_tE_tG() {
		
		 //Both lane possible, released carriers 
		 // small lot lane goes first
	      
		// Lane-tE                ---- ---- 1081 -> EXIT-tE on
		// Lane-tF                          1082 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		removeLaneCarrier("tF","1020",1);
		addLaneCarrier("tF","1082",1); 
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tE", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tE", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
	@Test
	public void test10Same_Lot_Alternative_Move_tF_tG() {
		
		 //Both lane possible, released carriers 
		 // small lot lane goes first
	      
		// Lane-tE                ---- ---- 1081 -> EXIT-tE on
		// Lane-tF                          1082 +> EXIT-tF on
		// Lane-tG ---- ---- ---- ---- ---- ---- <+ ENTRY-wQ on
		
		toggleGate(true,"EXIT-tE","EXIT-tF","ENTRY-tG"); // on
		
		issueIndicatorChange("CS-R3", 1);

		TestUtils.sleep(200);
		
		assertMove("tF", "tG", GtsMoveStatus.CREATED);
		resetMoveRequest("tF", "tG");

		issueIndicatorChange("CS-R3", 0);
		
	}
	
}
