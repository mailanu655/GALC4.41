package com.honda.galc.client.bridge.mdrs;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.logging.TrainingDataCache;
import com.honda.galc.dto.TrainingDataDto;
import com.honda.galc.net.HttpClient;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.SortedArrayList;

public class MdrsStateBean {
	private static final String GET_INACTIVE_DAYS = "GetInactiveDays/";
	private static final String GET_ASSOCIATE_TRAINING = "GetAssociateTraining/";
	private static TrainingDataCache userTrainingCache = null;
	private static final String LOG_NAME = "Mdrs_bridge";
	
	  private static Logger getLogger(){
	  return Logger.getLogger(LOG_NAME);
}
	
	public static void getUserTrainingData(String userid) {
		List dataDto = null ;
		boolean isUserTrained = true;
		int lastLoginDateDiff = getLastLoginDateDiff(userid);
		SystemPropertyBean systemPropertyBean =  PropertyService.getPropertyBean(SystemPropertyBean.class);
		SortedArrayList<String> cacheKeys = new SortedArrayList<String>((Collection<? extends String>) getUserTrainingCache().getValidKeys());
		for (Object cacheKey : cacheKeys) {
			Element element = getUserTrainingCache().get(cacheKey);
			if (element != null) {
				dataDto = (List) element.getObjectValue();

			}
		}
		if(null!=dataDto){
			for (int i = 0; i < dataDto.size(); i++) {
				TrainingDataDto  trainingDataDto = (TrainingDataDto) dataDto.get(i);
				if(null!=trainingDataDto ){
					if(trainingDataDto.getTrainingStatus() == 0 ){
						isUserTrained = false;
						break;
					}
				}
			}
		}
		
		if(!(!isUserTrained && lastLoginDateDiff < systemPropertyBean.getTrainingDataCachedDays())){
			TrainingDataCache.getInstance().put(userid, getMDRSTrainingData(userid));
		}
		
	}
	
	public static int getLastLoginDateDiff(String userid) {
		int noOFDays = 10;
		Gson gson =  new Gson();
		String response = HttpClient.get(PropertyService.getPropertyBean(SystemPropertyBean.class).getMdrsRestUrl()+GET_INACTIVE_DAYS+CommonUtil.removeChars(userid),  HttpURLConnection.HTTP_OK);
		if (StringUtils.isBlank(response)) return noOFDays;
		if(response.contains("Inactive_days")){
			JsonParser parser = new JsonParser();
			String noOfDaysJson = parser.parse(response.toString()).getAsString();
			TrainingDataDto trainingDataDto = gson.fromJson(noOfDaysJson, TrainingDataDto.class);
			noOFDays = trainingDataDto.getInactive_days();
			getLogger().info("Number of inactive days from MDRS "+noOFDays + "for User "+userid);
		}
		 
		return noOFDays ;
	}
	
	public static  List<TrainingDataDto> getMDRSTrainingData(String userid) {
		String response = "";
		Gson gson =  new Gson();
		List<TrainingDataDto> trainingDataDtoList = new ArrayList<TrainingDataDto>();
		try {
			
			response = HttpClient.get(PropertyService.getPropertyBean(SystemPropertyBean.class).getMdrsRestUrl()+GET_ASSOCIATE_TRAINING+ CommonUtil.removeChars(userid),HttpURLConnection.HTTP_OK);
			if (StringUtils.isBlank(response)) return trainingDataDtoList;
			if(response.contains("ProcessId")){
				JsonParser parser = new JsonParser();
				String obj = parser.parse(response).getAsString();
				JsonArray jsonArray = parser.parse(obj).getAsJsonArray();
				Type type = new TypeToken<List<TrainingDataDto>>(){}.getType();
		    	trainingDataDtoList = gson.fromJson(jsonArray, type);
		    	getLogger().info("Training data for User  "+userid +" is "+response);
			}else{
				getLogger().info("No data found for User "+userid);
			}
		} catch (Exception e) {
			getLogger().info("Unable to invoke MDRS service "+userid);
		}
		
		return trainingDataDtoList;
	}

	
	public static TrainingDataCache getUserTrainingCache() {
		if (userTrainingCache == null)
			userTrainingCache = new TrainingDataCache();

		return userTrainingCache;
	}
	
	

}
