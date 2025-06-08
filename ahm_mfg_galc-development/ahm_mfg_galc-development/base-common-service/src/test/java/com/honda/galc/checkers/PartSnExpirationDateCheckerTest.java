package com.honda.galc.checkers;

import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Jun 6, 2018
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyService.class)
public class PartSnExpirationDateCheckerTest {
	
	private PartSnExpirationDateChecker checker = null;
	private PartSerialScanData scanData = null;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private static Map<String, String> yearCode = new HashMap<String, String>();
	private static Map<String, String> monthCode = new HashMap<String, String>();
	
	private static Map<String, String> revYearCode = new HashMap<String, String>();
	private static Map<String, String> revMonthCode = new HashMap<String, String>();
	
	@Mock
	private static ProductCheckPropertyBean productPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
	
	@BeforeClass
	public static void classSetup() {
		initYearMonthMap();
		initReverseYearMonthMap();
	}
	
	@Before
	public void methodSetup() {		
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class)).thenReturn(productPropertyBeanMock);
		PowerMockito.when(productPropertyBeanMock.getExpirationCheckInDays()).thenReturn(0);
		
		checker = new PartSnExpirationDateChecker();
		scanData = new PartSerialScanData();
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Tests required length of the
	 * input serial number
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * SD2-MH2	(invalid length, no day)
	 */
	@Test
	public void executeCheck_invalidLength() {
		scanData.setSerialNumber("SD2-MH2");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Tests date format of the 
	 * input serial number
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * SD2-AB240000	(A for year is invalid)
	 */
	@Test
	public void executeCheck_invalidDateFormat_year() {
		scanData.setSerialNumber("SD2-RB240000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Tests date format of the 
	 * input serial number
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * SD2-MX240000	(X for month is invalid)
	 */
	@Test
	public void executeCheck_invalidDateFormat_month() {
		scanData.setSerialNumber("SD2-MX240000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Tests date format of the 
	 * input serial number
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * SD2-MX320000	(32 for day is invalid)
	 */
	@Test
	public void executeCheck_invalidDateFormat_dayofMonth() {
		scanData.setSerialNumber("SD2-MX320000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date July 11, 2019
	 * 
	 * Tests for exempt expired battery
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = -30
	 * 
	 * expired 31 days ago
	 */
	@Test
	public void executeCheck_expirationDaysMinus30_minus31days() throws ParseException {
		Date thirtyOneDaysBeforeNow = DateUtils.addDays(new Date(), -31);
		
		String encodedExpDate = revYearCode.get(convertDate(thirtyOneDaysBeforeNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(thirtyOneDaysBeforeNow, Calendar.MONTH))
							+ convertDate(thirtyOneDaysBeforeNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 1);
		assertTrue(checkResults.get(0).getCheckMessage().equals("Battery expired on " + sdf.parse(getDateString(thirtyOneDaysBeforeNow))));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date July 11, 2019
	 * 
	 * Tests for exempt expired battery
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = -30
	 * 
	 * expired 30 days ago
	 */
	@Test
	public void executeCheck_expirationDaysMinus30_minus30days() throws ParseException {
		Date thirtyDaysBeforeNow = DateUtils.addDays(new Date(), -30);
		
		String encodedExpDate = revYearCode.get(convertDate(thirtyDaysBeforeNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(thirtyDaysBeforeNow, Calendar.MONTH))
							+ convertDate(thirtyDaysBeforeNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
		assertTrue(checkResults.get(0).getCheckMessage().equals("Battery expired on " + sdf.parse(getDateString(thirtyDaysBeforeNow))));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date July 11, 2019
	 * 
	 * Tests for exempt expired battery
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = -30
	 * 
	 * expired 29 days ago
	 */
	@Test
	public void executeCheck_expirationDaysMinus30_minus29days() {
		Date eightDaysBeforeNow = DateUtils.addDays(new Date(), -29);
		
		String encodedExpDate = revYearCode.get(convertDate(eightDaysBeforeNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(eightDaysBeforeNow, Calendar.MONTH))
							+ convertDate(eightDaysBeforeNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date July 11, 2019
	 * 
	 * Tests for exempt expired battery
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = -30
	 * 
	 * expiring today
	 */
	@Test
	public void executeCheck_expirationDaysMinus30_today() {
		Date today = new Date();
		
		String encodedExpDate = revYearCode.get(convertDate(today, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(today, Calendar.MONTH))
							+ convertDate(today, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Tests for expired battery
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * SD2-PA120000	(expired on Jan 12, 2015)
	 */
	@Test
	public void executeCheck_expirationDays0_minusSeveralDays() {
		scanData.setSerialNumber("SD2-PA120000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
		assertTrue(checkResults.get(0).getCheckMessage().matches("Battery expired on Mon Jan 12 00:00:00 .{1,100} 2015"));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * expired yesterday
	 */
	@Test
	public void executeCheck_expirationDays0_yesterday() throws ParseException {
		Date yesterday = DateUtils.addDays(new Date(), -1);
		String encodedExpDate = revYearCode.get(convertDate(yesterday, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(yesterday, Calendar.MONTH))
							+ convertDate(yesterday, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 1);
		assertTrue(checkResults.get(0).getCheckMessage().equals("Battery expired on " + sdf.parse(getDateString(yesterday))));
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * expiring today
	 */
	@Test
	public void executeCheck_expirationDays0_today() throws ParseException {
		Date now = new Date();
		String encodedExpDate = revYearCode.get(convertDate(now, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(now, Calendar.MONTH))
							+ convertDate(now, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 1);
		assertTrue(checkResults.get(0).getCheckMessage().equals("Battery expired on " + sdf.parse(getDateString(now))));
	}
		
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * expiring tomorrow
	 */
	@Test
	public void executeCheck_expirationDays0_tomorrow() throws ParseException {
		Date eightDaysFromNow = DateUtils.addDays(new Date(), 1);
		
		String encodedExpDate = revYearCode.get(convertDate(eightDaysFromNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(eightDaysFromNow, Calendar.MONTH))
							+ convertDate(eightDaysFromNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * Happy path test. Tests if the battery is still good
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 0
	 * 
	 * expiring in 100 days 
	 */
	@Test
	public void executeCheck_expirationDays0_100days() {
		Date hundredDaysFromNow = DateUtils.addDays(new Date(), 100);
		
		String encodedExpDate = revYearCode.get(convertDate(hundredDaysFromNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(hundredDaysFromNow, Calendar.MONTH))
							+ convertDate(hundredDaysFromNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 15
	 * 
	 * SD2-PA120000	(expired on Jan 12, 2015)
	 */
	@Test
	public void executeCheck_expirationDays15_minusSeveralDays() throws ParseException {
		scanData.setSerialNumber("SD2-PA120000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() > 0);
		assertTrue(checkResults.get(0).getCheckMessage().matches("Battery expired on Mon Jan 12 00:00:00 .{1,100} 2015"));
	}
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 15
	 * 
	 * expiring in 14 days
	 */
	@Test
	public void executeCheck_expirationDays15_14Days() throws ParseException {
		Date eightDaysFromNow = DateUtils.addDays(new Date(), 14);
		
		String encodedExpDate = revYearCode.get(convertDate(eightDaysFromNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(eightDaysFromNow, Calendar.MONTH))
							+ convertDate(eightDaysFromNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 0);
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * @throws ParseException 
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 15
	 * 
	 * expiring in 15 days
	 */
	@Test
	public void executeCheck_expirationDays15_15Days() throws ParseException {
		Date eightDaysFromNow = DateUtils.addDays(new Date(), 15);
		
		String encodedExpDate = revYearCode.get(convertDate(eightDaysFromNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(eightDaysFromNow, Calendar.MONTH))
							+ convertDate(eightDaysFromNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 0);
		
	}
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Jun 6, 2018
	 * 
	 * EXPIRATION_CHECK_IN_DAYS = 15
	 * 
	 * expiring in 16 days
	 */
	@Test
	public void executeCheck_expirationDays15_16Days() {
		Date twentyThreeDaysFromNow = DateUtils.addDays(new Date(), 16);
		
		String encodedExpDate = revYearCode.get(convertDate(twentyThreeDaysFromNow, Calendar.YEAR)) 
							+ revMonthCode.get(convertDate(twentyThreeDaysFromNow, Calendar.MONTH))
							+ convertDate(twentyThreeDaysFromNow, Calendar.DAY_OF_MONTH);
		
		scanData.setSerialNumber("SD2-" + encodedExpDate + "0000");
		List<CheckResult> checkResults = checker.executeCheck(scanData);
		assertTrue(checkResults.size() == 0);
	}
	
	private static void initYearMonthMap() {
		yearCode.put("P", "2015");
		yearCode.put("O", "2016");
		yearCode.put("N", "2017");
		yearCode.put("M", "2018");
		yearCode.put("L", "2019");
		yearCode.put("K", "2020");
		yearCode.put("J", "2021");
		yearCode.put("I", "2022");
		yearCode.put("H", "2023");
		yearCode.put("G", "2024");
		yearCode.put("F", "2025");
		yearCode.put("E", "2026");
		yearCode.put("D", "2027");
		yearCode.put("C", "2028");
		yearCode.put("B", "2029");
		yearCode.put("A", "2030");
		yearCode.put("Z", "2031");
		yearCode.put("Y", "2032");
		
		monthCode.put("A", "01");
		monthCode.put("B", "02");
		monthCode.put("C", "03");
		monthCode.put("D", "04");
		monthCode.put("E", "05");
		monthCode.put("F", "06");
		monthCode.put("G", "07");
		monthCode.put("H", "08");
		monthCode.put("I", "09");
		monthCode.put("J", "10");
		monthCode.put("K", "11");
		monthCode.put("L", "12");
	}
	
	private static void initReverseYearMonthMap() {
		revYearCode.put("2015","P");
		revYearCode.put("2016","O");
		revYearCode.put("2017","N");
		revYearCode.put("2018","M");
		revYearCode.put("2019","L");
		revYearCode.put("2020","K");
		revYearCode.put("2021","J");
		revYearCode.put("2022","I");
		revYearCode.put("2023","H");
		revYearCode.put("2024","G");
		revYearCode.put("2025","F");
		revYearCode.put("2026","E");
		
		revMonthCode.put("01","A");
		revMonthCode.put("02","B");
		revMonthCode.put("03","C");
		revMonthCode.put("04","D");
		revMonthCode.put("05","E");
		revMonthCode.put("06","F");
		revMonthCode.put("07","G");
		revMonthCode.put("08","H");
		revMonthCode.put("09","I");
		revMonthCode.put("10","J");
		revMonthCode.put("11","K");
		revMonthCode.put("12","L");
	}

	private String getDateString(Date date) {
		String dateString = convertDate(date, Calendar.YEAR)
							+ convertDate(date, Calendar.MONTH)
							+ convertDate(date, Calendar.DAY_OF_MONTH);
		return dateString;
	}
	
	private String convertDate(Date date, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		switch(type) {
		case Calendar.YEAR:
			return Integer.toString(cal.get(Calendar.YEAR));
		case Calendar.MONTH:
			return StringUtils.leftPad(Integer.toString(cal.get(Calendar.MONTH) + 1), 2, '0');
		case Calendar.DAY_OF_MONTH:
			return StringUtils.leftPad(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)), 2, '0');
		default:
			return "";
		}
	}	
}
