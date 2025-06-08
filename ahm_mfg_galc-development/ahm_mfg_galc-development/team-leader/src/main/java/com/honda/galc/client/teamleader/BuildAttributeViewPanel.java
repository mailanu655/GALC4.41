package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.teamleader.property.BuildAttributeMaintenancePropertyBean;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.ui.event.ProductSpecSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.tablemodel.AttributeValueTableModel;
import com.honda.galc.client.ui.tablemodel.BuildAttributeTableModel;
import com.honda.galc.client.ui.tablemodel.ValueTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;

public class BuildAttributeViewPanel extends TabbedPanel implements ListSelectionListener, ActionListener {
	private static final long serialVersionUID = 1L;

	private BuildAttributeDao buildAttributeDao;

	private JPanel groupProductSpecSelectionPanel;
	private LabeledListBox groupSelectionPanel;
	private ProductSpecSelectionBase productSpecSelectionPanel;

	private JPanel upperRightPanel;
	private TablePane attributePanel;
	private JTextField attributeFilterInput;
	private TablePane attributeValuePanel;

	private TablePane buildAttributePanel;

	private ValueTableModel attributeTableModel;
	private AttributeValueTableModel attributeValueTableModel;
	private BuildAttributeTableModel buildAttributeTableModel;

	private List<KeyValue<String, String>> allAttributes; 

	private List<String> attributeNames = new SortedArrayList<String>();
	private List<String> attributeValues = new SortedArrayList<String>();
	private List<BuildAttribute> attributeValueList = new SortedArrayList<BuildAttribute>();
	private List<BuildAttribute> buildAttributes = new ArrayList<BuildAttribute>();

	public BuildAttributeViewPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute View", KeyEvent.VK_B,mainWindow);
		AnnotationProcessor.process(this);
	}



	/*
	 * Inherited methods
	 */
	@Override
	public void onTabSelected() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					if (!isInitialized) {
						initComponents();
						loadData();
						addListeners();
						isInitialized = true;
					} else {
						loadData();
					}
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		if (e.getSource() == getAttributePanel().getTable().getSelectionModel()) {
			attributeSelected();
		} else if (e.getSource() == getGroupSelectionPanel().getComponent()) {
			displayBuildAttributeResult();
		}
	}

	@EventSubscriber(eventClass=ProductSpecSelectionEvent.class)
	public void productSpecSelectedPanelChanged(ProductSpecSelectionEvent event) {
		if(event.isEventFromSource(SelectionEvent.SELECTING, productSpecSelectionPanel) ||
				event.isEventFromSource(SelectionEvent.POPULATED, productSpecSelectionPanel) ||
				event.isEventFromSource(SelectionEvent.SELECTED, productSpecSelectionPanel)){
			displayBuildAttributeResult();
		}
	}

	private void displayBuildAttributeResult() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					showBuildAttributeResult();
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}



	/*
	 * Utility methods
	 */
	protected static JLabel createLabelFor(String labelText, Component forComponent) {
		JLabel label = new JLabel(labelText);
		label.setLabelFor(forComponent);
		return label;
	}



	/*
	 * Getter methods
	 */
	private BuildAttributeDao getBuildAttributeDao() {
		if (this.buildAttributeDao == null) {
			this.buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		}
		return this.buildAttributeDao;
	}

	private JPanel getGroupProductSpecSelectionPanel() {
		if (this.groupProductSpecSelectionPanel == null) {
			this.groupProductSpecSelectionPanel = new JPanel(new GridBagLayout());
			final double groupWeight = 1d/(getProductSpecSelectionPanel().getColumnBoxsList().size());
			final double productSpecWeight = 1d-groupWeight;
			ViewUtil.setGridBagConstraints(this.groupProductSpecSelectionPanel, getGroupSelectionPanel(), 0, 0, 1, 1, GridBagConstraints.BOTH, null, null, new Insets(0, 3, 0, 3), GridBagConstraints.CENTER, groupWeight, 1.0);
			ViewUtil.setGridBagConstraints(this.groupProductSpecSelectionPanel, getProductSpecSelectionPanel(), 1, 0, 1, 1, GridBagConstraints.BOTH, null, null, null, GridBagConstraints.CENTER, productSpecWeight, 1.0);
		}
		return this.groupProductSpecSelectionPanel;
	}

	private LabeledListBox getGroupSelectionPanel() {
		if (this.groupSelectionPanel == null) {
			this.groupSelectionPanel = new LabeledListBox("Group");
			this.groupSelectionPanel.getComponent().setName("JGroupList");
			this.groupSelectionPanel.getComponent().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return this.groupSelectionPanel;
	}

	private ProductSpecSelectionBase getProductSpecSelectionPanel() {
		if (this.productSpecSelectionPanel == null) {
			if (isMbpnProduct()) {
				this.productSpecSelectionPanel = new MbpnSelectionPanel(getApplicationProductTypeName());
			} else {
				this.productSpecSelectionPanel = new ProductSpecSelectionPanel(getApplicationProductTypeName());
			}
			this.productSpecSelectionPanel.setBorder(null);
		}
		return this.productSpecSelectionPanel;
	}

	private JPanel getUpperRightPanel() {
		if (this.upperRightPanel == null) {
			this.upperRightPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(this.upperRightPanel, getAttributePanel(), 0, 0, 2, 1, GridBagConstraints.BOTH, null, null, null, GridBagConstraints.CENTER, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.upperRightPanel, getAttributeValuePanel(), 2, 0, 1, 2, GridBagConstraints.BOTH, null, null, null, GridBagConstraints.CENTER, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.upperRightPanel, createLabelFor("Attr Filter:", getAttributeFilterInput()), 0, 1, 1, 1, GridBagConstraints.NONE, null, null, null, GridBagConstraints.CENTER, null, null);
			ViewUtil.setGridBagConstraints(this.upperRightPanel, getAttributeFilterInput(), 1, 1, 1, 1, GridBagConstraints.HORIZONTAL, null, null, null, GridBagConstraints.CENTER, 0.5, null);
		}
		return this.upperRightPanel;
	}

	private TablePane getAttributePanel() {
		if (this.attributePanel == null) {
			this.attributePanel = new TablePane("Attribute List", true);
			this.attributeTableModel = new ValueTableModel(attributeNames, "Attribute Name", attributePanel.getTable());
		}
		return this.attributePanel;
	}

	private JTextField getAttributeFilterInput() {
		if (this.attributeFilterInput == null) {
			this.attributeFilterInput = new JTextField();
			this.attributeFilterInput.setDocument(new UpperCaseDocument(32));
			this.attributeFilterInput.setFont(Fonts.DIALOG_BOLD_12);
		}
		return this.attributeFilterInput;
	}

	private TablePane getAttributeValuePanel() {
		if (this.attributeValuePanel == null) {
			this.attributeValuePanel = new TablePane("Attribute Value List", true);
			this.attributeValueTableModel = new AttributeValueTableModel(this.attributeValueList, this.attributeValuePanel.getTable());
		}
		return attributeValuePanel;
	}

	private TablePane getBuildAttributePanel() {
		if (this.buildAttributePanel == null) {
			this.buildAttributePanel = new TablePane(null, true);
			this.buildAttributeTableModel = new BuildAttributeTableModel(this.buildAttributePanel.getTable(), isProductSpec(), this.buildAttributes);
		}
		return buildAttributePanel;
	}



	/*
	 * Initialization methods
	 */
	protected void initComponents() {
		setLayout(new GridLayout(1, 1));
		JSplitPane topPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getGroupProductSpecSelectionPanel(), getUpperRightPanel());
		topPanel.setDividerLocation(750);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, getBuildAttributePanel());
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(300);
		add(splitPane);
	}

	private void loadData() {
		final ListModel<String> groupModel; {
			List<String> attributeGroups = ServiceFactory.getDao(BuildAttributeGroupDefinitionDao.class).findAllAttributeGroups();
			List<String> groupList = new ArrayList<String>();
			groupList.add(Delimiter.ASTERISK);
			if (attributeGroups != null) {
				groupList.addAll(attributeGroups);
			}
			groupModel = new ListModel<String>(groupList);
		}
		getGroupSelectionPanel().getComponent().setModel(groupModel);

		this.allAttributes = getBuildAttributeDao().findAllDistinctAttributes(getProductTypeString());
		Set<String> attributeNameSet = new HashSet<String>();
		for(KeyValue<String,String> keyValue : this.allAttributes) {
			attributeNameSet.add(keyValue.getKey());
		}
		String attribute = this.attributeTableModel.getSelectedItem();
		this.attributeNames = new SortedArrayList<String>(attributeNameSet);
		this.attributeTableModel.refresh(this.attributeNames);
		this.attributeTableModel.selectItem(attribute);
	}

	private void addListeners() {
		getAttributePanel().addListSelectionListener(this);
		getBuildAttributePanel().addListSelectionListener(this);
		getGroupSelectionPanel().getComponent().addListSelectionListener(this);
		getAttributeFilterInput().getDocument().addDocumentListener(createAttributeFilterListener());
	}

	protected DocumentListener createAttributeFilterListener() {
		DocumentListener listner = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}

			public void changedUpdate(DocumentEvent e) {
				filterAttributes();
			}
		};
		return listner;
	}



	/*
	 * Data manipulation methods
	 */
	protected void filterAttributes() {
		String filter = getAttributeFilterInput().getText();
		getAttributePanel().clearSelection();
		this.attributeNames = filterAttributes(filter, this.allAttributes);
		this.attributeTableModel.refresh(this.attributeNames);
	}

	protected List<String> filterAttributes(String filter, List<KeyValue<String, String>> attributeNames) {
		List<String> filteredList = new SortedArrayList<String>();
		if (attributeNames == null || attributeNames.isEmpty()) {
			return filteredList;
		}
		Set<String> attributeNameSet = new HashSet<String>();
		if (StringUtils.isEmpty(filter)) {
			for (KeyValue<String,String> keyValue : attributeNames) {
				attributeNameSet.add(keyValue.getKey());
			}
		} else {
			for (KeyValue<String,String> keyValue : attributeNames) {
				String key = keyValue.getKey();
				if (key == null) {
					continue;
				}
				if (key.toUpperCase().contains(filter)) {
					attributeNameSet.add(key);
				}
			}			
		}
		filteredList.addAll(attributeNameSet);
		return filteredList;
	}

	private void attributeSelected() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String attribute = attributeTableModel.getSelectedItem();
					reloadAttributeValues(attribute);
					showBuildAttributeResult();
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}

	private void showBuildAttributeResult() {
		try {
			clearErrorMessage();
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			final String attribute = this.attributeTableModel.getSelectedItem();
			final String attributeGroup = getSelectedGroup();
			getAttributeValuePanel().getTable().updateUI();

			final List<String> productSpecCodes = getProductSpecSelectionPanel().buildSelectedProductSpecCodes();
			this.buildAttributes.clear();
			final BuildAttributeMaintenancePropertyBean properties = PropertyService.getPropertyBean(BuildAttributeMaintenancePropertyBean.class, getApplicationId());

			final int maxSize = properties.getBuildAttributeResultsetMaxSize();
			final String productType = getProductTypeString();
			if (!productSpecCodes.isEmpty()) {
				for (String specCode : productSpecCodes) {
					if (maxSize > 0) {
						final long count = getBuildAttributeDao().count(specCode, attribute, productType, attributeGroup);
						if (count > maxSize) {
							final String msg = String.format("Resultset: %s exceeds max size : %s for group: %s and spec: %s, please select additional criteria.", count, maxSize, attributeGroup == null ? Delimiter.ASTERISK : attributeGroup, specCode);
							setErrorMessage(msg);
							break;
						}
					}
					this.buildAttributes.addAll(getBuildAttributeDao().findAllMatchIdAndGroup(specCode, attribute, productType, attributeGroup));
				}
			} else {
				if (maxSize > 0) {
					final long count = getBuildAttributeDao().count(null, attribute, productType, attributeGroup);
					if (count > maxSize) {
						final String msg = String.format("Resultset: %s exceeds max size : %s for group: %s and spec: %s, please select additional criteria.", count, maxSize, attributeGroup == null ? Delimiter.ASTERISK : attributeGroup, null);
						setErrorMessage(msg);
					} else {
						this.buildAttributes.addAll(getBuildAttributeDao().findAllMatchIdAndGroup(null, attribute, productType, attributeGroup));
					}
				} else {
					this.buildAttributes.addAll(getBuildAttributeDao().findAllMatchIdAndGroup(null, attribute, productType, attributeGroup));
				}
			}
			this.buildAttributeTableModel.refresh(this.buildAttributes);
		} finally {
			setCursor(Cursor.getDefaultCursor());
		}
	}

	private void reloadAttributeValues(String attribute) {
		this.attributeValues.clear();
		this.attributeValueList.clear();
		if (attribute == null) return;
		for (KeyValue<String,String> keyValue : this.allAttributes) {
			if (attribute.equals(keyValue.getKey())) {
				this.attributeValues.add(keyValue.getValue());
				this.attributeValueList.add(getBuildAttributeDao().findfirstByAttributeAndValue(keyValue.getKey(), keyValue.getValue()));
			}
		}
		this.attributeValueTableModel.refresh(this.attributeValueList);
	}

	private String getSelectedGroup() {
		final String selectedGroup = (String) getGroupSelectionPanel().getComponent().getSelectedValue();
		if (Delimiter.ASTERISK.equals(selectedGroup)) return null;
		return selectedGroup;
	}
}
