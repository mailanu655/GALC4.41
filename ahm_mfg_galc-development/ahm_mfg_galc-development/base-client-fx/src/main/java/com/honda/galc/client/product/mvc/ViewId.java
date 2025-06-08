package com.honda.galc.client.product.mvc;

import static com.honda.galc.client.product.mvc.ViewId.ViewType.*;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.ui.IEvent;

/**
 * 
 * <h3>QicsViewId Class description</h3>
 * <p> QicsViewId description </p>
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
 * Apr 25, 2012
 *
 *
 */
public enum ViewId implements FXViewId{
	
	DATA_COLLECTION_VIEW					(PROCESS_VIEW,"Lot Control","com.honda.galc.client.dc.mvc.DataCollectionView"),
	DATA_COLLECTION_VIEW_REPAIR_CLIENT		(PROCESS_VIEW,"Lot Control","com.honda.galc.client.dc.mvc.DataCollectionRepairClientView"),
	PROCESS_INSTRUCTION_VIEW				(PROCESS_VIEW,"Lot Control","com.honda.galc.client.dc.mvc.ProcessInstructionView"),
	PROCESS_IMAGE_VIEW						(PROCESS_VIEW,"Lot Control","com.honda.galc.client.dc.mvc.ProcessImageView"),
	CLASSIC_DATA_COLLECTION_VIEW			(PROCESS_VIEW,"Lot Control","com.honda.galc.client.dc.mvc.ClassicDataCollectionView"),
	UNSCRAP_VIEW							(PROCESS_VIEW, "Product Unscrap", "com.honda.galc.client.teamleader.view.UnscrapView"),
	HISTORY_VIEW							(MAINT_VIEW,"History","com.honda.galc.client.product.HistoryView"),
	SAFETY_QUALITY_IMAGE_LIST_WIDGET		(WIDGET,"Safet Quality Image List","com.honda.galc.client.dc.view.SafetyQualityImageListWidget"),
	OPERATION_LIST_WIDGET					(WIDGET,"Operation List","com.honda.galc.client.dc.view.OperationListWidget"),
	OPERATION_LIST_WIDGETFX					(WIDGET,"Operation List","com.honda.galc.client.dc.view.OperationListWidgetFX"),
	OPERATION_IMAGE_WIDGET					(WIDGET,"Operation Image","com.honda.galc.client.dc.view.OperationImageWidget"),
	UPCOMING_PRODUCT_WIDGET					(WIDGET,"Upcoming Products","com.honda.galc.client.product.pane.UpcomingProductWidget"),
	PRODUCTION_SCHEDULE_UNITVIEWWIDGET		(WIDGET,"Production Schedule UnitView","com.honda.galc.client.dc.view.widget.ProductionScheduleUnitViewWidget"),
	PRODUCTION_SCHEDULE_LOTVIEWWIDGET		(WIDGET,"Production Schedule LotView","com.honda.galc.client.dc.view.widget.ProductionScheduleLotViewWidget"),
	PRODUCTION_SCHEDULE_LOTVIEWWIDGETFX 	(WIDGET,"Production Schedule LotView","com.honda.galc.client.dc.view.widget.ProductionScheduleLotViewWidgetFX"),
	PRODUCTION_SCHEDULE_UNITVIEWWIDGETFX 	(WIDGET,"Production Schedule UnitView","com.honda.galc.client.dc.view.widget.ProductionScheduleUnitViewWidgetFX"),
	PROCESS_CYCLETIMER_WIDGET				(WIDGET,"Process Cycle Timer","com.honda.galc.client.dc.view.widget.ProcessCycleTimerWidget"),
	ADVANCED_PROCESS_CYCLE_TIMER_WIDGET		(WIDGET,"Process Cycle Timer","com.honda.galc.client.dc.view.widget.AdvancedProcessCycleTimerWidget"),
	UNIT_PARTLIST_WIDGET					(WIDGET,"Unit Part List","com.honda.galc.client.dc.view.widget.UnitPartListWidget"),
	LOT_CONTROL_DATACOLLECTION_WIDGET		(WIDGET,"Lot Control Data Collection","com.honda.galc.client.dc.view.widget.LotControlDataCollectionWidget"),
	PRODUCTION_PLAN_WIDGETFX				(WIDGET,"Production Plan Widget","com.honda.galc.client.dc.view.widget.ProductionPlanWidgetFX"),
	UNIT_PROGRESS_WIDGET					(WIDGET,"Unit Progress Timer","com.honda.galc.client.dc.view.widget.UnitProgressTimerPane"),
	PUSH_TIMER_WIDGET                   	(WIDGET,"Push Timer","com.honda.galc.client.dc.view.widget.PushTimerPane"),
	ACCUMULATED_TIMER_WIDGET				(WIDGET,"Accumulated Timer","com.honda.galc.client.dc.view.widget.AccumulatedTimerPane"),
	DISPLAY_PROCESS_COMPLETE_WIDGET         (WIDGET,"Process Complete","com.honda.galc.client.dc.view.widget.DisplayProcessCompleteWidget"),
	STRUCTURE_DETAILS_WIDGET				(WIDGET,"Structure Details","com.honda.galc.client.dc.view.widget.StructureDetailsWidget"),
	DEFECT_ENTRY				            (PROCESS_VIEW,"Defect Entry","com.honda.galc.client.qi.defectentry.DefectEntryView"),
	PRE_CHECK				                (PROCESS_VIEW,"Pre Check","com.honda.galc.client.qi.precheck.PreCheckView"),
	REPAIR_ENTRY				            (PROCESS_VIEW,"Repair Entry","com.honda.galc.client.qi.repairentry.RepairEntryView"),
	BULK_REPAIR_ENTRY				        (PROCESS_VIEW,"Repair Entry","com.honda.galc.client.qi.repairentry.BulkRepairEntryView"),
	PRODUCT_HISTORY					        (PROCESS_VIEW,"Product History","com.honda.galc.client.qi.history.HistoryPanel"),
	DUNNAGE					                (PROCESS_VIEW,"Dunnage","com.honda.galc.client.dunnage.DunnageAssignmentView"),
	IPP_TAG_ENTRY					        (PROCESS_VIEW,"Ipp Tag Entry","com.honda.galc.client.qi.ipptagentry.IppTagEntryPanel"),
	PRODUCT_CHECK					        (PROCESS_VIEW,"Product Check","com.honda.galc.client.qi.productcheck.ProductCheckView"),
	DIE_CAST_RECOVERY					    (PROCESS_VIEW,"DC Recovery","com.honda.galc.client.dataRecovery.DieCastRecoveryView"), 
	PRODUCT_RECOVERY						(PROCESS_VIEW,"Product Recovery","com.honda.galc.client.qi.lotControl.LotControlManualRepairView"),
	PROD_SEQ_VIEW							(WIDGET,"Product Sequence","com.honda.galc.client.qi.homescreen.ProductSequencePanel"),
	SPOKE_ANGLE_ENTRY						(PROCESS_VIEW,"Spoke Angle Entry","com.honda.galc.client.qi.datacollection.SpokeAngleSliderPanel"),
	TOOL_DETAILS_WIDGET						(WIDGET,"Tools Details","com.honda.galc.client.dc.view.widget.ToolDetailsWidget");
	
	private ViewType viewType;
	private String viewLabel;
	private String viewClass;

	private ViewId(ViewType viewType,String label,String viewClass) {
		this.viewType = viewType;
		this.viewLabel = label;
		this.viewClass = viewClass;
	}
	
	private ViewId(String label,String panelClass) {
		this(PROCESS_VIEW,label,panelClass);
	}

	public static ViewId getValueOf(String name) {
		return name == null ? null : ViewId.valueOf(name);
	}
	
	public String getViewLabel() {
		return viewLabel;
	}

	public void setViewLabel(String viewLabel) {
		this.viewLabel = viewLabel;
	}

	public ViewType getViewType() {
		return viewType;
	}
	
	public String getViewClass() {
		return viewClass;
	}
	
	public static List<ViewId> getAllProcessViewIds() {
		
		List<ViewId> viewIds = new ArrayList<ViewId>();
		
		for(ViewId viewId : ViewId.values()) {
			if(viewId.getViewType().equals(PROCESS_VIEW)) viewIds.add(viewId);
		}
		return viewIds;
	}
	
	public static List<ViewId> getAllMaintenanceViewIds() {
		List<ViewId> viewIds = new ArrayList<ViewId>();
		
		for(ViewId viewId : ViewId.values()) {
			if(viewId.getViewType().equals(MAINT_VIEW)) viewIds.add(viewId);
		}
		return viewIds;
	}
	
	public static ViewId getViewId(String viewId) {
		return ViewId.valueOf(viewId);
	}
	
	public static Class<?> getViewClass(String viewIdName){
		ViewId viewId = ViewId.valueOf(viewIdName);
		if(viewId == null) return null;
		try {
			return Class.forName(viewId.getViewClass());
		} catch (ClassNotFoundException e) {
		}
		return null;
	}
	
	public enum ViewType{
		PROCESS_VIEW,WIDGET,MAINT_VIEW
	};
	
}
