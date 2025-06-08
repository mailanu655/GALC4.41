package com.honda.galc.handheld.forms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import org.apache.openjpa.persistence.PersistenceException;
import org.apache.struts.action.ActionMapping;
import com.honda.galc.dao.conf.DCZoneDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ZoneDao;
import com.honda.galc.entity.conf.DCZone;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.Zone;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.data.HandheldWebPropertyBean;
import com.honda.galc.handheld.plugin.InitializationPlugIn;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

public class ChooseProcessForm extends ValidatedUserHandheldForm{
	private static final long serialVersionUID = 1L;
	private HashMap<String, HashMap<String, List<ProcessPoint>>> divisionZoneProcessPointMap = new HashMap<String, HashMap<String, List<ProcessPoint>>>();
	private String selectedZone, lastSelectedZone, selectedProcessPointId, selectedDivision, lastSelectedDivision;
	private ProcessPoint selectedProcess;
	private ArrayList<String> availableDivisions = new SortedArrayList<String>();
	private String mbpnOnRequseted;
	
	public String getMbpnOnRequseted() {
		return mbpnOnRequseted;
	}


	public boolean wasMbpnOnRequseted() {
		return mbpnOnRequseted != null && mbpnOnRequseted.equals("true");
	}

	
	public void setMbpnOnRequseted(String mbpnOnRequseted) {
		this.mbpnOnRequseted = mbpnOnRequseted;
	}

	private HandheldWebPropertyBean handheldPropertyBean;
	
	public HandheldWebPropertyBean getHandheldPropertyBean() {
		if(handheldPropertyBean == null)
			handheldPropertyBean = PropertyService.getPropertyBean(HandheldWebPropertyBean.class);
		return handheldPropertyBean;
	}

	public String getLastSelectedDivision() {
		return lastSelectedDivision;
	}

	public void setLastSelectedDivision(String lastSelectedDivision) {
		this.lastSelectedDivision = lastSelectedDivision;
	}
	public void addDivisionZoneProcessPointMapping(String division, String zoneId, ProcessPoint processPoint) {
		HashMap<String, List<ProcessPoint>> zoneProcessMap =  divisionZoneProcessPointMap.get(division);
		if (zoneProcessMap == null) {
			zoneProcessMap = new HashMap<String, List<ProcessPoint>>();
			divisionZoneProcessPointMap.put(division, zoneProcessMap);
		}

		List<ProcessPoint> processPoints =zoneProcessMap.get(zoneId); 
		if(processPoints == null) {
			processPoints = new SortedArrayList<ProcessPoint>("getProcessPointName");
			zoneProcessMap.put(zoneId, processPoints);
		}
		
		if (!processPoints.contains(processPoint))
			processPoints.add(processPoint);
	}
	
	public ArrayList<String> getAvailableDivisions() {
		return availableDivisions;
	}

	public String getSelectedProcessPointId() {
		return selectedProcessPointId;
	}

	public void setSelectedProcessPointId(String selectedProcessPointId) {
		this.selectedProcessPointId = selectedProcessPointId;
		setSelectedProcess(getProcessPointForId(selectedProcessPointId));
	}

	private ProcessPoint getProcessPointForId(String processPointId) {
		for (ProcessPoint eachProcessPoint :getProcessesForSelectedZone()) {
			if (eachProcessPoint.getProcessPointId().equals(processPointId))
				return eachProcessPoint;
		}
		return null;
	}

	public ProcessPoint getSelectedProcess() {
		return selectedProcess;
	}

	public void setSelectedProcess(ProcessPoint selectedProcess) {
		this.selectedProcess = selectedProcess;
	}

	public List<ProcessPoint> getProcessesForSelectedZone() {
		return selectedZone == null || selectedZone.isEmpty()
				? new ArrayList<ProcessPoint>()
				: getProccessNamesInZoneNamed(selectedZone);
	}

	
	public List<ProcessPoint> getProccessNamesInZoneNamed(String zoneName) {
		List<ProcessPoint> result = new ArrayList<ProcessPoint>();
		if (divisionZoneProcessPointMap == null || divisionZoneProcessPointMap.isEmpty())
			return result;
		HashMap< String, List<ProcessPoint>> zoneProcessMap = divisionZoneProcessPointMap.get(getSelectedDivision());
		if (zoneProcessMap.containsKey(zoneName)) {
			for (ProcessPoint eachProcess : zoneProcessMap.get(zoneName)) {
				ProcessPoint newProcessPoint = new ProcessPoint();
				newProcessPoint.setProcessPointName(eachProcess.getProcessPointName());
				newProcessPoint.setProcessPointId(eachProcess.getProcessPointId());
				result.add(newProcessPoint);
			}
		}
		return result;
	}
	
	public ArrayList<String> getAvailableZones() {
		return getZonesForSelectedDivisions();
	}

	public String getSelectedZone() {
		return selectedZone;
	}

	public void setSelectedZone(String selectedZone) {
		this.selectedZone = selectedZone;
	}

	public String getLastSelectedZone() {
		return lastSelectedZone;
	}

	public void setLastSelectedZone(String lastSelectedZone) {
		this.lastSelectedZone = lastSelectedZone;
	}

	protected List<String> getInstalledPartUpdateDivisions() {
		Map<String, String> divisionMap = getHandheldPropertyBean().getProductTypeInstalledPartUpdateDivisionsMap();
		List<String> divisions = new ArrayList<String>();
		for(String eachProductType : divisionMap.values())
			divisions.addAll(Arrays.asList(eachProductType.split(",")));
		return divisions;
	}
	
	@SuppressWarnings("unchecked")
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		if (!(availableDivisions.size() == 0))
			return;
		else 
			availableDivisions = new SortedArrayList<String>();
		
		try {
			// go through each division and collect each zone/process map
			List<String> divisionList = (List<String>)request.getSession().getAttribute(HandheldConstants.DIVISIONS);
			if (divisionList == null)
				request.getSession().setAttribute(HandheldConstants.DIVISIONS, getInstalledPartUpdateDivisions());
				divisionList = (List<String>)request.getSession().getAttribute(HandheldConstants.DIVISIONS);
			
			for (String divisionName : (List<String>)request.getSession().getAttribute(HandheldConstants.DIVISIONS)) {
				List<Zone> zonesForDivision = ServiceFactory.getDao(ZoneDao.class).findAllByDivisionId(divisionName);
				for (Zone eachZone : zonesForDivision) {
					List<ProcessPoint> processPoints = new ArrayList<ProcessPoint>();
					for (DCZone eachDCZone : ServiceFactory.getDao(DCZoneDao.class).findAllRepairableByZoneId(eachZone.getZoneId())) {
						ProcessPoint eachProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findById(eachDCZone.getProcessPointId());
						if (eachProcessPoint != null) {
							addDivisionZoneProcessPointMapping(divisionName, eachZone.getZoneName(), eachProcessPoint);
							processPoints.add(eachProcessPoint);
						}else
							InitializationPlugIn.error(String.format("Zone: %s for: %s is invalid.  There is no process point defined for %s.", eachDCZone.getZoneId(), eachDCZone.getProcessPointId(), eachDCZone.getProcessPointId()));
					}
					if(!processPoints.isEmpty()) {
						if (!availableDivisions.contains(divisionName)) {
							availableDivisions.add(divisionName);
						}
					}
				}
			}
		} catch (PersistenceException e) {
				e.getMessage();
		}
	}

	private ArrayList<String> getZonesForSelectedDivisions(){
		if (StringUtil.isNullOrEmpty(getSelectedDivision()) || divisionZoneProcessPointMap == null || divisionZoneProcessPointMap.isEmpty())
			return new ArrayList<String>();
		return new SortedArrayList<String>(((HashMap<String, List<ProcessPoint>>)divisionZoneProcessPointMap.get(selectedDivision)).keySet());
	}
	
	public String getSelectedDivision() {
		return selectedDivision;
	}

	public void setSelectedDivision(String selectedDivision) {
		this.selectedDivision = selectedDivision;
	}

	public boolean didDivisionSelectionChange() {
		return getSelectedDivision() != null
				&& (getLastSelectedDivision() == null
					|| !getLastSelectedDivision().equals(getSelectedDivision()));
	}
	
	public boolean didZoneSelectionChange() {
		return getSelectedZone() != null
				&& (getLastSelectedZone() == null
					|| !getLastSelectedZone().equals(getSelectedZone()));
	}
}
