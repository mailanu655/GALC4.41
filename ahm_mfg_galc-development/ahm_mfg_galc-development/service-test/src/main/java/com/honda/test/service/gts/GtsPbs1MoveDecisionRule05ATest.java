package com.honda.test.service.gts;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.honda.galc.entity.enumtype.GtsMoveStatus;
import com.honda.test.util.TestUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsPbs1MoveDecisionRule05ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
			
		
		issueIndicatorChange("CP-tY", 1);
		issueIndicatorChange("CP-tS", 1);
		issueIndicatorChange("CB-B2AP9A", 1);
		issueIndicatorChange("CS-T2", 0);

	}
	
	
	@Test
	public void test02Move_tY_tZ() {
		// ENTRY-tW is ff
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1001 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <-ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1002 +> EXIT-tS
				
		// 2 moves at the same time tY-tZ , tS-tC
		
		addLaneCarrier("tY","1001",1);
		addLaneCarrier("tS","1002",1);
		
		toggleGate(false,"ENTRY-tW");
		toggleGate(true,"EXIT-tY","EXIT-tS","ENTRY-tC","ENTRY-tZ"); // on
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tZ", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tZ");
		
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		issueIndicatorChange("CS-T2", 0);

	}
	
	@Test
	public void test03Move_tY_tC_sameLot() {
		// ENTRY-tW is on and tY has carriers < 11 and same lot at tY and tS 
		// move alternatively
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1001 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1002 +> EXIT-tS
						
		// tY-tC
		
		toggleGate(true,"ENTRY-tW");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tC");
		
		
		issueIndicatorChange("CS-T2", 0);

	}
	
	@Test
	public void test04Move_tS_tC_sameLot() {
		// ENTRY-tW is on and tY has carriers < 11 and same lot at tY and tS 
		// move alternatively
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1001 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1002 +> EXIT-tS
						
		// tY-tC
		
		toggleGate(true,"ENTRY-tW");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		
		issueIndicatorChange("CS-T2", 0);

	}
	
	@Test
	public void test05Move_tY_tC_samll_lots() {
		// ENTRY-tW is on and tY has carriers < 11 and small lot at tY 
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1001 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1081 +> EXIT-tS
						
		// tY-tC
		
		removeLaneCarrier("tS","1002",1);
		addLaneCarrier("tS","1081",1);
		
		toggleGate(true,"ENTRY-tW");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tC");
		
		issueIndicatorChange("CS-T2", 0);
	}
	
	@Test
	public void test06Move_tS_tC_samll_lots() {
		// ENTRY-tW is on and tY has carriers < 11 and small lot at tS 
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1081 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1001 +> EXIT-tS
						
		// tS-tC
		
		removeLaneCarrier("tY","1001",1);
		removeLaneCarrier("tS","1081",1);
		addLaneCarrier("tY","1081",1);
		addLaneCarrier("tS","1001",1);
		
		toggleGate(true,"ENTRY-tW");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		issueIndicatorChange("CS-T2", 0);
	}
	
	@Test
	public void test07Move_tS_tC_samll_lots() {
		// ENTRY-tW is on and tY has carriers < 11 and small lot at tS 
		// And size of lane tS, tR,tQ,tZ > 11 , make move tY-tZ at the same time
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1081 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        1005 1004 1003 1002 1001 +> EXIT-tS
		// Lane-tQ, lane-tR has the following carriers
		
		// tS-tC, tY-tZ
		
		addLaneCarrier("tS","1002",2);
		addLaneCarrier("tS","1003",3);
		addLaneCarrier("tS","1004",4);
		addLaneCarrier("tS","1005",5);
		
		addLaneCarrier("tR","1020",1);
		addLaneCarrier("tR","1021",2);
		addLaneCarrier("tR","1022",3);
		addLaneCarrier("tR","1023",4);
		addLaneCarrier("tR","1024",5);
		
		addLaneCarrier("tQ","1083",1);
		addLaneCarrier("tQ","1084",2);
		
		toggleGate(true,"ENTRY-tW");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		assertMove("tY", "tZ", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tZ");
		
		removeLaneCarrier("tS","1002",2);
		removeLaneCarrier("tS","1003",3);
		removeLaneCarrier("tS","1004",4);
		removeLaneCarrier("tS","1005",5);
		
		removeLaneCarrier("tR","1020",1);
		removeLaneCarrier("tR","1021",2);
		removeLaneCarrier("tR","1022",3);
		removeLaneCarrier("tR","1023",4);
		removeLaneCarrier("tR","1024",5);
		
		removeLaneCarrier("tQ","1083",1);
		removeLaneCarrier("tQ","1084",2);
		
		
		issueIndicatorChange("CS-T2", 0);
	}
	
	@Test
	public void test08Move_tS_tC() {
		// ENTRY-tW is on and tY has carriers < 11 
		// move tY->tC and tY->tZ not possible (set CP-tY = 0)
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1081 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1001 +> EXIT-tS
						
		// tS-tC
		
		toggleGate(true,"ENTRY-tW");
		issueIndicatorChange("CP-tY", 0);

		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		issueIndicatorChange("CS-T2", 0);
		issueIndicatorChange("CP-tY", 1);

	}
	
	@Test
	public void test09Move_tY_tZ() {
		// ENTRY-tW is on and tY has carriers < 11 
		// move tY->tC and tS->tC not possible (ENTRY-tC = 0)
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- ---- 1081 +> EXIT-tY
		// Lane-tZ                                                             ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                                       ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                                        ---- ---- ---- ---- 1001 +> EXIT-tS
						
		// tY-tZ
		
		toggleGate(true,"ENTRY-tW");
		toggleGate(false,"ENTRY-tC");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tZ", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tZ");
		
		issueIndicatorChange("CS-T2", 0);

	}
	
	@Test
	public void test10Move_tY_tZ_And_tS_tC() {
		// ENTRY-tW is on and tY has carriers >= 11 
		// move tY->tC and tS->tC not possible (ENTRY-tC = 0)
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ----  1088 1087 1086 1085 1084 1024 1023 1022 1021 1020 1081 +> EXIT-tY
		// Lane-tZ                                          ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                    ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                          ---- ---- ---- 1001 +> EXIT-tS
						
		// tY-tZ and tS-tC
		
		addLaneCarrier("tY","1020",2);
		addLaneCarrier("tY","1021",3);
		addLaneCarrier("tY","1022",4);
		addLaneCarrier("tY","1023",5);
		addLaneCarrier("tY","1024",6);
		addLaneCarrier("tY","1084",7);
		addLaneCarrier("tY","1085",8);
		addLaneCarrier("tY","1086",9);
		addLaneCarrier("tY","1087",10);
		addLaneCarrier("tY","1088",11);
		
		
		toggleGate(true,"ENTRY-tW");
		toggleGate(true,"ENTRY-tC");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tZ", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tZ");
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		issueIndicatorChange("CS-T2", 0);

	}
	
	@Test
	public void test11Move_tY_tC() {
		// ENTRY-tW is on and tY has carriers >= 11 
		// move tY->tC and tS->tC not possible (ENTRY-tC = 0)
		
		// tY->tC, tY->tZ and tS->tZ 
		// Lane-tY ----  1088 1087 1086 1085 1084 1024 1023 1022 1021 1020 1081 +> EXIT-tY
		// Lane-tZ                                          ---- ---- ---- ---- <+ ENTRY-tZ
		// Lane-tC                                                    ---- ---- <+ ENTRY-tC <+ENTRY-tW
		// Lane-tS                                          ---- ---- ---- 1001 +> EXIT-tS
								
		// tY-tZ
		
		toggleGate(true,"ENTRY-tW");
		toggleGate(true,"ENTRY-tC");
		
		issueIndicatorChange("CS-T2", 1);

		TestUtils.sleep(200);
		
		assertMove("tY", "tZ", GtsMoveStatus.CREATED);
		resetMoveRequest("tY", "tZ");
		assertMove("tS", "tC", GtsMoveStatus.CREATED);
		resetMoveRequest("tS", "tC");
		
		issueIndicatorChange("CS-T2", 0);

	}
    

}
