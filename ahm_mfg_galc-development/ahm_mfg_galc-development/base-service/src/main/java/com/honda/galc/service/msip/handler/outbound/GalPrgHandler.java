package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.honda.galc.dao.oif.MissionMaterialServiceDao;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.entity.oif.MaterialServiceId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.GalPrgDto;
import com.honda.galc.service.msip.property.outbound.GalPrgPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class GalPrgHandler extends BaseMsipOutboundHandler<GalPrgPropertyBean> {

	@SuppressWarnings("unchecked")
	@Override
	public List<GalPrgDto> fetchDetails() {
		List<GalPrgDto> dtoList = new ArrayList<GalPrgDto>();
		try{
			return saveMaterialInfo();
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			GalPrgDto dto = new GalPrgDto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}

	private List<GalPrgDto> saveMaterialInfo() {
		
		getLogger().info("Step in the Transmission Warranty Interface: " + this.getClass().getName());
		// General properties
		List<GalPrgDto> listOfGalPrgDto = new ArrayList<GalPrgDto>();
		final Integer daysBefore = getPropertyBean().getDaysToRunBefore();
		final String processPoint = getPropertyBean().getProcessPointOn();
		final String plantCode = getPropertyBean().getPlantCode();
		final String[] processLocation = getPropertyBean().getProcessLocations();
		// Get the current date with 2 days less
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- daysBefore);
		Date dateBefore = calendar.getTime();
		MissionMaterialServiceDao materialServiceDao = ServiceFactory
				.getDao(MissionMaterialServiceDao.class);
		try {
			getLogger().info("Starting to delete old records in MS_PMX_TBX");
			materialServiceDao.removeAll(materialServiceDao
					.selectOldRecordMaterialService(dateBefore));
			getLogger().info("Finish to delete old records in MS_PMX_TBX");

			getLogger().info("Starting to get the Line Signal information to send gccs");

			List<Object> listMaterialService = new ArrayList<Object>();
			//this is for filter the process location that it can be setting in properties 
			for (String process : processLocation) {
				List<Object> listTemp = null;
				if (process.equals(processLocation[0])) {
					listTemp = materialServiceDao
							.getTransmissionMaterialServicePriorityPlanSchedule(
									dateBefore, processPoint, plantCode);
					for (Object object : listTemp) {
						listMaterialService.add(object);
					}
				}
				if (!process.equals(processLocation[0])) {
					listTemp = materialServiceDao
							.getTransmissionMaterialServiceInHouseSchedule(
									dateBefore, processPoint, plantCode);
					for (Object object : listTemp) {
						listMaterialService.add(object);
					}
				}
			}
			return getDtoList(listMaterialService, materialServiceDao);
		} catch(Exception ex) {
			listOfGalPrgDto.clear();
			GalPrgDto dto = new GalPrgDto();
			dto.setErrorMsg("Unexpected Error Occured: " + ex.getMessage());
			dto.setIsError(true);
			listOfGalPrgDto.add(dto);
			getLogger().error(ex);
			return listOfGalPrgDto;
		}
	}

	private List<GalPrgDto> getDtoList(List<Object> listMaterialService, MissionMaterialServiceDao materialServiceDao){
		// Send data to MQ
		GalPrgDto materialServiceDTO = null;
		List<GalPrgDto> listOfGalPrgDto = new ArrayList<GalPrgDto>();
		Map<String,String> processPointMap = PropertyService.getPropertyMap(getComponentId(), OIFConstants.PROCESS_POINT_MAP);
		
		for (Object obj : listMaterialService) {
			materialServiceDTO = new GalPrgDto();
			
			// Setting information in DTO
			materialServiceDTO.setProductId((String) ((Object[]) obj)[0]);
			materialServiceDTO.setPlanCode((String) ((Object[]) obj)[1]);
			materialServiceDTO.setLineNo((String) ((Object[]) obj)[2]);
			{
				String pp = (String) ((Object[]) obj)[3];
				String mapPP = processPointMap.get(pp);
				materialServiceDTO.setProcessPointId(mapPP == null ? pp : mapPP);
			}
			materialServiceDTO.setProductionDate(new SimpleDateFormat(getPropertyBean().getFormatDate())
					.format((Date) ((Object[]) obj)[4]));
			materialServiceDTO.setActualTimestamp(new SimpleDateFormat(getPropertyBean().getFormatTime())
					.format((Date) ((Object[]) obj)[5]));
			materialServiceDTO.setProductSpecCode((String) ((Object[]) obj)[6]);
			materialServiceDTO.setLotSize((String) ((Object[]) obj)[7]);
			materialServiceDTO.setOnSeqNo((String) ((Object[]) obj)[8]);
			materialServiceDTO.setProductionLot((String) ((Object[]) obj)[9]);
			materialServiceDTO.setKdLotNumber((String) ((Object[]) obj)[10]);
			materialServiceDTO.setPlanOffDate(new SimpleDateFormat(getPropertyBean().getFormatDate())
					.format((Date) ((Object[]) obj)[11]));
			materialServiceDTO.setCurrentTimestamp((String) ((Object[]) obj)[12]);
			materialServiceDTO.setPartNumber((String) ((Object[]) obj)[14]);

			// Setting Information in Entity
			MaterialService pmxTbx = new MaterialService();
			BeanUtils.copyProperties(materialServiceDTO, pmxTbx,
					new String[] { "productId", "processPointId",
							"partNumber", "productionDate", "planOffDate",
							"createTimestamp", "lotSize", "onSeqNo", "actualTimestamp" });
			
			MaterialServiceId id = new MaterialServiceId();
			id.setProcessPointId(materialServiceDTO.getProcessPointId());
			id.setProductId(materialServiceDTO.getProductId());
			pmxTbx.setId(id);
			pmxTbx.setProductionDate((Date) ((Object[]) obj)[4]);
			pmxTbx.setPlanOffDate((Date) ((Object[]) obj)[11]);
			pmxTbx.setActualTimestamp((Timestamp) ((Object[]) obj)[5]);
			pmxTbx.setSentFlag(((String) ((Object[]) obj)[13]).charAt(0));
			pmxTbx.setLotSize(Integer.parseInt((String) ((Object[]) obj)[7]));
			pmxTbx.setOnSeqNo(Integer.parseInt((String) ((Object[]) obj)[8]));
			// Saving in database the current record
			materialServiceDao.save(pmxTbx);
			listOfGalPrgDto.add(materialServiceDTO);
		} 
		return listOfGalPrgDto;
	}
	

}
