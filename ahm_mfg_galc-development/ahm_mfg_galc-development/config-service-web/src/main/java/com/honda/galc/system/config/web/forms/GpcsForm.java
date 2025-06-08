package com.honda.galc.system.config.web.forms;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionForm;

import com.honda.galc.dto.GpcsDto;

public class GpcsForm extends ActionForm{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String siteName;
	private String plantName;
	private String divisionId;
	private String divisionName;
	private String gpcsPlantCode;
	private String gpcsProcessLocation;
	private String gpcsLineNo;
	private String planCode;
	private String actionType="view";
	
	private boolean initializePage = false;
	private boolean initializeFrame = false;
	private boolean deleteConfirm = false;
	private boolean existingGPCS = false;
	private String buttonName  = null;
	private String selectedSiteName;
	private String selectedPlantName;
	private String selectedDivision;
	private String view;
	
	Map<String,String> siteNameMapping = new LinkedHashMap<String,String>();

	
	Map<String,String> siteNameMap = new LinkedHashMap<String,String>();
	Map<String,String> plantNameMap = new LinkedHashMap<String,String>();
	Map<String,String> divisionIdMap = new LinkedHashMap<String,String>();
	Map<String,String> divisionNameMap = new LinkedHashMap<String,String>();
	Map<String,String> gpcsPlantCodeMap = new LinkedHashMap<String,String>();
	Map<String,String> gpcsProcessLocationMap = new LinkedHashMap<String,String>();
	Map<String,String> gpcsLineNoMap = new LinkedHashMap<String,String>();
	Map<String,String> divisionIdAndMap = new LinkedHashMap<String,String>();
	
	Map<String,List<String>> siteWithPlantNameList = new LinkedHashMap<String,List<String>>();
	Map<String,List<String>> plantWithDivisionNameList  = new LinkedHashMap<String,List<String>>();
	
	private List<GpcsDto> gpcsData;
	private List<String> gpcsView;
   
	public GpcsDto getGPCSData(){
		GpcsDto gPCSData = new GpcsDto();
	     gPCSData.setSiteName(this.getSiteName());
	     gPCSData.setPlantName(this.getPlantName());
	     gPCSData.setDivisionId(this.getDivisionId());
	     gPCSData.setDivisionName(this.getDivisionName());
	     gPCSData.setgPCSPlantCode(this.getgPCSPlantCode());
	     gPCSData.setgPCSProcessLocation(this.getgPCSProcessLocation());
	     gPCSData.setgPCSLineNo(this.getgPCSLineNo());
	     gPCSData.setPlanCode(this.getPlanCode());
	     
	     return gPCSData; 
	}
	
	
	public void populate(GpcsDto gpcs) {
		this.setSiteName(gpcs.getSiteName());
		this.setPlantName(gpcs.getPlantName());
		this.setDivisionId(gpcs.getDivisionId());
		this.setDivisionName(gpcs.getDivisionName());
		this.setgPCSPlantCode(gpcs.getgPCSPlantCode());
		this.setgPCSProcessLocation(gpcs.getgPCSProcessLocation());
		this.setgPCSLineNo(gpcs.getgPCSLineNo());
		this.setPlanCode(gpcs.getPlanCode());
		
	}
	
	public void radioButtonAction() {
	List<String> gpcsActions = new ArrayList<String>();
		 gpcsActions.add("view");
		 gpcsActions.add("add");
		 gpcsActions.add("update");
		 gpcsActions.add("delete");
		 
	}

	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public String getDivisionId() {
		return divisionId;
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getgPCSPlantCode() {
		return gpcsPlantCode;
	}
	public void setgPCSPlantCode(String gPCSPlantCode) {
		this.gpcsPlantCode = gPCSPlantCode;
	}
	public String getgPCSProcessLocation() {
		return gpcsProcessLocation;
	}
	public void setgPCSProcessLocation(String gPCSProcessLocation) {
		this.gpcsProcessLocation = gPCSProcessLocation;
	}
	public String getgPCSLineNo() {
		return gpcsLineNo;
	}
	public void setgPCSLineNo(String gPCSLineNo) {
		this.gpcsLineNo = gPCSLineNo;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}


	public boolean isInitializePage() {
		return initializePage;
	}


	public void setInitializePage(boolean initializePage) {
		this.initializePage = initializePage;
	}


	public boolean isInitializeFrame() {
		return initializeFrame;
	}


	public void setInitializeFrame(boolean initializeFrame) {
		this.initializeFrame = initializeFrame;
	}


	public boolean isDeleteConfirm() {
		return deleteConfirm;
	}


	public void setDeleteConfirm(boolean deleteConfirm) {
		this.deleteConfirm = deleteConfirm;
	}


	public boolean isExistingGPCS() {
		return existingGPCS;
	}


	public void setExistingGPCS(boolean existingGPCS) {
		this.existingGPCS = existingGPCS;
	}
	

	public List<String> getgPCSView() {
		return gpcsView;
	}


	public void setgPCSView(List<String> gPCSView) {
		this.gpcsView = gPCSView;
	}


	public List<GpcsDto> getgPCSData() {
		return gpcsData;
	}


	public void setgPCSData(List<GpcsDto> gPCSData) {
		this.gpcsData = gPCSData;
	}

	public Map<String, String> getSiteNameMap() {
		return siteNameMap;
	}


	public void setSiteNameMap(Map<String, String> siteNameMap) {
		this.siteNameMap = siteNameMap;
	}


	public Map<String, String> getPlantNameMap() {
		return plantNameMap;
	}


	public void setPlantNameMap(Map<String, String> plantNameMap) {
		this.plantNameMap = plantNameMap;
	}


	public Map<String, String> getDivisionNameMap() {
		return divisionNameMap;
	}


	public void setDivisionNameMap(Map<String, String> divisionNameMap) {
		this.divisionNameMap = divisionNameMap;
	}


	public Map<String, String> getGpcsPlantCodeMap() {
		return gpcsPlantCodeMap;
	}


	public void setGpcsPlantCodeMap(Map<String, String> gpcsPlantCodeMap) {
		this.gpcsPlantCodeMap = gpcsPlantCodeMap;
	}


	public Map<String, String> getGpcsProcessLocationMap() {
		return gpcsProcessLocationMap;
	}


	public void setGpcsProcessLocationMap(Map<String, String> gpcsProcessLocationMap) {
		this.gpcsProcessLocationMap = gpcsProcessLocationMap;
	}


	public Map<String, String> getGpcsLineNoMap() {
		return gpcsLineNoMap;
	}


	public void setGpcsLineNoMap(Map<String, String> gpcsLineNoMap) {
		this.gpcsLineNoMap = gpcsLineNoMap;
	}


	public String getButtonName() {
		return buttonName;
	}


	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}


	public String getSelectedSiteName() {
		return selectedSiteName;
	}


	public void setSelectedSiteName(String selectedSiteName) {
		this.selectedSiteName = selectedSiteName;
	}


	public String getSelectedPlantName() {
		return selectedPlantName;
	}


	public void setSelectedPlantName(String selectedPlantName) {
		this.selectedPlantName = selectedPlantName;
	}


	public String getSelectedDivision() {
		return selectedDivision;
	}


	public void setSelectedDivision(String selectedDivision) {
		this.selectedDivision = selectedDivision;
	}


	public String getView() {
		return view;
	}


	public void setView(String view) {
		this.view = view;
	}


	public String getActionType() {
		return actionType;
	}


	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	public Map<String, String> getDivisionIdAndMap() {
		return divisionIdAndMap;
	}


	public void setDivisionIdAndMap(Map<String, String> divisionIdAndMap) {
		this.divisionIdAndMap = divisionIdAndMap;
	}


	public Map<String, List<String>> getSiteWithPlantNameList() {
		return siteWithPlantNameList;
	}


	public void setSiteWithPlantNameList(Map<String, List<String>> siteWithPlantNameList) {
		this.siteWithPlantNameList = siteWithPlantNameList;
	}


	public Map<String, List<String>> getPlantWithDivisionNameList() {
		return plantWithDivisionNameList;
	}


	public void setPlantWithDivisionNameList(Map<String, List<String>> plantWithDivisionNameList) {
		this.plantWithDivisionNameList = plantWithDivisionNameList;
	}
		

}
