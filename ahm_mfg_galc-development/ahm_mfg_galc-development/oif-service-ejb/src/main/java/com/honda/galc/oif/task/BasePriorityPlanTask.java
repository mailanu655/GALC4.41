package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.openjpa.util.OpenJPAException;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.MissionDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.entity.product.Mission;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductStampingSequenceId;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.oif.dto.PreProductionLotDTO;
import com.honda.galc.oif.dto.ProductionLotDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.OIFConstants;

/**
 * 
 * <h3>OifAbstractTask Class description</h3>
 * <p> OifAbstractTask description </p>
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
 * @author Larry Karpov<br>
 * March 31, 2014
 *
 *
 */
/** * * 
* @version 0.2 
* @author Larry Karpov 
* @since March 31, 2014
*/
public class BasePriorityPlanTask<T> extends OifTask<T> {
	
	private static final String MS_OFF_LINE_ID = "MS_OFF_LINE_ID";
	private static final String MS_OFF_PPID = "MS_OFF_PPID";
	//private static final int	MIN_OFF_SET	=	1999999;

	public BasePriorityPlanTask(String name) {
		super(name);
		siteProcessLocations = Arrays.asList(getPropertyArray("PROCESS_LOCATIONS"));
		logger.info("Current process location(s): " + siteProcessLocations);
	}

	protected List<String> siteProcessLocations;

	/**
	 * Gets files from MQ and puts in {@link OifTask#receivedFileList} based on {@link OIFConstants#INTERFACE_ID} property
	 * @return {@code int} number of received files
	 */
	protected int getPriorityPlans() { 
		refreshProperties();
		int filesCount = 0;
		if(StringUtils.isEmpty(siteLineId)) {
			throw new TaskException("Could not find the ASSEMPLY_LINE_ID property");
		}
		logger.info("Current assembly line Id is " + siteLineId);
		
//		Get list of objects created from received file
		receivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		if (receivedFileList != null) {
			filesCount = receivedFileList.length;
		}
		// No file received. Check for temporary files.
		if(filesCount == 0) {
			logger.info("Checking for temporary files...");
			if( getBatchProcessCount() > 0) {
		         receivedFileList = getTempFiles();
		         if (receivedFileList != null) {
		 			filesCount = receivedFileList.length;
		 		}
			}
			logger.info(String.format("%d temporary files found.", filesCount));
		}
		// If no temporary files found either...
		if(filesCount == 0) {
			String interfaceId = getProperty(OIFConstants.INTERFACE_ID);
			logger.info("No temporary files in the incoming MQ queue for interface = " + interfaceId);
			errorsCollector.error("No temporary files received for interface " + interfaceId + ".");
		}
		return filesCount;
	}
	
	protected int getBatchProcessCount() {
		return getPropertyInt(OIFConstants.BATCH_PROCESSING_COUNT,0);
	}
	
	/**
	 * Gets tails for each process location based on {@link BasePriorityPlanTask#siteProcessLocations}
	 * @return {@code Map<String, PreProductionLotDTO>} where key is a Site Process Location and the value is a {@link PreProductionLotDTO} that is the tail
	 * or {@code null} if tails are incorrect
	 */
	protected Map<String, PreProductionLotDTO> getTailsByProcessLocation() { 
		Map<String, PreProductionLotDTO> tailsByLocation = new HashMap<String, PreProductionLotDTO>(); 
		for(String siteProcessLocation : siteProcessLocations) {
			List<PreProductionLot> tails = getDao(PreProductionLotDao.class).getTailsByPlantLineLocation(siteName, siteLineId, siteProcessLocation);
//		 	There should be only one "tail" i.e. a record in PreProduction Plan table
//			with next_production_plan set to null for plantCode, lineNo and processLocation 	
			switch(tails.size()) {
				case 0:
					StringBuffer sb = new StringBuffer("Unable to find the last preproduction lot record with ")
							.append(" PlantCode = ").append(siteName)
							.append("; LineNo = ").append(siteLineId)
							.append("; ProcessLocation = " + siteProcessLocation);
					logger.emergency(sb.toString());
					errorsCollector.emergency(sb.toString());
					return null;
				case 1: 	// OK
					logger.info("Tails are OK for Site Process Location: " + siteProcessLocation);
					break;
				default:
					sb = new StringBuffer("Found more than one production lot with null in the next_production_lot with ")
						.append(" PlantCode = ").append(siteName)
						.append("; LineNo = ").append(siteLineId)
						.append("; ProcessLocation = " + siteProcessLocation);
					logger.emergency(sb.toString());
					errorsCollector.emergency(sb.toString());
					return null;
			};
			PreProductionLotDTO tailDto = new PreProductionLotDTO(tails.get(0));
			logger.info(String.format("Tail is %s for %s Site Process Location.", tailDto.getProductionLot(), siteProcessLocation));
			tailsByLocation.put(siteProcessLocation, tailDto);
		}
		return tailsByLocation;
	}
	
	/**
	 * Creates a new {@link Engine} based on {@link ProductionLot} and {@code String} Product Id. Does not save to table.
	 * @param pl {@link ProductionLot} to create engine from
	 * @param productId {@code String}
	 * @return new {@link Engine}
	 */
	protected Engine createEngine(ProductionLot pl, String productId) {
		 Engine engine = new Engine();
		 engine.setProductId(productId);
		 engine.setProductionLot(pl.getProductionLot());
		 engine.setKdLotNumber(pl.getKdLotNumber());
		 engine.setProductSpecCode(pl.getProductSpecCode());
		 EngineSpec engineSpec = getDao(EngineSpecDao.class).findByKey(pl.getProductSpecCode());
		 engine.setActualMissionType(engineSpec.getActualMissionType());
		 
		 engine.setPlanOffDate(pl.getPlanOffDate());
		 engine.setProductionDate(pl.getProductionDate());
		 engine.setLastPassingProcessPointId(getProperty(MS_OFF_LINE_ID));
		 engine.setTrackingStatus(getProperty(MS_OFF_PPID));
		 return engine;
	}
	
	/**
	 * @see {@link ComponentReleasedAssemblyTask#processFile}
	 * @deprecated This method is not referenced anywhere that Intellisense could find...
	 * @param preProductionLot {@link PreProductionLotDTO} to create
	 * @return {@code Boolean}
	 * @throws TaskException
	 */
	@Deprecated // This method is not referenced anywhere that Intellisense could find...
	protected Boolean insertPreProductionLot(PreProductionLotDTO preProductionLot) throws TaskException
	{
		PreProductionLotDao preProductionLotDao	= ServiceFactory.getDao( PreProductionLotDao.class );
		Boolean isScheduleBySequence =  getPropertyBoolean( OIFConstants.IS_SCHEDULE_BY_SEQUENCE , false );
		//validate the sequence type (next_production_lot or sequence)
		if( !isScheduleBySequence )
		{
			try
			{
				//the schedule sequence is using the NEXT_PRODUCTION_LOT
				List<PreProductionLot> tailPreProducitonLot = preProductionLotDao.getTailsByPlantLineLocation( preProductionLot.getPlantCode(), preProductionLot.getLineNo(), preProductionLot.getProcessLocation() );
				if ( tailPreProducitonLot.size() != 1 )
				{
					final String messageError = tailPreProducitonLot.size() == 0
									? "Find no last pre production lot with NULL in the NEXT_PRODUCTION_LOT. No plan schedule will be created "
									: "Find one than one pre production lot in GAL212TBX with NULL in the NEXT_PRODUCTION_LOT. No production lot will be created" ;
					logger.error( messageError );
					errorsCollector.error( messageError );
					return Boolean.FALSE;
				}
				PreProductionLot lastPreProductionLot = tailPreProducitonLot.get( 0 );
				lastPreProductionLot.setNextProductionLot( preProductionLot.getProductionLot() );
				//update to the last lot, the next production lot.
				preProductionLotDao.update( lastPreProductionLot );
				//create the new preproduction lot
				PreProductionLot newPreProductionLot = preProductionLot.derivePreProductionLot();
				preProductionLotDao.save( newPreProductionLot );
			} catch ( OpenJPAException px )
			{
				logger.error( "Error creating the pre production lot using next_production_lot sequence" + px.getMessage() );
				errorsCollector.error( "Error creating the pre production lot using next_production_lot sequence" + px.getMessage() ); 
				return Boolean.FALSE;
			}
		}
		else
		{
			try
			{
				//load the schedule client using the SEQUENCE
				PreProductionLot newLot = preProductionLot.derivePreProductionLot();
				//get the maximum sequence.
				Double lastSequence = preProductionLotDao.findMaxSequence( preProductionLot.getPlanCode() );
				if ( lastSequence == null )
				{
					lastSequence	=	new Double(0d);
				}
				//increase the sequence by 1
				newLot.setSequence( lastSequence + 1d );
				//save the new pre production lot with the new sequence.
				preProductionLotDao.save( newLot );
			} catch ( OpenJPAException px )
			{
				logger.error( "Error creating the pre production lot using the sequence at GAL212TBX " 
							+ preProductionLot.getProductionLot() + " msg: " + px.getMessage() );
				errorsCollector.error( "Error creating the pre production lot using the sequence at GAL212TBX "
							+ preProductionLot.getProductionLot() + " msg: " + px.getMessage() ); 
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
	
	/**
	 * @see {@link ComponentReleasedAssemblyTask#processFile}
	 * @deprecated This method is not referenced anywhere that Intellisense could find...
	 * @param productionLotDTO {@link ProductionLotDTO} to create
	 * @return {@code Boolean}
	 */
	@Deprecated // This method is not referenced anywhere that Intellisense could find...
	protected Boolean insertProductionLot(final ProductionLotDTO productionLotDTO)
	{
		try
		{
			ProductionLotDao productionLotDao	=	ServiceFactory.getDao( ProductionLotDao.class );
			ProductionLot productionLot			=	productionLotDTO.deriveProductionLot();
			//create or update the production lot.
			productionLotDao.save( productionLot );
		} catch ( OpenJPAException px )
		{
			logger.error( "Error creating the production lot at GAL217TBX " + productionLotDTO.getProductionLot() );
			errorsCollector.error( px.getCause(), "Error creating the production lot at GAL217TBX " + productionLotDTO.getProductionLot() );
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Creates {@link Mission}
	 * @param productionLotDTO {@link ProductionLotDTO} to create {@link Mission} from
	 */
	protected void insertTransmissions(final ProductionLotDTO productionLotDTO)
	{
		MissionDao 					missionDao			= ServiceFactory.getDao( MissionDao.class );
		ProductStampingSequenceDao productStampingDao	=	ServiceFactory.getDao( ProductStampingSequenceDao.class );
		//get the start product id
		final String startProduct		= productionLotDTO.getStartProductId ();
		final Integer	lotSize			= productionLotDTO.getLotSize ();
		//get the product id base data
		final String	productIdBase	= startProduct.substring ( 0, 5 );
		//get the base sequence to start the sequence calculation
		final Integer sequenceBase	= Integer.valueOf( startProduct.substring( 5, 11 ) );
		//get initialization properties, 
		//the initial process point is the material service off process point
		//the initial line id (tracking status) is the material service 
		final String initialPassingProcessPoint	=	getProperty( MS_OFF_PPID, "" );
		final String initialTrackingStatus		=	getProperty( MS_OFF_LINE_ID, "" );
		for (int i = 0; i < lotSize; i++)
		{
			final Integer sequence	=	sequenceBase + i;
			Mission mission	=	new Mission();
			mission.setProductId( new StringBuilder( productIdBase ).append( String.format( "%06d", sequence )).toString() );
			mission.setProductionLot	( productionLotDTO.getProductionLot () 	);
			mission.setProductSpecCode	( productionLotDTO.getProductSpecCode ());
			mission.setPlanOffDate		( productionLotDTO.getPlanOffDate ()	);
			mission.setKdLotNumber		( productionLotDTO.getKdLotNumber ()	);
			mission.setProductionDate	( productionLotDTO.getProductionDate ()	);
			mission.setLastPassingProcessPointId	( initialPassingProcessPoint );
			mission.setTrackingStatus	( initialTrackingStatus );
			missionDao.save( mission );
			
			//creating the product stamping sequence (table gal216tbx)
			//create Id
			ProductStampingSequenceId productStampingSequeneId	=	new ProductStampingSequenceId ();
			productStampingSequeneId.setProductID		(	mission.getId()				);
			productStampingSequeneId.setProductionLot	(	mission.getProductionLot()	);
			//create the prdocut sequence
			ProductStampingSequence	productStampingSequence = new ProductStampingSequence ();
			productStampingSequence.setId			(	productStampingSequeneId	);
			productStampingSequence.setSendStatus	(	0	);
			productStampingSequence.setStampingSequenceNumber( i + 1 );
			productStampingDao.save(productStampingSequence);
			
		}
	}
}