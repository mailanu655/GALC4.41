package com.honda.galc.client.gts.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Table</code> is a subclass of JTable.
 * It automatically calculates the preferred width for each column
 * 
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
 * <TD>Mar 24, 2008</TD>
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

public class Table extends JTable {

    private static final long serialVersionUID = 1L;
    
    private int[] colAlignments;

    
    public Table() {
        super();
    }    
    public Table(TableModel dm) {
        super(dm);
    }    
    
    public Table(TableModel dm, TableColumnModel cm) {
        super(dm,cm);
    }    
    
    public Table(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm,cm,sm);
    }    
        
    public Table(int numRows, int numColumns) {
        super(numRows,numColumns);
    }    
    
    public Table(final Object[][] rowData, final Object[] columnNames) {
        super(rowData,columnNames);
    } 
    
    // Sets the preferred width of the visible column specified by vColIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
 
    public void pack() {
        pack(2);
    }
    
    
    public void pack(int margin){
 
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for(int i=0;i<getColumnCount();i++){
            
            packColumn(i,margin);
        }
        
        setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
 
    }
    
    public void setColumnAlignments(int[] colAlignments){
        
        this.colAlignments = colAlignments;
    }
    
    public void setColumnAlignments(int alignment) {
        
        int count = this.getColumnCount();
        if(count == 0) return;
        colAlignments = new int[count];
        
        for(int i = 0; i<count; i++) {
            colAlignments[i] = alignment;
        }
    }
    
    private void packColumn(int columnIndx,int margin){
        
        TableColumn col = getColumnModel().getColumn(columnIndx);
        DefaultTableCellRenderer aRenderer = (DefaultTableCellRenderer)col.getCellRenderer();
        if(col.getCellRenderer() == null )aRenderer = new DefaultTableCellRenderer();
        aRenderer.setHorizontalAlignment(alignment(columnIndx));
        col.setCellRenderer(aRenderer);
        
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = getTableHeader().getDefaultRenderer();
        }
        
        
        Component comp = renderer.getTableCellRendererComponent(
            this, col.getHeaderValue(), false, false, 0, 0);
        int width = comp.getPreferredSize().width;
    
        // Get maximum width of column data
        for (int r=0; r<getRowCount(); r++) {
            
            DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer)getCellRenderer(r, columnIndx);
            comp = cellRenderer.getTableCellRendererComponent(
                this, getValueAt(r, columnIndx), false, false, r, columnIndx);
            width = Math.max(width, comp.getPreferredSize().width);
        }
    
        // Add margin
        width += 2*margin;
    
        // Set the width
        col.setPreferredWidth(width);
    }
    
    private int alignment(int columnIndex){
            
        if(columnIndex < 0 || colAlignments == null || columnIndex >= colAlignments.length) return JLabel.LEFT;
        else return colAlignments[columnIndex];
        
    }
}
