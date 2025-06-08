package com.honda.galc.client.plastics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.product.SubProductShippingDetail;

/**
 * 
 * 
 * <h3>SubProductShippingView Class description</h3>
 * <p> SubProductShippingView description </p>
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
 * Aug 26, 2014
 *
 *
 */
public class SubProductShippingView extends ApplicationMainPanel{

	private static final long serialVersionUID = 1L;
	
	private SubProductShippingModel model;
	private SubProductShippingController controller;
	
	protected LabeledUpperCaseTextField containerIdField;
	
	protected JButton removeButton;
	protected JButton shipButton;
	
	protected ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> shippingContainerPane;
	
	protected ObjectTablePane<SubProductShippingDetail> shippingDetailTablePane;
	
	public SubProductShippingView(DefaultWindow window) {
		super(window);
		model = new SubProductShippingModel(window.getApplicationContext());
		controller = new SubProductShippingController(model,this);
		initComponents();
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		add(createProductInputPanel(),BorderLayout.NORTH);
		add(createContainerTable(),BorderLayout.WEST);
		add(createShippingDetailTable(),BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		mapActions();
	}
	
	private void mapActions(){
		containerIdField.getComponent().addActionListener(controller);
		removeButton.addActionListener(controller);
		shipButton.addActionListener(controller);
		shippingContainerPane.addListSelectionListener(controller);
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setFont(new Font("sansserif", 1, 20));
		ViewUtil.setPreferredWidth(button, 200);
		return button;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.add(removeButton = createButton("REMOVE"));
		panel.add(shipButton = createButton("SHIP"));
		return panel;
	}

	private LabeledUpperCaseTextField createProductInputField(){
		LabeledUpperCaseTextField productInputField = new LabeledUpperCaseTextField("CONTAINER ID:");
		productInputField.getComponent().setMaximumLength(10);
		productInputField.setFont(Fonts.DIALOG_BOLD_26);
		productInputField.getComponent().setBackground(Color.GREEN);
		this.containerIdField = productInputField;
		return productInputField;
	}
	
	private JPanel createProductInputPanel(){
		JPanel panel = new JPanel();
		panel.add(createProductInputField());
		return panel;
	}

	private ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> createContainerTable() {
		ColumnMappings clumnMappings =ColumnMappings.with("Container ID")
			.put("Sub ID");
		
		ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>> pane = 
			new ObjectTablePane<MultiValueObject<List<SubProductShippingDetail>>>(clumnMappings.get(),false);
		pane.getTable().setName("container list");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		ViewUtil.setPreferredWidth(200, pane);
		this.shippingContainerPane = pane;
		return pane;
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

}
