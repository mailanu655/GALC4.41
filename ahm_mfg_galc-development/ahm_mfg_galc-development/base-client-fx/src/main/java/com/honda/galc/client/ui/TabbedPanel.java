package com.honda.galc.client.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.ReflectionUtils;

/**
 * 
 * <h3>TabbedPanel Class description</h3>
 * <p> TabbedPanel description </p>
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
 * Nov 21, 2010
 *
 * @author Suriya Sena
 * Jan 25, 2014  JavaFx Migration
 *
 * @author Justin Jiang
 * To support FXML tab
 * April 10, 2016
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */

/**
 * 
 * @version 0.3
 * @author L&T Infotech
 * To add FXML Tab for NAQ Project
 * @since Aug 15, 2016
 */
public abstract  class TabbedPanel extends ApplicationMainPane implements ITabbedPanel,EventHandler<ActionEvent>{
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private static Map<String,String> panels = new HashMap<String,String>();     // Map of PANEL names in GAL489TBX to classname
	private static Map<String,String> fxmlPanels = new HashMap<String,String>();     // Map of PANEL names in GAL489TBX to FXML file name
	
	static {
		panels.put("PRODUCT_SEQUENCE", "com.honda.galc.client.teamleader.fx.ExpectedProductSequencePanel");
		panels.put("PLANT_LEVEL_MESSAGE", "com.honda.galc.client.teamleader.fx.PlantMessageSenderPanel");
		panels.put("DIVISION_LEVEL_MESSAGE", "com.honda.galc.client.teamleader.fx.DivisionMessageSenderPanel");
		panels.put("LINE_MESSAGE_SENDER", "com.honda.galc.client.teamleader.fx.LineMessageSenderPanel");
		panels.put("SAFETY_LINE_MESSAGE_SENDER", "com.honda.galc.client.teamleader.fx.SafetyLineMessageSenderPanel");
		panels.put("QUALITY_LINE_MESSAGE_SENDER", "com.honda.galc.client.teamleader.fx.QualityLineMessageSenderPanel");
		panels.put("GENERIC_TABLE_MAINT", "com.honda.galc.client.teamleader.fx.GenericTableMaintenance.GenericTableMaintenancePanel");
		panels.put("MANUAL_LOT_CONTROL_REPAIR", "com.honda.galc.client.teamleader.fx.ManualLotControlRepairPanel");
		panels.put("CURE_TRACKING", "com.honda.galc.client.teamleader.fx.CureTimerTrackingPanel");
		
		panels.put("INSPECTION_PART","com.honda.galc.client.teamleader.qi.view.InspectionPartMaintPanel");
		panels.put("DEFECT","com.honda.galc.client.teamleader.qi.view.DefectMaintPanel");
		panels.put("PART_LOCATION_COMBINATION","com.honda.galc.client.teamleader.qi.view.PartLocationCombinationMaintPanel");
		panels.put("BOM_QICS_PART_ASSOCIATION","com.honda.galc.client.teamleader.qi.view.BomQicsAssociationMaintPanel");
		panels.put("IMAGE", "com.honda.galc.client.teamleader.qi.view.ImageMaintenancePanel");
		panels.put("IMAGE_SECTION_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.ImageSectionMaintenancePanel");
		panels.put("PART_DEFECT_COMBINATION", "com.honda.galc.client.teamleader.qi.view.PartDefectCombinationMaintPanel");
		panels.put("REPAIR_METHOD_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.RepairMethodMaintenancePanel");
		panels.put("THEME","com.honda.galc.client.teamleader.qi.view.ThemeMaintenanceMainPanel");
		panels.put("PDC_TO_ENTRY_SCREEN_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.PdcToEntryScreenAssignmentMainPanel");
		panels.put("IMAGE_TO_ENTRY_SCREEN_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.ImageToEntryScreenPanel");
		panels.put("IQS_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.IqsMaintPanel");
		panels.put("QICS_STATION_REPAIR_METHOD_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.QicsStationRepairMethodMaintPanel");
		panels.put("INSPECTION_LOCATION", "com.honda.galc.client.teamleader.qi.view.InspectionLocationMaintPanel");
		panels.put("REGIONAL_ATTRIBUTE_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.PdcRegionalAttributeMaintPanel");
        panels.put("MTC_TO_ENTRY_MODEL_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.MtcToEntryModelPanel");
		panels.put("ENTRY_SCREEN_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.EntryScreenMaintPanel");
		panels.put("LOCAL_ATTRIBUTE_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.PdcLocalAttributeMaintPanel");
		panels.put("PRODUCTION_LOT_BACKOUT", "com.honda.galc.client.teamleader.schedule.backout.ProductionLotBackoutPanel");
		panels.put("USER_MAINTENANCE","com.honda.galc.client.teamleader.user.UserMaintenancePanel");
		panels.put("MBPN_MAINTENANCE","com.honda.galc.client.teamleader.mbpn.MbpnMaintPanel");
		panels.put("STATION_CONFIGURATION_MANAGEMENT","com.honda.galc.client.teamleader.qi.stationconfig.EntryStationConfigPanel");
		panels.put("PLANT_LEVEL_MESSAGE","com.honda.galc.client.teamleader.fx.PlantMessageSenderPanel");
		panels.put("DIVISION_LEVEL_MESSAGE","com.honda.galc.client.teamleader.fx.DivisionMessageSenderPanel");
		panels.put("QUALITY_LINE_MESSAGE_SENDER","com.honda.galc.client.teamleader.fx.QualityLineMessageSenderPanel");
		panels.put("SAFETY_LINE_MESSAGE_SENDER","com.honda.galc.client.teamleader.fx.SafetyLineMessageSenderPanel");
		panels.put("HEADLESS_DEFECT_ENTRY","com.honda.galc.client.teamleader.qi.view.HeadlessDefectEntryPanel");
		panels.put("INVENTORY_LOCATION_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.ParkingLocationMaintPanel");
		panels.put("TARGET_MAINTENANCE","com.honda.galc.client.teamleader.qi.reportingtarget.ReportingTargetMaintenancePanel");
		panels.put("EXTERNAL_SYSTEM_ERROR","com.honda.galc.client.teamleader.qi.view.ExternalSystemErrorMaintPanel");
		panels.put("MTC_TO_MODEL_GROUP","com.honda.galc.client.teamleader.mtctomodelgroup.MtcToModelGroupPanel");
		panels.put("QI_LOCAL_RESPONSIBILITY_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.QiLocalResponsibilityAssignmentView");
		panels.put("QI_REGIONAL_RESPONSIBILITY_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.QiRegionalResponsibilityAssignmentView");
		panels.put("DUNNAGE_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.DunnageMaintPanel");
		panels.put("PRODUCT_STRUCTURE_DELETE","com.honda.galc.client.teamlead.structure.delete.StructureDeletePanel");
		panels.put("DATA_MIGRATION_ASSOCIATION_MAINTENANCE","com.honda.galc.client.teamleader.datamigration.DataMigrationAssociationMaintenancePanel");
		panels.put("LOCAL_THEME","com.honda.galc.client.teamleader.qi.view.LocalThemeMaintPanel");
		panels.put("DATA_CORRECTION","com.honda.galc.client.teamleader.qi.defectResult.DefectResultMaintPanel");
		panels.put("DEFECT_TAGGING","com.honda.galc.client.teamleader.qi.defectTagging.DefectTaggingMaintenancePanel");
		panels.put("RESPONSIBLE_LOAD_TRIGGER","com.honda.galc.client.teamleader.qi.view.ResponsibleLoadTriggeredPanel");
		panels.put("CHECKER_CONFIG","com.honda.galc.client.teamlead.checker.CheckerConfigView");
		panels.put("MFG_DATA_LOAD_PART_MASK","com.honda.galc.client.loader.MfgDataLoaderPartMaskPanel");
		panels.put("MFG_DATA_LOAD_MEASUREMENT","com.honda.galc.client.loader.MfgDataLoaderMeasurementPanel");
		panels.put("MFG_DATA_LOAD_MBPN_MASK","com.honda.galc.client.loader.MfgDataLoaderMbpnMaskPanel");
		panels.put("PART_SHIPMENT_PANEL","com.honda.galc.client.teamleader.fx.PartShipmentMaintenancePanel");
		panels.put("MFG_DATA_LOAD_PROCESS","com.honda.galc.client.loader.MfgDataLoader");
		panels.put("REGIONAL_COMMON_DATA_MAINTENANCE","com.honda.galc.client.teamleader.common.RegionalDataMaintenancePanel");
		panels.put("REGIONAL_PROCESS_POINT_GROUP_MAINTENANCE","com.honda.galc.client.teamleader.common.RegionalProcessPointGroupMaintenancePanel");
		panels.put("VIOS_PLATFORM_MAINTENANCE","com.honda.galc.client.teamlead.vios.platform.ViosPlatformMaintView");
		panels.put("VIOS_PROCESS_MAINTENANCE","com.honda.galc.client.teamlead.vios.process.ViosProcessMaintView");
		panels.put("VIOS_UNIT_NUMBER_CONFIG","com.honda.galc.client.teamlead.vios.opconfig.ViosUnitNoConfigView");
		panels.put("VIOS_PART_CONFIG","com.honda.galc.client.teamlead.vios.partconfig.ViosPartConfigView");
		panels.put("VIOS_MEAS_CONFIG","com.honda.galc.client.teamlead.vios.measconfig.ViosMeasConfigView");
		panels.put("ONE_CLICK_APPROVAL","com.honda.galc.client.teamlead.vios.oneclick.ViosOneClickConfigView");
        panels.put("LET_DOWNLOAD", "com.honda.galc.client.teamleader.fx.let.LetResultDownloadPanel");	
        panels.put("LET_VIEW", "com.honda.galc.client.teamleader.fx.let.LetResultViewPanel");
        panels.put("QI_RESPONSIBILITY_MAPPING_ASSIGNMENT","com.honda.galc.client.teamleader.qi.view.QiResponsibleMappingAssignmentView");
        panels.put("UNSCRAP", "com.honda.galc.client.teamleader.view.UnscrapView");
        panels.put("KICKOUT", "com.honda.galc.client.teamleader.view.KickoutView");
        panels.put("DOCUMENT_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.DocumentMaintenancePanel");
        panels.put("STATION_DOCUMENT_MAINTENANCE","com.honda.galc.client.teamleader.qi.view.StationDocumentMaintenancePanel");
        panels.put("MFG_MBPN_APPROVAL_PROCESS","com.honda.galc.client.loader.mbpn.ViosMBPNMatrixPanel");
        panels.put("STATION_DOCUMENT_LIST","com.honda.galc.client.teamleader.qi.view.StationDocumentListPanel");
        panels.put("PROD_SPEC_CODE_MAINTAINANCE", "com.honda.galc.client.teamleader.speccode.SpecCodeColorMaintPanel");
        panels.put("REASON_FOR_CHANGE_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.ReasonForChangeMaintPanel");
        panels.put("FRAME_MAINTENANCE","com.honda.galc.client.teamleader.frame.FrameSpecMaintPanel");
		panels.put("ENGINE_MAINTENANCE","com.honda.galc.client.teamleader.engine.EngineSpecMaintPanel");
		panels.put("EXTERNAL_SYSTEM_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.ExternalSystemMaintPanel");
        panels.put("DEFECT_REPAIR_IMAGE_MAINTENANCE", "com.honda.galc.client.teamleader.qi.view.DefectRepairImageMaintenancePanel");
	}
	
	private String screenName;
	private int keyEvent;
	protected boolean isInitialized = false;
	private final static String TABBED_PANEL_FXML_CLASS = "com.honda.galc.client.ui.TabbedPanelFXML";
	
	@SuppressWarnings("unchecked")
	public static ApplicationMainPane createTabbedPanel(String panelId, TabbedMainWindow window) {
		
		String className = panels.get(panelId);
		if (StringUtils.isEmpty(className)) {
			className = fxmlPanels.get(panelId);
		}
		if(StringUtils.isEmpty(className)) throw new TaskException("non existent javafx pane id " + panelId);
		
		Class<?> clazz;
		ApplicationMainPane tabbedPane;
		String fxmlFileName = "";
		
		if (className.endsWith(".fxml")) {
			fxmlFileName = className;
			//use generic FXML Tab class
			className = TABBED_PANEL_FXML_CLASS;
		} 	

		try {
			clazz = Class.forName(className);
		} catch (Exception e) {
			throw new SystemException("Unable to get tabbled javafx pane class from the class name", e);
		}
		
		if (!ITabbedPanel.class.isAssignableFrom(clazz)) 
			throw new TaskException("class " + clazz.getSimpleName() + " is not a TabbedPanel");
		
		if (fxmlFileName.endsWith(".fxml")) {
			tabbedPane = ReflectionUtils.createInstance((Class<? extends TabbedPanelFXML>)clazz, panelId, fxmlFileName, window);
		} else {		
			tabbedPane = ReflectionUtils.createInstance((Class<? extends ApplicationMainPane>)clazz, window);
		}

		return tabbedPane;
	}
	
	public TabbedPanel(String screenName, int keyEvent) {
		this(screenName,keyEvent,null);
	}
	
	public TabbedPanel(String screenName, int keyEvent, MainWindow mainWindow) {
		super(mainWindow);
		this.screenName = screenName;
		this.keyEvent = keyEvent;
		
		Logger.getLogger().info("Opened tabbedPanel \"" + screenName + "\"");
	}
	
	public TabbedPanel(String screenName, MainWindow mainWindow) {
		super(mainWindow);
		this.screenName = screenName;
		Logger.getLogger().info("Opened tabbedPanel \"" + screenName + "\"");
	}
	
	public abstract void onTabSelected();
	
	@Override
	public  void handle(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	public int getKeyEvent() {
		return keyEvent;
	}
	
	public void setKeyEvent(int keyEvent) {
		this.keyEvent = keyEvent;
	}
	
	public String getScreenName() {
		return screenName;
	}
	
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	
//  Does not appear to be used	
//	protected JMenuItem createMenuItem(String name,boolean enabled) {
//		JMenuItem menuItem = new JMenuItem(name);
//		menuItem.setName(name);
//		menuItem.addActionListener(this);
//		menuItem.setEnabled(enabled);
//		return menuItem;
//	}
	
	public Logger getLogger(){
		return Logger.getLogger(deriveClientName());
	}
	
	private String deriveClientName() {
		String[] names = screenName.split(" ");
		boolean isFirst = true;
		String clientName ="";
		
		for(String name : names) {
			if(!isFirst) {
				clientName +="_";
			}else isFirst = false;
			clientName +=name;
		}
		
		return clientName;
		
	}

//	
//	protected String getSiteName() {
//		return PropertyService.getSiteName();
//	}
	
}
