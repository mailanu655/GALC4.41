package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.service.msip.dto.outbound.Gpp102Dto;
import com.honda.galc.service.msip.property.outbound.Gpp102PropertyBean;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Gpp102Handler extends BaseMsipOutboundHandler<Gpp102PropertyBean>{
	

	// runs once daily
	@SuppressWarnings("unchecked")
	@Override
	public List<Gpp102Dto> fetchDetails() {
		
		String[] departments = getPropertyBean().getDepartments();
		Map<String,String> processPointOnMap = getPropertyBean().getProcessPointOn();
		Map<String,String> processPointOffMap = getPropertyBean().getProcessPointOff();
		
		String[] lines = getPropertyBean().getTrackingStatus();
		Boolean useSequenceForBuildSequence = getPropertyBean().getUseSeqToBuildSeq();
		Integer  sequenceNumberScale = getPropertyBean().getSequenceNumberScale();
		Boolean excludeListedPlanCodes = getPropertyBean().getExcludeListedPlanCodes();
		Boolean allowDBUpdate = getPropertyBean().getAllowDbUpdate();
		String[] planCodesToExclude = getPropertyBean().getPlanCodesToExclude();
		
		int prodProgressType = 102; // GPP102
		
		Map<String, List<String>> productionProgressMap;
		
		List<String> results = new ArrayList<String>();
		List<Gpp102Dto> dtoList = new ArrayList<Gpp102Dto>();
		try{
			for(String department : departments) {
				String ppOn = processPointOnMap.get(department);
				String ppOff = processPointOffMap.get(department);
				List<String> ppOnList = StringUtils.isNotEmpty(ppOn)?Arrays.asList(ppOn.split(",")): null;
				List<String> ppOffList = StringUtils.isNotEmpty(ppOff)?Arrays.asList(ppOff.split(",")): null;
				productionProgressMap = getDao(ProductionLotDao.class)
					.getProductionProgress(prodProgressType,ppOnList ,ppOffList , department,
							Arrays.asList(lines), allowDBUpdate,useSequenceForBuildSequence,sequenceNumberScale, excludeListedPlanCodes, Arrays.asList(planCodesToExclude));
				results.addAll(productionProgressMap.get("GPP102"));
			}
			
			return createProductionResult(results);
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			Gpp102Dto dto = new Gpp102Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	private List<Gpp102Dto> createProductionResult(List<String> results) {
		List<Gpp102Dto> productionResults = new ArrayList<Gpp102Dto>();
		//HMA 014AF  01AF1201308219805DSZBAD500 NH578     B 000300002920170928055013N000000000000             
		for(String result : results) {
			Gpp102Dto gpp102Dto = new Gpp102Dto();
			gpp102Dto.setPlanCode(result.substring(0,11));
			gpp102Dto.setLineNo(result.substring(11,13));
			gpp102Dto.setProcessLocation(result.substring(13,15));
			gpp102Dto.setOnOffFlg(result.substring(15,16));			
			gpp102Dto.setProductionSeqNo(result.substring(16,28));
			
			gpp102Dto.setModelYearCode(result.substring(28,29));
			gpp102Dto.setModelCode(result.substring(29,32));
			gpp102Dto.setModelTypeCode(result.substring(32,35));
			gpp102Dto.setModelOptionCode(result.substring(35,38));
			gpp102Dto.setExtColourCode(result.substring(38,48));
			gpp102Dto.setIntColourCode(result.substring(48,50));
			gpp102Dto.setProductingQuantity(result.substring(50,55));
			gpp102Dto.setRemainingQuantity(result.substring(55,60));			
			gpp102Dto.setCreateDate(result.substring(60,68));
			gpp102Dto.setCreateTime(result.substring(68,74));			
			gpp102Dto.setBuildSeqFlag(result.substring(74,75));
			gpp102Dto.setBuildSeqNo(result.substring(75,87));
			gpp102Dto.setFiller("             ");
			productionResults.add(gpp102Dto);
		}
		
		return productionResults;
	}

}
