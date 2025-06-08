/**
 * 
 */
package com.honda.galc.entity.qi;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dto.qi.QiRecentDefectDto;

/**
 * @author VCC44349
 *
 */
public class QiDefectResultTest {

	private QiRecentDefectDto dto = null;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		dto = new QiRecentDefectDto();

		dto.setProductId("5J6RW1H87KA018587");
		dto.setInspectionPartName("PILLAR GARN L RR");
		dto.setInspectionPart2Name("HEADLINER");
		dto.setDefectTypeName("GAP");
		dto.setDefectTypeName2("");
		dto.setPointX(90);
		dto.setPointY(281);
		dto.setImageName("17M CRV INTERIOR");
		dto.setApplicationId("AVQ1QC1PQ0211   ");
		dto.setIqsVersion("IQS4");
		dto.setIqsCategoryName("INTERIOR                        ");
		dto.setIqsQuestionNo(176);
		dto.setIqsQuestion("HEADLINER - GAP/MISALIGNED");
		dto.setThemeName("INTERIOR");
		dto.setReportable(Short.valueOf("0"));
		dto.setResponsibleSite("MAP");
		dto.setResponsiblePlant("FRAME1          ");
		dto.setResponsibleDept("AF");
		dto.setResponsibleLevel3("CENTRAL");
		dto.setResponsibleLevel2("I0");
		dto.setResponsibleLevel1("IN04");
		dto.setProcessNo("1HB80   ");
		dto.setProcessName("LEFT HEADLINER/REAR GARNISH INSTALL");
		dto.setUnitNo("H20179  ");
		dto.setUnitDesc("INSTALL LEFT SIDE QUARTER PILLAR GARNISH TO LEFT SIDE QUARTER PILLAR AND SET RIDGE ALONG HEADLINER AND CLIPS (X10) TO THE PILLAR.");
		dto.setWriteUpDept("VQ");
		dto.setEntrySiteName("MAP");
		dto.setEntryPlantName("Frame1          ");
		dto.setEntryProdLineNo(1);
		dto.setEntryDept("AVQ1");
		Calendar cal1 = GregorianCalendar.getInstance();
		Calendar cal2 = GregorianCalendar.getInstance();
		cal2.setTime(cal1.getTime());
		cal2.add(Calendar.DATE, -1);
		dto.setActualTimestamp(cal1.getTime());
		dto.setProductionDate(cal2.getTime());
		dto.setShift("02");
		dto.setTeam("MAP-B");
		dto.setProductType("FRAME     ");
		dto.setProductSpecCode("KTPDAB500 NH883PX   F");
		dto.setProductionLot("MAP 01AF201902080250");
		dto.setKdLotNumber("MAP 01201902014401");
		dto.setAfOnSequenceNumber(71000);
		dto.setDefectCategoryName("REAL PROBLEM");
		dto.setBomMainPartNo("84551");
		dto.setOriginalDefectStatus(Short.valueOf("1"));
		dto.setCurrentDefectStatus(Short.valueOf("7"));
		dto.setRepairArea("XX");
		dto.setRepairMethodNamePlan("UNDETERMINED REPAIR METHOD");
		dto.setRepairTimePlan(0);
		dto.setLocalTheme("FIT AND FINISH    ");
		dto.setDeleted(Short.valueOf("0"));
		dto.setGdpDefect(Short.valueOf("0"));
		dto.setTrpuDefect(Short.valueOf("1"));
		dto.setTerminalName("AVQ1QC1TQ0212                   ");
		dto.setIsRepairRelated(Short.valueOf("0"));
		dto.setGroupTimestamp(new Timestamp(cal1.getTime().getTime()));
		dto.setEntryScreen("INTERIOR  CRV");
		dto.setEngineFiringFlag(Short.valueOf("0"));
		dto.setIncidentId(Short.valueOf("0"));
		dto.setEntryModel("CRV JTP");
		dto.setCorrectionRequestBy(null);
		dto.setReasonForChange(null);
		dto.setPddaModelYear(2019.0f);
		dto.setPddaVehicleModelCode("H");
		dto.setCount(5);
	}

	/**
	 * Test method for {@link com.honda.galc.entity.qi.QiDefectResult#createInstance(com.honda.galc.dto.qi.QiRecentDefectDto)}.
	 */
	@Test
	public void testCreateInstance() {
		QiDefectResult defectResult = QiDefectResult.createInstance(dto);
		assertNotNull(defectResult);
		assertTrue(defectResult.getProductId() == null || defectResult.getProductId().trim().length() == 0);
		assert(defectResult.getApplicationId() .equals(dto.getApplicationId()));
		assert(defectResult.getDefectResultId() == 0);
		assert(defectResult.getPartDefectDesc().equals(dto.getPartDefectDesc()));
		assert(defectResult.getResponsibilityDesc().equals(dto.getResponsibilityDesc()));
		assertNull(defectResult.getActualTimestamp());
		assert(dto.getCount() == 5);
	}

}
