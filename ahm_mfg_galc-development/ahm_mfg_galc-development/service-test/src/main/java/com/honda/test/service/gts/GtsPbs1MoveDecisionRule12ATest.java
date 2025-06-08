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
public class GtsPbs1MoveDecisionRule12ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		issueIndicatorChange("CPLF-pS", 1);
		issueIndicatorChange("CP-pC", 1);
		issueIndicatorChange("CP-pD", 1);
		
		issueIndicatorChange("CB-B4P12", 1);
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test02Move_pC_pG() {
		
		// Check for a current lot vehicle at the head of a storage lane
	         
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+ 1002 ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		addLaneCarrier("pC","1002",1);
		addLaneCarrier("pG","1001",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pC", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pC", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03Move_pD_pG() {
		
		// Check for a current lot vehicle at the head of a storage lane
	         
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1002 ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pC","1002",1);
		addLaneCarrier("pD","1002",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pD", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pD", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test04Move_pS_pG() {
		
		// Check for a current lot vehicle at the head of a storage lane
	         
		// Lane-pS EXIT-pS on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+  ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pD","1002",1);
		addLaneCarrier("pS","1002",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pS", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pS", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test05Move_pS_pG() {
		
		// Non-current Lot. Check for a released vehicle at the head of a storage lane, oldest lot gets priority
    	     
		// Lane-pS EXIT-pS on <+ 1020 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1081 ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+  ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pS","1002",1);
		addLaneCarrier("pS","1020",1);
		addLaneCarrier("pD","1081",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pS", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pS", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test06Move_pD_pG() {
		
		// Non-current Lot. Check for a released vehicle at the head of a storage lane, oldest lot gets priority
    	     
		// Lane-pS EXIT-pS on <+ 1081 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1020 ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+  ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pS","1020",1);
		removeLaneCarrier("pD","1081",1);
		addLaneCarrier("pD","1020",1);
		addLaneCarrier("pS","1081",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pD", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pD", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test07Move_pC_pG() {
		
		// Non-current Lot. Check for a released vehicle at the head of a storage lane, oldest lot gets priority
    	     
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1081 ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+  1020 ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pS","1081",1);
		removeLaneCarrier("pD","1020",1);
		addLaneCarrier("pC","1020",1);
		addLaneCarrier("pD","1081",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pC", "pG", GtsMoveStatus.CREATED);
		resetMoveRequest("pC", "pG");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test08Move_pD_pH() {
		
		// if there are current lot vehicles on lane pC/pD , try to move the head carrier to pH
    	     
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1201 1002 ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pC","1020",1);
		removeLaneCarrier("pD","1081",1);
		
		addLaneCarrier("pD","1201",1);
		addLaneCarrier("pD","1002",2);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG","ENTRY-pH"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pD", "pH", GtsMoveStatus.CREATED);
		resetMoveRequest("pD", "pH");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test09Move_pC_pH() {
		
		// if there are current lot vehicles on lane pC/pD , try to move the head carrier to pH
    	     
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1201 1002 ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pD","1002",2);
		removeLaneCarrier("pD","1201",1);
		
		addLaneCarrier("pC","1201",1);
		addLaneCarrier("pC","1002",2);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG","ENTRY-pH"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pC", "pH", GtsMoveStatus.CREATED);
		resetMoveRequest("pC", "pH");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test10Move_pS_pH() {
		
		// if move possible, try to move pS to pH
    	     
		// Lane-pS EXIT-pS on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pD EXIT-pD on <+ 1201 1002 ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pC EXIT-pC on <+ ---- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pG ENTRY-pG on +>---- ---- ---- 1001
				
		removeLaneCarrier("pC","1002",2);
		removeLaneCarrier("pC","1201",1);
		
		addLaneCarrier("pS","1201",1);
		
		
		toggleGate(true,"EXIT-pS","EXIT-pC","EXIT-pD","ENTRY-pG","ENTRY-pH"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pS", "pH", GtsMoveStatus.CREATED);
		resetMoveRequest("pS", "pH");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
}
