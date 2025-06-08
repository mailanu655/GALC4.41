package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.QiCoreMQDto;
import com.honda.galc.oif.dto.QiCoreMQHeaderDTO;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
/**
* 
* <h3QiCoreMQTask.java</h3>
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
* <TD>vcc01417</TD>
* <TD>April 7, 2017</TD>
* <TD>0.1</TD>
* <TD>none</TD>
* <TD>Initial Version</TD> 
* </TR>  
*
* </TABLE>
*    
* @version 0.1
* @author vcc01417
* @created April 7, 2017
*/

public class QiCoreMQTask extends OifTask<Object> implements IEventTaskExecutable {
	
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	String plantName = getProperty("PLANT_ID");
	String lines = getProperty("PROPERTY_ACTIVE_LINE_URL");	
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, true);
	
	public QiCoreMQTask(String name) {
		super(name);
	}
	
	@Override
	public void execute(Object[] args) {
		try {
			processQiCoreMQ();
		} catch (TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		} catch (Exception e) {
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} 
	}

	@SuppressWarnings("unused")
	private void processQiCoreMQ() throws ParseException{
		
				refreshProperties();
				
				String mQConfig = getProperty("MQ_CONFIG");
				String interfaceId = getProperty("INTERFACE_ID");
				String currentDate = new Date().toString();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String opFormatDefKey = getProperty(OIFConstants.PARSE_LINE_DEFS);
				Calendar now = GregorianCalendar.getInstance();
				Timestamp nowTs = new Timestamp(now.getTimeInMillis());
				
				if(StringUtils.isBlank(lines)) {
					logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
					return;
				}
				
				String[] activeLines = lines.split(",");
				HashMap<String, QiCoreMQDto> resultMap = new LinkedHashMap<String, QiCoreMQDto>();
				List<QiCoreMQHeaderDTO> headerDtoList = new ArrayList<QiCoreMQHeaderDTO>();
				List<String> headerRecList = null;
				
				for(String line : activeLines) {
					
					Timestamp startTs = null, endTs = null;
					startTs = getPropertyTimestamp("START_TIMESTAMP");
					if(startTs == null)  {
						startTs = getLastProcessTimestamp(); 
					}
					if(startTs == null)  {
						startTs = getStartOfToday();
					}
					endTs = getPropertyTimestamp("END_TIMESTAMP");
					if(endTs == null)  {
						endTs = nowTs;
					}
					if(startTs.after(endTs))  {
						startTs = getStartOfToday();
						endTs = nowTs;
					}
					
					resultMap = getCoreMQData(startTs, endTs, line);
					QiCoreMQHeaderDTO hDto = createHeader(now, resultMap.size());
					headerDtoList.add(hDto);
					headerRecList = createOutputRecords(QiCoreMQHeaderDTO.class, headerDtoList, opFormatDefKey);
				}
				
				StringBuilder sbHeader = new StringBuilder();
				if(headerRecList != null && headerRecList.size() > 0)  {
					sbHeader.append(headerRecList.get(0));
				}
				exportDataByOutputFormatHelper(sbHeader.toString(),QiCoreMQDto.class, new ArrayList<QiCoreMQDto>(resultMap.values()),null);
				updateLastProcessTimestamp(nowTs);
				logger.info("Successfully executed Core MQ Task");
	}

	private HashMap<String, QiCoreMQDto> getCoreMQData(Timestamp startTs,
			Timestamp endTs, String line) {
		DefectResultDao coreMQDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,DefectResultDao.class);
		List<Object[]> resultSetList = coreMQDao.findAllCoreMQDefectDataByTimestamp(startTs, endTs);
		HashMap<String, QiCoreMQDto> resultMap = new LinkedHashMap<String, QiCoreMQDto>();
		QiCoreMQDto qiCoreMQDto = null;
		
		for(int i=0; resultSetList!=null && i<resultSetList.size(); i++) {
			Object[] lineObj = resultSetList.get(i);
			String key = lineObj[1].toString();
			if(resultMap.containsKey(key)) {
				qiCoreMQDto = resultMap.get(key);
			} else {
				qiCoreMQDto = new QiCoreMQDto();
				qiCoreMQDto.setVin(ProductNumberDef.justifyJapaneseVIN(lineObj[0].toString(), JapanVINLeftJustified.booleanValue()));
				long defectResultId = (Long)lineObj[1];
				String sVal = String.format("%015d", defectResultId);
				qiCoreMQDto.setReportedDefectID(sVal);
				qiCoreMQDto.setEntryTimestamp(lineObj[2].toString());
				qiCoreMQDto.setPartName(lineObj[3].toString());
				qiCoreMQDto.setPartLocationName(lineObj[4].toString());
				qiCoreMQDto.setDefectTypeName(StringUtils.abbreviate(lineObj[5].toString() +" "+ lineObj[6].toString(),50));
				qiCoreMQDto.setSecondaryPartName(StringUtils.abbreviate(lineObj[7].toString(),50));
				qiCoreMQDto.setSecondaryPartLoc(lineObj[8].toString());
				qiCoreMQDto.setResponsiblePlant(StringUtils.abbreviate(plantName,6));
				qiCoreMQDto.setResponsibleDepartment(StringUtils.abbreviate(lineObj[10].toString(),16));
				qiCoreMQDto.setFiller(" ");
				
				resultMap.put(key, qiCoreMQDto);
			}
		}
		return resultMap;
	}
	
	private QiCoreMQHeaderDTO createHeader(Calendar now, int count)  {
		String nowTs = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.000000").format(now.getTime());
		QiCoreMQHeaderDTO qiCoreHeaderDto = new QiCoreMQHeaderDTO();
		qiCoreHeaderDto.setHdrDesc("QICS DATA TO COREMQ");
		qiCoreHeaderDto.setHdrPlant(plantName);
		String recCount = String.format("%07d", count);
		qiCoreHeaderDto.setTotalRecsCnt(recCount);
		qiCoreHeaderDto.setCreateTimestamp(nowTs);
		return qiCoreHeaderDto;
	}
	
	
}
