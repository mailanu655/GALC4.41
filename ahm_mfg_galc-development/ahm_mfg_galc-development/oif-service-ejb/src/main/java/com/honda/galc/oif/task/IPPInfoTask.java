package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.product.IPPTagDao;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.DtoUtil;
import com.honda.galc.dto.oif.IPPTagDTO;
import com.honda.galc.entity.enumtype.IPPType;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.IPPInfoDTO;
import com.honda.galc.oif.property.OifTaskPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
/**
 * 
 * <h3>IPPInfoTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>Used to record IPP tag scans to track the first unit to use a new or redesigned part. IPP = Initial Part Product.</p>
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
 * <TR>
 * <TD>KG</TD>
 * <TD>Dec 31, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Dec 31, 2014
 */
public class IPPInfoTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final Logger logger = Logger.getLogger();
	//private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private static final String PROPERTY_ACTIVE_LINE_URL = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','
	
	private String mQConfig; 
	private String plantCode;
	private String lineNo;
	private String ippType;
	private String interfaceId;
	private String userIDConfig;
	private Boolean japanVINLeftJustified;
	private SimpleDateFormat formatter; 
	private Map<String,String> ippScannedOverrideMap;

	//properties name constants
	private static final String LAST_EXECUTION_DATE	=	"LAST_EXECUTION_DATE";
	private static final String PROCESS_POINT_OFF	=	ApplicationConstants.PROCESS_POINT_OFF_SHORT;
	private static final String IPP_TYPE			=	"IPP_TYPE";
	private static final String USER_ID				=	"USER_ID_CONFIG";
	private static final String PLANT_CODE			=	"PLANT_CODE";
	private static final int	LINE_LENGTH			=	300;
	private static final String PRODUCT_TYPE		=	"PRODUCT_TYPE";
	
	public IPPInfoTask(String name) {
		super(name);
	}

	//@Override
	public void execute(Object[] args)
	{
	
		//validar properties para decidir que ejecutar.
		try
		{
			//if the new properties exist, should be run the new ipp tag proces
			//this is able to process for all the products (frame and transmissions)
			String lastExecutionDate	=	getProperty( LAST_EXECUTION_DATE );
			String ppOff				=	getProperty( PROCESS_POINT_OFF );
			ippScannedOverrideMap = PropertyService.getPropertyBean(OifTaskPropertyBean.class, getName()).getIppScannedOverride();
			if (lastExecutionDate == null && ppOff == null)
			{
				//use the current implementation to avoid affect the process
				processIppTagFrame();
			}
			else
			{
				//execute the IPP tag process for frame and transmissions
				processIPPTag();
			}
			updateLastProcessTimestamp();
		} catch ( Exception ex ) {
			String msg = "Error in the IPP Tag process interface " + ex.getMessage();
			logger.error( msg );
			errorsCollector.error( msg );
		}finally
		{
			errorsCollector.sendEmail();
		}
	}
	
	private void processIppTagFrame()
	{
		//initialize the properties
		mQConfig	= getProperty("MQ_CONFIG");
		plantCode	= getProperty( PLANT_CODE );
		lineNo		= getProperty("LINE_NUMBER");
		ippType		= getProperty( IPP_TYPE );
		interfaceId	= getProperty( OIFConstants.INTERFACE_ID );
		userIDConfig = getProperty( USER_ID );
		japanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, true);
		formatter	= new SimpleDateFormat("yyyyMMdd");
		
		// initialize configuration
		String lines = getProperty(PROPERTY_ACTIVE_LINE_URL);	
		
		//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
		if(StringUtils.isBlank(lines)) {
			logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		}
		
		//retrieve and merge data
		String[] activeLines = lines.split(",");
		HashMap<String, IPPInfoDTO> resultMap = new LinkedHashMap<String, IPPInfoDTO>();
		for(String line : activeLines) {
			IPPTagDao ippDao = HttpServiceProvider.getService(line + OIFConstants.HTTP_SERVICE_URL_PART,IPPTagDao.class);
			
			if(ippDao==null) {
				logger.error("Can not access Service DAO for Line " + line + ", move to next Line");
				setJobStatus(OifRunStatus.COMPLETE_WITH_ERRORS);
				continue;
			}
			List<Object[]> resultSetList = ippDao.getIPPInfo();
			
			//Trying to fix Null pointer exception
			IPPInfoDTO dto = new IPPInfoDTO();
			if (resultSetList!=null) {
				for(int i=0; i<resultSetList.size(); i++) {
					Object[] lineObj = resultSetList.get(i);
					String key = lineObj[1].toString();
					
					dto.setPlantCode(plantCode);
					dto.setLineNumber(lineNo);
					dto.setScanned(lineObj[0].toString());
					
					//if ipp tag override is configured then use property value to override
					if(ippScannedOverrideMap != null && ippScannedOverrideMap.keySet().contains(lineObj[0].toString()))
						dto.setScanned(ippScannedOverrideMap.get(lineObj[0].toString()));
					dto.setPsnNumber(ProductNumberDef.justifyJapaneseVIN(lineObj[1].toString(), japanVINLeftJustified.booleanValue()));
					dto.setIppTagNumber(lineObj[2].toString());
					dto.setBusinessDate(lineObj[3].toString());
					dto.setProdOrderLotNumber(lineObj[4].toString());
					dto.setEosLotNumber(lineObj[5].toString());
					dto.setMtcModel(lineObj[6].toString());
					dto.setMtcType(lineObj[7].toString());
					dto.setMtcOption(lineObj[8].toString());
					dto.setMtcColor(lineObj[9].toString());
					dto.setMtcIntColor(lineObj[10].toString());
					dto.setEinNumber(lineObj[11].toString());
					dto.setAssySeqNo(lineObj[12].toString());
					dto.setProcessTSTP(lineObj[13].toString());
					dto.setPsnNoCalc(lineObj[14].toString());
					dto.setLotNumber(" ");
					dto.setUserID(userIDConfig);
					dto.setCreateDTTS(lineObj[15].toString());
					dto.setIppType(ippType);
					dto.setFiller(" ");
	
					if(!resultMap.containsKey(key)) {
						resultMap.put(key, dto);
						dto = new IPPInfoDTO();
						
					}
				}
			}
		}
		
		//export data
		exportDataByOutputFormatHelper(IPPInfoDTO.class, new ArrayList<IPPInfoDTO>(resultMap.values()));
		logger.info("Finished IPP Information Task");
		
	}

	//Method to execute IPP tag for parts products
	private void processIPPTag()
	{
		try
		{
			logger.info( "Step In the IPP task for Product Parts" );
			ippType			=	getProperty( IPP_TYPE );
			interfaceId 	=	getProperty( OIFConstants.INTERFACE_ID );
			userIDConfig	=	getProperty( USER_ID );
			japanVINLeftJustified	=	getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, true);
			plantCode		=	getProperty( PLANT_CODE );
			
			
			Calendar now = GregorianCalendar.getInstance();
			Timestamp nowTs = new Timestamp(now.getTimeInMillis());
			
			Timestamp startTs = null, endTs = null;
			//get the configured start timestamp, if not set/null then get the component last run timestamp
			startTs = getPropertyTimestamp("START_TIMESTAMP");
			if(startTs == null)  {
				startTs = getLastProcessTimestamp(); //component status
			}
			//if startTs still cannot be found, set it to start of current day/midnight
			if(startTs == null)  {
				startTs = getStartOfToday();
			}
			//get the configured start timestamp, if not set/null then default to now
			endTs = getPropertyTimestamp("END_TIMESTAMP");
			if(endTs == null)  {
				endTs = nowTs;
			}
			//if the start > end, then reset to defaults
			if(startTs.after(endTs))  {
				startTs = getStartOfToday();
				endTs = nowTs;
			}
			//the end date is the last timestamp for the range to get the data from DB
			final String startDate = startTs.toString();
			final String	endDate				=	endTs.toString();
			final String	processPointOff		=	getProperty( PROCESS_POINT_OFF );
			
			String linesUrl = getProperty(PROPERTY_ACTIVE_LINE_URL);
			if ( StringUtils.isEmpty( linesUrl ) )
			{
				logger.error("Needed configuration is missing [" + "Counting  active lines: " + linesUrl +"]");
				errorsCollector.emergency( "Needed configuration is missing [" + "Counting  active lines: " + linesUrl +"]" );
				setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
				return;
			}
			
			String[] activeLinesUrl	=	linesUrl.split( "," );
			logger.info( "Getting data from the GALC Lines...  " );
			List<String> result	=	null;
			Map<String, String> layout = new HashMap<String, String>();
			layout = getMapLayout();
			IPPType enumType = IPPType.LINE_SCAN;
			if(ippType != null && ippType.trim().equalsIgnoreCase(IPPType.LINE_MTOIC.getName()))  {
				enumType = IPPType.LINE_MTOIC;
			}
			for (String activeLine : activeLinesUrl)
			{
				IPPTagDao ippDao	= HttpServiceProvider.getService( activeLine + OIFConstants.HTTP_SERVICE_URL_PART, IPPTagDao.class );
				if( ippDao == null )
				{
					logger.error( "Can not access Service DAO for Line: " + activeLine + ", move to next Line" );
					errorsCollector.error( "Can not access Service DAO for Line: " + activeLine + ", move to next Line" );
					setJobStatus(OifRunStatus.COMPLETE_WITH_ERRORS);
					continue;
				}
				List<IPPTagDTO> ippTagInfoList	=	null;
				if(enumType.equals(IPPType.LINE_SCAN))  {
					if(StringUtils.equalsIgnoreCase(ProductType.MBPN.name(), getProperty(PRODUCT_TYPE))){
						ippTagInfoList = ippDao.getIPPIPUInfo( startDate, endDate );
					} else {
						ippTagInfoList = ippDao.getIPPInfo( startDate, endDate );
					}
				}
				else  {
					ippTagInfoList	=	ippDao.getFirstForEachLot(startDate, endDate, processPointOff );					
				}
				//apply the japan vin justfy
				for (IPPTagDTO ippObject : ippTagInfoList)
				{
					ippObject.setPsnNumber	( ProductNumberDef.justifyJapaneseVIN( ippObject.getPsnNumber(), japanVINLeftJustified.booleanValue()) );
					ippObject.setPlantCode	( plantCode );
					ippObject.setUserID		( userIDConfig );
					ippObject.setIppType	( ippType );
					ippObject.setFiller		( "" );
					String startProductId = ippObject.getPsnNoCalc();
					String vin = "";
					if(startProductId != null && !"".equals(startProductId.trim()))  {
						vin = startProductId.trim();
						if(vin.length() > 9 && vin.charAt(8) == '*')  {
							char checkDigit = ProductDigitCheckUtil.calculateVinCheckDigit(vin);
							vin = vin.replace('*', checkDigit);
						}
					}
					ippObject.setPsnNoCalc(vin);
					if(ippScannedOverrideMap != null && ippScannedOverrideMap.keySet().contains(ippObject.getScanned()))
							ippObject.setScanned(ippScannedOverrideMap.get(ippObject.getScanned()));
				}
				if ( result == null )
				{
					result = new ArrayList<String>();
				}
				result.addAll( DtoUtil.output( IPPTagDTO.class, ippTagInfoList, layout, getPropertyInt( OIFConstants.MESSAGE_LINE_LENGTH, LINE_LENGTH ) ) );
			}
			if ( result != null && result.size() > 0)
			{
				logger.info( "Processing " + result.size() + " IPP Tags record " );
				logger.info( "Sending to MQ... " );
				final String path				=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.SEND );
				final String mqConfig			=	PropertyService.getProperty	( OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG );
				final String fileName			=	new StringBuilder( interfaceId )
													.append("_")
													.append( OIFConstants.stsf1.format( new Date() ))
													.toString();
				this.sendData ( result, null, null, path, fileName, mqConfig );
			}
			else {
				logger.info( "No IPP Tag to process..." );
			}
			
			logger.info( "Updating component status LAST_PROCESS_TIMESTAMP " + " with date " + endDate );
			//Update the date with the current date of last execution.
			logger.info( "Step Out the IPP task for Product Parts" );
			
		} catch ( Exception ex )
		{
			logger.error( "Error in the IPP Tag process interface " + ex.getMessage() );
			errorsCollector.error( "Error in the IPP Tag process interface " + ex.getMessage() );
		}
	}
}
