package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.BuildAttributeDefinitionTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.entity.conf.BuildAttributeDefinition;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.ServiceFactory;

public class BuildAttributeDefinitionPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String CREATE_BUILD_ATTRIBUTE_DEFINITION = "Create Build Attribute Def";
	private static final String EDIT_BUILD_ATTRIBUTE_DEFINITION = "Edit Build Attribute Def";
	private static final String DELETE_BUILD_ATTRIBUTE_DEFINITION = "Delete Build Attribute Def";
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 20;
	private static final Dimension COMBO_BOX_DIMENSION;
	static {
		JTextField jTextField = new JTextField(TEXT_FIELD_SIZE);
		jTextField.setFont(FONT);
		JComboBox jComboBox = new JComboBox();
		jComboBox.setFont(FONT);
		COMBO_BOX_DIMENSION = new Dimension((int) jTextField.getPreferredSize().getWidth(), (int) jComboBox.getPreferredSize().getHeight());
	}

	private BuildAttributeGroupDefinitionDao buildAttributeGroupDefinitionDao;
	private BuildAttributeDefinitionDao buildAttributeDefinitionDao;
	private BuildAttributeByBomDao buildAttributeByBomDao;
	private BuildAttributeDefinitionDialog buildAttributeDefinitionDialog;

	private JPanel filterPanel;
	private JComboBox attributeGroupComboBox;
	private LengthFieldBean attributeTextField;
	private LengthFieldBean attributeLabelTextField;
	private JComboBox autoUpdateComboBox;
	private JButton filterButton;
	private TablePane tablePane;
	private BuildAttributeDefinitionTableModel tableModel;
	private List<BuildAttributeDefinition> buildAttributeDefinitions = new ArrayList<BuildAttributeDefinition>();

	public BuildAttributeDefinitionPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute Definition", KeyEvent.VK_B, mainWindow);
		AnnotationProcessor.process(this);
	}



	/*
	 * Inherited methods
	 */
	@Override
	public void onTabSelected() {
		if (!this.isInitialized) {
			initComponents();
			addListeners();
			this.isInitialized = true;
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					loadAttributeGroupComboBox();
					setCursor(Cursor.getDefaultCursor());
				} catch (Exception e) {
					setCursor(Cursor.getDefaultCursor());
					throwException(e);
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try {
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if (menuItem.getName().equals(CREATE_BUILD_ATTRIBUTE_DEFINITION)) createBuildAttributeDefinition();
				else if (menuItem.getName().equals(EDIT_BUILD_ATTRIBUTE_DEFINITION)) editBuildAttributeDefinition();
				else if (menuItem.getName().equals(DELETE_BUILD_ATTRIBUTE_DEFINITION)) deleteBuildAttributeDefinition();
			} catch(Exception ex) {
				exception = ex;
			}
			handleException(exception);
		}
	}



	/*
	 * Utility methods
	 */
	protected static LengthFieldBean createLengthFieldBean(int maxLength) {
		LengthFieldBean lengthFieldBean = new LengthFieldBean();
		lengthFieldBean.setColumns(TEXT_FIELD_SIZE);
		lengthFieldBean.setFont(FONT);
		lengthFieldBean.setMaximumLength(maxLength);
		return lengthFieldBean;
	}

	protected static JComboBox createJComboBox(final String[] options) {
		JComboBox jComboBox = options == null ? new JComboBox() : new JComboBox(options);
		jComboBox.setBackground(Color.WHITE);
		jComboBox.setFont(FONT);
		jComboBox.setPreferredSize(COMBO_BOX_DIMENSION);
		return jComboBox;
	}

	protected static JLabel createLabelFor(String labelText, Component forComponent) {
		JLabel label = new JLabel(labelText);
		label.setFont(FONT);
		label.setLabelFor(forComponent);
		return label;
	}

	@Override
	protected JMenuItem createMenuItem(final String name, final boolean enabled) {
		JMenuItem menuItem = super.createMenuItem(name, enabled);
		menuItem.setFont(FONT);
		return menuItem;
	}

	protected static void throwException(Exception e) {
		if (e instanceof RuntimeException) {
			RuntimeException re = (RuntimeException) e;
			throw re;
		}
		throw new RuntimeException(e);
	}



	/*
	 * Getter methods
	 */
	private BuildAttributeGroupDefinitionDao getBuildAttributeGroupDefinitionDao() {
		if (this.buildAttributeGroupDefinitionDao == null) {
			this.buildAttributeGroupDefinitionDao = ServiceFactory.getDao(BuildAttributeGroupDefinitionDao.class);
		}
		return this.buildAttributeGroupDefinitionDao;
	}

	private BuildAttributeDefinitionDao getBuildAttributeDefinitionDao() {
		if (this.buildAttributeDefinitionDao == null) {
			this.buildAttributeDefinitionDao = ServiceFactory.getDao(BuildAttributeDefinitionDao.class);
		}
		return this.buildAttributeDefinitionDao;
	}

	private BuildAttributeByBomDao getBuildAttributeByBomDao() {
		if (this.buildAttributeByBomDao == null) {
			this.buildAttributeByBomDao = ServiceFactory.getDao(BuildAttributeByBomDao.class);
		}
		return this.buildAttributeByBomDao;
	}

	private BuildAttributeDefinitionDialog getBuildAttributeDefinitionDialog() {
		if (this.buildAttributeDefinitionDialog == null) {
			this.buildAttributeDefinitionDialog = new BuildAttributeDefinitionDialog(getMainWindow());
		}
		return this.buildAttributeDefinitionDialog;
	}



	/*
	 * Build Attribute Definition getter methods
	 */
	private JPanel getFilterPanel() {
		if (this.filterPanel == null) {
			this.filterPanel = new JPanel(new GridBagLayout());
			this.filterPanel.setBorder(new TitledBorder("Build Attribute Definition Filter"));
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Group:", getAttributeGroupComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeGroupComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Attribute:", getAttributeLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Label:", getAttributeLabelLengthFieldBean()), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeLabelLengthFieldBean(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Auto Update:", getAutoUpdateComboBox()), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAutoUpdateComboBox(), 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getFilterButton(), 0, 4, 2, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}
		return this.filterPanel;
	}

	private JComboBox getAttributeGroupComboBox() {
		if (this.attributeGroupComboBox == null) {
			this.attributeGroupComboBox = createJComboBox(null);
			this.attributeGroupComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkFilterButton();
				}
			});
		}
		return this.attributeGroupComboBox;
	}

	private LengthFieldBean getAttributeLengthFieldBean() {
		if (this.attributeTextField == null) {
			this.attributeTextField = createLengthFieldBean(32);
		}
		return this.attributeTextField;
	}

	private LengthFieldBean getAttributeLabelLengthFieldBean() {
		if (this.attributeLabelTextField == null) {
			this.attributeLabelTextField = createLengthFieldBean(32);
		}
		return this.attributeLabelTextField;
	}

	private JComboBox getAutoUpdateComboBox() {
		if (this.autoUpdateComboBox == null) {
			final String[] autoUpdateOptions = { "", "0", "1" };
			this.autoUpdateComboBox = createJComboBox(autoUpdateOptions);
		}
		return this.autoUpdateComboBox;
	}

	private JButton getFilterButton() {
		if (this.filterButton == null) {
			this.filterButton = new JButton("FILTER");
			this.filterButton.setFont(FONT);
			this.filterButton.setFocusable(false);
			this.filterButton.setToolTipText("Filter the build attribute definitions in the table");
		}
		return this.filterButton;
	}

	private TablePane getTablePane() {
		if (this.tablePane == null) {
			this.tablePane = new TablePane("Build Attribute Definitions", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.tablePane.setFont(FONT);
			this.tableModel = new BuildAttributeDefinitionTableModel(this.tablePane.getTable(), this.buildAttributeDefinitions);
		}
		return this.tablePane;
	}



	/*
	 * Initialization methods
	 */
	protected void initComponents() {
		setLayout(new GridLayout(1, 1));
		final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getFilterPanel(), getTablePane());
		splitPane.setDividerLocation(275);
		add(splitPane);
	}

	protected void resetTab() {
		// clear build attribute definition filter
		getAttributeGroupComboBox().setSelectedIndex(-1);
		loadAttributeGroupComboBox();
		getAttributeLengthFieldBean().setText((String) null);
		getAttributeLabelLengthFieldBean().setText((String) null);
		getAutoUpdateComboBox().setSelectedIndex(-1);
		// clear build attribute definition table
		this.buildAttributeDefinitions.clear();
		this.tableModel.refresh(this.buildAttributeDefinitions);
	}

	private void addListeners() {
		// Build Attribute Definition listeners
		getTablePane().getTable().addMouseListener(createBuildAttributeDefinitionMouseListener());
		getTablePane().addMouseListener(createBuildAttributeDefinitionMouseListener());
		getFilterButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() { filterBuildAttributeDefinitions(); }
				});
			}
		});
	}

	private MouseListener createBuildAttributeDefinitionMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showBuildAttributeDefinitionPopupMenu(e);
			}
		});  
	}

	private void showBuildAttributeDefinitionPopupMenu(MouseEvent e) {
		int rowCount = getTablePane().getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_BUILD_ATTRIBUTE_DEFINITION, true));
		popupMenu.add(createMenuItem(EDIT_BUILD_ATTRIBUTE_DEFINITION, rowCount == 1));
		popupMenu.add(createMenuItem(DELETE_BUILD_ATTRIBUTE_DEFINITION, rowCount > 0));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}

	private void checkFilterButton() {
		if (getAttributeGroupComboBox().getSelectedIndex() > 0) {
			getFilterButton().setEnabled(true);
		} else {
			getFilterButton().setEnabled(false);
		}
	}



	/*
	 * Data load methods
	 */
	private void filterBuildAttributeDefinitions() {
		try {
			final String attributeGroup = (String) getAttributeGroupComboBox().getSelectedItem();
			final String attribute = getAttributeLengthFieldBean().getText();
			final String attributeLabel = getAttributeLabelLengthFieldBean().getText();
			final String autoUpdate = (String) getAutoUpdateComboBox().getSelectedItem();
			this.buildAttributeDefinitions = getBuildAttributeDefinitionDao().findByFilter(attributeGroup, attribute, attributeLabel, autoUpdate);
			this.tableModel.refresh(this.buildAttributeDefinitions);
			setCursor(Cursor.getDefaultCursor());
		} catch (Exception e) {
			setCursor(Cursor.getDefaultCursor());
			throwException(e);
		}
	}

	private void loadAttributeGroupComboBox() {
		String selectedAttributeGroup = (String) getAttributeGroupComboBox().getSelectedItem();
		List<String> groups = findAllAccessibleAttributeGroups();
		if (groups != null) {
			String[] groupsArray = groups.toArray(new String[groups.size()]);
			ComboBoxModel groupModel = new DefaultComboBoxModel(groupsArray);
			getAttributeGroupComboBox().setModel(groupModel);
		} else {
			getAttributeGroupComboBox().removeAllItems();
		}
		getAttributeGroupComboBox().insertItemAt("", 0);
		getAttributeGroupComboBox().setSelectedIndex(-1);
		getAttributeGroupComboBox().setSelectedItem(selectedAttributeGroup);
	}

	private List<String> findAllAccessibleAttributeGroups() {
		final List<BuildAttributeGroupDefinition> allGroups = getBuildAttributeGroupDefinitionDao().findAllInOrder();
		final List<String> accessibleGroups = new ArrayList<String>();
		for (BuildAttributeGroupDefinition group : allGroups) {
			if (ClientMain.getInstance().getAccessControlManager().isAccessPermitted(group.getScreenId())) {
				accessibleGroups.add(group.getAttributeGroup());
			}
		}
		if (accessibleGroups.isEmpty()) return null;
		return accessibleGroups;
	}



	/*
	 * Data edit methods
	 */
	private void createBuildAttributeDefinition() {
		getBuildAttributeDefinitionDialog().showDialog(null, true);
		if (!getBuildAttributeDefinitionDialog().isCanceled()) {
			getFilterButton().doClick();
		}
	}

	private void editBuildAttributeDefinition() {
		getBuildAttributeDefinitionDialog().showDialog(getSelectedBuildAttributeDefinition(), false);
		if (!getBuildAttributeDefinitionDialog().isCanceled()) {
			getFilterButton().doClick();
		}
	}

	private void deleteBuildAttributeDefinition() {
		if (MessageDialog.confirm(getMainWindow(), "Delete the selected rows?")) {
			List<BuildAttributeDefinition> selectedBuildAttributeDefinitions = getSelectedBuildAttributeDefinitions();
			for (BuildAttributeDefinition buildAttributeDefinition : selectedBuildAttributeDefinitions) {
				final long existingBuildAttributeByBomCount = getBuildAttributeByBomDao().getCountOfAttribute(buildAttributeDefinition.getAttribute());
				if (existingBuildAttributeByBomCount > 0) {
					if (existingBuildAttributeByBomCount == 1) {
						JOptionPane.showMessageDialog(this, "Cannot delete: 1 BuildAttributeByBom record already exists for attribute " + buildAttributeDefinition.getAttribute() + ".", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete: " + existingBuildAttributeByBomCount + " BuildAttributeByBom records already exist for attribute " + buildAttributeDefinition.getAttribute() + ".", "Error", JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
			}
			for (BuildAttributeDefinition buildAttributeDefinition : selectedBuildAttributeDefinitions) {
				getBuildAttributeDefinitionDao().remove(buildAttributeDefinition);
				logUserAction(REMOVED, buildAttributeDefinition);
			}
			getFilterButton().doClick();
		}
	}

	private BuildAttributeDefinition getSelectedBuildAttributeDefinition() {
		return this.tableModel.getSelectedItem();
	}

	private List<BuildAttributeDefinition> getSelectedBuildAttributeDefinitions() {
		return this.tableModel.getSelectedItems();
	}
}
