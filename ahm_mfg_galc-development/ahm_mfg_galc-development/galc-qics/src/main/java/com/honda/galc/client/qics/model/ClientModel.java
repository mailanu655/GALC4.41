package com.honda.galc.client.qics.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.client.entity.manager.DailyDepartmentScheduleUtil;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.qics.DefectDescription;
import com.honda.galc.entity.qics.DefectTypeDescription;
import com.honda.galc.entity.qics.Image;
import com.honda.galc.entity.qics.InspectionModel;
import com.honda.galc.entity.qics.StationResult;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ClientModel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 5, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ClientModel {

	private static final long serialVersionUID = 1L;

	private String clientId;
	private String processPointId;
	private ProcessPoint processPoint;
	private Plant plant;
	private Map<String, ProcessPoint> processPoints;
	private DailyDepartmentScheduleUtil dailyDepartmentScheduleUtil;

	private Map<String, String> associateNumberCache;
	
	private Map<String,Application> applications = new HashMap<String,Application>();

	private StationResult stationResult;

	// cache the inspection model by the model code - key is the model code
	private Map<String, List<InspectionModel>> allInspectionModels = new HashMap<String,List<InspectionModel>>();
	
	// cache all the DefectDescription by defectTypeName - key is the defect type name
	private Map<String, List<DefectDescription>> allDefectDescriptions = new HashMap<String,List<DefectDescription>>();
	
	
	private Map<String,List<DefectTypeDescription>> allDefectTypeDescriptions 
		= new HashMap<String,List<DefectTypeDescription>>();
	
	// cache all qics images - key is defect group name
	private Map<String, Image> images = new HashMap<String, Image>();

	private String dunnageNumber;
	
	private List<BroadcastDestination> dunnagePrinters;

	public ClientModel(String clientId, ProcessPoint processPoint) {
		this.clientId = clientId;
		this.processPointId = processPoint.getProcessPointId();
		this.processPoint = processPoint;
		this.dunnagePrinters = new ArrayList<BroadcastDestination>();
	}
	
	// === get/set === //

	public StationResult getStationResult() {
		return stationResult;
	}

	public void setStationResult(StationResult stationResult) {
		this.stationResult = stationResult;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public List<Division> getDepartments() {
		return plant.getDivisions();
	}
	
	public boolean isDepartmentFullyLoaded() {
		for(Division division : plant.getDivisions()) {
			if(!division.getLines().isEmpty()) return true;
		}
		return false;
	}
	
	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public void setProcessPoint(ProcessPoint processPoint) {
		this.processPoint = processPoint;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Map<String, String> getAssociateNumberCache() {
		return associateNumberCache;
	}

	public void setAssociateNumberCache(Map<String, String> associateNumberCache) {
		this.associateNumberCache = associateNumberCache;
	}

	protected Map<String, Image> getImages() {
		return images;
	}

	public Image getImage(String defectGroupName) {
		Image image = getImages().get(defectGroupName);
		return image;
	}

	public void putImage(String defectGroupName, Image image) {
		getImages().put(defectGroupName, image);
	}

	public void putApplication(Application app) {
		applications.put(app.getApplicationId(), app);
	}
	
	public Application getApplication(String appId) {
		return applications.containsKey(appId) ? 
				applications.get(appId) : null;
	}
	public String getApplicationName(String appId) {
		Application app = getApplication(appId);
		return app == null ? null : app.getApplicationName();
	}
	
	public List<InspectionModel> getInspectionModels(String modelCode) {
		
		return allInspectionModels.get(modelCode);
		
	}
	
	public void putInspectionModels(String modelCode, List<InspectionModel> models) {
	
		allInspectionModels.put(modelCode, models);
		
	}
	
	public List<DefectDescription> getDefectDescriptions(String defectTypeName) {
		
		return allDefectDescriptions.get(defectTypeName);
		
	}
	
	public void putDefectDescriptions(String defectTypeName, List<DefectDescription> defectDescriptions) {
	
		allDefectDescriptions.put(defectTypeName, defectDescriptions);
		
	}
	
	public List<DefectTypeDescription> getDefectTypeDescriptions(String defectGroupName) {
		
		return allDefectTypeDescriptions.get(defectGroupName);
		
	}
	
	public void putDefectTypeDescriptions(String defectGroupName, List<DefectTypeDescription> defectTypeDescriptions) {
	
		allDefectTypeDescriptions.put(defectGroupName, defectTypeDescriptions);
		
	}

	public Map<String, ProcessPoint> getProcessPoints() {
		return processPoints;
	}

	public void setProcessPoints(Map<String, ProcessPoint> processPoints) {
		this.processPoints = processPoints;
	}

	public ProcessPoint getProcessPoint(String processPointId) {
		
		return processPoints == null ? null : processPoints.get(processPointId);
		
	}
	
	public String getProcessPointName(String processPointId) {
		
		ProcessPoint processPoint = getProcessPoint(processPointId);
		return processPoint == null ? null : processPoint.getProcessPointName();
		
	}
	
	public String getDunnageNumber() { 
		return dunnageNumber;
	}

	public void setDunnageNumber(String dunnageNumber) {
		this.dunnageNumber = dunnageNumber;
	}

	public List<BroadcastDestination> getDunnagePrinters() {
		return dunnagePrinters;
	}
	
	public boolean isPrintDunnage() {
		return getDunnagePrinters() != null && !getDunnagePrinters().isEmpty();
	}
	
	public DailyDepartmentSchedule getCurrentSchedule() {
		
		return dailyDepartmentScheduleUtil == null ? null : dailyDepartmentScheduleUtil.getCurrentSchedule();
		
	}
	
	public String getCurrentShiftCode() {
		DailyDepartmentSchedule schedule = getCurrentSchedule();
		return schedule == null ? "" : schedule.getId().getShift();
	}

	public DailyDepartmentScheduleUtil getDailyDepartmentScheduleUtil() {
		return dailyDepartmentScheduleUtil;
	}

	public void setDailyDepartmentScheduleUtil(
			DailyDepartmentScheduleUtil dailyDepartmentScheduleUtil) {
		this.dailyDepartmentScheduleUtil = dailyDepartmentScheduleUtil;
	}

	public Plant getPlant() {
		return plant;
	}

	public void setPlant(Plant plant) {
		this.plant = plant;
	}
	
	public boolean isTrackingRequired() {
		
		return processPoint != null && (processPoint.isTrackingPoint() || processPoint.isPassingCount());
		
	}

	
	
}
