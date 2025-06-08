package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.service.msip.dto.outbound.XmRadioDto;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.property.outbound.XmRadioPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Sivakumar Ponnusamy
 * @date Nov 17, 2017
 */
public class XmRadioHandler extends BaseMsipOutboundHandler<XmRadioPropertyBean> {

	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	String trackingStatus = null;
	String ppVQShip = null;
	String ppAFOff = null;
	String acuraModels = null;
	String countryCode = null;
	
	Map<String, String> partMasksMap= null;
	Map<String, String> parseInfoMap= null;
	Map<String, String> partTypesMap= null;
	Map<String, String> dataUsedMap= null;
	
	enum XmPartType{
		XM("XM"),TCU("TCU");
		
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
			if(StringUtils.isEmpty(xmPartName) || xmPartName.length() < 11)  {
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
					getLogger().error("Unable to get data position for XMRadio id or prefix");
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
	@SuppressWarnings({ "deprecation", "unused", "unchecked" })
	@Override
	public List<XmRadioDto> fetchDetails() {
		List<XmRadioDto> xmRadioList = new ArrayList<XmRadioDto>();
		Timestamp startTs = getStartTime();
		Timestamp endTs = getEndTime(startTs);
		return getRecords(startTs, endTs);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<XmRadioDto> fetchDetails(Date startTimestamp, int duration) {
		Timestamp sTs = new Timestamp(startTimestamp.getTime());
		Timestamp endTs = getTimestamp(sTs, duration);
		return getRecords(sTs, endTs);
	}
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings({ "rawtypes" })
	public List<XmRadioDto> getRecords(Timestamp startTs, Timestamp endTs) {
		List<XmRadioDto> xmRadioList = new ArrayList<XmRadioDto>();
		String errorMsg = null;
		Boolean isError = false;
		initProperties();
		try{
			// initialize configuration
			String lines = getPropertyBean().getActiveLineUrls();
			String lastUpdate = startTs.toString();
			
			//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
			if(StringUtils.isBlank(lines)) {
				XmRadioDto dto = new XmRadioDto();
				getLogger().error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
				dto.setErrorMsg("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
				dto.setIsError(true);
				xmRadioList.add(dto);
				return xmRadioList;
			}
			//retrieve and merge data
			String[] activeLines = lines.split(",");
			HashMap<String, XmRadioDto> resultMap = new LinkedHashMap<String, XmRadioDto>();
			for(String line : activeLines) {
				isError = exportData(line, errorMsg, resultMap, lastUpdate);
			}
			if(!isError){	
				updateLastProcessTimestamp(endTs);
				Iterator<Entry<String, XmRadioDto>> it = resultMap.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry pair = (Map.Entry)it.next();
					xmRadioList.add((XmRadioDto)pair.getValue());
				}
			}else{
				XmRadioDto dto = new XmRadioDto();
				dto.setErrorMsg(errorMsg);
				dto.setIsError(isError);
				xmRadioList.add(dto);
				return xmRadioList;
			}
			getLogger().info("Finished XM Radio Task");
		} catch (Exception e) {
			getLogger().error("Unexpected Exception Occurred  while running the XmRadio Task :"
					+ e.getMessage());
			e.printStackTrace();
			XmRadioDto dto = new XmRadioDto();
			dto.setErrorMsg("Unexpected exception occured");
			dto.setIsError(true);
			xmRadioList.add(dto);
			return xmRadioList;
		}
		return xmRadioList;
	}
	
	
	private List<String> getPartNames(){
		String groupNames = getPropertyBean().getGroupNames();
		Map<String, String> partNamesMap= PropertyService.getPropertyMap(getComponentId(),"PART_NAME");
		String[] groups = groupNames.split(",");
		List<String> partNames = new ArrayList<String>();
		
		for(String group:groups){
			String partName = partNamesMap.get(group);
			if(partName != null && !StringUtils.isEmpty(partName)){
				partNames.add(partName);
			}
		}
		
		return partNames;
	}
	
	private List<String> getGroupsForPartName(String partName){
		Map<String, String> partNamesMap= PropertyService.getPropertyMap(getComponentId(),"PART_NAME");
		String groupNames = getPropertyBean().getGroupNames();
		String[] groups = groupNames.split(",");
		List<String> matchingGroups = new ArrayList<String>();
		for(String group:groups){
			String tempPartName = partNamesMap.get(group.trim());
			if(tempPartName!= null && tempPartName.trim().equalsIgnoreCase(partName)){
				matchingGroups.add(group.trim());
			}
		}
		return matchingGroups;
	}
	
	private XmRadioDto getDtoFromMap(Object[] lineObj, String key, Map<String, XmRadioDto> resultMap){
		XmRadioDto dto = null;
		if(resultMap.containsKey(key)) {
			dto = resultMap.get(key);
			getLogger().info("Record for Vin : "+key +" exists, updating existing dto record");
		} else {
			dto = new XmRadioDto();
			dto.setVin((inBound(lineObj, 0))?lineObj[0].toString():"");
			dto.setModelName((inBound(lineObj, 1))?StringUtils.trim(lineObj[1].toString()):"");
			dto.setModelYear((inBound(lineObj, 2))?lineObj[2].toString():"");

			String model = (inBound(lineObj, 3))?lineObj[3].toString():"";
			if(acuraModels.contains(model)) //check for Acura model code, otherwise assign as Honda
				dto.setProductDivisionCode("B");
			else dto.setProductDivisionCode("A");
		}
		return dto;
	}

	private boolean inBound(Object[] obj, int index){
		boolean inBound = (index >= 0) && (index < obj.length);
		return inBound;
	}
	
	private boolean updateRadioPartsInfo(XmRadioDto dto, Object[] lineObj, List<String> groups, String partSerialNumber){
		boolean matchFound = false;
		for(String group: groups){
			String partType = partTypesMap.get(group);
			String parseInfo = parseInfoMap.get(group);
			String partMask = partMasksMap.get(group);
			String dataUsed = dataUsedMap.get(group);
		
			if(!StringUtils.isBlank(partSerialNumber) ) {
				if(CommonPartUtility.verification(partSerialNumber, partMask,PropertyService.getPartMaskWildcardFormat())){
					matchFound= true;
					if(Boolean.parseBoolean(dataUsed)){
						String pos[] = parseInfo.split(",");
						if(partType.equalsIgnoreCase(XmPartType.XM.name())){
							String posPrefix = (inBound(pos, 0))?pos[0]:"";
							String posPrefixLen = (inBound(pos, 1))?pos[1]:""; 
							String posId = (inBound(pos, 2))?pos[2]:"";
							String posIdLen = (inBound(pos, 3))?pos[3]:"";
							XMRadioParts xmParts = new XMRadioParts(posPrefix,posPrefixLen, posId, posIdLen);
							xmParts.parseXMParts(partSerialNumber);
							dto.setXmRadioIdPrefix(xmParts.xmRadioPrefix);
							dto.setXmRadioIdMusic(xmParts.xmRadioId);
							dto.setXmRadioIdData(" ");
						}else{
							dto.setTcuNumber(partSerialNumber);
						}
					}
				}
			}
		}
		return matchFound;
	}
	
	private boolean exportData(String line, String errorMsg, Map<String, XmRadioDto> resultMap, String lastUpdate){

		FrameDao frameDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,FrameDao.class);
					
		List<String> partNames = getPartNames();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
				
		List<Object[]> resultSetList = frameDao.getExOutVinXmRadio(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode, trackingStatus);
		resultSetList.addAll(frameDao.getVQShipVinXmRadio(partNames, ppVQShip, ppAFOff, lastUpdate, countryCode));
		XmRadioDto dto = null;
		boolean isError = false;
	
		for(int i=0; resultSetList!=null && i<resultSetList.size(); i++) {
			Object[] lineObj = resultSetList.get(i);
			String key = StringUtils.trim(lineObj[0].toString());
			dto = getDtoFromMap(lineObj, key, resultMap);
			
			String partName = (String)lineObj[4];
			if(!StringUtils.isBlank(partName) ) {
				List<String> groups = getGroupsForPartName(partName.trim());
				if(groups.size() == 0){
					getLogger().info("No Group Configured for part - "+partName);
					isError = true;
					errorMsg = "No Group Configured for part - "+partName;
				}else{
					String partSerialNumber = (String)lineObj[5];
					boolean matchFound= false;
					matchFound = updateRadioPartsInfo(dto, lineObj, groups, partSerialNumber);
					if(!matchFound)
					{
						getLogger().info("Failed to verify part serial number, check configuration for part - "+ partName +" with partSerialNumber- "+partSerialNumber);
						isError = true;
						errorMsg = "No Group Configured for part - "+partName;
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
					if (lineObj.length > 7 && !StringUtils.isBlank(lineObj[7].toString())) {
						dto.setShipDate(df.format(lineObj[7]));
					} else {
						dto.setShipDate(dto.getProdDate());
					}
					dto.setFiller(" ");
					getLogger().info("Adding data for product Id - "+key+" data ("+ ToStringUtil.generateToString(dto)+")");
					resultMap.put(key, dto);
				}
			}
		}
		return isError;
	}
	
	private void initProperties(){
		trackingStatus = getPropertyBean().getTrackingStatus();
		ppVQShip = getPropertyBean().getPpvqship();
		ppAFOff = getPropertyBean().getPpafoff();
		acuraModels = getPropertyBean().getAcuraModelCodes();
		countryCode = getPropertyBean().getCountryCode();
		
		partMasksMap= PropertyService.getPropertyMap(getComponentId(),"PART_MASK");
		parseInfoMap= PropertyService.getPropertyMap(getComponentId(),"PARSE_INFO");
		partTypesMap= PropertyService.getPropertyMap(getComponentId(),"PART_TYPE");
		dataUsedMap= PropertyService.getPropertyMap(getComponentId(),"DATA_USED");
		
		
	}
}
