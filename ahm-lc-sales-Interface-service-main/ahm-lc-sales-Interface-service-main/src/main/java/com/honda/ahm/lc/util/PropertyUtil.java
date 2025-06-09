package com.honda.ahm.lc.util;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PropertyUtil {
	
	@Autowired
	private Environment env;
	
	
	public String getLCReceivingQueueName() {
		return env.getProperty("lc.receiving.queue.name");
	}
	
	public String getSalesReceivingQueueName() {
		return env.getProperty("sales.receiving.queue.name");
	}
	
	public String getSalesShippingQueueName() {
		return env.getProperty("sales.shipping.queue.name");
	}
	
	public String getProductType() {
		return env.getProperty("lc.product.type");
	}
	
	public String getGALCUrl() {
		return env.getProperty("lc.url");
	}
	
	public String getPlantName() {
		return env.getProperty("lc.plant.name");
	}
	
	public String getTrackingUrl() {
		return getGALCUrl()+env.getProperty("lc.tracking.url");
	}
	
	public String getProcessPoint(String messageType) {
		return env.getProperty(messageType+".process.id");
	}

	public String getCCCPartName() {
		return env.getProperty("lc.ccc.part.name");
	}

	public String getAFOffProcessPoint() {
		return env.getProperty("lc.AFOFF.process.id");
	}

	public String getKeyNoPartName() {
		return env.getProperty("lc.key.part.name");
	}

	public String getAdcProcessCode() {
		return env.getProperty("lc.adc.process.code");
	}

	public String getPartInstalled() {
		return env.getProperty("lc.part.installed");
	}

	public String getSendLocation() {
		return env.getProperty("lc.send.location");
	}

	public String getTranType() {
		return env.getProperty("lc.tran.type");
	}
	
	public String getPropertyByDefectName(){
		return env.getProperty("AH-RTN.naq.defect.name");
	}

	public String getPropertyByRepairName(){
		return env.getProperty("AH-RTN.naq.repair.area");
	}

	public List<String> getBackoutPartList() {
		String backoutParts = env.getProperty("AH-RTN.backout.part");
		return Arrays.asList(backoutParts.split("\\s*,\\s*"));
	}

	public Boolean shippingJobEnable() {
		String isEnable = env.getProperty("lc.ship.msg.job.enable", "FALSE");
		return Boolean.valueOf(isEnable);
	}
	
	public Boolean statusJobEnable() {
		String isEnable = env.getProperty("lc.status.msg.job.enable", "FALSE");
		return Boolean.valueOf(isEnable);
	}

	public Boolean updateNaqEnable() {
		String isEnable = env.getProperty("AH-RTN.naq.update", "FALSE");
		return Boolean.valueOf(isEnable);
	}
	
	public String[] getEmailTo() {
		String mailTo = env.getProperty("spring.mail.to");
		String[] mailTolist = mailTo.split(",");
		return mailTolist;
	}
	
	public String getEmailFrom() {
		return env.getProperty("spring.mail.from");
	}
	
	public String getEmailSubject() {
		return env.getProperty("spring.mail.subject");
	}

	public String getDestinationSite() {
		return env.getProperty("lc.destination.site");
	}

	public String getDestinationEnv() {
		return env.getProperty("lc.destination.env");
	}

	public Integer getReadTimeout() {
		String readTimeOut = env.getProperty("lc.rest.read.timeout","10000");
		return Integer.parseInt(readTimeOut);
	}
	
	public Integer getConnectTimeout() {
		String connectTimeOut = env.getProperty("lc.rest.connect.timeout","10000");
		return Integer.parseInt(connectTimeOut);
	}
}
