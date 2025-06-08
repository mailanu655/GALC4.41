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
import com.honda.galc.service.msip.dto.outbound.SrsDataDto;
import com.honda.galc.service.msip.property.outbound.SrsDataPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class SrsDataHandler extends BaseMsipOutboundHandler<SrsDataPropertyBean> {

	private static String serviceId = null;
	
	private String productType = null;
	private String engineUrls = null;
	
	SrsDataHandler(){
	}
	
	/**
	 * The timestamp to indicate if its date falls in a working production day.
	 * This is mainly used to determine if the interface file needs to be sent
	 * on Saturday. If the Saturday is a production day, the file will be sent.
	 * Otherwise, it will not be sent to MQ.
	 */
	private java.sql.Timestamp prodStartTs;
	private java.sql.Timestamp prodEndTs;
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SrsDataDto> fetchDetails(Date startTimestamp, int duration) {
		List<SrsDataDto> dtoList = new ArrayList<SrsDataDto>();
		try{
			getLogger().info("initializing AHM020SecondaryVIN.");
			productType = getPropertyBean().getEngProductType();
			engineUrls = getPropertyBean().getEngineUrls();
			prodStartTs = new Timestamp(startTimestamp.getTime());
			prodEndTs = getTimestamp(prodStartTs, duration);
			return exportRecords();
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			SrsDataDto dto = new SrsDataDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
		
	}
	
	private List<SrsDataDto> exportRecords() {
		// call the web services on the active lines and get
		// data from each line
		String[] activeLinesURLsArr = getPropertyBean().getActiveLineUrls();
		String activeLineURL = null;
		String strStartTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodStartTs);
		String strEndTs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(prodEndTs);
		try {
			List<SrsDataDto> sVinDTOList = new ArrayList<SrsDataDto>();
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
		}
		return null;
	}
	

	public List<SrsDataDto> getSecondaryVins(String startTime, String endTime, String serviceId, String lineURL) throws Exception {

		List<SrsDataDto> sVinDTOList = new ArrayList<SrsDataDto>();
		try {
			String pp = PropertyService.getProperty(getComponentId(), "COLLECT_PROCESS_POINT", null);
			Boolean JapanVINLeftJustified = getPropertyBean().getJapanVinLeftJustified();
			String separator = PropertyService.getProperty(getComponentId(), "INSTALLED_PART_NAMES_SEPARATOR", ",");
			List<String> partNameList = PropertyService.getPropertyList(getComponentId(), "INSTALLED_PART_NAMES",separator);
			Map<String, String> dtoPartNameMapping = getPropertyBean().getNameMapping(); 
			InstalledPartDao daoIPart = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, InstalledPartDao.class);
			ProductResultDao daoPResult = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, ProductResultDao.class);
			FrameDao frameDao = HttpServiceProvider.getService(lineURL + OIFConstants.HTTP_SERVICE_URL_PART, FrameDao.class);
			
			InstalledPartDao daoIPartEngineLine = null;
			EngineDao engineDaoEngineLine = null;
			// NOTE: specify the URLs to Engine environments, use "LOCAL" for the same line
			String[] engineURLsArr = new String[0];
			if (StringUtils.isNotBlank(engineUrls))
				engineURLsArr = engineUrls.split(",");
			
			HashMap<String, String> thisEntry = null;
			List<ProductResult> resultList = daoPResult.getSecondaryVinsByProcessPointAndDateRange(pp, startTime, endTime);
			for(ProductResult thisProductResult : resultList)  {
				String productId = thisProductResult.getProductId();
				List<InstalledPart> partsList = daoIPart.findAllByProductIdAndPartNames(productId, partNameList);
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
				SrsDataDto sVinDTO = new SrsDataDto();
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
	

}
