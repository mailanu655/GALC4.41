package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.oif.dto.AsynchProductionReportGIV730DTO;
import com.honda.galc.service.ProductionResultService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;

	/**
	 * 
	 * <h3>AsynchProductionReportGIV730Task.java</h3>
	 * <h3> Class description</h3>
	 * <h4> Description </h4>
	 * <p> Create asynchronous BMPN production report and send to GPCS </p>
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
	 * <TD>Justin Jiang</TD>
	 * <TD>Nov 24, 2014</TD>
	 * <TD>0.1</TD>
	 * <TD>none</TD>
	 * <TD>Initial Version</TD> 
	 * </TR>  
	 *
	 * </TABLE>
	 *    
	 * @version 0.1
	 * @author Justin Jiang
	 * @created Nov 24, 2014
	 */

	public class AsynchProductionReportGIV730Task extends OifTask<Object> implements IEventTaskExecutable {
		private static final String HTTP_SERVICE_URL_PART = "/BaseWeb/HttpServiceHandler";
		private String componentId;
		
		public AsynchProductionReportGIV730Task(String name) {
			super(name);
			componentId = name;
		}

		@Override
		public void execute(Object[] args) {
			// get the current date and time
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat timeFormat = new SimpleDateFormat("HHmmss");
			Timestamp now = propertyDao.getDatabaseTimeStamp(); 
			String dateStr = dateFormat.format(now);
			String timeStr = timeFormat.format(now);
			
			refreshProperties();
			errorsCollector = new OifErrorsCollector(componentId);

			// support multiple lines
			String lines = getProperty(OIFConstants.ACTIVE_LINES);
			if(StringUtils.isBlank(lines)) {
				logger.error("Needed configuration is missing [active lines: " + lines +"]");
				errorsCollector.error("Needed configuration is missing [active lines: " + lines +"]");
				setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
				return;
			}
			
			String[] activeLines = lines.split(",");

			List<AsynchProductionReportGIV730DTO> resultList = new ArrayList<AsynchProductionReportGIV730DTO>();
			
			Timestamp startTime = getStartTime();
			Timestamp endTime = getEndTimestamp();  //returns property END_TIMESTAMP if configured
			if(endTime == null)  {
				endTime = now;
			}

			try {
				for(String line : activeLines) {
					
					ProductionResultService service = HttpServiceProvider.getService(line + HTTP_SERVICE_URL_PART, ProductionResultService.class);
					if(service == null) {
						logger.error("Can not access service for Line " + line);
						errorsCollector.error("Can not access service for Line " + line);
						setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
						continue;
					}
					
					Map<String, List<Object[]>> asynchProductionReports = service.getAsynchProductionReport(this.componentId, startTime, endTime);
					
					if(isEmptyResult(asynchProductionReports))  {
						updateLastProcessTimestamp(now);
						logger.info("There is no data for Asynchronous Production Report GIV730.");
						return;
					}
					AsynchProductionReportGIV730DTO dto = null;
					
					Iterator<String> it = asynchProductionReports.keySet().iterator();
					String planCode = "";
					List<Object[]> data = null;
					
	
					//loop through PLAN_CODE
					while (it.hasNext()) {
						planCode = it.next().toString();
						data = asynchProductionReports.get(planCode);
						for (Object[] obj : data){
							String lotNo = (String)obj[1];
							if(StringUtils.isBlank(lotNo))  {
								String errMsg = "Lot No. not found for this record, skipping: " + dto.toString();
								logger.error(errMsg);
								errorsCollector.error(errMsg);
								setOutgoingJobStatusAndFailedCount(1,OifRunStatus.LOT_NO_FOUND, null);
								continue;
							}
							dto = new AsynchProductionReportGIV730DTO();
							dto.setPlanCode(planCode);
							dto.setProductSpecCode(obj[0].toString().substring(0, 29)); //Product_Spec_Code length=30, MBPN+HES_COLOR=29
							int qty = (Integer)obj[2];
							String sQty = String.format("%05d", qty);
							dto.setProductionQuantity(sQty);
							dto.setProductionDate(dateStr);
							dto.setProductionTime(timeStr);
							dto.setLotNumber((String)obj[1]);
							resultList.add(dto);
						}
					}
				}
				
				//format and export data
				exportDataByOutputFormatHelper(AsynchProductionReportGIV730DTO.class, resultList);
				updateLastProcessTimestamp(now);
				logger.info("Finish Asynchronous Production Report GIV730.");		
			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Error processing Asynchronous Production Report GIV730.");
				errorsCollector.error(ex.getCause(), "Error processing Asynchronous Production Report GIV730.");
			} finally {
				errorsCollector.sendEmail();
			}
		}
		
		boolean isEmptyResult(Map<String, List<Object[]>> asynchProductionReports)  {
			
			boolean isEmpty = true;;
			if (asynchProductionReports == null || asynchProductionReports.isEmpty()) {
				return true;
			}
			else {
				Set<String> mapSet = asynchProductionReports.keySet();
				for(String planCode : mapSet)  {
					List<Object[]> data = asynchProductionReports.get(planCode);
					//data is a list of arrays per plan code; each list element is an array
					//each array has product_spec_code and associated count
					for(Object[] specCode : data)  {
						if(specCode != null && specCode.length != 0)  {
							isEmpty = false;
							break;
						}
					}
					if(!isEmpty)  break;
				}
			}
			return isEmpty;
		}
	}
	