package com.honda.galc.service.lcvinbom;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.entity.enumtype.ReturnToActiveStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.lcvinbom.DesignChangeRuleDao;
import com.honda.galc.dao.lcvinbom.LetCategoryCodeDao;
import com.honda.galc.dao.lcvinbom.LetPartialCheckDao;
import com.honda.galc.dao.lcvinbom.LotPartDao;
import com.honda.galc.dao.lcvinbom.ModelLotDao;
import com.honda.galc.dao.lcvinbom.ModelPartApprovalDao;
import com.honda.galc.dao.lcvinbom.ModelPartDao;
import com.honda.galc.dao.lcvinbom.SystemRelationshipDao;
import com.honda.galc.dao.lcvinbom.VinBomPartDao;
import com.honda.galc.dao.lcvinbom.VinPartApprovalDao;
import com.honda.galc.dao.lcvinbom.VinPartDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.StragglerDao;
import com.honda.galc.dto.lcvinbom.BeamPartDto;
import com.honda.galc.dto.lcvinbom.DcmsDto;
import com.honda.galc.dto.lcvinbom.LetCategoryCodeDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotFilterDto;
import com.honda.galc.dto.lcvinbom.ModelPartLotRowDto;
import com.honda.galc.dto.lcvinbom.PartsByProductDto;
import com.honda.galc.dto.lcvinbom.PartsDto;
import com.honda.galc.dto.lcvinbom.PendingApprovalDto;
import com.honda.galc.dto.lcvinbom.PmqaDto;
import com.honda.galc.dto.lcvinbom.SystemrelationshipDto;
import com.honda.galc.dto.lcvinbom.VinBomDesignChangeDto;
import com.honda.galc.dto.lcvinbom.VinBomPartDto;
import com.honda.galc.dto.lcvinbom.VinBomPartSetDto;
import com.honda.galc.dto.lcvinbom.VinPartDto;
import com.honda.galc.dto.lcvinbom.VinPartFilterDto;
import com.honda.galc.entity.enumtype.VinBomActiveStatus;
import com.honda.galc.entity.enumtype.VinBomApprovalStatus;
import com.honda.galc.entity.enumtype.VinBomAuditLogChangeType;
import com.honda.galc.entity.enumtype.VinBomDesignChangeRuleRequired;
import com.honda.galc.entity.lcvinbom.DesignChangeRule;
import com.honda.galc.entity.lcvinbom.LetCategoryCode;
import com.honda.galc.entity.lcvinbom.LetPartialCheck;
import com.honda.galc.entity.lcvinbom.LetPartialCheckId;
import com.honda.galc.entity.lcvinbom.LotPart;
import com.honda.galc.entity.lcvinbom.LotPartId;
import com.honda.galc.entity.lcvinbom.ModelLot;
import com.honda.galc.entity.lcvinbom.ModelLotId;
import com.honda.galc.entity.lcvinbom.ModelPart;
import com.honda.galc.entity.lcvinbom.ModelPartApproval;
import com.honda.galc.entity.lcvinbom.SystemRelationship;
import com.honda.galc.entity.lcvinbom.SystemRelationshipId;
import com.honda.galc.entity.lcvinbom.VinBomPart;
import com.honda.galc.entity.lcvinbom.VinBomPartId;
import com.honda.galc.entity.lcvinbom.VinPart;
import com.honda.galc.entity.lcvinbom.VinPartApproval;
import com.honda.galc.entity.lcvinbom.VinPartId;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.SmtpMailPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.lcvinbom.property.VinBomPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.LDAPService;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.StringUtil;
import com.honda.galc.util.lcvinbom.VinBomAuditLoggerUtil;

public class VinBomServiceImpl implements VinBomService {
	
	private Logger logger = Logger.getLogger("VinBomServiceImpl");
	private static final String IMMEDIATE = "IMMEDIATE";
	
	@Autowired
	protected ModelPartDao modelPartDao;
	
	@Autowired
	protected ModelLotDao modelLotDao;
	
	@Autowired
	protected VinBomPartDao vinBomPartDao;
	
	@Autowired
	protected VinPartDao vinPartDao;
	
	@Autowired
	protected LotPartDao lotPartDao;
	
	@Autowired
	protected VinPartApprovalDao vinPartApprovalDao;
	
	@Autowired
	protected ModelPartApprovalDao modelPartApprovalDao;
	
	@Autowired
	protected LetPartialCheckDao letPartialCheckDao;
	
	@Autowired
	protected LetCategoryCodeDao letCategoryCodeDao;
	
	@Autowired
	protected DesignChangeRuleDao designChangeRuleDao;
	
	@Autowired
	protected FrameDao frameDao;
	
	@Autowired
	protected PreProductionLotDao preProductionLotDao;
	
	@Autowired
	protected ProductionLotDao productionLotDao;
	
	@Autowired
	protected StragglerDao stragglerDao;
	
	@Autowired
	protected SystemRelationshipDao systemRelationshipDao;
	
	private static final String VIN_BOM = "VIN_BOM";
  	private static final String DCMS_HEADER_HONDA_MSG_ID = "hondaHeaderType.messageId";
	private static final String DCMS_HEADER_MSG_ID = "messageId";
	private static final String DCMS_HEADER_HONDA_TIMESTAMP = "hondaHeaderType.collectedTimestamp";
	private static final String DCMS_HEADER_TIMESTAMP = "collectedTimestamp";
	
	private static final String GMQA_HEADER_HONDA_MSG_ID = "hondaHeaderType.messageId";
	private static final String GMQA_HEADER_MSG_ID = "messageId";
	private static final String GMQA_HEADER_HONDA_TIMESTAMP = "hondaHeaderType.collectedTimestamp";
	private static final String GMQA_HEADER_TIMESTAMP = "collectedTimestamp";
	
	private static final String GMQA_FILE_DATA = "file_data";
	
	@Override
	public List<ModelPartApproval> getPendingModelPartApprovals() {
		List<ModelPartApproval> modelPartApprovalList = modelPartApprovalDao.getPendingApprovals();
		if(modelPartApprovalList!=null && !modelPartApprovalList.isEmpty()) {
			for(ModelPartApproval modelPartApproval: modelPartApprovalList) {
				modelPartApproval.setRequestAssociateName(getAssociateName(modelPartApproval.getRequestAssociateNumber()));
				ProductionLot currentProductionLot = productionLotDao.findByKey(modelPartApproval.getCurrentStartingProductionLot());
				if(currentProductionLot!=null) {
					modelPartApproval.setCurrentStartingProductionDate(currentProductionLot.getProductionDate());
				}else {
					if(StringUtils.isBlank(modelPartApproval.getCurrentStartingProductionLot())) {
						modelPartApproval.setCurrentStartingProductionLot("-");
					}
				}
				ProductionLot newProductionLot = productionLotDao.findByKey(modelPartApproval.getNewStartingProductionLot());
				if(newProductionLot!=null) {
					modelPartApproval.setNewStartingProductionDate(newProductionLot.getProductionDate());
				}else {
					if(StringUtils.isBlank(modelPartApproval.getNewStartingProductionLot())) {
						modelPartApproval.setNewStartingProductionLot("-");
					}
				}
				ModelPart modelPart = modelPartDao.findByKey(modelPartApproval.getModelPartId());
				if (modelPart != null) {
					List<ModelLot> modelLotList = modelLotDao.getAssignedLots(modelPart.getModelPartId());

					if (modelLotList != null && !modelLotList.isEmpty()) {
						for (ModelLot modelLot : modelLotList) {
							modelPartApproval.setModelLot(modelLot);
						}
					}
				}
				
				if(modelPart!=null) {
					modelPartApproval.setModelPart(modelPart);
					modelPartApproval.setModel(extractModel(modelPart.getProductSpecWildcard()));
					modelPartApproval.setModelType(extractModelType(modelPart.getProductSpecWildcard()));
				}
			}
		}
		return modelPartApprovalList;
	}
	
	@Override
	public List<VinPartApproval> findAllPendingVinPartApprovals() {
		List<VinPartApproval> vinPartApprovalList = vinPartApprovalDao.findAllPending();
		if(vinPartApprovalList!=null && !vinPartApprovalList.isEmpty()) {
			for(VinPartApproval vinPartApproval: vinPartApprovalList) {
				vinPartApproval.setRequestAssociateName(getAssociateName(vinPartApproval.getRequestAssociateNumber()));

				Frame frame = frameDao.findByKey(vinPartApproval.getProductId());
				if (frame != null) {
					vinPartApproval.setProductSpecCode(frame.getProductSpecCode()); 
					vinPartApproval.setProductionLot(frame.getProductionLot());
				} 
			}
		}
		return vinPartApprovalList;
	}
	
	private String getAssociateName(String associateNumber) {
		String associateName = LDAPService.getInstance().getAssociateName(associateNumber);
		return StringUtils.isEmpty(associateName)?associateNumber:associateName;
	}
	
	@Override
	@Transactional
	public void setInterchangableInactive(long modelPartId, String userId) {
		ModelPart modelPart = modelPartDao.findByKey(modelPartId);
		if(modelPart!=null) {
			ModelPart updatedModelPart = modelPartDao.setInterchangableInactive(modelPartId);
			VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPart, updatedModelPart, userId, VinBomAuditLogChangeType.UPDATE);
			lotPartDao.setInterchangableInactive(modelPart.getLetSystemName(), modelPart.getDcPartNumber());
			VinBomAuditLoggerUtil.logAuditInfo(LotPart.class, modelPart, userId, VinBomAuditLogChangeType.UPDATE);
		}
	}
	
	@Override
	@Transactional
	public void updateCategoryInspections(LetCategoryCodeDto letCategoryCodeDto, String userId) {
		if(letCategoryCodeDto!=null) {
			LetCategoryCode letCategoryCode = null;
			if(letCategoryCodeDto.getCategoryCodeId()!=null) {
				letCategoryCode = letCategoryCodeDao.findByKey(letCategoryCodeDto.getCategoryCodeId());
			}
			if(letCategoryCode == null)
				letCategoryCode = new LetCategoryCode();
			letCategoryCode.setName(letCategoryCodeDto.getName());
			letCategoryCode.setDescription(letCategoryCodeDto.getDescription());
			letCategoryCode.setInclusive(letCategoryCodeDto.getInclusive());
			letCategoryCode = letCategoryCodeDao.save(letCategoryCode);
			
			VinBomAuditLoggerUtil.logAuditInfo(LetCategoryCode.class, letCategoryCode, userId, VinBomAuditLogChangeType.INSERT);
			
			List<String> inspectionNames = letCategoryCodeDto.getInspectionNames();
			List<LetPartialCheck> partialChecks = letPartialCheckDao.findAssignedInspectionsByCategoryCode(letCategoryCode.getCategoryCodeId());
			if(inspectionNames!=null && !inspectionNames.isEmpty()) {
			
				//delete removed inspections
				for(LetPartialCheck partialCheck: partialChecks) {
					if(!inspectionNames.contains(partialCheck.getId().getLetInspectionName())) {
						letPartialCheckDao.remove(partialCheck);
						VinBomAuditLoggerUtil.logAuditInfo(LetPartialCheck.class, partialCheck, userId, VinBomAuditLogChangeType.DELETE);
					}
				}
				
				//add new inspectin names		
				for(String inspectionName: inspectionNames) {
					
					LetPartialCheckId letPartialCheckId = new LetPartialCheckId();
					letPartialCheckId.setCategoryCodeId(letCategoryCode.getCategoryCodeId());
					
					LetPartialCheck letPartialCheck = letPartialCheckDao.findByKey(letPartialCheckId);
					if(letPartialCheck == null) {
						letPartialCheck = new LetPartialCheck();
						letPartialCheckId.setLetInspectionName(StringUtils.trim(inspectionName));
						letPartialCheck.setId(letPartialCheckId);
						letPartialCheckDao.save(letPartialCheck);
						VinBomAuditLoggerUtil.logAuditInfo(LetPartialCheck.class, letPartialCheck, userId, VinBomAuditLogChangeType.INSERT);
					}
					
				}
			}
		}
	}
	
	@Override
	public List<LetCategoryCodeDto> getInspectionsByCategoryName(String categoryName) {
		List<LetCategoryCodeDto> letCategoryCodeDtoList = new ArrayList<LetCategoryCodeDto>();
		List<LetCategoryCode> letCategoryCodeList = letCategoryCodeDao.findByCategoryName(categoryName);
		if(letCategoryCodeList!=null && !letCategoryCodeList.isEmpty()) {
			for(LetCategoryCode letCategoryCode: letCategoryCodeList) {
				LetCategoryCodeDto letCategoryCodeDto = new LetCategoryCodeDto();
				letCategoryCodeDto.setCategoryCodeId(letCategoryCode.getCategoryCodeId());
				letCategoryCodeDto.setName(letCategoryCode.getName());
				letCategoryCodeDto.setDescription(letCategoryCode.getDescription());
				letCategoryCodeDto.setInclusive(letCategoryCode.getInclusive());
				List<String> inspectionNames = new ArrayList<String>();
				List<LetPartialCheck> letPartialCheckList = letPartialCheckDao.findAssignedInspectionsByCategoryCode(letCategoryCode.getCategoryCodeId());
				if(letPartialCheckList!=null && !letPartialCheckList.isEmpty()) {
					for(LetPartialCheck letPartialCheck: letPartialCheckList) {
						inspectionNames.add(letPartialCheck.getId().getLetInspectionName());
					}
				}
				letCategoryCodeDto.setInspectionNames(inspectionNames);
				letCategoryCodeDtoList.add(letCategoryCodeDto);
			}
		}
		return letCategoryCodeDtoList;
	}
	
	@Transactional
	public void removeLotPart(String partNumber, String systemName, String productionLot) {
		LotPartId lotPartId = new LotPartId();
		lotPartId.setDcPartNumber(partNumber);
		lotPartId.setLetSystemName(systemName);
		lotPartId.setProductionLot(productionLot); 
		lotPartDao.removeByKey(lotPartId);
	}
	
	@Override
	@Transactional
	public void removeVinPart(String productId, String letSystemName, String dcPartNumber, String userId) {
		VinPartId vinPartId = new VinPartId();
		vinPartId.setProductId(productId);
		vinPartId.setDcPartNumber(dcPartNumber);
		vinPartId.setLetSystemName(letSystemName);
		vinPartDao.removeByKey(vinPartId);
	}
	
	@Override
	@Transactional
	public void removeLetCategory(long categoryCodeId, String userId) {
		letPartialCheckDao.removeByCategoryCode(categoryCodeId);
		VinBomAuditLoggerUtil.logAuditInfo(LetPartialCheck.class,categoryCodeId, userId, VinBomAuditLogChangeType.DELETE);
		letCategoryCodeDao.removeByKey(categoryCodeId);
		VinBomAuditLoggerUtil.logAuditInfo(LetCategoryCode.class,categoryCodeId, userId, VinBomAuditLogChangeType.DELETE);
	}
	
	@Override
	@Transactional
	public void deleteModelPartAssignment(long modelPartId, String planCode, String startingProductionLot, String userId) {
		modelPartApprovalDao.removeByModelPartIdAndProductionLot(modelPartId,startingProductionLot);
		ModelLotId modelLotId = new ModelLotId();
		modelLotId.setModelPartId(modelPartId);
		modelLotId.setPlanCode(planCode);
		modelLotDao.removeByKey(modelLotId);
		List<ModelLot> modelLots = modelLotDao.getAssignedLots(modelPartId);
		if(modelLots == null || modelLots.size() == 0) modelPartDao.removeByKey(modelPartId);
		
		VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelPartId, userId, VinBomAuditLogChangeType.DELETE);
		VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPartId, userId, VinBomAuditLogChangeType.DELETE);
	}

	@Override
	@Transactional
	public void updateBeamPartData(String plantLocCode, String division) {
		String sysNameExclude = getPropertyBean().getSystemExclude();
		
		String[] sysNameExcludeArr =  sysNameExclude.split(",");
		List<BeamPartDto> beamPartDtoList = retrieveBeamPartDataFromGmqa(plantLocCode, division);
		if (!beamPartDtoList.isEmpty()) {
			Map<String, VinBomPart> partsMap = new HashMap<>();
			String key = null;
			for (BeamPartDto beamPartDto : beamPartDtoList) {
				if (((beamPartDto.getSystemName() != null || !beamPartDto.getSystemName().isEmpty())) && (StringUtils.isEmpty(sysNameExclude) || !StringUtils.endsWithAny(beamPartDto.getSystemName(), sysNameExcludeArr))) {
					VinBomPartId vinBomPartId = new VinBomPartId();
					vinBomPartId.setLetSystemName(beamPartDto.getSystemName());
					vinBomPartId.setDcPartNumber(beamPartDto.getDcpn());
					vinBomPartId.setProductSpecCode(beamPartDto.getModelYearCode() + beamPartDto.getModelCode()
							+ beamPartDto.getModelTypeCode());
					key = vinBomPartId.getProductSpecCode() + "-" + vinBomPartId.getLetSystemName() + "-"
							+ vinBomPartId.getDcPartNumber();
					VinBomPart vinBomPart = new VinBomPart();
					vinBomPart.setId(vinBomPartId);
					vinBomPart.setBasePartNumber(beamPartDto.getBpn());
					vinBomPart.setDescription(beamPartDto.getPartName());
					vinBomPart.setEffectiveBeginDate(beamPartDto.getEffectiveBeginDate());
					vinBomPart.setEffectiveEndDate(beamPartDto.getEffectiveEndDate());
					partsMap.put(key, vinBomPart);
				}
			} 
			vinBomPartDao.removeAll();
			for (VinBomPart vinBomPart : partsMap.values())
				vinBomPartDao.insert(vinBomPart); 
		} 
	}

	@Override
	public List<DcmsDto> retrieveDesignChange(String plantLocCode, String designChangeNumber) {
		List<DcmsDto> dcmsDtoList = retrieveDesignChangeFromDcms(plantLocCode, designChangeNumber);
		if (dcmsDtoList != null && !dcmsDtoList.isEmpty()) {
			Set<String> filterKeySet = new HashSet<String>();
			
			List<DcmsDto> filterDcmsList = new ArrayList<DcmsDto>();
			for (DcmsDto dcmsDto : dcmsDtoList) {
				logger.info(dcmsDto.toString());
				String dcpartNumber=StringUtils.isBlank(dcmsDto.getDesignChangePartNumber())?"":dcmsDto.getDesignChangePartNumber().substring(0, 11);
					String letSystemName = vinBomPartDao.getSystemNameByPartNumber(dcpartNumber,dcmsDto.getYmtoCode()+dcmsDto.getModelType());
					logger.info("desigChangePartNumber - systemName : "+ dcmsDto.getDesignChangePartNumber() +"-"+letSystemName);
					String modelType = dcmsDto.getModelType();
					String ymtCode = dcmsDto.getYmtoCode();

					List<ModelPart> modelParts = modelPartDao.filterModalPart(letSystemName, modelType, ymtCode);
					if(StringUtils.isNotBlank(letSystemName) && !StringUtils.equalsIgnoreCase(letSystemName, "*NOT SET*") && !modelParts.isEmpty()) {
						dcmsDto.setLetSystemName(letSystemName);
						//Filter out Design Change Rules already defined
						String filterKey = dcmsDto.getDesignChangeNumber()+"|"+dcmsDto.getDesignChangePartNumber()+"|"+
								dcmsDto.getYmtoCode()+dcmsDto.getModelType()+"|"+dcmsDto.getLetSystemName();
						if(filterKeySet.contains(filterKey)) {
							logger.info("\"Rules for filter key already assigned add dcnumber to removeList - : "+ dcmsDto.getDesignChangePartNumber() +"-"+filterKey);
							filterDcmsList.add(dcmsDto);
						} else {
							List<ModelPart> modelPartList = modelPartDao.filterRulesAlreadyAssigned(dcmsDto.getDesignChangeNumber(), dcmsDto.getDesignChangePartNumber(), 
									dcmsDto.getYmtoCode()+dcmsDto.getModelType(), dcmsDto.getLetSystemName());
							if(modelPartList!=null && !modelPartList.isEmpty()) {
								logger.info("Rules already assigned add to removeList - : "+ dcmsDto.getDesignChangePartNumber() +"-"+filterKey);
								filterDcmsList.add(dcmsDto);
								filterKeySet.add(filterKey);
							}
						}
					} else {
						logger.info("systemName is blank or set to NOT SET add dcnumber  to removeList: "+ dcmsDto.toString());
						filterDcmsList.add(dcmsDto);
						
					}
				
			}
			dcmsDtoList.removeAll(filterDcmsList);
		}
		return dcmsDtoList;
	}

	@Override
	public List<VinPartDto> getVinPartAndStatus() {
		List<VinPartDto> vinPartDtoList = new ArrayList<>();
		List<VinPart> vinPartList = vinPartDao.findAll();
		if (vinPartList != null && !vinPartList.isEmpty())
			for (VinPart vinPart : vinPartList) {
				VinPartDto vinPartDto = new VinPartDto();
				vinPartDto.setProductId(vinPart.getId().getProductId());
				vinPartDto.setLetSystemName(vinPart.getId().getLetSystemName());
				vinPartDto.setDcPartNumber(vinPart.getId().getDcPartNumber());
				vinPartDto.setShipStatus(vinPart.getShipStatus());
				Frame frame = frameDao.findByKey(vinPart.getId().getProductId());
				if (frame != null) {
					vinPartDto.setProductSpecCode(StringUtils.isBlank(frame.getProductSpecCode())?"-":frame.getProductSpecCode());
					vinPartDto.setProductionLot(StringUtils.isBlank(frame.getProductionLot())?"-":frame.getProductionLot());
				} else {
					vinPartDto.setProductSpecCode("-");
					vinPartDto.setProductionLot("-");
				}
				vinPartDtoList.add(vinPartDto);
			}  
		return vinPartDtoList;
	}
	
	public void approveMultiVinPartChange(List<Long> vinPartApprovalIdList, String approveAssociateNumber) {
		
		for(Long vinPartApprovalId:vinPartApprovalIdList) {
			approveVinPartChange(vinPartApprovalId, approveAssociateNumber);
		}
	}
	
	@Override
	@Transactional
	public void approveVinPartChange(long vinPartApprovalId, String approveAssociateNumber) {	
		try {
			VinPartApproval vinPartApproval = vinPartApprovalDao.findByKey(vinPartApprovalId);
			if(vinPartApproval!=null) {
				VinPart currentVinPart = vinPartDao.findByKey(getVinPartId(vinPartApproval.getProductId(), vinPartApproval.getLetSystemName(), vinPartApproval.getCurrentDcPartNumber()));
				VinPart newVinPart = new VinPart();
				if(!vinPartApproval.getNewDcPartNumber().equals("-")) {
				if(vinPartApproval.getCurrentDcPartNumber().equals("-") || !vinPartApproval.getCurrentDcPartNumber().equalsIgnoreCase(vinPartApproval.getNewDcPartNumber())) {
					//DC Part Number has changed
					if(vinPartApproval.getInterchangeable() || vinPartApproval.getCurrentDcPartNumber().equals("-")) {
						//Create a new VIN Part record with a new DC Part Number 
						//Ship Status should be set as 'N'
						newVinPart.setId(getVinPartId(vinPartApproval.getProductId(), vinPartApproval.getLetSystemName(), vinPartApproval.getNewDcPartNumber()));
						newVinPart.setShipStatus(false);
						vinPartDao.save(newVinPart);
						VinBomAuditLoggerUtil.logAuditInfo(VinPart.class, newVinPart, approveAssociateNumber, VinBomAuditLogChangeType.INSERT);
					} else {
						//Update the existing VIN Part record with the new DC Part Number
						if(currentVinPart!=null) {
							newVinPart.setId(getVinPartId(vinPartApproval.getProductId(), vinPartApproval.getLetSystemName(), vinPartApproval.getNewDcPartNumber()));
							newVinPart.setShipStatus(currentVinPart.getShipStatus());
							vinPartDao.remove(currentVinPart);
							vinPartDao.save(newVinPart);
							VinBomAuditLoggerUtil.logAuditInfo(VinPart.class, currentVinPart, newVinPart, approveAssociateNumber, VinBomAuditLogChangeType.UPDATE);
						}
					}
				} else if(vinPartApproval.getCurrentShipStatus() != (vinPartApproval.getNewShipStatus())) {
					//Ship Status has changed
					//Update the existing VIN Part record with the new Ship Status
					currentVinPart.setShipStatus(vinPartApproval.getNewShipStatus());
					newVinPart = vinPartDao.save(currentVinPart);
					VinBomAuditLoggerUtil.logAuditInfo(VinPart.class, currentVinPart, newVinPart, approveAssociateNumber, VinBomAuditLogChangeType.UPDATE);
				}
				}else {
					removeVinPart(vinPartApproval.getProductId(), vinPartApproval.getLetSystemName(), vinPartApproval.getCurrentDcPartNumber(), vinPartApproval.getApproveAssociateNumber());
				}
				//Set Approval Status
				vinPartApproval.setApproveStatus(VinBomApprovalStatus.APPROVED);
				vinPartApproval.setApproveAssociateNumber(approveAssociateNumber);
				vinPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
				vinPartApprovalDao.save(vinPartApproval);
			}
		
	} catch(Exception e) {
		logger.error(e, e.getMessage());
		throw new SystemException("Could not approve the change. Please contact IT support.", e);
		}
		
	}
	
	private VinPartId getVinPartId(String productId, String letSystemName, String dcPartNumber) {
		VinPartId vinPartId = new VinPartId();
		vinPartId.setProductId(productId);
		vinPartId.setLetSystemName(letSystemName);
		vinPartId.setDcPartNumber(dcPartNumber);
		return vinPartId;
	}

	@Override
	@Transactional
	public void approveModelPartChange(long modelPartApprovalId, String approveAssociateNumber) {
		ModelPartApproval modelPartApproval = modelPartApprovalDao.findByKey(modelPartApprovalId);
		ModelPart modelPart = modelPartDao.findByKey(modelPartApproval.getModelPartId());
		if(modelPartApproval != null){
		if( (modelPartApproval.getReturnToActive() == ReturnToActiveStatus.INTERCHANGABLE.getId()) || (modelPartApproval.getReturnToActive() == ReturnToActiveStatus.RETURN_TO_ACTIVE_RULE.getId())) {
				approveModelPartByYmt(modelPartApproval, modelPart, approveAssociateNumber);
				return;
			}
			if(modelPartApproval.getReturnToActive() == ReturnToActiveStatus.DEPRECATED.getId()) {
				changeActiveRule(modelPart, VinBomActiveStatus.DEPRECATED);
					modelPartApproval.setApproveStatus(VinBomApprovalStatus.APPROVED);
					modelPartApproval.setApproveAssociateNumber(approveAssociateNumber);
					modelPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
					modelPartApprovalDao.save(modelPartApproval);
			
				return;
			}

			//If Reflash, Interchangeable, or Scrap Parts Change; update Model Part
			if(modelPartApproval.getCurrentInterchangable() != modelPartApproval.getNewInterchangable() 
					|| modelPartApproval.getCurrentReflash() != modelPartApproval.getNewReflash() 
					|| modelPartApproval.getCurrentScrapParts() != modelPartApproval.getNewScrapParts()) {
				if(modelPart!=null) {
					if(!modelPart.getInterchangeable()) {
						deprecateInactiveRules(modelPart.getProductSpecWildcard(),modelPart.getLetSystemName(), modelPart.getModelPartId());
					}
					modelPart.setInterchangeable(modelPartApproval.getNewInterchangable());
					modelPart.setReflash(modelPartApproval.getNewReflash());
					modelPart.setScrapParts(modelPartApproval.getNewScrapParts());
					ModelPart updatedModelPart = modelPartDao.save(modelPart);
					VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPart, updatedModelPart, approveAssociateNumber, VinBomAuditLogChangeType.UPDATE);
				}
			}
			//Production Lot Change Check
			if(!modelPartApproval.getCurrentStartingProductionLot().equalsIgnoreCase(modelPartApproval.getNewStartingProductionLot()) 
					|| StringUtils.equalsIgnoreCase(ApplicationConstants.IMMEDIATE, modelPartApproval.getNewStartingProductionLot())) {
				if(StringUtils.equalsIgnoreCase(ApplicationConstants.IMMEDIATE, modelPartApproval.getNewStartingProductionLot())) {
					//TODO: Perform call to VinBomPartAssignmentService.processNoProduct
					immediateDesignChange(modelPart);
				} else {
					List<ModelLot> modelLotList = modelLotDao.getByModelPartIdAndProdLot(modelPartApproval.getModelPartId(), modelPartApproval.getCurrentStartingProductionLot());//check query
					if(modelLotList!=null && !modelLotList.isEmpty()) {
						for(ModelLot modelLot: modelLotList) {
							//compare plan code ifplan code does not match insert record
							PreProductionLot prodLot = preProductionLotDao.findByKey(modelPartApproval.getNewStartingProductionLot());
							ModelLot updatedModelLot = null;
							String planCode = prodLot!= null?prodLot.getPlanCode().trim():"";
							String modelPlanCode = modelLot.getId().getPlanCode().trim();
							if(!planCode.equalsIgnoreCase(modelPlanCode)) {
								
									ModelLot newModelLot = new ModelLot();
									ModelLotId newModelLotId = new ModelLotId();
									newModelLotId.setPlanCode(planCode);
									newModelLotId.setModelPartId(modelLot.getId().getModelPartId());
									
									newModelLot.setId(newModelLotId);
									newModelLot.setStartingProductionLot(modelPartApproval.getNewStartingProductionLot());
									updatedModelLot = modelLotDao.save(newModelLot);
							}else {
								modelLot.setStartingProductionLot(modelPartApproval.getNewStartingProductionLot());
								updatedModelLot = modelLotDao.save(modelLot);
							}
							VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelLot, updatedModelLot, approveAssociateNumber, VinBomAuditLogChangeType.UPDATE);
						}
					}else {
						PreProductionLot prodLot = preProductionLotDao.findByKey(modelPartApproval.getNewStartingProductionLot());
						ModelLot updatedModelLot = null;
						String planCode = prodLot!= null?prodLot.getPlanCode().trim():"";
						
						ModelLot newModelLot = new ModelLot();
						ModelLotId newModelLotId = new ModelLotId();
						newModelLotId.setPlanCode(planCode);
						newModelLotId.setModelPartId(modelPartApproval.getModelPartId());
								
						newModelLot.setId(newModelLotId);
						newModelLot.setStartingProductionLot(modelPartApproval.getNewStartingProductionLot());
						updatedModelLot = modelLotDao.save(newModelLot);
						
						VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, updatedModelLot, approveAssociateNumber, VinBomAuditLogChangeType.INSERT);
					}
				}
			}

			
			//Set Approval Status for Model Part Approval
			modelPartApproval.setApproveStatus(VinBomApprovalStatus.APPROVED);
			modelPartApproval.setApproveAssociateNumber(approveAssociateNumber);
			modelPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
			modelPartApprovalDao.save(modelPartApproval);
		}
	}

	private void changeActiveRule(ModelPart modelPart, VinBomActiveStatus vinBomActiveStatus) {

		
				modelPart.setActive(vinBomActiveStatus);
				modelPartDao.save(modelPart);
			
		
		
	}


		private void approveModelPartByYmt(ModelPartApproval modelPartApproval, ModelPart modelPart,
			String approveAssociateNumber) {
		List<ModelPart> modelParts = modelPartDao.getModelPartByYmt(modelPart.getProductSpecWildcard(),
				modelPart.getLetSystemName());
		
		
		if (modelPartApproval.getReturnToActive() == ReturnToActiveStatus.RETURN_TO_ACTIVE_RULE.getId()) {

		for (ModelPart modelPartByYmt : modelParts) {

		changeActiveRule(modelPartByYmt, VinBomActiveStatus.DEPRECATED);
			}
			
		}
		changeActiveRule(modelPart, VinBomActiveStatus.ACTIVE);
		ModelPart updatedModelPart = modelPartDao.save(modelPart);
		VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPart, updatedModelPart, approveAssociateNumber,
				VinBomAuditLogChangeType.UPDATE);

		modelPartApproval.setApproveStatus(VinBomApprovalStatus.APPROVED);
		modelPartApproval.setApproveAssociateNumber(approveAssociateNumber);
		modelPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
		modelPartApprovalDao.save(modelPartApproval);

	}
	
	private void deprecateInactiveRules(String productSpecWildcard, String letSystemName,long modelPartId) {
		List<ModelPart> modelParts = modelPartDao.getInactiveByProductSpecAndSystemName(productSpecWildcard, letSystemName);
		for(ModelPart modelPart:modelParts) {
			if(!(modelPart.getModelPartId()== modelPartId)) {
				modelPart.setActive(VinBomActiveStatus.DEPRECATED);
				modelPartDao.save(modelPart);
			}
		}
		
	}
	@Override
	@Transactional
	public void denyModelPartChange(long modelPartApprovalId, String approveAssociateNumber) {
		ModelPartApproval modelPartApproval = modelPartApprovalDao.findByKey(modelPartApprovalId);
		if(modelPartApproval!=null) {
			//If New Request (All current fields are empty strings), then remove from ModelLot and ModelPart
			if(StringUtils.isEmpty(modelPartApproval.getCurrentStartingProductionLot())|| StringUtils.equals(modelPartApproval.getCurrentStartingProductionLot(),"-")) {
				modelLotDao.removeByModelPartId(modelPartApproval.getModelPartId());
				VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelPartApproval.getModelPartId(), approveAssociateNumber, VinBomAuditLogChangeType.DELETE);
				modelPartDao.removeByKey(modelPartApproval.getModelPartId());
				VinBomAuditLoggerUtil.logAuditInfo(ModelPart.class, modelPartApproval.getModelPartId(), approveAssociateNumber, VinBomAuditLogChangeType.DELETE);
			}else if(!modelPartApproval.getCurrentStartingProductionLot().equalsIgnoreCase(modelPartApproval.getNewStartingProductionLot())){
				List<ModelLot> modelLots = modelLotDao.getByModelPartIdAndProdLot(modelPartApproval.getModelPartId(), modelPartApproval.getNewStartingProductionLot());
				for(ModelLot modelLot: modelLots) {
					modelLot.setStartingProductionLot(modelPartApproval.getCurrentStartingProductionLot());
					modelLotDao.save(modelLot);
					VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelPartApproval.getModelPartId(), approveAssociateNumber, VinBomAuditLogChangeType.UPDATE);
				}
				
				
			}
			//Set Approval Status for Model Part Approval
			modelPartApproval.setApproveStatus(VinBomApprovalStatus.DENIED);
			modelPartApproval.setApproveAssociateNumber(approveAssociateNumber);
			modelPartApproval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
			modelPartApprovalDao.save(modelPartApproval);
		}
	}
	
	@Override
	public List<PreProductionLot> getProductionLotsByProdDateAndLine(Date productionDate, String lineNo) {
		return preProductionLotDao.findAllByProdDateAndLine(getPropertyBean().getProcessLocation(), productionDate, lineNo);
	}
	
	@Override
	public List<Object[]> getLines(String plantCode) {
		return preProductionLotDao.findDistinctLines(plantCode, getPropertyBean().getProcessLocation());
	}

	@Override
	public List<ModelPartLotDto> getAvailableLotAssignments(boolean  pendingOnly) {
		List<ModelPartLotDto> modelPartLotDtoList = new ArrayList<ModelPartLotDto>();
		List<ModelPart> modelPartList = pendingOnly?modelPartDao.getPendingInactivewithRules():modelPartDao.getInactivewithRules();
		if(modelPartList!=null && !modelPartList.isEmpty()) {
			for(ModelPart modelPart: modelPartList) {
				List<ModelLot> modelLotList = modelLotDao.getAssignedLots(modelPart.getModelPartId());
				if(modelLotList!=null && !modelLotList.isEmpty()) {
					for(ModelLot modelLot: modelLotList) {
												
						ModelPartLotDto modelPartLotDto = convertToModelPartLotDto(modelPart);
						modelPartLotDto.setPlanCode(modelLot.getId().getPlanCode());
						modelPartLotDto.setStartingProductionLot(StringUtils.isBlank(modelLot.getStartingProductionLot())?"-":modelLot.getStartingProductionLot());
						if(pendingOnly) {
							if(modelPartLotDto.getApproveStatus()== VinBomApprovalStatus.PENDING) {
								modelPartLotDtoList.add(modelPartLotDto);
							}
						}else {
							modelPartLotDtoList.add(modelPartLotDto);
						}
					}
				} else {
					ModelPartLotDto modelPartLotDto = convertToModelPartLotDto(modelPart);
					modelPartLotDto.setStartingProductionLot("-");
					if(pendingOnly) {
						if(modelPartLotDto.getApproveStatus()== null || modelPartLotDto.getApproveStatus()== VinBomApprovalStatus.PENDING) {
							modelPartLotDtoList.add(modelPartLotDto);
						}
					}else {
						modelPartLotDtoList.add(modelPartLotDto);
					}
					
				}
			}
		}
		return modelPartLotDtoList;
	}
	
	@Override
	public void saveModelPartApproval(ModelPartApproval modelPartApproval, String userId) {
		if(modelPartApproval!=null) {
			modelPartApprovalDao.save(modelPartApproval);
			//Send email notification to VIN-BOM-Lot-Approve email group
			String subject = "Notification about VIN-BOM Model Part pending approval for Plant " + PropertyService.getSiteName();
			sendEmail(subject, getApprovalNotificationEmailBody(formatPendingModelPartApprovals(modelPartApprovalDao.getPendingApprovals())));
		}
	}
	
	public void saveMultiModelPartApproval(List<ModelPartApproval> modelPartApprovalList, String userId) {
		String subject = "Notification about VIN-BOM Model Part pending approval for Plant " + PropertyService.getSiteName();
		if(modelPartApprovalList != null && modelPartApprovalList.size() > 0) {
			
				modelPartApprovalDao.saveAll(modelPartApprovalList);
				//Send email notification to VIN-BOM-Lot-Approve email group
				sendEmail(subject, getApprovalNotificationEmailBody(formatPendingModelPartApprovals(modelPartApprovalDao.getPendingApprovals())));
			
		}
	}
	
	@Override
	public void saveVinPartApproval(VinPartApproval vinPartApproval, String userId) {
		if(vinPartApproval!=null) {
			vinPartApprovalDao.save(vinPartApproval);
			//Send email to VIN-BOM-Lot-Approve email group
			String subject = "Notification about VIN-BOM VIN Part pending approval for Plant " + PropertyService.getSiteName();
			sendEmail(subject, getApprovalNotificationEmailBody(formatPendingVinPartApprovals(vinPartApprovalDao.findAllPending())));
		}
	}
	
	public void saveMultiVinPart(List<VinPart> vinPartList, String userId) {
		
		logger.info("VinBomServiceImpl/saveMultiVinPart Request to save multiple records in VinPart STARTED");
		if (vinPartList != null && vinPartList.size() > 0) {
			int batchSize = getPropertyBean().getMassManualOverrideBatchSize();
			logger.info("Processed mass manual override Batch size: " + batchSize);
			int count = 0;
			while (count < vinPartList.size()) {
				List<VinPart> batchVinPartApprovalList = new ArrayList<>();
				int endIndex = Math.min(count + batchSize, vinPartList.size());
				for (int i = count; i < endIndex; i++) {
					batchVinPartApprovalList.add(vinPartList.get(i));
				}
				vinPartDao.saveAll(batchVinPartApprovalList);
				count += batchSize;
				logger.info("Processed record count " + count + " and end index " + endIndex);
			}
			logger.info("VinBomServiceImpl/saveMultiVinPart Request to save multiple records in VinPart FINISHED");
		}
	}

	@Override
	@Transactional
	public void removeMultiVinPart(String partNumber, String productSpecCode, String systemName) {
		ArrayList<String> trackingStatus = new ArrayList<>();
		trackingStatus.add("");
		vinPartDao.deleteVinParts(partNumber, productSpecCode, trackingStatus, systemName);
	}

	public void saveMultiVinPartApproval(List<VinPartApproval> vinPartApprovalList, String userId) {
		if(vinPartApprovalList!=null && vinPartApprovalList.size() > 0) {
			vinPartApprovalDao.saveAll(vinPartApprovalList);
			//Send email to VIN-BOM-Lot-Approve email group
			String subject = "Notification about VIN-BOM VIN Part pending approval for Plant " + PropertyService.getSiteName();
			sendEmail(subject, getApprovalNotificationEmailBody(formatPendingVinPartApprovals(vinPartApprovalDao.findAllPending())));
		}
	}
	
	private String getApprovalNotificationEmailBody(String pendingApprovals) {
		StringBuilder builder = new StringBuilder();
        builder.append(String.format("Dear User,%n%n"));
        builder.append(String.format("This is to notify you that VIN-BOM items are pending your approval. Please check and take appropriate action.%n%n"));
        builder.append(pendingApprovals);
        builder.append(String.format("%n"+PropertyService.getSiteName()+" VIN-BOM URL: "+getPropertyBean().getAppUrl()));
        builder.append(String.format("%n%nPlease do not reply to this mail as this is auto generated."));
        builder.append(String.format("%n%nThank You,%nVIN-BOM Support Team"));
        return builder.toString();
	}
	
	private String formatPendingModelPartApprovals(List<ModelPartApproval> pendingModelPartApproval) {
		List<PendingApprovalDto> pendingApprovalDtoList = new ArrayList<PendingApprovalDto>();
		if(pendingModelPartApproval!=null && !pendingModelPartApproval.isEmpty()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(ModelPartApproval modelPartApproval: pendingModelPartApproval) {
				PendingApprovalDto pendingApprovalDto = new PendingApprovalDto();
				String requestor = getAssociateName(modelPartApproval.getRequestAssociateNumber());
				pendingApprovalDto.setRequestor(requestor);
				ModelPart modelPart = modelPartDao.findByKey(modelPartApproval.getModelPartId());
				if(modelPart!=null) {
					pendingApprovalDto.setModel(extractModel(modelPart.getProductSpecWildcard()));
					pendingApprovalDto.setLetSystemName(modelPart.getLetSystemName());
					pendingApprovalDto.setDcNumber(StringUtils.trimToEmpty(modelPart.getDcNumber()));
					pendingApprovalDto.setDcEffBegDate((modelPart.getDcEffBegDate()!=null)?df.format(modelPart.getDcEffBegDate()):StringUtils.EMPTY);
				
				}
				pendingApprovalDtoList.add(pendingApprovalDto);
			}
		}
		return  getFormattedPendingApprovals(pendingApprovalDtoList);
	}
	
	private String formatPendingVinPartApprovals(List<VinPartApproval> pendingVinPartApprovals) {
		List<PendingApprovalDto> pendingApprovalDtoList = new ArrayList<PendingApprovalDto>();
		if(pendingVinPartApprovals!=null && !pendingVinPartApprovals.isEmpty()) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(VinPartApproval vinPartApproval: pendingVinPartApprovals) {
				PendingApprovalDto pendingApprovalDto = new PendingApprovalDto();
				String requestor = getAssociateName(vinPartApproval.getRequestAssociateNumber());
				pendingApprovalDto.setRequestor(requestor);
				Frame frame = frameDao.findByKey(vinPartApproval.getProductId());
				if(frame!=null) {
					String model = extractModel(frame.getProductSpecCode());
					pendingApprovalDto.setModel(model);
					List<ModelPart> modelParts = modelPartDao.findAllBy(model+extractModelType(frame.getProductSpecCode()), 
							vinPartApproval.getLetSystemName(), vinPartApproval.getNewDcPartNumber());
					if(modelParts!=null && !modelParts.isEmpty()) {
						ModelPart modelPart = modelParts.iterator().next();
						pendingApprovalDto.setDcNumber(StringUtils.trimToEmpty(modelPart.getDcNumber()));
						pendingApprovalDto.setDcEffBegDate((modelPart.getDcEffBegDate()!=null)?df.format(modelPart.getDcEffBegDate()):StringUtils.EMPTY);
					}
				}
				pendingApprovalDto.setLetSystemName(vinPartApproval.getLetSystemName());
				pendingApprovalDtoList.add(pendingApprovalDto);
			}
		}
		return  getFormattedPendingApprovals(pendingApprovalDtoList);
	}
	
	private String getFormattedPendingApprovals(List<PendingApprovalDto> pendingApprovalDtoList) {
		String[] header = {"Requestor","Model","DC #","System Name","DC Eff Begin Date"};
		StringBuilder builder = new StringBuilder();
		if(pendingApprovalDtoList!=null && !pendingApprovalDtoList.isEmpty()) {
			for(PendingApprovalDto pendingApprovalDto: pendingApprovalDtoList) {
				builder.append(String.format("%s: %s%n",header[0],pendingApprovalDto.getRequestor()));
		        builder.append(String.format("%s: %s%n",header[1],pendingApprovalDto.getModel()));
		        builder.append(String.format("%s: %s%n",header[2],pendingApprovalDto.getDcNumber()));
		        builder.append(String.format("%s: %s%n",header[3],pendingApprovalDto.getLetSystemName()));
		        builder.append(String.format("%s: %s%n%n",header[4],pendingApprovalDto.getDcEffBegDate()));
			}
		}
		return builder.toString();
	}

	@Override
	public boolean validateRuleSelection(String dcClass, int reflash, int interchangable, int scrapParts) {
				DesignChangeRule designChangeRule = designChangeRuleDao.findByKey(dcClass);
		
		boolean reflashFlag= false;
		boolean interchangableFlag = false;
		boolean scrapPartsFlag = false;
		
		if(designChangeRule != null) {
			logger.info(designChangeRule.toString());
			reflashFlag = designChangeRule.getReflash().getId() == VinBomDesignChangeRuleRequired.OPTIONAL.getId()?true:(designChangeRule.getReflash().getId() == reflash);
			interchangableFlag = designChangeRule.getInterchangable().getId() == VinBomDesignChangeRuleRequired.OPTIONAL.getId()?true:(designChangeRule.getInterchangable().getId() == interchangable );
			scrapPartsFlag = designChangeRule.getScrapParts().getId() == VinBomDesignChangeRuleRequired.OPTIONAL.getId()?true:(designChangeRule.getScrapParts().getId() == scrapParts);
			
			return (reflashFlag && interchangableFlag && scrapPartsFlag);
		}
		
		return false;
	}
	
	/**
	 * Call PMQA REST Service
	 * URL: {PMQAServerAddress}/api/defect-inquiry?vin=JHMGK5H88GS010000
	 * Output: String JSON response (examples below)
	 * LET Inspections NG:
	 * -------------------
	 * {
	 * 		"product_id" : "JHMGK5H88GS010000",
	 * 		"inspection_passing_flag" : "1",
	 * 		"defect_items" : ["HEV_ENG", "AUTO_P_CANCEL", "EV_EWP_CANCEL"]
	 * }
	 * 
	 * LET Inspections OK:
	 * -------------------
	 * {
	 * 		"product_id" : "JHMGK5H88GS010000",
	 * 		"inspection_passing_flag" : "0",
	 * 		"defect_items" : []
	 * }
	 */
	@Override
	public PmqaDto retrievePmqaData(String productId) {
		try {
			String json = null;
			if(StringUtils.isNotEmpty(getPropertyBean().getPmqaUrl())) {
				String url = getPropertyBean().getPmqaUrl() + "/api/latest-defect-inquiry?vin="+productId;
				json = HttpClient.get(url,  HttpURLConnection.HTTP_OK);
				logger.info("PMQA REST call - URL: "+url+"; Result: "+json);
			} else {
				//PMQA URL is empty, use hard-coded data for testing purpose
				json = getHardcodedPmqaJson();
			}
			Gson gson = new Gson();
			PmqaDto PmqaDto = gson.fromJson(json, PmqaDto.class);
			return PmqaDto;
		} catch (Exception e) {
	 		logger.error(e, "Failed to execute PMQA REST call");
		}
		return null;
	}
	
	private String getHardcodedPmqaJson() {
		return "{" + 
				"    \"product_id\" : \"JHMGK5H88GS010000\"," + 
				"    \"inspection_passing_flag\" : \"1\"," + 
				"    \"defect_items\" : [" + 
				"        \"HEV_ENG\"," + 
				"        \"AUTO_P_CANCEL\"," + 
				"        \"EV_EWP_CANCEL\"" + 
				"    ]" + 
				"}";
	}

	private ModelPartLotDto convertToModelPartLotDto(ModelPart modelPart) {
		ModelPartLotDto modelPartLotDto = new ModelPartLotDto();
		if(modelPart!=null) {
			modelPartLotDto.setModelPartId(modelPart.getModelPartId());
			modelPartLotDto.setProductSpecWildcard(modelPart.getProductSpecWildcard());
			modelPartLotDto.setModel(extractModel(modelPart.getProductSpecWildcard()));
			modelPartLotDto.setModelType(extractModelType(modelPart.getProductSpecWildcard()));
			modelPartLotDto.setLetSystemName(modelPart.getLetSystemName());
			modelPartLotDto.setDcPartNumber(modelPart.getDcPartNumber());
			modelPartLotDto.setActive(modelPart.getActive());
			modelPartLotDto.setDcClass(modelPart.getDcClass());
			modelPartLotDto.setDcEffBegDate(modelPart.getDcEffBegDate());
			modelPartLotDto.setDcNumber(modelPart.getDcNumber());
			modelPartLotDto.setInterchangeable(modelPart.getInterchangeable());
			modelPartLotDto.setReflash(modelPart.getReflash());
			modelPartLotDto.setScrapParts(modelPart.getScrapParts());
			ModelPartApproval modelPartApproval = modelPartApprovalDao.findByModelPartId(modelPart.getModelPartId());
			if(modelPartApproval!=null)
				modelPartLotDto.setApproveStatus(modelPartApproval.getApproveStatus());
		}
		return modelPartLotDto;
	}
	
	private String extractModel(String productSpecCode) {
		return ProductSpecUtil.extractModelYearCode(productSpecCode)
				+ ProductSpecUtil.extractModelCode(productSpecCode);
	}
	
	private String extractModelType(String productSpecCode) {
		return ProductSpecUtil.extractModelTypeCode(productSpecCode);
	}

	/**
	 * 
	 * DCMS REST CALL
	 * ==============
	 * Input: 
	 * dcmsRestUrl/getDesignChangeByPlantAndDesignChangeNumber?plantName=MAP&designChangeNumber=A2100001
	 * 
	 * Output:
	 * [
	 *     {
	 *         "designChangeNumber": "A2100001",
	 *         "designChangeClass": "A",
	 *         "serviceInterchangeableCode": "YY",
	 *         "stragglerInterchangeableCode": "Y",
	 *         "effectiveBeginDate": "2020-08-01",
	 *         "effectiveEndDate": "9999-12-31",
	 *         "dcmsModelCode": "TPCX",
	 *         "modelCode": "LTPC",
	 *         "modelType": "AA7",
	 *         "designChangePartNumber": "39110TX5 A000"
	 *     },
	 *     {
	 *         "designChangeNumber": "A2100001",
	 *         "designChangeClass": "A",
	 *         "serviceInterchangeableCode": "YN",
	 *         "stragglerInterchangeableCode": "Y",
	 *         "effectiveBeginDate": "2020-08-01",
	 *         "effectiveEndDate": "9999-12-31",
	 *         "dcmsModelCode": "TPCX",
	 *         "modelCode": "LTPC",
	 *         "modelType": "AC7",
	 *         "designChangePartNumber": "39110TX5 A000"
	 *     }
	 * ]
	 * 
	 */
	private List<DcmsDto> retrieveDesignChangeFromDcms(String plantLocCode, String designChangeNumber) {
		try {
			String json = null;
			if(StringUtils.isNotEmpty(getPropertyBean().getDcmsUrl())) {
				String url = getPropertyBean().getDcmsUrl() + "/getDesignChangeByPlantAndDesignChangeNumber?plantName="+plantLocCode+"&designChangeNumber="+designChangeNumber;
				json = HttpClient.get(url,  HttpURLConnection.HTTP_OK, getDcmsRequestHeaders());
				logger.info("DCMS REST call - URL: "+url+"; Result: "+json);
			} else {
				//DCMS URL is empty, use hard-coded data for testing purpose
				json = getHardcodedDcmsJson();
			}
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			Type listType = new TypeToken<ArrayList<DcmsDto>>(){}.getType();
			return gson.fromJson(json, listType);
		} catch (Exception e) {
	 		logger.error(e, "Failed to execute DCMS REST call");
		}
		return new ArrayList<DcmsDto>();
	}

	private String getHardcodedDcmsJson() {
		return "[" + 
				"    {" + 
				"        \"designChangeNumber\": \"A2100001\"," + 
				"        \"designChangeClass\": \"A\"," + 
				"        \"serviceInterchangeableCode\": \"YY\"," + 
				"        \"stragglerInterchangeableCode\": \"Y\"," + 
				"        \"effectiveBeginDate\": \"2020-08-01\"," + 
				"        \"effectiveEndDate\": \"9999-12-31\"," + 
				"        \"dcmsModelCode\": \"TPCX\"," + 
				"        \"modelCode\": \"JT7A\"," + 
				"        \"modelType\": \"BA7\"," + 
				"        \"designChangePartNumber\": \"31700T9S J010M1\"" + 
				"    }," + 
				"    {" + 
				"        \"designChangeNumber\": \"A2100001\"," + 
				"        \"designChangeClass\": \"A\"," + 
				"        \"serviceInterchangeableCode\": \"YN\"," + 
				"        \"stragglerInterchangeableCode\": \"Y\"," + 
				"        \"effectiveBeginDate\": \"2020-08-01\"," + 
				"        \"effectiveEndDate\": \"9999-12-31\"," + 
				"        \"dcmsModelCode\": \"TPCX\"," +
				"        \"modelCode\": \"JT7A\"," + 
				"        \"modelType\": \"BA7\"," + 
				"        \"designChangePartNumber\": \"31705T9S J010M1\"" + 
				"    }," + 
				"    {" + 
				"        \"designChangeNumber\": \"A2100001\"," + 
				"        \"designChangeClass\": \"A\"," + 
				"        \"serviceInterchangeableCode\": \"YY\"," + 
				"        \"stragglerInterchangeableCode\": \"N\"," + 
				"        \"effectiveBeginDate\": \"2020-09-01\"," + 
				"        \"effectiveEndDate\": \"9999-12-31\"," + 
				"        \"dcmsModelCode\": \"TPCX\"," +
				"        \"modelCode\": \"JT7A\"," + 
				"        \"modelType\": \"BA7\"," + 
				"        \"designChangePartNumber\": \"39110TX5 A000\"" + 
				"    }," + 
				"    {" + 
				"        \"designChangeNumber\": \"A2100001\"," + 
				"        \"designChangeClass\": \"B\"," + 
				"        \"serviceInterchangeableCode\": \"NN\"," + 
				"        \"stragglerInterchangeableCode\": \"Y\"," + 
				"        \"effectiveBeginDate\": \"2020-08-01\"," + 
				"        \"effectiveEndDate\": \"9999-12-31\"," + 
				"        \"dcmsModelCode\": \"TPCX\"," +
				"        \"modelCode\": \"JT7A\"," + 
				"        \"modelType\": \"BA7\"," + 
				"        \"designChangePartNumber\": \"57110TX5 A000\"" + 
				"    }," + 
				"    {" + 
				"        \"designChangeNumber\": \"A2100001\"," + 
				"        \"designChangeClass\": \"B\"," + 
				"        \"serviceInterchangeableCode\": \"YY\"," + 
				"        \"stragglerInterchangeableCode\": \"Y\"," + 
				"        \"effectiveBeginDate\": \"2020-08-01\"," + 
				"        \"effectiveEndDate\": \"9999-12-31\"," + 
				"        \"dcmsModelCode\": \"TPCX\"," +
				"        \"modelCode\": \"JT7A\"," + 
				"        \"modelType\": \"BA7\"," + 
				"        \"designChangePartNumber\": \"57110TX5 A000\"" + 
				"    }" + 
				"]";
	}

	/**
	 * 
	 * GMQA REST CALL
	 * ==============
	 * 2|J|T7A|BA7|00 |31700T9S J000M1|31700T9S J010M1|20170424|99991231|11111111 11****|01|Y|BBC_PROD|UNIT ASSY,BBC
	 * 2|J|T7A|BA7|00 |31705T9S J000M1|31705T9S J010M1|20170424|99991231|11111111 11****|02| |BBC_SOFT|SOFTWARE,ECU
	 * 2|J|T7A|BA7|00 |3780550T B700M1|3780550T B710M1|20200512|99991231|11111111 11****|03|Y|FI_SOFT|SOFTWARE,ECU
	 * 1|J|T7A|BA7|00 |3782050T B700M1|3782050T B710M1|20200512|99991231|11111111 11****|04| |FI_PROD|ELECTRONIC CONTROL UNIT
	 * 1|J|T7A|BA7|00 |3782050TZB700M1|3782050TZB710M1|20200512|99991231|11111111111****|04| |FI_PROD|ELECTRONIC CONTROL UNIT
	 * 
	 * 1 Prototype 2 Mass Pro
	 * Year Code
	 * Model Code
	 * Type Code
	 * Option Code
	 * BPN (15digit FIX) Remove blanks when supplementary code is NOT available.
	 * DCPN (15digit FIX) Remove blanks when no supplementary code NOT available.
	 * Effective Begin Date
	 * Effective End Date
	 * 1 matching digit "*" or blank   non matching digit (15 digit FIX)
	 * e.g.01 08 or blank (2 digit FIX)
	 * Y RxSWIN part;  BLANK not RxSWIN part
	 * System name
	 * Part name
	 * 
	 */
	private List<BeamPartDto> retrieveBeamPartDataFromGmqa(String plantLocCode, String division) {
		List<BeamPartDto> beamPartDtoList = new ArrayList<BeamPartDto>();
		try {
			String response = null;
			if(StringUtils.isNotEmpty(getPropertyBean().getGmqaUrl())) {
				String url = getPropertyBean().getGmqaUrl() + "?plant="+plantLocCode+"&division="+division;
				logger.info("Connecting to GMQA server " + url);
				response = HttpClient.get(url,  HttpURLConnection.HTTP_OK,getGMQARequestHeaders(),
						getPropertyBean().getGmqaConnectionTimeOut(),getPropertyBean().getGmqaReadTimeOut());
				logger.info("GMQA REST call - URL: "+url+"; Response: "+response);
			} else {
				//GMQA URL is empty, use hard-coded data for testing purpose
				response = getHardcodedBeamPartData();
			}
			if(StringUtils.isNotEmpty(response)) {
				JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
				JsonArray fileData = jobj.get(GMQA_FILE_DATA).getAsJsonArray();
				
				for(int i = 0;i<fileData.size();i++) {
					String beamData = fileData.get(i).getAsString();
					BeamPartDto beamPartDto = convertToBeamPartDto(beamData);
					if (beamPartDto != null)
						beamPartDtoList.add(beamPartDto);
				}
				
			}
		} catch (Exception e) {
	 		logger.error(e, "Failed to execute GMQA REST call");
		}
		return beamPartDtoList;
	}

	private String getHardcodedBeamPartData() {
		return "2|J|T7A|BA7|00 |31700T9S J000M1|31700T9S J010M1|20170424|99991231|11111111 11****|01|Y|BBC_PROD|UNIT ASSY,BBC" + System.getProperty("line.separator") +  
				"2|J|T7A|BA7|00 |31705T9S J000M1|31705T9S J010M1|20170424|99991231|11111111 11****|02| |BBC_SOFT|SOFTWARE,ECU" + System.getProperty("line.separator") +  
				"2|J|T7A|BA7|00 |3780550T B700M1|3780550T B710M1|20200512|99991231|11111111 11****|03|Y|FI_SOFT|SOFTWARE,ECU" + System.getProperty("line.separator") +  
				"1|J|T7A|BA7|00 |3782050T B700M1|3782050T B710M1|20200512|99991231|11111111 11****|04| |FI_PROD|ELECTRONIC CONTROL UNIT" + System.getProperty("line.separator") +  
				"1|J|T7A|BA7|00 |3782050TZB700M1|3782050TZB710M1|20200512|99991231|11111111111****|04| |FI_PROD|ELECTRONIC CONTROL UNIT" + System.getProperty("line.separator") + 
				"2|J|T7A|BA7|00 |39110TX5 A000|39110TX5 A000|20200512|99991231|11111111 11****|01|Y|EPS_SOFT|ELECTRONIC UNIT" + System.getProperty("line.separator") + 
				"2|J|T7A|BA7|00 |57110TX5 A000|57110TX5 A000|20200512|99991231|11111111 11****|01|Y|VBS_PROD|BRAKE UNIT";
	}

	private BeamPartDto convertToBeamPartDto(String beamData) {
		String[] beamPartInfo = StringUtils.split(beamData, "|");
		if (beamPartInfo != null && beamPartInfo.length == 14)
			try {
				DateFormat df = new SimpleDateFormat("yyyyMMdd");
				BeamPartDto beamPartDto = new BeamPartDto();
				beamPartDto.setPrototypeOrMassProd(Integer.parseInt(beamPartInfo[0]));
				beamPartDto.setModelYearCode(beamPartInfo[1]);
				beamPartDto.setModelCode(beamPartInfo[2]);
				beamPartDto.setModelTypeCode(beamPartInfo[3]);
				beamPartDto.setModelOptionCode(beamPartInfo[4]);
				beamPartDto.setBpn(beamPartInfo[5]);
				beamPartDto.setDcpn(beamPartInfo[6]);
				beamPartDto.setEffectiveBeginDate(df.parse(beamPartInfo[7]));
				beamPartDto.setEffectiveEndDate(df.parse(beamPartInfo[8]));
				beamPartDto.setMatchingDigits(beamPartInfo[9]);
				beamPartDto.setSwClassification(beamPartInfo[10]);
				beamPartDto.setRxWin(beamPartInfo[11]);
				beamPartDto.setSystemName(beamPartInfo[12]);
				beamPartDto.setPartName(beamPartInfo[13]);
				return beamPartDto;
			} catch (Exception e) {
				logger.info(new String[] { "VinBomServiceImpl.convertToBeamPartDto Beam Data (" + beamData + ") could not get parsed into DTO; error message: " + e.getMessage() });
			}  
		return null;
	}
	public VinBomDesignChangeDto[] retrieveDesignChangeByEffectiveDate(String plantLocCode, Date effectiveDate) {
		VinBomDesignChangeDto[] dcmsDtoList = getDesignChangeByEffectiveDateFromDcms(plantLocCode, effectiveDate);
		return dcmsDtoList;
	}

	private VinBomDesignChangeDto[] getDesignChangeByEffectiveDateFromDcms(String plantLocCode, Date effectiveDate) {
		try {
			String json = null;
			String dateFormatPattern = "yyyy-MM-dd";
			if(StringUtils.isNotEmpty(getPropertyBean().getDcmsUrl())) {
				String url = getPropertyBean().getDcmsUrl() + "/getDesignChangesByPlantAndEffectiveBeginDate?plantName="+plantLocCode+"&effectiveBeginDate="+new SimpleDateFormat(dateFormatPattern).format(effectiveDate);
				json = HttpClient.get(url,  HttpURLConnection.HTTP_OK, getDcmsRequestHeaders());
				logger.info("DCMS REST call - URL: "+url+"; Result: "+json);
			} else {
				//DCMS URL is empty, use hard-coded data for testing purpose
				json = getHardcodedDcmsByEffectiveDateJson();
			}
			Gson gson = new GsonBuilder().setDateFormat(dateFormatPattern).create();
			return gson.fromJson(json,VinBomDesignChangeDto[].class);
		} catch (Exception e) {
	 		logger.error(e, "Failed to execute DCMS REST call");
		}
		return null;
	}
	
	private Map<String, String> getDcmsRequestHeaders() {
		Map<String, String> dcmsRequestHeaders = getPropertyBean().getDcmsRequestHeader();
		//Adding Message Id headers
		String msgId = PropertyService.getSiteName() + Delimiter.HYPHEN + UUID.randomUUID().toString();
		dcmsRequestHeaders.put(DCMS_HEADER_HONDA_MSG_ID, msgId);
		dcmsRequestHeaders.put(DCMS_HEADER_MSG_ID, msgId);
		//Adding collected time-stamp headers
		String collectedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
		dcmsRequestHeaders.put(DCMS_HEADER_HONDA_TIMESTAMP, collectedTimestamp);
		dcmsRequestHeaders.put(DCMS_HEADER_TIMESTAMP, collectedTimestamp);
		return dcmsRequestHeaders;
	}
  
	private Map<String, String> getGMQARequestHeaders() {
		Map<String, String> dcmsRequestHeaders = getPropertyBean().getGmqaRequestHeader();
		//Adding Message Id headers
		String msgId = PropertyService.getSiteName() + Delimiter.HYPHEN + UUID.randomUUID().toString();
		dcmsRequestHeaders.put(GMQA_HEADER_HONDA_MSG_ID, msgId);
		dcmsRequestHeaders.put(GMQA_HEADER_MSG_ID, msgId);
		//Adding collected time-stamp headers
		String collectedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
		dcmsRequestHeaders.put(GMQA_HEADER_HONDA_TIMESTAMP, collectedTimestamp);
		dcmsRequestHeaders.put(GMQA_HEADER_TIMESTAMP, collectedTimestamp);
		logger.info("connecting to GMQA server with request headers - " + dcmsRequestHeaders);
		return dcmsRequestHeaders;
	}
  	private String getHardcodedDcmsByEffectiveDateJson() {
		return "[\r\n" + 
				"    {\r\n" + 
				"        \"designChangeNumber\": \"A2100001\",\r\n" + 
				"        \"designChangePartNumber\": \"39110TX5 A000\",\r\n" + 
				"        \"effectiveBeginDate\": \"2020-08-01\",\r\n" + 
				"        \"modelCode\": \"LTLA\",\r\n" + 
				"        \"modelType\": \"AH7\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"designChangeNumber\": \"A2100002\",\r\n" + 
				"        \"designChangePartNumber\": \"57110TX5 A000\",\r\n" + 
				"        \"effectiveBeginDate\": \"2020-08-01\",\r\n" + 
				"        \"modelCode\": \"LTPD\",\r\n" + 
				"        \"modelType\": \"AA7\"\r\n" + 
				"    },\r\n" + 
				"    {\r\n" + 
				"        \"designChangeNumber\": \"A2100003\",\r\n" + 
				"        \"designChangePartNumber\": \"57110TX5 K000\",\r\n" + 
				"        \"effectiveBeginDate\": \"2020-08-03\",\r\n" + 
				"        \"modelCode\": \"LTPD\",\r\n" + 
				"        \"modelType\": \"KA7\"\r\n" + 
				"    }\r\n" + 
				"]";
	}

	private VinBomPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(VinBomPropertyBean.class);
	}
	
	public List<VinBomPart> findDistinctPartNumberBySystemName(String letSystemName, String productId){
		List<VinBomPart> vinBomPartList =  new ArrayList<VinBomPart>();
		Frame frame = frameDao.findByKey(productId);
		if (frame != null) {
			String productSpec = frame.getProductSpecCode().substring(0, 7);
			logger.info("findDistinctPartNumberBySystemName - "+letSystemName +", "+productSpec);
			vinBomPartList = vinBomPartDao.findDistinctPartNumberBySystemNameAndProductSpec(letSystemName,productSpec);
		}else {
			vinBomPartList = vinBomPartDao.findDistinctPartNumberBySystemName(letSystemName);
		}
		
		
		for(VinBomPart part: vinBomPartList) {
			part.setEffectiveBeginDateString(StringUtil.dateToString(part.getEffectiveBeginDate(),"yyyy-MM-dd "));
			part.setEffectiveEndDateString(StringUtil.dateToString(part.getEffectiveEndDate(),"yyyy-MM-dd "));
		}
		
		return vinBomPartList;
	}
	
	private void sendEmail(String subject, String message) {
		SmtpMailPropertyBean mailPropertyBean = PropertyService.getPropertyBean(SmtpMailPropertyBean.class, VIN_BOM);
		try {
			final MailContext mailContext = new MailContext();
			mailContext.setMessage(message);
			mailContext.setSubject(subject);
			mailContext.setHost(mailPropertyBean.getHost());
			mailContext.setSender(mailPropertyBean.getSender());
			mailContext.setRecipients(mailPropertyBean.getRecipients());
			mailContext.setTimeout(mailPropertyBean.getConnectionTimeout() * 1000);
			MailSender.sendAsync(mailContext); 
		} catch(Exception e) {
			logger.error(e, "Unable to send VIN-BOM mail notification");
		}
	}
	

	@Override
	public void vinPartAssignment(String productSpecCode, String productId, String productionLot, String processPointId) {
		logger.info("Starting vinPartAssignment for productId : "+productId);
		List<String> systemNames = vinBomPartDao.getSystemNamesByProductSpec(productSpecCode);
		boolean isError = false;
		String errorSystemName = "";
		if(!systemNames.isEmpty()) {
			for (String systemName : systemNames) {
				try{
					if(StringUtils.equalsIgnoreCase(systemName, "*NOT SET*")) {
						logger.info("System name is : "+ systemName);
						continue;
					}
					List<String> partNumbersAssignedToVins = vinPartDao.getPartNumbersByProductIdAndSystemName(productId, systemName);
					if(!partNumbersAssignedToVins.isEmpty()) {
						logger.info("Part Numbers : "+ partNumbersAssignedToVins +" are already assigned to VIN : "+ productId +" for the system : "+ systemName);
						continue;
					}
					List<String> partNumbersAssignedToLots = lotPartDao.getPartNumbersByProductionLotAndSystemName(productionLot, systemName);
					if(!partNumbersAssignedToLots.isEmpty()) {
						for (String partNumber : partNumbersAssignedToLots) {
							VinPart vinPart = new VinPart();
							VinPartId vinPartId = new VinPartId();
							vinPartId.setProductId(productId);
							vinPartId.setLetSystemName(systemName);
							vinPartId.setDcPartNumber(partNumber);
							vinPart.setId(vinPartId);
							vinPart.setShipStatus(false);
							vinPartDao.save(vinPart);
	
							logger.info(" Assigned Part Number : "+ partNumber +" to VIN : "+ productId +" for the system : "+ systemName);
						}
						continue;
					}
	
					List<ModelPart> approvedInactiveList =  modelPartDao.getApprovedInactiveByProductSpecAndSystemName(productSpecCode, systemName);				
					List<ModelPart> activeModelPartList =  modelPartDao.getActiveByProductSpecAndSystemName(productSpecCode, systemName);
					logger.info("ApprovedInactiveList size : "+ approvedInactiveList.size() +" for the productSpecCode:  "+ productSpecCode +" and for the system : "+ systemName);
					logger.info("ActiveList size : "+ activeModelPartList.size() +" for the productSpecCode:  "+ productSpecCode +" and for the system : "+ systemName);
					if(approvedInactiveList.size() > 0) {
						String planCode = "";
						String processLocation = "";
						ProductionLot currentProductionLot = productionLotDao.findByKey(productionLot);
						if(currentProductionLot!=null) {
							planCode = currentProductionLot.getPlanCode();
							processLocation =currentProductionLot.getProcessLocation();
						}
						
						ModelLotId modelLotId = new ModelLotId();
						modelLotId.setModelPartId(approvedInactiveList.get(0).getModelPartId()); 
						modelLotId.setPlanCode(planCode);
						ModelLot modelLot = modelLotDao.findByKey(modelLotId);
						
						if(modelLot != null) {
							//boolean isPassedProdLot = preProductionLotDao.isPassedProductionLot(processLocation, planCode, productionLot, modelLot.getStartingProductionLot());
							//logger.info("IsPassedProdLot : "+ isPassedProdLot +" for the processLocation :  "+ processLocation +" planCode : "+ planCode +" productionLot : "+productionLot +" startingProductionLot :"+ modelLot.getStartingProductionLot());
							boolean isPassedProdLot = preProductionLotDao.isPassedProductionLot(processPointId, modelLot.getStartingProductionLot());
							logger.info("IsPassedProdLot : "+ isPassedProdLot +" for the processPointId :  "+ processPointId +" startingProductionLot : "+ modelLot.getStartingProductionLot());
							if(isPassedProdLot) {
								for (ModelPart modelPart : approvedInactiveList) {
									if(modelPart.getReflash()) {
										logger.info("doReflash for modelPart : "+ modelPart +" processPointId :  "+ processPointId);
										doReflash(modelPart, processPointId, activeModelPartList);
									} else if(modelPart.getInterchangeable()) {
										logger.info("Interchangeable for modelPart : "+ modelPart +" processPointId :  "+ processPointId);
										if(modelPart.getScrapParts()) {
											lotPartDao.insertStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
										}
										if (activeModelPartList.size() > 0) {
											for (ModelPart activeModelPart : activeModelPartList) {
												logger.info("AssignPartNumberToLotAndVin for interchangable modelPart : "+ activeModelPart +" productId :  "+ productId +" and productionLot : "+ productionLot);
												assignPartNumberToLotAndVin(activeModelPart, productId, productionLot);
											}
										}
									} else {
										deprecateActiveModelParts(activeModelPartList, modelPart);
										if(modelPart.getScrapParts()) {
											lotPartDao.deleteStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
											lotPartDao.insertStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
										} 
									}
									logger.info("AssignPartNumberToLotAndVin for modelPart : "+ modelPart +" productId :  "+ productId +" and productionLot : "+ productionLot);
									assignPartNumberToLotAndVin(modelPart, productId, productionLot);
								}
							} else {
								if (activeModelPartList.size() > 0) {
									for (ModelPart modelPart : activeModelPartList) {
										logger.info("AssignPartNumberToLotAndVin for modelPart : "+ modelPart +" productId :  "+ productId +" and productionLot : "+ productionLot);
										assignPartNumberToLotAndVin(modelPart, productId, productionLot);
									}
								} else {
									logger.info("Setting error to true for productId : "+ productId);
									errorSystemName = errorSystemName +" "+ systemName;
									isError = true;
								}
							}
						}
					} else {
						if (activeModelPartList.size() > 0) {
							for (ModelPart modelPart : activeModelPartList) {
								logger.info("AssignPartNumberToLotAndVin for modelPart : "+ modelPart +" productId :  "+ productId +" and productionLot : "+ productionLot);
								assignPartNumberToLotAndVin(modelPart, productId, productionLot);
							}
						} else {
							logger.info("Setting error to true for productId : "+ productId);
							errorSystemName = errorSystemName +" "+ systemName;
							isError = true;
						}
					}
				} catch (Exception ex) {
					logger.info("Exception in VinBomService.vinPartAssignment: " +ex.getMessage());
				} 
			}
		} else {
			logger.info("No System name for Product ID: " + productId  +" and productSpecCode: "+ productSpecCode);
		}
		
		if(isError) {
			sendEmail( "VIN-BOM", "No Part Found for Product ID: " + productId + " and system name:"+errorSystemName);
		}
	}
	
	private void assignPartNumberToLotAndVin(ModelPart modelPart, String productId, String productionLot) {
		saveLotPart(modelPart.getDcPartNumber(), modelPart.getLetSystemName(), productionLot, VinBomActiveStatus.ACTIVE);
		saveVinPart(modelPart.getDcPartNumber(), modelPart.getLetSystemName(), productId, false);
		if(!modelPart.getActive().equals(VinBomActiveStatus.ACTIVE)) {
			modelPart.setActive(VinBomActiveStatus.ACTIVE);
			modelPartDao.save(modelPart);
		}
	}
	
	private void doReflash(ModelPart modelPart, String processPointId, List<ModelPart> activeModelPartList) {
		deprecateActiveModelParts(activeModelPartList, modelPart);
		vinPartDao.deleteVinParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
		vinPartDao.insertVinParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
		lotPartDao.deleteLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
		lotPartDao.insertLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
	}
	
	private List<String> getShippedTrackingStatuses() {
		String trackingStatus =  getPropertyBean().getShippedTrackingStatuses();
		if(StringUtils.isNotBlank(trackingStatus)){
			String[] statuses = trackingStatus.split(",");
			return Arrays.asList(statuses);
		}
		return new ArrayList<String>();
	}
	
	public void immediateDesignChange(ModelPart modelPart) {
		logger.info("Entering method immediateDesignChange of VinBomServiceImpl with ModelPart :" + modelPart);
		String processPointId = null;
		List<ModelLot> modelLotList = modelLotDao.getAssignedLots(modelPart.getModelPartId());
		logger.info("ModelLotList : " +modelLotList);
		if(modelLotList!=null && !modelLotList.isEmpty()) {
			
			ModelLot modelLot = modelLotList.get(0);
			modelLot.setStartingProductionLot(ApplicationConstants.IMMEDIATE);
			modelLotDao.save(modelLot);	
			
			processPointId = getPropertyBean().getProcessPointMap().get(modelLotList.get(0).getId().getPlanCode());
		}
		if(StringUtils.isNotBlank(processPointId)) {
			List<ModelPart> activeModelPartList =  modelPartDao.getActiveByProductSpecAndSystemName(modelPart.getProductSpecWildcard(), modelPart.getLetSystemName());
			
			if(modelPart.getReflash()) {
				logger.info("Reflash for immediateDesignChange modelPart : "+ modelPart +" processPointId :  "+ processPointId +" tracking status :  "+ getShippedTrackingStatuses());
				vinPartDao.deleteVinParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
				vinPartDao.insertVinParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
				lotPartDao.deleteLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
				lotPartDao.insertLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName());
			} else if(modelPart.getInterchangeable()) {
				logger.info("Interchangeable for immediateDesignChange modelPart.getScrapParts() : "+ modelPart.getScrapParts() +" processPointId :  "+ processPointId +" tracking status :  "+ getShippedTrackingStatuses());
				if(modelPart.getScrapParts()) {
					// Added to fix the bug NALC-1124 VINBOM Doesn't allow approval of part
					lotPartDao.deleteStragglerLotPartsWithPartNumber(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
					lotPartDao.insertStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
				}
			} else {
				if(modelPart.getScrapParts()) {
					logger.info("Scrap Parts for immediateDesignChange processPointId :  "+ processPointId +" tracking status :  "+ getShippedTrackingStatuses());
					lotPartDao.deleteStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
					lotPartDao.insertStragglerLotParts(modelPart.getDcPartNumber(), modelPart.getProductSpecWildcard(), getShippedTrackingStatuses(), modelPart.getLetSystemName(), processPointId);
				}
			}
			
			if(modelPart.getReflash()) {
				deprecateActiveModelParts(activeModelPartList, modelPart);
			} else if(!modelPart.getInterchangeable()) {
				deprecateActiveModelParts(activeModelPartList, modelPart);
			}
			
			if(!modelPart.getActive().equals(VinBomActiveStatus.ACTIVE)) {
				modelPart.setActive(VinBomActiveStatus.ACTIVE);
				modelPartDao.save(modelPart);
				logger.info("ImmediateDesignChange set model part ACTIVE");
			}
		} else {
			logger.info("ImmediateDesignChange processPointId is null for plan code : " +modelLotList.get(0).getId().getPlanCode());
		}
		logger.info("Exiting method immediateDesignChange of VinBomServiceImpl");
	}

	private void deprecateActiveModelParts(List<ModelPart> activeModelPartList, ModelPart currentModelPart) {
		if (activeModelPartList.size() > 0) {
			for (ModelPart activeModelPart : activeModelPartList) {
				if(!(activeModelPart.getModelPartId().equals(currentModelPart.getModelPartId()))) {
					activeModelPart.setActive(VinBomActiveStatus.DEPRECATED);
					modelPartDao.save(activeModelPart);
					logger.info("Updated current active model parts status to DEPRECATED");
				}
			}
		}
	}
	
	private void saveLotPart(String partNumber, String systemName, String productionLot, VinBomActiveStatus active) {
		LotPart lotPart = new LotPart();
		LotPartId lotPartId = new LotPartId();
		lotPartId.setDcPartNumber(partNumber);
		lotPartId.setLetSystemName(systemName);
		lotPartId.setProductionLot(productionLot); 
		lotPart.setId(lotPartId);
		lotPart.setActive(active);
		lotPartDao.save(lotPart);
	}
	
	private void saveVinPart(String partNumber, String systemName, String productId, boolean shipStatus) {
		VinPart vinPart = new VinPart();
		VinPartId vinPartId = new VinPartId();
		vinPartId.setProductId(productId);
		vinPartId.setLetSystemName(systemName);
		vinPartId.setDcPartNumber(partNumber);
		vinPart.setId(vinPartId);
		vinPart.setShipStatus(shipStatus); 
		vinPartDao.save(vinPart);
	}

	@Override
	public PartsByProductDto getPartsByProductForSystem(String productId, String systemName) {
		try {
			List<VinPartId> vinParts = vinPartDao.getPartNumber(productId, systemName);
			
			PartsByProductDto partsByProductDto = new PartsByProductDto();
			List<PartsDto> parts = new ArrayList<PartsDto>();
			
			for(VinPartId vinPartId : vinParts) {
				PartsDto part = new PartsDto(vinPartId.getLetSystemName(), vinPartId.getDcPartNumber());
				parts.add(part);
			}
			partsByProductDto.setProductId(productId);
			partsByProductDto.setPartsList(parts);
			
			return partsByProductDto;
		}catch(Exception e) {
			logger.info(e.getMessage());
		}
		
		return null;
	}

	public void saveModelLotAndModelPartApproval(List<ModelPartLotRowDto> modelPartLotDtoList, String selectedPlanCode, String selectedProdLot, String userId) {
		for(ModelPartLotRowDto modelPartLotDto: modelPartLotDtoList) {
			ModelLotId lotId = new ModelLotId();
			lotId.setModelPartId(modelPartLotDto.getModelPartId());
			lotId.setPlanCode(selectedPlanCode);
			ModelLot modelLot = new ModelLot();
			modelLot.setId(lotId);
			modelLot.setStartingProductionLot(selectedProdLot);
			
			ModelPartApproval modelPartApproval = new ModelPartApproval();
			
			modelPartApproval.setApproveStatus(VinBomApprovalStatus.PENDING);
			modelPartApproval.setModelPartId(modelPartLotDto.getModelPartId());
			modelPartApproval.setRequestAssociateNumber(userId);
			modelPartApproval.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
			modelPartApproval.setCurrentInterchangable(modelPartLotDto.getInterchangeable());
			modelPartApproval.setCurrentReflash(modelPartLotDto.getReflash());
			modelPartApproval.setCurrentScrapParts(modelPartLotDto.getScrapParts());
			modelPartApproval.setNewInterchangable(modelPartLotDto.getInterchangeable());
			modelPartApproval.setNewReflash(modelPartLotDto.getReflash());
			modelPartApproval.setNewScrapParts(modelPartLotDto.getScrapParts());
			modelPartApproval.setNewStartingProductionLot(selectedProdLot);
			modelPartApproval.setCurrentStartingProductionLot(modelPartLotDto.getStartingProductionLot());
			
			modelLotDao.save(modelLot);
			VinBomAuditLoggerUtil.logAuditInfo(ModelLot.class, modelLot, userId, VinBomAuditLogChangeType.INSERT);
			modelPartApprovalDao.save(modelPartApproval);
			VinBomAuditLoggerUtil.logAuditInfo(ModelPartApproval.class, modelPartApproval, userId, VinBomAuditLogChangeType.INSERT);
		}
		
	}

	@Override
	public void approveMultiModelPartChange(List<Long> modelPartApprovalIdList, String approveAssociateNumber) {
		for(Long modelPartApprovalId: modelPartApprovalIdList) {
			approveModelPartChange(modelPartApprovalId, approveAssociateNumber);
		}
		
	}

	@Override
	public void denyMultiModelPartChange(List<Long> modelPartApprovalIdList, String approveAssociateNumber) {
		for(Long modelPartApprovalId: modelPartApprovalIdList) {
			denyModelPartChange(modelPartApprovalId, approveAssociateNumber);
		}
		
	}
	
    @Override
	public void denyMultiVinPartChanges(List<Long> vinPartApprovalIdList, String approveAssociateNumber) {
		for(Long vinPartApprovalId: vinPartApprovalIdList) {
			vinPartApprovalDao.denyChange(vinPartApprovalId, approveAssociateNumber);
		}
		
	}
	
	public List<ModelPartLotFilterDto> getLotAssignmentFilters() {
		return modelPartDao.findAllDistinctModelParts();
	}
	
	public List<VinPartFilterDto> getVinPartFilters() {
		return  vinPartDao.findAllDistinctVinParts();
		
	}

	public List<ModelPartLotDto> filterLotAssignments(String dcNumber, String dcPartNumber, String systemName, String model, String active) {
		// TODO Auto-generated method stub
		List<String> activeList = new ArrayList<>();
		List<String> modelsList = new ArrayList<>();

		if(!active.isEmpty()) {
			activeList = Arrays.asList(active.split(","));
		}
		
		if(!model.isEmpty()) {
			modelsList = Arrays.asList(model.split(","));
		}
		
		List<ModelPartLotDto> modelPartLotDtoList = new ArrayList<ModelPartLotDto>();
		List<ModelPart> modelPartList = modelPartDao.filterByCriteria(dcNumber, dcPartNumber, systemName, modelsList, activeList);
		if(modelPartList!=null && !modelPartList.isEmpty()) {
			for(ModelPart modelPart: modelPartList) {
				List<ModelLot> modelLotList = modelLotDao.getAssignedLots(modelPart.getModelPartId());
				if(modelLotList!=null && !modelLotList.isEmpty()) {
					for(ModelLot modelLot: modelLotList) {
						ModelPartLotDto modelPartLotDto = convertToModelPartLotDto(modelPart);
						modelPartLotDto.setPlanCode(modelLot.getId().getPlanCode());
						modelPartLotDto.setStartingProductionLot(StringUtils.isBlank(modelLot.getStartingProductionLot())?"-":modelLot.getStartingProductionLot());
						modelPartLotDtoList.add(modelPartLotDto);
					}
				} else {
					ModelPartLotDto modelPartLotDto = convertToModelPartLotDto(modelPart);
					modelPartLotDto.setStartingProductionLot("-");
					modelPartLotDtoList.add(modelPartLotDto);
				}
			}
		}
		return modelPartLotDtoList;
	}

	
	public List<VinPartDto> filterVinParts(String productId, String productionLot, String partNumber,
			String systemName) {
		List<VinPartDto> vinPartDtoList = new ArrayList<>();
		List<VinPart> vinPartList = vinPartDao.filterByCriteria(productId,productionLot, partNumber, systemName);
		if (vinPartList != null && !vinPartList.isEmpty()) {
			for (VinPart vinPart : vinPartList) {
				VinPartDto vinPartDto = new VinPartDto();
				vinPartDto.setProductId(vinPart.getId().getProductId());
				vinPartDto.setLetSystemName(vinPart.getId().getLetSystemName());
				vinPartDto.setDcPartNumber(vinPart.getId().getDcPartNumber());
				vinPartDto.setShipStatus(vinPart.getShipStatus());
				Frame frame = frameDao.findByKey(vinPart.getId().getProductId());
				if (frame != null) {
					vinPartDto.setProductSpecCode(StringUtils.isBlank(frame.getProductSpecCode())?"-":frame.getProductSpecCode());
					vinPartDto.setProductionLot(StringUtils.isBlank(frame.getProductionLot())?"-":frame.getProductionLot());
				} else {
					vinPartDto.setProductSpecCode("-");
					vinPartDto.setProductionLot("-");
				}
				vinPartDtoList.add(vinPartDto);
			} 
		}
		
		return vinPartDtoList;
	}
	
	// filterVinPartsForMass
	public List<VinPartDto> filterVinPartsByCriteria(String partNumber, String productSpecCode, String systemName) {
		logger.info("STARTED fetching VinPart records based on productSpecCode " + productSpecCode);
		List<VinPartDto> vinPartDtoList = new ArrayList<>();
		List<VinPart> vinPartList = vinPartDao.filterVinPartsBySysNamePartNumAndProdSpecCode(partNumber, productSpecCode, systemName);
		if (vinPartList != null && !vinPartList.isEmpty()) {
			for (VinPart vinPart : vinPartList) {
				VinPartDto vinPartDto = new VinPartDto();
				vinPartDto.setProductId(vinPart.getId().getProductId());
				vinPartDto.setLetSystemName(vinPart.getId().getLetSystemName());
				vinPartDto.setDcPartNumber(vinPart.getId().getDcPartNumber());
				vinPartDto.setShipStatus(vinPart.getShipStatus());
				vinPartDtoList.add(vinPartDto);
			}
		}

		logger.info("FINISHED fetching " + vinPartDtoList.size() + " Vinpart Records");
		return vinPartDtoList;
	}
	
	public List<ModelPart> createVinBomRules(VinBomPartSetDto vinBomPartSet) {
		
			List<ModelPart> modelPartList = new ArrayList<ModelPart>();
			List<ModelLot> modelLotList = new ArrayList<ModelLot>();
			List<ModelPartApproval> modelPartApprovalList = new ArrayList<ModelPartApproval>();
			
			for(VinBomPartDto dto : vinBomPartSet.getVinBomPartList()) {
				ModelPart mpart = createModelPart(dto);
				modelPartList.add(mpart);
				
			}
			List<ModelPart> saveAll = ServiceFactory.getDao(ModelPartDao.class).saveAll(modelPartList);
			
			for(ModelPart part: saveAll) {
				ModelLot mlot = createModelLot(part);
				modelLotList.add(mlot);
			}
			ServiceFactory.getDao(ModelLotDao.class).saveAll(modelLotList);
			
		    for(ModelPart part: saveAll) {
		    	ModelPartApproval approval = createModelPartApproval(vinBomPartSet.getAssociate(), part);
		    	modelPartApprovalList.add(approval);
			}
		    ServiceFactory.getDao(ModelPartApprovalDao.class).saveAll(modelPartApprovalList);
			return modelPartList;
		
	}
	private ModelPartApproval createModelPartApproval(String associate, ModelPart part) {
		ModelPartApproval approval = new ModelPartApproval();
		approval.setModelPartId(part.getModelPartId());
		approval.setCurrentStartingProductionLot(IMMEDIATE);
		approval.setNewStartingProductionLot(IMMEDIATE);
		approval.setRequestAssociateNumber(associate);
		approval.setApproveAssociateNumber(associate);
		approval.setRequestTimestamp(new Timestamp(System.currentTimeMillis()));
		approval.setApproveTimestamp(new Timestamp(System.currentTimeMillis()));
		approval.setApproveStatus(VinBomApprovalStatus.APPROVED);
		approval.setCurrentReflash(false);
		approval.setNewReflash(false);
		approval.setCurrentInterchangable(false);
		approval.setNewInterchangable(false);
		approval.setCurrentScrapParts(false);
		approval.setNewScrapParts(false);
		return approval;
	}

	private ModelLot createModelLot(ModelPart part) {
		String lineNumber = getPropertyBean().getLineNumber();
		ModelLot mlot = new ModelLot();
		ModelLotId id = new ModelLotId();
		id.setModelPartId(part.getModelPartId());
		id.setPlanCode(lineNumber);
		mlot.setId(id);
		mlot.setStartingProductionLot(IMMEDIATE);
		return mlot;
	}

	private ModelPart createModelPart(VinBomPartDto dto) {
		ModelPart mpart = new ModelPart();
		mpart.setProductSpecWildcard(dto.getProductSpecWildCard());
		mpart.setLetSystemName(dto.getLetSystemName());
		mpart.setDcPartNumber(dto.getDcPartNumber());
		mpart.setActive(VinBomActiveStatus.ACTIVE);
		mpart.setDcEffBegDate(new Date());
		mpart.setDcNumber("AUTO");
		mpart.setDcClass("NA");
		return mpart;
	}

	@Override
	public void saveSystemRelationship(List<SystemrelationshipDto> systemRelationshipDtoList) {
		List<SystemRelationship> systemRelationships = new ArrayList<>();
		
		for(SystemrelationshipDto systemrelationshipDto : systemRelationshipDtoList) {
			SystemRelationshipId systemRelationshipId = new SystemRelationshipId();
			systemRelationshipId.setBeamSystemName(systemrelationshipDto.getBeamSystemName());
			systemRelationshipId.setLetSystemName(systemrelationshipDto.getLetSystemName());
			
			SystemRelationship sysRelation = new SystemRelationship();
			sysRelation.setId(systemRelationshipId);
			sysRelation.setUpdatedUser(systemrelationshipDto.getUpdateUser());
			
			systemRelationships.add(sysRelation);
		}
		
		systemRelationshipDao.saveAll(systemRelationships);
	}
	
	@Override
	public void deleteSystemRelationship(List<SystemrelationshipDto> systemRelationshipDtoList) {
		List<SystemRelationship> systemRelationships = new ArrayList<>();
		
		for(SystemrelationshipDto systemrelationshipDto : systemRelationshipDtoList) {
			SystemRelationshipId systemRelationshipId = new SystemRelationshipId();
			systemRelationshipId.setBeamSystemName(systemrelationshipDto.getBeamSystemName());
			systemRelationshipId.setLetSystemName(systemrelationshipDto.getLetSystemName());
			
			SystemRelationship sysRelation = new SystemRelationship();
			sysRelation.setId(systemRelationshipId);
			sysRelation.setUpdatedUser(systemrelationshipDto.getUpdateUser());
			
			systemRelationships.add(sysRelation);
		}
		
		systemRelationshipDao.removeAll(systemRelationships);
	}

	
}
