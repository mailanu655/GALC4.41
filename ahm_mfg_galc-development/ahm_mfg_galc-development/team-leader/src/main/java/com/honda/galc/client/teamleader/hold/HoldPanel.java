package com.honda.galc.client.teamleader.hold;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.report.TableReport;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>HoldPanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class HoldPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_DIVISION = "DEFAULT_DIVISION";

	private JSplitPane splitPanel;
	private InputPanel inputPanel;
	private ObjectTablePane<Map<String, Object>> productPanel;
	private QsrMaintenancePropertyBean qsrMaintPropertyBean;

	private Map<ProductType, PropertiesMapping> productTypeColumnsMapping;
	private Map<ProductType, TableReport> reports;

	private JPopupMenu productPopupMenu;

	private Map<String, Object> cache;
	
	private List<String> scrapDisabledProductTypes=new ArrayList<String>();

	public HoldPanel(String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
		this.cache = new HashMap<String, Object>();
		this.productTypeColumnsMapping = new HashMap<ProductType, PropertiesMapping>();
		this.reports = new HashMap<ProductType, TableReport>();
		defineProductTypeColumnsMapping();
		defineReports();
		initView();
		initModel();
		mapActions();
		initState();

	}

	// === tab frame overrides === //
	@Override
	public void onTabSelected() {

	}

	public void actionPerformed(ActionEvent arg0) {

	}

	// === init methods === //
	protected void initView() {
		setLayout(new GridLayout(1, 1));

		this.inputPanel = createInputPanel();
		this.productPanel = createProductPanel();

		this.productPopupMenu = createProductPopupMenu();
		setScrapDisabledProductTypes(getScrapDisabledProductTypesList());
		this.splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, getInputPanel(), getProductPanel());
		getSplitPanel().setDividerLocation(50);
		getSplitPanel().setEnabled(false);
		getSplitPanel().setName("splitPanel");
		add(getSplitPanel());
	}

	protected void mapActions() {

	}

	protected void initState() {
		String division = getQsrMaintPropertyBean().getDivision();
		if(division.equals(DEFAULT_DIVISION)) {
			getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
		}else  {
		Division div = ServiceFactory.getDao(DivisionDao.class).findByDivisionId(division);
		ProductType productType = ProductType.getType(PropertyService.getPropertyBean(SystemPropertyBean.class, getApplicationId()).getProductType());
		getInputPanel().getDepartmentComboBox().getModel().setSelectedItem(div);
		getInputPanel().getProductTypeComboBox().getModel().setSelectedItem(productType);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initModel() {
		List<Division> divisions = Config.getInstance(window.getApplication().getApplicationId()).getDivisions();
		getInputPanel().getDepartmentComboBox().setModel(new DefaultComboBoxModel(new Vector<Division>(divisions)));
		getInputPanel().getDepartmentComboBox().setSelectedIndex(-1);
	}

	// === factory methods === //
	protected InputPanel createInputPanel() {
		InputPanel panel = new InputPanel(this);
		return panel;
	}

	protected JPopupMenu createProductPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		return popup;
	}

	protected ObjectTablePane<Map<String, Object>> createProductPanel() {
		PropertiesMapping mapping = getColumnsMapping();
		ObjectTablePane<Map<String, Object>> panel = new ObjectTablePane<Map<String, Object>>(mapping.get(), true, true);
		
		panel.getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
			private static final long serialVersionUID = 1L;
			private static final int AUTOFIT_COLS = 5;
			
            int[] colWidth = new int[AUTOFIT_COLS];
            FontMetrics metrics;
            
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (metrics == null) metrics = table.getTableHeader().getFontMetrics(table.getTableHeader().getFont());
                if (column < AUTOFIT_COLS) {    
                    if (row == 0) {
                        Object colHeader = table.getColumnModel().getColumn(column).getHeaderValue();
                        colWidth[column] = (colHeader == null) ? 0 : metrics.stringWidth(colHeader.toString());
                    }
                    if (value != null && metrics.stringWidth(value.toString()) > colWidth[column]) {
                        colWidth[column] =  metrics.stringWidth(value.toString());
                        panel.getTable().getColumnModel().getColumn(column).setMinWidth(colWidth[column]);
                    }
                }
                
				Map<String, Object> map = productPanel.getItems().get(row);

				if(Boolean.TRUE.equals(map.get("ship"))){
					c.setBackground(Color.LIGHT_GRAY);
								}
				else if(!isSelected){
					c.setBackground(Color.WHITE);
				}
				return c;
			}
		});
		panel.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		panel.getTable().setName("productPanel");
		return panel;
	}

	// === product columms mapping === //
	protected void defineProductTypeColumnsMapping() {
		getProductTypeColumnsMapping().put(null, createProductColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.IPU, createProductColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.KNUCKLE, createProductColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.BLOCK, createBlockColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.HEAD, createHeadColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.ENGINE, createEngineColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.FRAME, createFrameColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.IPU_MBPN, createMbpnColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.MBPN, createMbpnColumnsMapping());
		getProductTypeColumnsMapping().put(ProductType.PLASTICS, createMbpnColumnsMapping());
		for (Map.Entry<ProductType, PropertiesMapping> entry : getProductTypeColumnsMapping().entrySet()) {
			if (entry.getKey() != null && Config.isOwnerProductHold(entry.getKey().name())){
				putOwnerColumnsMapping(entry.getValue());
			}
		}
	}

	// === product columns definitions === //
	protected PropertiesMapping createDefaultColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Product Id", "product.productId");
		return mapping;
	}
	
	protected PropertiesMapping putOwnerColumnsMapping(PropertiesMapping mapping) {	
		mapping.put("Owner ID", "owner.productId");
		mapping.put("Owner Type", "owner.productType");
		mapping.put("Owner SpecCode", "owner.productSpecCode");
		return mapping;
	}

	protected PropertiesMapping createProductColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Product ID", "product.productId");
		mapping.put("Production Lot", "product.productionLot");
		mapping.put("KD Lot", "product.kdLotNumber");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");

		return mapping;
	}

	protected PropertiesMapping createBlockColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Block ID", "product.productId");
		mapping.put("DC Number", "product.dcSerialNumber");
		mapping.put("MC Number", "product.mcSerialNumber");
		mapping.put("EIN", "product.engineSerialNumber");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");
		return mapping;
	}

	protected PropertiesMapping createHeadColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("DC Number", "product.dcSerialNumber");
		mapping.put("MC Number", "product.mcSerialNumber");
		mapping.put("EIN", "product.engineSerialNumber");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");

		return mapping;
	}

	protected PropertiesMapping createEngineColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("EIN", "product.productId");
		mapping.put("VIN", "product.vin");
		mapping.put("Production Lot", "product.productionLot");
		mapping.put("KD Lot", "product.kdLotNumber");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");
		return mapping;
	}

	protected PropertiesMapping createFrameColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("VIN", "product.productId");
		mapping.put("Short Vin", "product.shortVin");
		mapping.put("YMTOC", "product.productSpecCode");
		mapping.put("Production Lot", "product.productionLot");
		mapping.put("AF ON Seq.", "product.afOnSequenceNumber");
		mapping.put("KD Lot", "product.kdLotNumber");
		mapping.put("EIN", "product.engineSerialNo");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");
		return mapping;
	}

	protected PropertiesMapping createMbpnColumnsMapping() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("#", "row");
		mapping.put("Product ID", "product.productId");
		mapping.put("Last Process", "lastProcessPointName");
		mapping.put("Last Process Time Stamp", "updateTimestamp");
		mapping.put("Equipment Id", "deviceId");
		return mapping;
	}

	// === reports defs === //
	public void defineReports() {

		getReports().put(null, createDefaultReport());
		getReports().put(ProductType.IPU, createProductReport());
		getReports().put(ProductType.KNUCKLE, createProductReport());

		getReports().put(ProductType.BLOCK, createDiecastReport());
		getReports().put(ProductType.HEAD, createDiecastReport());
		getReports().put(ProductType.ENGINE, createEngineReport());
		getReports().put(ProductType.FRAME, createFrameReport());

		getReports().put(ProductType.MBPN, createMbpnReport());
		getReports().put(ProductType.PLASTICS, createMbpnReport());
	}

	public TableReport createReport() {
		TableReport report = TableReport.createXlsxTableReport();
		report.setFileName("ExportData.xlsx");
		report.setTitle("Export Data");
		report.setReportName("Export Data");
		return report;
	}

	protected TableReport createDefaultReport() {
		TableReport report = createReport();
		report.addColumn("product.productId", "Product ID", 7000);
		return report;
	}

	protected TableReport createProductReport() {
		TableReport report = createReport();
		report.addColumn("product.productId", "Product ID", 7000);
		report.addColumn("product.productionLot", "Production Lot",  7000);
		report.addColumn("product.kdLotNumber", "KD Lot",  7000);
		return report;
	}

	protected TableReport createDiecastReport() {
		TableReport report = createReport();
		report.addColumn("product.dcSerialNumber", "DC Number", 7000);
		report.addColumn("product.mcSerialNumber", "MC Number", 7000);
		report.addColumn("product.engineSerialNumber", "EIN Number", 6000);
		return report;
	}

	protected TableReport createEngineReport() {
		TableReport report = createReport();
		report.addColumn("product.productId", "EIN", 7000);
		report.addColumn("product.vin", "VIN", 7000);
		report.addColumn("product.productionLot", "Production Lot" , 7000);
		report.addColumn("product.kdLotNumber", "KD Lot",  7000);
		return report;
	}

	protected TableReport createFrameReport() {
		TableReport report = createReport();
		report.addColumn("product.productId", "VIN", 7000);
		report.addColumn("product.shortVin", "Short VIN", 7000);
		report.addColumn("product.productSpecCode", "YMTOC", 7000);
		report.addColumn("product.productionLot", "Production Lot", 7000);
		report.addColumn("product.kdLotNumber", "KD Lot",  7000);
		report.addColumn("product.engineSerialNo", "EIN",  6000);
		report.addColumn("product.lastPassingProcessPointId", "Last Process",  6000);
		report.addColumn("product.updateTimestamp", "Last Process Time Stamp", 6000);
		return report;
	}

	protected TableReport createMbpnReport() {
		TableReport report = createReport();
		report.addColumn("product.productId", "Product ID", 7000);
		return report;
	}

	// === get/set === //
	protected PropertiesMapping getColumnsMapping() {
		ProductType productType = getProductType();
		PropertiesMapping mapping = getProductTypeColumnsMapping().get(productType);
		if (mapping != null) {
			return mapping;
		}
		mapping = createDefaultColumnsMapping();
		getProductTypeColumnsMapping().put(productType, mapping);
		return mapping;
	}

	public TableReport getReport() {
		ProductType productType = getProductType();
		TableReport report = getReports().get(productType);
		if (report != null) {
			return report;
		}
		report = createDefaultReport();
		getReports().put(productType, report);
		return report;
	}

	public ObjectTablePane<Map<String, Object>> getProductPanel() {
		return productPanel;
	}

	public JPopupMenu getProductPopupMenu() {
		return productPopupMenu;
	}

	public Map<String, Object> getCache() {
		return cache;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public Division getDivision() {
		return (Division) getInputPanel().getDepartmentComboBox().getSelectedItem();
	}

	public ProductType getProductType() {
		return (ProductType) getInputPanel().getProductTypeComboBox().getSelectedItem();
	}

	protected JSplitPane getSplitPanel() {
		return splitPanel;
	}

	protected Map<ProductType, PropertiesMapping> getProductTypeColumnsMapping() {
		return productTypeColumnsMapping;
	}

	public void setProductPanelColumns() {
		getProductPanel().setColumnMappings(getColumnsMapping().get());
	}

	protected Map<ProductType, TableReport> getReports() {
		return reports;
	}
	
	protected List<String> getScrapDisabledProductTypesList() {
		QsrMaintenancePropertyBean propertyBean = PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getApplicationId());
		List<String> productTypes = Arrays.asList(propertyBean.getScrapDisabledProductTypes());
		return productTypes;
	}
	
	public List<String> getScrapDisabledProductTypes() {
		return scrapDisabledProductTypes;
	}

	public void setScrapDisabledProductTypes(List<String> scrapDisabledProductTypes) {
		this.scrapDisabledProductTypes = scrapDisabledProductTypes;
	}
	
	protected boolean disableScrapMenuItem()
	{		
		for(String scrapDisableProductType:getScrapDisabledProductTypes())
		{
			if(getProductType().name().trim().equalsIgnoreCase(scrapDisableProductType.trim()))
				return true;
		}	
		return false;
	}
	
	protected QsrMaintenancePropertyBean getQsrMaintPropertyBean() {
		return (qsrMaintPropertyBean == null) ? 
				PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getApplicationId()) : qsrMaintPropertyBean;
	}
}
