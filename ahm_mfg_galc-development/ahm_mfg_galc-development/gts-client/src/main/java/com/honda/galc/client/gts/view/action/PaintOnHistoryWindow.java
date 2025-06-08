package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.product.Product;


/**
 * 
 * 
 * 
 * <h3>WeldProductionCountWindow Class description</h3>
 * <p> WeldProductionCountWindow description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Apr 4, 2018
 *
 *
 */
public class PaintOnHistoryWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    
    private ObjectTablePane<Product> productResultTablePane;
    
    
    private GtsDrawing drawing;
    
    
    public PaintOnHistoryWindow(GtsDrawing drawing){
        
        super(drawing.getController().getWindow(),true);
        
        setTitle("Paint On Detail Window ");
        
        this.drawing = drawing;
        
        
        initComponent();
        addActionListeners();
        
        pack();
        setSize(700, 400);
        
        this.setLocationRelativeTo(drawing.getController().getWindow());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        productResultTablePane = createProductListTablePane();
        
        productResultTablePane.reloadData(fetchProducts());
        
        setCommandPanel();
        
        mainPanel.add(productResultTablePane,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private ObjectTablePane<Product> createProductListTablePane() {
    	ColumnMappings clumnMappings = ColumnMappings.with("Product Id", "productId")
    			.put("Production Lot","productionLot")
    			.put("MTOC","productSpecCode")
    			.put("Paint On Timestamp","updateTimestamp");
    		
    		ObjectTablePane<Product> pane = 
    				new ObjectTablePane<Product>("Paint On Details",clumnMappings.get(),false,true);
    		return pane;
    }
    
     private List<Product> fetchProducts() {
    	String paintOnPPID = drawing.getModel().getPropertyBean().getPaintOnProcessPointId();
    	return drawing.getModel().findAllProducts(paintOnPPID);
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
    
    
     
}
