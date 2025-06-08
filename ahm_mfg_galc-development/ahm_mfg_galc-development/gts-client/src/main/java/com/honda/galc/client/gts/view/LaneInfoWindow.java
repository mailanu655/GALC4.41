package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneCarrier;


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

public class LaneInfoWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel lanePanel = new JPanel();
    private JPanel carrierPanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    private Table carrierTable = new Table();
    private GtsDrawing drawing;
    
    private GtsLane lane;
    
    public LaneInfoWindow(GtsDrawing drawing,GtsLane lane){
        
        super(drawing.getController().getWindow(),true);
        
        setTitle("Lane Information Window - " + lane.getLaneId());
        
        this.drawing = drawing;
        this.lane = lane;
        
        initComponent();
        addActionListeners();
        
        pack();

        this.setLocationRelativeTo(drawing.getController().getWindow());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        setLanePanel();
        
        setCarrierPanel();
        
        setCommandPanel();
        
        mainPanel.add(lanePanel,BorderLayout.NORTH);
        mainPanel.add(carrierPanel,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private void setLanePanel(){

        StringBuilder buf = new StringBuilder();
        
        buf.append("Lane Name : ");
        buf.append(lane.getLaneId());
        buf.append("    Capacity : ");
        buf.append(lane.getLaneCapacity());
        buf.append("    Total Carriers : ");
        buf.append(lane.getLaneCarriers().size());
        
        JLabel label = new JLabel(buf.toString());
        label.setFont(new Font("Arial", Font.BOLD, 18));
        
        
        lanePanel.add(label);
    }
    
    private void setCarrierPanel(){
        
        CarrierTableModel model = new CarrierTableModel(lane);
        carrierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        carrierTable.setModel(model);
        carrierTable.setColumnAlignments(JLabel.CENTER);
        carrierTable.pack(5);
        carrierTable.setRowHeight(30);
        carrierTable.setColumnSelectionAllowed(false);
        carrierTable.setRowSelectionAllowed(true);
        Dimension dim = carrierTable.getPreferredSize();
        dim.height = Math.min(300, dim.height);
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
       
    
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
    }
    
    private class CarrierTableModel extends AbstractTableModel{
        
        private static final long serialVersionUID = 1L;
        
        private String[] names = new String[] {"Carrier","Product ID","Production Lot","Kd Lot","YMTO","Ext Color","Int Color"};
        private GtsLane lane;
        
        public CarrierTableModel(GtsLane lane){
            this.lane = lane;
        }
        
        public String getColumnName(int column) {
            return names[column];
        }    
        public int getColumnCount() {
            return names.length;
        }
        
        public int getRowCount() {
            return lane.getLaneCarriers().size();
        }
        
         
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= getRowCount()) return null;
            GtsLaneCarrier laneCarrier = lane.getLaneCarriers().get(rowIndex);
            switch(columnIndex){
                case 0: return laneCarrier.getCarrierId();
                case 1: return laneCarrier.getProductId();
                case 2: return laneCarrier.getProductionLot();
                case 3: return laneCarrier.getKdLot();
                case 4: return laneCarrier.getProductSpec();
                case 5: return laneCarrier.getExtColorCode();
                case 6: return laneCarrier.getIntColorCode();
            }
            return null;
        }

    }
}
