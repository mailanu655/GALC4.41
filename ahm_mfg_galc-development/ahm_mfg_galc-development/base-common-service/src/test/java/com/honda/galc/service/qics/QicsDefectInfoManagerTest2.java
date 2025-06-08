package com.honda.galc.service.qics;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.service.property.PropertyService;

@RunWith(PowerMockRunner.class)
@PrepareForTest (PropertyService.class)
public class QicsDefectInfoManagerTest2 {
	
	@Mock
	public QicsServicePropertyBean qBean = Mockito.mock(QicsServicePropertyBean.class);
	QicsDefectInfoManager qiDefectInfoManager = null;
	Map<String,String> defectCodeMap = null;
	Properties props = new Properties();

	@BeforeClass
	public static void setUpClass() {
	}

	@Before
	public void setUp() throws Exception {
		Mockito.when(qBean.getDefectCodeMapping()).thenReturn(defectCodeMap);
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(QicsServicePropertyBean.class)).thenReturn(qBean);
		PowerMockito.when(PropertyService.getComponentProperties(anyString())).thenReturn(props);
		qiDefectInfoManager = QicsDefectInfoManager.getInstance();
		setUpProperties();
	}
	
	public void setUpProperties() {
		
	}
	
	@Test
	public void testGetInstance() { 
		assertNotNull(QicsDefectInfoManager.getInstance());
	}
	
	@Test
	public void testGetDefectLocation() {
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", "")));
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "")));
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", "")));
	}
	
	public void testGetDefectLocationWithNull() {
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null)));
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null)));
		assertEquals(true, "".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null)));
	}
	
	@Test
	public void testGetDefectName() {
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", (String)null, (String)null)), true);
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "", "")), true);
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", (String)null, "")), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH01", "")), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH02", "")), true); 
	}
	
	@Test
	public void testGetDefectNameWithNullDefectCodeMap() {
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH01", null)), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", "SH01", null)), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "SH01", "")), true);
	}

	@Test
	public void testGetDefectNameWithNull() {
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null, null)), true);
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null,null)), true);
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null, null)), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH01", null)), true); 
		assertEquals("".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH02", null)), true); 
	}
	
	@Test
	public void testGetGroupName() {
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", "")), true);
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "")), true);
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", "")), true);
	}
	
	@Test
	public void testGetGroupNameWithNull() {
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null)), true);
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null)), true);
		assertEquals("".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null)), true);
	}
}
