package com.honda.galc.client.datacollection.view.action;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import com.honda.galc.service.property.PropertyService;

/**
 * @author Subu Kathiresan
 * @date Nov 14, 2018
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class,DataCollectionController.class})
public class SkipProductButtonActionTest {

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
	
	@Spy
	private SkipProductButtonAction skipProductButtonAction = new SkipProductButtonAction(clientContextMock, "NEXT VIN");
	
	@BeforeClass
	public static void classSetup() {}
	
	@Before
	public void methodSetup() throws Exception {	
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(DataCollectionController.class);
		PowerMockito.when(PropertyService.getPropertyBean(TerminalPropertyBean.class)).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(DataCollectionController.getInstance("TESTAPP_ID")).thenReturn(dataCollectionControllerMock);
		PowerMockito.when(dataCollectionControllerMock.getState()).thenReturn(dataCollectionStateMock);
		PowerMockito.when(dataCollectionStateMock.getProductId()).thenReturn("TESTPROD000000001");
		PowerMockito.when(dataCollectionStateMock.getExpectedProductId()).thenReturn("TESTPROD000000001");
		PowerMockito.when(persistenceManagerMock.getNextExpectedProductId("TESTPROD000000001")).thenReturn("TESTPROD000000002");
		PowerMockito.when(clientContextMock.getAppContext()).thenReturn(appContextMock);
		PowerMockito.when(clientContextMock.getProperty()).thenReturn(terminalPropertyBeanMock);
		PowerMockito.when(clientContextMock.getDbManager()).thenReturn(persistenceManagerMock);
		PowerMockito.when(appContextMock.getApplicationId()).thenReturn("TESTAPP_ID");
	}
	
	@After
	public void methodCleanUp() {	
		terminalPropertyBeanMock = null;
		dataCollectionControllerMock = null;
		dataCollectionStateMock = null;
		clientContextMock = null;
		appContextMock = null;
		persistenceManagerMock = null;
	}

	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 14, 2018
	 * 
	 * Tests skip single product
	 * 
	 * CHECK_EXPECTED_PRODUCT_ID = true
	 * SAVE_NEXT_PRODUCT_AS_EXPECTED_PRODUCT = true
	 * SKIP_MULTIPLE_PRODUCTS = false
	 */
	@Test
	public void doSkipProduct_single_expectedOnNextProductOn() {
		PowerMockito.doNothing().when(persistenceManagerMock).saveExpectedProductId("TESTPROD000000002");
		PowerMockito.when(clientContextMock.isCheckExpectedProductId()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSaveNextProductAsExpectedProduct()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSkipMultipleProducts()).thenReturn(false);
		
		skipProductButtonAction.doSkipProduct();
		
		Mockito.verify(skipProductButtonAction, Mockito.times(1)).skipSingleProduct();
		Mockito.verify(persistenceManagerMock, Mockito.times(1)).getNextExpectedProductId("TESTPROD000000001");
		Mockito.verify(dataCollectionStateMock, Mockito.times(1)).setExpectedProductId("TESTPROD000000002");
		Mockito.verify(persistenceManagerMock, Mockito.times(1)).saveExpectedProductId("TESTPROD000000002", "TESTPROD000000001");
	} 
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 14, 2018
	 * 
	 * Tests skip single product
	 * 
	 * CHECK_EXPECTED_PRODUCT_ID = true
	 * SAVE_NEXT_PRODUCT_AS_EXPECTED_PRODUCT = false
	 * SKIP_MULTIPLE_PRODUCTS = false
	 */
	@Test
	public void doSkipProduct_single_expectedOnNextProductOff() {
		PowerMockito.doNothing().when(persistenceManagerMock).saveExpectedProductId("TESTPROD000000001");
		PowerMockito.when(clientContextMock.isCheckExpectedProductId()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSaveNextProductAsExpectedProduct()).thenReturn(false);
		PowerMockito.when(terminalPropertyBeanMock.isSkipMultipleProducts()).thenReturn(false);
		
		skipProductButtonAction.doSkipProduct();
		
		Mockito.verify(skipProductButtonAction, Mockito.times(1)).skipSingleProduct();
		Mockito.verify(dataCollectionStateMock, Mockito.times(1)).setExpectedProductId("TESTPROD000000001");
		Mockito.verify(persistenceManagerMock, Mockito.times(1)).saveExpectedProductId("TESTPROD000000001", "TESTPROD000000001");
	} 
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 14, 2018
	 * 
	 * Tests skip single product
	 * 
	 * CHECK_EXPECTED_PRODUCT_ID = false
	 * SAVE_NEXT_PRODUCT_AS_EXPECTED_PRODUCT = false
	 * SKIP_MULTIPLE_PRODUCTS = false
	 */
	@Test
	public void doSkipProduct_single_expectedOffNextProductOff() {
		PowerMockito.doNothing().when(persistenceManagerMock).saveExpectedProductId("TESTPROD000000001");
		PowerMockito.when(clientContextMock.isCheckExpectedProductId()).thenReturn(false);
		PowerMockito.when(terminalPropertyBeanMock.isSaveNextProductAsExpectedProduct()).thenReturn(false);
		PowerMockito.when(terminalPropertyBeanMock.isSkipMultipleProducts()).thenReturn(false);
		
		skipProductButtonAction.doSkipProduct();
		
		Mockito.verify(skipProductButtonAction, Mockito.times(0)).skipSingleProduct();
		Mockito.verify(persistenceManagerMock, Mockito.times(0)).getNextExpectedProductId("TESTPROD000000001");
		Mockito.verify(dataCollectionStateMock, Mockito.times(0)).setExpectedProductId("TESTPROD000000002");
		Mockito.verify(persistenceManagerMock, Mockito.times(0)).saveExpectedProductId("TESTPROD000000002", "TESTPROD000000001");
	} 
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 14, 2018
	 * 
	 * Tests skip single product
	 * 
	 * CHECK_EXPECTED_PRODUCT_ID = false
	 * SAVE_NEXT_PRODUCT_AS_EXPECTED_PRODUCT = true
	 * SKIP_MULTIPLE_PRODUCTS = false
	 */
	@Test
	public void doSkipProduct_single_expectedOffNextProductOn() {
		PowerMockito.doNothing().when(persistenceManagerMock).saveExpectedProductId("TESTPROD000000002");
		PowerMockito.when(clientContextMock.isCheckExpectedProductId()).thenReturn(false);
		PowerMockito.when(terminalPropertyBeanMock.isSaveNextProductAsExpectedProduct()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSkipMultipleProducts()).thenReturn(false);
		
		skipProductButtonAction.doSkipProduct();
		
		Mockito.verify(skipProductButtonAction, Mockito.times(0)).skipSingleProduct();
		Mockito.verify(persistenceManagerMock, Mockito.times(0)).getNextExpectedProductId("TESTPROD000000001");
		Mockito.verify(dataCollectionStateMock, Mockito.times(0)).setExpectedProductId("TESTPROD000000001");
		Mockito.verify(persistenceManagerMock, Mockito.times(0)).saveExpectedProductId("TESTPROD000000001", "TESTPROD000000001");
	} 
	
	/**
	 * @author Subu Kathiresan, HMIN
	 * @date Nov 14, 2018
	 * 
	 * Tests invocation of skip multiple products
	 * 
	 * CHECK_EXPECTED_PRODUCT_ID = true
	 * SAVE_NEXT_PRODUCT_AS_EXPECTED_PRODUCT = true
	 * SKIP_MULTIPLE_PRODUCTS = true
	 */
	@Test
	public void doSkipProduct_multiple_expectedOnNextProductOn() {
		PowerMockito.doNothing().when(skipProductButtonAction).skipMultipleProducts();
		PowerMockito.when(clientContextMock.isCheckExpectedProductId()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSkipMultipleProducts()).thenReturn(true);
		PowerMockito.when(terminalPropertyBeanMock.isSaveNextProductAsExpectedProduct()).thenReturn(true);
		
		skipProductButtonAction.doSkipProduct();
		
		Mockito.verify(skipProductButtonAction, Mockito.times(1)).skipMultipleProducts();
	} 
}
