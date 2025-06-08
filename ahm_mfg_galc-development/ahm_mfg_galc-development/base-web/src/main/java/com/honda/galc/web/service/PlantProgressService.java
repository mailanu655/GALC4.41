package com.honda.galc.web.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCOpEfficiencyHistoryDao;
import com.honda.galc.dao.conf.PushTimerStatusDao;
import com.honda.galc.dao.oif.SalesOrderFifDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.dto.FrameInfoDto;
import com.honda.galc.dto.ProcessPointEfficiencyDto;
import com.honda.galc.dto.PushTimerStatusDto;
import com.honda.galc.entity.conf.MCOpEfficiencyHistory;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.service.FactoryNewsUpdateService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.OpEfficiencyUtil;

/**
 * @author Subu Kathiresan
 * @date Aug 5, 2015
 */
@WebService(serviceName = "PlantProgressService")
public class PlantProgressService {

	private static final String LOGGER_ID = "PlantProgressService";

	public PlantProgressService() {}

	/**
	 * Returns the push timer status for all process points in the plant
	 * 
	 * @param plantName
	 * @return
	 */
	public List<PushTimerStatusDto> getProgress(@WebParam(name="plantName") String plantName) {
		getLogger().info("Received WebService request getProgress(" + plantName + ")");

		List<PushTimerStatusDto> statusList = null;
		try {
			statusList = ServiceFactory.getDao(PushTimerStatusDao.class).getPlantProgress(plantName);
			getLogger().info("Replied to WebService request getProgress(" + plantName + "): " + statusList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getProgress(" + plantName + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return statusList;
	}

	/**
	 * Returns only the top process points that are lagging for 
	 * the provided plant and department
	 * 
	 * @param plantName
	 * @param department
	 * @param howMany
	 * @return
	 */
	public List<ProcessPointEfficiencyDto> getTopLaggingProcessPoints(@WebParam(name="plantName") String plantName, @WebParam(name="department")String department, @WebParam(name="howMany")int howMany) {
		getLogger().info("Received WebService request getTopLaggingProcessPoints(" + plantName + ", " + department + ", " + howMany + ")");
		MCOpEfficiencyHistoryDao opEffHistDao = null;
		List<ProcessPointEfficiencyDto> ppEffDtoList = new ArrayList<ProcessPointEfficiencyDto>();;
		try {
			opEffHistDao = ServiceFactory.getDao(MCOpEfficiencyHistoryDao.class);
			//Getting list of latest completed products and process points
			List<MCOpEfficiencyHistory> latestCompletedOpEffList = opEffHistDao.getLatestCompleted(plantName, department);
			//Calculating (Actual-Expected) time
			for(MCOpEfficiencyHistory opEffHist: latestCompletedOpEffList) {
				ProcessPointEfficiencyDto ppEffDto = new ProcessPointEfficiencyDto();
				ppEffDto.setProcessPointId(opEffHist.getProcessPointId());
				ppEffDto.setTerminalId(opEffHist.getHostName());
				ppEffDto.setProcessPointEfficiency(opEffHistDao.getEfficiencyInSeconds(opEffHist.getProductId(), opEffHist.getProcessPointId(), opEffHist.getHostName()));
				ppEffDtoList.add(ppEffDto);
			}
			//Sorting records in descending order
			Collections.sort(ppEffDtoList, new Comparator<ProcessPointEfficiencyDto>() {
				public int compare(ProcessPointEfficiencyDto e1, ProcessPointEfficiencyDto e2) {
					return Double.compare(e2.getProcessPointEfficiency(), e1.getProcessPointEfficiency());
				}
			});
			//Slicing list with respect to 'howMany'
			if(howMany < ppEffDtoList.size()) {
				ppEffDtoList = ppEffDtoList.subList(0, howMany);
			}
			getLogger().info("Replied to WebService request getTopLaggingProcessPoints(" + plantName + ", " + department + ", " + howMany + "): " + ppEffDtoList);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getTopLaggingProcessPoints(" + plantName + ", " 
					+ department + ", "	
					+ howMany + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return ppEffDtoList;
	}

	/**
	 * Returns the current inventory for the requested line and plant
	 * 
	 * @param lineId
	 * @param plantName
	 * @return
	 */
	public int getCurrentInventory(@WebParam(name="lineId") String lineId, @WebParam(name="plantName") String plantName) {
		getLogger().info("Received WebService request getCurrentInventory(" + lineId + ", " + plantName + ")");

		int inventory = -1;
		try {
			inventory = ServiceFactory.getService(FactoryNewsUpdateService.class).getCurrentInventory(lineId, plantName);
			getLogger().info("Replied to WebService request getCurrentInventory(" + lineId + ", " + plantName + "): " + inventory);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getCurrentInventory(" + lineId + ", " 
					+ plantName + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return inventory;
	}

	/**
	 * Returns the aged inventory between two process points and age
	 * 
	 * @param processPointIdOne
	 * @param processPointIdTwo
	 * @param ageInMins
	 * @return
	 */
	public int getAgedInventory(@WebParam(name="processPointIdOne") String processPointIdOne, 
			@WebParam(name="processPointIdTwo") String processPointIdTwo, 
			@WebParam(name="ageInMins") int ageInMins) {
		getLogger().info("Received WebService request getAgedInventory(" + processPointIdOne + ", " + processPointIdTwo + ", " + ageInMins + ")");

		int inventory = -1;
		try {
			inventory = ServiceFactory.getService(ProductResultDao.class).getAgedInventoryCount(processPointIdOne, processPointIdTwo, ageInMins);
			getLogger().info("Replied to WebService request getAgedInventory(" + processPointIdOne 
					+ ", " + processPointIdTwo + ", " + ageInMins + "): " + inventory);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getAgedInventory(" + processPointIdOne 
					+ ", " + processPointIdTwo + ", " + ageInMins + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return inventory;
	}

	/**
	 * Returns information about the frame at the current terminal & process point
	 * 
	 * @param processPointId
	 * @param terminalId
	 * @return
	 */
	public FrameInfoDto getFrameInfo(@WebParam(name="processPointId") String processPointId) {
		getLogger().info("Received WebService request getFrameInfo(" + processPointId + ")");

		try {
			FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
			List<Frame> lastProcessedFrames = frameDao.findAllLastProcessed(processPointId, 1);

			Frame frame = frameDao.findByKey(lastProcessedFrames.iterator().next().getProductId());
			FrameSpec frameSpec = ServiceFactory.getDao(FrameSpecDao.class).findByProductSpecCode(frame.getProductSpecCode(), ProductType.FRAME.toString());
			String fifCodes = ServiceFactory.getDao(SalesOrderFifDao.class).getFIFCodeByProductSpec(frameSpec);
			FrameInfoDto frameInfoDto = new FrameInfoDto(frame.getProductId(), frame.getProductSpecCode(), fifCodes);
			getLogger().info("Replied to WebService request getFrameInfo(" + processPointId + "): " + frameInfoDto);
			return frameInfoDto;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getFrameInfo(" + processPointId + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return new FrameInfoDto();
	}

	/**
	 * Returns the efficiency
	 * @param plantName
	 * @param department
	 * @param efficiencyFactor
	 * @return
	 */
	public double getPerformanceGuage(@WebParam(name="plantName") String plantName, @WebParam(name="department")String department, @WebParam(name="efficiencyFactor")float efficiencyFactor) {
		getLogger().info("Received WebService request getPerformanceGuage(" + plantName + ", " + department + ", " + efficiencyFactor + ")");
		double totalEfficiency = 0.0;
		MCOpEfficiencyHistoryDao opEffHistDao = null;
		DailyDepartmentScheduleDao dailyDeptScheduleDao = null;
		try {
			opEffHistDao = ServiceFactory.getDao(MCOpEfficiencyHistoryDao.class);
			dailyDeptScheduleDao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
			//Get total time excluding non work time
			List<DailyDepartmentSchedule> nonWorkSchedule = dailyDeptScheduleDao.findAllByDivisionAndPlan(department, "N", new java.sql.Date(System.currentTimeMillis()));
			double totalWorkTime = OpEfficiencyUtil.getTotalTimeInSeconds(nonWorkSchedule);
			//Get the total unit time for completed units
			List<ProcessPointEfficiencyDto> ppEffDtoList = opEffHistDao.getCompletedUnitTotalTime(plantName, department);
			//Getting process point data
			int noOfProcessPoints = 0;
			double completedUnitTotalTime = 0.0;
			for (ProcessPointEfficiencyDto ppEffDto: ppEffDtoList) {
				noOfProcessPoints++;
				completedUnitTotalTime += ppEffDto.getUnitTime();
			}
			totalWorkTime = totalWorkTime * noOfProcessPoints;
			//Efficiency Calculation (Total Completed Unit Time)/(Total Work Time)*100
			if(totalWorkTime > 0.0) {
				//Consider factor
				if(efficiencyFactor > 0.0) {
					completedUnitTotalTime = completedUnitTotalTime/efficiencyFactor;
				}
				totalEfficiency = (completedUnitTotalTime/totalWorkTime)*100;
			}
			getLogger().info("Replied to WebService request getPerformanceGuage(" + plantName + ", " + department + ", " + efficiencyFactor + "): " + totalEfficiency);
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error(ex, "Unable to handle WebService request getPerformanceGuage(" + plantName + ", " + department + ", " + efficiencyFactor + "): " 
					+ StringUtils.trimToEmpty(ex.getMessage()));
		}
		return totalEfficiency;
	}

	private Logger getLogger(){
		return Logger.getLogger(LOGGER_ID);
	}
}
