package com.honda.galc.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.util.SnConverter.ConversionType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SnConverterTest</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Apr 8, 2014
 */
public class SnConverterTest {

	private static String DCH_HUM_FORMAT = "MYYMMDDSSSS";
	private static String DCB_HUM_FORMAT = "MYMMDDSSS";

	private static String DCH_CSS_FORMAT = "MMMPLAYMDDLSSSSD";
	private static String DCB_CSS_FORMAT = "MMMDIYYMMDDSSSSL";

	private static String DC_FORMAT = "MMMPPLYMDDLDSSSSG";
	private static String MC_FORMAT = "MMMPPLYMDDLSSSSG";

	private static HashMap<String, String> mappedModels;

	public static void main(String[] args) {

		loadPredefinedConfigs();
		toJson();
		prepareModelMappings();
		toJson();

		SnConverterTest test = new SnConverterTest();
		test.testSampleHead();
		test.testSampleBlock();

		test.testHumDchConversion();
		test.testHumDcbConversion();

		test.testCssDchConversion();
		test.testCssDcbConversion();

		loadConfigsFromJson();
		prepareModelMappings();

		test.testHumMchConversion();
		test.testHumMcbConversion();

		test.testCssMchConversion();
		test.testCssMcbConversion();
	}

	@BeforeClass
	public static void prepare() {
		loadPredefinedConfigs();
		toJson();
		prepareModelMappings();
		toJson();

		loadConfigsFromJson();
		prepareModelMappings();
	}

	public static void loadPredefinedConfigs() {
		SnConverter.getConfigs().clear();
		SnConverter.getConfigs().putAll(SnConverter.getPredefinedConfigs());
	}

	public static void prepareModelMappings() {

		mappedModels = new HashMap<String, String>();
		mappedModels.put("K", "R1B");
		mappedModels.put("E", "R1B");
		mappedModels.put("R40", "R2H");
		mappedModels.put("R60", "R2B");

		for (String configId : SnConverter.getConfigs().keySet()) {
			SnConverterConfig[] configs = SnConverter.getConfigs().get(configId);
			for (int i = 0; i < configs.length; i++) {
				SnConverterConfig c = configs[i];
				if (ConversionType.MODEL_ATTR.equals(c.getConversionType())) {
					if (configId.contains("CSS")) {
						configs[i] = createConfig("MODEL", 0, 3, mappedModels);
					} else {
						configs[i] = createConfig("MODEL", 0, 1, mappedModels);
					}
				}
			}
		}
	}

	public static void loadConfigsFromJson() {
		System.out.println("************ From Json *****************************************");
		SnConverter.getConfigs().clear();

		String dchCssDch = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:3,value:MODEL_CSS},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:6,length:1},{tokenName:MONTH,conversionType:SUB_STR,startIx:7,length:1},{tokenName:DAY,conversionType:SUB_STR,startIx:8,length:2},{tokenName:LINE,conversionType:SUB_STR,startIx:10,length:1},{tokenName:DIE,conversionType:SUB_STR,startIx:15,length:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:11,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dcbCssDcb = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:3,value:MODEL_CSS},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:6,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:7,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:9,length:2},{tokenName:LINE,conversionType:SUB_STR,startIx:15,length:1},{tokenName:DIE,conversionType:SUB_STR,startIx:4,length:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:11,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dchHumDch = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:1,value:MODEL_HUM},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:2,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:3,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:5,length:2},{tokenName:LINE,conversionType:FIXED_VALUE,value:1},{tokenName:DIE,conversionType:FIXED_VALUE,value:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:7,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dcbHumDcb = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:1,value:MODEL_HUM},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:1,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:2,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:4,length:2},{tokenName:LINE,conversionType:FIXED_VALUE,value:1},{tokenName:DIE,conversionType:FIXED_VALUE,value:1},{tokenName:LEFT_PAD,conversionType:FIXED_VALUE,value:0},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:6,length:3},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";

		String dchCssMch = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:3,value:MODEL_CSS},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:6,length:1},{tokenName:MONTH,conversionType:SUB_STR,startIx:7,length:1},{tokenName:DAY,conversionType:SUB_STR,startIx:8,length:2},{tokenName:LINE,conversionType:SUB_STR,startIx:10,length:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:11,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dcbCssMcb = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:3,value:MODEL_CSS},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:6,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:7,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:9,length:2},{tokenName:LINE,conversionType:SUB_STR,startIx:15,length:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:11,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dchHumMch = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:1,value:MODEL_HUM},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:2,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:3,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:5,length:2},{tokenName:LINE,conversionType:FIXED_VALUE,value:1},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:7,length:4},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";
		String dcbHumMcb = "[{tokenName:MODEL,conversionType:MODEL_ATTR,startIx:0,length:1,value:MODEL_HUM},{tokenName:PART_LEVEL,conversionType:FIXED_VALUE,value:1},{tokenName:PLANT,conversionType:FIXED_VALUE,value:FS},{tokenName:YEAR,conversionType:SUB_STR,startIx:1,length:1},{tokenName:MONTH,conversionType:TO_HEX,startIx:2,length:2},{tokenName:DAY,conversionType:SUB_STR,startIx:4,length:2},{tokenName:LINE,conversionType:FIXED_VALUE,value:1},{tokenName:LEFT_PAD,conversionType:FIXED_VALUE,value:0},{tokenName:SEQUENCE,conversionType:SUB_STR,startIx:6,length:3},{tokenName:CHECK_DIGIT,conversionType:CHECK_DIGIT}]";

		Map<String, String> configs = new LinkedHashMap<String, String>();
		configs.put("DCH_CSS.DCH", dchCssDch);
		configs.put("DCB_CSS.DCB", dcbCssDcb);
		configs.put("DCH_HUM.DCH", dchHumDch);
		configs.put("DCB_HUM.DCB", dcbHumDcb);

		configs.put("DCH_CSS.MCH", dchCssMch);
		configs.put("DCB_CSS.MCB", dcbCssMcb);
		configs.put("DCH_HUM.MCH", dchHumMch);
		configs.put("DCB_HUM.MCB", dcbHumMcb);

		Gson gson = new Gson();
		for (String configId : configs.keySet()) {
			String json = configs.get(configId);
			SnConverterConfig[] config = gson.fromJson(json, SnConverterConfig[].class);
			SnConverter.getConfigs().put(configId, config);
			System.out.println("Loaded:" + configId + ":" + json);
		}
	}

	public static void toJson() {
		Gson gson = new Gson();
		for (String configId : SnConverter.getConfigs().keySet()) {
			String json = gson.toJson(SnConverter.getConfig(configId, null));
			json = json.replace("\"", "");
			System.out.println("ToJson:" + configId + ":" + json);
		}
	}

	@Test
	public void testSampleHead() {
		System.out.println("*************** HUM DCH ************************");
		ProductNumberDef sourceDef = ProductNumberDef.DCH_HUM;
		ProductNumberDef targetDef = ProductNumberDef.DCH;
		System.out.println(String.format("            %s   %s       %s", DCH_HUM_FORMAT, DC_FORMAT, DC_FORMAT));
		testConversion("K1410200646", "DCH_HUM.DCH", "R1B1FS4A20110646", sourceDef, targetDef);
	}

	@Test
	public void testSampleBlock() {
		System.out.println("*************** HUM DCB ************************");
		ProductNumberDef sourceDef = ProductNumberDef.DCB_HUM;
		ProductNumberDef targetDef = ProductNumberDef.DCB;
		System.out.println(String.format("            %s   %s       %s", DCB_HUM_FORMAT, DC_FORMAT, DC_FORMAT));
		testConversion("E40102503", "DCB_HUM.DCB", "R1B1FS4102110503", sourceDef, targetDef);
	}

	@Test
	public void testHumDchConversion() {
		System.out.println("*************** HUM DCH ************************");
		ProductNumberDef sourceDef = ProductNumberDef.DCH_HUM;
		ProductNumberDef targetDef = ProductNumberDef.DCH;
		System.out.println(String.format("            %s   %s       %s", DCH_HUM_FORMAT, DC_FORMAT, DC_FORMAT));
		testConversion("K1409290139", "DCH_HUM.DCH", "R1B1FS4929110139", sourceDef, targetDef);
		testConversion("K1409290139", "DCH_HUM.DCH", "R1B1FS4929110139", sourceDef, targetDef);
		testConversion("K1410290139", "DCH_HUM.DCH", "R1B1FS4A29110139", sourceDef, targetDef);
		testConversion("K1411290139", "DCH_HUM.DCH", "R1B1FS4B29110139", sourceDef, targetDef);
		testConversion("K1412290139", "DCH_HUM.DCH", "R1B1FS4C29110139", sourceDef, targetDef);
	}

	@Test
	public void testHumDcbConversion() {
		System.out.println("*************** HUM DCB ************************");
		System.out.println(String.format("            %s     %s       %s", DCB_HUM_FORMAT, DC_FORMAT, DC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCB_HUM;
		ProductNumberDef targetDef = ProductNumberDef.DCB;
		testConversion("E30427567", "DCB_HUM.DCB", "R1B1FS3427110567", sourceDef, targetDef);
		testConversion("E30927567", "DCB_HUM.DCB", "R1B1FS3927110567", sourceDef, targetDef);
		testConversion("E31027567", "DCB_HUM.DCB", "R1B1FS3A27110567", sourceDef, targetDef);
		testConversion("E31127567", "DCB_HUM.DCB", "R1B1FS3B27110567", sourceDef, targetDef);
		testConversion("E31227567", "DCB_HUM.DCB", "R1B1FS3C27110567", sourceDef, targetDef);
	}

	@Test
	public void testCssDchConversion() {
		System.out.println("*************** CSS DCH ************************");
		System.out.println(String.format("            %s  %s       %s", DCH_CSS_FORMAT, DC_FORMAT, DC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCH_CSS;
		ProductNumberDef targetDef = ProductNumberDef.DCH;
		testConversion("R40HFJ1423L0123D", "DCH_CSS.DCH", "R2H1FS1423LD0123", sourceDef, targetDef);
		testConversion("R40HFJ1423L0123D", "DCH_CSS.DCH", "R2H1FS1423LD0123", sourceDef, targetDef);
		testConversion("R40HFJ1923L0123D", "DCH_CSS.DCH", "R2H1FS1923LD0123", sourceDef, targetDef);
		testConversion("R40HFJ1A23L0123D", "DCH_CSS.DCH", "R2H1FS1A23LD0123", sourceDef, targetDef);
		testConversion("R40HFJ1B23L0123D", "DCH_CSS.DCH", "R2H1FS1B23LD0123", sourceDef, targetDef);
		testConversion("R40HFJ1C23L0123D", "DCH_CSS.DCH", "R2H1FS1C23LD0123", sourceDef, targetDef);
	}

	@Test
	public void testCssDcbConversion() {
		System.out.println("*************** CSS DCB ************************");
		System.out.println(String.format("            %s  %s       %s", DCB_CSS_FORMAT, DC_FORMAT, DC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCB_CSS;
		ProductNumberDef targetDef = ProductNumberDef.DCB;
		testConversion("R600305092901231", "DCB_CSS.DCB", "R2B1FS5929130123", sourceDef, targetDef);
		testConversion("R600314102901231", "DCB_CSS.DCB", "R2B1FS4A29130123", sourceDef, targetDef);
		testConversion("R600314112901231", "DCB_CSS.DCB", "R2B1FS4B29130123", sourceDef, targetDef);
		testConversion("R600314122901231", "DCB_CSS.DCB", "R2B1FS4C29130123", sourceDef, targetDef);
	}

	@Test
	public void testHumMchConversion() {
		System.out.println("*************** HUM MCH ************************");
		System.out.println(String.format("            %s   %s       %s", DCH_HUM_FORMAT, MC_FORMAT, MC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCH_HUM;
		ProductNumberDef targetDef = ProductNumberDef.MCH;
		testConversion("K1409290139", "DCH_HUM.MCH", "R1B1FS492910139", sourceDef, targetDef);
		testConversion("K1409290139", "DCH_HUM.MCH", "R1B1FS492910139", sourceDef, targetDef);
		testConversion("K1410290139", "DCH_HUM.MCH", "R1B1FS4A2910139", sourceDef, targetDef);
		testConversion("K1411290139", "DCH_HUM.MCH", "R1B1FS4B2910139", sourceDef, targetDef);
		testConversion("K1412290139", "DCH_HUM.MCH", "R1B1FS4C2910139", sourceDef, targetDef);
	}

	@Test
	public void testHumMcbConversion() {
		System.out.println("*************** HUM MCB ************************");
		System.out.println(String.format("            %s     %s       %s", DCB_HUM_FORMAT, MC_FORMAT, MC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCB_HUM;
		ProductNumberDef targetDef = ProductNumberDef.MCB;
		testConversion("E30427567", "DCB_HUM.MCB", "R1B1FS342710567", sourceDef, targetDef);
		testConversion("E30927567", "DCB_HUM.MCB", "R1B1FS392710567", sourceDef, targetDef);
		testConversion("E31027567", "DCB_HUM.MCB", "R1B1FS3A2710567", sourceDef, targetDef);
		testConversion("E31127567", "DCB_HUM.MCB", "R1B1FS3B2710567", sourceDef, targetDef);
		testConversion("E31227567", "DCB_HUM.MCB", "R1B1FS3C2710567", sourceDef, targetDef);
	}

	@Test
	public void testCssMchConversion() {
		System.out.println("*************** CSS MCH ************************");
		System.out.println(String.format("            %s  %s       %s", DCH_CSS_FORMAT, MC_FORMAT, MC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCH_CSS;
		ProductNumberDef targetDef = ProductNumberDef.MCH;
		testConversion("R40HFJ1423L0123D", "DCH_CSS.MCH", "R2H1FS1423L0123", sourceDef, targetDef);
		testConversion("R40HFJ1423L0123D", "DCH_CSS.MCH", "R2H1FS1423L0123", sourceDef, targetDef);
		testConversion("R40HFJ1923L0123D", "DCH_CSS.MCH", "R2H1FS1923L0123", sourceDef, targetDef);
		testConversion("R40HFJ1A23L0123D", "DCH_CSS.MCH", "R2H1FS1A23L0123", sourceDef, targetDef);
		testConversion("R40HFJ1B23L0123D", "DCH_CSS.MCH", "R2H1FS1B23L0123", sourceDef, targetDef);
		testConversion("R40HFJ1C23L0123D", "DCH_CSS.MCH", "R2H1FS1C23L0123", sourceDef, targetDef);
	}

	@Test
	public void testCssMcbConversion() {
		System.out.println("*************** CSS MCB ************************");
		System.out.println(String.format("            %s  %s       %s", DCB_CSS_FORMAT, MC_FORMAT, MC_FORMAT));
		ProductNumberDef sourceDef = ProductNumberDef.DCB_CSS;
		ProductNumberDef targetDef = ProductNumberDef.MCB;
		testConversion("R600305092901231", "DCB_CSS.MCB", "R2B1FS592910123", sourceDef, targetDef);
		testConversion("R600314102901231", "DCB_CSS.MCB", "R2B1FS4A2910123", sourceDef, targetDef);
		testConversion("R600314112901231", "DCB_CSS.MCB", "R2B1FS4B2910123", sourceDef, targetDef);
		testConversion("R600314122901231", "DCB_CSS.MCB", "R2B1FS4C2910123", sourceDef, targetDef);
	}

	// === test utility methods === //
	public void testConversion(String source, String configId, String expected, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		System.out.print(configId);
		String target = SnConverter.convert(source, configId);
		boolean equals = target.substring(0, target.length() - 1).equals(expected);
		boolean valid = targetDef.isNumberValid(target) && ProductDigitCheckUtil.isCheckDigitValid(target);
		String str = String.format(":%s->%s, exp: %s, e:%s, v:%s.", StringUtil.padRight(source, 12, ' ', false), target, expected, equals, valid);
		System.out.println(str);

		String sourceRevision = "1";
		if (targetDef.getTokens().containsKey(TokenType.REVISION.name())) {
			String targetRevision = targetDef.getRevision(target);
			Assert.assertEquals(sourceRevision, targetRevision);
		}
		Assert.assertEquals(expected, target.substring(0, target.length() - 1));
		assertModel(source, target, sourceDef, targetDef);
		assertPartLevel(source, target, sourceDef, targetDef);
		assertRevision(source, target, sourceDef, targetDef);
		assertPlant(source, target, sourceDef, targetDef);
		assertYear(source, target, sourceDef, targetDef);
		assertMonth(source, target, sourceDef, targetDef);
		assertDay(source, target, sourceDef, targetDef);
		assertLine(source, target, sourceDef, targetDef);
		assertDie(source, target, sourceDef, targetDef);
		assertSequence(source, target, sourceDef, targetDef);
		assertCheckDigit(source, target, sourceDef, targetDef);
		assertLength(source, target, sourceDef, targetDef);
		Assert.assertEquals(true, valid);
	}

	// === util api === //
	protected static void assertModel(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = sourceDef.getModel(source);
		String targetToken = targetDef.getModel(target);
		Assert.assertEquals(mappedModels.get(sourceToken), targetToken);
	}

	protected static void assertPartLevel(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = "1";
		if (targetDef.getTokens().containsKey(TokenType.PART_LEVEL.name())) {
			String targetToken = targetDef.getPartLevel(target);
			Assert.assertEquals(sourceToken, targetToken);
		}
	}

	protected static void assertRevision(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = "1";
		if (targetDef.getTokens().containsKey(TokenType.REVISION.name())) {
			String targetToken = targetDef.getRevision(target);
			Assert.assertEquals(sourceToken, targetToken);
		}
	}

	protected static void assertPlant(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = "FS";
		String targetToken = targetDef.getPlant(target);
		Assert.assertEquals(sourceToken, targetToken);
	}

	protected static void assertYear(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = sourceDef.getYear(source);
		sourceToken = sourceToken.substring(sourceToken.length() - 1, sourceToken.length());
		String targetToken = targetDef.getYear(target);
		Assert.assertEquals(sourceToken, targetToken);
	}

	protected static void assertMonth(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = sourceDef.getMonth(source);
		String targetToken = targetDef.getMonth(target);
		if (sourceToken.length() == 2) {
			Assert.assertEquals(Integer.toHexString(Integer.valueOf(sourceToken)).toUpperCase(), targetToken);
		} else {
			Assert.assertEquals(sourceToken, targetToken);
		}
	}

	protected static void assertDay(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = sourceDef.getDay(source);
		String targetToken = targetDef.getDay(target);
		Assert.assertEquals(sourceToken, targetToken);
	}

	protected static void assertLine(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = "1";
		String targetToken = targetDef.getLine(target);
		if (sourceDef.getTokens().containsKey(TokenType.LINE.name())) {
			sourceToken = sourceDef.getLine(source);
		}
		Assert.assertEquals(sourceToken, targetToken);
	}

	protected static void assertDie(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		if (targetDef.getTokens().containsKey(TokenType.DIE.name())) {
			String sourceToken = "1";
			String targetToken = targetDef.getDie(target);
			if (sourceDef.getTokens().containsKey(TokenType.DIE.name())) {
				sourceToken = sourceDef.getDie(source);
				sourceToken = sourceToken.substring(sourceToken.length() - 1, sourceToken.length());
			}
			Assert.assertEquals(sourceToken, targetToken);
		}
	}

	protected static void assertSequence(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		String sourceToken = sourceDef.getSequence(source);
		sourceToken = StringUtil.padLeft(sourceToken, 4, '0');
		String targetToken = targetDef.getSequence(target);
		targetToken = StringUtil.padLeft(targetToken, 4, '0');
		Assert.assertEquals(sourceToken, targetToken);
	}

	protected static void assertCheckDigit(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		boolean valid = ProductDigitCheckUtil.isCheckDigitValid(target);
		Assert.assertEquals(true, valid);
	}

	protected static void assertLength(String source, String target, ProductNumberDef sourceDef, ProductNumberDef targetDef) {
		boolean valid = targetDef.isNumberValid(target);
		Assert.assertEquals(true, valid);
	}

	protected static SnConverterConfig createConfig(String tokenName, Integer startIx, Integer length, HashMap<String, String> mappedValues) {
		SnConverterConfig config = new SnConverterConfig();
		config.setTokenName(tokenName);
		config.setConversionType(ConversionType.MAPPED_VALUE);
		config.setStartIx(startIx);
		config.setLength(length);
		config.setMappedValues(mappedValues);
		return config;
	}
}
