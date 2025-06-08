package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.gts.GtsLaneCarrier;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.util.KeyValue;

/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CarrierInfoWindow</code> is a class to display  Carrier Info
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

public class CarrierInfoWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel();
    private JPanel carrierPanel = new JPanel(new BorderLayout());
    private JPanel defectPanel = new JPanel(new BorderLayout());
    
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    private Table carrierTable; 
    
    private ObjectTablePane<KeyValue<String,String>> defectTable;
    
    private GtsLaneCarrier carrier;
    
    public CarrierInfoWindow(GtsLaneCarrier carrier){
        
        super(getFrame(),true);
        
        setTitle("Carrier Information Window");
        
        this.carrier = carrier;
        
        
        initComponent();
        addActionListeners();
        pack();
        
        this.setLocationRelativeTo(getView());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        setCarrierPanel();
        
        
        setCommandPanel();
        
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(carrierPanel,BorderLayout.WEST);
        if(isShowDefects()) {
        	setDefectPanel();
            mainPanel.add(defectPanel, BorderLayout.EAST);
        }
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private void setCarrierPanel(){
        
        Object [][] data = {{"Carrier",carrier.getCarrierId()},
                            {"Carrier Status",carrier.getCarrier().getStatus().name()},
                            {"Product Id",carrier.getProductId()},
                            {"Production Lot",carrier.getProductionLot()},
                            {"Kd Lot",carrier.getKdLot()},
                            {"Model Code",carrier.getProduct() == null ? "" : ProductSpec.excludeToModelCode(carrier.getProductSpec())},
                            {"Model Type Code",carrier.getProduct() == null ? "" : ProductSpec.extractModelTypeCode(carrier.getProductSpec())},
                            {"Model Option",carrier.getProduct() == null ? "" : ProductSpec.extractModelOptionCode(carrier.getProductSpec())},
                            {"Exterior Color Code",carrier.getProduct() == null ? ""  :carrier.getProduct().getExtColorCode()},
                            {"Interior Color Code",carrier.getProduct() == null ? ""  :carrier.getProduct().getIntColorCode()},
                            {"Product Status",carrier.getProductStatusString()},
                            {"Defect Status",carrier.getDefectStatusString()},
                            {"Inspection Status",carrier.getInspectionStatusString()},
                            {"Discrepancy Status",carrier.getDiscrepancyString()}};
        
        carrierTable = new Table(data, new Object[]{"Item","Value"}){
            
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        
        carrierTable.setColumnAlignments(new int[]{JLabel.LEFT,JLabel.CENTER});
        carrierTable.setBackground(getBackground());
        
        carrierTable.setFont(new Font("Arial", Font.BOLD, 14));

        carrierTable.pack(10);
        
        carrierTable.setTableHeader(null);

        carrierTable.setRowHeight(25);
        carrierTable.setPreferredScrollableViewportSize(carrierTable.getPreferredSize());
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(carrierTable);
  
        
        carrierPanel.setBorder(new TitledBorder("Carrier & Product Information"));
        
        carrierPanel.add(scrollPane,BorderLayout.CENTER);
 
    }
    
    private boolean isShowDefects() {
    	GtsTrackingModel model = getView().getDrawing().getModel();
    	return model.getServerPropertyBean().isCheckDefectRepaired() && carrier.getDefectStatusString().equalsIgnoreCase(DefectStatus.OUTSTANDING.getName());
    }
    
    private void setDefectPanel() {
    	
    	List<KeyValue<String,String>> defects = new ArrayList<KeyValue<String,String>>();
    	GtsTrackingModel model = getView().getDrawing().getModel();
    	if(carrier.getProduct() != null) {
    		defects = model.findAllOutstandingDefects(carrier.getProductId());
    	}
    	
    	ColumnMappings columnMappings = ColumnMappings.with("Inspection Part","key")
    			.put("Defect Name","value");
    		
    	defectTable = new ObjectTablePane<KeyValue<String,String>>(columnMappings.get(),true);
    	
    	defectTable.getTable().setFont(Fonts.DIALOG_PLAIN_20);
    	defectTable.getTable().setRowHeight(30);
    	
    	
    	defectPanel.setBorder(new TitledBorder("Outstanding Defect List"));
         
    	defectPanel.add(defectTable,BorderLayout.CENTER);
    	
    	defectTable.reloadData(defects);
  
    	
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
 
}
