package com.honda.galc.service.productionschedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class, ServiceUtil.class, PropertyService.class,BuckLoadProductionScheduleServiceImpl.class})
public class BuckLoadProductionScheduleServiceImplTest {

	DefaultDataContainer dc = new DefaultDataContainer();
	
	@Mock
	private static ExpectedProductDao expectedProductDaoMock = PowerMockito.mock(ExpectedProductDao.class);
	private static BuckLoadProductionScheduleServicePropertyBean mockBean = PowerMockito.mock(BuckLoadProductionScheduleServicePropertyBean.class);

	@InjectMocks
	BuckLoadProductionScheduleServiceImpl buckLoadService = new BuckLoadProductionScheduleServiceImpl();

	@Before
	public void setUp() throws Exception {		
		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(ServiceUtil.class);
		PowerMockito.mockStatic(PropertyService.class);
		
		PowerMockito.when(PropertyService.getPropertyBean(BuckLoadProductionScheduleServicePropertyBean.class, "TestPP")).thenReturn(mockBean);

		PowerMockito.when(mockBean.getPlanCodeTag()).thenReturn("PLAN_CODE");
		PowerMockito.when(mockBean.getLastVINTag()).thenReturn("LAST_VIN");
		PowerMockito.when(mockBean.getVINTag()).thenReturn("VIN");
		PowerMockito.when(mockBean.getInfoCodeTag()).thenReturn("ALC_INFO_CODE");
		
		
		
	}
	
	@Test
	public void getNextProductScheduleNoLastVin() {
		
		dc =  new DefaultDataContainer();
		dc.put(DataContainerTag.PROCESS_POINT_ID, "TestPP");
		dc.put(DataContainerTag.PLAN_CODE, "PMC 04PA");
		
		//No product Id
		DataContainer output = buckLoadService.getNextProductSchedule(dc);
		
		assertNotNull(output);
		assertEquals(output.get("ALC_INFO_CODE"), BuckLoadInfoCode.INVALID_SN.getInfoCode());
		
	}

	@Test
	public void getNextProductScheduleNoPlanCode() {
		
		dc =  new DefaultDataContainer();
		dc.put(DataContainerTag.PROCESS_POINT_ID, "TestPP");
		
		//No product Id
		DataContainer output = buckLoadService.getNextProductSchedule(dc);
		
		assertNotNull(output);
		assertEquals(output.get("ALC_INFO_CODE"), BuckLoadInfoCode.INVALID_REQUEST.getInfoCode());
		
	}
	

	
	@Test
	public void getNextProductScheduleNoSchedule() {
		
		dc =  new DefaultDataContainer();
		dc.put(DataContainerTag.PROCESS_POINT_ID, "TestPP");
		dc.put(DataContainerTag.PLAN_CODE, "PMC 04PA");
		dc.put(DataContainerTag.LAST_VIN, "VIN01");
		PowerMockito.when(mockBean.isValidateProductId()).thenReturn(false);
		
		//No product Id
		DataContainer output = buckLoadService.getNextProductSchedule(dc);
		
		assertNotNull(output);
		assertEquals(output.get("ALC_INFO_CODE"), BuckLoadInfoCode.UNKNOWN_ERROR.getInfoCode());
		
	}
	
	
	
	
	@Test
	public void getProductScheduleNoVin() {
		
		dc =  new DefaultDataContainer();
		dc.put(DataContainerTag.PROCESS_POINT_ID, "TestPP");
		dc.put(DataContainerTag.PLAN_CODE, "PMC 04PA");
		dc.put(DataContainerTag.LAST_VIN, "VIN01");
		//No product Id
		DataContainer output = buckLoadService.getProductSchedule(dc);
		
		assertNotNull(output);
		assertEquals(output.get("ALC_INFO_CODE"), BuckLoadInfoCode.INVALID_SN.getInfoCode());
		
	}


}
