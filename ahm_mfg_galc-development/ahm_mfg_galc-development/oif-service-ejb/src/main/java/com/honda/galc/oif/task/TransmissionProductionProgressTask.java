package com.honda.galc.oif.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.GpcsProductionProgressDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;

public class TransmissionProductionProgressTask extends OifTask<Object>
		implements IEventTaskExecutable {

	private final static String MESSAGE_LENGTH = "MESSAGE_LINE_LENGTH";	
	private final static String PROCESS_LOCATION = "PROCESS_LOCATION";	
	private final static String PLANT_CODE = "PLANT_CODE";
	private final static String DATE = "DATE";
	private final static String PROCESS_POINT_AM_ON = "PROCESS_POINT_AM_ON";
	private final static String PROCESS_POINT_AM_OFF = "PROCESS_POINT_AM_OFF";
	private final static String INTERFACE_ID = "INTERFACE_ID";
	private final static String DATE_FORMAT = "yyyyMMdd";
	private final static String TIME_FORMAT = "HHmmss";
	private final static Integer OFF_FLAG = 2;
	private final static String MINUS_FLAG = "0";

	/**
	 * Interface name that was setting in the configurator, it the name assigned
	 * when was setting the class
	 * 
	 * @param Interface
	 *            Name
	 */
	public TransmissionProductionProgressTask(String name) {
		super(name);
	}

	/**
	 * In this method is defined all business logic for the interface
	 */
	public void execute(Object[] args) {
		logger.info("Step in the Transmission Production Progress Interface: "
				+ getName());
		refreshProperties();

		// General properties
		final Integer recordSize = getPropertyInt(MESSAGE_LENGTH);		
		final String processLocation = getProperty(PROCESS_LOCATION);

		final String propertyDate = getProperty(DATE);
		final String processPointAmOn = getProperty(PROCESS_POINT_AM_ON);
		final String processPointAmOff = getProperty(PROCESS_POINT_AM_OFF);
		final String plantCode = getProperty(PLANT_CODE);
		final String interfaceId = getProperty(INTERFACE_ID);

		ProductionLotDao productionLotDao = ServiceFactory.getDao(ProductionLotDao.class);
		Date createDate = null;
		try {
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(propertyDate);

			List<Object> listTransmission = productionLotDao.getProductionProgress(processLocation,
							plantCode, createDate, processPointAmOn, processPointAmOff);
			// list with the String result formated corresponding with the
			// layout
			final List<GpcsProductionProgressDTO> result = new ArrayList<GpcsProductionProgressDTO>();
			// initialize the array with empty characters
			final char[] lenghtArray = new char[recordSize];
			Arrays.fill(lenghtArray, ' ');

			// copy properties from entity to a DTO
			Date actualDate = new Date();

			for (Object object : listTransmission) {
				final Integer productionCountOff = (Integer) ((Object[]) object)[12];
				ProductionLot productionLot = productionLotDao.findByKey((String) ((Object[]) object)[16]);

				final GpcsProductionProgressDTO productionProgressDTO = new GpcsProductionProgressDTO();
				productionProgressDTO.setPlanCode(productionLot.getPlanCode());
				productionProgressDTO.setLineNo(productionLot.getLineNo());
				productionProgressDTO.setProcessLocation(productionLot.getProcessLocation());
				productionProgressDTO.setOnOffFlag(OFF_FLAG + "");
				productionProgressDTO.setKdLotNo(productionLot.getKdLotNumber());
				productionProgressDTO.setProdSeqNo(productionLot.getLotNumber());
				productionProgressDTO.setMbpn((String) ((Object[]) object)[5]);
				productionProgressDTO.setHesColor((String) ((Object[]) object)[6]);
				productionProgressDTO.setMtoc(productionLot.getProductSpecCode());
				productionProgressDTO.setResultQty(String.format("%8s", productionCountOff.toString()).replace(' ', '0'));
				productionProgressDTO.setCreatedDate(new SimpleDateFormat(DATE_FORMAT).format(actualDate));
				productionProgressDTO.setCreatedTime(new SimpleDateFormat(TIME_FORMAT).format(actualDate));
				productionProgressDTO.setMinusFlag(MINUS_FLAG);
				productionProgressDTO.setFiller("");
				result.add(productionProgressDTO);
			}
			
			logger.info( "Sending to MQ... " );
			final String path				=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT );
			final String mqConfig			=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
			final String layoutComponentId	=	getProperty					( OIFConstants.PARSE_LINE_DEFS );
			final String fileName			=	new StringBuilder( interfaceId )
												.append("_")
												.append( OIFConstants.stsf1.format( new Date() ))
												.toString();
			
			if ( result != null && result.size() > 0)
			{
				this.exportDataByOutputFormatHelper(GpcsProductionProgressDTO.class, result, path, fileName, mqConfig, layoutComponentId);
			}
			else
			{
				//send the message with GPCS NORECORD YYYYMMDDHHMMSS + 121 bytes(spaces)
				logger.info( "Sending the GPCS NORECORD message" );
				List<String> noDataMessage	=	new ArrayList<String>();
				final String message	=	MQUtility.createNoDataMessage( recordSize );
				noDataMessage.add( message );
				this.sendNoDataMessage(noDataMessage, path, fileName, mqConfig);
			}
		} catch (ParseException parseException) {
			logger.error("Error when try to convert date. " + parseException);
			errorsCollector.error("Error when try to convert date. " + parseException);
		} finally {
			errorsCollector.sendEmail();
		}

	}

}
