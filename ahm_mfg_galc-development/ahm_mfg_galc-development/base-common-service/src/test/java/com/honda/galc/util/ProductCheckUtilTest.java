package com.honda.galc.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ShippingStatusDao;
import com.honda.galc.dao.product.FrameMTOCPriceMasterSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.KickoutDao;
import com.honda.galc.dao.product.KickoutLocationDao;
import com.honda.galc.dao.product.LetPartCheckSpecDao;
import com.honda.galc.dao.product.LetResultDao;
import com.honda.galc.dao.product.SequenceDao;
import com.honda.galc.dao.qi.QiDefectResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.InstalledPartDetail;
import com.honda.galc.dto.RequiredLetPartSpecDetailsDto;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.ShippingStatus;
import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.Kickout;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.qi.QiDefectResult;
import com.honda.galc.entity.product.Sequence;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.check.PartResultData;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class, FrameMTOCPriceMasterSpecDao.class, ShippingStatusDao.class, FrameDao.class, FrameSpecDao.class, SequenceDao.class, PropertyService.class}) 
public class ProductCheckUtilTest {
	
	List<InstalledPartDetail> partDetailList;
	List<PartResultData> partResultData;
	
	@Before
	public void init() {
		partDetailList = new ArrayList<InstalledPartDetail>();
		partResultData = new ArrayList<PartResultData>();
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for kickoutExistCheck when the recursive property is set to false and no kickouts exist
	 */
	@Test
	public void kickoutExistCheck_RecursiveFalseNoKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);
		KickoutDao kickoutDaoMock = PowerMockito.mock(KickoutDao.class);
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		
		when(PropertyService.getPropertyBean(SystemPropertyBean.class,processPoint.getProcessPointId())).thenReturn(systemPropertyBeanMock);
		when(ServiceFactory.getDao(KickoutDao.class)).thenReturn(kickoutDaoMock);	
		when(systemPropertyBeanMock.isRecursiveKickout()).thenReturn(false);
		when(kickoutDaoMock.findAllActiveByProductIdAndProcessPoint(product.getProductId(), processPoint.getProcessPointId(),0)).thenReturn(new ArrayList<Kickout>());		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
	
		boolean result = productCheckUtil.kickoutExistCheck();
		assertFalse("kickoutExistCheck_RecursiveFalseNoKickout() failed, expected false but method returned true.", result);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for kickoutExistCheck when the recursive property is set to false and kickout exist
	 */
	@Test
	public void kickoutExistCheck_RecursiveFalseWithKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);
		KickoutDao kickoutDaoMock = PowerMockito.mock(KickoutDao.class);
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		
		when(PropertyService.getPropertyBean(SystemPropertyBean.class,processPoint.getProcessPointId())).thenReturn(systemPropertyBeanMock);
		when(ServiceFactory.getDao(KickoutDao.class)).thenReturn(kickoutDaoMock);
		when(systemPropertyBeanMock.isRecursiveKickout()).thenReturn(false);
		when(kickoutDaoMock.findAllActiveByProductIdAndProcessPoint(product.getProductId(), processPoint.getProcessPointId(),0)).thenReturn(createKickout("BAG9B22583N"));
			
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		boolean result = productCheckUtil.kickoutExistCheck();
		assertTrue("kickoutExistCheck_RecursiveFalseWithKickout() failed, expected true but method returned false.", result);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for kickoutExistCheck when the recursive property is set to true and no kickouts exist
	 */
	@Test
	public void kickoutExistCheck_RecursiveTrueNoKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);
		KickoutDao kickoutDaoMock = PowerMockito.mock(KickoutDao.class);
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		
		when(PropertyService.getPropertyBean(SystemPropertyBean.class,processPoint.getProcessPointId())).thenReturn(systemPropertyBeanMock);
		when(ServiceFactory.getDao(KickoutDao.class)).thenReturn(kickoutDaoMock);
		when(systemPropertyBeanMock.isRecursiveKickout()).thenReturn(true);
		when(kickoutDaoMock.findAllActiveByProductId(product.getProductId())).thenReturn(new ArrayList<Kickout>());		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		boolean result = productCheckUtil.kickoutExistCheck();
		assertFalse("kickoutExistCheck_RecursiveTrueNoKickout() failed, expected false but method returned true.", result);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for kickoutExistCheck when the recursive property is set to true and  kickout exist
	 */
	@Test
	public void kickoutExistCheck_RecursiveTrueWithKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);
		KickoutDao kickoutDaoMock = PowerMockito.mock(KickoutDao.class);		
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		
		when(PropertyService.getPropertyBean(SystemPropertyBean.class,processPoint.getProcessPointId())).thenReturn(systemPropertyBeanMock);
		when(ServiceFactory.getDao(KickoutDao.class)).thenReturn(kickoutDaoMock);
		when(systemPropertyBeanMock.isRecursiveKickout()).thenReturn(true);
		when(kickoutDaoMock.findAllActiveByProductId(product.getProductId())).thenReturn(createKickout(product.getProductId()));		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		boolean result = productCheckUtil.kickoutExistCheck();
		assertTrue("kickoutExistCheck_RecursiveTrueWithKickout() failed, expected true but method returned false.", result);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiDefectCheck when the isKickoutDefect property is set to false and no defect exist
	 */
	@Test
	public void qiDefectCheck_KickoutCheckFalseWithNoDefects() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId())).thenReturn(productCheckPropertyBeanMock);
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(productCheckPropertyBeanMock.isKickoutDefect()).thenReturn(false);
		when(qidefectResultDaoMock.findAllNotRepairedNotKickoutDefects(product.getProductId())).thenReturn(new ArrayList<QiDefectResult>());		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		List<String> result = productCheckUtil.qiDefectCheck();
		assertTrue("qiDefectCheck_KickoutCheckFalseWithNoDefects() failed, expected no defects, but defects were returned.", result.isEmpty());
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiDefectCheck when the isKickoutDefect property is set to true and defect exist
	 * that are not associated with a kickout
	 */
	@Test
	public void qiDefectCheck_KickoutCheckTrueWithDefectsNoKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId())).thenReturn(productCheckPropertyBeanMock);
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(productCheckPropertyBeanMock.isKickoutDefect()).thenReturn(true);
		when(qidefectResultDaoMock.findAllNotRepairedDefects(product.getProductId())).thenReturn(createQiDefectList(product.getProductId()));		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		List<String> result = productCheckUtil.qiDefectCheck();
		assertTrue("qiDefectCheck_KickoutCheckTrueWithDefectsNoKickout() failed, expected defects, but no defects were returned.", result.size() == 1);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiDefectCheck when the isKickoutDefect property is set to false and defect exist
	 * that are not associated with a kickout
	 */
	@Test
	public void qiDefectCheck_KickoutCheckFalseWithDefectsNoKickout() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId())).thenReturn(productCheckPropertyBeanMock);
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(productCheckPropertyBeanMock.isKickoutDefect()).thenReturn(false);
		when(qidefectResultDaoMock.findAllNotRepairedNotKickoutDefects(product.getProductId())).thenReturn(createQiDefectList(product.getProductId()));

		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		List<String> result = productCheckUtil.qiDefectCheck();
		assertTrue("qiDefectCheck_KickoutCheckFalseWithDefectsNoKickout() failed, expected defects, but no defects were returned.", result.size() == 1);
	}

	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiDefectCheck when the isKickoutDefect property is set to true and no defect exist
	 */
	@Test
	public void qiDefectCheck_KickoutCheckTrueWithNoDefects() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		KickoutLocationDao kickoutLocationDaoMock = PowerMockito.mock(KickoutLocationDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPoint.getProcessPointId())).thenReturn(productCheckPropertyBeanMock);
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(productCheckPropertyBeanMock.isKickoutDefect()).thenReturn(true);
		when(qidefectResultDaoMock.findAllNotRepairedDefects(product.getProductId())).thenReturn(new ArrayList<QiDefectResult>());
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		List<String> result = productCheckUtil.qiDefectCheck();
		assertTrue("qiDefectCheck_KickoutCheckTrueWithNoDefects() failed, expected no defects, but defects were returned.", result.isEmpty());
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiNonRepairablqiNonRepairableDefectCheck when no non-reparable defects are found
	 */
	@Test
	public void qiNonRepairableDefectCheck_NoDefectExist() {
		PowerMockito.mockStatic(ServiceFactory.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(qidefectResultDaoMock.findAllByProductIdAndCurrentDefectStatus(product.getProductId(), (short) 8)).thenReturn(new ArrayList<QiDefectResult>());
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		boolean result = productCheckUtil.qiNonRepairableDefectCheck();
		assertFalse(result);
	}
	
	/**
	 * @author Bradley Brown
	 * @date Dec 2, 2019
	 * 
	 * Test for qiNonRepairableDefectCheck when a non-reparable defect is found
	 */
	@Test
	public void qiNonRepairableDefectCheck_DefectExist() {
		PowerMockito.mockStatic(ServiceFactory.class);
		QiDefectResultDao qidefectResultDaoMock = PowerMockito.mock(QiDefectResultDao.class);
		BaseProduct product = ProductTypeUtil.createProduct("BLOCK", "BAG9B22583N");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("BMC100001");
		
		when(ServiceFactory.getDao(QiDefectResultDao.class)).thenReturn(qidefectResultDaoMock);
		when(qidefectResultDaoMock.findAllByProductIdAndCurrentDefectStatus(product.getProductId(), (short) 8)).thenReturn(createQiDefectList(product.getProductId()));		
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, processPoint);
		
		boolean result = productCheckUtil.qiNonRepairableDefectCheck();
		assertTrue(result);
	}
	
	/**
	 * @author Madhuri Edala
	 * @date Nov 14,2018
	 * Tests priceEmptyCheck method in ProductCheckUtil.java
	 * 
	 */
	
	@Test
	public void framePriceNotEmptyCheck_productSpecCodeNotEmpty() {				
		PowerMockito.mockStatic(ServiceFactory.class);  
		FrameMTOCPriceMasterSpecDao frameMTOCPriceMasterSpecDaoMock =  PowerMockito.mock(FrameMTOCPriceMasterSpecDao.class);			
		when(ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class)).thenReturn(frameMTOCPriceMasterSpecDaoMock);
		when( frameMTOCPriceMasterSpecDaoMock.getMTOCPrice(Matchers.anyString(),Matchers.anyInt()))
			.thenReturn("999").thenReturn(null);
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Product product = new Frame();
		product.setProductSpecCode("7SCVAA000 B537M     A ".trim());		
		productCheckUtil.setProduct(product);	
		
		assertTrue(productCheckUtil.framePriceNotEmptyCheck());		
		assertFalse(productCheckUtil.framePriceNotEmptyCheck());		
		}
	
	@Test
	public void framePriceEmptyCheck_productSpecCodeNotEmpty() {				
		PowerMockito.mockStatic(ServiceFactory.class);  
		FrameMTOCPriceMasterSpecDao frameMTOCPriceMasterSpecDaoMock =  PowerMockito.mock(FrameMTOCPriceMasterSpecDao.class);			
		when(ServiceFactory.getDao(FrameMTOCPriceMasterSpecDao.class)).thenReturn(frameMTOCPriceMasterSpecDaoMock);
		when( frameMTOCPriceMasterSpecDaoMock.getMTOCPrice(Matchers.anyString(),Matchers.anyInt()))
		    .thenReturn("999").thenReturn(null);
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Product product = new Frame();
		product.setProductSpecCode("7SCVAA000 B537M     A ".trim());		
		productCheckUtil.setProduct(product);	
		
		assertFalse(productCheckUtil.framePriceEmptyCheck());		
		assertTrue(productCheckUtil.framePriceEmptyCheck());
	}
	
	@Test
	public void framePriceNotEmptyCheck_productSpecCodeEmpty() {
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Product product = new Frame();
		product.setProductSpecCode(null);				
		productCheckUtil.setProduct(product);
		
		assertFalse(productCheckUtil.framePriceNotEmptyCheck());		
	    }
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Dec ,19 2018
	 * 
	 * Test initialization method in the case that the ShippingStatus is null
	 */
	@Test
	public void isProductShipped_statusNull() {
		ShippingStatus shippingStatus = new ShippingStatus();
		shippingStatus = null;
		
		PowerMockito.mockStatic(ServiceFactory.class);
		ShippingStatusDao shippingStatusDaoMock = PowerMockito.mock(ShippingStatusDao.class);
		when(ServiceFactory.getDao(ShippingStatusDao.class)).thenReturn(shippingStatusDaoMock);
		when(shippingStatusDaoMock.findByKey("5FNRL6H72KB045786")).thenReturn(shippingStatus);
		assertFalse(ProductCheckUtil.isProductShipped("5FNRL6H72KB045786"));
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Dec ,19 2018
	 * 
	 * Test initialization method in the case that the ShippingStatus is in not Shipped status
	 */
	@Test
	public void isProductShipped_statusNotShipped() {
		ShippingStatus shippingStatus = new ShippingStatus();
		shippingStatus.setVin("5FNRL6H72KB045786");
		shippingStatus.setStatus(ShippingStatusEnum.S90A.getStatus());
		
		PowerMockito.mockStatic(ServiceFactory.class);
		ShippingStatusDao shippingStatusDaoMock = PowerMockito.mock(ShippingStatusDao.class);
		when(ServiceFactory.getDao(ShippingStatusDao.class)).thenReturn(shippingStatusDaoMock);
		when(shippingStatusDaoMock.findByKey("5FNRL6H72KB045786")).thenReturn(shippingStatus);
		assertFalse(ProductCheckUtil.isProductShipped("5FNRL6H72KB045786"));
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date Dec ,19 2018
	 * 
	 * Test initialization method in the case that the ShippingStatus is in shipped status
	 */
	@Test
	public void isProductShipped_statusShipped() {
		ShippingStatus shippingStatus = new ShippingStatus();
		shippingStatus.setVin("5FNRL6H72KB045786");
		shippingStatus.setStatus(ShippingStatusEnum.INIT.getStatus());
		
		PowerMockito.mockStatic(ServiceFactory.class);
		ShippingStatusDao shippingStatusDaoMock = PowerMockito.mock(ShippingStatusDao.class);
		when(ServiceFactory.getDao(ShippingStatusDao.class)).thenReturn(shippingStatusDaoMock);
		when(shippingStatusDaoMock.findByKey("5FNRL6H72KB045786")).thenReturn(shippingStatus);
		assertTrue(ProductCheckUtil.isProductShipped("5FNRL6H72KB045786"));
	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get destination check
	 */
	@Test
	public void getDestinationCheck() {
		Frame frame = new Frame();
		frame.setProductSpecCode("7SCVAA000 B537M     A");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(frame);
		FrameSpec frameSpec = new FrameSpec();
		frameSpec.setSalesModelTypeCode("KA");
		
		PowerMockito.mockStatic(ServiceFactory.class);
		FrameSpecDao frameSpecDaoMock = PowerMockito.mock(FrameSpecDao.class);
		when(ServiceFactory.getDao(FrameSpecDao.class)).thenReturn(frameSpecDaoMock);
		when(frameSpecDaoMock.findByKey(Matchers.anyString())).thenReturn(frameSpec);
		
		List<String> list = productCheckUtil.destinationCheck();
		assertTrue(list.size() > 0);
	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get destination null check
	 */
	@Test
	public void getDestinationNull() {
		FrameSpec frameSpec = new FrameSpec();
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		
		PowerMockito.mockStatic(ServiceFactory.class);
		FrameSpecDao frameSpecDaoMock = PowerMockito.mock(FrameSpecDao.class);
		when(ServiceFactory.getDao(FrameSpecDao.class)).thenReturn(frameSpecDaoMock);
		when(frameSpecDaoMock.findByKey(null)).thenReturn(frameSpec);
		
		List<String> list = productCheckUtil.destinationCheck();
		assertTrue(list.size() == 0);

	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get tracking status frame
	 */
	@Test
	public void getTrackingStatusFrame() {
		Frame frame = new Frame();
		frame.setTrackingStatus("AAF1OF1");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(frame);
		Line line = new Line();
		line.setLineDescription("AF Off");
		
		PowerMockito.mockStatic(ServiceFactory.class);
		LineDao lineDaoMock = PowerMockito.mock(LineDao.class);
		when(ServiceFactory.getDao(LineDao.class)).thenReturn(lineDaoMock);
		when(lineDaoMock.findByKey(Matchers.anyString())).thenReturn(line);
		
		List<String> list = productCheckUtil.trackingStatusCheck();
		assertTrue(list.size() > 0);
	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get tracking status engine
	 */
	@Test
	public void getTrackingStatusEngine() {
		Engine engine = new Engine();
		engine.setTrackingStatus("AAF1OF1");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(engine);
		Line line = new Line();
		line.setLineDescription("AF Off");
		
		PowerMockito.mockStatic(ServiceFactory.class);
		LineDao lineDaoMock = PowerMockito.mock(LineDao.class);
		when(ServiceFactory.getDao(LineDao.class)).thenReturn(lineDaoMock);
		when(lineDaoMock.findByKey(Matchers.anyString())).thenReturn(line);
		
		List<String> list = productCheckUtil.trackingStatusCheck();
		assertTrue(list.size() > 0);
	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get tracking status mbpn
	 */
	@Test
	public void getTrackingStatusMBPN() {
		MbpnProduct product = new MbpnProduct();
		product.setTrackingStatus("AAF1OF1");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(product);
		Line line = new Line();
		line.setLineDescription("AF Off");
		
		PowerMockito.mockStatic(ServiceFactory.class);
		LineDao lineDaoMock = PowerMockito.mock(LineDao.class);
		when(ServiceFactory.getDao(LineDao.class)).thenReturn(lineDaoMock);
		when(lineDaoMock.findByKey(Matchers.anyString())).thenReturn(line);
		
		List<String> list = productCheckUtil.trackingStatusCheck();
		assertTrue(list.size() > 0);
	}
	
	/**
	 * @author Mahesh
	 * @date Jan, 16 2019
	 * 
	 * Test get tracking status frame when null
	 */
	@Test
	public void getTrackingStatusFrameNull() {
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		Line line = new Line();
		PowerMockito.mockStatic(ServiceFactory.class);
		LineDao lineDaoMock = PowerMockito.mock(LineDao.class);
		when(ServiceFactory.getDao(LineDao.class)).thenReturn(lineDaoMock);
		when(lineDaoMock.findByKey(null)).thenReturn(line);
		
		List<String> list = productCheckUtil.trackingStatusCheck();
		assertTrue(list.size() == 0);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame with a good status
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckPass() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(new ArrayList<InstalledPartDetail>());
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);

		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 0);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where one has
	 * a NG status
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckPartNGStatus() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getNGInstalledPartDetail());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where
	 * installed part in missing PART_ID
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckMissingPartId() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailMissingPartId());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where one part
	 * is not installed
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckPartNotInstalled() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailPartNotInstalled());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where one part
	 * is not installed and another part is missing the PART_ID
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckPartNotInstalledAndMissingPartId() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailPartNotInstalled());
		partDetailList.add(getInstalledPartDetailMissingPartId());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 2, actual " + partDetails.size(), partDetails.size() == 2);
		PartResultData result = partResultData.get(0);
		PartResultData result2 = partResultData.get(1);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
		assertTrue("Incorrect result " + result2.toString(), partDetails.contains(result2));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where the part
	 * in missing a measurement value.
	 * MEASUREMENT_COUNT != number of records in GAL198TBX
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckIncorrectMeasurementCount() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailIncorrectMeasCount());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where the part
	 * MEASUREMENT_VALUE is null
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckMissingMeasurementValue() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailMissingMeasurementValue());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Mar 18, 2019
	 * 
	 * Test for when all part are installed of Frame where the part
	 * has a NG measurement result
	 * 
	 * Product Id = 5FNRL6H70KB086093
	 * Product Type = FRAME
	 * Process Point Id = PP10000
	 */
	@Test
	public void installedheck_PartConfirmCheckNGMeasurement() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H70KB086093");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "ENABLE_REPAIR_CHECK", false)).thenReturn(true);
		when(PropertyService.getPropertyBoolean("Default_LotControl", "LIMIT_RULES_BY_DIVISION", false)).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		partDetailList.add(getInstalledPartDetailNGMeasurement1());
		partDetailList.add(getInstalledPartDetailNGMeasurement2());
		when(installedPartDaoMock.getAllInstalledPartDetails("5FNRL6H70KB086093", "FRAME", "PP10000", true, true, true, true)).thenReturn(partDetailList);
	
		List<PartResultData> partDetails = productCheckUtil.installedPartCheck("PP10000", true, true); 

		assertTrue("Incorrect number of parts returned: expected 1, actual " + partDetails.size(), partDetails.size() == 1);
		PartResultData result = partResultData.get(0);
		assertTrue("Incorrect result " + result.toString(), partDetails.contains(result));
	}
	

	private InstalledPartDetail getNGInstalledPartDetail() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART NG");
		installedPartDetail.setRulePartName("TEST NG PART");
		installedPartDetail.setPartId("A000");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(0);
		installedPartDetail.setMeasurementCount(0);
		
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Incorrect Part",
				""
				));
		
		return installedPartDetail;
	}

	private InstalledPartDetail getInstalledPartDetailMissingPartId() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART MISSING PARTID");
		installedPartDetail.setRulePartName("TEST PART MISSING PARTID");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(0);
		installedPartDetail.setMeasurementCount(0);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Missing PartId",
				""
				));
		return installedPartDetail;
	}
	
	private InstalledPartDetail getInstalledPartDetailPartNotInstalled() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId(null);
		installedPartDetail.setPartName(null);
		installedPartDetail.setInstalledPartStatus(null);
		installedPartDetail.setMeasurementCount(null);
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setPartConfirmCheck(0);
		installedPartDetail.setRepairCheck(0);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Missing Part",
				""
				));
		return installedPartDetail;
	}
	
	private InstalledPartDetail getInstalledPartDetailIncorrectMeasCount() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setRulePartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setPartId("A000");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(1);
		installedPartDetail.setMeasurementCount(3);
		installedPartDetail.setMeasurementValue(new BigDecimal(5));
		installedPartDetail.setMeasurementStatus(1);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Measurement Count Mismatch",
				"5, "
				));
		
		return installedPartDetail;
	}
	
	private InstalledPartDetail getInstalledPartDetailMissingMeasurementValue() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setRulePartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setPartId("A000");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(1);
		installedPartDetail.setMeasurementCount(1);
		installedPartDetail.setMeasurementStatus(1);
		installedPartDetail.setPartConfirmCheck(1);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Missing Measurement",
				""
				));
		
		return installedPartDetail;
	}
	
	private InstalledPartDetail getInstalledPartDetailNGMeasurement1() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setRulePartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setPartId("A000");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(1);
		installedPartDetail.setMeasurementCount(2);
		installedPartDetail.setMeasurementValue(new BigDecimal(5));
		installedPartDetail.setMeasurementStatus(1);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Incorrect Measurement",
				"5, 10, "
				));
	
		return installedPartDetail;
	}
	
	private InstalledPartDetail getInstalledPartDetailNGMeasurement2() {
		InstalledPartDetail installedPartDetail = new InstalledPartDetail();
		
		installedPartDetail.setProductId("5FNRL6H70KB086093");
		installedPartDetail.setPartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setRulePartName("TEST PART MISSING MEASUREMENT");
		installedPartDetail.setPartId("A000");
		installedPartDetail.setProcessPointId("PP10000");
		installedPartDetail.setInstalledPartStatus(1);
		installedPartDetail.setMeasurementCount(2);
		installedPartDetail.setMeasurementValue(new BigDecimal(10));
		installedPartDetail.setMeasurementStatus(0);
		partResultData.add(getNGInstalledPartData(
				installedPartDetail.getPartName(),
				installedPartDetail.getPartSerialNumber(),
				"Incorrect Measurement",
				"5, 10, "
				));
		
		return installedPartDetail;
	}
	
	private PartResultData getNGInstalledPartData(String partName, String partSerialNumber, String partReason, String torque) {
		PartResultData result = new PartResultData();
		result.part_Name = partName;
		result.part_Serial = partSerialNumber;
		result.part_Reason = partReason;
		result.torques = torque;
		 
		return result;
	}
	
	/**
	 * @author Mahesh
	 * @date March, 22 2019
	 * 
	 * Test get check engine mismatch and hold
	 */
	@Test
	public void getCheckEngineMismatchAndHold() {
		
		Frame frame = new Frame();
		frame.setProductSpecCode("7SCVAA000 B537M     A");
		frame.setProductionLot("MAP 01AF201806050030");
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("TAF3OF1PQ0111");
		
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(frame);
		productCheckUtil.setProcessPoint(processPoint);
		
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao ipDao = PowerMockito.mock(InstalledPartDao.class);
		when(PropertyService.getPropertyMap("Default_LotControl", "NGLC_CHECKER_CLIENT_ID")).thenReturn(new HashMap<String, String>());
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(ipDao);
		when(ipDao.getPartSerialNumber(Matchers.anyString(),Matchers.anyString())).thenReturn("K20C42354241");
		
		LegacyUtil legacyUtilMock = PowerMockito.mock(LegacyUtil.class);		
		when(legacyUtilMock.executeCheck(Matchers.any(DataContainer.class), Matchers.anyString(), Matchers.anyString())).thenReturn(getLegacyUtil());
		
		BuildAttributeDao buildAttributeDao = PowerMockito.mock(BuildAttributeDao.class);
	    when(ServiceFactory.getDao(BuildAttributeDao.class)).thenReturn(buildAttributeDao);
	    BuildAttribute attribute = new BuildAttribute();
	    attribute.setAttributeValue("false");
	    when(buildAttributeDao.findById(Matchers.anyString(),Matchers.anyString())).thenReturn(attribute);
	    
		List<String> resultList = productCheckUtil.checkEngineMismatchAndHold();
		assertTrue(resultList.size() > 0);
		
	}
		
	private List<String> getLegacyUtil() {
		List<String> partNumbers = new ArrayList<String>();
		partNumbers.add("73300TJB B0");		
		return partNumbers;
	}
	
	/**
	 * @author Mahesh
	 * @date April, 8 2019
	 * 
	 * Test get check production lot
	 */
	@Test
	public void getProductionLot() {
		Frame frame = new Frame();
		frame.setProductionLot("MAP 01AF201806050030");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(frame);
		List<String> list = productCheckUtil.productionLotCheck();
		assertTrue(list.size() > 0);
	}
	
	/**
	 * @author Mahesh
	 * @date April, 8 2019
	 * 
	 * Test get check production lot when null
	 */	
	@Test
	public void getProductionLotNull() {
		Frame frame = new Frame();
		frame.setProductionLot(null);
		ProductCheckUtil productCheckUtil = new ProductCheckUtil();
		productCheckUtil.setProduct(frame);
		List<String> list = productCheckUtil.productionLotCheck();
		assertTrue(list.contains(null));
		
	}	

	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date April, 26 2019
	 * 
	 *       Test installed part (352267085610672) with Let Result containing
	 *       special Character(352267085610672???). It should match and checker
	 *       should return true
	 * 
	 */
	@Test
	public void letDataCheckWithTrimSpecialCharacter_whenMatches_thenCorrect() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H99KB103331");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		RequiredLetPartSpecDetailsDto requiredLetPartSpecDetailsDto = getRequiredLetPartSpecDetailsDto();
		List<RequiredLetPartSpecDetailsDto> requiredLetPartSpecDetails = new ArrayList<RequiredLetPartSpecDetailsDto>();
		requiredLetPartSpecDetails.add(requiredLetPartSpecDetailsDto);

		InstalledPartId installedPartId = new InstalledPartId();
		installedPartId.setPartName("TCU");
		installedPartId.setProductId(product.getProductId());

		InstalledPart installedPart = new InstalledPart();
		installedPart.setPartSerialNumber("352267085610672");
		installedPart.setPartName("TCU");

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		LetPartCheckSpecDao letPartCheckSpecDaoMock = PowerMockito.mock(LetPartCheckSpecDao.class);
		LetResultDao letResultDaoMock = PowerMockito.mock(LetResultDao.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);

		when(ServiceFactory.getDao(LetPartCheckSpecDao.class)).thenReturn(letPartCheckSpecDaoMock);
		when(letPartCheckSpecDaoMock.findAllActiveByProductSpecCode(product.getProductSpecCode(),
				product.getProductId(), product.getProductType().toString(), 1)).thenReturn(requiredLetPartSpecDetails);
		when(ServiceFactory.getDao(LetResultDao.class)).thenReturn(letResultDaoMock);
		when(letResultDaoMock.getInspectionResult(product.getProductId(),
				requiredLetPartSpecDetailsDto.getInspectionProgramId(),
				requiredLetPartSpecDetailsDto.getInspectionParamId(), 1)).thenReturn("352267085610672???");
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, pp.getProcessPointId()))
				.thenReturn(productCheckPropertyBeanMock);
		when(productCheckPropertyBeanMock.isTrimSpecialCharacters()).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		when(installedPartDaoMock.findByKey(installedPartId)).thenReturn(installedPart);

		assertFalse(productCheckUtil.letDataCheck());
	}
	
	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date April, 26 2019
	 * 
	 *       Test installed part (352267085610672-ExtraValue) with Let Result containing
	 *       special Character(352267085610672???). It should match and checker
	 *       should return true
	 * 
	 */
	@Test
	public void letDataCheckWithTrimSpecialCharacterAndSNwithExtravalaue_whenMatches_thenCorrect() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H99KB103331");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		RequiredLetPartSpecDetailsDto requiredLetPartSpecDetailsDto = getRequiredLetPartSpecDetailsDto();
		List<RequiredLetPartSpecDetailsDto> requiredLetPartSpecDetails = new ArrayList<RequiredLetPartSpecDetailsDto>();
		requiredLetPartSpecDetails.add(requiredLetPartSpecDetailsDto);

		InstalledPartId installedPartId = new InstalledPartId();
		installedPartId.setPartName("TCU");
		installedPartId.setProductId(product.getProductId());

		InstalledPart installedPart = new InstalledPart();
		installedPart.setPartSerialNumber("352267085610672-ExtraValue");
		installedPart.setPartName("TCU");

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		LetPartCheckSpecDao letPartCheckSpecDaoMock = PowerMockito.mock(LetPartCheckSpecDao.class);
		LetResultDao letResultDaoMock = PowerMockito.mock(LetResultDao.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);

		when(ServiceFactory.getDao(LetPartCheckSpecDao.class)).thenReturn(letPartCheckSpecDaoMock);
		when(letPartCheckSpecDaoMock.findAllActiveByProductSpecCode(product.getProductSpecCode(),
				product.getProductId(), product.getProductType().toString(), 1)).thenReturn(requiredLetPartSpecDetails);
		when(ServiceFactory.getDao(LetResultDao.class)).thenReturn(letResultDaoMock);
		when(letResultDaoMock.getInspectionResult(product.getProductId(),
				requiredLetPartSpecDetailsDto.getInspectionProgramId(),
				requiredLetPartSpecDetailsDto.getInspectionParamId(), 1)).thenReturn("352267085610672???");
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, pp.getProcessPointId()))
				.thenReturn(productCheckPropertyBeanMock);
		when(productCheckPropertyBeanMock.isTrimSpecialCharacters()).thenReturn(true);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		when(installedPartDaoMock.findByKey(installedPartId)).thenReturn(installedPart);

		assertFalse(productCheckUtil.letDataCheck());
	}

	private RequiredLetPartSpecDetailsDto getRequiredLetPartSpecDetailsDto() {
		RequiredLetPartSpecDetailsDto rdd = new RequiredLetPartSpecDetailsDto();
		rdd.setInspectionParamId(322);
		rdd.setPartName("TCU");
		rdd.setPartId("A000");
		rdd.setSequenceNumber(1);
		return rdd;
	}

	/**
	 * @author Kamlesh Maharjan, HMA
	 * @date April, 26 2019
	 * 
	 *       Test installed part (352267085610672) with Let Result containing
	 *       special Character(352267085610672???). When not trimming the special
	 *       Characters, it's not matching hence checker failing
	 * 
	 */
	@Test
	public void letDataCheckWithOutTrimSpecialCharacter_whenNotMatches_thenCorrect() {
		BaseProduct product = ProductTypeUtil.createProduct("FRAME", "5FNRL6H99KB103331");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP10000");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(product, pp);

		RequiredLetPartSpecDetailsDto requiredLetPartSpecDetailsDto = getRequiredLetPartSpecDetailsDto();
		List<RequiredLetPartSpecDetailsDto> requiredLetPartSpecDetails = new ArrayList<RequiredLetPartSpecDetailsDto>();
		requiredLetPartSpecDetails.add(requiredLetPartSpecDetailsDto);

		InstalledPartId installedPartId = new InstalledPartId();
		installedPartId.setPartName(requiredLetPartSpecDetailsDto.getPartName());
		installedPartId.setProductId(product.getProductId());

		InstalledPart installedPart = new InstalledPart();
		installedPart.setPartSerialNumber("352267085610672");
		installedPart.setPartName("TCU");

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		InstalledPartDao installedPartDaoMock = PowerMockito.mock(InstalledPartDao.class);
		LetPartCheckSpecDao letPartCheckSpecDaoMock = PowerMockito.mock(LetPartCheckSpecDao.class);
		LetResultDao letResultDaoMock = PowerMockito.mock(LetResultDao.class);
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);

		when(ServiceFactory.getDao(LetPartCheckSpecDao.class)).thenReturn(letPartCheckSpecDaoMock);
		when(letPartCheckSpecDaoMock.findAllActiveByProductSpecCode(product.getProductSpecCode(),
				product.getProductId(), product.getProductType().toString(), 1)).thenReturn(requiredLetPartSpecDetails);
		when(ServiceFactory.getDao(LetResultDao.class)).thenReturn(letResultDaoMock);
		when(letResultDaoMock.getInspectionResult(product.getProductId(),
				requiredLetPartSpecDetailsDto.getInspectionProgramId(),
				requiredLetPartSpecDetailsDto.getInspectionParamId(), 1)).thenReturn("352267085610672???");
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, pp.getProcessPointId()))
				.thenReturn(productCheckPropertyBeanMock);
		when(productCheckPropertyBeanMock.isTrimSpecialCharacters()).thenReturn(false);
		when(ServiceFactory.getDao(InstalledPartDao.class)).thenReturn(installedPartDaoMock);
		when(installedPartDaoMock.findByKey(installedPartId)).thenReturn(installedPart);

		assertTrue(productCheckUtil.letDataCheck());
	}
	
	private List<Kickout> createKickout(String productId) {
		List<Kickout> kickoutList = new ArrayList<Kickout>();
		Kickout kickout = new Kickout();
		kickout.setKickoutId(123L);
		kickout.setProductId(productId);
		kickoutList.add(kickout);
		return kickoutList;
	}
	
	private List<QiDefectResult> createQiDefectList(String productId) {
		List<QiDefectResult> qiDefectResultList = new ArrayList<QiDefectResult>();
		QiDefectResult qiDefectResult = new QiDefectResult();
		qiDefectResult.setDefectResultId(123L);
		qiDefectResult.setKickoutId(0L);
		qiDefectResult.setProductId(productId);
		qiDefectResult.setInspectionPartName("inspectionPartName");
		qiDefectResult.setInspectionPartLocationName("inspectionPartLocationName");
		qiDefectResult.setInspectionPart2Location2Name("inspectionPart2Location2Name");
		qiDefectResult.setInspectionPart2Name("inspectionPart2Name");
		qiDefectResult.setInspectionPart2LocationName("inspectionPart2LocationName");
		qiDefectResult.setInspectionPart2Location2Name("inspectionPart2Location2Name");
		qiDefectResult.setInspectionPart3Name("inspectionPart3Name");
		qiDefectResult.setDefectTypeName("defectTypeName");
		qiDefectResult.setDefectTypeName2("defectTypeName2");
		
		qiDefectResultList.add(qiDefectResult);
		return qiDefectResultList;
	}
	
	private List<QiDefectResult> createQiDefectListWithAssociatedKickout(String productId) {
		List<QiDefectResult> qiDefectResultList = createQiDefectList(productId);
		for(int x = 0; x < qiDefectResultList.size(); x++) {
			qiDefectResultList.get(x).setKickoutId(x + 1);
		}
		return qiDefectResultList;
	}
	
	/**
	 * prepare mocks for stragglerCheck
	 */
	private void createStragglerCheckMocks() {
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(ServiceFactory.class);
		String processPointId = "PP_NAQ_QG_FINAL";
		String afOnProcessPointId = "PP10067";
		
		ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
		when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, processPointId)).thenReturn(productCheckPropertyBeanMock);
		when(productCheckPropertyBeanMock.isIncludeStragglersForSpecCheck()).thenReturn(true);
		
		FrameLinePropertyBean FrameLinePropertyBeanMock = PowerMockito.mock(FrameLinePropertyBean.class);
		when(PropertyService.getPropertyBean(FrameLinePropertyBean.class)).thenReturn(FrameLinePropertyBeanMock);
		when(FrameLinePropertyBeanMock.getAfOnProcessPointId()).thenReturn(afOnProcessPointId);
		
		Sequence seq = new Sequence();
		seq.setEndSeq(9999);
		SequenceDao seqDaoMock = PowerMockito.mock(SequenceDao.class);
		when(ServiceFactory.getDao(SequenceDao.class)).thenReturn(seqDaoMock);
		when(seqDaoMock.findByKey(Matchers.anyString())).thenReturn(seq);
	}
	
	/**
	 * @author Subu Kathiresan
	 * @date July 13, 2022
	 * 
	 * Tests happy path
	 * 
	 * 19XZE4F96NE016880, is the first in the lot and is not a straggler
	 * 
	 */
	@Test
	public void stragglerCheck_notAStraggler_firstInLot() {
		
		createStragglerCheckMocks();
		
		Frame frame = (Frame) ProductTypeUtil.createProduct("FRAME", "19XZE4F96NE016880");
		frame.setKdLotNumber("HMI 01202206024201");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP_NAQ_QG_FINAL");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(frame, pp);
				
		FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
				
		List<Object[]> frameSeqList = new ArrayList<Object[]>();
		for (int i = 0, j = 2200; i < 1; i++) {
			Object[] arr = new Object[2];
			arr[0] = "19XZE4F96NE01688" + i;
			arr[1] = j++;
			frameSeqList.add(arr);
		}

		when(frameDaoMock.findVinsInKdLotSortByAfOnTimestamp(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(frameSeqList);		
		assertFalse(productCheckUtil.stragglerCheck());
	}
	
	/**
	 * @author Subu Kathiresan
	 * @date July 13, 2022
	 * 
	 * Tests happy path
	 * 
	 * 19XZE4F96NE016885 is the 5th product in the lot and is not a straggler
	 * 
	 */
	@Test
	public void stragglerCheck_notAStraggler_middleOfLot() {
		
		createStragglerCheckMocks();
		
		Frame frame = (Frame) ProductTypeUtil.createProduct("FRAME", "19XZE4F96NE016885");
		frame.setKdLotNumber("HMI 01202206024201");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP_NAQ_QG_FINAL");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(frame, pp);
				
		FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
				
		List<Object[]> frameSeqList = new ArrayList<Object[]>();
		for (int i = 0, j = 2200; i < 6; i++) {
			Object[] arr = new Object[2];
			arr[0] = "19XZE4F96NE01688" + i;
			arr[1] = j++;
			frameSeqList.add(arr);
		}

		when(frameDaoMock.findVinsInKdLotSortByAfOnTimestamp(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(frameSeqList);		
		assertFalse(productCheckUtil.stragglerCheck());
	}
	
	/**
	 * @author Subu Kathiresan
	 * @date July 13, 2022
	 * 
	 * Tests happy path
	 *
	 * 19XZE4F96NE016830 is the last product in the lot and not a straggler
	 * 
	 */
	@Test
	public void stragglerCheck_notAStraggler_endOfLot() {
		
		createStragglerCheckMocks();
		
		Frame frame = (Frame) ProductTypeUtil.createProduct("FRAME", "19XZE4F96NE016830");
		frame.setKdLotNumber("HMI 01202206024201");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP_NAQ_QG_FINAL");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(frame, pp);
				
		FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
				
		List<Object[]> frameSeqList = new ArrayList<Object[]>();
		for (int i = 0, j = 2200; i < 6; i++) {
			Object[] arr = new Object[2];
			arr[0] = StringUtil.padRight(("19XZE4F96NE0168" + i), 17, '0', false);
			arr[1] = j++;
			frameSeqList.add(arr);
		}

		when(frameDaoMock.findVinsInKdLotSortByAfOnTimestamp(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(frameSeqList);		
		assertFalse(productCheckUtil.stragglerCheck());
	}
	
	/**
	 * @author Subu Kathiresan
	 * @date July 13, 2022
	 * 
	 * This VIN is the first straggler in the lot
	 * 80 - 2200
	 * 81 - 2201
	 * 82 - 2202 
	 * 83 - 2203 
	 * 84 - 2204 
	 * 85 - 2300 (S)
	 * 
	 * 19XZE4F96NE016885 is a straggler
	 * 
	 */
	@Test
	public void stragglerCheck_straggler_firstStraggler() {
		
		createStragglerCheckMocks();
		
		Frame frame = (Frame) ProductTypeUtil.createProduct("FRAME", "19XZE4F96NE016885");
		frame.setKdLotNumber("HMI 01202206024201");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP_NAQ_QG_FINAL");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(frame, pp);
				
		FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
				
		List<Object[]> frameSeqList = new ArrayList<Object[]>();
		for (int i = 0, j = 2200; i < 6; i++) {
			Object[] arr = new Object[2];
			arr[0] = "19XZE4F96NE01688" + i;
			if (i == 5) {
				arr[1] = 2300;
			} else {
				arr[1] = j++;
			}
			frameSeqList.add(arr);
		}

		when(frameDaoMock.findVinsInKdLotSortByAfOnTimestamp(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(frameSeqList);		
		assertTrue(productCheckUtil.stragglerCheck());
	}
	
	/**
	 * @author Subu Kathiresan
	 * @date July 13, 2022
	 * 
	 * This VIN is the 4th straggler in the lot
	 * 80 - 2200
	 * 81 - 2201
	 * 82 - 2300 (S)
	 * 83 - 2301 (S)
	 * 84 - 2302 (S)
	 * 85 - 2303 (S)
	 * 
	 * 19XZE4F96NE016885 is a straggler
	 * 
	 */
	@Test
	public void stragglerCheck_straggler_notFirstStraggler() {
		
		createStragglerCheckMocks();
		
		Frame frame = (Frame) ProductTypeUtil.createProduct("FRAME", "19XZE4F96NE016885");
		frame.setKdLotNumber("HMI 01202206024201");
		ProcessPoint pp = new ProcessPoint();
		pp.setProcessPointId("PP_NAQ_QG_FINAL");
		ProductCheckUtil productCheckUtil = new ProductCheckUtil(frame, pp);
				
		FrameDao frameDaoMock = PowerMockito.mock(FrameDao.class);
		when(ServiceFactory.getDao(FrameDao.class)).thenReturn(frameDaoMock);
				
		List<Object[]> frameSeqList = new ArrayList<Object[]>();
		for (int i = 0, j = 2200; i < 6; i++) {
			Object[] arr = new Object[2];
			arr[0] = "19XZE4F96NE01688" + i;
			if (i == 2) {
				j = 2300;
			}
			arr[1] = j++;
			frameSeqList.add(arr);
		}

		when(frameDaoMock.findVinsInKdLotSortByAfOnTimestamp(Matchers.anyString(), Matchers.anyString(), Matchers.anyString())).thenReturn(frameSeqList);		
		assertTrue(productCheckUtil.stragglerCheck());
	}
}
