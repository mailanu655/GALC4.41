package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>PartDataPanel Class description</h3>
 * <p>
 * PartDataPanel description
 * </p>
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
 *         Dec 21, 2011
 * 
 * 
 */
public class PartDataPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<PartEditPanel> partEditPanels = new ArrayList<PartEditPanel>();
	private DataRecoveryController controller;
	private int maxRowHeight = 65;

	public PartDataPanel(DataRecoveryController controller) {
		this.controller = controller;
		setLayout(new MigLayout("insets 0, gap 0", "[max,fill]", String.format("[::%s]", getMaxRowHeight())));
		setSize(Utils.getDataPanelWidth(), Utils.getDataPanelHeight());
	}

	@Override
	public void add(Component comp, Object constraint) {
		if (comp instanceof PartEditPanel && !getPartPanels().contains(comp)) {
			getPartPanels().add((PartEditPanel) comp);
		}
		super.add(comp, constraint);
	}

	@Override
	public void remove(Component comp) {
		super.remove(comp);
		getPartPanels().remove(comp);
	}

	public void addPartDefinition(PartDefinition partDefinition) {
		PartEditPanel panel = new PartEditPanel(this, partDefinition);
		add(panel, "wrap");
	}

	public String[] getPartNames() {
		List<String> partNames = new ArrayList<String>();
		for (PartEditPanel panel : getPartPanels()) {
			partNames.addAll(panel.getPartNames());
		}
		return partNames.toArray(new String[partNames.size()]);
	}

	public DataRecoveryController getController() {
		return controller;
	}

	public void setIdleMode() {
		for (PartEditPanel panel : getPartPanels()) {
			panel.setIdleMode();
		}
	}

	public void setInputMode() {
		for (PartEditPanel panel : getPartPanels()) {
			panel.setReadOnlyMode();
		}
	}

	protected int getMaxRowHeight() {
		return maxRowHeight;
	}

	protected List<PartEditPanel> getPartPanels() {
		return partEditPanels;
	}
}
