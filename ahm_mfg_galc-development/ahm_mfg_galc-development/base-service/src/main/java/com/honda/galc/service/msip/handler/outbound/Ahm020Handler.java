package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.Ahm020Dto;
import com.honda.galc.service.msip.property.outbound.Ahm020PropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Ahm020Handler extends BaseMsipOutboundHandler<Ahm020PropertyBean> {

	private static String serviceId = null;
	
	private String productType = null;
	private String engineUrls = null;
	
	Ahm020Handler(){
	}
	
	/**
	 * The timestamp to indicate if its date falls in a working production day.
	 * This is mainly used to determine if the interface file needs to be sent
	 * on Saturday. If the Saturday is a production day, the file will be sent.
	 * Otherwise, it will not be sent to MQ.
	 */
	private java.sql.Timestamp prodStartTs;
	private java.sql.Timestamp prodEndTs;
	
	List<InstalledPart> partsList;
	InstalledPartDao daoIPartEngineLine = null;
	EngineDao engineDaoEngineLine = null;

	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Ahm020Dto> fetchDetails(Date startTimestamp, int duration) {
		getLogger().info("initializing AHM020SecondaryVIN.");
		productType = getPropertyBean().getEngProductType();
		engineUrls = getPropertyBean().getEngineUrls();
		prodStartTs = new Timestamp(startTimestamp.getTime());
		prodEndTs = getTimestamp(prodStartTs, duration);
		return exportRecords();
	}
	
	private List<Ahm020Dto> exportRecords() {
		// call the web services on the active lines and get
		// data from each line
		String[] activeLinesURLsArr = getPropertyBean().getActiveLineUrls();
		String activeLineURL = null;
		String strStartTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodStartTs);
		String strEndTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodEndTs);
		List<Ahm020Dto> sVinDTOList = new ArrayList<Ahm020Dto>();
		try {
			for (String lineURL : activeLinesURLsArr){
				activeLineURL = lineURL;				
				sVinDTOList.addAll( getSecondaryVins(strStartTs, strEndTs, serviceId, lineURL) );
			}
			return sVinDTOList;

		} catch (Exception e) {
			String errorStr = "Exception raised when sending interface data for "
					+ getComponentId()
					+ " URL: "
					+ activeLineURL
					+ "  Exception : " + e.getMessage();
			getLogger().error(errorStr);
			sVinDTOList.clear();
			Ahm020Dto dto = new Ahm020Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + errorStr);
			dto.setIsError(true);
			sVinDTOList.add(dto);
			return sVinDTOList;
		}
	}
	

	public List<Ahm020Dto> getSecondaryVins(String startTime, String endTime, String serviceId, String lineURL) throws Exception {

		List<Ahm020Dto> sVinDTOList = new ArrayList<Ahm020Dto>();
		try {
			String pp = PropertyService.getProperty(getComponentId(), "COLLECT_PROCESS_POINT", null);
			Boolean JapanVINLeftJustified = getPropertyBean().getJapanVinLeftJustified();
			String separator = PropertyService.getProperty(getComponentId(), "INSTALLED_PART_NAMES_SEPARATOR", ",");
			List<String> partNameList = PropertyService.getPropertyList(getComponentId(), "INSTALLED_PART_NAMES",separator);
			Map<String, String> dtoPartNameMapping = getPropertyBean().getNameMapping(); 
			InstalledPartDao daoIPart = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, InstalledPartDao.class);
			ProductResultDao daoPResult = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, ProductResultDao.class);
			FrameDao frameDao = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, FrameDao.class);
			
			
			// NOTE: specify the URLs to Engine environments, use "LOCAL" for the same line
			String[] engineURLsArr = new String[0];
			if (StringUtils.isNotBlank(engineUrls))
				engineURLsArr = engineUrls.split(",");
			
			HashMap<String, String> thisEntry = null;
			List<ProductResult> resultList = daoPResult.getSecondaryVinsByProcessPointAndDateRange(pp, startTime, endTime);
			for(ProductResult thisProductResult : resultList)  {
				String productId = thisProductResult.getProductId();
				partsList = daoIPart.findAllByProductIdAndPartNames(productId, partNameList);
				Frame thisFrame = frameDao.findByKey(productId);
				String engineSerialNo =  "";
				if (thisFrame!=null)
					engineSerialNo = thisFrame.getEngineSerialNo();
				
				thisEntry = new HashMap<String, String>();
				thisEntry.put("PRODUCT_ID", productId);
				thisEntry.put("ENGINE_SERIAL_NO", engineSerialNo);
				if(productId != null)  {
					String value = ProductNumberDef.justifyJapaneseVIN(productId, JapanVINLeftJustified.booleanValue());
					thisEntry.put("FRAME_NO", value);								
				}
				
				if(StringUtils.isNotBlank(engineSerialNo)){
					String value = String.format("%12s", engineSerialNo.trim());
					thisEntry.put("ENGINE_NO", value);								
					thisEntry = processEngineParts(thisEntry, engineURLsArr, lineURL, engineSerialNo, partNameList);
				}
				
				for (InstalledPart thisPart : partsList)  {
					String partName = thisPart.getPartName();
					if (dtoPartNameMapping != null) {
						String dtoPartName = dtoPartNameMapping.get(partName);
						if (StringUtils.isNotBlank(dtoPartName)) {
							partName = StringUtils.trim(dtoPartName);
						}
					}
					thisEntry.put(partName, thisPart.getPartSerialNumber());
				}
				Ahm020Dto sVinDTO = new Ahm020Dto();
				sVinDTO.initialize(thisEntry);
				sVinDTOList.add(sVinDTO);
			}
			
		} catch (Exception e) {
			String errMsg = "Exception occured in ProductionResultServiceImpl.getSecondaryVins(...) method. The exception message is:- " + e.getMessage();
			getLogger().error(e, errMsg);
			throw e;
		}
		return sVinDTOList;
	}
	
	private HashMap<String, String> processEngineParts(HashMap<String, String>thisEntry, String[] engineURLsArr,
			String lineURL,String engineSerialNo,List<String> partNameList){
		// allow multiple Engine Lines
		for (String engineUrl : engineURLsArr) {
			if (StringUtils.isNotBlank(engineUrl)){
				// it determines if it's necessary to use the same line on we are working or a custom url for engine line.
				String auxEngineURL = engineUrl.equalsIgnoreCase("LOCAL") ? lineURL : engineUrl;
				daoIPartEngineLine = HttpServiceProvider.getService(auxEngineURL + OIFConstants.HTTP_SERVICE_URL_PART, InstalledPartDao.class);
				engineDaoEngineLine = HttpServiceProvider.getService(auxEngineURL + OIFConstants.HTTP_SERVICE_URL_PART, EngineDao.class);
			}
			
			String missionSerialNo = "";
			if (engineDaoEngineLine!=null){
				Engine thisEngine = engineDaoEngineLine.findByKey(engineSerialNo);
				if (thisEngine!=null)
					missionSerialNo = thisEngine.getMissionSerialNo();
			}
			thisEntry.put("MISSION_SERIAL_NO", missionSerialNo);
			
			// Engine Parts List. Try to obtain Installed Parts for the Engine associated with the Frame
			if (StringUtils.isNotBlank(productType) && daoIPartEngineLine!=null){
				List<InstalledPart> enginePartsList = daoIPartEngineLine.findAllEngineByEngineProductIdAndPartNames(productType, engineSerialNo, partNameList);
				partsList.addAll(enginePartsList);
			}
			
			daoIPartEngineLine = null;
			engineDaoEngineLine = null;
		}
		return thisEntry;
	}
}
