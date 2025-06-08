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
import com.honda.galc.dao.conf.NotificationSubscriberDao;
import com.honda.galc.entity.conf.Notification;
import com.honda.galc.entity.conf.NotificationProvider;
import com.honda.galc.entity.conf.NotificationSubscriber;
import com.honda.galc.enumtype.SubscriptionType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.StringUtil;

public class NotificationSubscriberDialog extends JDialog  {

	private static final long serialVersionUID = 1L;
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final int TEXT_FIELD_SIZE = 24;
	private static final Dimension COMBO_BOX_DIMENSION;
	static {
		JTextField jTextField = new JTextField(TEXT_FIELD_SIZE);
		jTextField.setFont(FONT);
		JComboBox jComboBox = new JComboBox();
		jComboBox.setFont(FONT);
		COMBO_BOX_DIMENSION = new Dimension((int) jTextField.getPreferredSize().getWidth(), (int) jComboBox.getPreferredSize().getHeight());
	}

	private boolean canceled = false;
	private NotificationSubscriberDao notificationSubscriberDao;

	public NotificationSubscriberDialog(final java.awt.Frame owner, final Notification notification, final List<NotificationProvider> providers) {
		super(owner, true);
		init(notification, providers);
	}

	private void init(final Notification notification, final List<NotificationProvider> providers) {
		try {
			setName("NotificationSubscriber");
			setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(600, 435);
			setResizable(false);
			setTitle("NotificationSubscriber");
			initGuiFields(notification, providers);
			setContentPane(createEditPanel());
			addListeners();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	private NotificationSubscriberDao getNotificationSubscriberDao() {
		if (this.notificationSubscriberDao == null) {
			this.notificationSubscriberDao = ServiceFactory.getDao(NotificationSubscriberDao.class);
		}
		return this.notificationSubscriberDao;
	}

	private JPanel editPanel;
	private JTextField notificationClassTextField;
	private LengthFieldBean clientNameTextField;
	private LengthFieldBean clientIpTextField;
	private LengthFieldBean clientPortTextField;
	private LengthFieldBean notificationHandlerClassTextField;
	private JComboBox providerComboBox;
	private JComboBox subscriptionTypeComboBox;
	private JButton saveButton;
	private JButton cancelButton;
	private NotificationSubscriber original;

	private void initGuiFields(final Notification notification, final List<NotificationProvider> providers) {
		this.saveButton = new JButton("SAVE");
		this.saveButton.setFont(FONT);
		this.saveButton.setToolTipText("Save the notification subscriber definition entered above");

		this.cancelButton = new JButton("CANCEL");
		this.cancelButton.setFont(FONT);
		this.cancelButton.setToolTipText("Cancel the edit operation");

		this.notificationClassTextField = new JTextField();
		this.notificationClassTextField.setColumns(TEXT_FIELD_SIZE);
		this.notificationClassTextField.setFont(FONT);
		this.notificationClassTextField.setText(notification.getNotificationClass());
		this.notificationClassTextField.setEditable(false);

		this.clientNameTextField = createLengthFieldBean(32);

		this.clientIpTextField = createLengthFieldBean(255);

		this.clientPortTextField = createLengthFieldBean(10);
		this.clientPortTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				String newValue = clientPortTextField.getText();
				if (!newValue.matches("\\d+")) {
					JOptionPane.showMessageDialog(NotificationSubscriberDialog.this, "Client Port may only contain numeric characters.", "Error", JOptionPane.ERROR_MESSAGE);
					final String updateValue = newValue.replaceAll("[^0-9]","");
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							clientPortTextField.setText(updateValue);
						}
					});
					return;
				}
			}
			public void removeUpdate(DocumentEvent e) {}
			public void changedUpdate(DocumentEvent e) {}
		});

		this.notificationHandlerClassTextField = createLengthFieldBean(255);

		this.providerComboBox = createJComboBox();
		if (providers != null && providers.size() > 0) {
			String[] providerArray = new String[providers.size()];
			for (int i = 0; i < providers.size(); i++) {
				providerArray[i] = providers.get(i).getHostName();
			}
			this.providerComboBox.setModel(new DefaultComboBoxModel(providerArray));
		}
		this.providerComboBox.insertItemAt(NotificationSubscriber.ANY_PROVIDER, 0);
		this.providerComboBox.setSelectedIndex(-1);
		this.providerComboBox.setEditable(true);

		this.subscriptionTypeComboBox = createJComboBox();
		this.subscriptionTypeComboBox.setModel(new DefaultComboBoxModel(SubscriptionType.values()));
		this.subscriptionTypeComboBox.setSelectedIndex(-1);
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
				NotificationSubscriberDialog.this.saveButton.setEnabled(canSave());
			}
		});
		return jComboBox;
	}

	private JPanel createEditPanel() {
		this.editPanel = new JPanel(new GridBagLayout());
		this.editPanel.setBorder(new TitledBorder("Notification Subscriber"));
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Notification Class:", this.notificationClassTextField), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.notificationClassTextField, 1, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Client Name:", this.clientNameTextField), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.clientNameTextField, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Client IP:", this.clientIpTextField), 0, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.clientIpTextField, 1, 2, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Client Port:", this.clientPortTextField), 0, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.clientPortTextField, 1, 3, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Notification Handler Class:", this.notificationHandlerClassTextField), 0, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.notificationHandlerClassTextField, 1, 4, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Provider:", this.providerComboBox), 0, 5, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.providerComboBox, 1, 5, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(this.editPanel, createLabelFor("Subscription Type:", this.subscriptionTypeComboBox), 0, 6, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(this.editPanel, this.subscriptionTypeComboBox, 1, 6, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		final JPanel buttonPanel = new JPanel(new GridBagLayout());
		ViewUtil.setGridBagConstraints(buttonPanel, this.saveButton, 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(buttonPanel, this.cancelButton, 1, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_END, 0.5, 1.0);
		ViewUtil.setGridBagConstraints(this.editPanel, buttonPanel, 0, 7, 2, 1, null, null, null, null, GridBagConstraints.PAGE_START, 1.0, 1.0);
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
				NotificationSubscriberDialog.this.saveButton.setEnabled(canSave());
			}
			public void removeUpdate(DocumentEvent e) {
				NotificationSubscriberDialog.this.saveButton.setEnabled(canSave());
			}
			public void changedUpdate(DocumentEvent e) {
				NotificationSubscriberDialog.this.saveButton.setEnabled(canSave());
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
					NotificationSubscriberDialog.this.saveButton.doClick(0);
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
					NotificationSubscriberDialog.this.cancelButton.doClick(0);
				}
			});
		}
	}

	public NotificationSubscriber getEnteredNotificationSubscriber() {
		final String notificationClass = getEnteredNotificationClass();
		final String clientName = getEnteredClientName();
		final String clientIp = getEnteredClientIp();
		final int clientPort = getEnteredClientPort();
		final String notificationHandlerClass = getEnteredNotificationHandlerClass();
		final String provider = getEnteredProvider();
		final SubscriptionType subscriptionType = getEnteredSubscriptionType();
		NotificationSubscriber notificationSubscriber = new NotificationSubscriber(notificationClass, clientIp, clientPort, provider, clientName);
		notificationSubscriber.setNotificationHandlerClass(notificationHandlerClass);
		notificationSubscriber.setSubscriptionType(subscriptionType);
		return notificationSubscriber;
	}

	private String getEnteredNotificationClass() {
		final String notificationClass = this.notificationClassTextField.getText();
		return StringUtils.isNotEmpty(notificationClass) ? notificationClass : null;
	}
	private String getEnteredClientName() {
		final String clientName = this.clientNameTextField.getText();
		return StringUtils.isNotEmpty(clientName) ? clientName : null;
	}
	private String getEnteredClientIp() {
		final String clientIp = this.clientIpTextField.getText();
		return StringUtils.isNotEmpty(clientIp) ? clientIp : null;
	}
	private Integer getEnteredClientPort() {
		try {
			final String clientPort = this.clientPortTextField.getText();
			return StringUtils.isNotEmpty(clientPort) && StringUtils.isNumeric(clientPort) ? Integer.valueOf(clientPort) : null;
		} catch (NumberFormatException nfe) {
			Logger.getLogger().error(nfe, "Error getting entered client port");
			return null;
		}
	}
	private String getEnteredNotificationHandlerClass() {
		final String notificationHandlerClass = this.notificationHandlerClassTextField.getText();
		return StringUtils.isNotEmpty(notificationHandlerClass) ? notificationHandlerClass : null;
	}
	private String getEnteredProvider() {
		final String provider = (String) this.providerComboBox.getSelectedItem();
		return StringUtils.isNotEmpty(provider) ? provider : null;
	}
	private SubscriptionType getEnteredSubscriptionType() {
		final SubscriptionType subscriptionType = (SubscriptionType) this.subscriptionTypeComboBox.getSelectedItem();
		return subscriptionType;
	}

	private boolean canSave() {
		if (this.original == null) {
			return notNullFieldsDefined();
		} else {
			return notNullFieldsDefined() &&
					(!StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getId().getNotificationClass())
							|| !StringUtil.emptyOrEqual(getEnteredClientName(), this.original.getClientName())
							|| !StringUtil.emptyOrEqual(getEnteredClientIp(), this.original.getId().getClientIp())
							|| !ObjectUtils.equals(getEnteredClientPort(), this.original.getId().getClientPort())
							|| !StringUtil.emptyOrEqual(getEnteredNotificationHandlerClass(), this.original.getNotificationHandlerClass())
							|| !StringUtil.emptyOrEqual(getEnteredProvider(), this.original.getId().getProvider())
							|| !ObjectUtils.equals(getEnteredSubscriptionType(), this.original.getSubscriptionType()));
		}
	}

	/**
	 * Returns true if all NOT NULL fields are defined.
	 */
	private boolean notNullFieldsDefined() {
		return (getEnteredNotificationClass() != null
				&& getEnteredClientIp() != null
				&& getEnteredClientPort() != null
				&& getEnteredProvider() != null
				&& getEnteredSubscriptionType() != null);
	}

	/**
	 * Returns true if at least one of the primary key fields has been updated.
	 */
	private boolean primaryKeyUpdated() {
		if (this.original == null) {
			return false;
		}
		return !StringUtil.emptyOrEqual(getEnteredNotificationClass(), this.original.getId().getNotificationClass())
				|| !StringUtil.emptyOrEqual(getEnteredClientIp(), this.original.getId().getClientIp())
				|| !ObjectUtils.equals(getEnteredClientPort(), this.original.getId().getClientPort())
				|| !StringUtil.emptyOrEqual(getEnteredProvider(), this.original.getId().getProvider());
	}

	/**
	 * Save the entered NotificationSubscriber
	 */
	private void save() {
		final NotificationSubscriber notificationSubscriber = getEnteredNotificationSubscriber();
		final NotificationSubscriber existingNotificationSubscriber = getNotificationSubscriberDao().findByKey(notificationSubscriber.getId());
		if (this.original == null) {
			if (existingNotificationSubscriber != null) {
				JOptionPane.showMessageDialog(this, "A NotificationSubscriber with id " + notificationSubscriber.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getNotificationSubscriberDao().save(notificationSubscriber);
			AuditLoggerUtil.logAuditInfo(null,notificationSubscriber, "insert", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
		} else {
			if (primaryKeyUpdated()) {
				if (existingNotificationSubscriber != null) {
					JOptionPane.showMessageDialog(this, "A NotificationSubscriber with id " + notificationSubscriber.getId() + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				getNotificationSubscriberDao().remove(this.original);
				AuditLoggerUtil.logAuditInfo(this.original,null, "delete",getName(),ApplicationContext.getInstance().getUserId().toUpperCase(), "GALC", "GALC_Maintenance");
			}
			getNotificationSubscriberDao().save(notificationSubscriber);
			AuditLoggerUtil.logAuditInfo(existingNotificationSubscriber,notificationSubscriber, "update", getName(),ApplicationContext.getInstance().getUserId().toUpperCase() , "GALC", "GALC_Maintenance");
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

	public void showDialog(final NotificationSubscriber notificationSubscriber) {
		this.original = notificationSubscriber;
		if (this.original != null) {
			this.notificationClassTextField.setText(this.original.getId().getNotificationClass());
			this.clientNameTextField.setText(this.original.getClientName());
			this.clientIpTextField.setText(this.original.getId().getClientIp());
			this.clientPortTextField.setText(String.valueOf(this.original.getId().getClientPort()));
			this.notificationHandlerClassTextField.setText(this.original.getNotificationHandlerClass());
			this.providerComboBox.setSelectedItem(this.original.getId().getProvider());
			this.subscriptionTypeComboBox.setSelectedItem(this.original.getSubscriptionType());
		}
		this.canceled = false;
		setLocation(200, 200);
		setVisible(true);
	}

	public boolean isCanceled() {
		return canceled;
	}
}