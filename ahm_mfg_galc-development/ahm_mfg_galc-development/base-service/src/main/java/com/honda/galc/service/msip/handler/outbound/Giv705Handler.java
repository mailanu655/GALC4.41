package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.property.EngineLinePropertyBean;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.msip.dto.outbound.Giv705Dto;
import com.honda.galc.service.msip.property.outbound.Giv705PropertyBean;
import com.honda.galc.service.property.PropertyService;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv705Handler extends BaseMsipOutboundHandler<Giv705PropertyBean>{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv705Dto> fetchDetails(Date ts) {
		List<Giv705Dto> dtoList = new ArrayList<Giv705Dto>();
		try{
			Timestamp sTs = null;
			if(ts != null){
				sTs = new Timestamp(ts.getTime());
			}
			return getProductionResults(sTs);
		}catch(Exception e){
			dtoList.clear();
			Giv705Dto dto = new Giv705Dto();
			e.printStackTrace();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv705Dto> fetchDetails() {
		List<Giv705Dto> dtoList = new ArrayList<Giv705Dto>();
		try{
			Timestamp sTs = new java.sql.Timestamp(
					(new java.util.Date()).getTime());
			return getProductionResults(sTs);
		}catch(Exception e){
			dtoList.clear();
			e.printStackTrace();
			Giv705Dto dto = new Giv705Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	
	private List<Giv705Dto> getProductionResults(Timestamp ts) {
		
		String weOffResCntPPId = getPropertyBean().getWeOffResCntPpid();
		String paOffResCntPPId = getPropertyBean().getPaOffResCntPpid();
		String afOffResCntPPId = getPropertyBean().getAfOffResCntPpid();
		String aeOffResCntPPId1 = getPropertyBean().getAeOffResPpid1();
		String aeOffResCntPPId2 = getPropertyBean().getAeOffResPpid2();
		
		List<Object[]> resultList;
		
		List<Giv705Dto> productionResultList = new ArrayList<Giv705Dto>();
		
		ProductResultDao productResultDao = getDao(ProductResultDao.class);
		
		List<Timestamp> afTsList = getEndTimestamp("AF",ts);
		
		if(!afTsList.isEmpty()){
			// AF production result
			List<String> afOffList = StringUtils.isNotEmpty(afOffResCntPPId)?Arrays.asList(afOffResCntPPId.split(",")): null;
			resultList = productResultDao.getAFProductionResult(afTsList.get(0), afTsList.get(1), afOffList);
			productionResultList.addAll(createProductionResults(resultList));
			// maybe only needed once a day
			// af scrapped products 
			productionResultList.addAll(getScrappedVehicleResults(afTsList.get(0), afTsList.get(1)));
		}
		
		List<Timestamp> paTsList = getEndTimestamp("PA",ts);
		if(!paTsList.isEmpty()){
			// PA production result
			List<String> paOffList = StringUtils.isNotEmpty(paOffResCntPPId)?Arrays.asList(paOffResCntPPId.split(",")): null;
			resultList = productResultDao.getAFProductionResult(paTsList.get(0), paTsList.get(1), paOffList);
			productionResultList.addAll(createProductionResults(resultList));
		}
				
		List<Timestamp> weTsList = getEndTimestamp("WE",ts);
		if(!weTsList.isEmpty()){
			// WE production result
			List<String> weOffList = StringUtils.isNotEmpty(weOffResCntPPId)?Arrays.asList(weOffResCntPPId.split(",")): null;
			resultList = productResultDao.getAFProductionResult(weTsList.get(0), weTsList.get(1), weOffList);
			productionResultList.addAll(createProductionResults(resultList));
		}
		
		List<Timestamp> aeTsList = getEndTimestamp("AE",ts);
		if(!aeTsList.isEmpty()){
			// AE production result
			resultList = productResultDao.getAEProductionResult(aeTsList.get(0), aeTsList.get(1), aeOffResCntPPId1,aeOffResCntPPId2);
			productionResultList.addAll(createProductionResults(resultList));
			// maybe only needed once a day
			// af scrapped products 
			productionResultList.addAll(getScrappedEngineResults(aeTsList.get(0), aeTsList.get(1)));
		}
		
		return productionResultList;
		
	}
	
	private List<Giv705Dto> getScrappedVehicleResults(Timestamp afStartTimestamp,Timestamp afEndTimestamp) {
		
		List<Giv705Dto> scrapProductionResults = new ArrayList<Giv705Dto>();
		
		FrameLinePropertyBean framePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, getComponentId());
		
		List<Object[]> afScrappedVINsList =  new ArrayList<Object[]>();
		
		ProductResultDao productResultDao = getDao(ProductResultDao.class);
		
		if (!StringUtils.isEmpty(framePropertyBean.getScrapLineId())) {
			afScrappedVINsList = productResultDao.getFrameScrap(afStartTimestamp, afEndTimestamp, Arrays.asList(framePropertyBean.getScrapLineId()));
		}
		
		/***********************************************************************************************************************************************
		 * So for each "scrap VIN we find, we want to see what areas (WE, PA
		 * and AF) that VIN has already passed through and sent a previous
		 * result for. We need to send a counter to that result that tells
		 * them the VIN was scrapped. Since this file is used to backflush
		 * parts out of inventory, this means from a business prospective
		 * that if we scrap a VIN, we can reuse those parts, so we want to
		 * add them back to our inventory and NOT place an order for more
		 * parts. That is the general "gist" of this change because
		 * additional parts are being ordered and not used.
		 ***********************************************************************************************************************************************/
		String weOffResCntPPId = getPropertyBean().getWeOffResCntPpid();
		String paOffResCntPPId = getPropertyBean().getPaOffResCntPpid();
		String afOffResCntPPId = getPropertyBean().getAfOffResCntPpid();
		
		List<String> afOffList = StringUtils.isNotEmpty(afOffResCntPPId)?Arrays.asList(afOffResCntPPId.split(",")): null;
		List<String> paOffList = StringUtils.isNotEmpty(paOffResCntPPId)?Arrays.asList(paOffResCntPPId.split(",")): null;
		List<String> weOffList = StringUtils.isNotEmpty(weOffResCntPPId)?Arrays.asList(weOffResCntPPId.split(",")): null;
		
		for (Object[] afScrappedVinsArr : afScrappedVINsList) {
			String tempVal = afScrappedVinsArr[3].toString();

			List<Object> weOffResultCntList = productResultDao.getOffResultCnt(tempVal, weOffList);

			List<Object> paOffResultCntList = productResultDao.getOffResultCnt(tempVal, paOffList);

			List<Object> afOffResultCntList = productResultDao.getOffResultCnt(tempVal, afOffList);

			if (weOffResultCntList != null && weOffResultCntList.size() > 0 && new Integer(weOffResultCntList.get(0).toString()) > 0) {
				scrapProductionResults.add(createScrappedProductResults(afScrappedVinsArr, "WE",isJapaneseVinLeftJustified()));
			}
			if (paOffResultCntList != null && paOffResultCntList.size() > 0 && new Integer(paOffResultCntList.get(0).toString()) > 0) {
				scrapProductionResults.add(createScrappedProductResults(afScrappedVinsArr, "PA",isJapaneseVinLeftJustified()));
			}
			if (afOffResultCntList != null && afOffResultCntList.size() > 0 && new Integer(afOffResultCntList.get(0).toString()) > 0) {
				scrapProductionResults.add(createScrappedProductResults(afScrappedVinsArr, "AF",isJapaneseVinLeftJustified()));
			}
		}
		
		return scrapProductionResults;
	}
	
	private List<Giv705Dto> getScrappedEngineResults(Timestamp aeStartTimestamp,Timestamp aeEndTimestamp) {
		List<Giv705Dto> scrapProductionResults = new ArrayList<Giv705Dto>();
		
		EngineLinePropertyBean enginePropertyBean = PropertyService.getPropertyBean(EngineLinePropertyBean.class, getComponentId());
		
		List<Object[]> aeScrappedEINsList =  new ArrayList<Object[]>();
		
		ProductResultDao productResultDao = getDao(ProductResultDao.class);
		
		if (!StringUtils.isEmpty(enginePropertyBean.getScrapLineId())) {
			aeScrappedEINsList = productResultDao.getEngineScrap(aeStartTimestamp, aeEndTimestamp, Arrays.asList(enginePropertyBean.getScrapLineId()));
		}
		
		for(Object[] afScrappedVinsArr : aeScrappedEINsList) {
			scrapProductionResults.add(createScrappedProductResults(afScrappedVinsArr, "AF",true)); //left justified for Engine
		}
		
		return scrapProductionResults;
	}

	private List<Giv705Dto> createProductionResults(List<Object[]> resultList) {
		
		List<Giv705Dto> productResultList = new ArrayList<Giv705Dto>();
		
		for(Object[] objects : resultList) {
			Giv705Dto productionResult = new Giv705Dto ();
			
			productionResult.setPlanCode(objects[0].toString());
			productionResult.setLineNumber(objects[1].toString());
			productionResult.setProcessLocation(objects[2].toString());
			productionResult.setVinNumber(ProductNumberDef.justifyJapaneseVIN(objects[3].toString(), isJapaneseVinLeftJustified()));
			productionResult.setProductionSequenceNumber(objects[4].toString());
			productionResult.setAlcActualTimestamp(objects[5].toString()+objects[6].toString()+objects[7].toString()+objects[8].toString()+objects[9].toString()+objects[10].toString());
			productionResult.setProductSpecCode(objects[11].toString());
			productionResult.setBandNumber("                    ");
			productionResult.setKdLotNumber(objects[12].toString());
			productionResult.setPartNumber("                  ");
			productionResult.setPartColorCode("           ");
			productionResult.setBosSerialNumber("     ");
			productionResult.setFiller("                                              ");
			productionResult.setCancelReasonCode(" ");
			productionResult.setResultFlag("0");
			productionResult.setCancelFlag(objects[13].toString());
			productResultList.add(productionResult);
		}
		return productResultList;
		
	}
	
	private Giv705Dto createScrappedProductResults(Object[] objects,String dept,boolean isJapaneseVinLeftJustified) {
		Giv705Dto productionResult = new Giv705Dto ();
		productionResult.setPlanCode(objects[0].toString());
		productionResult.setLineNumber(objects[1].toString());
		productionResult.setProcessLocation(dept);
		productionResult.setVinNumber(ProductNumberDef.justifyJapaneseVIN(objects[3].toString(), isJapaneseVinLeftJustified()));
		productionResult.setProductionSequenceNumber(objects[4].toString());
		productionResult.setAlcActualTimestamp(objects[5].toString()+objects[6].toString()+objects[7].toString()+objects[8].toString()+objects[9].toString()+objects[10].toString());
		productionResult.setProductSpecCode(objects[11].toString());
		productionResult.setBandNumber("                    ");
		productionResult.setKdLotNumber(objects[12].toString());
		productionResult.setPartNumber("                  ");
		productionResult.setPartColorCode("           ");
		productionResult.setBosSerialNumber("     ");
		productionResult.setFiller("                                             ");
		productionResult.setCancelReasonCode("2");
		productionResult.setResultFlag("0");
		productionResult.setCancelFlag(objects[13].toString());
		return productionResult;
	}
		 
	
	private List<Timestamp> getEndTimestamp(String processLocation, Timestamp prodTimestamp) {
		Timestamp endTimestamp;
		Timestamp startTimestamp;
		List<Timestamp> tsList = new ArrayList<Timestamp>();
		ProductResultDao productResultDao = getDao(ProductResultDao.class);
		List<Object[]> afProdPeriodList = productResultDao.getProductionPeriod(prodTimestamp, processLocation, processLocation);
		if (afProdPeriodList != null && afProdPeriodList.size() > 0) {
			Object[] firstRecArr = afProdPeriodList.get(0);
			startTimestamp = (Timestamp) firstRecArr[3];
			Object[] lastRecArr = afProdPeriodList.get(afProdPeriodList.size() - 1);
			endTimestamp = (Timestamp) lastRecArr[4];
			tsList.add(startTimestamp);
			tsList.add(endTimestamp);
			getLogger().info(processLocation+"production period :- " + firstRecArr[2] + " | " + firstRecArr[3] + " | " + lastRecArr[4]);
		} else {
			getLogger().debug("No production periods found for AF department");
		}
		return tsList;
	}

}
