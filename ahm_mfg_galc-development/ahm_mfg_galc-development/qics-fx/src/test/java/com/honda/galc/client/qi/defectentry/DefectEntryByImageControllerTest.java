package com.honda.galc.client.qi.defectentry;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.honda.galc.dto.qi.QiMostFrequentDefectsDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.entity.qi.QiStationConfigurationId;

public class DefectEntryByImageControllerTest {

	List<String> freqNamesList = null;
	List<String> defectList = null;
	List<QiMostFrequentDefectsDto> freqDtoList = null;
	DefectEntryController defectEntryControllerMock = null;
	DefectEntryModel modelMock = null;
	QiStationConfiguration qiStationConfig = null;
	@Before
	public void setUp() throws Exception {
		freqNamesList = new ArrayList<String>();
		String[][] freqArrayData =
			{
				{"NOT INSTALLED","","4"},
				{"NOT SET","","4"},
				{"CLEAN UP","PRIMER","2"}, 
				{"CLEAN UP","","1"}, 
				{"CLEAN UP","AF PRIMER","1"}
			};
		
		String[] freqNamesArray =
			{
				"NOT INSTALLED",
				"NOT SET",
				"CLEAN UP - PRIMER", 
				"CLEAN UP", 
				"CLEAN UP - AF PRIMER"
			};
		
		String[] defectArray =
			{
				"SCRATCH",
				"STICKS",
				"WRONG PART",
				"CLEAN UP",
				"CLEAN UP - AF PRIMER",
				"NOT AVAILABLE",
				"NOT INSTALLED",
				"NOT SET",
				"CLEAN UP - GREASE",
				"CLEAN UP - PRIMER", 
				"DEFORM", 
				"FREEPLAY",
				"INOPERATIVE",
			};
		defectList = new ArrayList<String>(Arrays.asList(defectArray));
		freqNamesList = new ArrayList<String>(Arrays.asList(freqNamesArray));
		freqDtoList = new ArrayList<QiMostFrequentDefectsDto>();
		for(int i = 0; i < freqArrayData.length; i++)  {
			QiMostFrequentDefectsDto dto = new QiMostFrequentDefectsDto();
			String defect1 = freqArrayData[i][0];
			String defect2 = freqArrayData[i][1];
			int count = Integer.parseInt(freqArrayData[i][2]);
			dto.setPrimaryDefect(defect1);
			dto.setSecondaryDefect(defect2);
			dto.setCount(count);
			freqDtoList.add(dto);
		}
		qiStationConfig = new QiStationConfiguration();
		QiStationConfigurationId id = new QiStationConfigurationId();
		id.setProcessPointId("TVQ3QC1PQ0211");
		id.setPropertyKey(QiEntryStationConfigurationSettings.MAX_MOST_FREQ_USED_SZ.getSettingsName());
		qiStationConfig.setId(id);
		qiStationConfig.setPropertyValue("7");
		modelMock = mock(DefectEntryModel.class);
		defectEntryControllerMock = mock(DefectEntryController.class);
		when(modelMock.findMostFrequentDefectsByProcessPointEntryScreenDuration(anyString(), any(Date.class))).thenReturn(freqDtoList);
		when(defectEntryControllerMock.getModel()).thenReturn(modelMock);
		when(modelMock.findPropertyKeyValueByProcessPoint(
				QiEntryStationConfigurationSettings.MAX_MOST_FREQ_USED_SZ.getSettingsName())).
				thenReturn(qiStationConfig,qiStationConfig,qiStationConfig,null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMergeDefectList() {
		DefectEntryByImageController imageController = new DefectEntryByImageController(defectEntryControllerMock);
		List<String> mostRecentList = imageController.getMostRecentlyUsed("INTERIOR L1");
		assertEquals(5, mostRecentList.size());
		List<String> merged = imageController.mergeDefectList(mostRecentList, defectList);
		assertEquals(13, merged.size());
		Collections.sort(defectList);
		Collections.sort(freqNamesList);

		for(int i = 0; i < mostRecentList.size(); i++)  {
			assertArrayEquals(freqNamesList.toArray(), merged.subList(0, 5).toArray());
		}
	}

	@Test
	public void testGetMostRecentlyUsed() {
		DefectEntryByImageController imageController = new DefectEntryByImageController(defectEntryControllerMock);
		List<String> mostRecentList = imageController.getMostRecentlyUsed("INTERIOR L1");
		assertEquals(5, mostRecentList.size());
	}
	
	@Test
	public void testGetFreqUsedListSize()  {
		DefectEntryByImageController imageController = new DefectEntryByImageController(defectEntryControllerMock);
		assertEquals(7,imageController.getFreqUsedListSize());
		qiStationConfig.setPropertyValue("");
		assertEquals(10,imageController.getFreqUsedListSize());
		qiStationConfig.setPropertyValue(null);
		assertEquals(10,imageController.getFreqUsedListSize());
		//next call to modelMock will return null
		assertEquals(10,imageController.getFreqUsedListSize());
	}

}
