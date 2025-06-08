package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.springframework.util.StringUtils;

import com.honda.galc.client.glassload.GlassLoadPropertyBean;
import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.gts.GtsCarrierDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.GenericDaoService;
import com.honda.galc.service.Parameters;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

public class GlassCarrierMaintenancePanel extends ApplicationMainPanel implements ActionListener{
	
	private static String SQL = "SELECT * FROM GTS_CARRIER_TBX WHERE TRACKING_AREA = ?1 order by STATUS ASC";
	private ObjectTablePane<MultiValueObject<GtsCarrier>> carrierTablePane;

	private List<GtsCarrier> carriers;

	private JButton addBeforeButton= new JButton("Add Before");
	private JButton addAfterButton= new JButton("Add After");
	private JButton removeButton= new JButton("Remove");
	private JButton modifyButton= new JButton("Modify");
	private JButton upButton= new JButton("Up");
	private JButton downButton= new JButton("Down");

	private LabeledUpperCaseTextField sequenceTextField;
	private LabeledUpperCaseTextField carrierTextField;
	private LabeledUpperCaseTextField vinTextField;

	public GlassCarrierMaintenancePanel(MainWindow window)  {
		super(window);
		initComponents();
		
		loadData();
	}
	
	protected void initComponents() {
		setLayout(new BorderLayout());
		add(createCarrierPanel(),BorderLayout.CENTER);		
	}
	
	protected void loadData() {
		carriers = fetchGlassCarriers();
		
		List<MultiValueObject<GtsCarrier>> list = new ArrayList<MultiValueObject<GtsCarrier>>();

		for(GtsCarrier carrier : carriers) {
			Frame frame = null;
			if(!StringUtils.isEmpty(carrier.getProductId())) {
				frame = fetchFrame(carrier.getProductId());
			}
			List<Object> values = prepareCarrierValues(carrier, frame);
			MultiValueObject<GtsCarrier> valueObject = new MultiValueObject<GtsCarrier>(carrier, values);
			list.add(valueObject);

		}
		
		carrierTablePane.reloadData(list);
		
		if(carrierTablePane.getTable().getSelectedRow() < 0)
			carrierTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);

	}
	
	private JPanel createCarrierPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(carrierTablePane = createCarrierTablePane(),BorderLayout.CENTER);
		panel.add(createCarrierButtonPanel(),BorderLayout.SOUTH);
		
		return panel;
	}
	
	
	private JPanel createCarrierButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 30, 10));
		buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPanel.add(Box.createHorizontalStrut(100));
		buttonPanel.add(addBeforeButton);
		buttonPanel.add(addAfterButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(Box.createHorizontalStrut(100));
		
		addBeforeButton.addActionListener(this);
		addAfterButton.addActionListener(this);
		removeButton.addActionListener(this);
		modifyButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		return buttonPanel;

	}
	
	private ObjectTablePane<MultiValueObject<GtsCarrier>> createCarrierTablePane()  {
		ColumnMappings columnMappings = ColumnMappings.with(new String[] {"Seq","Carrier #","VIN","MTOC", "Process Point"});
		ObjectTablePane<MultiValueObject<GtsCarrier>> pane = new ObjectTablePane<MultiValueObject<GtsCarrier>>(
				"",columnMappings.get());
		
		pane.getTable().getTableHeader().setFont(Fonts.DIALOG_BOLD(getPropertyBean().getHeaderFontSize()));
		pane.getTable().setFont(new Font("sansserif", 1, getPropertyBean().getFontSize()));
		pane.getTable().setRowHeight(getPropertyBean().getItemHeight());
		pane.setAlignment(SwingConstants.CENTER);
				
		return pane;
	}
	
	public List<GtsCarrier> fetchGlassCarriers() {
		
		List<GtsCarrier> carriers = getService(GenericDaoService.class).findAll(SQL, Parameters.with("1",  "GLASS_LOAD"), GtsCarrier.class);
		
		return carriers;
	}
	
	public Frame fetchFrame(String productId) {
		if(StringUtils.isEmpty(productId))  return null;
		else {
			return getDao(FrameDao.class).findByKey(productId);
		}
	}
	
	protected List<Object> prepareCarrierValues(GtsCarrier carrier, Frame frame) {
		List<Object> values = new ArrayList<Object>();
		
		values.add(carrier.getStatusValue());
		values.add(carrier.getCarrierNumber());
		values.add(carrier.getProductId());
		values.add(frame == null ? "" : frame.getProductSpecCode());
		values.add(frame == null? "": frame.getLastPassingProcessPointId());
		return values;
	}
	
	private void addBeforeButtonClicked() {	
		addCarrier(true);
	}
	
	private void addAfterButtonClicked() {
		addCarrier(false);
	}
	
	private void upButtonClicked() {
		
		int selectionIndex = this.carrierTablePane.getTable().getSelectedRow();
		
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem().getKeyObject();
		int index = this.carriers.indexOf(selectedCarrier);
		
	    getMainWindow().getLogger().info("Carrier Maintenance - Move Up Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");
		
		GtsCarrier lastCarrier = this.carriers.get(index -1);
		
		int seq = lastCarrier.getStatusValue();
		
		lastCarrier.setStatusValue(selectedCarrier.getStatusValue());
		selectedCarrier.setStatusValue(seq);
		
		List<GtsCarrier> updatedCarriers = new ArrayList<GtsCarrier>();
		updatedCarriers.add(lastCarrier);
		updatedCarriers.add(selectedCarrier);
		
		updateCarriers(updatedCarriers);
		
		loadData();
		carrierTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex -1, selectionIndex -1);
	
	}
	
	private void downButtonClicked() {
		int selectionIndex = this.carrierTablePane.getTable().getSelectedRow();

		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem().getKeyObject();
		int index = this.carriers.indexOf(selectedCarrier);
		
	    getMainWindow().getLogger().info("Carrier Maintenance - Move Down Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		GtsCarrier nextCarrier = this.carriers.get(index +1);
		
		int seq = nextCarrier.getStatusValue();
		
		nextCarrier.setStatusValue(selectedCarrier.getStatusValue());
		selectedCarrier.setStatusValue(seq);
		
		List<GtsCarrier> updatedCarriers = new ArrayList<GtsCarrier>();
		updatedCarriers.add(nextCarrier);
		updatedCarriers.add(selectedCarrier);
		
		updateCarriers(updatedCarriers);
		
		loadData();
		
		carrierTablePane.getTable().getSelectionModel().setSelectionInterval(selectionIndex +1, selectionIndex +1);
	
	}
	
	private void removeButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem().getKeyObject();
		
		 getMainWindow().getLogger().info("Carrier Maintenance - Remove Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		this.carriers.remove(selectedCarrier);
		
		for(int i = 0; i<carriers.size() ; i++) {
			GtsCarrier carrier = carriers.get(i);
			carrier.setStatusValue(i + 1);
		}
		
		deleteCarrier(selectedCarrier);
		
		updateCarriers(carriers);
		
		loadData();
		
	}
	
	private void modifyButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem().getKeyObject();
		
		JPanel  aPanel= createPanel();
		sequenceTextField.getComponent().setText("" + selectedCarrier.getStatusValue());
		carrierTextField.getComponent().setText(selectedCarrier.getCarrierNumber());
		vinTextField.getComponent().setText(selectedCarrier.getProductId());
		vinTextField.getComponent().setEnabled(true);

		int result = JOptionPane.showConfirmDialog(this, aPanel, 
	               "Modify Carrier", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.CANCEL_OPTION) return;
	    
	    selectedCarrier.setProductId(vinTextField.getComponent().getText());

	    updateCarrier(selectedCarrier);  
	    
	    getMainWindow().getLogger().info("Carrier Maintenance - Modify Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		loadData();
		
	}
	
	
	private void addCarrier(boolean isBefore) {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem().getKeyObject();
		
		JPanel  aPanel= createPanel();
		
		int seq = isBefore ? selectedCarrier.getStatusValue() : selectedCarrier.getStatusValue() + 1;
		
		sequenceTextField.getComponent().setText(""+seq);
		
		int result = JOptionPane.showConfirmDialog(this, aPanel, 
	               "Add Carrier", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.CANCEL_OPTION) return;
	
	    String carrierId = carrierTextField.getComponent().getText();
	    if(StringUtils.isEmpty(carrierId)) {
	    	MessageDialog.showError(this, "Carrier # is empty");
	    	return;
	    }
	    
	    GtsCarrier carrier = containsCarrier(carrierTextField.getComponent().getText());		
	    if(carrier != null) {
	    	MessageDialog.showError(this, "Carrier # " + carrier.getCarrierNumber() + " is used by sequence " + carrier.getStatusValue());
	    	return;
	    }
	    
	    List<GtsCarrier> changedCarriers = new ArrayList<GtsCarrier>();
	    	    
	    for(GtsCarrier item: carriers) {
	    	if(item.getStatusValue() >= seq) {
	    		item.setStatusValue(item.getStatusValue() + 1);
	    		changedCarriers.add(item);
	    	}
	    }
	    
	    if(!changedCarriers.isEmpty()) updateCarriers(changedCarriers);
	    
	    selectedCarrier.getId().setCarrierNumber(carrierTextField.getComponent().getText());
	    selectedCarrier.setProductId(vinTextField.getComponent().getText());
	    if(isBefore) selectedCarrier.setStatusValue(selectedCarrier.getStatusValue() -1);
	    else selectedCarrier.setStatusValue(selectedCarrier.getStatusValue() +1);
	    
	    updateCarrier(selectedCarrier);
	    		
	    getMainWindow().getLogger().info("Carrier Maintenance - Add Carrier " + (isBefore?"Before" :"After"),
	    		". Added Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber() + "]");

	    loadData();
		
	}
	
	private GtsCarrier containsCarrier(String carrierId) {
		for(GtsCarrier carrier : carriers) {
			if(carrierId.equalsIgnoreCase(carrier.getCarrierNumber())) return carrier;
		}
		
		return null;
	}
	
	public void deleteCarrier(GtsCarrier carrier) {
		getDao(GtsCarrierDao.class).remove(carrier);
	}
	
	public void updateCarrier(GtsCarrier carrier) {
		getDao(GtsCarrierDao.class).save(carrier);
	}
	
	public void updateCarriers(List<GtsCarrier> carriers) {
		getDao(GtsCarrierDao.class).saveAll(carriers);
	}

	
	private JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		ViewUtil.setPreferredSize(360, 200, panel);
		
		sequenceTextField = createLabeledTextField("SEQ",100,getLabelFont(), 17, Color.cyan,false);
		carrierTextField = createLabeledTextField("Carrier #",100,getLabelFont(), 17, Color.green,true);
		vinTextField = createLabeledTextField("VIN",100,getLabelFont(), 17, Color.green,true);
		
		sequenceTextField.getComponent().setDocument(new NumericDocument(100));

		panel.add(Box.createVerticalStrut(30));	
		panel.add(sequenceTextField);
		panel.add(Box.createVerticalStrut(30));
		panel.add(carrierTextField);
		panel.add(Box.createVerticalStrut(30));
		panel.add(vinTextField);
		panel.add(Box.createVerticalStrut(30));
		
		return panel;
		
	}
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,22);
	}
	
	private LabeledUpperCaseTextField createLabeledTextField(String label,int labelWidth,Font font,int columnSize,Color backgroundColor,boolean enabled) {
		LabeledUpperCaseTextField labeledTextField =  new LabeledUpperCaseTextField(label);
		labeledTextField.setFont(font);
		labeledTextField.setLabelPreferredWidth(labelWidth);
		labeledTextField.getComponent().setMaximumLength(columnSize);
		labeledTextField.setInsets(0, 10, 0, 10);
		labeledTextField.getComponent().setFixedLength(false);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.CENTER);
		labeledTextField.getComponent().setBackground(backgroundColor);
		labeledTextField.getComponent().setEnabled(enabled);
		labeledTextField.getComponent().setDisabledTextColor(Color.black);
		return labeledTextField;
	}

	public List<Frame> fetchAFonVinList() {
		List<Frame> frames = getDao(FrameDao.class).findByTrackingStatus(getPropertyBean().getAfOnTrackingStatus());
		
		List<Frame> filteredFrames = new ArrayList<Frame>();
		
		for(Frame frame : frames) {
			if(frame.getAfOnSequenceNumber() != null) filteredFrames.add(frame);
		}
		
		List<Frame> items = null ;
		try {
			items =  new SortedArrayList<Frame>(filteredFrames, "getAfOnSequenceNumber");
			
		}catch(Throwable ex) {
			ex.printStackTrace();
		}
		
		return items;
	}
	
	public GlassLoadPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(GlassLoadPropertyBean.class, getApplicationId());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.addBeforeButton)) addBeforeButtonClicked();
		else if(e.getSource().equals(this.addAfterButton)) addAfterButtonClicked();		
		else if(e.getSource().equals(this.removeButton)) removeButtonClicked();
		else if(e.getSource().equals(this.modifyButton)) modifyButtonClicked();
		else if(e.getSource().equals(this.upButton)) upButtonClicked();
		else if(e.getSource().equals(this.downButton)) downButtonClicked();

	}


}
