package com.honda.galc.client.engine.mcshipping;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.ProductShipping;

/**
 * 
 * 
 * <h3>MCShippingView Class description</h3>
 * <p> MCShippingView description </p>
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
 * Sep 10, 2014
 *
 *
 */
public class MCShippingView extends AbstractView<MCShippingModel, MCShippingController>{
	
	private static final long serialVersionUID = 1L;
	
	protected LabeledComboBox trailerComboBox;
	protected LabeledUpperCaseTextField dunnageTextField;
	protected LabeledUpperCaseTextField headDunnageCountField;
	protected LabeledUpperCaseTextField blockDunnageCountField;
	protected LabeledUpperCaseTextField conrodDunnageCountField;
	protected LabeledUpperCaseTextField crankshaftDunnageCountField;
	protected LabeledUpperCaseTextField totalCountField;
	protected ObjectTablePane<ProductShipping> headDunnageList;
	protected ObjectTablePane<ProductShipping> blockDunnageList;
	protected ObjectTablePane<ProductShipping> conrodDunnageList;
	protected ObjectTablePane<ProductShipping> crankshaftDunnageList;
	protected JButton newButton;
	protected JButton modelFilterButton;
	protected JButton removeButton;
	protected JButton completeButton;
	
	public MCShippingView(DefaultWindow mainWindow) {
		super(mainWindow);
	}
	
	public void prepare() {
		initComponents();
		super.prepare();
	}
	
	public void initComponents() {
		trailerComboBox = createLabeledCombobox("Trailer #", 200, Fonts.DIALOG_BOLD_24, true);
		dunnageTextField = createLabeledTextField("Dunnage #", 200, Fonts.DIALOG_BOLD_24, 20, Color.white, false);
		headDunnageCountField = createLabeledTextField("Count", 100, Fonts.DIALOG_BOLD_24, 6, Color.green, false);
		blockDunnageCountField = createLabeledTextField("Count", 100, Fonts.DIALOG_BOLD_24, 6, Color.green, false);
		conrodDunnageCountField = createLabeledTextField("Count", 100, Fonts.DIALOG_BOLD_24, 6, Color.green, false);
		crankshaftDunnageCountField = createLabeledTextField("Count", 100, Fonts.DIALOG_BOLD_24, 6, Color.green, false);

		totalCountField = createLabeledTextField("Count", 100, Fonts.DIALOG_BOLD_24, 6, Color.green, false);
		headDunnageList = createDunnageListPane("Head Dunnage List");
		blockDunnageList = createDunnageListPane("Block Dunnage List");
		conrodDunnageList = createDunnageListPane("Conrod Dunnage List");
		crankshaftDunnageList = createDunnageListPane("Crankshaft Dunnage List");

		newButton = createButton("New", Fonts.DIALOG_BOLD_24);
		modelFilterButton = createButton("Model Filter", Fonts.DIALOG_BOLD_24);
		removeButton = createButton("Remove", Fonts.DIALOG_BOLD_24);
		completeButton = createButton("Complete", Fonts.DIALOG_BOLD_24);

		setLayout(new MigLayout("insets 10 20 1 20", "[grow,fill]"));
		add(trailerComboBox, "span 2");
		add(newButton, "width 150:150:150,align center,wrap");
		add(dunnageTextField, "span 2");
		add(modelFilterButton, "width 150:150:150,align center,wrap");

		List<String> productTypes = Arrays.asList(getModel().getPropertyBean().getProductTypes());
		int startCount = 1;
		int maxCount = 2;// used product count -max 2 product types
		if (productTypes.size() < maxCount)
			startCount = 2;
		for (String productType : productTypes) {
			if (StringUtils.equalsIgnoreCase(productType, "head") && startCount <= maxCount) {
				add(createDunnageListPanel("Head Dunnage List", headDunnageCountField, headDunnageList),
						(startCount == 1) ? "grow, push, width 50%" : "grow, push,span 2, wrap");
				startCount++;
			}
			if (StringUtils.equalsIgnoreCase(productType, "block") && startCount <= maxCount) {
				add(createDunnageListPanel("Block Dunnage List", blockDunnageCountField, blockDunnageList),
						(startCount == 1) ? "grow, push, width 50%" : "grow, push,span 2, wrap");
				startCount++;
			}
			if (StringUtils.equalsIgnoreCase(productType, "conrod") && startCount <= maxCount) {
				add(createDunnageListPanel("Conrod Dunnage List", conrodDunnageCountField, conrodDunnageList),
						(startCount == 1) ? "grow, push, width 50%" : "grow, push,span 2, wrap");
				startCount++;
			}
			if (StringUtils.equalsIgnoreCase(productType, "crankshaft") && startCount <= maxCount) {
				add(createDunnageListPanel("Crankshaft Dunnage List", crankshaftDunnageCountField,
						crankshaftDunnageList),
						(startCount == 1) ? "grow, push, width 50%" : "grow, push,span 2, wrap");
				startCount++;
			}
		}

		add(createTotalCountPanel(), "align center,wrap");
		add(removeButton, "width 200:200:200,align center");
		add(completeButton, "width 200:200:200,align center");
	}
	
	protected void mapActions() {
		newButton.addActionListener(getController());
		modelFilterButton.addActionListener(getController());
		removeButton.addActionListener(getController());
		completeButton.addActionListener(getController());
		trailerComboBox.getComponent().addActionListener(getController());
		dunnageTextField.getComponent().addActionListener(getController());
		headDunnageList.addListSelectionListener(getController());
		blockDunnageList.addListSelectionListener(getController());
		conrodDunnageList.addListSelectionListener(getController());
		crankshaftDunnageList.addListSelectionListener(getController());
	}
	
	public void reload() {
		getController().loadActiveDunnages();
	}
	
	public void start() {
		
	}
	
	private JPanel createTotalCountPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder("Total Products"));
		panel.add(totalCountField,BorderLayout.NORTH);
		return panel;
	}
	
	private JPanel createDunnageListPanel(String title,LabeledUpperCaseTextField countField, ObjectTablePane<ProductShipping>  dunnageListPane) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(new TitledBorder(title));
		panel.add(countField,BorderLayout.NORTH);
		panel.add(dunnageListPane,BorderLayout.CENTER);
		
		return panel;
	}
	
	private ObjectTablePane<ProductShipping> createDunnageListPane(String name) {
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row")
			.put("Dunnage", "dunnage").put("Count","count");
		
		ObjectTablePane<ProductShipping> tablePane = new ObjectTablePane<ProductShipping>(clumnMappings.get(),true);
		
		int fontSize = getModel().getPropertyBean().getListFontSize();
		Font newFont = new Font("Dialog", Font.PLAIN, fontSize);
		tablePane.getTable().setFont(newFont);
		
		Font tableFont = tablePane.getTable().getFont();
		tablePane.getTable().setRowHeight(Math.round(tableFont.getSize() * 1.1f));		
		tablePane.getTable().setName(name);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return tablePane;
	}
	
}
