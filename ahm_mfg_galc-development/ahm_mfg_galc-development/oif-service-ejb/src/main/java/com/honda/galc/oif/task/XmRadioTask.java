package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.XmRadioDTO;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.CommonPartUtility;
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
 * <TD>KG</TD>
 * <TD>Nov 18, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Nov 18, 2014
 */
public class XmRadioTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private static final String PROPERTY_ACTIVE_LINE_URL = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','  
	String mQConfig = getProperty("MQ_CONFIG");
	String interfaceId = getProperty("INTERFACE_ID");
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	String trackingStatus = getProperty("TRACKING_STATUS");
	String ppVQShip = getProperty("PPVQSHIP");
	String ppAFOff = getProperty("PPAFOFF");
	String acuraModels = getProperty("ACURA_MODEL_CODES");
	String countryCode = getProperty("COUNTRY_CODE");
	String groupNames = getProperty("GROUP_NAMES");
	Map<String, String> partNamesMap= PropertyService.getPropertyMap(getComponentId(),"PART_NAME");
	Map<String, String> partMasksMap= PropertyService.getPropertyMap(getComponentId(),"PART_MASK");
	Map<String, String> parseInfoMap= PropertyService.getPropertyMap(getComponentId(),"PARSE_INFO");
	Map<String, String> partTypesMap= PropertyService.getPropertyMap(getComponentId(),"PART_TYPE");
	Map<String, String> dataUsedMap= PropertyService.getPropertyMap(getComponentId(),"DATA_USED");
	Map<String, String> subPartNameMap = PropertyService.getPropertyMap(getComponentId(),"SUB_PART_NAME");
	Map<String, String> subSubPartNameMap = PropertyService.getPropertyMap(getComponentId(),"SUB_SUB_PART_NAME");
	Map<String, String> partSerialNoSourceMap= PropertyService.getPropertyMap(getComponentId(),"PART_SERIAL_NO_SOURCE");
	Boolean isCommonName =  PropertyService.getPropertyBoolean(getComponentId(), "IS_COMMON_NAME", false);
	
	enum XmPartType{
		XM("XM"),TCU("TCU"),ECALL("ECALL");
		
		String xmType;
		XmPartType(String type){
			xmType = type;
		}
		
	}
	class XMRadioParts  {
		
		String xmRadioId = "";
		String xmRadioPrefix = "";
		int startPrefix = 0, lenPrefix = 0;
		int startId = 0, lenId = 0;
		String posStartPrefix = null;
		String poslenPrefix = null;
		String posStartId = null;
		String posLenId = null;
		
		XMRadioParts(String startPrefix,String lenPrefix, String startId, String lenId)  {
			posStartPrefix = startPrefix;
			poslenPrefix=lenPrefix;
			posStartId = startId;
			posLenId = lenId;
		}
		
		private void parseXMParts(String xmPartName)  {	
			reset();
			if(StringUtils.isEmpty(xmPartName))  {
				return;
			}
			if(!StringUtils.isEmpty(posStartPrefix) && !StringUtils.isEmpty(poslenPrefix) && !StringUtils.isEmpty(posStartId) && !StringUtils.isEmpty(posLenId) ){
			try {
						startPrefix = Integer.parseInt(posStartPrefix);
						lenPrefix = Integer.parseInt(poslenPrefix);
						startId = Integer.parseInt(posStartId);
						lenId = Integer.parseInt(posLenId);
					
				} catch (NumberFormatException nfe) {
					reset();
					logger.error(nfe, "Unable to get data position for XMRadio id or prefix");
					errorsCollector.error("Unable to get data position for XMRadio id or prefix, using default values");
				}
			}
			if((xmPartName.length() >= (startPrefix + lenPrefix)) && (xmPartName.length() >= (startId + lenId)))   {
				xmRadioPrefix = xmPartName.substring(startPrefix, startPrefix+lenPrefix);
				xmRadioId = xmPartName.substring(startId, startId+lenId);
			}
		}
		private void reset()  {
			startPrefix = 0;
			lenPrefix = 0;
			startId = 0;
			lenId = 0;
		}
	}
		
	public XmRadioTask(String name) {
		super(name);
		errorsCollector = new OifErrorsCollector(name);
	}
	
	
	@Override
	public void execute(Object[] args) {
		try{
			refreshProperties();
			// initialize configuration
			String lines = getProperty(PROPERTY_ACTIVE_LINE_URL);
			Timestamp startTs = getStartTime();
			String lastUpdate = startTs.toString();
			Timestamp endTs = getEndTime(startTs);
			
			//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
			if(StringUtils.isBlank(lines) || StringUtils.isBlank(groupNames)) {
				logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"],[Group Names: "+groupNames+"]");
				setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
				return;
			}
		
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			
			//retrieve and merge data
			String[] activeLines = lines.split(",");
			HashMap<String, XmRadioDTO> resultMap = new LinkedHashMap<String, XmRadioDTO>();
			
			for(String line : activeLines) {
				FrameDao frameDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,FrameDao.class);
				
				List<Object[]> resultSetList = new ArrayList<Object[]>();
				List<String> partNames = getPartNames();
				List<String> subPartNames = getSubPartNames();
				List<String> subSubPartNames = getSubSubPartNames();
				List<String> galcPartList = new ArrayList<String>();
				List<String> letPartList = new ArrayList<String>();
				
				for(String partName:partNames) {
					String source = partSerialNoSourceMap.get(partName);
					if(source == null || source.trim().equalsIgnoreCase("GALC")) {
						galcPartList.add(partName);
					} else if(source.trim().equalsIgnoreCase("LET")) {
						letPartList.add(partName);
					} else {
						galcPartList.add(partName);
					}
				}
				
				if(isCommonName) {
					List<Object[]> exOutVinXmRadio = frameDao.getVIOSExOutVinXmRadio(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus);
					List<Object[]> vqShipVinXmRadio = frameDao.getVIOSVQShipVinXmRadio(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode);
				 	resultSetList.addAll(exOutVinXmRadio);
					resultSetList.addAll(vqShipVinXmRadio);
				}else {
					if(!subPartNames.isEmpty()) {
						resultSetList = frameDao.getExOutVinXmRadioWithSubProduct(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus,subPartNames);
						List<Object[]> vqShipVinXmRadio = frameDao.getVQShipVinXmRadioWithSubProduct(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, subPartNames);
						resultSetList.addAll(vqShipVinXmRadio);
					}
					if(!subSubPartNames.isEmpty()) {
						resultSetList = frameDao.getExOutVinXmRadioWithSubSubProduct(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus,subPartNames, subSubPartNames);
						List<Object[]> vqShipVinXmRadio = frameDao.getVQShipVinXmRadioWithSubSubProduct(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, subPartNames, subSubPartNames);
						resultSetList.addAll(vqShipVinXmRadio);
					}
	                if(!galcPartList.isEmpty()){
	                	List<Object[]> exOutVinXmRadio = frameDao.getExOutVinXmRadio(galcPartList, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus);
	                	List<Object[]> vqShipVinXmRadio = frameDao.getVQShipVinXmRadio(galcPartList, ppVQShip, ppAFOff, lastUpdate, countryCode);
	                	if(exOutVinXmRadio != null && exOutVinXmRadio.size() > 0)
	                		resultSetList.addAll(exOutVinXmRadio);
	                	if(vqShipVinXmRadio != null && vqShipVinXmRadio.size() > 0)
	                		resultSetList.addAll(vqShipVinXmRadio);
	                }
				}
				
				XmRadioDTO dto = null;
				if(resultSetList != null)	{
					for(int i=0; i<resultSetList.size(); i++) {
						Object[] lineObj = resultSetList.get(i);
						String key = StringUtils.trim(lineObj[0].toString());
						if(resultMap.containsKey(key)) {
							dto = resultMap.get(key);
							logger.info("Record for Vin : "+key +" exists, updating existing dto record");
						} else {
							dto = new XmRadioDTO();
							String vin = lineObj[0].toString();
							dto.setVin(vin);
							dto.setModelName(StringUtils.trim(lineObj[1].toString()));
							dto.setModelYear(lineObj[2].toString());
		
							if(acuraModels.contains(lineObj[3].toString())) //check for Acura model code, otherwise assign as Honda
								dto.setProductDivisionCode("B");
							else dto.setProductDivisionCode("A");
						}
						
						String partName = (String)lineObj[4];
						if(!StringUtils.isBlank(partName) ) {
							List<String> groups = new ArrayList<String>();
							if(isCommonName) {
								//Ex: modelCode+CommonName(T6NXMRADIO)
								groups.add(lineObj[3].toString()+lineObj[9].toString());
							}else {
								groups = getGroupsForPartName(partName.trim());
							}
							if(groups.size() == 0){
								logger.info("No Group Configured for part - "+partName);
								errorsCollector.error("No Group Configured for part - "+partName);
								setJobStatus(OifRunStatus.NO_GROUP_CONFIGURED_FOR_PART);
							}else{
								if(!letPartList.isEmpty()) {
									for(String letPart: letPartList) {
										groups.add(letPart);
									}
								}
								String partSerialNumber = (String)lineObj[5];
								boolean matchFound= false;
								for(String group: groups){
									String partType = partTypesMap.get(group);
									String parseInfo = parseInfoMap.get(group);
									String partMask = partMasksMap.get(group);
									String dataUsed = dataUsedMap.get(group);
								
									if(!StringUtils.isBlank(partSerialNumber) ) {
										if(CommonPartUtility.verification(partSerialNumber, partMask,PropertyService.getPartMaskWildcardFormat())){
											matchFound= true;
											if(Boolean.parseBoolean(dataUsed)){
												
												XmPartType xmType = XmPartType.valueOf(partType);
												switch(xmType){
													
												case XM:
													String xmPos[] = parseInfo != null?parseInfo.split(","): new String[]{"0","0","0","0"};
													String posPrefix = xmPos[0];
													String posPrefixLen = xmPos[1]; 
													String posId = xmPos[2];
													String posIdLen = xmPos[3];
													XMRadioParts xmParts = new XMRadioParts(posPrefix,posPrefixLen, posId, posIdLen);
													xmParts.parseXMParts(partSerialNumber);
													dto.setXmRadioIdPrefix(xmParts.xmRadioPrefix);
													dto.setXmRadioIdMusic(xmParts.xmRadioId);
													dto.setXmRadioIdData(" ");
													break;
												case TCU:
													String tcuPartSerialNumber = partSerialNumber;
													if(letPartList != null && letPartList.size() > 0) {
														tcuPartSerialNumber = "                                    ";
														List<Object[]> vqShipVinXmRadio = frameDao.getVQShipVinXmRadioFromLet(group.trim(), ((String)lineObj[0].toString()).trim());
														if(vqShipVinXmRadio != null && vqShipVinXmRadio.size() > 0) {
															Object[] tcuPartSerialObj = vqShipVinXmRadio.get(0);
															String tcuPartSerial = StringUtils.trim(tcuPartSerialObj[0].toString());
															tcuPartSerialNumber = tcuPartSerial;
														}
													}
													String tcu = parsePartSerialNumber(tcuPartSerialNumber, parseInfo);
													if(StringUtils.isEmpty(tcu)){
														logger.info("unable to parse TCU - "+ partName +" with partSerialNumber- "+partSerialNumber +" for vin-"+key);
														errorsCollector.error("unable to parse TCU - "+ partName +" with partSerialNumber- "+partSerialNumber +" for vin-"+key);
													}else{
														dto.setTcuNumber(tcu);
													}
													break;
												case ECALL:
													String iccidPartSerialNumber = partSerialNumber;
													if(letPartList != null && letPartList.size() > 0) {
														iccidPartSerialNumber = "                                    ";
														List<Object[]> vqShipVinXmRadio = frameDao.getVQShipVinXmRadioFromLet(partName.trim(), ((String)lineObj[0].toString()).trim());
														if(vqShipVinXmRadio != null && vqShipVinXmRadio.size() > 0) {
															Object[] iccidPartSerialObj = vqShipVinXmRadio.get(0);
															String iccidPartSerial = StringUtils.trim(iccidPartSerialObj[0].toString());
															iccidPartSerialNumber = iccidPartSerial;
														}
													}
													String iccId = parsePartSerialNumber(iccidPartSerialNumber, parseInfo);
													if(StringUtils.isEmpty(iccId)){
														logger.info("unable to parse ICCId - "+ partName +" with partSerialNumber- "+partSerialNumber +" for vin-"+key);
														errorsCollector.error("unable to parse ICCId - "+ partName +" with partSerialNumber- "+partSerialNumber +" for vin-"+key);
													}else{
														dto.setIccId(iccId);
													}
													
													break;
												default:
														break;
												
												}
												
											}
										}
									}
								}
								if(!matchFound)
								{
									logger.info("Failed to verify part serial number, check configuration for part - "+ partName +" with partSerialNumber- "+partSerialNumber);
									errorsCollector.error("Failed to verify part serial number, check configuration for part - "+ partName+" with partSerialNumber- "+partSerialNumber);
								}
						
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
								//Exceptional ship doesn't have ship date
								if (lineObj.length > 7 && lineObj[7] != null && !StringUtils.isBlank(lineObj[7].toString())) {
									dto.setShipDate(df.format(lineObj[7]));
								} else {
									dto.setShipDate(dto.getProdDate());
								}
								dto.setFiller(" ");
								logger.info("Adding data for product Id - "+key+" data ("+ ToStringUtil.generateToString(dto)+")");
								resultMap.put(key, dto);
							}
						}
						
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
				logger.info("Finished XM Radio Task");
			}else {
				setFailedCount(resultMap.size());
				logger.error("Finished XM Radio Task with errors");
			}
		} catch (Exception e) {
			logger.error(e, "Unexpected Exception Occurred  while running the XmRadio Task : "
					+ (e.getMessage() == null ? e.toString() : e.getMessage()));
				e.printStackTrace();
				errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
				errorsCollector.sendEmail();
		}
		
	}
	
	private List<String> getPartNames(){
		String[] groups = groupNames.split(",");
		List<String> partNames = new ArrayList<String>();

		for(String group:groups){
			String partName = partNamesMap.get(group);
			//Only add the partName if not exists
			if(partName != null && !StringUtils.isEmpty(partName) && !partNames.contains(partName)){
				partNames.add(partName);
			}
		}
		return partNames;
	}
	
	private List<String> getSubPartNames(){
		String[] groups = groupNames.split(",");
		List<String> partNames = new ArrayList<String>();

		for(String group:groups){
			if(!subPartNameMap.isEmpty()){
				String partName = subPartNameMap.get(group);
				if(!StringUtils.isEmpty(partName)){
					partNames.add(partName);
				}
			}
		}
		return partNames;
	}
	
	private List<String> getSubSubPartNames(){
		String[] groups = groupNames.split(",");
		List<String> partNames = new ArrayList<String>();

		for(String group:groups){
			if(!subSubPartNameMap.isEmpty()){
				String partName = subSubPartNameMap.get(group);
				if(!StringUtils.isEmpty(partName)){
					partNames.add(partName);
				}
			}
		}
		return partNames;
	}

	private List<String> getGroupsForPartName(String partName){
		String[] groups = groupNames.split(",");
		List<String> matchingGroups = new ArrayList<String>();
		for(String group:groups){
			String tempPartName = partNamesMap.get(group.trim());
			if(tempPartName!= null && partName.contains(tempPartName.trim())){
				matchingGroups.add(group.trim());
			}
			
		}
		return matchingGroups;
	}
	
	private String parsePartSerialNumber(String partSerialNumber, String parseInfo){
		String pos[] = parseInfo != null?parseInfo.split(","): new String[]{"0","0"};
		int start = Integer.parseInt(pos[0]);
		int len = Integer.parseInt(pos[1]);
		
		if(partSerialNumber.length() >= (start+len)){
			return partSerialNumber.substring(start, start+len);
		}
		return "";
	}
}	