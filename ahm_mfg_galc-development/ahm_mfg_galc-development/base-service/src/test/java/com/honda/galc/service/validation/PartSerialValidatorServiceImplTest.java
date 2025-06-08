package com.honda.galc.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;

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

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.InstalledPartId;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.service.validation.PartSerialValidatorServiceImpl;
import com.honda.galc.util.CommonPartUtility;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class, ServiceUtil.class, ProductTypeUtil.class, 
	CommonPartUtility.class, PropertyService.class, ProductTypeCatalog.class})
public class PartSerialValidatorServiceImplTest {

	DefaultDataContainer dc = new DefaultDataContainer();

	@Mock
	private static ProcessPointDao processPointDaoMock = PowerMockito.mock(ProcessPointDao.class);
	private static LotControlRuleDao lotControlRuleDaoMock = PowerMockito.mock(LotControlRuleDao.class); 
	private static BaseProductSpecDao productSpecMock = PowerMockito.mock(BaseProductSpecDao.class);
	private static ProductBuildResultDao productBuildResultDaoMock = PowerMockito.mock(ProductBuildResultDao.class);
	private static SystemPropertyBean systemPropertyBeanMock = PowerMockito.mock(SystemPropertyBean.class);


	@InjectMocks
	PartSerialValidatorServiceImpl partSerialValidationService = new PartSerialValidatorServiceImpl();

	@Before
	public void setUp() throws Exception {		
		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(ServiceFactory.class);
		PowerMockito.mockStatic(ServiceUtil.class);
		PowerMockito.mockStatic(ProductTypeUtil.class);
		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.mockStatic(CommonPartUtility.class);
		PowerMockito.mockStatic(ProductTypeCatalog.class);

		PowerMockito.when(ProductTypeUtil.getProductSpecDao("FRAME")).thenReturn(productSpecMock);
		PowerMockito.when(ProductTypeUtil.getProductBuildResultDao(getProductType())).thenReturn(productBuildResultDaoMock);

		PowerMockito.when(ServiceFactory.getDao(ProcessPointDao.class)).thenReturn(processPointDaoMock);
		PowerMockito.when(ServiceFactory.getDao(LotControlRuleDao.class)).thenReturn(lotControlRuleDaoMock);

		PowerMockito.when(PropertyService.getPartMaskWildcardFormat()).thenReturn("DEFAULT");
		PowerMockito.when(PropertyService.getPropertyBean(SystemPropertyBean.class, "PP10409")).thenReturn(systemPropertyBeanMock);

		PowerMockito.when(CommonPartUtility.verify(any(String.class),any(List.class),any(String.class), any(Boolean.class),
				any(int.class), any(BaseProduct.class), any(Boolean.class))).thenReturn(getPartSpec());
		PowerMockito.when(ProductTypeCatalog.getProductType("FRAME")).thenReturn(getProductType());

	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the PRODUCT_ID tag is missing  
	 * {PROCESS_POINT_ID=PP10409}(PART_SERIAL_NUMBER=HA18I280279}{PRODUCT_TYPE=FRAME}(PART_NAME=SEMI CON}{SEQUENCE=1}
	 */
	@Test(expected = TaskException.class)
	public void init_missingProductId() {
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the PROCESS_POINT_ID tag is missing  
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PART_SERIAL_NUMBER=HA18I280279}{PRODUCT_TYPE=FRAME}(PART_NAME=SEMI CON}{SEQUENCE=1}
	 */
	@Test(expected = TaskException.class)
	public void init_missingProcessPointId() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the PART_SERIAL_NUBER tag is missing  
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{PRODUCT_TYPE=FRAME}(PART_NAME=SEMI CON}{SEQUENCE=1}
	 */
	@Test(expected = TaskException.class)
	public void init_missingPartSn() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
	}

	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the SEQUENC tag is missing  
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{PRODUCT_TYPE=FRAME}(PART_NAME=SEMI CON}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test(expected = TaskException.class)
	public void init_missingPartSequence() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");

		partSerialValidationService.init(dc);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the PRODUCT_TYPE tag is missing  
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PART_NAME=SEMI CON}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test(expected = TaskException.class)
	public void init_missingProductType() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the PART_NAME was not sent 
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void init_goodInputNoPartName() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
		assertEquals("5FNRL6H72KB045786", partSerialValidationService.getPartSerialScenData().getProductId());
		assertEquals("PP10409", partSerialValidationService.getProcessPointId());
		assertEquals("HA18I280279", partSerialValidationService.getPartSerialScenData().getSerialNumber());
		assertEquals("FRAME", partSerialValidationService.getPartSerialScenData().getProductType());
		assertNull(partSerialValidationService.getPartSerialScenData().getPartName());
		assertEquals("1", partSerialValidationService.getSequenceNumber());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test initialization method in the case that the all data was sent 
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void init_goodInput() {
		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");

		partSerialValidationService.init(dc);
		assertEquals("5FNRL6H72KB045786", partSerialValidationService.getPartSerialScenData().getProductId());
		assertEquals("PP10409", partSerialValidationService.getProcessPointId());
		assertEquals("HA18I280279", partSerialValidationService.getPartSerialScenData().getSerialNumber());
		assertEquals("FRAME", partSerialValidationService.getPartSerialScenData().getProductType());
		assertEquals("SEMI CON", partSerialValidationService.getPartSerialScenData().getPartName());
		assertEquals("1", partSerialValidationService.getSequenceNumber());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the process point id does not exist in DB
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10000}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_invalidProcessPoint() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(null);

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10000");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("05", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when a valid process point is sent
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_validProcessPoint() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("PP10409", partSerialValidationService.getProcessPointId());
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when a invalid product id is sent
	 * {PRODUCT_ID=5FNRL6H72KB040000}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_invalidProduct() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(null);
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB040000");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("02", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));		
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when a valid product id is sent
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_validProduct() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals(getFrameProduct(), partSerialValidationService.getProduct());	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the product spec code in not found in the DB
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_productSpecCodeNotFound() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(null);

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertNull(partSerialValidationService.getProductSpec());	
		assertEquals("09", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the product spec code is valid and in the DB
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_validProductSpecCode() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals(getFrameProductSpec(), partSerialValidationService.getProductSpec());	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when no lot control rule is found
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_noLotControlRule() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(null);
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertNull(partSerialValidationService.getRule());
		assertEquals("11", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when a valid lot control rule is found
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_validLotControlRule() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);
		
		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("11"));

	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the incorrect part name is sent compared to lot control
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=ANOTHER PART}
	 */
	@Test
	public void validateSerialNumber_incorrectPartName() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "ANOTHER PART");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("10", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the correct part name is sent compared to lot control
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}{PART_NAME=SEMI CON}
	 */
	@Test
	public void validateSerialNumber_correctPartName() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.PART_NAME.name(), "SEMI CON");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("12"));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when the part same is not sent
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_partNameNotSent() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("12"));
	}

	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when then part serial number sent is invalid
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_invalidPartSerialNumber() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());
		PowerMockito.when(CommonPartUtility.verify(any(String.class),any(List.class),any(String.class), any(Boolean.class),
				any(int.class), any(BaseProduct.class), any(Boolean.class))).thenReturn(null);

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("12", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));
		assertFalse((Boolean) partSerialValidationService.getRetDC().get(TagNames.OVERALL_STATUS.name()));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when then part serial number sent is invalid but the verify flag in lot control if not selected
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_invalidPartSerialNumberNoValidation() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRuleNoVerify());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());
		PowerMockito.when(CommonPartUtility.verify(any(String.class),any(List.class),any(String.class), any(Boolean.class),
				any(int.class), any(BaseProduct.class), any(Boolean.class))).thenReturn(null);

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("12"));
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when then part serial number sent is valid
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_validPartSerialNumber() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRuleNotUnique());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("12"));	
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when then part serial already exist in the DB but the verify flag is not selected
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_duplicatePartCheckNonVerify() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRuleNotUnique());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());
		
		PowerMockito.when(productBuildResultDaoMock.findAllByPartNameAndSerialNumber("SEMI CON", "HA18I280279")).thenReturn(null);

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);
		
		assertFalse(partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()).equals("12"));	
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateSerialNumber method when then part serial number already exist in the DB and the unique flag is selected
	 * {PRODUCT_ID=5FNRL6H72KB045786}(PROCESS_POINT_ID=PP10409}{SEQUENCE=1}(PRODUCT_TYPE=FRAME}{PART_SERIAL_NUMBER=HA18I280279}
	 */
	@Test
	public void validateSerialNumber_duplicatePartCheckUnique() {
		PowerMockito.when(processPointDaoMock.findById(any(String.class))).thenReturn(getProcessPoint());
		PowerMockito.when(ServiceUtil.validateProductId(any(String.class), any(String.class), any(DataContainer.class),
				any(String.class))).thenReturn(getFrameProduct());
		PowerMockito.when(lotControlRuleDaoMock.getLotControlRuleByProductSpecCodeProcessId("PP10409", "SPEC", 1)).thenReturn(getLotControlRule());
		PowerMockito.when(productSpecMock.findByProductSpecCode("SPEC", "FRAME")).thenReturn(getFrameProductSpec());
		
		PowerMockito.when(productBuildResultDaoMock.findAllByPartNameAndSerialNumber("SEMI CON", "HA18I280279")).thenReturn(getProductBuildResult());

		dc.put(TagNames.PRODUCT_ID.name(), "5FNRL6H72KB045786");
		dc.put(TagNames.PROCESS_POINT_ID.name(), "PP10409");
		dc.put(TagNames.PART_SERIAL_NUMBER.name(), "HA18I280279");
		dc.put(TagNames.PRODUCT_TYPE.name(), "FRAME");
		dc.put(TagNames.SEQUENCE.name(), "1");
		partSerialValidationService.validateSerialNumber(dc);

		assertEquals("06", partSerialValidationService.getRetDC().getString(TagNames.ERROR_CODE.name()));	
	}

	private BaseProduct getFrameProduct() {
		Frame  product = new Frame(); 
		product.setProductId("5FNRL6H72KB045786");
		product.setProductSpecCode("SPEC");

		return product;
	}

	private ProcessPoint getProcessPoint() {
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("PP10409");
		return processPoint;
	}

	private ProductSpec getFrameProductSpec() {
		ProductSpecCodeId id = new ProductSpecCodeId();
		ProductSpecCode productSpecCode = new ProductSpecCode();

		id.setProductSpecCode("SPEC");
		id.setProductType("FRAME");
		productSpecCode.setId(id);

		return productSpecCode;
	}

	private PartSpec getPartSpec() {
		PartSpec spec = new PartSpec();
		PartSpecId id = new PartSpecId();
		id.setPartId("A000");
		id.setPartName("SEMI CON");
		spec.setId(id);

		return spec;
	}
	
	private List<ProductBuildResult> getProductBuildResult() {
		List<ProductBuildResult> partList = new ArrayList<ProductBuildResult>();
		InstalledPart installedPart = new InstalledPart();
		InstalledPartId id = new InstalledPartId();
		
		id.setPartName("SEMI CON");
		id.setProductId("SOMEPRODUCT");
		installedPart.setId(id);
		installedPart.setPartSerialNumber("HA18I280279");
		
		partList.add(installedPart);
		
		return partList;
	}

	private PartName getPartName() {
		PartName part = new PartName();
		return part;
	}

	private ProductType getProductType() {
		return ProductType.getType("FRAME");
	}

	private List<LotControlRule> getLotControlRule() {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId();
		id.setProcessPointId("PP10409");
		id.setModelYearCode("G");
		id.setModelCode("ABC");
		id.setModelTypeCode("DEF");
		id.setModelOptionCode("00");
		id.setIntColorCode("X");
		id.setExtColorCode("HIJKLM");
		id.setPartName("SEMI CON");
		id.setProductSpecCode("SPEC");
		rule.setId(id);
		rule.setPartName(getPartName());
		rule.setVerificationFlag(1);
		rule.setUnique(true);
		List<PartSpec> spec = new ArrayList<PartSpec>();
		spec.add(getPartSpec());
		rule.setParts(spec);

		rules.add(rule);

		return rules;
	}

	private List<LotControlRule> getLotControlRuleNoVerify() {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId();
		id.setProcessPointId("PP10409");
		id.setModelYearCode("G");
		id.setModelCode("ABC");
		id.setModelTypeCode("DEF");
		id.setModelOptionCode("00");
		id.setIntColorCode("X");
		id.setExtColorCode("HIJKLM");
		id.setPartName("SEMI CON");
		id.setProductSpecCode("SPEC");
		rule.setId(id);
		rule.setPartName(getPartName());
		rule.setVerificationFlag(0);
		List<PartSpec> spec = new ArrayList<PartSpec>();
		spec.add(getPartSpec());
		rule.setParts(spec);

		rules.add(rule);

		return rules;
	}

	private List<LotControlRule> getLotControlRuleNotUnique() {
		List<LotControlRule> rules = new ArrayList<LotControlRule>();
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId();
		id.setProcessPointId("PP10409");
		id.setModelYearCode("G");
		id.setModelCode("ABC");
		id.setModelTypeCode("DEF");
		id.setModelOptionCode("00");
		id.setIntColorCode("X");
		id.setExtColorCode("HIJKLM");
		id.setPartName("SEMI CON");
		id.setProductSpecCode("SPEC");
		rule.setId(id);
		rule.setPartName(getPartName());
		rule.setVerificationFlag(1);
		rule.setUnique(false);
		List<PartSpec> spec = new ArrayList<PartSpec>();
		spec.add(getPartSpec());
		rule.setParts(spec);

		rules.add(rule);

		return rules;
	}
}
