package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.IAccessControlManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.FilteredLabeledComboBox;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledNumberSpinner;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.client.ui.component.SortableObjectTableModel;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.TemplateDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.IPrintDevice;
import com.honda.galc.device.events.PrintDeviceStatusInfo;
import com.honda.galc.device.printer.AbstractPrintDevice;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.DestinationType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.Template;
import com.honda.galc.property.BroadcastReprintPropertyBean;
import com.honda.galc.property.ProductPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.teamleader.enumtype.BlockColumn;
import com.honda.galc.teamleader.enumtype.Column;
import com.honda.galc.teamleader.enumtype.ConrodColumn;
import com.honda.galc.teamleader.enumtype.CrankshaftColumn;
import com.honda.galc.teamleader.enumtype.DieCastColumn;
import com.honda.galc.teamleader.enumtype.EngineColumn;
import com.honda.galc.teamleader.enumtype.FrameColumn;
import com.honda.galc.teamleader.enumtype.HeadColumn;
import com.honda.galc.teamleader.enumtype.MbpnProductColumn;
import com.honda.galc.teamleader.enumtype.ProductColumn;
import com.honda.galc.util.ReflectionUtils;
import net.miginfocom.swing.MigLayout;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Dec 13, 2016
 */
public class BroadcastReprintPanel extends TabbedPanel implements ActionListener, ListSelectionListener {
	
	private static final long serialVersionUID = 1L;
	private String[] productHeadings;
	private String[] productGetters;
	private Map <String,Column> columnMap;
	private static final String[] PRODUCT_COLUMNS =	{"PRODUCT_ID","PRODUCT_SPEC_CODE","PRODUCTION_LOT","TRACKING_STATUS"};
	private static final String[] FRAME_COLUMNS = {"PRODUCT_ID","AF_ON_SEQUENCE_NUMBER","ENGINE_SERIAL_NO"};
	private static final String[] ENGINE_COLUMNS = {"PRODUCT_ID","VIN"};
	
	private static final String[] DIE_CAST_COLUMNS = {"PRODUCT_ID","DC_SERIAL_NUMBER","MC_SERIAL_NUMBER","ENGINE_SERIAL_NUMBER","TRACKING_STATUS"};
	private static final String[] BLOCK_COLUMNS = {"PRODUCT_ID"};
	private static final String[] CONROD_COLUMNS = {"PRODUCT_ID"};
	private static final String[] CRANKSHAFT_COLUMNS = {"PRODUCT_ID"};
	private static final String[] HEAD_COLUMNS = {"PRODUCT_ID"};
	
	private static final String[] MBPN_COLUMNS = {"PRODUCT_ID","CURRENT_PRODUCT_SPEC_CODE","TRACKING_STATUS"};

	private static final String[] PRINTER_HEADINGS = {"Process Point", "Destination ID", "Request ID", "Argument"};
	private static final String[] PRINTER_GETTERS = {"getProcessPointId", "getDestinationId", "getRequestId", "getArgument"};
	private static final String VISIBLE_BROADCAST_DESTINATION_TYPES = "VISIBLE_BROADCAST_DESTINATION_TYPES";
	
	private LabeledComboBox productTypeComboBox;
	private LabeledComboBox divisionComboBox;
	private LabeledComboBox planCodeComboBox;
	private LabeledComboBox broadcastDestinationTypeComboBox;
	private FilteredLabeledComboBox productionLotComboBox;
	private LabeledComboBox trayComboBox;
	private LabeledTextField destinationFilterTextField;
	private LabeledTextField limitTextField;
	private JRadioButton jRadioButtonVIN,jRadioButtonSeqNumber,jRadioButtonSeqNumRange,jRadioButtonProductionLot,jRadioButtonTrackingStatus,jRadioButtonLastProcPoint,jRadioButtonNotShipped,jRadioButtonShipped = null;
	public List<? extends BaseProduct> products;
	private List<Line> lines;
	private String msg;
	private JPanel jPaneSearchOptionRadioBtn = null;
	private JTextField jTextVINSearchTxt = null;
	private JTextField jTextSeqMin = null;
	private JTextField jTextSeqMax = null;
	private JPanel procPointSearchPanel;
	private JPanel prodLotPanel;
	private JButton jButtonSearch,jButtonPrint = null;
	private ObjectTablePane<MultiValueObject<BroadcastDestination>> printerTablePane;
	private ObjectTablePane<MultiValueObject<BaseProduct>> productTablePane;
	private ObjectTablePane<ProcessPoint> processPointTablePane;
	private List<BroadcastDestination> broadcastDestinations;
	private List<MultiValueObject<BroadcastDestination>> broadcastPrinters;
	private String[] additionalBroadcastDestinationIds = null;
	private LabeledComboBox lineComboBox;
	private LabeledComboBox formFeedPrintersComboBox;
	private LabeledNumberSpinner numberOfCopiesSpinner;
	private JButton jButtonFormFeed;
	private BroadcastReprintPropertyBean broadcastReprintPropertyBean;
	private BroadcastReprintPropertyBean bReprintPropertyBean;
	private Map<String, String> formFeedDevices;
	private Map<String, String> formFeedTemplates;
	private Map<String, String> linesMap = new HashMap<String, String>();
	private Map<String, String> productTypeByProcessPointIdIx = new HashMap<String, String>();
	private String[] shippingLines;
	private Map<String, List<Integer>> traysByDestMap;
	
	private AtomicInteger actionCounter = new AtomicInteger(0);
	
	private IAccessControlManager accessControlManager;
	
	private static final String TRAY_VALUE = "TRAY_VALUE";
	private static final String PROD_LOT_ROW_LIMIT = "400";
	private static final String DEFAULT_TRAY_LIST = "DEFAULT_TRAY_LIST";
	private static final String TRAY_SIZE_MATCH_ERR = "Some of the selected printers have different number of trays. Please change your selection.";

	public BroadcastReprintPanel(TabbedMainWindow mainWindow) {
		super("Rebroadcast", KeyEvent.VK_G, mainWindow);
		initComponents();
		addListeners();
		productChanged();
	}
	
	protected void initComponents() {
		setLayout(new BorderLayout());
		SystemPropertyBean property = PropertyService.getPropertyBean(SystemPropertyBean.class, ApplicationContext.getInstance().getApplicationId());
		shippingLines = property.getProductSearchShippedLineIds();
		broadcastReprintPropertyBean=PropertyService.getPropertyBean(BroadcastReprintPropertyBean.class,getApplicationId());
		bReprintPropertyBean=PropertyService.getPropertyBean(BroadcastReprintPropertyBean.class,getMainWindow().getApplicationContext().getApplicationId());
		accessControlManager = ClientMain.getInstance().getAccessControlManager();
		add(createMainPanel(), BorderLayout.CENTER);
		this.getLineData();
	}

	private void addListeners() {
		productTablePane.getTable().getSelectionModel().addListSelectionListener(this);
		printerTablePane.addListSelectionListener(this);
		getProductComboBox().getComponent().addActionListener(this);
		getDivisionComboBox().getComponent().addActionListener(this);
		this.getProcPointTablePane().addListSelectionListener(this);
		getBDTypeComboBox().getComponent().addActionListener(this);
		getDestinationFilterTextField().getComponent().getDocument().addDocumentListener(createDestinationFilterListener());
		getJButtonPrint().addActionListener(this);
		getJRadioButtonVIN().addActionListener(this);
		getJRadioButtonSeqNumber().addActionListener(this);
		getJRadioButtonSeqNumRange().addActionListener(this);
		getJRadioButtonProductionLot().addActionListener(this);
		getJRadioButtonTrackingStatus().addActionListener(this);
		getJRadioButtonLastProcPoint().addActionListener(this);
		getJButtonSearch().addActionListener(this);
		getPlanCodeComboBox().getComponent().addActionListener(this);
		getJButtonFormFeed().addActionListener(this);
		getJRadioButtonNotShipped().addActionListener(this);
		getJRadioButtonShipped().addActionListener(this);
	}

	private JSplitPane createMainPanel() {
		JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		mainPanel.setContinuousLayout(true);
		mainPanel.setResizeWeight(0.6);
		
		mainPanel.add(createTopPanel(), JSplitPane.TOP);
		mainPanel.add(createBottomPanel(), JSplitPane.BOTTOM);
		
		return mainPanel;
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2));

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.add(getProductComboBox(), getProductComboBox().getName());
		leftPanel.add(topPanel, BorderLayout.NORTH);
		leftPanel.add(getSearchOptionPanel(), BorderLayout.CENTER);
		panel.add(leftPanel);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(createProductTablePane(), BorderLayout.CENTER);
		panel.add(rightPanel);

		return panel;
	}
	
	private JPanel createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(createTypePanel(), BorderLayout.NORTH);
		bottomPanel.add(createPrinterTablePane(), BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,30,0));
		buttonPanel.add(getFormFeedPrintersComboBox(), getFormFeedPrintersComboBox().getName());
		buttonPanel.add(getNumberOfCopiesSpinner(), getNumberOfCopiesSpinner().getName());
		buttonPanel.add(getJButtonFormFeed(), getJButtonFormFeed().getName());
		buttonPanel.add(getJButtonPrint(), getJButtonPrint().getName());
		bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

		return bottomPanel;
	}
	
	private JPanel createTypePanel() {
		JPanel typePanel = new JPanel(new MigLayout());
		typePanel.add(getBDTypeComboBox());
		typePanel.add(this.getTrayComboBox());
		typePanel.add(getDestinationFilterTextField());
		return typePanel;
	}
	
	private DocumentListener createDestinationFilterListener() {
		DocumentListener listener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {  
				onDestinationFilterChange();
			}
			public void removeUpdate(DocumentEvent e) { 
				onDestinationFilterChange(); 
			}
			public void changedUpdate(DocumentEvent e) { 
				onDestinationFilterChange();
			}
		};
		return listener;
	}
	
	private void onDestinationFilterChange() {
		printerTablePane.clearSelection();
		triggerFilterDestinations();
	}
	
	private ObjectTablePane<MultiValueObject<BaseProduct>> createProductTablePane() {
		this.loadColumnMap();
		productTablePane = new ObjectTablePane<MultiValueObject<BaseProduct>> ("Product", this.getColumnMappings().get(),false,true);
		productTablePane.getTable().setName("ProductTable");
		productTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);		
		return productTablePane;
	}
	
	private ColumnMappings getColumnMappings() {
		Map<String, String> map = getColumnNameGetterMap();
		this.productHeadings = map.keySet().toArray(new String[0]);
		this.productGetters = map.values().toArray(new String[0]);
		return ColumnMappings.with(this.productHeadings, this.productGetters);
	}
	
	private Map<String,String> getColumnNameGetterMap(){
		Map<String, String> columnNameGetterMap = new LinkedHashMap<String, String>();
		Map<String, Column> columnMapFromProperies = this.getColumnMapFromProperies();
		Map<String, Column> defaultColumnMap = this.getDefaultColumnMap();
		for (Column column : columnMapFromProperies.values()) {
			if (defaultColumnMap.containsKey(column.toString())) defaultColumnMap.remove(column.toString());
			columnNameGetterMap.put(column.getColumnName(), column.getColumnGetter());
		}
		for (Column column : defaultColumnMap.values()) {
			columnNameGetterMap.put(column.getColumnName(), column.getColumnGetter());
		}
		return columnNameGetterMap;
	}
	
	private Map<String, Column> getDefaultColumnMap() {
		Map<String, Column> columnMap = new LinkedHashMap<String, Column>();
		ProductType productType = ProductType.getType(this.getSelectedProductType());
		if (ProductTypeUtil.isMbpnProduct(productType)) {
			columnMap = this.getFilteredColumnMap(MBPN_COLUMNS);
		} else if (ProductTypeUtil.isDieCast(productType)) {
			switch(productType) {
				case BLOCK :
					columnMap = this.getFilteredColumnMap(BLOCK_COLUMNS);
					break;
				case CONROD :
					columnMap = this.getFilteredColumnMap(CONROD_COLUMNS);
					break;
				case CRANKSHAFT :
					columnMap = this.getFilteredColumnMap(CRANKSHAFT_COLUMNS);
					break;
				case HEAD :
					columnMap = this.getFilteredColumnMap(HEAD_COLUMNS);
					break;
				default :
			}
			for (Column column : this.getFilteredColumnMap(DIE_CAST_COLUMNS).values()) {
				if (columnMap.containsKey(column.toString())) continue;
				columnMap.put(column.toString(), column);
			}
		} else {
			switch(productType) {
				case FRAME : 
					columnMap = this.getFilteredColumnMap(FRAME_COLUMNS);
					break;
				case ENGINE : 
					columnMap = this.getFilteredColumnMap(ENGINE_COLUMNS);
					break;
				case MISSION :
				default :
			}
			for (Column column : this.getFilteredColumnMap(PRODUCT_COLUMNS).values()) {
				if (columnMap.containsKey(column.toString())) continue;
				columnMap.put(column.toString(), column);
			}
		}
		return columnMap;
	}
	
	private Map<String, Column> getColumnMapFromProperies() {
		Map<String, Column> columnMap = new LinkedHashMap<String, Column>();
		Map<String, String> columnProperies = new HashMap<String, String>();
		
		if (this.jRadioButtonLastProcPoint.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchByLastProcPointColumns(String.class);
		} else if (this.jRadioButtonProductionLot.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchByProdLotColumns(String.class);
		} else if (this.jRadioButtonSeqNumber.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchBySeqNumberColumns(String.class);
		} else if (this.jRadioButtonSeqNumRange.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchBySeqNumRangeColumns(String.class);
		} else if (this.jRadioButtonTrackingStatus.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchByTrackingStatusColumns(String.class);
		} else if (this.jRadioButtonVIN.isSelected()) {
			columnProperies = this.broadcastReprintPropertyBean.isSearchByProductIdColumns(String.class);
		}
		if (columnProperies == null || columnProperies.isEmpty() || !columnProperies.containsKey(this.getSelectedProductType())) return columnMap;
		
		String[] columns = columnProperies.get(this.getSelectedProductType()).split(","); 
		columnMap = getFilteredColumnMap(columns);
		return columnMap;
	}
	
	private Map<String, Column> getFilteredColumnMap(String[] columnNames){
		Map<String, Column> filteredColumnMap = new LinkedHashMap<String, Column>();
		for (String columnName : columnNames) {
			columnName = columnName.trim();
			if (!this.columnMap.containsKey(columnName)) continue;
			filteredColumnMap.put(columnName, this.columnMap.get(columnName));
		}
		return filteredColumnMap;
	}
	
	private void loadColumnMap() {
		this.columnMap = new LinkedHashMap<String, Column>();
		ArrayList<Column> columns = new ArrayList<Column>();
		ProductType productType = ProductType.getType(this.getSelectedProductType());
		if (ProductTypeUtil.isMbpnProduct(productType)) {
			columns.addAll(Arrays.asList(MbpnProductColumn.values()));
		} else if (ProductTypeUtil.isDieCast(productType)) {
			switch(productType) {
				case BLOCK :
					columns.addAll(Arrays.asList(BlockColumn.values()));
					break;
				case CONROD :
					columns.addAll(Arrays.asList(ConrodColumn.values()));
					break;
				case CRANKSHAFT :
					columns.addAll(Arrays.asList(CrankshaftColumn.values()));
					break;
				case HEAD :
					columns.addAll(Arrays.asList(HeadColumn.values()));
					break;
				default :
			}
			columns.addAll(Arrays.asList(DieCastColumn.values()));
		} else {
			switch(productType) {
				case FRAME : 
					columns.addAll(Arrays.asList(FrameColumn.values()));
					break;
				case ENGINE : 
					columns.addAll(Arrays.asList(EngineColumn.values()));
					break;
				default :
			}
			columns.addAll(Arrays.asList(ProductColumn.values()));
		}
		
		for (Column column : columns) {
			if (this.columnMap.containsKey(column.toString())) continue;
			this.columnMap.put(column.toString(), column);
		}
	}


	private ObjectTablePane<MultiValueObject<BroadcastDestination>> createPrinterTablePane() {
		ColumnMappings columnMappings = ColumnMappings.with(PRINTER_HEADINGS, PRINTER_GETTERS);
		printerTablePane = new ObjectTablePane<MultiValueObject<BroadcastDestination>> ("", columnMappings.get(), true, true);
		printerTablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		printerTablePane.getTable().setName("PrinterTable");
		printerTablePane.reloadData(getBroadcastPrinters());
		return printerTablePane;
	}
	
	
	private JButton getJButtonPrint() {
		if (jButtonPrint == null) {
			try {
				jButtonPrint = new javax.swing.JButton();
				jButtonPrint.setName("JButtonPrint");
				jButtonPrint.setFont(Fonts.DIALOG_BOLD_12);
				jButtonPrint.setText("Print");
				jButtonPrint.setEnabled(false);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jButtonPrint;
	}
	
	private LabeledComboBox getFormFeedPrintersComboBox() {
		if (formFeedPrintersComboBox == null) {
			formFeedPrintersComboBox = new LabeledComboBox("Printers");
			formFeedPrintersComboBox.setFont(Fonts.DIALOG_BOLD_14);
			formFeedPrintersComboBox.getComponent().setPreferredSize(new Dimension(150,25));
			formFeedDevices=broadcastReprintPropertyBean.getFormFeedDevice();
			formFeedTemplates=broadcastReprintPropertyBean.getFormFeedTemplate();
			if(formFeedDevices!=null)
			{
				for(Entry<String, String> entry : formFeedDevices.entrySet()) {
					formFeedPrintersComboBox.getComponent().addItem(entry.getValue());				
				}
			}
		}
		return formFeedPrintersComboBox;
	}

	private JButton getJButtonFormFeed() {
		if (jButtonFormFeed == null) {

			jButtonFormFeed = new javax.swing.JButton();
			jButtonFormFeed.setName("jButtonFormFeed");
			jButtonFormFeed.setFont(Fonts.DIALOG_BOLD_12);
			jButtonFormFeed.setText("Form Feed");
			jButtonFormFeed.setEnabled(true);
		}
		return jButtonFormFeed;
	}

	private LabeledNumberSpinner getNumberOfCopiesSpinner(){
		if(numberOfCopiesSpinner==null)
		{
			numberOfCopiesSpinner = new LabeledNumberSpinner("Number of copies", true,1,25);
			numberOfCopiesSpinner.setFont(Fonts.DIALOG_BOLD_14);
			numberOfCopiesSpinner.setEnabled(true);
			numberOfCopiesSpinner.setVisible(true);
		}
		return numberOfCopiesSpinner;

	}

	private JPanel getSearchOptionPanel(){
		if (jPaneSearchOptionRadioBtn == null) {
			try {
				ButtonGroup radioBtnGroupSearchOptions = new ButtonGroup();
				radioBtnGroupSearchOptions.add(getJRadioButtonVIN());
				radioBtnGroupSearchOptions.add(getJRadioButtonSeqNumber());
				radioBtnGroupSearchOptions.add(getJRadioButtonSeqNumRange());
				radioBtnGroupSearchOptions.add(getJRadioButtonProductionLot());
				radioBtnGroupSearchOptions.add(getJRadioButtonTrackingStatus());
				radioBtnGroupSearchOptions.add(getJRadioButtonLastProcPoint());
				radioBtnGroupSearchOptions.setSelected(this.getJRadioButtonVIN().getModel(), true);

				jPaneSearchOptionRadioBtn = new javax.swing.JPanel();
				jPaneSearchOptionRadioBtn.setLayout(null);
				jPaneSearchOptionRadioBtn.add(getJRadioButtonVIN());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonSeqNumber());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonSeqNumRange());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonProductionLot());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonTrackingStatus());
				jPaneSearchOptionRadioBtn.add(getJRadioButtonLastProcPoint());
				jPaneSearchOptionRadioBtn.add(getJTextVINSearchTxt());
				jPaneSearchOptionRadioBtn.add(getJTextSeqMin());
				jPaneSearchOptionRadioBtn.add(getJTextSeqMax());
				jPaneSearchOptionRadioBtn.add(getProcessPointSearchPanel());
				jPaneSearchOptionRadioBtn.add(getLineComboBox());
				jPaneSearchOptionRadioBtn.add(getProdLotPanel());
				jPaneSearchOptionRadioBtn.add(getJButtonSearch());

				//Ship option
				if(shippingLines != null && shippingLines.length > 0){
					ButtonGroup radioBtnSearchOptions = new ButtonGroup();
					radioBtnSearchOptions.add(getJRadioButtonNotShipped());
					radioBtnSearchOptions.add(getJRadioButtonShipped());
					radioBtnSearchOptions.setSelected(this.getJRadioButtonNotShipped().getModel(), true);
					
					jPaneSearchOptionRadioBtn.add(getJRadioButtonNotShipped());
					jPaneSearchOptionRadioBtn.add(getJRadioButtonShipped());
				}
				TitledBorder radioButtonZone = new TitledBorder(new LineBorder(Color.BLACK, 1), "Search option: ");
				radioButtonZone.setTitleFont(Fonts.DIALOG_BOLD_14);
				radioButtonZone.setTitlePosition(TitledBorder.CENTER);
				radioButtonZone.setTitleJustification(TitledBorder.LEFT);
				jPaneSearchOptionRadioBtn.setBorder(radioButtonZone);
				jPaneSearchOptionRadioBtn.setBounds(20, 10, 420, 240);

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
				jTextSeqMax.setBounds(420, 100, 100, 40);
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
				jButtonSearch.setBounds(30, 200, 100, 30);
				
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jButtonSearch;
	}
	
	private JRadioButton getJRadioButtonNotShipped() {
		if (jRadioButtonNotShipped == null) {
			try {
				jRadioButtonNotShipped = new javax.swing.JRadioButton();
				jRadioButtonNotShipped.setName("JRadioButtonNotShipped");
				jRadioButtonNotShipped.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonNotShipped.setText("Not Shipped");
				jRadioButtonNotShipped.setBounds(30, 250, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonNotShipped;
	}
	
	private JRadioButton getJRadioButtonShipped() {
		if (jRadioButtonShipped == null) {
			try {
				jRadioButtonShipped = new javax.swing.JRadioButton();
				jRadioButtonShipped.setName("JRadioButtonShipped");
				jRadioButtonShipped.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonShipped.setText("Shipped");
				jRadioButtonShipped.setBounds(200, 250, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonShipped;
	}
	
	private JTextField getJTextVINSearchTxt() {
		if (jTextVINSearchTxt == null) {
			try {
				jTextVINSearchTxt = new javax.swing.JTextField();
				jTextVINSearchTxt.setName("JTextVINSearchTxt");
				jTextVINSearchTxt.setFont(Fonts.DIALOG_PLAIN_24);
				jTextVINSearchTxt.setText("");
				jTextVINSearchTxt.setVisible(true);
				jTextVINSearchTxt.setBounds(170, 15, 240, 30);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jTextVINSearchTxt;
	}
	
	private JPanel getProcessPointSearchPanel() {
		if (this.procPointSearchPanel == null) {
			this.procPointSearchPanel = new JPanel(new MigLayout());
			this.procPointSearchPanel.add(this.getDivisionComboBox(),"wrap");
			this.procPointSearchPanel.add(this.getProcPointTablePane(),"wrap");
			this.procPointSearchPanel.setVisible(false);
			this.procPointSearchPanel.setBounds(170, 10, 350, 230);
		}
		return this.procPointSearchPanel;
	}
	
	private ObjectTablePane<ProcessPoint> getProcPointTablePane() {
		if (this.processPointTablePane == null) {
			PropertiesMapping mapping = new PropertiesMapping();
			mapping.put("ID", "processPointId");
			mapping.put("Name", "processPointName");
			this.processPointTablePane = new ObjectTablePane<ProcessPoint>(mapping.get(), true, true);
			this.processPointTablePane.getTable().setName("ProcessPointTable");
			this.processPointTablePane.setBorder(new TitledBorder("Process Points"));
			this.processPointTablePane.getTable().setRowSelectionAllowed(true);
		}
		return this.processPointTablePane;
	}
	
	private LabeledComboBox getDivisionComboBox() {
		if (this.divisionComboBox == null) {
			this.divisionComboBox = new LabeledComboBox("Division");
			List<String> divisions = this.getDivisionData();
			ComboBoxModel<String> model = new  ComboBoxModel<String>(divisions);
	        this.divisionComboBox.getComponent().setName("DivisionComboBox");
	        this.divisionComboBox.getComponent().setModel(model);
	        this.divisionComboBox.getComponent().setRenderer(model);
	        this.divisionComboBox.getComponent().setSelectedIndex(-1);
		}
		return divisionComboBox;
	}
	
	private void reloadProcessPoints() {
		String divisionId = ((String) this.divisionComboBox.getComponent().getSelectedItem()).trim();
		divisionId = divisionId.trim().split("-")[1].trim();
		ArrayList<ProcessPoint> procPointList = (ArrayList<ProcessPoint>) ServiceFactory.getDao(ProcessPointDao.class).findAllByDivisionId(divisionId);
		this.processPointTablePane.reloadData(procPointList);
		this.processPointTablePane.clearSelection();
	}
	
	private JRadioButton getJRadioButtonVIN() {
		if (jRadioButtonVIN == null) {
			try {
				jRadioButtonVIN = new javax.swing.JRadioButton();
				jRadioButtonVIN.setName("JRadioButtonVIN");
				jRadioButtonVIN.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonVIN.setText("Product ID ");
				jRadioButtonVIN.setBounds(30, 20, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonVIN;
	}
	
	private JRadioButton getJRadioButtonSeqNumber() {
		if (jRadioButtonSeqNumber == null) {
			try {
				jRadioButtonSeqNumber = new javax.swing.JRadioButton();
				jRadioButtonSeqNumber.setName("JRadioButtonSeqNumber");
				jRadioButtonSeqNumber.setFont(Fonts.DIALOG_BOLD_12);
				jRadioButtonSeqNumber.setText("SEQ Number ");
				jRadioButtonSeqNumber.setBounds(30, 45, 120, 20);
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
				jRadioButtonSeqNumRange.setBounds(30, 70, 120, 20);
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
				jRadioButtonProductionLot.setBounds(30, 95, 120, 20);
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
				jRadioButtonTrackingStatus.setBounds(30, 120, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return jRadioButtonTrackingStatus;
	}
	
	private JRadioButton getJRadioButtonLastProcPoint() {
		if (this.jRadioButtonLastProcPoint == null) {
			try {
				this.jRadioButtonLastProcPoint = new javax.swing.JRadioButton();
				this.jRadioButtonLastProcPoint.setName("JRadioButtonLastProcPoint");
				this.jRadioButtonLastProcPoint.setFont(Fonts.DIALOG_BOLD_12);
				this.jRadioButtonLastProcPoint.setText("Last Proc Point");
				this.jRadioButtonLastProcPoint.setBounds(30, 145, 120, 20);
			} catch (java.lang.Throwable ivjExc) {
				
			}
		}
		return this.jRadioButtonLastProcPoint;
	}

	private LabeledComboBox getProductComboBox() {
		if (productTypeComboBox == null) {
			List<String> prodTypes = new ArrayList<String>();
			String applicableProductTypes = broadcastReprintPropertyBean.getApplicableProductTypes();
			if(StringUtils.isNotEmpty(applicableProductTypes)) {
				String[] types = applicableProductTypes.split(",");
				for(int i = 0; i < types.length; i++) {
					prodTypes.add(ProductType.getType(types[i]).name());
				}
			}
			
			if(prodTypes.size() == 0) {
				for(String type : ProductType.getProductTypeNames().keySet()) {
					prodTypes.add(type);
				}
			}

			productTypeComboBox = new LabeledComboBox("Product");
			productTypeComboBox.getComponent().setName("ProductComboBox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(prodTypes);
			productTypeComboBox.getComponent().setModel(model);
			productTypeComboBox.getComponent().setSelectedIndex(-1);
			productTypeComboBox.getComponent().setRenderer(model);
			productTypeComboBox.setBounds(290, 25, 260, 50);
			model.setSelectedItem(prodTypes.get(0));
		}
		return productTypeComboBox;
	}
	
	private String getSelectedProductType() {
		return getProductComboBox().getComponent().getSelectedItem().toString().trim();
	}
	
	private LabeledComboBox getBDTypeComboBox() {
		if (broadcastDestinationTypeComboBox == null) {
			List<String> types = getVisibleBDTypes();
			
			broadcastDestinationTypeComboBox = new LabeledComboBox("Type");
			broadcastDestinationTypeComboBox.getComponent().setName("BroadcastDestinationTypeComboBox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(types);
			broadcastDestinationTypeComboBox.getComponent().setModel(model);
			broadcastDestinationTypeComboBox.getComponent().setSelectedIndex(-1);
			broadcastDestinationTypeComboBox.getComponent().setRenderer(model);
			broadcastDestinationTypeComboBox.setBounds(290, 25, 260, 50);
			model.setSelectedItem(types.get(0));
		}
		return broadcastDestinationTypeComboBox;
	}
	
	private Map<String, List<Integer>> getTrayValuesByDestMap() {
		if (traysByDestMap == null) {
			traysByDestMap = new HashMap<String, List<Integer>>();
			traysByDestMap.put(DEFAULT_TRAY_LIST, Arrays.asList(1, 2, 3));

			Map<String, String> traysByDestinationIdMap = this.broadcastReprintPropertyBean.getTraysByBroadcastIdMap(String.class);
			if (traysByDestinationIdMap == null)
				return traysByDestMap;

			for (String destinationId : traysByDestinationIdMap.keySet()) {
				List<Integer> trayValueList = new ArrayList<Integer>();
				String trayValues = traysByDestinationIdMap.get(destinationId);
				if (StringUtils.isBlank(trayValues)) {
					getLogger().error("Tray values for destination ID " + destinationId + " are not set. "
							+ "Default values will be used. Please check TRAYS_BY_BROADCAST_ID property settings.");
					continue;
				}
				try {
					for (String trayValue : traysByDestinationIdMap.get(destinationId).split(","))
						trayValueList.add(Integer.parseInt(trayValue));
				} catch (NumberFormatException ex) {
					getLogger().error("Tray values for destination ID " + destinationId
							+ " contain invalid characters. "
							+ "Default values will be used. Please check TRAYS_BY_BROADCAST_ID property settings.");
					continue;
				}
				traysByDestMap.put(destinationId, trayValueList);
			}
		}
		return traysByDestMap;
	}

	private LabeledComboBox getTrayComboBox() {
		if (trayComboBox == null) {
			trayComboBox = new LabeledComboBox("Tray");
			trayComboBox.getComponent().setPreferredSize(new Dimension(50, 20));
			trayComboBox.setBounds(20, 465, 300, 40);
			trayComboBox.setVisible(true);
			trayComboBox.setEnabled(false);
		} 
		return trayComboBox;
	}
	
	private LabeledTextField getDestinationFilterTextField() {
		if (destinationFilterTextField == null) {
			destinationFilterTextField = new LabeledTextField("Destination ID Filter");
			destinationFilterTextField.getComponent().setBounds(50, 465, 500, 50);
			destinationFilterTextField.getComponent().setDocument(new UpperCaseDocument(32));
			destinationFilterTextField.getComponent().setFont(Fonts.DIALOG_BOLD_12);
		}
		return destinationFilterTextField;
	}
	
	private LabeledComboBox getLineComboBox() {
		if (lineComboBox == null) {
			List<String> linesData = getLineData();
			
			lineComboBox = new LabeledComboBox("Line");
			lineComboBox.getComponent().setName("lineComboBox");
			ComboBoxModel<String> model = new ComboBoxModel<String>(linesData);
			lineComboBox.getComponent().setModel(model);
			lineComboBox.setBounds(170, 70, 250, 50);
			String defaultSeqLine = broadcastReprintPropertyBean.getDefaultSeqLine();
			if(!StringUtils.isEmpty(defaultSeqLine))
			{
				model.setSelectedItem(defaultSeqLine);
			}else
			{
    			model.setSelectedItem(linesData.get(0));
			}
			lineComboBox.setVisible(false);
		}
		return lineComboBox;
	}
	
	private List<String> getLineData() {
		ArrayList<String> result = new ArrayList<String>();
		linesMap = new HashMap<String, String>();
		this.lines = ServiceFactory.getDao(LineDao.class).findAll();
		if (this.lines != null && !this.lines.isEmpty()) {
			TreeSet<String> lineNames = new TreeSet<String>();
			for(Line line : this.lines) {
				lineNames.add(line.getLineName() + " - " + line.getLineId());
				linesMap.put(line.getLineId().trim(), line.getLineName().trim());
			}
			result.add(""); 
			result.addAll(lineNames);
		}
		return result;
	}

	private List<String> getDivisionData() {
		ArrayList<String> result = new ArrayList<String>();
		List<Division> divisionList = ServiceFactory.getDao(DivisionDao.class).findAll();
		
		if (divisionList != null && !divisionList.isEmpty()) {
			TreeSet<String> divisionNames = new TreeSet<String>();
			for(Division division : divisionList)
				divisionNames.add(division.getDivisionName().trim() + " - " + division.getDivisionId().trim());
			result.add(""); 
			result.addAll(divisionNames);
		}
		return result;
	}	

	private LabeledComboBox getPlanCodeComboBox() {
		if (this.planCodeComboBox == null) {
			this.planCodeComboBox = new LabeledComboBox("Plan Code");
			List<String> processLocationsList = ServiceFactory.getDao(PreProductionLotDao.class).getAllPlanCodes();
			processLocationsList.add(0,"Select");
			ComboBoxModel<String> model = new  ComboBoxModel<String>(processLocationsList);
	        this.planCodeComboBox.getComponent().setName("PlanCodeComboBox");
	        this.planCodeComboBox.getComponent().setModel(model);
	        this.planCodeComboBox.getComponent().setRenderer(model);
	        this.planCodeComboBox.getComponent().setSelectedIndex(-1);
		}
		return planCodeComboBox;
	}

	private JPanel getProdLotPanel() {
		if (this.prodLotPanel == null) {
			this.prodLotPanel = new JPanel(new MigLayout());
			this.prodLotPanel.add(this.getPlanCodeComboBox());
			this.prodLotPanel.add(this.getLimitTextField(),"width 111:111:111, wrap");
			this.prodLotPanel.add(this.getProdLotComboBox(),"width 300:300:300, spanx 2");
			this.prodLotPanel.setVisible(false);
			this.prodLotPanel.setBounds(170, 15, 450, 160);
		}
		return this.prodLotPanel;
	}

	private FilteredLabeledComboBox getProdLotComboBox() {
		
		if (this.productionLotComboBox == null) {
			this.productionLotComboBox = new FilteredLabeledComboBox("Prod Lot: ");
			this.productionLotComboBox.setPreferredWidth(700);
			this.productionLotComboBox.setMaxWidth(700);
			this.productionLotComboBox.getComponent().setName("productionLotComboBox");
			List<String> prodLotList = new ArrayList<String>();
			ComboBoxModel<String> model = new ComboBoxModel<String>(prodLotList);
			this.productionLotComboBox.getComponent().setModel(model);
			this.productionLotComboBox.setSelectedIndex(-1);
        }
        return this.productionLotComboBox;
	}
	
	private LabeledTextField getLimitTextField() {
		if (this.limitTextField == null) {
			this.limitTextField = new LabeledTextField("# of lots:",true);
			this.limitTextField.getLabel().setHorizontalAlignment(SwingConstants.CENTER);
			((BorderLayout)this.limitTextField.getLayout()).setVgap(0);
			this.limitTextField.getComponent().setText(PROD_LOT_ROW_LIMIT);
			this.limitTextField.getComponent().addKeyListener(new KeyAdapter() {
	    		@Override
	    	    public void keyTyped(KeyEvent e) { 
	    	        if (limitTextField.getComponent().getText().length() >= 7)
	    	            e.consume(); 
	    	    }  
	    	});
		}
		return this.limitTextField;
	}
	
	private void refreshProdLotComboBox() {
		String limit = this.getLimitTextField().getComponent().getText().trim();
		int numOfRecords = 0;
		try {
			numOfRecords = Integer.parseInt(limit);
		} catch (NumberFormatException e) {
			this.setErrorMessage("Limit value " + limit + " is not a valid number.");
		}
		
		if (numOfRecords <= 0) {
			this.setErrorMessage("Limit value must be a positive integer.");
			return;
		}
		
		String planCode = (String) getPlanCodeComboBox().getComponent().getSelectedItem();
		List<String> prodLotList = new ArrayList<String>();
		if(planCode == null || planCode.trim().equals("") || planCode.trim().equalsIgnoreCase("Select")) {
			ComboBoxModel<String> model = new ComboBoxModel<String>(prodLotList);
			this.getProdLotComboBox().setModel(model);
			this.getProdLotComboBox().getComponent().setSelectedIndex(-1);
			String s = this.getProdLotComboBox().getTextFieldText();
			System.out.println(s);
		} else {
			String productName = getProductComboBox().getComponent().getSelectedItem().toString().trim();
			
			if(ProductType.getType(productName).equals(ProductType.MBPN)) {
				List<PreProductionLot> preProdList = getDao(PreProductionLotDao.class).findAllByPlanCodeSort(planCode.trim());
				for(PreProductionLot lot : preProdList) {
					prodLotList.add(lot.getProductionLot());
				}
			}
			else {
				prodLotList = getDao(PreProductionLotDao.class).getNonShippedProductionLotsByPlanCode(planCode.trim());
			}
			
			
			if (prodLotList != null) {
				TreeSet<String> prodLotSet = new TreeSet<String>(prodLotList);
				prodLotList = new ArrayList<String>(prodLotSet);
				if(numOfRecords > prodLotSet.size()) numOfRecords = prodLotSet.size();			
				ComboBoxModel<String> model = new ComboBoxModel<String>(prodLotList.subList(prodLotList.size() - numOfRecords, prodLotList.size()));
				this.getProdLotComboBox().setModel(model);
				this.getProdLotComboBox().setSelectedIndex(-1);
			} else {
				this.getProdLotComboBox().setModel(null);
			}
		}
	}
	
	private List<String> getVisibleBDTypes() {
		List<String> types = new ArrayList<String>();
		String[] list = getProperty(VISIBLE_BROADCAST_DESTINATION_TYPES, "PRINTER").split(",");
		for(int i = 0; i < list.length; i++) {
			for(DestinationType dt : DestinationType.values()) {
				if(dt.name().equalsIgnoreCase(list[i])) {
					types.add(dt.name());	
				}
			}
		}
		return types;
	}

	public void actionPerformed(ActionEvent e) {
		try {
			clearErrorMessage();
			setWaitCursor();
			if (e.getSource().equals(getProductComboBox().getComponent())){
				productChanged();
			} else if (e.getSource().equals(getBDTypeComboBox().getComponent())){
				typeChanged();
			} else if (e.getSource().equals(getDivisionComboBox().getComponent())){
				reloadProcessPoints();
			} else if(e.getSource().equals(getJRadioButtonVIN())){
				showVINSearchText();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJRadioButtonSeqNumber())){
				showSeqSearchText();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJRadioButtonSeqNumRange())){
				showSequenceRangeOptions();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJRadioButtonProductionLot())){
				showProductionLotSearch();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJRadioButtonTrackingStatus())){
				showTrackingStatusSearch();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJRadioButtonLastProcPoint())){
				showProcPointTablePane();
				productTablePane.setColumnMappings(getColumnMappings().get());
			} else if(e.getSource().equals(getJButtonSearch())){
				showSearch();
			} else if (e.getSource().equals(getPlanCodeComboBox().getComponent())){
				refreshProdLotComboBox();
			} else if(e.getSource().equals(getJButtonPrint())){
				clickPrint();
			}else if(e.getSource().equals(getJButtonFormFeed())){
				clickFormFeed();
			} else if(e.getSource().equals(this.getLimitTextField().getComponent())) {
				refreshProdLotComboBox();
		    }
		} catch(Exception ex) {
			getLogger().error(ex, "Exception occurred.");
			setErrorMessage("Exception occurred: \n" + ExceptionUtils.getStackTrace(ex));
		} finally {
			setDefaultCursor();
		}
	}
		
	private void clickFormFeed() {
		int copies = numberOfCopiesSpinner.getValue();
		if(!(getFormFeedPrintersComboBox().getComponent().getItemCount()==0))
		{				
			String deviceName = (String) getFormFeedPrintersComboBox().getComponent().getSelectedItem();
			IPrintDevice iPrintDevice = (IPrintDevice) DeviceManager.getInstance().getDevice(deviceName);
			if(iPrintDevice!=null)
			{
				AbstractPrintDevice printDevice = (AbstractPrintDevice) iPrintDevice;
				String templateName=getTemplateName(deviceName);
				if (templateName!=null) {
					Template template = ServiceFactory.getDao(TemplateDao.class).findByKey(templateName);
					if (template != null) {
						if (template.getTemplateDataBytes() != null) {
							String dataToPrint = template.getTemplateDataString();
							if (printDevice.print(dataToPrint, copies, "")) {
								MessageDialog.showInfo(this,"Labels printed successfully", "Information");
							} else
								MessageDialog.showInfo(this,"Labels not printed ", "Information");							
						} else
							MessageDialog.showInfo(this, "Template Data not found","Information");
					} else
						MessageDialog.showInfo(this, "Template not found","Information");
				}else
					MessageDialog.showInfo(this, "Template name property for device not setup","Information");
			}else
			{			
				MessageDialog.showInfo(this, "Device not found","Information");		
			}
		}else
		{
			MessageDialog.showInfo(this,"Please set up Form feed Printers and select printer from list", "Information");
		}
	}

	private String getTemplateName(String deviceName) {
		if (formFeedDevices.containsValue(deviceName))
		{
			for (Map.Entry<String,String> formFeedDeviceEntry : formFeedDevices.entrySet()) {
				if ((formFeedDeviceEntry.getValue().toString()).equalsIgnoreCase(deviceName))
				{
					return formFeedTemplates.get(formFeedDeviceEntry.getKey());
				}
			}
		}   
		return null;
	}

	private void clickPrint(){
		setErrorMessage("");
		BroadcastService service = ServiceFactory.getService(BroadcastService.class);
		List<MultiValueObject<BroadcastDestination>> bdList =   printerTablePane.getSelectedItems();
		
		Map<String, String> formAuthorizedGroups = broadcastReprintPropertyBean.getFormAuthorizedGroups();
		ArrayList<String> forms = new ArrayList<String>();
		for (MultiValueObject<BroadcastDestination> bd : bdList) {
			forms.add(bd.getKeyObject().getRequestId().trim());
		}
		if (!this.isFormsAccessAllowed(forms, formAuthorizedGroups, true)) return;
		
		List<MultiValueObject<BaseProduct>> prdList =   productTablePane.getSelectedItems();
		List<String> errorMsgs = new ArrayList<String>();
		for(MultiValueObject<BaseProduct> object : prdList) {
			BaseProduct prd = object.getKeyObject();
			for (MultiValueObject<BroadcastDestination> mvo : bdList) {
				BroadcastDestination bd = mvo.getKeyObject();
				DataContainer container = new DefaultDataContainer();
				container.put(DataContainerTag.PRODUCT_ID, prd.getProductId());
				container.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
				if(	this.broadcastReprintPropertyBean.isTraySelectionEnabled() &&
					this.getTrayComboBox().getComponent().getSelectedItem() != null )
					container.put(TRAY_VALUE, getTrayComboBox().getComponent().getSelectedItem().toString());

				int trayIndx = getTrayComboBox().getComponent().getSelectedIndex();
				String destinationId = mvo.getValue(1).toString();
				String formFeed ="";
				if(!(getFormFeedPrintersComboBox().getComponent().getItemCount()==0)) 
					formFeed = getFormFeedPrintersComboBox().getComponent().getSelectedItem().toString();

				Integer trayValue = null;
				if( this.broadcastReprintPropertyBean.isTraySelectionEnabled()
						&& this.getTrayComboBox().getComponent().getSelectedItem() != null )
					trayValue = getTrayValuesByDest(destinationId, trayIndx);
				container.put(TRAY_VALUE, trayValue);
				getLogger().info("Final Report Info :"
						+" ProductId: " + prd.getProductId() 
						+", Selected Process PointId: " + bd.getProcessPointId()
						+", DestinationId: " + bd.getDestinationId() 
						+", RequestId: " + bd.getRequestId()
						+", FormFeedPrinter: "+ formFeed
						+", Print Quantity: "+ getNumberOfCopiesSpinner().getComponent().getValue()
						+", Tray Value: "+ trayValue
						+", Requested From: "+  ApplicationContext.getInstance().getLocalHostIp()
						+", UserId: "+  ApplicationContext.getInstance().getUserId()						
						+ ".");
				
				DataContainer dc = service.broadcast(bd.getProcessPointId(), bd.getSequenceNumber(), container);
				if (dc == null ) {
					continue;
				}
				String errorMsg = parseErrorMessages(dc);
				if (StringUtils.isNotBlank(errorMsg)) {
					String msg = "ProductId: " + prd.getProductId() + ", DestinationId: " + bd.getDestinationId() + ", RequestId: " + bd.getRequestId() + ".";
					errorMsg = msg + System.getProperty("line.separator") + "\t" + errorMsg;
				}
				if (StringUtils.isNotBlank(errorMsg)) {
					errorMsgs.add(errorMsg);
				}
			}
		}
		
		if (!errorMsgs.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Error occured for broadcast: " + System.getProperty("line.separator"));
			for (String str : errorMsgs) {
				sb.append(str).append(System.getProperty("line.separator"));
			}
			setErrorMessage(sb.toString());
		}
	}
	
	private boolean isFormsAccessAllowed(ArrayList<String> forms, Map<String, String> formAuthorizedGroups, boolean showError) {
		for (String form : forms) {
			if (formAuthorizedGroups!=null && formAuthorizedGroups.containsKey(form)) {
				String propValue = formAuthorizedGroups.get(form);
				for(String formSecurityGroup : propValue.split(",")) {
					if (accessControlManager.isAuthorized(formSecurityGroup.toUpperCase().trim())) return true;
				}
				if (showError)
					MessageDialog.showError("User " + accessControlManager.getUserName() + " does not have permissions required to print " + form + " form.");
				return false;
			}
		}
		return true;
	}
	
	private String removeLeadingVinChars(String productId){
		String leadingVinChars =PropertyService.getPropertyBean(SystemPropertyBean.class).getLeadingVinCharsToRemove();
		if(StringUtils.isNotBlank(leadingVinChars)){
		String[] vinChars = leadingVinChars.trim().split(",");
		
			for(String c:vinChars){
				if (productId.toUpperCase().startsWith(c)) {
					return productId.substring(c.length());
				}
			}
		}
		return productId;
	}
	
	private void findProducts(String productType) {
		List<BaseProduct> productsUpdated = new ArrayList<BaseProduct>();
		boolean isBroadcastEmptyCarrier =  PropertyService.getPropertyBoolean(getApplicationId(),"IS_BROADCAST_EMPTY_CARRIER",false);
		ProductDao<? extends BaseProduct> dao = ProductTypeUtil.getProductDao(productType);
		if(getJRadioButtonVIN().isSelected()){
			String searchString = getJTextVINSearchTxt().getText();
			if(!StringUtils.isEmpty(searchString) || searchString.trim().length() >= getSearchMinLength()){
				if(PropertyService.getPropertyBean(ProductPropertyBean.class).isRemoveIEnabled() &&
						ProductType.FRAME.name().equals((getSelectedProductType()))){
					products = dao.findAllBySN(removeLeadingVinChars(searchString.trim()));
				} else {
					products = dao.findAllBySN(searchString.trim());
				}
			}	else this.setErrorMessage(productType+" search field must have at least " + getSearchMinLength() + " characters.");				
		} else if(getJRadioButtonProductionLot().isSelected()) {
			String selectedProdLot = "";
			if(ProductType.FRAME.name().equals((getSelectedProductType()))) {
				String planCode = (String) getPlanCodeComboBox().getComponent().getSelectedItem();
				if(planCode == null || planCode.trim().equals("") || planCode.trim().equalsIgnoreCase("Select")
						|| planCode.trim().equalsIgnoreCase("Enter")) {
					
					String enteredText = this.getProdLotComboBox().getTextFieldText();
					if(null == enteredText || enteredText.trim().equals("")) {
						
					} else {
						selectedProdLot = enteredText;
					}
				} else if(this.getProdLotComboBox().getComponent().getSelectedItem() != null){
					selectedProdLot = this.getProdLotComboBox().getComponent().getSelectedItem().toString();
				}
				if(selectedProdLot != null && !selectedProdLot.trim().equals("") && selectedProdLot.trim().length() > 5) {
					products = dao.findAllByLikeProductionLot(selectedProdLot);
				}
			} else {
				if(this.getProdLotComboBox().getComponent().getSelectedItem() != null){
					selectedProdLot = this.getProdLotComboBox().getComponent().getSelectedItem().toString();
				}
				products = dao.findAllByProductionLot(selectedProdLot);
			}
		} else if(getJRadioButtonTrackingStatus().isSelected()) {
			String selectedLineId = getLineComboBox().getComponent().getSelectedItem().toString();
			products = dao.findByTrackingStatus(selectedLineId.trim().split("-")[1].trim());
			
		} else if(getJRadioButtonSeqNumber().isSelected()) {
			if(!StringUtils.isBlank(getJTextSeqMin().getText().toString())) {
				if(isBroadcastEmptyCarrier) {
					List<Frame> results =ServiceFactory.getDao(ProductCarrierDao.class).findByAfOnSeqNumber(getJTextSeqMin().getText().trim().toString());
					products = results;
				} else {
					int afOn = Integer.parseInt(getJTextSeqMin().getText().trim().toString());
					products = ((FrameDao) dao).findByAfOnSequenceNumber(afOn);
				}
			}
		} else if(getJRadioButtonSeqNumRange().isSelected()) {
			String selectedLineId = getLineComboBox().getComponent().getSelectedItem().toString();
			String[] temp = selectedLineId.trim().split("-");
			selectedLineId = temp.length > 1? temp[1].trim():selectedLineId;
			String seqMinEntry = this.getJTextSeqMin().getText().trim();
			String seqMaxEntry = this.getJTextSeqMax().getText().trim();
			String errorMessage = "";
			if(StringUtils.isBlank(seqMinEntry) || !StringUtils.isNumeric(seqMinEntry))
				errorMessage = "Sequence MIN number is invalid.";
			else if(StringUtils.isBlank(seqMaxEntry) || !StringUtils.isNumeric(seqMaxEntry))
				errorMessage = "Sequence MAX number is invalid.";
			else if (seqMinEntry.length() < 4 || seqMaxEntry.length() < 4)
				errorMessage = "Sequence number must be at least 4 characters long.";
		
			if (StringUtils.isBlank(errorMessage)) {
				Integer seqMin = ((FrameDao)dao).findAfOnSequenceNumberByShortSequence(seqMinEntry.length(), Integer.parseInt(seqMinEntry));
				Integer seqMax = ((FrameDao)dao).findAfOnSequenceNumberByShortSequence(seqMaxEntry.length(), Integer.parseInt(seqMaxEntry));	
				if (seqMin == null)
					errorMessage = "No match found for sequence MIN " + seqMinEntry + ".";
				else if (seqMax == null)
					errorMessage = "No match found for sequence MAX " + seqMaxEntry + ".";
				else if (seqMin > seqMax)
					errorMessage = "Sequence MIN " + seqMin + " is greater than sequence MAX " + seqMax + ".";
				else if (StringUtils.isBlank(selectedLineId)){
					if(isBroadcastEmptyCarrier) {
						errorMessage = "Please select a line.";
					} else {
						this.products = ((FrameDao)dao).findAllByAfOnSequenceNumber(seqMin,seqMax);
					}
				} else {
					if(isBroadcastEmptyCarrier) {
						List<Frame> results = ServiceFactory.getDao(ProductCarrierDao.class).findByAfOnSeqRangeLineId(getJTextSeqMin().getText().trim().toString(), 
								getJTextSeqMax().getText().trim().toString(), selectedLineId);
						this.products = results;
					} else {
						this.products = ((FrameDao)dao).findAllByAfOnSeqRangeLineId(seqMin,seqMax,selectedLineId);
					}
				}
			}
			if (!StringUtils.isBlank(errorMessage)) this.setErrorMessage(errorMessage);	
		} else if(getJRadioButtonLastProcPoint().isSelected()){
			ProcessPoint selectedProcPoint = getProcPointTablePane().getSelectedItem();
			if (selectedProcPoint == null) return;
			else if (!StringUtils.isEmpty(selectedProcPoint.getProcessPointId()))
				this.products = ((FrameDao) dao).findAllByLastPassingProcessPointId(selectedProcPoint.getProcessPointId().trim());
					
		}
		
		//shipping logic
		this.products = findByShippedStatus(this.products);
		
		for(BaseProduct p : this.products) {
			if(null != p.getTrackingStatus()) {
				if(this.linesMap.containsKey(p.getTrackingStatus().trim())) {
					p.setTrackingStatus(this.linesMap.get(p.getTrackingStatus().trim()));
					productsUpdated.add(p);
				}
			}else {
				//if tracking status is null then setTrackingStatus to a blank string and add the product to productsUpdated list.
				p.setTrackingStatus("");
				productsUpdated.add(p);	
			}
		}
		this.products = null;
		this.products = productsUpdated;
		
	}
	
	@SuppressWarnings("unchecked")
	private List<BaseProduct> findByShippedStatus(List<? extends BaseProduct> productsList) {
		if(productsList == null || productsList.isEmpty()) 
			return new ArrayList<BaseProduct>();
		
		if(shippingLines == null || shippingLines.length == 0) 		
			return (List<BaseProduct>) productsList;
		
		//check if the products are shipped in the shipping line or not.
		List<String> shippingLinesList = Arrays.asList(shippingLines);
		List<BaseProduct> filteredProducts = new ArrayList<BaseProduct>();
		for(BaseProduct product : productsList) {
			if(product.getTrackingStatus() != null && shippingLinesList.contains(product.getTrackingStatus().trim())) {
				if(getJRadioButtonShipped().isSelected())  
					filteredProducts.add(product);
			} else {
				if(getJRadioButtonNotShipped().isSelected())
					filteredProducts.add(product);
			}
		}
					
		return filteredProducts;
	}
	
	private List<MultiValueObject<BaseProduct>> prepareDisplayObjects(){
		List<MultiValueObject<BaseProduct>> list = new ArrayList<MultiValueObject<BaseProduct>>();
		Method method;
		if(products != null) {
			for (BaseProduct bp : products) {
				List<Object> values = new ArrayList<Object>();

				for(int i = 0; i < this.productHeadings.length; i++) {
					try {
						method = bp.getClass().getMethod(this.productGetters[i], new Class[0]);
					} catch (SecurityException e) {
						method = null;
					} catch (NoSuchMethodException e) {
						method = null;
					}
					if(method != null) {
						values.add(ReflectionUtils.invoke(bp, this.productGetters[i], new Object[0]));
					} else {
						values.add("");
					}
				}
				MultiValueObject<BaseProduct> multiValueObject = new MultiValueObject<BaseProduct>(bp,values);
				list.add(multiValueObject);
			}
		}
		return list;
	}

	private  void showSearch(){
		setErrorMessage("");
		getLogger().info("search button is clicked");
		products = null;
		msg = "";
		updateStatus(msg);
		productTablePane.clearSelection();
		productTablePane.removeData();
		products = new ArrayList<Product>();
		printerTablePane.clearSelection();
		findProducts(getSelectedProductType());
		productTablePane.reloadData(prepareDisplayObjects());
		sortRows();			
	}

	private void sortRows() {
		SortableObjectTableModel tableModel = (SortableObjectTableModel)productTablePane.getTable().getModel();
		
		boolean default_sort = false;
		
		Map<String, String> columnSortingStatus = broadcastReprintPropertyBean.getColumnSortingStatus();
		String selectedProductType = getProductComboBox().getComponent().getSelectedItem().toString();
		
		//check if columnSortingStatus is null (means property wasn't configured) or product type exists in columnSortingStatus map
		if (columnSortingStatus == null || !columnSortingStatus.containsKey(selectedProductType)) {
			default_sort = true;
		} else {
			int column_to_sort = Integer.parseInt(columnSortingStatus.get(selectedProductType));
			
			if (column_to_sort >= 0 && column_to_sort < tableModel.getColumnCount()) {
				tableModel.setSortingStatus(column_to_sort, 1);
			} else {
				default_sort = true;
			}
		}
		
		if (default_sort) {
			tableModel.setSortingStatus(1, 1);
		}
		productTablePane.refresh();
	}

	private void showVINSearchText() {
		setErrorMessage("");
		getJTextVINSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setVisible(false);
		getJTextSeqMax().setVisible(false);
		getLineComboBox().setVisible(false);
		getProcessPointSearchPanel().setVisible(false);
		getProdLotPanel().setVisible(false);
		getJTextVINSearchTxt().setVisible(true);
		getJTextVINSearchTxt().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(true);
	}
	
	private void showSeqSearchText() {
		setErrorMessage("");
		jTextSeqMin.setBounds(170, 40, 240, 30);
		getJTextVINSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMax().setVisible(false);
		getLineComboBox().setVisible(false);
		getJTextVINSearchTxt().setVisible(false);
		getProcessPointSearchPanel().setVisible(false);
		getProdLotPanel().setVisible(false);
		
		getJTextSeqMin().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(true);
	}
	
	private void showSequenceRangeOptions() {
		setErrorMessage("");
		getJTextVINSearchTxt().setText("");
		getJTextVINSearchTxt().setVisible(false);
		jTextSeqMin.setBounds(420, 35, 100, 30);
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");		
		getJTextSeqMax().setVisible(true);
		jTextSeqMax.setBounds(420, 80, 100, 30);
		getLineComboBox().setVisible(true);
		lineComboBox.setBounds(170, 50, 250, 50);
		getJTextSeqMax().setToolTipText("Max Value");
		getJTextSeqMin().setVisible(true);
		getJTextSeqMin().setToolTipText("Min Value");
		getJTextSeqMin().requestFocus();
		getProcessPointSearchPanel().setVisible(false);
		getProdLotPanel().setVisible(false);
				
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(true);
	}
	
	private void showProductionLotSearch(){
		setErrorMessage("");
		if(ProductType.FRAME.name().equals(getSelectedProductType())) {
			jTextSeqMin.setBounds(170, 90, 240, 30);
		} else {
			jTextSeqMin.setBounds(170, 90, 240, 30);
		}
		getJTextVINSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(false);
		getJTextSeqMax().setVisible(false);	
		getLineComboBox().setVisible(false);
		getJTextVINSearchTxt().setVisible(false);
		getProcessPointSearchPanel().setVisible(false);
		
		this.getProdLotPanel().setVisible(true);
		this.getPlanCodeComboBox().getComponent().setSelectedIndex(-1);
		this.getProdLotComboBox().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(true);
	}
	
	private void showTrackingStatusSearch(){
		setErrorMessage("");
		if(ProductType.FRAME.name().equals(getSelectedProductType())) {
			jTextSeqMin.setBounds(170, 120, 240, 30);
		} else {
			jTextSeqMin.setBounds(170, 140, 240, 30);
		}
		
		getJTextVINSearchTxt().setText("");
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setToolTipText("");
		getJTextSeqMax().setToolTipText("");
		getJTextSeqMin().setVisible(false);
		getJTextSeqMax().setVisible(false);	
		getLineComboBox().setVisible(true);
		getJTextVINSearchTxt().setVisible(false);
		getProcessPointSearchPanel().setVisible(false);
		getProdLotPanel().setVisible(false);
		
		getJTextSeqMin().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(true);
	}
	
	private void showProcPointTablePane(){
		setErrorMessage("");
		getProcessPointSearchPanel().setVisible(true);
		getJTextVINSearchTxt().setVisible(false);
		getJTextSeqMin().setText("");
		getJTextSeqMax().setText("");
		getJTextSeqMin().setVisible(false);
		getJTextSeqMax().setVisible(false);
		getLineComboBox().setVisible(false);
		getProdLotPanel().setVisible(false);
		
		getProcessPointSearchPanel().requestFocus();
		getSearchOptionPanel().validate();	
		getSearchOptionPanel().repaint();
		this.getJButtonSearch().setEnabled(processPointTablePane.getSelectedItems().size() > 0);		
	}
	
	private void productChanged(){
		setErrorMessage("");
		String productName = getSelectedProductType();
		getLogger().info(productName +" is selected");
		jTextVINSearchTxt.setText("");
		jTextSeqMin.setText("");
		jTextSeqMax.setText("");
		this.loadColumnMap();
		productTablePane.setColumnMappings(getColumnMappings().get());
		productTablePane.clearSelection();
		productTablePane.removeData();
		printerTablePane.clearSelection();
		getProcessPointSearchPanel().setVisible(false);
		getJTextSeqMin().setVisible(false);
		jRadioButtonVIN.doClick();
		switch(ProductType.getType(productName)) {
		case FRAME :
			jRadioButtonSeqNumber.setVisible(true);
			jRadioButtonSeqNumRange.setVisible(true);
			jRadioButtonProductionLot.setVisible(true);
			jRadioButtonVIN.setBounds(30, 20, 120, 20);
			jRadioButtonTrackingStatus.setBounds(30, 120, 120, 20);
			jRadioButtonProductionLot.setBounds(30, 95, 120, 20);
			jRadioButtonLastProcPoint.setVisible(true);
			jRadioButtonNotShipped.setVisible(true);
			jRadioButtonShipped.setVisible(true);
			break;
		case ENGINE :
		case MBPN:
		case MISSION :
		case KNUCKLE :
		case BUMPER :
			jRadioButtonSeqNumber.setVisible(false);
			jRadioButtonSeqNumRange.setVisible(false);
			jRadioButtonProductionLot.setVisible(true);
			jRadioButtonLastProcPoint.setVisible(false);
			jRadioButtonNotShipped.setVisible(false);
			jRadioButtonShipped.setVisible(false);
			jRadioButtonVIN.setBounds(30, 20, 120, 20);
			jRadioButtonProductionLot.setBounds(30, 85, 120, 40);
			jRadioButtonTrackingStatus.setBounds(30, 140, 120, 40);
			break;
		default :
			jRadioButtonSeqNumber.setVisible(false);
			jRadioButtonSeqNumRange.setVisible(false);
			jRadioButtonProductionLot.setVisible(false);
			jRadioButtonLastProcPoint.setVisible(false);
			jRadioButtonNotShipped.setVisible(false);
			jRadioButtonShipped.setVisible(false);
			jRadioButtonVIN.setBounds(30, 20, 120, 20);
			jRadioButtonTrackingStatus.setBounds(30, 140, 120, 40);
		}
		resetBroadcastPrinters();
		printerTablePane.reloadData(getBroadcastPrinters());
		this.showTrayComboBox();
	}
	
	private void updateStatus(String message) {
		getLogger().info(message);
		super.setMessage(message);
	}
	
	private List<MultiValueObject<BroadcastDestination>> findBroadcastPrinters() {
		Map<String, String> formAuthorizedGroups = broadcastReprintPropertyBean.getFormAuthorizedGroups();
		List<MultiValueObject<BroadcastDestination>> printers = new ArrayList<MultiValueObject<BroadcastDestination>>();
		String selectedProductTypeName = getSelectedProductType();
		String filterIds = bReprintPropertyBean.getFilterByDestinationIds();
		String filterPPIds = bReprintPropertyBean.getFilterByProcessPointIds();
		for (BroadcastDestination bd : getBroadcastDestinations()) {
			if(getBDTypeComboBox().getComponent().getSelectedItem() == bd.getDestinationType().name() || isIdIncluded(bd.getDestinationId())) {
				if((filterByIds(bd.getDestinationId(), filterIds) && filterByIds(bd.getId().getProcessPointId(), filterPPIds))){
					//filter form by auth group -- only filter Printers not any other destination types
					if (bd.getDestinationType() == DestinationType.Printer) {
						ArrayList<String> forms = new ArrayList<>();
						
						//Printers should not have null or empty request ID
						if (bd.getRequestId() != null) {
							forms.add(bd.getRequestId().trim());
							if (isFormsAccessAllowed(forms, formAuthorizedGroups, false)) {
								String processPointId = bd.getProcessPointId();
								String productType = getProductTypeByProcessPointIdIx().get(processPointId);
								if (!StringUtils.equals(StringUtils.trim(selectedProductTypeName), StringUtils.trim(productType))) {
									continue;
								}
								List<Object> values = new ArrayList<Object>();
								for(String methodName : PRINTER_GETTERS) {
									values.add(ReflectionUtils.invoke(bd, methodName, new Object[0]));
								}
								MultiValueObject<BroadcastDestination> multiValueObject = new MultiValueObject<BroadcastDestination>(bd,values);
								printers.add(multiValueObject);
							}
						}
					} else {
						String processPointId = bd.getProcessPointId();
						String productType = getProductTypeByProcessPointIdIx().get(processPointId);
						if (!StringUtils.equals(StringUtils.trim(selectedProductTypeName), StringUtils.trim(productType))) {
							continue;
						}
						List<Object> values = new ArrayList<Object>();
						for(String methodName : PRINTER_GETTERS) {
							values.add(ReflectionUtils.invoke(bd, methodName, new Object[0]));
						}
						MultiValueObject<BroadcastDestination> multiValueObject = new MultiValueObject<BroadcastDestination>(bd,values);
						printers.add(multiValueObject);
					}
				}
			}
		}
		return printers;
	}
	
	private String getFilterFromDestinationFilterTextField() {
		return getDestinationFilterTextField().getComponent().getText();
	}
	
	private void triggerFilterDestinations() {		
		String filter = getFilterFromDestinationFilterTextField();
		printerTablePane.reloadData(filterBroadcastPrintersList(filter));
	}
	
	private List<MultiValueObject<BroadcastDestination>> filterBroadcastPrintersList(String filter) {
		List<MultiValueObject<BroadcastDestination>> filteredDestinations = new ArrayList<>();
		
		for (MultiValueObject<BroadcastDestination> mvo : getBroadcastPrinters()) {
			if (mvo.getKeyObject().getDestinationId().toLowerCase().contains(filter.toLowerCase())) {
				filteredDestinations.add(mvo);
			}
		}
		
		return filteredDestinations;
	}
	
	private void typeChanged() {
		setErrorMessage("");
		resetBroadcastPrinters();
		printerTablePane.reloadData(getBroadcastPrinters());
		this.showTrayComboBox();
	}
	
	private void showTrayComboBox() {
		String selectedBDType = (String)this.getBDTypeComboBox().getComponent().getSelectedItem();
		Boolean isPrinter = selectedBDType != null && selectedBDType.equalsIgnoreCase(DestinationType.Printer.name());
		this.getTrayComboBox().setVisible(isPrinter);
		if (!isPrinter) return;
		this.getTrayComboBox().setEnabled(!printerTablePane.getSelectedItems().isEmpty() &&
					this.broadcastReprintPropertyBean.isTraySelectionEnabled());
	}
	
	private Integer getTrayValuesByDest(String destinationId, Integer trayIndex) {
		Integer trayValue = getTrayValuesByDestMap().get(DEFAULT_TRAY_LIST).get(0);
		try {
			if (destinationId != null && getTrayValuesByDestMap().containsKey(destinationId)) {
				List<Integer> trayValueList = getTrayValuesByDestMap().get(destinationId);
				trayValue = trayValueList.get(trayIndex);
			}else {
				List<Integer> trayValueList = getTrayValuesByDestMap().get(DEFAULT_TRAY_LIST);
				trayValue = trayValueList.get(trayIndex);
			}
		} catch (Exception e) {
			getLogger().error("Tray index" + trayIndex + " doesn not exist for destination" + destinationId);
		}
		return trayValue;
	}
	
	@SuppressWarnings("unchecked")
	private void showTrayValues() {
		List<MultiValueObject<BroadcastDestination>> selectedPrinterList = printerTablePane.getSelectedItems();
		int trayCount = 0;

		for (MultiValueObject<BroadcastDestination> selectedPrinter : selectedPrinterList) {
			BroadcastDestination broadcastDestination = selectedPrinter.getKeyObject();
			List<Integer> trayValue;
			clearErrorMessage();
			if (getTrayValuesByDestMap().containsKey(broadcastDestination.getDestinationId()))
				trayValue = getTrayValuesByDestMap().get(broadcastDestination.getDestinationId());
			else
				trayValue = getTrayValuesByDestMap().get(DEFAULT_TRAY_LIST);

			if (trayCount == 0) {
				trayCount = trayValue.size();
			} else if (trayCount == trayValue.size()) {
				continue;
			} else {
				setErrorMessage(TRAY_SIZE_MATCH_ERR);
				getLogger().error(TRAY_SIZE_MATCH_ERR);
				this.getTrayComboBox().setEnabled(false);
				this.getJButtonPrint().setEnabled(false);
				return;
			}
			List<Integer> trayValues = new ArrayList<Integer>();		
			for (int i = 0; i < trayCount; i++) {
				trayValues.add(i + 1);
			}
			
			ComboBoxModel<Integer> model = new ComboBoxModel<Integer>(trayValues);
			trayComboBox.getComponent().setModel(model);
			trayComboBox.getComponent().setSelectedIndex(0);
			this.getTrayComboBox().setVisible(this.broadcastReprintPropertyBean.isTraySelectionEnabled());
			this.getJButtonPrint().setEnabled(true);
			this.getTrayComboBox().setEnabled(true);
		}
	}
	
	private boolean filterByIds(String anId, String filterIds){
		if(StringUtils.isEmpty(filterIds)) return true;
		for(int i = 0; i < getFilterBIds(filterIds).length; i++) {
			if(getFilterBIds(filterIds)[i].equals(anId)) {
				return true;
			}
		}
		return false;
	}
	
	public String[] getFilterBIds(String filterIds) {
		String[] filterByIds = null;
		if(!StringUtils.isEmpty(filterIds)) {
			filterByIds = filterIds.split(",");
		} else {
			filterByIds = new String[] {};
		}
		return filterByIds;
	}
	
	public boolean isIdIncluded(String anId) {
		for(int i = 0; i < getAdditionalBroadcastDestinationIds().length; i++) {
			if(getAdditionalBroadcastDestinationIds()[i].equals(anId)) {
				return true;
			}
		}
		return false;
	}

	public String[] getAdditionalBroadcastDestinationIds() {
		if(additionalBroadcastDestinationIds == null) {
			String destinationIds = bReprintPropertyBean.getAdditionalDestinationIds();
			if(!StringUtils.isEmpty(destinationIds)) {
				additionalBroadcastDestinationIds = destinationIds.split(",");
			} else {
				additionalBroadcastDestinationIds = new String[] {};
			}
		}
		return additionalBroadcastDestinationIds;
	}
	
	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		if (printerTablePane.getSelectedItems().size() > 0 && productTablePane.getSelectedItems().size() > 0) {
			this.getTrayComboBox().setVisible(this.broadcastReprintPropertyBean.isTraySelectionEnabled());
			getTrayValuesByDestMap();
			showTrayValues();
			
		} else {
			this.getTrayComboBox().setEnabled(false);
		}
		
		if (e.getSource() == productTablePane.getTable().getSelectionModel()
				|| e.getSource() == printerTablePane.getTable().getSelectionModel()) {
			getJButtonPrint().setEnabled(
					(productTablePane.getSelectedItems().size() > 0) && (printerTablePane.getSelectedItems().size() > 0)
							&& (!getMainWindow().getMessage().equals(TRAY_SIZE_MATCH_ERR)));
		} else if (e.getSource() == processPointTablePane.getTable().getSelectionModel()){
			getJButtonSearch().setEnabled((processPointTablePane.getSelectedItems().size() > 0));
		}
	}
	
	@Override
	public void onTabSelected() {
		Logger.getLogger().info("Broadcast Reprint Label Panel is selected");
		
	}
	
	public String getId() {
		return "BroadcastReprintPanel";
	}
		
	public void handleStatusChange(PrintDeviceStatusInfo statusInfo) {
		updateStatus(statusInfo.getDisplayMessage());
	}

	public String getApplicationName() {
		return "BroadcastReprintPanel";
	}

	public void controlGranted(String deviceId) {
	}

	public void controlRevoked(String deviceId) {
	}

	public List<BroadcastDestination> getBroadcastDestinations() {
		if(broadcastDestinations == null) {
			broadcastDestinations = ServiceFactory.getDao(BroadcastDestinationDao.class).findAll();
			loadProductTypeIx(broadcastDestinations);
		}
		return broadcastDestinations;
	}
	
	public List<MultiValueObject<BroadcastDestination>> getBroadcastPrinters() {
		if (broadcastPrinters == null) {
			broadcastPrinters = findBroadcastPrinters();
		}
		return broadcastPrinters;
	}
	
	private void resetBroadcastPrinters() {
		broadcastPrinters = null;
	}
	
    protected void loadProductTypeIx(List<BroadcastDestination> destinations) {
    	getProductTypeByProcessPointIdIx().clear();
    	if (destinations == null || destinations.isEmpty()) {
    		return;
    	}
    	
    	List<String> ppIds = new ArrayList<String>();
    	for (BroadcastDestination bd : destinations) {
    		ppIds.add(bd.getProcessPointId());
    	}
    	
    	ComponentPropertyDao cpDao = ServiceFactory.getDao(ComponentPropertyDao.class);
    	List<ComponentProperty> list = cpDao.findAllProductTypes();
    	if (list == null) {
    		list = new ArrayList<ComponentProperty>();
    	}
    	for (ComponentProperty cp : list) {
    		String componentId = cp.getId().getComponentId();
    		if (ppIds.contains(componentId)) {
    			getProductTypeByProcessPointIdIx().put(componentId, cp.getPropertyValue());
    		}
    	}
    	String defaultProductType = PropertyService.getProductType();
    	for (String ppId : ppIds) {
    		if (getProductTypeByProcessPointIdIx().keySet().contains(ppId)) {
    			continue;
    		}
    		getProductTypeByProcessPointIdIx().put(ppId, defaultProductType);
    	}
    }
	
	protected String parseErrorMessages(DataContainer dc) {
		if (dc == null) {
			return null;
		}
		
		String errorMsg = StringUtils.join(dc.getErrorMessages(),  System.getProperty("line.separator") + "\t");
		Throwable ex = dc.getException();
		String msg = "";
		if (StringUtils.isNotBlank(errorMsg)) {
			msg = errorMsg;
		}
		if (ex != null) {
			if (StringUtils.isNotBlank(msg)) {
				msg = msg + System.getProperty("line.separator") + "\t";
			}
			msg = msg + "Exception: " + ex;
		}
		return msg;
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

	protected AtomicInteger getActionCounter() {
		return actionCounter;
	}

	protected void setActionCounter(AtomicInteger actionCounter) {
		this.actionCounter = actionCounter;
	}

	protected Map<String, String> getProductTypeByProcessPointIdIx() {
		return productTypeByProcessPointIdIx;
	}
	
	protected int getSearchMinLength(){
		return PropertyService.getPropertyBean(ProductPropertyBean.class, ApplicationContext.getInstance().getApplicationId()).getProductSearchMinLength();
	}
}