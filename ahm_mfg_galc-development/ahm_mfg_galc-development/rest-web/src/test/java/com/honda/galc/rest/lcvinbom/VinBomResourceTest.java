package com.honda.galc.rest.lcvinbom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.ClassPathResource;

import com.honda.galc.dao.lcvinbom.DesignChangeRuleDao;
import com.honda.galc.dao.lcvinbom.LetCategoryCodeDao;
import com.honda.galc.dao.lcvinbom.LetPartialCheckDao;
import com.honda.galc.dao.lcvinbom.LotPartDao;
import com.honda.galc.dao.lcvinbom.ModelLotDao;
import com.honda.galc.dao.lcvinbom.ModelPartApprovalDao;
import com.honda.galc.dao.lcvinbom.ModelPartDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dao.lcvinbom.VinPartApprovalDao;
import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.dto.IDto;
import com.honda.galc.dto.lcvinbom.BeamPartInputDto;
import com.honda.galc.dto.lcvinbom.DcmsDto;
import com.honda.galc.dto.lcvinbom.LetCategoryCodeDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotDto;
import com.honda.galc.dto.lcvinbom.PartsByProductDto;
import com.honda.galc.dto.lcvinbom.VinBomPartDto;
import com.honda.galc.dto.lcvinbom.VinBomPartSetDto;
import com.honda.galc.dto.lcvinbom.VinPartDto;
import com.honda.galc.entity.lcvinbom.LetCategoryCode;
import com.honda.galc.entity.lcvinbom.LetPartialCheck;
import com.honda.galc.entity.lcvinbom.LetPartialCheckId;
import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelLotId;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinBomPartId;
import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.entity.lcvinbom.VinPartId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.VinBomService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ServiceFactory.class,VinBomService.class})
public class VinBomResourceTest {

	@Mock
	protected ModelPartDao modelPartDao;
	@Mock
	protected ModelLotDao modelLotDao;
	@Mock
	protected VinBomPartDao vinBomPartDao;
	@Mock
	protected LotPartDao lotPartDao;
	@Mock
	protected VinPartApprovalDao vinPartApprovalDao;
	@Mock
	protected ModelPartApprovalDao modelPartApprovalDao;
	@Mock
	protected LetPartialCheckDao letPartialCheckDao;
	@Mock
	protected LetCategoryCodeDao letCategoryCodeDao;
	@Mock
	protected DesignChangeRuleDao designChangeRuleDao;
	@Mock
	protected FrameDao frameDao;
	@Mock
	protected VinPartDao vinPartDao;
	@Mock
	protected FrameSpecDao frameSpecDao;
	@Mock
	private VinBomService mockService;
	
	@Mock
	private HttpHeaders headers;
	
	VinBomResource vinBomResource;

	
	private static Properties configProps = new Properties();
	
	@BeforeClass 
	public static void steupBeforeClass() {
		try {
			InputStream iStream = new ClassPathResource("vinbom-resource-test.properties").getInputStream();
			configProps.load(iStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		PowerMockito.mockStatic(ServiceFactory.class);
		when(ServiceFactory.getService((VinBomService.class))).thenReturn(mockService);
		when(ServiceFactory.getDao((DesignChangeRuleDao.class))).thenReturn(designChangeRuleDao);
		when(ServiceFactory.getDao(VinBomPartDao.class)).thenReturn(vinBomPartDao);
		when(ServiceFactory.getDao(FrameSpecDao.class)).thenReturn(frameSpecDao);
		when(ServiceFactory.getDao(ModelPartDao.class)).thenReturn(modelPartDao);
		when(ServiceFactory.getDao(ModelPartApprovalDao.class)).thenReturn(modelPartApprovalDao);
		when(ServiceFactory.getDao(ModelLotDao.class)).thenReturn(modelLotDao);
		when(ServiceFactory.getDao(LetPartialCheckDao.class)).thenReturn(letPartialCheckDao);
		when(ServiceFactory.getDao(LetCategoryCodeDao.class)).thenReturn(letCategoryCodeDao);
		when(ServiceFactory.getDao(VinPartApprovalDao.class)).thenReturn(vinPartApprovalDao);
		when(ServiceFactory.getDao(VinPartDao.class)).thenReturn(vinPartDao);
		when(ServiceFactory.getDao(ModelLotDao.class)).thenReturn(modelLotDao);
		when(headers.getHeaderString("userId")).thenReturn("user");
		
		List<VinBomPart> returnList = new ArrayList<VinBomPart>();
		VinBomPart vinBomPart1 = new VinBomPart();
		VinBomPartId vinBomPartId = new VinBomPartId();
		vinBomPartId.setProductSpecCode(configProps.getProperty("productSpecCode"));
		vinBomPartId.setLetSystemName(configProps.getProperty("letSystemName"));
		vinBomPart1.setId(vinBomPartId);
		vinBomPart1.setBasePartNumber(configProps.getProperty("basePartNumber"));
		returnList.add(vinBomPart1);

		VinBomPart vinBomPart2 = new VinBomPart();
		VinBomPartId vinBomPartId2 = new VinBomPartId();
		vinBomPartId2.setProductSpecCode(configProps.getProperty("productSpecCode1"));
		vinBomPartId2.setLetSystemName(configProps.getProperty("letSystemName1"));
		vinBomPart2.setId(vinBomPartId2);
		vinBomPart2.setBasePartNumber(configProps.getProperty("basePartNumber1"));
		returnList.add(vinBomPart2);
		when(vinBomPartDao.findAll()).thenReturn(returnList);
		when(vinBomPartDao.findDistinctPartNumberBySystemName(anyString())).thenReturn(returnList);
		
		List<String> modelYearCodes = new ArrayList<String>();
		modelYearCodes.add(configProps.getProperty("modelyearcode1"));
		modelYearCodes.add(configProps.getProperty("modelyearcode2"));
		List<String> modesCodes = new ArrayList<String>();
		modesCodes.add(configProps.getProperty("modelcode1"));
		modesCodes.add(configProps.getProperty("modelcode2"));
		when(ServiceFactory.getDao(FrameSpecDao.class).findModelCodes(modelYearCodes)).thenReturn(modesCodes);
	
		ModelPartApproval modelPartApproval = new ModelPartApproval();
		modelPartApproval.setModelPartApprovalId(Long.parseLong(configProps.getProperty("modelPartApprovalId")));
		modelPartApproval.setApproveAssociateNumber(configProps.getProperty("associateNumber"));
		when(modelPartApprovalDao.findByKey(Long.parseLong(configProps.getProperty("modelPartApprovalId")))).thenReturn(modelPartApproval);
		
		List<LetPartialCheck> list = new ArrayList<>();
		LetPartialCheck letPartialCheck = new LetPartialCheck();
		LetPartialCheckId id = new LetPartialCheckId();
		id.setCategoryCodeId(Long.parseLong(configProps.getProperty("categoryCodeId")));
		id.setLetInspectionName(configProps.getProperty("letInspectionName"));
		letPartialCheck.setId(id);
		list.add(letPartialCheck);
		when(ServiceFactory.getDao(LetPartialCheckDao.class).findAll()).thenReturn(list);
		
		List<LetCategoryCode>  catagoryCodeList = new ArrayList<>();
		LetCategoryCode code = new LetCategoryCode();
		code.setCategoryCodeId(Long.parseLong(configProps.getProperty("categoryCodeId")));
		catagoryCodeList.add(code);
		when(ServiceFactory.getDao(LetCategoryCodeDao.class).findAll()).thenReturn(catagoryCodeList);
		
		List<VinPartApproval> vinPartApprovalList = new ArrayList<VinPartApproval>();
		VinPartApproval vinPartApproval = new VinPartApproval();
		vinPartApproval.setVinPartApprovalId(Long.parseLong(configProps.getProperty("vinPartApprovalId")));
		vinPartApprovalList.add(vinPartApproval);
		when(ServiceFactory.getDao(VinPartApprovalDao.class).findAllPending()).thenReturn(vinPartApprovalList);
		
		List<ModelPart> modelPartList = new ArrayList<>();
		ModelPart modelPart = new ModelPart();
		modelPart.setModelPartId(Long.parseLong(configProps.getProperty("modelPartId")));
		modelPartList.add(modelPart);
		when(ServiceFactory.getDao(ModelPartDao.class).findAllActiveInterchangeble()).thenReturn(modelPartList);
	
		List<String> letSystemNameList = new ArrayList<>();
		letSystemNameList.add(configProps.getProperty("letSystemName"));
		when(ServiceFactory.getDao(VinBomPartDao.class).findDistinctLetSystemName()).thenReturn(letSystemNameList);
		
		
		List<VinPartId> idList = new  ArrayList<>();
		VinPartId vinPartId = new  VinPartId();
		vinPartId.setProductId(configProps.getProperty("productId"));
		vinPartId.setLetSystemName(configProps.getProperty("systemName"));
		idList.add(vinPartId);
		when(ServiceFactory.getDao(VinPartDao.class).getPartNumber(anyString(), anyString())).thenReturn(idList);
		
	
	}
	/**
	 * Test method for {@link com.honda.galc.rest.lcvinbom.VinBomResource#validateRuleSelection(java.lang.String, int, int, int)}.
	 */
	@Test
	public void testValidClassValidateRuleSelection() {
		vinBomResource =  new VinBomResource();
		int value = Integer.parseInt(configProps.getProperty("VinBomDesignChangeRuleRequired"));
		Boolean result = vinBomResource.validateRuleSelection(headers,configProps.getProperty("junit.class"), value,
				value, value);
		assertFalse(result);
	}

	
	@Test
	public void testVinBomResourceService() {
		vinBomResource =  new VinBomResource();
		assertNotNull(vinBomResource);
	}

	@Test
	public void testGetAllModelYearCodes() {
		vinBomResource =  new VinBomResource();
		List<String> modelYearCodes = new ArrayList<String>();
		modelYearCodes.add(configProps.getProperty("modelyearcode1"));
		modelYearCodes.add(configProps.getProperty("modelyearcode2"));
		List<String> modelCodes = vinBomResource.getModelCodesByModelYearCode(headers,modelYearCodes);
		assertNotNull(modelCodes);
		assertEquals(2, modelCodes.size());
	}
	
	@Test
	public void testApproveVinPartChange(){
		vinBomResource = new VinBomResource();
		VinPartApproval vinApproval = new VinPartApproval();
		vinApproval.setVinPartApprovalId(Long.parseLong(configProps.getProperty("vinPartApprovalId")));
		vinApproval.setApproveAssociateNumber(configProps.getProperty("approveAssociateNumber"));
		vinBomResource.approveVinPartChange(headers,vinApproval);
		verify(mockService).approveVinPartChange(Long.parseLong(configProps.getProperty("vinPartApprovalId")),
				configProps.getProperty("approveAssociateNumber"));
	}
	
	@Test 
	public void testDenyVinPartChange() {
		vinBomResource =  new VinBomResource();
		vinBomResource.deleteModelPartAssignment(headers,Long.parseLong(configProps.getProperty("modelPartId")),configProps.getProperty("planCode"), configProps.getProperty("productionLot"));
		verify(mockService).deleteModelPartAssignment(Long.parseLong(configProps.getProperty("modelPartId")),configProps.getProperty("planCode"), configProps.getProperty("productionLot"), "user");
	}
	
	
	@Test 
	public void testFindAllPartsByFilter() {
		vinBomResource =  new VinBomResource();
		VinBomPartDto vinBomPartDto = new VinBomPartDto();
		vinBomPartDto.setBasePartNumber(configProps.getProperty("basePartNumber"));
		vinBomPartDto.setDcPartNumber(configProps.getProperty("dcPartNumber"));
		List<VinBomPartDto> vinBomPartDtoList = vinBomResource.findAllPartsByFilter(headers,vinBomPartDto);
		assertNotNull(vinBomPartDtoList);
	}
	
	@Test 
	public void testCreateVinBomRules() {
		vinBomResource =  new VinBomResource();
		VinBomPartDto vinBomPartDto = new VinBomPartDto();
		
		
		vinBomPartDto.setBasePartNumber(configProps.getProperty("basePartNumber"));
		vinBomPartDto.setDcPartNumber(configProps.getProperty("dcPartNumber"));
		vinBomPartDto.setProductSpecCode(configProps.getProperty("productSpecCode"));
		vinBomPartDto.setLetSystemName(configProps.getProperty("letSystemName"));
		
		VinBomPartDto[] vinBomPartDtoList = new VinBomPartDto[] {vinBomPartDto};
		VinBomPartSetDto partSet = new VinBomPartSetDto();
		partSet.setVinBomPartList(vinBomPartDtoList);
		partSet.setAssociate("test_associate");
		List<ModelPart> modelPartList = vinBomResource.createVinBomRules(headers,partSet);
		assertNotNull(modelPartList);
		
	}
	
	
	@Test 
	public void testFindPartsByFilter() {
		vinBomResource =  new VinBomResource();
		VinBomPartDto vinBomPartDto = new VinBomPartDto();
		vinBomPartDto.setBasePartNumber(configProps.getProperty("basePartNumber"));
		vinBomPartDto.setDcPartNumber(configProps.getProperty("dcPartNumber"));
		List<VinBomPartDto> vinBomPartDtoList = vinBomResource.findPartsByFilter(headers,vinBomPartDto);
		assertNotNull(vinBomPartDtoList);
	}

	
	@Test 
	public void testSavePart() {
		vinBomResource =  new VinBomResource();
		VinBomPart vinBomPart = new VinBomPart();
		VinBomPartId vinBomPartId = new VinBomPartId();
		vinBomPartId.setProductSpecCode(configProps.getProperty("productSpecCode"));
		vinBomPartId.setLetSystemName(configProps.getProperty("letSystemName"));
		vinBomPart.setId(vinBomPartId);
		vinBomPart.setBasePartNumber(configProps.getProperty("partNumber"));
		vinBomResource.savePart(headers,vinBomPart);
		verify(vinBomPartDao).save(vinBomPart);	
	}
	
	@Test 
	public void testRemovePart() {
		vinBomResource =  new VinBomResource();
		VinBomPart vinBomPart = new VinBomPart();
		VinBomPartId vinBomPartId = new VinBomPartId();
		vinBomPartId.setProductSpecCode(configProps.getProperty("productSpecCode"));
		vinBomPartId.setLetSystemName(configProps.getProperty("letSystemName"));
		vinBomPart.setId(vinBomPartId);
		vinBomPart.setBasePartNumber(configProps.getProperty("partNumber"));
		vinBomResource.removePart(headers,vinBomPartId);
		verify(vinBomPartDao).removeByKey(any(VinBomPartId.class));
	}
	
	@Test 
	public void testGetPendingModelPartApprovals() {
		vinBomResource =  new VinBomResource();
		List<ModelPartApproval> list  = vinBomResource.getPendingModelPartApprovals(headers);
		assertNotNull(list);
	}
	
	@Test 
	public void testApproveModelPartChange() {
		vinBomResource =  new VinBomResource();
		ModelPartApproval modelPartApproval = new ModelPartApproval();
		modelPartApproval.setModelPartApprovalId(Long.parseLong(configProps.getProperty("modelPartApprovalId")));
		modelPartApproval.setApproveAssociateNumber(configProps.getProperty("associateNumber"));
		vinBomResource.approveModelPartChange(headers,modelPartApproval);
		verify(mockService).approveModelPartChange(Long.parseLong(configProps.getProperty("modelPartApprovalId")),
				configProps.getProperty("associateNumber"));
	}
	
	@Test 
	public void testDenyModelPartChange() {
		vinBomResource =  new VinBomResource();
		ModelPartApproval modelPartApproval = new ModelPartApproval();
		modelPartApproval.setModelPartApprovalId(Long.parseLong(configProps.getProperty("modelPartApprovalId")));
		modelPartApproval.setApproveAssociateNumber(configProps.getProperty("approveAssociateNumber"));
		vinBomResource.denyModelPartChange(headers,modelPartApproval);
		verify(mockService).denyModelPartChange(Long.parseLong(configProps.getProperty("modelPartApprovalId")),
				configProps.getProperty("approveAssociateNumber"));
	}
	
	@Test 
	public void testGetDesignChange() {
		vinBomResource =  new VinBomResource();
		List<DcmsDto> dcmsDto = vinBomResource.getDesignChange(headers,configProps.getProperty("plantLocCode"),
				configProps.getProperty("designChangeNumber"));
		assertNotNull(dcmsDto);
	}
	
	@Test
	public void testValidateRuleSelection() {
		vinBomResource =  new VinBomResource();
		boolean result = vinBomResource.validateRuleSelection(headers,configProps.getProperty("dcClass"),
				Integer.parseInt(configProps.getProperty("reflash")),
				Integer.parseInt(configProps.getProperty("interchangeable")), 
				Integer.parseInt(configProps.getProperty("scrapParts")));
		
		assertFalse(result);
	}



	@Test
	public void testFindAssignedInspectionsByCategoryCode() {
		vinBomResource =  new VinBomResource();
		List<LetPartialCheck>  list = vinBomResource.findAssignedInspectionsByCategoryCode(headers,
				Long.parseLong(configProps.getProperty("categoryCodeId")));
		assertNotNull(list);
	}

	@Test
	public void testGetAllCategoryCodes() {
		vinBomResource =  new VinBomResource();
		List<LetCategoryCode> catagoryCodeList = vinBomResource.getAllCategoryCodes(headers);
		assertNotNull(catagoryCodeList);
		assertEquals(1, catagoryCodeList.size());
	}


	@Test
	public void testFindAllPendingVinPartApprovals() {
		vinBomResource =  new VinBomResource();
		List<VinPartApproval> vinPartApporvalList = vinBomResource.findAllPendingVinPartApprovals(headers);
		assertNotNull(vinPartApporvalList);
	}

	@Test
	public void testGetVinPartAndStatus() {
		vinBomResource =  new VinBomResource();
		List<VinPartDto> vinPartApporvalList= vinBomResource.getVinPartAndStatus(headers);
		assertNotNull(vinPartApporvalList);
	}

	@Test
	public void testFindDistinctPartNumberBySystemName() {
		vinBomResource =  new VinBomResource();
		List<VinBomPart> list = vinBomResource.findDistinctPartNumberBySystemName(headers,configProps.getProperty("letSystemName"),configProps.getProperty("productId"));
		assertNotNull(list);
		assertEquals(0, list.size());
	}

	@Test
	public void testFindAllActiveInterchangeble() {
		vinBomResource =  new VinBomResource();
		List<ModelPart> modelpartList = vinBomResource.findAllActiveInterchangeble(headers);
		assertNotNull(modelpartList);
		assertEquals(1, modelpartList.size());
	}

	@Test
	public void testFindDistinctLetSystemName() {
		vinBomResource =  new VinBomResource();
		List<String> letSystemNameList = vinBomResource.findDistinctLetSystemName(headers);
		assertNotNull(letSystemNameList);
		assertEquals(1, letSystemNameList.size());
	}

	@Test
	public void testGetAvailableLotAssignments() {
		vinBomResource =  new VinBomResource();
		List<ModelPartLotDto> modelPartLotDtoList = vinBomResource.getAvailableLotAssignments(headers,configProps.getProperty("pendingOnly"));
		assertNotNull(modelPartLotDtoList);
	}

	@Test
	public void testGetLines() {
		vinBomResource =  new VinBomResource();
		List<Object[]> lines = vinBomResource.getLines(headers,configProps.getProperty("plantCode"));
		assertNotNull(lines);
	}

	@Test
	public void testGetPartNumber() {
		vinBomResource =  new VinBomResource();
		IDto vinPartIdList = vinBomResource.getPartNumber(headers,configProps.getProperty("productId"),
				configProps.getProperty("systemName"));
		verify(mockService).getPartsByProductForSystem(configProps.getProperty("productId"),
				configProps.getProperty("systemName"));
		
	}

	@Test
	public void testUpdateBeamPartData() {
		vinBomResource =  new VinBomResource();
		BeamPartInputDto beamPartInputDto = new BeamPartInputDto();
		beamPartInputDto.setDivision(configProps.getProperty("division"));
		beamPartInputDto.setPlantLocCode(configProps.getProperty("plantLocCode"));
		vinBomResource.updateBeamPartData(headers,beamPartInputDto);
		verify(mockService).updateBeamPartData(configProps.getProperty("plantLocCode"),
				configProps.getProperty("division"));
	}

	@Test
	public void testUpdateCategoryInspections() {
		vinBomResource =  new VinBomResource();
		LetCategoryCodeDto letCategoryCodeDto = new LetCategoryCodeDto();
		vinBomResource.updateCategoryInspections(headers,letCategoryCodeDto);
		verify(mockService).updateCategoryInspections(letCategoryCodeDto,"user");
	}

	@Test
	public void testSaveModelPart() {
		vinBomResource =  new VinBomResource();
		ModelPart modelPart = new ModelPart();
		modelPart.setModelPartId(Long.parseLong(configProps.getProperty("modelPartId")));
		vinBomResource.saveModelPart(headers,modelPart);
		verify(modelPartDao).save(any(ModelPart.class));
	}

	@Test
	public void testSaveModelLot() {
		vinBomResource =  new VinBomResource();
		ModelLot modelLot = new ModelLot();
		ModelLotId id = new ModelLotId();
		id.setModelPartId(Long.parseLong(configProps.getProperty("modelPartId")));
		id.setPlanCode(configProps.getProperty("plantCode"));
		modelLot.setId(id);
		vinBomResource.saveModelLot(headers,modelLot);
		verify(modelLotDao).saveModelLot(any(ModelLot.class));
	}

	@Test
	public void testSaveVinPartApproval() {
		vinBomResource =  new VinBomResource();
		VinPartApproval vinPartApproval = new VinPartApproval();
		vinPartApproval.setVinPartApprovalId(Long.parseLong(configProps.getProperty("vinPartApprovalId")));
		vinBomResource.saveVinPartApproval(headers,vinPartApproval);
		//verify(vinPartApprovalDao).save(any(VinPartApproval.class));
		verify(mockService).saveVinPartApproval(vinPartApproval,"user");
	}


	@Test
	public void testSaveModelPartApproval() {
		vinBomResource =  new VinBomResource();
		ModelPartApproval modelPartApproval = new ModelPartApproval();
		modelPartApproval.setModelPartApprovalId(Long.parseLong(configProps.getProperty("modelPartApprovalId")));
		vinBomResource.saveModelPartApproval(headers,modelPartApproval);
		verify(mockService).saveModelPartApproval(modelPartApproval,"user");
	}

	@Test
	public void testSetInterchangableInactive() {
		vinBomResource =  new VinBomResource();
		vinBomResource.setInterchangableInactive(headers,Long.parseLong(configProps.getProperty("modelPartId")));
		verify(mockService).setInterchangableInactive(Long.parseLong(configProps.getProperty("modelPartId")),"user");
	}

	@Test
	public void testPutFlashResults() {
		vinBomResource =  new VinBomResource();
		vinBomResource.putFlashResults(configProps.getProperty("jsonLetVinPart"));
		verify(vinPartDao).putFlashResults(any(DataContainer.class));
	}

	@Test
	public void testRemoveLetCategory() {
		vinBomResource =  new VinBomResource();
		vinBomResource.removeLetCategory(headers,Long.parseLong(configProps.getProperty("categoryCodeId")));
		verify(mockService).removeLetCategory(Long.parseLong(configProps.getProperty("categoryCodeId")),"user");
	}

	@Test
	public void testDeleteModelPartAssignment() {
		vinBomResource =  new VinBomResource();
		vinBomResource.deleteModelPartAssignment(headers,Long.parseLong(configProps.getProperty("modelPartId")),configProps.getProperty("planCode"),configProps.getProperty("productionLot"));
		verify(mockService).deleteModelPartAssignment(Long.parseLong(configProps.getProperty("modelPartId")),configProps.getProperty("planCode"),configProps.getProperty("productionLot"),"user");
		//verify(modelPartApprovalDao).removeByModelPartId(Long.parseLong(configProps.getProperty("modelPartId")));
		//verify(modelLotDao).removeByModelPartId(Long.parseLong(configProps.getProperty("modelPartId")));
		//verify(modelPartDao).removeByKey(Long.parseLong(configProps.getProperty("modelPartId")));
	}

	@Test
	public void testGetVinBomService() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getVinBomService());
	}

	@Test
	public void testGet() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getVinBomService());
	}

	@Test
	public void testGetVinBomPartDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getVinBomPartDao());
	}

	@Test
	public void testGetModelPartDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getModelPartDao());
	}

	@Test
	public void testGetModelLotDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getModelLotDao());
	}

	@Test
	public void testGetModelPartApprovalDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getModelPartApprovalDao());
	}

	@Test
	public void testGetVinPartApprovalDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getVinPartApprovalDao());
	}

	@Test
	public void testGetLetCategoryCodeDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getLetCategoryCodeDao());
	}

	@Test
	public void testGetLetPartialCheckDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getLetPartialCheckDao());
	}

	@Test
	public void testGetFrameSpecDao() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getFrameSpecDao());
	}

	@Test
	public void testGetLogger() {
		vinBomResource = new VinBomResource();
		assertNotNull(vinBomResource.getLogger());
	}
	
	
}
