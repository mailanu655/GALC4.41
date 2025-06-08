package com.honda.galc.client.teamleader;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BuildAttributeGroupDefinitionDao;
import com.honda.galc.entity.conf.BuildAttributeGroupDefinition;
import com.honda.galc.service.ServiceFactory;

public class BuildAttributeGroupDefinitionDialog extends JDialog  {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 20;

	private boolean canceled = false;
	private boolean isCreateNew = false;
	private BuildAttributeGroupDefinitionDao buildAttributeGroupDefinitionDao;

	private BuildAttributeGroupDefinitionDao getBuildAttributeGroupDefinitionDao() {
		if (this.buildAttributeGroupDefinitionDao == null) {
			this.buildAttributeGroupDefinitionDao = ServiceFactory.getDao(BuildAttributeGroupDefinitionDao.class);
		}
		return this.buildAttributeGroupDefinitionDao;
	}

	public BuildAttributeGroupDefinitionDialog(java.awt.Frame owner) {
		super(owner, true);
		init();
	}

	private void init() {
		try {
			setName("BuildAttributeGroupDefinition");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(475, 205);
			setResizable(false);
			setTitle("BuildAttributeGroupDefinition");
			setContentPane(getEditPanel());
			addListeners();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private JPanel editPanel;
	private LengthFieldBean attributeGroupTextField;
	private LengthFieldBean screenIdTextField;
	private JButton saveButton;
	private JButton cancelButton;
	private BuildAttributeGroupDefinition original;

	private JPanel getEditPanel() {
		if (this.editPanel == null) {
			this.editPanel = new JPanel(new GridBagLayout());
			this.editPanel.setBorder(new TitledBorder("Build Attribute Group Definition"));
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Group:", getAttributeGroupLengthFieldBean()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getAttributeGroupLengthFieldBean(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Screen ID:", getScreenIdLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getScreenIdLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getSaveButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(buttonPanel, getCancelButton(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 2, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
		}
		return this.editPanel;
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

	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("SAVE");
			this.saveButton.setFont(FONT);
			this.saveButton.setEnabled(false);
			this.saveButton.setToolTipText("Save the build attribute group definition entered above");
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

	private BuildAttributeGroupDefinition getEnteredBuildAttributeGroupDefinition() {
		final String attributeGroup = getAttributeGroupLengthFieldBean().getText();
		final String screenId = getScreenIdLengthFieldBean().getText();
		BuildAttributeGroupDefinition buildAttributeGroupDefinition = new BuildAttributeGroupDefinition(attributeGroup, screenId);
		return buildAttributeGroupDefinition;
	}

	private String getOriginalAttributeGroup() {
		if (this.original == null) {
			return null;
		}
		return this.original.getAttributeGroup();
	}

	private String getOriginalScreenId() {
		if (this.original == null) {
			return null;
		}
		return this.original.getScreenId();
	}

	private boolean canSave() {
		if (isCreateNew) {
			return (!StringUtils.isEmpty(getAttributeGroupLengthFieldBean().getText())
					&& !StringUtils.isEmpty(getScreenIdLengthFieldBean().getText()));
		} else {
			return ((!StringUtils.isEmpty(getAttributeGroupLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalAttributeGroup(), getAttributeGroupLengthFieldBean().getText()))
					|| (!StringUtils.isEmpty(getScreenIdLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalScreenId(), getScreenIdLengthFieldBean().getText())));
		}
	}

	/**
	 * Save the entered BuildAttributeGroupDefinition
	 */
	private void save() {
		BuildAttributeGroupDefinition buildAttributeGroupDefinition = getEnteredBuildAttributeGroupDefinition();
		if (isCreateNew) {
			BuildAttributeGroupDefinition existingBuildAttributeGroupDefinition = getBuildAttributeGroupDefinitionDao().findByKey(buildAttributeGroupDefinition.getAttributeGroup());
			if (existingBuildAttributeGroupDefinition != null) {
				JOptionPane.showMessageDialog(this, "A BuildAttributeGroupDefinition with group " + existingBuildAttributeGroupDefinition.getAttributeGroup() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			if (!ObjectUtils.equals(getOriginalAttributeGroup(), buildAttributeGroupDefinition.getAttributeGroup())) {
				getBuildAttributeGroupDefinitionDao().remove(this.original);
			}
		}
		getBuildAttributeGroupDefinitionDao().save(buildAttributeGroupDefinition);
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

	public void showDialog(final BuildAttributeGroupDefinition buildAttributeGroupDefinition, final boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
		this.original = buildAttributeGroupDefinition;

		getAttributeGroupLengthFieldBean().setText(getOriginalAttributeGroup());
		getScreenIdLengthFieldBean().setText(getOriginalScreenId());
		getSaveButton().setEnabled(canSave());

		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}
}