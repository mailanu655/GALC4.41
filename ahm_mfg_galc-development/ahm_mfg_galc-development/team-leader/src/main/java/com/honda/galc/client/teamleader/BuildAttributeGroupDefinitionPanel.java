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

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.BuildAttributeGroupDefinitionTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.ServiceFactory;

public class BuildAttributeGroupDefinitionPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String CREATE_BUILD_ATTRIBUTE_GROUP_DEFINITION = "Create Build Attribute Group Def";
	private static final String EDIT_BUILD_ATTRIBUTE_GROUP_DEFINITION = "Edit Build Attribute Group Def";
	private static final String DELETE_BUILD_ATTRIBUTE_GROUP_DEFINITION = "Delete Build Attribute Group Def";
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
	private BuildAttributeGroupDefinitionDialog buildAttributeGroupDefinitionDialog;

	private JPanel filterPanel;
	private LengthFieldBean attributeGroupTextField;
	private LengthFieldBean screenIdTextField;
	private JButton filterButton;
	private TablePane tablePane;
	private BuildAttributeGroupDefinitionTableModel tableModel;
	private List<BuildAttributeGroupDefinition> buildAttributeGroups = new ArrayList<BuildAttributeGroupDefinition>();

	public BuildAttributeGroupDefinitionPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute Group Def", KeyEvent.VK_B, mainWindow);
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
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try {
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if (menuItem.getName().equals(CREATE_BUILD_ATTRIBUTE_GROUP_DEFINITION)) createBuildAttributeGroupDefinition();
				else if (menuItem.getName().equals(EDIT_BUILD_ATTRIBUTE_GROUP_DEFINITION)) editBuildAttributeGroupDefinition();
				else if (menuItem.getName().equals(DELETE_BUILD_ATTRIBUTE_GROUP_DEFINITION)) deleteBuildAttributeGroupDefinition();
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

	private BuildAttributeGroupDefinitionDialog getBuildAttributeGroupDefinitionDialog() {
		if (this.buildAttributeGroupDefinitionDialog == null) {
			this.buildAttributeGroupDefinitionDialog = new BuildAttributeGroupDefinitionDialog(getMainWindow());
		}
		return this.buildAttributeGroupDefinitionDialog;
	}



	/*
	 * Build Attribute Group getter methods
	 */
	private JPanel getFilterPanel() {
		if (this.filterPanel == null) {
			this.filterPanel = new JPanel(new GridBagLayout());
			this.filterPanel.setBorder(new TitledBorder("Build Attribute Group Filter"));
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Group:", getAttributeGroupLengthFieldBean()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeGroupLengthFieldBean(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Screen ID:", getScreenIdLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getScreenIdLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getFilterButton(), 0, 2, 2, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}
		return this.filterPanel;
	}

	private LengthFieldBean getAttributeGroupLengthFieldBean() {
		if (this.attributeGroupTextField == null) {
			this.attributeGroupTextField = createLengthFieldBean(32);
		}
		return this.attributeGroupTextField;
	}

	private LengthFieldBean getScreenIdLengthFieldBean() {
		if (this.screenIdTextField == null) {
			this.screenIdTextField = createLengthFieldBean(16);
		}
		return this.screenIdTextField;
	}

	private JButton getFilterButton() {
		if (this.filterButton == null) {
			this.filterButton = new JButton("FILTER");
			this.filterButton.setFont(FONT);
			this.filterButton.setFocusable(false);
			this.filterButton.setToolTipText("Filter the build attribute group definitions in the table");
		}
		return this.filterButton;
	}

	private TablePane getTablePane() {
		if (this.tablePane == null) {
			this.tablePane = new TablePane("Build Attribute Groups", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.tableModel = new BuildAttributeGroupDefinitionTableModel(this.tablePane.getTable(), this.buildAttributeGroups);
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
		// clear build attribute group filter
		getAttributeGroupLengthFieldBean().setText((String) null);
		getScreenIdLengthFieldBean().setText((String) null);
		// clear build attribute group table
		this.buildAttributeGroups.clear();
		this.tableModel.refresh(this.buildAttributeGroups);
	}

	private void addListeners() {
		// Build Attribute Group listeners
		getTablePane().getTable().addMouseListener(createBuildAttributeGroupMouseListener());
		getTablePane().addMouseListener(createBuildAttributeGroupMouseListener());
		getFilterButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() { filterBuildAttributeGroups(); }
				});
			}
		});
	}

	private MouseListener createBuildAttributeGroupMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showBuildAttributeGroupPopupMenu(e);
			}
		});  
	}

	private void showBuildAttributeGroupPopupMenu(MouseEvent e) {
		int rowCount = getTablePane().getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_BUILD_ATTRIBUTE_GROUP_DEFINITION, true));
		popupMenu.add(createMenuItem(EDIT_BUILD_ATTRIBUTE_GROUP_DEFINITION, rowCount == 1));
		popupMenu.add(createMenuItem(DELETE_BUILD_ATTRIBUTE_GROUP_DEFINITION, rowCount > 0));
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}



	/*
	 * Data load methods
	 */
	private void filterBuildAttributeGroups() {
		try {
			final String attributeGroup = getAttributeGroupLengthFieldBean().getText();
			final String screenId = getScreenIdLengthFieldBean().getText();
			this.buildAttributeGroups = getBuildAttributeGroupDefinitionDao().findByFilter(attributeGroup, screenId);
			this.tableModel.refresh(this.buildAttributeGroups);
			setCursor(Cursor.getDefaultCursor());
		} catch (Exception e) {
			setCursor(Cursor.getDefaultCursor());
			throwException(e);
		}
	}



	/*
	 * Data edit methods
	 */
	private void createBuildAttributeGroupDefinition() {
		getBuildAttributeGroupDefinitionDialog().showDialog(null, true);
		if (!getBuildAttributeGroupDefinitionDialog().isCanceled()) {
			filterBuildAttributeGroups();
		}
	}

	private void editBuildAttributeGroupDefinition() {
		getBuildAttributeGroupDefinitionDialog().showDialog(getSelectedBuildAttributeGroup(), false);
		if (!getBuildAttributeGroupDefinitionDialog().isCanceled()) {
			filterBuildAttributeGroups();
		}
	}

	private void deleteBuildAttributeGroupDefinition() {
		if (MessageDialog.confirm(getMainWindow(), "Delete the selected rows?")) {
			List<BuildAttributeGroupDefinition> selectedBuildAttributeGroupDefinitions = getSelectedBuildAttributeGroups();
			for (BuildAttributeGroupDefinition buildAttributeGroupDefinition : selectedBuildAttributeGroupDefinitions) {
				final long existingBuildAttributeDefinitionCount = getBuildAttributeDefinitionDao().getCountOfAttributeGroup(buildAttributeGroupDefinition.getAttributeGroup());
				if (existingBuildAttributeDefinitionCount > 0) {
					if (existingBuildAttributeDefinitionCount == 1) {
						JOptionPane.showMessageDialog(this, "Cannot delete: 1 BuildAttributeDefinition record already exists for attribute group " + buildAttributeGroupDefinition.getAttributeGroup() + ".", "Error", JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(this, "Cannot delete: " + existingBuildAttributeDefinitionCount + " BuildAttributeDefinition records already exist for attribute group " + buildAttributeGroupDefinition.getAttributeGroup() + ".", "Error", JOptionPane.ERROR_MESSAGE);
					}
					return;
				}
			}
			for (BuildAttributeGroupDefinition buildAttributeGroupDefinition : selectedBuildAttributeGroupDefinitions) {
				getBuildAttributeGroupDefinitionDao().remove(buildAttributeGroupDefinition);
				logUserAction(REMOVED, buildAttributeGroupDefinition);
			}
			filterBuildAttributeGroups();
		}
	}

	private BuildAttributeGroupDefinition getSelectedBuildAttributeGroup() {
		return this.tableModel.getSelectedItem();
	}

	private List<BuildAttributeGroupDefinition> getSelectedBuildAttributeGroups() {
		return this.tableModel.getSelectedItems();
	}
}
