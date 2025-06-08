/**
 * 
 */
package com.honda.galc.client.metrics.monitors;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.honda.galc.dao.conf.MetricDao;
import com.honda.galc.dao.conf.MetricGroupDao;
import com.honda.galc.dao.conf.MetricTypeDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.conf.Metric;
import com.honda.galc.entity.conf.MetricGroup;
import com.honda.galc.entity.conf.MetricGroupType;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * @author Subu Kathiresan
 * Aug 31, 2011
 * @param <S>
 */
public class PrinterMisconfigurationMonitor<S extends MetricGroupType> extends AbstractBaseMetricsMonitor<S> {

	public static final int PRINTER_DEVICE_TYPE_ID = 1;
	public static final int METRIC_GROUP_PRIORITY = 1;
	
	public static final String METRIC_GROUP_LOCATION_LEVEL ="1";
	public static final String TABLE_DATA = "TABLE_DATA";
	public static final String MTOC_FORMLOC = "MTOC FormLoc";
	public static final String MTOC_SEPARATOR = "--";
	
	private String _monitorName = "PrinterMisconfigurationMonitor";
	private Hashtable<String, BuildAttribute> _buildAttribMap = new Hashtable<String, BuildAttribute>();
	private Hashtable<String, Date> _mtocImpactTimeMap = new Hashtable<String, Date>();
	private BigInteger _metricTypeId = new BigInteger("-1");

	/**
	 * starts the print misconfiguration monitoring process
	 */
	@Override
	public void run() {
		setRunning(true);
		setSingleInstanceOnly(true);
		getLogger().info("Executing monitor " + getMonitorName() + " " + getName());
		
		try {
			_metricTypeId = ServiceFactory.getDao(MetricTypeDao.class).findFirstByMetricTypeName(TABLE_DATA).getMetricTypeId();
		} catch(Exception ex) {
			getLogger().warn("Could not retrieve metric type id for " + TABLE_DATA);
		}
		
		createMetricGroupsForMTOCsInScope();
		updateTimeToImpact();
		persistData();
	}

	/**
	 * persists changes to Metric and MetricGroup objects
	 */
	private void persistData() {
		Enumeration<MetricGroup> groupsEnum = getMetricGroupMap().elements();
		while(groupsEnum.hasMoreElements()) {
			ServiceFactory.getDao(MetricGroupDao.class).save(groupsEnum.nextElement());
		}
		
		Enumeration<Metric> metricsEnum = getMetricMap().elements();
		while(metricsEnum.hasMoreElements()) {
			ServiceFactory.getDao(MetricDao.class).save(metricsEnum.nextElement());
		}
	}
	
	/**
	 * Create metric groups and metrics for MTOCs that are in scope but not already created
	 */
	private void createMetricGroupsForMTOCsInScope() {
		for(S groupType: getMetricGroupTypes()) {
			try {
				getLogger().info("MetricGroupType retrieved: " + groupType.getMetricGroupTypeId()+ MTOC_SEPARATOR + groupType.getMetricGroupTypeName());
				addNewMetricGroups(getAllFramesInScope(), groupType);
			} catch(Exception ex) {
				getLogger().warn("Could not add Metric/MetricGroups for MetricGroupType " + groupType.getMetricGroupTypeName());
			}
		}
		getLogger().info("Finished adding MTOCs in scope for missing Metric/MetricGroups");
	}
	
	/**
	 * update time to impact for missing print attributes
	 */
	private void updateTimeToImpact() {
		
		try {
			setBuildAttribMap(ServiceFactory.getDao(BuildAttributeDao.class).findAllMatchPrintAttributes());
			
			Enumeration<MetricGroup> groupsEnum = getMetricGroupMap().elements();
			while(groupsEnum.hasMoreElements()) {
				MetricGroup group = groupsEnum.nextElement();
				if(_buildAttribMap.containsKey(group.getMetricGroupName()))	{			
					updateTime(group, _buildAttribMap.get(group.getMetricGroupName()));
				} else {
					getLogger().info(group.getMetricGroupName() + " does not exist");
					group.setTimeRemainingMsec(getTimeToImpact(_mtocImpactTimeMap.get(getMtocFromGroupName(group.getMetricGroupName()))));	
				} 
			}
		} catch (Exception ex) {
			getLogger().error("Could not update time to impact for missing print attributes");
			getLogger().error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param group
	 * @param bAttrib
	 */
	private void updateTime(MetricGroup group, BuildAttribute bAttrib) {
		if (bAttrib.getAttributeValue() == null || bAttrib.getAttributeValue().trim().equals("")) {
			getLogger().info(group.getMetricGroupName() + " has no attribute value set");
			group.setTimeRemainingMsec(getTimeToImpact(_mtocImpactTimeMap.get(getMtocFromGroupName(group.getMetricGroupName()))));	
			group.setEndTime(null);
		} else if(group.getEndTime() == null) {
			group.setEndTime(new Date());
			getLogger().info(group.getMetricGroupName() + " attribute value has been set");
		}
	}

	/**
	 * Add new Metric/MetricGroups, if needed
	 */
	private void addNewMetricGroups(Hashtable<String, Frame> mtocs, S groupType) {

		List<PrintForm> printForms = new ArrayList<PrintForm>();

		try {
			// get a list of all printForms
			printForms = ServiceFactory.getDao(PrintFormDao.class).findAll();

		} catch (Exception ex) {
			getLogger().error("Could not retrieve print forms");
			getLogger().error(ex.getMessage());
			ex.printStackTrace();
		}		
		
		// if metric group is not found add Metric/MetricGroups to map
		for(PrintForm printForm: printForms) {
			for(Frame frame: mtocs.values()) {
				createNewEntities(groupType, printForm, frame);
			}
		}
	}

	/**
	 * creates and saves new Metric and MetricGroup entities that need to be added
	 * 
	 * @param groupType
	 * @param printForm
	 * @param frame
	 */
	private void createNewEntities(S groupType, PrintForm printForm, Frame frame) {

		String groupKey = "";
		try {
			groupKey = (frame.getProductSpecCode().trim() + MTOC_SEPARATOR + printForm.getId().getFormId().trim()).toUpperCase();
			if (!getMetricGroupMap().containsKey(groupKey)) {
				getLogger().info("Adding metric group: " + groupKey);
				MetricGroup group = createNewMetricGroup(groupType, printForm, frame, groupKey);
				getMetricGroupMap().put(groupKey, group);
				Metric metric = createNewMetric(groupKey);
				getMetricMap().put(groupKey, metric);
			}
		}catch (Exception ex) {
			getLogger().error("Could not create new Metric/MetricGroups for group name " + groupKey);
			getLogger().error(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Create a new Metric to be added for TABLE_DATA monitoring
	 * 
	 * @param groupType
	 * @param groupKey
	 * @return
	 */
	private Metric createNewMetric(String metricKey) {
		
		Metric metric = new Metric();
		metric.setMetricName(metricKey);
		metric.setMetricDesc(MTOC_FORMLOC);
		metric.setMetricTypeId(_metricTypeId);
		metric.setCurrentValue(0);
		metric.setPreviousValue(0);	
		
		return metric;
	}
	
	/**
	 * Create a new MetricGroup to be added for TABLE_DATA monitoring
	 * 
	 * @param groupType
	 * @param printForm
	 * @param frame
	 * @param groupKey
	 * @return
	 */
	private MetricGroup createNewMetricGroup(S groupType, PrintForm printForm, Frame frame, String groupKey) {
		
		MetricGroup group = new MetricGroup();
		group.setMetricGroupName(groupKey);
		group.setDisplayType(TABLE_DATA);
		group.setMetricGroupTypeId(groupType.getMetricGroupTypeId());
		group.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		group.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		group.setLocationLevelId(METRIC_GROUP_LOCATION_LEVEL.toCharArray());
		group.setStartTime(new Date());
		group.setPriority(METRIC_GROUP_PRIORITY);
								
		if (printForm.getDefaultFlag() == -1)
			group.setMetricGroupDescription("MTOC " + frame.getProductSpecCode().trim() + " not configured for formloc " + printForm.getId().getFormId().trim());
		else
			group.setMetricGroupDescription("MTOC " + frame.getProductSpecCode().trim() + " not configured for form " + printForm.getId().getFormId().trim() + " and printer " + printForm.getId().getDestinationId().trim());
		
		return group;
	}
	
	/**
	 * Retrieve all MTOCs that are in scope for the next few production days.
	 */
	private Hashtable<String, Frame> getAllFramesInScope() {
		// get all frames in scope
		Hashtable<String, Frame> framesInScopeMap = new Hashtable<String, Frame>();

		try {
			List<Frame> framesInScope = ServiceFactory.getDao(FrameDao.class).findAll();

			// create a map of unique product spec codes that are in scope
			for(Frame frame: framesInScope) {
				if (!framesInScopeMap.containsKey(frame.getProductSpecCode().trim())) {
					getLogger().info("Frame retrieved: " + frame.getProductSpecCode());
					framesInScopeMap.put(frame.getProductSpecCode().trim(), frame);
					_mtocImpactTimeMap.put(frame.getProductSpecCode().trim(), frame.getPlanOffDate());
				}
			}
		}catch (Exception ex) {
			getLogger().error("Could not retrieve all frames that are in scope for the next few production days");
		}
		return framesInScopeMap;
	}
	
	/**
	 * sets the build attribute map
	 * 
	 * @param buildAttribs
	 * @return
	 */
	private void setBuildAttribMap(List<BuildAttribute> buildAttribs) {
		
		for(BuildAttribute bAttrib: buildAttribs) {
			String key = (bAttrib.getId().getProductSpecCode().trim() + MTOC_SEPARATOR + bAttrib.getId().getAttribute().trim()).toUpperCase();
			if (!_buildAttribMap.containsKey(key)) {
				_buildAttribMap.put(key, bAttrib);
			}
		}
	}
	
	/**
	 * returns the time remaining to impact production
	 * 
	 * @param productionDate
	 * @return
	 */
	private BigInteger getTimeToImpact(Date planOffDate) {
		long milliSecs = Long.MAX_VALUE;
		Date now = new Date();
		
		if (planOffDate != null){
			if (now.after(planOffDate))
				milliSecs = 0;
			else
				milliSecs = planOffDate.getTime() - now.getTime();
		}
		
		return new BigInteger(Long.toString(milliSecs));
	}
	
	/**
	 * parses out the MTOC from MetricGroup name
	 * 
	 * @param groupName
	 * @return
	 */
	private String getMtocFromGroupName(String groupName) {
		try {
			return groupName.substring(0, groupName.indexOf(MTOC_SEPARATOR));
		} catch (IndexOutOfBoundsException ex) {
			getLogger().warn("Could not parse MTOC from MetricGroup name: " + groupName);
		} catch (NullPointerException ex) {
			getLogger().warn("Unable to parse MTOC: MetricGroup name is null");
		}
		return "";
	}
	
	public boolean activate() {
		getLogger().info("Activating " + getMonitorName());
		return true;
	}

	public boolean deActivate() {
		getLogger().info("DeActivating " + getMonitorName());
		return true;
	}
	
	public String getMonitorName() {
		return _monitorName;
	}
	
	public void setMonitorName(String monitorName) {
		_monitorName = monitorName;
	}
}
