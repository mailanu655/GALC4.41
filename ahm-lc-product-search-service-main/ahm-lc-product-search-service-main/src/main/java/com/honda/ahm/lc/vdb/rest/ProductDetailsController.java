package com.honda.ahm.lc.vdb.rest;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.honda.ahm.lc.vdb.dao.ProductAgeDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductDetailsDao;
import com.honda.ahm.lc.vdb.dao.ProductLastStatusDetailsDao;
import com.honda.ahm.lc.vdb.dto.ChartDto;
import com.honda.ahm.lc.vdb.dto.DetailResponseDto;
import com.honda.ahm.lc.vdb.dto.DrilldownCountDto;
import com.honda.ahm.lc.vdb.dto.ProductCountDto;
import com.honda.ahm.lc.vdb.dto.ProductIdDto;
import com.honda.ahm.lc.vdb.entity.ProductAgeDetails;
import com.honda.ahm.lc.vdb.entity.ProductDetails;
import com.honda.ahm.lc.vdb.entity.ProductLastStatusDetails;
import com.honda.ahm.lc.vdb.entity.VinRangeProductDetails;
import com.honda.ahm.lc.vdb.service.DataService;
import com.honda.ahm.lc.vdb.util.CommonUtils;
import com.honda.ahm.lc.vdb.util.Constants;

public class ProductDetailsController<S extends DataService> {

	private static Logger logger = LogManager.getLogger(ProductDetailsController.class);

	@Autowired
	S dataService;

	public ProductDetailsController() {
		super();
	}

	@GetMapping(path = "productIds")
	public @ResponseBody List<ProductIdDto> findAllProductIds() {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		/*
		 * List<ProductIdDto> productCodeList =
		 * dataService.getProductDetailsDao().findAllProductIds(plantNameList,
		 * checkPlantNameList);
		 */
		return dataService.getProductDetailsDao().findAllProductIds(plantNameList, checkPlantNameList);
	}

	@GetMapping(path = "engineSerialIds")
	public @ResponseBody List<ProductIdDto> findAllengineSerialIds() {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		/*
		 * List<ProductIdDto> engineSerialList =
		 * dataService.getProductDetailsDao().findAllProductIds(plantNameList,
		 * checkPlantNameList);
		 */
		return dataService.getProductDetailsDao().findAllProductIds(plantNameList, checkPlantNameList);
	}

	@GetMapping(path = "missionSerialIds")
	public @ResponseBody List<ProductIdDto> findAllmissionSerialIds() {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		/*
		 * List<ProductIdDto> missionSerialList =
		 * dataService.getProductDetailsDao().findAllProductIds(plantNameList,
		 * checkPlantNameList);
		 */
		return dataService.getProductDetailsDao().findAllProductIds(plantNameList, checkPlantNameList);
	}

	@GetMapping(path = "getAllSpecCodes")
	public @ResponseBody List<String> getAllSpecCodes() {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
		List<ProductIdDto> productCodeList = dataService.getProductDetailsDao().findAllProductIds(plantNameList,
				checkPlantNameList);
		return productCodeList.parallelStream().map(ProductIdDto::getProductSpecCode).distinct()
				.collect(Collectors.toList());
	}

	@PostMapping(path = "by")
	public @ResponseBody DetailResponseDto findProductDetailsBy(@RequestBody String body, HttpServletRequest request) {
		Page<ProductDetails> pageProduct = null;
		try {
			ProductDetailsDao productDetailsDao = dataService.getProductDetailsDao(); 
			
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
			String clientRemoteAddr = request.getRemoteAddr();

			List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
			Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
			List<String> engineIds = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "engineSerialIds"));
			Integer engineIdFlag = CollectionUtils.isEmpty(engineIds) ? 0 : 1;
			List<String> missionId = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "missionSerialIds"));
			Integer missionIdFlag = CollectionUtils.isEmpty(missionId) ? 0 : 1;
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
			String qicsStatus = CommonUtils.getValueFromJson(json, "qicsStatus");
			List<String> trackingStatus = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "lineNameStatus"));
			Integer trackingStatusFlag = CollectionUtils.isEmpty(trackingStatus) ? 0 : 1;
			String processPoint = CommonUtils.getValueFromJson(json, "processPointStatus");
			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			if (searchBy != null) {
				searchBy = searchBy.trim();
			}

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date date = new Date();

			JSONObject log = new JSONObject();
			log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
					.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

			logger.info(log.toString());

			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())) {
				isShippedStr = Constants.BOTH;
			}
			Integer isShipped;
			if (null != isShippedStr) {
				isShipped = CommonUtils.getShippedIntVal(isShippedStr);
			} else {
				isShipped = 2;
			}

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
			List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
			Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
			List<String> specCodeList = new ArrayList<>();
			if (specCode != null && !specCode.isEmpty()) {
				specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))
						.map(code -> productDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
						.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode).distinct()
						.collect(Collectors.toList());
			} else {
				specCodeList = null;
			}

			Pageable pageable = PageRequest.of(pageIndex, pageSize);

			if (!StringUtils.isEmpty(qicsStatus)) {
				pageProduct = productDetailsDao.findProductDetailsByDefectStatus(productId, seqNo,
						productionLot, kdLotNumber, specCodeList, destination, trackingStatus, isShipped, processPoint,
						startAfOn, endAfOn, qicsStatus, plantNameList, checkPlantNameList, engineIds, missionId,
						productIdFlag, seqNoFlag, productionFlag, kdFlag, specCodeFlag, destinationflag,
						trackingStatusFlag, engineIdFlag, missionIdFlag, searchBy, pageable);
			} else {
				pageProduct = productDetailsDao.findProductDetailsBy(productId, seqNo, productionLot,
						kdLotNumber, specCodeList, destination, trackingStatus, isShipped, processPoint, startAfOn,
						endAfOn, plantNameList, checkPlantNameList, engineIds, missionId, productIdFlag, seqNoFlag,
						productionFlag, kdFlag, specCodeFlag, destinationflag, trackingStatusFlag, engineIdFlag,
						missionIdFlag, searchBy, pageable);
			}
			if (pageProduct != null) {
				ProductAgeDetailsDao productAgeDetailsDao=dataService.getProductAgeDetailsDao();
				ProductLastStatusDetailsDao productLastStatusDetailsDao=dataService.getProductLastStatusDetailsDao();
				
				
				pageProduct.getContent().parallelStream().map(product -> {
					if (null != product.getLastProcessPointId()) {
						List<ProductAgeDetails> ageDetails = productAgeDetailsDao
								.findAllBy(product.getId(), product.getLastProcessPointId());
						if (!ageDetails.isEmpty()) {
							product.setTrackingStatusDate(ageDetails.get(0).getActualTimestamp());
						}

					}
					if (null != product.getLineId()) {
						List<ProductLastStatusDetails> statusDetails = productLastStatusDetailsDao
								.getLastTrackingDate(product.getId(), product.getLineId());
						if (!statusDetails.isEmpty()) {
							product.setActualTimestamp(statusDetails.get(0).getActualTimestamp());
						}

					}

					return product;
				}).collect(Collectors.toList());

			}
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

	@PostMapping(path = "byVinRange")
	public @ResponseBody DetailResponseDto findProductDetailsByVinRange(@RequestBody String body,
			HttpServletRequest request) {
		Page<VinRangeProductDetails> pageProduct = null;
		ProductDetailsDao productDetailsDao = dataService.getProductDetailsDao(); 
		try {
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			String specCodeFlag = CollectionUtils.isEmpty(specCode) ? null:"specCode" ;
			List<String> specCodeList = new ArrayList<>();
			if (specCode != null && !specCode.isEmpty()) {
				specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))
						.map(code -> productDetailsDao.findSpecIds(code))
						.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode).distinct()
						.collect(Collectors.toList());
			} else {
				specCodeList = null;
			}
			String fromTrackingStatus = CommonUtils.getValueFromJson(json, "fromTrackingStatus");
			String toTrackingStatus = CommonUtils.getValueFromJson(json, "toTrackingStatus");
			String fromSerial = CommonUtils.getValueFromJson(json, "fromSerial");
			String toSerial = CommonUtils.getValueFromJson(json, "toSerial");
			String clientRemoteAddr = request.getRemoteAddr();
			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			if (searchBy != null) {
				searchBy = searchBy.trim();
			}

			String trackingStatusFlag = checkNullValidation(fromTrackingStatus, toTrackingStatus);
			String serailFlag = checkNullValidation(fromSerial, toSerial);
			

			Pageable pageable = PageRequest.of(pageIndex, pageSize);

			pageProduct = dataService.getVinRangeProductDetailsDao().findProductDetailsByVinRange(trackingStatusFlag,
					fromTrackingStatus, toTrackingStatus, specCodeFlag, specCodeList, serailFlag,
					fromSerial != null ? Integer.parseInt(fromSerial) : null,
					toSerial != null ? Integer.parseInt(toSerial) : null, searchBy, pageable);

			/*
			 * if (pageProduct != null) {
			 * pageProduct.getContent().parallelStream().map(product -> {
			 * product.setTrackingStatusDate(dataService.getProductAgeDetailsDao()
			 * .findAllBy(product.getId(),
			 * product.getLastProcessPointId()).get(0).getActualTimestamp());
			 * product.setActualTimestamp(dataService.getProductLastStatusDetailsDao().
			 * getLastTrackingDate(product.getId(),
			 * product.getLineId()).get(0).getActualTimestamp()); return product;
			 * }).collect(Collectors.toList());
			 * 
			 * }
			 */

			if (pageProduct != null) {
				pageProduct.getContent().parallelStream().map(product -> {
					if (null != product.getLastProcessPointId()) {
						List<ProductAgeDetails> ageDetails = dataService.getProductAgeDetailsDao()
								.findAllBy(product.getId(), product.getLastProcessPointId());
						if (!ageDetails.isEmpty()) {
							product.setTrackingStatusDate(ageDetails.get(0).getActualTimestamp());
						}

					}
					if (null != product.getLineId()) {
						List<ProductLastStatusDetails> statusDetails = dataService.getProductLastStatusDetailsDao()
								.getLastTrackingDate(product.getId(), product.getLineId());
						if (!statusDetails.isEmpty()) {
							product.setActualTimestamp(statusDetails.get(0).getActualTimestamp());
						}

					}

					return product;
				}).collect(Collectors.toList());

			}

		} catch (Exception e) {
			logger.info("error:-" + e.getMessage());
		}
		return new DetailResponseDto(
				CommonUtils.convertToLotDetailsDto(
						Optional.ofNullable(pageProduct).map(Page::getContent).orElse(Collections.emptyList())),
				Optional.ofNullable(pageProduct).map(Page::getContent).orElse(Collections.emptyList()),
				Optional.ofNullable(pageProduct).map(Page::getSize).orElse(0),
				Optional.ofNullable(pageProduct).map(Page::getNumber).orElse(0),
				Optional.ofNullable(pageProduct).map(Page::getTotalElements).orElse(0L));
	}

	private String checkNullValidation(String value1, String value2) {

		if (value1 == null || value1.isEmpty()) {
			return null;
		} else if (value2 == null || value2.isEmpty()) {
			return null;
		}
		return value1 + value2;
	}

	@PostMapping(path = "byList")
	public @ResponseBody DetailResponseDto findProductDetailsByList(@RequestBody String body,
			HttpServletRequest request) {
		Page<ProductDetails> pageProduct = null;
		try {
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			String paramVal = CommonUtils.getValueFromJson(json, "productList");
			String productType = CommonUtils.getValueFromJson(json, "productType");
			List<String> paramList = Arrays.asList(paramVal.split("\r\n|\n|,"));
			String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
			String clientRemoteAddr = request.getRemoteAddr();

			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date date = new Date();

			JSONObject log = new JSONObject();
			log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
					.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			Pageable pageable = PageRequest.of(pageIndex, pageSize);
			logger.info(log.toString());

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			switch (productType) {
			case Constants.VIN:
				pageProduct = dataService.getProductDetailsDao().findProductDetailsByProductIdList(paramList,
						plantNameList, checkPlantNameList, searchBy, pageable);
				break;
			case Constants.EIN:
				pageProduct = dataService.getProductDetailsDao().findProductDetailsByEngineNoList(paramList,
						plantNameList, checkPlantNameList, searchBy, pageable);
				break;
			case Constants.MIN:
				pageProduct = dataService.getProductDetailsDao().findProductDetailsByMissionNoList(paramList,
						plantNameList, checkPlantNameList, searchBy, pageable);
				break;
			default:
				logger.error("Invalid Product Type");

				break;
			}
			if (pageProduct != null) {
				pageProduct.getContent().parallelStream().map(product -> {
					if (null != product.getLastProcessPointId()) {
						List<ProductAgeDetails> ageDetails = dataService.getProductAgeDetailsDao()
								.findAllBy(product.getId(), product.getLastProcessPointId());
						if (!ageDetails.isEmpty()) {
							product.setTrackingStatusDate(ageDetails.get(0).getActualTimestamp());
						}

					}
					if (null != product.getLineId()) {
						List<ProductLastStatusDetails> statusDetails = dataService.getProductLastStatusDetailsDao()
								.getLastTrackingDate(product.getId(), product.getLineId());
						if (!statusDetails.isEmpty()) {
							product.setActualTimestamp(statusDetails.get(0).getActualTimestamp());
						}

					}

					return product;
				}).collect(Collectors.toList());

			}

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

	@PostMapping(path = "byInventory")
	public @ResponseBody DetailResponseDto findProductDetailsByInventory(@RequestBody String body,
			HttpServletRequest request) {
		Page<ProductDetails> pageProduct = null;
		try {
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			String paramVal = CommonUtils.getValueFromJson(json, "inventorySearchList");
			List<String> paramList = Arrays.asList(paramVal.split("\r\n|\n|,"));
			String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
			String clientRemoteAddr = request.getRemoteAddr();
			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			Pageable pageable = PageRequest.of(pageIndex, pageSize);
			SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
			Date date = new Date();
			JSONObject log = new JSONObject();
			log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
					.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

			logger.info(log.toString());

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			pageProduct = dataService.getProductDetailsDao().findProductDetailsByTrackingStatusList(paramList,
					plantNameList, checkPlantNameList, searchBy, pageable);
			if (pageProduct != null) {
				pageProduct.getContent().parallelStream().map(product -> {
					if (null != product.getLastProcessPointId()) {
						List<ProductAgeDetails> ageDetails = dataService.getProductAgeDetailsDao()
								.findAllBy(product.getId(), product.getLastProcessPointId());
						if (!ageDetails.isEmpty()) {
							product.setTrackingStatusDate(ageDetails.get(0).getActualTimestamp());
						}

					}
					if (null != product.getLineId()) {
						List<ProductLastStatusDetails> statusDetails = dataService.getProductLastStatusDetailsDao()
								.getLastTrackingDate(product.getId(), product.getLineId());
						if (!statusDetails.isEmpty()) {
							product.setActualTimestamp(statusDetails.get(0).getActualTimestamp());
						}

					}

					return product;
				}).collect(Collectors.toList());

			}

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
		try {
			JSONObject json = new JSONObject(body);
			String productId = CommonUtils.getValueFromJson(json, "productId");
			List<String> productionLot = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "productionLot"));
			Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
			List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
			Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;

			Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
			Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
			String qicsStatus = CommonUtils.getValueFromJson(json, "qicsStatus");
			String trackingStatus = CommonUtils.getValueFromJson(json, "lineNameStatus");
			String processPoint = CommonUtils.getValueFromJson(json, "processPointStatus");

			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty())) {
				isShippedStr = Constants.BOTH;
			}

			Integer isShipped = CommonUtils.getShippedIntVal(isShippedStr);

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			List<String> destination = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "destination"));
			Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
			List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
			Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
			List<String> specCodeList = new ArrayList<String>();
			if (specCode != null && !specCode.isEmpty()) {
				ProductDetailsDao productDetailsDao = dataService.getProductDetailsDao(); // Fetch DAO once

				specCodeList = specCode.parallelStream().map(code -> code.replace("*", "").toString())
						.map(code -> productDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
						.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode).distinct()
						.collect(Collectors.toList());
			} else {
				specCodeList = null;
			}

			if (!StringUtils.isEmpty(qicsStatus)) {
				data = dataService.getProductDetailsDao().findProductDetailsByDefectStatusCount(productId,
						productionLot, kdLotNumber, specCodeList, destination, trackingStatus, isShipped, processPoint,
						startAfOn, endAfOn, qicsStatus, plantNameList, checkPlantNameList, productionFlag, kdFlag,
						specCodeFlag, destinationflag);
			} else {
				data = dataService.getProductDetailsDao().findProductDetailsCount(productId, productionLot, kdLotNumber,
						specCodeList, destination, trackingStatus, isShipped, processPoint, startAfOn, endAfOn,
						plantNameList, checkPlantNameList, productionFlag, kdFlag, specCodeFlag, destinationflag);
			}
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
		try {
			JSONObject json = new JSONObject(body);
			String productId = CommonUtils.getValueFromJson(json, "productId");
			List<String> productionLot = convertCommaSeparatedToList(
					CommonUtils.getValueFromJson(json, "productionLot"));
			Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
			List<String> kdLotNumber = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "kdLotNumber"));
			Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;
			Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
			Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
			String qicsStatus = CommonUtils.getValueFromJson(json, "qicsStatus");
			String trackingStatus = CommonUtils.getValueFromJson(json, "lineNameStatus");
			String processPoint = CommonUtils.getValueFromJson(json, "processPointStatus");

			String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
			if ((productId != null && !productId.isEmpty())) {
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
				ProductDetailsDao productDetailsDao = dataService.getProductDetailsDao(); // Fetch DAO once

				specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))
						.map(code -> productDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
						.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode).distinct()
						.collect(Collectors.toList());
			} else {
				specCodeList = null;
			}

			if (!StringUtils.isEmpty(qicsStatus)) {
				data = dataService.getProductDetailsDao().findProductDrilldownByDefectStatusCount(productId,
						productionLot, kdLotNumber, specCodeList, destination, trackingStatus, isShipped, processPoint,
						startAfOn, endAfOn, qicsStatus, plantNameList, checkPlantNameList, productionFlag, kdFlag,
						specCodeFlag, destinationflag);
			} else {
				data = dataService.getProductDetailsDao().findProductDrilldownCount(productId, productionLot,
						kdLotNumber, specCodeList, destination, trackingStatus, isShipped, processPoint, startAfOn,
						endAfOn, plantNameList, checkPlantNameList, productionFlag, kdFlag, specCodeFlag,
						destinationflag);
			}

			Set<String> divisionNameSet = data.parallelStream().map(ChartDto::getDivisionName)
					.collect(Collectors.toSet());

			Map<String, List<Object[]>> divisionMap = data.parallelStream()
					.collect(Collectors.groupingByConcurrent(ChartDto::getDivisionName, Collectors
							.mapping(obj -> new Object[] { obj.getLineName(), obj.getCount() }, Collectors.toList())));

			countList = divisionNameSet.parallelStream()
					.map(division -> new DrilldownCountDto(division, division, divisionMap.get(division)))
					.collect(Collectors.toList());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return countList;
	}

	@GetMapping(path = "kdLots")
	public @ResponseBody List<String> findAllKdLot(@RequestParam String params) {
		List<String> kdLotList = new ArrayList<>();
		JSONObject json;
		try {
			json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			kdLotList = dataService.getProductDetailsDao().findAllKdLot(isShipped, plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return kdLotList;
	}

	@GetMapping(path = "productionLots")
	public @ResponseBody List<String> findAllProductionLot(@RequestParam String params) {
		List<String> productionLotList = new ArrayList<>();
		JSONObject json;
		try {
			json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			productionLotList = dataService.getProductDetailsDao().findAllProductionLot(kdLotNumber, isShipped,
					plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return productionLotList;
	}

	@GetMapping(path = "models")
	public @ResponseBody List<String> findAllModel(@RequestParam String params) {
		List<String> modelList = new ArrayList<>();
		JSONObject json;
		try {
			json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;
			modelList = dataService.getProductDetailsDao().findAllModel(kdLotNumber, productionLot, isShipped,
					plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return modelList;
	}

	@GetMapping(path = "types")
	public @ResponseBody List<String> findAllType(@RequestParam String params) {
		List<String> typeList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");
			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String model = CommonUtils.getValueFromJson(json, "model");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			typeList = dataService.getProductDetailsDao().findAllType(productionLot, kdLotNumber, model, isShipped,
					plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return typeList;
	}

	@GetMapping(path = "options")
	public @ResponseBody List<String> findAllOption(@RequestParam String params) {
		List<String> optionList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");
			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String model = CommonUtils.getValueFromJson(json, "model");
			String type = CommonUtils.getValueFromJson(json, "type");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			optionList = dataService.getProductDetailsDao().findAllOption(productionLot, kdLotNumber, model, type,
					isShipped, plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return optionList;
	}

	@GetMapping(path = "exteriorColors")
	public @ResponseBody List<String> findAllExteriorColor(@RequestParam String params) {
		List<String> exteriorColorList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");
			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String model = CommonUtils.getValueFromJson(json, "model");
			String type = CommonUtils.getValueFromJson(json, "type");
			String option = CommonUtils.getValueFromJson(json, "option");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			exteriorColorList = dataService.getProductDetailsDao().findAllExteriorColor(productionLot, kdLotNumber,
					model, type, option, isShipped, plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return exteriorColorList;
	}

	@GetMapping(path = "interiorColors")
	public @ResponseBody List<String> findAllInteriorColor(@RequestParam String params) {
		List<String> interiorColorList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");
			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String model = CommonUtils.getValueFromJson(json, "model");
			String type = CommonUtils.getValueFromJson(json, "type");
			String option = CommonUtils.getValueFromJson(json, "option");
			String exteriorColor = CommonUtils.getValueFromJson(json, "exteriorColor");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			interiorColorList = dataService.getProductDetailsDao().findAllInteriorColor(productionLot, kdLotNumber,
					model, type, option, exteriorColor, isShipped, plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return interiorColorList;
	}

	@GetMapping(path = "destinations")
	public @ResponseBody List<String> findAllDestination(@RequestParam String params) {
		List<String> destinationList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(params);

			Integer isShipped = CommonUtils.getShippedIntVal(CommonUtils.getValueFromJson(json, "isShipped"));

			String productionLot = CommonUtils.getValueFromJson(json, "productionLot");
			String kdLotNumber = CommonUtils.getValueFromJson(json, "kdLotNumber");
			String model = CommonUtils.getValueFromJson(json, "model");
			String type = CommonUtils.getValueFromJson(json, "type");
			String option = CommonUtils.getValueFromJson(json, "option");
			String exteriorColor = CommonUtils.getValueFromJson(json, "exteriorColor");
			String interiorColor = CommonUtils.getValueFromJson(json, "interiorColor");

			List<String> plantNameList = dataService.getConfig().getPlantList();
			Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

			destinationList = dataService.getProductDetailsDao().findAllDestination(productionLot, kdLotNumber, model,
					type, option, exteriorColor, interiorColor, isShipped, plantNameList, checkPlantNameList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return destinationList;
	}

	@GetMapping(path = "processPoints")
	public @ResponseBody List<String> findAllProcessPoint(@RequestParam String lineName) {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

		return dataService.getProductDetailsDao().findAllProcessPoint(lineName, plantNameList, checkPlantNameList);
	}

	@GetMapping(path = "trackingStatus")
	public @ResponseBody List<String> findAllTrackingStatus() {
		List<String> plantNameList = dataService.getConfig().getPlantList();
		Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

		return dataService.getProductDetailsDao().findAllTrackingStatus(plantNameList, checkPlantNameList);
	}

	@PostMapping(path = "downloadFile")
	public ResponseEntity<byte[]> exportDataToExcel(@RequestBody String body, HttpServletRequest request)
			throws IOException, Exception {

		try {
			ProductDetailsDao productDetailsDao = dataService.getProductDetailsDao();
			ProductAgeDetailsDao productAgeDetailsDao=dataService.getProductAgeDetailsDao();
			ProductLastStatusDetailsDao productLastStatusDetailsDao=dataService.getProductLastStatusDetailsDao();
			JSONObject json = new JSONObject(body);
			String searchType = CommonUtils.getValueFromJson(json, "searchType");
			Integer pageSize = CommonUtils.getIntValueFromJson(json, "pageSize");
			Integer pageIndex = CommonUtils.getIntValueFromJson(json, "pageIndex");
			String fileType = CommonUtils.getValueFromJson(json, "fileType");
			Pageable pageable = PageRequest.of(pageIndex, pageSize);
			String searchBy = CommonUtils.getValueFromJson(json, "searchBy");
			if (searchBy != null) {
				searchBy = searchBy.trim();
			}

			if (searchType.equals("Multi Search") || searchType.equals("Single Search")
					|| searchType.equals("Bulk Search") || searchType.equals("Inventory Search")) {
				Page<ProductDetails> pageProduct = null;
				String isHistoricalSearch = CommonUtils.getValueFromJson(json, "isHistoricalSearch");
				String clientRemoteAddr = request.getRemoteAddr();

				List<String> productId = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "productId"));
				Integer productIdFlag = CollectionUtils.isEmpty(productId) ? 0 : 1;
				List<Integer> seqNo = convertCommaSeparatedToListOfIntegers(
						CommonUtils.getCommaSeparatedIntegersFromJson(json, "seqNo"));
				Integer seqNoFlag = CollectionUtils.isEmpty(seqNo) ? 0 : 1;
				List<String> engineIds = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "engineSerialIds"));
				Integer engineIdFlag = CollectionUtils.isEmpty(engineIds) ? 0 : 1;
				List<String> missionId = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "missionSerialIds"));
				Integer missionIdFlag = CollectionUtils.isEmpty(missionId) ? 0 : 1;
				List<String> destination = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "destination"));
				Integer destinationflag = CollectionUtils.isEmpty(destination) ? 0 : 1;
				String paramVal;
				
				if(searchType.equals("Inventory Search")) {
					paramVal = CommonUtils.getValueFromJson(json, "inventorySearchList");
				} else {
					paramVal = CommonUtils.getValueFromJson(json, "productList");
				}
				
				List<String> paramList = new ArrayList<String>();
				if (paramVal != null) {
					paramList = Arrays.asList(paramVal.split("\r\n|\n|,"));
				}

				String productType = CommonUtils.getValueFromJson(json, "productType");

				List<String> productionLot = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "productionLot"));
				Integer productionFlag = CollectionUtils.isEmpty(productionLot) ? 0 : 1;
				List<String> kdLotNumber = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "kdLotNumber"));
				Integer kdFlag = CollectionUtils.isEmpty(kdLotNumber) ? 0 : 1;
                
				Integer startAfOn = CommonUtils.getIntValueFromJson(json, "startAfOn");
				Integer endAfOn = CommonUtils.getIntValueFromJson(json, "endAfOn");
				String qicsStatus = CommonUtils.getValueFromJson(json, "qicsStatus");
				List<String> trackingStatus = convertCommaSeparatedToList(
						CommonUtils.getValueFromJson(json, "lineNameStatus"));
				Integer trackingStatusFlag = CollectionUtils.isEmpty(trackingStatus) ? 0 : 1;
				String processPoint = CommonUtils.getValueFromJson(json, "processPointStatus");

				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				Date date = new Date();

				JSONObject log = new JSONObject();
				log.put("searchDate", formatter.format(date)).put("searchType", searchType).put("searchParams", body)
						.put("isHistoricalSearch", isHistoricalSearch).put("clientIp", clientRemoteAddr);

				logger.info(log.toString());

				String isShippedStr = CommonUtils.getValueFromJson(json, "isShipped");
				if ((productId != null && !productId.isEmpty()) || (seqNo != null && !seqNo.isEmpty())
						|| (engineIds != null && !engineIds.isEmpty()) || (missionId != null && !missionId.isEmpty())) {
					isShippedStr = Constants.BOTH;
				}
				Integer isShipped = null;
				if (isShippedStr != null) {
					isShipped = CommonUtils.getShippedIntVal(isShippedStr);
				}

				List<String> plantNameList = dataService.getConfig().getPlantList();
				Integer checkPlantNameList = !CollectionUtils.isEmpty(plantNameList) ? 1 : 0;

				List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
				Integer specCodeFlag = CollectionUtils.isEmpty(specCode) ? 0 : 1;
				List<String> specCodeList = new ArrayList<>();
				if (specCode != null && !specCode.isEmpty()) {

					specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))
							.map(code -> productDetailsDao.findSpecIds(plantNameList, checkPlantNameList, code))
							.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode)
							.distinct().collect(Collectors.toList());
				} else {
					specCodeList = null;
				}

				if (searchType.equals("Multi Search") || searchType.equals("Single Search")) {
					if (!StringUtils.isEmpty(qicsStatus)) {
						pageProduct = productDetailsDao.findProductDetailsByDefectStatus(productId,
								seqNo, productionLot, kdLotNumber, specCodeList,destination, trackingStatus, isShipped,
								processPoint, startAfOn, endAfOn, qicsStatus, plantNameList, checkPlantNameList,
								engineIds, missionId, productIdFlag, seqNoFlag, productionFlag, kdFlag,
								specCodeFlag,destinationflag, trackingStatusFlag, engineIdFlag, missionIdFlag, searchBy, pageable);
					} else {
						pageProduct = productDetailsDao.findProductDetailsBy(productId, seqNo,
								productionLot, kdLotNumber, specCodeList,destination, trackingStatus, isShipped,
								processPoint, startAfOn, endAfOn, plantNameList, checkPlantNameList, engineIds,
								missionId, productIdFlag, seqNoFlag, productionFlag, kdFlag,
								specCodeFlag,destinationflag, trackingStatusFlag, engineIdFlag, missionIdFlag, searchBy, pageable);

					}
					

				} else if (searchType.equals("Bulk Search")) {
					
					switch (productType) {
					case Constants.VIN:
						pageProduct = dataService.getProductDetailsDao().findProductDetailsByProductIdList(paramList,
								plantNameList, checkPlantNameList, searchBy, pageable);
						break;
					case Constants.EIN:
						pageProduct = dataService.getProductDetailsDao().findProductDetailsByEngineNoList(paramList,
								plantNameList, checkPlantNameList, searchBy, pageable);
						break;
					case Constants.MIN:
						pageProduct = dataService.getProductDetailsDao().findProductDetailsByMissionNoList(paramList,
								plantNameList, checkPlantNameList, searchBy, pageable);
						break;		
					default:
						logger.error("Invalid Product Type");

						break;
					}
					
				} else if (searchType.equals("Inventory Search")) {
					pageProduct = dataService.getProductDetailsDao().findProductDetailsByTrackingStatusList(paramList,
							plantNameList, checkPlantNameList, searchBy, pageable);
				}
				if (pageProduct != null) {
				    List<ProductDetails> products = pageProduct.getContent();
				    
				    Map<String, String> productToProcessPointMap = products.parallelStream()
				            .filter(p -> p.getLastProcessPointId() != null)
				            .collect(Collectors.toConcurrentMap(ProductDetails::getId, ProductDetails::getLastProcessPointId, (e, r) -> e));

				    Map<String, String> productToLineIdMap = products.parallelStream()
				            .filter(p -> p.getLineId() != null)
				            .collect(Collectors.toConcurrentMap(ProductDetails::getId, ProductDetails::getLineId, (e, r) -> e));
				    
				    Set<String> processPointIds = new HashSet<>(productToProcessPointMap.values());
				    Set<String> lineIds = new HashSet<>(productToLineIdMap.values());
				    
				    Map<String, ProductAgeDetails> productAgeDetailsMap = productAgeDetailsDao.findAllBy(
				            productToProcessPointMap.keySet(),
				            new ArrayList<>(processPointIds)
				    ).parallelStream().collect(Collectors.toConcurrentMap(ProductAgeDetails::getProductId, d -> d, (e, r) -> e));
				    
				    Map<String, ProductLastStatusDetails> productLastStatusDetailsMap = productLastStatusDetailsDao.findAllBy(
				            productToLineIdMap.keySet(),
				            new ArrayList<>(lineIds)
				    ).parallelStream().collect(Collectors.toConcurrentMap(ProductLastStatusDetails::getProductId, d -> d, (e, r) -> e));
				    
				    products.parallelStream().forEach(product -> {
				        ProductAgeDetails ageDetails = productAgeDetailsMap.get(product.getId());
				        if (ageDetails != null) product.setTrackingStatusDate(ageDetails.getActualTimestamp());

				        ProductLastStatusDetails statusDetails = productLastStatusDetailsMap.get(product.getId());
				        if (statusDetails != null) product.setActualTimestamp(statusDetails.getActualTimestamp());
				    });
				}



				if (pageProduct != null && pageProduct.getContent() != null) {

					if (fileType.equalsIgnoreCase("excel")) {
						byte[] excelData = CommonUtils.downloadFile(fileType, pageProduct.getContent());

						HttpHeaders headersResponse = new HttpHeaders();
						headersResponse.setContentType(MediaType
								.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
						headersResponse.add("Content-Disposition", "attachment; filename=productData.xlsx");

						return ResponseEntity.ok().headers(headersResponse).body(excelData);

					} else {

						byte[] pdfData = CommonUtils.downloadFile(fileType, pageProduct.getContent());

						HttpHeaders headersResponse = new HttpHeaders();
						headersResponse.setContentType(MediaType.APPLICATION_PDF);
						headersResponse.add("Content-Disposition", "attachment; filename=productData.pdf");

						return ResponseEntity.ok().headers(headersResponse).body(pdfData);
					}
				}
			} else if (searchType.equals("Vin Range Search")) {
				Page<VinRangeProductDetails> pageProduct = null;
				String fromTrackingStatus = CommonUtils.getValueFromJson(json, "fromTrackingStatus");
				String toTrackingStatus = CommonUtils.getValueFromJson(json, "toTrackingStatus");
				String fromSerial = CommonUtils.getValueFromJson(json, "fromSerial");
				String toSerial = CommonUtils.getValueFromJson(json, "toSerial");

				String trackingStatusFlagcheck = checkNullValidation(fromTrackingStatus, toTrackingStatus);
				String serailFlag = checkNullValidation(fromSerial, toSerial);
				List<String> specCode = convertCommaSeparatedToList(CommonUtils.getValueFromJson(json, "specCode"));
				String specCodeFlag = CollectionUtils.isEmpty(specCode) ? null:"specCode" ;
				List<String> specCodeList = new ArrayList<>();
				if (specCode != null && !specCode.isEmpty()) {
					specCodeList = specCode.parallelStream().map(code -> code.replace("*", ""))
							.map(code -> productDetailsDao.findSpecIds(code))
							.filter(Objects::nonNull).flatMap(List::stream).map(ProductIdDto::getProductSpecCode).distinct()
							.collect(Collectors.toList());
				} else {
					specCodeList = null;
				}

				pageProduct = dataService.getVinRangeProductDetailsDao().findProductDetailsByVinRange(
						trackingStatusFlagcheck, fromTrackingStatus, toTrackingStatus, specCodeFlag, specCodeList,
						serailFlag, fromSerial != null ? Integer.parseInt(fromSerial) : null,
						toSerial != null ? Integer.parseInt(toSerial) : null, searchBy, pageable);

				if (pageProduct != null) {
					pageProduct.getContent().parallelStream().map(product -> {
						if (null != product.getLastProcessPointId()) {
							List<ProductAgeDetails> ageDetails = dataService.getProductAgeDetailsDao()
									.findAllBy(product.getId(), product.getLastProcessPointId());
							if (!ageDetails.isEmpty()) {
								product.setTrackingStatusDate(ageDetails.get(0).getActualTimestamp());
							}

						}
						if (null != product.getLineId()) {
							List<ProductLastStatusDetails> statusDetails = dataService.getProductLastStatusDetailsDao()
									.getLastTrackingDate(product.getId(), product.getLineId());
							if (!statusDetails.isEmpty()) {
								product.setActualTimestamp(statusDetails.get(0).getActualTimestamp());
							}

						}

						return product;
					}).collect(Collectors.toList());

				}

				if (pageProduct != null && pageProduct.getContent() != null) {

					if (fileType.equalsIgnoreCase("excel")) {
						byte[] excelData = CommonUtils.downloadFileVinRange(fileType, pageProduct.getContent());

						HttpHeaders headersResponse = new HttpHeaders();
						headersResponse.setContentType(MediaType
								.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
						headersResponse.add("Content-Disposition", "attachment; filename=productData.xlsx");

						return ResponseEntity.ok().headers(headersResponse).body(excelData);

					} else {

						byte[] pdfData = CommonUtils.downloadFileVinRange(fileType, pageProduct.getContent());

						HttpHeaders headersResponse = new HttpHeaders();
						headersResponse.setContentType(MediaType.APPLICATION_PDF);
						headersResponse.add("Content-Disposition", "attachment; filename=productData.pdf");

						return ResponseEntity.ok().headers(headersResponse).body(pdfData);
					}
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

	private List<ProductCountDto> mapList(List<ChartDto> source) {

		return IntStream.range(0, source.size()).parallel().mapToObj(index -> {
			ChartDto chartDto = source.get(index);
			String name = chartDto.getDivisionName();
			Integer y = (int) chartDto.getCount();
			return new ProductCountDto(name, y, name, index + 1);
		}).collect(Collectors.toList());
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

}
