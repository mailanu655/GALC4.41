package com.honda.galc.oif.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.VinInVQDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.ProductSpecUtil;
/**
 * 
 * <h3>VinInVqTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>Sends all VINs that have been produced (AF OFF) but not yet shipped to AHM </p>
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
 * <TD>Oct 21, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author KG
 * @created Oct 21, 2014
 */
public class VinInVQTask extends OifTask<Object> implements IEventTaskExecutable {
	private static final Logger logger = Logger.getLogger();
	private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
	private static final String PROPERTY_ACTIVE_LINE_URL = "ACTIVE_LINES_URLS"; //property key for the data sources needed to collect data from, which is separated by ','  
	private static final String DEFAULT_NON_AH_ATTRIBUTE_NAME = "NON-AH";
	String mQConfig = getProperty("MQ_CONFIG");
	String shipperID = getProperty("SHIPPER_ID_QRY");
	String versionNumber = getProperty("VERSION_NUMBER_QRY");
	String trackingStatus = getProperty("TRACKING_STATUS");
	String interfaceId = getProperty("INTERFACE_ID");
	String nonAhAttributeName = getProperty("NON_AH");
	Boolean JapanVINLeftJustified = getPropertyBoolean(JAPAN_VIN_LEFT_JUSTIFIED, false);
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	private BuildAttributeDao _buildAttributeDao = null;

	public VinInVQTask(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		
		// initialize configuration
		String lines = getProperty(PROPERTY_ACTIVE_LINE_URL);	
		
		//check if the needed configurations(report's file name and path, the process point and lines which the data is generated from) are set
		if(StringUtils.isBlank(lines)) {
			logger.error("Needed configuration is missing [" + "Counting  active lines: " + lines +"]");
			setOutgoingJobStatusAndFailedCount(0, OifRunStatus.MISSING_CONFIGURATION, null);
			return;
		}

		// Fetch all NON-AH [Sony] BuildAttribute
		if (nonAhAttributeName == null) {
			nonAhAttributeName = DEFAULT_NON_AH_ATTRIBUTE_NAME;
		}
		List<BuildAttribute> buildAttributeList = getBuildAttributeDao().findAllByAttribute(nonAhAttributeName);
		if (buildAttributeList == null) {
			buildAttributeList = new ArrayList<>();
		}
		
		//retrieve and merge data
		String[] activeLines = lines.split(",");
		HashMap<String, VinInVQDTO> resultMap = new LinkedHashMap<String, VinInVQDTO>();
		for(String line : activeLines) {
			FrameDao frameDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,FrameDao.class);
			DailyDepartmentScheduleDao dailyDepartmentScheduleDao = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART,DailyDepartmentScheduleDao.class);

			if(frameDao==null) {
				logger.error("Can not access Service DAO for Line " + line + ", move to next Line");
				continue;
			}
			List<Object[]> resultSetList = frameDao.getVinInVQ(trackingStatus);
			
			VinInVQDTO dto = null;
			if (resultSetList!=null) {
			for(int i=0; i<resultSetList.size(); i++) {
				Object[] lineObj = resultSetList.get(i);

				boolean isNonAHFrame = isNonAhFrame(buildAttributeList, lineObj[15].toString());
				if (isNonAHFrame) {
					continue;
				}

				String key = lineObj[0].toString();
				try{
					if(lineObj[13] == null){
						logger.error("The ACTUAL_OFF_DATE is null for the PRODUCT_ID: "+key+" , this record will be ignored");
						setFailedCount(1);
						continue;
					}
					Date afOffDate = (Date) lineObj[13];
					Date nextWorkday = dailyDepartmentScheduleDao.getNextProductionDate(afOffDate);	
					
					if(!resultMap.containsKey(key)) {
						dto = new VinInVQDTO();
						dto.setShipperId(shipperID);
						dto.setVersionNumber(versionNumber);
						dto.setVin(ProductNumberDef.justifyJapaneseVIN(lineObj[0].toString(), JapanVINLeftJustified.booleanValue()));
						dto.setCondKdLot(lineObj[1].toString());
						dto.setCondProductionLot(lineObj[2].toString());
						dto.setSalesModelCode(lineObj[3].toString());
						dto.setSalesModelTypeCode(lineObj[4].toString());
						dto.setSalesModelOption(lineObj[5].toString());
						dto.setSalesExtColorCode(lineObj[6].toString());
						dto.setSalesIntColorCode(lineObj[7].toString());
						dto.setManfModelCode(lineObj[8].toString());
						dto.setManfModelTypeCode(lineObj[9].toString());
						dto.setManfModelOptionCode(lineObj[10].toString());
						dto.setManfExtColorCode(lineObj[11].toString());
						dto.setManfIntColorCode(lineObj[12].toString());
						dto.setExpShipDate(nextWorkday.toString().replace("-", ""));
						dto.setLastWorkingDate(lineObj[14].toString().substring(0, 10).replace("-", ""));
						dto.setLastWorkingTime(lineObj[14].toString().substring(11, 19).replace(":", ""));
						dto.setFiller(" ");
						resultMap.put(key, dto);
					}
				}
				catch(NullPointerException e){
					logger.error(e, "Unexpected error on PRODUCT_ID: "+key);
					setOutgoingJobStatusAndFailedCount(1, OifRunStatus.UNEXPECTED_EXCEPTION, null);
				}
				
			}
		}
		
		//export data
		exportDataByOutputFormatHelper(VinInVQDTO.class, new ArrayList<VinInVQDTO>(resultMap.values()));
		logger.info("Finished Vin in VQ Task");
	}}

	protected BuildAttributeDao getBuildAttributeDao() {
		if (_buildAttributeDao == null) {
			_buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return _buildAttributeDao;
	}

	private boolean isNonAhFrame(List<BuildAttribute> buildAttributeList, String frameProductSpecCode) {
		if (buildAttributeList != null) {
			for (BuildAttribute buildAttribute : buildAttributeList) {
				if (frameProductSpecCode != null) {
					boolean matchFound = ProductSpecUtil.matchProductSpec(frameProductSpecCode,
							buildAttribute.getProductSpecCode());
					if (matchFound) {
						if (StringUtils.equalsIgnoreCase("true", buildAttribute.getAttributeValue())) {
							return true;
						}
						break;
					}
				}
			}
		}
		return false;
	}
	
}
