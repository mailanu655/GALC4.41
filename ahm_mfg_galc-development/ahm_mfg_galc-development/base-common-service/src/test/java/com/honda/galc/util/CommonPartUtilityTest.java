package com.honda.galc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.ExtRequiredPartSpec;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonPartUtility.PartMaskFormat;

/**
 * @author Subu Kathiresan
 * @date Mar 28, 2016
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class,VinPartDao.class,ServiceFactory.class})
public class CommonPartUtilityTest {

	@Mock
	private static SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);
	
	@Mock
	private static VinPartDao vinPartDaoMock = PowerMockito.mock(VinPartDao.class);
	
	private PartSpec partSpec = null;
	private ExtRequiredPartSpec extPartSpec = null;
	
	@BeforeClass
	public static void classSetup() {}
	
	@Before
	public void methodSetup() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(SystemPropertyBean.class)).thenReturn(systemPropertyBeanMock);
		PowerMockito.when(systemPropertyBeanMock.getExtColorExceptionChars()).thenReturn("X");
		PowerMockito.when(PropertyService.getExtColorExceptionChars()).thenReturn("X");
		
		PowerMockito.when(vinPartDaoMock.getPartNumbersByProductIdAndSystemName("vin1","APS_SOFT")).thenReturn(Arrays.asList(new String[]{"1234567","45641"}));
		
		partSpec = new PartSpec();
		extPartSpec = new ExtRequiredPartSpec();
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests comma delimited format
	 * {INDEX=2}{DELIMITER=,}
	 */
	@Test
	public void parsePartSerialNumber_commaDelimitedIndexFirst() {
		partSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		partSpec.setParserInformation("{INDEX=2}{DELIMITER=,}");
		String partSn = "Hello,Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests comma delimited format
	 * {DELIMITER=,}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_commaDelimitedIndexSecond() {
		partSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		partSpec.setParserInformation("{DELIMITER=,}{INDEX=2}");
		String partSn = "Hello,Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests colon delimited format
	 * {INDEX=2}{DELIMITER=:}
	 */
	@Test
	public void parsePartSerialNumber_colonDelimitedIndexFirst() {
		partSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		partSpec.setParserInformation("{INDEX=2}{DELIMITER=:}");
		String partSn = "Hello:Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests colon delimited format
	 * {DELIMITER=:}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_colonDelimitedIndexSecond() {
		partSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		partSpec.setParserInformation("{DELIMITER=:}{INDEX=2}");
		String partSn = "Hello:Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests colon delimited with nothing to parse
	 * {DELIMITER=:}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_colonDelimitedNoDelimiter() {
		partSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		partSpec.setParserInformation("{DELIMITER=:}{INDEX=2}");
		String partSn = "Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests fixed length
	 * {OFFSET=5}{LENGTH=12}
	 */
	@Test
	public void parsePartSerialNumber_fixedLengthOffSetFirst() {
		partSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		partSpec.setParserInformation("{OFFSET=5}{LENGTH=12}");
		String partSn = "HelloIamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests fixed length
	 * {LENGTH=12}{OFFSET=5}
	 */
	@Test
	public void parsePartSerialNumber_fixedLengthOffsetSecond() {
		partSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		partSpec.setParserInformation("{LENGTH=12}{OFFSET=5}");
		String partSn = "HelloIamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests fixed length with spaces
	 * {LENGTH=12}{OFFSET=6}
	 */
	@Test
	public void parsePartSerialNumber_fixedLengthWithSpaces() {
		partSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		partSpec.setParserInformation("{LENGTH=12}{OFFSET=6}");
		String partSn = "Hello Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests fixed length with nothing to parse
	 * {LENGTH=12}{OFFSET=0}
	 */
	@Test
	public void parsePartSerialNumber_fixedLengthNothingToParse() {
		partSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		partSpec.setParserInformation("{LENGTH=12}{OFFSET=0}");
		String partSn = "Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests tag value
	 * <ROOT_TAG>
	 *   <SOME_TAG>hello</SOME_TAG>
	 *   <PART_NAME>Iamtheserial</PART_NAME>
	 * </ROOT_TAG>
	 */
	@Test
	public void parsePartSerialNumber_tagValueSimple() {
		partSpec.setParseStrategy(ParseStrategyType.TAG_VALUE.toString());
		partSpec.setParserInformation("PART_NAME");
		String partSn = "<ROOT_TAG><SOME_TAG>hello</SOME_TAG><PART_NAME>Iamtheserial</PART_NAME></ROOT_TAG>";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Mar 28, 2016
	 * 
	 * Tests tag value
	 * <ROOT_TAG>
	 *   <SOME_TAG>
	 *   	<PART_NAME>Iamtheserial</PART_NAME>
	 *   </SOME_TAG>
	 * </ROOT_TAG>
	 */
	@Test
	public void parsePartSerialNumber_tagValueNested() {
		partSpec.setParseStrategy(ParseStrategyType.TAG_VALUE.toString());
		partSpec.setParserInformation("PART_NAME");
		String partSn = "<ROOT_TAG><SOME_TAG><PART_NAME>Iamtheserial</PART_NAME></SOME_TAG></ROOT_TAG>";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(partSpec, partSn));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests comma delimited format
	 * {INDEX=2}{DELIMITER=,}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecCommaDelimitedIndexFirst() {
		extPartSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		extPartSpec.setParserInformation("{INDEX=2}{DELIMITER=,}");
		String partSn = "Hello,Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests comma delimited format
	 * {DELIMITER=,}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecCommaDelimitedIndexSecond() {
		extPartSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		extPartSpec.setParserInformation("{DELIMITER=,}{INDEX=2}");
		String partSn = "Hello,Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests colon delimited format
	 * {INDEX=2}{DELIMITER=:}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecColonDelimitedIndexFirst() {
		extPartSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		extPartSpec.setParserInformation("{INDEX=2}{DELIMITER=:}");
		String partSn = "Hello:Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests colon delimited format
	 * {DELIMITER=:}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecColonDelimitedIndexSecond() {
		extPartSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		extPartSpec.setParserInformation("{DELIMITER=:}{INDEX=2}");
		String partSn = "Hello:Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests colon delimited with nothing to parse
	 * {DELIMITER=:}{INDEX=2}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecColonDelimitedNoDelimiter() {
		extPartSpec.setParseStrategy(ParseStrategyType.DELIMITED.toString());
		extPartSpec.setParserInformation("{DELIMITER=:}{INDEX=2}");
		String partSn = "Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests fixed length
	 * {OFFSET=5}{LENGTH=12}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecFixedLengthOffSetFirst() {
		extPartSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		extPartSpec.setParserInformation("{OFFSET=5}{LENGTH=12}");
		String partSn = "HelloIamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests fixed length
	 * {LENGTH=12}{OFFSET=5}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecFixedLengthOffsetSecond() {
		extPartSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		extPartSpec.setParserInformation("{LENGTH=12}{OFFSET=5}");
		String partSn = "HelloIamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests fixed length with spaces
	 * {LENGTH=12}{OFFSET=6}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecFixedLengthWithSpaces() {
		extPartSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		extPartSpec.setParserInformation("{LENGTH=12}{OFFSET=6}");
		String partSn = "Hello Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests fixed length with nothing to parse
	 * {LENGTH=12}{OFFSET=0}
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecFixedLengthNothingToParse() {
		extPartSpec.setParseStrategy(ParseStrategyType.FIXED_LENGTH.toString());
		extPartSpec.setParserInformation("{LENGTH=12}{OFFSET=0}");
		String partSn = "Iamtheserial";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests tag value
	 * <ROOT_TAG>
	 *   <SOME_TAG>hello</SOME_TAG>
	 *   <PART_NAME>Iamtheserial</PART_NAME>
	 * </ROOT_TAG>
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecTagValueSimple() {
		extPartSpec.setParseStrategy(ParseStrategyType.TAG_VALUE.toString());
		extPartSpec.setParserInformation("PART_NAME");
		String partSn = "<ROOT_TAG><SOME_TAG>hello</SOME_TAG><PART_NAME>Iamtheserial</PART_NAME></ROOT_TAG>";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Jun 18, 2018
	 * 
	 * Tests tag value
	 * <ROOT_TAG>
	 *   <SOME_TAG>
	 *   	<PART_NAME>Iamtheserial</PART_NAME>
	 *   </SOME_TAG>
	 * </ROOT_TAG>
	 */
	@Test
	public void parsePartSerialNumber_extReqPartSpecTagValueNested() {
		extPartSpec.setParseStrategy(ParseStrategyType.TAG_VALUE.toString());
		extPartSpec.setParserInformation("PART_NAME");
		String partSn = "<ROOT_TAG><SOME_TAG><PART_NAME>Iamtheserial</PART_NAME></SOME_TAG></ROOT_TAG>";
		
		assertEquals("Iamtheserial", CommonPartUtility.parsePartSerialNumber(extPartSpec, partSn));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * TrimExtColorCode Test mask verification
	 */
	@Test
	public void verification_trimExtColorCode() {
		
		Frame product = new Frame();
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
		
		partSpec.setPartSerialNumberMask("<<TRIMEXTCOLOR>>");
		assertTrue(CommonPartUtility.verification("NH797M",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("NH797M",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * ExtColorCode Test mask verification
	 */
	@Test
	public void verification_extColorCode() {
	
		Frame product = new Frame();
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
		
		partSpec.setPartSerialNumberMask("<<EXTCOLOR>>");
		assertTrue(CommonPartUtility.verification("NH797M    ",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("NH797M    ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * IntColorCode Test mask verification
	 */
	@Test
	public void verification_intColorCode() {
		
		Frame product = new Frame();
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
				
		partSpec.setPartSerialNumberMask("<<INTCOLOR>>");
		assertTrue(CommonPartUtility.verification("B ",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("B ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * TrimIntColorCode Test mask verification
	 */
	@Test
	public void verification_trimIntColorCode() {
		
		Frame product = new Frame();
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
		
		partSpec.setPartSerialNumberMask("<<TRIMINTCOLOR>>");
		assertTrue(CommonPartUtility.verification("B",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("B",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * IntColorCodeOptions Test mask verification
	 */
	@Test
	public void verification_intColorCodeOptions() {
		
		Frame product = new Frame();
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
				
		partSpec.setPartSerialNumberMask("<<MODELYEAR>><<INTCOLORCODE|A;B>>");
		assertTrue(CommonPartUtility.verification("GB",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("GB",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
		
		partSpec.setPartSerialNumberMask("L1<<INTCOLORCODE|A;Z;D;R>><<*>>");
		assertFalse(CommonPartUtility.verification("L1A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		assertTrue(CommonPartUtility.verification("L1BX",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		
		partSpec.setPartSerialNumberMask("L1<<INTCOLORCODE|A;Z;D;R|B;X;E;F|C;U>><<*>>");
		assertTrue(CommonPartUtility.verification("L1BX",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		
		product.setProductSpecCode("GT1WAA500 NH797M    A ");
		partSpec.setPartSerialNumberMask("<<INTCOLORCODE|A;B;PB>>");
		assertTrue(CommonPartUtility.verification("A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		product.setProductSpecCode("GT1WAA500 NH797M    F ");
		assertFalse(CommonPartUtility.verification("A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		
		product.setProductSpecCode("GT1WAA500 NH797M    A ");
		partSpec.setPartSerialNumberMask("M3<<INTCOLORCODE|A;B;PB|F;C>><<?>>");
		assertTrue(CommonPartUtility.verification("M3PBX",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		product.setProductSpecCode("GT1WAA500 NH797M    F ");
		assertTrue(CommonPartUtility.verification("M3CD",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
		
		product.setProductSpecCode("GT1WAA500 NH797M    A ");
		partSpec.setPartSerialNumberMask("<<INTCOLORCODE|AC;B>>");
		assertTrue(CommonPartUtility.verification("A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
		assertTrue(CommonPartUtility.verification("AC",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
		
		
	}
	
	@Test
	public void verification_SumsPart() {
		PowerMockito.mockStatic(ServiceFactory.class);
		when(ServiceFactory.getDao(VinPartDao.class)).thenReturn(vinPartDaoMock);	
		Frame product = new Frame();
		product.setProductId("vin1");
		product.setProductSpecCode("GT1WAA500 NH797M    A ");
		partSpec.setPartSerialNumberMask(" <<SUMS_PART;APS_SOFT>>");
		assertTrue(CommonPartUtility.verification("1234567",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString(), product));
		product.setProductSpecCode("GT1WAA500 NH797M    B ");
		assertTrue(CommonPartUtility.verification("45641",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString(), product));
	}
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * WildCards (%;n) Test mask verification
	 */
	@Test
	public void verification_wildCardTest() {

		partSpec.setPartSerialNumberMask("PL<<%;5>><<*>>");
		assertTrue(CommonPartUtility.verification("PLNH797AAA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PLNH797AAA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	
		partSpec.setPartSerialNumberMask("PL%%%%%*");
		assertTrue(CommonPartUtility.verification("PLNH797AAA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PLNH797AAA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * AsteristWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_asteristWithEscapeChars() {

		partSpec.setPartSerialNumberMask("PL\\*");
		assertTrue(CommonPartUtility.verification("PL*",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL*",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL*");
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("PL",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * QuestionMarkWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_questionMarkWithEscapeChars() {
		
		partSpec.setPartSerialNumberMask("PL\\?");
		assertTrue(CommonPartUtility.verification("PL?",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL?",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL?");
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * PoundSymbolWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_poundSymbolWithEscapeChars() {
		
		partSpec.setPartSerialNumberMask("PL\\#");
		assertTrue(CommonPartUtility.verification("PL#",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL#",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL#");
		assertTrue(CommonPartUtility.verification("PL5",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL5",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification("PL",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertFalse(CommonPartUtility.verification("PL",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * CaratSymbolWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_caratSymbolWithEscapeChars() {
		
		partSpec.setPartSerialNumberMask("PL\\^");
		assertTrue(CommonPartUtility.verification("PL^",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL^",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL^");
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL7",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("PL7",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * MultipleWildCardsWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_multipleWildCardsWithEscapeChars() {
		
		partSpec.setPartSerialNumberMask("PL\\#?");
		assertTrue(CommonPartUtility.verification("PL#A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL#A",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL\\#?\\?*");
		assertTrue(CommonPartUtility.verification("PL#A?67",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL#A?67",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("PL\\*?\\?*");
		assertTrue(CommonPartUtility.verification("PL*A?67",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL*A?",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification("PL*A",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification(" ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * CommaWithEscapeChars Test mask verification
	 */
	@Test
	public void verification_commaWithEscapeChars() {

		partSpec.setPartSerialNumberMask("PL\\,A*,ASF*");
		assertTrue(CommonPartUtility.verification("PL,A123",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("ASFA",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("PL,A123",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("ASF",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * Escape_BackSlash Test mask verification
	 */
	@Test
	public void verification_escapeBackSlash() {

		partSpec.setPartSerialNumberMask("A\\\\PL");
		assertTrue(CommonPartUtility.verification("A\\PL",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		assertTrue(CommonPartUtility.verification("A\\PL",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification(" ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * AsteriskWildCard Test mask verification
	 */
	@Test
	public void verification_asteriskWildCard() {
				
		partSpec.setPartSerialNumberMask("PL*");
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL.toString()));
		assertFalse(CommonPartUtility.verification(" ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL.toString()));
		
		partSpec.setPartSerialNumberMask("PL<<*>>");
		assertTrue(CommonPartUtility.verification("PLA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * WildCardAsterisk_notAcceptEmptyString Test mask verification
	 */
	@Test
	public void verification_wildCardAsteriskNotAcceptEmptyString() {
				
		partSpec.setPartSerialNumberMask("*");
		assertFalse(CommonPartUtility.verification("",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification("",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * Escape_QuestionMark_FollowedByWildCardAsterisk Test mask verification
	 */
	@Test
	public void verification_escapeQuestionMarkFollowedByWildCardAsterisk() {
				
		partSpec.setPartSerialNumberMask("P\\?*");
		assertTrue(CommonPartUtility.verification("P?",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("P?A",partSpec.getPartSerialNumberMask(), PartMaskFormat.DEFAULT.toString()));
		
		partSpec.setPartSerialNumberMask("A\\?*");
		assertFalse(CommonPartUtility.verification("",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification(" ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("A?",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Ambica Gawarla, HAM
	 * @date Jan 05, 2017
	 * 
	 * WildCardQuestionMark_FollowedByAsterisk Test mask verification
	 */
	@Test
	public void verification_wildCardQuestionMarkFollowedByAsterisk() {

		partSpec.setPartSerialNumberMask("?*");
		assertFalse(CommonPartUtility.verification("",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification(" ",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("A",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("AA",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	
	/**
	 * @author Angel Vergara, HDM
	 * @date Sep 04, 2018
	 * 
	 * ParsePartMask_WithoutCommand Test parse part mask validation
	 */
	@Test
	public void parsePartMask_withoutCommands() {
		assertEquals("51M1HU#^########^",CommonPartUtility.parsePartMask("51M1HU<<#>><<^>><<#>><<#>><<#>><<#>><<#>><<#>><<#>><<#>><<^>>"));
		assertEquals("########R2.000",CommonPartUtility.parsePartMask("<<#>><<#>><<#>><<#>><<#>><<#>><<#>><<#>>R2.000"));
		assertEquals("3????^^^^??######",CommonPartUtility.parsePartMask("3<<?>><<?>><<?>><<?>><<^>><<^>><<^>><<^>><<?>><<?>><<#>><<#>><<#>><<#>><<#>><<#>>"));
		assertEquals("90137",CommonPartUtility.parsePartMask("90137"));
		assertEquals("*",CommonPartUtility.parsePartMask("<<*>>"));
	}
	
	/**
	 * @author Angel Vergara, HDM
	 * @date Sep 04, 2018
	 * 
	 * ParsePartMask_WithCommand Test parse part mask validation
	 */
	@Test
	public void parsePartMask_withCommands() {				
		assertEquals("51M1HU#^########^",CommonPartUtility.parsePartMask("51M1HU<<#>><<^>><<#;8>><<^>>"));
		assertEquals("########R2.000",CommonPartUtility.parsePartMask("<<#;8>>R2.000"));
		assertEquals("3????^^^^??######",CommonPartUtility.parsePartMask("3<<?;4>><<^;4>><<?;2>><<#;6>>"));
		assertEquals(" ",CommonPartUtility.parsePartMask(" "));
	}
	
	/**
	 * @author Angel Vergara, HDM
	 * @date Sep 04, 2018
	 * 
	 * ParsePartMask_WithCommandAndAnyCharacter Test parse part mask validation (<<%;n>>)
	 */
	@Test
	public void parsePartMask_withCommandsAndAnyCharacter() {
		assertEquals("51M1HU#^DDDDDDDD^",CommonPartUtility.parsePartMask("51M1HU<<#>><<^>><<D;8>><<^>>"));
		assertEquals("AAR2.000",CommonPartUtility.parsePartMask("<<A;2>>R2.000"));
		assertEquals("3AAAA^^^^??333333",CommonPartUtility.parsePartMask("3<<A;4>><<^;4>><<?;2>><<3;6>>"));
		assertEquals("001100000",CommonPartUtility.parsePartMask("0011<<0;5>>"));
		assertEquals("0011&&&&&",CommonPartUtility.parsePartMask("0011<<&;5>>"));
		assertEquals("0011|||||",CommonPartUtility.parsePartMask("0011<<|;5>>"));
		assertEquals("3AAAAX1X1X1_??%%%%%%",CommonPartUtility.parsePartMask("3<<A;4>><<X1;3>>_<<?;2>><<%;6>>"));
	}
	
	/**
	 * @author Paul Chou HCM
	 * @date Oct 12, 2018
	 * verification_multiCharwildCard Test multi char wild card part mask validation
	 * 
	 */
	@Test
	public void verification_multiCharwildCard() {
		partSpec.setPartSerialNumberMask("K0DP*9");
		assertTrue(CommonPartUtility.verification("K0DPABC1239",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertFalse(CommonPartUtility.verification("K0DPABC9123",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("K0DP9",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("S3-8P*");
		assertTrue(CommonPartUtility.verification("S3-8Padkdkd1",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("S3-8P",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
		partSpec.setPartSerialNumberMask("TAB A0 -2*");
		assertTrue(CommonPartUtility.verification("TAB A0 -233A",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("TAB A0 -2",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));


		partSpec.setPartSerialNumberMask(".A0*");
		assertTrue(CommonPartUtility.verification(".A0",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification(".A098",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
	}
	
	/**
	 * @author Paul Chou HCM
	 * @date Nov 20, 2018
	 * verification_multiCharwildCard Test multi char wild card part mask validation
	 * BUG FOUND: part mask: K0*9   part serial number: K07   system was validated as OK/pass
	 * 
	 */
	@Test
	public void verification_multiCharwildCardFix2() {
		
		partSpec.setPartSerialNumberMask("K0*9");
		assertFalse(CommonPartUtility.verification("K07",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		assertTrue(CommonPartUtility.verification("K079",partSpec.getPartSerialNumberMask(), PartMaskFormat.MSEXEL_ENHANCED.toString()));
		
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "MM/DD/YYYY" within checked range(10 days) 
	 */
	@Test
	public void verifyDateMaskMethod_withValidRange() {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -2); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -8);
		String ScannedDate1 = formatter.format(validDate.getTime());
		
		
		assertEquals(true,CommonPartUtility.verifyDateMask(ScannedDate,"MM/DD/YYYY",10));
		assertEquals(true,CommonPartUtility.verifyDateMask(ScannedDate1,"MM/DD/YYYY",11));

	}
	

	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "MM/DD/YYYY" outside of checked range(10 days)
	 */
	@Test
	public void verifyDateMaskMethod_withInvalidRange() {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -11); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -24);
		String ScannedDate1 = formatter.format(validDate.getTime());
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate,"MM/DD/YYYY",10));
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate1,"MM/DD/YYYY",10));
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "MM/DD/YYYY" within checked range(10 days) but 
	 * scanned in different format "yyyy/dd/MM"
	 */
	@Test
	public void verifyDateMaskMethod_withValidRangeButDifferentFormat() {
		DateFormat formatter = new SimpleDateFormat("yyyy/dd/MM");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -2); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -8);
		String ScannedDate1 = formatter.format(validDate.getTime());
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate,"MM/DD/YYYY",10));
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate1,"MM/DD/YYYY",10));

	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "MM/DD/YYYY" outside of range(10 days) and 
	 * scanned in different format "yyyy/dd/MM"
	 */
	@Test
	public void verifyDateMaskMethod_withInvalidRangeAndDifferentFormat() {
		DateFormat formatter = new SimpleDateFormat("yyyy/dd/MM");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -11); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -24);
		String ScannedDate1 = formatter.format(validDate.getTime());
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate,"MM/DD/YYYY",10));
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate1,"MM/DD/YYYY",10));
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "MM-DD-YYYY" inside and outside of range(10 days) 
	 */
	@Test
	public void verifyDateMaskMethod_withDifferentFormat1() {
		DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -8); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -24);
		String ScannedDate1 = formatter.format(validDate.getTime());
		assertEquals(true,CommonPartUtility.verifyDateMask(ScannedDate,"MM-DD-YYYY",10));
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate1,"MM-DD-YYYY",10));
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Jan 31, 2020
	 * 
	 * verifyDateMask with part mask "DDMMYYYY" inside and outside of range(10 days) 
	 */
	@Test
	public void verifyDateMaskMethod_withDifferentFormat2() {
		DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		formatter.setLenient(false);
		Date today = new Date();
		
		Calendar validDate = Calendar.getInstance();
		validDate.setTime(today); 
		validDate.add(Calendar.DATE, -8); 
		String ScannedDate = formatter.format(validDate.getTime());
		validDate.add(Calendar.DATE, -24);
		String ScannedDate1 = formatter.format(validDate.getTime());
		assertEquals(true,CommonPartUtility.verifyDateMask(ScannedDate,"DDMMYYYY",10));
		assertEquals(false,CommonPartUtility.verifyDateMask(ScannedDate1,"DDMMYYYY",10));
	}
	
	/**
	 * @author Mahesh Muniyappa
	 * @date Oct 26, 2020
	 * 
	 * verify getParseInfos for empty parseStrategy
	 */
	@Test
	public void parseStrategy_Empty() {
		partSpec.setParseStrategy("");
		partSpec.setParserInformation("");
		assertNotNull(CommonPartUtility.getParseInfos(partSpec));
	}
	
	/**
	 * @author Mahesh Muniyappa
	 * @date Oct 26, 2020
	 * 
	 * verify getParseInfos for null parseStrategy
	 */
	@Test
	public void parseStrategy_Null() {
		partSpec.setParseStrategy(null);
		partSpec.setParserInformation(null);
		assertNotNull(CommonPartUtility.getParseInfos(partSpec));
	}
	
	/**
	 * @author Mahesh Muniyappa
	 * @date Oct 26, 2020
	 * 
	 * parsePartMaskDisplay_withoutCommands Test parse part mask validation
	 */
	@Test
	public void parsePartMaskDisplay_withoutCommands() {
		assertEquals("51M1HU#^########^",CommonPartUtility.parsePartMaskDisplay("51M1HU<<#>><<^>><<#>><<#>><<#>><<#>><<#>><<#>><<#>><<#>><<^>>"));
		assertEquals("########R2.000",CommonPartUtility.parsePartMaskDisplay("<<#>><<#>><<#>><<#>><<#>><<#>><<#>><<#>>R2.000"));
		assertEquals("3????^^^^??######",CommonPartUtility.parsePartMaskDisplay("3<<?>><<?>><<?>><<?>><<^>><<^>><<^>><<^>><<?>><<?>><<#>><<#>><<#>><<#>><<#>><<#>>"));
		assertEquals("90137",CommonPartUtility.parsePartMaskDisplay("90137"));
		assertEquals("*",CommonPartUtility.parsePartMaskDisplay("<<*>>"));
	}
	
	/**
	 * @author Mahesh Muniyappa
	 * @date Oct 26, 2020
	 * 
	 * parsePartMaskDisplay_withCommands Test parse part mask validation
	 */
	@Test
	public void parsePartMaskDisplay_withCommands() {				
		assertEquals("51M1HU#^<<#;8>>^",CommonPartUtility.parsePartMaskDisplay("51M1HU<<#>><<^>><<#;8>><<^>>"));
		assertEquals("<<#;8>>R2.000",CommonPartUtility.parsePartMaskDisplay("<<#;8>>R2.000"));
		assertEquals("3<<?;4>><<^;4>><<?;2>><<#;6>>",CommonPartUtility.parsePartMaskDisplay("3<<?;4>><<^;4>><<?;2>><<#;6>>"));
		assertEquals(" ",CommonPartUtility.parsePartMaskDisplay(" "));
	}
	
	/**
	 * @author Mahesh Muniyappa
	 * @date Oct 26, 2020
	 * 
	 * parsePartMaskDisplay_withCommandsAndAnyCharacter Test parse part mask validation (<<%;n>>)
	 */
	@Test
	public void parsePartMaskDisplay_withCommandsAndAnyCharacter() {
		assertEquals("51M1HU#^<<D;8>>^",CommonPartUtility.parsePartMaskDisplay("51M1HU<<#>><<^>><<D;8>><<^>>"));
		assertEquals("<<A;2>>R2.000",CommonPartUtility.parsePartMaskDisplay("<<A;2>>R2.000"));
		assertEquals("3<<A;4>><<^;4>><<?;2>><<3;6>>",CommonPartUtility.parsePartMaskDisplay("3<<A;4>><<^;4>><<?;2>><<3;6>>"));
		assertEquals("0011<<0;5>>",CommonPartUtility.parsePartMaskDisplay("0011<<0;5>>"));
		assertEquals("0011<<&;5>>",CommonPartUtility.parsePartMaskDisplay("0011<<&;5>>"));
		assertEquals("0011<<|;5>>",CommonPartUtility.parsePartMaskDisplay("0011<<|;5>>"));
		assertEquals("3<<A;4>><<X1;3>>_<<?;2>><<%;6>>",CommonPartUtility.parsePartMaskDisplay("3<<A;4>><<X1;3>>_<<?;2>><<%;6>>"));
	}
		
}
