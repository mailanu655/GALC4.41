package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.entity.gts.GtsMove;

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

public class EditInspectionStatusWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel movePanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    private JTable moveTable = new JTable(); 
    
    private List<GtsMove> moves;
    
    public EditInspectionStatusWindow(List<GtsMove> moves){
        
        super(getFrame(),true);
        
        setTitle("Move Status Window");
        
        this.moves = moves;
        
        
        initComponent();
        addActionListeners();
        pack();
        
        this.setLocationRelativeTo(getView());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        setIndicatorPanel();
        
        setCommandPanel();
        
        mainPanel.setLayout(new BorderLayout());
  //      mainPanel.setPreferredSize(new Dimension(500,100));
        mainPanel.add(movePanel,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private void setIndicatorPanel(){
        
        MoveTableModel model = new MoveTableModel();
        moveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moveTable.setModel(model);
        
        moveTable.setBackground(getBackground());
        moveTable.setFont(new Font("Arial", Font.BOLD, 14));
        
        this.packTable(moveTable);

        moveTable.setRowHeight(30);
        moveTable.setColumnSelectionAllowed(false);
        moveTable.setRowSelectionAllowed(true);
        Dimension dim = moveTable.getPreferredSize();
        dim.height = Math.min(300, dim.height);
        moveTable.setPreferredScrollableViewportSize(dim);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(moveTable);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        
  
        movePanel.setBorder(new TitledBorder("Move Status"));
        
        movePanel.add(scrollPane,BorderLayout.CENTER);
 
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
    
 
    private static JFrame getFrame(){
        Component focusOwner = KeyboardFocusManager.
        getCurrentKeyboardFocusManager().
        getPermanentFocusOwner();
        if (focusOwner != null && focusOwner instanceof GtsDrawingView) {
            return (JFrame)((GtsDrawingView)focusOwner).getRootPane().getParent();
        }else return null;
    }
    
    private GtsDrawingView getView(){
        Component focusOwner = KeyboardFocusManager.
        getCurrentKeyboardFocusManager().
        getPermanentFocusOwner();
        if (focusOwner != null && focusOwner instanceof GtsDrawingView) {
            return (GtsDrawingView)focusOwner;
        }
        
        return null;
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
    
    private class MoveTableModel extends AbstractTableModel{
        
        private static final long serialVersionUID = 1L;
        
        private String[] names = new String[] {"Source Lane","Destination Lane","Status"};
        
        public MoveTableModel(){
        }
        
        public String getColumnName(int column) {
            return names[column];
        }    
        public int getColumnCount() {
            return names.length;
        }
        
        public int getRowCount() {
            
            return moves.size();
            
        }
        
         
        public Object getValueAt(int rowIndex, int columnIndex) {
            
            if(rowIndex >= getRowCount()) return null;
            GtsMove move = moves.get(rowIndex);
            switch(columnIndex){
                case 0: return move.getId().getSourceLaneId();
                case 1: return move.getId().getDestinationLaneId();
                case 2: return move.getMoveStatus().name();
            }
            return null;
        }

    }
 
}
