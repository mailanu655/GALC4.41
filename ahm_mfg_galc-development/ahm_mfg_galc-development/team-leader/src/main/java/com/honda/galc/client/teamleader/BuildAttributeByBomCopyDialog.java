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
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.BuildAttributeByBomDao;
import com.honda.galc.dao.product.ModelGroupingDao;
import com.honda.galc.entity.product.BuildAttributeByBom;
import com.honda.galc.service.ServiceFactory;

public class BuildAttributeByBomCopyDialog extends JDialog  {

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

	public BuildAttributeByBomCopyDialog(final java.awt.Frame owner, final String system) {
		super(owner, true);
		init(system);
	}

	private void init(final String system) {
		try {
			setName("BuildAttributeByBom");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(500, 300);
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

	private JPanel editPanel;
	private JComboBox modelGroupComboBox;
	private JButton saveButton;
	private JButton cancelButton;
	private BuildAttributeByBom original;
	List<BuildAttributeByBom> buildAttributeByBomList;

	private JPanel getEditPanel() {
		if (this.editPanel == null) {
			this.editPanel = new JPanel(new GridBagLayout());
			this.editPanel.setBorder(new TitledBorder("Build Attribute By BOM - Copy Function"));
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("New Model Group:", getModelGroupComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getModelGroupComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getSaveButton(), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(buttonPanel, getCancelButton(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
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

	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("SAVE");
			this.saveButton.setFont(FONT);
			this.saveButton.setToolTipText("Save the build attributes");
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

	private String getOriginalModelGroup() {
		if (this.original == null) {
			return null;
		}
		return this.original.getModelGroup();
	}


	private boolean canSave() {
		if (isCreateNew) {
			return (!StringUtils.isEmpty((String) getModelGroupComboBox().getSelectedItem()));
		} else {
			return ((!StringUtils.isEmpty((String) getModelGroupComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalModelGroup(), (String) getModelGroupComboBox().getSelectedItem())));
		}
	}

	/**
	 * Save the entered BuildAttributeByBom
	 */
	private void save() {
		if(MessageDialog.confirm(this, "Are you sure you want to copy all "+ buildAttributeByBomList.size() +" Attributes")) {
			for (final BuildAttributeByBom selectedRow : buildAttributeByBomList) {
				BuildAttributeByBom buildAttributeByBom = new BuildAttributeByBom((String) getModelGroupComboBox().getSelectedItem(), selectedRow.getPartNo(), 
						selectedRow.getPartColorCode(), selectedRow.getAttribute(), selectedRow.getAttributeValue(), selectedRow.getAttributeDescription());
				
				getBuildAttributeByBomDao().save(buildAttributeByBom);
			}
			
			this.setVisible(false);
			this.dispose();
		}
		
	}

	/**
	 * Cancel edit operation
	 */
	private void cancel() {
		canceled = true;
		this.setVisible(false);
		this.dispose();
	}

	public void showDialog(final List<BuildAttributeByBom> buildAttributeByBomList, final boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
		this.buildAttributeByBomList = buildAttributeByBomList;

		if (getModelGroupComboBox().getItemCount() != 1) {
			getModelGroupComboBox().setSelectedItem(getOriginalModelGroup());
		}
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


	public boolean isCanceled() {
		return canceled;
	}
}