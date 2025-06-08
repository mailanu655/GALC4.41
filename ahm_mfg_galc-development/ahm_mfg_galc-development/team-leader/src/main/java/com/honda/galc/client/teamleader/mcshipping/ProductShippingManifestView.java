package com.honda.galc.client.teamleader.mcshipping;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.ProductShipping;

import net.miginfocom.swing.MigLayout;

/**
 * <h3>ProductShippingManifestView Class description</h3>
 * <p> ProductShippingManifestView description </p>
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
 * @author Paul Chou<br>
 * Jul. 27, 2022
 */
public class ProductShippingManifestView 
              extends AbstractView<ProductShippingManifestModel, ProductShippingManifestController> {
	
	private static final long serialVersionUID = 1L;
	
	protected LabeledComboBox destinationComboBox;
	protected LabeledComboBox trailerComboBox;
	protected ObjectTablePane<ProductShipping> productList;
	protected ObjectTablePane<ProductShipping> dunnageList;
	protected JButton sendAllButton;
	protected JButton sendButton;
	protected JPanel datePickerPane;
	private JDatePickerImpl jDatePicker;
	private JLabel dateLabel;
	public ProductShippingManifestView(DefaultWindow mainWindow) {
		super(mainWindow);
	}
	
	public void prepare() {
		initComponents();
		super.prepare();
		mapActions();
	}
	

	
	public void initComponents() {
		destinationComboBox = createLabeledCombobox("Destination", 200,Fonts.DIALOG_BOLD_24, true);
		trailerComboBox = createLabeledCombobox("Trailer #", 200, Fonts.DIALOG_BOLD_24, true);
		
		productList = createProductListPane("Product List");
		dunnageList = createDunnageListPane("Dunnage List");
		sendAllButton = createButton("Send All", Fonts.DIALOG_BOLD_24);
		sendButton = createButton("Send Selected", Fonts.DIALOG_BOLD_24);
		sendAllButton.setEnabled(false);
		sendButton.setEnabled(false);
		datePickerPane = createDatePickerPane();
		
		setLayout(new MigLayout("insets 10 100 1 100", "[grow,fill]"));
		add(destinationComboBox,"span 2, wrap");
		add(trailerComboBox,"span 2");
		add(datePickerPane,"wrap");
		add(createDunnageListPanel("Dunnage List",dunnageList),"span 2");
		add(createProductListPanel("Product List",productList), "wrap");
		add(sendAllButton,"width 200:200:200,align center, span 2");
		add(sendButton,"width 200:200:200,align center");
	}
	
	

	private JPanel createDatePickerPane() {
		datePickerPane = new JPanel(new BorderLayout()); 
		datePickerPane.add(getDateLabel(), BorderLayout.WEST);
		datePickerPane.add(createDatePicker(),BorderLayout.CENTER);
		return datePickerPane;
	}

	protected void mapActions() {
		sendAllButton.addActionListener(getController());
		sendButton.addActionListener(getController());
		trailerComboBox.getComponent().addActionListener(getController());
		destinationComboBox.getComponent().addActionListener(getController());
		productList.addListSelectionListener(getController());
		dunnageList.addListSelectionListener(getController());
	}
	
	public void reload() {
		getController().findShippingDestinations();
	}
	
	public void start() {
		
	}
	
	private JPanel createDunnageListPanel(String title, ObjectTablePane<ProductShipping>  dunnageListPane) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder(title));
		panel.add(dunnageListPane,BorderLayout.CENTER);
		
		return panel;
	}
	
	private JPanel createProductListPanel(String title, ObjectTablePane<ProductShipping> productListPane) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder(title));
		panel.add(productListPane,BorderLayout.CENTER);
		return panel;
	}
	
	private ObjectTablePane<ProductShipping> createDunnageListPane(String name) {
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row")
			.put("Dunnage", "dunnage").put("Product Type","productType").put("Count","count");
		
		ObjectTablePane<ProductShipping> tablePane = new ObjectTablePane<ProductShipping>(clumnMappings.get(),true);
		
		tablePane.getTable().setName(name);
		tablePane.getTable().setRowHeight(30);
		tablePane.getTable().setFont(Fonts.DIALOG_PLAIN_30);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return tablePane;
	}
	
	private ObjectTablePane<ProductShipping> createProductListPane(String name) {
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row")
			.put("Product Id", "productId");
		
		ObjectTablePane<ProductShipping> tablePane = new ObjectTablePane<ProductShipping>(clumnMappings.get(),true);
		
		tablePane.getTable().setName(name);
		tablePane.getTable().setRowHeight(30);
		tablePane.getTable().setFont(Fonts.DIALOG_PLAIN_24);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return tablePane;
	}
	
	private JLabel getDateLabel() {
		if (dateLabel == null) {
			dateLabel = new JLabel();
			dateLabel.setFont(Fonts.DIALOG_BOLD_24);
			dateLabel.setText("Date: ");
		}
		return dateLabel;
	}
	
	private JDatePickerImpl createDatePicker() {
		UtilDateModel model = new UtilDateModel(new Date());
		model.setSelected(true);
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl jDatePanelImpl = new JDatePanelImpl(model, p);
		jDatePanelImpl.setShowYearButtons(true);
		jDatePicker = new JDatePickerImpl(jDatePanelImpl, new DateComponentFormatter());
		jDatePicker.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			    getController().loadTrailers();
				
			}
		});

		return jDatePicker;
	}
	
    // ----- getter & setter ------
	public JDatePickerImpl getjDatePicker() {
		return jDatePicker;
	}
	
}
