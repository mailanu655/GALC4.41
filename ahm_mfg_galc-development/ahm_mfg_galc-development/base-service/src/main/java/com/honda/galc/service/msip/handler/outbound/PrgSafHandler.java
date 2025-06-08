package com.honda.galc.service.msip.handler.outbound;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.msip.dto.outbound.PrgSafDto;
import com.honda.galc.service.msip.property.outbound.PrgSafPropertyBean;
import com.honda.galc.service.property.PropertyService;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class PrgSafHandler extends BaseMsipOutboundHandler<PrgSafPropertyBean>  {
	public final String LAST_LOT_SENT_TO_NSE = "LAST_LOT_SENT_TO_NSE";
	public final String LAST_KD_LOT_SENT_TO_NSE = "LAST_KD_LOT_SENT_TO_NSE";
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PrgSafDto> fetchDetails()  {
		List<PrgSafDto> dtoList = new ArrayList<PrgSafDto>();
		try{
			getLogger().info("Inside  List<PrgSafDto> ");
			return processPpidList();
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			PrgSafDto dto = new PrgSafDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			e.printStackTrace();
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	/**
	 * Description
	 * 
	 * @param div
	 * @return	boolean 
	 */
	private boolean isSubAssemblyLine(String div) {
		List<String> subAssemblyLines= PropertyService.getPropertyList(getComponentId(),"SUB_ASSEMBLY_LINE");
		for (String subLines : subAssemblyLines)
		{
			if(StringUtils.equals(subLines, div)) return true;
		}
		return false;
	}
	
	private PreProductionLot getFirstAvailableProductionLot(String processLocation) {
		PreProductionLot preProdLot = null;
		PreProductionLot tempPreProdLot = null;
		PreProductionLot retPreProdLot = null;
		preProdLot = getDao(PreProductionLotDao.class).findLastPreProductionLotByProcessLocation(processLocation);
		if (preProdLot != null) {
			retPreProdLot = preProdLot;
			int sendStatus = preProdLot.getSendStatusId();
			while (sendStatus < PreProductionLotSendStatus.DONE.getId()) {
				tempPreProdLot =getDao(PreProductionLotDao.class).findParent(
						preProdLot.getProductionLot());
				retPreProdLot = preProdLot;
				if (tempPreProdLot == null)
					break;
				sendStatus = tempPreProdLot.getSendStatusId();
				preProdLot = tempPreProdLot;
			}
			logger.info("Successful getFirstAvailableProductionLot(): "	+ retPreProdLot);
			return retPreProdLot;
		} else
			return null;
	}
	
	// get data from Product Result (GAL215TBX), Process Point (GAL214TBX), and
		// Production Lot (GAL217TBX) tables
		// update COMPONENT_STATUS_TBX
		private List<PrgSafDto> processPpidList() {
			List<PrgSafDto> lotResultList = new ArrayList<PrgSafDto>();
			String[] departments = getPropertyBean().getDepartments();
			for (String div : departments) {
				List<String> ppIdList = PropertyService.getPropertyList(getComponentId(), createDepartmentKey("PPID", div));
				for (String ppid : ppIdList) {
					if(isSubAssemblyLine(div)){
						PreProductionLot preProdLot = getFirstAvailableProductionLot(div);
						ProcessPoint processPoint = getDao(ProcessPointDao.class).findById(ppid);
						PrgSafDto lotResult = getProductionProgressDto(preProdLot,processPoint);					
							
						lotResult.setKdLotNumber(lotResult.getKdLotNumber().substring(0,16));
						lotResultList.add(lotResult);
					}else{
						List<Object[]> tempList = new ArrayList<Object[]>();
						tempList = getDao(ProductResultDao.class).findProdLotResults(
								getPropertyBean().getSiteName(), getPropertyBean().getLineNo(), div, ppid);
						String strResult = "";
						for (Object[] objArray : tempList) {
							PrgSafDto lotResult = generateLotResult(objArray, div);
							lotResultList.add(lotResult);
							updateCompStatus(lotResult.getPpId(), strResult, LAST_KD_LOT_SENT_TO_NSE);
						}
					}
				}
			}
			return lotResultList;
		}

		private PrgSafDto generateLotResult(Object[] objArray, String div){
			PrgSafDto lotResult = getProductionProgressDto(objArray);
			String ppId = lotResult.getPpId();
			String kdLot = lotResult.getKdLotNumber();
			String prodLot = lotResult.getProductionLot();
			String lastLot = updateCompStatus(ppId, prodLot, LAST_LOT_SENT_TO_NSE);
			if (prodLot.compareTo(lastLot) > 0) {
				ProductionLot skippedLot = getDao(ProductionLotDao.class).findLastSkippedLot(ppId, lastLot, prodLot, 
						getPropertyBean().getSiteName(), getPropertyBean().getLineNo(), div);
				getLogger().info("Skipped Lot: " + skippedLot);
				if(skippedLot != null) {
					kdLot = skippedLot.getKdLotNumber();
					prodLot = skippedLot.getProductionLot();
				}
			}
			List<ProductResult> lots = getDao(ProductResultDao.class).getSubLots(kdLot, div, ppId);
			HashSet<String> setLots = new HashSet<String>();
			Date maxTimestamp = null;
			for(ProductResult lot : lots) {
				setLots.add(lot.getId().getProductId());	// count only distinct ProductId
				if(maxTimestamp == null || maxTimestamp.before(lot.getActualTimestamp())) {
					maxTimestamp = lot.getActualTimestamp();
				}
			}
			int unitCount = setLots.size();
			if(maxTimestamp != null) { 
				lotResult.setLineOnTimestamp(new Timestamp(maxTimestamp.getTime()));
			}
			lotResult.setUnitsPassed(unitCount);
			lotResult.setKdLotNumber(lotResult.getKdLotNumber().substring(0,16));
			return lotResult;
		}
		
		private String  updateCompStatus(String ppId, String strResult, String statusKey){
			ComponentStatusId cpId = new ComponentStatusId(ppId, statusKey);
			ComponentStatus cpLot = getDao(ComponentStatusDao.class).findByKey(cpId);
			if (cpLot == null) {
				cpLot = new ComponentStatus();
				cpLot.setId(cpId);
			}
			cpLot.setStatusValue(strResult);
			getDao(ComponentStatusDao.class).save(cpLot);
			return cpLot.getStatusValue();
		}
		private String createDepartmentKey(String key, String div) {
			return (new StringBuilder(key)).append("{").append(div).append("}").toString();
		}

		private PrgSafDto getProductionProgressDto(Object[] objArray) { 
			PrgSafDto lotResult = new PrgSafDto();
			lotResult.setProductionLot(objArray[0].toString());
			String kdLot = objArray[1].toString();
			lotResult.setKdLotNumber(kdLot);
			String procLoc = objArray[2].toString();
			lotResult.setProcessLocation(procLoc);
			int lotSize = getDao(ProductionLotDao.class).getLotSize(kdLot, procLoc);
			lotResult.setLotSize(lotSize);
			String ppId = objArray[4].toString().trim();
			lotResult.setPpId(ppId);
			int ppIdLength = ppId.length();
			String ppId7 = ppIdLength > 7 ? ppId.substring(ppIdLength-7) : ppId;
			lotResult.setPpId7(ppId7);
			lotResult.setPpIdDescription(objArray[5].toString());
			return lotResult;
		}
		
		private PrgSafDto getProductionProgressDto(PreProductionLot ppLot, ProcessPoint processPoint){
			PrgSafDto lotResult = new PrgSafDto();
			lotResult.setProductionLot(ppLot.getProductionLot().toString());
			String kdLot = ppLot.getKdLot().toString();
			lotResult.setKdLotNumber(kdLot);
			String procLoc = ppLot.getProcessLocation().toString();
			lotResult.setProcessLocation(procLoc);
			int lotSize = ppLot.getLotSize();
			lotResult.setLotSize(lotSize);
			String ppId = processPoint.getProcessPointId().toString().trim();
			lotResult.setPpId(ppId);
			int ppIdLength = ppId.length();
			String ppId7 = ppIdLength > 7 ? ppId.substring(ppIdLength-7) : ppId;
			lotResult.setPpId7(ppId7);
			lotResult.setPpIdDescription(processPoint.getProcessPointDescription().toString());
			int unitCount = ppLot.getStampedCount();
			lotResult.setUnitsPassed(unitCount);
			Date maxTimestampStr = ppLot.getUpdateTimestamp();
			lotResult.setLineOnTimestamp(new Timestamp(maxTimestampStr.getTime()));
			return lotResult;
		}
}
