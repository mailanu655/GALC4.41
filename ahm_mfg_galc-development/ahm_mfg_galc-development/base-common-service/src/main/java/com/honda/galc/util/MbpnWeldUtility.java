package com.honda.galc.util;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.checkers.CheckPoints;
import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.ReactionType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.constant.OperationType;
import com.honda.galc.dao.conf.MCOperationMeasurementDao;
import com.honda.galc.dao.conf.MCOperationPartRevisionDao;
import com.honda.galc.dao.conf.MCOperationRevisionDao;
import com.honda.galc.dao.conf.MCOrderStructureDao;
import com.honda.galc.dao.conf.MCOrderStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCProductStructureDao;
import com.honda.galc.dao.conf.MCProductStructureForProcessPointDao;
import com.honda.galc.dao.conf.MCStructureDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.conf.MCOperationMeasurement;
import com.honda.galc.entity.conf.MCOperationRevision;
import com.honda.galc.entity.conf.MCOperationRevisionId;
import com.honda.galc.entity.conf.MCProductStructure;
import com.honda.galc.entity.conf.MCProductStructureForProcessPoint;
import com.honda.galc.entity.conf.MCStructure;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.ExceptionalOut;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.vios.ProductStructureService;
import com.honda.galc.vios.dto.PddaPlatformDto;

public class MbpnWeldUtility {
	private static final String MBPN_MADE_FROM_PREFIX = "MBPN_MADE_FROM_PREFIX";
	private static final String MBPN_SERIAL_NO_PREFIX = "MBPN_SERIAL_NO_PREFIX";
	private static final String PROCESS_POINT_LIST = "PROCESS_POINT_LIST";
	public static MCStructureDao structureDao;
	public static MCOperationRevisionDao opRevDao;
	public static MCOperationPartRevisionDao opPartRevDao;
	public static MbpnProductDao mbpnProductDao;
	public static InstalledPartDao installedPartDao;
	public static MeasurementDao measurementDao;
	public static MCOperationMeasurementDao operationMeasurementDao;
	public static ProcessPointDao processPointDao;
	public static MCOrderStructureDao orderStructureDao;
	public static MCOrderStructureForProcessPointDao orderStructureForProcessPointDao;
	public static MCProductStructureDao productStructureDao;
	public static MCProductStructureForProcessPointDao productStructureForProcessPointDao;
	public static BuildAttributeDao buildAttributeDao;
	public static ProductStructureService productStructureService;
	
	public MbpnWeldUtility() {
	}

	public static MCStructureDao getMCStructureDao() {
		if (structureDao == null) {
			structureDao = getDao(MCStructureDao.class);
		}
		return structureDao;
	}

	public static MCOperationPartRevisionDao getMCOperationPartRevisionDao() {
		if (opPartRevDao == null) {
			opPartRevDao = getDao(MCOperationPartRevisionDao.class);
		}
		return opPartRevDao;
	}

	public static MCOperationRevisionDao getMCOperationRevisionDao() {
		if (opRevDao == null) {
			opRevDao = getDao(MCOperationRevisionDao.class);
		}
		return opRevDao;
	}

	public static CheckResult checkMadeFrom(PddaPlatformDto pddaplatform, String productId, String productSpecCode, ProcessPoint processPoint, ReactionType defaultReactionType) {
		String msg = null;
		//Check if product id exists
		MbpnProduct mbpnProduct = getMbpnProduct(productId);
		//Get Made From MBPN
		BuildAttribute mbpnAttribute = null;
		if (pddaplatform!= null && pddaplatform.getModelYearDate() > 0.0) {
			//Model year received, fetch build attributes using model year
			mbpnAttribute = getBuildAttributeDao().findById(MBPN_MADE_FROM_PREFIX+"_"+Float.toString(pddaplatform.getModelYearDate()), productSpecCode);
		}
		if(mbpnAttribute == null) {
			//Model year is not received or no attributes found, fetch build attributes without model year
			mbpnAttribute = getBuildAttributeDao().findById(MBPN_MADE_FROM_PREFIX, productSpecCode);
		}
		if(mbpnAttribute != null && StringUtils.isNotBlank(mbpnAttribute.getAttributeValue())) {
			if (mbpnProduct == null) {
				//Record does not exist in mbpn_product_tbx, it is new product id
				//Made From MBPN is configured for new product id
				msg = "Made From MBPN is configured for new product id: " + productId;
				return createCheckResult(msg, defaultReactionType);
			}
			else {
				//Product exists
				//Perform Same MBPN already assigned check
				//if yes, return warning
				msg = MbpnWeldUtility.validateSpecCodeAssignment(mbpnProduct, productSpecCode);
				if (StringUtils.isNotBlank(msg)) {
					return createCheckResult(msg, ReactionType.DISPLAY_WARNING_MSG);
				}
				
				//Perform current MBPN completed check
				//if no, return warning
				CheckResult checkResult = stopShipCheck(mbpnProduct, processPoint, ReactionType.DISPLAY_WARNING_MSG);
				if(checkResult!=null) {
					return checkResult;
				}
				
				//Perform made from check
				String madeFromSpecCode = mbpnProduct.getCurrentProductSpecCode();
				String[] validPrefixes = StringUtils.split(mbpnAttribute.getAttributeValue(), Delimiter.COMMA);
				if(StringUtils.startsWithAny(madeFromSpecCode, validPrefixes)) {
					//MBPN Made From mask is good
					return null;
				}
				else {
					//MBPN Made from Mask is Not Good
					msg = "MBPN ("+madeFromSpecCode+") does not match the Made From prefixes ("+mbpnAttribute.getAttributeValue()+")";
					return createCheckResult(msg, defaultReactionType);
					
				}
			}
		}
		else {
			//No MBPN Made From found
			if(mbpnProduct!=null) {
				//Throw Warning
				msg = "No Made From Unit Found for MBPN "+productSpecCode;
				return createCheckResult(msg, ReactionType.DISPLAY_WARNING_MSG);
			}
		}
		return null;
	}
	
	public static String assignedToOrder(String productId, String currentOrderNo) {
		MbpnProduct mbpnProduct = getMbpnProduct(productId);
		
		if (mbpnProduct == null) {
			// No record exists in mbpn_product_tbx, it is new product id
			return null;
		}
		String mbpnAssignedToOrder = mbpnProduct.getCurrentOrderNo();
		
		if(mbpnAssignedToOrder.equalsIgnoreCase(currentOrderNo)){
			return null;
		}else{
			String msg = "Scanned Serial Number is not associated to current Order "+currentOrderNo;
			List<MbpnProduct> assignedMbpns = getMbpnProductDao().findAllByProductionLot(currentOrderNo);
			
			if(assignedMbpns != null && assignedMbpns.size()> 0){
				msg = msg +" Serial Number(s) for current order are: "+Delimiter.NEW_LINE;
				
				for(MbpnProduct mbpn:assignedMbpns){
					msg = msg + mbpn.getProduct()+Delimiter.NEW_LINE;
				}
			}
			return msg;
		}
	}
	
	public static CheckResult stopShipCheck(MbpnProduct mbpnProduct, ProcessPoint processPoint, ReactionType reactionType) {
		String msg = null;
		List<ProcessPoint> processPointLst = null;
		String mode = PropertyService.getProperty(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE, StructureCreateMode.DIVISION_MODE.toString());

		if (mbpnProduct != null) {
			//Creating product structure for process point if it does not exist
			if(processPoint != null) {
				try {
					if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())){
						getProductStructureService().findOrCreateProductStructure(mbpnProduct, processPoint, mode);
					}else if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())){
						List<String> configuredProcessPointLst = PropertyService.getPropertyList(processPoint.getProcessPointId(), PROCESS_POINT_LIST);
						processPointLst = ServiceFactory.getDao(ProcessPointDao.class).getProcessPointLst(configuredProcessPointLst);
						
						for(ProcessPoint pp : processPointLst){
							getProductStructureService().findOrCreateProductStructure(mbpnProduct, pp, mode);
						}
					}
				
				} catch (Exception e) {
					Logger.getLogger(processPoint.getProcessPointId()).error(e, "Ignoring Exception occurred while creating structure before executing checkers at check point "
							+ CheckPoints.AFTER_STRUCTURE_CREATE.toString());
					
				}

			}
			// Fetch structure
			List<MCStructure> structure = fetchStructures(mbpnProduct, processPointLst, mode);
			if (structure != null) {
				if(!checkProduct(structure, mbpnProduct.getProductId())) {
					msg = "Product id '"+mbpnProduct.getProductId()+"' (MBPN: '"+mbpnProduct.getCurrentProductSpecCode()+"') is not completed";
					return createCheckResult(msg, reactionType);
				}
			}
			else {
				msg = "No VIOS data found for Product id '"+mbpnProduct.getProductId()+"' (MBPN: '"+mbpnProduct.getCurrentProductSpecCode()+"')";
				return createCheckResult(msg, reactionType);
			}
		}
		else {
			msg = "MBPN Product does not exist";
			return createCheckResult(msg, reactionType);
		}
		return null;
	}

	public static MbpnProductDao getMbpnProductDao() {
		if (mbpnProductDao == null) {
			mbpnProductDao = getDao(MbpnProductDao.class);
		}
		return mbpnProductDao;
	}

	public static String validateSpecCodeAssignment(String productId,
			String productSpecCode) {
		MbpnProduct mbpnProduct = getMbpnProduct(productId);
		return validateSpecCodeAssignment(mbpnProduct, productSpecCode);
	}
	
	public static String validateSpecCodeAssignment(MbpnProduct mbpnProduct,
			String productSpecCode) {
		if(mbpnProduct!=null && mbpnProduct.getCurrentProductSpecCode()
				.equals(productSpecCode))
			return "Product Already Assigned to Order "+mbpnProduct.getCurrentOrderNo();
		else
			return null;

	}
	
	public static boolean checkProduct(List<MCStructure> structure, String productId) {
		ConcurrentHashMap<String, MCOperationRevision> operationMap = new ConcurrentHashMap<String, MCOperationRevision>();
		ConcurrentHashMap<String, List<MCOperationMeasurement>> operationMeasMap = new ConcurrentHashMap<String, List<MCOperationMeasurement>>();
		Set<String> opPartSet = new HashSet<String>();
		for (MCStructure record : structure) {
			String operationName = record.getId().getOperationName();
			if (!operationMap.containsKey(operationName)) {
				MCOperationRevision operation = fetchOpRev(record);
				operationMap.put(operationName, operation);
			}
			// Fetching measurements
			if (hasMeasurements(operationMap.get(operationName))) {
				String partId = record.getId().getPartId();
				int partRevision = record.getId().getPartRevision();
				String opPart = operationName + "_" + partId + "_"
						+ partRevision;
				if (!opPartSet.contains(opPart)) {
					List<MCOperationMeasurement> opMeasurements = getOperationMeasurementDao()
							.findAllMeasurementForOperationPartAndPartRevision(
									operationName, partId, partRevision);
					List<MCOperationMeasurement> opMeasList = operationMeasMap
							.get(operationName);
					opMeasList = opMeasList != null ? opMeasList
							: new ArrayList<MCOperationMeasurement>();
					if (opMeasurements != null && !opMeasurements.isEmpty()) {
						opMeasList.addAll(opMeasurements);
					}
					operationMeasMap.put(operationName, opMeasList);
					opPartSet.add(opPart);
				}
			}
		}

		return isComplete(productId, operationMap, operationMeasMap);
	
	}

	private static MCOperationRevision fetchOpRev(MCStructure record) {
		// Fetching operation revision
		MCOperationRevisionId opRevId = new MCOperationRevisionId();
		opRevId.setOperationName(record.getId().getOperationName());
		opRevId.setOperationRevision(record.getId().getOperationRevision());
		return getMCOperationRevisionDao().findByKey(opRevId);
	}

	private static boolean isComplete(
			String productId,
			ConcurrentHashMap<String, MCOperationRevision> operationMap,
			ConcurrentHashMap<String, List<MCOperationMeasurement>> operationMeasMap) {
		// Fetching installed parts and measurements
		ConcurrentHashMap<String, InstalledPart> installedPartMap = getInstalledPartMap(
				productId, new ArrayList<String>(Collections.list(operationMap.keys())));
		
		// Checking whether all operations are complete
		for (String operationName : Collections.list(operationMap.keys())) {
			if (!(Collections.list(installedPartMap.keys()).contains(operationName))) {
				// operation name is absent in installed parts
				return false;
			} else {
				MCOperationRevision operation = operationMap.get(operationName);
				InstalledPart installedPart = installedPartMap
						.get(operationName);
				if (hasScanPart(operation)
						&& StringUtils.isEmpty(installedPart
								.getPartSerialNumber())) {
					// Part mask is not present
					return false;
				}
				if (hasMeasurements(operation)) {
					// Validating measurement
					int goodCount = getGoodMeasurementCount(installedPart);
					List<MCOperationMeasurement> opMeas = operationMeasMap
							.get(operationName);
					int expectedCount = opMeas != null ? opMeas.size() : 0;
					if (goodCount < expectedCount) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static List<MCStructure> fetchStructures(MbpnProduct mbpnProduct, List<ProcessPoint> processPointList, String mode) {
		List<MCStructure> structureList = new ArrayList<MCStructure>();
		String productId = mbpnProduct.getProductId();
		String productSpecCode = mbpnProduct.getCurrentProductSpecCode();
		if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())){
			List<MCProductStructure> productStructureList = getProductStructureDao().findAllBy(productId, productSpecCode);
			if(productStructureList!=null) {
				for(MCProductStructure productStructure: productStructureList) {
					String divId = productStructure.getId().getDivisionId();
					long structureRevision = productStructure.getStructureRevision();
					List<MCStructure> structures = getMCStructureDao().getStructuresByDivision(productSpecCode, divId, structureRevision);
					if(structures!=null && !structures.isEmpty()) {
						structureList.addAll(structures);
					}
				}
			}
		}else if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.PROCESS_POINT_MODE.toString())){
			
			for(ProcessPoint processPoint : processPointList){
				List<MCProductStructureForProcessPoint> productStructureList = getProductStructureForProcessPointDao().findAllBy(productId, productSpecCode, processPoint.getProcessPointId());
				if(productStructureList!=null) {
					for(MCProductStructureForProcessPoint productStructure: productStructureList) {

						List<MCStructure> structures = getMCStructureDao().getStructures(productSpecCode, productStructure.getId().getProcessPointId(), productStructure.getStructureRevision());
						if(structures!=null && !structures.isEmpty()) {
							structureList.addAll(structures);
						}
					}
				}
			}
			

		}
		return structureList;
	}
	
	private static ConcurrentHashMap<String, InstalledPart> getInstalledPartMap(
			String productId, List<String> partNameList) {
		ConcurrentHashMap<String, InstalledPart> installedPartMap = new ConcurrentHashMap<String, InstalledPart>();
		List<InstalledPart> installedParts = getValidInstalledParts(productId,
				partNameList);
		if (installedParts != null) {
			for (InstalledPart installedPart : installedParts) {
				installedPartMap.put(installedPart.getId().getPartName(),
						installedPart);
			}
		}
		return installedPartMap;
	}

	private static List<InstalledPart> getValidInstalledParts(String productId,
			List<String> partNameList) {
		List<InstalledPart> installedParts = getInstalledPartDao()
				.findAllValidParts(productId, partNameList);
		return getMeasurementDao().findMeasurementsForInstalledParts(
				installedParts);
	}

	private static boolean hasMeasurements(MCOperationRevision operation) {
		if (operation == null)
			return false;

		boolean hasMeasurements = operation.getType().equals(
				OperationType.GALC_MEAS)
				|| operation.getType().equals(OperationType.GALC_MEAS_MANUAL)
				|| operation.getType()
						.equals(OperationType.GALC_SCAN_WITH_MEAS)
				|| operation.getType().equals(
						OperationType.GALC_SCAN_WITH_MEAS_MANUAL);
		return hasMeasurements;
	}

	private static boolean hasScanPart(MCOperationRevision operation) {
		if (operation == null)
			return false;

		return (operation.getType().equals(OperationType.GALC_SCAN)
				|| operation.getType()
						.equals(OperationType.GALC_SCAN_WITH_MEAS) || operation
				.getType().equals(OperationType.GALC_SCAN_WITH_MEAS_MANUAL));
	}

	public static int getGoodMeasurementCount(InstalledPart installedPart) {
		int i = 0;
		if (installedPart != null) {
			List<Measurement> measurements = installedPart.getMeasurements();
			if (measurements != null) {
				for (Measurement meas : measurements) {
					if (meas.getMeasurementStatus()
							.equals(MeasurementStatus.OK)) {
						i++;
					}
				}
			}
		}
		return i;
	}
	
	public static ProductStructureService getProductStructureService() {
		if (productStructureService == null) {
			productStructureService = ServiceFactory
					.getService(ProductStructureService.class);
		}
		return productStructureService;
	}

	public static InstalledPartDao getInstalledPartDao() {
		if (installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		return installedPartDao;
	}

	public static MeasurementDao getMeasurementDao() {
		if (measurementDao == null)
			measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		return measurementDao;
	}

	public static MCOperationMeasurementDao getOperationMeasurementDao() {
		if (operationMeasurementDao == null)
			operationMeasurementDao = ServiceFactory
					.getDao(MCOperationMeasurementDao.class);
		return operationMeasurementDao;
	}

	public static ProcessPointDao getProcesPointDao() {
		if (processPointDao == null) {
			processPointDao = getDao(ProcessPointDao.class);
		}
		return processPointDao;
	}

	public static MCProductStructureForProcessPointDao getProductStructureForProcessPointDao() {
		if (productStructureForProcessPointDao == null) {
			productStructureForProcessPointDao = getDao(MCProductStructureForProcessPointDao.class);
		}
		return productStructureForProcessPointDao;
	}
	
	public static MCProductStructureDao getProductStructureDao() {
		if (productStructureDao == null) {
			productStructureDao = getDao(MCProductStructureDao.class);
		}
		return productStructureDao;
	}
	
	public static BuildAttributeDao getBuildAttributeDao() {
		if(buildAttributeDao == null){
			buildAttributeDao = getDao(BuildAttributeDao.class);
		}
		return buildAttributeDao;
	}
	
	public static String checkMbpnSn(String serialNumber, String productSpecCode){
		List<BuildAttribute> mbpnAttributes =  getBuildAttributeDao().findAllMatchBuildAttributes(MBPN_SERIAL_NO_PREFIX);
		
		if(mbpnAttributes != null && !mbpnAttributes.isEmpty()){
			for(BuildAttribute attribute : mbpnAttributes){
				if(CommonPartUtility.simpleVerification(productSpecCode, attribute.getProductSpecCode())){
					if(!StringUtils.isEmpty(attribute.getAttributeValue())){
						String[] validPrefixes = StringUtils.split(attribute.getAttributeValue(), Delimiter.COMMA);
						if(validPrefixes!= null && !StringUtils.startsWithAny(serialNumber, validPrefixes)){
							return "The valid Serial No. prefixes for '"+productSpecCode+"': "+attribute.getAttributeValue();
						}
					}else return "Serial No. prefixes are not configured for MBPN ("+productSpecCode+")";
						
				}
			}
		} else return "Serial No. prefixes are not configured for MBPN ("+productSpecCode+")";
			
		return null;
	}
	public static MbpnProduct getMbpnProduct(String productId) {
		return getMbpnProductDao().findByKey(productId);
	}
	
	public static CheckResult mbpnPartMaskVerification(MbpnProduct mbpnProduct, String partMask, ReactionType reactionType) {
		String msg = null;
		if (mbpnProduct == null) {
			// No record exists in mbpn_product_tbx, it is new product id
			msg = "MBPN Product does not exist";
			return createCheckResult(msg, reactionType);
		}
		else if (StringUtils.isEmpty(partMask)
				|| !CommonPartUtility.verification(mbpnProduct.getCurrentProductSpecCode(),
						partMask,
						PropertyService.getPartMaskWildcardFormat())) {
			msg = "MBPN "+mbpnProduct.getCurrentProductSpecCode()+" is not matching with part mask";
			return createCheckResult(msg, reactionType);
		}
		else {
			// Check Defect Status
			ProductCheckUtil productCheckUtil = new ProductCheckUtil();
			productCheckUtil.setProduct(mbpnProduct);
			if(productCheckUtil.productScrappedCheck()) {
				//Product is scrapped
				String comment = "";
				List<ExceptionalOut> scrappedExceptionalOutList =  productCheckUtil.checkScrappedExceptionalOut();
				if(scrappedExceptionalOutList!=null && scrappedExceptionalOutList.iterator().hasNext()) {
					comment = scrappedExceptionalOutList.iterator().next().getExceptionalOutComment();
				}
				msg = "MBPN Product "+mbpnProduct.getProductId()+" is scrapped";
				if(StringUtils.isNotBlank(comment)) {
					msg += " with comment: " + comment;
				}
				return createCheckResult(msg, reactionType);	
			}
			else return null;
		}
	}
	
	public static CheckResult mbpnChildPartVerification(MbpnProduct mbpnProduct, String partMask, ReactionType reactionType) {
		CheckResult checkResult = null;
		//Perform MBPN Mask Check
		checkResult = mbpnPartMaskVerification(mbpnProduct, partMask, reactionType);
		if(checkResult!=null) {
			return checkResult;
		}
		//Perform MBPN Complete Check
		checkResult = stopShipCheck(mbpnProduct, null, reactionType);
		if(checkResult!=null) {
			return checkResult;
		}
		return null;
	}

	public static CheckResult createCheckResult(String msg, ReactionType reactionType) {
		CheckResult checkResult = new CheckResult();
		checkResult.setCheckMessage(msg);
		checkResult.setReactionType(reactionType);
		return checkResult;
	}
}
