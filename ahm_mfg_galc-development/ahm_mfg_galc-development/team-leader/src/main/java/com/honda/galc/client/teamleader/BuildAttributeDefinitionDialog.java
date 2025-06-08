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

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BuildAttributeDefinitionDao;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.entity.conf.BuildAttributeDefinition;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.ServiceFactory;

public class BuildAttributeDefinitionDialog extends JDialog  {

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
	private BuildAttributeGroupDefinitionDao buildAttributeGroupDefinitionDao;
	private BuildAttributeDefinitionDao buildAttributeDefinitionDao;

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

	public BuildAttributeDefinitionDialog(java.awt.Frame owner) {
		super(owner, true);
		init();
	}

	private void init() {
		try {
			setName("BuildAttributeDefinition");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(480, 300);
			setResizable(false);
			setTitle("BuildAttributeDefinition");
			setContentPane(getEditPanel());
			addListeners();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private JPanel editPanel;
	private LengthFieldBean attributeTextField;
	private LengthFieldBean attributeLabelTextField;
	private JComboBox attributeGroupComboBox;
	private JComboBox autoUpdateComboBox;
	private JButton saveButton;
	private JButton cancelButton;
	private BuildAttributeDefinition original;

	private JPanel getEditPanel() {
		if (this.editPanel == null) {
			this.editPanel = new JPanel(new GridBagLayout());
			this.editPanel.setBorder(new TitledBorder("Build Attribute Definition"));
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Attribute:", getAttributeLengthFieldBean()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeLengthFieldBean(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Label:", getAttributeLabelLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeLabelLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Group:", getAttributeGroupComboBox()), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeGroupComboBox(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Auto Update:", getAutoUpdateComboBox()), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAutoUpdateComboBox(), 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getSaveButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(buttonPanel, getCancelButton(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 4, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
		}
		return this.editPanel;
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

	private JComboBox getAttributeGroupComboBox() {
		if (this.attributeGroupComboBox == null) {
			this.attributeGroupComboBox = createJComboBox(null);
		}
		return this.attributeGroupComboBox;
	}

	private JComboBox getAutoUpdateComboBox() {
		if (this.autoUpdateComboBox == null) {
			final String[] autoUpdateOptions = { "", "0", "1" };
			this.autoUpdateComboBox = createJComboBox(autoUpdateOptions);
		}
		return this.autoUpdateComboBox;
	}

	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("SAVE");
			this.saveButton.setFont(FONT);
			this.saveButton.setEnabled(false);
			this.saveButton.setToolTipText("Save the build attribute definition entered above");
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

	private JComboBox createJComboBox(final String[] options) {
		JComboBox jComboBox = options == null ? new JComboBox() : new JComboBox(options);
		jComboBox.setBackground(Color.WHITE);
		jComboBox.setFont(FONT);
		jComboBox.setPreferredSize(COMBO_BOX_DIMENSION);
		jComboBox.addActionListener(createEditActionListener());
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

	private ActionListener createEditActionListener() {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getSaveButton().setEnabled(canSave());
			}
		};
		return actionListener;
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

	private BuildAttributeDefinition getEnteredBuildAttributeDefinition() {
		final String attribute = getAttributeLengthFieldBean().getText();
		final String attributeLabel = getAttributeLabelLengthFieldBean().getText();
		final String attributeGroup = (String) getAttributeGroupComboBox().getSelectedItem();
		final String autoUpdate = (String) getAutoUpdateComboBox().getSelectedItem();
		BuildAttributeDefinition buildAttributeDefinition = new BuildAttributeDefinition(attribute, attributeLabel, attributeGroup, autoUpdate);
		return buildAttributeDefinition;
	}

	private void loadAttributeGroupComboBox() {
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

	private String getOriginalAttribute() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttribute();
	}

	private String getOriginalAttributeLabel() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttributeLabel();
	}

	private String getOriginalAttributeGroup() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttributeGroup();
	}

	private String getOriginalAutoUpdate() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAutoUpdate();
	}

	private boolean canSave() {
		if (isCreateNew) {
			return (!StringUtils.isEmpty(getAttributeLengthFieldBean().getText())
					&& !StringUtils.isEmpty(getAttributeLabelLengthFieldBean().getText())
					&& !StringUtils.isEmpty((String) getAttributeGroupComboBox().getSelectedItem())
					&& !StringUtils.isEmpty((String) getAutoUpdateComboBox().getSelectedItem()));
		} else {
			return ((!StringUtils.isEmpty(getAttributeLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalAttribute(), getAttributeLengthFieldBean().getText()))
					|| (!StringUtils.isEmpty(getAttributeLabelLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalAttributeLabel(), getAttributeLabelLengthFieldBean().getText()))
					|| (!StringUtils.isEmpty((String) getAttributeGroupComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalAttributeGroup(), (String) getAttributeGroupComboBox().getSelectedItem()))
					|| (!StringUtils.isEmpty((String) getAutoUpdateComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalAutoUpdate(), (String) getAutoUpdateComboBox().getSelectedItem())));
		}
	}

	/**
	 * Save the entered BuildAttributeDefinition
	 */
	private void save() {
		BuildAttributeDefinition buildAttributeDefinition = getEnteredBuildAttributeDefinition();
		if (isCreateNew) {
			BuildAttributeDefinition existingBuildAttributeDefinition = getBuildAttributeDefinitionDao().findByKey(buildAttributeDefinition.getAttribute());
			if (existingBuildAttributeDefinition != null) {
				JOptionPane.showMessageDialog(this, "A BuildAttributeDefinition with attribute " + existingBuildAttributeDefinition.getAttribute() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			if (!ObjectUtils.equals(getOriginalAttribute(), buildAttributeDefinition.getAttribute())) {
				getBuildAttributeDefinitionDao().remove(this.original);
			}
		}
		getBuildAttributeDefinitionDao().save(buildAttributeDefinition);
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

	public void showDialog(final BuildAttributeDefinition buildAttributeDefinition, final boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
		this.original = buildAttributeDefinition;

		loadAttributeGroupComboBox();
		getAttributeLengthFieldBean().setText(getOriginalAttribute());
		getAttributeLabelLengthFieldBean().setText(getOriginalAttributeLabel());
		getAttributeGroupComboBox().setSelectedItem(getOriginalAttributeGroup());
		getAutoUpdateComboBox().setSelectedItem(getOriginalAutoUpdate());
		getSaveButton().setEnabled(canSave());

		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}
}