package com.honda.test.service.gts;

import static com.honda.galc.service.ServiceFactory.getDao;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.dao.product.HoldResultDao;
import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.galc.entity.enumtype.GtsProductStatus;
import com.honda.galc.entity.enumtype.HoldResultType;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.product.HoldResult;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsPbs1MoveDecisionRule10ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		addLaneCarrier("pP","1001",1);
		
		TestUtils.sleep(200);
		
		List<GtsLaneCarrier> lcs = findLaneCarriers("pP");
		assertEquals(1,lcs.size());
		assertEquals("1001",lcs.get(0).getCarrierId());
		
		issueIndicatorChange("CP-pP", 1);
		
		issueIndicatorChange("CB-B3P12", 1);
		issueIndicatorChange("CB-B9P12", 1);
		issueIndicatorChange("CS-PB2", 1);
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test02NoMove_pP_pN() {
		
		// no move from pP when there is body at lane pQ
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // no move
		
		issueIndicatorChange("CPLF-pQ", 1);
		toggleGate(false,"EXIT-pQ"); // off
		toggleGate(true,"EXIT-pP","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.FINISHED);
		resetMoveRequest("pP", "pN");
		assertMove("pP", "pM", GtsMoveStatus.FINISHED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		issueIndicatorChange("CPLF-pQ", 0);
		
	}
	
	@Test
	public void test03Move_pP_pN() {
		
		// Only pP->pN is possible
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // pP->pN
		
		toggleGate(false,"EXIT-pQ"); // off
		toggleGate(true,"EXIT-pP","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);

	}
	
	
	@Test
	public void test04Move_pP_pM() {
		
		// Only pP->pM is possible
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // pP->pM
		
		toggleGate(true,"EXIT-pP","ENTRY-pM"); // on
		toggleGate(false,"ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test05Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
		// lane pN has 7 carriers , then move pP->pM
		// Lane-pN 1081 1082 1083 1084 1085 1086 1087 ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // pP->pM
		
		addLaneCarrier("pN","1081",1);
		addLaneCarrier("pN","1082",2);
		addLaneCarrier("pN","1083",3);
		addLaneCarrier("pN","1084",4);
		addLaneCarrier("pN","1085",5);
		addLaneCarrier("pN","1086",6);
		addLaneCarrier("pN","1087",7);
		System.out.println("test ing");
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
		removeLaneCarrier("pN","1087",7);
		removeLaneCarrier("pN","1086",6);
		removeLaneCarrier("pN","1085",5);
		removeLaneCarrier("pN","1084",4);
		removeLaneCarrier("pN","1083",3);
		removeLaneCarrier("pN","1082",2);
		removeLaneCarrier("pN","1081",1);
	}
	
	@Test
	public void test06Empty_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // CS-PE-pP1 = 0 empty carrier, move to pN
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // pP->pM
		
		issueIndicatorChange("CS-PE-pP1",0);
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		issueIndicatorChange("CS-PE-pP1",1);
		
	}
	
	@Test
	public void test07Empty_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // CS-PE-pP1 = 0 empty carrier, move to pN
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1201 ^+ EXIT-pP on
		
        // pP->pM
		
		removeLaneCarrier("pP","1001",1);
		addLaneCarrier("pP","1201",1); // 1201 is an empty carrier

		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);

	}

	@Test
	public void test08Non_Released_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // non-released vin, move to pN
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1089 ^+ EXIT-pP on
		
        // pP->pM
		
		removeLaneCarrier("pP","1201",1);
		addLaneCarrier("pP","1089",1); // 1089 is a vehicle on hold

		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test09Released_Carrier_Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle, same lot with pM move to pM
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- 1082 <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1089 ^+ EXIT-pP on
		
        // pP->pM
		
		removeLaneCarrier("pP","1089",1);
		addLaneCarrier("pP","1081",1); // 1081 is a released vehicle
		addLaneCarrier("pM","1082",1); // 1081 is a released vehicle

		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test10Released_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle, same lot with pN move to pN
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- 1082 <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1081 ^+ EXIT-pP on
		
        // pP->pM
		
		removeLaneCarrier("pM","1082",1);
		addLaneCarrier("pN","1082",1); 
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test11Released_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1081 ^+ EXIT-pP on
		
        // lane - pM is empty pP->pM
		
		removeLaneCarrier("pN","1082",1);
		addLaneCarrier("pL","1082",1); 
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test12Released_Carrier_Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1081 ^+ EXIT-pP on
		
        // lane - differnet lot at pM , pN is empty , pP->pN
		
		addLaneCarrier("pM","1021",1); // smaller lot
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test13Released_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		
		// 3. for lane pM and pN. goes the lane with lowest lot number (at the end of lane).
        
		// Lane-pN 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1081 ^+ EXIT-pP on
		
        // lane -  , pP->pN
		
		addLaneCarrier("pN","1001",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test14Released_Carrier_Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		
		// 3. for lane pM and pN. goes the lane with lowest lot number (at the end of lane).
        
		// Lane-pN 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1001 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1081 ^+ EXIT-pP on
		
        // lane -  , pP->pN
		
		removeLaneCarrier("pN","1001",1);
		removeLaneCarrier("pM","1021",1);
		addLaneCarrier("pN","1021",1);
		addLaneCarrier("pM","1001",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test15Released_Carrier_Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		// the lot at lane pP is smaller than lane pM,pN
		//4. goes to the lane with greatest empty spaces
        
		// Lane-pN 1021 1082 ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1002 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // lane -  , pP->pM
		
		removeLaneCarrier("pM","1001",1);
		removeLaneCarrier("pL","1082",1);
		removeLaneCarrier("pP","1081",1);
		
		addLaneCarrier("pP","1001",1);
		addLaneCarrier("pL","1002",1);
		addLaneCarrier("pM","1081",2);
		addLaneCarrier("pN","1082",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test16Released_Carrier_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
        // released vehicle at pP, no released last lot at pM and pN
		// released lots at any of lane (pC,pD,pE,pF,pL,pM,pN)
		// the lot at lane pP is smaller than lane pM,pN
		//4. goes to the lane with greatest empty spaces
        
		// Lane-pN 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1081 1082 ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL 1002 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		
        // lane -  , pP->pN
		
		removeLaneCarrier("pN","1082",1);
		addLaneCarrier("pM","1082",2);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test17Delayed_Lot_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
		// delayed lot at lane pP goes to pN
		
		// Lane-pN 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		// Lane-pG                                    1022 ^+ EXIT-pG on
		
        // lane -  , pP->pN
		
		removeLaneCarrier("pM","1082",2);
		removeLaneCarrier("pM","1081",1);
		removeLaneCarrier("pL","1002",1);
		
		addLaneCarrier("pG","1022",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test18Same_Lot_Move_pP_pN() {
		
		// both pP->pM and pP->pN are possible
		// Same Lot to lane pG goes to greatest lane of pM,pN
		
		// Lane-pN 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		// Lane-pG                                    1002 ^+ EXIT-pP on
		
        // lane -  , pP->pN
		
		removeLaneCarrier("pG","1022",1);
		addLaneCarrier("pG","1002",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
	}
	
	@Test
	public void test19Same_Lot_Move_pP_pM() {
		
		// both pP->pM and pP->pN are possible
		// Same Lot to lane pG goes to greatest lane of pM,pN
		
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM 1021 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pL off
		// Lane-pQ                                         ^- EXIT-pQ off
		// Lane-pP                                    1001 ^+ EXIT-pP on
		// Lane-pG                                    1002 ^+ EXIT-pP on
		
        // lane -  , pP->pN
		
		removeLaneCarrier("pN","1021",1);
		addLaneCarrier("pM","1021",1);
		
		toggleGate(true,"EXIT-pP","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pP", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pP", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		// disable move from pP
		removeLaneCarrier("pP","1001",1);
		toggleGate(false,"EXIT-pP");
		
	}
	
	@Test
	public void test20Empty_Carrier_Move_pQ_pN() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane
	       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1101 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		
        // lane -  , pQ->pN
		
		issueIndicatorChange("CPLF-pQ", 1);
		
		addLaneCarrier("pQ","1101",1); // empty carrier
		
		toggleGate(true,"EXIT-pQ","ENTRY-pL","ENTRY-pM","ENTRY-pN"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test21Empty_Carrier_Move_pQ_pM() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane
	       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1101 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		
        // lane -  , pQ->pM
		
		toggleGate(false,"ENTRY-pN"); // off
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test22Empty_Carrier_Move_pQ_pL() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane
	       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1101 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		
        // lane -  , pQ->pL
		
		toggleGate(false,"ENTRY-pM"); // off
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pL", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pL");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test23NonReleased_Carrier_Move_pQ_pL() {
		
		 // Empty carrier, bad label body and non-released body goes to the farthest lane
	       
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN off
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM off
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1089 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		
        // lane -  , pQ->pL
		
		removeLaneCarrier("pQ","1101",1);
		addLaneCarrier("pQ","1089",1); // 1089 is a vehicle on hold

		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pL", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pL");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test24Released_Carrier_Move_pQ_pL() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
	      
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pL if move possible
		toggleGate(true,"ENTRY-pM","ENTRY-pN"); // on
		
		removeLaneCarrier("pQ","1089",1);
		addLaneCarrier("pQ","1020",1);
		removeLaneCarrier("pM","1021",1);
		addLaneCarrier("pC","1021",1);

		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pL", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pL");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test25Released_Carrier_Move_pQ_pM() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		// 4.1 for pM and pN. goes to the lane with same lot number (end of lane) 
     	  
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pM
		
		addLaneCarrier("pM","1022",1);
		toggleGate(false,"ENTRY-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test26Released_Carrier_Move_pQ_pN() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		// 4.1 for pM and pN. goes to the lane with same lot number (end of lane) 
     	  
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pN
		
		removeLaneCarrier("pM","1022",1);
		addLaneCarrier("pN","1022",1);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test27Released_Carrier_Move_pQ_pM() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		// 4.2 no same last lot at lane pM, pN. goes to the empty lane of lane pM,pN
 		  
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pM
		
		removeLaneCarrier("pN","1022",1);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test28Released_Carrier_Move_pQ_pN() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		// 4.2 no same last lot at lane pM, pN. goes to the empty lane of lane pM,pN
 		  
		// Lane-pN ---- ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pN
		
		addLaneCarrier("pM","1081",1);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test29Released_Carrier_Move_pQ_pN() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		// 4.3 for lane pM and pN. goes the lane with lowest lot number (at the end of lane), 
		// if the lowest lot number is less than the moving lot number
 		  
		// Lane-pN 1001 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pN (smaller lot at pN)
		
		addLaneCarrier("pN","1001",1);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test30Released_Carrier_Move_pQ_pM() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
	    // 1. Storage lot body goes to the lane with last body that has the same lot (pC,pD,pE, pF, pK, pL in order)
		//4.4 goes to the lane with greatest empty spaces (lot at pQ is smaller than last lot of pM,pN)
          
		// Lane-pN 1001 1082 ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL off
		// Lane-pQ                                    1020 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- 1021
		
        // lane -  ,pQ->pN (smaller lot at pN)
		
		addLaneCarrier("pN","1082",2);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test31Delayed_Lot_Move_pQ_pN() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
		 // Delayed lot VIN goes to farthest available lane (Non Release lot)
           
		// Lane-pN 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1002 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- ----
		// Lane-pG                                    1022 ^+ EXIT-pG on
		
        // lane -  ,pQ->pN 
		
		removeLaneCarrier("pQ","1020",1);
		removeLaneCarrier("pC","1021",1);
		removeLaneCarrier("pN","1001",1);
		addLaneCarrier("pQ","1002",1);
		addLaneCarrier("pG","1022",1);
		toggleGate(true,"ENTRY-pL"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pN", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pN");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test32Delayed_Lot_Move_pQ_pM() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
		 // Delayed lot VIN goes to farthest available lane (Non Release lot)
           
		// Lane-pN 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1002 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- ----
		// Lane-pG                                    1022 ^+ EXIT-pG on
				
        // lane -  ,pQ->pM
		
		toggleGate(false,"ENTRY-pN"); // off
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pM", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pM");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test33Delayed_Lot_Move_pQ_pL() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
		 // Delayed lot VIN goes to farthest available lane (Non Release lot)
           
		// Lane-pN 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1002 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- ----
		// Lane-pG                                    1022 ^+ EXIT-pG on
				
		
        // lane -  ,pQ->pN (smaller lot at pN)
		
		toggleGate(false,"ENTRY-pM"); // off
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pL", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pL");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test33New_Lot_Move_pQ_pL() {
		
		// Now the vehicle is a RELEASE one
	    // Storage lot body goes to :
		// Anything else should go to the lane with the fewest carriers
        // (includes the current lot and the upcoming lot)
         
		// Lane-pN 1082 ---- ---- ---- ---- ---- ---- ---- <- ENTRY-pN on
		// Lane-pM 1081 ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pM on
		// Lane-pL ---- ---- ---- ---- ---- ---- ---- ---- <+ ENTRY-pL on
		// Lane-pQ                                    1002 ^+ EXIT-pQ on
		// Lane-pP                               ---- ---- ^- EXIT-pP on
		// Lane-pC                               ---- ----
		// Lane-pG                                    1022 ^+ EXIT-pG on
				
		
        // lane -  ,pQ->pN (smaller lot at pN)
		
		toggleGate(true,"ENTRY-pM","ENTRY-pN"); // on
		removeLaneCarrier("pQ","1002",1);
		addLaneCarrier("pQ","1020",1);
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pQ", "pL", GtsMoveStatus.CREATED);
		resetMoveRequest("pQ", "pL");
		
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	
}
