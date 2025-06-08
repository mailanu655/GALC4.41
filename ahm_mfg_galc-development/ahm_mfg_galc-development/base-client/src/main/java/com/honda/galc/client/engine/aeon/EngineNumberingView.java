package com.honda.galc.client.engine.aeon;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dto.EngineNumberingDto;
import com.honda.galc.entity.product.BlockLoad;

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
public class EngineNumberingView extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;
	
	protected LabeledUpperCaseTextField expectedProductIdField;
	protected LabeledUpperCaseTextField receivedProductIdField;
	
	protected JButton refreshButton = createButton("Refresh");
	protected JButton mcButton = createButton("Send MC# To PLC");
	
	protected ObjectTablePane<BlockLoad> blockTablePane;
	
	protected ObjectTablePane<EngineNumberingDto> engineTablePane;
	
	private EngineNumberingModel model;
	private EngineNumberingController controller;
	
	public EngineNumberingView(DefaultWindow window) {
		super(window);
		model = new EngineNumberingModel(window.getApplicationContext());
		controller = new EngineNumberingController(model,this);
		initComponents();
		loadData();
	}

	private void initComponents() {
		setLayout(new MigLayout("insets 5", "[grow,fill]"));
		add(createCurrentEnginePanel(),"wrap");
		add(createUpcomingBlockListPanel(),"wrap");
		add(createProcessedBlockPane());
		mapActions();
	}
	
	private void mapActions() {
		refreshButton.addActionListener(controller);
		mcButton.addActionListener(controller);
	}

	public void loadData() {
		
		List<BlockLoad> blocks = null;
		List<EngineNumberingDto> engines = null;
		try{
			model.loadData();
			blocks = model.getLoadedBlocks();
			engines = model.getProcessedEngines();
			receivedProductIdField.getComponent().setText("");
		}catch(Exception ex) {
			setErrorMessage(ex.getMessage());
			return;
		}
		expectedProductIdField.getComponent().setText(model.getExpectedEngine().getProductId());
		blockTablePane.reloadData(blocks);
		engineTablePane.reloadData(engines);
	}

	public void refreshBlockLoadList(List<BlockLoad>  blockLoads)  {
		try {
			blockTablePane.reloadData(blockLoads);
		}catch(Exception ex) {
			setErrorMessage("Exception when refreshing blockload list " + ex.getMessage());
			return;
		}
	}

	private JPanel createCurrentEnginePanel() {
		JPanel currentEnginePanel = new JPanel();
		currentEnginePanel.setBorder(new TitledBorder("Current Engine"));
		currentEnginePanel.setLayout(new MigLayout("insets 2", "[grow,fill]"));
		
		expectedProductIdField = createLabeledTextField("EXPECTED ESN:",400,new Font("sansserif", 0, 30), 20, Color.cyan,false);
		receivedProductIdField = createLabeledTextField("RECEIVED ESN:",400,new Font("sansserif", 0, 30), 20, Color.cyan,false);
		currentEnginePanel.add(expectedProductIdField,"wrap");
		currentEnginePanel.add(receivedProductIdField,"wrap");
		currentEnginePanel.add(refreshButton,"width 300:300:300,align center");
		return currentEnginePanel;
	}
	
	private JPanel createUpcomingBlockListPanel() {
		JPanel upcomingBlockPanel = new JPanel();
		upcomingBlockPanel.setBorder(new TitledBorder("Upcoming MC List"));
		upcomingBlockPanel.setLayout(new MigLayout("insets 2", "[grow,fill]"));
		upcomingBlockPanel.add(mcButton,"width 300:300:300,align center,wrap");
		upcomingBlockPanel.add(blockTablePane = createUpcomingBlockTable(),"height 250:250:250,span");
		return upcomingBlockPanel;
	}
	
	private JPanel createProcessedBlockPane() {
		JPanel processedBlockPanel = new JPanel();
		processedBlockPanel.setBorder(new TitledBorder("Processed Engine List"));
		processedBlockPanel.setLayout(new MigLayout("insets 2", "[grow,fill]"));
		processedBlockPanel.add(engineTablePane = createEngineTablePane(),"span");
		return processedBlockPanel;
	}
	
	private LabeledUpperCaseTextField createLabeledTextField(String label,int labelWidth,Font font,int columnSize,Color backgroundColor,boolean enabled) {
		LabeledUpperCaseTextField labeledTextField =  new LabeledUpperCaseTextField(label);
		labeledTextField.setFont(font);
		labeledTextField.setLabelPreferredWidth(labelWidth);
		labeledTextField.getComponent().setMaximumLength(columnSize);
		labeledTextField.setInsets(0, 10, 0, 10);
		labeledTextField.getComponent().setFixedLength(false);
		labeledTextField.getComponent().setHorizontalAlignment(JTextField.RIGHT);
		labeledTextField.getComponent().setBackground(backgroundColor);
		labeledTextField.getComponent().setEnabled(enabled);
		labeledTextField.getComponent().setDisabledTextColor(Color.black);
		return labeledTextField;
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setFont(new Font("sansserif", 1, 20));
		return button;
	}
	
	private ObjectTablePane<BlockLoad> createUpcomingBlockTable() {
		ColumnMappings clumnMappings = ColumnMappings.with("MC#", "McNumber")
			.put("YMTO","productSpecCode")
			.put("Production Lot","productionLot")
			.put("Lot Sequence","lotSeq")
			.put("Status","status");
		
		ObjectTablePane<BlockLoad> pane = new ObjectTablePane<BlockLoad>(clumnMappings.get(),true);
		pane.getTable().setName("Block MC List");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<EngineNumberingDto> createEngineTablePane(){
		ColumnMappings clumnMappings = ColumnMappings.with("Process Point","processPointId")
			.put("Engine Number","productId")
			.put("MC#","McNumber")
			.put("YMTO","productSpecCode")
			.put("Production Lot","productionLot")
			.put("Lot Sequence","lotSeq");
	
		ObjectTablePane<EngineNumberingDto> pane = new ObjectTablePane<EngineNumberingDto>(clumnMappings.get(),true);
		pane.getTable().setName("Processed Engine List");
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("dialog", 1, 15));
		pane.getTable().setRowHeight(25);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}


	
}
