package com.honda.galc.client.teamleader.tracking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.product.controller.listener.InputNumberChangeListener;
import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LimitedLengthPlainDocument;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.PropertyComboBoxRenderer;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TrackingMaintenancePanel</code> is ... .
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
 * @created Aug 2, 2013
 */
public class TrackingMaintenancePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	private String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	private JComboBox productTypeComboBox;
	private JTextField inputNumberTextField;
	private JTextField productLineTextField;
	private JTextField productProcessPointTextField;
	private JTextField moveReasonTextField;
	private JTextField associateIdTextField;
	private JTree plantStrucureTree;
	private ObjectTablePane<Map<String, Object>> productHistoryPane;
	private JButton submitButton;
	private JButton doneButton;

	private TrackingMaintenanceController controller;

	public TrackingMaintenancePanel(TabbedMainWindow mainWindow) {
		super("Tracking", KeyEvent.VK_T, mainWindow);
		this.controller = new TrackingMaintenanceController(this);
		initView();
		mapActions();
		initData();
	}

	@Override
	public void onTabSelected() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getInputNumberTextField().requestFocus();
			}
		});
	}

	public void actionPerformed(ActionEvent arg0) {

	}

	protected void initView() {
		setLayout(new GridLayout(1, 1));
		this.productHistoryPane = createProductHistoryPanel();

		JSplitPane bottomPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getProductHistoryPane(), createTreePanel());
		bottomPanel.setDividerLocation(600);
		bottomPanel.setOneTouchExpandable(true);

		JSplitPane pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createInputPanel(), bottomPanel);
		pane.setDividerLocation(110);
		pane.setOneTouchExpandable(true);
		add(pane);
	}

	protected ObjectTablePane<Map<String, Object>> createProductHistoryPanel() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Line Name", "line.lineName");
		mapping.put("Line ID", "line.id");
		mapping.put("PP Name", "processPoint.processPointName");
		final int processPointIdIx = 4;
		mapping.put("PP ID", "processPoint.id");
		mapping.put("Actual Timestamp", "history.actualTimestamp", new SimpleDateFormat(getDateTimePattern()));
		final ObjectTablePane<Map<String, Object>> pane = new ObjectTablePane<Map<String, Object>>(mapping.get(), true, true);
		pane.getTable().setName("productHistoryPanel");

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				BaseProduct product = getController().getProduct();
				if (product != null && product.getLastPassingProcessPointId() != null) {
					Object v = table.getValueAt(row, processPointIdIx);
					if (v != null && v.equals(product.getLastPassingProcessPointId())) {
						setBackground(Color.GREEN);
					} else {
						setBackground(Color.WHITE);
					}
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};

		pane.setCellRenderer(renderer);
		return pane;
	}

	protected JScrollPane createTreePanel() {
		this.plantStrucureTree = new JTree() {
			private static final long serialVersionUID = 1L;

			@Override
			public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				if (value instanceof DefaultMutableTreeNode) {
					String patern = "%s : %s";
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
					if (node.getUserObject() instanceof ProductType) {
						ProductType pt = (ProductType) node.getUserObject();
						return pt.getProductName();
					} else if (node.getUserObject() instanceof Division) {
						Division division = (Division) node.getUserObject();
						return String.format(patern, division.getDivisionName(), division.getDivisionId());
					} else if (node.getUserObject() instanceof Line) {
						Line line = (Line) node.getUserObject();
						return String.format(patern, line.getLineName(), line.getLineId());
					} else if (node.getUserObject() instanceof ProcessPoint) {
						ProcessPoint pp = (ProcessPoint) node.getUserObject();
						return String.format(patern, pp.getProcessPointName(), pp.getProcessPointId());
					}
				}
				return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
			}

		};

		getPlantStrucureTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));

		DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
				Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				if (value instanceof DefaultMutableTreeNode) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
					Object userObject = node.getUserObject();
					
					if (userObject instanceof ProcessPoint) {
						ProcessPoint pp = (ProcessPoint) userObject;
						if (!pp.isTrackingPoint()) {
							component.setForeground(Color.GRAY);
						}
					}
					if (getController().isTrackingStatusValid()) {
						if (getController().getTrackingPath().contains(userObject)) {
							component.setForeground(new Color(0, 150, 0));
						}
					} else {
						if (getController().getTrackingPath().contains(userObject)) {
							component.setForeground(new Color(255, 0, 0));
						}
					}
				}
				return component;
			}
		};
		getPlantStrucureTree().setCellRenderer(renderer);
		getPlantStrucureTree().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		JScrollPane panel = new JScrollPane(getPlantStrucureTree());
		getPlantStrucureTree().setFont(new Font("Dialog", Font.BOLD, 11));
		return panel;
	}

	protected JPanel createInputPanel() {
		JPanel panel = new JPanel();
		UiFactory factory = UiFactory.create(Fonts.DIALOG_BOLD_12, new Font("Dialog", Font.BOLD, 13), Fonts.DIALOG_BOLD_12);
		panel.setLayout(new MigLayout("insets 0", "[max, fill][max, fill]"));
		this.productTypeComboBox = new JComboBox();

		PropertyComboBoxRenderer<ProductType> renderer = new PropertyComboBoxRenderer<ProductType>(ProductType.class, "productName");
		getProductTypeComboBox().setRenderer(renderer);
		getProductTypeComboBox().setFont(UiFactory.getInputSmall().getInputFont());

		this.inputNumberTextField = UiFactory.getInputLarge().createTextField(17, TextFieldState.EDIT);
		this.productLineTextField = factory.createTextField(TextFieldState.DISABLED);
		this.productProcessPointTextField = factory.createTextField(TextFieldState.DISABLED);
		this.moveReasonTextField = factory.createTextField(TextFieldState.DISABLED);
		getMoveReasonTextField().setDocument(new LimitedLengthPlainDocument(80));
		this.associateIdTextField = factory.createTextField(TextFieldState.DISABLED);

		this.submitButton = factory.createButton("Move", false);
		this.doneButton = factory.createButton("Done", false);
		getSubmitButton().setFont(Fonts.DIALOG_PLAIN_22);
		getDoneButton().setFont(getSubmitButton().getFont());

		JPanel infoPanel = new JPanel(new MigLayout("", "[][max,fill]"));

		infoPanel.add(getProductTypeComboBox());
		infoPanel.add(getInputNumberTextField(), "wrap");
		infoPanel.add(factory.createLabel("Current Line"));
		infoPanel.add(getProductLineTextField(), "wrap");
		infoPanel.add(factory.createLabel("Process Point"));
		infoPanel.add(getProductProcessPointTextField());

		JPanel inputPanel = new JPanel(new MigLayout("", "[][max,fill][][fill]"));
		inputPanel.add(factory.createLabel("Associate Id"));
		inputPanel.add(getAssociateIdTextField(), "");
		inputPanel.add(getSubmitButton(), "width 120!, height 50");
		inputPanel.add(getDoneButton(), "width 120!, height 50, wrap 20");

		inputPanel.add(factory.createLabel("Move Reason"));
		inputPanel.add(getMoveReasonTextField(), "span 3");

		panel.add(infoPanel);
		panel.add(inputPanel, "wrap");

		return panel;
	}

	protected void initData() {
		Set<ProductType> types = getController().getProductTypeDivisionMapping().keySet();
		for (ProductType type : types) {
			getProductTypeComboBox().addItem(type);
		}
	}

	protected void mapActions() {
		getProductTypeComboBox().addActionListener(getController());
		getInputNumberTextField().addActionListener(getController());
		getInputNumberTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getInputNumberTextField()));
		getMoveReasonTextField().getDocument().addDocumentListener(new InputNumberChangeListener(this, getMoveReasonTextField()));
		getPlantStrucureTree().addTreeSelectionListener(getController());

		getSubmitButton().addActionListener(getController());
		getDoneButton().addActionListener(getController());
	}

	// === get/set === //
	protected JComboBox getProductTypeComboBox() {
		return productTypeComboBox;
	}

	protected JTextField getInputNumberTextField() {
		return inputNumberTextField;
	}

	protected JTextField getProductLineTextField() {
		return productLineTextField;
	}

	protected JTextField getProductProcessPointTextField() {
		return productProcessPointTextField;
	}

	protected JTextField getMoveReasonTextField() {
		return moveReasonTextField;
	}

	protected JTextField getAssociateIdTextField() {
		return associateIdTextField;
	}

	protected JButton getSubmitButton() {
		return submitButton;
	}

	protected JButton getDoneButton() {
		return doneButton;
	}

	protected ObjectTablePane<Map<String, Object>> getProductHistoryPane() {
		return productHistoryPane;
	}

	protected String getDateTimePattern() {
		return dateTimePattern;
	}

	protected TrackingMaintenanceController getController() {
		return controller;
	}

	protected JTree getPlantStrucureTree() {
		return plantStrucureTree;
	}
}
