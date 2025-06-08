package com.honda.galc.client.gts.view.action;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.gts.figure.CarrierFigure;
import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.HighlightCondition;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;

import net.miginfocom.swing.MigLayout;


/**
 * 
 * 
 * 
 * <h3>SearchDialog Class description</h3>
 * <p> SearchDialog description </p>
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
 * Mar 1, 2018
 *
 *
 */

public class SearchDialog extends JDialog implements  ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;
	
	
	private GtsDrawingView view;
	
	 private ButtonGroup  searchGroup = new ButtonGroup();
	 private JRadioButton productionLotRadioButton =  new JRadioButton("Production Lot (Last 8 digits)");
	 private JRadioButton productRadioButton = new JRadioButton("Product (Last 6 digits)");
	 private JRadioButton carrierRadioButton =  new JRadioButton("Carrier Number");
	 private JRadioButton yearModelTypeRadioButton =  new JRadioButton("Year/Model/Type('JTBG AC5')");
	 private JRadioButton colorRadioButton =  new JRadioButton("Exterior/Interior Color('NH830M Z')");
	 
	 private JButton addButton = new JButton("Add Search Criteira");
	 private JButton removeButton = new JButton("Remove Search Criteria");
	 private JButton colorSelectionButton = new JButton("Select Color");
	 
	 @SuppressWarnings("rawtypes")
	private JList listBox = new JList();
	 
	 private JButton doneButton = new JButton("Done");
	 
	 private ObjectTablePane<HighlightCondition> conditionTablePane;
	  
	 
	   
	public SearchDialog(GtsDrawingView view){
		super(view.getDrawing().getController().getWindow(),"Search Criteria Dialog",true);
		 this.view = view;
	     initComponent();
	     addActionListeners();
	     pack();
	     conditionTablePane.reloadData(CarrierFigure.getHighlightConditions());
	     conditionTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	     disableButtons();
		 
	     productionLotRadioButton.doClick();
			
	     this.setLocationRelativeTo(view.getDrawing().getController().getWindow());
	}   
	
	private void initComponent(){
		 Container contentPane = getContentPane();
		 JPanel mainPanel = new JPanel();
		 mainPanel.setLayout(new MigLayout("insets 10 30 1 30", "[grow,fill]"));
		 contentPane.add(mainPanel);
		 
		 mainPanel.add(new JLabel("Search Criteria"));
		 mainPanel.add(new JLabel("Search Value"),"wrap");
		 
		 searchGroup.add(productionLotRadioButton);
		 searchGroup.add(productRadioButton);
		 searchGroup.add(carrierRadioButton);
		 searchGroup.add(yearModelTypeRadioButton);
		 searchGroup.add(colorRadioButton);
		 
		 
		 mainPanel.add(productionLotRadioButton);
		 mainPanel.add(createSearchListBox(),"span 1 5,wrap");
		 mainPanel.add(productRadioButton,"wrap");
		 mainPanel.add(carrierRadioButton,"wrap");
		 mainPanel.add(yearModelTypeRadioButton,"wrap");
		 mainPanel.add(colorRadioButton,"wrap");
		  conditionTablePane = createConditionTablePane();
		 mainPanel.add(conditionTablePane,"span 1 5");
		 mainPanel.add(addButton,"wrap");
		 mainPanel.add(removeButton,"wrap");
		 mainPanel.add(colorSelectionButton,"wrap");
		 mainPanel.add(doneButton);
			  
	}
	
	private JScrollPane createSearchListBox() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(listBox);
		DefaultListCellRenderer renderer =
                (DefaultListCellRenderer)listBox.getCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);

		return scrollPane;
	}
	
	private void addActionListeners(){
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		colorSelectionButton.addActionListener(this);
		doneButton.addActionListener(this);
		
		productionLotRadioButton.addActionListener(this);
		productRadioButton.addActionListener(this);
		carrierRadioButton.addActionListener(this);
		yearModelTypeRadioButton.addActionListener(this);
		colorRadioButton.addActionListener(this);
		
		conditionTablePane.addListSelectionListener(this);
		
		listBox.addListSelectionListener(this);
		
	} 
	
	private ObjectTablePane<HighlightCondition> createConditionTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Search Type", "type")
				.put("Expected Value", "expectedValue").put("Highlight Color","");
			
		ObjectTablePane<HighlightCondition> tablePane = new ObjectTablePane<HighlightCondition>(clumnMappings.get(),false);
		
		tablePane.getTable().setRowHeight(30);
		tablePane.getTable().setFont(Fonts.DIALOG_PLAIN_16);
		tablePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		tablePane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePane.setPreferredHeight(200);
		
		tablePane.getTable().getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer()
		{
		    @Override
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        if(column == 2) {
		        	Color color = conditionTablePane.getItems().get(row).getColor();
		        	setBackground( color);
		        }
		        return c;
		    }
		});
		return tablePane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == addButton){
			addSearchCriteria();
		}else if(e.getSource() == removeButton){
			removeSearchCriteria();
		}else if(e.getSource() == colorSelectionButton){
		    selectColor();
		}else if(e.getSource() == doneButton){
			this.dispose();
		}else if(e.getSource() == productionLotRadioButton) {
			productionLotSelected();
		}else if(e.getSource() == productRadioButton) {
			productSelected();
		}else if(e.getSource() == carrierRadioButton) {
			carrierSelected();
		}else if(e.getSource() == yearModelTypeRadioButton) {
			ymtoSelected();
		}else if(e.getSource() == colorRadioButton){
			colorCodeSelected();
		}
	}
	
	private void addSearchCriteria() {
		String value =(String)listBox.getSelectedValue();
		HighlightCondition.Type option = findSearchType();
		CarrierFigure.addHightlightCondition(option, value, Color.ORANGE);
		
		conditionTablePane.reloadData(CarrierFigure.getHighlightConditions());
		int lastIndex = conditionTablePane.getItems().size() -1;
		conditionTablePane.getTable()
			.getSelectionModel().setSelectionInterval(lastIndex, lastIndex);
		listBox.clearSelection();
		disableButtons();
	}
	
	private HighlightCondition.Type findSearchType() {
		if(productionLotRadioButton.isSelected()) return HighlightCondition.Type.TYPE_PROD_LOT;
		else if(productRadioButton.isSelected()) return HighlightCondition.Type.TYPE_PROD_NUMBER;
		else if(carrierRadioButton.isSelected()) return HighlightCondition.Type.TYPE_CARRIER;
		else if(yearModelTypeRadioButton.isSelected()) return HighlightCondition.Type.TYPE_MTO;
		else if(colorRadioButton.isSelected()) return HighlightCondition.Type.TYPE_COLOR;
		return HighlightCondition.Type.TYPE_NONE;
	}
	
	private void removeSearchCriteria() {
		HighlightCondition condition = conditionTablePane.getSelectedItem();
		if(condition == null) return;
		CarrierFigure.getHighlightConditions().remove(condition);
		conditionTablePane.reloadData(CarrierFigure.getHighlightConditions());
		conditionTablePane.getTable().getSelectionModel().setSelectionInterval(0, 0);
	}
	
	private void selectColor() {
		HighlightCondition condition = conditionTablePane.getSelectedItem();
		if(condition == null) return;
		
		 Color chosenColor = ColorChooser.selectColor(this, condition.getColor());
		 if (chosenColor != null) {
			 condition.setColor(chosenColor);
			 conditionTablePane.refresh();
		 }         
	}
	
	private void productionLotSelected() {
		updateListBox(view.getDrawing().getModel().findAllProductionLots());
	}
	
	private void productSelected() {
		updateListBox(view.getDrawing().getModel().findAllProductIds());
	}
	
	private void carrierSelected() {
		updateListBox(view.getDrawing().getModel().findAllCarriers());
	}
	
	private void ymtoSelected() {
		updateListBox(view.getDrawing().getModel().findAllYmtos());
	}
	
	private void colorCodeSelected() {
		updateListBox(view.getDrawing().getModel().findAllColorCodes());
	}
	
	@SuppressWarnings("unchecked")
	private void updateListBox(List<String> items) {
		disableButtons();
		listBox.setListData(items.toArray(new String[items.size()]));
	}
	
	private void disableButtons() {
		boolean flag =  conditionTablePane.getSelectedItem() != null;
		addButton.setEnabled(listBox.getSelectedIndex() >= 0);
		removeButton.setEnabled(flag);
		colorSelectionButton.setEnabled(flag);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting())
			disableButtons();
	}


}
