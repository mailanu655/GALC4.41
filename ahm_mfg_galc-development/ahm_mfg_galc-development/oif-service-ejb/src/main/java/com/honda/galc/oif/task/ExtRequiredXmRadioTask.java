package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.RuleExclusionDao;
import com.honda.galc.dto.ExtRequiredPartSpecDto;
import com.honda.galc.entity.product.PartName;
import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.XmRadioDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.ProductSpecUtil;
import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>XmRadioTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>This file contains VINs that have been VQ Shipped or Exceptional Out 
 * and that have an installed XM Radio. Both American & Canadian Honda (AH) 
 * use this file to activate the XM Radio service trial. This format was revised 
 * starting with 2015 model TLX.</p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>BB</TD>
 * <TD>Mar 20, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author BB
 * @created Nov 18, 2014
 */
public class ExtRequiredXmRadioTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private static final String PROPERTY_ACTIVE_LINE_URL = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','  
	private static final String EXT_REQUIRED_PART_GROUP_NAME = "EXT_REQUIRED_PART_GROUP_NAME";
	private static final String ACURA_PRODUCT_DIVISION_CODE = "ACURA_PRODUCT_DIVISION_CODE";
	private static final String HONDA_PRODUCT_DIVISION_CODE = "HONDA_PRODUCT_DIVISION_CODE";
	private static final String DELIMITER = ",";
	private String partGroup = null;
	private String partNames = null;
	String mQConfig = getProperty("MQ_CONFIG");
	String interfaceId = getProperty("INTERFACE_ID");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	String trackingStatus = getProperty("TRACKING_STATUS");
	String ppVQShip = getProperty("PPVQSHIP");
	String ppAFOff = getProperty("PPAFOFF");
	String acuraModels = getProperty("ACURA_MODEL_CODES");
	String acuraDivisionCode = getProperty(ACURA_PRODUCT_DIVISION_CODE);
	String hondaDivisionCode = getProperty(HONDA_PRODUCT_DIVISION_CODE);
	String countryCode = getProperty("COUNTRY_CODE");
	String productIdPrefix = getProperty("PRODUCT_ID_PREFIX");

	private List<Object[]> partNameList = new ArrayList<Object[]>();

	enum XmPartType{
		XM("XM"),TCU("TCU"),ECALL("ECALL");

		String xmType;
		XmPartType(String type){
			xmType = type;
		}
	}

	public ExtRequiredXmRadioTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
	}


	@Override
	public void execute(Object[] args) {
		try{
			refreshProperties();
			// initialize configuration
			String lines = getProperty(PROPERTY_ACTIVE_LINE_URL);
			partGroup = getProperty(EXT_REQUIRED_PART_GROUP_NAME);
			Timestamp startTs = getStartTime();
			String lastUpdate = startTs.toString();
			Timestamp endTs = getEndTime(startTs);

			//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
			if(StringUtils.isBlank(lines)) {
				logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
				errorsCollector.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
				return;
			}
			
			if(StringUtils.isBlank(partGroup)){
				logger.error("Needed configuration is missing " + EXT_REQUIRED_PART_GROUP_NAME);
				errorsCollector.error("Needed configuration is missing " + EXT_REQUIRED_PART_GROUP_NAME);
				return;
			}

			DateFormat df = new SimpleDateFormat("yyyyMMdd");

			//retrieve and merge data
			String[] activeLines = lines.split(DELIMITER);
			HashMap<String, XmRadioDTO> resultMap = new LinkedHashMap<String, XmRadioDTO>();
			for(String line : activeLines) {
				FrameDao frameDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,FrameDao.class);

				FrameSpecDao frameSpecDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART, FrameSpecDao.class);
				
				ExtRequiredPartSpecDao extRequiredPartSpecDao = HttpServiceProvider.
						getService(line + HTTP_SERVICE_URL_PART, ExtRequiredPartSpecDao.class);

				RuleExclusionDao ruleExclusionDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART, RuleExclusionDao.class);

				List<Object[]> resultSetList = new ArrayList<Object[]>();
				List<ExtRequiredPartSpecDto> extRequiredPartSpecs = extRequiredPartSpecDao.findAllActiveRequiredSpecs(partGroup);
				List<RuleExclusion> excludedRules = ruleExclusionDao.findAll();

				partNameList = getPartNames(partGroup);
				if(partNameList.isEmpty()) {
					logger.error("No parts are configured for Part Group(s).");
					errorsCollector.error("No parts are configured for Part Group(s).");
					return;
				}
				parsePartNames();
				
				List<String> framePrefix = frameSpecDao.findDistinctFramePrefix();

				resultSetList.addAll(frameDao.getVinXmRadioWithExtRequired(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, framePrefix));
				resultSetList.addAll(frameDao.getExtOutVinXmRadioWithExtRequired(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus, framePrefix));

				XmRadioDTO dto = null;

				for(int i=0; resultSetList!=null && i<resultSetList.size(); i++) {
					Object[] lineObj = resultSetList.get(i);
					String key = StringUtils.trim(lineObj[0].toString());
					if(resultMap.containsKey(key)) {
						dto = resultMap.get(key);
						logger.info("Record for Vin : "+key +" exists, updating existing dto record");
					} else {
						dto = new XmRadioDTO();
						dto.setVin(lineObj[0].toString());
						dto.setModelName(StringUtils.trim(lineObj[1].toString()));
						dto.setModelYear(lineObj[2].toString());

						if(acuraModels.contains(lineObj[3].toString())) { //check for Acura model code, otherwise assign as Honda
							if(acuraDivisionCode == null) {
								logger.error("Missing OIF configuration property " + ACURA_PRODUCT_DIVISION_CODE);
								errorsCollector.error("Missing OIF configuration property " + ACURA_PRODUCT_DIVISION_CODE);
								return;
							}
							dto.setProductDivisionCode(acuraDivisionCode);
						} else {

							if(hondaDivisionCode == null) {
								logger.error("Missing OIF configuration property " + HONDA_PRODUCT_DIVISION_CODE);
								errorsCollector.error("Missing OIF configuration property " + HONDA_PRODUCT_DIVISION_CODE);
								return;
							}
							dto.setProductDivisionCode(hondaDivisionCode); 
						}
					}
					boolean partExcluded = false;

					String partName = (String)lineObj[4];
					if(!StringUtils.isBlank(partName)) {
						String partSerialNumber = (String)lineObj[5];
						boolean matchFound= false;
						String productSpecCode = StringUtils.trim((String)lineObj[9].toString());
						String partId = StringUtils.trim((String)lineObj[10].toString());

						String partMask = getPartMask(extRequiredPartSpecs, partName, partId);
						if(!StringUtils.isBlank(partSerialNumber)) {
							String productType = getProductType(partName);
							if(!(productType == null)) {
								partExcluded = checkForExcludedRule(excludedRules, partName, productSpecCode, productType);
								if(!partExcluded) {
									if(CommonPartUtility.verification(partSerialNumber, partMask, PropertyService.getPartMaskWildcardFormat())){
										matchFound = true;
										XmPartType xmType = XmPartType.valueOf(getPartGroup(extRequiredPartSpecs, partName, partId));

										switch(xmType){
										case XM:
											String xmRadioId = getParseInfo(extRequiredPartSpecs, partName, 
													StringUtils.trim((String)lineObj[10].toString()), partSerialNumber);
											dto.setXmRadioIdMusic(xmRadioId);
											dto.setXmRadioIdData(" ");
											break;
										case TCU:
											String tcuNumber = getParseInfo(extRequiredPartSpecs, partName, 
													StringUtils.trim((String)lineObj[10].toString()), partSerialNumber);
											dto.setTcuNumber(tcuNumber);
											break;
										case ECALL:
											String IccId = getParseInfo(extRequiredPartSpecs, partName, 
													StringUtils.trim((String)lineObj[10].toString()), partSerialNumber);
											dto.setIccId(IccId);
											break;
										default:
											break;
										}
									}
								}
							}
						}
						if(!matchFound && !partExcluded)
						{
							logger.info("Failed to verify part serial number, check configuration for part - "+ partName +" with partSerialNumber- "+partSerialNumber);
							errorsCollector.error("Failed to verify part serial number, check configuration for part - "+ partName+" with partSerialNumber- "+partSerialNumber);
						}
					}

					if(!partExcluded) {

						if (lineObj[6] != null) {
							dto.setRadioInstalledDate(df.format(lineObj[6]));
						} else {
							dto.setRadioInstalledDate("");
						}
						if (lineObj.length > 8 && !StringUtils.isBlank(lineObj[8].toString())) {
							dto.setProdDate(df.format(lineObj[8]));
						}else {
							dto.setProdDate("");
						}
						if (lineObj.length > 7 && !StringUtils.isBlank(lineObj[7].toString())) {
							dto.setShipDate(df.format(lineObj[7]));
						} else {
							dto.setShipDate(dto.getProdDate());
						}
						dto.setFiller(" ");
						logger.info("Adding data for product Id - "+key+" data ("+ ToStringUtil.generateToString(dto)+")");
						resultMap.put(key, dto);
					} else {
						logger.info("Product Id - " + key + " with a product spec code of - "
								+ StringUtils.trim((String)lineObj[9].toString()) + " is excluded for part - " + partName);
					}
				}
			}

			if(errorsCollector.getErrorList().isEmpty()){	
				updateLastProcessTimestamp(endTs);
				//export data
				if (resultMap.size() > 0) {
					logger.info("Exporting Xm Radio Data");
					exportDataByOutputFormatHelper(XmRadioDTO.class, new ArrayList<XmRadioDTO>(resultMap.values()));
				}
			}
			logger.info("Finished XM Radio Task");
		} catch (Exception e) {
			logger.info("Unexpected Exception Occurred  while running the XmRadio Task :"
					+ e.getMessage());
			e.printStackTrace();
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}

	private String getParseInfo(
			List<ExtRequiredPartSpecDto> extRequiredPartSpecs, String partName,
			String partId, String partSerialNumber) {
		String parseInfo = null;
		for(ExtRequiredPartSpecDto part : extRequiredPartSpecs) {
			if(part.getPartName().trim().equals(partName.trim()) && part.getPartId().trim().equals(partId.trim())) {
				parseInfo = CommonPartUtility.parsePartSerialNumber(part, partSerialNumber);
				break;
			}
		}
		return parseInfo;
	}

	private String getPartMask(List<ExtRequiredPartSpecDto> extRequiredPartSpecs, String partName, String partId) {
		String partMask = null;

		for(ExtRequiredPartSpecDto  spec : extRequiredPartSpecs) {
			if(spec.getPartName().trim().equals(partName.trim()) && spec.getPartId().trim().equals(partId.trim())){
				partMask = spec.getPartSerialNumberMask().trim();
				break;
			}
		}
		return partMask;
	}

	public List<PartName> getParts(List<String> partNames, PartNameDao partNameDao) {
		List<PartName> objPartNames = new ArrayList<PartName>();
		for(String part : partNames) {
			objPartNames.add(partNameDao.findByKey(part));
		}
		return objPartNames;
	}

	private List<Object[]> getPartNames(String partGroup) {
		List<Object[]> parts =  ServiceFactory.getDao(ExtRequiredPartSpecDao.class).findAllActiveByPartGroup(partGroup);
		return parts;
	}

	private boolean checkForExcludedRule(List<RuleExclusion> excludedRules, String partName,
			String productSpecCode, String productType ) {

		List<RuleExclusion> matchedExcludedRules = new ArrayList<RuleExclusion>();
		if(!StringUtils.isBlank(productType)) {
			matchedExcludedRules = ProductSpecUtil.getMatchedRuleList(productSpecCode, productType, 
					excludedRules, RuleExclusion.class);
		}

		for(RuleExclusion rule : matchedExcludedRules) {
			if(rule.getId().getPartName().trim().equals(partName.trim())) {
				return true;	
			}
		}
		return false;
	}

	private void parsePartNames() {
		for(Object[] partName : partNameList) {
			if(partNames == null) {
				partNames = partName[0].toString().trim();
			}else
				partNames = partNames + DELIMITER +  partName[0].toString().trim();
		}
	}

	private String getProductType(String partName) {
		for(Object[] part : partNameList) {
			if(StringUtils.trim(partName).equals(StringUtils.trim(part[0].toString()))){
				return StringUtils.trim(part[1].toString());
			}
		}
		logger.error("Could not find product type for part: " + partName);
		errorsCollector.error("Could not find product type for part: " + partName);
		return null;
	}

	private String getPartGroup(List<ExtRequiredPartSpecDto> extRequiredPartSpecs, String partName, String partId) {

		for(ExtRequiredPartSpecDto spec : extRequiredPartSpecs) {
			if(StringUtils.trim(spec.getPartName()).equals(StringUtils.trim(partName)) &&
					StringUtils.trim(spec.getPartId()).equals(StringUtils.trim(partId))){
				return spec.getPartGroup();
			}
		}
		return null;
	}
}	