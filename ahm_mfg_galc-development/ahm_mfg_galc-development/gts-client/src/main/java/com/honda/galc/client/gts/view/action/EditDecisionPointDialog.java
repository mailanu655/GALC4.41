package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsDecisionPointCondition;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsLane;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsMoveCondition;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>EditDecisionPointDialog</code> is ...
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
 * <TD>Apr 23, 2008</TD>
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

public class EditDecisionPointDialog extends JDialog implements  ActionListener,TableModelListener,ListSelectionListener{

    private static final long serialVersionUID = 1L;

    private GtsDrawingView view;
    private JPanel mainPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private JPanel upperLeftPanel = new JPanel();
    private JPanel lowerLeftPanel = new JPanel();
    private JPanel lowerCenterPanel = new JPanel();
    private JPanel lowerRightPanel = new JPanel();
    
    private JButton addDecisionPointButton =    new JButton("  Add ");
    private JButton removeDecisionPointButton = new JButton("Remove ");
    private JButton addDecisionPointConditionButton =    new JButton("  Add ");
    private JButton removeDecisionPointConditionButton = new JButton("Remove ");
    private JButton addMoveButton =    new JButton("  Add ");
    private JButton removeMoveButton = new JButton("Remove ");
    private JButton addMoveConditionButton =    new JButton("  Add ");
    private JButton removeMoveConditionButton = new JButton("Remove ");

    private JTable decisionPointTable = new JTable();
    private JTable decisionPointConditionTable = new JTable();
    private JTable moveTable = new JTable();
    private JTable moveConditionTable = new JTable();
    
    private List<GtsDecisionPoint> decisionPoints;
    
    private List<GtsDecisionPointCondition> decisionPointConditions;
    
    private List<GtsMove> moves;
    
    private List<GtsMoveCondition> moveConditions;
    
    
    
    private GtsDecisionPoint currentDecisionPoint;
    private GtsMove currentMove;
    
    
    public EditDecisionPointDialog(GtsDrawingView view){
 
        super(view.getDrawing().getController().getWindow(),true);
        this.view = view;
        
        decisionPoints = getModel().fetchAllDecisionPoints();
        decisionPointConditions = getModel().fetchAllDecisionPointConditions();
        moves = getModel().fetchAllMoves();
        moveConditions = getModel().fetchAllMoveConditions();
        setTitle("Configure Decision Point Window");
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(view);
        decisionPointTable.setRowSelectionInterval(0, 0);
        
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
        
        decisionPointTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decisionPointTable.setModel(new DecisionPointTableModel());
        decisionPointTable.getModel().addTableModelListener(this);
        decisionPointTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        decisionPointTable.setPreferredScrollableViewportSize(new Dimension(600,200));
        decisionPointTable.setColumnSelectionAllowed(false);
        decisionPointTable.setRowSelectionAllowed(true);
        
        decisionPointTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(decisionPointTable);
        upperLeftPanel.setBorder(new TitledBorder("Decision Point List"));
        upperLeftPanel.setLayout(new BorderLayout());
        upperLeftPanel.add(scrollPane,BorderLayout.CENTER);
        upperLeftPanel.add(this.createCommandPanel(addDecisionPointButton, removeDecisionPointButton),BorderLayout.SOUTH);
        
        upperPanel.setLayout(new BorderLayout());
        upperPanel.add(upperLeftPanel,BorderLayout.CENTER);
        
    }
    
    private void setLowerPanel(){
        
        decisionPointConditionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decisionPointConditionTable.setModel(new DecisionPointConditionTableModel());
        decisionPointConditionTable.getModel().addTableModelListener(this);
        decisionPointConditionTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        decisionPointConditionTable.setPreferredScrollableViewportSize(new Dimension(300,200));
        decisionPointConditionTable.setColumnSelectionAllowed(false);
        decisionPointConditionTable.setRowSelectionAllowed(true);
        decisionPointConditionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(decisionPointConditionTable);
        
        lowerLeftPanel.setBorder(new TitledBorder("Decision Point Condition List"));
        lowerLeftPanel.setLayout(new BorderLayout());
        lowerLeftPanel.add(scrollPane,BorderLayout.CENTER);
        lowerLeftPanel.add(this.createCommandPanel(addDecisionPointConditionButton, removeDecisionPointConditionButton),BorderLayout.SOUTH);
        
        moveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moveTable.setModel(new MoveTableModel());
        moveTable.getModel().addTableModelListener(this);
        moveTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        moveTable.setPreferredScrollableViewportSize(new Dimension(300,200));
        moveTable.setColumnSelectionAllowed(false);
        moveTable.setRowSelectionAllowed(true);
        moveTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(moveTable);
        
        lowerCenterPanel.setBorder(new TitledBorder("Move List"));
        lowerCenterPanel.setLayout(new BorderLayout());
        lowerCenterPanel.add(scrollPane1,BorderLayout.CENTER);
        lowerCenterPanel.add(this.createCommandPanel(addMoveButton, removeMoveButton),BorderLayout.SOUTH);
       
        moveConditionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moveConditionTable.setModel(new MoveConditionTableModel());
        moveConditionTable.getModel().addTableModelListener(this);
        moveConditionTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        moveConditionTable.setPreferredScrollableViewportSize(new Dimension(300,200));
        moveConditionTable.setColumnSelectionAllowed(false);
        moveConditionTable.setRowSelectionAllowed(true);
        moveConditionTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setViewportView(moveConditionTable);
        
        lowerRightPanel.setBorder(new TitledBorder("Move Condition List"));
        lowerRightPanel.setLayout(new BorderLayout());
        lowerRightPanel.add(scrollPane2,BorderLayout.CENTER);
        lowerRightPanel.add(this.createCommandPanel(addMoveConditionButton, removeMoveConditionButton),BorderLayout.SOUTH);
        
        lowerPanel.setLayout(new BorderLayout());
        lowerPanel.add(lowerLeftPanel,BorderLayout.WEST);
        lowerPanel.add(lowerCenterPanel,BorderLayout.CENTER);
        lowerPanel.add(lowerRightPanel,BorderLayout.EAST);
    }
    
    
    private JPanel createCommandPanel(JButton addButton, JButton removeButton){
        
        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.X_AXIS));
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(addButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(removeButton);
        return commandPanel;
        
    }
    
    private void addActionListeners(){
        
        decisionPointTable.getSelectionModel().addListSelectionListener(this);
        moveTable.getSelectionModel().addListSelectionListener(this);
        addDecisionPointButton.addActionListener(this);
        removeDecisionPointButton.addActionListener(this);
        addDecisionPointConditionButton.addActionListener(this);
        removeDecisionPointConditionButton.addActionListener(this);
        addMoveButton.addActionListener(this);
        removeMoveButton.addActionListener(this);
        addMoveConditionButton.addActionListener(this);
        removeMoveConditionButton.addActionListener(this);
        
    }
     
    public void valueChanged(ListSelectionEvent event){
        
        if(event.getValueIsAdjusting()) return;
        
        if(event.getSource() == decisionPointTable.getSelectionModel()){
            currentDecisionPoint = ((DecisionPointTableModel)decisionPointTable.getModel()).getDecisionPoint(decisionPointTable.getSelectedRow());
            moveTable.updateUI();
            moveTable.clearSelection();
            if(moveTable.getRowCount() > 0) moveTable.setRowSelectionInterval(0, 0);
            decisionPointConditionTable.updateUI();
        }else if(event.getSource()== moveTable.getSelectionModel()){
            currentMove = ((MoveTableModel)moveTable.getModel()).getMove(moveTable.getSelectedRow());
            moveConditionTable.updateUI();
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == addDecisionPointButton) {
            
            addDecisionPoint();
            
        }else if(e.getSource() == removeDecisionPointButton) {
            
            removeDecisionPoint();
            
        }else if(e.getSource() == addDecisionPointConditionButton) {
            
            addDecisionPointCondition();
            
        }else if(e.getSource() == removeDecisionPointConditionButton) {
            
            removeDecisionPointCondition();
            
        }else if(e.getSource() == addMoveButton) {
            
            addMove();
            
        }else if(e.getSource() == removeMoveButton) {
            
            removeMove();
            
        }else if(e.getSource() == addMoveConditionButton) {
            
            addMoveCondition();
            
        }else if(e.getSource() == removeMoveConditionButton) {
            
           removeMoveCondition();
            
        }
    }
    
    private void addDecisionPoint(){
        
        GtsDecisionPoint dp = getModel().createDecisionPoint();
        
        decisionPoints.add(dp);

        decisionPointTable.updateUI();
    }
    
    private void removeDecisionPoint() {
        
        int index = decisionPointTable.getSelectedRow();
        if(index < 0) return;
        
        if(this.decisionPointConditionTable.getRowCount() > 0 || this.moveTable.getRowCount() > 0){
            JOptionPane.showMessageDialog(this, 
                            "Decision point not allowed to be removed before its decision point conditions or moves are removed!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(JOptionPane.showConfirmDialog(this,
                        "Are you sure that you want to remove current decision point ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        
            
        GtsDecisionPoint dp = decisionPoints.get(index);
        getModel().removeDecisionPoint(dp);
        decisionPoints.remove(dp);
        decisionPointTable.revalidate();
        if(index > 0)decisionPointTable.setRowSelectionInterval(index -1, index -1);
        
    }
    
    private void addMove(){
        
        if(currentDecisionPoint == null) return;
        
        String source = this.selectLane("Source");
        
        if(source == null) return;
        
        String dest = this.selectLane("Destination");
        
        if(dest == null) return;
        
        if(moveExists(source,dest)) {
            JOptionPane.showMessageDialog(this, 
                            "The move exists. Could not add this move", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        GtsMove move = getModel().createMove(source, dest, currentDecisionPoint.getId().getDecisionPointId());
         
        moves.add(move);
            
        moveTable.updateUI();
        
     }
    
    private String selectLane(String sd){
        
        String[] laneNames = new String[getModel().getLanes().size()];
        
        int i = 0;
        for(GtsLane lane : getModel().getLanes()){
            laneNames[i++] = lane.getLaneName();
        }
        
        String laneName = (String)JOptionPane.showInputDialog(
                            this,
                            "Please select the " + sd + " lane ",
                            sd + " Lane Selection",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            laneNames,
                            laneNames[0]);
        
        return laneName;
    }
    
    private boolean moveExists(String source, String dest) {
        
        for(GtsMove move : moves) {
            if(move.getSource().equals(source) && move.getDestination().equals(dest)) return true;
        }
        
        return false;
    }
    
    private void removeMove(){
        
        int index = moveTable.getSelectedRow();
        if(index < 0) return;
        
        if(this.moveConditionTable.getRowCount() > 0){
            JOptionPane.showMessageDialog(this, 
                            "Move not allowed to be removed before its move conditions are removed!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(JOptionPane.showConfirmDialog(this,
                        "Are you sure that you want to remove current move ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        
        GtsMove mv = ((MoveTableModel)moveTable.getModel()).getMove(index);
        
       getModel().removeMove(mv);
       moves.remove(mv);
       moveTable.revalidate();
       if(index > 0)moveTable.setRowSelectionInterval(index -1, index -1);
        
    }
    
    private void addDecisionPointCondition(){
        
        if(currentDecisionPoint == null) return;
        
        String indicatorId = selectIndicator();
        
        if(indicatorId == null) return;
        
        GtsDecisionPointCondition dpc = getModel().createDecisionPointCondition(currentDecisionPoint.getId().getDecisionPointId(), indicatorId);
            
        decisionPointConditions.add(dpc);
            
        decisionPointConditionTable.updateUI();
        
    }
   
    private void removeDecisionPointCondition(){
       
       int index = decisionPointConditionTable.getSelectedRow();
       if(index < 0) return;
       
       if(JOptionPane.showConfirmDialog(this,
                       "Are you sure that you want to remove current decision point condition ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                       != JOptionPane.YES_OPTION) return;
       
       GtsDecisionPointCondition dpc = ((DecisionPointConditionTableModel)decisionPointConditionTable.getModel()).
                   getDecisionPointCondition(index);

       getModel().removeDecisionPointCondition(dpc);
       decisionPointConditions.remove(dpc);
       decisionPointConditionTable.revalidate();
       if(index > 0)decisionPointConditionTable.setRowSelectionInterval(index -1, index -1);
       
    }
    
    private void addMoveCondition(){
        
        if(currentMove == null) return;
        
        String str = selectIndicator();
        
        if(str == null) return;
        
        GtsMoveCondition mc = getModel().createMoveCondition(currentMove.getSource(), currentMove.getDestination(), str);
            
        moveConditions.add(mc);
            
        moveConditionTable.updateUI();
        
    }
    
    private void removeMoveCondition(){
        
        int index = moveConditionTable.getSelectedRow();
        if(index < 0) return;
        
        if(JOptionPane.showConfirmDialog(this,
                        "Are you sure that you want to remove current move condition ?", "Confirmation", JOptionPane.YES_NO_OPTION)
                        != JOptionPane.YES_OPTION) return;
        
        GtsMoveCondition mc = ((MoveConditionTableModel)moveConditionTable.getModel()).
                            getMoveCondition(index);
        
        getModel().removeMoveCondition(mc);
        moveConditions.remove(mc);
        moveConditionTable.revalidate();
        if(index > 0)moveConditionTable.setRowSelectionInterval(index -1, index -1);
        
    }
    
    private String selectIndicator(){
        
        List<GtsIndicator> indicators = getModel().fetchAllIndicators();
        if(indicators.size() <= 0) return null;
        
        String[] indicatorNames = new String[indicators.size()];
        
        int i = 0;
        for(GtsIndicator indicator : indicators){
            indicatorNames[i++] = indicator.getIndicatorName();
        }
        
        String indicator = (String)JOptionPane.showInputDialog(
                            this,
                            "Please select the indicator ",
                            " Indicator Selection",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            indicatorNames,
                            indicatorNames[0]);
        
        return indicator;
    }
    
    /**
     * lane table changed - save the change to the database
     */
    
    public void tableChanged(TableModelEvent e) {
        
        TableModel model = (TableModel)e.getSource();
        if(model instanceof DecisionPointTableModel){
            
            getModel().updateDecisionPoint(decisionPoints.get(e.getFirstRow()));
            
        }else if(model instanceof DecisionPointConditionTableModel){

            GtsDecisionPointCondition dpc = ((DecisionPointConditionTableModel)decisionPointConditionTable.getModel()).
                                    getDecisionPointCondition(decisionPointConditionTable.getSelectedRow());
            getModel().updateDecisionPointCondition(dpc);
            
        }else if(model instanceof MoveConditionTableModel){
            
            GtsMoveCondition mc = ((MoveConditionTableModel)moveConditionTable.getModel()).
                                getMoveCondition(moveConditionTable.getSelectedRow());

            getModel().updateMoveCondition(mc);
            
        }
        
    }
    
    public GtsTrackingModel getModel() {
    	return view.getDrawing().getModel();
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
    
    private class DecisionPointTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        public DecisionPointTableModel(){
            this.names = new String[] {"Name","Description","Rule Class","Execution Time Interval","Enabled"};
        }
        
        public int getRowCount() {
            return decisionPoints.size();
        }
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         *  @param columnIndex  the column being queried
         *  @return the Object.class
         */
        
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex <= 2) return String.class;
            else if(columnIndex == 3) return Integer.class;
            else if(columnIndex == 4) return Boolean.class;
            else return super.getColumnClass(columnIndex);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }
        
        public GtsDecisionPoint getDecisionPoint(int index){
            
            if(index < 0 || index >= this.getRowCount()) return null;
            
            return decisionPoints.get(index);
            
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= decisionPoints.size()) return null;
            GtsDecisionPoint dp = decisionPoints.get(rowIndex);
            switch(columnIndex){
                case 0: return dp.getDecisionPointName();
                case 1: return dp.getDecisionPointDescription();
                case 2: return dp.getRuleClass();
                case 3: return dp.getTimeInterval();
                case 4: return dp.isEnabled();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            
            if(row >= decisionPoints.size()) return;
            GtsDecisionPoint dp = decisionPoints.get(row);
            
            switch(column) {
                case 0 : 
                    dp.setDecisionPointName((String)value);
                    break;
                case 1 : 
                    dp.setDecisionPointDescription((String) value);
                    break;
                case 2 : 
                    dp.setRuleClass((String)value);
                    break;
                case 3 : 
                    dp.setTimeInterval((Integer) value);
                    break;
                case 4 : 
                    dp.setEnabled((Boolean)value);
                    break;
            } 

            this.fireTableCellUpdated(row, column);

        }
    }

    private class DecisionPointConditionTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        public DecisionPointConditionTableModel(){
            this.names = new String[] {"Indicator","Required Status"};
       }
     
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }
        
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 1) return Boolean.class;
            else return String.class;
        }
        
        public int getRowCount() {
            int count = 0;
            if(currentDecisionPoint == null) return 0;
            for(GtsDecisionPointCondition condition: decisionPointConditions){
                if(condition.getDecisionPointId() == currentDecisionPoint.getId().getDecisionPointId()) count++;
            }
            return count;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            GtsDecisionPointCondition condition = getDecisionPointCondition(rowIndex);
            if(condition == null) return null;
            
            switch(columnIndex){
                case 0: return condition.getId().getIndicatorId();
                case 1: return condition.isRequired();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            GtsDecisionPointCondition condition = getDecisionPointCondition(row);
            if(condition == null) return;
            
            if(column == 1){
                condition.setRequiredValue((Boolean)value == true ? 1 : 0);
            }
            
            this.fireTableCellUpdated(row, column);
        }    
        
        private GtsDecisionPointCondition getDecisionPointCondition(int rowIndex){
            
            int count = 0;
            for(GtsDecisionPointCondition condition: decisionPointConditions){
                if(condition.getDecisionPointId() == currentDecisionPoint.getId().getDecisionPointId()) {
                    if(count == rowIndex) return condition;
                    count++;
                }
            }
            return null;
        }
        
    }

    private class MoveTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        public MoveTableModel(){
            
            this.names = new String[] {"Source Lane","Destination Lane"};
            
       }
     
        public int getRowCount() {
            int count = 0;
            if(currentDecisionPoint == null) return 0;
            for(GtsMove move: moves){
                if(move.getDecisionPointId() == currentDecisionPoint.getId().getDecisionPointId()) count++;
            }
            return count;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            GtsMove move = getMove(rowIndex);
            if(move == null) return null;
            
            switch(columnIndex){
                case 0: return move.getSource();
                case 1: return move.getDestination();
            }
            return null;
        }
        
        public GtsMove getMove(int rowIndex){
            
            int count = 0;
            for(GtsMove move: moves){
                if(move.getDecisionPointId() == currentDecisionPoint.getId().getDecisionPointId()) {
                    if(count == rowIndex) return move;
                    count++;
                }
            }
            return null;
        }
        
    }

    
   private class MoveConditionTableModel extends TableModel{
        
        private static final long serialVersionUID = 1L;
        
        public MoveConditionTableModel(){
            
            this.names = new String[] {"Indicator","Required Status"};
            
       }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }
        
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 1) return Boolean.class;
            else return String.class;
        }
        
        public int getRowCount() {
            int count = 0;
            if(currentMove == null) return 0;
            for(GtsMoveCondition mc: moveConditions){
                if(mc.getId().getSourceLaneId().equals(currentMove.getSource()) &&
                   mc.getId().getDestinationLaneId().equals(currentMove.getDestination()))
                   count++;
            }
            return count;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            GtsMoveCondition mc = getMoveCondition(rowIndex);
            if(mc == null) return null;
            
            switch(columnIndex){
                case 0: return mc.getId().getIndicatorId();
                case 1: return mc.isRequired();
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            GtsMoveCondition mc = getMoveCondition(row);
            if(mc == null) return ;
            
            if(column == 1){
                mc.setRequiredValue((Boolean)value);
            }
            
            this.fireTableCellUpdated(row, column);
        }    

        
        public GtsMoveCondition getMoveCondition(int rowIndex){
            
            int count = 0;
            for(GtsMoveCondition mc: moveConditions){
                if(mc.getId().getSourceLaneId().equals(currentMove.getSource()) &&
                   mc.getId().getDestinationLaneId().equals(currentMove.getDestination())){
                    if(count == rowIndex) return mc;
                    count++;
                }
            }
            return null;
        }
        
    }
    
}
