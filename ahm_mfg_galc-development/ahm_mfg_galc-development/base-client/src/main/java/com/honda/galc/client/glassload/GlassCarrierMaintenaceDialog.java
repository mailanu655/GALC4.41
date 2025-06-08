package com.honda.galc.client.glassload;

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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.springframework.util.StringUtils;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.gts.GtsCarrier;

import net.miginfocom.swing.MigLayout;

public class GlassCarrierMaintenaceDialog extends JDialog implements ActionListener{

	private ObjectTablePane<GtsCarrier> carrierTablePane;
	
	private List<GtsCarrier> carriers;
	
	private GlassLoadController controller;
	
	private JButton addBeforeButton= new JButton("Add Before");
	private JButton addAfterButton= new JButton("Add After");
	private JButton removeButton= new JButton("Remove");
	private JButton modifyButton= new JButton("Modify");
	private JButton upButton= new JButton("Up");
	private JButton downButton= new JButton("Down");
	private JButton closeButton= new JButton("Close");
	
	private LabeledUpperCaseTextField sequenceTextField;
	private LabeledUpperCaseTextField carrierTextField;
	private LabeledUpperCaseTextField vinTextField;
	
	protected boolean isDirty = false;
	
	
	public GlassCarrierMaintenaceDialog(GlassLoadController controller) {
		super(controller.getView().getMainWindow(),true);
		
		this.controller = controller;
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	//	setResizable(false);
		setTitle("Glass Carrier Maintenance Window");
		setSize(700, 400);
		setLocationRelativeTo(controller.getView().getMainWindow());
		
		initComponents();
//		mapActions();
		loadData();

	}
	
	private void initComponents() {
		
		JPanel panel = new JPanel(new MigLayout("insets 20 10 10 10", "[grow,fill]"));
		panel.add(carrierTablePane = createCarrierTablePane(),"width 300:300:300");
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		buttonPanel.add(addBeforeButton);
		buttonPanel.add(addAfterButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(upButton);
		buttonPanel.add(downButton);
		buttonPanel.add(closeButton);
		
		addBeforeButton.addActionListener(this);
		addAfterButton.addActionListener(this);
		removeButton.addActionListener(this);
		modifyButton.addActionListener(this);
		upButton.addActionListener(this);
		downButton.addActionListener(this);
		closeButton.addActionListener(this);
		
		panel.add(buttonPanel);
		
		
		setContentPane(panel);

	}

	private void loadData() {
		carriers = controller.getModel().fetchGlassCarriers();
		int index = this.carrierTablePane.getTable().getSelectedRow();
		if(index < 0) index = 0;
		this.carrierTablePane.reloadData(carriers);
		this.carrierTablePane.getTable().getSelectionModel().setSelectionInterval(index,index);
	}
	
	private ObjectTablePane<GtsCarrier> createCarrierTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Seq", "statusValue").put("Carrier #", "carrierNumber").put("VIN", "productId");
		
		ObjectTablePane<GtsCarrier> tablePane = new ObjectTablePane<GtsCarrier>(clumnMappings.get(),false);
		tablePane.setBorder(new TitledBorder("Carrier List"));
		configureTablePane(tablePane);
		return tablePane;
	}
	
	private void configureTablePane(ObjectTablePane<?> tablePane) {
		tablePane.getTable().setRowHeight(26);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",15));
		tablePane.setAlignment(JLabel.CENTER);
		tablePane.getTable().setSelectionBackground(Color.green);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void addBeforeButtonClicked() {	
		addCarrier(true);
	}
	
	private void addAfterButtonClicked() {
		addCarrier(false);
	}
	
	private void addCarrier(boolean isBefore) {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem();
		
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
	    
	    if(!changedCarriers.isEmpty())controller.getModel().updateCarriers(changedCarriers);
	    
	    selectedCarrier.getId().setCarrierNumber(carrierTextField.getComponent().getText());
	    selectedCarrier.setProductId(vinTextField.getComponent().getText());
	    if(isBefore) selectedCarrier.setStatusValue(selectedCarrier.getStatusValue() -1);
	    else selectedCarrier.setStatusValue(selectedCarrier.getStatusValue() +1);
	    controller.getModel().updateCarrier(selectedCarrier);
		
	    controller.getLogger().info("Carrier Maintenance - Add Carrier " + (isBefore?"Before" :"After"),
	    		". Added Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber() + "]");

	    loadData();
		
		isDirty = true;

	}
	
	private GtsCarrier containsCarrier(String carrierId) {
		for(GtsCarrier carrier : carriers) {
			if(carrierId.equalsIgnoreCase(carrier.getCarrierNumber())) return carrier;
		}
		
		return null;
	}
	
	
	private void removeButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem();
		
	    controller.getLogger().info("Carrier Maintenance - Remove Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		this.carriers.remove(selectedCarrier);
		
		for(int i = 0; i<carriers.size() ; i++) {
			GtsCarrier carrier = carriers.get(i);
			carrier.setStatusValue(i + 1);
		}
		
		controller.getModel().deleteCarrier(selectedCarrier);
		
		controller.getModel().updateCarriers(carriers);
		
		loadData();
		
		isDirty = true;
		
		
	}
	
	private void modifyButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem();
		
		JPanel  aPanel= createPanel();
		sequenceTextField.getComponent().setText("" + selectedCarrier.getStatusValue());
		carrierTextField.getComponent().setText(selectedCarrier.getCarrierNumber());
		vinTextField.getComponent().setText(selectedCarrier.getProductId());
		vinTextField.getComponent().setEnabled(true);

		int result = JOptionPane.showConfirmDialog(this, aPanel, 
	               "Modify Carrier", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.CANCEL_OPTION) return;
	    
	    selectedCarrier.setProductId(vinTextField.getComponent().getText());

	    controller.getModel().updateCarrier(selectedCarrier);  
	    
	    controller.getLogger().info("Carrier Maintenance - Modify Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		loadData();
		
		isDirty = true;
	}
	
	private void upButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem();
		int index = this.carriers.indexOf(selectedCarrier);
		
	    controller.getLogger().info("Carrier Maintenance - Move Up Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");
		
		GtsCarrier lastCarrier = this.carriers.get(index -1);
		
		int seq = lastCarrier.getStatusValue();
		
		lastCarrier.setStatusValue(selectedCarrier.getStatusValue());
		selectedCarrier.setStatusValue(seq);
		
		List<GtsCarrier> updatedCarriers = new ArrayList<GtsCarrier>();
		updatedCarriers.add(lastCarrier);
		updatedCarriers.add(selectedCarrier);
		
		controller.getModel().updateCarriers(updatedCarriers);
		
		loadData();
	
	}
	
	private void downButtonClicked() {
		GtsCarrier selectedCarrier  = this.carrierTablePane.getSelectedItem();
		int index = this.carriers.indexOf(selectedCarrier);
		
	    controller.getLogger().info("Carrier Maintenance - Move Down Carrier [" + selectedCarrier.getStatusValue() + "," + selectedCarrier.getCarrierNumber(),
	    		"," + selectedCarrier.getProductId() + "]");

		GtsCarrier nextCarrier = this.carriers.get(index +1);
		
		int seq = nextCarrier.getStatusValue();
		
		nextCarrier.setStatusValue(selectedCarrier.getStatusValue());
		selectedCarrier.setStatusValue(seq);
		
		List<GtsCarrier> updatedCarriers = new ArrayList<GtsCarrier>();
		updatedCarriers.add(nextCarrier);
		updatedCarriers.add(selectedCarrier);
		
		controller.getModel().updateCarriers(updatedCarriers);
		
		loadData();
	
	}
	
	private void closeButtonClicked() {
		this.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.addBeforeButton)) addBeforeButtonClicked();
		else if(e.getSource().equals(this.addAfterButton)) addAfterButtonClicked();		
		else if(e.getSource().equals(this.removeButton)) removeButtonClicked();
		else if(e.getSource().equals(this.modifyButton)) modifyButtonClicked();
		else if(e.getSource().equals(this.upButton)) upButtonClicked();
		else if(e.getSource().equals(this.downButton)) downButtonClicked();
		else if(e.getSource().equals(this.closeButton)) closeButtonClicked();			
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
	
}
