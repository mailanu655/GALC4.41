package com.honda.galc.client.plastics;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.entity.product.SubProductShippingDetail;

/**
 * 
 * 
 * <h3>BlockLoadView Class description</h3>
 * <p> BlockLoadView description </p>
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
 * Mar 24, 2014
 *
 *
 */
public class SubProductContainerAssignmentView extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;
	
	protected LabeledTextField productIdField;
	
	protected JButton refreshButton = createButton("REFRESH");
	protected JButton addButton = createButton("SET");
	
	protected ObjectTablePane<SubProductShipping> shippingTablePane;
	protected ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> shippingContainerPane;
	protected ObjectTablePane<SubProductShippingDetail> shippingDetailTablePane;
	
	private SubProductContainerAssignmentModel model;
	private SubProductContainerAssignmentController controller;
	
	public SubProductContainerAssignmentView(DefaultWindow window) {
		super(window);
		model = new SubProductContainerAssignmentModel(window.getApplicationContext());
		controller = new SubProductContainerAssignmentController(model,this);
		initComponents();
		loadData();
	}

	private void initComponents() {
		setLayout(new MigLayout("insets 0", "[grow,fill]"));
		add(createShippingTablePane(),"width 700:700:700, height 190:190:190");
		add(createContainerPanel(),"height 190:190:190,wrap");
		add(createProductInputField()," gapleft 250,gapright 250,span");
		add(createShippingDetailTable(),"span");
		mapActions();
	}
	
	private void mapActions() {
		refreshButton.addActionListener(controller);
		addButton.addActionListener(controller);
		shippingTablePane.addListSelectionListener(controller);
		shippingContainerPane.addListSelectionListener(controller);
		shippingDetailTablePane.addListSelectionListener(controller);
		productIdField.getComponent().addActionListener(controller);
	}

	public void loadData() {
		shippingTablePane.reloadData(model.getAllShippingLots());
	}
	
	private JPanel createContainerPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 0", "[grow,fill]"));
		panel.add(shippingContainerPane = createContainerTable(),"span,wrap");
		panel.add(addButton);
		panel.add(refreshButton);
		return panel;
	}
	
	private LabeledTextField createProductInputField(){
		LabeledTextField productInputField = new LabeledTextField("PRODUCT ID:");
		productInputField.getComponent().setColumns(20);
		productInputField.setFont(Fonts.DIALOG_BOLD_22);
		productInputField.getComponent().setBackground(Color.GREEN);
		
		this.productIdField = productInputField;
		return productInputField;
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setFont(new Font("sansserif", 1, 20));
		return button;
	}
	
	private ObjectTablePane<SubProductShippingDetail> createShippingDetailTable() {
		ColumnMappings clumnMappings = ColumnMappings.with("KD Lot", "kdLot")
			.put("Production Lot","productionLot")
			.put("MTOC","productSpecCode")
			.put("Product ID","productId")
			.put("Prod Seq","productSeqNo")
			.put("Status","status");
		
		ObjectTablePane<SubProductShippingDetail> pane = new ObjectTablePane<SubProductShippingDetail>(clumnMappings.get(),false);
		pane.getTable().setName("Shipping product list");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		this.shippingDetailTablePane = pane;
		return pane;
	}
	
	private ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> createContainerTable() {
		ColumnMappings clumnMappings =ColumnMappings.with("#", "key")
			.put("Sub ID")
			.put("Container ID");
		
		ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> pane = 
			new ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>>(clumnMappings.get(),false);
		pane.getTable().setName("container list");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<SubProductShipping> createShippingTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("ProductionLot", "productionLot")
			.put("KD Lot","kdLotNumber")
			.put("YMTO","productSpecCode")
			.put("Sch QTY","schQuantity")
			.put("Act QTY","actQuantity")
			.put("Status","status");
		
		ObjectTablePane<SubProductShipping> pane = new ObjectTablePane<SubProductShipping>(clumnMappings.get(),false);
		
		pane.getTable().setName("Shipping Table");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		
		this.shippingTablePane = pane;
		return pane;
	}
	
}
