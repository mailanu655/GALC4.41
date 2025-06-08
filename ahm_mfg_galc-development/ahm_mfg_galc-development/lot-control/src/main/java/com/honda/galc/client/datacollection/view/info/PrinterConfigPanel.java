package com.honda.galc.client.datacollection.view.info;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.device.property.PrinterDevicePropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledNumberSpinner;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.IDevice;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.product.Template;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.device.printer.AbstractPrintDevice;


public class PrinterConfigPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private LabeledComboBox printerComboBox;
	private LabeledNumberSpinner numberOfCopiesSpinner;
	private ClientContext context;
	private JButton formFeedButton = new JButton("FormFeed");
	String terminal = ApplicationContext.getInstance().getHostName();
	private IDevice device;
	String propertyKey = " ";
	public PrinterConfigPanel(){
		super();
		setLayout(new FlowLayout());
		
	}
	
	public PrinterConfigPanel(IDevice device, ClientContext context){
		super();
		this.context = context;
		this.device = device;
		setLayout(new FlowLayout());
		initialize(this.device);
	}

	private void initialize(IDevice device) {
		createComponents();
		loadInitialData(device);
		initComponents();
	}
	
	private void initComponents(){
		formFeedButton.addActionListener(this);
		getPrinterComboBox().getComponent().addActionListener(this);
	}
	
	private void createComponents() {
		JPanel comboPanel = new JPanel();
	//	comboPanel.setFont(Fonts.DIALOG_BOLD_14);
		comboPanel.setLayout(new FlowLayout());
		comboPanel.add(getPrinterComboBox());
		comboPanel.add(createNumberOfCopiesSpinner());
		comboPanel.add(createButtonPanel());
		add(comboPanel);
		
		
	}
	
	private LabeledNumberSpinner createNumberOfCopiesSpinner(){
		
		numberOfCopiesSpinner = new LabeledNumberSpinner("Number of copies", true,1,10);
		numberOfCopiesSpinner.setFont(Fonts.DIALOG_BOLD_14);
		numberOfCopiesSpinner.setEnabled(false);
		numberOfCopiesSpinner.setVisible(false);
		return numberOfCopiesSpinner;
		
	}
	
	private JPanel createButtonPanel() {
		
		JPanel buttonPanel = new JPanel();
		formFeedButton.setFont(Fonts.DIALOG_BOLD_14);
		formFeedButton.setEnabled(false);
		formFeedButton.setVisible(false);
		buttonPanel.add(formFeedButton);
		
		return buttonPanel;
		
	}

	private void loadInitialData(IDevice device) {
		List<String> printersList = new ArrayList<String>();
		AbstractPrintDevice printDevice = (AbstractPrintDevice) device;

		if (printDevice.getPrintersList().contains(",")){
			printersList = Arrays.asList(printDevice.getPrintersList().split(","));
		} else {
			printersList.add(printDevice.getPrintersList());
		}

		getPrinterComboBox().setModel(new ComboBoxModel<String>(printersList), 0);
		if (printersList.size() > 0) {
			formFeedButton.setEnabled(true);
			formFeedButton.setVisible(true);
			numberOfCopiesSpinner.setEnabled(true);
			numberOfCopiesSpinner.setVisible(true);
		}
	}
		
	
	private LabeledComboBox getPrinterComboBox() {
		if (printerComboBox == null) {
			printerComboBox = new LabeledComboBox("Printers");
			printerComboBox.setFont(Fonts.DIALOG_BOLD_14);
			printerComboBox.getComponent().setPreferredSize(new Dimension(150,25));
			
			}
		return printerComboBox;
	}
	
	private ComponentProperty createProperty(String key, String value) {
		ComponentProperty property = new ComponentProperty(ApplicationContext.getInstance().getHostName(), key, value);
		property.setDescription("updated by client");
		property.setChangeUserId(context.getProcessPointId());
		property.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		return property;
	}
	
	public void saveDeviceConfig()
	{
				
	}
	
	 private List<ComponentProperty> getPrinterProperties(PrinterDevicePropertyBean bean) {
			List<ComponentProperty> properties = new ArrayList<ComponentProperty>();
			String printProperty = (String)getPrinterComboBox().getComponent().getSelectedItem();
			if(!printProperty.equals(bean.getDestinationPrinter())){
			
			properties.add(createProperty(propertyKey, printProperty));
			}
			return properties;
	 }

	 private void printerChanged(){
		 
		 String selectedItem = getPrinterComboBox().getComponent().getSelectedItem().toString().trim();
		 
		 if (selectedItem != null) {
			try {
				AbstractPrintDevice printDevice = (AbstractPrintDevice)device;
				String destPrinter = printDevice.getDestinationPrinter();
				
				List<ComponentProperty> componentPropertyList = PropertyService.getInstance().getComponentProperty(terminal);
				for(ComponentProperty comp: componentPropertyList){
					if(comp.getPropertyValue().equals(destPrinter) && comp.getPropertyKey().contains("destinationPrinter")){
						propertyKey = comp.getPropertyKey().toString();
						break;
					}
						
				}
				
				PrinterDevicePropertyBean bean = getDevicePropertyBean(terminal, propertyKey);
				List<ComponentProperty> properties = getPrinterProperties(bean);
				if (properties.size() > 0) {
						if (!MessageDialog.confirm(this, "Do you want to make " + selectedItem + " the primary printer?")) {
							properties.clear();
							return;
						}
						PropertyService.updateProperty(terminal, propertyKey, selectedItem);
						
						context.getDbManager().saveProperties(properties);
						
						printDevice.setDestinationPrinter(selectedItem);
						
						PropertyService.refreshComponentProperties(terminal);
					}
			
			} catch (Exception e) {
				MessageDialog.showInfo(this, e.getMessage());
			}
		}
	 }
	 
	 public PrinterDevicePropertyBean getDevicePropertyBean(String terminalName,
			String propertyKey) {
		String deviceString = propertyKey;
		String deviceType = getDeviceType(deviceString);
		String suffix = getDeviceSuffix(deviceString);
		Class clazz = getPropertyBeanInterface(deviceType);
		PrinterDevicePropertyBean devicePropertyBean = (PrinterDevicePropertyBean) PropertyService
				.getPropertyBean(clazz, terminalName, suffix);
		return devicePropertyBean;
	}
	 
	 private String getDeviceType(String deviceString) {
		return deviceString.split("\\.")[1];
	}

	private String getDeviceSuffix(String deviceString) {
		String suffix = "";
		if (deviceString.contains("destinationPrinter")) {
			int index = deviceString.lastIndexOf("destinationPrinter");
			if (index < 0)
				return "";
			suffix = deviceString.substring(index + "destinationPrinter".length());

		}
		return suffix;
	}

	private Class getPropertyBeanInterface(String deviceType) {
		String interfaceName = "com.honda.galc.client.device.property."
				+ deviceType.substring(0, 1).toUpperCase()
				+ deviceType.substring(1) + "DevicePropertyBean";
		try {
			return Class.forName(interfaceName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(getPrinterComboBox().getComponent())) {
			printerChanged();
		} else if (e.getSource().equals(formFeedButton)) {
			int copies = numberOfCopiesSpinner.getValue();
			String deviceName = (String) getPrinterComboBox().getComponent()
					.getSelectedItem();
			AbstractPrintDevice printDevice = (AbstractPrintDevice)device;
			List<PrintForm> printform = ServiceFactory.getDao(PrintFormDao.class).findByDesitnationId(deviceName);
			
			if(printform!= null){
			Template templates = ServiceFactory.getDao(TemplateDao.class)
					.findByKey(printDevice.getTemplateName());
			if (templates != null) {
				if (templates.getTemplateDataBytes() != null) {
					String dataToPrint = templates.getTemplateDataString();
					Device dev = ServiceFactory.getDao(DeviceDao.class)
							.findByKey(deviceName);
					if (dev != null) {
						
							if (printDevice.print(dataToPrint, copies, "")) {
								MessageDialog.showInfo(this,"Labels prined successfully", "Information");
							} else
								MessageDialog.showInfo(this,
										"Labels not printed ", "Information");
						

					} else {
						MessageDialog.showInfo(this, "Device not found",
								"Information");
					}
				} else
					MessageDialog.showInfo(this, "Template Data not found",
							"Information");
			} else
				MessageDialog.showInfo(this, "Template not found",
						"Information");
			}
			else
				MessageDialog.showInfo(this, "Printer not found in 294 table");
		}
	}
}