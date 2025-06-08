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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GtsTbs1MoveDecisionRule01ATest extends GtsTbs1MoveDecisionRuleBastTest{
	
	
	@BeforeClass
	public static void  loadConfig() {
		loadTbsConfig();
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
		
		issueIndicatorChange("CP-toC", 1);
		
		issueIndicatorChange("CB-P13-B5P13", 1);
		issueIndicatorChange("CS-P13-SD1", 0);
		
	}
	
	@Test
	public void test02Move_toC_toD() {
		
	    // just check move possible
	      
		// Lane-toC                ---- ---- 1001 +> EXIT-toC on
		// Lane-toD                ---- ---- ---- <+ ENTRY-toD on
		// Lane-tsA                ---- ---- ---- <+ ENTRY-tsA on
		
		addLaneCarrier("toC","1001",1);
		
		toggleGate(true,"EXIT-toC","ENTRY-toD","ENTRY-tsA"); // on
		
		issueIndicatorChange("CS-P13-SD1", 1);
		
		assertMove("toC", "toD", GtsMoveStatus.CREATED);
		resetMoveRequest("toC", "toD");

		issueIndicatorChange("CS-P13-SD1", 0);
		
		
	}
	
	@Test
	public void test03Move_toC_tsA() {
		
	    // just check move possible
	      
		// Lane-toC                ---- ---- 1001 +> EXIT-toC on
		// Lane-toD                ---- ---- ---- <+ ENTRY-toD on
		// Lane-tsA                ---- ---- ---- <+ ENTRY-tsA on
		
		addLaneCarrier("toC","1001",1);
		
		toggleGate(false,"ENTRY-toD"); // off
		
		issueIndicatorChange("CS-P13-SD1", 1);
		
		assertMove("toC", "tsA", GtsMoveStatus.CREATED);
		resetMoveRequest("toC", "tsA");

		issueIndicatorChange("CS-P13-SD1", 0);
		
		
	}
	
}
