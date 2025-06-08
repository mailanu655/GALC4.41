/**
 * 
 */
package com.honda.galc.client.teamleader.qi.productRecovery;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PartSerialNumberScanType;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.ManualLotControlRepairPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * @author VCC44349
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyService.class)
public class PartResultTest {

	@Mock
	private static ManualLotControlRepairPropertyBean mlcrPropertyBean = PowerMockito.mock(ManualLotControlRepairPropertyBean.class);
	private Map<String, String> quickFixMap = Stream.of(new String[][] {
		  { "FUEL_LEAK", "TRUE" }, 
		  { "WATER_LEAK", "TRUE" }, 
		}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	/**
	 * @throws java.lang.Exception
	 */
	LotControlRuleId lcRuleId = new LotControlRuleId("FUEL_LEAK", "NAE2DL1PM0861");
	LotControlRule lcRule = new LotControlRule();
	
	PartResult myPartResult = null;
	ProductBuildResult buildResult;
	
	ProcessPoint pp = new ProcessPoint();
	
	@Before
	public void setUp() throws Exception {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(Mockito.any(), Mockito.anyString())).thenReturn(mlcrPropertyBean);
		PowerMockito.when(mlcrPropertyBean.getQuickFixHeadLessMap()).thenReturn(quickFixMap);		
		PowerMockito.when(mlcrPropertyBean.isQuickFixHeadLess()).thenReturn(false);
		
		buildResult = Mockito.mock(ProductBuildResult.class);
		PowerMockito.when(buildResult.getPartName()).thenReturn("FUEL_LEAK");
		
		lcRule.setId(lcRuleId);
		lcRule.setSerialNumberScanFlag(PartSerialNumberScanType.NONE.ordinal());
		lcRule.setVerify(true);
		
		pp.setProcessPointId("NAE2DL1PM0861");
		
		myPartResult = new PartResult(lcRule, pp, true);	
		myPartResult.setHeadLess(true);
	}

	public Map<String, String> getQuickFixMap() {
		return quickFixMap;
	}

	public void setQuickFixMap(Map<String, String> quickFixMap) {
		this.quickFixMap = quickFixMap;
	}

	@Test
	/**
	 * quickFix part flag = TRUE
	 * isVerify in LC rule = TRUE
	 * serial number scan flag in LC rule = 0 (NONE)
	 * isQuickFix = FALSE
	 */
	public void testIsQuickFixFalseWhenIsVerify() {
		assertEquals(myPartResult.isQuickFix(), false);
	}

	@Test
	/**
	 * quickFix part flag = TRUE
	 * isVerify in LC rule = FALSE
	 * serial number scan flag in LC rule = 0 (NONE)
	 * isQuickFix = TRUE
	 */
	public void testIsQuickFixTrueWhenNotIsVerify() {
		lcRule.setVerify(false);
		assertEquals(myPartResult.isQuickFix(), true);
	}
	
	@Test
	/**
	 * quickFix part flag = FALSE
	 * isVerify in LC rule = FALSE
	 * serial number scan flag in LC rule = 0 (NONE)
	 * isQuickFix = FALSE
	 */
	public void testIsQuickFixFalseWhenNotQuickFix() {
		lcRule.setVerify(false);
		getQuickFixMap().put("FUEL_LEAK", "FALSE");
		assertEquals(myPartResult.isQuickFix(), false);
	}
}
