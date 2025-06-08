package com.honda.galc.client.engine.shipping;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.product.ShippingQuorum;

/**
 * 
 * 
 * <h3>CreateRepairQuorumDialog Class description</h3>
 * <p> CreateRepairQuorumDialog description </p>
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
 * Nov 12, 2014
 *
 *
 */
public class RepairQuorumDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private LabeledComboBox quorumSizeComboBox = new LabeledComboBox("Select Quorum Size:",false);;
	private LabeledComboBox palletTypeComboBox = new LabeledComboBox("Select Pallet Type:", false);
	private JButton createButton;
	private JButton cancelButton;
	private EngineShippingController controller;
	
	private boolean isOK = false;
	
	public RepairQuorumDialog(EngineShippingController controller) {
		super(controller.getView().getMainWindow(),true);
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.controller = controller;
		setResizable(false);
		setSize(427, 400);
		setTitle("Create Repair Quorum");
		setLocationRelativeTo(controller.getView().getMainWindow());
		initComponents();
		mapActions();
		loadData();
		setVisible(true);
	}
	
	private void initComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(quorumSizeComboBox);
		panel.add(Box.createVerticalStrut(50));
		panel.add(palletTypeComboBox);
		panel.add(Box.createVerticalStrut(50));
		panel.add(createButtonPanel());
		ViewUtil.setInsets(panel, 40,100, 40, 100);
		setContentPane(panel);
	}
	
	private void mapActions(){
		createButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	private void loadData() {
		int defaultQuorumSize = controller.getModel().getPropertyBean().getRepairQuorumSize();
		if(defaultQuorumSize > ShippingQuorum.DEFAULT_QUORUM_SIZE) 
			defaultQuorumSize =  ShippingQuorum.DEFAULT_QUORUM_SIZE;
		Integer[] quorumSizes = new Integer[defaultQuorumSize];
		for(int i=0;i<defaultQuorumSize;i++) quorumSizes[i] = i+1;
		quorumSizeComboBox.setModel(quorumSizes, defaultQuorumSize);
		palletTypeComboBox.setModel(controller.getModel().getAllPalletTypes(), 0);
	}
	
	private JPanel createButtonPanel(){
		createButton = createButton("Create");
		cancelButton = createButton("Cancel");
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(createButton);
		panel.add(Box.createHorizontalGlue());
		panel.add(cancelButton);
		return panel;
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setMargin(new Insets(5,20,5,20));
		button.setFont(Fonts.DIALOG_BOLD_16);
		return button;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(createButton)) createButtonClicked();
		else if(e.getSource().equals(cancelButton)) cancelButtonClicked();
	}

	private void createButtonClicked() {
		isOK = true;
		setVisible(false);
	}

	private void cancelButtonClicked() {
		setVisible(false);
	}
	
	public ShippingQuorum getShippingQuorum() {
		if(!isOK) return null;
		ShippingQuorum quorum = new ShippingQuorum();
		quorum.setPalletType((String)palletTypeComboBox.getComponent().getSelectedItem());
		quorum.setQuorumSize((Integer)quorumSizeComboBox.getComponent().getSelectedItem());
		return quorum;
	}
	
	

}
