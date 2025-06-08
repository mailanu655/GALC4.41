package com.honda.galc.client.glassload;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.gts.GtsCarrier;
import com.honda.galc.entity.product.Frame;

public class GlassReassignDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;

	private GlassLoadController controller;
	
	private ObjectTablePane<Frame> afonVinTablePane;
	
	private List<GtsCarrier> carriers = new ArrayList<GtsCarrier>();
	
	private JTextArea messageArea = new JTextArea("Please make sure the belt and loader \nare empty before reassignment", 2, 10);
	
	private LabeledComboBox carrierAtGlassInstallComboBox = new LabeledComboBox("Carrier At Install");
	
	private LabeledComboBox emptycarrierComboBox = new LabeledComboBox("Next Empty Carrier");
	
	private JButton reassignButton = new JButton("Reassign");
	private JButton cancelButton = new JButton("Cancel");
	

	public GlassReassignDialog(GlassLoadController controller) {
		super(controller.getView().getMainWindow(),true);
		
		this.controller = controller;	
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	//	setResizable(false);
		setTitle("Glass Carrier Maintenance Window");
		setSize(700, 400);
		setLocationRelativeTo(controller.getView().getMainWindow());
		
		initComponents();
		mapActions();
		loadData();

	}
	
	public void initComponents() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		messageArea.setFont(Fonts.DIALOG_BOLD_20);
		messageArea.setEditable(false);
		
		reassignButton.setFont(Fonts.DIALOG_BOLD_20);
		cancelButton.setFont(Fonts.DIALOG_BOLD_20);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));		
		buttonPanel.add(reassignButton);
		buttonPanel.add(Box.createHorizontalStrut(50));
		buttonPanel.add(cancelButton);
		
		carrierAtGlassInstallComboBox.setLabelPreferredWidth(250);
		carrierAtGlassInstallComboBox.setTextAlignment(JLabel.CENTER);
		carrierAtGlassInstallComboBox.setFont(Fonts.DIALOG_BOLD_20);
		emptycarrierComboBox.setLabelPreferredWidth(250);
		emptycarrierComboBox.setTextAlignment(JLabel.CENTER);
		emptycarrierComboBox.setFont(Fonts.DIALOG_BOLD_20);
		
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(Box.createVerticalStrut(50));
		rightPanel.add(messageArea);
		rightPanel.add(carrierAtGlassInstallComboBox);
		rightPanel.add(emptycarrierComboBox);
		rightPanel.add(buttonPanel);
		rightPanel.add(Box.createVerticalStrut(50));

		afonVinTablePane = createVinTablePane();
		
		mainPanel.add(afonVinTablePane,BorderLayout.CENTER);
		mainPanel.add(rightPanel,BorderLayout.EAST);
	
		setContentPane(mainPanel);

	}
	
	private void mapActions() {
		this.reassignButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
	}
	
	private void loadData() {
		afonVinTablePane.reloadData(controller.getModel().fetchAFonVinList());
		
		this.carriers = controller.getModel().fetchGlassCarriers();
			
		carrierAtGlassInstallComboBox.setModel(new ComboBoxModel<GtsCarrier>(this.carriers,"getCarrierNumber"),0);
		emptycarrierComboBox.setModel(new ComboBoxModel<GtsCarrier>(this.carriers,"getCarrierNumber"),this.carriers.size());		

	}

	private ObjectTablePane<Frame> createVinTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Ref #", "afOnSequenceNumber").put("VIN", "productId");
		
		ObjectTablePane<Frame> tablePane = new ObjectTablePane<Frame>(clumnMappings.get(),false);
		tablePane.setBorder(new TitledBorder("Vehicles at Glass Install"));
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(reassignButton)) reassignCarriers();
		if(e.getSource().equals(cancelButton)) cancel();
		
	}
	
	private void cancel() {
		this.dispose();
	}
	
	private void reassignCarriers() {
		
		int index = afonVinTablePane.getTable().getSelectedRow();
		if(index < 0) {
			MessageDialog.showError(this,"Please Select VIN");
            return;
		}
		
		GtsCarrier startCarrier = (GtsCarrier)this.carrierAtGlassInstallComboBox.getComponent().getSelectedItem();
		GtsCarrier nextEmptyCarrier = (GtsCarrier)this.emptycarrierComboBox.getComponent().getSelectedItem();
		
		for(GtsCarrier carrier : this.carriers) {
			carrier.setProductId(null);	
		}
		
		List<GtsCarrier> assignedCarriers = setCarriersInSequence(startCarrier.getStatusValue(),nextEmptyCarrier.getStatusValue());
		
		
		for(int i = 0; i < assignedCarriers.size(); i++) {
			Frame frame = afonVinTablePane.getItems().get(index+ i);
			GtsCarrier carrier = assignedCarriers.get(i);
			carrier.setProductId(frame.getProductId());
		}
		
		controller.getModel().updateCarriers(this.carriers);
		
		Frame carrierFrame = afonVinTablePane.getItems().get(index+ assignedCarriers.size() - 1);
		
		controller.getModel().updateExpectedProduct(carrierFrame.getProductId());
		
		setCursor(controller.getView().getMainWindow().getWaitCursor());
	
		controller.getView().loadAllData();
		
		setCursor(controller.getView().getMainWindow().getDefaultCursor());
	
		controller.getLogger().info("Reassign Carriers - start Carrier [" + startCarrier.getStatusValue() + "," + startCarrier.getCarrierNumber(),
				                    "], empty carrier [" + nextEmptyCarrier.getStatusValue() + "," + nextEmptyCarrier.getCarrierNumber() + "]");
		this.dispose();

	}
	
	private List<GtsCarrier> setCarriersInSequence(int seq1, int seqEmptyCarrier) {
		
		List<GtsCarrier> items = new ArrayList<GtsCarrier>();
		int maxSeq = carriers.size();
		
		if(seqEmptyCarrier > seq1) {
			for(int i = seq1; i < seqEmptyCarrier; i++) {
				items.add(carriers.get(i-1));
			}
		}else {
			for(int i = seq1; i <= maxSeq; i++) {
				items.add(carriers.get(i-1));
			}
			
			for(int i = 1 ; i < seqEmptyCarrier;i++) {
				items.add(carriers.get(i-1));
			}
		}
		
		return items;
	}
	
}
