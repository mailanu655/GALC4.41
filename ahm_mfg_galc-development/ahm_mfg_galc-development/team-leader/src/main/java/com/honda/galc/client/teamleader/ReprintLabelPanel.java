package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.datacollection.property.TerminalPropertyBean;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.jasper.JasperDevice;
import com.honda.galc.client.teamleader.model.EngineTableModel;
import com.honda.galc.client.teamleader.model.FrameTableModel;
import com.honda.galc.client.teamleader.model.MbpnTableModel;
import com.honda.galc.client.teamleader.model.ProductTableModel;
import com.honda.galc.client.teamleader.model.SubProductTableModel2;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.dao.product.BuildAttributeDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IDevice;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.device.events.IPrintDeviceListener;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.Template;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.AttributeConvertor;
import com.honda.galc.service.printing.CompiledJasperPrintAttributeConvertor;
import com.honda.galc.service.printing.IPrintAttributeConvertor;
import com.honda.galc.service.printing.JasperPrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;
/** * * 
* 
* @author Gangadhararao Gadde 
* @since May 16, 2016
*/
public class ReprintLabelPanel extends TabbedPanel implements ActionListener, ListSelectionListener, IPrintDeviceListener{
	
	private static final long serialVersionUID = 1L;
	private static final String INVALID_PRODUCT_ID = "Invalid product ID";
	private static final String INVALID_SEQUENCE_NUMBER = "Invalid sequence number";
	private static final String INVALID_MIN_SEQUENCE_NUMBER = "Invalid MIN sequence number";
	private static final String INVALID_MAX_SEQUENCE_NUMBER = "Invalid MAX sequence number";
	private static final String INVALID_PRODUCTION_LOT = "Invalid production lot";
	private static final String INVALID_TRACKING_STATUS = "Invalid tracking status";

	private JScrollPane jScrollPane = null;
	private LabeledComboBox formComboBox, productTypeComboBox,printerComboBox,duplexFlagComboBox,trayComboBox;
	private List<String> form;
	private ComboBoxModel<String> model;
	private JRadioButton jRadioButtonProductId,jRadioButtonSeqNumber, jRadioButtonSeqNumRange,jRadioButtonProductionLot, jRadioButtonTrackingStatus = null;
	public List<? extends BaseProduct> products;
	private String formId, status= "FAIL", msg;
	private JPanel jPaneSearchOptionRadioBtn = null;
	private JTextField jTextProductIdSearchTxt = null;
	private JTextField jTextSeqMin = null;
	private JTextField jTextSeqMax = null;
	private JButton jButtonSearch,jButtonPrint = null;
	private JPanel jPanel1 = null;
	private JLabel jLabelPrintQueue = null;
    private JScrollPane jPrintQueueScrollPane = null;
    private JTable jViewTablePrintQueue = null;
    private TableColumn ivjTableColumnPrintQueueProductId = null;
	private TableColumn ivjTableColumnPrintQueuePrinter = null;
	private ProductTableModel productTableModel;
	private TablePane attributePane;
	private volatile ConcurrentLinkedQueue<ProductPrintQueueItem> _printQueue = null;
	private int _deviceAccessKey = -1;
	private DataContainer dc;
	private static final String TRAY_VALUE = "TRAY_VALUE";
	private static final String JASPER_DUPLEX_FLAG = "JASPER_DUPLEX_FLAG";
	private TerminalPropertyBean property;
	

	public ReprintLabelPanel(TabbedMainWindow mainWindow) {
		super("Reprint", KeyEvent.VK_G, mainWindow);
		initComponents();
		addListeners();
	}
	
	protected void initComponents() {
		loadData();
		setLayout(new BorderLayout());
		add(getJPanel1());
	}

	private void addListeners() {
		attributePane.getTable().getSelectionModel().addListSelectionListener(this);
		getFormComboBox().getComponent().addActionListener(this);
		getPrinterComboBox().getComponent().addActionListener(this);
		getProductTypeComboBox().getComponent().addActionListener(this);
		getJButtonPrint().addActionListener(this);
		getJRadioButtonProductId().addActionListener(this);
		getJRadioButtonSeqNumber().addActionListener(this);
		getJRadioButtonSeqNumRange().addActionListener(this);
		getJRadioButtonProductionLot().addActionListener(this);
		getJRadioButtonTrackingStatus().addActionListener(this);
		getJButtonSearch().addActionListener(this);
	}

	private void loadData() {
		try {
			getApplicationProperty();
			Map<String, String> formIds = property.getFormId();
						
			if(formIds != null){
				form = new ArrayList<String>();
				for(Entry<String, String> obj : formIds.entrySet()){
					form.add(obj.getValue());
				}
			}
			if(form == null || form.isEmpty())
					form = ServiceFactory.getDao(PrintFormDao.class).findDistinctForms();
			
			form.removeAll(Collections.singleton(null)); 

		} catch (Exception e) {
			getLogger().info("Load Data" + e);
		}
	}

	public void getApplicationProperty() {
		property = PropertyService.getPropertyBean(TerminalPropertyBean.class, ApplicationContext.getInstance().getHostName());
	}
	
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
				jPanel1 = new javax.swing.JPanel();
				jPanel1.setName("JPanel1");
				jPanel1.setLayout(null);
				getJPanel1().add(getFormComboBox(), getFormComboBox().getName());
				getJPanel1().add(getProductTypeComboBox(), getProductTypeComboBox().getName());
				getJPanel1().add(getSearchOptionPanel(), getSearchOptionPanel().getName());
				getJPanel1().add(getJScrollPane(), getJScrollPane().getName());
				getJPanel1().add(getJLabelPrintQueue(), getJLabelPrintQueue().getName());
				getJPanel1().add(getJPrintQueueScrollPane(), getJPrintQueueScrollPane().getName());
				getJPanel1().add(getPrinterComboBox(), getPrinterComboBox().getName());
				getJPanel1().add(getTrayComboBox(), getTrayComboBox().getName());
				getJPanel1().add(getDuplexComboBox(), getDuplexComboBox().getName());
				getJPanel1().add(getJButtonPrint(), getJButtonPrint().getName());
			}
			return jPanel1;
		}
	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			try {
				jScrollPane = new javax.swing.JScrollPane();
				jScrollPane.setName("JScrollPane");
				jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jScrollPane.setBounds(450, 35, 565, 290);
				jScrollPane.setRequestFocusEnabled(false);

				getJScrollPane().setViewportView(createAttributeTablePane());
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jScrollPane;
	}
	
	private TablePane createAttributeTablePane() {
		attributePane = new TablePane("Product Table",ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		attributePane.setPreferredWidth(350);
		attributePane.setMaxWidth(350);
		productTableModel = new FrameTableModel((List<BaseProduct>) products, attributePane.getTable());
		return attributePane;
	}
	

	private JLabel getJLabelPrintQueue() {
		if (jLabelPrintQueue == null) {
			try {
				jLabelPrintQueue = new javax.swing.JLabel();
				jLabelPrintQueue.setName("JLabelPrintQueue");
				jLabelPrintQueue.setFont(Fonts.DIALOG_BOLD_14);
				jLabelPrintQueue.setText("Print Queue");
				jLabelPrintQueue.setBounds(780, 350, 130, 20);
				jLabelPrintQueue.setVisible(false);
				jLabelPrintQueue.setForeground(new java.awt.Color(0,0,0));
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jLabelPrintQueue;
	}
	
	private JScrollPane getJPrintQueueScrollPane() {
		if (jPrintQueueScrollPane == null) {
			try {
				jPrintQueueScrollPane = new javax.swing.JScrollPane();
				jPrintQueueScrollPane.setName("JPrintQueueScrollPane");
				jPrintQueueScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jPrintQueueScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				jPrintQueueScrollPane.setBounds(700, 390, 240, 200);
				jPrintQueueScrollPane.setRequestFocusEnabled(false);
				jPrintQueueScrollPane.setVisible(false);

				getJPrintQueueScrollPane().setViewportView(getViewTablePrintQueue());
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jPrintQueueScrollPane;
	}
	
	private JTable getViewTablePrintQueue() {
		if (jViewTablePrintQueue == null) {
			try {
				jViewTablePrintQueue = new JTable();
				jViewTablePrintQueue.setName("ViewTablePrintQueue");
				jViewTablePrintQueue.setTableHeader(null);
				getJPrintQueueScrollPane().setColumnHeaderView(jViewTablePrintQueue.getTableHeader());
				getJPrintQueueScrollPane().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
				jViewTablePrintQueue.setBounds(0, 0, 240, 250);
				jViewTablePrintQueue.setAutoCreateColumnsFromModel(false);
				jViewTablePrintQueue.addColumn(getTableColumnPrintQueueProductId());
				jViewTablePrintQueue.addColumn(getTableColumnPrintQueuePrinter());				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jViewTablePrintQueue;
	}
	
	private TableColumn getTableColumnPrintQueueProductId() {
		if (ivjTableColumnPrintQueueProductId == null) {
			try {
				ivjTableColumnPrintQueueProductId = new TableColumn();
				ivjTableColumnPrintQueueProductId.setModelIndex(0);
				ivjTableColumnPrintQueueProductId.setWidth(160);
				ivjTableColumnPrintQueueProductId.setPreferredWidth(ivjTableColumnPrintQueueProductId.getWidth());
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return ivjTableColumnPrintQueueProductId;
	}
	
	
	private TableColumn getTableColumnPrintQueuePrinter() {
		if (ivjTableColumnPrintQueuePrinter == null) {
			try {
				ivjTableColumnPrintQueuePrinter = new TableColumn();
				ivjTableColumnPrintQueuePrinter.setModelIndex(1);
				ivjTableColumnPrintQueuePrinter.setWidth(80);
				ivjTableColumnPrintQueuePrinter.setPreferredWidth(ivjTableColumnPrintQueuePrinter.getWidth());
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return ivjTableColumnPrintQueuePrinter;
	}
	
	
	private JButton getJButtonPrint() {
		if (jButtonPrint == null) {
			try {
				jButtonPrint = new javax.swing.JButton();
				jButtonPrint.setName("JButtonPrint");
				jButtonPrint.setFont(Fonts.DIALOG_BOLD_12);
				jButtonPrint.setText("Print");
				jButtonPrint.setEnabled(false);
				jButtonPrint.setBounds(30, 550, 90, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jButtonPrint;
	}
	
	private JPanel getSearchOptionPanel(){
		if (jPaneSearchOptionRadioBtn == null) {
			try {
				ButtonGroup radioBtnGroupSearchOptions = new ButtonGroup();
				radioBtnGroupSearchOptions.add(getJRadioButtonProductId());
				radioBtnGroupSearchOptions.add(getJRadioButtonSeqNumber());
				radioBtnGroupSearchOptions.add(getJRadioButtonSeqNumRange());
				radioBtnGroupSearchOptions.add(getJRadioButtonProductionLot());
				radioBtnGroupSearchOptions.add(getJRadioButtonTrackingStatus());
				radioBtnGroupSearchOptions.setSelected(this.getJRadioButtonProductId().getModel(), true);

				jPaneSearchOptionRadioBtn = new javax.swing.JPanel();
				jPaneSearchOptionRadioBtn.setLayout(null);
				jPaneSearchOptionRadioBtn.add(getJRadioButtonProductId());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonSeqNumber());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonSeqNumRange());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonProductionLot());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonTrackingStatus());
				jPaneSearchOptionRadioBtn.add(getJTextProductIdSearchTxt());
				jPaneSearchOptionRadioBtn.add(getJTextSeqMin());
				jPaneSearchOptionRadioBtn.add(getJTextSeqMax());
				jPaneSearchOptionRadioBtn.add(getJButtonSearch());

				TitledBorder radioButtonZone = new TitledBorder(new LineBorder(Color.BLACK, 1), "Search option: ");
				radioButtonZone.setTitleFont(Fonts.DIALOG_BOLD_14);
				radioButtonZone.setTitlePosition(TitledBorder.CENTER);
				radioButtonZone.setTitleJustification(TitledBorder.LEFT);
				jPaneSearchOptionRadioBtn.setBorder(radioButtonZone);
				jPaneSearchOptionRadioBtn.setBounds(20, 120, 420, 280);

			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jPaneSearchOptionRadioBtn;
	}
	
	private JTextField getJTextSeqMin() {
		if (jTextSeqMin == null) {
			try {
				jTextSeqMin = new javax.swing.JTextField();
				jTextSeqMin.setName("JTextSeqMin");
				jTextSeqMin.setFont(Fonts.DIALOG_PLAIN_24);
				jTextSeqMin.setText("");
				jTextSeqMin.setBounds(170, 40, 240, 40);
				jTextSeqMin.setVisible(false);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jTextSeqMin;
	}
	
	private JTextField getJTextSeqMax() {
		if (jTextSeqMax == null) {
			try {
				jTextSeqMax = new javax.swing.JTextField();
				jTextSeqMax.setName("JTextSeqMax");
				jTextSeqMax.setFont(Fonts.DIALOG_PLAIN_24);
				jTextSeqMax.setText("");
				jTextSeqMax.setBounds(170, 100, 240, 40);
				jTextSeqMax.setVisible(false);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jTextSeqMax;
	}
	
	private JButton getJButtonSearch() {
		if (jButtonSearch == null) {
			try {
				jButtonSearch = new javax.swing.JButton();
				jButtonSearch.setName("JButtonSearch");
				jButtonSearch.setFont(Fonts.DIALOG_BOLD_12);
				jButtonSearch.setText("Search");
				jButtonSearch.setBounds(125, 205, 90, 30);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jButtonSearch;
	}
	
	private JTextField getJTextProductIdSearchTxt() {
		if (jTextProductIdSearchTxt == null) {
			try {
				jTextProductIdSearchTxt = new javax.swing.JTextField();
				jTextProductIdSearchTxt.setName("getJTextProductIdSearchTxt");
				jTextProductIdSearchTxt.setFont(Fonts.DIALOG_PLAIN_24);
				jTextProductIdSearchTxt.setText("");
				jTextProductIdSearchTxt.setVisible(true);
				jTextProductIdSearchTxt.setBounds(170, 30, 240, 40);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jTextProductIdSearchTxt;
	}
	
	private JRadioButton getJRadioButtonProductId() {
		if (jRadioButtonProductId == null) {
			try {
				jRadioButtonProductId = new javax.swing.JRadioButton();
				jRadioButtonProductId.setName("getJRadioButtonProductId");
				jRadioButtonProductId.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonProductId.setText("Product ID ");
				jRadioButtonProductId.setBounds(30, 35, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonProductId;
	}
	
	private JRadioButton getJRadioButtonSeqNumber() {
		if (jRadioButtonSeqNumber == null) {
			try {
				jRadioButtonSeqNumber = new javax.swing.JRadioButton();
				jRadioButtonSeqNumber.setName("JRadioButtonSeqNumber");
				jRadioButtonSeqNumber.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonSeqNumber.setText("SEQ Number ");
				jRadioButtonSeqNumber.setBounds(30, 65, 120, 20);
				jRadioButtonSeqNumber.setVisible(true);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonSeqNumber;
	}
	
	private JRadioButton getJRadioButtonSeqNumRange() {
		if (jRadioButtonSeqNumRange == null) {
			try {
				jRadioButtonSeqNumRange = new javax.swing.JRadioButton();
				jRadioButtonSeqNumRange.setName("JRadioButtonSeqNumRange");
				jRadioButtonSeqNumRange.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonSeqNumRange.setText("SEQ Range");
				jRadioButtonSeqNumRange.setBounds(30, 95, 120, 20);
				jRadioButtonSeqNumRange.setVisible(true);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonSeqNumRange;
	}
	
	private JRadioButton getJRadioButtonProductionLot() {
		if (jRadioButtonProductionLot == null) {
			try {
				jRadioButtonProductionLot = new javax.swing.JRadioButton();
				jRadioButtonProductionLot.setName("JRadioButtonProductionLot");
				jRadioButtonProductionLot.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonProductionLot.setText("Production Lot");
				jRadioButtonProductionLot.setBounds(30, 125, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonProductionLot;
	}
	
	private JRadioButton getJRadioButtonTrackingStatus() {
		if (jRadioButtonTrackingStatus == null) {
			try {
				jRadioButtonTrackingStatus = new javax.swing.JRadioButton();
				jRadioButtonTrackingStatus.setName("JRadioButtonTrackingStatus");
				jRadioButtonTrackingStatus.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonTrackingStatus.setText("TrackingStatus");
				jRadioButtonTrackingStatus.setBounds(30, 155, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonTrackingStatus;
	}
	
	private LabeledComboBox getPrinterComboBox() {
		if (printerComboBox == null) {
			printerComboBox = new LabeledComboBox("Printer");
			printerComboBox.getComponent().setName("PrinterCombobox");
			printerComboBox.getComponent().setPreferredSize(new Dimension(150, 20));
			printerComboBox.setBounds(20, 410, 300, 40);
		} 
		return printerComboBox;
	}

	private LabeledComboBox getFormComboBox() {
		if (formComboBox == null) {
			formComboBox = new LabeledComboBox("Forms");
			formComboBox.getComponent().setName("FormCombobox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(form);
			formComboBox.getComponent().setModel(model);
			formComboBox.getComponent().setSelectedIndex(-1);
			formComboBox.getComponent().setRenderer(model);
			formComboBox.setBounds(20, 25, 170, 50);
		}
		return formComboBox;
	}

	private LabeledComboBox getProductTypeComboBox() {
		List<String> prodtypes = new ArrayList<String>();
		prodtypes.add(ProductType.FRAME.toString());
		prodtypes.add("PRODUCT");
		prodtypes.add(ProductType.ENGINE.toString());
		prodtypes.add(ProductType.IPU.toString());
		prodtypes.add(ProductType.KNUCKLE.toString());
		prodtypes.add(ProductType.PLASTICS.toString());
		prodtypes.add(ProductType.HEAD.toString());
		prodtypes.add(ProductType.BLOCK.toString());
		prodtypes.add(ProductType.MBPN.toString());
		
		if (productTypeComboBox == null) {
			productTypeComboBox = new LabeledComboBox("Product Type");
			productTypeComboBox.getComponent().setName("ProductComboBox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(prodtypes);
			productTypeComboBox.getComponent().setModel(model);
			productTypeComboBox.getComponent().setSelectedIndex(-1);
			productTypeComboBox.getComponent().setRenderer(model);
			productTypeComboBox.setBounds(190, 25, 260, 50);
		}
		return productTypeComboBox;
	}
	
	public void actionPerformed(ActionEvent e) {
		updateStatus("");
		if (e.getSource().equals(getFormComboBox().getComponent())){
			formChanged();
		}
		if (e.getSource().equals(getPrinterComboBox().getComponent())){
			printChanged();
		}
		if (e.getSource().equals(getProductTypeComboBox().getComponent())){
			productTypeChanged();
		}
		else if(e.getSource().equals(getJRadioButtonProductId())){
			showProductIdSearchText();
		}
		else if(e.getSource().equals(getJRadioButtonSeqNumber())){
			showSeqSearchText();
		}
		else if(e.getSource().equals(getJRadioButtonSeqNumRange())){
			showSequenceRangeOptions();
		}
		else if(e.getSource().equals(getJRadioButtonProductionLot())){
			showProductionLotSearch();
		}
		else if(e.getSource().equals(getJRadioButtonTrackingStatus())){
			showTrackingStatusSearch();
		}
		else if(e.getSource().equals(getJButtonSearch())){
			showSearch();
		}
		else if(e.getSource().equals(getJButtonPrint())){
			clickPrint();
		}
	}
		
	private void addEntryToPrintQueue(Entry<String,String> entry){
		ProductPrintQueueItem item = new ProductPrintQueueItem();
		item.setProductId(entry.getKey());
		item.setMtoc(entry.getValue());
		item.setPrinterName(getPrinterName());
		getPrintQueue().add(item);
	}
	
	private void clickPrint(){
		DefaultTableModel printQueueTable = new DefaultTableModel(0, 2);
		List<ProductPrintQueueItem> selectedPrintItem = new ArrayList<ProductPrintQueueItem>();
		getJLabelPrintQueue().setVisible(true);
		getJPrintQueueScrollPane().setVisible(true);
		LinkedHashMap<String, String> linkedMap = getSelectedPrintCandidates();
		for(Entry<String, String> entry: linkedMap.entrySet()){
					addEntryToPrintQueue(entry);
					Vector<String> row = new Vector<String>();
					row.addElement(entry.getKey().trim());
					row.addElement(getPrinterName().trim());
					printQueueTable.addRow(row);
		}
		while (!getPrintQueue().isEmpty()) {
			getViewTablePrintQueue().setModel(printQueueTable);
			ProductPrintQueueItem printItem = null;
			try{
				printItem = getPrintQueue().poll();
				if (printItem == null) {
					continue;
				}
				else
				{
					printLabel(printItem);
					printItem.setStatus(status);
					selectedPrintItem.add(printItem);
				}
			} catch(Exception ex){
				msg = printItem.getProductId()+ "Print Queue stopped";
				productTableModel.sendPrintStatusList(selectedPrintItem);
				productTableModel.refresh((List<BaseProduct>) products);
			}
		}
		productTableModel.sendPrintStatusList(selectedPrintItem);
		productTableModel.refresh( (List<BaseProduct>) products);
		getJLabelPrintQueue().setVisible(false);
		getJPrintQueueScrollPane().setVisible(false);
		
	}
	
	private Boolean hasPrintAttributes(String form, String templateName) {
		try {
			List<String> tempSet = new ArrayList<String>();
			Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
			dc =  getDataAssembler(template).convertFromPrintAttribute(form, dc);
			if (DataContainerUtil.getAttributeMap(dc) == null) {
				tempSet = getDataAssembler(template).parseTemplateData(template.getTemplateDataString());
				if(tempSet == null ||tempSet.size()==0)
					return true;
				else{
				getLogger().info("No Template Attributes found");
				msg =  "No Template Attributes found";
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
		if("JASPER".equalsIgnoreCase(template.getTemplateTypeString()))
			return new JasperPrintAttributeConvertor(getLogger());
		else if("COMPILED_JASPER".equalsIgnoreCase(template.getTemplateTypeString()))
			return new CompiledJasperPrintAttributeConvertor(getLogger());
		else 
			return new AttributeConvertor(getLogger());
	}
	
	private void printLabel(ProductPrintQueueItem printItem){
		try{
			dc = new DefaultDataContainer();
			status = "FAIL"; 
			formId = getFormComboBox().getComponent().getSelectedItem()
					.toString();
			String productId = printItem.getProductId();
			String mtoc = printItem.getMtoc();
			String template = getAttributeByMTOC(formId.trim(), mtoc);
			dc.put(DataContainerTag.TERMINAL, ApplicationContext.getInstance().getHostName());
			dc.put(DataContainerTag.PRODUCT_ID, productId);
			dc.put(DataContainerTag.PRODUCT_SPEC_CODE, mtoc);
			dc.put(DataContainerTag.QUEUE_NAME, printItem.getPrinterName());
			dc.put(DataContainerTag.FORM_ID, formId);
			dc.put(DataContainerTag.TEMPLATE_NAME, template);
			
			if (!StringUtils.isBlank(template)) {
				String templateData = getTemplateData(template);
				if (templateData != null) {
					
					if(hasPrintAttributes(formId, template)){
					
						
						if(getTrayComboBox().getComponent().getSelectedItem()!= null)
							dc.put(TRAY_VALUE, getTrayComboBox().getComponent().getSelectedItem().toString());
							if(getDuplexComboBox().getComponent().getSelectedItem()!= null)
							dc.put(JASPER_DUPLEX_FLAG, getDuplexComboBox().getComponent().getSelectedItem()
									.toString());
							String printQuantity = DataContainerUtil.getString(dc, DataContainerTag.PRINT_QUANTITY, "1");
							dc.put(DataContainerTag.PRINT_QUANTITY, printQuantity);
							getLogger().info("final Report Info :"+ "ProductId:"+ DataContainerUtil.getString(dc,DataContainerTag.PRODUCT_ID, "")
								+"PartName:"+DataContainerUtil.getString(dc,DataContainerTag.FORM_ID, "")
								+"TemplateId:"+DataContainerUtil.getString(dc,DataContainerTag.TEMPLATE_NAME, "")
								+"DestinationId:"+ DataContainerUtil.getString(dc,DataContainerTag.QUEUE_NAME, "")
								+"TrayValue:"+ DataContainerUtil.getString(dc,TRAY_VALUE, "")
								+"Duplex:"+ DataContainerUtil.getString(dc,JASPER_DUPLEX_FLAG, "")
								+"PrinterName:"+ DataContainerUtil.getString(dc,DataContainerTag.PRINTER_NAME, "")
								);
						
						for (Entry<String, String> entry : DataContainerUtil.getAttributeMap(dc).entrySet()) {
							   getLogger().info("Key:"+ entry.getKey()+"Value:"+ entry.getValue());
						}
						
						dc.put(DataContainerTag.PRINTER_NAME, PropertyService.getProperty(ApplicationContext.getInstance().getHostName(), dc.get(DataContainerTag.QUEUE_NAME).toString()));
						
						if (printItem.getPrinterName() != null){
								DataContainer dataContainer = new DefaultDataContainer();
								dataContainer = prepareDataFromDataContainer(dc);
							
								String finalData = getPrintDataFromDevice(printItem.getPrinterName(), dataContainer);
								if(finalData != null)
								sendToPrintDevice(printItem.getPrinterName(),
									finalData, Integer.parseInt(printQuantity), printItem.getProductId());
							else
								msg = "Attributes invalid in PrintAttributeFormat";
						}
						else
							msg = "Device not found";
					} else
						msg = "Print Attributes not found";
				} else
					msg = "Template Data not found";
			} else
				msg = "Template not found"; 

		} catch (Exception ex) {
			ex.printStackTrace();
			msg = "unable to print the label" + printItem.getProductId();
			status = "FAIL";
		}
		updateStatus(msg);

	}
	
	private DataContainer prepareDataFromDataContainer(DataContainer dc){
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
	
	private String getPrintDataFromDevice(String deviceName, DataContainer dc){
		IPrintDevice iPrintDevice = null;
		
			iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(deviceName);
			AbstractPrintDevice printDevice = (AbstractPrintDevice)iPrintDevice;
			return printDevice.getprintData(dc);
	}
	
	
	
	private void sendToPrintDevice(String deviceName, String dataToPrint, int printQuantity, String productId) {
		IPrintDevice iPrintDevice = null;
		try {
			iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(deviceName);
			AbstractPrintDevice printDevice = (AbstractPrintDevice)iPrintDevice;
			PrintForm printform = ServiceFactory.getDao(PrintFormDao.class).findByKey(new PrintFormId(formId.trim(), printDevice.getDestinationPrinter()));
			
			if(printform!= null){
				if (!iPrintDevice.isActive()) {
					iPrintDevice.activate();
					iPrintDevice.registerListener(deviceName,this);
					iPrintDevice.requestControl(deviceName,this);
				}
				if(iPrintDevice.isActive()&& iPrintDevice.isConnected()){
				
				if(printDevice.print(dataToPrint, printQuantity, productId)){
					msg = "Template sent to Printer successfully";
					status ="SUCCESS";
				} else {

					msg = "Unable to sent the template to Printer: " + deviceName;
				}
				checkComboSetUp();
				} else{
					msg = "Device is not active or not connected: " + deviceName;
				}
			}
			else{
				msg = deviceName +" Device not configured in PrintForm";
				
			}
		} catch(Exception ex){
			ex.printStackTrace();
			checkComboSetUp();
		}
		updateStatus(msg);
	}
	
	public void checkComboSetUp(){
		getDuplexComboBox().getComponent().setSelectedItem(null);
		getTrayComboBox().getComponent().setSelectedItem(null);
	}
	
	private String getTemplateData(String template)
	{
		String templateData = null;
		Template templateObj = null;
		TemplateDao templateDao = ServiceFactory.getDao(TemplateDao.class);
		templateObj = templateDao.findTemplateByTemplateName(template.trim());
		if(templateObj != null && templateObj.getTemplateDataBytes()!= null){
			 templateData = templateObj.getTemplateDataString();
		}
		return templateData;
	}
	
	private String getAttributeByMTOC(String form, String mtoc)
	{
		String template = " ";
		BuildAttribute buildAttribute = null;
		BuildAttributeDao buildAttributeDao = ServiceFactory.getDao(BuildAttributeDao.class);
		buildAttribute = buildAttributeDao.findById(form, mtoc);
		if(buildAttribute != null)
		{
			template = buildAttribute.getAttributeValue();
		}
		return template;
	}
	
	private ConcurrentLinkedQueue<ProductPrintQueueItem> getPrintQueue() {
		if (_printQueue == null) {
			_printQueue = new ConcurrentLinkedQueue<ProductPrintQueueItem>();
		}
		
		return _printQueue;
	}
	
	
	private String getPrinterName() {
		if(getPrinterComboBox().getComponent().getSelectedItem()!= null)
			return getPrinterComboBox().getComponent().getSelectedItem().toString();
		else
			return null;
	}
	
	private LinkedHashMap<String,String> getSelectedPrintCandidates() {
		HashMap<String, String> productMap = new HashMap<String, String>();
		List<BaseProduct> fr = productTableModel.getSelectedItems();

		for (BaseProduct fra: fr) {
			productMap.put(fra.getProductId(), fra.getProductSpecCode());
		}
		LinkedHashMap<String, String> linkedMap = new LinkedHashMap(productMap);

		return  linkedMap;
	}
	
	private void checkFrameProduct(){
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		if(getJRadioButtonProductId().isSelected()){
			if(getJTextProductIdSearchTxt().getText()!= null)
				products =  frameDao.findAllBySN(getJTextProductIdSearchTxt().getText().trim());
				
			}
		else if(getJRadioButtonSeqNumber().isSelected()){
			if(getJTextSeqMin().getText().toString()!= null){
				int afOn = Integer.parseInt(getJTextSeqMin().getText().trim().toString());
				products = frameDao.findByAfOnSequenceNumber(afOn);
			}
		
		}
		else if(getJRadioButtonSeqNumRange().isSelected()){
			if(getJTextSeqMin().getText()!= null && getJTextSeqMax().getText()!= null)
				products = frameDao.findBySeqRange(getJTextSeqMin().getText().trim(), getJTextSeqMax().getText().trim());
		
		}
		else if(getJRadioButtonProductionLot().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = frameDao.findByProductionLot(getJTextSeqMin().getText().trim());
			
		}
		else if(getJRadioButtonTrackingStatus().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = frameDao.findByTrackingStatus(getJTextSeqMin().getText().trim());
		
		}
	}
	
	private void checkEngineProduct(){
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		if(getJRadioButtonProductId().isSelected()){
			if(getJTextProductIdSearchTxt().getText()!= null)
				products =  engineDao.findAllBySN(getJTextProductIdSearchTxt().getText().trim());
					
		}
		
		else if(getJRadioButtonProductionLot().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = engineDao.findAllByProductionLot(getJTextSeqMin().getText().trim());
			
		}
		else if(getJRadioButtonTrackingStatus().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = engineDao.findByTrackingStatus(getJTextSeqMin().getText().trim());
			
		}
	}
	
	private void checkSubProduct(){
		SubProductDao subProductDao = ServiceFactory.getDao(SubProductDao.class);
		if(getJRadioButtonProductId().isSelected()){
			if(getJTextProductIdSearchTxt().getText()!= null)
				products =  subProductDao.findAllBySN(getJTextProductIdSearchTxt().getText().trim());
					
		}
		
		else if(getJRadioButtonProductionLot().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = subProductDao.findAllByProductionLot(getJTextSeqMin().getText().trim());
			
		}
		else if(getJRadioButtonTrackingStatus().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = subProductDao.findByTrackingStatus(getJTextSeqMin().getText().trim());
			
		}
	}
	
	private void checkMbpnProduct(){
		MbpnProductDao mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		if(getJRadioButtonProductId().isSelected()){
			if(getJTextProductIdSearchTxt().getText()!= null)
				products =  mbpnProductDao.findAllBySN(getJTextProductIdSearchTxt().getText().trim());
					
		}
		
		else if(getJRadioButtonProductionLot().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = mbpnProductDao.findAllByOrderNo(getJTextSeqMin().getText().trim());
			
		}
		else if(getJRadioButtonTrackingStatus().isSelected()){
			if(getJTextSeqMin().getText()!= null)
				products = mbpnProductDao.findByTrackingStatus(getJTextSeqMin().getText().trim());
			
		}
	}
	
	private void getFrameTableModel(){
		productTableModel = new FrameTableModel((List<BaseProduct>) products, attributePane.getTable());
		checkFrameProduct();
		if(products!= null && products.size()>0) {
			productTableModel.refresh((List<BaseProduct>) products);
		}
		else {
			msg = "No Labels found";
			updateStatus(msg);
		}
	}
	
	private void getEngineTableModel(){
		productTableModel = new EngineTableModel((List<BaseProduct>) products, attributePane.getTable());
		checkEngineProduct();
		if(products!= null && products.size()>0) {
			productTableModel.refresh((List<BaseProduct>) products);
		}
		else {
			msg = "No Labels found";
			updateStatus(msg);
		}
	}
	
	private void getSubProductTableModel(){
		productTableModel = new SubProductTableModel2((List<BaseProduct>) products, attributePane.getTable());
		checkSubProduct();
		if(products!= null && products.size()>0) {
			productTableModel.refresh((List<BaseProduct>) products);
		}
		else {
			msg = "No Labels found";
			updateStatus(msg);
		}
	}
	
	
	
	private void getMbpnTableModel(){
		productTableModel = new MbpnTableModel((List<BaseProduct>) products, attributePane.getTable());
		checkMbpnProduct();
		if(products!= null && products.size()>0) {
			productTableModel.refresh((List<BaseProduct>) products);
		}
		else {
			msg = "No Labels found";
			updateStatus(msg);
		}
	}
	
	private void confirmProduct(){
		
		if(getProductTypeComboBox().getComponent().getSelectedItem() == ProductType.FRAME.toString()
			||getProductTypeComboBox().getComponent().getSelectedItem() == "PRODUCT"){
			getFrameTableModel();
		}
		else if(getProductTypeComboBox().getComponent().getSelectedItem() == ProductType.ENGINE.toString())
			getEngineTableModel();
		else if(getProductTypeComboBox().getComponent().getSelectedItem() == ProductType.IPU.toString()
				|| getProductTypeComboBox().getComponent().getSelectedItem() == ProductType.KNUCKLE.toString())
			getSubProductTableModel();
		else if(getProductTypeComboBox().getComponent().getSelectedItem() == ProductType.MBPN.toString())
			getMbpnTableModel();
		else{
			msg = "No Labels found";
			updateStatus(msg);
		}	
	}
	
	private  void showSearch(){
		getLogger().info("search button is clicked");
		String errorMessage = "";
		if(getJRadioButtonProductId().isSelected()) {
			if(StringUtils.isEmpty(getJTextProductIdSearchTxt().getText()) || getJTextProductIdSearchTxt().getText().trim().length() < getSearchMinLength()) {
				errorMessage = INVALID_PRODUCT_ID +": "+ getProductTypeComboBox().getComponent().getSelectedItem() + " search field must have at least " + getSearchMinLength() + " characters.";
			}
		} else if(getJRadioButtonSeqNumber().isSelected() 
					&& (StringUtils.isEmpty(getJTextSeqMin().getText()) || !StringUtils.isNumeric(getJTextSeqMin().getText()))) {
			errorMessage = INVALID_SEQUENCE_NUMBER;
		} else if(getJRadioButtonSeqNumRange().isSelected() 
					&& (StringUtils.isEmpty(getJTextSeqMin().getText()) || !StringUtils.isNumeric(getJTextSeqMin().getText()))) {
			errorMessage = INVALID_MIN_SEQUENCE_NUMBER;
		} else if(getJRadioButtonSeqNumRange().isSelected() 
					&& (StringUtils.isEmpty(getJTextSeqMax().getText()) || !StringUtils.isNumeric(getJTextSeqMax().getText()))) {
			errorMessage = INVALID_MAX_SEQUENCE_NUMBER;
		} else if(getJRadioButtonProductionLot().isSelected() && StringUtils.isEmpty(getJTextSeqMin().getText())) {
			errorMessage = INVALID_PRODUCTION_LOT;
		} else if(getJRadioButtonTrackingStatus().isSelected() && StringUtils.isEmpty(getJTextSeqMin().getText())) {
			errorMessage = INVALID_TRACKING_STATUS;
		}
		updateErrorStatus(errorMessage);

		if(errorMessage.length() == 0) {
			products = null;
			attributePane.clearSelection();
			productTableModel.sendPrintStatusList(null);
			productTableModel.refresh((List<BaseProduct>) products);
			products = new ArrayList<Product>();
			confirmProduct();
		}
	}
	

	private void showProductIdSearchText() {
		getJTextProductIdSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setVisible(false);
		getJTextSeqMax().setVisible(false);
		
		getJTextProductIdSearchTxt().setVisible(true);
		getJTextProductIdSearchTxt().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
	}
	
	private void showSeqSearchText() {
		jTextSeqMin.setBounds(170, 60, 240, 40);
		getJTextProductIdSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMax().setVisible(false);		
		getJTextProductIdSearchTxt().setVisible(false);
		
		getJTextSeqMin().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
	}
	
	private void showSequenceRangeOptions() {
		getJTextProductIdSearchTxt().setText("");
		getJTextProductIdSearchTxt().setVisible(false);
		jTextSeqMin.setBounds(170, 40, 240, 40);
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");		
		getJTextSeqMax().setVisible(true);
		getJTextSeqMax().setToolTipText("Max Value");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMin().setToolTipText("Min Value");
		getJTextSeqMin().requestFocus();
				
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
	}
	
	private void showProductionLotSearch(){
		if(getProductTypeComboBox().getComponent().getSelectedItem()!= null &&
				getProductTypeComboBox().getComponent().getSelectedItem() == "ENGINE"
					|| getProductTypeComboBox().getComponent().getSelectedItem() == "IPU"
						||getProductTypeComboBox().getComponent().getSelectedItem() == "KNUCKLE"){
			jTextSeqMin.setBounds(170, 90, 240, 40);
		}
		else
			jTextSeqMin.setBounds(170, 115, 240, 40);
		getJTextProductIdSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMax().setVisible(false);		
		getJTextProductIdSearchTxt().setVisible(false);
		
		getJTextSeqMin().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
	}
	
	private void showTrackingStatusSearch(){
		jTextSeqMin.setBounds(170, 140, 240, 40);
		getJTextProductIdSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMax().setVisible(false);		
		getJTextProductIdSearchTxt().setVisible(false);
		
		getJTextSeqMin().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
	}
	
	private void productTypeChanged(){
		String productName = getProductTypeComboBox().getComponent().getSelectedItem().toString().trim();
		getLogger().info(productName +" is selected");
		jRadioButtonProductId.setSelected(true);
		showProductIdSearchText();
		jTextProductIdSearchTxt.setText("");
		jTextSeqMin.setText("");
		jTextSeqMax.setText("");
		if(productName.equals(ProductType.FRAME.toString())){
			jRadioButtonSeqNumber.setVisible(true);
			jRadioButtonSeqNumRange.setVisible(true);
			jRadioButtonProductId.setBounds(30, 35, 120, 20);
			jRadioButtonTrackingStatus.setBounds(30, 155, 120, 20);
			jRadioButtonProductionLot.setBounds(30, 125, 120, 20);
		}
		else
		{
			jRadioButtonSeqNumber.setVisible(false);
			jRadioButtonSeqNumRange.setVisible(false);
			jRadioButtonProductId.setBounds(30, 35, 120, 40);
			jRadioButtonProductionLot.setBounds(30, 85, 120, 40);
			jRadioButtonTrackingStatus.setBounds(30, 140, 120, 40);
		}
	}
	
	private void printChanged(){
		String deviceName = getPrinterComboBox().getComponent().getSelectedItem().toString().trim();
		getLogger().info(deviceName +" is selected");
		IDevice device = DeviceManager.getInstance().getDevice(deviceName);
		if(device instanceof JasperDevice){
			duplexFlagComboBox.setVisible(true);
			trayComboBox.setVisible(true);
		} else{
			duplexFlagComboBox.setVisible(false);
			trayComboBox.setVisible(false);
		}
	}
	
	private LabeledComboBox getTrayComboBox() {
		List<String>printerList = new ArrayList<String>();
		printerList.add("1");
		printerList.add("2");
		printerList.add("0");
		if (trayComboBox == null) {
			trayComboBox = new LabeledComboBox("Tray");
			ComboBoxModel<String> trays = new ComboBoxModel<String>(printerList);
			trayComboBox.getComponent().setModel(trays);
			trayComboBox.getComponent().setSelectedIndex(-1);
			trayComboBox.getComponent().setRenderer(trays);
			trayComboBox.getComponent().setPreferredSize(new Dimension(50, 20));
			trayComboBox.setBounds(20, 465, 300, 40);
			trayComboBox.setVisible(false);
		} 
		return trayComboBox;
	}
	
	private LabeledComboBox getDuplexComboBox() {
		List<String>printerList = new ArrayList<String>();
		printerList.add("true");
		printerList.add("false");
		if (duplexFlagComboBox == null) {
			duplexFlagComboBox = new LabeledComboBox("DuplexFlag");
			ComboBoxModel<String> trays = new ComboBoxModel<String>(printerList);
			duplexFlagComboBox.getComponent().setModel(trays);
			duplexFlagComboBox.getComponent().setSelectedIndex(-1);
			duplexFlagComboBox.getComponent().setRenderer(trays);
			duplexFlagComboBox.getComponent().setPreferredSize(new Dimension(50, 20));
			duplexFlagComboBox.setBounds(20, 500, 300, 40);
			duplexFlagComboBox.setVisible(false);
		} 
		return duplexFlagComboBox;
	}
	
	private void formChanged() {
		List<String>printerList = new ArrayList<String>();
		products = null;
		jTextProductIdSearchTxt.setText("");
		jTextSeqMin.setText("");
		jTextSeqMax.setText("");
		
		if(getFormComboBox().getComponent().getSelectedItem()!= null){
			formId = getFormComboBox().getComponent().getSelectedItem().toString().trim();
			getLogger().info(formId +" is selected");
			msg= formId;
			updateStatus(msg);
			Hashtable<String, IDevice> devices = DeviceManager.getInstance().getDevices();
			Iterator<Entry<String, IDevice>> it = devices.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, IDevice> pairs = it.next();
				String availPrinters = pairs.getKey().toString();
				printerList.add(availPrinters);
			}
			 model = new ComboBoxModel<String>(printerList);
			 printerComboBox.getComponent().setModel(model);
			 getDuplexComboBox().getComponent().setSelectedIndex(-1);
			 getTrayComboBox().getComponent().setSelectedIndex(-1);
			 getDuplexComboBox().setVisible(false);
			 getTrayComboBox().setVisible(false);
			}
		else
			msg = "Form  not found";
				
	}
	
	private void updateStatus(String message) {
		getLogger().info(message);
		super.setMessage(message);
	}
	
	private void updateErrorStatus(String message) {
		getLogger().info(message);
		super.setErrorMessage(message);
	}
	
	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting()) {
			productTableModel.sendPrintStatusList(null);
			return;
		}
		if(e.getSource() == attributePane.getTable().getSelectionModel()) {
			
		if(productTableModel.getSelectedItems().size() > 0)
			getJButtonPrint().setEnabled(true);
		
		else
			getJButtonPrint().setEnabled(false);			
		}
	}
	
	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Reprint Label Panel is selected");
		
	}
	
	public String getId() {
		return "ReprintPanel";
	}
		
	public void handleStatusChange(PrintDeviceStatusInfo statusInfo) {
		updateStatus(statusInfo.getDisplayMessage());
	}

	public String getApplicationName() {
		return "ReprintPanel";
	}

	public Integer getDeviceAccessKey(String deviceId) {
		return _deviceAccessKey;
	}

	public void controlGranted(String deviceId) {
	}

	public void controlRevoked(String deviceId) {
	}

	protected int getSearchMinLength(){
		return PropertyService.getPropertyBean(ProductPropertyBean.class, ApplicationContext.getInstance().getApplicationId()).getProductSearchMinLength();
	}
	
	
}