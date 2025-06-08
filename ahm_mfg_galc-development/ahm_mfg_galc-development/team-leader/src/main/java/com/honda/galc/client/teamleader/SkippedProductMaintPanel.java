package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;

import com.honda.galc.client.knuckle.KnuckleLabelPrintingUtil;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ProcessPointSelectiontPanel;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.event.ProcessPointSelectionEvent;
import com.honda.galc.client.ui.event.SelectionEvent;
import com.honda.galc.client.ui.tablemodel.SkippedProductTableModel;
import com.honda.galc.dao.product.SkippedProductDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.service.ServiceFactory;

/**
 * 
 * <h3>SkippedProductMaintPanel Class description</h3>
 * <p> SkippedProductMaintPanel description </p>
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
 * Jan 27, 2011
 *
 *
 */
public class SkippedProductMaintPanel extends TabbedPanel implements ActionListener, TableModelListener{
	
	private static final long serialVersionUID = 1L;
	
	private static final String SPLASH_SHIELD_PPID ="AF0FK11001";
	
	protected ProcessPointSelectiontPanel processPointPanel;
	protected TablePane skippedProductPanel;
	
	private SkippedProductTableModel skippedProductTableModel;
	
	private JButton printKsnButton = new JButton("Print KSN");
	
	private int startX = 10;
	private int startY = 10;
	private int processPointPanelWidth = 1000;
	private int processPointPanelHeight = 50;
	
	public SkippedProductMaintPanel() {

		super("Skipped Product", KeyEvent.VK_R);
		AnnotationProcessor.process(this);
		
	}
	
	protected void initComponents() {
		
		setLayout(new BorderLayout());
		
		add(createProcessPointSelectionPanel(),BorderLayout.NORTH);
		add(createSkippedProductPanel(),BorderLayout.CENTER);
		add(createButtonPanel(),BorderLayout.SOUTH);
		
	}

	private ProcessPointSelectiontPanel createProcessPointSelectionPanel() {
		processPointPanel= new ProcessPointSelectiontPanel(getSiteName());
		processPointPanel.setLocation(startX, startY);
		processPointPanel.setSize(processPointPanelWidth, processPointPanelHeight);
		return processPointPanel;
	}
	
	private Component createSkippedProductPanel() {
		
		skippedProductPanel = new TablePane("Skipped Products");
		skippedProductPanel.getTable().setRowHeight(20);
		skippedProductTableModel = new SkippedProductTableModel(skippedProductPanel.getTable(),new ArrayList<SkippedProduct>());
		return skippedProductPanel;
		
	}
	
	private Component createButtonPanel() {
		
		JPanel panel = new JPanel();
	    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));  
	    
	    printKsnButton.setEnabled(false);
	    
	    panel.add(Box.createHorizontalGlue());
	    panel.add(printKsnButton);
	    panel.add(Box.createHorizontalStrut(20));
	    return panel;
	}
	
	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			initComponents();
			addListeners();
			isInitialized = true;
		}
	}
	
	private void addListeners() {
		skippedProductTableModel.addTableModelListener(this);
		printKsnButton.addActionListener(this);
	}

    

	public void actionPerformed(ActionEvent e) {
		
		clearErrorMessage();
		
		SkippedProduct skippedProduct = skippedProductTableModel.getSelectedItem();
		if(skippedProduct == null) return ;
		
		try{
			SubProduct subProduct = getDao(SubProductDao.class).findByKey(skippedProduct.getId().getProductId());
			if(subProduct == null){
				setErrorMessage("Sub product " + skippedProduct.getId().getProductId() + " is not found");
				return;
			}
			
			new KnuckleLabelPrintingUtil().print(subProduct);
			
			String message = "print " + subProduct.getProductId() + " successfully";
			setMessage(message);
			getLogger().info(message);
			
		}catch (Exception ex) {
			handleException(ex);
		}
			
	}
    
    private void showSkippedProducts() {
    	
    	ProcessPoint processPoint = processPointPanel.getCurrentProcessPoint();
    	if(processPoint == null) return;
    	printKsnButton.setEnabled(processPoint.getProcessPointId().equals(SPLASH_SHIELD_PPID));
    	Exception exception = null;
    	try{
    		
    		List<SkippedProduct> skippedProducts = getDao(SkippedProductDao.class).findAllByProcessPointId(processPoint.getProcessPointId());
        	skippedProductTableModel.refresh(skippedProducts);
        	
    	}catch(Exception ex) {
    		exception = ex;
    	}
    	handleException(exception);
    }
    
   @EventSubscriber(eventClass=ProcessPointSelectionEvent.class)
    public void processPointSelectedPanelChanged(ProcessPointSelectionEvent event) {
    	if(event.isEventFromSource(SelectionEvent.PROCESSPOINT_SELECTED, processPointPanel) &&
				isValidProcessPointSelected()) {
    		showSkippedProducts();
    	}
    }
    
 	private boolean isValidProcessPointSelected() {
		return processPointPanel.getProcessPointComboBox().getComponent().getSelectedIndex() != -1;
	}
	
	public void tableChanged(TableModelEvent e) {
		
		clearErrorMessage();
		
		if(e.getSource() instanceof SkippedProductTableModel) {
			
			SkippedProductTableModel model =  (SkippedProductTableModel)e.getSource();
			SkippedProduct skippedProduct = model.getSelectedItem();
			if(skippedProduct == null) return;
			Exception exception = null;
			try{
				ServiceFactory.getDao(SkippedProductDao.class).update(skippedProduct);
				String message = "update " + skippedProduct + " successfully";
				setMessage(message);
				getLogger().info(message);
			}catch(Exception ex) {
				exception = ex;
				model.rollback();
				handleException(exception);
			}
			
		}
	}

}
