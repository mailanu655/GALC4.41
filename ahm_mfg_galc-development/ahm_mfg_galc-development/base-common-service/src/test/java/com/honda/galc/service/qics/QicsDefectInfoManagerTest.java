package com.honda.galc.service.qics;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;

import java.util.HashMap;
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
public class QicsDefectInfoManagerTest {
	
	private enum QicsDefect{QICS_DEFECT_LOCATION, QICS_DEFECT_NAME, QICS_DEFECT_GROUP_NAME};
	private enum QicsLine{RESPONSIBLE_DEPT, RESPONSIBLE_ZONE, RESPONSIBLE_LINE};
	@Mock
	public QicsServicePropertyBean qBean = Mockito.mock(QicsServicePropertyBean.class);
	QicsDefectInfoManager qiDefectInfoManager = null;
	Map<String,String> defectCodeMap = new HashMap<String,String>();
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
		// === mock static calls === //
		/**
		 *	qics	DEFECT_CODE_MAPPING{0}	FAILED	Auto Defect
			qics	DEFECT_CODE_MAPPING{8}	BYPASS	Machine is in bypass mode
			qics	DEFECT_CODE_MAPPING{SH01}	Seating Time Lower NG
			qics	DEFECT_CODE_MAPPING{SH02}	Seating Angle Lower NG
		 */
		props.clear();
		defectCodeMap.clear();
		defectCodeMap.put("0", "FAILED");
		defectCodeMap.put("8", "BYPASS");
		defectCodeMap.put("SH01", "Seating Time Lower NG");
		defectCodeMap.put("SH02", "Seating Angle Lower NG");
		
		props.setProperty(QicsDefect.QICS_DEFECT_NAME.name(), "NG-0");
		props.setProperty(QicsDefect.QICS_DEFECT_LOCATION.name(), "STATUS-0");
		props.setProperty(QicsDefect.QICS_DEFECT_GROUP_NAME.name(), "GEN-0");
		
		props.setProperty(QicsLine.RESPONSIBLE_DEPT.name(), "VL5");
		props.setProperty(QicsLine.RESPONSIBLE_LINE.name(), "VALVE");
		props.setProperty(QicsLine.RESPONSIBLE_ZONE.name(), "ZONE01");
		
		props.setProperty("OUTER_INT_RC_INSTALL_01." + QicsDefect.QICS_DEFECT_NAME.name(), "OUTER-NG1");
		props.setProperty("OUTER_INT_RC_INSTALL_01." + QicsDefect.QICS_DEFECT_LOCATION.name(), "OUTER-L1");
		props.setProperty("OUTER_INT_RC_INSTALL_01." + QicsDefect.QICS_DEFECT_GROUP_NAME.name(), "OUTER-GP1");
		
		props.setProperty("OUTER_INT_RC_VERIFY_01." + QicsDefect.QICS_DEFECT_NAME.name(), "NG");
		props.setProperty("OUTER_INT_RC_VERIFY_01." + QicsDefect.QICS_DEFECT_LOCATION.name(), "STATUS");
		props.setProperty("OUTER_INT_RC_VERIFY_01." + QicsDefect.QICS_DEFECT_GROUP_NAME.name(), "GEN");
		
	}
	
	@Test
	public void testGetInstance() { 
		assertNotNull(QicsDefectInfoManager.getInstance());
	}

	@Test
	public void testGetDefectLocation() {
		assertEquals(true, "STATUS".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", "")));
		assertEquals(true, "OUTER-L1".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "")));
		assertEquals(true, "STATUS-0".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", "")));
	}

	public void testGetDefectLocationWithNull() {
		assertEquals(true, "STATUS".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null)));
		assertEquals(true, "OUTER-L1".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null)));
		assertEquals(true, "STATUS-0".equals(qiDefectInfoManager.getDefectLocation("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null)));
	}
	
	@Test
	public void testGetDefectName() {
		assertEquals("NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", (String)null, (String)null)), true);
		assertEquals("OUTER-NG1".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "", "")), true);
		assertEquals("NG-0".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", (String)null, "")), true); 
		assertEquals("Seating Time Lower NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH01", "")), true); 
		assertEquals("Seating Angle Lower NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH02", "")), true); 
	}

	@Test
	public void testGetDefectNameWithNull() {
		assertEquals("NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null, null)), true);
		assertEquals("OUTER-NG1".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null,null)), true);
		assertEquals("NG-0".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null, null)), true); 
		assertEquals("Seating Time Lower NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH01", null)), true); 
		assertEquals("Seating Angle Lower NG".equals(qiDefectInfoManager.getDefectName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_02", "SH02", null)), true); 
	}

	@Test
	public void testGetGroupName() {
		assertEquals("GEN".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", "")), true);
		assertEquals("OUTER-GP1".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", "")), true);
		assertEquals("GEN-0".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", "")), true);
	}

	@Test
	public void testGetGroupNameWithNull() {
		assertEquals("GEN".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_01", null)), true);
		assertEquals("OUTER-GP1".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_INSTALL_01", null)), true);
		assertEquals("GEN-0".equals(qiDefectInfoManager.getGroupName("NVL5LN1P01001", "OUTER_INT_RC_VERIFY_02", null)), true);
	}

	@Test
	public void testGetResponsibleZone() {
		assertEquals("VL5".equals(qiDefectInfoManager.getResponsibleDepartment("NVL5LN1P01001")), true);
	}

	@Test
	public void testGetResponsibleLine() {
		assertEquals("VALVE".equals(qiDefectInfoManager.getResponsibleLine("NVL5LN1P01001")), true);
	}

	@Test
	public void testGetResponsibleDepartment() {
		assertEquals("ZONE01".equals(qiDefectInfoManager.getResponsibleZone("NVL5LN1P01001")), true);
	}

}
