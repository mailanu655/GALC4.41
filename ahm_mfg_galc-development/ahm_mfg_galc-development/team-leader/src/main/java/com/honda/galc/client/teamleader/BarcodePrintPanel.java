package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
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
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.RuleDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.entity.product.Rule;
import com.honda.galc.entity.product.Template;
import com.honda.galc.property.BarcodePrintPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.CompiledJasperPrintAttributeConvertor;
import com.honda.galc.service.printing.IPrintAttributeConvertor;
import com.honda.galc.service.printing.JasperPrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4> View class for requesting 2D barcode prints for Knuckle
 * Torque feature
 * 
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Kamlesh Maharjan</TD>
 * <TD>09/14/2023</TD>
 * <TD>1.0</TD>
 * <TD>(none)</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * 
 * @ver 0.1
 * @author Kamlesh Maharjan
 */
public class BarcodePrintPanel extends TabbedPanel implements ActionListener, IPrintDeviceListener {

	private static final long serialVersionUID = 1L;
	private JLabel _locationLabel = null;
	private JLabel _yearLabel = null;
	private JLabel _monthLabel = null;
	private JLabel _modelLabel = null;
	private JLabel _sequenceLabel = null;
	private JLabel _numBarcodesLabel = null;
	private JLabel _printerLabel = null;
	private JComboBox _locationCombo = null;
	private JComboBox _yearCombo = null;
	private JComboBox _monthCombo = null;
	private JComboBox _modelCombo = null;
	private JComboBox _printerCombo = null;
	private UpperCaseFieldBean _sequenceField = null;
	private UpperCaseFieldBean _numBarcodesField = null;

	private JButton _printButton = null;

	private String lastParamString = "";
	private int _maxBarcodes = 500;

	private JPanel valueSelectionPanel;
	private static final Insets INSETS = new Insets(8, 8, 8, 8);
	private static final Font FONT = Fonts.DIALOG_PLAIN_20;

	private DecimalFormat _sequenceFormat = new DecimalFormat("00000");
	private SimpleDateFormat _yearFormat = new SimpleDateFormat("yy");
	private SimpleDateFormat _monthFormat = new SimpleDateFormat("MM");
	private DataContainer dc;
	private static final String TRAY_VALUE = "TRAY_VALUE";
	private static final String JASPER_DUPLEX_FLAG = "JASPER_DUPLEX_FLAG";
	private String formId, status = "FAIL", msg;

	private static final int TEXT_FIELD_SIZE = 20;
	private static final Dimension COMBO_BOX_DIMENSION;
	static {
		JTextField jTextField = new JTextField(TEXT_FIELD_SIZE);
		jTextField.setFont(FONT);
		JComboBox jComboBox = new JComboBox();
		jComboBox.setFont(FONT);
		COMBO_BOX_DIMENSION = new Dimension((int) jTextField.getPreferredSize().getWidth(),
				(int) jComboBox.getPreferredSize().getHeight());
	}

	// Inner class which creates dialog allowing user to "interrupt" or
	// cancel a print job which is in progress. This is needed in case
	// a very large job is requested with a problem.
	// This class runs as a separate thread to allow sharing of GUI
	// resources with Swing event handlers.
	private class Waiter extends Thread {
		private BarcodePrintPanel _view;
		private boolean _cancelled = false;
		private boolean _complete = false;
		private BarcodePrintDialog _msgDialog = null;

		public Waiter(BarcodePrintPanel view) {
			_view = view;
			_msgDialog = new BarcodePrintDialog(_view.getMainWindow(), "In progress",
					"Printing in progress.  Press OK button to cancel job...");
		}

		public void run() {
			_msgDialog.setVisible(true);
			_msgDialog.dispose();
			if (!_complete) {
				_cancelled = true;
				_msgDialog = new BarcodePrintDialog(_view.getMainWindow(), "Cancelling", "Cancelling job...");
				_msgDialog.setVisible(true);
			}
		}

		public void printingComplete() {
			_complete = true;
			_msgDialog.dispose();
			_msgDialog = new BarcodePrintDialog(_view.getMainWindow(), "Complete", "Printing complete.");
			_msgDialog.setVisible(true);
		}

		public void cancelComplete() {
			_complete = true;
			_msgDialog.dispose();
		}

		public boolean isCancelled() {
			return _cancelled;
		}
	};

	// Inner class to handle sending of actual print requests. Requests are
	// broken into pieces and after each piece has completed, the class checks
	// if the user has cancelled the job (see Waiter class). If job completes
	// without cancellation, this class "stops" the Waiter class.
	private class Sender extends Thread {
		private BarcodePrintPanel _view;
		private DataContainer _dc;
		private Waiter _waitThread;
		private final int LABELS_PER_CYCLE = 10;

		public Sender(BarcodePrintPanel view, DataContainer dc, Waiter waitThread) {
			_view = view;
			_dc = dc;
			_waitThread = waitThread;
		}

		public void run() {
			try {
				int numBarCodes = Integer.parseInt((String) _dc.get("NumBarcodes"));		
				String loc = (String) _dc.get("LOC");
				String year = (String) _dc.get("YR");
				String month = (String) _dc.get("MONTH");
				String model = (String) _dc.get("MODEL");
				_dc.put("NextSeqNumber", _view.getSequenceField().getText());

				while (numBarCodes > 0) {
					// Send partial (or last) label request
					int numToSend = Math.min(numBarCodes, LABELS_PER_CYCLE);
					_dc.put("NumBarcodes", Integer.toString(numToSend));				

					// We've been getting some duplicate barcodes, which needs to be avoided
					// In case there is a problem with the "setting" of the NextSeqNumber, we
					// double check here to avoid any duplication
					String oldStart = _view.getSequenceField().getText();
					int oldStartInt = Integer.parseInt(oldStart);
					
					for (int i = 0; i < numToSend; i++) {
						String barcodeSN = loc + year + month + model + _sequenceFormat.format(oldStartInt + i);
						_dc.put("BSN", barcodeSN);
						_dc.put("NextSeqNumber", _sequenceFormat.format(oldStartInt + i));
						// The Zebra Printer is sort of tricky...you can't send the requests
						// too close together or one or more may be lost.
						// Insert a delay here to help the problem.
						try { 
							Thread.sleep(getProperty().getPrintDelayBy());
						}catch (InterruptedException e1) {}
						
						printLabel(_dc);
					}
					
					setNextSequenceNumber(loc, year, month, oldStartInt + numToSend);
					_view.getSequenceField().setText(_sequenceFormat.format(oldStartInt + numToSend));
					
					// Now check to see if we need to "cancel" the pending job
					if (_waitThread.isCancelled()) {
						// Job cancelled.
						_waitThread.cancelComplete();
						return;
					}
					numBarCodes -= numToSend;
				}
				_waitThread.printingComplete();
				
			} catch (Exception e) {
				_view.handleException(e);
				_waitThread.cancelComplete();
			}
		}
	}

	public BarcodePrintPanel(TabbedMainWindow mainWindow) {
		super("BarcodePrint", KeyEvent.VK_B, mainWindow);
		initComponents();
		addListeners();
	}
	
	// ----------------------------
	// Method to initialize class
	// ----------------------------
	protected void initComponents() {
		setLayout(new BorderLayout());
		BarcodePrintPropertyBean barcodeProperty = getProperty();

		try {
			String[] locations = barcodeProperty.getPartLocation();
			List<String> locVec2 = Arrays.asList(locations);
			addItemsToList(locVec2, getLocationCombo());

			// -----------
			// Years
			// -----------
			Vector<String> yearVec = new Vector<String>();
			String year = _yearFormat.format(new Date());
			int lastYear = Integer.parseInt(year) - 1;
			yearVec.add((lastYear < 10 ? "0" : "") + lastYear);
			yearVec.add("[DEFAULT]" + (lastYear + 1 < 10 ? "0" : "") + (lastYear + 1));
			yearVec.add("" + (lastYear + 2 < 10 ? "0" : "") + (lastYear + 2));
			List<String> list = Collections.list(yearVec.elements());
			addItemsToList(list, getYearCombo());

			// -----------
			// Months
			// -----------
			Vector<String> monthVec = new Vector<String>();
			String month = _monthFormat.format(new Date());
			String[] months = { "01  [Jan]", "02  [Feb]", "03  [Mar]", "04  [Apr]", "05  [May]", "06  [Jun]",
					"07  [Jul]", "08  [Aug]", "09  [Sep]", "10  [Oct]", "11  [Nov]", "12  [Dec]" };
			for (int i = 0; i < 12; i++) {
				String s = months[i];
				if (s.startsWith(month)) {
					monthVec.add("[DEFAULT]" + s);
				} else {
					monthVec.add(s);
				}
			}
			list = Collections.list(monthVec.elements());
			addItemsToList(list, getMonthCombo());

			String[] modelList = barcodeProperty.getModelList();
			List<String> models = Arrays.asList(modelList);
			addItemsToList(models, getModelCombo());

			// String s = "00001";
			// ------------------------
			// Get nextSequenceNumber
			// ------------------------
			String nextSequenceNumber = getNextSequenceNumber("FR", year, month,
					ApplicationContext.getInstance().getApplicationId());
			getSequenceField().setText(nextSequenceNumber);

			String s = String.valueOf(barcodeProperty.getDefaultNumberOfLabels());
			getNumBarcodesField().setText(s);

			_maxBarcodes = barcodeProperty.getMaxNumberOfLabels();

			// ---------------------------------
			// Get Printer List from 294 table
			// ---------------------------------
			String formId = barcodeProperty.getBarcodeForm();
			List<PrintForm> printform = ServiceFactory.getDao(PrintFormDao.class).findByFormId(formId);
			List<String> printerList = new ArrayList<>();

			// vec.add("Knuckle_Barcode_Label");
			// SEL3479B = select t1.destination_id, t2.alias_name from gal294tbx t1,
			// gal253tbx t2 where t1.form_id = '{0}' and t1.destination_id = t2.client_id
			if (printform != null) {
				for (PrintForm p : printform) {
					Device device = ServiceFactory.getDao(DeviceDao.class).findByKey(p.getDestinationId());
					String dd = p.getDestinationId().toString();
					String desc = device.getAliasName();
					printerList.add(dd.trim() + " [" + desc.trim() + "]");
				}
			}

			addItemsToList(printerList, getPrinterCombo());
		} catch (Exception e) {
			handleException(e);
		}

		add(getPrintPanel(), BorderLayout.CENTER);		

	}
	
	private void addListeners() {
		getLocationCombo().addActionListener(this);
		getYearCombo().addActionListener(this);
		getMonthCombo().addActionListener(this);
		getModelCombo().addActionListener(this);
		getPrintButton().addActionListener(this);
	}


//	public List<BroadcastDestination> getPrinters() {
//		return printers;
//	}
//
//	public boolean isPrintSupported() {
//		return getPrinters() != null && !getPrinters().isEmpty();
//	}

	// ----------------------------------------------------------------
	// ActionPerformed method (required for ActionListener interface)
	// ----------------------------------------------------------------
	public void actionPerformed(ActionEvent ae) {
		if (ae.paramString().equals(lastParamString)) {
			return;
		} else {
			lastParamString = ae.paramString();
		}

		this.setErrorMessage("");

		if (ae.getSource() == getLocationCombo()) {
			locationComboActionPerformed(ae);
		} else if (ae.getSource() == getYearCombo()) {
			yearComboActionPerformed(ae);
		} else if (ae.getSource() == getMonthCombo()) {
			monthComboActionPerformed(ae);
		} else if (ae.getSource() == getModelCombo()) {
			modelComboActionPerformed(ae);
		} else if (ae.getSource() == getPrintButton()) {
			printButtonActionPerformed(ae);
		}
	}

	private void locationComboActionPerformed(ActionEvent ae) {
		findNextSequenceNumber();
	}

	private void yearComboActionPerformed(ActionEvent ae) {
		findNextSequenceNumber();
	}

	private void monthComboActionPerformed(ActionEvent ae) {
		findNextSequenceNumber();
	}

	private void modelComboActionPerformed(ActionEvent ae) {}
	
	private void printButtonActionPerformed(ActionEvent ae) {
		try {
			clearErrors();
			DataContainer aData = new DefaultDataContainer();

			String loc = ((String) getLocationCombo().getSelectedItem()).substring(0, 2);
			String year = ((String) getYearCombo().getSelectedItem()).substring(0, 2);
			String month = ((String) getMonthCombo().getSelectedItem()).substring(0, 2);
			String model = (String) getModelCombo().getSelectedItem();

			aData.put("LOC", loc);
			aData.put("YR", year);
			aData.put("MONTH", month);
			aData.put("MODEL", model);
			aData.put("PRINTER", findClientIdForPrinter());
			int startSeq = 0;
			try {
				startSeq = Integer.parseInt(getSequenceField().getText());
				if (startSeq < 0) {
					throw new NumberFormatException();
				}
				getSequenceField().setText(_sequenceFormat.format(startSeq));
			} catch (NumberFormatException nfe) {
				this.setErrorMessage("Invalid Sequence Number Format");
				getSequenceField().setBackground(Color.red);
				return;
			}
			aData.put("StartSequenceNumber", getSequenceField().getText());
			
			int printCount = 0;
			try {
				printCount = Integer.parseInt(getNumBarcodesField().getText());
				if (printCount < 0) {
					throw new NumberFormatException();
				} else if (printCount > _maxBarcodes) {
					displayMessageDialog("Notice", "The number of labels printed will be limited to " + _maxBarcodes, this);
					printCount = _maxBarcodes;
					getNumBarcodesField().setText("" + printCount);
				}
			} catch (NumberFormatException nfe) {
				this.setErrorMessage("Invalid Count Number Format");
				getNumBarcodesField().setBackground(Color.red);
				return;
			}			
			aData.put("NumBarcodes", getNumBarcodesField().getText());
			
			boolean response = false;

			response = getResponseFromDialog("Are you sure?", "Print "+getNumBarcodesField().getText()+" label(s) starting with \""+loc+year+month+model+getSequenceField().getText()+"\" ?");

			if (!response) {
				return;
			}
			
			// Spawn separate threads to handle "sending" of print requests in
			// pieces, and to handle "waiting" to see if user cancels an
			// in-progress job.  These both must be separate threads since this
			// method only gets called on event thread and thus ties up GUI
			// resources until it completes.
			Waiter waiter = new Waiter(this);
			Sender sender = new Sender(this, aData, waiter);
			waiter.start();
			sender.start();			

		} catch (Exception e) {
			handleException(e);
		}
	}
	
	private String findClientIdForPrinter() {
		String client_id = null;
		client_id = getPrinterCombo().getSelectedItem().toString();
		client_id = client_id.substring(0, client_id.indexOf("[")).trim();
		return client_id;
	}
	
	private void findNextSequenceNumber() {
		try {
			clearErrors();

			String loc = ((String) ((String) getLocationCombo().getSelectedItem()).subSequence(0, 2)).substring(0, 2);
			String year = ((String) ((String) getYearCombo().getSelectedItem()).subSequence(0, 2)).substring(0, 2);
			String month = ((String) ((String) getMonthCombo().getSelectedItem()).subSequence(0, 2)).substring(0, 2);

			String s = getNextSequenceNumber(loc, year, month, ApplicationContext.getInstance().getApplicationId());
			getSequenceField().setText(s);
		} catch (Exception e) {
			handleException(e);
		}
	}

	private String getNextSequenceNumber(String loc, String year, String month, String ppId) {
		// Search for current sequence number (only using rule_id)
		// If found, return next
		// If not, create record with sequence of 1 and return 1 (create with process point)

		int seq = 0;
		String ruleId = loc + year + month;
		Rule rule = ServiceFactory.getDao(RuleDao.class).findByKey(ruleId);
		if (rule != null) {
			seq = rule.getActiveState();
		} else {
			// Create new record
			Rule newRule = new Rule();
			newRule.setId(loc + year + month);
			newRule.setActiveState(1);
			newRule.setRuleDesc("2D Barcode sequence for loc=" + loc + "; year=" + year + "; month=" + month);
			newRule.setProcessPointId(ppId);
			ServiceFactory.getDao(RuleDao.class).insert(newRule);
			seq = 1;
		}
		return _sequenceFormat.format(seq);
	}

	private void setNextSequenceNumber(String loc, String year, String month, int seq) {
		String ruleId = loc + year + month;
		Rule rule = ServiceFactory.getDao(RuleDao.class).findByKey(ruleId);
		rule.setActiveState(seq);
		ServiceFactory.getDao(RuleDao.class).update(rule);
	}

	private boolean getResponseFromDialog(String title, String message) {
		int responseFromDialog = JOptionPane.showConfirmDialog(this, message, "Continue with print?",  JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return responseFromDialog == JOptionPane.YES_OPTION ? true : false;
	}

	private void displayMessageDialog(String title, String message, Component parentComponent) {
        BarcodePrintDialog msgDialog = new BarcodePrintDialog(title, message);
        msgDialog.setLocationRelativeTo(parentComponent); 
        msgDialog.setVisible(true);
    }

	private void printLabel(DataContainer printItem) {
		try {
			dc = new DefaultDataContainer();
			status = "FAIL";
			dc.putAll(printItem);
			formId = getProperty().getBarcodeForm();
			String productId = printItem.getString("BSN");
			String template = ServiceFactory.getDao(TemplateDao.class).findByKey(formId.toString().trim()).getTemplateName();
			dc.put(DataContainerTag.TERMINAL, ApplicationContext.getInstance().getHostName());
			dc.put(DataContainerTag.PRODUCT_ID, productId);
			dc.put(DataContainerTag.QUEUE_NAME, printItem.getString("PRINTER"));
			dc.put(DataContainerTag.TEMPLATE_NAME, template);
			dc.put(DataContainerTag.FORM_ID, formId);

			if (!StringUtils.isBlank(template)) {
				String templateData = getTemplateData(template);
				if (templateData != null) {

					if (hasPrintAttributes(formId, template)) {
						String printQuantity = DataContainerUtil.getString(dc, DataContainerTag.PRINT_QUANTITY, "1");
						dc.put(DataContainerTag.PRINT_QUANTITY, printQuantity);
						dc.put(DataContainerTag.PRINTER_NAME,PropertyService.getProperty(ApplicationContext.getInstance().getHostName(),dc.get(DataContainerTag.QUEUE_NAME).toString()));
						
						getLogger().info("final Report Info :" + "ProductId:"
								+ DataContainerUtil.getString(dc, DataContainerTag.PRODUCT_ID, "") + " FormId:"
								+ DataContainerUtil.getString(dc, DataContainerTag.FORM_ID, "") + " TemplateId:"
								+ DataContainerUtil.getString(dc, DataContainerTag.TEMPLATE_NAME, "") + " DestinationId:"
								+ DataContainerUtil.getString(dc, DataContainerTag.QUEUE_NAME, "") + " TrayValue:"
								+ DataContainerUtil.getString(dc, TRAY_VALUE, "") + " Duplex:"
								+ DataContainerUtil.getString(dc, JASPER_DUPLEX_FLAG, "") + " PrinterName:"
								+ DataContainerUtil.getString(dc, DataContainerTag.PRINTER_NAME, ""));

						for (Entry<String, String> entry : DataContainerUtil.getAttributeMap(dc).entrySet()) {
							getLogger().info("Key:" + entry.getKey() + "Value:" + entry.getValue());
						}						

						if (printItem.getString("PRINTER") != null) {
							DataContainer dataContainer = new DefaultDataContainer();
							dataContainer = prepareDataFromDataContainer(dc);

							String finalData = getPrintDataFromDevice(printItem.getString("PRINTER"), dataContainer);
							if (finalData != null)
								sendToPrintDevice(printItem.getString("PRINTER"), finalData,Integer.parseInt(printQuantity), printItem.getString("BSN"));
							else
								msg = "Attributes invalid in PrintAttributeFormat";
						} else
							msg = "Device not found";
					} else
						msg = "Print Attributes not found";
				} else
					msg = "Template Data not found";
			} else
				msg = "Template not found";

		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "unable to print the label" + printItem.getString("BSN");
			status = "FAIL";
		}
		updateStatus(msg);

	}

	private String getTemplateData(String template) {
		String templateData = null;
		Template templateObj = null;
		TemplateDao templateDao = ServiceFactory.getDao(TemplateDao.class);
		templateObj = templateDao.findTemplateByTemplateName(template.trim());
		if (templateObj != null && templateObj.getTemplateDataBytes() != null) {
			templateData = templateObj.getTemplateDataString();
		}
		return templateData;
	}

	private String getPrintDataFromDevice(String deviceName, DataContainer dc) {
		IPrintDevice iPrintDevice = null;

		iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(deviceName);
		AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
		return printDevice.getprintData(dc);
	}

	private DataContainer prepareDataFromDataContainer(DataContainer dc) {
		DataContainer dataContainer = new DefaultDataContainer();
		dataContainer.put(DataContainerTag.FORM_ID, dc.get(DataContainerTag.FORM_ID));
		dataContainer.put(DataContainerTag.TEMPLATE_NAME, dc.get(DataContainerTag.TEMPLATE_NAME));
		dataContainer.put(DataContainerTag.PRINTER_NAME, dc.get(DataContainerTag.PRINTER_NAME));
		Map<String, String> map = new HashMap<String, String>();
		map.putAll(DataContainerUtil.getAttributeMap(dc));
		map.put("TRAY_VALUE", DataContainerUtil.getString(dc, "TRAY_VALUE", "0"));
		map.put("JASPER_DUPLEX_FLAG", DataContainerUtil.getString(dc, "JASPER_DUPLEX_FLAG", "false"));
		dataContainer.put(DataContainerTag.KEY_VALUE_PAIR, map);

		return dataContainer;
	}

	private Boolean hasPrintAttributes(String form, String templateName) {
		try {
			List<String> tempSet = new ArrayList<String>();
			Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
			dc = getDataAssembler(template).convertFromPrintAttribute(form, dc);
			if (DataContainerUtil.getAttributeMap(dc) == null) {
				tempSet = getDataAssembler(template).parseTemplateData(template.getTemplateDataString());
				if (tempSet == null || tempSet.size() == 0)
					return true;
				else {
					getLogger().info("No Template Attributes found");
					msg = "No Template Attributes found";
					return false;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Error occured at print attributes check");
		}

		return true;
	}

	private IPrintAttributeConvertor getDataAssembler(Template template) {
		if ("JASPER".equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintAttributeConvertor(getLogger());
		else if ("COMPILED_JASPER".equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintAttributeConvertor(getLogger());
		else
			return new AttributeConvertor(getLogger());
	}
	
	/**
	 * 
	 * @param deviceName
	 * @param dataToPrint
	 */
	private void sendToPrintDevice(String deviceName, String dataToPrint, int printQty, String productId) {
		IPrintDevice iPrintDevice = null;
		try {
			iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(deviceName);
			AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
			PrintForm printform = ServiceFactory.getDao(PrintFormDao.class).findByKey(new PrintFormId(
					getProperty().getBarcodeForm(), printDevice.getDestinationPrinter()));

			if (printform != null) {
				if (!iPrintDevice.isActive()) {
					iPrintDevice.activate();
					iPrintDevice.registerListener(deviceName, this);
					iPrintDevice.requestControl(deviceName, this);
				}
				if (iPrintDevice.isActive() && iPrintDevice.isConnected()) {

					if (printDevice.print(dataToPrint, printQty, productId)) {
						msg = "Template sent to Printer successfully";
						status = "SUCCESS";

					} else {

						msg = "Unable to sent the template to Printer " + deviceName;
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

	private void clearErrors() {
		this.setErrorMessage("");
		getSequenceField().setBackground(Color.white);
		getNumBarcodesField().setBackground(Color.white);
	}

	public BarcodePrintPropertyBean getProperty() {
		return PropertyService.getPropertyBean(BarcodePrintPropertyBean.class,
				ApplicationContext.getInstance().getApplicationId());
	}

	private JPanel getPrintPanel() {
		if (this.valueSelectionPanel == null) {
			this.valueSelectionPanel = new JPanel(new GridBagLayout());
			this.valueSelectionPanel.setBorder(new TitledBorder("BARCODE PRINT"));
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel,
					createLabelFor("Part Location:", getLocationCombo()), 0, 0, 2, 1, null, null, null, INSETS,
					GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getLocationCombo(), 1, 0, 2, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, createLabelFor("Year:", getYearCombo()), 0, 1, 1,
					1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getYearCombo(), 1, 1, 1, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, createLabelFor("Month:", getMonthCombo()), 0, 2, 1,
					1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getMonthCombo(), 1, 2, 1, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, createLabelFor("Model:", getModelCombo()), 0, 3, 1,
					1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getModelCombo(), 1, 3, 1, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel,
					createLabelFor("Starting Sequence #:", getSequenceField()), 0, 4, 1, 1, null, null, null, INSETS,
					GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getSequenceField(), 1, 4, 1, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel,
					createLabelFor("# Barcodes to print:", getNumBarcodesField()), 0, 5, 1, 1, null, null, null, INSETS,
					GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getNumBarcodesField(), 1, 5, 1, 1, null, null,
					null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel,
					createLabelFor("Select printer:", getPrinterCombo()), 0, 6, 1, 1, null, null, null, INSETS,
					GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getPrinterCombo(), 1, 6, 1, 1, null, null, null,
					INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(this.valueSelectionPanel, getPrintButton(), 0, 7, 2, 1, null, null, null,
					INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}
		return this.valueSelectionPanel;
	}

	protected static JLabel createLabelFor(String labelText, Component forComponent) {
		JLabel label = new JLabel(labelText);
		label.setFont(FONT);
		label.setLabelFor(forComponent);
		return label;
	}

	private void addItemsToList(List list, JComboBox combo) {
		combo.removeAllItems();
		// If null list, add single <empty> item to list
		if (list == null || list.size() == 0) {
			combo.addItem("");
		}
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			String s = (String) list.get(i);
			if (s.startsWith("[DEFAULT]")) {
				index = i;
				s = s.substring(9);
			}
			combo.addItem(s);
		}
		combo.setSelectedIndex(index);
	}
	
	protected JLabel getLocationLabel() {
		if (_locationLabel == null) {
			_locationLabel = new JLabel();
			_locationLabel.setName("AreaLabel");
			_locationLabel.setFont(FONT);
			_locationLabel.setText("Part Location:");
		}
		return _locationLabel;
	}

	protected JLabel getYearLabel() {
		if (_yearLabel == null) {
			_yearLabel = new JLabel();
			_yearLabel.setName("YearLabel");
			_yearLabel.setFont(FONT);
			_yearLabel.setText("Year:");
		}
		return _yearLabel;
	}

	protected JLabel getMonthLabel() {
		if (_monthLabel == null) {
			_monthLabel = new JLabel();
			_monthLabel.setName("MonthLabel");
			_monthLabel.setFont(FONT);
			_monthLabel.setText("Month:");
		}
		return _monthLabel;
	}

	protected JLabel getModelLabel() {
		if (_modelLabel == null) {
			_modelLabel = new JLabel();
			_modelLabel.setName("ModelLabel");
			_modelLabel.setFont(FONT);
			_modelLabel.setText("Model:");
		}
		return _modelLabel;
	}

	protected JLabel getSequenceLabel() {
		if (_sequenceLabel == null) {
			_sequenceLabel = new JLabel();
			_sequenceLabel.setName("SequenceLabel");
			_sequenceLabel.setFont(FONT);
			_sequenceLabel.setText("Starting sequence #:");
		}
		return _sequenceLabel;
	}

	protected JLabel getNumBarcodesLabel() {
		if (_numBarcodesLabel == null) {
			_numBarcodesLabel = new JLabel();
			_numBarcodesLabel.setName("NumBarcodesLabel");
			_numBarcodesLabel.setFont(FONT);
			_numBarcodesLabel.setText("# Barcodes to print:");
		}
		return _numBarcodesLabel;
	}

	protected JLabel getPrinterLabel() {
		if (_printerLabel == null) {
			_printerLabel = new JLabel();
			_printerLabel.setName("PrinterLabel");
			_printerLabel.setFont(FONT);
			_printerLabel.setText("Select Printer:");
		}
		return _printerLabel;
	}

	protected JComboBox getLocationCombo() {
		if (_locationCombo == null) {
			_locationCombo = new JComboBox();
			_locationCombo.setName("LocationCombo");
			_locationCombo.setFont(FONT);
			_locationCombo.setPreferredSize(COMBO_BOX_DIMENSION);

		}
		return _locationCombo;
	}

	protected JComboBox getYearCombo() {
		if (_yearCombo == null) {
			_yearCombo = new JComboBox();
			_yearCombo.setName("YearCombo");
			_yearCombo.setFont(FONT);
			_yearCombo.setPreferredSize(COMBO_BOX_DIMENSION);
		}
		return _yearCombo;
	}

	protected JComboBox getMonthCombo() {
		if (_monthCombo == null) {
			_monthCombo = new JComboBox();
			_monthCombo.setName("MonthCombo");
			_monthCombo.setFont(FONT);
			_monthCombo.setPreferredSize(COMBO_BOX_DIMENSION);
		}
		return _monthCombo;
	}

	protected JComboBox getModelCombo() {
		if (_modelCombo == null) {
			_modelCombo = new JComboBox();
			_modelCombo.setName("ModelCombo");
			_modelCombo.setFont(FONT);
			_modelCombo.setPreferredSize(COMBO_BOX_DIMENSION);
		}
		return _modelCombo;
	}

	protected UpperCaseFieldBean getSequenceField() {
		if (_sequenceField == null) {
			_sequenceField = new UpperCaseFieldBean();
			_sequenceField.setName("SequenceTextField");
			_sequenceField.setFont(FONT);
			_sequenceField.setText("00001");
			_sequenceField.setMaximumLength(5);
			_sequenceField.setColumns(TEXT_FIELD_SIZE);
			_sequenceField.setEditable(false);
		}
		return _sequenceField;
	}

	protected UpperCaseFieldBean getNumBarcodesField() {
		if (_numBarcodesField == null) {
			_numBarcodesField = new UpperCaseFieldBean();
			_numBarcodesField.setName("NumBarcodesTextField");
			_numBarcodesField.setFont(FONT);
			_numBarcodesField.setText("250");
			_numBarcodesField.setColumns(TEXT_FIELD_SIZE);
			_numBarcodesField.setMaximumLength(4);
		}
		return _numBarcodesField;
	}

	protected JComboBox getPrinterCombo() {
		if (_printerCombo == null) {
			_printerCombo = new JComboBox();
			_printerCombo.setName("PrinterCombo");
			_printerCombo.setFont(FONT);
			_printerCombo.setPreferredSize(COMBO_BOX_DIMENSION);
			_printerCombo.addItem("");
		}
		return _printerCombo;
	}

	private JButton getPrintButton() {
		if (_printButton == null) {
			_printButton = new JButton();
			_printButton.setName("PrintButton");
			_printButton.setText("Print");
			_printButton.setFont(FONT);
		}
		return _printButton;
	}
	
	@Override
	public void onTabSelected() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param message
	 */
	private void updateStatus(String message) {
		getLogger().info(message);
		super.setMessage(message);
	}

	@Override
	public String getApplicationName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getDeviceAccessKey(String deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void controlGranted(String deviceId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controlRevoked(String deviceId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleStatusChange(PrintDeviceStatusInfo statusInfo) {
		// TODO Auto-generated method stub

	}
}