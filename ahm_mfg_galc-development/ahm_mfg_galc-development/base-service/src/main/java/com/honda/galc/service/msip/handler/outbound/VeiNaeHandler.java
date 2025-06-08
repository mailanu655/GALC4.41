package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.VeiNaeDto;
import com.honda.galc.service.msip.property.outbound.VeiNaePropertyBean;


/*
 * 
 * @author Sivakumar Ponnusamy
 * @date Nov 17, 2017
 */
public class VeiNaeHandler extends BaseMsipOutboundHandler<VeiNaePropertyBean> {
	final static String COMPONENT_ID = "VEINAE";
	
	@SuppressWarnings("unchecked")
	@Override
	public List<VeiNaeDto> fetchDetails() {
		List<VeiNaeDto> dtoList = new ArrayList<VeiNaeDto>();
		try{
			return processNseValidEins();
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			VeiNaeDto dto = new VeiNaeDto();
			e.printStackTrace();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getStackTrace());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	private List<VeiNaeDto> processNseValidEins() {
		getLogger().info("Started NseValidEinsTask.");
		List<VeiNaeDto> validEinList = new ArrayList<VeiNaeDto>();
		List<Object[]> tempList = new ArrayList<Object[]>();
		String[] activeLineURLs = getPropertyBean().getActiveLines();
		for (String activeLineURL : activeLineURLs) {
			if(activeLineURL == null) {
				getLogger().error("ACTIVE_LINE_URLs not set.");
				continue;
			}
			getLogger().info("Requesting data from " + activeLineURL);
			EngineDao engineDao = HttpServiceProvider.getService(activeLineURL + getPropertyBean().getHttpServiceUrlPart(), EngineDao.class);
			tempList.clear();
			if(engineDao != null) {
				tempList = engineDao.findValidEins();
			}
			if(tempList.size() == 0) {
				getLogger().error("No data from " + activeLineURL);
				continue;
			} else {
				getLogger().info("Found " + tempList.size() + " records from " + activeLineURL);
			}
			String prefix = getPropertyBean().getPrefix();
			for(Object[] objs : tempList) {
				VeiNaeDto engineData = new VeiNaeDto();
				engineData.setEngineId(objs[0].toString());
				engineData.setModelYear(objs[1].toString());
				engineData.setModelCode(objs[2].toString());
				engineData.setModelTypeCode(objs[3].toString());
				engineData.setModelOptionCode(objs[4].toString());
				engineData.setTimestamp((Timestamp)objs[5]);
				engineData.setPrefix(prefix);
				validEinList.add(engineData);
			}
		}
		return validEinList;
	}
}
