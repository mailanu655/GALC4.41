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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.honda.galc.client.gts.view.GtsDrawingView;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.gts.GtsColorMap;
import com.honda.galc.entity.gts.GtsOutlineMap;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>EditColorMapDialog</code> is a class to edit the color map
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

public class EditColorMapDialog extends JDialog implements  ActionListener,TableModelListener{
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawingView view;
    private JPanel mainPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JPanel lowerPanel = new JPanel();
    private JButton addButton = new JButton("Add");
    private JButton removeButton = new JButton("Remove");
    private JButton EditButton = new JButton("Edit Color");
    private JButton closeButton =      new JButton("Close");
    private JTable colorMapTable = new JTable();
    private List<GtsColorMap> colors;
    
    
    public EditColorMapDialog(GtsDrawingView view){
        super((JFrame)view.getRootPane().getParent(),true);
        this.view = view;
        setTitle("Edit Color Map Window");
        colors = view.getDrawing().getController().getModel().fetchAllColorMaps();
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(view);
        if(colorMapTable.getRowCount()>0) colorMapTable.setRowSelectionInterval(0, 0);
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
        colorMapTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        colorMapTable.setModel(new ColorMapTableModel(colors));
        colorMapTable.setRowHeight(30);
        colorMapTable.getModel().addTableModelListener(this);
        colorMapTable.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,12));
        
        colorMapTable.setPreferredScrollableViewportSize(new Dimension(350,400));
        colorMapTable.setDefaultRenderer(CellRenderer.class, new CellRenderer());
        colorMapTable.setColumnSelectionAllowed(false);
        colorMapTable.setRowSelectionAllowed(true);
        colorMapTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(colorMapTable);
        upperPanel.add(scrollPane);
     }
    
    
    private void setLowerPanel(){
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        EditButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        lowerPanel.setLayout(new BoxLayout(lowerPanel,BoxLayout.X_AXIS));
        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.add(addButton);
        lowerPanel.add(Box.createHorizontalStrut(20));
        lowerPanel.add(removeButton);
        lowerPanel.add(Box.createHorizontalStrut(20));
        lowerPanel.add(EditButton);
        lowerPanel.add(Box.createHorizontalStrut(20));
        lowerPanel.add(closeButton);
        lowerPanel.add(Box.createHorizontalGlue());
        lowerPanel.setPreferredSize(new Dimension(80,50));
    }
    
    private void addActionListeners(){
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        EditButton.addActionListener(this);
        closeButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addButton){
            addColorMap();
        }else if(e.getSource()== removeButton){
            removeColorMap();
        } else if(e.getSource() == EditButton){
            editColor(e);
        }else if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
    }
    
    
    private void addColorMap(){
 
        GtsColorMap colorMap = view.getDrawing().getModel().createColorMap(getDummyColorCode());
        
        colors.add(colorMap);
        colorMapTable.revalidate();
        
    }
    
    private void removeColorMap(){
        int index = colorMapTable.getSelectedRow();
        if(index < 0) return;
        view.getDrawing().getModel().removeColorMap(colors.get(index));
        colors.remove(index);
        colorMapTable.revalidate();
        if(index > 0)colorMapTable.setRowSelectionInterval(index -1, index -1);
    }
    
    private void editColor(ActionEvent e){
        int index = colorMapTable.getSelectedRow();
        if(index < 0) return;
        GtsColorMap oldMap = colors.get(index);
        Color chosenColor = JColorChooser.showDialog((Component) e.getSource(), "Please select color", oldMap.getColor());
        if (chosenColor != null) {
        	GtsColorMap map = oldMap;
            map.setColor(chosenColor);
            view.getDrawing().getModel().updateColorMap(map);
            colors.get(index).setColor(chosenColor);
            colorMapTable.updateUI();
        }
    }
    
    private String getDummyColorCode(){
        int index = -1;
        if(colors.isEmpty()) return "C1";
        for(GtsColorMap map:colors){
            int i = getIndex(map.getColorCode());
            if(i>index) index = i;
        }
        return "C"+Integer.toString(index + 1);
    }
    
    private int getIndex(String str){
        if(str==null || str.length() < 2  || str.charAt(0)!='C') return -1;
        try{
            return Integer.parseInt(str.substring(1)); 
        }catch(NumberFormatException e){
        }
        return -1;
    }
    
    public void tableChanged(TableModelEvent e) {
        view.getDrawing().getModel().updateColorMap(colors.get(e.getFirstRow()));
        colorMapTable.repaint();
    }
    
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }

    private class CellRenderer extends DefaultTableCellRenderer{
        private static final long serialVersionUID = 1L;
        
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(column ==4)c.setBackground(colors.get(row).getColor());
            return c;
        }

    }
   
    
    private class ColorMapTableModel extends AbstractTableModel{
        
        private static final long serialVersionUID = 1L;
        
        private String[] names;
        private List<GtsColorMap> colors;
        
        public ColorMapTableModel(List<GtsColorMap> colors){
            this.names = new String[] {"Color Code","Red","Green","Blue","Color"};
            this.colors = colors;
        }
        
        public String getColumnName(int column) {
            return names[column];
        }    
        public int getColumnCount() {
            return names.length;
        }
        
        public int getRowCount() {
            return colors.size();
        }
        
        /**
         *  Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
         *
         *  @param columnIndex  the column being queried
         *  @return the Object.class
         */
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex >0 && columnIndex <= 3) return Integer.class;
            else if(columnIndex == 4) return CellRenderer.class;
            else return super.getColumnClass(columnIndex);
        }
        
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex != 4) return true;
            return false;
        }
        
        public Object getValueAt(int rowIndex, int columnIndex) {
            if(rowIndex >= colors.size()) return null;
            GtsColorMap colorMap = colors.get(rowIndex);
            switch(columnIndex){
                case 0: return colorMap.getColorCode();
                case 1: return colorMap.getColor()== null? null:colorMap.getColor().getRed();
                case 2: return colorMap.getColor()== null? null:colorMap.getColor().getGreen();
                case 3: return colorMap.getColor()== null? null:colorMap.getColor().getBlue();
                case 4: return null;
            }
            return null;
        }
        
        public void setValueAt(Object value, int row, int column) {
            if(column<0 || column > 3) return;
            GtsColorMap map= colors.get(row);
            if(column == 0){
            	for(GtsColorMap item : colors) {
              	   if(item.equals(map)) continue;
              	   if(item.getColorCode().equalsIgnoreCase((String)value)) {
              		   MessageDialog.showError("Duplicate Color Code not allowed - " + map.getColorCode() );
              		   return;
              	   }
              	}
                map.setColorCode((String)value);
                this.fireTableCellUpdated(row, column);
                return;
            }
            int val = (Integer) value;
            if(val <0 || val > 255)return;
            
            if(column == 1){
                Color color = map.getColor();
                if(color == null) map.setColor(new Color(val,0,0));
                else map.setColor(new Color(val,color.getGreen(),color.getBlue()));
            }else if(column == 2){
                Color color = map.getColor();
                if(color == null) map.setColor(new Color(0,val,0));
                else map.setColor(new Color(color.getRed(),val,color.getBlue()));
            }else if(column == 3){
                Color color = map.getColor();
                if(color == null) map.setColor(new Color(0,0,val));
                else map.setColor(new Color(color.getRed(),color.getGreen(),val));
            }

            this.fireTableCellUpdated(row, column);
        }

    }
}
