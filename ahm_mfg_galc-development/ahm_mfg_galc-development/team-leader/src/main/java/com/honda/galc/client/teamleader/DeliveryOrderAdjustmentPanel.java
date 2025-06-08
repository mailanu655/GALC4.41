package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang.StringUtils;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledListBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.ProductSpecSelectionPanel;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

public class DeliveryOrderAdjustmentPanel extends TabbedPanel implements ActionListener, TableModelListener
{
	private static final long serialVersionUID = 1L;
	private JDatePanelImpl datePanel;
	private JDatePickerImpl datePicker;
	private ProductSpecSelectionPanel ymtoSelectPanel;
	private ObjectTablePane<FrameSpec> filteredFrameSpecTable;
	private ObjectTablePane<FrameSpec> commonFrameSpecTable;
	private ObjectTablePane<PurchaseContract> purchaseContractTable;
	private HashMap<Long,Integer> orderUnitsByPurchaseContract;
	private JPanel buttonsPanel;
	private JButton createButton;
    private JButton saveButton;
    private JButton undoButton;
    private PurchaseContractDao purchaseContractDao;
    private FrameSpecDao frameSpecDao;
    
   
	public DeliveryOrderAdjustmentPanel(TabbedMainWindow mainWindow){
		super("Delivery Order Adjustment", KeyEvent.VK_M, mainWindow);
	}

	@Override
	public void onTabSelected() {
		try {
			if (this.isInitialized)	return;
			this.initComponents();
			this.addListeners();
			this.refreshPurchaseContractTable();
			this.isInitialized = true;
		} catch (Exception e) {
			Logger.getLogger().error(e, "Exception to start DeliveryOrderAdjustmentPanel.");
			setErrorMessage("Exception to start DeliveryOrderAdjustmentPanel." + e.toString());
		}	
	}
	
	private void initComponents() {
		setLayout(new MigLayout());
		this.add(this.createFilterPanel(), "spanx, center, pushx, wrap");
		this.add(this.createFilteredFrameSpecTable(), "grow");
		this.add(this.createPurchaseContractPanel(), "width " + this.getMainWindow().getWidth()/2.5 + ", spany, grow, pushy, wrap");
		this.add(this.createFrameSpecCommonTable(), "grow");
	}
	
	private JPanel createFilterPanel() {
		JPanel filterPanel = new JPanel(new MigLayout());
		filterPanel.add(this.createDatePickerPanel(),"wrap");
		filterPanel.add(this.createYmtoSelectionPanel(),"aligny top");
		return filterPanel;
	}
	
	private JPanel createDatePickerPanel() {
		JPanel datePickerPanel = new JPanel(new MigLayout());
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		this.datePanel = new JDatePanelImpl(model, p);
		this.datePicker = new JDatePickerImpl(datePanel, new DeliveryOrderDateLabelFormatter());
		datePickerPanel.add(new JLabel("Order Due Date: "));
		datePickerPanel.add(this.datePicker);
		return datePickerPanel;
	}
	
	private JPanel createYmtoSelectionPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 0 0 0 0"));
		int height = this.getMainWindow().getHeight()/3;
		panel.setSize(panel.getWidth(), height);
		this.ymtoSelectPanel = new ProductSpecSelectionPanel(ProductType.FRAME.getProductName());
		this.ymtoSelectPanel.setBorder(new TitledBorder("Frame Model"));
		for(LabeledListBox lbox : ymtoSelectPanel.getColumnBoxsList()){
			((BorderLayout)lbox.getLayout()).setVgap(0);
			lbox.getLabel().setHorizontalAlignment(JLabel.CENTER);
		}
		this.ymtoSelectPanel.getPanel("Model_Type").getLabel().setText("Type");
		this.ymtoSelectPanel.remove(5);
		this.ymtoSelectPanel.remove(4);
		this.ymtoSelectPanel.remove(3);
		panel.add(this.ymtoSelectPanel,"width 400:400:400, height 150:150:150");
		return panel;
	}
	
	private ObjectTablePane<FrameSpec> createFilteredFrameSpecTable() {
		this.filteredFrameSpecTable = this.createFrameSpecTable();
		this.filteredFrameSpecTable.getTable().setName("FrameSpecTable");
		this.filteredFrameSpecTable.setBorder(new TitledBorder("Frame Specs"));
		this.filteredFrameSpecTable.getTable().setDefaultRenderer(Object.class, new FilteredFrameSpecCellRenderer());
		return this.filteredFrameSpecTable;
	}
	
	private ObjectTablePane<FrameSpec> createFrameSpecCommonTable() {
		this.commonFrameSpecTable = this.createCommonFrameSpecTable();
		this.commonFrameSpecTable.getTable().setName("CommonFrameSpecTable");
		this.commonFrameSpecTable.setBorder(new TitledBorder("Commonized Frame Specs"));
		this.commonFrameSpecTable.getTable().setDefaultRenderer(Object.class, new FilteredCommonFrameSpecCellRenderer());
		return this.commonFrameSpecTable;
	}
	
	private ObjectTablePane<FrameSpec> createFrameSpecTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Year", "modelYearCode");
		mapping.put("Model", "modelCode");
		mapping.put("Type", "modelTypeCode");
		mapping.put("Option", "modelOptionCode");
		mapping.put("Ext Color", "extColorCode");
		mapping.put("Int Color", "intColorCode");
		mapping.put("Sales Model", "salesModelCode");
		mapping.put("Sales Type", "salesModelTypeCode");
		mapping.put("Sales Ext Color", "salesExtColorCode");
		mapping.put("Common Sales Model", "commonSalesModelCode");
		ObjectTablePane<FrameSpec> frameSpecTable = new ObjectTablePane<FrameSpec>(mapping.get(), false, true);
		frameSpecTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frameSpecTable.getTable().setEditingColumn(10);
		frameSpecTable.getTable().setCellSelectionEnabled(false);
		frameSpecTable.getTable().setRowSelectionAllowed(true);
		return frameSpecTable;
	}
	
	public ObjectTablePane<FrameSpec> getFilteredFrameSpecTable() {
		return filteredFrameSpecTable;
	}
	
	private ObjectTablePane<FrameSpec> createCommonFrameSpecTable() {
		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("Year", "modelYearCode");
		mapping.put("Model", "modelCode");
		mapping.put("Type", "modelTypeCode");
		mapping.put("Option", "modelOptionCode");
		mapping.put("Ext Color", "extColorCode");
		mapping.put("Sales Model", "salesModelCode");
		mapping.put("Sales Type", "salesModelTypeCode");
		mapping.put("Sales Ext Color", "salesExtColorCode");
		mapping.put("Common Sales Model", "commonSalesModelCode");
		ObjectTablePane<FrameSpec> frameSpecTable = new ObjectTablePane<FrameSpec>(mapping.get(), false, true);
		frameSpecTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frameSpecTable.getTable().setEditingColumn(10);
		frameSpecTable.getTable().setCellSelectionEnabled(false);
		frameSpecTable.getTable().setRowSelectionAllowed(true);
		return frameSpecTable;
	}
	
	public ObjectTablePane<FrameSpec> getCommonFrameSpecTable() {
		return commonFrameSpecTable;
	}
	
	private JPanel createPurchaseContractPanel() {
		JPanel purchaseContractPanel = new JPanel(new MigLayout("insets 0 0 0 0"));
		purchaseContractPanel.add(this.getPurchaseContractTable(), "grow, push, wrap");
		purchaseContractPanel.add(this.getButtonPanel(), "center");
		return purchaseContractPanel;
	}
	
	private ObjectTablePane<PurchaseContract> getPurchaseContractTable() {
		if (this.purchaseContractTable == null) {		
			ColumnMappings columnMappings = ColumnMappings.with("Sales Model", "salesModelCode")
				.put("Sales Ext Color", "salesExtColorCode")
				.put("Purchase Contract", "purchaseContractNumber")
				.put("Order Unit", Integer.class, "orderUnit", true)
				.put("Ship Unit", "shipUnit")
				.put("Due Date", "orderDueDate");
			
			this.purchaseContractTable = new ObjectTablePane<PurchaseContract>(columnMappings.get(), false, false);
			System.out.println(this.purchaseContractTable.getTable().getModel().getClass());
			this.purchaseContractTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.purchaseContractTable.getTable().setCellSelectionEnabled(false);
			this.purchaseContractTable.getTable().setName("PurchaseContractTable");
			this.purchaseContractTable.setBorder(new TitledBorder("Purchase Contract"));
			this.purchaseContractTable.getTable().setRowSelectionAllowed(true);
			this.purchaseContractTable.getTable().putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			this.purchaseContractTable.getTable().setDefaultRenderer(Object.class, new PurchaseContractCellRenderer());
		}
		return this.purchaseContractTable;
	}
	
	private JPanel getButtonPanel() {
		if (this.buttonsPanel == null) {
			this.buttonsPanel = new JPanel(new MigLayout());
			this.buttonsPanel.add(this.getCreateButton(),"");
			this.buttonsPanel.add(this.getSaveButton(),"");
			this.buttonsPanel.add(this.getUndoButton(),"");
		}
		return this.buttonsPanel;
	}
	
	private JButton getCreateButton() {
		if (this.createButton == null) {
			this.createButton = new JButton("Create");
			this.createButton.setName("CreateButton");
			this.createButton.setEnabled(false);
		}
		return this.createButton;
	}
	
	private JButton getSaveButton() {
		if (this.saveButton == null) {
			this.saveButton = new JButton("Save");
			this.saveButton.setName("SaveButton");
			this.saveButton.setEnabled(false);
		}
		return this.saveButton;
	}
	
	private JButton getUndoButton() {
		if (this.undoButton == null) {
			this.undoButton = new JButton("Undo");
			this.undoButton.setName("UndoButton");
			this.undoButton.setEnabled(false);
		}
		return this.undoButton;
	}
	

	private void addListeners() {
		for(LabeledListBox lbox : ymtoSelectPanel.getColumnBoxsList()){
			lbox.getComponent().addListSelectionListener(
				new ListSelectionListener(){
					public void valueChanged(ListSelectionEvent e){
						setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						getMainWindow().clearMessage();
						filteredFrameSpecTable.clearSelection();
						refreshFilteredFrameSpecTable();						
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			);
			if (lbox == ymtoSelectPanel.getComponent(0)) {
				lbox.getComponent().addListSelectionListener(
					new ListSelectionListener(){
						public void valueChanged(ListSelectionEvent e){
							setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
							commonFrameSpecTable.clearSelection();
							refreshCommonFrameSpecTable();						
							setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
					}
				);
			}
		}
		this.filteredFrameSpecTable.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (filteredFrameSpecTable.getSelectedItem() != null) {
					getMainWindow().clearMessage();
					commonFrameSpecTable.clearSelection();
					purchaseContractTable.clearSelection();
					refreshPurchaseContractTable();
					enableButtons();
					refreshCommonFrameSpecTable();
					getCreateButton().setEnabled(true);
				} else
					getCreateButton().setEnabled(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
		});
		this.commonFrameSpecTable.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				if (commonFrameSpecTable.getSelectedItem() != null) {
					getMainWindow().clearMessage();
					filteredFrameSpecTable.clearSelection();
					purchaseContractTable.clearSelection();
					refreshPurchaseContractTable();
					enableButtons();
				}
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
		});
		this.purchaseContractTable.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				if (purchaseContractTable.getTable().isEditing())
					purchaseContractTable.getTable().getCellEditor().stopCellEditing();
			}
		});
		
		this.purchaseContractTable.addTableModelListener(this);
		this.createButton.addActionListener(this);
		this.saveButton.addActionListener(this);
		this.undoButton.addActionListener(this);
		this.datePanel.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.saveButton)) this.saveChanges();
		else if(e.getSource().equals(this.createButton)) this.createContract();
		else if(e.getSource().equals(this.undoButton)) this.refreshPurchaseContractTable();
		else if(e.getSource().equals(this.datePanel)) this.refreshPurchaseContractTable();
	}
	
	private void refreshFilteredFrameSpecTable() {
		String selectedYear = this.ymtoSelectPanel.getSelectedModelYearCode();
		String selectedModel = this.ymtoSelectPanel.getSelectedModelCode();
		String selectedType = this.ymtoSelectPanel.getSelectedModelTypeCode();
		String selectedOption = this.ymtoSelectPanel.getSelectedModelOptionCode();
		String selectedExtColor = this.ymtoSelectPanel.getSelectedExtColorCode();
		String selectedIntColor = this.ymtoSelectPanel.getSelectedIntColorCode();
		List<FrameSpec> filteredRecords = getFilteredRecords(selectedYear, selectedModel, selectedType, selectedOption, selectedExtColor, selectedIntColor);
		this.filteredFrameSpecTable.reloadData(filteredRecords);
		this.refreshCommonFrameSpecTable();
	}
	
	private void refreshCommonFrameSpecTable() {
		List<FrameSpec> frameSpecs = new ArrayList<FrameSpec>();
		List<FrameSpec> filteredFrameSpecs = new ArrayList<FrameSpec>();
		FrameSpec selectedFrameSpec = this.filteredFrameSpecTable.getSelectedItem();
		if (selectedFrameSpec != null) {
			String salesModelCode = selectedFrameSpec.getSalesModelCode();
			String salesTypeCode = selectedFrameSpec.getSalesModelTypeCode();
			String salesExtColorCode = selectedFrameSpec.getSalesExtColorCode();
			String commonSalesModelCode = selectedFrameSpec.getCommonSalesModelCode();
			if (commonSalesModelCode != null)
				frameSpecs.addAll(this.getFrameSpecDao().findAllBySalesModelTypeExtColor(commonSalesModelCode.trim(), salesTypeCode.trim(), salesExtColorCode.trim()));
			else 
				frameSpecs.addAll(this.getFrameSpecDao().findAllBySalesCommonModelTypeExtColor(salesModelCode.trim(), salesTypeCode.trim(), salesExtColorCode.trim()));
			frameSpecs.addAll(this.getFrameSpecDao().findAllBySalesModelTypeExtColor(salesModelCode.trim(), salesTypeCode.trim(), salesExtColorCode.trim()));
			for (FrameSpec frameSpec : frameSpecs) {
				if (	frameSpec.getModelYearCode().equals(selectedFrameSpec.getModelYearCode()) &&
						frameSpec.getModelCode().equals(selectedFrameSpec.getModelCode()) &&
						frameSpec.getModelTypeCode().equals(selectedFrameSpec.getModelTypeCode()) &&
						frameSpec.getModelOptionCode().equals(selectedFrameSpec.getModelOptionCode()) &&
						frameSpec.getExtColorCode().equals(selectedFrameSpec.getExtColorCode())){
					continue;
				} else {
					filteredFrameSpecs.add(frameSpec);
				}
			}
		}
		this.commonFrameSpecTable.reloadData(filteredFrameSpecs);
		this.refreshPurchaseContractTable();
	}
	
	private List<FrameSpec> getFilteredRecords(String year, String model, String type, String option, String extColor, String intColor){
		List<FrameSpec> filteredRecords = new SortedArrayList<FrameSpec>();
		if (!StringUtils.isBlank(year)) {
			if (model == null) model = "*";
			if (type == null) type = "*";
			if (option == null) option = "*";
			if (extColor == null) extColor = "*";
			if (intColor == null) intColor = "*";
			filteredRecords = ServiceFactory.getDao(FrameSpecDao.class).findAllByYMTOCWildCard(year, model, type, option, extColor, intColor);
		} return filteredRecords;
	}
	
	private void refreshPurchaseContractTable() {
		ArrayList<PurchaseContract> purchaseContracts = new ArrayList<PurchaseContract>();
		FrameSpec selectedSpec = null;
		
		if ((selectedSpec = this.filteredFrameSpecTable.getSelectedItem()) == null)
			selectedSpec = this.commonFrameSpecTable.getSelectedItem();
		
		if (selectedSpec != null) {
			String commonSalesModelCode = selectedSpec.getCommonSalesModelCode();
			String salesModelCode = selectedSpec.getSalesModelCode().trim();
			String salesTypeCode = selectedSpec.getSalesModelTypeCode().trim();
			String salesExtColorCode = selectedSpec.getSalesExtColorCode().trim();
			if (!StringUtils.isBlank(commonSalesModelCode))
				purchaseContracts.addAll(this.getPurchaseContractDao().findAllBySalesMtcAndOrderDueDate(commonSalesModelCode, salesTypeCode, salesExtColorCode, this.getSelectedDate()));
			purchaseContracts.addAll(this.getPurchaseContractDao().findAllBySalesMtcAndOrderDueDate(salesModelCode, salesTypeCode, salesExtColorCode, this.getSelectedDate()));
		}
		
		this.purchaseContractTable.reloadData(purchaseContracts);
		
		this.orderUnitsByPurchaseContract = new HashMap<Long,Integer>();
		for (PurchaseContract purchaseContract : purchaseContracts) {
			this.orderUnitsByPurchaseContract.put(purchaseContract.getPurchaseContractId(), purchaseContract.getOrderUnit());
		}
	}
	
	public java.sql.Date getSelectedDate() {
		Date selectedDate = (java.util.Date)datePanel.getModel().getValue();
		if (selectedDate == null) return null;
		return new java.sql.Date(selectedDate.getTime());
	}
	

	public class FilteredFrameSpecCellRenderer extends DefaultTableCellRenderer {    
		private static final long serialVersionUID = 1L;
        
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column > 5 && column <= 8){
				if (isSelected)	this.setBackground(new Color(250, 215, 160));
				else this.setBackground(new Color(252, 243, 207));
				this.setForeground(table.getForeground());
			} else if (column > 8){
				if (isSelected)	this.setBackground(new Color(174, 214, 241));
				else this.setBackground(new Color(214, 234, 248));
				this.setForeground(table.getForeground());
			} else if (isSelected) {
	            this.setBackground(table.getSelectionBackground());
	            this.setForeground(table.getSelectionForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}
	
	
	public class FilteredCommonFrameSpecCellRenderer extends DefaultTableCellRenderer {    
		private static final long serialVersionUID = 1L;
        
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column > 4 && column <= 7){
				if (isSelected)	this.setBackground(new Color(250, 215, 160));
				else this.setBackground(new Color(252, 243, 207));
				this.setForeground(table.getForeground());
			} else if (column > 7){
				if (isSelected)	this.setBackground(new Color(174, 214, 241));
				else this.setBackground(new Color(214, 234, 248));
				this.setForeground(table.getForeground());
			} else if (isSelected) {
	            this.setBackground(table.getSelectionBackground());
	            this.setForeground(table.getSelectionForeground());
			} else {
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}
	
	public class PurchaseContractCellRenderer extends DefaultTableCellRenderer {    
	private static final long serialVersionUID = 1L;
    SimpleDateFormat f = new SimpleDateFormat("MM/dd/yy");
	
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) { 
			if( value instanceof Date) value = f.format(value);
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (column != 3) {
				if (isSelected) this.setBackground(table.getSelectionBackground());	
				else this.setBackground(new Color(243, 243, 243));
			}
			return this;
		}
	}
	
	public void tableChanged(TableModelEvent e) {
		this.enableButtons();
	}
	
	private void enableButtons() {
		Boolean valuesChanged = false;
		for (PurchaseContract purchaseContract : this.getPurchaseContractTable().getItems()) {
			int origOrderUnits = this.orderUnitsByPurchaseContract.get(purchaseContract.getPurchaseContractId());
			if (origOrderUnits != purchaseContract.getOrderUnit())
				valuesChanged = true;
		}
		this.saveButton.setEnabled(valuesChanged);
		this.undoButton.setEnabled(valuesChanged);
	}
	
	private void createContract() {
		//call deliveryordercreatedialog
		DeliveryOrderCreateDialog createDeliveryOrderDialog = new DeliveryOrderCreateDialog(this, "Create Delivery Order");
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - createDeliveryOrderDialog.getWidth()) / 2;
		final int y = (screenSize.height - createDeliveryOrderDialog.getHeight()) / 2;
		createDeliveryOrderDialog.setLocation(x, y);
		createDeliveryOrderDialog.setModal(true);
		createDeliveryOrderDialog.setVisible(true);
		
		this.refreshPurchaseContractTable();
	}
	
	private void saveChanges() {
		ArrayList<PurchaseContract> changedPurchaseContracts = new ArrayList<PurchaseContract>();
		String msg = "Order units can not be lowered.\n\nThe following changes will not be saved:\n";
		StringBuilder sb = new StringBuilder();
		for (PurchaseContract purchaseContract : this.getPurchaseContractTable().getItems()) {
			int origOrderUnits = this.orderUnitsByPurchaseContract.get(purchaseContract.getPurchaseContractId());
			if (origOrderUnits	>	purchaseContract.getOrderUnit()) {
				sb.append(	"purchase contract:" + purchaseContract.getPurchaseContractNumber() +
							"  original value:" + origOrderUnits +
							"  new value:" + purchaseContract.getOrderUnit() + "\n");
			} else if (origOrderUnits < purchaseContract.getOrderUnit()) {
				changedPurchaseContracts.add(purchaseContract);
			}
		}
		if (!StringUtils.isEmpty(sb.toString()))
			JOptionPane.showMessageDialog(this, msg + sb.toString());
		ServiceFactory.getDao(PurchaseContractDao.class).updateAll(changedPurchaseContracts);
		this.refreshPurchaseContractTable();
		this.enableButtons();
	}
	
	private FrameSpecDao getFrameSpecDao() {
		if (this.frameSpecDao == null)
			this.frameSpecDao = ServiceFactory.getDao(FrameSpecDao.class);
		return this.frameSpecDao;
	}
	
	private PurchaseContractDao getPurchaseContractDao() {
		if (this.purchaseContractDao == null)
			this.purchaseContractDao = ServiceFactory.getDao(PurchaseContractDao.class);
		return this.purchaseContractDao;
	}
	
	public Logger getLogger() {
		return Logger.getLogger();
	}
}
