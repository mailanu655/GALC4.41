package com.honda.galc.client.qics.view.constants;

import java.awt.event.KeyEvent;

import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.screen.CheckResultsPanel;
import com.honda.galc.client.qics.view.screen.DefectPictureInputPanel;
import com.honda.galc.client.qics.view.screen.DefectRepairPanel;
import com.honda.galc.client.qics.view.screen.DefectScanTextPanel;
import com.honda.galc.client.qics.view.screen.DefectTextInputPanel;
import com.honda.galc.client.qics.view.screen.DunnagePanel;
import com.honda.galc.client.qics.view.screen.HistoryPanel;
import com.honda.galc.client.qics.view.screen.IPPTagMainPanel;
import com.honda.galc.client.qics.view.screen.LotControlManualRepairWrapper;
import com.honda.galc.client.qics.view.screen.LotControlWrapper;
import com.honda.galc.client.qics.view.screen.PreCheckResultsPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.client.qics.view.screen.RecoveryPanel;
import com.honda.galc.client.qics.view.screen.RepairInPanel;
import com.honda.galc.client.qics.view.screen.SubProductDefectRepairPanel;
import com.honda.galc.util.ReflectionUtils;

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
 /**
 * added REPAIR_IN
 * @author Gangadhararao Gadde
 * @date Apr 17, 2014
 */
public enum QicsViewId {

	IDLE, 
	MAIN,
	// === configurable tab panels === //
	PICTURE_INPUT("Picture Input", KeyEvent.VK_P,DefectPictureInputPanel.class), 
	TEXT_INPUT("Text Input", KeyEvent.VK_T,DefectTextInputPanel.class), 
	REPAIR("Defect Repair", KeyEvent.VK_R,DefectRepairPanel.class),
	DC_RECOVERY("Product Recovery", KeyEvent.VK_V,RecoveryPanel.class), 
	RECOVERY("Product Recovery", KeyEvent.VK_V,LotControlManualRepairWrapper.class), 
	HISTORY("Product History", KeyEvent.VK_H,HistoryPanel.class),
	LOT_CONTROL("Lot Control", KeyEvent.VK_L,LotControlWrapper.class),
	SUB_PRODUCT_REPAIR("SubProduct Defect Repair",KeyEvent.VK_S,SubProductDefectRepairPanel.class),
	// === end configurable tab panels === //
	// === implicit tab panels === //
	PRE_CHECK_RESULTS("Pre Check", KeyEvent.VK_E,PreCheckResultsPanel.class), 
	CHECK_RESULTS("Product Check", KeyEvent.VK_K,CheckResultsPanel.class), 
	DUNNAGE("Dunnage", KeyEvent.VK_N,DunnagePanel.class),
	IPPTAG("IPP Tag Entry", KeyEvent.VK_I,IPPTagMainPanel.class),
	DEFECT_SCAN_TEXT("Defect Text Scan", KeyEvent.VK_D,DefectScanTextPanel.class),
	REPAIR_IN("Repair In", KeyEvent.VK_K,RepairInPanel.class);

	private String label;
	private int keyEvent;
	private Class<? extends QicsPanel> panelClass;

	private QicsViewId() {
		this.label = name();
	}

	private QicsViewId(String label, int keyEvent,Class<? extends QicsPanel> panelClass) {
		this.label = label;
		this.keyEvent = keyEvent;
		this.panelClass = panelClass;
	}

	public static QicsViewId getValueOf(String name) {

		QicsViewId value = IDLE;
		if (name == null) {
			return value;
		}
		try {
			value = QicsViewId.valueOf(name.trim());
		} catch (Exception e) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("Info: " + QicsViewId.class.getSimpleName()).append(" enumaration value for '").append(name + "' does not exist, returning default '" + IDLE.toString() + "'.");
			System.err.println(stringBuilder.toString());
		}
		return value;
	}

	public String getLabel() {
		return label;
	}

	public int getKeyEvent() {
		return keyEvent;
	}
	
	public Class<? extends QicsPanel> getPanelClass() {
		return panelClass;
	}
	
	public QicsPanel createQicsPanel(QicsFrame frame) {
		return ReflectionUtils.createInstance(panelClass, new Class<?>[] {QicsFrame.class}, frame);
	}
}
