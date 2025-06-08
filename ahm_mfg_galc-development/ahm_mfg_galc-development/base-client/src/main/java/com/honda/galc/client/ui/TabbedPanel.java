package com.honda.galc.client.ui;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
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
 *
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public abstract class TabbedPanel extends ApplicationMainPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected static final String SAVED = "saved";
	protected static final String UPDATED = "updated";
	protected static final String INSERTED = "inserted";
	protected static final String REMOVED = "removed";
	
	private static Map<String,String> panels = new HashMap<String,String>();

	static {
		panels.put("PART", "com.honda.galc.client.teamleader.PartPanel");
		panels.put("LOT_CONTROL", "com.honda.galc.client.teamleader.LotControlRulePanel");
		panels.put("BOM_LOT_CONTROL", "com.honda.galc.client.teamleader.LotControlRulesFromBomPanel");
		panels.put("REQUIRED_PARTS", "com.honda.galc.client.teamleader.RequiredPartPanel");
		panels.put("LOG_LEVEL", "com.honda.galc.client.teamleader.LogLevelPanel");
		panels.put("NOTIFICATION", "com.honda.galc.client.teamleader.NotificationPanel");
		panels.put("BUILD_ATTRIBUTE", "com.honda.galc.client.teamleader.BuildAttributePanel");
		panels.put("BUILD_ATTRIBUTE_BY_BOM", "com.honda.galc.client.teamleader.BuildAttributeByBomPanel");
		panels.put("BUILD_ATTRIBUTE_CHECK", "com.honda.galc.client.teamleader.BuildAttributeCheckPanel");
		panels.put("BUILD_ATTRIBUTE_DEFINITION", "com.honda.galc.client.teamleader.BuildAttributeDefinitionPanel");
		panels.put("BUILD_ATTRIBUTE_GROUP_DEFINITION", "com.honda.galc.client.teamleader.BuildAttributeGroupDefinitionPanel");
		panels.put("BUILD_ATTRIBUTE_TRANSFER", "com.honda.galc.client.teamleader.BuildAttributeTransferPanel");
		panels.put("BUILD_ATTRIBUTE_VIEW", "com.honda.galc.client.teamleader.BuildAttributeViewPanel");
		panels.put("PART_LOT", "com.honda.galc.client.teamleader.PartLotPanel");
		panels.put("PRODUCT_SEQUENCE", "com.honda.galc.client.teamleader.ExpectedProductSequencePanel");
		panels.put("KNUCKLE_MANUAL_SHIPPING", "com.honda.galc.client.teamleader.knuckle.KnuckleManualShippingPanel");
		panels.put("KNUCKLE_SHIPPING_QUERY", "com.honda.galc.client.teamleader.knuckle.KnuckleShippingQueryPanel");
		panels.put("MANUAL_LOT_CONTROL_REPAIR", "com.honda.galc.client.teamleader.ManualLotControlRepairView");
		panels.put("SKIPPED_PRODUCT", "com.honda.galc.client.teamleader.SkippedProductMaintPanel");
		panels.put("STANDARD_SCHEDULE", "com.honda.galc.client.teamleader.StandardScheduleMaintenancePanel");
		panels.put("DAILY_SCHEDULE", "com.honda.galc.client.teamleader.DailyDeptScheduleMaintenancePanel");
		panels.put("USER", "com.honda.galc.client.teamleader.UserPanel");
		panels.put("RULE_VALIDATION", "com.honda.galc.client.teamleader.LotControlValidationPanel");
		panels.put("INSPECTION_PART","com.honda.galc.client.teamleader.qics.part.PartPanel");
		panels.put("DEFECT","com.honda.galc.client.teamleader.qics.defect.DefectPanel");
		panels.put("PART_DEFECT_COMB","com.honda.galc.client.teamleader.qics.combination.PartDefectCombinationPanel");
		panels.put("IMAGE","com.honda.galc.client.teamleader.qics.image.ImagePanel");
		panels.put("IMAGE_SECTION","com.honda.galc.client.teamleader.qics.image.ImageSectionPanel");
		panels.put("MODEL_ASSIGNMENT","com.honda.galc.client.teamleader.qics.modelassignment.ModelAssignmentPanel");
		panels.put("PRODUCT_SPEC_CODE","com.honda.galc.client.teamleader.ProductSpecCodePanel");
		panels.put("GENERIC_TEMPLATE", "com.honda.galc.client.teamleader.GenericTemplatePanel");
		panels.put("REPRINT","com.honda.galc.client.teamleader.ReprintLabelPanel");
		panels.put("BROADCAST_REPRINT","com.honda.galc.client.teamleader.BroadcastReprintPanel");
		panels.put("QSR_HOLD_BY_LOT","com.honda.galc.client.teamleader.hold.qsr.put.lot.HoldLotPanel");
		panels.put("QSR_HOLD_BY_PROCESS","com.honda.galc.client.teamleader.hold.qsr.put.process.ProcessPanel");
		panels.put("QSR_IMPORT_PRODUCTS","com.honda.galc.client.teamleader.hold.qsr.put.file.ImportFilePanel");
		panels.put("QSR_SCAN_PRODUCT","com.honda.galc.client.teamleader.hold.qsr.put.scan.ScanProductPanel");
		panels.put("QSR_DUNNAGE","com.honda.galc.client.teamleader.hold.qsr.put.dunnage.HoldDunnagePanel");
		panels.put("QSR_DIE_HOLD","com.honda.galc.client.teamleader.hold.qsr.put.die.DiePanel");		
		panels.put("QSR_RELEASE","com.honda.galc.client.teamleader.hold.qsr.release.ReleasePanel");
		panels.put("QSR_REASON","com.honda.galc.client.teamleader.hold.qsr.reason.ReasonPanel");
		panels.put("QSR_SCAN_RELEASE","com.honda.galc.client.teamleader.hold.qsr.release.ScanReleasePanel");
		panels.put("QSR_HOLD_BY_PRODUCT_SEQ","com.honda.galc.client.teamleader.hold.qsr.put.vinseq.VinSeqPanel");
		panels.put("QSR_RELEASE_BY_PRODUCT_SEQ","com.honda.galc.client.teamleader.hold.qsr.release.vinseq.ReleaseBySeqPanel");
		panels.put("DUNNAGE_MAINTENANCE","com.honda.galc.client.teamleader.dunnage.DunnageMaintenancePanel");		
		panels.put("HOLD_PRODUCT","com.honda.galc.client.teamleader.HoldByProductPanel");
		panels.put("RELEASE_PRODUCT","com.honda.galc.client.teamleader.ReleaseByProductPanel");
		panels.put("HOLD_RELEASE_PRE_PRODUCTION_LOT","com.honda.galc.client.teamleader.HoldReleasePreProductionLotPanel");
		panels.put("BEARING_MODEL_MAINTENANCE","com.honda.galc.client.teamleader.bearing.BearingModelMaintenancePanel");
		panels.put("BEARING_MAIN_MATRIX_MAINTENANCE","com.honda.galc.client.teamleader.bearing.MainMatrixMaintenancePanel");
		panels.put("BEARING_CONROD_MATRIX_MAINTENANCE","com.honda.galc.client.teamleader.bearing.ConrodMatrixMaintenancePanel");
		panels.put("MC_DEASSIGNMENT","com.honda.galc.client.teamleader.McDeassignmentPanel");
		panels.put("PRODUCT_HISTORY","com.honda.galc.client.teamleader.history.ProductHistoryMaintenancePanel");
		panels.put("UNSCRAP","com.honda.galc.client.teamleader.scrap.UnscrapPanel");
		panels.put("TRACKING","com.honda.galc.client.teamleader.tracking.TrackingMaintenancePanel");
		panels.put("LET_FAULT_RESULT","com.honda.galc.client.teamleader.let.LetFaultResultPanel");
		panels.put("LET_INSPECTION_RESULT_DELETION","com.honda.galc.client.teamleader.let.LetInspectionResultDeletionPanel");
		panels.put("LET_INSPECTION_RESULT_MANUAL_INPUT","com.honda.galc.client.teamleader.let.LetInspectionResultManualInputPanel");
		panels.put("LET_INSPECTION_RESULT_UPDATE_HISTORY","com.honda.galc.client.teamleader.let.LetInspectionResultUpdateHistoryPanel");
		panels.put("LET_INSPECTION_RESULT","com.honda.galc.client.teamleader.let.LetInspectionResultPanel");
		panels.put("LET_INSPECTION_RESULT_UPDATE", "com.honda.galc.client.teamleader.let.LetInspectionResultUpdatePanel");
		panels.put("LET_INSPECTION_RESULT_DOWNLOAD", "com.honda.galc.client.teamleader.let.LetInspectionResultDownloadPanel");	
		panels.put("LET_SELF_DIAGNOSIS_RESULT_DOWNLOAD", "com.honda.galc.client.teamleader.let.LetSelfDiagResultDownloadPanel");
		panels.put("ENG_MISSION_ASSIGNMENT", "com.honda.galc.client.teamleader.frame.EngineMissionAssignmentPanel");
		panels.put("LET_PREOPERATION_CHECK_RESULT", "com.honda.galc.client.teamleader.let.LetPreoperationalCheckResultPanel");
		panels.put("LET_SHIPPING_JUDGEMENT_SETTING", "com.honda.galc.client.teamleader.let.LetShippingJudgementSettingPanel");
		panels.put("LET_FAULT_RESULT_DOWNLOAD", "com.honda.galc.client.teamleader.let.LetFaultResultDownloadPanel");
		panels.put("EXCEPTIONAL_OUTGOING", "com.honda.galc.client.teamleader.ExceptionalOutgoingPanel");
		panels.put("DISPLAY_AUDIO_SEND", "com.honda.galc.client.teamleader.let.DisplayAudioSendPanel");
		panels.put("PREPRODUCTIONLOT_SEQUENCE", "com.honda.galc.client.teamleader.PreProductionLotSequencePanel");
		panels.put("ACTUAL_PROBLEM_REPAIR_METHOD", "com.honda.galc.client.teamleader.qics.actualproblemrepairmethod.ActualProblemRepairMethodPanel");
		panels.put("TEAM_ROTATION", "com.honda.galc.client.teamleader.TeamRotationPanel");
		panels.put("IQS_REGRESSION_CODE_MAINTENANCE", "com.honda.galc.client.teamleader.qics.iqscatregressionmode.IQSCatRegressionModeMaintPanel");
		panels.put("PRODUCT_CARRIER_REPAIR", "com.honda.galc.client.teamleader.ProductCarrierRepairView");
		panels.put("MANUAL_LOT_CONTROL_REPAIR_RECURSION", "com.honda.galc.client.teamleader.ManualLotControlRepairRecursionView");
		panels.put("TWO_PART_PAIR_MAINTENANCE", "com.honda.galc.client.teamleader.qics.twopartpair.TwoPartPairMaintenancePanel");
		panels.put("TWO_PART_DEFECT_COMB_MAINTENANCE", "com.honda.galc.client.teamleader.qics.twopartpair.TwoPartDefectCombMaintenancePanel");
		panels.put("PLAN_CODE_LOT_SEQ_MAINTENANCE", "com.honda.galc.client.teamleader.PlanCodeLotSequenceMaintenancePanel");
		panels.put("LOT_CONTROL_TRANSFER", "com.honda.galc.client.teamleader.transfer.LotControlRuleTransferPanel");
		panels.put("PRINT_FORM", "com.honda.galc.client.teamleader.PrintFormPanel");
		panels.put("MBPN_MAINTENANCE", "com.honda.galc.client.teamleader.MbpnMaintenancePanel");
		panels.put("IN_PROCESS_PRODUCT_MAINTENANCE", "com.honda.galc.client.teamleader.InProcessProductMaintenancePanel");
		panels.put("MBPN_SPEC_ASSIGNMENT", "com.honda.galc.client.teamleader.MbpnSpecPanel");
		panels.put("LET_SPOOL_MANAGER", "com.honda.galc.client.teamleader.let.LetSpoolManager");
		panels.put("LOT_CONTROL_RULE_GROUP", "com.honda.galc.client.teamleader.LotControlRuleGroupMaintPanel");
		panels.put("GAUGE_FLEX_GROUP_REPAIR", "com.honda.galc.client.teamleader.ManualLotControlGroupRepairView");
		panels.put("CODE", "com.honda.galc.client.teamleader.CodePanel");
		panels.put("CODE_ASSIGNMENT", "com.honda.galc.client.teamleader.StragglerCodeAssignmentPanel");
		panels.put("CODE_BROADCAST_PRODUCT", "com.honda.galc.client.codebroadcast.CodeBroadcastProductPanel");
		panels.put("CODE_BROADCAST_SPECIAL_TAG", "com.honda.galc.client.codebroadcast.CodeBroadcastSpecialTagPanel");
		panels.put("CODE_BROADCAST_SPECIAL_TAG_BY_COLOR_CODE", "com.honda.galc.client.codebroadcast.CodeBroadcastSpecialTagByColorCodePanel");
		panels.put("EQUIP_ALARM_PRIORITY", "com.honda.galc.client.teamleader.EquipAlarmPriorityPanel");
		panels.put("EQUIP_ALARM_GROUP_UNIT", "com.honda.galc.client.teamleader.EquipAlarmGroupUnitPanel");
		panels.put("EQUIP_ALARM_MANUAL_IMPORT", "com.honda.galc.client.teamleader.EquipAlarmManualImportPanel");
		panels.put("PRODUCTION_LOT_BACKOUT", "com.honda.galc.client.teamleader.schedule.backout.ProductionLotBackoutPanel");
		panels.put("DOWNLOAD_LOT_SEQUENCE", "com.honda.galc.client.teamleader.DownloadLotSequencePanel");
		panels.put("PRE_PRODUCTION_LOT_MAINT", "com.honda.galc.client.teamleader.PreProductionLotMaintPanel");
		panels.put("OFFLINE_LOT_CONTROL_REPAIR", "com.honda.galc.client.teamleader.OfflineLotControlRepairPanel");
		panels.put("PRINT_ATTRIBUTE_FORMAT", "com.honda.galc.client.teamleader.PrintAttributeFormatPanel");
		panels.put("MBPN_TYPE", "com.honda.galc.client.teamleader.mbpn.MbpnTypeDefPanel");
		panels.put("ENGINE_MTO_REPAIR", "com.honda.galc.client.teamleader.EngineMtoRepairPanel");
		panels.put("PRINTER_CHANGE", "com.honda.galc.client.teamleader.PrinterChangePanel");
		panels.put("DATA_COLLECTION_IMAGE_MAINTENANCE","com.honda.galc.client.teamleader.DataCollectionImageMaintenancePanel");
		panels.put("LET_STOP_SHIP_FILE_UPLOADER","com.honda.galc.client.teamleader.let.LetStopShipFileUploaderPanel");
		panels.put("BULK_PART_MAINTENANCE", "com.honda.galc.client.teamleader.BulkPartMaintPanel");
		panels.put("DC_ZONE", "com.honda.galc.client.teamleader.DCZonePanel");
		panels.put("PART_MARK_GROUP","com.honda.galc.client.teamleader.PartMarkGroupPanel");
		panels.put("VEHICLE_COLOR_CHANGE", "com.honda.galc.client.teamleader.VehicleColorChangePanel");
		panels.put("EXTERNAL_REQUIRED_PART_PANEL", "com.honda.galc.client.teamleader.ExternalRequiredPartPanel");
		panels.put("EXTERNAL_PART_EXCLUSION_PANEL", "com.honda.galc.client.teamleader.ExternalPartExclusionPanel");
		panels.put("LET_DATA_CHECK_PANEL", "com.honda.galc.client.teamleader.LETDataCheckPanel");
		panels.put("FRAME_ENGINE_MODEL_MAP", "com.honda.galc.client.teamleader.FrameEngineModelMapPanel");
		panels.put("STOP_SHIP_MASS_UPDATE", "com.honda.galc.client.teamleader.StopShipMassUpdatePanel");
		panels.put("PROCESS_POINT_DETAILS", "com.honda.galc.client.teamleader.ProcessPointDetails");
		panels.put("ENHANCED_REPAIR_PART_LINKAGE", "com.honda.galc.client.teamleader.PartEnhancementLinkagePanel");
		panels.put("RETURN_TO_FACTORY", "com.honda.galc.client.teamleader.ReturnToFactoryPanel");
		panels.put("DELIVERY_ORDER_ADJUSTMENT", "com.honda.galc.client.teamleader.DeliveryOrderAdjustmentPanel");
		panels.put("INSPECTION_SAMPLING_MAINTENANCE", "com.honda.galc.client.teamleader.InspectionSamplingMaintenancePanel");
		panels.put("IMAGE_TEMPLATES_MAINTENANCE", "com.honda.galc.client.teamleader.template.ImageTemplateMaintenancePanel");
		panels.put("RESET_SEQUENCE", "com.honda.galc.client.teamleader.ResetSequencePanel");
		panels.put("SCHED_REPL_UPDATE_MBPN", "com.honda.galc.client.teamleader.SchedReplicationUpdateMbpnPanel");
		panels.put("REQUIRED_PART_TRANSFER", "com.honda.galc.client.teamleader.transfer.RequiredPartTransferPanel");
		panels.put("BARCODE_PRINT", "com.honda.galc.client.teamleader.BarcodePrintPanel");
		panels.put("MBPN_PRODUCT_CHANGE", "com.honda.galc.client.teamleader.mbpn.MbpnProductChangePanel");
	}


	private String screenName;
	private int keyEvent;
	protected boolean isInitialized = false;

	@SuppressWarnings("unchecked")
	public static TabbedPanel createTabbedPanel(String panelId, TabbedMainWindow window) {

		String className = panels.get(panelId);
		if(StringUtils.isEmpty(className)) throw new TaskException("non existent panel id " + panelId);
		Class<?> clazz;
		try{
			clazz = Class.forName(className);
		}catch(Exception e) {
			throw new SystemException("Unable to get tabbled panel class from the class name", e);
		}
		if(!TabbedPanel.class.isAssignableFrom(clazz)) 
			throw new TaskException("class " + clazz.getSimpleName() + " is not a TabbedPanel");
		return ReflectionUtils.createInstance((Class<? extends TabbedPanel>)clazz, window);
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

	public abstract void onTabSelected();

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

	protected JMenuItem createMenuItem(String name,boolean enabled) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.setName(name);
		menuItem.addActionListener(this);
		menuItem.setEnabled(enabled);
		return menuItem;
	}

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
	
	protected void logUserAction(String message) {
		getLogger().info("User ", getUserName(), " ", message);
	}
	
	protected void logUserAction(String message, Object object) {
		if(object != null) {
			getLogger().info("User ", getUserName(), " ", message, " ", object.getClass().getSimpleName(), ": ", object.toString());
		}
	}

	protected void logUserAction(String message, List<?> items) {
		for(Object object : items) {
			logUserAction(message, object);
		}
	}
	
	protected String getUserName() {
		return ClientMain.getInstance().getAccessControlManager().getUserName();
	}

	protected String getSiteName() {
		return PropertyService.getSiteName();
	}

	protected boolean isMbpnProduct() {
		return ProductTypeUtil.isMbpnProduct(ProductTypeCatalog.getProductType(getProductTypeString()));
	}

	protected boolean isProductSpec() {
		return !isMbpnProduct();
	}

	public String getProductTypeString(){
		return PropertyService.getPropertyBean(SystemPropertyBean.class, window.getApplication().getApplicationId()).getProductType();
	}

}