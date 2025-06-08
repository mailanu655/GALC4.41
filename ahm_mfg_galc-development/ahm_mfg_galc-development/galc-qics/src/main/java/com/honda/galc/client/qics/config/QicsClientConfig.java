package com.honda.galc.client.qics.config;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.qics.property.QicsPropertyBean;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel.UnitInfoConfig;
import com.honda.galc.client.teamleader.recovery.frame.ProductRecoveryConfig;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ApplicationType;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.property.TrackingPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>QicsClientConfig</code> is ...
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
 * <TD>Apr 23, 2008</TD>
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
 /**
 * added new property for Repair In Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public class QicsClientConfig {

	private ProcessPoint processPoint;
	private Application application;
	private QicsPropertyBean qicsPropertyBean;
	private TrackingPropertyBean trackingPropertyBean;

	private List<QicsViewId> inputViewIds;

	private List<UnitInfoConfig> idlePanelCalculationConfigs;

	private float screenTouchFactor = 1.5f;

	private ProductRecoveryConfig productRecoveryConfig;

	public QicsClientConfig(ProductType productType,ProcessPoint processPoint, Application application) {
		this.processPoint = processPoint;
		this.application = application;
		this.qicsPropertyBean = PropertyService.getPropertyBean(QicsPropertyBean.class,processPoint.getProcessPointId());
		this.trackingPropertyBean = PropertyService.getPropertyBean(TrackingPropertyBean.class,processPoint.getProcessPointId());
		init(productType);
	}

	public ProcessPoint getProcessPoint() {
		return processPoint;
	}

	public Application getApplication() {
		return application;
	}

	// === config api (qics app level) === //
	public Color getDefectStatusColor(DefectStatus defectStatus) {
		return getDefectStatusColor(defectStatus.toString());
	}
	
	public Color getDefectStatusColor(String defectStatus) {
		Color color = null;
		if (defectStatus == null) return Color.white;
		if (getDefectStatusColors() != null) color = getDefectStatusColors().get(defectStatus);
		if (color == null) color = getDefaultDefectStatusColor(defectStatus);
		return color;
	}

	public DefectStatus getDefaultDefectStatus() {
		boolean isRepair = getApplication() != null && ApplicationType.QICS_REPAIR.getId() == getApplication().getApplicationTypeId();
		return isRepair ? DefectStatus.REPAIRED : DefectStatus.OUTSTANDING;
	}

	public boolean isDunnageRequired() {
		return getQicsPropertyBean().isDunnage();
	}
	
	public QicsPropertyBean getQicsPropertyBean() {
		return qicsPropertyBean;
	}

	public void setQicsPropertyBean(QicsPropertyBean qicsPropertyBean) {
		this.qicsPropertyBean = qicsPropertyBean;
	}


	public String getForwardProcessPointId() {
		return trackingPropertyBean.getTrackingProcessPointIdOnSuccess();
	}
	
	public String getAltForwardProcessPointId() {
		return trackingPropertyBean.getTrackingProcessPointIdOnFailure();
	}

	public boolean isOffProcessPointIdDefined() {
		return isForwardProcessPointIdDefined();
	}
	
	public boolean isForwardProcessPointIdDefined() {
		return !StringUtils.isEmpty(getForwardProcessPointId());
	}
	
	public boolean isMissionShippingTransactionEnabled(){
		return qicsPropertyBean.isMissionShippingTransactionEnabled();
	}
	
	public String getShippingTransactionUrl(){
		return qicsPropertyBean.getShippingTransactionUrl();
	}
	
	public String getShippingTransactionProcessPoint(){
		return qicsPropertyBean.getShippingTransactionProcessPoint();
	}

	// === bl utility api === //
	protected Color getDefaultDefectStatusColor(String defectStatus) {
	
		Color color = Color.white;
		if (defectStatus == null) return color;
		
		if(DefectStatus.OUTSTANDING.getName().equals(defectStatus))
			color = Color.RED;
		else if(DefectStatus.REPAIRED.getName().equals(defectStatus))
			color = Color.GREEN;
		
		return color;

	}

	public Map<String, Color> getDefectStatusColors() {
		Map<String, Color> colorMap = new HashMap<String, Color>();

		Map<String,String> map = qicsPropertyBean.getDefectStatusColors();
		if (map == null || map.isEmpty()) return colorMap;

		for(Entry<String,String> entry : map.entrySet())
			colorMap.put(entry.getKey(), createColor(entry.getValue()));

		return colorMap;
	}

	public List<UnitInfoConfig> getIdlePanelCalculationConfigs() {
		return idlePanelCalculationConfigs;
	}

	protected void setIdlePanelCalculationConfigs(List<UnitInfoConfig> idlePanelCalculationConfigs) {
		this.idlePanelCalculationConfigs = idlePanelCalculationConfigs;
	}

	public String getSendInputNumberToDeviceId() {
		return qicsPropertyBean.getSendInputNumberToDeviceId();
	}

	public List<QicsViewId> getInputViewIds() {
		return inputViewIds;
	}

	public void setInputViewIds(List<QicsViewId> inputViewIds) {
		this.inputViewIds = inputViewIds;
	}

	public boolean isScreenTouch() {
		return getQicsPropertyBean().isTouchScreen();	
	}

	public float getScreenTouchFactor() {
		return screenTouchFactor;
	}

	public void setScreenTouchFactor(float screenTouchFactor) {
		this.screenTouchFactor = screenTouchFactor;
	}

	public ProductRecoveryConfig getProductRecoveryConfig() {
		return productRecoveryConfig;
	}

	public void setProductRecoveryConfig(ProductRecoveryConfig productRecoveryConfig) {
		this.productRecoveryConfig = productRecoveryConfig;
	}

	private int[] split(String str) {
		int[] ints = {};
		if (StringUtils.isEmpty(str)) return ints;
		String[] stringValues = str.split(Delimiter.COMMA);
		ints = new int[stringValues.length];
		try {
			for (int i = 0; i < stringValues.length; i++) 
				ints[i] = Integer.valueOf(stringValues[i]);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return ints;
	}
	
	private Color createColor(String colorStr) {
		int[] rgb = split(colorStr);
		if (rgb == null || rgb.length < 3) 	return Color.WHITE;
		return new Color(rgb[0], rgb[1], rgb[2]);
	}
	
	private List<QicsViewId> deriveInputViewIds() {
		List<QicsViewId> list = new ArrayList<QicsViewId>();
		String[] panelNames = qicsPropertyBean.getInputPanels();

		for (String id : panelNames) {
			if (StringUtils.isEmpty(id))	continue;
			try {
				QicsViewId viewId = QicsViewId.valueOf(id);
				list.add(viewId);
			} catch (Exception e) {

			}
		}
		return list;
	}
	private List<UnitInfoConfig> createIdlePanelCalculationConfigs(ProductType productType, String processPointid) {
		List<UnitInfoConfig> list = new ArrayList<UnitInfoConfig>();
		Set<UnitInfoConfig> set = new LinkedHashSet<UnitInfoConfig>();
		set.addAll(createIdlePanelCalculationConfigs());
		set.addAll(createIdlePanelCalculationConfigs(productType));
		set.addAll(createIdlePanelCalculationConfigs(processPointid));
		list.addAll(set);
		return list;
	}
	
	private List<UnitInfoConfig> createIdlePanelCalculationConfigs() {
		List<UnitInfoConfig> list = new ArrayList<UnitInfoConfig>();
		list.add(UnitInfoConfig.INSPECTED);
		list.add(UnitInfoConfig.DIRECT_PASSED);
		list.add(UnitInfoConfig.WITH_DEFECTS);
		list.add(UnitInfoConfig.REPAIRED);
		list.add(UnitInfoConfig.OUTSTANDING);
		return list;
	}

	private List<UnitInfoConfig> createIdlePanelCalculationConfigs(String processPointId) {
		List<UnitInfoConfig> list = new ArrayList<UnitInfoConfig>();
		for(String property : qicsPropertyBean.getIdlePanelCalculations()) {
				UnitInfoConfig config = UnitInfoConfig.valueOf(property);
				list.add(config);
		}
		return list;
	}

	private List<UnitInfoConfig> createIdlePanelCalculationConfigs(ProductType productType) {
		List<UnitInfoConfig> list = new ArrayList<UnitInfoConfig>();
		if (ProductType.BLOCK.equals(productType) || ProductType.HEAD.equals(productType)) {
			list.add(UnitInfoConfig.SCRAPPED);
		} else if (ProductType.FRAME.equals(productType)) {
			// TODO ?
		} else if (ProductType.PLASTICS.equals(productType)) {
			// TODO ?
		} else if (ProductType.CRANKSHAFT.equals(productType) || ProductType.CONROD.equals(productType)) {
			// TODO?
		}
		return list;
	}
	

	private void init(ProductType productType) {
		setIdlePanelCalculationConfigs(createIdlePanelCalculationConfigs(productType,processPoint.getProcessPointId()));
		setInputViewIds(deriveInputViewIds());
	}
	
	
	public boolean isRepairTracking() {
		return getQicsPropertyBean().isRepairTracking();
	}
}
