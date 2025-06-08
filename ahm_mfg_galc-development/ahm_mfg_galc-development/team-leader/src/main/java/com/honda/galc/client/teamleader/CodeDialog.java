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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
import com.honda.galc.dao.product.CodeDao;
import com.honda.galc.entity.product.Code;
import com.honda.galc.service.ServiceFactory;

public class CodeDialog extends JDialog  {

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
	private CodeDao codeDao;

	public CodeDialog(final java.awt.Frame owner, final List<String> codeTypesAllowed) {
		super(owner, true);
		init(codeTypesAllowed);
	}

	private void init(final List<String> codeTypesAllowed) {
		try {
			setName("Code");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(500, 400);
			setResizable(false);
			setTitle("Code");
			setContentPane(getEditPanel());
			addListeners();
			loadCodeTypeComboBox(codeTypesAllowed);
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private CodeDao getCodeDao() {
		if (this.codeDao == null) {
			this.codeDao = ServiceFactory.getDao(CodeDao.class);
		}
		return this.codeDao;
	}

	private JPanel editPanel;
	private JComboBox codeTypeComboBox;
	private LengthFieldBean codeTextField;
	private LengthFieldBean codeDescriptionTextField;
	private LengthFieldBean divisionIdTextField;
	private JButton saveButton;
	private JButton cancelButton;
	private Code original;

	private JPanel getEditPanel() {
		if (this.editPanel == null) {
			this.editPanel = new JPanel(new GridBagLayout());
			this.editPanel.setBorder(new TitledBorder("Code"));
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Code Type:", getCodeTypeComboBox()), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getCodeTypeComboBox(), 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Code:", getCodeLengthFieldBean()), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getCodeLengthFieldBean(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Code Description:", getCodeDescriptionLengthFieldBean()), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getCodeDescriptionLengthFieldBean(), 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Division Id:", getDivisionIdLengthFieldBean()), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.editPanel, getDivisionIdLengthFieldBean(), 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 1.0, null);
			final JPanel buttonPanel = new JPanel(new GridBagLayout());
			ViewUtil.setGridBagConstraints(buttonPanel, getSaveButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(buttonPanel, getCancelButton(), 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
			ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 4, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
		}
		return this.editPanel;
	}

	private JComboBox getCodeTypeComboBox() {
		if (this.codeTypeComboBox == null) {
			this.codeTypeComboBox = createJComboBox();
		}
		return this.codeTypeComboBox;
	}

	private LengthFieldBean getCodeLengthFieldBean() {
		if (this.codeTextField == null) {
			this.codeTextField = createLengthFieldBean(64);
		}
		return this.codeTextField;
	}

	private LengthFieldBean getCodeDescriptionLengthFieldBean() {
		if (this.codeDescriptionTextField == null) {
			this.codeDescriptionTextField = createLengthFieldBean(80);
		}
		return this.codeDescriptionTextField;
	}

	private LengthFieldBean getDivisionIdLengthFieldBean() {
		if (this.divisionIdTextField == null) {
			this.divisionIdTextField = createLengthFieldBean(16);
		}
		return this.divisionIdTextField;
	}

	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("SAVE");
			this.saveButton.setFont(FONT);
			this.saveButton.setToolTipText("Save the Code entered above");
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

	private Code getEnteredCode() {
		final String codeType = (String) getCodeTypeComboBox().getSelectedItem();
		final String code = getCodeLengthFieldBean().getText();
		final String codeDescription = getCodeDescriptionLengthFieldBean().getText();
		final String divisionId = getDivisionIdLengthFieldBean().getText();
		Code enteredCode = new Code(codeType, code, codeDescription, divisionId);
		return enteredCode;
	}

	private String getOriginalCodeType() {
		if (this.original == null) {
			return null;
		}
		return this.original.getCodeType();
	}

	private String getOriginalCode() {
		if (this.original == null) {
			return null;
		}
		return this.original.getCode();
	}

	private String getOriginalCodeDescription() {
		if (this.original == null) {
			return null;
		}
		return this.original.getCodeDescription();
	}

	private String getOriginalDivisionId() {
		if (this.original == null) {
			return null;
		}
		return this.original.getDivisionId();
	}

	private boolean canSave() {
		if (isCreateNew) {
			return (StringUtils.isNotEmpty((String) getCodeTypeComboBox().getSelectedItem())
					&& StringUtils.isNotEmpty(getCodeLengthFieldBean().getText()));
		} else {
			return ((StringUtils.isNotEmpty((String) getCodeTypeComboBox().getSelectedItem()) && !ObjectUtils.equals(getOriginalCodeType(), (String) getCodeTypeComboBox().getSelectedItem()))
					|| (StringUtils.isNotEmpty(getCodeLengthFieldBean().getText()) && !ObjectUtils.equals(getOriginalCode(), getCodeLengthFieldBean().getText()))
					|| (!ObjectUtils.equals(getOriginalCodeDescription(), getCodeDescriptionLengthFieldBean().getText()))
					|| (!ObjectUtils.equals(getOriginalDivisionId(), getDivisionIdLengthFieldBean().getText())));
		}
	}

	/**
	 * Save the entered Code
	 */
	private void save() {
		final Code enteredCode = getEnteredCode();
		if (isCreateNew) {
			final Code existingCode = getCodeDao().findByKey(enteredCode.getId());
			if (existingCode != null) {
				JOptionPane.showMessageDialog(this, "A Code with id " + enteredCode.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} else {
			if (!ObjectUtils.equals(getOriginalCodeType(), enteredCode.getCodeType())
					|| !ObjectUtils.equals(getOriginalCode(), enteredCode.getCode())) {
				getCodeDao().remove(this.original);
			}
		}
		getCodeDao().save(enteredCode);
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

	public void showDialog(final Code code, final boolean isCreateNew) {
		this.isCreateNew = isCreateNew;
		this.original = code;

		getCodeTypeComboBox().setEnabled(isCreateNew);
		getCodeLengthFieldBean().setEnabled(isCreateNew);
		if (getCodeTypeComboBox().getItemCount() != 1) {
			getCodeTypeComboBox().setSelectedItem(getOriginalCodeType());
		}
		getCodeLengthFieldBean().setText(getOriginalCode());
		getCodeDescriptionLengthFieldBean().setText(getOriginalCodeDescription());
		getDivisionIdLengthFieldBean().setText(getOriginalDivisionId());
		getSaveButton().setEnabled(canSave());

		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	private void loadCodeTypeComboBox(final List<String> codeTypesAllowed) {
		ComboBoxModel codeTypesModel = new DefaultComboBoxModel(codeTypesAllowed.toArray());
		getCodeTypeComboBox().setModel(codeTypesModel);
		if (codeTypesAllowed != null && !codeTypesAllowed.isEmpty()) {
			if (codeTypesAllowed.size() == 1) {
				getCodeTypeComboBox().setSelectedIndex(0);
			} else {
				getCodeTypeComboBox().insertItemAt("", 0);
				getCodeTypeComboBox().setSelectedIndex(-1);
			}
		} else {
			getCodeTypeComboBox().insertItemAt("", 0);
			getCodeTypeComboBox().setSelectedIndex(-1);
		}
	}

	public boolean isCanceled() {
		return canceled;
	}
}