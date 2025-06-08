package com.honda.galc.client.datacollection.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlPersistenceManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductSequenceDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.dto.ProcessProductDto;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Straggler;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class,DataCollectionController.class,ServiceFactory.class })
public class PaintOffFrameVinProcessorTest {

	@Mock
	private TerminalPropertyBean terminalPropertyBeanMock = PowerMockito.mock(TerminalPropertyBean.class);
	
	@Mock
	private DataCollectionController dataCollectionControllerMock = PowerMockito.mock(DataCollectionController.class);
	
	@Mock
	private DataCollectionState dataCollectionStateMock = PowerMockito.mock(DataCollectionState.class);
	
	@Mock
	private ClientContext clientContextMock = PowerMockito.mock(ClientContext.class);
	
	@Mock
	private ApplicationContext appContextMock = PowerMockito.mock(ApplicationContext.class);

	@Mock
	private LotControlPersistenceManager persistenceManagerMock = PowerMockito.mock(LotControlPersistenceManager.class);
	
	@Mock
	private PreProductionLotDao productionLotDaoMock = PowerMockito.mock(PreProductionLotDao.class);
	
	@Mock
	private ProcessPointDao processPointDaoMock = PowerMockito.mock(ProcessPointDao.class);
	
	@Mock
	private FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
	
	@Mock
	private ProductSequenceDao productSequenceDaoMock = PowerMockito.mock(ProductSequenceDao.class);
	
	@Mock
	private StragglerDao stragglerDaoMock = PowerMockito.mock(StragglerDao.class);
	
	@BeforeClass
	public static void classSetup() {}
	
	@Before
	public void methodSetup() throws Exception {	
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(DataCollectionController.class);
		PowerMockito.mockStatic(ServiceFactory.class);  
		PowerMockito.when(PropertyService.getPropertyBean(TerminalPropertyBean.class)).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(DataCollectionController.getInstance("TESTAPP_ID")).thenReturn(dataCollectionControllerMock);
		PowerMockito.when(dataCollectionControllerMock.getState()).thenReturn(dataCollectionStateMock);
		PowerMockito.when(dataCollectionStateMock.getProductId()).thenReturn("TESTPROD000000001");
		PowerMockito.when(dataCollectionStateMock.getExpectedProductId()).thenReturn("TESTPROD000000001");
		PowerMockito.when(persistenceManagerMock.getNextExpectedProductId("TESTPROD000000001")).thenReturn("TESTPROD000000002");
		PowerMockito.when(clientContextMock.getAppContext()).thenReturn(appContextMock);
		PowerMockito.when(clientContextMock.getProperty()).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(clientContextMock.getProcessPointId()).thenReturn("processPointId");
		PowerMockito.when(clientContextMock.getDbManager()).thenReturn(persistenceManagerMock);
		PowerMockito.when(appContextMock.getApplicationId()).thenReturn("TESTAPP_ID");
		PowerMockito.when(frameDaoMock.findByKey("TESTPROD000000001")).thenReturn(getFrame("TESTPROD000000001","kdLotNumber1","productionLot1"));
		PowerMockito.when(frameDaoMock.findByKey("TESTPROD000000002")).thenReturn(getFrame("TESTPROD000000002","kdLotNumber2","productionLot2"));
		PowerMockito.when(productionLotDaoMock.findByKey("productionLot1")).thenReturn( getPreProductionLot("productionLot1","kdLotNumber1",2));
		PowerMockito.when(productionLotDaoMock.findByKey("productionLot2")).thenReturn( getPreProductionLot("productionLot2","kdLotNumber2",3));
		PowerMockito.when(processPointDaoMock.findByKey("processPointId")).thenReturn( getProcessPoint());
		PowerMockito.when(PropertyService.getProperty("processPointId", TagNames.PLAN_CODE.name(), null)).thenReturn("ELP034");
		PowerMockito.when(frameDaoMock.getScheduleQuantity("TESTPROD000000001", "ELP034")).thenReturn(30);
		PowerMockito.when(frameDaoMock.getPassQuantity("TESTPROD000000001", "processPointId")).thenReturn(20);
		PowerMockito.when(productSequenceDaoMock.findAllAlreadyProcessed("processPointId", 1)).thenReturn(getProcessProductDTOList("TESTPROD000000001"));
		PowerMockito.when(stragglerDaoMock.findStragglerProductList("TESTPROD000000001", "processPointId")).thenReturn(new ArrayList<Straggler>());
		when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDaoMock);
		when(ServiceFactory.getDao(PreProductionLotDao.class)).thenReturn(productionLotDaoMock);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
		when(ServiceFactory.getDao(ProductSequenceDao.class)).thenReturn(productSequenceDaoMock);
		when(ServiceFactory.getDao(StragglerDao.class)).thenReturn(stragglerDaoMock);
	}
	
	private List<ProcessProductDto> getProcessProductDTOList(String productId) {
		List<ProcessProductDto> processProductDtoList = new ArrayList<ProcessProductDto>();
		ProcessProductDto processproductDto = new ProcessProductDto();
		processproductDto.setVin(productId);
		
		processProductDtoList.add(processproductDto);
		return processProductDtoList;
	}

	private Frame getFrame(String productId, String kdLot,String prodLot) {
		Frame frame = new Frame();
		frame.setProductId(productId);
		frame.setProductionLot(prodLot);
		frame.setKdLotNumber(kdLot);
		return frame;
	}
	
	private PreProductionLot getPreProductionLot(String productionLot,String kdLot, int inc) {
		PreProductionLot preProductionLot = new PreProductionLot();
		preProductionLot.setProductionLot(productionLot);
		preProductionLot.setKdLotNumber(kdLot);
		preProductionLot.setNextProductionLot("productionLot"+inc);
		return preProductionLot;
	}
	
	private ProcessPoint getProcessPoint() {
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("processPointId");
		processPoint.setCurrentKdLot("kdLotNumber1");
		processPoint.setCurrentProductionLot("productionLot1");
		return processPoint;
	}


	@After
	public void methodCleanUp() {	
		terminalPropertyBeanMock = null;
		dataCollectionControllerMock = null;
		dataCollectionStateMock = null;
		clientContextMock = null;
		appContextMock = null;
		persistenceManagerMock = null;
		productionLotDaoMock =  null;
		processPointDaoMock = null;
		frameDaoMock = null;
		productSequenceDaoMock = null;
		stragglerDaoMock = null;
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 14,2019
	 * Tests isLotInCorrectOrderAsPerSchedule method in PaintOffFrameVinProcessor.java
	 * 
	 */
	@Test
	public void isLotInCorrectOrderAsPerScheduleTest() {
		PaintOffFrameVinProcessor paintOffFrameVinProcessor = new PaintOffFrameVinProcessor(clientContextMock);
		paintOffFrameVinProcessor.product.setProductId("TESTPROD000000001");
		
		assertFalse(paintOffFrameVinProcessor.isLotInCorrectOrderAsPerSchedule());
		
		paintOffFrameVinProcessor.product.setProductId("TESTPROD000000002");
		assertTrue(paintOffFrameVinProcessor.isLotInCorrectOrderAsPerSchedule());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 14,2019
	 * Tests isPreviousLotCompletede method in PaintOffFrameVinProcessor.java
	 * 
	 */
	@Test
	public void isPreviousLotCompletedTest() {
		PaintOffFrameVinProcessor paintOffFrameVinProcessor = new PaintOffFrameVinProcessor(clientContextMock);
		paintOffFrameVinProcessor.product.setProductId("TESTPROD000000001");
		paintOffFrameVinProcessor.processPoint = getProcessPoint();
		assertFalse(paintOffFrameVinProcessor.isPreviousLotCompleted());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 14,2019
	 * Tests getUprocessedProductCnt() method in PaintOffFrameVinProcessor.java
	 * 
	 */
	@Test
	public void getUprocessedProductCntTest() {
		PaintOffFrameVinProcessor paintOffFrameVinProcessor = new PaintOffFrameVinProcessor(clientContextMock);
		assertEquals(paintOffFrameVinProcessor.getUprocessedProductCnt(),"10");
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 14,2019
	 * Tests isSameLot method in PaintOffFrameVinProcessor.java
	 * 
	 */
	@Test
	public void isSameLotTest() {
		PaintOffFrameVinProcessor paintOffFrameVinProcessor = new PaintOffFrameVinProcessor(clientContextMock);
		paintOffFrameVinProcessor.product.setProductId("TESTPROD000000001");
		paintOffFrameVinProcessor.processPoint = getProcessPoint();
		
		assertTrue(paintOffFrameVinProcessor.isSameLot());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Jan 14,2019
	 * Tests isStraggler method in PaintOffFrameVinProcessor.java
	 * 
	 */
	@Test
	public void isStragglerTest() {
		PaintOffFrameVinProcessor paintOffFrameVinProcessor = new PaintOffFrameVinProcessor(clientContextMock);
		paintOffFrameVinProcessor.product.setProductId("TESTPROD000000001");
		paintOffFrameVinProcessor.processPoint = getProcessPoint();
		
		assertFalse(paintOffFrameVinProcessor.isStraggler());
	}
}

