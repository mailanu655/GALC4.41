package com.honda.galc.service.defect.scrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.common.exception.InputValidationException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.ReuseProductResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.qics.DefectResult;
import com.honda.galc.entity.qics.ReuseProductResult;
import com.honda.galc.entity.qics.ReuseProductResultId;
import com.honda.galc.property.UnscrapPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.notification.service.IProductScrapNotification;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProductTypeUtil.class, ServiceFactory.class, PropertyService.class})
public class UnscrapServiceImplTest {
	@Mock
	private DiecastDao diecastDaoMock = PowerMockito.mock(DiecastDao.class);

	@Mock
	private ProductDao productDaoMock = PowerMockito.mock(ProductDao.class);

	@Mock
	private UnscrapPropertyBean unscrapPropertyBeanMock = PowerMockito.mock(UnscrapPropertyBean.class);

	@Mock
	private DefectResultDao defectResultDaoMock = PowerMockito.mock(DefectResultDao.class);

	@Mock
	ReuseProductResultDao reuseProductResultDaoMock = PowerMockito.mock(ReuseProductResultDao.class);

	@Mock
	ExceptionalOutDao exceptionalOutDaoMock = PowerMockito.mock(ExceptionalOutDao.class);

	@Mock
	ProcessPointDao processPointDaoMock = PowerMockito.mock(ProcessPointDao.class);

	@InjectMocks
	private UnscrapServiceImpl unscrapService;
	
	@Mock
	IProductScrapNotification notificationServiceMock;

	@Before
	public void setUp() throws Exception {
		unscrapService = new UnscrapServiceImpl();

		PowerMockito.mockStatic(ProductTypeUtil.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(PropertyService.class);

		when(ProductTypeUtil.createProduct(any(String.class), any(String.class))).thenCallRealMethod();


		MockitoAnnotations.initMocks(this);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test unscrapProduct method when the product in a type that cannot be scrapped
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT}
	 */
	@Test
	public void unscrapProduct_InvalidProductForScrap() {
		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B ", "AFON");

		when(ProductTypeUtil.getProductDao("FRAME")).thenReturn(productDaoMock);
		when(((ProductDao) productDaoMock).findByKey("5FNRL6H78KB117803")).thenReturn(product);

		DefaultDataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("5FNRL6H78KB117803");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		unscrapService.parseDataContainer(data);
		DataContainer returnDc = unscrapService.unscrapProduct(data);

		assertEquals("0", returnDc.get(TagNames.REQUEST_RESULT.name()));
	}
	
	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test unscrapProduct method when the block is valid and is unscrapped
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT}
	 */
	@Test
	public void unscrapProduct_BlockShouldUnscrap() {
		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "LINE57");
		String[] scrapLine = {"LINE57"};
		product.setDefectStatus(DefectStatus.SCRAP);
		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(ServiceFactory.getDao(ReuseProductResultDao.class)).thenReturn(reuseProductResultDaoMock);
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);
		when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDaoMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);
		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(null);
		when(unscrapPropertyBeanMock.getScrapLines()).thenReturn(scrapLine);
		when(processPointDaoMock.findLastTrackingStatus("C9G9612296U","'','LINE57'","Block")).thenReturn("BLOCKON");
		
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(exceptionalOutList);
		when(ServiceFactory.getNotificationService(IProductScrapNotification.class)).thenReturn(notificationServiceMock);

		DefaultDataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.parseDataContainer(data);
		ReuseProductResult reuseProductResult = createReuseProductResult("C9G9612296U", "VF012345", "No Good Part");
		when(reuseProductResultDaoMock.save((ReuseProductResult) any(Object.class))).thenReturn(reuseProductResult);
		unscrapService.setLogger();
		DataContainer returnDc = unscrapService.unscrapProduct(data);

		assertEquals("1", returnDc.get(TagNames.REQUEST_RESULT.name()));
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData places the correct data from the data container into the member variables.
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345}
	 */
	@Test
	public void parseDataContainer_correctData() {
		DataContainer data = new DefaultDataContainer();

		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		unscrapService.parseDataContainer(data);

		assertTrue(unscrapService.getProductIdList().contains("C9G9612296U"));
		assertTrue(unscrapService.getProductIdList().size() == 1);
		assertEquals(unscrapService.getProductType(), "BLOCK");
		assertEquals(unscrapService.getUnscrapReason(), "Can't be fixed");
		assertEquals(unscrapService.getAssociateId(), "VF012345");
		assertEquals(unscrapService.getApplicationId(), "APPLICATIONID");
		assertEquals(unscrapService.getProcessPointId(), "PROCESSPOINT");
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when all data is in the member variables
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345}
	 */
	@Test
	public void verifyInputData_correctData() {
		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();

		assertTrue(unscrapService.getProductIdList().contains("C9G9612296U"));
		assertTrue(unscrapService.getProductIdList().size() == 1);
		assertEquals(unscrapService.getProductType(), "BLOCK");
		assertEquals(unscrapService.getUnscrapReason(), "Can't be fixed");
		assertEquals(unscrapService.getAssociateId(), "VF012345");
		assertEquals(unscrapService.getApplicationId(), "APPLICATIONID");
		assertEquals(unscrapService.getProcessPointId(), "PROCESSPOINT");
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the product Id is not contained in the data container
	 * {productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyInputData_NullProductId() {
		DataContainer data = new DefaultDataContainer();
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the unscrap reason is not contained in the data container
	 * {productId = C9G9612296U}{productType = BLOCK}{associateId = VF012345}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyInputData_NullUnscrapReason() {
		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the unscrap reason is not contained in the data container but it not required
	 * {productId = C9G9612296U}{productType = BLOCK}{associateId = VF012345}
	 */
	@Test
	public void verifyInputData_NullUnscrapReasonnotRequired() {
		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(false);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();

		assertTrue(unscrapService.getProductIdList().contains("C9G9612296U"));
		assertTrue(unscrapService.getProductIdList().size() == 1);
		assertEquals(unscrapService.getProductType(), "BLOCK");
		assertNull(unscrapService.getUnscrapReason());
		assertEquals(unscrapService.getAssociateId(), "VF012345");
		assertEquals(unscrapService.getApplicationId(), "APPLICATIONID");
		assertEquals(unscrapService.getProcessPointId(), "PROCESSPOINT");
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the associate id is not contained in the data container
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyInputData_NullAssociateId() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the product type is not contained in the data container
	 * {productId = C9G9612296U}{productType = BLOCK}{productType = BLOCK}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyInputData_NullProductType() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the process point id attribute is null
	 * {productId = C9G9612296U}{productType = BLOCK}{productType = BLOCK}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyInputData_NullProcessPointId() {

		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyInputData when the application id attribute is null and the process point id attribure
	 * is not null
	 * {productId = C9G9612296U}{productType = BLOCK}{productType = BLOCK}
	 */
	@Test
	public void verifyInputData_NullApplicationIdWithProcessPointId() {
		when(PropertyService.getPropertyBean(UnscrapPropertyBean.class)).thenReturn(unscrapPropertyBeanMock);
		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		when(unscrapPropertyBeanMock.isUnscrapReasonRequired()).thenReturn(true);

		unscrapService.parseDataContainer(data);

		unscrapService.verifyInputData();
		assertEquals(unscrapService.getApplicationId(), "PROCESSPOINT");
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test verifyApplicationId when the application id attribute is null and the process point id attribure
	 * is null
	 * {productId = C9G9612296U}{productType = BLOCK}{productType = BLOCK}
	 */
	@Test(expected = InputValidationException.class)
	public void verifyApplicationId_NullApplicationIdAndNullProcessPoint() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		unscrapService.verifyApplicationId();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 *
	 *Test initReturnDc to verify proper initialization
	 * 
	 * {productId = C9G9612296U}
	 */
	@Test
	public void initReturnDc_verifyReturnDcValues() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		unscrapService.parseDataContainer(data);

		unscrapService.initReturnDc();

		DataContainer returnDc = unscrapService.getReturnDataContainer();
		assertEquals("VF012345", returnDc.get(TagNames.ASSOCIATE_ID.name()));
		assertEquals("PROCESSPOINT", returnDc.get(TagNames.PROCESS_POINT_ID.name()));
		assertEquals("APPLICATIONID", returnDc.get(TagNames.APPLICATION_ID.name()));
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test validateProduct with a diecast product when the product does not exist
	 * {productId = C9G9612296U}{productType = BLOCK}
	 */
	@Test
	public void isValidProduct_DiecastProductNotFound() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("LG96141440O")).thenReturn(null);
		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test validateProduct with a diecast product is found.
	 * {productId = C9G9612296U}{productType = BLOCK}
	 */
	@Test
	public void isValidProduct_DiecastProductNotScrapped() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "BLOCK1ON");
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);
		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);
		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
		assertTrue(unscrapService.getProductList().isEmpty());
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test validateProduct with a diecast product is found.
	 * {productId = C9G9612296U}{productType = BLOCK}
	 */
	@Test
	public void isValidProduct_DiecastProductScrapped() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "BLOCK1ON");
		product.setDefectStatus(DefectStatus.SCRAP);
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(exceptionalOutList);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();
		boolean isValid = unscrapService.isValidProducts();

		assertTrue(isValid);
		assertNotNull(unscrapService.getProductList().get(0));
		assertEquals("C9G9612296U", unscrapService.getProductList().get(0).getProductId());
		Block blockProduct = (Block) unscrapService.getProductList().get(0);
		assertEquals("LG96141440O",(blockProduct.getMcSerialNumber()));
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test validateProduct with a FRAME product when the product does not exist
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}
	 */
	@Test
	public void isProductValid_FrameProductNotFound() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		when(ProductTypeUtil.getProductDao("FRAME")).thenReturn(productDaoMock);
		when(((ProductDao) productDaoMock).findByKey("5FNRL6H78KB117803")).thenReturn(null);
		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);

		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test validateProduct with a FRAME product is found.
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}
	 */
	@Test
	public void isValidProduct_FrameNotScrapped() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("5FNRL6H78KB117803");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B ", "AFON");
		when(ProductTypeUtil.getProductDao("FRAME")).thenReturn(productDaoMock);
		when(((ProductDao) productDaoMock).findByKey("5FNRL6H78KB117803")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);
		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
		assertTrue(unscrapService.getProductList().isEmpty());
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a diecast product that is scrapped.
	 * {productId = C9G9612296U}{productType = BLOCK}{DefectStatus = SCRAP}
	 */
	@Test
	public void isProductScrapped_DiecastDefectStatusScrap() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "BLOCK1ON");
		product.setDefectStatus(DefectStatus.SCRAP);
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(exceptionalOutList);

		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertTrue("[Actual : Not Scrapped] [Expected : Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a diecast product that not scrapped.
	 * {productId = C9G9612296U}{productType = BLOCK}{DefectStatus = FIXED}
	 */
	@Test
	public void isProductScrapped_DiecastDefectStatusNotScrapped() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "BLOCK1ON");
		product.setDefectStatus(DefectStatus.FIXED);
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);
		unscrapService.setLogger();
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse("[Actual : Scrapped] [Expected : Not Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a diecast product that has a null defect status
	 * {productId = C9G9612296U}{productType = BLOCK}{DefectStatus = null}
	 */
	@Test
	public void isProductScrapped_DiecastDefectStatusNull() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createBlockProduct("C9G9612296U", "LG96141440O", "BLOCK", "RV0", "BLOCK1ON");
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);

		unscrapService.setLogger();
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse("[Actual : Scrapped] [Expected : Not Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a frame product that is set as scrapped but Frame products are unscrappable.
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}{DefectStatus = SCRAP}
	 */
	@Test
	public void isProductScrapped_FrametDefectStatusScrap() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("5FNRL6H78KB117803");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B", "AFON");
		product.setDefectStatus(DefectStatus.SCRAP);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(new ArrayList<ExceptionalOut>());

		unscrapService.setLogger();
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse("[Actual : Scrapped] [Expected : Not Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a frame product that not scrapped.
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}{DefectStatus = FIXED}
	 */
	@Test
	public void isProductScrapped_FrametDefectStatusNotScrapped() {
		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B", "AFON");
		product.setDefectStatus(DefectStatus.FIXED);

		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(new ArrayList<ExceptionalOut>());

		DataContainer data = new DefaultDataContainer();
		data.put(TagNames.PRODUCT_ID.name(), "5FNRL6H78KB117803");
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.setLogger();
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse("[Actual : Scrapped] [Expected : Not Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped with a frame product that has a null defect status
	 * {productId = 5FNRL6H78KB117803}{productType = FRAME}{DefectStatus = null}
	 */
	@Test
	public void isProductScrapped_FrameDefectStatusNull() {
		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B", "AFON");
		when(ProductTypeUtil.getProductDao("FRAME")).thenReturn(productDaoMock);
		when(((ProductDao) productDaoMock).findByKey("5FNRL6H78KB117803")).thenReturn(product);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("5FNRL6H78KB117803"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);
		unscrapService.setLogger();
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse("[Actual : Scrapped] [Expected : Not Srapped]", isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test calculateDefectStatus when no defects fpr the product id are null
	 * {productId = C9G9612296U}{productType = C9G9612296U}{DefectStatus = null} {
	 */
	@Test
	public void calculateDefectStatus_NullDefectsForProductId() {
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);
		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(null);

		DefectStatus defectStatus = unscrapService.calculateDefectStatus("C9G9612296U");
		assertEquals(DefectStatus.REPAIRED.getId(), defectStatus.getId());
	}
	
	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test calculateDefectStatus when no defects are found for the product id
	 * {productId = C9G9612296U}{productType = C9G9612296U}{DefectStatus = []}
	 */
	@Test
	public void calculateDefectStatus_EmptyDefectListForProductId() {
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);
		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(new ArrayList<DefectResult>());

		DefectStatus defectStatus = unscrapService.calculateDefectStatus("C9G9612296U");
		assertEquals(DefectStatus.REPAIRED.getId(), defectStatus.getId());
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test calculateDefectStatus when the product has a defect that has been repaired
	 * {productId = C9G9612296U}{productType = C9G9612296U}{DefectStatus = REPAIRED}
	 */
	@Test
	public void calculateDefectStatus_OneRepairedDefectFoundRepaired() {
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);

		List<DefectResult> defectResultList = new ArrayList<DefectResult>();
		defectResultList.add(createDefectResult(DefectStatus.REPAIRED));

		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(defectResultList);

		DefectStatus defectStatus = unscrapService.calculateDefectStatus("C9G9612296U");
		assertEquals(DefectStatus.REPAIRED.getId(), defectStatus.getId());
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test calculateDefectStatus when a defect for the product has been scrapped
	 * {productId = C9G9612296U}{productType = C9G9612296U}{DefectStatus = NON_REPAIRIABLE} {
	 */
	@Test
	public void calculateDefectStatus_OneRepairedDefectFoundScrap() {
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);

		List<DefectResult> defectResultList = new ArrayList<DefectResult>();
		defectResultList.add(createDefectResult(DefectStatus.SCRAP));

		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(defectResultList);

		DefectStatus defectStatus = unscrapService.calculateDefectStatus("C9G9612296U");
		assertEquals(DefectStatus.OUTSTANDING.getId(), defectStatus.getId());
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test calculateDefectStatus when the product has a repaired deffect and a scrap defect
	 * {productId = C9G9612296U}{productType = C9G9612296U}{DefectStatus = REPAIRED, SCRAP}
	 */
	@Test
	public void calculateDefectStatus_MultipleRepairedDefectFoundRepairedScrap() {
		when(ServiceFactory.getDao(DefectResultDao.class)).thenReturn(defectResultDaoMock);

		List<DefectResult> defectResultList = new ArrayList<DefectResult>();
		defectResultList.add(createDefectResult(DefectStatus.SCRAP));
		defectResultList.add(createDefectResult(DefectStatus.REPAIRED));

		when(defectResultDaoMock.findAllByProductId("C9G9612296U")).thenReturn(defectResultList);

		DefectStatus defectStatus = unscrapService.calculateDefectStatus("C9G9612296U");
		assertEquals(DefectStatus.OUTSTANDING.getId(), defectStatus.getId());
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test createReuseProductResult to verify the object is proppery created and set
	 * {productId = C9G9612296U}{Unscrap Reason = No Good Part}{Associate Id = VF012345}
	 */
	@Test
	public void createReuseProductResult_createObject() {
		ReuseProductResult reuseProductResult = createReuseProductResult("C9G9612296U", "VF012345", "No Good Part");

		unscrapService.setUnscrapReason("No Good Part");
		unscrapService.setAssociateId("VF012345");
		reuseProductResult = unscrapService.createReuseProductResult("C9G9612296U");

		assertEquals("C9G9612296U", reuseProductResult.getId().getProductId());
		assertNotNull(reuseProductResult.getId().getActualTimestamp());
		assertEquals("No Good Part", reuseProductResult.getReuseVinReason());
		assertEquals("VF012345", reuseProductResult.getAssociateNo());
		assertNotNull(reuseProductResult.getProductionDate());
	}
	
	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test saveReuseProductResult to verify method call to save the ReuseProdcutResult
	 * {productId = C9G9612296U}{Unscrap Reason = No Good Part}{Associate Id = VF012345}
	 */
	@Test
	public void saveReuseProductResult_VerifySaveMethodCalled() {
		when(ServiceFactory.getDao(ReuseProductResultDao.class)).thenReturn(reuseProductResultDaoMock);
		ReuseProductResult reuseProductResult = createReuseProductResult("C9G9612296U", "VF012345", "No Good Part");
		when(reuseProductResultDaoMock.save((ReuseProductResult) any(Object.class))).thenReturn(reuseProductResult);

		unscrapService.setLogger();
		unscrapService.saveReuseProductResult(reuseProductResult);

		verify(reuseProductResultDaoMock, times(1)).save(reuseProductResult);
	}

	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test updateDefectStatus to verify method call to update defect status in the product table
	 * {productId = C9G9612296U}{product type = BLOCK}{defect status = OUTSTANDING}
	 */
	@Test
	public void updateDefectStatus_VerifyUpdateMethodCalled() {
		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);

		DefectStatus defectStatus = DefectStatus.OUTSTANDING;

		unscrapService.setLogger();
		unscrapService.setProductType("BLOCK");
		unscrapService.updateDefectStatus("C9G9612296U", defectStatus);

		verify(diecastDaoMock, times(1)).updateDefectStatus("C9G9612296U", defectStatus);
	}
	
	/**
	 * @author Bradley Brown
	 * @date July 8, 2019
	 * 
	 * Test deleteExceptionalOutForProduct to verify method call delete exceptional out records for product
	 * {productId = C9G9612296U} 
	 */
	@Test
	public void deleteExceptionalOutForProduct_VerifyDeleteMethodCalled() {
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);

		unscrapService.setLogger();
		unscrapService.deleteExceptionalOutForProduct("C9G9612296U");

		verify(exceptionalOutDaoMock, times(1)).deleteAllByProductId("C9G9612296U");
	}

	private BaseProduct createBlockProduct(String productId, String mcSerialNumber, String productType,
			String model, String trackingStatus) {
		Block product = new Block(productId);
		product.setMcSerialNumber(mcSerialNumber);
		product.setModelCode(model);
		product.setTrackingStatus(trackingStatus);
		return product;
	}

	private BaseProduct createFrameProduct(String productId, String productType, String productSpecCode, String trackingStatus) {
		Frame product = new Frame(productId);
		product.setProductSpecCode(productSpecCode);
		product.setTrackingStatus(trackingStatus);
		return product;
	}

	private DefectResult createDefectResult(DefectStatus defectStatus) {
		DefectResult defectResult = new DefectResult();
		defectResult.setDefectStatus(defectStatus);
		return defectResult;
	}

	private ReuseProductResult createReuseProductResult(String productId, String associateId, String unscrapReason) {
		long time = System.currentTimeMillis();
		ReuseProductResult reuseProductResult = new ReuseProductResult();
		ReuseProductResultId id = new ReuseProductResultId();
		id.setProductId(productId);
		id.setActualTimestamp(new Timestamp(time));
		reuseProductResult.setId(id);
		reuseProductResult.setReuseVinReason(unscrapReason);
		reuseProductResult.setAssociateNo(associateId);
		reuseProductResult.setProductionDate(new Date(time));
		return reuseProductResult;
	}

	private ExceptionalOut createExceptionalOutResult(String productId) {
		ExceptionalOut exceptionalOut = new ExceptionalOut();
		exceptionalOut.setProcessPointId("PPID");
		exceptionalOut.setProductId(productId);
		exceptionalOut.setAssociateNo("assoc");

		return exceptionalOut;
	}
}
