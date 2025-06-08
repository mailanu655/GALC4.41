package com.honda.galc.client.common.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.common.datacollection.data.AutoCloseMessage;
import com.honda.galc.client.common.datacollection.data.AutoFocusMessage;
import com.honda.galc.net.ConnectionStatus;

 /** * *
 * @version 0.2 
 * @author Gangadhararao Gadde 
 * @since Aug 09, 2012 
 */ 
public class ComStatusDialog extends JDialog implements ActionListener{

    private static final int ROW_HIGHT = 28;
    private static final long serialVersionUID = 1L;
    private Map<String,ConnectionStatus> statusList;
    private JComponent owner;
    private JScrollPane statusPane;
    private JTable statusTable;
    Color ANOTHER_ROW_COLOR = Color.lightGray;

    public ComStatusDialog( JComponent owner, Map<String,ConnectionStatus> statusList) {
        super();
        this.statusList = statusList;
        this.owner = owner;
        
        initialize();
    }

    private void initialize() {

    	AnnotationProcessor.process(this);
    	
        setContentPane(getStatusPane());
        
        this.setUndecorated(true);
        this.pack();

        this.setLocation(Math.max(2,owner.getLocationOnScreen().x - (this.getWidth() - owner.getWidth())/2),
                        owner.getLocationOnScreen().y - this.getHeight() - 2);

    }

    private Container getStatusPane() {
        if(statusPane == null){
            statusPane = new JScrollPane();
        }
        
        statusPane.getViewport().add(getStatusTable());
        getStatusTable().setModel(getStatusTableModel());
        getStatusTable().setDefaultRenderer(Object.class, (TableCellRenderer)getStatusTableModel());
        
        statusTable.setPreferredScrollableViewportSize(new Dimension(360, ROW_HIGHT * statusList.size()));
        statusTable.getColumnModel().getColumn(0).setPreferredWidth(360);
        statusTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        return statusPane;
    }


    @SuppressWarnings("serial")
    private JTable getStatusTable() {
        if (statusTable == null){
            statusTable = new JTable(){
                public boolean isCellEditable (int row, int column){
                    return false;
                }
            };
            
            statusTable.setLayout(new BorderLayout());
            DefaultTableModel tm = getStatusTableModel();
            statusTable.setModel(tm);
            statusTable.setFont(new Font("sansserif", 1, 18));
            statusTable.setRowHeight(ROW_HIGHT);
        }

        return statusTable;
    }

    @SuppressWarnings("serial")
    private DefaultTableModel getStatusTableModel(){
        String[] columnNames = {"", ""};
        Object[][] rowData = getStatusRowData();

        RowColorTableModelCellRenderer tm = new RowColorTableModelCellRenderer(rowData, columnNames){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 1) {
                	if (StringUtils.isEmpty((String)value))
                		comp.setBackground(Color.WHITE);
                	else if (value.toString().equals("OK"))
                        comp.setBackground(Color.GREEN);
                    else
                        comp.setBackground(Color.RED);
                    
                }   
                return comp;
            }
            
        };

        if (rowData!=null){
            for(int i=0; i<rowData.length; i++){
                if ( i%2 != 0){
                    tm.setRowColor(i, ANOTHER_ROW_COLOR);
                } 

            }
        }

        return tm;
    }

    private String[][] getStatusRowData() {
        String[][] data = new String[statusList == null ? 0 : statusList.size()][2];
        if(statusList == null) return data;
        
        int i = 0;
        
        for( ConnectionStatus status : statusList.values()){
            if(status == null) continue;
        	data[i][0] = status.getConnectionId();
            
            data[i][1] = status.isConnected()  ? "OK" :"ERR";
            
            i++;
        }
        
        
        return data;
    }

    public void actionPerformed(ActionEvent e) {
        close();
    }

    
    
    public void close(){
    	if (this.isVisible()) {
    		this.dispose();
    		this.setVisible(false);
    		EventBus.publish(new AutoFocusMessage(this.getClass().getSimpleName(), null));
    	}

    }
    
    @EventSubscriber(eventClass = AutoCloseMessage.class)
    public void autoClose(AutoCloseMessage message){
    	close();
    }
    
    public void refreshScreen(){
    	this.setLocation(Math.max(2,owner.getLocationOnScreen().x - (this.getWidth() - owner.getWidth())/2),
                owner.getLocationOnScreen().y - this.getHeight() - 2);
    	 getStatusTable().setModel(getStatusTableModel());
    	 statusTable.setPreferredScrollableViewportSize(new Dimension(360, ROW_HIGHT * statusList.size()));
         statusTable.getColumnModel().getColumn(0).setPreferredWidth(360);
         statusTable.getColumnModel().getColumn(1).setPreferredWidth(60);
    }

}
