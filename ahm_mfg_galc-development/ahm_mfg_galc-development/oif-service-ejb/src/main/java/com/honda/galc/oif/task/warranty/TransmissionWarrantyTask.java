package com.honda.galc.oif.task.warranty;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.OutputFormatHelper;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.oif.dto.TransmissionWarrantyDTO;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * Send the Transmission Information to HAM - Warranty System.
 * @author anuar vasquez gomez
 *
 */
public class TransmissionWarrantyTask extends OifTask<Object> implements IEventTaskExecutable{
	
	//Constant property of transmission warranty interfaces
	//static properties with the key property name to use for this interface
	private static final String OIF_TM_WAR_LAST_DATE_FILTER = "OIF_TM_WAR_LAST_DATE_FILTER";
	private final static String OIF_TM_SCRAP_EXCEPTIONAL	= "OIF_TM_SCRAP_EXCEPTIONAL";
	private final static String OIF_TM_WAR_TMPP_OFF			= "OIF_TM_WAR_TMPP_OFF";
	private final static String OIF_TM_WAR_TORQUE_PART_NAME = "OIF_TM_WAR_TORQUE_PART_NAME";
	private final static String OIF_TM_WAR_CASE_PART_NAME	= "OIF_TM_WAR_CASE_PART_NAME";

	
	public TransmissionWarrantyTask(String name) {
		super(name);
	}

	public void execute(Object[] args) 
	{
		logger.info("Step in the Transmission Warranty Interface: " + getName() );
		refreshProperties();
		//general properties
		final String	lastDateFilter		= getProperty			( OIF_TM_WAR_LAST_DATE_FILTER		);
		final Integer	recordSize			= getPropertyInt		( OIFConstants.MESSAGE_LINE_LENGTH	);
		final String	interfaceId			= getProperty			( OIFConstants.INTERFACE_ID			);
		//Transmission warranty properties
		final String trackingStatus = getProperty( OIF_TM_SCRAP_EXCEPTIONAL		);
		final String tmOff 			= getProperty( OIF_TM_WAR_TMPP_OFF			);
		final String torquePartName = getProperty( OIF_TM_WAR_TORQUE_PART_NAME	);
		final String casePartName	= getProperty( OIF_TM_WAR_CASE_PART_NAME	);
				
		logger.info( "Initialize OutputFormaterHelper for Transmission Warranty Task" );
		//Initialize the output format helper
		final String layoutDefinition = getProperty( OIFConstants.PARSE_LINE_DEFS );
		final OutputFormatHelper<TransmissionWarrantyDTO> outputFormatHelper = new OutputFormatHelper<TransmissionWarrantyDTO>( layoutDefinition, this.logger, this.errorsCollector );
		outputFormatHelper.initialize(TransmissionWarrantyDTO.class);
		//initialize the array with empty characters
		final char[] lenghtArray = new char[recordSize]; 
		Arrays.fill( lenghtArray, ' ');
		
		logger.info ( "Query the transmission with AM OFF" );
		MissionDao warrantyDao 			= ServiceFactory.getDao					( MissionDao.class );
		List<Object[]> warrantyData 	= warrantyDao.queryTransmissionWarranty	( tmOff, trackingStatus, torquePartName, casePartName, lastDateFilter );
		
		//variable with the current date after get the warranty data
		final Date currentDate = new Date( );
		//variable to save the total records retrieved.
		int totalRecord = 0;
		if ( warrantyData != null && !warrantyData.isEmpty( ) )
		{
			totalRecord = warrantyData.size();
		}
		
		final String warrantyHeaerRecord	= new StringBuilder( PropertyService.getSiteName() + " PRODATA ")
											.append( String.format( "%09d", totalRecord ) )
											.append( String.format( "%179s", " ") )
											.toString();
		//list with the String result formated corresponding with the layout
		final List<String> resultFormated = new ArrayList<String>();
		//Add the header record in the list, it should be the first record
		resultFormated.add(warrantyHeaerRecord);
		//copy properties from entity to a DTO
		for (Object[] data : warrantyData)
		{
			final TransmissionWarrantyDTO warrantyDTO = new TransmissionWarrantyDTO();
			warrantyDTO.setProductId			( data[0] == null ? "" : data[0].toString()  );
			warrantyDTO.setBuildDate			( data[1] == null ? "" : data[1].toString()  );
			warrantyDTO.setBuildTime			( data[2] == null ? "" : data[2].toString()  );
			//just to send number line without zero. the line number format is 01 and warranty just need the 1
			warrantyDTO.setLineNumber			( data[3] == null ? "" : data[3].toString().length() > 1 ?  data[3].toString().substring(1,2) : data[3].toString());
			warrantyDTO.setPlantCode			( data[4] == null ? "" : data[4].toString()  );
			warrantyDTO.setShift				( data[5] == null ? "" : data[5].toString()  );
			warrantyDTO.setTeamCd				( data[6] == null ? "" : data[6].toString()  );
			warrantyDTO.setTypeCd				( data[7] == null ? "" : data[7].toString()  );
			warrantyDTO.setCastLotNumber		( data[8] == null ? "" : data[8].toString()  );
			warrantyDTO.setMachLotNumber		( data[9] == null ? "" : data[9].toString()  );
			warrantyDTO.setTorqueCastLotNumber	( data[10] == null ? "" : data[10].toString() );
			warrantyDTO.setTorqueMachLotNumber	( data[11] == null ? "" : data[11].toString() );
			warrantyDTO.setKdLotNumber			( data[12] == null ? "" : data[12].toString() );
			warrantyDTO.setProdLotKd			( data[13] == null ? "" : data[13].toString() );
			
			//format data and add to the list with the result formated for each record
			final String record = outputFormatHelper.formatOutput( warrantyDTO, lenghtArray );
			resultFormated.add( record );
		}
		
		//Create a name file
		final String path		= PropertyService.getProperty( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT );
		final String fileName	= new StringBuilder( path )
									.append( File.separator )
									.append( interfaceId )
									.append( "_" )
									.append( OIFConstants.stsf1.format( currentDate ))
									.toString();
				
		try {
			logger.info( "Writing the TM Warranty file: " + fileName );
			OIFFileUtility.writeToFile(resultFormated, fileName);
			
			//Update the date with the current date of last execution.
			ComponentPropertyDao propertyDao = ServiceFactory.getDao ( ComponentPropertyDao.class );
			ComponentProperty lastDateProperty = new ComponentProperty ( getName(), OIF_TM_WAR_LAST_DATE_FILTER, new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( currentDate ) );
			propertyDao.save(lastDateProperty);
			//Send data to MQ
			final String mqftpConfig	= PropertyService.getProperty( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
			MQUtility mqClient 			= new MQUtility(this);
			logger.info( "Sending the MQ message for Transmission Warranty " );
			mqClient.executeMQSendAPI( interfaceId, mqftpConfig, fileName );
			
		} catch (IOException e)
		{
			errorsCollector.error( "Error writing the file " + fileName );
			logger.error( "Error writing the file " + fileName );
		} catch ( MQUtilityException mqx)
		{
			errorsCollector.error	( "Error sending the mq message for Transmission Warranty Interface" + mqx.getMessage() );
			logger.error	( "Error to the mq message for Transmission Warranty Interface" + mqx.getMessage()		);
		}
	}
	
}
