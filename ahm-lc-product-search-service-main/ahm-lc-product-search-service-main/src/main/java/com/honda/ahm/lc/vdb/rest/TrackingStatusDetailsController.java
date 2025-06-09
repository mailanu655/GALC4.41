package com.honda.ahm.lc.vdb.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.entity.TrackingStatusDetails;
import com.honda.ahm.lc.vdb.service.DataService;

public class TrackingStatusDetailsController<S extends DataService> {
	
	private Logger logger = LogManager.getLogger(TrackingStatusDetailsController.class);
	
	@Autowired
	S dataService;

	@GetMapping(path = "lines")
	public @ResponseBody List<String> findAllLine() {
		logger.info("Get all lines");
		List<String> divNameList = dataService.getConfig().getDivList();	
		Integer checkDivNameList = !CollectionUtils.isEmpty(divNameList) ? 1 : 0;
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		List<String> lineNameList = dataService.getTrackingStatusDetailsDao().findAllLine(divNameList, checkDivNameList, plantNameList, checkPlantNameList);
		List<String> trimmedList = 
				lineNameList.stream().map(String::trim).collect(Collectors.toList());
		return trimmedList;
	}

	@GetMapping(path = "processPoints")
	public @ResponseBody List<String> findAllProcessPoint(@RequestParam String lineName) {
		logger.info("Get all process points");
		lineName = StringUtils.trimToNull(lineName);
		List<String> divNameList = dataService.getConfig().getDivList();
		Integer checkDivNameList = !CollectionUtils.isEmpty(divNameList) ? 1 : 0;
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

		List<String> processPointNameList = dataService.getTrackingStatusDetailsDao().findAllProcessPoint(lineName, divNameList, checkDivNameList, plantNameList, checkPlantNameList);
		List<String> trimmedList = 
				processPointNameList.stream().map(String::trim).collect(Collectors.toList());
		return trimmedList;
	}

	@GetMapping(path = "sequencedTrackingStatus")
	public @ResponseBody List<String> findAllSequencedProcessPoint() {
		logger.info("Get all sequenced process points");
		List<String> divNameList = (dataService.getConfig().getDivList() != null) ? dataService.getConfig().getDivList() : new ArrayList<String>();
		List<String> plantNameList = (dataService.getConfig().getPlantList() != null) ? dataService.getConfig().getPlantList() : null;
		List<TrackingStatusDetails> trackingStatusDetailsList = dataService.getTrackingStatusDetailsDao().findAll(Sort.by("divSeqNo","lineSeqNo","ppSeqNo"));
		if(plantNameList != null && !plantNameList.isEmpty()) {
			trackingStatusDetailsList = trackingStatusDetailsList
					.stream()
					.filter(c -> plantNameList.contains(StringUtils.trimToEmpty(c.getPlantName())))
					.collect(Collectors.toList());		
		}
		if(divNameList != null && !divNameList.isEmpty()) {
			trackingStatusDetailsList = trackingStatusDetailsList
					.stream()
					.filter(c -> divNameList.contains(StringUtils.trimToEmpty(c.getDivisionName())))
					.collect(Collectors.toList());
		}
		List<String> lineNameList = 
				trackingStatusDetailsList.stream().map(TrackingStatusDetails::getLineName).distinct().collect(Collectors.toList());
		return lineNameList;
	}

}
