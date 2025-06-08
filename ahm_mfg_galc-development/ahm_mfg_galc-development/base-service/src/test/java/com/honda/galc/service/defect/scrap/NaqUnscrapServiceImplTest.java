package com.honda.galc.service.defect.scrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ExceptionalOutDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Block;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.MultiLineHelper;
import com.honda.galc.util.ProductCheckUtil;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ProductTypeUtil.class, MultiLineHelper.class, ServiceFactory.class, PropertyService.class, ProductCheckUtil.class})
public class NaqUnscrapServiceImplTest {
	@Mock
	private DiecastDao diecastDaoMock = PowerMockito.mock(DiecastDao.class);

	@Mock
	private ProductDao productDaoMock = PowerMockito.mock(ProductDao.class);

	@Mock
	private QiDefectResultDao qiDefectResultMock = PowerMockito.mock(QiDefectResultDao.class);

	@Mock
	private ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);

	@Mock
	private MultiLineHelper multiLineHelperMock = PowerMockito.mock(MultiLineHelper.class);

	@Mock
	private ProductCheckUtil productCheckUtilMock = PowerMockito.mock(ProductCheckUtil.class);

	@Mock
	ExceptionalOutDao exceptionalOutDaoMock = PowerMockito.mock(ExceptionalOutDao.class);

	@InjectMocks
	private NaqUnscrapServiceImpl unscrapService;

	@Before
	public void setUp() throws Exception {
		unscrapService = new NaqUnscrapServiceImpl();
		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(ProductTypeUtil.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(MultiLineHelper.class);

		when(ProductTypeUtil.createProduct(any(String.class), any(String.class))).thenCallRealMethod();
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qiDefectResultMock);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped method when the product is scrapped
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT}
	 */
	@Test
	public void isProductScrapped_ProductScrapped() {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NON_REPAIRABLE.getId())).thenReturn(qiDefectResultList);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId("C9G9612296U")).thenReturn(exceptionalOutList);

		
		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertTrue(isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isProductScrapped method when the product not scrapped
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT}
	 */
	@Test
	public void isProductScrapped_ProductNotScrapped() {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.REASON.name(), "Can't be fixed");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();

		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NOT_FIXED.getId())).thenReturn(qiDefectResultList);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId("C9G9612296U")).thenReturn(exceptionalOutList);

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");
		boolean isScrapped = unscrapService.isProductScrapped(product);

		assertFalse(isScrapped);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product is not found
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT}
	 */
	@Test
	public void isValidProduct_ProductNotFound() {
		DataContainer data = new DefaultDataContainer();

		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(null);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();
		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
		verify(diecastDaoMock, times(1)).findByMCDCNumber("C9G9612296U");
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product has an invalid tracking status
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {tracking status = BLOCK1ON}
	 */
	@Test
	public void isValidProduct_InvalidTrackingStatus() throws Exception {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");	

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PROCESSPOINT")).thenReturn(productCheckPropertyBeanMock);
		when(MultiLineHelper.getInstance("PROCESSPOINT")).thenReturn(multiLineHelperMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(productCheckPropertyBeanMock.isLineIdCheckEnabled()).thenReturn(true);
		when(multiLineHelperMock.getProcessPointToUse(product)).thenReturn(new ProcessPoint());
		when(productCheckUtilMock.invalidPreviousLineCheck()).thenReturn(true);
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short)DefectStatus.NON_REPAIRABLE.getId())).thenReturn(qiDefectResultList);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(exceptionalOutList);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();


		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
		verify(productCheckUtilMock, times(1)).invalidPreviousLineCheck();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product has an invalid tracking status but Line Id check is not enabled
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {tracking status = BLOCK1ON}
	 */
	@Test
	public void isValidProduct_LineIdCheckNotEnabled() throws Exception {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");	

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PROCESSPOINT")).thenReturn(productCheckPropertyBeanMock);
		when(MultiLineHelper.getInstance("PROCESSPOINT")).thenReturn(multiLineHelperMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(productCheckPropertyBeanMock.isLineIdCheckEnabled()).thenReturn(false);
		when(multiLineHelperMock.getProcessPointToUse(product)).thenReturn(new ProcessPoint());
		when(productCheckUtilMock.invalidPreviousLineCheck()).thenReturn(true);
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NON_REPAIRABLE.getId())).thenReturn(qiDefectResultList);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId("C9G9612296U")).thenReturn(exceptionalOutList);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();

		boolean isValid = unscrapService.isValidProducts();

		assertTrue(isValid);
		verify(productCheckUtilMock, times(0)).invalidPreviousLineCheck();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product is in a valid tracking status
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {tracking status = BLOCK1ON}
	 */
	@Test
	public void isValidProduct_ValidTrackingStatus() throws Exception {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");	

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PROCESSPOINT")).thenReturn(productCheckPropertyBeanMock);
		when(MultiLineHelper.getInstance("PROCESSPOINT")).thenReturn(multiLineHelperMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(productCheckPropertyBeanMock.isLineIdCheckEnabled()).thenReturn(true);
		when(multiLineHelperMock.getProcessPointToUse(product)).thenReturn(new ProcessPoint());
		when(productCheckUtilMock.invalidPreviousLineCheck()).thenReturn(false);
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NON_REPAIRABLE.getId())).thenReturn(qiDefectResultList);
		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(exceptionalOutList);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();

		boolean isValid = unscrapService.isValidProducts();

		assertTrue(isValid);
		verify(productCheckUtilMock, times(1)).invalidPreviousLineCheck();
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product is not scrapped
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {defect status = NOT FIXED}
	 */
	@Test
	public void isValidProduct_ProductNotScrapped() throws Exception {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);
		qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NOT_FIXED.getId());
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createBlockProduct("C9G9612296U","LG6C151202V", "BLOCK", "RV0", "BLOCK1ON");	

		when(ProductTypeUtil.getProductDao("BLOCK")).thenReturn(diecastDaoMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PROCESSPOINT")).thenReturn(productCheckPropertyBeanMock);
		when(MultiLineHelper.getInstance("PROCESSPOINT")).thenReturn(multiLineHelperMock);
		when(((DiecastDao) diecastDaoMock).findByMCDCNumber("C9G9612296U")).thenReturn(product);
		when(productCheckPropertyBeanMock.isLineIdCheckEnabled()).thenReturn(true);
		when(multiLineHelperMock.getProcessPointToUse(product)).thenReturn(new ProcessPoint());
		when(productCheckUtilMock.invalidPreviousLineCheck()).thenReturn(false);
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NOT_FIXED.getId())).thenReturn(qiDefectResultList);

		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		List<ExceptionalOut> exceptionalOutList = new ArrayList<ExceptionalOut>();
		exceptionalOutList.add(createExceptionalOutResult("C9G9612296U"));
		when(exceptionalOutDaoMock.findAllByProductId("C9G9612296U")).thenReturn(exceptionalOutList);

		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();

		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test isValidProduct method when the product is scrapped but the product type is not scrappable
	 * {productId = C9G9612296U}{productType = FRAME}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {defect status = NON_REPAIRIABLE}
	 */
	@Test
	public void isValidProduct_ProductNotScrapable() throws Exception {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = createQiDefectResult();
		qiDefectResultList.add(qiDefectResult);
		qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NON_REPAIRABLE.getId());
		qiDefectResultList.add(qiDefectResult);

		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		BaseProduct product = createFrameProduct("5FNRL6H78KB117803", "FRAME", "KTHRAJ600 NH830MX   B", "AFON");

		when(ProductTypeUtil.getProductDao("FRAME")).thenReturn(productDaoMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PROCESSPOINT")).thenReturn(productCheckPropertyBeanMock);
		when(MultiLineHelper.getInstance("PROCESSPOINT")).thenReturn(multiLineHelperMock);
		when(((ProductDao) productDaoMock).findByKey("5FNRL6H78KB117803")).thenReturn(product);
		when(productCheckPropertyBeanMock.isLineIdCheckEnabled()).thenReturn(true);
		when(multiLineHelperMock.getProcessPointToUse(product)).thenReturn(new ProcessPoint());
		when(productCheckUtilMock.invalidPreviousLineCheck()).thenReturn(false);
		when(qiDefectResultMock.findAllByProductIdAndCurrentDefectStatus("C9G9612296U", (short) DefectStatus.NOT_FIXED.getId())).thenReturn(qiDefectResultList);

		when(ServiceFactory.getDao(ExceptionalOutDao.class)).thenReturn(exceptionalOutDaoMock);
		when(exceptionalOutDaoMock.findAllByProductId(product.getProductId())).thenReturn(null);
		unscrapService.parseDataContainer(data);
		unscrapService.setLogger();

		boolean isValid = unscrapService.isValidProducts();

		assertFalse(isValid);
	}

	/**
	 * @author Bradley Brown
	 * @date June 20, 2019
	 * 
	 * Test prepareQiDefectResultForUnscrap method to verify the defect result is correctly updated
	 * {productId = C9G9612296U}{productType = BLOCK}{reason = Can't be fixed} {associateId = VF012345} {application id = APPLICATIONID} {process point id = PROCESSPOINT} {defect status = NON_FIXED}
	 */
	@Test
	public void prepareQiDefectResultForUnscrap_verifyDataForUpdate() {
		DataContainer data = new DefaultDataContainer();
		List<String> productIdList = new ArrayList<String>();
		productIdList.add("C9G9612296U");
		data.put(TagNames.PRODUCT_ID.name(), productIdList);
		data.put(TagNames.REASON.name(), "NAQ part was unscrapped");
		data.put(TagNames.ASSOCIATE_ID.name(), "VF012345");
		data.put(TagNames.PRODUCT_TYPE.name(), "BLOCK");
		data.put(TagNames.PROCESS_POINT_ID.name(), "PROCESSPOINT");
		data.put(TagNames.APPLICATION_ID.name(), "APPLICATIONID");

		unscrapService.setLogger();
		unscrapService.parseDataContainer(data);
		QiDefectResult qiDefectResult = unscrapService.prepareQiDefectResultForUnscrap(createQiDefectResult());

		assertEquals(10, qiDefectResult.getDefectResultId());
		assertEquals("C9G9612296U", qiDefectResult.getProductId());
		assertEquals(DefectStatus.NOT_FIXED.getId(), qiDefectResult.getCurrentDefectStatus());
		assertEquals("NAQ part was unscrapped", qiDefectResult.getComment());
		assertEquals("VF012345", qiDefectResult.getUpdateUser());
	}

	private QiDefectResult createQiDefectResult() {
		QiDefectResult qiDefectResult = new QiDefectResult();
		qiDefectResult.setDefectResultId(10);
		qiDefectResult.setProductId("C9G9612296U");
		qiDefectResult.setCurrentDefectStatus((short) DefectStatus.NON_REPAIRABLE.getId());
		qiDefectResult.setComment("NAQ part scrap Reason");
		qiDefectResult.setUpdateUser("VF000000");
		return qiDefectResult;
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

	private ExceptionalOut createExceptionalOutResult(String productId) {
		ExceptionalOut exceptionalOut = new ExceptionalOut();
		exceptionalOut.setProcessPointId("PPID");
		exceptionalOut.setProductId(productId);
		exceptionalOut.setAssociateNo("assoc");

		return exceptionalOut;
	}
}
