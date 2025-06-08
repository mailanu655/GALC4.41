package com.honda.galc.client.engine.shipping;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.ObjectTableModel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;

/**
 * 
 * 
 * <h3>EmergencyQuorumDialog Class description</h3>
 * <p> EmergencyQuorumDialog description </p>
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
 * Nov 28, 2014
 *
 *
 */
public class ExceptionalQuorumDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private LabeledComboBox quorumSizeComboBox = new LabeledComboBox("Select Quorum Size:",false);;
	private TablePane quorumDetailTablePane;
	private ObjectTableModel<ShippingQuorumDetail> tableModel;
	private JButton createButton;
	private JButton cancelButton;
	private EngineShippingController controller;
	
	private boolean isOK = false;
	
	public ExceptionalQuorumDialog(EngineShippingController controller) {
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
		panel.add(Box.createVerticalStrut(10));
		panel.add(quorumDetailTablePane = createQuorumDetailTablePane());
		panel.add(Box.createVerticalStrut(10));
		panel.add(createButtonPanel());
		ViewUtil.setInsets(panel, 20,100, 40, 100);
		setContentPane(panel);
	}
	
	private void mapActions(){
		quorumSizeComboBox.getComponent().addActionListener(this);
		createButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	private void loadData() {
		int defaultQuorumSize = controller.getModel().getPropertyBean().getEmergencyQuorumSize();
		if(defaultQuorumSize > ShippingQuorum.DEFAULT_QUORUM_SIZE) 
			defaultQuorumSize =  ShippingQuorum.DEFAULT_QUORUM_SIZE;
		Integer[] quorumSizes = new Integer[defaultQuorumSize];
		for(int i=0;i<defaultQuorumSize;i++) quorumSizes[i] = i+1;
		quorumSizeComboBox.setModel(quorumSizes, defaultQuorumSize);
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
		if(e.getSource().equals(quorumSizeComboBox.getComponent())) quorumSizeSelected();
		else if(e.getSource().equals(createButton)) createButtonClicked();
		else if(e.getSource().equals(cancelButton)) cancelButtonClicked();
	}

	private void quorumSizeSelected() {
		int quorumSize = (Integer)quorumSizeComboBox.getComponent().getSelectedItem();
		List<ShippingQuorumDetail> quorumDetails = new ArrayList<ShippingQuorumDetail>();
		List<String> ymtos = controller.getModel().getAllMTOCsInTrackingArea();
		for(int i=1;i<= quorumSize;i++) {
			ShippingQuorumDetail detail = new ShippingQuorumDetail(null,0,i);
			if(!ymtos.isEmpty()) detail.setYmto(ymtos.get(0));
			quorumDetails.add(detail);
		}
		tableModel.refresh(quorumDetails);
		TableColumn tc = quorumDetailTablePane.getTable().getColumnModel().getColumn(1);
		tc.setCellEditor(
				new DefaultCellEditor(new JComboBox(ymtos.toArray())));
		
	}
	
	private void createButtonClicked() {
		isOK = true;
		setVisible(false);
	}

	private void cancelButtonClicked() {
		setVisible(false);
	}
	
	private TablePane createQuorumDetailTablePane() {
		ColumnMappings columnMappings = ColumnMappings.with("Seq", "quorumSeq")
			.put("YMTO", "ymto");
		
		TablePane tablePane = new TablePane();
		tableModel = 
			new ObjectTableModel<ShippingQuorumDetail>(tablePane.getTable(),null,columnMappings.get()){
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int col) {
					return col == 1;
				} 
				
				public void setValueAt(Object value, int row, int col) {
					if(col == 1){
						ShippingQuorumDetail detail = getItem(row);
						detail.setYmto((String)value);
					}
				}	  
			};
		tablePane.setBorder(new TitledBorder("Enter YMTO for the Quorum:"));
		
		tablePane.getTable().setRowHeight(22);
		tablePane.getTable().setFont(Fonts.FONT_BOLD("sansserif",14));
		tableModel.setAlignment(JLabel.CENTER);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return tablePane;
	}
	
	public ShippingQuorum getShippingQuorum() {
		if(!isOK) return null;
		ShippingQuorum quorum = new ShippingQuorum();
		ShippingQuorumDetail detail = tableModel.getItems().get(0);
		quorum.setPalletType(controller.getModel().getPalletType(detail.getYmto()));
		quorum.setQuorumSize((Integer)quorumSizeComboBox.getComponent().getSelectedItem());
		quorum.setShippingQuorumDetails(tableModel.getItems());
		return quorum;
	}

}
