package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JRException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.teamleader.qics.image.JpgFilter;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ImageBean;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.KeyValueTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DataContainerXMLUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.device.exception.DeviceInUseException;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.entity.enumtype.TemplateType;
import com.honda.galc.entity.product.Template;
import com.honda.galc.enumtype.EnumUtil;
import com.honda.galc.script.DataUtil;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.JasperExternalPrintAttributes;
import com.honda.galc.service.printing.JasperPrintUtil;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ExtensionFileFilter;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.StringUtil;

public class GenericTemplatePanel extends TabbedPanel implements ActionListener, IPrintDeviceListener {

	private static final long serialVersionUID = 1L;
	public static final int ETCH_DELAY = 5000;
	private static final String CLIENT_ID = "CLIENT_ID";
	private static final String TRAY_VALUE = "TRAY_VALUE";
	private static final String JASPER_DUPLEX_FLAG = "JASPER_DUPLEX_FLAG";
	private static final String PRINT_QUANTITY = "PRINT_QUANTITY";
	private static final String JRXML_EXT = "jrxml";
	private static final String JASPER_EXT = "jasper";
	private LabeledTextField nameField = new LabeledTextField("Template Description");
	private LabeledComboBox templateComboBox;
	private LabeledComboBox printerComboBox;
	private LabeledComboBox formComboBox;
	private LabeledComboBox templateTypeComboBox;
	private ComboBoxModel<String> model;
	private JTextArea templateTextArea;
	private JScrollPane textAreascrollPane;
	private TablePane attributePane;
	private String[] attribarray;
	private List<KeyValue> attributes = new ArrayList<KeyValue>();
	private KeyValueTableModel attributeTableModel;
	private int _deviceAccessKey = -1;
	protected JButton saveButton = null;
	protected JButton printButton = null;
	protected JButton loadButton = null;
	protected JButton refreshButton = null;
	protected TemplateDao templatesDao;
	protected BuildAttributeDao buildAttributeDao;
	private ImageBean image;
	JSplitPane imagePanel;

	// === model === //
	private AtomicInteger actionCounter;
	private Map<String, Template> templateMap = new TreeMap<String, Template>();
	private String lastSelectedFileDirectory;
	private byte[] uploadedTemplateData;
	
	public GenericTemplatePanel(TabbedMainWindow mainWindow) {
		super("Generic Template", KeyEvent.VK_G, mainWindow);
		initComponents();
		addListeners();
	}

	protected void initComponents() {
		setActionCounter(new AtomicInteger(0));
		initNameField();
		setLayout(new MigLayout("insets 1", "[grow,fill]"));
		this.image = createImageBean();
		add(createComboPanel(),"wrap");
		add(createAttributesPanel(),"h 100%,wrap");
		add(createButtonPanel());
		loadData();
	}

	private void addListeners() {
		getFormComboBox().getComponent().addActionListener(this);
		getTemplateComboBox().getComponent().addActionListener(this);
		getTemplateTypeComboBox().getComponent().addActionListener(this);
		getPrinterComboBox().getComponent().addActionListener(this);
		getSaveButton().addActionListener(this);
		getLoadButton().addActionListener(this);
		getPrintButton().addActionListener(this);
		getRefreshButton().addActionListener(this);
		templateTextArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyTyped(KeyEvent e) {
				if (getTemplateTextArea().isEditable()){
					saveButton.setEnabled(true);
					getRefreshButton().setEnabled(true);
				}
			}
		});
		
		nameField.getComponent().addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyTyped(KeyEvent e) {
				saveButton.setEnabled(true);
			}
		});
	}

	@Override
	public void onTabSelected() {
		if (!isInitialized) {

			isInitialized = true;
		}
	}

	private TablePane createAttributeTablePane() {
		attributePane = new TablePane("PrintAttributeFormatPane");
		attributePane.setBounds(520, 150, 500, 450);
		attributeTableModel = new KeyValueTableModel(attributes, "Attribute",
				"Att_Value", attributePane.getTable(), true);
		return attributePane;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setFont(Fonts.DIALOG_BOLD_14);
		buttonPanel.setLayout(new GridLayout(1, 4, 5, 5));
		buttonPanel.add(getLoadButton());
		buttonPanel.add(getPrintButton());
		buttonPanel.add(getRefreshButton());
		buttonPanel.add(getSaveButton());
		buttonPanel.setSize(1000, 50);
		buttonPanel.setLocation(10, 650);
		return buttonPanel;
	}

	private JButton getPrintButton() {
		if (printButton == null) {
			printButton = new JButton();
			printButton.setName("PrintButton");
			printButton.setFont(Fonts.DIALOG_PLAIN_18);
			printButton.setText("Print");
			printButton.setMultiClickThreshhold(1000);
			printButton.setEnabled(false);
		}
		return printButton;
	}

	private JButton getRefreshButton() {
		if (refreshButton == null) {
			refreshButton = new JButton();
			refreshButton.setName("refreshButton");
			refreshButton.setFont(Fonts.DIALOG_PLAIN_18);
			refreshButton.setText("Attributes Refresh");
			refreshButton.setEnabled(false);
		}
		return refreshButton;
	}

	private JButton getSaveButton() {
		if (saveButton == null) {
			saveButton = new JButton();
			saveButton.setName("SaveButton");
			saveButton.setFont(Fonts.DIALOG_PLAIN_18);
			saveButton.setText("Save");
			saveButton.setEnabled(false);
		}
		return saveButton;
	}

	private JPanel createComboPanel() {
		JPanel comboPanel = new JPanel();
		comboPanel.setFont(Fonts.DIALOG_BOLD_14);
		comboPanel.setLayout(new GridLayout(2, 3, 5, 5));
		comboPanel.add(getFormComboBox());
		comboPanel.add(getTemplateComboBox());
		comboPanel.add(getPrinterComboBox());
		comboPanel.add(getTemplateTypeComboBox());
		comboPanel.add(nameField);
		comboPanel.setSize(1000, 100);
		comboPanel.setLocation(10, 10);
		return comboPanel;
	}

	private LabeledComboBox getTemplateTypeComboBox() {
		if (templateTypeComboBox == null) {
			templateTypeComboBox = new LabeledComboBox("Template Type");
			templateTypeComboBox.getComponent().setName("TemplateTypeCombobox");
			templateTypeComboBox.getComponent().setPreferredSize(
					new Dimension(150, 20));

		}
		return templateTypeComboBox;
	}

	private void initNameField() {
		nameField.getLabel().setPreferredSize(new Dimension(123, 25));
		nameField.getComponent().setFont(new java.awt.Font("dialog", Font.BOLD, 16));
	}

	private JScrollPane getTextAreascrollPane() {
		if (textAreascrollPane == null) {
			textAreascrollPane = new JScrollPane(getTemplateTextArea());
			textAreascrollPane
					.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			textAreascrollPane.setBounds(10, 150, 500, 450);
			textAreascrollPane.setVisible(true);
		}
		return textAreascrollPane;
	}

	private JTextArea getTemplateTextArea() {
		if (templateTextArea == null) {
			templateTextArea = new JTextArea(5, 35);
			templateTextArea.setName("TemplateTextarea");
			templateTextArea.setFont(new java.awt.Font("dialog", Font.BOLD, 16));
			templateTextArea.setEditable(false);
			templateTextArea.setEnabled(true);
			templateTextArea.setVisible(true);
			templateTextArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		}
		return templateTextArea;
	}

	private void loadData() {
		loadForms();
		loadTemplateTypeBox();
	}
	
	protected void loadForms() {
		List<String> forms = ServiceFactory.getDao(PrintFormDao.class).findDistinctForms();
		if (forms == null) {
			return;
		}
		ComboBoxModel<String> model = new ComboBoxModel<String>(forms);
		formComboBox.getComponent().setModel(model);
		formComboBox.getComponent().setSelectedIndex(-1);
	}
	
	private void loadTemplateTypeBox() {
		List<String> list = new ArrayList<String>();
		for (TemplateType tt : TemplateType.values()) {
			list.add(tt.name());
		}
		ComboBoxModel<String> model = new ComboBoxModel<String>(list);
		templateTypeComboBox.getComponent().setModel(model);
		templateTypeComboBox.getComponent().setSelectedIndex(-1);
	}

	private LabeledComboBox getTemplateComboBox() {
		if (templateComboBox == null) {
			templateComboBox = new LabeledComboBox("Templates");
			templateComboBox.getComponent().setName("TemplateCombobox");
			templateComboBox.getComponent().setPreferredSize(
					new Dimension(150, 20));
			templateComboBox.getComponent().setFont(
					new java.awt.Font("dialog", Font.BOLD, 16));
			templateComboBox.getComponent().setEditable(true);
		}
		return templateComboBox;
	}

	private LabeledComboBox getPrinterComboBox() {
		if (printerComboBox == null) {
			printerComboBox = new LabeledComboBox("Printer");
			printerComboBox.getComponent().setName("PrinterCombobox");
			printerComboBox.getComponent().setPreferredSize(
					new Dimension(150, 20));

		}
		return printerComboBox;
	}

	private LabeledComboBox getFormComboBox() {
		if (formComboBox == null) {
			formComboBox = new LabeledComboBox("Forms");
			formComboBox.getComponent().setName("FormCombobox");
		}
		return formComboBox;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			setWaitCursor();
			clearErrorMessage();
			if (e.getSource().equals(getFormComboBox().getComponent())) {
				formChanged();
			} else if (e.getSource().equals(getTemplateComboBox().getComponent())) {
				templateChanged();
			} else if (e.getSource().equals(getTemplateTypeComboBox().getComponent())) {
				templateTypeChanged();
			} else if (e.getSource().equals(getPrinterComboBox().getComponent())) {
				printerChanged();
			} else if (e.getSource().equals(getRefreshButton())) {
				refresh();
			} else if (e.getSource().equals(getPrintButton())) {
				try {
					doPrint();
				} catch (DeviceInUseException e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource().equals(getLoadButton())) {
				loadFile();
			} else if (e.getSource().equals(getSaveButton())) {
				doSave();
			}
		} catch (Exception ex) {
			setErrorMessage("Exception:" + ex.getMessage());
			getLogger().error(ex, "Exception occured : ");
		} finally {
			setDefaultCursor();
		}
	}

	private void doPrint() throws DeviceInUseException {
		String msg = " ";
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.TERMINAL, ApplicationContext.getInstance().getHostName());
	    dc.put(DataContainerTag.FORM_ID, getSelectedFormId());
	    dc.put(DataContainerTag.TEMPLATE_NAME, getSelectedTemplateName());
		
		
		if (printerComboBox.getComponent().getSelectedItem() != null) {
			String queueName = (String) printerComboBox.getComponent()
					.getSelectedItem().toString().trim();
			attributes = attributeTableModel.getItems();
			dc.put(DataContainerTag.QUEUE_NAME, queueName);
			List<String> att = new ArrayList<String>(attributes.size());
			List<String> attValue = new ArrayList<String>(attributes.size());
			List<String> attrList = new ArrayList<String>();
			List<Object> values = new ArrayList<Object>();

			for (KeyValue<String, String> keyValue : attributes) {
				attrList.add(keyValue.getKey().toString());
				values.add(keyValue.getValue().toString());

				dc.put(keyValue.getKey().toString(), keyValue.getValue()
						.toString());
				String attr = ("@" + keyValue.getKey().toString() + "@")
						.toString();
				String attrval = keyValue.getValue().toString();
				if (DataUtil.isInEnum(keyValue.getKey().toString(),
						JasperExternalPrintAttributes.class)) {
					switch (EnumUtil.getType(
							JasperExternalPrintAttributes.class, keyValue
									.getKey())) {
					case VIN:
					case PRODUCT_ID:
						dc.put(DataContainerTag.PRODUCT_ID, keyValue.getValue()
								.toString().trim());
						break;
					case CLIENT_ID:
						dc
								.put(CLIENT_ID, keyValue.getValue().toString()
										.trim());
						break;
					case TRAY_VALUE:
						dc.put(TRAY_VALUE, keyValue.getValue().toString()
								.trim());
						break;
					case JASPER_DUPLEX_FLAG:
						dc.put(JASPER_DUPLEX_FLAG, Boolean
								.parseBoolean(keyValue.getValue().toString()
										.trim()));
						break;
					case PRINT_QUANTITY:
						dc.put(PRINT_QUANTITY, keyValue.getValue().toString()
								.trim());
						break;
					default:
						Logger.getLogger().info(
								keyValue.getKey().toString()
										+ " is not a valid attribute");
						break;
					}
				}
				att.add(attr);
				attValue.add(attrval);
			}
			dc.put(DataContainerTag.TAG_LIST, attrList);
			String printerName = PropertyService.getProperty(ApplicationContext
					.getInstance().getHostName(), dc.get(
					DataContainerTag.QUEUE_NAME).toString());
			if (StringUtils.isEmpty(printerName)) {
				Logger.getLogger().info(
						"Printer name is missing in configuration");
				msg = "Printer name is missing in configuration";
			}
			dc.put(DataContainerTag.PRINTER_NAME, printerName);
			String printQty = DataContainerUtil.getString(dc,
					DataContainerTag.PRINT_QUANTITY, "1");
			String vin = DataContainerUtil.getString(dc,
					DataContainerTag.PRODUCT_ID, "");
			DataContainer dataContainer = new DefaultDataContainer();
			dataContainer = prepareDataFromDataContainer(dc);
			String finalData = getPrintDataFromDevice(queueName, dataContainer);
			Logger.getLogger().info(
					"final Report Data :"
							+ "ProductId:"
							+ DataContainerUtil.getString(dc,
									DataContainerTag.PRODUCT_ID, "")
							+ "PartName:"
							+ DataContainerUtil.getString(dc,
									DataContainerTag.FORM_ID, "")
							+ "TemplateId:"
							+ DataContainerUtil.getString(dc,
									DataContainerTag.TEMPLATE_NAME, "")
							+ "DestinationId:"
							+ DataContainerUtil.getString(dc,
									DataContainerTag.QUEUE_NAME, "")
							+ "TrayValue:"
							+ DataContainerUtil.getString(dc, TRAY_VALUE, "")
							+ "Duplex:"
							+ DataContainerUtil.getString(dc,
									JASPER_DUPLEX_FLAG, "")
							+ "PrinterName:"
							+ DataContainerUtil.getString(dc,
									DataContainerTag.PRINTER_NAME, "")
							+ "PrintData:" + finalData);

			for (Entry<String, String> entry : DataContainerUtil
					.getAttributeMap(dc).entrySet()) {
				Logger.getLogger().info(
						"Key:" + entry.getKey() + "Value:" + entry.getValue());
			}

			sendToPrintDevice(queueName, finalData, Integer.parseInt(printQty),
					vin);
		} else {
			msg = "please select printer";
			updateStatus(msg);
		}
	}

	private DataContainer prepareDataFromDataContainer(DataContainer dc) {
		DataContainer dataContainer = new DefaultDataContainer();
		dataContainer.put(DataContainerTag.FORM_ID, dc
				.get(DataContainerTag.FORM_ID));
		dataContainer.put(DataContainerTag.TEMPLATE_NAME, dc
				.get(DataContainerTag.TEMPLATE_NAME));
		dataContainer.put(DataContainerTag.PRINTER_NAME, dc
				.get(DataContainerTag.PRINTER_NAME));
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(DataContainerUtil.getAttributeMap(dc));
		dataContainer.put(DataContainerTag.KEY_VALUE_PAIR, map);

		return dataContainer;
	}

	private String getPrintDataFromDevice(String deviceName, DataContainer dc) {
		IPrintDevice iPrintDevice = null;

		iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(
				deviceName);
		AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
		return printDevice.getprintData(dc);
	}

	/**
	 * 
	 * @param deviceName
	 * @param dataToPrint
	 */
	private void sendToPrintDevice(String deviceName, String dataToPrint,
			int printQty, String vin) {
		IPrintDevice iPrintDevice = null;
		String msg = "";
		try {
			iPrintDevice = (IPrintDevice) DeviceManager.getInstance()
					.getDevice(deviceName);
			AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
			PrintForm printform = ServiceFactory.getDao(PrintFormDao.class).findByKey(new PrintFormId(getSelectedFormId(), printDevice.getDestinationPrinter()));

			if (printform != null) {
				if (!iPrintDevice.isActive()) {
					iPrintDevice.activate();
					iPrintDevice.registerListener(deviceName, this);
					iPrintDevice.requestControl(deviceName, this);
				}
				if (iPrintDevice.isActive() && iPrintDevice.isConnected()) {

					if (printDevice.print(dataToPrint, printQty, vin)) {
						msg = "Template sent to Printer successfully";

					} else {

						msg = "Unable to sent the template to Printer "
								+ deviceName;
					}
				} else
					msg = "Device not active or not connected: " + deviceName;
			} else {
				msg = deviceName + " Device not configured in PrintForm";

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		updateStatus(msg);
	}

	/**
	 * 
	 * @param message
	 */
	private void updateStatus(String message) {
		getLogger().info(message);
		super.setMessage(message);
	}

	protected boolean isInputValid() {
		String formId = getSelectedFormId();
		String templateName = getSelectedTemplateName();
		String templateType = getSelectedTemplateTypeName();
		StringBuilder sb = new StringBuilder();
		String separator = "\n";
		if (StringUtils.isBlank(formId)) {
			sb.append("Please select Form !");
		}
		if (StringUtils.isBlank(templateName)) {
			StringUtil.appendIfNotEmpty(sb, separator);
			sb.append("Please select/enter Template Name !");
		}
		if (StringUtils.isBlank(templateType)) {
			StringUtil.appendIfNotEmpty(sb, separator);
			sb.append("Please select Template Type !");
		}

		if (sb.length() > 0) {
			JOptionPane.showMessageDialog(this, sb, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		
		byte[] data = getUploadedTemplateData(templateType);
		if (data == null || data.length == 0) {
			sb.append("Template content can not be blank !");
			JOptionPane.showMessageDialog(this, sb, "Invalid Input", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		String[] types = {TemplateType.IMAGE.name(), TemplateType.JASPER.name(), TemplateType.COMPILED_JASPER.name(), TemplateType.MQ_CONFIG.name()};
		List<String> skipTplTypes = Arrays.asList(types);

		if (!skipTplTypes.contains(templateType)) {
			boolean templateAttributesCheckStatus = verifyTemplateData(templateTextArea.getText());
			if (!templateAttributesCheckStatus) {
				StringUtil.appendIfNotEmpty(sb, separator);
				sb.append("Template attributes missing !");
			}
		}

		if (sb.length() == 0) {
			return true;
		}
		JOptionPane.showMessageDialog(this, sb, "Invalid Input", JOptionPane.WARNING_MESSAGE);
		return false;
	}
	
	protected boolean isInputValidForCreate() {
		String formId = getSelectedFormId();
		String templateName = getSelectedTemplateName();
		Template tmp = getDao().findByKey(templateName);
		if (tmp != null) {
			if (!formId.equals(tmp.getFormId())) {
				String msg = "Template " + templateName + " already exists and is associated with different form " + StringUtils.trim(tmp.getFormId()) + " !";
			    JOptionPane.showMessageDialog(this, msg);
				return false;
			} else {
				String msg = "It looks like Template " + templateName + " has been created by different user, please refresh your data to avoid concurrency issues !";
			    JOptionPane.showMessageDialog(this, msg);
				return false;
			}
		}
		return true;
	}
	
	
	protected boolean isInputValidForUpdate(Template template) {
		String templateType = getSelectedTemplateTypeName();
		if (!template.getTemplateTypeString().equals(templateType)) {
			String msg = "You are about to change template type from " +  template.getTemplateTypeString() + " to " + templateType + " ! \nAre you sure ? ";
			int retCode = JOptionPane.showConfirmDialog(this, msg, "", JOptionPane.YES_NO_OPTION);
			if (JOptionPane.YES_OPTION != retCode) {
				return false;
			}
		}
		return true;
	}

	protected Template constructTemplate() {
		
		String formId = getSelectedFormId();
		String templateName = getSelectedTemplateName();
		String templateType = getSelectedTemplateTypeName();
		String templateDescription = nameField.getComponent().getText();
		byte[] data = getUploadedTemplateData(templateType);
		
		Template template = new Template();
		template.setFormId(formId);
		template.setTemplateName(templateName);
		template.setTemplateTypeString(templateType);
		template.setTemplateDescription(templateDescription);
		template.setRevisionId(0);
		template.setTemplateData(data);
		
		return template;
	}
	
	protected Template setTemplateData(Template template) {
		String templateType = getSelectedTemplateTypeName();
		String templateDescription = nameField.getComponent().getText();
		byte[] data = getUploadedTemplateData(templateType);

		template.setTemplateTypeString(templateType);
		template.setTemplateDescription(templateDescription);
		template.setTemplateData(data);
		
		List<String> buildAttributes = ServiceFactory.getDao(BuildAttributeDao.class).findByBuildAttributeValue(template.getTemplateName());
		
		if (buildAttributes != null && buildAttributes.size() > 0) {
			String msg = "Do you want to create a new revision for "+template.getTemplateName()+"?";
			int res = JOptionPane.showConfirmDialog(this, msg, "", JOptionPane.YES_NO_OPTION);
			if(JOptionPane.YES_OPTION == res){
				Template maxTemplate = getDao().maxTemplateName(template);
				template = maxTemplate;
			}
		}
		return template;
	}
	
	private void doSave() {

		if (!isInputValid()) {
			return;
		}

		String templateName = getSelectedTemplateName();
		String msg = null;
		Template template = getTemplateMap().get(templateName);
		if (template == null) {
			if (!isInputValidForCreate()) {
				return;
			}
			template = constructTemplate();
			msg = "Template " + templateName + " created";
		} else {
			template = (Template) template.deepCopy();
			if (!isInputValidForUpdate(template)) {
				return;
			}
			template = setTemplateData(template);
			msg = "Template " + templateName + " updated";		
		}

		Template saved = getDao().save(template);
		logUserAction(SAVED, template);
		getTemplateMap().put(saved.getTemplateName(), saved);
		populateTemplateBox();
		getTemplateComboBox().getComponent().setSelectedItem(saved.getTemplateName());
		getSaveButton().setEnabled(false);
		getLogger().info(msg);
		setMessage(msg);
	}
	
	protected byte[] getUploadedTemplateData(String templateType) {
		byte[] data = null;
		if (TemplateType.IMAGE.name().equals(templateType)) {
			data = getUploadedTemplateData();
		} else if (TemplateType.COMPILED_JASPER.name().equals(templateType)) {
			data = getUploadedTemplateData();
		} else {
			String content = getTemplateTextArea().getText();
			if (StringUtils.isNotBlank(content)) {
				data = content.getBytes();
			}
		}
		return data;
	}
	
	private boolean verifyTemplateData(String templateData) {
		if (StringUtils.isEmpty(templateData)) {
			getLogger().info("Template Data is missed");
			return false;
		}
		if (parseTemplateData(templateData) != null) {
			String currentTemplate = getSelectedTemplateName();
			String formId = getSelectedFormId();
			String templateType = null;
			String[] temparray = (String[]) ArrayUtils.addAll(
					getAttributes(formId), getAttributes(currentTemplate));
			Template template = ServiceFactory.getDao(TemplateDao.class)
					.findByKey(currentTemplate);
			if (template != null && template.getTemplateTypeString() != null)
				templateType = template.getTemplateTypeString();
			if (getTemplateTypeComboBox().getComponent().getSelectedItem() != null)
				templateType = getTemplateTypeComboBox().getComponent()
						.getSelectedItem().toString();
			if (templateType != null
					&& !templateType.equalsIgnoreCase(TemplateType.JASPER.toString()))
				return Arrays.asList(temparray).containsAll(
						Arrays.asList(parseTemplateData(templateData)));
		}
		return true;
	}
	
	protected void loadTemplates(String formId) {
		getTemplateMap().clear();
		List<Template> list = ServiceFactory.getDao(TemplateDao.class).findPrinters(formId);
		if (list == null || list.isEmpty()) {
			return;
		}
		for (Template t : list) {
			getTemplateMap().put(t.getTemplateName(), t);
		}
	}
	
	protected List<String> getTemplateNames() {
		return new ArrayList<String>(getTemplateMap().keySet());
	}
	
	private void printerChanged() {
		if (getPrinterComboBox().getComponent().getSelectedItem() != null) {
			getLogger().info(
					getPrinterComboBox().getComponent().getSelectedItem()
							+ " is selected");
			printButton.setEnabled(true);

		}
	}

	private void refresh() {
		getLogger().info("Refresh Button is clicked");
		attributes.clear();
		attributeTableModel.refresh(attributes);
		String data = getTemplateTextArea().getText();
		if (StringUtils.isNotBlank(data)) {
			parseData(data);
		} 
		refreshButton.setEnabled(false);	
	}

	private void cleanUp(List<String> printerList) {
		printButton.setEnabled(false);
		getTemplateMap().clear();
		getTemplateComboBox().getComponent().setSelectedIndex(-1);
		model = new ComboBoxModel<String>(printerList);
		printerComboBox.getComponent().setModel(model);
	}

	private void populateTemplateBox() {
		DefaultComboBoxModel listmodel = new DefaultComboBoxModel(getTemplateNames().toArray());
		getTemplateComboBox().getComponent().setModel(listmodel);
		getTemplateComboBox().getComponent().setSelectedIndex(-1);
	}

	private void populatePrintersBox(List<String> printerList) {
		Hashtable<String, IDevice> devices = DeviceManager.getInstance()
				.getDevices();
		Iterator<Entry<String, IDevice>> it = devices.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, IDevice> pairs = it.next();
			String availPrinters = pairs.getKey().toString();
			printerList.add(availPrinters);
		}
		model = new ComboBoxModel<String>(printerList);
		printerComboBox.getComponent().setModel(model);
		printerComboBox.getComponent().setSelectedIndex(-1);
		printerComboBox.getComponent().setRenderer(model);

	}

	private void formChanged() {
		List<String> printerList = new ArrayList<String>();
		cleanUp(printerList);
		String formId = getSelectedFormId();
		getLogger().info(formId + " is selected");
		loadTemplates(formId);
		populateTemplateBox();
		populatePrintersBox(printerList);
	}

	protected void resetTemplate() {
		saveButton.setEnabled(false);	
		refreshButton.setEnabled(false);
		getTemplateTypeComboBox().getComponent().setSelectedIndex(-1);
		nameField.getComponent().setText("");
	}
	
	protected void setContentComponent(String templateTypeName) {
		if (StringUtils.isBlank(templateTypeName)) {
			return;
		}
		templateTextArea.setForeground(Color.BLACK);
		if (TemplateType.IMAGE.name().equals(templateTypeName)) {
			imagePanel.setLeftComponent(getImageBean());
			templateTextArea.setEnabled(false);
			templateTextArea.setEditable(false);
		} else {
			imagePanel.setLeftComponent(getTextAreascrollPane());
			if (TemplateType.COMPILED_JASPER.name().equals(templateTypeName)) {
				templateTextArea.setEditable(false);
				templateTextArea.setForeground(new Color(100, 100, 100));
			} else {
				templateTextArea.setEnabled(true);
			}
		}
		getLoadButton().setEnabled(true);
	}
	
	protected void resetContentComponent() {
		int divLoc = imagePanel.getDividerLocation();
		getImageBean().clearImage();
		getTemplateTextArea().setText("");
		setUploadedTemplateData(null);
		imagePanel.setLeftComponent(getTextAreascrollPane());
		templateTextArea.setEnabled(true);	
		templateTextArea.setEditable(true);
		
		attributes.clear();
		attributeTableModel.refresh(attributes);
		
		getLoadButton().setEnabled(false);
		getRefreshButton().setEnabled(false);
		getSaveButton().setEnabled(false);
		imagePanel.setDividerLocation(divLoc);
	}
	
	private void templateChanged() {
		resetTemplate();
		
		String templateName= getSelectedTemplateName();
		if (StringUtils.isBlank(templateName)) {
			return;
		}

		getLogger().info(templateName + " is selected");

		if (getTemplateMap().get(templateName) == null) {
			return;	
		}
		
		Template template = (Template) getTemplateMap().get(templateName).deepCopy();
		String templateType = template.getTemplateTypeString();	
		getTemplateTypeComboBox().getComponent().setSelectedItem(templateType);
		nameField.getComponent().setText(template.getTemplateDescription());

		setUploadedTemplateData(template.getTemplateDataBytes());

			if (TemplateType.IMAGE.name().equals(templateType)) {
				if (template.getTemplateDataBytes() != null) {
					getImageBean().loadImage(template.getTemplateDataBytes());
				} 
				
			} else if (TemplateType.COMPILED_JASPER.name().equals(templateType)) {
				if (template.getTemplateDataBytes() != null) {
					
					try {
						byte[] jrxmlData = JasperPrintUtil.decompileJasper(template.getTemplateDataBytes());
						setText(new String(jrxmlData));
					} catch (Exception e) {
						String msg =  "Failed to decompile template content for template " + templateName + " !";
                        getLogger().warn(e, msg);						
						setErrorMessage(msg + "\n" + e);
                        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
					}
				} 
			} else {
				if (template.getTemplateDataBytes() != null) {
					setText(template.getTemplateDataString());
				}
			}
	}
	
	
	public void setText(String text) {
		templateTextArea.setText(text);
		parseData(text);
	}

	private List<PrintAttributeFormat> getPrintAttributeFormats(String form_id) {
		return ServiceFactory.getDao(PrintAttributeFormatDao.class)
				.findAllByFormId(form_id);
	}

	private String[] getAttributes(String formId) {
		String[] printattributearray = null;
		List<PrintAttributeFormat> printAttributeFormats = getPrintAttributeFormats(formId);
		if (printAttributeFormats != null) {
			List<String> attribut = new ArrayList<String>(printAttributeFormats
					.size());
			for (PrintAttributeFormat paf : printAttributeFormats) {
				attribut.add(paf.getAttribute());
			}
			printattributearray = (String[]) attribut
					.toArray(new String[attribut.size()]);

		}
		return printattributearray;
	}

	private Hashtable<String, Integer> getAttributesWithLength() {
		String formId = getSelectedFormId();
		String currentTemplate = getSelectedTemplateName();
		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		List<PrintAttributeFormat> formPrintAttributeFormat = getPrintAttributeFormats(formId);
		List<PrintAttributeFormat> templatePrintAttributeFormat = getPrintAttributeFormats(currentTemplate);
		Set<PrintAttributeFormat> printSet = new HashSet<PrintAttributeFormat>();
		formPrintAttributeFormat.addAll(templatePrintAttributeFormat);
		printSet.addAll(formPrintAttributeFormat);

		for (PrintAttributeFormat paf : printSet) {
			ht.put(paf.getAttribute(), paf.getLength());
		}

		return ht;
	}

	private void matchAttributes(String[] tds) {

		Hashtable<String, Integer> ht = new Hashtable<String, Integer>();
		ht = getAttributesWithLength();
		attributeTableModel.getTemplateData(tds, ht);
		attributes.clear();
		getAllAttributearray(tds);
		if (attribarray != null) {
			for (int i = 0; i < attribarray.length; i++) {
				attributes
						.add(new KeyValue<String, String>(attribarray[i], ""));
			}
		}
	}

	private String[] getAllAttributearray(String[] ts) {
		String formId = getSelectedFormId();
		String currentTemplate = getSelectedTemplateName();
		String[] formAttribarray = getAttributes(formId);
		String[] templateAttribarray = getAttributes(currentTemplate);
		String[] concarray = (String[]) ArrayUtils.addAll(formAttribarray,
				templateAttribarray);
		concarray = (String[]) ArrayUtils.addAll(concarray, ts);
		Set<String> uniqueStr = new HashSet<String>(Arrays.asList(concarray));
		String[] buf = new String[0];
		attribarray = uniqueStr.toArray(buf);
		return attribarray;

	}

	private String[] parseTemplateData(String data) {
		String[] tds = null;
		try {
			if (data != null && data.contains("$P")) {
				tds = DataContainerXMLUtil.parseJasperXMLString(data);
			} else if (data != null && data.contains("@")) {
				tds = StringUtils.substringsBetween(data, "@", "@");

			}
			if (tds != null) {
				Set set = new HashSet(Arrays.asList(tds));
				tds = (String[]) (set.toArray(new String[set.size()]));
			} else
				getLogger().info("no parameters found in template data");
		} catch (Exception exception) {
			getLogger().info("exception occured in parsing template data");
			exception.printStackTrace();
		}

		return tds;
	}

	private void parseData(String data) {
		String[] tds = null;
		tds = parseTemplateData(data);
		matchAttributes(tds);
		attributeTableModel.refresh(attributes);
		refreshButton.setEnabled(false);
	}

	public String getId() {
		return "GenericTemplatepanel";
	}

	public void handleStatusChange(PrintDeviceStatusInfo statusInfo) {
		updateStatus(statusInfo.getDisplayMessage());
	}

	public String getApplicationName() {
		return "GenericTemplatepanel";
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return _deviceAccessKey;
	}

	public void controlGranted(String deviceId) {
	}

	public void controlRevoked(String deviceId) {
	}

	private ImageBean createImageBean() {
		ImageBean image = new ImageBean();
		image.setBorder(new EtchedBorder());
		image.setSize(500, 400);
		image.setLocation(10, 120);
		image.setVisible(true);
		return image;
	}

	private JSplitPane createAttributesPanel() {
		JSplitPane panel  = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, getTextAreascrollPane(),  createAttributeTablePane());
		panel.setFont(Fonts.DIALOG_BOLD_14);
		panel.setDividerLocation(0.5);
		imagePanel = panel;
		return panel;
	}

	private JButton getLoadButton() {
		if (loadButton == null) {
			loadButton = new JButton();
			loadButton.setName("LoadButton");
			loadButton.setFont(Fonts.DIALOG_PLAIN_18);
			loadButton.setText("File");
			loadButton.setEnabled(false);
		}
		return loadButton;
	}

	protected void loadFile() {
		String templateName = getSelectedTemplateName();
		String templateTypeName = getSelectedTemplateTypeName();
		if (StringUtils.isBlank(templateName)) {
			JOptionPane.showMessageDialog(this, "Please select or input Template Name");
			return;
		}
		if (StringUtils.isBlank(templateTypeName)) {
			JOptionPane.showMessageDialog(this, "Please select Template Type");
		}
		if (TemplateType.IMAGE.name().equals(templateTypeName)) {
			loadImageFile();
		} else if (TemplateType.COMPILED_JASPER.name().equals(templateTypeName)) {
			loadJasperFile();
		} else {
			loadTextFile();
		}
	}

	protected void loadImageFile() {
		byte[] imageData = getFileContent(null, false, new JpgFilter());
		if (imageData == null) {
			return;
		}
		getImageBean().loadImage(imageData);
		getSaveButton().setEnabled(true);
		setUploadedTemplateData(imageData);
	}
	
	protected void loadJasperFile() {

		StringBuilder sb = new StringBuilder();
		byte[] data = getFileContent(sb, false, new ExtensionFileFilter(JASPER_EXT), new ExtensionFileFilter(JRXML_EXT));
		if (data == null) {
			return;
		}
		String fileName = sb.toString();
		attributes.clear();
		attributeTableModel.refresh(attributes);
		byte[] jrxmlData = null;
		byte[] jasperData = null;

		boolean validData = false;
		if (StringUtils.endsWithIgnoreCase(fileName, "." + JRXML_EXT)) {
			jrxmlData = data;
			try {
				jasperData = JasperPrintUtil.compileJrxml(jrxmlData);
				validData = true;
			} catch (JRException e) {
				String msg = "Failed to compile file: " + fileName + " !";
				getLogger().error(e, msg);
				setErrorMessage(msg + "\n" + ExceptionUtils.getStackTrace(e));
				JOptionPane.showMessageDialog(this, "Failed to compile file: " + fileName + " !", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			jasperData = data;
			try {
				jrxmlData = JasperPrintUtil.decompileJasper(jasperData);
				validData = true;
			} catch (JRException e) {
				String msg = "Failed to decompile file: " + fileName + " !";
				getLogger().error(e, msg);
				setErrorMessage(msg + "\n" + ExceptionUtils.getStackTrace(e));
				JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (validData) {
			String str = new String(jrxmlData);
			getTemplateTextArea().setText(str);
			parseData(str);
			setUploadedTemplateData(jasperData);
			getSaveButton().setEnabled(true);
		} else {
			getTemplateTextArea().setText("");
			setUploadedTemplateData(null);
			getSaveButton().setEnabled(false);
		}
	}
	
	protected void loadTextFile() {

		byte[] data = getFileContent(null, true);
		if (data == null) {
			return;
		}
		attributes.clear();
		attributeTableModel.refresh(attributes);
		String str = new String(data);
		getTemplateTextArea().setText(str);
		parseData(str);
		setUploadedTemplateData(data);
		getSaveButton().setEnabled(true);
	}
	
	protected byte[] getFileContent(StringBuilder fileNameOut, boolean acceptAllFileFilter, FileFilter... filters) {

		JFileChooser fc = new JFileChooser(StringUtils.trimToEmpty(getLastSelectedFileDirectory()));
		fc.setAcceptAllFileFilterUsed(acceptAllFileFilter);
		if (filters != null && filters.length > 0) {
			for (FileFilter ff : filters) {
				fc.addChoosableFileFilter(ff);
			}
		}

		int returnVal = fc.showDialog(getMainWindow(), "Select");
		setLastSelectedFileDirectory(fc.getCurrentDirectory().getPath());
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			return null;
		}
		File file = fc.getSelectedFile();
		String fileName = file.getName();
		if (fileNameOut != null) {
			fileNameOut.append(fileName);
		}
		byte[] data = null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
			long length = file.length();
			data = new byte[(int) length];
			in.read(data);
			in.close();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Failed to load file: " + fileName, "Error", JOptionPane.ERROR_MESSAGE);
		} 
		setMessage("Loaded file : " + fileName);
		return data;
	}

	private ImageBean getImageBean() {
		return image;
	}

	private Template scale(Template imageToScale) {
		try {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(
					imageToScale.getTemplateDataBytes());
			ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();

			BufferedImage inputBufferedImage = ImageIO.read(byteInput);
			Integer defaultWidth = getMainWindow().getApplicationProperty(
					"DEFAULT_IMAGE_WIDTH") != null ? Integer
					.parseInt(getMainWindow().getApplicationProperty(
							"DEFAULT_IMAGE_WIDTH")) : 500;
			Integer defaultHeight = getMainWindow().getApplicationProperty(
					"DEFAULT_IMAGE_HEIGHT") != null ? Integer
					.parseInt(getMainWindow().getApplicationProperty(
							"DEFAULT_IMAGE_HEIGHT")) : 500;

			java.awt.Image scaledImage = inputBufferedImage.getScaledInstance(
					defaultWidth, defaultHeight, java.awt.Image.SCALE_SMOOTH);
			BufferedImage outputBufferedImage = new BufferedImage(defaultWidth,
					defaultHeight, BufferedImage.TYPE_INT_RGB);
			outputBufferedImage.getGraphics().drawImage(scaledImage, 0, 0,
					new Color(0, 0, 0), null);

			ImageIO.write(outputBufferedImage, "jpg", byteOutput);
			imageToScale.setTemplateData(byteOutput.toByteArray());

		} catch (Exception e) {
			getLogger().error("An error Occurred scaling the image");
			e.printStackTrace();
		}
		return imageToScale;

	}

	protected void templateTypeChanged() {
		resetContentComponent();
		String selectedTemplateType = getSelectedTemplateTypeName();
		setContentComponent(selectedTemplateType);
		if (getTemplateTextArea().isEditable()) {
			getTemplateTextArea().requestFocus();
		} else {
			getLoadButton().requestFocus();
		}
	}

	protected Map<String, Template> getTemplateMap() {
		return templateMap;
	}
	
	protected String getSelectedString(LabeledComboBox component) {
		if (component == null) {
			return "";
		}
		Object obj = component.getComponent().getSelectedItem();
		if (obj == null) {
			return "";
		}
		String item = obj.toString().trim();
		return item;
	}
	
	protected void setWaitCursor() {
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		getActionCounter().incrementAndGet();
	}

	protected void setDefaultCursor() {
		getActionCounter().decrementAndGet();
		if (getActionCounter().get() < 1) {
			getMainWindow().setCursor(Cursor.getDefaultCursor());
		}
	}
	
	protected String getSelectedFormId() {
		return getSelectedString(getFormComboBox());
	}
	
	protected String getSelectedTemplateName() {
		return getSelectedString(getTemplateComboBox());
	}
	
	protected String getSelectedTemplateTypeName() {
		return getSelectedString(getTemplateTypeComboBox());
	}
	
	protected boolean isTemplateExists(String templateName) {
		return getTemplateMap().containsKey(templateName);
	}

	protected String getLastSelectedFileDirectory() {
		return lastSelectedFileDirectory;
	}

	protected void setLastSelectedFileDirectory(String lastSelectedFileDirectory) {
		this.lastSelectedFileDirectory = lastSelectedFileDirectory;
	}

	protected TemplateDao getDao() {
		return ServiceFactory.getDao(TemplateDao.class);
	}

	protected byte[] getUploadedTemplateData() {
		return uploadedTemplateData;
	}

	protected void setUploadedTemplateData(byte[] uploadedTemplateData) {
		this.uploadedTemplateData = uploadedTemplateData;
	}

	protected AtomicInteger getActionCounter() {
		return actionCounter;
	}

	protected void setActionCounter(AtomicInteger actionCounter) {
		this.actionCounter = actionCounter;
	}
}
