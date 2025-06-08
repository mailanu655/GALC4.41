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
public class GtsPbs1MoveDecisionRule11ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		issueIndicatorChange("CP-pM", 1);
		issueIndicatorChange("CP-pN", 1);
		issueIndicatorChange("CP-pL", 1);
		issueIndicatorChange("CP-pR", 1);
		
		issueIndicatorChange("CS-PB2", 1);
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test02Move_pR_pT() {
		
	    // just check move possible
	      
		// Lane-pR EXIT-pR on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on +> ---- ---- --
				
		
		addLaneCarrier("pR","1201",1);
		
		toggleGate(true,"EXIT-pR"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pR", "pT", GtsMoveStatus.CREATED);
		resetMoveRequest("pR", "pT");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03Move_pN_pT() {
		
		 // Move any empty carriers at the head of lanes N or M, if lane pR is not possible
	      
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on +> ---- ---- --
				
		
		removeLaneCarrier("pR","1201",1);
		addLaneCarrier("pN","1201",1);
		
		issueIndicatorChange("CS-P12NPpN1CarrierEmpty", 1);
		toggleGate(false,"EXIT-pR"); // need to turn off EXIT-pR before turning on ENTRY-pT
		
		toggleGate(true,"EXIT-pN","EXIT-pM","ENTRY-pT"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pN", "pT", GtsMoveStatus.CREATED);
		resetMoveRequest("pN", "pT");

		issueIndicatorChange("CS-PB1", 0);
		issueIndicatorChange("CS-P12NPpN1CarrierEmpty", 0);
		
	}
	
	@Test
	public void test04Move_pM_pT() {
		
		 // Move any empty carriers at the head of lanes N or M, if lane pR is not possible
	      
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on +> ---- ---- --
				
		
		removeLaneCarrier("pN","1201",1);
		addLaneCarrier("pM","1201",1);
		
		issueIndicatorChange("CS-P12NPpM1CarrierEmpty", 1);
		toggleGate(false,"EXIT-pR"); // need to turn off EXIT-pR before turning on ENTRY-pT
		
		toggleGate(true,"EXIT-pN","EXIT-pM","ENTRY-pT"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pM", "pT", GtsMoveStatus.CREATED);
		resetMoveRequest("pM", "pT");

		issueIndicatorChange("CS-PB1", 0);
		issueIndicatorChange("CS-P12NPpM1CarrierEmpty", 0);
		
	}
	
	@Test
	public void test05Move_pN_pW() {
		
		 // same released lot with lane pG (Current lot)
		         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pM","1201",1);
		addLaneCarrier("pN","1002",1);
		addLaneCarrier("pG","1001",1);
		
		toggleGate(false,"ENTRY-pT"); 
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pN", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pN", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test06Move_pM_pW() {
		
		 // same released lot with lane pG (Current lot)
		    
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pN","1002",1);
		addLaneCarrier("pM","1002",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pM", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pM", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test07Move_pL_pW() {
		
		 // same released lot with lane pG (Current lot)
	      
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pM","1002",1);
		addLaneCarrier("pL","1002",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pL", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pL", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test08Move_pN_pW() {
		
		// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pL","1002",1);
		addLaneCarrier("pN","1020",1);
		addLaneCarrier("pM","1081",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pN", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pN", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test09Move_pM_pW() {
		
		// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ 1002 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pN","1020",1);
		removeLaneCarrier("pM","1081",1);
		addLaneCarrier("pN","1081",1);
		addLaneCarrier("pM","1020",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pM", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pM", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test10Move_pN_pW() {
		
		// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pN","1081",1);
		removeLaneCarrier("pM","1020",1);
		addLaneCarrier("pN","1201",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pN", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pN", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test11Move_pM_pW() {
		
		// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pN","1201",1);
		addLaneCarrier("pM","1201",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pM", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pM", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test12Move_pL_pW() {
		
		// Check for a released vehicle at the head of a storage lane, oldest lot gets priority
         
		// Lane-pR EXIT-pR on <- ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pN EXIT-pN on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pM EXIT-pM on <+ 1201 ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pL EXIT-pL on <+ ---- ---- ---- ---- ---- ---- ---- ---- 
		// Lane-pT ENTRY-pT on -> ---- ---- --
		// Lane-pG                1001 ---- --
		
				
		
		removeLaneCarrier("pM","1201",1);
		addLaneCarrier("pL","1201",1);
		
		toggleGate(true,"EXIT-pN","EXIT-pM","EXIT-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pL", "pW", GtsMoveStatus.CREATED);
		resetMoveRequest("pL", "pW");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
}
