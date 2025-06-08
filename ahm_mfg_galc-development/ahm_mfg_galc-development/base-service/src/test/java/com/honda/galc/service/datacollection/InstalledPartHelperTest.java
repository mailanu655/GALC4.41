package com.honda.galc.service.datacollection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.datacollection.HeadlessDataMapping;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.DeviceFormatId;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.property.PropertyService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyService.class)
public class InstalledPartHelperTest {
	
	private String processPointId = "PP10332";
	private String associateNo = "vnc60037";
	private String memoryLoc = "EM01:00019";
	
	@Mock
	private static HeadLessPropertyBean headlessPropertyBean = PowerMockito.mock(HeadLessPropertyBean.class);
	
	@Mock
	private static Logger logger = PowerMockito.mock(Logger.class);
	
	@BeforeClass
	public static void classSetup() {}
	
	@Before
	public void methodSetup() {
		PowerMockito.mockStatic(PropertyService.class);
		
		// set default property values
		PowerMockito.when(headlessPropertyBean.isLotControl()).thenReturn(true);
		PowerMockito.when(headlessPropertyBean.isInlineRepair()).thenReturn(false);
		PowerMockito.when(headlessPropertyBean.isPdaRecipeDownload()).thenReturn(false);
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(false);	
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_TAG_NAME");
		
		PowerMockito.when(PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId)).thenReturn(headlessPropertyBean);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * test MAP_BY_PART_NAME (using name attribute)
	 * Annotation: @Tag(name="SN", alt="PART_SERIAL_NUMBER")
	 */
	@Test
	public void emitBuildResultTag_mapByPartName_name() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_PART_NAME");
		
		// create data to test with
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		InstalledPart installedPart = new InstalledPart("TESTPRODUCT000000","GSFL PARAMETER NUMBER");
		installedPart.setPartSerialNumber("SN0000001");
		installedParts.add(installedPart);

		String tags = "SN";
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		
		// invoke method that is being tested
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_PART_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		installedPartHelper.emitBuildResultTag(installedParts, tags, contextMap);
		
		// make assertions about expected result
		assertEquals(contextMap.size(), 1);
		assertTrue(contextMap.containsKey("GSFL PARAMETER NUMBER.SN"));
		assertEquals(contextMap.get("GSFL PARAMETER NUMBER.SN"), "SN0000001");
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * test MAP_BY_PART_NAME (using alt attribute)
	 * Annotation: @Tag(name="SN", alt="PART_SERIAL_NUMBER")
	 */
	@Test
	public void emitBuildResultTag_mapByPartName_alt() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_PART_NAME");
		
		// create data to test with
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		InstalledPart installedPart = new InstalledPart("TESTPRODUCT000000","GSFL PARAMETER NUMBER");
		installedPart.setPartSerialNumber("SN0000001");
		installedParts.add(installedPart);

		String tags = "PART_SERIAL_NUMBER";
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		
		// invoke method that is being tested
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_PART_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		installedPartHelper.emitBuildResultTag(installedParts, tags, contextMap);
		
		// make assertions about expected result
		assertEquals(contextMap.size(), 1);
		assertTrue(contextMap.containsKey("GSFL PARAMETER NUMBER.PART_SERIAL_NUMBER"));
		assertEquals(contextMap.get("GSFL PARAMETER NUMBER.PART_SERIAL_NUMBER"), "SN0000001");
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * test MAP_BY_TAG_NAME (using name attribute)
	 * Annotation: @Tag(name="SN", alt="PART_SERIAL_NUMBER")
	 */
	@Test
	public void emitBuildResultTag_mapByTagName_name() {
		
		// create data to test with
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		InstalledPart installedPart = new InstalledPart("TESTPRODUCT000000","GSFL PARAMETER NUMBER");
		installedPart.setPartSerialNumber("SN0000001");
		installedParts.add(installedPart);

		String tags = "SN";
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		
		// invoke method that is being tested
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_TAG_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		installedPartHelper.emitBuildResultTag(installedParts, tags, contextMap);
		
		// make assertions about expected result
		assertEquals(1, contextMap.size());
		assertTrue(contextMap.containsKey("SN"));
		assertEquals("SN0000001", contextMap.get("SN"));
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * test MAP_BY_TAG_NAME (using alt attribute)
	 * Annotation: @Tag(name="SN", alt="PART_SERIAL_NUMBER")
	 */
	@Test
	public void emitBuildResultTag_mapByTagName_alt() {
		
		// create data to test with
		List<InstalledPart> installedParts = new ArrayList<InstalledPart>();
		InstalledPart installedPart = new InstalledPart("TESTPRODUCT000000","GSFL PARAMETER NUMBER");
		installedPart.setPartSerialNumber("SN0000001");
		installedParts.add(installedPart);

		String tags = "PART_SERIAL_NUMBER";
		Map<Object, Object> contextMap = new HashMap<Object, Object>();
		
		// invoke method that is being tested
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_TAG_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		installedPartHelper.emitBuildResultTag(installedParts, tags, contextMap);
		
		// make assertions about expected result
		assertEquals(1, contextMap.size());
		assertTrue(contextMap.containsKey("PART_SERIAL_NUMBER"));
		assertEquals("SN0000001", contextMap.get("PART_SERIAL_NUMBER"));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_PART_NAME
	 * DEVICE_DRIVEN = FALSE
	 */
	@Test
	public void getBuildResults_mapByPartName_check_measurement() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_PART_NAME");

		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_PART_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(12.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_PART_NAME
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByPartName_check_measurement_deviceDriven() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_PART_NAME");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_PART_NAME, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_PART_NAME (not explicitly set)
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByPartName_check_measurement_deviceDriven_noLcRules() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_PART_NAME, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(null);
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Dec 18, 2018
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_TAG_NAME
	 * DEVICE_DRIVEN = FALSE
	 */
	@Test
	public void getBuildResults_mapByTagName_check_measurement() {
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_TAG_NAME);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(13.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_TAG_NAME
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByTagName_check_measurement_deviceDriven() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_TAG_NAME, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_TAG_NAME 
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByTagName_check_measurement_deviceDriven_noLcRules() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_TAG_NAME, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(null);
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE
	 * DEVICE_DRIVEN = FALSE
	 */
	@Test
	public void getBuildResults_mapByRule_check_measurement() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_RULE");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(14.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}

	/**
	 * @author Vivek Bettada, HAM
	 * @date Jan 31, 2020
	 * 
	 * Tests for case when installed part is null (zero measurements)
	 * MAP_BY_RULE --> dynamicDataMapping = true
	 * and
	 * serialNumberScamType == NONE
	 * and
	 * there are no measurements
	 * DEVICE_DRIVEN = FALSE
	 */
	@Test
	public void getBuildResults_mapByRule_no_measurement() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_RULE");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDeviceZeroMeas("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE, false);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRulesZeroMeas(parts));
		
		// make assertions about expected result
		assertEquals(0, installedParts.size());
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByRule_check_measurement_deviceDriven() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_RULE");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByRule_check_measurement_deviceDriven_noLcRules() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(null);
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE_INDEX
	 * DEVICE_DRIVEN = FALSE
	 */
	@Test
	public void getBuildResults_mapByRuleIndex_check_measurement() {
		
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_RULE_INDEX");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE_INDEX);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(15.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE_INDEX
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByRuleIndex_check_measurement_deviceDriven() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		PowerMockito.when(headlessPropertyBean.getPlcDataMapping()).thenReturn("MAP_BY_RULE_INDEX");
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE_INDEX, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(setupRules(parts));
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 01, 2019
	 * 
	 * Tests existence of installed part & measurement value
	 * MAP_BY_RULE_INDEX
	 * DEVICE_DRIVEN = TRUE
	 */
	@Test
	public void getBuildResults_mapByRuleIndex_check_measurement_deviceDriven_noLcRules() {
		
		PowerMockito.when(headlessPropertyBean.isDeviceDriven()).thenReturn(true);	
		
		// create data to test with
		Map<String, String> parts = new LinkedHashMap<String, String>();
		parts.put("GSFL PARAMETER NUMBER", "A0000");
		
		Device device = setupDevice("GAS_FILL_REQ", HeadlessDataMapping.MAP_BY_RULE_INDEX, true);
		InstalledPartHelper installedPartHelper = new InstalledPartHelper(device, headlessPropertyBean, processPointId, logger, associateNo);
		
		// invoke method that is being tested
		List<ProductBuildResult> installedParts = installedPartHelper.getBuildResults(null);
		
		// make assertions about expected result
		assertEquals(1, installedParts.size());
		assertEquals(installedParts.get(0).getPartName(), "GSFL PARAMETER NUMBER");
		assertEquals(new Double(11.0), new Double(installedParts.get(0).getMeasurements().get(0).getMeasurementValue()));
	}
	
	private List<LotControlRule> setupRules(Map<String, String> partMap) {
		
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		
		for (Map.Entry<String, String> entry : partMap.entrySet()) {
			LotControlRuleId ruleId = new LotControlRuleId();
			ruleId.setPartName(entry.getKey());
			ruleId.setProcessPointId(processPointId);
			ruleId.setProductSpecCode("JTNRAA6");
			ruleId.setModelYearCode("J");
			ruleId.setModelCode("TNR");
			ruleId.setModelTypeCode("AA6");
			ruleId.setModelOptionCode("*");
			ruleId.setIntColorCode("*");
			ruleId.setExtColorCode("*");

			PartName partName = new PartName();
			partName.setPartName(entry.getKey());
			partName.setPartConfirmCheck(0);
			partName.setProductTypeName("FRAME");

			MeasurementSpecId measSpecId = new MeasurementSpecId();
			measSpecId.setPartName(entry.getKey());
			measSpecId.setPartId(entry.getValue());

			MeasurementSpec measSpec = new MeasurementSpec();
			measSpec.setId(measSpecId);

			ArrayList<MeasurementSpec> measSpecs = new ArrayList<MeasurementSpec>();
			measSpecs.add(measSpec);

			PartSpecId partSpecId = new PartSpecId();
			partSpecId.setPartName(entry.getKey());
			partSpecId.setPartId(entry.getValue());

			PartSpec partSpec = new PartSpec();
			partSpec.setId(partSpecId);
			partSpec.setMeasurementCount(1);
			partSpec.setMeasurementSpecs(measSpecs);

			ArrayList<PartSpec> partSpecList = new ArrayList<PartSpec>();
			partSpecList.add(partSpec);

			LotControlRule rule = new LotControlRule();
			rule.setId(ruleId);
			rule.setSequenceNumber(1);
			rule.setInstructionCode("1");
			rule.setSerialNumberUniqueFlag(0);
			rule.setPartConfirmFlag(0);
			rule.setStrategy("com.honda.global.galc.client.lotcontrol.controller.StandardRuleProcessor");
			rule.setPartName(partName);
			rule.setParts(partSpecList);
			rules.add(rule);
		}
		return rules;
	}
	
	private List<LotControlRule> setupRulesZeroMeas(Map<String, String> partMap) {
		
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		
		for (Map.Entry<String, String> entry : partMap.entrySet()) {
			LotControlRuleId ruleId = new LotControlRuleId();
			ruleId.setPartName(entry.getKey());
			ruleId.setProcessPointId(processPointId);
			ruleId.setProductSpecCode("JTNRAA6");
			ruleId.setModelYearCode("J");
			ruleId.setModelCode("TNR");
			ruleId.setModelTypeCode("AA6");
			ruleId.setModelOptionCode("*");
			ruleId.setIntColorCode("*");
			ruleId.setExtColorCode("*");

			PartName partName = new PartName();
			partName.setPartName(entry.getKey());
			partName.setPartConfirmCheck(0);
			partName.setProductTypeName("FRAME");

			PartSpecId partSpecId = new PartSpecId();
			partSpecId.setPartName(entry.getKey());
			partSpecId.setPartId(entry.getValue());

			PartSpec partSpec = new PartSpec();
			partSpec.setId(partSpecId);

			ArrayList<PartSpec> partSpecList = new ArrayList<PartSpec>();
			partSpecList.add(partSpec);

			LotControlRule rule = new LotControlRule();
			rule.setId(ruleId);
			rule.setSequenceNumber(1);
			rule.setInstructionCode("1");
			rule.setSerialNumberUniqueFlag(0);
			rule.setPartConfirmFlag(0);
			rule.setStrategy("com.honda.global.galc.client.lotcontrol.controller.StandardRuleProcessor");
			rule.setPartName(partName);
			rule.setParts(partSpecList);
			rules.add(rule);
		}
		return rules;
	}
	
	private Device setupDevice(String clientId, HeadlessDataMapping mapping) {
		return setupDevice(clientId, mapping, false);
	}
		
	private Device setupDevice(String clientId, HeadlessDataMapping mapping, boolean isDeviceDriven) {
		ArrayList<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
		if (isDeviceDriven) {
			// if deviceDriven only MAP_BY_PART_NAME is supported
			deviceFormats.add(createDeviceFormat(1, clientId, "GSFL PARAMETER NUMBER.VALUE1", 4, "11"));
		} else {
			switch (mapping) {
				case MAP_BY_PART_NAME:
					deviceFormats.add(createDeviceFormat(1, clientId, "GSFL PARAMETER NUMBER.VALUE1", 4, "12"));
					break;
				case MAP_BY_TAG_NAME:
					deviceFormats.add(createDeviceFormat(1, clientId, "VALUE1", 4, "13"));
					break;
				case MAP_BY_RULE:
					deviceFormats.add(createDeviceFormat(1, clientId, "VALUE1", 4, "14"));
					break;
				case MAP_BY_RULE_INDEX:
					deviceFormats.add(createDeviceFormat(1, clientId, "1.VALUE1", 4, "15"));
					break;
				default:
					break;
			}
		}
		
		Device device = new Device();
		device.setClientId(clientId);
		device.setDeviceTypeId(0);
		device.setIoProcessPointId(processPointId);
		device.setReplyClientId("GAS_FILL_REPLY");
		device.setDeviceDataFormats(deviceFormats);
		return device;
	}

	private Device setupDeviceZeroMeas(String clientId, HeadlessDataMapping mapping, boolean isDeviceDriven) {
		ArrayList<DeviceFormat> deviceFormats = new ArrayList<DeviceFormat>();
		Device device = new Device();
		device.setClientId(clientId);
		device.setDeviceTypeId(0);
		device.setIoProcessPointId(processPointId);
		device.setReplyClientId("GAS_FILL_REPLY");
		device.setDeviceDataFormats(deviceFormats);
		return device;
	}

	private DeviceFormat createDeviceFormat(int seqNo, String clientId, String tag, int dataType, String measValue) {
		DeviceFormatId deviceFormatId = new DeviceFormatId();
		deviceFormatId.setClientId(clientId);
		deviceFormatId.setTag(tag);
		
		DeviceFormat deviceFormat = new DeviceFormat();
		deviceFormat.setId(deviceFormatId);
		deviceFormat.setSequenceNumber(seqNo);
		deviceFormat.setTagType(4);
		deviceFormat.setTagValue(memoryLoc);
		deviceFormat.setDataType(dataType);
		deviceFormat.setValue(measValue);
	
		return deviceFormat;
	}
}
