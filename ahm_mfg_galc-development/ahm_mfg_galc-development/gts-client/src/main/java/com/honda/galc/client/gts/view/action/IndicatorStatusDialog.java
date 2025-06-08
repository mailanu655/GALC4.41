package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.ui.component.SortableTableModel;
import com.honda.galc.entity.gts.GtsIndicator;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>IndicatorStatusWindow</code> is a class to display  Carrier Info
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 5, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class IndicatorStatusDialog extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel indicatorPanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    private JTable indicatorTable = new JTable(); 
    
    private List<GtsIndicator> indicators;
    
    public IndicatorStatusDialog(GtsDrawing drawing){
        
        super(drawing.getController().getWindow(),true);
        
        setTitle("Indicator Status Window");
        
        this.indicators = drawing.getController().getModel().fetchAllIndicators();
        
        
        initComponent();
        addActionListeners();
        pack();
        
        this.setLocationRelativeTo(drawing.getController().getWindow());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        setIndicatorPanel();
        
        setCommandPanel();
        
        mainPanel.setLayout(new BorderLayout());
  //      mainPanel.setPreferredSize(new Dimension(500,100));
        mainPanel.add(indicatorPanel,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private void setIndicatorPanel(){
        
        IndicatorTableModel model = new IndicatorTableModel(indicators,indicatorTable);
        indicatorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        indicatorTable.setModel(model);
        
        indicatorTable.setBackground(getBackground());
        indicatorTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));

        //      set the bold font for the first column
        TableColumn column = indicatorTable.getColumnModel().getColumn(0);
        column.setCellRenderer( new FontRenderer() ); 
        
        
        this.packTable(indicatorTable);

        indicatorTable.setRowHeight(30);
        indicatorTable.setColumnSelectionAllowed(false);
        indicatorTable.setRowSelectionAllowed(true);
        Dimension dim = indicatorTable.getPreferredSize();
        dim.height = Math.min(300, dim.height);
        indicatorTable.setPreferredScrollableViewportSize(dim);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(indicatorTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
  
        indicatorPanel.setBorder(new TitledBorder("Indicator Status"));
        
        indicatorPanel.add(scrollPane,BorderLayout.CENTER);
 
    }
    
    
    private void setCommandPanel(){
        
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        commandPanel.add(closeButton);
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.setPreferredSize(new Dimension(80,50));
    }
    
    private void addActionListeners(){
 
        closeButton.addActionListener(this);
        
    }
       
    
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
    }
    
 
    //  Sets the preferred width of the visible column specified by vColIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    private void packTable(JTable table) {
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=0;i<table.getColumnCount();i++){
            
            packColumn(table,i);
        }
    
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

    }     
    
    private void packColumn(JTable table,int columnIndx){
        
        int margin = 2;
        TableColumn col = table.getColumnModel().getColumn(columnIndx);
        
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        
        
        Component comp = renderer.getTableCellRendererComponent(
            table, col.getHeaderValue(), false, false, 0, 0);
        int width = comp.getPreferredSize().width;
    
        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            
            DefaultTableCellRenderer aRenderer = (DefaultTableCellRenderer)table.getCellRenderer(r, columnIndx);
            aRenderer.setHorizontalAlignment(JLabel.LEFT);
            comp = aRenderer.getTableCellRendererComponent(
                table, table.getValueAt(r, columnIndx), false, false, r, columnIndx);
            width = Math.max(width, comp.getPreferredSize().width);
        }
    
        // Add margin
        width += 2*margin;
    
        // Set the width
        col.setPreferredWidth(width);
    }
    
    private class IndicatorTableModel extends SortableTableModel<GtsIndicator>{
        
        private static final long serialVersionUID = 1L;
        
        public IndicatorTableModel(List<GtsIndicator> indicators, JTable table){
            super(indicators,new String[] {"Indicator", "Type", "Value"},table);
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            if(rowIndex >= getRowCount()) return null;
            GtsIndicator indicator = items.get(rowIndex);
            switch(columnIndex){
                case 0: return indicator.getId().getIndicatorName();
                case 1: return indicator.getIndicatorType();
                case 2: return indicator.getIndicatorValue();
            }
            return null;
        }

    }
    
    class FontRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        public Component getTableCellRendererComponent(JTable table,
          Object value, boolean isSelected, boolean hasFocus, int row,
           int column) {

         super.getTableCellRendererComponent(table, value, isSelected,
                                              hasFocus, row, column);

         if (column == 0) {
             this.setBackground(new Color(255,255,230));
             setFont(new Font("SansSerif",Font.BOLD,12));
          }

          return this;
        }

     } 
 
}
