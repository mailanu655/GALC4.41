package com.honda.galc.client.teamleader.let;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.view.UiFactory;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.LetConnectedProgramDao;
import com.honda.galc.dao.product.LetInspectionProgramDao;
import com.honda.galc.dao.product.LetPassCriteriaProgramDao;
import com.honda.galc.dao.product.LetProgramCategoryDao;
import com.honda.galc.entity.product.LetConnectedProgram;
import com.honda.galc.entity.product.LetConnectedProgramId;
import com.honda.galc.entity.product.LetInspectionProgram;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.entity.product.LetProgramCategory;
import com.honda.galc.entity.product.LetProgramCategoryId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.ObjectComparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ConnectedProgramDialog</code> is ... .
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created Jan 5, 2018
 */
public class ConnectedProgramDialog extends JDialog implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private Map<LetProgramCategoryId, LetProgramCategory> categoryIx;
	private Map<String, LetInspectionProgram> inspectionProgramIx;
	private Map<String, LetPassCriteriaProgram> criteriaProgramIx;

	private ObjectTablePane<LetPassCriteriaProgram> programPanel;
	private ObjectTablePane<LetInspectionProgram> triggeringProgramPanel;
	private ObjectTablePane<LetConnectedProgram> connectedProgramPanel;

	private JButton connectButton;
	private JButton disonnectButton;
	private JButton closeButton;

	// === construct === //
	public ConnectedProgramDialog() {
		super();
		this.categoryIx = new LinkedHashMap<LetProgramCategoryId, LetProgramCategory>();
		this.inspectionProgramIx = new HashMap<String, LetInspectionProgram>();
		this.criteriaProgramIx = new HashMap<String, LetPassCriteriaProgram>();
		initUi();
		mapListeners();
		loadData();
		add(createCategoryLegend(), "cell 0 2, hmax 50");
	}

	// === init === //
	protected void initUi() {
		setTitle("Connected Programs Maintenance");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(950, 550);
		setLayout(new MigLayout("insets 15 5 5 5", "[max][max][max][max]", "[max][50]"));

		PropertiesMapping trgColumnMappings = new PropertiesMapping();
		trgColumnMappings.put("#", "row_reverted");
		trgColumnMappings.put("Program Name", "inspectionPgmName");
		this.triggeringProgramPanel = new ObjectTablePane<LetInspectionProgram>("Select Triggering Program", trgColumnMappings.get(), true, true);

		PropertiesMapping terColumnMappings = new PropertiesMapping();
		terColumnMappings.put("#", "row_reverted");
		terColumnMappings.put("Program Name", "criteriaPgmName");
		this.programPanel = new ObjectTablePane<LetPassCriteriaProgram>("Select Program to Terminate", terColumnMappings.get(), true, true);

		PropertiesMapping conColumnMappings = new PropertiesMapping();
		conColumnMappings.put("#", "row_reverted");
		conColumnMappings.put("Triggering Program", "triggeringProgram.inspectionPgmName");
		conColumnMappings.put("Terminated Program", "program.criteriaPgmName");
		this.connectedProgramPanel = new ObjectTablePane<LetConnectedProgram>("Connected Programs", conColumnMappings.get(), true, true);

		this.connectButton = UiFactory.getInfoSmall().createButton("Connect >>");
		this.disonnectButton = UiFactory.getInfoSmall().createButton("<< Disconnect");
		this.closeButton = UiFactory.getInfoSmall().createButton("Close", true);

		add(getTriggeringProgramPanel(), "grow");
		add(getProgramPanel(), "grow");
		add(getConnectedProgramPanel(), "span 2, wrap, grow");

		add(getConnectButton(), "cell 1 2, width 150, align right");
		add(getDisonnectButton(), "width 150, align left");
		add(getCloseButton(), "width 150, align center");

		getProgramPanel().getTable().setDefaultRenderer(Object.class, createProgramTableRenderer(getProgramPanel()));

		getConnectedProgramPanel().getTable().setDefaultRenderer(Object.class, createConnectedProgramTableRenderer());
	}

	protected DefaultTableCellRenderer createProgramTableRenderer(final ObjectTablePane<LetPassCriteriaProgram> panel) {
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			private ObjectTablePane<LetPassCriteriaProgram> programPanel = panel;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

				Color color = null;
				if (column == 1) {
					LetPassCriteriaProgram program = programPanel.getItems().get(row);
					color = getCategoryColor(program);
				}
				if (color != null) {
					setBackground(color);
				} else {
					setBackground(Color.WHITE);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
		return renderer;
	}

	protected DefaultTableCellRenderer createConnectedProgramTableRenderer() {
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Color color = null;
				if (column == 2) {
					LetConnectedProgram connectedProgram = getConnectedProgramPanel().getItems().get(row);
					LetPassCriteriaProgram program = connectedProgram.getProgram();
					if (program != null) {
						color = getCategoryColor(program);
					}
				}
				if (color != null) {
					setBackground(color);
				} else {
					setBackground(Color.WHITE);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
		};
		return renderer;
	}

	protected JPanel createCategoryLegend() {
		JPanel panel = new JPanel(new MigLayout("insets 0 10 0 0, gap 0", "[]", ""));
		Font font = new Font("Dialog", Font.BOLD, 8);
		int size = getCategoryIx().size();
		int ctr = 0;
		for (LetProgramCategory category : getCategoryIx().values()) {
			JLabel label = UiFactory.createLabel(category.getPgmCategoryName(), font, JLabel.LEFT);
			label.setBackground(getCategoryColor(category, Color.LIGHT_GRAY));
			label.setOpaque(true);
			String constraint = "width 80, align center";
			if (ctr < size) {
				constraint = constraint + ", wrap";
			}
			panel.add(label, constraint);
		}
		return panel;
	}

	// === util === //
	protected Color getCategoryColor(LetPassCriteriaProgram program) {
		return getCategoryColor(program, null);
	}

	protected Color getCategoryColor(LetPassCriteriaProgram program, Color defaultColor) {
		Color color = Color.WHITE;
		if (defaultColor == null) {
			defaultColor = color;
		}
		if (program == null) {
			return defaultColor;
		}
		LetProgramCategoryId id = new LetProgramCategoryId(program.getInspectionDeviceType(), program.getCriteriaPgmAttr());
		LetProgramCategory category = getCategoryIx().get(id);
		color = getCategoryColor(category, defaultColor);
		return color;
	}

	protected Color getCategoryColor(LetProgramCategory category, Color defaultColor) {
		if (category == null) {
			return defaultColor;
		}
		Color color = defaultColor;
		try {
			color = Color.decode(category.getBgColor());
		} catch (Exception e) {
			Logger.getLogger().warn("Invalid color code for Let Category: " + category.getPgmCategoryName() + ", code:" + category.getBgColor());
		}
		if (color == null) {
			color = defaultColor;
		}
		return color;
	}

	// === data === //
	protected void loadData() {
		LetProgramCategoryDao pcDao = ServiceFactory.getDao(LetProgramCategoryDao.class);
		LetPassCriteriaProgramDao pcpDao = ServiceFactory.getDao(LetPassCriteriaProgramDao.class);
		LetInspectionProgramDao ipDao = ServiceFactory.getDao(LetInspectionProgramDao.class);
		List<LetProgramCategory> categories = pcDao.findAll();
		List<LetPassCriteriaProgram> criteriaPrograms = pcpDao.findAll();
		List<LetInspectionProgram> inspectionPrograms = ipDao.findAll();
		if (categories != null) {
			for (LetProgramCategory pc : categories) {
				getCategoryIx().put(pc.getId(), pc);
			}
		}
		if (inspectionPrograms != null) {
			for (LetInspectionProgram ip : inspectionPrograms) {
				getInspectionProgramIx().put(ip.getInspectionPgmName(), ip);
			}
		}
		if (criteriaPrograms != null) {
			for (LetPassCriteriaProgram cp : criteriaPrograms) {
				getCriteriaProgramIx().put(cp.getCriteriaPgmName(), cp);
			}
		}
		Collections.sort(inspectionPrograms, new ObjectComparator<LetInspectionProgram>("inspectionPgmName"));
		Collections.sort(criteriaPrograms, new ObjectComparator<LetPassCriteriaProgram>("criteriaPgmName"));
		getTriggeringProgramPanel().reloadData(inspectionPrograms);
		getProgramPanel().reloadData(criteriaPrograms);
		reloadConnectedPrograms();
	}

	protected void reloadConnectedPrograms() {
		LetConnectedProgramDao dao = ServiceFactory.getDao(LetConnectedProgramDao.class);
		List<LetConnectedProgram> connectedPrograms = dao.findAll();
		Collections.sort(connectedPrograms, new ObjectComparator<LetConnectedProgram>("triggeringProgram.inspectionPgmName"));
		getConnectedProgramPanel().reloadData(connectedPrograms);
	}

	// === listener mappings === //
	protected void mapListeners() {
		getConnectButton().addActionListener(this);
		getDisonnectButton().addActionListener(this);
		getCloseButton().addActionListener(this);
		getTriggeringProgramPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getProgramPanel().getTable().getSelectionModel().addListSelectionListener(this);
		getConnectedProgramPanel().getTable().getSelectionModel().addListSelectionListener(this);
	}

	/**
	 * ListSelectionListener
	 */
	public void valueChanged(ListSelectionEvent lse) {
		if (lse.getValueIsAdjusting()) {
			return;
		}
		if (lse.getSource().equals(getTriggeringProgramPanel().getTable().getSelectionModel())) {
			triggeringTerminatingProgramSelected(lse);
		} else if (lse.getSource().equals(getProgramPanel().getTable().getSelectionModel())) {
			triggeringTerminatingProgramSelected(lse);
		} else if (lse.getSource().equals(getConnectedProgramPanel().getTable().getSelectionModel())) {
			connectedProgramSelected(lse);
		}
	}

	/**
	 * ActionListener
	 */
	public void actionPerformed(ActionEvent ae) {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (ae.getSource().equals(getCloseButton())) {
				dispose();
			} else if (ae.getSource().equals(getConnectButton())) {
				connectPrograms();
			} else if (ae.getSource().equals(getDisonnectButton())) {
				disconnectPrograms();
			}
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Exception occured : ");
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	// === event handlers === //
	public void triggeringTerminatingProgramSelected(ListSelectionEvent lse) {
		LetInspectionProgram triggeringProgram = getTriggeringProgramPanel().getSelectedItem();
		LetPassCriteriaProgram program = getProgramPanel().getSelectedItem();
		getConnectButton().setEnabled(triggeringProgram != null && program != null);
	}

	public void connectedProgramSelected(ListSelectionEvent lse) {
		Object item = getConnectedProgramPanel().getSelectedItem();
		getDisonnectButton().setEnabled(item != null);
	}

	public void connectPrograms() {

		LetInspectionProgram triggeringProgram = getTriggeringProgramPanel().getSelectedItem();
		LetPassCriteriaProgram program = getProgramPanel().getSelectedItem();

		if (triggeringProgram == null || program == null) {
			JOptionPane.showMessageDialog(this, "Please select programs to connect !", "Selection Required", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (StringUtils.equalsIgnoreCase(triggeringProgram.getInspectionPgmName(), program.getCriteriaPgmName())) {
			JOptionPane.showMessageDialog(this, "Triggering Program cannot be the same as Program to Terminate !", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		LetPassCriteriaProgram matchedCriteriaPgm = getCriteriaProgramIx().get(triggeringProgram.getInspectionPgmName());
		LetInspectionProgram matchedInspectionPgm = getInspectionProgramIx().get(program.getCriteriaPgmName());
		if (matchedCriteriaPgm != null && matchedInspectionPgm != null) {
			LetConnectedProgramId inverseId = new LetConnectedProgramId(matchedCriteriaPgm.getId(), matchedInspectionPgm.getInspectionPgmId());
			LetConnectedProgram inverseConnectedProgram = new LetConnectedProgram(inverseId);
			if (getConnectedProgramPanel().getItems().contains(inverseConnectedProgram)) {
				JOptionPane.showMessageDialog(this, "Programs are already connected in inverse relation !", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}

		LetConnectedProgramId id = new LetConnectedProgramId(program.getId(), triggeringProgram.getInspectionPgmId());
		LetConnectedProgram connectedProgram = new LetConnectedProgram(id);

		if (getConnectedProgramPanel().getItems().contains(connectedProgram)) {
			JOptionPane.showMessageDialog(this, "Programs are already connected !", "Invalid Selection", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want to connect programs ? ", "Connect Programs", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		LetConnectedProgramDao dao = ServiceFactory.getDao(LetConnectedProgramDao.class);
		connectedProgram = dao.save(connectedProgram);
		getTriggeringProgramPanel().clearSelection();
		getProgramPanel().clearSelection();
		reloadConnectedPrograms();
	}

	public void disconnectPrograms() {
		LetConnectedProgram connectedProgram = getConnectedProgramPanel().getSelectedItem();
		if (connectedProgram == null) {
			JOptionPane.showMessageDialog(this, "Please select Connected Programs !", "Selection Required", JOptionPane.WARNING_MESSAGE);
			return;
		}
		int retCode = JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect programs ?", "Disconnect Programs", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		LetConnectedProgramDao dao = ServiceFactory.getDao(LetConnectedProgramDao.class);
		dao.removeByKey(connectedProgram.getId());
		getConnectedProgramPanel().clearSelection();
		reloadConnectedPrograms();
	}

	// === get/set === //
	public ObjectTablePane<LetPassCriteriaProgram> getProgramPanel() {
		return programPanel;
	}

	protected ObjectTablePane<LetInspectionProgram> getTriggeringProgramPanel() {
		return triggeringProgramPanel;
	}

	protected ObjectTablePane<LetConnectedProgram> getConnectedProgramPanel() {
		return connectedProgramPanel;
	}

	protected JButton getConnectButton() {
		return connectButton;
	}

	protected JButton getDisonnectButton() {
		return disonnectButton;
	}

	protected JButton getCloseButton() {
		return closeButton;
	}

	protected Map<LetProgramCategoryId, LetProgramCategory> getCategoryIx() {
		return categoryIx;
	}

	protected Map<String, LetInspectionProgram> getInspectionProgramIx() {
		return inspectionProgramIx;
	}

	protected Map<String, LetPassCriteriaProgram> getCriteriaProgramIx() {
		return criteriaProgramIx;
	}
}
