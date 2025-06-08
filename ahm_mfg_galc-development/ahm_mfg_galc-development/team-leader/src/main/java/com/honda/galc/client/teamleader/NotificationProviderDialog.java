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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.NotificationProviderDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.entity.conf.NotificationProviderId;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.StringUtil;

public class NotificationProviderDialog extends JDialog  {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 24;

	private boolean canceled = false;
	private NotificationProviderDao notificationProviderDao;

	public NotificationProviderDialog(final java.awt.Frame owner, final Notification notification) {
		super(owner, true);
		init(notification);
	}

	private void init(final Notification notification) {
		try {
			setName("NotificationProvider");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(535, 335);
			setResizable(false);
			setTitle("NotificationProvider");
			initGuiFields(notification);
			setContentPane(createEditPanel());
			addListeners();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private NotificationProviderDao getNotificationProviderDao() {
		if (this.notificationProviderDao == null) {
			this.notificationProviderDao = ServiceFactory.getDao(NotificationProviderDao.class);
		}
		return this.notificationProviderDao;
	}

	private JPanel editPanel;
	private JTextField notificationClassTextField;
	private LengthFieldBean hostIpTextField;
	private LengthFieldBean hostPortTextField;
	private LengthFieldBean hostNameTextField;
	private LengthFieldBean descriptionTextField;
	private JButton saveButton;
	private JButton cancelButton;
	private NotificationProvider original;

	private void initGuiFields(final Notification notification) {
		this.saveButton = new JButton("SAVE");
		this.saveButton.setFont(FONT);
		this.saveButton.setToolTipText("Save the notification provider definition entered above");

		this.cancelButton = new JButton("CANCEL");
		this.cancelButton.setFont(FONT);
		this.cancelButton.setToolTipText("Cancel the edit operation");

		this.notificationClassTextField = new JTextField();
		this.notificationClassTextField.setColumns(TEXT_FIELD_SIZE);
		this.notificationClassTextField.setFont(FONT);
		this.notificationClassTextField.setText(notification.getNotificationClass());
		this.notificationClassTextField.setEditable(false);

		this.hostIpTextField = createLengthFieldBean(255);

		this.hostPortTextField = createLengthFieldBean(10);
		this.hostPortTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				String newValue = hostPortTextField.getText();
				if (!newValue.matches("\\d+")) {
					JOptionPane.showMessageDialog(NotificationProviderDialog.this, "Host Port may only contain numeric characters.", "Error", JOptionPane.ERROR_MESSAGE);
					final String updateValue = newValue.replaceAll("[^0-9]","");
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							hostPortTextField.setText(updateValue);
						}
					});
					return;
				}
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});

		this.hostNameTextField = createLengthFieldBean(32);

		this.descriptionTextField = createLengthFieldBean(256);
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
		this.editPanel.setBorder(new TitledBorder("Notification Provider"));
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Notification Class:", this.notificationClassTextField), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.notificationClassTextField, 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Host IP:", this.hostIpTextField), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.hostIpTextField, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Host Port:", this.hostPortTextField), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.hostPortTextField, 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Host Name:", this.hostNameTextField), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.hostNameTextField, 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Description:", this.descriptionTextField), 0, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.descriptionTextField, 1, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		final JPanel buttonPanel = new JPanel(new GridBagLayout());
		ViewUtil.setGridBagConstraints(buttonPanel, this.saveButton, 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(buttonPanel, this.cancelButton, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 5, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
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
				NotificationProviderDialog.this.saveButton.setEnabled(canSave());
			}
			public void removeUpdate(DocumentEvent e) {
				NotificationProviderDialog.this.saveButton.setEnabled(canSave());
			}
			public void changedUpdate(DocumentEvent e) {
				NotificationProviderDialog.this.saveButton.setEnabled(canSave());
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
					NotificationProviderDialog.this.saveButton.doClick(0);
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
					NotificationProviderDialog.this.cancelButton.doClick(0);
				}
			});
		}
	}

	public NotificationProvider getEnteredNotificationProvider() {
		final String notificationClass = getEnteredNotificationClass();
		final String hostIp = getEnteredHostIp();
		final Integer hostPort = getEnteredHostPort();
		final String hostName = getEnteredHostName();
		final String description = getEnteredDescription();
		NotificationProvider notificationProvider = new NotificationProvider();
		NotificationProviderId notificationProviderId = new NotificationProviderId();
		notificationProviderId.setNotificationClass(notificationClass);
		notificationProviderId.setHostIp(hostIp);
		notificationProviderId.setHostPort(hostPort);
		notificationProvider.setId(notificationProviderId);
		notificationProvider.setHostName(hostName);
		notificationProvider.setDescription(description);
		return notificationProvider;
	}

	private String getEnteredNotificationClass() {
		final String notificationClass = this.notificationClassTextField.getText();
		return StringUtils.isNotEmpty(notificationClass) ? notificationClass : null;
	}
	private String getEnteredHostIp() {
		final String hostIp = this.hostIpTextField.getText();
		return StringUtils.isNotEmpty(hostIp) ? hostIp : null;
	}
	private Integer getEnteredHostPort() {
		try {
			final String hostPort = this.hostPortTextField.getText();
			return StringUtils.isNotEmpty(hostPort) && StringUtils.isNumeric(hostPort) ? Integer.valueOf(hostPort) : null;
		} catch (NumberFormatException nfe) {
			Logger.getLogger().error(nfe, "Error getting entered host port");
			return null;
		}
	}
	private String getEnteredHostName() {
		final String hostName = this.hostNameTextField.getText();
		return StringUtils.isNotEmpty(hostName) ? hostName : null;
	}
	private String getEnteredDescription() {
		final String description = this.descriptionTextField.getText();
		return StringUtils.isNotEmpty(description) ? description : null;
	}

	private boolean canSave() {
		if (this.original == null) {
			return notNullFieldsDefined();
		} else {
			return notNullFieldsDefined() &&
					(!StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getId().getNotificationClass())
							|| !StringUtil.emptyOrEqual(getEnteredHostIp(), this.original.getId().getHostIp())
							|| !ObjectUtils.equals(getEnteredHostPort(), this.original.getId().getHostPort())
							|| !StringUtil.emptyOrEqual(getEnteredHostName(), this.original.getHostName())
							|| !StringUtil.emptyOrEqual(getEnteredDescription(), this.original.getDescription()));
		}
	}

	/**
	 * Returns true if all NOT NULL fields are defined.
	 */
	private boolean notNullFieldsDefined() {
		return (getEnteredNotificationClass() != null
				&& getEnteredHostIp() != null
				&& getEnteredHostPort() != null);
	}

	/**
	 * Returns true if at least one of the primary key fields has been updated.
	 */
	private boolean primaryKeyUpdated() {
		if (this.original == null) {
			return false;
		}
		return !StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getId().getNotificationClass())
				|| !StringUtil.emptyOrEqual(getEnteredHostIp(), this.original.getId().getHostIp())
				|| !ObjectUtils.equals(getEnteredHostPort(), this.original.getId().getHostPort());
	}

	/**
	 * Save the entered NotificationProvider
	 */
	private void save() {
		final NotificationProvider notificationProvider = getEnteredNotificationProvider();
		final NotificationProvider existingNotificationProvider = getNotificationProviderDao().findByKey(notificationProvider.getId());
		if (this.original == null) {
			if (existingNotificationProvider != null) {
				JOptionPane.showMessageDialog(this, "A NotificationProvider with id " + notificationProvider.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getNotificationProviderDao().save(notificationProvider);
			AuditLoggerUtil.logAuditInfo(null,notificationProvider, "insert", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
		} else {
			if (primaryKeyUpdated()) {
				if (existingNotificationProvider != null) {
					JOptionPane.showMessageDialog(this, "A NotificationProvider with id " + notificationProvider.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				getNotificationProviderDao().remove(this.original);
				AuditLoggerUtil.logAuditInfo(this.original,null, "delete",getName(),ApplicationContext.getInstance().getUserId().toUpperCase(), "GALC", "GALC_Maintenance");
			}
			getNotificationProviderDao().save(notificationProvider);
			AuditLoggerUtil.logAuditInfo(existingNotificationProvider,notificationProvider, "update", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
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

	public void showDialog(final NotificationProvider notificationProvider) {
		this.original = notificationProvider;
		if (this.original != null) {
			this.notificationClassTextField.setText(this.original.getId().getNotificationClass());
			this.hostIpTextField.setText(this.original.getId().getHostIp());
			this.hostPortTextField.setText(String.valueOf(this.original.getId().getHostPort()));
			this.hostNameTextField.setText(this.original.getHostName());
			this.descriptionTextField.setText(this.original.getDescription());
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