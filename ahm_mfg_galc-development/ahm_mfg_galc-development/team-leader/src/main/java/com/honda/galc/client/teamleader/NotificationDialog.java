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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.NotificationDao;
import com.honda.galc.dao.conf.NotificationProviderDao;
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.StringUtil;

public class NotificationDialog extends JDialog  {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 24;

	private boolean canceled = false;
	private NotificationDao notificationDao;
	private NotificationProviderDao notificationProviderDao;
	private NotificationSubscriberDao notificationSubscriberDao;

	public NotificationDialog(final java.awt.Frame owner) {
		super(owner, true);
		init();
	}

	private void init() {
		try {
			setName("Notification");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(535, 250);
			setResizable(false);
			setTitle("Notification");
			initGuiFields();
			setContentPane(createEditPanel());
			addListeners();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private NotificationDao getNotificationDao() {
		if (this.notificationDao == null) {
			this.notificationDao = ServiceFactory.getDao(NotificationDao.class);
		}
		return this.notificationDao;
	}

	private NotificationProviderDao getNotificationProviderDao() {
		if (this.notificationProviderDao == null) {
			this.notificationProviderDao = ServiceFactory.getDao(NotificationProviderDao.class);
		}
		return this.notificationProviderDao;
	}

	private NotificationSubscriberDao getNotificationSubscriberDao() {
		if (this.notificationSubscriberDao == null) {
			this.notificationSubscriberDao = ServiceFactory.getDao(NotificationSubscriberDao.class);
		}
		return this.notificationSubscriberDao;
	}

	private JPanel editPanel;
	private LengthFieldBean notificationClassTextField;
	private LengthFieldBean description;
	private JCheckBox clientOnlyCheckBox;
	private JButton saveButton;
	private JButton cancelButton;
	private Notification original;

	private void initGuiFields() {
		this.saveButton = new JButton("SAVE");
		this.saveButton.setFont(FONT);
		this.saveButton.setToolTipText("Save the notification definition entered above");

		this.cancelButton = new JButton("CANCEL");
		this.cancelButton.setFont(FONT);
		this.cancelButton.setToolTipText("Cancel the edit operation");

		this.notificationClassTextField = createLengthFieldBean(256);

		this.description = createLengthFieldBean(256);

		this.clientOnlyCheckBox = new JCheckBox();
		this.clientOnlyCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NotificationDialog.this.saveButton.setEnabled(canSave());
			}
		});
	}

	private LengthFieldBean createLengthFieldBean(int maxLength) {
		LengthFieldBean lengthFieldBean = new LengthFieldBean();
		lengthFieldBean.getDocument().addDocumentListener(createEditDocumentListener());
		lengthFieldBean.setColumns(TEXT_FIELD_SIZE);
		lengthFieldBean.setFont(FONT);
		lengthFieldBean.setMaximumLength(maxLength);
		return lengthFieldBean;
	}

	private JPanel createEditPanel() {
		this.editPanel = new JPanel(new GridBagLayout());
		this.editPanel.setBorder(new TitledBorder("Notification"));
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Notification Class:", this.notificationClassTextField), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.notificationClassTextField, 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Description:", this.description), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.description, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Client Only:", this.clientOnlyCheckBox), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.clientOnlyCheckBox, 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		final JPanel buttonPanel = new JPanel(new GridBagLayout());
		ViewUtil.setGridBagConstraints(buttonPanel, this.saveButton, 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(buttonPanel, this.cancelButton, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 3, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
		return this.editPanel;
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
				NotificationDialog.this.saveButton.setEnabled(canSave());
			}
			public void removeUpdate(DocumentEvent e) {
				NotificationDialog.this.saveButton.setEnabled(canSave());
			}
			public void changedUpdate(DocumentEvent e) {
				NotificationDialog.this.saveButton.setEnabled(canSave());
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
		this.saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		{
			final String clickSaveButton = "clickSaveButton";
			this.saveButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), clickSaveButton);
			this.saveButton.getActionMap().put(clickSaveButton, new AbstractAction() {
				public void actionPerformed(ActionEvent actionEvent) {
					NotificationDialog.this.saveButton.doClick(0);
				}
			});
		}
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		{
			final String clickCancelButton = "clickCancelButton";
			this.cancelButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), clickCancelButton);
			this.cancelButton.getActionMap().put(clickCancelButton, new AbstractAction() {
				public void actionPerformed(ActionEvent actionEvent) {
					NotificationDialog.this.cancelButton.doClick(0);
				}
			});
		}
	}

	public Notification getEnteredNotification() {
		final String notificationClass = getEnteredNotificationClass();
		final String description = getEnteredDescription();
		final boolean clientOnly = getEnteredClientOnly();
		Notification notification = new Notification();
		notification.setNotificationClass(notificationClass);
		notification.setDescription(description);
		notification.setClientOnly(clientOnly);
		return notification;
	}

	private String getEnteredNotificationClass() {
		final String notificationClass = this.notificationClassTextField.getText();
		return StringUtils.isNotEmpty(notificationClass) ? notificationClass : null;
	}
	private String getEnteredDescription() {
		final String description = this.description.getText();
		return StringUtils.isNotEmpty(description) ? description : null;
	}
	private boolean getEnteredClientOnly() {
		final boolean clientOnly = this.clientOnlyCheckBox.isSelected();
		return clientOnly;
	}

	private boolean canSave() {
		if (this.original == null) {
			return notNullFieldsDefined();
		} else {
			return notNullFieldsDefined() &&
					(!StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getNotificationClass())
							|| !StringUtil.emptyOrEqual(getEnteredDescription(), this.original.getDescription())
							|| getEnteredClientOnly() != this.original.isClientOnly());
		}
	}

	/**
	 * Returns true if all NOT NULL fields are defined.
	 */
	private boolean notNullFieldsDefined() {
		return getEnteredNotificationClass() != null;
	}

	/**
	 * Returns true if at least one of the primary key fields has been updated.
	 */
	private boolean primaryKeyUpdated() {
		if (this.original == null) {
			return false;
		}
		return !StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getNotificationClass());
	}

	/**
	 * Save the entered Notification
	 */
	private void save() {
		final Notification notification = getEnteredNotification();
		final Notification existingNotification = getNotificationDao().findByKey(notification.getId());
		if (this.original == null) {
			if (existingNotification != null) {
				JOptionPane.showMessageDialog(this, "A Notification with id " + notification.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getNotificationDao().save(notification);
			AuditLoggerUtil.logAuditInfo(null,notification, "insert", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
		} else {
			if (primaryKeyUpdated()) {
				if (existingNotification != null) {
					JOptionPane.showMessageDialog(this, "A Notification with id " + notification.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// remove original
				getNotificationDao().remove(this.original);
				AuditLoggerUtil.logAuditInfo(this.original,null, "delete",getName(),ApplicationContext.getInstance().getUserId().toUpperCase(), "GALC", "GALC_Maintenance");
				// update providers
				getNotificationProviderDao().updateNotificationClass(notification.getNotificationClass(), this.original.getNotificationClass());
				// update subscribers
				getNotificationSubscriberDao().updateNotificationClass(notification.getNotificationClass(), this.original.getNotificationClass());
			}
			getNotificationDao().save(notification);
			AuditLoggerUtil.logAuditInfo(existingNotification,notification, "update", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
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

	public void showDialog(final Notification notification) {
		this.original = notification;
		if (this.original != null) {
			this.notificationClassTextField.setText(this.original.getNotificationClass());
			this.description.setText(this.original.getDescription());
			this.clientOnlyCheckBox.setSelected(this.original.isClientOnly());
		}
		this.saveButton.setEnabled(canSave());
		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}
}