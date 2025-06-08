package com.honda.galc.oif.task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.oif.dto.TransmissionProductionReportDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

public class TransmissionProductionReportTask extends OifTask<Object> implements
		IEventTaskExecutable {

	private final static String MESSAGE_LENGTH = "MESSAGE_LINE_LENGTH";
	private final static String PLANT_CODE = "PLANT_CODE";
	private final static String LAST_DATE = "LAST_RUN";

	private final static String PROCESS_POINT_DC_MC = "PROCESS_POINT_DC_M_CASE";
	private final static String PROCESS_POINT_DC_TC = "PROCESS_POINT_DC_TORQUE_CASE";

	private final static String PROCESS_POINT_MC_MC = "PROCESS_POINT_MC_M_CASE";
	private final static String PROCESS_POINT_MC_TC = "PROCESS_POINT_MC_TORQUE_CASE";
	private final static String PROCESS_POINT_MC_P = "PROCESS_POINT_MC_PULLEY";

	private final static String INTERFACE_ID = "INTERFACE_ID";
	private final static String DATE_FORMAT = "yyyyMMdd";
	private final static String TIME_FORMAT = "HHmmss";

	private String componentId;

	/**
	 * Interface name that was setting in the configurator, it the name assigned
	 * when was setting the class
	 * 
	 * @param Interface
	 *            Name
	 */
	public TransmissionProductionReportTask(String name) {
		super(name);
		this.componentId = name;
	}

	/**
	 * In this method is defined all business logic for the interface
	 */
	public void execute(Object[] args) {
		logger.info("Step in the Transmission Production Report Interface: "
				+ getName());
		refreshProperties();

		// General properties
		final Integer recordSize = getPropertyInt(MESSAGE_LENGTH);		
		final String propertyDate = getProperty(LAST_DATE,
				"2000-01-01-00.00.00.000000");

		final String processPointDcMc = getProperty(PROCESS_POINT_DC_MC);
		final String processPointDcTc = getProperty(PROCESS_POINT_DC_TC);

		final String processPointMcMc = getProperty(PROCESS_POINT_MC_MC);
		final String processPointMcTc = getProperty(PROCESS_POINT_MC_TC);
		final String processPointMcP = getProperty(PROCESS_POINT_MC_P);

		final String plantCode = getProperty(PLANT_CODE);
		final String interfaceId = getProperty(INTERFACE_ID);

		// ///////////////////////////////////////////////////////
		PreProductionLotDao preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);

		try {
			List<Object> listMbpn = preProductionLotDao.getTmProductionReport(propertyDate,
					processPointDcMc, processPointDcTc, processPointMcMc,
					processPointMcTc, processPointMcP, plantCode);

			Date now = Calendar.getInstance().getTime();
			String dateStr = new SimpleDateFormat(DATE_FORMAT).format(now);
			String timeStr = new SimpleDateFormat(TIME_FORMAT).format(now);

			// list with the String result formated corresponding with the
			// layout
			List<String> list = new ArrayList<String>();

			// initialize the array with empty characters
			final char[] lenghtArray = new char[recordSize];
			Arrays.fill(lenghtArray, ' ');

			final String layoutDefinition = getProperty(OIFConstants.PARSE_LINE_DEFS);
			final OutputFormatHelper<TransmissionProductionReportDTO> outputFormatHelper = new OutputFormatHelper<TransmissionProductionReportDTO>(
					layoutDefinition, this.logger, this.errorsCollector);
			outputFormatHelper
					.initialize(TransmissionProductionReportDTO.class);

			TransmissionProductionReportDTO productionReportDTO = null;
			for (Object object : listMbpn) {
				productionReportDTO = new TransmissionProductionReportDTO();
				productionReportDTO.setPlanCode((String) ((Object[]) object)[0]);
				productionReportDTO.setMfgBasicPartNo((String) ((Object[]) object)[1]);
				productionReportDTO.setPartClrCd((String) ((Object[]) object)[2]);
				productionReportDTO.setProdQty((String) ((Object[]) object)[3]);
				productionReportDTO.setProdDate(dateStr);
				productionReportDTO.setProdTime(timeStr);
				final String record = outputFormatHelper.formatOutput(
						productionReportDTO, lenghtArray);
				list.add(record);
			}

			// Update the time for the property of the component id
			ComponentPropertyDao componentPropertyDao = ServiceFactory
					.getDao(ComponentPropertyDao.class);
			componentPropertyDao.updateTimestamp(componentId, LAST_DATE);

			// Create a name file
			if (!list.isEmpty()) {
				final String path = PropertyService
						.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES,
								OIFConstants.RESULT);
				final String fileName = new StringBuilder(path).append(
						File.separator).append(interfaceId).append("_").append(
						OIFConstants.stsf1.format(new Date())).toString();
				logger.info("Writing the Transmission Production Report file: "
						+ fileName);
				OIFFileUtility.writeToFile(list, fileName);

				// Send data to MQ
				final String mqftpConfig = PropertyService.getProperty(
						OIFConstants.OIF_SYSTEM_PROPERTIES,
						OIFConstants.MQ_CONFIG);
				MQUtility mqClient = new MQUtility(this);
				logger
						.info("Sending the MQ message for Transmission Production Report ");
				mqClient.executeMQSendAPI(interfaceId, mqftpConfig, fileName);
			}
			
			logger.info("Step out the Transmission Production Report Interface: " + getName());

		} catch (IOException ioe) {
			errorsCollector
					.error("Error sending the mq message for Transmission Production Report Interface"
							+ ioe.getMessage());
			logger
					.error("Error to the mq message for Transmission Production Report Interface"
							+ ioe.getMessage());
		} catch (MQUtilityException mqx) {
			errorsCollector
					.error("Error sending the mq message for Transmission Production Report Interface"
							+ mqx.getMessage());
			logger
					.error("Error to the mq message for Transmission Production Report Interface"
							+ mqx.getMessage());
		} finally {
			errorsCollector.sendEmail();
		}

	}

}
