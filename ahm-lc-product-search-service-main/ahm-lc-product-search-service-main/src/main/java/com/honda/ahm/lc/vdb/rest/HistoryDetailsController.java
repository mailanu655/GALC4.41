package com.honda.ahm.lc.vdb.rest;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.dao.HistoryDetailsDao;
import com.honda.ahm.lc.vdb.dto.ChartDto;
import com.honda.ahm.lc.vdb.dto.DetailResponseDto;
import com.honda.ahm.lc.vdb.dto.DrilldownCountDto;
import com.honda.ahm.lc.vdb.dto.ProductCountDto;
import com.honda.ahm.lc.vdb.dto.ProductIdDto;
import com.honda.ahm.lc.vdb.entity.HistoryDetails;
import com.honda.ahm.lc.vdb.entity.ProductAgeDetails;
import com.honda.ahm.lc.vdb.service.DataService;
import com.honda.ahm.lc.vdb.util.CommonUtils;
import com.honda.ahm.lc.vdb.util.Constants;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HistoryDetailsController<S extends DataService> {

	private static Logger logger = LogManager.getLogger(HistoryDetailsController.class);

	@Autowired
	S dataService;

	@PostMapping(path = "by")
	public @ResponseBody Object findProductDetailsBy(@RequestBody String body, HttpServletRequest request) {
		Page<HistoryDetails> pageProduct = null;
		try {
			HistoryDetailsDao historyDetailsDao = dataService.getHistoryDetailsDao(); 
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			// String clientDetails = CommonUtils.getValueFromJson(json, "clientDetails");
			String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
			String clientRemoteAddr = request.getRemoteAddr();

			List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
			Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
			List<Integer> seqNo = convertCommaSeparatedToListOfIntegers(
					CommonUtils.getCommaSeparatedIntegersFromJson(json, "seqNo"));
			Integer seqNoFlag = CollectionUtils.isEmpty(seqNo) ? 0 : 1;

			List<String> productionLot = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "productionLot"));
			Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
			List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
			Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;


			Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
			Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
			List<String> trackingStatus = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "lineNameStatus"));
			String startTimeAsStr = CommonUtils.getValueFromJson(json, "startTime");
			String endTimeAsStr = CommonUtils.getValueFromJson(json, "endTime");

			String startDateAsStr = CommonUtils.getValueFromJson(json, "startDate");
			java.sql.Date startProductionDate = getSqlDate(startDateAsStr);

			String endDateAsStr = CommonUtils.getValueFromJson(json, "endDate");
			java.sql.Date endProductionDate = getSqlDate(endDateAsStr);

			String startShift = CommonUtils.getValueFromJson(json, "startShift");
			String endShift = CommonUtils.getValueFromJson(json, "endShift");
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			if (searchBy != null) {
				searchBy = searchBy.trim();
			}

			String startShiftVal = null;
			String startIdVal = null;

			String endShiftVal = null;
			String endIdVal = null;

			Timestamp startTime = null;
			Timestamp endTime = null;
			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			Pageable pageable = PageRequest.of(pageIndex, pageSize);
			if (startShift != null && startShift.contains("__")) {
				startIdVal = startShift;
			} else {
				startShiftVal = startShift;
			}

			if (endShift != null && endShift.contains("__")) {
				endIdVal = endShift;
			} else {
				endShiftVal = endShift;
			}

			if ((StringUtils.isEmpty(startTimeAsStr) || startTimeAsStr.equalsIgnoreCase("null"))
					&& (StringUtils.isEmpty(endTimeAsStr) || endTimeAsStr.equalsIgnoreCase("null"))) {
				Time startVal = dataService.getShiftDetailsDao().findStartTime(startIdVal, startShiftVal,
						startProductionDate);
				Time endVal = dataService.getShiftDetailsDao().findEndTime(endIdVal, endShiftVal, endProductionDate);
				if (startProductionDate != null) {
					if (startVal != null) {
						startTimeAsStr = startProductionDate + "T" + startVal;
					} else {
						startTimeAsStr = startProductionDate + "T" + "00:00:00";
					}
				}

				if (endProductionDate != null) {
					if (endVal != null) {
						endTimeAsStr = endProductionDate + "T" + endVal;
					} else {
						endTimeAsStr = endProductionDate + "T" + "23:59:59";
					}
				}

			}

			startTime = getTimestamp(startTimeAsStr);
			endTime = getTimestamp(endTimeAsStr);

			if (startTime != null && endTime == null) {
				LocalDate today = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formattedDate = today.format(formatter);
				endTimeAsStr = formattedDate + "T" + "23:59:59";
				endTime = getTimestamp(endTimeAsStr);
			}

			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())) {
				isShippedStr = Constants.BOTH;
			}

			Integer isShipped = CommonUtils.getShippedIntVal(isShippedStr);

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date date = new Date();

			JSONObject log = new JSONObject();
			log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
					.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

			logger.info(log.toString());

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
			
			List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
			Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
			List<String> specCodeList = new ArrayList<>();
			if (specCode != null && !specCode.isEmpty()) {
			    specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))  
			    		.map(code -> historyDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
			            .filter(Objects::nonNull)
			            .flatMap(List::stream)  
			            .map(ProductIdDto::getProductSpecCode)
			            .distinct()
			            .collect(Collectors.toList());
			} else {
			    specCodeList = null;
			}


			pageProduct = dataService.getHistoryDetailsDao().findProductDetailsBy(productId, productionLot, kdLotNumber,
					specCodeList,destination, trackingStatus, isShipped, startAfOn, endAfOn, startTime, endTime, plantNameList,
					checkPlantNameList, productIdFlag, productionFlag, kdFlag, specCodeFlag,destinationflag, searchBy, pageable);
			pageProduct.getContent().parallelStream().map(product -> {
				List<ProductAgeDetails> productAgeList = dataService.getProductAgeDetailsDao()
						.findAllBy(product.getId(), product.getLastProcessPointId());
				if (productAgeList != null && !productAgeList.isEmpty()) {
					product.setTrackingStatusDate(productAgeList.get(0).getActualTimestamp());
					return product;
				} else {
					product.setTrackingStatusDate(null);
					return product;
				}

			}).collect(Collectors.toList());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return new DetailResponseDto(
				CommonUtils.convertToLotDetailsDto(
						Optional.ofNullable(pageProduct).map(Page::getContent).orElse(Collections.emptyList())),
				Optional.ofNullable(pageProduct).map(Page::getContent).orElse(Collections.emptyList()),
				Optional.ofNullable(pageProduct).map(Page::getSize).orElse(0),
				Optional.ofNullable(pageProduct).map(Page::getNumber).orElse(0),
				Optional.ofNullable(pageProduct).map(Page::getTotalElements).orElse(0L));
	}

	@PostMapping(path = "productCount")
	public @ResponseBody List<ProductCountDto> findProductDetailsCountBy(@RequestBody String body) {
		List<ProductCountDto> productList = new ArrayList<>();
		List<ChartDto> data = new ArrayList<>();
		HistoryDetailsDao historyDetailsDao = dataService.getHistoryDetailsDao();
		try {
			JSONObject json = new JSONObject(body);
			List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
			Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
			List<Integer> seqNo = convertCommaSeparatedToListOfIntegers(
					CommonUtils.getCommaSeparatedIntegersFromJson(json, "seqNo"));
			Integer seqNoFlag = CollectionUtils.isEmpty(seqNo) ? 0 : 1;

			List<String> productionLot = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "productionLot"));
			Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
			List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
			Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;
			
			

			/*
			 * List<String> type =
			 * convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "type"));
			 * Integer typeFlag = CollectionUtils.isEmpty(type) ? 0 : 1; List<String> option
			 * = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "option"));
			 * Integer optionFlag = CollectionUtils.isEmpty(option) ? 0 : 1; List<String>
			 * exteriorColor = convertCommaSeparatedToList(
			 * CommonUtils.getValueFromJson(json, "exteriorColor")); Integer
			 * exteriorColorFlag = CollectionUtils.isEmpty(exteriorColor) ? 0 : 1;
			 * List<String> interiorColor = convertCommaSeparatedToList(
			 * CommonUtils.getValueFromJson(json, "interiorColor")); Integer
			 * interiorColorFlag = CollectionUtils.isEmpty(interiorColor) ? 0 : 1;
			 * List<String> destination =
			 * convertCommaSeparatedToList(CommonUtils.getValueFromJson(json,
			 * "destination")); Integer destinationFlag =
			 * CollectionUtils.isEmpty(destination) ? 0 : 1;
			 */
			Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
			Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
			List<String> trackingStatus = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "lineNameStatus"));

			String startTimeAsStr = CommonUtils.getValueFromJson(json, "startTime");
			String endTimeAsStr = CommonUtils.getValueFromJson(json, "endTime");

			String startDateAsStr = CommonUtils.getValueFromJson(json, "startDate");
			java.sql.Date startProductionDate = getSqlDate(startDateAsStr);

			String endDateAsStr = CommonUtils.getValueFromJson(json, "endDate");
			java.sql.Date endProductionDate = getSqlDate(endDateAsStr);

			String startShift = CommonUtils.getValueFromJson(json, "startShift");
			String endShift = CommonUtils.getValueFromJson(json, "endShift");

			String startShiftVal = null;
			String startIdVal = null;

			String endShiftVal = null;
			String endIdVal = null;

			Timestamp startTime = null;
			Timestamp endTime = null;

			if (startShift != null && startShift.contains("__")) {
				startIdVal = startShift;
			} else {
				startShiftVal = startShift;
			}

			if (endShift != null && endShift.contains("__")) {
				endIdVal = endShift;
			} else {
				endShiftVal = endShift;
			}
			if ((StringUtils.isEmpty(startTimeAsStr) || startTimeAsStr.equalsIgnoreCase("null"))
					&& (StringUtils.isEmpty(endTimeAsStr) || endTimeAsStr.equalsIgnoreCase("null"))) {
				Time startVal = dataService.getShiftDetailsDao().findStartTime(startIdVal, startShiftVal,
						startProductionDate);
				Time endVal = dataService.getShiftDetailsDao().findEndTime(endIdVal, endShiftVal, endProductionDate);
				if (startProductionDate != null) {
					if (startVal != null) {
						startTimeAsStr = startProductionDate + "T" + startVal;
					} else {
						startTimeAsStr = startProductionDate + "T" + "00:00:00";
					}
				}

				if (endProductionDate != null) {
					if (endVal != null) {
						endTimeAsStr = endProductionDate + "T" + endVal;
					} else {
						endTimeAsStr = endProductionDate + "T" + "23:59:59";
					}
				}

			}

			startTime = getTimestamp(startTimeAsStr);
			endTime = getTimestamp(endTimeAsStr);

			if (startTime != null && endTime == null) {
				LocalDate today = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formattedDate = today.format(formatter);
				endTimeAsStr = formattedDate + "T" + "23:59:59";
				endTime = getTimestamp(endTimeAsStr);
			}

			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())) {
				isShippedStr = Constants.BOTH;
			}

			Integer isShipped = CommonUtils.getShippedIntVal(isShippedStr);

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
			
			List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
			Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
			List<String> specCodeList = new ArrayList<>();
			if (specCode != null && !specCode.isEmpty()) {
			    specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))  
			    		.map(code -> historyDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
			            .filter(Objects::nonNull)
			            .flatMap(List::stream)  
			            .map(ProductIdDto::getProductSpecCode)
			            .distinct()
			            .collect(Collectors.toList());
			} else {
			    specCodeList = null;
			}
			
			data = dataService.getHistoryDetailsDao().findProductDetailsCount(productId, productionLot, kdLotNumber,
					specCodeList,destination, trackingStatus, isShipped, startAfOn, endAfOn, startTime, endTime, plantNameList,
					checkPlantNameList, productIdFlag, productionFlag, kdFlag, specCodeFlag,destinationflag);
			productList = mapList(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return productList;
	}

	@PostMapping(path = "drilldownCount")
	public @ResponseBody List<DrilldownCountDto> findProductDrilldownCountBy(@RequestBody String body) {
		List<DrilldownCountDto> countList = new ArrayList<>();
		List<ChartDto> data = new ArrayList<>();
		HistoryDetailsDao historyDetailsDao = dataService.getHistoryDetailsDao();
		try {
			JSONObject json = new JSONObject(body);
			List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
			Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
			List<Integer> seqNo = convertCommaSeparatedToListOfIntegers(
					CommonUtils.getCommaSeparatedIntegersFromJson(json, "seqNo"));
			Integer seqNoFlag = CollectionUtils.isEmpty(seqNo) ? 0 : 1;

			List<String> productionLot = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "productionLot"));
			Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
			List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
			Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;
			Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
			Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
			List<String> trackingStatus = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "lineNameStatus"));

			String startTimeAsStr = CommonUtils.getValueFromJson(json, "startTime");
			String endTimeAsStr = CommonUtils.getValueFromJson(json, "endTime");

			String startDateAsStr = CommonUtils.getValueFromJson(json, "startDate");
			java.sql.Date startProductionDate = getSqlDate(startDateAsStr);

			String endDateAsStr = CommonUtils.getValueFromJson(json, "endDate");
			java.sql.Date endProductionDate = getSqlDate(endDateAsStr);

			String startShift = CommonUtils.getValueFromJson(json, "startShift");
			String endShift = CommonUtils.getValueFromJson(json, "endShift");

			String startShiftVal = null;
			String startIdVal = null;

			String endShiftVal = null;
			String endIdVal = null;

			Timestamp startTime = null;
			Timestamp endTime = null;

			if (startShift != null && startShift.contains("__")) {
				startIdVal = startShift;
			} else {
				startShiftVal = startShift;
			}

			if (endShift != null && endShift.contains("__")) {
				endIdVal = endShift;
			} else {
				endShiftVal = endShift;
			}

			if ((StringUtils.isEmpty(startTimeAsStr) || startTimeAsStr.equalsIgnoreCase("null"))
					&& (StringUtils.isEmpty(endTimeAsStr) || endTimeAsStr.equalsIgnoreCase("null"))) {
				Time startVal = dataService.getShiftDetailsDao().findStartTime(startIdVal, startShiftVal,
						startProductionDate);
				Time endVal = dataService.getShiftDetailsDao().findEndTime(endIdVal, endShiftVal, endProductionDate);
				if (startProductionDate != null) {
					if (startVal != null) {
						startTimeAsStr = startProductionDate + "T" + startVal;
					} else {
						startTimeAsStr = startProductionDate + "T" + "00:00:00";
					}
				}

				if (endProductionDate != null) {
					if (endVal != null) {
						endTimeAsStr = endProductionDate + "T" + endVal;
					} else {
						endTimeAsStr = endProductionDate + "T" + "23:59:59";
					}
				}

			}

			startTime = getTimestamp(startTimeAsStr);
			endTime = getTimestamp(endTimeAsStr);

			if (startTime != null && endTime == null) {
				LocalDate today = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String formattedDate = today.format(formatter);
				endTimeAsStr = formattedDate + "T" + "23:59:59";
				endTime = getTimestamp(endTimeAsStr);
			}
			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())) {
				isShippedStr = Constants.BOTH;
			}

			Integer isShipped = CommonUtils.getShippedIntVal(isShippedStr);

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
			
			List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
			Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
			List<String> specCodeList = new ArrayList<>();
			if (specCode != null && !specCode.isEmpty()) {
			    specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))  
			    		.map(code -> historyDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
			            .filter(Objects::nonNull)
			            .flatMap(List::stream)  
			            .map(ProductIdDto::getProductSpecCode)
			            .distinct()
			            .collect(Collectors.toList());
			} else {
			    specCodeList = null;
			}

			data = dataService.getHistoryDetailsDao().findProductDrilldownCount(productId, productionLot, kdLotNumber,
					specCodeList,destination, trackingStatus, isShipped, startAfOn, endAfOn, startTime, endTime, plantNameList,
					checkPlantNameList, productIdFlag, productionFlag, kdFlag, specCodeFlag,destinationflag);

			Set<String> divisionNameSet = new HashSet<>();
			Map<String, List<Object[]>> divisionMap = new HashMap<>();
			data.forEach(obj -> {
				String division = obj.getDivisionName();
				divisionNameSet.add(division);
				if (divisionMap.containsKey(division)) {
					List<Object[]> valList = divisionMap.get(division);
					List<Object> valArr = new ArrayList<>();
					valArr.add(obj.getLineName());
					valArr.add(obj.getCount());
					valList.add(valArr.toArray());
					divisionMap.put(division, valList);
				} else {
					List<Object[]> valList = new ArrayList<>();
					List<Object> valArr = new ArrayList<>();
					valArr.add(obj.getLineName());
					valArr.add(obj.getCount());
					valList.add(valArr.toArray());
					divisionMap.put(division, valList);
				}
			});

			for (String division : divisionNameSet) {
				DrilldownCountDto dto = new DrilldownCountDto(division, division, divisionMap.get(division));
				countList.add(dto);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return countList;
	}

	@PostMapping(path = "downloadFile")
	public ResponseEntity<byte[]> exportDataToExcel(@RequestBody String body, HttpServletRequest request)
			throws IOException, Exception {
		Page<HistoryDetails> pageProduct = null;
		JSONObject json = new JSONObject(body);
		HistoryDetailsDao historyDetailsDao = dataService.getHistoryDetailsDao();
		String searchType = CommonUtils.getValueFromJson(json, "searchType");
		String fileType = CommonUtils.getValueFromJson(json, "fileType");
		// String clientDetails = CommonUtils.getValueFromJson(json, "clientDetails");
		String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
		String clientRemoteAddr = request.getRemoteAddr();

		List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
		Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
		List<Integer> seqNo = convertCommaSeparatedToListOfIntegers(
				CommonUtils.getCommaSeparatedIntegersFromJson(json, "seqNo"));
		Integer seqNoFlag = CollectionUtils.isEmpty(seqNo) ? 0 : 1;

		List<String> productionLot = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productionLot"));
		Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
		List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
		Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;
		Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
		Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
		List<String> trackingStatus = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "lineNameStatus"));
		String startTimeAsStr = CommonUtils.getValueFromJson(json, "startTime");
		String endTimeAsStr = CommonUtils.getValueFromJson(json, "endTime");

		String startDateAsStr = CommonUtils.getValueFromJson(json, "startDate");
		java.sql.Date startProductionDate = getSqlDate(startDateAsStr);

		String endDateAsStr = CommonUtils.getValueFromJson(json, "endDate");
		java.sql.Date endProductionDate = getSqlDate(endDateAsStr);

		String startShift = CommonUtils.getValueFromJson(json, "startShift");
		String endShift = CommonUtils.getValueFromJson(json, "endShift");
		String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
		if (searchBy != null) {
			searchBy = searchBy.trim();
		}

		String startShiftVal = null;
		String startIdVal = null;

		String endShiftVal = null;
		String endIdVal = null;

		Timestamp startTime = null;
		Timestamp endTime = null;
		Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
		Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		if (startShift != null && startShift.contains("__")) {
			startIdVal = startShift;
		} else {
			startShiftVal = startShift;
		}

		if (endShift != null && endShift.contains("__")) {
			endIdVal = endShift;
		} else {
			endShiftVal = endShift;
		}

		if ((StringUtils.isEmpty(startTimeAsStr) || startTimeAsStr.equalsIgnoreCase("null"))
				&& (StringUtils.isEmpty(endTimeAsStr) || endTimeAsStr.equalsIgnoreCase("null"))) {
			Time startVal = dataService.getShiftDetailsDao().findStartTime(startIdVal, startShiftVal,
					startProductionDate);
			Time endVal = dataService.getShiftDetailsDao().findEndTime(endIdVal, endShiftVal, endProductionDate);
			if (startProductionDate != null) {
				if (startVal != null) {
					startTimeAsStr = startProductionDate + "T" + startVal;
				} else {
					startTimeAsStr = startProductionDate + "T" + "00:00:00";
				}
			}

			if (endProductionDate != null) {
				if (endVal != null) {
					endTimeAsStr = endProductionDate + "T" + endVal;
				} else {
					endTimeAsStr = endProductionDate + "T" + "23:59:59";
				}
			}

		}

		startTime = getTimestamp(startTimeAsStr);
		endTime = getTimestamp(endTimeAsStr);

		if (startTime != null && endTime == null) {
			LocalDate today = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDate = today.format(formatter);
			endTimeAsStr = formattedDate + "T" + "23:59:59";
			endTime = getTimestamp(endTimeAsStr);
		}

		String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
		if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())) {
			isShippedStr = Constants.BOTH;
		}

		Integer isShipped = CommonUtils.getShippedIntVal(isShippedStr);

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
		Date date = new Date();

		JSONObject log = new JSONObject();
		log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
				.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

		logger.info(log.toString());

		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		
		List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
		Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
		List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
		Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
		List<String> specCodeList = new ArrayList<>();
		if (specCode != null && !specCode.isEmpty()) {
		    specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))  
		    		.map(code -> historyDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
		            .filter(Objects::nonNull)
		            .flatMap(List::stream)  
		            .map(ProductIdDto::getProductSpecCode)
		            .distinct()
		            .collect(Collectors.toList());
		} else {
		    specCodeList = null;
		}

		pageProduct = dataService.getHistoryDetailsDao().findProductDetailsBy(productId, productionLot, kdLotNumber,
				specCodeList,destination, trackingStatus, isShipped, startAfOn, endAfOn, startTime, endTime, plantNameList,
				checkPlantNameList, productIdFlag, productionFlag, kdFlag, specCodeFlag,destinationflag, searchBy, pageable);
		pageProduct.getContent().parallelStream().map(product -> {
			List<ProductAgeDetails> productAgeList = dataService.getProductAgeDetailsDao().findAllBy(product.getId(),
					product.getLastProcessPointId());
			if (productAgeList != null && !productAgeList.isEmpty()) {
				product.setTrackingStatusDate(productAgeList.get(0).getActualTimestamp());
				return product;
			} else {
				product.setTrackingStatusDate(null); // Or set a default value if applicable
				return product;
			}

		}).collect(Collectors.toList());

		if (fileType.equalsIgnoreCase("excel")) {
			byte[] excelData = CommonUtils.downloadFile(pageProduct.getContent(), fileType);

			HttpHeaders headersResponse = new HttpHeaders();
			headersResponse.setContentType(
					MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
			headersResponse.add("Content-Disposition", "attachment; filename=productData.xlsx");

			return ResponseEntity.ok().headers(headersResponse).body(excelData);

		} else {

			byte[] pdfData = CommonUtils.downloadFile(pageProduct.getContent(), fileType);

			HttpHeaders headersResponse = new HttpHeaders();
			headersResponse.setContentType(MediaType.APPLICATION_PDF);
			headersResponse.add("Content-Disposition", "attachment; filename=productData.pdf");

			return ResponseEntity.ok().headers(headersResponse).body(pdfData);
		}

	}

	private Timestamp getTimestamp(String val) {
		Timestamp timestamp = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
			Date parsedDate = dateFormat.parse(val);
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (Exception e) {
			this.logger.info(e.getMessage());
		}
		return timestamp;
	}

	private java.sql.Date getSqlDate(String val) {
		java.sql.Date date = null;
		try {
			date = java.sql.Date.valueOf(val);
		} catch (Exception e) {
			this.logger.info(e.getMessage());
		}
		return date;
	}

	List<ProductCountDto> mapList(List<ChartDto> source) {
		List<ProductCountDto> list = new ArrayList<>();
		Integer index = 1;
		for (ChartDto obj : source) {
			String name = obj.getDivisionName();
			Integer y = (int) obj.getCount();
			ProductCountDto dto = new ProductCountDto(name, y, name, index);
			list.add(dto);
			index++;
		}
		return list;
	}

	public static List<String> convertCommaSeparatedToList(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		}
		String[] items = input.split(",\\s*");
		return Arrays.asList(items);
	}

	public static List<Integer> convertCommaSeparatedToListOfIntegers(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		}
		String[] items = input.split(",\\s*");
		List<Integer> result = new ArrayList<>();

		try {
			for (String item : items) {
				result.add(Integer.parseInt(item));
			}
		} catch (NumberFormatException e) {
			logger.info(e.getMessage());
			return null;
		}

		return result;
	}

	private PdfPCell createDataCell(String content, Font font) {
		PdfPCell cell = new PdfPCell(new Phrase(content, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setPadding(5f);
		return cell;
	}

	public static class FooterHandler extends PdfPageEventHelper {
		Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfPTable footerTable = new PdfPTable(2);
			try {
				footerTable.setWidthPercentage(100);
				footerTable.setTotalWidth(527);
				footerTable.setLockedWidth(true);
				footerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

				String createdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				PdfPCell dateCell = new PdfPCell(new Phrase("Created on: " + createdDate, footerFont));
				dateCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dateCell.setBorder(Rectangle.NO_BORDER);

				PdfPCell pageNumberCell = new PdfPCell(new Phrase("Page " + writer.getPageNumber(), footerFont));
				pageNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				pageNumberCell.setBorder(Rectangle.NO_BORDER);

				footerTable.addCell(dateCell);
				footerTable.addCell(pageNumberCell);

				PdfContentByte cb = writer.getDirectContent();
				footerTable.writeSelectedRows(0, -1, 34, 50, cb);
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
	}

	

}