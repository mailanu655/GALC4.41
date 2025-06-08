package com.honda.galc.system.oif.svc.common;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;

import junit.framework.TestCase;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PropertyService.class)
public class OifErrorsCollectorTest extends TestCase {
	
	static OifErrorsCollector testClass =null;
	
	@BeforeClass
	public static void classSetup() {
		
	}
	
	@Before
	public void methodSetup() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when( PropertyService.getProperty(OIFConstants.OIF_NOTIFICATION_PROPERTIES, OIFConstants.EMAIL_NOTIFICATION_LEVEL)).thenReturn("NONE");
		testClass = new OifErrorsCollector();
	}
	
	@Test
	public void getRunHistoryTest() {
		assertNotSame(testClass.getRunHistory(),null);
	}
	
	@Test
	public void emergencyTest() {
		testClass.emergency("ERROR Test");
		assertEquals(OifRunStatus.FAILURE, testClass.getRunHistory().getStatus());
	}

}
