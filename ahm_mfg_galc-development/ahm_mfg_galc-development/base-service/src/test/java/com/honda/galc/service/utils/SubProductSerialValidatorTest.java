package com.honda.galc.service.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.validation.PartSerialValidatorServiceImpl;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SubproductUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PropertyService.class, ProductCheckUtil.class})
public class SubProductSerialValidatorTest {
	HeadlessDataCollectionContext context = new HeadlessDataCollectionContextStub();

	@Mock
	 HeadLessPropertyBean headLessPropertyBeanMock = PowerMockito.mock(HeadLessPropertyBean.class);
	@Mock
	PartSerialValidatorServiceImpl partSerialValidatorMock = PowerMockito.mock(PartSerialValidatorServiceImpl.class);
	@Mock
	EngineUtil engineUtilMock = PowerMockito.mock(EngineUtil.class);
	@Mock
	 SubproductPropertyBean subProductPropertyBeanMock = PowerMockito.mock(SubproductPropertyBean.class);
	@Mock
	 ProductCheckUtil productCheckUtilMock = PowerMockito.mock(ProductCheckUtil.class);
	@Mock
	 SubproductUtil subProductUtilMock = PowerMockito.mock(SubproductUtil.class);
	@Mock
	ProductCheckPropertyBean productCheckPropertyBeanMock = PowerMockito.mock(ProductCheckPropertyBean.class);
	
	@InjectMocks
	private SubProductSerialValidator subProductValidator = new SubProductSerialValidator(getContext(), getEnginePartSpec(), getEngineLotControlRule(), getPartSerialNumber());

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		PowerMockito.mockStatic(PropertyService.class);
		PowerMockito.when(PropertyService.getPropertyBean(HeadLessPropertyBean.class, "PP10072")).thenReturn(headLessPropertyBeanMock);
		PowerMockito.when(PropertyService.getPropertyBean(ProductCheckPropertyBean.class, "PP10072")).thenReturn(productCheckPropertyBeanMock);
		
		PowerMockito.whenNew(EngineUtil.class).withAnyArguments().thenReturn(engineUtilMock);
		PowerMockito.whenNew(ProductCheckUtil.class).withAnyArguments().thenReturn(productCheckUtilMock);
		PowerMockito.whenNew(SubproductUtil.class).withAnyArguments().thenReturn(subProductUtilMock);
		PowerMockito.when(PropertyService.getPropertyBean(SubproductPropertyBean.class, "PP10072")).thenReturn(subProductPropertyBeanMock);
		PowerMockito.when(subProductPropertyBeanMock.getInstallProcessPointMap()).thenReturn(getMap());
		PowerMockito.when(subProductPropertyBeanMock.isUseMainNoFromPartSpec()).thenReturn(false);
	}

	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is already assigned to another Product
	 */
	@Test
	public void validateEngine_duplicateEngineAssigned() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", getCheckTypes())).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);

		boolean result = subProductValidator.validateEngine(new Engine());
		assertEquals("06", context.getErrorCode().getCode());
		assertFalse(result);
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is not currently assigned to a Product
	 */
	@Test
	public void validateEngine_uniqueEngineAssigned() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);

		subProductValidator.validateEngine(new Engine());
		assertFalse(context.getErrorCode().getCode().equals("06"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is being assigned to a Product other than Frame
	 */
	@Test
	public void validateEngine_productNotFrame() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getMbpn());
		
		subProductValidator.validateEngine(getEngine());
		Mockito.verify(productCheckPropertyBeanMock, Mockito.times(0)).isUseAltEngineMto();
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is being assigned to a Frame Product
	 */
	@Test
	public void validateEngine_productIsFrame() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		Mockito.verify(productCheckPropertyBeanMock, Mockito.times(1)).isUseAltEngineMto();
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is the incorrect spec for the Product that it is being assigned to
	 */
	@Test
	public void validateEngine_invalidEngineSpec() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(false);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertEquals("08", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is the correct spec for the Product that it is being assigned to
	 */
	@Test
	public void validateEngine_validEngineSpec() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertFalse(context.getErrorCode().getCode().equals("08"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is on hold
	 */
	@Test
	public void validateEngine_engineOnHold() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(getHoldList());
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertEquals("18", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is not on hold
	 */
	@Test
	public void validateEngine_engineNotOnHold() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertFalse(context.getErrorCode().getCode().equals("18"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is not in a invalid tracking status to be assigned
	 */
	@Test
	public void validateEngine_engineHasInvalidTrackingStatus() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072",new String[0])).thenReturn(null);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(null);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertEquals("19", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine is not in a valid tracking status to be assigned
	 */
	@Test
	public void validateEngine_engineHasValidTrackingStatus() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertFalse(context.getErrorCode().getCode().equals("19"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine fails product checks
	 */
	@Test
	public void validateEngine_engineProductChecksFail() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[1])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(getHoldList());
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
				
		assertEquals("14", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine passes product checks
	 */
	@Test
	public void validateEngine_engineProductChecksPass() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertFalse(context.getErrorCode().getCode().equals("14"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateEngine method when Engine passes all check and validations
	 */
	@Test
	public void validateEngine_allCheckesPassed() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072", new String[0])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getEngine(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getFrame());
		
		subProductValidator.validateEngine(getEngine());
		
		assertEquals("01", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when getting the install process point id based off the MainNo
	 */
	@Test
	public void validateMbpn_getProcessPointIdWithMainNo() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072", new String[0])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(true);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		PowerMockito.when(subProductPropertyBeanMock.isUseMainNoFromPartSpec()).thenReturn(true);

		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertEquals("PP12345", subProductValidator.getInstallProcessPointId());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when getting the install process point id
	 */
	@Test
	public void validateMbpn_getProcessPointId() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072", new String[0])).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(true);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);

		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertEquals("PP10072", subProductValidator.getInstallProcessPointId());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when MBPN passes all required product checks
	 */
	@Test
	public void validateEngine_MbpnRequiredProductChecksPass() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getMbpn(), "PP10072", getAllCheckTypes())).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(true);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertFalse(context.getErrorCode().getCode().equals("14"));
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when MBPN fails required product checks
	 */
	@Test
	public void validateEngine_MbpnRequiredProductChecksFailed() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getMbpn(), "PP10072", getAllCheckTypes())).thenReturn(getHoldList());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(true);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertEquals("14", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when MBPN has an invalid spec code
	 */
	@Test
	public void validateEngine_MbpnInvalidSpecCode() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getMbpn(), "PP10072", getAllCheckTypes())).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(false);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertEquals("08", context.getErrorCode().getCode());
	}
	
	/**
	 * @author Bradley Brown, HMA
	 * @date Nov ,10 2018
	 * 
	 * Test validateMbpn method when MBPN has a valid spec code
	 */
	@Test
	public void validateEngine_MbpnValidSpecCode() throws Exception {
		PowerMockito.when(productCheckUtilMock.duplicateEngineAssignmentCheck()).thenReturn(false);
		PowerMockito.when(productCheckUtilMock.engineOnHoldCheck()).thenReturn(null);
		PowerMockito.when(engineUtilMock.checkValidPreviousEngineLine(getEngine())).thenReturn(true);
		PowerMockito.when(productCheckUtilMock.checkEngineTypeForEngineAssignment(any(Frame.class), any(Engine.class), any(Boolean.class))).thenReturn(true);
		PowerMockito.when(subProductUtilMock.performSubProductChecks("ENGINE", getMbpn(), "PP10072", getAllCheckTypes())).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.performSubProductChecks("MBPN", getMbpn(), "PP10072")).thenReturn(new ArrayList<String>());
		PowerMockito.when(subProductUtilMock.isValidSpecCode(any(String.class), any(BaseProduct.class), any(String.class))).thenReturn(true);
		PowerMockito.when(productCheckPropertyBeanMock.isUseAltEngineMto()).thenReturn(true);
		
		context.setProduct(getMbpn());
		
		subProductValidator.validateMbpn(getMbpn());
		
		assertFalse(context.getErrorCode().getCode().equals("08"));
	}
	
	private HeadlessDataCollectionContext getContext() {
		context.setProperty(headLessPropertyBeanMock);
		context.setProcessPointId("PP10072");
		context.setLogger(Logger.getLogger("PP10072"));
		context.getProcessPoint();
		context.setProductType(getProductType());
		context.setLotControlRules(Arrays.asList(getEngineLotControlRule()));
		context.setProduct(getEngine());
		
		return context;
	}
	
	private Frame getFrame() {
		Frame  product = new Frame(); 
		product.setProductId("5FNRL6H72KB045786");
		product.setProductSpecCode("SPEC");

		return product;
	}
	
	protected Engine getEngine() {
		Engine engine = new Engine();
		engine.setProductId("J35Y71056715");
		engine.setVin(null);
		engine.setMissionSerialNo(null);
		engine.setEngineFiringFlag((short) 0);
		engine.setTrackingStatus("LINE27");
		engine.setLastPassingProcessPointId("PP10355");
		engine.setMissionStatus(0);
		engine.setActualMissionType(null);	
		return engine;
	}
	
	private MbpnProduct getMbpn() {
		MbpnProduct  product = new MbpnProduct(); 
		product.setProductId("FS18116A02421");
		product.setCurrentProductSpecCode("MBPNSPEC");

		return product;
	}
	
	private ProductType getProductType() {
		return ProductType.getType("FRAME");
	}
	
	private ProcessPoint getProcessPoint() {
		ProcessPoint processPoint = new ProcessPoint();
		processPoint.setProcessPointId("PP10072");
		return processPoint;
	}
	
	private ProductSpec getEngineProductSpec() {
		ProductSpecCodeId id = new ProductSpecCodeId();
		ProductSpecCode productSpecCode = new ProductSpecCode();

		id.setProductSpecCode("ENGINESPEC");
		id.setProductType("ENGINE");
		productSpecCode.setId(id);

		return productSpecCode;
	}
	
	private PartName getPartName() {
		PartName part = new PartName();
		part.setProductTypeName("FRAME");
		part.setSubProductType("ENGINE");
		return part;
	}
	
	private PartSerialNumber getPartSerialNumber() {
		PartSerialNumber partSerialNumber = new PartSerialNumber();
		partSerialNumber.setPartSn("J35Y71056715");
		return partSerialNumber;
	}

	private PartSpec getEnginePartSpec() {
		PartSpec spec = new PartSpec();
		PartSpecId id = new PartSpecId();
		id.setPartId("A000");
		id.setPartName("ENGINE");
		spec.setId(id);

		return spec;
	}

	private LotControlRule getEngineLotControlRule() {
		LotControlRule rule = new LotControlRule();
		LotControlRuleId id = new LotControlRuleId();
		id.setProcessPointId("PP10072");
		id.setModelYearCode("G");
		id.setModelCode("ABC");
		id.setModelTypeCode("DEF");
		id.setModelOptionCode("00");
		id.setPartName("ENGINE");
		id.setProductSpecCode("SPECCODE");
		rule.setId(id);
		rule.setPartName(getPartName());
		rule.setVerificationFlag(1);
		rule.setUnique(true);
		
		List<PartSpec> spec = new ArrayList<PartSpec>();
		spec.add(getEnginePartSpec());
		rule.setParts(spec);

		return rule;
	}
	
	private String[] getCheckTypes() {
		String[] checkTypes = new String[2];
		checkTypes[0] = "Check 1";
		checkTypes[1] = "Check 2";
		return checkTypes;
	}
	
	private List<String>  getHoldList() {
		List<String> holdList = new ArrayList<String>();
		holdList.add("Hold Reason 1");
		holdList.add("Hold Reason 2");
		return holdList;
	}
	
	private Map<String, String> getMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ENGINE", "PP10072");
		map.put("MBPN", "PP10567");
		map.put("SPECC", "PP12345");
		return map;
	}
	
	private String[] getRequiredCheckTypeList() {
		String[] checkTypes = new String[2];
		checkTypes[0] = ProductCheckType.RECURSIVE_INSTALLED_PART_CHECK.name();
		checkTypes[1] = ProductCheckType.OUTSTANDING_PARTS_CHECK.name();
		return checkTypes;
	}
	
	private String[] getCheckTypeList() {
		String[] checkTypes = new String[3];
		checkTypes[0] = ProductCheckType.OUTSTANDING_DEFECTS_CHECK.name();
		checkTypes[1] = ProductCheckType.CHECK_SCRAPPED_EXCEPTIONAL_OUT.name();
		checkTypes[2] = ProductCheckType.PRODUCT_ON_HOLD_CHECK.name();
		return checkTypes;
	}
	
	private String[] getAllCheckTypes() {
		String[] checkTypes = (String[]) ArrayUtils.addAll(getRequiredCheckTypeList(), getCheckTypeList());
		return checkTypes;
	}
}
