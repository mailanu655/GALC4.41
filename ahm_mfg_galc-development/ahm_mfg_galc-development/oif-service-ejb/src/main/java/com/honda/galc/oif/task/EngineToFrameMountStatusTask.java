package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.oif.FrameShipConfirmationDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.dto.DtoUtil;
import com.honda.galc.dto.oif.FrameShipConfirmationDTO;
import com.honda.galc.entity.oif.FrameShipConfirmation;
import com.honda.galc.entity.oif.FrameShipConfirmationId;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>EngineToFrameMountStatusTask</h3>
 * <p>
 * EngineToFrameMountStatusTask is for AEP010
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author Daniel Garcia<br>
 *         March 19, 2015
 * 
 */
public class EngineToFrameMountStatusTask extends OifTask<Object> implements
		IEventTaskExecutable {

	private final static String PLANT_CODE = "PLANT_CODE_AEP";
	private final static String PROCESS_POINT = "PROCESS_POINT";
	private final static String RECORD_TYPE = "RECORD_TYPE";
	private final static String RECORD_TYPES = "RECORD_TYPES";
	private final static String FLAG_INSERTED = "I";
	private final static String FLAG_PROCESED = "P";
	private final static int FRAME_OPTION_MAX_LENGTH	=	3;
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, true);

	/**
	 * Interface name that was setting in the configurator, it the name assigned
	 * when was setting the class
	 * 
	 * @param Interface
	 *            Name
	 */
	public EngineToFrameMountStatusTask(String name) {
		super(name);
	}

	/**
	 * In this method is defined all business logic for the interface
	 */
	public void execute(Object[] args) {
		logger.info("Step in the Engine to Frame Mount Status Interface: "
				+ getName());
		refreshProperties();

		// General properties
		final String interfaceId = getProperty(OIFConstants.INTERFACE_ID);
		final String[] recordTypes = getPropertyArray(RECORD_TYPES);
		final String plantCodeAep = getProperty(PLANT_CODE);
		final String[] activeLineUrl = getPropertyArray(OIFConstants.ACTIVE_LINES);
		final int recordLength = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
		final int deleteOldRecordsByCalendarDays = getPropertyInt(OIFConstants.DELETE_OLD_RECORDS_BY_CALENDAR_DAYS,3);
		final int insertLatestRecordsByCalendarDays = getPropertyInt(OIFConstants.INSERT_LATEST_RECORDS_BY_CALENDAR_DAYS,14);
		
		try {
			Calendar calendar = Calendar.getInstance();
			int deleteBeforeDate = calendar.get(Calendar.DAY_OF_MONTH) - deleteOldRecordsByCalendarDays;
			int insertFromDate = calendar.get(Calendar.DAY_OF_MONTH) - insertLatestRecordsByCalendarDays;
			calendar.set(Calendar.DAY_OF_MONTH, deleteBeforeDate);
			Timestamp timestamp = Timestamp.valueOf(new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.S").format(calendar.getTime()));

			List<String> result = new ArrayList<String>();

			for (String activeLine : activeLineUrl) {

				
				logger.info("Process the line " + activeLine);
				// get the production result service
				FrameShipConfirmationDao frameShipConfirmationDao = HttpServiceProvider
						.getDao(
								activeLine + OIFConstants.HTTP_SERVICE_URL_PART,
								FrameShipConfirmationDao.class);

				// Delete old records
				frameShipConfirmationDao.deleteByDate(timestamp,FLAG_PROCESED);
				List<FrameShipConfirmationDTO> list = new ArrayList<FrameShipConfirmationDTO>();

				//Handle multiple record types in single file
				if (recordTypes != null) {
					for (String recordType : recordTypes) {
						String[] processPointList = getPropertyArray(PROCESS_POINT+"{"+recordType+"}");
						String recordValue = getProperty(RECORD_TYPE+ "{"+recordType+"}");
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
								
							}
							list.addAll(frameShipConfirmationDTOList);
						}

					}


					Map<String, String> layout = new HashMap<String, String>();
					layout = getMapLayout();

					result.addAll(DtoUtil.output(
							FrameShipConfirmationDTO.class,
							list, layout, recordLength));
				}

				if (!result.isEmpty()) {
					final String path = PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.RESULT);
					final String fileName = new StringBuilder(interfaceId)
							.append("_")
							.append(OIFConstants.stsf1.format(new Date()))
							.toString();
					final String mqConfig = PropertyService.getProperty(
							OIFConstants.OIF_SYSTEM_PROPERTIES,
							OIFConstants.MQ_CONFIG);

					sendData(result, null, null, path, fileName, mqConfig);
					//Update the status to 'P' if the message is send successfully.
					List<FrameShipConfirmation> frameShipConfirmationList = new ArrayList<FrameShipConfirmation>();
					for (FrameShipConfirmationDTO object : list) {
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
			}
		} catch (TaskException tx) {
			logger.error(tx.getMessage());
			errorsCollector.error(tx.getCause(), tx.getMessage());
		} finally {
			errorsCollector.sendEmail();
		}

	}

}
