package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.PrintFormDao;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.PrintForm;
import com.honda.galc.entity.conf.PrintFormId;
import com.honda.galc.entity.enumtype.DeviceType;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3>PrintFormPanel Class description</h3>
 * <p> PrintFormPanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Oct 27, 2015
 *
 *
 */
public class PrintFormPanel extends TabbedPanel implements ListSelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<PrintForm> printForms;
	private List<String> printDevices = new SortedArrayList<String>();
	
	private JButton addButton = new JButton("Add");
	private JButton saveButton = new JButton("Save");
	private JButton deleteButton = new JButton("Delete");
	
	private LabeledTextField formIdField;
	private LabeledComboBox printerComboBox = new LabeledComboBox("Printer");
	
	private boolean isNew = false;

	
	private ObjectTablePane<PrintForm> printFormTablePane;
	
	public PrintFormPanel(TabbedMainWindow mainWindow) {
		super("Print Form Config", KeyEvent.VK_P, mainWindow);
		initComponents();
	}	
	
	@Override
	public void onTabSelected() {
		if(isInitialized) return;
		loadData();
		addListeners();
		this.printFormTablePane.reloadData(this.printForms);
		this.printFormTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		
	}

	
	private void loadData() {
		this.printForms = getDao(PrintFormDao.class).findAll();
		List<Device> devices = getDao(DeviceDao.class).findAllByDeviceType(DeviceType.PRINTER);
		devices.addAll(getDao(DeviceDao.class).findAllByDeviceType(DeviceType.JASPER));
		devices.addAll(getDao(DeviceDao.class).findAllByDeviceType(DeviceType.MQ_PRINTER));
	    for(Device device:devices) {
	    	printDevices.add(device.getClientId());
	    }
	}
	
	private void initComponents() {
		setLayout(new MigLayout("insets 20 200 20 200", "[grow,fill]"));
		
		int labelWidth = 150;
		
		formIdField = createLabeledTextField("Form Id",labelWidth);
		printerComboBox.setFont(getLabelFont());
		printerComboBox.setLabelPreferredWidth(labelWidth);
	
		add(new JLabel("Select Print Form to be Processed:"),"span 2,wrap");
		add(printFormTablePane = createPrintFormTablePane(),"span,wrap");
		add(formIdField,"wrap");
		add(printerComboBox,"wrap");
		add(createButtonPanel(),"gapleft 200,span");
	
	}
	
	private void addListeners() {
		printFormTablePane.getTable().getSelectionModel().addListSelectionListener(this);
		formIdField.getComponent().addActionListener(this);
		addButton.addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
	}
	
	private ObjectTablePane<PrintForm> createPrintFormTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Form Id", "formId")
			.put("Printer", "destinationId")
			.put("Default Flag","isDefault");
		
		ObjectTablePane<PrintForm> tablePane = new ObjectTablePane<PrintForm>(clumnMappings.get(),false,true);
		
		tablePane.setBorder(new TitledBorder("Print Form List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private void configureTablePane(ObjectTablePane<?> tablePane) {
		tablePane.getTable().setRowHeight(22);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",14));
		tablePane.setAlignment(JLabel.CENTER);
		tablePane.getTable().setSelectionBackground(Color.green);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private JPanel createButtonPanel(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.setPreferredSize(new Dimension(200,60));
		buttonPanel.add(addButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(saveButton);
		buttonPanel.add(Box.createHorizontalStrut(10));
		buttonPanel.add(deleteButton);
		buttonPanel.add(Box.createHorizontalStrut(60));
		return buttonPanel;
    }
	
	private LabeledTextField createLabeledTextField(String label,int labelWidth) {
		LabeledTextField textField = new LabeledTextField(label);
		textField.setFont(getLabelFont());
		textField.setLabelPreferredWidth(labelWidth);
		textField.getComponent().setHorizontalAlignment(JTextField.RIGHT);
		textField.getComponent().setEnabled(true);
		textField.getComponent().setDisabledTextColor(Color.black);
		return textField;
	}
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,20);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(addButton)) addPrintForm();
		else if(e.getSource().equals(saveButton)) saveChange();
		else if(e.getSource().equals(deleteButton)) deletePrintForm();
	}

	private void deletePrintForm() {
		PrintForm form = printFormTablePane.getSelectedItem();
		if(form == null) return;
		if(MessageDialog.confirm(this, "Are you sure to delete the selected print form ")) {
			getDao(PrintFormDao.class).remove(form);
			logUserAction(REMOVED, form);
			setMessage("Print form " + form + "is deleted");
			refreshData();
		}	
	}


	private void saveChange() {
		String formId = formIdField.getComponent().getText();
		String printerId = (String)printerComboBox.getComponent().getSelectedItem(); 
		if(StringUtils.isEmpty(formId)) {
			MessageDialog.showError("Form Id is Empty");
			return;
		}
		
		if(isNew) {
			PrintForm form = new PrintForm();
			PrintFormId id = new PrintFormId();
			id.setFormId(formId);
			id.setDestinationId(printerId);
			form.setId(id);
			getDao(PrintFormDao.class).save(form);
			logUserAction(SAVED, form);
			setMessage("Form Id " + formId + " , printer Id " + printerId + " is created");
		}
		else {
			PrintForm form = printFormTablePane.getSelectedItem();
			getDao(PrintFormDao.class).updateDestinationId(formId,form.getDestinationId(),printerId);
			logUserAction(UPDATED, form);
			setMessage("Form Id " + formId + " , printer Id " + printerId + " is updated");
		};
		
		refreshData();
	}


	private void addPrintForm() {
		this.formIdField.setEnabled(true);
		this.formIdField.getComponent().setText("");
		this.isNew = true;
	}
	
	private void refreshData() {
		this.printForms = getDao(PrintFormDao.class).findAll();
		int selectionIndex = printFormTablePane.getTable().getSelectedRow();
		printFormTablePane.clearSelection();
		printFormTablePane.reloadData(this.printForms);
		printFormTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex, selectionIndex);
	}

	
	public void valueChanged(ListSelectionEvent e) {
		PrintForm form = printFormTablePane.getSelectedItem();
		if(form == null) return;
		this.formIdField.getComponent().setText(form.getId().getFormId());
		this.formIdField.setEnabled(false);
		this.printerComboBox.setModel(this.printDevices, findPrinterIndex(form.getId().getDestinationId()));
		this.isNew = false;
	}
	
	private int findPrinterIndex(String printer) {
		for(int i = 0; i< this.printDevices.size();i++) {
			String item = this.printDevices.get(i);
			if(item.equalsIgnoreCase(printer)) return i; 
		}
		return 0;
	}
	
	protected void setMessage(String message){
		if(getMainWindow() != null) getMainWindow().setMessage(message);
	}

}
