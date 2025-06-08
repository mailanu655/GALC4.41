package com.honda.galc.client.teamleader.bearing;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.process.engine.bearing.pick.model.BearingPickModel;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingPartType;
import com.honda.galc.entity.bearing.BearingType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixSetupPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created May 14, 2013
 */
public abstract class BearingMatrixMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox yearModelComboBox;
	private JComboBox modelTypeComboBox;
	private JComboBox bearingTypeComboBox;
	private JComboBox journalPositionComboBox;
	private ObjectTablePane<BearingPart> assignedBearingPanel;
	private BearingMatrixPanel matrixComponent;

	private JButton saveButton;
	private JButton resetButton;

	private BearingMatrixMaintenanceController controller;
	private BearingType bearingType;
	private Map<String, Color> bearingColors;

	public BearingMatrixMaintenancePanel(BearingType bearingType, String title, int tabKey, TabbedMainWindow mainWindow) {
		super(title, tabKey, mainWindow);
		this.bearingType = bearingType;
		this.controller = new BearingMatrixMaintenanceController(this);
		this.bearingColors = new BearingPickModel(getMainWindow().getApplicationContext()).getColors();
		initView();
		initData();
		mapActions();
	}

	// === abstract api === //
	public abstract String[] getColumnValues();
	public abstract String[] getRowValues();
	public abstract String[] getBearingTypeOptionValues();

	// === === //
	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {

	}
	
	protected void initView() {
		UiFactory factory = UiFactory.getInput();

		this.yearModelComboBox = createYearModelComboBox();
		this.modelTypeComboBox = new JComboBox();
		this.journalPositionComboBox = new JComboBox();
		this.bearingTypeComboBox = new JComboBox();

		this.assignedBearingPanel = createAssignedBearingPanel();
		this.matrixComponent = new BearingMatrixPanel(this, getBearingType(), getColumnValues(), getRowValues());

		this.saveButton = UiFactory.createButton("Save", Fonts.DIALOG_PLAIN_18, false);
		this.resetButton = UiFactory.createButton("Reset", Fonts.DIALOG_PLAIN_18, false);

		JPanel leftPanel = new JPanel(new MigLayout("", ""));
		setLayout(new MigLayout("", "[max,fill]"));

		JPanel selectionPanel = new JPanel(new MigLayout("", "[max,fill]"));
		selectionPanel.add(factory.createLabel("Year Model", JLabel.RIGHT));
		selectionPanel.add(getYearModelComboBox());
		selectionPanel.add(factory.createLabel("Model Type", JLabel.RIGHT));
		selectionPanel.add(getModelTypeComboBox(), "wrap");
		selectionPanel.add(factory.createLabel("Bearing Position", JLabel.RIGHT));
		selectionPanel.add(getJournalPositionComboBox());
		selectionPanel.add(factory.createLabel("Bearing Type", JLabel.RIGHT));
		selectionPanel.add(getBearingTypeComboBox(), "wrap");

		leftPanel.add(selectionPanel, new CC().spanX(2).wrap());
		leftPanel.add(getAssignedBearingPanel(), new CC().spanX(2).height("max").wrap());
		leftPanel.add(getResetButton(), new CC().minHeight("40").width("max"));
		leftPanel.add(getSaveButton(), new CC().minHeight("40").width("max"));

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, getMatrixComponent());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(420);

		add(splitPane);
	}

	protected void initData() {
		getController().loadYearModels();
	}

	protected void mapActions() {
		getYearModelComboBox().addActionListener(getController());
		getModelTypeComboBox().addActionListener(getController());
		getJournalPositionComboBox().addActionListener(getController());
		getBearingTypeComboBox().addActionListener(getController());

		getSaveButton().addActionListener(getController());
		getResetButton().addActionListener(getController());

		for (JTextField field : getMatrixComponent().getBearingPartFields()) {
			field.addMouseListener(getController());
		}
	}

	// === bl api === //
	public void loadMatrix(List<BearingPart> bearingParts, List<BearingMatrixCell> matrixCells) {
		getMatrixComponent().loadData(bearingParts, matrixCells);
	}

	public void clearMatrix() {
		getMatrixComponent().clearData();
	}

	public void resetButtons() {
		boolean dirty = getMatrixComponent().isUpdated();
		getSaveButton().setEnabled(dirty);
		getResetButton().setEnabled(dirty);
	}

	public void setBearingPart(JTextField field, BearingPart bearingPart) {
		BearingPartType bearingPartType = getBearingPartType(field);
		if (bearingPartType == null) {
			return;
		}
		if (BearingPartType.Lower.equals(bearingPartType)) {
			getMatrixComponent().setLowerBearingPart(field, bearingPart);
		} else if (BearingPartType.Upper.equals(bearingPartType)) {
			getMatrixComponent().setUpperBearingPart(field, bearingPart);
		}
	}

	public BearingPartType getBearingPartType(JTextField field) {
		if (getMatrixComponent().getLowerBearingPartFields().contains(field)) {
			return BearingPartType.Lower;
		}
		if (getMatrixComponent().getUpperBearingPartFields().contains(field)) {
			return BearingPartType.Upper;
		}
		return null;
	}

	// === factory methods === //
	protected JComboBox createYearModelComboBox() {
		JComboBox cb = new JComboBox();
		ListCellRenderer renderer = new BasicComboBoxRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof Map) {
					Map<?, ?> item = (Map<?, ?>) value;
					Object year = item.get("modelYearCode");
					Object model = item.get("modelCode");
					value = String.format("%s%s", year, model);
				}
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		};
		cb.setRenderer(renderer);
		return cb;
	}

	protected ObjectTablePane<BearingPart> createAssignedBearingPanel() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Bearing Number", "id");
		mapping.put("Bearing Type", "type");
		mapping.put("Bearing Color", "color");
		ObjectTablePane<BearingPart> panel = new ObjectTablePane<BearingPart>(mapping.get(), true, true);
		panel.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.getTable().setName("bearingPanel");
		return panel;
	}

	// === get/set === //
	public String getSelectedModelYearCode() {
		Map<?, ?> map = (Map<?, ?>) getYearModelComboBox().getSelectedItem();
		if (map == null) {
			return null;
		}
		return (String) map.get("modelYearCode");
	}

	public String getSelectedModelCode() {
		Map<?, ?> map = (Map<?, ?>) getYearModelComboBox().getSelectedItem();
		if (map == null) {
			return null;
		}
		return (String) map.get("modelCode");
	}

	protected JComboBox getYearModelComboBox() {
		return yearModelComboBox;
	}

	protected ObjectTablePane<BearingPart> getAssignedBearingPanel() {
		return assignedBearingPanel;
	}

	public BearingMatrixPanel getMatrixComponent() {
		return matrixComponent;
	}

	protected BearingMatrixMaintenanceController getController() {
		return controller;
	}

	protected Map<String, Color> getBearingColors() {
		return bearingColors;
	}

	public Color getBearingColor(String colorName) {
		return getBearingColors().get(colorName);
	}

	public JComboBox getModelTypeComboBox() {
		return modelTypeComboBox;
	}

	protected JButton getSaveButton() {
		return saveButton;
	}

	protected JButton getResetButton() {
		return resetButton;
	}

	public BearingType getBearingType() {
		return bearingType;
	}

	public JComboBox getJournalPositionComboBox() {
		return journalPositionComboBox;
	}

	public JComboBox getBearingTypeComboBox() {
		return bearingTypeComboBox;
	}
}
