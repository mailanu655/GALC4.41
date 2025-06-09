package com.honda.ahm.lc.handlers;

import com.honda.ahm.lc.enums.StatusEnum;
import com.honda.ahm.lc.messages.StatusMessage;
import com.honda.ahm.lc.messages.StatusVehicle;
import com.honda.ahm.lc.model.ParkChange;
import com.honda.ahm.lc.service.ParkChangeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.honda.ahm.lc.common.logging.Logger;
import com.honda.ahm.lc.common.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("AhParkingChangeMessageHandler")
public class AhParkingChangeMessageHandler implements IStatusMessageHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	ParkChangeService parkChangeService;

	@Override
	public List<String> handle(StatusMessage statusMessage, StatusEnum status) {
		List<String> errorMessages = new ArrayList<String>();
		StatusVehicle statusVehicle = (StatusVehicle) statusMessage.getVehicle();
		  logger.info("Received "+status.getType()+" Status Message for VIN {}", statusVehicle.getVin());
		String galcUrl = parkChangeService.getGalcUrl(statusVehicle.getVin(),
				statusMessage.getTransaction().getLine_id());
		if(StringUtils.isBlank(galcUrl)) {
			logger.info("Unable to find the VIN record - "+statusVehicle.getVin());
			errorMessages.add("Unable to find the VIN record - "+statusVehicle.getVin());
			return errorMessages;
		}
		Date date = new Date();
		try {
			date = getParsedTimestamp(statusVehicle.getTimestamp());
		} catch (ParseException e) {
			logger.info("Unable to parse - "+statusVehicle.getTimestamp());
		}
		String parkDate = new SimpleDateFormat("yyMMdd").format(date);
		String parkTime = new SimpleDateFormat("hhmmss").format(date);

		ParkChange parkChange = new ParkChange();
		parkChange.setVin(statusVehicle.getVin());
		parkChange.setParkControlNumber(statusVehicle.getControl_number());
		parkChange.setParkingLocation(statusVehicle.getParking_bay());
		parkChange.setDate(parkDate);
		parkChange.setTime(parkTime);
		parkChange.setSendLocation(statusMessage.getTransaction().getPlant_id());
		parkChange.setTransactiontype("PC");

		logger.info("Updated parkChange for VIN "+statusVehicle.getVin()+" to status "+StatusEnum.AH_PCHG);

		parkChangeService.saveParkChange(galcUrl, parkChange);
		
		return errorMessages;
	}

	private Date getParsedTimestamp(String timestamp) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd't'HH:mm:ss");
		
		return  inputFormat.parse(timestamp);
		
	}

}
