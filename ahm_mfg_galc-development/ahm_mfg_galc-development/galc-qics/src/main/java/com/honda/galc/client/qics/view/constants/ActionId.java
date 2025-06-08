package com.honda.galc.client.qics.view.constants;

import com.honda.galc.client.qics.view.action.AbstractPanelAction;
import com.honda.galc.client.qics.view.action.AcceptDefectRepairAction;
import com.honda.galc.client.qics.view.action.AcceptNewDefectAction;
import com.honda.galc.client.qics.view.action.CancelAction;
import com.honda.galc.client.qics.view.action.ChangeResponsibleAction;
import com.honda.galc.client.qics.view.action.DefectScanRepairAllAction;
import com.honda.galc.client.qics.view.action.DirectPassAction;
import com.honda.galc.client.qics.view.action.DirectPassWithCheckProductStateAction;
import com.honda.galc.client.qics.view.action.DirectPassWithDunnageAction;
import com.honda.galc.client.qics.view.action.DirectPassWithWarnAction;
import com.honda.galc.client.qics.view.action.IdlePanelSubmitAction;
import com.honda.galc.client.qics.view.action.ScrapAction;
import com.honda.galc.client.qics.view.action.SubmitAction;
import com.honda.galc.client.qics.view.action.SubmitWithCheckProductStateAction;
import com.honda.galc.client.qics.view.action.SubmitWithDunnageAction;
import com.honda.galc.client.qics.view.action.SubmitWithRepairTracking;
import com.honda.galc.client.qics.view.action.SubmitWithWarnAction;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.util.ReflectionUtils;
/**
 * added SUBMIT_WITH_REPAIR_TRACKING for Repair In Panel
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public enum ActionId {
	ACCEPT_NEW_DEFECT(AcceptNewDefectAction.class), 
	ACCEPT_REPAIR(AcceptDefectRepairAction.class), 
	DIRECT_PASS(DirectPassAction.class), 
	CANCEL(CancelAction.class), 
	SCRAP(ScrapAction.class), 
	SUBMIT(SubmitAction.class), 
	IDLE_PANEL_SUBMIT(IdlePanelSubmitAction.class), 
	SUBMIT_WITH_CHECKS(SubmitWithCheckProductStateAction.class), 
	DIRECT_PASS_WITH_CHECKS(DirectPassWithCheckProductStateAction.class), 
	SUBMIT_WITH_DUNNAGE(SubmitWithDunnageAction.class), 
	DIRECT_PASS_WITH_DUNNAGE(DirectPassWithDunnageAction.class), 
	VOID_ALL(null), 
	REPAIR_ALL(DefectScanRepairAllAction.class), 
	VOID_SELECTED(null), 
	CHANGE_RESPONSIBLE(ChangeResponsibleAction.class), 
	SUBMIT_WITH_WARN(SubmitWithWarnAction.class), 
	DIRECT_PASS_WITH_WARN(DirectPassWithWarnAction.class),
	SUBMIT_WITH_REPAIR_TRACKING(SubmitWithRepairTracking.class),
	NEW_DUNNAGE(AbstractPanelAction.class),
	CHANGE_DUNNAGE(AbstractPanelAction.class),
	PRINT_DUNNAGE(AbstractPanelAction.class);
	
	private Class <? extends AbstractPanelAction>  actionClass;
	
	private ActionId(Class<? extends AbstractPanelAction> actionClass) {
		this.actionClass = actionClass;
	}
	
	public Class <? extends AbstractPanelAction> getActionClass() {
		return actionClass;
	}
	
	public AbstractPanelAction createAction(QicsPanel qicsPanel) {
		return ReflectionUtils.createInstance(actionClass, new Class<?>[] {QicsPanel.class}, qicsPanel);
	}
}
