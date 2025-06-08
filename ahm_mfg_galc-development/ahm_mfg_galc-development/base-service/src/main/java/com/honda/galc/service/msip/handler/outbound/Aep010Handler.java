package com.honda.galc.service.msip.handler.outbound;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.FrameShipConfirmationDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.dto.oif.FrameShipConfirmationDTO;
import com.honda.galc.entity.oif.FrameShipConfirmation;
import com.honda.galc.entity.oif.FrameShipConfirmationId;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.msip.dto.outbound.Aep010Dto;
import com.honda.galc.service.msip.property.outbound.Aep010PropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Aep010Handler extends BaseMsipOutboundHandler<Aep010PropertyBean> {

	private final static String PROCESS_POINT = "PROCESS_POINT";
	private final static String RECORD_TYPE = "RECORD_TYPE";
	private final static String FLAG_INSERTED = "I";
	private final static String FLAG_PROCESED = "P";
	private final static int FRAME_OPTION_MAX_LENGTH	=	3;	
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Aep010Dto> fetchDetails() {
		Boolean JapanVINLeftJustified = getPropertyBean().getJapanVinLeftJustified();
		getLogger().info("Step in the Engine to Frame Mount Status Interface: ");
		
		// General properties
		final String[] recordTypes = getPropertyBean().getRecordTypes();
		final String plantCodeAep = getPropertyBean().getPlantCode();
		final String[] activeLineUrl = getPropertyBean().getActiveLines();
		final int deleteOldRecordsByCalendarDays = getPropertyBean().getDeleteOldRecordsByCalDays();
		final int insertLatestRecordsByCalendarDays = getPropertyBean().getInsertLatestRecordsByCalDays();
		List<Aep010Dto> result = new ArrayList<Aep010Dto>();
		
		try {
			Calendar calendar = Calendar.getInstance();
			int deleteBeforeDate = calendar.get(Calendar.DAY_OF_MONTH) - deleteOldRecordsByCalendarDays;
			int insertFromDate = calendar.get(Calendar.DAY_OF_MONTH) - insertLatestRecordsByCalendarDays;
			calendar.set(Calendar.DAY_OF_MONTH, deleteBeforeDate);
			Timestamp timestamp = Timestamp.valueOf(new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.S").format(calendar.getTime()));
			for (String activeLine : activeLineUrl) {
				getLogger().info("Process the line " + activeLine);
				// get the production result service
				FrameShipConfirmationDao frameShipConfirmationDao = HttpServiceProvider
						.getDao(
								activeLine + OIFConstants.HTTP_SERVICE_URL_PART,
								FrameShipConfirmationDao.class);

				// Delete old records
				frameShipConfirmationDao.deleteByDate(timestamp,FLAG_PROCESED);			

				//Handle multiple record types in single file
				if (recordTypes != null) {
					for (String recordType : recordTypes) {
						List<String> ppList = PropertyService.getPropertyList(getComponentId(), PROCESS_POINT+"{"+recordType+"}");
						String[] processPointList = (String[]) (String[]) ppList.toArray(new String[ppList.size()]);
						String recordValue = PropertyService.getProperty(getComponentId(), RECORD_TYPE+ "{"+recordType+"}"); 
						// insert records that don't exist in table
						calendar = Calendar.getInstance();
						calendar.set(Calendar.DAY_OF_MONTH, insertFromDate);

						frameShipConfirmationDao.insertRecordInexistent(
								processPointList, Timestamp
										.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(calendar.getTime())), plantCodeAep,recordValue, FRAME_OPTION_MAX_LENGTH);

						List<FrameShipConfirmationDTO> frameShipConfirmationDTOList = frameShipConfirmationDao.selectByFlag(FLAG_INSERTED,recordValue,plantCodeAep);
						if(frameShipConfirmationDTOList != null && !frameShipConfirmationDTOList.isEmpty()){
							/*
							 * Update the ProductId to justify Japanese VIN and frame_option
							 */
							for (FrameShipConfirmationDTO object : frameShipConfirmationDTOList) {
									
								object.setProductId(ProductNumberDef
									.justifyJapaneseVIN(object.getProductId(),
											JapanVINLeftJustified.booleanValue()));
								result.add(convertToDto(object));
							}
						}

					}
				}

				if (!result.isEmpty()) {
					updateStatus(result, frameShipConfirmationDao);
				}
			}
			return result;
		}catch (TaskException tx) {
			getLogger().error(tx.getMessage());
			result.clear();
			Aep010Dto dto = new Aep010Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + tx.getMessage());
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
	}
	
	private void updateStatus(List<Aep010Dto> list, FrameShipConfirmationDao frameShipConfirmationDao){
		//Update the status to 'P' if the message is send successfully.
		List<FrameShipConfirmation> frameShipConfirmationList = new ArrayList<FrameShipConfirmation>();
		for (Aep010Dto object : list) {
			FrameShipConfirmationId shipConfirmationId = new FrameShipConfirmationId();
			shipConfirmationId.setEngineId(object.getEngineId());
			shipConfirmationId.setProcessPointId(object.getProcessPointId());
			shipConfirmationId.setProductId(object.getProductId());

			FrameShipConfirmation frameShipConfirmation = frameShipConfirmationDao.findByKey(shipConfirmationId);
			// in the original logic is set sentFlag as "N" but
			// it can
			// be set as "P" because the next step change all
			// records
			// from "N" to "P"
			frameShipConfirmation.setSentFlag(FLAG_PROCESED);
			frameShipConfirmationList.add(frameShipConfirmation);
			//frameShipConfirmationDao.update(frameShipConfirmation);
		}
		if(!frameShipConfirmationList.isEmpty())	frameShipConfirmationDao.updateAll(frameShipConfirmationList);
	}
	
	public Aep010Dto convertToDto(FrameShipConfirmationDTO dto){
		Aep010Dto aep010Dto = new Aep010Dto();
		aep010Dto.setEngineId(dto.getEngineId());
		aep010Dto.setEventDate(dto.getEventDate());
		aep010Dto.setEventTime(dto.getEventTime());
		aep010Dto.setExtColor(dto.getExtColor());
		aep010Dto.setFiller(dto.getFiller());
		aep010Dto.setFrameModel(dto.getFrameModel());
		aep010Dto.setFrameOption(dto.getFrameOption());
		aep010Dto.setFrameType(dto.getFrameType());
		aep010Dto.setIntColor(dto.getIntColor());
		aep010Dto.setProcessPointId(dto.getProcessPointId());
		aep010Dto.setProductId(dto.getProductId());
		aep010Dto.setRecordType(dto.getRecordType());
		aep010Dto.setTransmissionId(dto.getTransmissionId());
		return aep010Dto;
	}

	

}
