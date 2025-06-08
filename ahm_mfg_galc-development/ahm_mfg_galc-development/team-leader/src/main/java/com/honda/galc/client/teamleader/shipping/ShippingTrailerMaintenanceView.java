package com.honda.galc.client.teamleader.shipping;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.ShippingTrailerDao;
import com.honda.galc.dao.product.ShippingTrailerRackDao;
import com.honda.galc.entity.enumtype.TrailerStatus;
import com.honda.galc.entity.product.ShippingTrailer;
import com.honda.galc.entity.product.ShippingTrailerRack;
import com.honda.galc.entity.product.ShippingTrailerRack.RackType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3>ShippingTrailerMaintenanceView Class description</h3>
 * <p> ShippingTrailerMaintenanceView description </p>
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
 * Feb 24, 2015
 *
 *
 */
public class ShippingTrailerMaintenanceView extends ApplicationMainPanel implements ActionListener, ListSelectionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<ShippingTrailer> trailerPane;
	
	private LabeledTextField trailerNumberField;
		
	private LabeledTextField capacityField ;
	private LabeledComboBox trailerStatusComboBox = new LabeledComboBox("Trailer Status");
	
	private Map<RackType,LabeledTextField> rackTextFieldMap = new HashMap<RackType, LabeledTextField>();
	
	private JButton saveButton = new JButton("Save");
	private JButton deleteButton = new JButton("Delete");
	
	private static int TYPE_NUMERIC = 1;
	private static int TYPE_UPPERCASE = 2;
	
	
	public ShippingTrailerMaintenanceView(DefaultWindow window) {
		super(window);
		initComponents();
		window.pack();
		addListeners();
		loadData();
	}
	
	private void initComponents(){
		setLayout(new MigLayout("insets 20 200 20 200", "[grow,fill]"));
		initInputFields();
		add(new JLabel("Select Trailer Number to be Processed:"),"span 2,wrap");
		add(trailerPane = createTrailerPane(),"span,wrap");
		add(trailerNumberField);
		add(rackTextFieldMap.get(RackType.Blue),"wrap");
		add(capacityField);
		add(rackTextFieldMap.get(RackType.Gray),"wrap");
		add(trailerStatusComboBox);
		add(rackTextFieldMap.get(RackType.Hanger),"wrap");
		add(createButtonPanel(),"span");
	}
	
	private void loadData(){
		List<ShippingTrailer> allTrailers = ServiceFactory.getDao(ShippingTrailerDao.class).findAll();
		List<ShippingTrailer> trailers = new SortedArrayList<ShippingTrailer>("getTrailerNumber");
		for(ShippingTrailer trailer : allTrailers) {
			if(!trailer.getStatus().equals(TrailerStatus.IN_USE)) trailers.add(trailer);
		}
		trailerPane.getTable().clearSelection();
		trailerPane.reloadData(trailers);
		trailerPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	private void initInputFields() {
		int labelWidth = 150;
	
		trailerNumberField = createLabeledTextField("Trailer Number",labelWidth,TYPE_UPPERCASE, 0);
		trailerNumberField.getComponent().getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  trailerNumberChanged();
			  }
			  public void removeUpdate(DocumentEvent e) {
					 trailerNumberChanged();
			  }
			  public void insertUpdate(DocumentEvent e) {
					 trailerNumberChanged();
			  }
		});	  
		capacityField = createLabeledTextField("Capacity",labelWidth,TYPE_NUMERIC,2);
		rackTextFieldMap.put(RackType.Blue, createLabeledTextField("Blue Carrier",labelWidth,TYPE_NUMERIC,2));
		rackTextFieldMap.put(RackType.Gray, createLabeledTextField("Gray Carrier",labelWidth,TYPE_NUMERIC,2));
		rackTextFieldMap.put(RackType.Hanger, createLabeledTextField("Engine Carrier",labelWidth,TYPE_NUMERIC,2));
		
		trailerStatusComboBox.setFont(getLabelFont());
		trailerStatusComboBox.setLabelPreferredWidth(labelWidth);
		trailerStatusComboBox.getComponent().addItem(TrailerStatus.AVAILABLE);
		trailerStatusComboBox.getComponent().addItem(TrailerStatus.ON_HOLD);
		
	}
	
	private LabeledTextField createLabeledTextField(String label,int labelWidth, int type, int limit) {
		LabeledTextField textField = new LabeledTextField(label);
		if(type == TYPE_NUMERIC) textField.setNumeric(limit);
		else if(type == TYPE_UPPERCASE) textField.setUpperCaseField(true);
		textField.setFont(getLabelFont());
		textField.setLabelPreferredWidth(labelWidth);
		textField.getComponent().setHorizontalAlignment(JTextField.RIGHT);
	//	textField.getComponent().setBackground(Color.blue);
		textField.getComponent().setEnabled(true);
		textField.getComponent().setDisabledTextColor(Color.black);
		return textField;
	}
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,20);
	}
	
	private void addListeners() {
		trailerPane.getTable().getSelectionModel().addListSelectionListener(this);
		trailerNumberField.getComponent().addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
	}
	
	private void trailerNumberChanged() {
		String trailerNumber = trailerNumberField.getComponent().getText();
		ShippingTrailer trailer = null;
		trailer = ServiceFactory.getDao(ShippingTrailerDao.class).findByKey(trailerNumber);
		if(trailer != null && trailer.getStatus() == TrailerStatus.IN_USE) {
			setErrorMessage("Trailer " + trailerNumber + " is in use");
			return;
		}
		ShippingTrailer shippingTrailer = trailerPane.getSelectedItem();
		boolean enableDeleteFlag = shippingTrailer != null && shippingTrailer.getTrailerNumber().equalsIgnoreCase(trailerNumber);
		deleteButton.setEnabled(enableDeleteFlag);
	}
	
	private void saveChange() {
		String trailerNumber = trailerNumberField.getComponent().getText();
		if(StringUtils.isEmpty(trailerNumber)) {
			setErrorMessage("Trailer Number is empty");
			return;
		}
		ShippingTrailer trailer = null;
		trailer = ServiceFactory.getDao(ShippingTrailerDao.class).findByKey(trailerNumber);
		if(trailer != null && trailer.getStatus() == TrailerStatus.IN_USE) {
			setErrorMessage("Trailer " + trailerNumber + " is in use");
			return;
		}
		
		saveTrailer(trailerNumber);
		loadData();
		setMessage("Saved the trailer " + trailerNumber + " successfully");
	}
	
	private void saveTrailer(String trailerNumber) {
		ShippingTrailer trailer = new ShippingTrailer();
		trailer.setTrailerNumber(trailerNumber);
		trailer.setTrailerCapacity(new Integer(capacityField.getComponent().getText()));
		trailer.setStatus((TrailerStatus)trailerStatusComboBox.getComponent().getSelectedItem());
		
		ServiceFactory.getDao(ShippingTrailerDao.class).save(trailer);
		ServiceFactory.getDao(ShippingTrailerRackDao.class).saveAll(createShippingTrailerRacks(trailerNumber));
		
	}
	
	private void deleteTrailer() {
		String trailerNumber = trailerNumberField.getComponent().getText();
		if(StringUtils.isEmpty(trailerNumber)) {
			setErrorMessage("Trailer Number is empty");
			return;
		}
		ShippingTrailer trailer = null;
		trailer = ServiceFactory.getDao(ShippingTrailerDao.class).findByKey(trailerNumber);
		if(trailer != null && trailer.getStatus() == TrailerStatus.IN_USE) {
			setErrorMessage("Trailer " + trailerNumber + " is in use");
			return;
		}
		if(MessageDialog.confirm(this, "Are you sure to delete trailer " + trailerNumber)) {
			ServiceFactory.getDao(ShippingTrailerRackDao.class).deleteRacks(trailerNumber);
			ServiceFactory.getDao(ShippingTrailerDao.class).remove(trailer);
			loadData();
		}
		setMessage("Removed the trailer " + trailerNumber + " successfully");
	}
	
	private JPanel createButtonPanel() {
		saveButton.setFont(new Font("sansserif", 1, 18));
		deleteButton.setFont(new Font("sansserif", 1, 18));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(200));
		panel.add(Box.createHorizontalStrut(20));
		panel.add(saveButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(deleteButton);
		panel.add(Box.createHorizontalStrut(200));
		return panel;
	}
	
	private ObjectTablePane<ShippingTrailer> createTrailerPane() {
		ColumnMappings clumnMappings = ColumnMappings
			.with("Trailer Number","trailerNumber")
			.put("Capacity","trailerCapacity")
			.put("Trailer Status","status");
		
		ObjectTablePane<ShippingTrailer> pane = new ObjectTablePane<ShippingTrailer>(clumnMappings.get(),false);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(saveButton)) saveChange();
		else if(e.getSource().equals(deleteButton)) deleteTrailer();
	}
	
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		ShippingTrailer trailer = trailerPane.getSelectedItem();
		if(trailer == null) return;
		trailerNumberField.getComponent().setText(trailer.getTrailerNumber());
		capacityField.getComponent().setText(Integer.toString(trailer.getTrailerCapacity()));
		trailerStatusComboBox.getComponent().setSelectedItem(trailer.getStatus());
		
		List<ShippingTrailerRack> shippingRacks = ServiceFactory.getDao(ShippingTrailerRackDao.class).findAllByTrailerNumber(trailer.getTrailerNumber());
		
		for(Map.Entry<RackType, LabeledTextField> entry :rackTextFieldMap.entrySet()) {
			int capacity = getRackCapacity(shippingRacks, entry.getKey());
			entry.getValue().getComponent().setText(Integer.toString(capacity));
		}
		
	}
	
	private int getRackCapacity(List<ShippingTrailerRack> shippingRacks, RackType rackType) {
		for(ShippingTrailerRack rack : shippingRacks) {
			if(rackType.equals(rack.getRackType())) return rack.getQuantity();
		}
		return 0;
	}
	
	private List<ShippingTrailerRack> createShippingTrailerRacks(String trailerNumber) {
		List<ShippingTrailerRack> racks = new ArrayList<ShippingTrailerRack>();
		for(Map.Entry<RackType, LabeledTextField> entry :rackTextFieldMap.entrySet()) {
			int capacity = new Integer(entry.getValue().getComponent().getText());
			entry.getValue().getComponent().setText(Integer.toString(capacity));
			racks.add(new ShippingTrailerRack(trailerNumber,entry.getKey(),capacity));
		}
		
		return racks;
	}

}
