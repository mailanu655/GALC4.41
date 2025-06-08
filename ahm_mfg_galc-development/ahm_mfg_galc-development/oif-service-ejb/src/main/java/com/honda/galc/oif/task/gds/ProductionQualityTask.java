package com.honda.galc.oif.task.gds;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.qics.DefectResultDao;
import com.honda.galc.dao.qics.StationResultDao;
import com.honda.galc.dto.DeptDefectResult;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>ProductionQualityTask Class description</h3>
 * <p> ProductionQualityTask description </p>
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
 * @author Jeffray Huang<br>
 * Nov 21, 2012
 *
 *
 */
public class ProductionQualityTask extends AbstractDataCalculationTask{

	public ProductionQualityTask(String name) {
		super(name);
	}

	@Override
	public void processCalculation() {
		fetchCurentDepartmentSchedules();
		processDepartmentRejections();
		processStationResults();
	}
	
	private void processDepartmentRejections() {
		String[] departmentNames = getDepartmentNames();
		
		for(String deptName : departmentNames) {
			List<String> shifts = getShifts(deptName);
			if(!currentSchedules.keySet().contains(deptName)) continue;
			List<DeptDefectResult> deptDefectResults = getDao(DefectResultDao.class).findAllRejectionCounts(getProductionDateFromDds(deptName), deptName);
			Map<String, Integer> totalCount = new LinkedHashMap<String, Integer>();
			for(String shift :shifts) {
				List<DeptDefectResult> shiftResults = findShiftDefectResult(deptDefectResults, deptName, shift);
				String name = deptName + "\\Shift " + shift + "\\NonRoyal Rejections\\Item-";
			
				//send only the current shift is out put
				for(int i = 0; i<getNumberOfRejections();i++) {
					DeptDefectResult result = i >= shiftResults.size() ? null : shiftResults.get(i);
					if(shift.equals(currentSchedules.get(deptName).getId().getShift())){
						updateValue(name + (i + 1) + "\\Part", result == null ? "" : result.getInspectionPartName());
						updateValue(name + (i + 1) + "\\Defect", result == null ? "" : result.getDefectTypeName());
						updateValue(name + (i + 1) + "\\Total", result == null ? 0 : result.getRejectionCount());
					}
					if(result != null) updateTotal(totalCount, result);
				}

			}
			
			//out is sorted by value
			totalCount = CommonUtil.sortMapByValue(totalCount);
			
			
			//out put total for the department
			for(int j = 0; j<getNumberOfRejections();j++){
				List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(totalCount.entrySet());
				Entry<String, Integer> entry = j >=list.size() ? null : list.get(j);
				String[] tokens = entry == null ? null : entry.getKey().split(Delimiter.COLON);
				String name = deptName + "\\NonRoyal Rejections\\Item-";
				updateValue(name + (j + 1) + "\\Part", entry == null? "" : tokens[0]);
				updateValue(name + (j + 1) + "\\Defect",entry == null? "" :  tokens[1]);
				updateValue(name + (j + 1) + "\\Total", entry == null ? 0: entry.getValue());
				
			}
		}
	}
	

	private void updateTotal(Map<String, Integer> totalCount, DeptDefectResult result) {
		String key = result.getInspectionPartName() + Delimiter.COLON + result.getDefectTypeName();
		if(totalCount.keySet().contains(key))
			totalCount.put(key, totalCount.get(key) + result.getRejectionCount());
		else
			totalCount.put(key, result.getRejectionCount());
		
	}

	private void processStationResults() {
		
		
		
		List<List<List<String>>> allAppIds = getAllProcessPoints("APPLICATION_ID_SS");
		
		for(List<List<String>> appIds :allAppIds){
			for(List<String> items :appIds){
				int dailyInspectionCount = 0;
				int dailyOutstandingCount = 0;
				String appName = "";
				Map<String,Integer> inspectionCounts = new LinkedHashMap<String, Integer>();
				Map<String,Integer> outStandingCounts = new LinkedHashMap<String, Integer>();
				String deptName = getDepartmentName(items.get(0));
				Date ddsProductionDate = getProductionDateFromDds(deptName);
				for(String appId :items) {
					String processPointName = getProcessPointName(appId);
					if(StringUtils.isEmpty(appName)) appName = processPointName;
					else appName += "." + processPointName;
					
					List<StationResult> results = getDao(StationResultDao.class).findAllByApplicationIdAndProductionDate(ddsProductionDate, appId);
					for(StationResult result : results) {
						String shift = result.getId().getShift();
						int oldInspectionCount = inspectionCounts.containsKey(shift)? 
								inspectionCounts.get(shift) : 0;
						inspectionCounts.put(shift, oldInspectionCount + result.getProductIdInspect());	
						dailyInspectionCount += result.getProductIdInspect();
						int oldOutstandindCount = outStandingCounts.containsKey(shift)? 
								outStandingCounts.get(shift) : 0;
								outStandingCounts.put(shift, oldOutstandindCount + result.getOutstandingProductId());
						dailyOutstandingCount += result.getOutstandingProductId();
					}
				}
				
				List<String> shifts = getShifts(deptName);
				for(String shift: shifts) {
					int inspectCount = inspectionCounts.containsKey(shift) ? inspectionCounts.get(shift) : 0;
					int outstandingCount = outStandingCounts.containsKey(shift) ? outStandingCounts.get(shift) : 0;
					int ss = calculateStraightShip(inspectCount, outstandingCount);
					updateValue(deptName + "\\Shift " + shift + "\\" + appName + "\\Straight Ship", ss);
				}
				int ss = calculateStraightShip(dailyInspectionCount, dailyOutstandingCount);
				updateValue(deptName + "\\" + appName + "\\Straight Ship", ss);
				
			}
		}
	}
	
	private int calculateStraightShip(int inspectCount, int outstandingCount) {
		int ss = 0;
		if(inspectCount > 0) {
			ss = Long.valueOf(Math.round((inspectCount - outstandingCount) * 100.0 / inspectCount)).intValue();
		}
		return ss;
	}
	

	private List<DeptDefectResult> findShiftDefectResult(List<DeptDefectResult> allResults, String deptName, String shift) {
		List<DeptDefectResult> results = new ArrayList<DeptDefectResult>();
		for(DeptDefectResult item : allResults) {
			if(item.getDivisionId().equals(deptName) && item.getShift().equals(shift))
				results.add(item);
		}
		return results;
	}
	
	private int getNumberOfRejections() {
		return getPropertyInt("NUMBER_REJECTION", 5);
	}
	
	private String[] getDepartmentNames() {
		return getPropertyArray("DEPARTMENT_NAMES");
	}

}
