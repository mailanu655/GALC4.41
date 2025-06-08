package com.honda.galc.client.engine.aeon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledUpperCaseTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.entity.enumtype.BlockLoadStatus;
import com.honda.galc.entity.product.BlockLoad;
import com.honda.galc.entity.product.PreProductionLot;

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
public class BlockLoadView extends ApplicationMainPanel {

	private static final long serialVersionUID = 1L;
	
	protected LabeledUpperCaseTextField prodLotTextField;
	protected LabeledUpperCaseTextField lotSeqTextField;
	protected LabeledUpperCaseTextField ymtoTextField;
	protected LabeledUpperCaseTextField starterTextField;
	protected LabeledUpperCaseTextField blockMCTextField;
	
	protected List<JTextField> boreMeasureTextFields;
	protected List<JTextField> crankJournalTextFields;
	protected List<JTextField> torqueTextFields;
	
	
	protected JButton resetButton = createButton("Reset");
	protected JButton removeButton = createButton("Remove Block");
	protected JButton reloadButton = createButton("Reload Block");
	protected JButton remakeButton = createButton("Remake Block");
	protected JButton skipPartButton = createButton("Skip Part");
	
	protected ObjectTablePane<BlockLoad> blockListPane;
	
	protected ObjectTablePane<PreProductionLot> upcomingLotPane;
	
	private BlockLoadModel blockLoadModel;
	private BlockLoadController blockLoadController;
	
	public BlockLoadView(DefaultWindow window) {
		super(window);
		blockLoadModel = new BlockLoadModel(window.getApplicationContext());
		blockLoadController = new BlockLoadController(blockLoadModel,this);
		initComponents();
		mapActions();
		loadData();
	}

	private void initComponents() {
		setLayout(new MigLayout("insets 1", "[grow,fill]"));
		add(createCurrentLotPanel(),"wrap");
		add(createCurrentBlockPanel(),"wrap");
		add(createProcessedBlockListPanel(),"wrap");
		add(createUpcomingLotPanel());
		
		skipPartButton.setEnabled(false);
	}
	
	private void mapActions() {
		blockMCTextField.getComponent().addActionListener(blockLoadController);
		resetButton.addActionListener(blockLoadController);
		removeButton.addActionListener(blockLoadController);
		reloadButton.addActionListener(blockLoadController);
		remakeButton.addActionListener(blockLoadController);
		skipPartButton.addActionListener(blockLoadController);
		blockListPane.getTable().getSelectionModel().addListSelectionListener(blockLoadController);
	}

	public void loadData() {
		
		List<BlockLoad> blocks = null;
		try{
			blockLoadModel.loadData();
			blocks = blockLoadModel.getProcessedBlocks();
		}catch(Exception ex) {
			setErrorMessage(ex.getMessage());
			return;
		}
		blockListPane.reloadData(blocks);
		blockListPane.getTable().getSelectionModel().setSelectionInterval(0, 0);
		upcomingLotPane.reloadData(blockLoadModel.getNextPreProdLots());
		
		loadProductionLotData();
	}
	
	public void loadProductionLotData() {
		prodLotTextField.getComponent().setText(blockLoadModel.getCurrentPreProdLot().getProductionLot());
		lotSeqTextField.getComponent().setText(blockLoadModel.getCurrentLotSeq());
		ymtoTextField.getComponent().setText(blockLoadModel.getCurrentPreProdLot().getProductSpecCode());
		String starter = blockLoadModel.getStarter(blockLoadModel.getCurrentPreProdLot().getProductSpecCode());
		if(!StringUtils.equals(starter, starterTextField.getComponent().getText())) {
			starterTextField.getComponent().setBackground(toggleColor(starterTextField.getComponent().getBackground()));
			if (blockLoadModel.NOTIFY_STARTER_CHANGE) notifyStarterChange(starterTextField.getComponent().getText(), starter);
		}
		starterTextField.getComponent().setText(starter);
		blockMCTextField.getComponent().requestFocus();
		getLogger().info("Current lot: " + blockLoadModel.getCurrentPreProdLot().getProductionLot()
				+ " lot seq: " + blockLoadModel.getCurrentLotSeq());
		setMessage("waiting for MC number Input");
	}
	
	private Color toggleColor(Color currentColor) {
		return currentColor.equals(Color.yellow) ? Color.magenta : Color.yellow; 
	}

	private JPanel createCurrentLotPanel() {
		JPanel currentLotPanel = new JPanel();
		currentLotPanel.setBorder(new TitledBorder("Current Production Lot"));
		currentLotPanel.setLayout(new MigLayout("insets 0", "[grow,fill]"));
		
		prodLotTextField = createLabeledTextField("LOT:",130,getLabelFont(), 20, Color.cyan,false);
		ymtoTextField = createLabeledTextField("YMTO:",130, getLabelFont(),9, Color.cyan,false);
		lotSeqTextField = createLabeledTextField("LOT SEQ:",180,getLabelFont(), 9, Color.cyan,false);
		starterTextField = createLabeledTextField("STARTER:",180,getLabelFont(), 9, Color.cyan,false);
		currentLotPanel.add(prodLotTextField,"width 620:620:620");
		currentLotPanel.add(lotSeqTextField,"wrap");
		currentLotPanel.add(ymtoTextField);
		currentLotPanel.add(starterTextField);
		return currentLotPanel;
	}
	
	private JPanel createCurrentBlockPanel() {
		JPanel currentBlockPanel = new JPanel();
		currentBlockPanel.setBorder(new TitledBorder("Current Block"));
		currentBlockPanel.setLayout(new MigLayout("insets 0", "[grow,fill]"));
		
		boreMeasureTextFields = createSingleCharTextFields("bore",4);
		crankJournalTextFields = createSingleCharTextFields("crank",5);
		torqueTextFields = createTorqueTextFields("torque",5);
		
		blockMCTextField = createLabeledTextField("BLOCK MC:",220,getLabelFont(), 18, Color.blue,true);
		currentBlockPanel.add(blockMCTextField,"span 3");
		currentBlockPanel.add(resetButton,"width 250:250:250,align center,gapright 10,wrap");
		currentBlockPanel.add(createMeasurementDataPanel("BORE:", boreMeasureTextFields),"gapleft 10");
		currentBlockPanel.add(createMeasurementDataPanel("CRANK JOURNAL:", crankJournalTextFields),"span,gapright 10,wrap");
		currentBlockPanel.add(createMeasurementDataPanel("TORQUE:", torqueTextFields),"gapleft 10,span 4,gapright 10");
		currentBlockPanel.add(skipPartButton,"width 150:150:150,align center,gapright 10,wrap");
		return currentBlockPanel;
		
	}
	
	private JPanel createProcessedBlockListPanel() {
		JPanel processedBlockPanel = new JPanel();
		processedBlockPanel.setBorder(new TitledBorder("Processed MC List"));
		processedBlockPanel.setLayout(new MigLayout("insets 1", "[grow,fill]"));
		processedBlockPanel.add(removeButton,"gapleft 200");
		processedBlockPanel.add(reloadButton);
		processedBlockPanel.add(remakeButton,"gapright 200,wrap");
		processedBlockPanel.add(blockListPane = createProcessedBlockListPane(),"span");
		return processedBlockPanel;
	}
	
	private JPanel createUpcomingLotPanel() {
		JPanel upcomingLotPanel = new JPanel();
		upcomingLotPanel.setBorder(new TitledBorder("Upcoming Lots"));
		upcomingLotPanel.setLayout(new MigLayout("insets 1", "[grow,fill]"));
		upcomingLotPanel.add(upcomingLotPane = createUpcomingLotPane(),"height 100:100:100,span");
		return upcomingLotPanel;
	}
	
	private JPanel createMeasurementDataPanel(String labelText,List<JTextField> fields) {
		JPanel panel = new JPanel(new MigLayout("insets 0", "[grow,fill]"));
		JLabel label = new JLabel(labelText);
		label.setFont(getLabelFont());
		panel.add(label,"span 2");
		for(JTextField field : fields) {
			panel.add(field);
		}
		
		return panel;
		
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
	
	private Font getLabelFont() {
		return new Font("sansserif", 1,35);
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setName(label);
		button.setFont(new Font("sansserif", 1, 18));
		return button;
	}
	
	private JTextField createSingleCharTextField(String name) {
		
		JTextField bean = new JTextField();
		bean.setDocument(new UpperCaseDocument(1));
		bean.setName(name);
		bean.setFont(getLabelFont());
		bean.setHorizontalAlignment(JTextField.CENTER);
		TextFieldState.READ_ONLY.setState(bean);
		ViewUtil.setPreferredWidth(bean, 80);
		
		return bean;
	}
	
	private List<JTextField> createSingleCharTextFields(String name, int size) {
		List<JTextField> fields = new ArrayList<JTextField>();
		for(int i=0;i<size;i++) {
			fields.add(createSingleCharTextField(name + i));
		}
		return fields;
	}
	
	private JTextField createTorqueTextField(String name) {
		
		JTextField bean = new JTextField();
		bean.setDocument(new UpperCaseDocument(5));
		bean.setName(name);
		bean.setFont(getLabelFont());
		bean.setHorizontalAlignment(JTextField.CENTER);
		TextFieldState.READ_ONLY.setState(bean);
		ViewUtil.setPreferredWidth(bean, 80);
		
		return bean;
	}
	
	private List<JTextField> createTorqueTextFields(String name, int size) {
		List<JTextField> fields = new ArrayList<JTextField>();
		for(int i=0;i<size;i++) {
			fields.add(createTorqueTextField(name + i));
		}
		return fields;
	}


	
	@SuppressWarnings("serial")
	private ObjectTablePane<BlockLoad> createProcessedBlockListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("MC#", "McNumber")
			.put("YMTO","productSpecCode")
			.put("Production Lot","productionLot")
			.put("KD Lot","kdLotNumber")
			.put("Lot Sequence","lotSeq");
		
		ObjectTablePane<BlockLoad> pane = new ObjectTablePane<BlockLoad>(clumnMappings.get(),true);
		
		pane.getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer()	{
			 public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        BlockLoad blockLoad = blockListPane.getItems().get(row);
		        if(blockLoad != null && blockLoad.getStatus() == BlockLoadStatus.REMOVE)
		        	c.setBackground(isSelected? new Color(200,0,0) :Color.RED);
		        else if(!isSelected)c.setBackground(Color.WHITE);
		        return c;
		    }
		});
	
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	@SuppressWarnings("serial")
	private ObjectTablePane<PreProductionLot> createUpcomingLotPane(){
		ColumnMappings clumnMappings = ColumnMappings.with("Production Lot","productionLot")
			.put("YMTO","productSpecCode")
			.put("KD Lot","kdLot")
			.put("Lot Size","lotSize");
	
		ObjectTablePane<PreProductionLot> pane = new ObjectTablePane<PreProductionLot>(clumnMappings.get(),true);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		
		pane.getTable().setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
		{
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        Color color = ymtoTextField.getComponent().getText().equals(table.getValueAt(row, 1)) ?
		        		Color.WHITE : Color.YELLOW;
		        	
		        	c.setBackground( color);
		        return c;
		    }
		});
		return pane;
	}
	
	protected void refreshMCListPane() {
		int index = blockListPane.getTable().getSelectedRow();
		blockListPane.reloadData(blockListPane.getItems());
		blockListPane.getTable().getSelectionModel().setSelectionInterval(index, index);
		updateButtonStatus();
	}

	protected void resetMCDataFields(){
		blockMCTextField.getComponent().setText("");
		blockMCTextField.getComponent().setEditable(true);
		TextFieldState.EDIT.setState(blockMCTextField.getComponent());
		for(JTextField textField : boreMeasureTextFields) {
			textField.setText("");
		}
		for(JTextField textField : crankJournalTextFields) {
			textField.setText("");
		}
		blockMCTextField.getComponent().requestFocus();
	}
	
	protected void resetTorqueDataFields() {
		if(torqueTextFields != null) {
			for(JTextField textField : torqueTextFields) {
				textField.setText("");
				TextFieldState.READ_ONLY.setState(textField);
			}
		}
	}
	
	protected void updateButtonStatus() {
		BlockLoad blockLoad = blockListPane.getSelectedItem();
		if(blockLoad == null) return;
		
		if(!blockLoadController.getCurrentState().equals(BlockLoadController.State.WaitingForMc)) {
			removeButton.setEnabled(false);
			reloadButton.setEnabled(false);
			remakeButton.setEnabled(false);
		}else {
			if(blockLoad.getStatus() == BlockLoadStatus.REMOVE) {
				removeButton.setEnabled(false);
				reloadButton.setEnabled(true);
				remakeButton.setEnabled(true);
			}else {
				removeButton.setEnabled(true);
				reloadButton.setEnabled(false);
				remakeButton.setEnabled(false);
			}
		}
		
		boolean isWaitingForTorque = blockLoadController.getCurrentState().equals(BlockLoadController.State.WaitingForTorque);
		skipPartButton.setEnabled(isWaitingForTorque);
	}
	
	protected void notifyStarterChange(final String lastStarter, final String currentStarter) {
		if(StringUtils.isEmpty(lastStarter) || StringUtils.isEmpty(currentStarter)) return;
		
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						String msg = "<html><font face='Calibri' size='14' color='red'> " +
								"Product Starter Changed - \n" 
								+ " Current Starter : " + currentStarter + "\n" 
								+ " Previous Starter : " + lastStarter
						        + "</html>";
						MessageDialog.showInfo(getMainWindow(), msg);
				}});
	}
	
	
}
