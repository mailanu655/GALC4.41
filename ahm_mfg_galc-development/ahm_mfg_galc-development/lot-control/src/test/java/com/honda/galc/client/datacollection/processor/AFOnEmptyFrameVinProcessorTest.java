package com.honda.galc.client.datacollection.processor;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.LotControlPersistenceManager;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class,DataCollectionController.class,ServiceFactory.class })
public class AFOnEmptyFrameVinProcessorTest {

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
	private ProcessPointDao processPointDaoMock = PowerMockito.mock(ProcessPointDao.class);
	
	@Mock
	private FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
	
	@Mock
	private SequenceDao sequenceDaoMock = PowerMockito.mock(SequenceDao.class);
	
	@Mock
	private ProductCarrierDao carrierDaoMock = PowerMockito.mock(ProductCarrierDao.class);

	
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
		PowerMockito.when(dataCollectionControllerMock.getProperty()).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(persistenceManagerMock.getNextExpectedProductId("TESTPROD000000001")).thenReturn("TESTPROD000000002");
		PowerMockito.when(clientContextMock.getAppContext()).thenReturn(appContextMock);
		PowerMockito.when(clientContextMock.getProperty()).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(clientContextMock.getProcessPointId()).thenReturn("processPointId");
		PowerMockito.when(clientContextMock.getDbManager()).thenReturn(persistenceManagerMock);
		PowerMockito.when(appContextMock.getApplicationId()).thenReturn("TESTAPP_ID");
		PowerMockito.when(terminalPropertyBeanMock.getSequenceName()).thenReturn("AFON_SEQUENCE");
		PowerMockito.when(terminalPropertyBeanMock.getEmptyCarrierName()).thenReturn("EMPTY_CARRIER");
		PowerMockito.when(sequenceDaoMock.getNextSequence(Matchers.anyString())).thenReturn(getSequence());
		PowerMockito.when(frameDaoMock.findByKey("TESTPROD000000001")).thenReturn(getFrame("TESTPROD000000001","kdLotNumber1","productionLot1"));
		PowerMockito.when(frameDaoMock.findByKey("EMPTY_CARRIER")).thenReturn(null);
		PowerMockito.when(processPointDaoMock.findByKey(Matchers.anyString())).thenReturn(getProcessPoint());
		when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDaoMock);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
		when(ServiceFactory.getDao(SequenceDao.class)).thenReturn(sequenceDaoMock);	
		when(ServiceFactory.getDao(ProductCarrierDao.class)).thenReturn(carrierDaoMock);	
	}
	
	private Sequence getSequence() {
		Sequence sequence = new Sequence();
		sequence.setSequenceIdName("AFON_SEQUENCE");
		sequence.setCurrentSeq(10);
		sequence.setStartSeq(1);
		sequence.setEndSeq(99999);
		sequence.setIncrementValue(1);
		
		return sequence;
	}
	
	private Frame getFrame(String productId, String kdLot,String prodLot) {
		Frame frame = new Frame();
		frame.setProductId(productId);
		frame.setProductionLot(prodLot);
		frame.setKdLotNumber(kdLot);
		return frame;
	}
	
	private ProcessPoint getProcessPoint() {
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("processPointId");
		processPoint.setCurrentKdLot("kdLotNumber1");
		processPoint.setCurrentProductionLot("productionLot1");
		processPoint.setLineId("LINE");
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
		processPointDaoMock = null;
		frameDaoMock = null;
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Apr 03,2019
	 * Tests setSeqNum method in AFOnEmptyFrameVinProcessor.java
	 * 
	 */
	@Test
	public void setSeqNumTest() {
		AFOnEmptyFrameVinProcessor aFOnEmptyFrameVinProcessor = new AFOnEmptyFrameVinProcessor(clientContextMock);
		aFOnEmptyFrameVinProcessor.product = new ProductBean();
		aFOnEmptyFrameVinProcessor.product.setProductId("EMPTY_CARRIER");
		
		assertTrue(aFOnEmptyFrameVinProcessor.setSeqNum());
	}
	
	/**
	 * @author Ambica Gawarla
	 * @date Apr 03,2019
	 * Tests isEmptyCarrier method in AFOnEmptyFrameVinProcessor.java
	 * 
	 */
	@Test
	public void isEmptyCarrierTest() {
		AFOnEmptyFrameVinProcessor aFOnEmptyFrameVinProcessor = new AFOnEmptyFrameVinProcessor(clientContextMock);
		aFOnEmptyFrameVinProcessor.product.setProductId("EMPTY_CARRIER");
		
		assertTrue(aFOnEmptyFrameVinProcessor.isEmptyCarrier(aFOnEmptyFrameVinProcessor.product.getProductId()));
	}
	
}

