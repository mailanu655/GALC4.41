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
public class GtsPbs1MoveDecisionRule13ATest extends GtsPbs1MoveDecisionRuleBastTest{
	
	
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
		
		issueIndicatorChange("CP-pG", 1);
		issueIndicatorChange("CPLF-pI", 1);
		
		issueIndicatorChange("CB-B6P12", 1);
		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test02Move_pI_pO() {
		
		// same release lot at pI or pG as lot at pO
		
		// Lane-pI                1002 +> EXIT-pI on
		// Lane-pG ---- ---- ---- ---- +> EXIT-pG on
		// Lane-pO           ---- 1001 <+ ENTRY-pO on
		
        // move pI->pO
		
		addLaneCarrier("pO","1001",1);
		addLaneCarrier("pI","1002",1);
		
		toggleGate(true,"EXIT-pI","EXIT-pG","ENTRY-pO"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pI", "pO", GtsMoveStatus.CREATED);
		resetMoveRequest("pI", "pO");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test03Move_pG_pO() {
		
		// same release lot at pI or pG as lot at pO
		
		// Lane-pI                ---- +> EXIT-pI on
		// Lane-pG ---- ---- ---- 1002 +> EXIT-pG on
		// Lane-pO           ---- 1001 <+ ENTRY-pO on
		
        // move pG->pO
		
		removeLaneCarrier("pI","1002",1);
		addLaneCarrier("pG","1002",1);
		
		toggleGate(true,"EXIT-pI","EXIT-pG","ENTRY-pO"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pG", "pO", GtsMoveStatus.CREATED);
		resetMoveRequest("pG", "pO");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test04Move_pG_pO() {
		
		// any other carrier at pI or pG 
		
		// Lane-pI                ---- +> EXIT-pI on
		// Lane-pG ---- ---- ---- 1002 +> EXIT-pG on
		// Lane-pO           ---- 1020 <+ ENTRY-pO on
		
        // move pI->pO
		
		removeLaneCarrier("pO","1001",1);
		addLaneCarrier("pO","1020",1);
		
		toggleGate(true,"EXIT-pI","EXIT-pG","ENTRY-pO"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pG", "pO", GtsMoveStatus.CREATED);
		resetMoveRequest("pG", "pO");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
	@Test
	public void test05Move_pI_pO() {
		
		// any other carrier at pI or pG 
		
		// Lane-pI                ---- +> EXIT-pI on
		// Lane-pG ---- ---- ---- ---- +> EXIT-pG on
		// Lane-pO           ---- ---- <+ ENTRY-pO on
		
        // move pI->pO
		
		addLaneCarrier("pI","1003",1);
		
		toggleGate(true,"EXIT-pI","EXIT-pG","ENTRY-pO"); // on
		
		issueIndicatorChange("CS-PB1", 1);

		TestUtils.sleep(200);
		
		assertMove("pI", "pO", GtsMoveStatus.CREATED);
		resetMoveRequest("pI", "pO");

		issueIndicatorChange("CS-PB1", 0);
		
	}
	
}
