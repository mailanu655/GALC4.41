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

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.CoreMQDto;
import com.honda.galc.oif.dto.CoreMQHeaderDTO;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
/**
 * 
 * <h3CoreMQTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>Used to analyze market quality data across plants.</p>
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
 * <TD>Jan 9, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Jan 9, 2015
 */
public class CoreMQTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final Logger logger = Logger.getLogger();
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private static final String PROPERTY_ACTIVE_LINE_URL = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','  
	String mQConfig = getProperty("MQ_CONFIG");
	String interfaceId = getProperty("INTERFACE_ID");
	String plantName = getProperty("PLANT_ID");
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, true);
	String currentDate = new Date().toString();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	
	public CoreMQTask(String name) {
		super(name);
	}
	
	@Override
	public void execute(Object[] args) {
		
		// initialize configuration
		String lines = getProperty(PROPERTY_ACTIVE_LINE_URL);	
		String opFormatDefKey = getProperty(OIFConstants.PARSE_LINE_DEFS);
		Calendar now = GregorianCalendar.getInstance();
		Timestamp nowTs = new Timestamp(now.getTimeInMillis());
		//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
		if(StringUtils.isBlank(lines)) {
			logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
			setIncomingJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		}
		
		//retrieve and merge data
		String[] activeLines = lines.split(",");
		HashMap<String, CoreMQDto> resultMap = new LinkedHashMap<String, CoreMQDto>();
		List<CoreMQHeaderDTO> headerDtoList = new ArrayList<CoreMQHeaderDTO>();
		List<String> headerRecList = null;
		
		for(String line : activeLines) {
			DefectResultDao coreMQDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,DefectResultDao.class);

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
			List<Object[]> resultSetList = coreMQDao.getAllCoreMQ(startTs, endTs);
			CoreMQDto dto = null;
			
			for(int i=0; resultSetList!=null && i<resultSetList.size(); i++) {
				Object[] lineObj = resultSetList.get(i);
				String key = lineObj[1].toString();
				if(resultMap.containsKey(key)) {
					dto = resultMap.get(key);
				} else {
					dto = new CoreMQDto();
					dto.setVin(ProductNumberDef.justifyJapaneseVIN(lineObj[0].toString(), JapanVINLeftJustified.booleanValue()));
					int defectResultId = (Integer)lineObj[1];
					String sVal = String.format("%015d", defectResultId);
					dto.setReportedDefectID(sVal);
					dto.setEntryTimestamp(lineObj[2].toString());
					dto.setPartName(lineObj[3].toString());
					dto.setPartLocationName(lineObj[4].toString());
					dto.setDefectTypeName(lineObj[5].toString());
					dto.setSecondaryPartName(lineObj[6].toString());
					dto.setSecondaryPartLoc(lineObj[7].toString());
					dto.setResponsiblePlant(plantName);
					dto.setResponsibleDepartment(lineObj[8].toString());
					dto.setFiller(" ");
					
					resultMap.put(key, dto);
				}
			}
			CoreMQHeaderDTO hDto = createHeader(now, resultMap.size());
			headerDtoList.add(hDto);
			headerRecList = createOutputRecords(CoreMQHeaderDTO.class, headerDtoList, opFormatDefKey);
		}
		
		
		StringBuilder sbHeader = new StringBuilder();
		if(headerRecList != null && headerRecList.size() > 0)  {
			sbHeader.append(headerRecList.get(0));
		}
		exportDataByOutputFormatHelper(sbHeader.toString(),CoreMQDto.class, new ArrayList<CoreMQDto>(resultMap.values()),null);
		updateLastProcessTimestamp(nowTs);
		logger.info("Finished Core MQ(Market Quality) Task");
		
	}
	
	public CoreMQHeaderDTO createHeader(Calendar now, int count)  {
		String nowTs = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.000000").format(now.getTime());
		CoreMQHeaderDTO coreHeaderDto = new CoreMQHeaderDTO();
		coreHeaderDto.setHdrDesc("QICS DATA TO COREMQ");
		coreHeaderDto.setHdrPlant(plantName);
		String recCount = String.format("%07d", count);
		coreHeaderDto.setTotalRecsCnt(recCount);
		coreHeaderDto.setCreateTimestamp(nowTs);
		return coreHeaderDto;
		
	}

}