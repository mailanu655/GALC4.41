package com.honda.galc.client.qics.view.screen;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel;
import com.honda.galc.client.qics.view.fragments.UnitInfoIdlePanel.UnitInfoConfig;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.entity.qics.StationResult;

/**
 * <h3>Class description</h3>
 * This is part of original IdlePanel. The IdlePanel was modified
 * to use tabs so that multiple panels can be displayed. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>May 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130522</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class QicsStationInfoPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;

	private static final int LABEL_WIDTH = 165;
	private static final int TEXT_FIELD_WIDTH = 200;
	private static final int LABEL_WIDTH_LONG = 365;
	private static final int TEXT_FIELD_WIDTH_LONG = 400;
	private static final int LABEL_WIDTH_SHORT = 340;
	private static final int TEXT_FIELD_WIDTH_SHORT = 140;
	private static final int ELEMENT_HEIGHT_SHORT = 40;

	private JPanel productNumbersGroupPanel;
	private UnitInfoIdlePanel firstProductPanel;
	private UnitInfoIdlePanel lastProductPanel;
	private JPanel dataGroupPanel;

	private List<UnitInfoIdlePanel> dataGroupPanelElements;

	public QicsStationInfoPanel(MainWindow window) {
		super((QicsFrame) window);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.IDLE;
	}

	protected void initialize() {

		setLayout(null);
		setSize(1024, 525);

		setEnabled(true);
		setDataGroupPanelElements(createDataGroupPanelElements());

		firstProductPanel = createLongUnitInfoIdlePanel("First ");
		lastProductPanel = createLongUnitInfoIdlePanel("Last ");

		productNumbersGroupPanel = createProductNumbersGroupPanel();
		dataGroupPanel = createDataGroupPanel();

		add(getProductNumbersGroupPanel());

		add(getDataGroupPanel());

		mapActions();
		mapEventHandlers();

	}

	// === controlling api == //
	@Override
	public void startPanel() {
		setStationInfoValues();
		resetPanel();
	}

	protected String toString(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	protected String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	protected void setStationInfoValues() {

		StationResult result = getQicsController().getClientModel().getStationResult();
		if (result == null) {
			return;
		}

		getFirstProductPanel().setText(toString(result.getFirstProductId()));
		getLastProductPanel().setText(toString(result.getLastProductId()));

		for (UnitInfoIdlePanel panel : getDataGroupPanelElements()) {
			panel.setTextValue(result);
		}
	}

	public void resetPanel() {
		
		
	}

	protected JPanel getProductNumbersGroupPanel() {
		return productNumbersGroupPanel;
	}

	protected UnitInfoIdlePanel getFirstProductPanel() {
		return firstProductPanel;
	}

	protected UnitInfoIdlePanel getLastProductPanel() {
		return lastProductPanel;
	}

	protected List<UnitInfoIdlePanel> getDataGroupPanelElements() {
		return dataGroupPanelElements;
	}

	protected void setDataGroupPanelElements(List<UnitInfoIdlePanel> dataGroupPanelElements) {
		this.dataGroupPanelElements = dataGroupPanelElements;
	}

	protected JPanel getDataGroupPanel() {
		return dataGroupPanel;
	}

	protected List<UnitInfoConfig> getIdlePanelCalculationConfigs() {
		return getClientConfig().getIdlePanelCalculationConfigs();
	}

	protected JPanel createProductNumbersGroupPanel() {
		JPanel panel = new JPanel();
		List<UnitInfoIdlePanel> elements = getProductNumbersGroupPanelElements();
		panel = createGroupPanel(panel, elements);
		panel.setLocation(getAlignment(panel.getWidth()), 5);
		return panel;
	}

	protected List<UnitInfoIdlePanel> getProductNumbersGroupPanelElements() {
		List<UnitInfoIdlePanel> elements = new ArrayList<UnitInfoIdlePanel>();
		elements.add(getFirstProductPanel());
		elements.add(getLastProductPanel());
		return elements;
	}

	protected JPanel createDataGroupPanel() {
		JPanel panel = new JPanel();
		List<UnitInfoIdlePanel> elements = getDataGroupPanelElements();
		panel = createGroupPanel(panel, elements);
		int y = getProductNumbersGroupPanel().getY() + getProductNumbersGroupPanel().getHeight();
		int height = getHeight() - y;
		int width = panel.getWidth();
		if (getProductNumbersGroupPanel() != null) {
			int productPanelWidth = getProductNumbersGroupPanel().getWidth();
			if (width < productPanelWidth) {
				width = productPanelWidth;
			}
		}
		panel.setSize(width, height);
		panel.setLocation(getAlignment(panel.getWidth()), y);

		return panel;
	}

	protected List<UnitInfoIdlePanel> createDataGroupPanelElements() {
		dataGroupPanelElements = new ArrayList<UnitInfoIdlePanel>();

		for (UnitInfoConfig config : getIdlePanelCalculationConfigs()) {
			dataGroupPanelElements.add(createShortUnitInfoIdlePanel(config));
		}

		return dataGroupPanelElements;
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(UnitInfoConfig config, String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_SHORT, TEXT_FIELD_WIDTH_SHORT, ELEMENT_HEIGHT_SHORT);
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(UnitInfoConfig config) {
		String labelText = getQicsController().getProductTypeData().getProductTypeLabel() + "s " + config.getName() + " ";
		UnitInfoIdlePanel panel = createShortUnitInfoIdlePanel(labelText);
		panel.setConfig(config);
		return panel;
	}

	protected UnitInfoIdlePanel createLongUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH, TEXT_FIELD_WIDTH_LONG);
	}

	protected UnitInfoIdlePanel createUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_LONG, TEXT_FIELD_WIDTH);
	}

	protected UnitInfoIdlePanel createShortUnitInfoIdlePanel(String label) {
		return new UnitInfoIdlePanel(label, LABEL_WIDTH_SHORT, TEXT_FIELD_WIDTH_SHORT, ELEMENT_HEIGHT_SHORT);
	}

	protected int getAlignment(int elementWidth) {
		int width = this.getWidth();
		int x = 0;
		if (elementWidth > 0 && elementWidth < width) {
			x = x + (width - elementWidth) / 2;
		}
		return x;
	}

	protected JPanel createGroupPanel(JPanel groupPanel, List<UnitInfoIdlePanel> elements) {

		if (groupPanel == null || elements == null) {
			return groupPanel;
		}
		if (!elements.isEmpty()) {

			for (UnitInfoIdlePanel component : elements) {
				groupPanel.add(component);
			}

			UnitInfoIdlePanel element = elements.get(0);
			int w = element.getWidth();
			int h = element.getHeight();
			int count = groupPanel.getComponentCount();
			int maxNumOfRows = 7;
			if (count > maxNumOfRows) {
				w = w * 2;
				int height = (int) ((count * h) / 2);
				groupPanel.setSize(w, height);
				groupPanel.setLayout(new GridLayout(maxNumOfRows, 1, 0, 0));
			} else {
				groupPanel.setSize(w, count * h);
				groupPanel.setLayout(new GridLayout(count, 1, 0, 0));
			}

		}
		groupPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		return groupPanel;
	}

	
}
