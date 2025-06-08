package com.honda.galc.client.qi.repairentry;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.client.qi.repairentry.RepairEntryModel;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.qi.QiRepairAreaDao;
import com.honda.galc.dao.qi.QiRepairResultDao;
import com.honda.galc.dao.qi.QiStationPreviousDefectDao;
import com.honda.galc.dto.qi.QiRepairResultDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.qi.QiRepairArea;
import com.honda.galc.entity.qi.QiStationPreviousDefect;
import com.honda.galc.entity.qi.QiStationPreviousDefectId;
import com.honda.galc.service.ServiceFactory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ServiceFactory.class)
public class RepairEntryModelTest {
	
	public static final String PROCESS_POINT_ID = "TEST_PP_ID";
	public static final String SITE_NAME = "ELP";
	public static final String INVALID_SITE_NAME = "INVALID";
	public static final String REPAIR_AREA_NAME = "Repair Area Name";
	public static final String REPAIR_AREA_DESCRIPTION = "Repair Area Description";
	public static final String ENTRY_DIVISION_ID = "DIV";
	public static final String PRODUCT_ID = "17VIN123456789012";
	public static final String DEPTS = "('AF', 'VQ')";
	public static final String DEPTS_INVALID = "('AZ', 'VQ')";
	public static final int COUNT = 10;
	
	List<QiRepairArea> qiRepairAreas = new ArrayList<QiRepairArea>();
	List<String> qiRepairAreaStrings = new ArrayList<String>();
	RepairEntryModel repairEntryModel;	
	ProcessPoint processPoint = new ProcessPoint();
	List<QiStationPreviousDefect> qiStationPreviousDefectList = new ArrayList<QiStationPreviousDefect>();
	List<QiRepairResultDto> qiRepairResultDtoList = new ArrayList<QiRepairResultDto>();
	
	@Mock
	public ProcessPointDao processPointDao = Mockito.mock(ProcessPointDao.class);
	
	@Mock
	public QiRepairAreaDao qiRepairAreaDao = Mockito.mock(QiRepairAreaDao.class);
	
	@Mock
	public QiStationPreviousDefectDao qiStationPreviousDefectDao = Mockito.mock(QiStationPreviousDefectDao.class);	

	@Mock
	public QiRepairResultDao qiRepairResultDao = Mockito.mock(QiRepairResultDao.class);
	
	@Before
	public void setUp() {
		for (int i = 0; i < COUNT; i++) {
			QiRepairArea qiRepairArea = new QiRepairArea();
			qiRepairArea.setRepairAreaName(REPAIR_AREA_NAME + i);
			qiRepairArea.setRepairAreaDescription(REPAIR_AREA_DESCRIPTION + i);
			qiRepairAreas.add(qiRepairArea);
			qiRepairAreaStrings.add(REPAIR_AREA_NAME + i + " - " + REPAIR_AREA_DESCRIPTION + i);
		}
		
		processPoint.setProcessPointId(PROCESS_POINT_ID);
		
		for (int i = 0; i < COUNT; i++) {
			QiStationPreviousDefect qiStationPreviousDefect = new QiStationPreviousDefect();
			QiStationPreviousDefectId qiStationPreviousDefectId = new QiStationPreviousDefectId();
			qiStationPreviousDefectId.setProcessPointId(PROCESS_POINT_ID);
			qiStationPreviousDefectId.setEntryDivisionId(ENTRY_DIVISION_ID + i);
			qiStationPreviousDefect.setId(qiStationPreviousDefectId);
			qiStationPreviousDefect.setOriginalDefectStatus((short)5);
			qiStationPreviousDefect.setCurrentDefectStatus((short)6);
			qiStationPreviousDefectList.add(qiStationPreviousDefect);
		}
		
		for (int i = 0; i < COUNT; i++) {
			QiRepairResultDto qiRepairResultDto = new QiRepairResultDto();
			qiRepairResultDto.setDefectResultId(i);
			qiRepairResultDtoList.add(qiRepairResultDto);
		}
		
		// === mock static calls === //
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.when(ServiceFactory.getDao(QiRepairAreaDao.class)).thenReturn(qiRepairAreaDao);
		PowerMockito.when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDao);
		PowerMockito.when(ServiceFactory.getDao(QiStationPreviousDefectDao.class)).thenReturn(qiStationPreviousDefectDao);
		PowerMockito.when(ServiceFactory.getDao(QiRepairResultDao.class)).thenReturn(qiRepairResultDao);
		
		// === mock DAOs === //
		Mockito.when(qiRepairAreaDao.findAllBySite(SITE_NAME)).thenReturn(qiRepairAreas);
		Mockito.when(processPointDao.findById(PROCESS_POINT_ID)).thenReturn(processPoint);
		Mockito.when(qiStationPreviousDefectDao.findAllByProcessPoint(PROCESS_POINT_ID)).thenReturn(qiStationPreviousDefectList);
		Mockito.when(qiRepairResultDao.findAllDefectsByProductIdEntryDepts(PRODUCT_ID, DEPTS)).thenReturn(qiRepairResultDtoList);
		
		repairEntryModel = new RepairEntryModel() {
			public String getProcessPointId() {
				return PROCESS_POINT_ID;
			}
			
			public String getCurrentWorkingProcessPointId() {
				return PROCESS_POINT_ID;
			}
		};
	}

	/**
	 * @author Justin Jiang, HNA
	 * @date Nov 12, 2018
	 * 
	 * Tests findAllRepairAreasBySite with valid site name
	 */
	@Test
	public void findAllRepairAreasBySite_siteName() {
		assertArrayEquals(null, repairEntryModel.findAllRepairAreasBySite(SITE_NAME).toArray(), 
				qiRepairAreaStrings.toArray());
	}
	
	/**
	 * @author Justin Jiang, HNA
	 * @date Nov 12, 2018
	 * 
	 * Tests findAllRepairAreasBySite with invalid site name
	 */
	@Test
	public void findAllRepairAreasBySite_invalidSiteName() {
		assertArrayEquals(null, repairEntryModel.findAllRepairAreasBySite(INVALID_SITE_NAME).toArray(), 
				new ArrayList<String>().toArray());
	}
	
	/**
	 * @author Justin Jiang, HNA
	 * @date Dec 5, 2018
	 * 
	 * Tests findAllByProcessPoint
	 */
	@Test
	public void findAllByProcessPoint() {
		assertArrayEquals(null, repairEntryModel.findAllByProcessPoint().toArray(), 
				qiStationPreviousDefectList.toArray());
	}
	
	/**
	 * @author Justin Jiang, HNA
	 * @date Dec 5, 2018
	 * 
	 * Tests findAllDefectsByProductIdEntryDepts
	 */
	@Test
	public void findAllDefectsByProductIdEntryDepts() {
		assertArrayEquals(null, repairEntryModel.findAllDefectsByProductIdEntryDepts(PRODUCT_ID, DEPTS).toArray(), 
				qiRepairResultDtoList.toArray());
	}
	
	/**
	 * @author Justin Jiang, HNA
	 * @date Dec 5, 2018
	 * 
	 * Tests findAllDefectsByProductIdEntryDepts
	 */
	@Test
	public void findAllDefectsByProductIdEntryDepts_invalidProductId() {
		assertArrayEquals(null, repairEntryModel.findAllDefectsByProductIdEntryDepts(PRODUCT_ID + "1", DEPTS).toArray(), 
				new ArrayList<String>().toArray());
	}
	
	/**
	 * @author Justin Jiang, HNA
	 * @date Dec 5, 2018
	 * 
	 * Tests findAllDefectsByProductIdEntryDepts
	 */
	@Test
	public void findAllDefectsByProductIdEntryDepts_invalidDepts() {
		assertArrayEquals(null, repairEntryModel.findAllDefectsByProductIdEntryDepts(PRODUCT_ID, DEPTS_INVALID).toArray(), 
				new ArrayList<String>().toArray());
	}
}
