package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.Table;
import com.honda.galc.entity.gts.GtsZone;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LaneInfoWindow</code> is a class to display lane Carrier Info
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

public class ZoneInfoWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel carrierPanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    private Table carrierTable = new Table();
    
    private ZoneInfoTableModel tableModel;
    
    private GtsDrawing drawing;
    
    private List<GtsZone> zones;
    
    public ZoneInfoWindow(GtsDrawing drawing){
        
        super(drawing.getController().getWindow(),true);
        
        setTitle("Carrier Count Window ");
        
        this.drawing = drawing;
        
        zones = drawing.getModel().fetchAllZones();
        
        initComponent();
        addActionListeners();
        
        pack();

        this.setLocationRelativeTo(drawing.getController().getWindow());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        setCarrierPanel();
        
        setCommandPanel();
        
        mainPanel.add(carrierPanel,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private void setCarrierPanel(){
        
        tableModel = new ZoneInfoTableModel(drawing,zones);
        carrierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carrierTable.setModel(tableModel);
        carrierTable.setColumnAlignments(JLabel.CENTER);
        
        // set the bold font for the first column
        TableColumn column = carrierTable.getColumnModel().getColumn(0);
        column.setCellRenderer( new FontRenderer() ); 
        
        carrierTable.pack(10);
        carrierTable.setRowHeight(25);
        setHeaderToolTips();
        
        // change table header font
        carrierTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        carrierTable.setColumnSelectionAllowed(false);
        carrierTable.setRowSelectionAllowed(true);
        Dimension dim = carrierTable.getPreferredSize();
        dim.height = Math.min(500, dim.height);
        carrierTable.setPreferredScrollableViewportSize(dim);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(carrierTable);
  
        carrierPanel.add(scrollPane,BorderLayout.CENTER);
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
       
    private void setHeaderToolTips(){
        JTableHeader header = carrierTable.getTableHeader();
        
        ColumnHeaderToolTips tips = new ColumnHeaderToolTips();
        
        // Assign a tooltip for each of the columns
        for (int c=0; c<carrierTable.getColumnCount(); c++) {
            TableColumn col = carrierTable.getColumnModel().getColumn(c);
            tips.setToolTip(col, tableModel.getHTMLToolTip(c));
        }
        header.addMouseMotionListener(tips);
    }
    
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
    }
    
    
    private class ColumnHeaderToolTips extends MouseMotionAdapter {
        // Current column whose tooltip is being displayed.
        // This variable is used to minimize the calls to setToolTipText().
        TableColumn curCol;
    
        // Maps TableColumn objects to tooltips
        Map<TableColumn,String> tips = new HashMap<TableColumn,String>();
    
        // If tooltip is null, removes any tooltip text.
        public void setToolTip(TableColumn col, String tooltip) {
            if (tooltip == null) {
                tips.remove(col);
            } else {
                tips.put(col, tooltip);
            }
        }
    
        public void mouseMoved(MouseEvent evt) {
            TableColumn col = null;
            JTableHeader header = (JTableHeader)evt.getSource();
            JTable table = header.getTable();
            TableColumnModel colModel = table.getColumnModel();
            int vColIndex = colModel.getColumnIndexAtX(evt.getX());
    
            // Return if not clicked on any column header
            if (vColIndex >= 0) {
                col = colModel.getColumn(vColIndex);
            }
    
            if (col != curCol) {
                header.setToolTipText((String)tips.get(col));
                curCol = col;
            }
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
