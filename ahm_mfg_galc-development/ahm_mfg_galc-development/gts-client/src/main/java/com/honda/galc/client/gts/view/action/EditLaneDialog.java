package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.GtsTrackingModel;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.enumtype.GtsLaneType;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsLaneSegment;
import com.honda.galc.entity.gts.GtsLaneSegmentMap;

public class EditLaneDialog extends JDialog implements  ActionListener,TableModelListener,ListSelectionListener{

    private static final long serialVersionUID = 1L;

    private GtsDrawingView view;
    private JPanel mainPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private JPanel upperRightPanel = new JPanel();
    private JPanel lowerMiddlePanel = new JPanel();
    private JButton addLaneButton =    new JButton("  Add Lane ");
    private JButton removeLaneButton = new JButton("Remove Lane");
    private JButton closeButton =      new JButton("    Close  ");
    private JButton moveUpButton = new JButton("\u2191 Up");
    private JButton moveDownButton = new JButton("\u2193 Down");
    private JButton moveLeftButton = new JButton("<<");
    private JButton moveRightButton = new JButton(">>");
    
    private JTable laneTable = new JTable();
    private JTable laneSegmentTable = new JTable();
    private JTable availableSegmentTable = new JTable();
    
    private LaneTableModel laneTableModel;
    
    
    public EditLaneDialog(GtsDrawingView view){
        super(view.getDrawing().getController().getWindow(),true);
        this.view = view;
        setTitle("Edit Lanes Window");
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(view);
        if(laneTable.getRowCount()>0) laneTable.setRowSelectionInterval(0, 0);
        if(availableSegmentTable.getRowCount()>0) 
            availableSegmentTable.setRowSelectionInterval(0, 0);
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        setUpperPanel();
        setLowerPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(upperPanel,BorderLayout.NORTH);
        mainPanel.add(lowerPanel,BorderLayout.SOUTH);
   }
    
    private void setUpperPanel(){
        laneTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        laneTable.setModel(laneTableModel = new LaneTableModel(view.getDrawing().getModel().getLanes()));
        laneTable.setPreferredScrollableViewportSize(new Dimension(300,200));
        laneTable.getModel().addTableModelListener(this);
        laneTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        laneTable.setPreferredScrollableViewportSize(new Dimension(350,400));
        laneTable.setColumnSelectionAllowed(false);
        laneTable.setRowSelectionAllowed(true);
        laneTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(new TitledBorder("Lane List"));
        scrollPane.setViewportView(laneTable);
        upperRightPanel.setLayout(new BoxLayout(upperRightPanel,BoxLayout.Y_AXIS));
        Box right = Box.createVerticalBox();
        addLaneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeLaneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        right.add(addLaneButton);
        right.add(Box.createRigidArea(new Dimension(0,10)));
        right.add(removeLaneButton);
        right.add(Box.createRigidArea(new Dimension(0,10)));
        right.add(closeButton);
        
        upperPanel.setLayout(new BoxLayout(upperPanel,BoxLayout.X_AXIS));
        upperPanel.add(scrollPane);
        upperPanel.add(Box.createHorizontalStrut(30));
        upperPanel.add(right);
        upperPanel.add(Box.createHorizontalStrut(30));
    }
    
    private void setLowerPanel(){
        moveUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveDownButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveLeftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        moveRightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBorder(new TitledBorder("Lane Segment List"));
        scrollPane1.setViewportView(laneSegmentTable);
        laneSegmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        laneSegmentTable.setModel(new LaneSegmentTableModel(new ArrayList<GtsLaneSegment>()));
        laneSegmentTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        laneSegmentTable.setPreferredScrollableViewportSize(new Dimension(300,200));
        lowerMiddlePanel.setLayout(new BoxLayout(lowerMiddlePanel,BoxLayout.Y_AXIS));
        lowerMiddlePanel.add(Box.createVerticalGlue());
        lowerMiddlePanel.add(moveUpButton);
        lowerMiddlePanel.add(Box.createVerticalStrut(10));
        lowerMiddlePanel.add(moveDownButton);
        lowerMiddlePanel.add(Box.createVerticalStrut(10));
        lowerMiddlePanel.add(moveLeftButton);
        lowerMiddlePanel.add(Box.createVerticalStrut(10));
        lowerMiddlePanel.add(moveRightButton);
        lowerMiddlePanel.add(Box.createVerticalGlue());
        lowerMiddlePanel.setPreferredSize(new Dimension(80,200));
        availableSegmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableSegmentTable.setModel(new LaneSegmentTableModel(getModel().getAvailableLaneSegments(null)));
        availableSegmentTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        availableSegmentTable.setPreferredScrollableViewportSize(new Dimension(300,200));
         JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBorder(new TitledBorder("Available Lane Segment List"));
        scrollPane2.setViewportView(availableSegmentTable);
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(scrollPane1,BorderLayout.WEST);
        lowerPanel.add(lowerMiddlePanel,BorderLayout.CENTER);
        lowerPanel.add(scrollPane2,BorderLayout.EAST);
    }
    
    private void addActionListeners(){
        laneTable.getSelectionModel().addListSelectionListener(this);
        addLaneButton.addActionListener(this);
        removeLaneButton.addActionListener(this);
        closeButton.addActionListener(this);
        moveUpButton.addActionListener(this);
        moveDownButton.addActionListener(this);
        moveLeftButton.addActionListener(this);
        moveRightButton.addActionListener(this);
    }
    
    public void valueChanged(ListSelectionEvent event){
        if(event.getSource() == laneTable.getSelectionModel() && getSelectedLane() != null){
            refreshLaneSegmentTable(getSelectedLane());
            refreshAvailableSegmentTable();
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == moveUpButton){
            decreaseSegmentSequence();
        }else if(e.getSource() == moveDownButton){
            increaseSegmentSequence();
        }else if(e.getSource() == moveLeftButton){
            addLaneSegmentToLane();
        }else if(e.getSource() == moveRightButton){
            removeLaneSegmentFromLane();
        }else if(e.getSource() == addLaneButton){
            addLane();
        }else if(e.getSource() == removeLaneButton){
            removeLane();
        }else if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
        
    }
    
    
    private void decreaseSegmentSequence(){
        GtsLane lane = getSelectedLane();
        int index = laneSegmentTable.getSelectedRow();
        if(index == 0) return;
        
        view.getDrawing().getModel().swapLaneSegmentMap(
        		 lane.getLaneSegments().get(index).getLaneSegmentMap(lane.getLaneId()),
        		 lane.getLaneSegments().get(index-1).getLaneSegmentMap(lane.getLaneId()));
        lane.moveUpLaneSegment(index);
        laneSegmentTable.setModel(new LaneSegmentTableModel(lane.getLaneSegments()));
        laneSegmentTable.setRowSelectionInterval(index-1, index -1);
    }
    
    private void increaseSegmentSequence(){
    	GtsLane lane = getSelectedLane();
        int index = laneSegmentTable.getSelectedRow();
        if(index >= lane.getLaneSegments().size() -1) return;
        view.getDrawing().getModel().swapLaneSegmentMap(
       		 lane.getLaneSegments().get(index).getLaneSegmentMap(lane.getLaneId()),
       		 lane.getLaneSegments().get(index).getLaneSegmentMap(lane.getLaneId()));
      
        lane.moveUpLaneSegment(index + 1);
        laneSegmentTable.setModel(new LaneSegmentTableModel(lane.getLaneSegments()));
        laneSegmentTable.setRowSelectionInterval(index+1, index +1);
        view.getDrawing().refreshCarriers();
    }
    
    private void addLaneSegmentToLane(){
        GtsLaneSegment segment = getSelectedAvailableLaneSegment();
        GtsLane lane = getSelectedLane();
        getModel().createLaneSegmentMap(lane,segment);
        refreshLaneTable();
        refreshLaneSegmentTable(lane);
        refreshAvailableSegmentTable();
        view.getDrawing().refreshCarriers();
    }
    
    private void removeLaneSegmentFromLane(){
    	GtsLaneSegment segment = getSelectedLaneSegment();
        GtsLane lane = getSelectedLane();
        getModel().removeLaneSegmentMap(lane,segment);
        refreshLaneTable();
        refreshLaneSegmentTable(lane);
        refreshAvailableSegmentTable();
        view.getDrawing().refreshCarriers();

    }
    
    private void addLane(){
        
        String laneName = JOptionPane.showInputDialog(this, "Input the lane Name");
        
        if(laneName == null) return;
        
        if(getModel().findLane(laneName) != null) {
            JOptionPane.showMessageDialog(this, "Lane name exists. Please input a different name!", "Incorrect Lane Name", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        GtsLane lane = new GtsLane(laneName);
        lane.setLaneDescription("Lane " + laneName);

        getModel().createLane(lane);
        refreshLaneTable();
    }
    
    private void removeLane(){
        
        GtsLane lane = this.getSelectedLane();
        
        if(!lane.getLaneSegmentMaps().isEmpty()) {
        	MessageDialog.showError(view.getDrawing().getController().getWindow(),
        			"Lane " + lane.getLaneName() + " cannot be deleted due to it has lane segment maps");
        	return;
        }
        String laneName = this.getSelectedLane().getLaneName();
        if(laneName == null || laneName.length() == 0) return;
        if(JOptionPane.showConfirmDialog(view.getDrawing().getController().getWindow(),
                        "Are you sure that you want to remove lane " + laneName + " ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        
        getModel().removeLane(getSelectedLane());
        refreshLaneTable();
        refreshAvailableSegmentTable();
            
        view.getDrawing().refreshCarriers();
    }
    
    private GtsLane getSelectedLane(){
        if(laneTable.getSelectedRow() == -1) return null;
        return ((LaneTableModel)laneTable.getModel()).getLane(laneTable.getSelectedRow());
    }
    
    private GtsLaneSegment getSelectedLaneSegment(){
        return ((LaneSegmentTableModel)laneSegmentTable.getModel()).getLaneSegment(laneSegmentTable.getSelectedRow());
    }
    
    
    private GtsLaneSegment getSelectedAvailableLaneSegment(){
        return ((LaneSegmentTableModel)availableSegmentTable.getModel()).getLaneSegment(availableSegmentTable.getSelectedRow());
    }
    
    private void refreshLaneTable(){
        int index = laneTable.getSelectedRow();
        laneTable.setModel(laneTableModel = new LaneTableModel(getModel().getLanes()));
        laneTable.getModel().addTableModelListener(this);
   
        if(index >= laneTable.getModel().getRowCount()){
            index = 0;
        }
        if(laneTable.getModel().getRowCount() > 0)
            laneTable.setRowSelectionInterval(index, index);
    }
    
    private void refreshLaneSegmentTable(GtsLane lane){
        laneSegmentTable.setModel(new LaneSegmentTableModel(lane.getLaneSegments()));
        if(laneSegmentTable.getModel().getRowCount() > 0)
            laneSegmentTable.setRowSelectionInterval(0, 0);
    }
    
    private void refreshAvailableSegmentTable(){
    	GtsLane lane = getSelectedLane();
        availableSegmentTable.setModel(new LaneSegmentTableModel(getModel().getAvailableLaneSegments(lane)));
        if(availableSegmentTable.getModel().getRowCount() > 0)
            availableSegmentTable.setRowSelectionInterval(0, 0);
    }
    
    /**
     * lane table changed - save the change to the database
     */
    
    public void tableChanged(TableModelEvent e) {
       
        getModel().updateLane(laneTableModel.getLane(e.getFirstRow()));
        
        view.getDrawing().refreshCarriers();
       
        laneTable.repaint();
        
    }
    
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }

    
    
    private abstract class TableModel extends AbstractTableModel{
        
    	private static final long serialVersionUID = 1L;
		
    	protected String[] names;
        
        public String getColumnName(int column) {
            return names[column];
        }    
        public int getColumnCount() {
            if(names.length == 0) return 4;
            return names.length;
        }
    }
    
    private class LaneTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        private List<GtsLane> lanes;
        
        public LaneTableModel(List<GtsLane> lanes){
            this.names = new String[] {"Name","Description","Capacity","LaneType"};
            this.lanes = lanes;
        }
        
        public int getRowCount() {
            return lanes.size();
        }
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         *  @param columnIndex  the column being queried
         *  @return the Object.class
         */
        
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex <= 1) return String.class;
            else if(columnIndex == 2 || columnIndex == 3) return Integer.class;
            else return super.getColumnClass(columnIndex);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex >= 1) return true;
            return false;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= lanes.size()) return null;
            GtsLane lane = lanes.get(rowIndex);
            switch(columnIndex){
                case 0: return lane.getLaneName();
                case 1: return lane.getLaneDescription();
                case 2: return lane.getLaneCapacity();
                case 3: return lane.getLaneTypeValue();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            
            if(row >= lanes.size()) return;
            GtsLane lane = lanes.get(row);
            
            if(column == 1){
              
                // lane description
                
                lane.setLaneDescription((String)value);
                
                
            } else if(column == 2){
                
                // lane capacity
                
                int val = (Integer) value;
                if(val <0 || val > 255)return;
                
                lane.setLaneCapacity(val);
                
            }else if(column == 3) {
            	int val = (Integer) value;
                if(val < 0 || val >=  GtsLaneType.values().length) return;
                lane.setLaneTypeValue(val);
            }

            this.fireTableCellUpdated(row, column);

        }
       
        public GtsLane getLane(int index){
            return lanes.get(index);
        }
    }

    private class LaneSegmentTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        private List<GtsLaneSegment> segments;
        
        public LaneSegmentTableModel(List<GtsLaneSegment> segments){
            this.names = new String[] {"Name","Capacity"};
            this.segments = segments;
        }
     
        public int getRowCount() {
            return segments.size();
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= segments.size()) return null;
            GtsLaneSegment segment = segments.get(rowIndex);
            switch(columnIndex){
                case 0: return segment.getLaneSegmentName();
                case 1: return segment.getCapacity();
            }
            return null;
        }
        
        public GtsLaneSegment getLaneSegment(int index){
            return segments.get(index);
        }
    }
    
    private GtsTrackingModel getModel(){
    	return view.getDrawing().getModel();
    }


    
}
