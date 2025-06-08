package com.honda.galc.device.bbscale;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.net.MessageHandler;

public class BBScaleMessageHandler extends MessageHandler<String>{
	
	public static final String DEVICE_MESSAGE_INVALID = "Invalid message received";

	private BBScaleSocketDevice device;
	private static int retryCount = 0;
	
	public BBScaleMessageHandler(BBScaleSocketDevice device) {
		this.device = device;
	}

	@Override
	public void processItem(String response) {
		
		String[] items = StringUtils.split(response, Delimiter.COMMA);
		if(items == null || items.length <5) return;
		
		String vin = StringUtils.trim(items[0]);
		long motionRequestInterval = device.getAutoRequestInterval();
		int numRetries = device.getNumRetries();
		List<ScaleWeight> scaleWeights = new ArrayList<ScaleWeight>();
		scaleWeights.add(parseScaleWeight(items[1], Corner.BBSCALES_LFRT));
		scaleWeights.add(parseScaleWeight(items[2], Corner.BBSCALES_LRR));
		scaleWeights.add(parseScaleWeight(items[3], Corner.BBSCALES_RFRT));
		scaleWeights.add(parseScaleWeight(items[4], Corner.BBSCALES_RRR));
		
		if(isStable(scaleWeights))  {
			device.notifyListeners(new VehicleWeight(vin,scaleWeights));
			setRetryCount(0);
		}
		else  {
			if (device.isRetryRequest() && getRetryCount() < numRetries) {
				try {
					Thread.sleep(motionRequestInterval);
					device.requestWeights(vin);
					incrementRetryCount();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private ScaleWeight parseScaleWeight(String item,Corner corner) {
		ScaleWeight scaleWeight = new ScaleWeight();
		double wt = parseDouble(item);
		if("LB".equalsIgnoreCase(parseWeightUnit(item)))  {
			wt = wt/2.2D;
		}
		scaleWeight.setWeight(wt);
		scaleWeight.setStable(!parseMotionFlag(item));
		scaleWeight.setCorner(corner);
		return scaleWeight;
	}
	
	private double parseDouble(String item) {
		Matcher doubleMatcher = Pattern.compile("[0-9\\.]+").matcher(item);
	    return doubleMatcher.find()? Double.parseDouble(doubleMatcher.group()) : 0.0D;	
	}
	
	private String parseWeightUnit(String item){
		return item.contains("lb") ? "LB" :"KG";
	}
	
	private boolean parseMotionFlag(String item) {
		return item.contains("m") || item.contains("M");
	}
	
	private boolean isStable(List<ScaleWeight> scaleWeights) {
		for(ScaleWeight weight : scaleWeights) {
			if(!weight.isStable()) return false;
		}
		return true;
	}

	public static synchronized int getRetryCount() {
		return retryCount;
	}

	public static synchronized void setRetryCount(int newCount) {
		retryCount = newCount;
	}
	
	public static synchronized void incrementRetryCount() {
		retryCount++;
	}
}
