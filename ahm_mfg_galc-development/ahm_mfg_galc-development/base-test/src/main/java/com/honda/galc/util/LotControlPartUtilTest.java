package com.honda.galc.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.ProductSpec;

public class LotControlPartUtilTest {

	@Test
	public void testGetLotControlRuleByProductSpec(){
		String productSpecCode = "GT6NAB600 N478M     IN";
		ProductSpec productSpec = new com.honda.galc.entity.product.FrameSpec();
		productSpec.setProductSpecCode(productSpecCode);
		productSpec.setModelYearCode("G");productSpec.setModelCode("T6N");
		productSpec.setModelTypeCode("AB6");productSpec.setModelOptionCode("00");
		
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule1 = new LotControlRule();
		LotControlRuleId id1 = new LotControlRuleId();
		id1.setProductSpecCode("*T6N");
		id1.setProcessPointId("processPointId");
		id1.setPartName("partName1");
		rule1.setId(id1);rule1.setSequenceNumber(1);
		
		LotControlRule rule2 = new LotControlRule();
		LotControlRuleId id2 = new LotControlRuleId();
		id2.setProductSpecCode("G   AB6");
		id2.setProcessPointId("processPointId");
		id2.setPartName("partName2");
		rule2.setId(id2);rule2.setSequenceNumber(2);
		
		LotControlRule rule3 = new LotControlRule();
		LotControlRuleId id3 = new LotControlRuleId();
		id3.setProductSpecCode("GT6N                IN");
		id3.setProcessPointId("processPointId");
		id3.setPartName("partName3");
		rule3.setId(id3);rule3.setSequenceNumber(3);
		
		rules.add(rule1);rules.add(rule2);rules.add(rule3);
		
		List<LotControlRule> selectedRules = LotControlPartUtil.getLotControlRuleByProductSpec(productSpec,rules);
		assertEquals(selectedRules.size(),3);
	}
	
	@Test
	public void testGetTheMostMatchedRules(){
		String productSpecCode = "GT6NAB600 N478M     IN";
		ProductSpec productSpec = new com.honda.galc.entity.product.FrameSpec();
		productSpec.setProductSpecCode(productSpecCode);
		
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule1 = new LotControlRule();
		LotControlRuleId id1 = new LotControlRuleId();
		id1.setProductSpecCode("*T6N");
		id1.setProcessPointId("processPointId1");
		id1.setPartName("partName1");
		rule1.setId(id1);
		
		LotControlRule rule2 = new LotControlRule();
		LotControlRuleId id2 = new LotControlRuleId();
		id2.setProductSpecCode("G   AB7");
		id2.setProcessPointId("processPointId2");
		id2.setPartName("partName1");
		rule2.setId(id2);
		
		LotControlRule rule3 = new LotControlRule();
		LotControlRuleId id3 = new LotControlRuleId();
		id3.setProductSpecCode("GT6N                IN");
		id3.setProcessPointId("processPointId3");
		id3.setPartName("partName1");
		rule3.setId(id3);
		
		rules.add(rule1);rules.add(rule2);rules.add(rule3);
		List<LotControlRule> selectedRules = LotControlPartUtil.getTheMostMatchedRules(productSpec, rules);
		
		assertEquals(selectedRules.size(),2);
	}
	
	
}
