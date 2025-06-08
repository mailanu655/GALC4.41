package com.honda.galc.client.teamleader.bearing;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.entity.bearing.BearingPart;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingModelAssignmentPanel</code> is ... .
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
 * @created May 9, 2013
 */
public class BearingModelMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox bearingTypeComboBox;
	private JComboBox yearModelComboBox;

	private ObjectTablePane<BearingPart> bearingPanel;
	private ObjectTablePane<BearingPart> assignedBearingPanel;

	private JPopupMenu bearingPanelPopupMenu;
	private JPopupMenu assignedBearingPanelPopupMenu;

	private JComboBox mainBearingCountComboBox;
	private JComboBox conrodBearingCountComboBox;
	private JButton saveBearingCountConfigButton;
	private JButton saveConCrankConfigButton;
	
	private JTextField conrodRankIndex;
	private JTextField conrodWeightIndex;
	private JTextField crankConIndex;
	private JTextField CrankMainIndex;

	private BearingModelMaintenanceController controller;

	public BearingModelMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Bearing Model Assignment", KeyEvent.VK_A, mainWindow);
		this.controller = new BearingModelMaintenanceController(this);

		initView();
		initData();
		mapActions();
	}

	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	protected void initView() {
		UiFactory factory = UiFactory.getInput();
		this.bearingTypeComboBox = new JComboBox();
		this.yearModelComboBox = createYearModelComboBox();

		this.bearingPanel = createBearingPanel();
		this.assignedBearingPanel = createAssignedBearingPanel();
		this.bearingPanelPopupMenu = createBearingPanelPopupMenu();
		this.assignedBearingPanelPopupMenu = createAssignedBearingPanelPopupMenu();

		this.mainBearingCountComboBox = createBearingCountValuesComboBox();
		this.conrodBearingCountComboBox = createBearingCountValuesComboBox();
		this.saveBearingCountConfigButton = UiFactory.createButton("Save", Fonts.DIALOG_PLAIN_18, false);
		
		this.conrodRankIndex = createTextfield();
		this.conrodWeightIndex = createTextfield();
		this.crankConIndex = createTextfield();
		this.CrankMainIndex = createTextfield();
		this.saveConCrankConfigButton = UiFactory.createButton("Save", Fonts.DIALOG_PLAIN_18, false);
		
		
		setLayout(new MigLayout("", "[][150!,fill][]5[]5[120!,fill][]"));

		add(factory.createLabel("Bearing Type"));
		add(getBearingTypeComboBox());

		add(factory.createLabel("Year Model"), new CC().cell(3));
		add(getYearModelComboBox(), "wrap");
		add(getBearingPanel(), new CC().spanX(3).spanY(2).grow().height("max").width("max"));
		
		JPanel p = new JPanel(new MigLayout("", "[52!, align right][max,fill]20[align right][max,fill][max,fill]"));
		p.add(factory.createLabel("Main"));
		p.add(getMainBearingCountComboBox());
		p.add(factory.createLabel("Conrod"));
		p.add(getConrodBearingCountComboBox()/*, new CC().width("max")*/);
		p.add(getSaveBearingCountConfigButton(), new CC().height("40!")/*.width("max")*/);
		p.setBorder(BorderFactory.createTitledBorder("Bearing Count"));
		add(p, new CC().span(3).wrap().width("max"));
		
		JPanel p1 = new JPanel(new GridLayout(5,2,0,5));
	
		p1.add(factory.createLabel("Conrod Weight Index"));
		p1.add( getConrodWeightIndex());
		
	
		p1.add(factory.createLabel("Conrod Rank Index"));
		p1.add(getConrodRankIndex());
		
		p1.add(factory.createLabel("Crankshaft Conrod Index"));
		p1.add(getCrankConIndex());
		p1.add(factory.createLabel("Crankshaft Main Index"));
		p1.add(getCrankMainIndex());
		p1.add(getSaveConCrankConfigButton(),  new CC().height("40!"));
		
		
		p1.setBorder(BorderFactory.createTitledBorder("Bearing CrankShaft/Conrod"));
		add(p1, new CC().span(3).wrap().width("380!").alignY("20"));
		add(getAssignedBearingPanel(), new CC().spanX(3).grow().height("max").cell(3,5));
	}

	private JTextField createTextfield() {
		JTextField textField = new JTextField();
		textField.setSize(5, 10);
		
		return textField;
	}

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

	protected JComboBox createBearingCountValuesComboBox() {
		JComboBox comboBox = new JComboBox();
		comboBox.setEnabled(false);
		((JLabel) comboBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		return comboBox;
	}

	protected ObjectTablePane<BearingPart> createBearingPanel() {
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

	protected JPopupMenu createBearingPanelPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add("Assign to Year Model").addActionListener(getController());
		popup.addSeparator();
		popup.add("Update Selected Bearing").addActionListener(getController());
		popup.add("Create Bearing").addActionListener(getController());
		popup.add("Delete Selected Bearing").addActionListener(getController());
		return popup;
	}

	protected JPopupMenu createAssignedBearingPanelPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add("Remove from Year Model").addActionListener(getController());
		return popup;
	}

	protected void initData() {
		Vector<String> bearingTypes = new Vector<String>(Arrays.asList(getController().getBearingTypes()));
		bearingTypes.add(0, null);
		getBearingTypeComboBox().setModel(new DefaultComboBoxModel(bearingTypes));
		getBearingTypeComboBox().setSelectedIndex(-1);

		List<Map<String, String>> ymCodesList = getController().selectYearModelCodes();
		getYearModelComboBox().setModel(new DefaultComboBoxModel(ymCodesList.toArray()));
		getYearModelComboBox().setSelectedIndex(-1);
		getController().selectBearings();

		getMainBearingCountComboBox().setModel(new DefaultComboBoxModel(getController().getMainBearingCountValues()));
		getMainBearingCountComboBox().setSelectedIndex(-1);
		getConrodBearingCountComboBox().setModel(new DefaultComboBoxModel(getController().getConrodCountValues()));
		getConrodBearingCountComboBox().setSelectedIndex(-1);
	}

	protected void mapActions() {

		getBearingTypeComboBox().addActionListener(getController());
		getYearModelComboBox().addActionListener(getController());
		getMainBearingCountComboBox().addActionListener(getController());
		getConrodBearingCountComboBox().addActionListener(getController());
		getSaveBearingCountConfigButton().addActionListener(getController());
		getSaveConCrankConfigButton().addActionListener(getController());
		MouseListener bearingPanelMouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean yearModelSelected = getYearModelComboBox().getSelectedItem() != null;
					boolean bearingsSelected = getBearingPanel().getTable().getSelectedRowCount() > 0;
					boolean oneBearingSelected = getBearingPanel().getTable().getSelectedRowCount() == 1;
					getBearingPanelPopupMenu().getSubElements()[0].getComponent().setEnabled(bearingsSelected && yearModelSelected);
					getBearingPanelPopupMenu().getSubElements()[1].getComponent().setEnabled(oneBearingSelected);
					getBearingPanelPopupMenu().getSubElements()[3].getComponent().setEnabled(oneBearingSelected);
					getBearingPanelPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		getBearingPanel().addMouseListener(bearingPanelMouseListener);
		getBearingPanel().getTable().addMouseListener(bearingPanelMouseListener);

		MouseListener assignedBearingPanelMouseListener = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					boolean assignedBearingsSelected = getAssignedBearingPanel().getTable().getSelectedRowCount() > 0;
					getAssignedBearingPanelPopupMenu().getSubElements()[0].getComponent().setEnabled(assignedBearingsSelected);
					getAssignedBearingPanelPopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};

		getAssignedBearingPanel().addMouseListener(assignedBearingPanelMouseListener);
		getAssignedBearingPanel().getTable().addMouseListener(assignedBearingPanelMouseListener);

	}

	public ObjectTablePane<BearingPart> getBearingPanel() {
		return bearingPanel;
	}

	public BearingModelMaintenanceController getController() {
		return controller;
	}

	public JComboBox getYearModelComboBox() {
		return yearModelComboBox;
	}

	public JComboBox getBearingTypeComboBox() {
		return bearingTypeComboBox;
	}

	public ObjectTablePane<BearingPart> getAssignedBearingPanel() {
		return assignedBearingPanel;
	}

	public JPopupMenu getBearingPanelPopupMenu() {
		return bearingPanelPopupMenu;
	}

	public JPopupMenu getAssignedBearingPanelPopupMenu() {
		return assignedBearingPanelPopupMenu;
	}

	protected JComboBox getMainBearingCountComboBox() {
		return mainBearingCountComboBox;
	}

	protected JComboBox getConrodBearingCountComboBox() {
		return conrodBearingCountComboBox;
	}

	protected JButton getSaveBearingCountConfigButton() {
		return saveBearingCountConfigButton;
	}
	
	protected JButton getSaveConCrankConfigButton() {
		return saveConCrankConfigButton;
	}

	public JTextField getConrodRankIndex() {
		return conrodRankIndex;
	}

	public JTextField getConrodWeightIndex() {
		return conrodWeightIndex;
	}

	public JTextField getCrankConIndex() {
		return crankConIndex;
	}

	public JTextField getCrankMainIndex() {
		return CrankMainIndex;
	}
	
	
}