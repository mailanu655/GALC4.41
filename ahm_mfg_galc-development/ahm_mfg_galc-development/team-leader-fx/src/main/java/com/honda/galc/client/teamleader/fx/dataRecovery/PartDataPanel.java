package com.honda.galc.client.teamleader.fx.dataRecovery;

import java.util.ArrayList;
import java.util.List;

import org.tbee.javafx.scene.layout.MigPane;

import javafx.scene.Node;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.LC;

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
 * @author L&T Infotech<br>
 *         July 15, 2017
 * 
 * 
 */
public class PartDataPanel extends MigPane {

	private List<PartEditPanel> partEditPanels = new ArrayList<PartEditPanel>();
	private DataRecoveryController controller;

	public PartDataPanel(DataRecoveryController controller) {
		super();
		this.controller = controller;
		this.setLayoutConstraints(new LC().insets("2").alignX("left"));
		this.setColumnConstraints(new AC().gap("2").align("left", 5)); 
	}

	public void add(Node node, Object constraint) {
		if (node instanceof PartEditPanel && !getPartPanels().contains(node)) {
			getPartPanels().add((PartEditPanel) node);
		}
		add(node, constraint);
	}

	public void remove(Node node) {
		super.getChildren().remove(node);
		getPartPanels().remove(node);
	}

	public void addPartDefinition(PartDefinition partDefinition) {
		PartEditPanel panel = new PartEditPanel(this, partDefinition);
		add(panel, "wrap ");
		if (!getPartPanels().contains(panel)) {
			getPartPanels().add((PartEditPanel) panel);
		}
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

	protected List<PartEditPanel> getPartPanels() {
		return partEditPanels;
	}


}
