package com.honda.galc.client.dc.common;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import com.honda.galc.client.dc.property.PDDAPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;


/**
 * @author Shweta kadav
 * @date Dec 28, 2015
 */
public final class ClientDeviceResolution {
	
	private static ClientDeviceResolution instance = null;
	private static String DEFAULT_VIOS = "DEFAULT_VIOS";
		
	private ClientDeviceResolution() {}
	
	public static ClientDeviceResolution getInstance() {
		if (instance == null) {
			instance = new ClientDeviceResolution();
		}
		return instance;
	}
	

	public static ArrayList<Integer> getPossibleResolution() {
		PDDAPropertyBean property = PropertyService.getPropertyBean(PDDAPropertyBean.class);
		String possibleResolutions = property.getPossibleResolutions();
		ArrayList<Integer> resolutionList = new ArrayList<Integer>();
		if(null!=possibleResolutions){
			String[] possibleResolutionList = possibleResolutions.split(",");
			
			for(int i=0 ;i< possibleResolutionList.length;i++){
				int indexOf = possibleResolutionList[i].indexOf("*");
				if (indexOf != -1) {
					resolutionList.add(Integer.parseInt(possibleResolutionList[i].substring(0 , indexOf)));
				}
			}
		}
		java.util.Collections.sort(resolutionList);
		return resolutionList;
	}
	
	public static String getClientResolutionProperty(String propertyName, String type,String defaultValue) {
	int clientResolution = 0 ;
	 String propertyValue = null ;
	 ArrayList<Integer> resolutionList = getPossibleResolution();
	 Rectangle2D primarySceneBounds = Screen.getPrimary().getVisualBounds();
		double clientDeviceWidth = primarySceneBounds.getWidth();
		if(null!=resolutionList){
			for(int i=0 ;i< resolutionList.size();i++){
				 clientResolution = resolutionList.get(i); 
				if(clientDeviceWidth >= (clientResolution-50)){
					if(type.equalsIgnoreCase("width")){
						String tempPropertyValue = PropertyService.getProperty(DEFAULT_VIOS, propertyName+clientResolution);
						if(null!=tempPropertyValue){
							propertyValue = String.valueOf((Double.parseDouble(tempPropertyValue)*clientResolution)/100);
						}else{
							propertyValue = defaultValue;
						}
					}else if(type.equalsIgnoreCase("font")){
						propertyValue = PropertyService.getProperty(DEFAULT_VIOS, propertyName+clientResolution);
					}
				}
			}
			
		}
		
		if(propertyValue == null){
			return defaultValue;
		}
		Logger.getLogger().check("Client device resolution :: " + clientResolution+", Width in pixel :: "+ propertyValue);
		return propertyValue;
	}
}
