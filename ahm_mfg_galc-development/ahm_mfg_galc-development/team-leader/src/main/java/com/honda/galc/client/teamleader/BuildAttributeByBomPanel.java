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

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
import com.honda.galc.client.ui.tablemodel.BuildAttributeByBomTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.dao.oif.BomDao;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.entity.fif.Bom;
import com.honda.galc.entity.fif.BomId;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.BuildAttributeByBom;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.AuditLoggerUtil;

public class BuildAttributeByBomPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final String CREATE_BUILD_ATTRIBUTE_BY_BOM = "Create Build Attribute by BOM";
	private static final String EDIT_BUILD_ATTRIBUTE_BY_BOM = "Edit Build Attribute by BOM";
	private static final String DUPLICATE_BUILD_ATTRIBUTE_BY_BOM = "Duplicate Build Attribute by BOM";
	private static final String DELETE_BUILD_ATTRIBUTE_BY_BOM = "Delete Build Attribute by BOM";
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

	private BuildAttributeByBomDialog buildAttributeByBomDialog;
	private BuildAttributeByBomCopyDialog buildAttributeByBomCopyDialog;
	private BuildAttributeByBomDao buildAttributeByBomDao;
	private BuildAttributeDefinitionDao buildAttributeDefinitionDao;
	private BuildAttributeGroupDefinitionDao buildAttributeGroupDefinitionDao;
	private BomDao bomDao;
	private BuildAttributeDao buildAttributeDao;
	private volatile int created;
	private volatile int updated;
	private final String system;

	private JPanel filterPanel;
	private JComboBox attributeGroupComboBox;
	private JComboBox modelGroupComboBox;
	private JComboBox attributeComboBox;
	private LengthFieldBean partNoPrefixTextField;
	private JButton filterButton;
	private JPanel loadPanel;
	private TablePane loadTablePane;
	private BuildAttributeByBomTableModel loadTableModel;
	private List<BuildAttributeByBom> buildAttributeByBoms = new ArrayList<BuildAttributeByBom>();
	private JButton loadButton;
	private JButton loadSeletedButton;
	private JButton copyButton;

	public BuildAttributeByBomPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute By BOM", KeyEvent.VK_B, mainWindow);
		{
			final String hostName = getMainWindow().getApplicationContext().getTerminal().getHostName();
			String modelGroupingSystem;
			try {
				modelGroupingSystem = PropertyService.getPropertyBean(FrameLinePropertyBean.class, hostName).getBuildSheetModelGroupingSystem();
			} catch (PropertyException pe) {
				modelGroupingSystem = "";
			}
			this.system = modelGroupingSystem;
		}
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
			loadModelGroupComboBox();
			this.isInitialized = true;
		}
		loadAttributeAndGroupComboBoxes();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try {
				JMenuItem menuItem = (JMenuItem)e.getSource();
				logUserAction("selected menu item: " + menuItem.getName());
				if (menuItem.getName().equals(CREATE_BUILD_ATTRIBUTE_BY_BOM)) createBuildAttributeByBom();
				else if (menuItem.getName().equals(EDIT_BUILD_ATTRIBUTE_BY_BOM)) editBuildAttributeByBom();
				else if (menuItem.getName().equals(DUPLICATE_BUILD_ATTRIBUTE_BY_BOM)) duplicateBuildAttributeByBom();
				else if (menuItem.getName().equals(DELETE_BUILD_ATTRIBUTE_BY_BOM)) deleteBuildAttributeByBom();

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

	protected static JComboBox createJComboBox() {
		JComboBox jComboBox = new JComboBox();
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
	private BuildAttributeByBomDialog getBuildAttributeByBomDialog() {
		if (this.buildAttributeByBomDialog == null) {
			this.buildAttributeByBomDialog = new BuildAttributeByBomDialog(getMainWindow(), this.system);
		}
		return this.buildAttributeByBomDialog;
	}
	
	private BuildAttributeByBomCopyDialog getBuildAttributeByBomCopyDialog() {
		if (this.buildAttributeByBomCopyDialog == null) {
			this.buildAttributeByBomCopyDialog = new BuildAttributeByBomCopyDialog(getMainWindow(), this.system);
		}
		return this.buildAttributeByBomCopyDialog;
	}

	private BuildAttributeByBomDao getBuildAttributeByBomDao() {
		if (this.buildAttributeByBomDao == null) {
			this.buildAttributeByBomDao = ServiceFactory.getDao(BuildAttributeByBomDao.class);
		}
		return this.buildAttributeByBomDao;
	}

	private BuildAttributeDefinitionDao getBuildAttributeDefinitionDao() {
		if (this.buildAttributeDefinitionDao == null) {
			this.buildAttributeDefinitionDao = ServiceFactory.getDao(BuildAttributeDefinitionDao.class);
		}
		return this.buildAttributeDefinitionDao;
	}

	private BuildAttributeGroupDefinitionDao getBuildAttributeGroupDefinitionDao() {
		if (this.buildAttributeGroupDefinitionDao == null) {
			this.buildAttributeGroupDefinitionDao = ServiceFactory.getDao(BuildAttributeGroupDefinitionDao.class);
		}
		return this.buildAttributeGroupDefinitionDao;
	}

	private BomDao getBomDao() {
		if (this.bomDao == null) {
			this.bomDao = ServiceFactory.getDao(BomDao.class);
		}
		return this.bomDao;
	}

	private BuildAttributeDao getBuildAttributeDao() {
		if (this.buildAttributeDao == null) {
			this.buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return this.buildAttributeDao;
	}



	/*
	 * Filter Build Attribute By BOM getter methods
	 */
	private JPanel getFilterPanel() {
		if (this.filterPanel == null) {
			this.filterPanel = new JPanel(new GridBagLayout());
			this.filterPanel.setBorder(new TitledBorder("Build Attribute By BOM Filter"));
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Group:", getAttributeGroupComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeGroupComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Model Group:", getModelGroupComboBox()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getModelGroupComboBox(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Attribute:", getAttributeComboBox()), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getAttributeComboBox(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, createLabelFor("Part # Prefix:", getPartNoPrefixLengthFieldBean()), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getPartNoPrefixLengthFieldBean(), 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.filterPanel, getFilterButton(), 0, 4, 2, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}
		return this.filterPanel;
	}

	private JComboBox getAttributeGroupComboBox() {
		if (this.attributeGroupComboBox == null) {
			this.attributeGroupComboBox = createJComboBox();
			this.attributeGroupComboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					checkFilterButton();
				}
			});
		}
		return this.attributeGroupComboBox;
	}

	private JComboBox getModelGroupComboBox() {
		if (this.modelGroupComboBox == null) {
			this.modelGroupComboBox = createJComboBox();
		}
		return this.modelGroupComboBox;
	}

	private JComboBox getAttributeComboBox() {
		if (this.attributeComboBox == null) {
			this.attributeComboBox = createJComboBox();
		}
		return this.attributeComboBox;
	}

	private LengthFieldBean getPartNoPrefixLengthFieldBean() {
		if (this.partNoPrefixTextField == null) {
			this.partNoPrefixTextField = createLengthFieldBean(18);
		}
		return this.partNoPrefixTextField;
	}

	private JButton getFilterButton() {
		if (this.filterButton == null) {
			this.filterButton = new JButton("FILTER");
			this.filterButton.setFont(FONT);
			this.filterButton.setFocusable(false);
			this.filterButton.setToolTipText("Filter the build attribute by BOM definitions in the table");
		}
		return this.filterButton;
	}

	private JPanel getLoadPanel() {
		if (this.loadPanel == null) {
			this.loadPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(this.loadPanel, getLoadTablePane(), 0, 0, 1, 1, GridBagConstraints.BOTH, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getLoadButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(buttonPanel, getLoadSelectedButton(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, null, null);
			ViewUtil.setGridBagConstraints(buttonPanel, getCopyButton(), 2, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, null, null);
			ViewUtil.setGridBagConstraints(this.loadPanel, buttonPanel, 0, 1, 1, 1, null, null, null, null, GridBagConstraints.PAGE_START, null, null);
		}
		return this.loadPanel;
	}

	private TablePane getLoadTablePane() {
		if (this.loadTablePane == null) {
			this.loadTablePane = new TablePane(null, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.loadTableModel = new BuildAttributeByBomTableModel(this.loadTablePane.getTable(), this.buildAttributeByBoms);
		}
		return this.loadTablePane;
	}

	private JButton getLoadButton() {
		if (this.loadButton == null) {
			this.loadButton = new JButton("LOAD ALL ATTRIBUTES");
			this.loadButton.setFont(FONT);
			this.loadButton.setFocusable(false);
			this.loadButton.setToolTipText("Load the all build attributes displayed in table");
		}
		return this.loadButton;
	}
	
	private JButton getLoadSelectedButton() {
		if (this.loadSeletedButton == null) {
			this.loadSeletedButton = new JButton("LOAD SELECTED ATTRIBUTES");
			this.loadSeletedButton.setFont(FONT);
			this.loadSeletedButton.setFocusable(false);
			this.loadSeletedButton.setToolTipText("Load the build attributes using the selected rows");
		}
		return this.loadSeletedButton;
	}
	
	private JButton getCopyButton() {
		if (this.copyButton == null) {
			this.copyButton = new JButton("COPY All ATTRIBUTES");
			this.copyButton.setFont(FONT);
			this.copyButton.setFocusable(false);
			this.copyButton.setToolTipText("Copy the all build attributes displayed in table");
		}
		return this.copyButton;
	}



	/*
	 * Initialization methods
	 */
	protected void initComponents() {
		this.setLayout(new GridLayout(1, 1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getFilterPanel(), getLoadPanel());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(275);
		add(splitPane);
	}

	protected void resetTab() {
		// clear build attribute by BOM filter
		getAttributeGroupComboBox().setSelectedIndex(-1);
		loadAttributeAndGroupComboBoxes();
		getPartNoPrefixLengthFieldBean().setText((String) null);
		// clear the build attribute by BOM table
		this.buildAttributeByBoms.clear();
		this.loadTableModel.refresh(this.buildAttributeByBoms);
	}

	private void addListeners() {
		getLoadTablePane().getTable().addMouseListener(createBuildAttributeByBomMouseListener());
		getLoadTablePane().addMouseListener(createBuildAttributeByBomMouseListener());
		getFilterButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							filterBuildAttributeByBoms();
							setCursor(Cursor.getDefaultCursor());
						} catch (Exception e) {
							setCursor(Cursor.getDefaultCursor());
							throwException(e);
						}
					}
				});
			}
		});
		getLoadButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final List<BuildAttributeByBom> selectedRows = getSelectAllBuildAttributeByBoms();
				if (selectedRows == null || selectedRows.isEmpty()) {
					JOptionPane.showMessageDialog(BuildAttributeByBomPanel.this, "Please display at least one row.");
					return;
				}
				if (MessageDialog.confirm(getMainWindow(), "Load all build attributes displayed in the table?")) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								final String summary = loadAttributes(selectedRows);
								setCursor(Cursor.getDefaultCursor());
								JOptionPane.showMessageDialog(BuildAttributeByBomPanel.this, summary, "Summary", JOptionPane.PLAIN_MESSAGE);
							} catch (Exception e) {
								setCursor(Cursor.getDefaultCursor());
								throwException(e);
							}
						}
					});
				}
			}
		});
		getLoadSelectedButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final List<BuildAttributeByBom> selectedRows = getSelectedBuildAttributeByBoms();
				if (selectedRows == null || selectedRows.isEmpty()) {
					JOptionPane.showMessageDialog(BuildAttributeByBomPanel.this, "Please select at least one row.");
					return;
				}
				if (MessageDialog.confirm(getMainWindow(), "Load build attributes using the selected rows?")) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							try {
								final String summary = loadAttributes(selectedRows);
								setCursor(Cursor.getDefaultCursor());
								JOptionPane.showMessageDialog(BuildAttributeByBomPanel.this, summary, "Summary", JOptionPane.PLAIN_MESSAGE);
							} catch (Exception e) {
								setCursor(Cursor.getDefaultCursor());
								throwException(e);
							}
						}
					});
				}
			}
		});
		getCopyButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final List<BuildAttributeByBom> selectedRows = getSelectAllBuildAttributeByBoms();
				if (selectedRows == null || selectedRows.isEmpty()) {
					JOptionPane.showMessageDialog(BuildAttributeByBomPanel.this, "Please display at least one row.");
					return;
				}
				getBuildAttributeByBomCopyDialog().showDialog(selectedRows, true);
				if (!getBuildAttributeByBomCopyDialog().isCanceled()) {
					getFilterButton().doClick();
				}
			}
		});
	}

	private MouseListener createBuildAttributeByBomMouseListener(){
		return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showBuildAttributeByBomPopupMenu(e);
			}
		});  
	}

	private void showBuildAttributeByBomPopupMenu(MouseEvent e) {
		int rowCount = this.loadTablePane.getTable().getSelectedRowCount();
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(CREATE_BUILD_ATTRIBUTE_BY_BOM, true));
		popupMenu.add(createMenuItem(EDIT_BUILD_ATTRIBUTE_BY_BOM, rowCount == 1));
		popupMenu.add(createMenuItem(DUPLICATE_BUILD_ATTRIBUTE_BY_BOM, rowCount == 1));
		popupMenu.add(createMenuItem(DELETE_BUILD_ATTRIBUTE_BY_BOM, rowCount > 0));
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
	private void filterBuildAttributeByBoms() {
		if (StringUtils.isEmpty((String) getAttributeGroupComboBox().getSelectedItem())) {
			JOptionPane.showMessageDialog(this, "The group filter must be specified.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.buildAttributeByBoms = getBuildAttributeByBomDao().findByFilter((String) getAttributeGroupComboBox().getSelectedItem(), this.system, (String) getModelGroupComboBox().getSelectedItem(), (String) getAttributeComboBox().getSelectedItem(), getPartNoPrefixLengthFieldBean().getText());
		this.loadTableModel.refresh(this.buildAttributeByBoms);
	}

	private void loadAttributeAndGroupComboBoxes() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					final List<String> accessibleGroups = findAllAccessibleAttributeGroups();
					Thread attributeGroupThread = new Thread(new Runnable() {
						public void run() {
							loadAttributeGroupComboBox(accessibleGroups);
						}
					});
					Thread attributeThread = new Thread(new Runnable() {
						public void run() {
							loadAttributeComboBox(accessibleGroups);
						}
					});
					attributeGroupThread.start();
					attributeThread.start();
					try {
						attributeGroupThread.join();
					} catch (InterruptedException ie) {}
					try {
						attributeThread.join();
					} catch (InterruptedException ie) {}
					setCursor(Cursor.getDefaultCursor());
				} catch (Exception e) {
					setCursor(Cursor.getDefaultCursor());
					throwException(e);
				}
			}
		});
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

	private void loadAttributeGroupComboBox(final List<String> groups) {
		final String selectedAttributeGroup = (String) getAttributeGroupComboBox().getSelectedItem();
		if (groups != null) {
			final String[] groupsArray = groups.toArray(new String[groups.size()]);
			final ComboBoxModel groupModel = new DefaultComboBoxModel(groupsArray);
			getAttributeGroupComboBox().setModel(groupModel);
		} else {
			getAttributeGroupComboBox().removeAllItems();
		}
		getAttributeGroupComboBox().insertItemAt("", 0);
		getAttributeGroupComboBox().setSelectedIndex(-1);
		getAttributeGroupComboBox().setSelectedItem(selectedAttributeGroup);
		checkFilterButton();
	}

	private void loadModelGroupComboBox() {
		final List<String> modelGroups = ServiceFactory.getDao(ModelGroupingDao.class).findRecentModelGroupsBySystem(this.system);
		if (modelGroups != null && !modelGroups.isEmpty()) {
			final String[] modelGroupsArray = modelGroups.toArray(new String[modelGroups.size()]);
			final ComboBoxModel modelGroupsModel = new DefaultComboBoxModel(modelGroupsArray);
			getModelGroupComboBox().setModel(modelGroupsModel);
		}
		getModelGroupComboBox().insertItemAt("", 0);
		getModelGroupComboBox().setSelectedIndex(-1);
	}

	private void loadAttributeComboBox(final List<String> groups) {
		final String selectedAttribute = (String) getAttributeComboBox().getSelectedItem();
		final List<String> attributes = groups == null ? null : getBuildAttributeDefinitionDao().findAllAttributesByAttributeGroups(groups);
		if (attributes != null) {
			final String[] attributesArray = attributes.toArray(new String[attributes.size()]);
			final ComboBoxModel attributeModel = new DefaultComboBoxModel(attributesArray);
			getAttributeComboBox().setModel(attributeModel);
		} else {
			getAttributeComboBox().removeAllItems();
		}
		getAttributeComboBox().insertItemAt("", 0);
		getAttributeComboBox().setSelectedIndex(-1);
		getAttributeComboBox().setSelectedItem(selectedAttribute);
	}

	private String loadAttributes(final List<BuildAttributeByBom> selectedRows) {
		final List<Thread> loadThreads = new ArrayList<Thread>(selectedRows.size());
		final String productType = getMainWindow().getApplicationPropertyBean().getProductType();
		this.created = 0;
		this.updated = 0;
		for (final BuildAttributeByBom selectedRow : selectedRows) {
			final Thread loadThread = new Thread(new Runnable() {
				public void run() {
					try {
						final List<Bom> matchingBoms;
						if (Delimiter.ASTERISK.equals(selectedRow.getPartColorCode())) {
							List<Object[]> modelAndTypeList = getBomDao().findModelAndTypeBySystemModelGroupAndPartNoPrefix(BuildAttributeByBomPanel.this.system, selectedRow.getModelGroup(), selectedRow.getPartNo());
							if (modelAndTypeList != null) {
								matchingBoms = new ArrayList<Bom>();
								for (Object[] modelAndType : modelAndTypeList) {
									Bom bom = new Bom(new BomId(null, null, null, null, null, null, null, null, (String) modelAndType[0], null, null, (String) modelAndType[1], null, null));
									matchingBoms.add(bom);
								}
							} else {
								matchingBoms = null;
							}
						} else {
							matchingBoms = getBomDao().findAllBySystmMdlGrpPrtPrfxNPrtClrCd(BuildAttributeByBomPanel.this.system, selectedRow.getModelGroup(), selectedRow.getPartNo(), selectedRow.getPartColorCode());
						}
						if (matchingBoms != null) {
							int localCreated = 0;
							int localUpdated = 0;
							for (final Bom matchingBom : matchingBoms) {
								final String productSpecCode; {
									final String model = StringUtils.rightPad(matchingBom.getId().getMtcModel(), 4);
									final String type = StringUtils.rightPad(matchingBom.getId().getMtcType(), 3);
									if (Delimiter.ASTERISK.equals(selectedRow.getPartColorCode()) || StringUtils.isEmpty(matchingBom.getId().getPartColorCode())) {
										productSpecCode = model + type;
									} else {
										final String option = StringUtils.rightPad(matchingBom.getId().getMtcOption(), 3);
										final String color = StringUtils.rightPad(matchingBom.getId().getMtcColor(), 10);
										final String intColor = StringUtils.rightPad(matchingBom.getId().getIntColorCode(), 2);
										productSpecCode = model + type + option + color + intColor;
									}
								}
								final String attribute = selectedRow.getAttribute();
								final String attributeValue = selectedRow.getAttributeValue();
								final String attributeDescription = getBuildAttributeDefinitionDao().findByKey(attribute).getAttributeLabel();

								final BuildAttribute newBuildAttribute = new BuildAttribute(productSpecCode, attribute, attributeValue, attributeDescription);
								newBuildAttribute.setProductType(productType);

								final BuildAttribute existingBuildAttribute = getBuildAttributeDao().findByKey(newBuildAttribute.getId());
								if (existingBuildAttribute == null) {
									newBuildAttribute.setUpdateUser(getUserName());
									getLogger().debug("User " + getUserName() + " creating BuildAttribute: " + newBuildAttribute);
									getBuildAttributeDao().save(newBuildAttribute);
									logUserAction(SAVED, newBuildAttribute);
									AuditLoggerUtil.logAuditInfo(null, newBuildAttribute,"save",getScreenName(),getUserName().toUpperCase(),"GALC","GALC_Maintenance");
									localCreated++;
								} else if (!ObjectUtils.equals(newBuildAttribute.getAttributeValue(), existingBuildAttribute.getAttributeValue())) {
									BuildAttribute previousBuildAttribute = getBuildAttributeDao().findByKey(newBuildAttribute.getId());
									final String previousAttributeValue = existingBuildAttribute.getAttributeValue();
									existingBuildAttribute.setAttributeValue(newBuildAttribute.getAttributeValue());
									existingBuildAttribute.setUpdateUser(getUserName());
									getLogger().debug("User " + getUserName() + " updating BuildAttribute: " + existingBuildAttribute + "; previous ATTRIBUTE_VALUE: " + previousAttributeValue);
									getBuildAttributeDao().save(existingBuildAttribute);			
									logUserAction(SAVED, existingBuildAttribute);
									AuditLoggerUtil.logAuditInfo(previousBuildAttribute, existingBuildAttribute,"update",getScreenName(), getUserName().toUpperCase(), "GALC", "GALC_Maintenance");

									localUpdated++;
								}
							}
							created += localCreated;
							updated += localUpdated;
						}
					} catch (Exception e) {
						getLogger().error("An error occurred while loading BuildAttributes for BuildAttributeByBom: " + selectedRow);
						traceThrowable(e);
					}
				}
			});
			loadThreads.add(loadThread);
		}
		for (final Thread loadThread : loadThreads) {
			loadThread.start();
		}
		for (final Thread loadThread : loadThreads) {
			try {
				loadThread.join(300000);
			} catch (InterruptedException ie) {
				continue;
			}
		}
		return "Created " + this.created + " records.\nUpdated " + this.updated + " records.";
	}



	/*
	 * Data edit methods
	 */
	private void createBuildAttributeByBom() {
		getBuildAttributeByBomDialog().showDialog(null, true);
		if (!getBuildAttributeByBomDialog().isCanceled()) {
			getFilterButton().doClick();
		}
	}

	private void editBuildAttributeByBom() {
		getBuildAttributeByBomDialog().showDialog(getSelectedBuildAttributeByBom(), false);
		if (!getBuildAttributeByBomDialog().isCanceled()) {
			getFilterButton().doClick();
		}
	}

	private void duplicateBuildAttributeByBom() {
		getBuildAttributeByBomDialog().showDialog(getSelectedBuildAttributeByBom(), true);
		if (!getBuildAttributeByBomDialog().isCanceled()) {
			getFilterButton().doClick();
		}
	}

	private void deleteBuildAttributeByBom() {
		if (MessageDialog.confirm(getMainWindow(), "Delete the selected rows?")) {
			List<BuildAttributeByBom> selectedBuildAttributeByBoms = getSelectedBuildAttributeByBoms();
			for (BuildAttributeByBom buildAttributeByBom : selectedBuildAttributeByBoms) {
				getBuildAttributeByBomDao().remove(buildAttributeByBom);
				logUserAction(REMOVED, buildAttributeByBom);
				AuditLoggerUtil.logAuditInfo(buildAttributeByBom,null, "delete", getScreenName(), getUserName().toUpperCase(), "GALC", "GALC_Maintenance");
			}
			getFilterButton().doClick();
		}
	}

	private BuildAttributeByBom getSelectedBuildAttributeByBom() {
		return this.loadTableModel.getSelectedItem();
	}

	private List<BuildAttributeByBom> getSelectedBuildAttributeByBoms() {
		return this.loadTableModel.getSelectedItems();
	}
	
	private List<BuildAttributeByBom> getSelectAllBuildAttributeByBoms() {
		return this.loadTableModel.getItems();
	}

	private void traceThrowable(Throwable t) {
		if (t == null) return;
		traceThrowable(t.getCause());
		getLogger().error(t);
	}
}
