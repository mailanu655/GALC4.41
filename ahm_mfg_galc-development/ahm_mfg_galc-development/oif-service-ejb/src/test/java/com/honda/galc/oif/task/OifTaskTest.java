package com.honda.galc.oif.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.oif.RunHistory;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyHelper;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class,PropertyService.class, OifErrorsCollector.class,ComponentPropertyDao.class,
	ComponentStatusDao.class, PropertyHelper.class})
public class OifTaskTest  { 
	
	private OifTask testClass = null;
	PropertyHelper mockPropertyHelper = null;
	
	
	@BeforeClass
	public static void classSetup() {
		
	}
	
	@Before
	public void methodSetup() {
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when( PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG)).thenReturn(null);
		mockPropertyHelper = PowerMockito.mock(PropertyHelper.class);
		PowerMockito.when(mockPropertyHelper.getProperty(OIFConstants.INTERFACE_ID)).thenReturn("Test");
		
		testClass = new BomTask("Test");
		
		
	}
	
	@Test	
	public void setJobStatusTestNull(){
		testClass.setOifErrorsCollector(null);
		testClass.setJobStatus(OifRunStatus.SUCCESS);
		assertEquals(testClass.getOifErrorsCollector(), null);
	}
	
	@Test	
	public void setJobStatusTest(){
		testClass.setOifErrorsCollector(new OifErrorsCollector());
		testClass.getOifErrorsCollector().getRunHistory().setStatus(OifRunStatus.SUCCESS);
		testClass.setJobStatus(OifRunStatus.FAILURE);
		assertEquals(testClass.getOifErrorsCollector().getRunHistory().getStatus(), OifRunStatus.FAILURE);
	}
	
	@Test
	public void setOutgoingJobStatusAndFailedCountTest() {
		testClass.setOutgoingJobStatusAndFailedCount(10, OifRunStatus.FAILURE, "Test.xml");
		RunHistory runHistory = testClass.getOifErrorsCollector().getRunHistory();
		assertEquals(new Integer(10),runHistory.getFailedCount() );
	    assertEquals(OifRunStatus.FAILURE, runHistory.getStatus());
	    testClass.setOutgoingJobStatusAndFailedCount(10, OifRunStatus.COMPLETE_WITH_ERRORS, "Test.xml");
	    assertEquals(new Integer(20),runHistory.getFailedCount() );
	    assertEquals(OifRunStatus.COMPLETE_WITH_ERRORS, runHistory.getStatus());
	    testClass.setOutgoingJobStatusAndFailedCount(10, OifRunStatus.MQ_ERROR, "Test.xml");
	    assertEquals(new Integer(30),runHistory.getFailedCount() );
	    assertNotSame(OifRunStatus.MQ_ERROR, runHistory.getStatus());
	 }
	
	@Test
	public void getFilesFromMQTest() {
		testClass.getFilesFromMQ("OIF_BOM", 100);
		assertEquals(OifRunStatus.MISSING_CONFIGURATION, testClass.getOifErrorsCollector().getRunHistory().getStatus());
		
	}
	
	@Test
	public void getSendDataFailure() {
		PowerMockito.when(PropertyService.getPropertyBoolean("Test",OIFConstants.MQ_SENDER_FLAG, true)).thenReturn(Boolean.TRUE);
		
		testClass.getOifErrorsCollector().getRunHistory().setSuccessCount(10);
		List<String> mockResults = new ArrayList<String>();
		mockResults.add("line1");
		mockResults.add("line2");
		
		//MQ Send
		try {
			testClass.sendData(mockResults,null,null,null,"test.xml",null);
		}catch(Exception e) {
			
		}
		assertEquals(new Integer(10), testClass.getOifErrorsCollector().getRunHistory().getSuccessCount());
		assertEquals(new Integer(2), testClass.getOifErrorsCollector().getRunHistory().getFailedCount());
		assertEquals("test.xml", testClass.getOifErrorsCollector().getRunHistory().getOutgoingFileName());
		assertEquals(OifRunStatus.MQ_ERROR, testClass.getOifErrorsCollector().getRunHistory().getStatus());
		
	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void getSendDataSuccess() {
		PowerMockito.when(mockPropertyHelper.getPropertyBoolean(OIFConstants.MQ_SENDER_FLAG,true)).thenReturn(Boolean.FALSE);
		testClass.getOifErrorsCollector().getRunHistory().setSuccessCount(10);
		List<String> mockResults = new ArrayList<String>();
		mockResults.add("line1");
		mockResults.add("line2");
		testClass.sendData(mockResults,null,null,null,"test.xml",null);
		assertEquals(new Integer(12), testClass.getOifErrorsCollector().getRunHistory().getSuccessCount());
		assertEquals("test.xml", testClass.getOifErrorsCollector().getRunHistory().getOutgoingFileName());
	}
	
	@Test
	public void createExportLogTest() {
		
		try {
			testClass.createExportLog(null);
		}catch(Exception e) {
			
		}
		assertEquals(OifRunStatus.MISSING_CONFIGURATION, testClass.getOifErrorsCollector().getRunHistory().getStatus());
		
	
	}
		
	
}
