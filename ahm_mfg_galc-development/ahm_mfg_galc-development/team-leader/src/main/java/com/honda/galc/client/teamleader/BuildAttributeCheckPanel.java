package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.annotation.AnnotationProcessor;

import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.ListModel;
import com.honda.galc.client.ui.component.MbpnSelectionPanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionBase;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BuildAttribute;

public class BuildAttributeCheckPanel extends TabbedPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String NO_MATCH = "NO MATCH";
	private static final int ATTRIBUTE_LENGTH = 32;

	private AttributesBackingList attributeList = new AttributesBackingList();
	private ProductSpecSelectionBase productSpecPanel;
	private LabeledListBox availableAttributePanel;
	private LabeledListBox selectedAttributePanel;
	private JButton selectAttributeButton;
	private JButton deselectAttributeButton;
	private JButton checkButton= new JButton("CHECK");
	private JButton clearButton = new JButton("CLEAR");
	private JTextField availableAttributeFilter;
	private JTextField selectedAttributeFilter;
	private ObjectTablePane<BuildAttribute> attributeCheckPanel;

	public BuildAttributeCheckPanel(TabbedMainWindow mainWindow) {
		super("Build Attribute Check", KeyEvent.VK_B, mainWindow);
		AnnotationProcessor.process(this);
		configComponents();
		initComponents();
		mapActions();
	}

	private void initComponents() {
		if (isMbpnProduct()) {
			this.productSpecPanel = new MbpnSelectionPanel(ProductType.MBPN.name());
		} else {
			String productType = getMainWindow().getProductType().name();
			ProductSpecData productSpecData = new ProductSpecData(productType);
			this.productSpecPanel = new ProductSpecSelectionPanel(productType, productSpecData);
		}

		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Box box1 = Box.createHorizontalBox();
		box1.setBorder(border);

		box1.add(this.productSpecPanel);
		box1.add(createAttributeSelectionPanel());

		ViewUtil.setPreferredWidth(this.productSpecPanel, 500);

		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(box1,BorderLayout.CENTER);

		JPanel commandPanel = new JPanel(new GridBagLayout());
		commandPanel.add(this.checkButton);

		GridBagConstraints gbConstCloseBtn = new GridBagConstraints();
		gbConstCloseBtn.insets = new Insets(0, 20, 0, 0);
		commandPanel.add(this.clearButton, gbConstCloseBtn);		

		JPanel upperPanel = new JPanel(new BorderLayout());
		upperPanel.setBorder(new TitledBorder("Attribute Selection Panel"));
		upperPanel.add(topPanel,BorderLayout.NORTH);
		upperPanel.add(commandPanel,BorderLayout.SOUTH);

		this.attributeCheckPanel = createAttributeCheckPanel();

		setLayout(new BorderLayout());
		add(upperPanel,BorderLayout.NORTH);
		add(this.attributeCheckPanel,BorderLayout.CENTER);
		ViewUtil.setInsets(this, 10, 10, 10, 10);
	}

	private void configComponents() {
		ViewUtil.setPreferredWidth(200, this.checkButton);
		this.checkButton.setFont(Fonts.DIALOG_BOLD_20);
		this.checkButton.setName("checkButton");
		ViewUtil.setPreferredWidth(200, this.clearButton);
		this.clearButton.setFont(Fonts.DIALOG_BOLD_20);
		this.clearButton.setName("clearButton");
	}

	private void mapActions() {
		this.checkButton.addActionListener(this);
		this.clearButton.addActionListener(this);
		getAddAttributeButton().addActionListener(this);
		getRemoveAttributeButton().addActionListener(this);
	}

	private ObjectTablePane<BuildAttribute> createAttributeCheckPanel() {
		ColumnMappings columnMappings = ColumnMappings.with("Attribute", "attribute").put("Product Spec Code", "productSpecCode").put("Value", "attributeValue");
		ObjectTablePane<BuildAttribute> panel = new ObjectTablePane<BuildAttribute>("Attribute Check Panel",columnMappings.get());
		return panel;
	}

	private Component createAttributeSelectionPanel() {
		JPanel attrPanel = new JPanel();
		attrPanel.setLayout(new BoxLayout(attrPanel,BoxLayout.Y_AXIS));
		Box box1 = Box.createHorizontalBox();
		box1.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		box1.add(createAvailableAttributeListPanel());
		box1.add(addRemoveButtonPanel());
		box1.add(createSelectedAttributeListPanel());
		attrPanel.add(box1);
		return attrPanel;
	}

	private Box addRemoveButtonPanel() {
		Border border = BorderFactory.createEmptyBorder(4, 4, 4, 4);
		Box box21 = Box.createVerticalBox();
		box21.setBorder(border);
		JPanel buttonPanel2 = new JPanel(new GridLayout(6,1, 10, 40));
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		buttonPanel2.add(getAddAttributeButton());
		buttonPanel2.add(getRemoveAttributeButton());
		buttonPanel2.add(new Label());
		buttonPanel2.add(new Label());
		box21.add(buttonPanel2);
		return box21;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource().equals(this.checkButton)) {
				runAttributesCheck();
			} else if (e.getSource().equals(this.clearButton)) {
				clearPanel();
			} else if (e.getSource().equals(this.selectAttributeButton)) {
				selectAttributes();
				filterSelectedAttributes();
			} else if (e.getSource().equals(this.deselectAttributeButton)) {
				deselectAttributes();
				filterAvailableAttributes();
			}
		} catch(Exception ex) {
			handleError(ex);
		}
	}

	private void filterAvailableAttributes() {
		String filter = getAvailableAttributeFilter().getText();
		getAvailableAttributePanel().getComponent().clearSelection();
		List<String> attributeNames = filterAttributes(filter, this.attributeList.getAvailableAttributes());
		getAvailableAttributePanel().getComponent().setModel(getAttributeListModel(attributeNames));
		((ListModel<String>) getAvailableAttributePanel().getComponent().getModel()).sort();
	}

	private void filterSelectedAttributes() {
		String filter = getSelectedAttributeFilter().getText();
		getSelectedAttributePanel().getComponent().clearSelection();
		List<String> attributeNames = filterAttributes(filter, this.attributeList.getSelectedAttributes());
		getSelectedAttributePanel().getComponent().setModel(getAttributeListModel(attributeNames));
		((ListModel<String>) getSelectedAttributePanel().getComponent().getModel()).sort();
	}

	private List<String> filterAttributes(String filter, List<String> attributeNames) {
		List<String> filteredAttributeNames = new ArrayList<String>();
		if (attributeNames == null || attributeNames.isEmpty()) {
			return filteredAttributeNames;
		}
		if (StringUtils.isEmpty(filter)) {
			return attributeNames;
		}
		for(String attributeName : attributeNames) {
			if (attributeName.toUpperCase().contains(filter)) {
				filteredAttributeNames.add(attributeName);
			}
		}
		return filteredAttributeNames;
	}

	private void runAttributesCheck() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					checkAttributes();
				} catch (Exception e) {
					handleError(e);
				} finally {
					setCursor(Cursor.getDefaultCursor());
				}
			}
		});
	}

	private void checkAttributes() {
		final String productSpecCode; {
			List<String> productSpecCodes = this.productSpecPanel.buildSelectedProductSpecCodes();
			if (productSpecCodes.size() < 1) {
				MessageDialog.showError(this, "Please select a single product spec code");
				return;
			}
			productSpecCode = productSpecCodes.get(0).replace("%", "");
		}
		List<String> selectedAttributeNames = new ArrayList<String>();
		int selectedAttributeCount = this.selectedAttributePanel.getComponent().getModel().getSize();
		if (selectedAttributeCount == 0) {
			MessageDialog.showError(this, "Please select at least one attribute");
			return;
		}
		for (int i = 0; i < selectedAttributeCount; i++) {
			selectedAttributeNames.add(StringUtils.trim((String) this.selectedAttributePanel.getComponent().getModel().getElementAt(i)));
		}
		getLogger().info("Checking attributes " + selectedAttributeNames + " for product spec code [" + productSpecCode + "]");
		List<BuildAttribute> selectedBuildAttributes = new ArrayList<BuildAttribute>();
		for (String selectedAttributeName : selectedAttributeNames) {
			BuildAttribute selectedBuildAttribute = getDao(BuildAttributeDao.class).findById(selectedAttributeName, productSpecCode);
			if (selectedBuildAttribute == null) {
				selectedBuildAttribute = new BuildAttribute(NO_MATCH, selectedAttributeName, NO_MATCH);
			}
			selectedBuildAttributes.add(selectedBuildAttribute);
		}
		this.attributeCheckPanel.reloadData(selectedBuildAttributes);
	}

	private void clearPanel() {
		this.productSpecPanel.clearSelection();
		getAvailableAttributeFilter().setText(null);
		getSelectedAttributeFilter().setText(null);
		loadAvailableAttributes();
		loadSelectedAttributes();
		this.attributeCheckPanel.reloadData(null);
	}

	private void selectAttributes() {
		List<String> selected = getSelectedAttributeNames(getAvailableAttributePanel());
		if (selected.size() == 0) {
			return;
		}
		ListModel<String> availableModel = (ListModel<String>) getAvailableAttributePanel().getComponent().getModel();
		ListModel<String> selectedModel = (ListModel<String>) getSelectedAttributePanel().getComponent().getModel();
		availableModel.removeAll(selected);
		selectedModel.addAll(selected);
		getAvailableAttributePanel().setModel(availableModel, 0);
		getSelectedAttributePanel().setModel(selectedModel, 0);
		this.attributeList.selectAttributes(selected);
	}

	private void deselectAttributes() {
		List<String> selected = getSelectedAttributeNames(getSelectedAttributePanel());
		if (selected.size() == 0) {
			return;
		}
		ListModel<String> selectedModel = (ListModel<String>) getSelectedAttributePanel().getComponent().getModel();
		ListModel<String> availableModel = (ListModel<String>) getAvailableAttributePanel().getComponent().getModel();
		selectedModel.removeAll(selected);
		availableModel.addAll(selected);
		getSelectedAttributePanel().setModel(selectedModel, 0);
		getAvailableAttributePanel().setModel(availableModel, 0);
		this.attributeList.deselectAttributes(selected);
	}

	private List<String> getSelectedAttributeNames(LabeledListBox attributeList) {
		Object[] selectedArray = attributeList.getComponent().getSelectedValues();
		if (selectedArray.length == 0) {
			return Collections.emptyList();
		}
		List<String> selectedList = new ArrayList<String>();
		for (Object selectedObject : selectedArray) {
			selectedList.add((String) selectedObject);
		}
		return selectedList;
	}

	private JButton getAddAttributeButton() {
		if (this.selectAttributeButton == null) {
			this.selectAttributeButton = new JButton(" >> ");
			this.selectAttributeButton.setToolTipText("Select attribute");
			this.selectAttributeButton.setName("selectAttributeButton");
			this.selectAttributeButton.setSize(80, 50);
		}
		return this.selectAttributeButton;
	}

	private JButton getRemoveAttributeButton() {
		if (this.deselectAttributeButton == null) {
			this.deselectAttributeButton = new JButton(" << ");
			this.deselectAttributeButton.setToolTipText("Deselect attribute");
			this.deselectAttributeButton.setName("deselectAttributeButton");
			this.deselectAttributeButton.setSize(80, 50);
		}
		return this.deselectAttributeButton;
	}

	private JPanel createAvailableAttributeListPanel() {
		JPanel availableAttributePanel = new JPanel(new BorderLayout());
		availableAttributePanel.add(getAvailableAttributePanel(), BorderLayout.CENTER);
		availableAttributePanel.add(createAttributeFilterPanel(getAvailableAttributeFilter()), BorderLayout.SOUTH);
		return availableAttributePanel;
	}

	private JPanel createSelectedAttributeListPanel() {
		JPanel selectedAttributePanel = new JPanel(new BorderLayout());
		selectedAttributePanel.add(getSelectedAttributePanel(), BorderLayout.CENTER);
		selectedAttributePanel.add(createAttributeFilterPanel(getSelectedAttributeFilter()), BorderLayout.SOUTH);
		return selectedAttributePanel;
	}

	private JPanel createAttributeFilterPanel(JTextField filterTextField) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(new JLabel("Attr Filter:"), BorderLayout.WEST);
		panel.add(filterTextField, BorderLayout.CENTER);
		return panel;
	}

	private JTextField getAvailableAttributeFilter() {
		if (this.availableAttributeFilter == null) {
			this.availableAttributeFilter = new JTextField(ATTRIBUTE_LENGTH);
			this.availableAttributeFilter.setDocument(new UpperCaseDocument(ATTRIBUTE_LENGTH));
			this.availableAttributeFilter.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				public void changedUpdate(DocumentEvent e) {
					filterAvailableAttributes();
				}
			});
			this.availableAttributeFilter.setFont(Fonts.DIALOG_BOLD_12);
		}
		return this.availableAttributeFilter;
	}

	private JTextField getSelectedAttributeFilter() {
		if (this.selectedAttributeFilter == null) {
			this.selectedAttributeFilter = new JTextField(ATTRIBUTE_LENGTH);
			this.selectedAttributeFilter.setDocument(new UpperCaseDocument(ATTRIBUTE_LENGTH));
			this.selectedAttributeFilter.getDocument().addDocumentListener(new DocumentListener() {
				public void insertUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				public void removeUpdate(DocumentEvent e) {
					changedUpdate(e);
				}
				public void changedUpdate(DocumentEvent e) {
					filterSelectedAttributes();
				}
			});
			this.selectedAttributeFilter.setFont(Fonts.DIALOG_BOLD_12);
		}
		return this.selectedAttributeFilter;
	}

	private LabeledListBox getAvailableAttributePanel() {
		if (this.availableAttributePanel == null) {
			this.availableAttributePanel = new LabeledListBox("Available Attributes", true);
			this.availableAttributePanel.getComponent().setPrototypeCellValue(StringUtils.rightPad("", ATTRIBUTE_LENGTH));
			loadAvailableAttributes();
		}
		return this.availableAttributePanel;
	}

	private LabeledListBox getSelectedAttributePanel() {
		if (this.selectedAttributePanel == null) {
			this.selectedAttributePanel = new LabeledListBox("Selected Attributes",true);
			this.selectedAttributePanel.getComponent().setPrototypeCellValue(StringUtils.rightPad("", ATTRIBUTE_LENGTH));
			loadSelectedAttributes();
		}
		return this.selectedAttributePanel;
	}

	private void loadAvailableAttributes() {
		List<String> availableAttributes = getDao(BuildAttributeDao.class).findAllDistinctAttributeNames(getProductTypeString());
		this.attributeList.setAvailableAttributes(availableAttributes);
		this.availableAttributePanel.setModel(getAttributeListModel(availableAttributes), 0);
	}

	private void loadSelectedAttributes() {
		List<String> selectedAttributes = new ArrayList<String>();
		this.attributeList.setSelectedAttributes(selectedAttributes);
		this.selectedAttributePanel.setModel(getAttributeListModel(selectedAttributes), 0);
	}

	private ListModel<String> getAttributeListModel(final List<String> attributes) {
		ListModel<String> attributeModel = new ListModel<String>(attributes) {
			protected String getDisplayObject(String attribute) {
				if (attribute == null) {
					return super.getDisplayObject(attribute);
				}
				return attribute;
			}
		};
		attributeModel.setComparator(new Comparator<String>() {
			public int compare(String attribute1, String attribute2) {
				return attribute1.toString().compareToIgnoreCase(attribute2.toString());
			}
		});
		return attributeModel;
	}

	private void handleError(Throwable t) {
		getLogger().error(t);
		MessageDialog.showError(this, "An error occured.  " + t.getMessage());
	}

	@Override
	public void onTabSelected() {
		getLogger().info("Build Atrribute Check Panel Selected");
	}

	private class AttributesBackingList {
		private List<String> availableAttributes;
		private List<String> selectedAttributes;

		public List<String> getAvailableAttributes() {
			return new ArrayList<String>(this.availableAttributes);
		}

		public void setAvailableAttributes(List<String> availableAttributes) {
			this.availableAttributes = new ArrayList<String>(availableAttributes);
		}

		public List<String> getSelectedAttributes() {
			return new ArrayList<String>(this.selectedAttributes);
		}

		public void setSelectedAttributes(List<String> selectedAttributes) {
			this.selectedAttributes = new ArrayList<String>(selectedAttributes);
		}

		public void selectAttributes(List<String> attributes) {
			this.availableAttributes.removeAll(attributes);
			this.selectedAttributes.addAll(attributes);
		}

		public void deselectAttributes(List<String> attributes) {
			this.selectedAttributes.removeAll(attributes);
			this.availableAttributes.addAll(attributes);
		}
	}
}