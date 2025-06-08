package com.honda.galc.oif.task.gccs;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.util.OpenJPAException;
import org.springframework.beans.BeanUtils;

import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.dao.oif.MissionMaterialServiceDao;
import com.honda.galc.entity.oif.MaterialService;
import com.honda.galc.entity.oif.MaterialServiceId;
import com.honda.galc.oif.dto.MaterialServiceDTO;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.RegularMQClient;
import com.ibm.mq.MQException;

/**
 * Implements the funcionallity to send the Transmission Information to GCCS -
 * Material Service.
 * 
 * @author Daniel Garcia Chavez
 */
public class TransmissionGCCSTask extends OifTask<Object> implements
		IEventTaskExecutable {

	// This section define the constants
	private final static String DAYS_TO_RUN_BEFORE = "OIF_DAYS_BEFORE";
	private static final String PROCESS_POINT_ON = "PROCESS_POINT_ON";
	private static final String FORMAT_DATE = "yyMMdd";
	private static final String FORMAT_TIME = "HHmmss";
	private static final String PLANT_CODE = "PLANT_CODE";
	private static final String PROCESS_LOCATIONS = "PROCESS_LOCATIONS";
	private static final String[] locations = new String[] { "AM", "MC", "DC" };
	private static final Integer CHARACTER_SET = 819;
	// In mexico is used ASCII encoding
	private static final Integer ENCODING_ASCII = 279;

	/**
	 * Interface name that was setting in the configurator, it the name assigned
	 * when was setting the class
	 * 
	 * @param Interface
	 *            Name
	 */
	public TransmissionGCCSTask(String name) {
		super(name);
	}

	/**
	 * In this method is defined all business logic for the interface
	 */
	public void execute(Object[] args) {
		logger
				.info("Step in the Transmission Warranty Interface: "
						+ getName());
		refreshProperties();

		// General properties
		final Integer daysBefore = getPropertyInt(DAYS_TO_RUN_BEFORE);
		final String processPoint = getProperty(PROCESS_POINT_ON);
		final Integer recordSize = getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH);
		final String plantCode = getProperty(PLANT_CODE);
		final String processLocation = getProperty(PROCESS_LOCATIONS);

		// Get the current date with 2 days less
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- daysBefore);
		Date dateBefore = calendar.getTime();

		MissionMaterialServiceDao materialServiceDao = ServiceFactory
				.getDao(MissionMaterialServiceDao.class);
		RegularMQClient regularMQClient = null;
		try {
			logger.info("Starting to delete old records in MS_PMX_TBX");
			materialServiceDao.removeAll(materialServiceDao
					.selectOldRecordMaterialService(dateBefore));
			logger.info("Finish to delete old records in MS_PMX_TBX");

			logger.info("Starting to get the Line Signal information to send gccs");

			List<Object> listMaterialService = new ArrayList<Object>();
			
			//this is for filter the process location that it can be setting in properties 
			for (String process : processLocation.split(",")) {
				List<Object> listTemp = null;
				if (process.equals(locations[0])) {
					listTemp = materialServiceDao
							.getTransmissionMaterialServicePriorityPlanSchedule(
									dateBefore, processPoint, plantCode);
					for (Object object : listTemp) {
						listMaterialService.add(object);
					}
				}
				if (!process.equals(locations[0])) {
					listTemp = materialServiceDao
							.getTransmissionMaterialServiceInHouseSchedule(
									dateBefore, processPoint, plantCode);
					for (Object object : listTemp) {
						listMaterialService.add(object);
					}
				}
			}

			// Initialize the output format helper
			final String layoutDefinition = getProperty(OIFConstants.PARSE_LINE_DEFS);
			final OutputFormatHelper<MaterialServiceDTO> outputFormatHelper = new OutputFormatHelper<MaterialServiceDTO>(
					layoutDefinition, this.logger, this.errorsCollector);
			outputFormatHelper.initialize(MaterialServiceDTO.class);

			// initialize the array with empty characters
			final char[] lenghtArray = new char[recordSize];
			Arrays.fill(lenghtArray, ' ');

			// Send data to MQ
			MaterialServiceDTO materialServiceDTO = null;
			Map<String,String> processPointMap = PropertyService.getPropertyMap(getComponentId(), OIFConstants.PROCESS_POINT_MAP);
			regularMQClient = getRegMQClient();
			for (Object obj : listMaterialService) {
				materialServiceDTO = new MaterialServiceDTO();
				if (regularMQClient == null) {
					regularMQClient = getRegMQClient();
					logger.info(regularMQClient.toString());
				}
				logger.info(regularMQClient.toString());

				// Setting information in DTO
				materialServiceDTO.setProductId((String) ((Object[]) obj)[0]);
				materialServiceDTO.setPlanCode((String) ((Object[]) obj)[1]);
				materialServiceDTO.setLineNo((String) ((Object[]) obj)[2]);
				{
					String pp = (String) ((Object[]) obj)[3];
					String mapPP = processPointMap.get(pp);
					materialServiceDTO.setProcessPointId(mapPP == null ? pp : mapPP);
				}
				materialServiceDTO.setProductionDate(new SimpleDateFormat(FORMAT_DATE).format((Date) ((Object[]) obj)[4]));
				materialServiceDTO.setActualTimestamp(new SimpleDateFormat(FORMAT_TIME).format((Date) ((Object[]) obj)[5]));
				materialServiceDTO.setProductSpecCode((String) ((Object[]) obj)[6]);
				materialServiceDTO.setLotSize((String) ((Object[]) obj)[7]);
				materialServiceDTO.setOnSeqNo((String) ((Object[]) obj)[8]);
				materialServiceDTO.setProductionLot((String) ((Object[]) obj)[9]);
				materialServiceDTO.setKdLotNumber((String) ((Object[]) obj)[10]);
				materialServiceDTO.setPlanOffDate(new SimpleDateFormat(FORMAT_DATE).format((Date) ((Object[]) obj)[11]));
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

				regularMQClient.putMessage(outputFormatHelper.formatOutput(materialServiceDTO, lenghtArray), CHARACTER_SET, ENCODING_ASCII); 
				
				// Saving in database the current record
				materialServiceDao.save(pmxTbx);
			}

		} catch (MQException mq) {
			errorsCollector.error("Error sending the mq message for Transmission Material Service Interface"
							+ mq.getMessage());
			logger.error("Error to the mq message for Transmission Material Service Interface"
							+ mq.getMessage());
		} catch (IOException ioe) {
			errorsCollector.error("Error sending the mq message for Transmission Material Service Interface"
							+ ioe.getMessage());
			logger.error("Error to the mq message for Transmission Material Service Interface"
							+ ioe.getMessage());
		} catch (OpenJPAException ex) {
			logger.error("Error while executed the query " + ex.getMessage());
			errorsCollector.error("Error while executed the query "
					+ ex.getMessage());
		} finally {
			try {
				if (regularMQClient != null) {
					regularMQClient.finalize();
					regularMQClient = null;
				}
			} catch (MQException e) {
				logger.error("Exception in closing MQ connection: "
						+ e.getMessage());
				errorsCollector.error("Exception in closing MQ connection: "
						+ e.getMessage());
			}
			errorsCollector.sendEmail();
		}

	}

	/**
	 * This method connect to the MQ with values defined in interface properties
	 * 
	 * @return the MQ connection
	 * @throws MQException
	 */
	private RegularMQClient getRegMQClient() throws MQException {
		RegularMQClient rmq = new RegularMQClient(getProperty("HOST_NAME"),
				getPropertyInt("PORT_NUMBER"),
				getProperty("QUEUE_MANAGER_NAME"), getProperty("CHANNEL"),
				getProperty("QUEUE_NAME"), getProperty("USER_NAME"),
				getProperty("PASSWORD"));
		return rmq;
	}

}
