package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.entity.conf.BuildAttributeDefinition;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.entity.product.BuildAttributeByBom;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;

public class BuildAttributeByBomDialog extends JDialog  {

	private static final long serialVersionUID = 1L;
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

	private boolean canceled = false;
	private boolean isCreateNew = false;
	private BuildAttributeByBomDao buildAttributeByBomDao;
	private BuildAttributeDefinitionDao buildAttributeDefinitionDao;
	private BuildAttributeGroupDefinitionDao buildAttributeGroupDefinitionDao;

	public BuildAttributeByBomDialog(final java.awt.Frame owner, final String system) {
		super(owner, true);
		init(system);
	}

	private void init(final String system) {
		try {
			setName("BuildAttributeByBom");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(500, 400);
			setResizable(false);
			setTitle("BuildAttributeByBom");
			setContentPane(getEditPanel());
			addListeners();
			loadModelGroupComboBox(system);
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
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

	private JPanel editPanel;
	private JComboBox modelGroupComboBox;
	private LengthFieldBean partNoTextField;
	private LengthFieldBean partColorCodeTextField;
	private JComboBox attributeComboBox;
	private LengthFieldBean attributeValueTextField;
	private LengthFieldBean attributeDescriptionTextField;
	private JButton saveButton;
	private JButton cancelButton;
	private BuildAttributeByBom original;

	private JPanel getEditPanel() {
		if (this.editPanel == null) {
			this.editPanel = new JPanel(new GridBagLayout());
			this.editPanel.setBorder(new TitledBorder("Build Attribute By BOM"));
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Model Group:", getModelGroupComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getModelGroupComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Part Number:", getPartNoLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getPartNoLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Part Color:", getPartColorCodeLengthFieldBean()), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getPartColorCodeLengthFieldBean(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Attribute:", getAttributeComboBox()), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeComboBox(), 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Attribute Value:", getAttributeValueLengthFieldBean()), 0, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeValueLengthFieldBean(), 1, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Attribute Description:", getAttributeDescriptionLengthFieldBean()), 0, 5, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeDescriptionLengthFieldBean(), 1, 5, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getSaveButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(buttonPanel, getCancelButton(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 6, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
		}
		return this.editPanel;
	}

	private JComboBox getModelGroupComboBox() {
		if (this.modelGroupComboBox == null) {
			this.modelGroupComboBox = createJComboBox();
		}
		return this.modelGroupComboBox;
	}

	private LengthFieldBean getPartNoLengthFieldBean() {
		if (this.partNoTextField == null) {
			this.partNoTextField = createLengthFieldBean(18);
		}
		return this.partNoTextField;
	}

	private LengthFieldBean getPartColorCodeLengthFieldBean() {
		if (this.partColorCodeTextField == null) {
			this.partColorCodeTextField = createLengthFieldBean(11);
			this.partColorCodeTextField.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					if (Delimiter.ASTERISK.equals(partColorCodeTextField.getText())) {
						partColorCodeTextField.selectAll();
					}
				}

				public void focusLost(FocusEvent e) {}
			});
		}
		return this.partColorCodeTextField;
	}

	private JComboBox getAttributeComboBox() {
		if (this.attributeComboBox == null) {
			this.attributeComboBox = createJComboBox();
		}
		return this.attributeComboBox;
	}

	private LengthFieldBean getAttributeValueLengthFieldBean() {
		if (this.attributeValueTextField == null) {
			this.attributeValueTextField = createLengthFieldBean(512);
		}
		return this.attributeValueTextField;
	}

	private LengthFieldBean getAttributeDescriptionLengthFieldBean() {
		if (this.attributeDescriptionTextField == null) {
			this.attributeDescriptionTextField = createLengthFieldBean(32);
		}
		return this.attributeDescriptionTextField;
	}

	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("SAVE");
			this.saveButton.setFont(FONT);
			this.saveButton.setToolTipText("Save the build attribute by BOM definition entered above");
		}
		return this.saveButton;
	}

	private JButton getCancelButton() {
		if (this.cancelButton == null) {
			this.cancelButton = new JButton("CANCEL");
			this.cancelButton.setFont(FONT);
			this.cancelButton.setToolTipText("Cancel the edit operation");
		}
		return this.cancelButton;
	}

	private LengthFieldBean createLengthFieldBean(int maxLength) {
		LengthFieldBean lengthFieldBean = new LengthFieldBean();
		lengthFieldBean.getDocument().addDocumentListener(createEditDocumentListener());
		lengthFieldBean.setColumns(TEXT_FIELD_SIZE);
		lengthFieldBean.setFont(FONT);
		lengthFieldBean.setMaximumLength(maxLength);
		return lengthFieldBean;
	}

	private JComboBox createJComboBox() {
		JComboBox jComboBox = new JComboBox();
		jComboBox.setBackground(Color.WHITE);
		jComboBox.setFont(FONT);
		jComboBox.setPreferredSize(COMBO_BOX_DIMENSION);
		jComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSaveButton().setEnabled(canSave());
			}
		});
		return jComboBox;
	}

	private JLabel createLabelFor(String labelText, Component forComponent) {
		JLabel label = new JLabel(labelText);
		label.setFont(FONT);
		label.setLabelFor(forComponent);
		return label;
	}

	private DocumentListener createEditDocumentListener() {
		DocumentListener documentListener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				getSaveButton().setEnabled(canSave());
			}
			public void removeUpdate(DocumentEvent e) {
				getSaveButton().setEnabled(canSave());
			}
			public void changedUpdate(DocumentEvent e) {
				getSaveButton().setEnabled(canSave());
			}
		};
		return documentListener;
	}

	private void addListeners(){
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				cancel();
			}
		});
		getSaveButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		{
			final String clickSaveButton = "clickSaveButton";
			getSaveButton().getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), clickSaveButton);
			getSaveButton().getActionMap().put(clickSaveButton, new AbstractAction() {
				public void actionPerformed(ActionEvent actionEvent) {
					getSaveButton().doClick(0);
				}
			});
		}
		getCancelButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		{
			final String clickCancelButton = "clickCancelButton";
			getCancelButton().getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), clickCancelButton);
			getCancelButton().getActionMap().put(clickCancelButton, new AbstractAction() {
				public void actionPerformed(ActionEvent actionEvent) {
					getCancelButton().doClick(0);
				}
			});
		}
	}

	private BuildAttributeByBom getEnteredBuildAttributeByBom() {
		final String modelGroup = (String) getModelGroupComboBox().getSelectedItem();
		final String partNo = getPartNoLengthFieldBean().getText();
		final String partColorCode = getPartColorCodeLengthFieldBean().getText();
		final String attribute = (String) getAttributeComboBox().getSelectedItem();
		final String attributeValue = getAttributeValueLengthFieldBean().getText();
		final String attributeDescription = getAttributeDescriptionLengthFieldBean().getText();
		BuildAttributeByBom buildAttributeByBom = new BuildAttributeByBom(modelGroup, partNo, partColorCode, attribute, attributeValue, attributeDescription);
		return buildAttributeByBom;
	}

	private String getOriginalModelGroup() {
		if (this.original == null) {
			return null;
		}
		return this.original.getModelGroup();
	}

	private String getOriginalPartNo() {
		if (this.original == null) {
			return null;
		}
		return this.original.getPartNo();
	}

	private String getOriginalPartColorCode() {
		if (this.original == null) {
			return null;
		}
		return this.original.getPartColorCode();
	}

	private String getOriginalAttribute() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttribute();
	}

	private String getOriginalAttributeValue() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttributeValue();
	}

	private String getOriginalAttributeDescription() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttributeDescription();
	}

	private boolean canSave() {
		if (isCreateNew) {
			return (!StringUtils.isEmpty((String) getModelGroupComboBox().getSelectedItem())
					&& !StringUtils.isEmpty(getPartNoLengthFieldBean().getText())
					&& !StringUtils.isEmpty((String) getAttributeComboBox().getSelectedItem())
					&& !StringUtils.isEmpty(getAttributeValueLengthFieldBean().getText()));
		} else {
			return ((!StringUtils.isEmpty((String) getModelGroupComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalModelGroup(), (String) getModelGroupComboBox().getSelectedItem()))
					|| (!StringUtils.isEmpty(getPartNoLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalPartNo(), getPartNoLengthFieldBean().getText()))
					|| (!ObjectUtils.equals(getOriginalPartColorCode(), getPartColorCodeLengthFieldBean().getText()))
					|| (!StringUtils.isEmpty((String) getAttributeComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalAttribute(), (String) getAttributeComboBox().getSelectedItem()))
					|| (!StringUtils.isEmpty(getAttributeValueLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalAttributeValue(), getAttributeValueLengthFieldBean().getText()))
					|| (!ObjectUtils.equals(getOriginalAttributeDescription(), getAttributeDescriptionLengthFieldBean().getText())));
		}
	}

	/**
	 * Save the entered BuildAttributeByBom
	 */
	private void save() {
		final BuildAttributeByBom buildAttributeByBom = getEnteredBuildAttributeByBom();
		if (isCreateNew) {
			final BuildAttributeByBom existingBuildAttributeByBom = getBuildAttributeByBomDao().findByKey(buildAttributeByBom.getId());
			if (existingBuildAttributeByBom != null) {
				JOptionPane.showMessageDialog(this, "A BuildAttributeByBom with id " + buildAttributeByBom.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			final BuildAttributeDefinition existingBuildAttributeDefinition = getBuildAttributeDefinitionDao().findByKey(buildAttributeByBom.getAttribute());
			if (existingBuildAttributeDefinition == null) {
				if (MessageDialog.confirm(this, "No BuildAttributeDefinition with attribute " + buildAttributeByBom.getAttribute() + " exists.\nDo you want to create that definition?")) {
					BuildAttributeDefinitionDialog buildAttributeDefinitionDialog = new BuildAttributeDefinitionDialog((java.awt.Frame) getOwner());
					buildAttributeDefinitionDialog.showDialog(new BuildAttributeDefinition(buildAttributeByBom.getAttribute(), null, null, null), true);
					if (buildAttributeDefinitionDialog.isCanceled()) {
						return;
					}
				} else {
					return;
				}
			}
		} else {
			if (!ObjectUtils.equals(getOriginalModelGroup(), buildAttributeByBom.getModelGroup())
					|| !ObjectUtils.equals(getOriginalPartNo(), buildAttributeByBom.getPartNo())
					|| !ObjectUtils.equals(getOriginalPartColorCode(), buildAttributeByBom.getPartColorCode())
					|| !ObjectUtils.equals(getOriginalAttribute(), buildAttributeByBom.getAttribute())) {
				getBuildAttributeByBomDao().remove(this.original);
				AuditLoggerUtil.logAuditInfo(this.original,null, "delete",getName(),ApplicationContext.getInstance().getUserId().toUpperCase(), "GALC", "GALC_Maintenance");

			}
		}
		final BuildAttributeByBom existingBuildAttributeByBom = getBuildAttributeByBomDao().findByKey(buildAttributeByBom.getId());
		getBuildAttributeByBomDao().save(buildAttributeByBom);
		if(existingBuildAttributeByBom==null) {
			AuditLoggerUtil.logAuditInfo(null,buildAttributeByBom, "insert", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
		}
		else {
			existingBuildAttributeByBom.setAttributeDescription(existingBuildAttributeByBom.getAttributeDescription().trim());
			AuditLoggerUtil.logAuditInfo(existingBuildAttributeByBom,buildAttributeByBom, "update", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
		}
		this.setVisible(false);
		this.dispose();
	}

	/**
	 * Cancel edit operation
	 */
	private void cancel() {
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	public void showDialog(final BuildAttributeByBom buildAttributeByBom, final boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
		this.original = buildAttributeByBom;

		loadAttributeComboBox();
		if (getModelGroupComboBox().getItemCount() != 1) {
			getModelGroupComboBox().setSelectedItem(getOriginalModelGroup());
		}
		getPartNoLengthFieldBean().setText(getOriginalPartNo());
		getPartColorCodeLengthFieldBean().setText(getOriginalPartColorCode() == null ? Delimiter.ASTERISK : getOriginalPartColorCode());
		getAttributeComboBox().setSelectedItem(getOriginalAttribute());
		getAttributeValueLengthFieldBean().setText(getOriginalAttributeValue());
		getAttributeDescriptionLengthFieldBean().setText(getOriginalAttributeDescription());
		getSaveButton().setEnabled(canSave());

		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	private void loadModelGroupComboBox(final String system) {
		final List<String> modelGroups = ServiceFactory.getDao(ModelGroupingDao.class).findRecentModelGroupsBySystem(system);
		if (modelGroups != null && !modelGroups.isEmpty()) {
			final String[] modelGroupsArray = modelGroups.toArray(new String[modelGroups.size()]);
			final ComboBoxModel modelGroupsModel = new DefaultComboBoxModel(modelGroupsArray);
			getModelGroupComboBox().setModel(modelGroupsModel);
			if (modelGroups.size() == 1) {
				getModelGroupComboBox().setSelectedIndex(0);
			} else {
				getModelGroupComboBox().insertItemAt("", 0);
				getModelGroupComboBox().setSelectedIndex(-1);
			}
		} else {
			getModelGroupComboBox().insertItemAt("", 0);
			getModelGroupComboBox().setSelectedIndex(-1);
		}
	}

	private void loadAttributeComboBox() {
		final String selectedAttribute = (String) getAttributeComboBox().getSelectedItem();
		final List<String> accessibleGroups = findAllAccessibleAttributeGroups();
		final List<String> attributes = accessibleGroups == null ? null : getBuildAttributeDefinitionDao().findAllAttributesByAttributeGroups(accessibleGroups);
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

	public boolean isCanceled() {
		return canceled;
	}
}