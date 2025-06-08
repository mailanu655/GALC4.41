package com.honda.galc.client.datacollection.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.property.CommonTlPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.VinDisplayValue;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

/**
 * <h3>Class description</h3>
 * <h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jun. 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author P Chou
 */

public class ExpectedProductSelectView extends JDialog  {

    private static final long serialVersionUID = 8872016825736146383L;
    private List<String> products = new ArrayList<String>();
    private static final String[] engineListTableCol = {"Incoming Product Id"};
    private JPanel productListPanel = null;
    private JScrollPane scrollPaneListTable = null;
    private JTable tableProducts = null;
    private JPanel buttonPanel = null;
    private JButton buttonOk = null;
    private JButton buttonCancel = null;
    private String skippedToProduct = null;
    private String skippedToProductPointer = null;
    private ClientContext context;
    private String skippedEngines = null;
    private boolean canceled = false;
    private JTextField productIdField;
    private CommonTlPropertyBean tlProperty;
	private FrameLinePropertyBean frameLinePropertyBean;

	public ExpectedProductSelectView(ClientContext context) {
    	super(context.getFrame(),true);

    	this.context = context;
    	initialize();
    }

    /**
     * initialize the view
     *
     */
    private void initialize()
    {
    	try {
    		setName("Incoming Product List");
    		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    		setSize(400, 400);
    		setTitle("Lot Controll Station: Select or Scan product number to skip to");			
    		setContentPane(getJScrollPaneProductList());
    		addListeners();
    	} catch (java.lang.Throwable ivjExc) {
    		handleException(ivjExc);
    	}
    }
    
    private void handleException(java.lang.Throwable e) {
    	Logger.getLogger().error(e, "---UNHANDLED EXCEPTION--");
    }

    /**
     * This method initializes labelProductList	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JPanel getJScrollPaneProductList() {
    	if (productListPanel == null) {			
    		try {
    			productListPanel = new JPanel();				
    			productListPanel.setLayout(new BorderLayout());
    			productListPanel.add(getJTextFieldProductId(),BorderLayout.NORTH);
    			productListPanel.add(getJScrollPaneListTable(), BorderLayout.CENTER);
    			productListPanel.add(getJButtonPanel(), BorderLayout.SOUTH);

    		} catch (java.lang.Throwable ex) {
    			handleException(ex);
    		}

    	}
    	return productListPanel;
    }

    /**
     * This method initializes jScrollPaneListTable	
     * 	
     * @return javax.swing.JScrollPane	
     */
    private JScrollPane getJScrollPaneListTable() {
    	if (scrollPaneListTable == null) {
    		scrollPaneListTable = new JScrollPane();
    		scrollPaneListTable.setViewportView(getJTableProducts());
    	}
    	return scrollPaneListTable;
    }

	private void addListeners() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				cancel();
			}
		});
		getJButtonOk().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				done();
			}
		});
		getJButtonCancel().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();

			}
		});
		getJTextFieldProductId().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doneScan();
			}
		});
	}

	private void setTextField(String productId) {
		if (products.isEmpty()) return;
		// Set the first next expected product to be skip
		if(StringUtils.isEmpty(productId))
			getJTextFieldProductId().setText(products.get(0));
		else
			getJTextFieldProductId().setText(productId);
		
		getJTextFieldProductId().selectAll();
		getJTextFieldProductId().requestFocus();
	}
	
    /**
     * This method initializes tableProducts	
     * 	
     * @return javax.swing.JTable	
     */
    @SuppressWarnings("serial")
	private JTable getJTableProducts() {
    	if (tableProducts == null) {
    		tableProducts = new JTable(){
    			public boolean isCellEditable(int rowIndex, int vColIndex) {
    		        return false;
    		    }
    		};
    		try {
    			//Initialize the product list
    			DefaultTableModel tm = new DefaultTableModel(createTableData(), engineListTableCol);

    			tableProducts.setModel(tm);
    			tableProducts.setLocation(new Point(0, 0));
    			tableProducts.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    			
    			ListSelectionModel rowSM = tableProducts.getSelectionModel();
    			//rowSM.setLeadSelectionIndex(0);

    			rowSM.addListSelectionListener(new ListSelectionListener() {
    				public void valueChanged(ListSelectionEvent e) {
    					// Ignore extra messages.
    					if (e.getValueIsAdjusting())
    						return;
    					ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    					if (lsm.isSelectionEmpty()) {
    						//log.info("No rows are selected.");
    					} else {
    						int selectedRow = lsm.getMaxSelectionIndex();
    						skippedToProduct = null;
    						skippedEngines = null;
    						if(selectedRow > 0){
    							//mark the current selected product
        						skippedToProduct = products.get(selectedRow);
        						skippedToProductPointer	= context.getProperty().isSaveNextProductAsExpectedProduct() ? 
        								skippedToProduct : products.get(selectedRow -1);	

        						getSkippedEngines(selectedRow);
    						}
    						lsm.setSelectionInterval(0, selectedRow);
    						setTextField(skippedToProduct);
    						
    					}
    				}
    			});
    		} catch (java.lang.Throwable ex) {
    			handleException(ex);
    		}
    	}
    	return tableProducts;
    }

    /**
     * Update table model from the Product list
     * @return
     */
    public Object[][] createTableData()
    {
    	// - To avoid plant floor operation error, user is allowed to select skip TO engine only
    	Object [][] tdata;
    	if (products != null && products.size() > 0) {
    		tdata = new Object[products.size()][];
    		for(int i = 0; i < products.size(); i++)
    		{
    			tdata[i] = new Object[1];
    			tdata[i][0] = products.get(i);
    		}
    	} else {
    		tdata = new Object[1][];
    	}	
    	return tdata;
    }

    /**
     * This method initializes jButtonPanel	
     * 	
     * @return javax.swing.jPanel	
     */
    private JPanel getJButtonPanel() {
    	if (buttonPanel == null) {
    		buttonPanel = new JPanel();			
    		buttonPanel.add(getJButtonOk(), null);			
    		buttonPanel.add(getJButtonCancel(), null);
    	}
    	return buttonPanel;
    }


    /**
     * This method initializes jButtonOk	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonOk() {
    	if (buttonOk == null) {
    		buttonOk = new JButton();
    		buttonOk.setText("OK");
    	}
    	return buttonOk;
    }

    /**
     * This method initializes jButtonCancel	
     * 	
     * @return javax.swing.JButton	
     */
    private JButton getJButtonCancel() {
    	if (buttonCancel == null) {
    		buttonCancel = new JButton();
    		buttonCancel.setText("Cancel");
    	}
    	return buttonCancel;
    }

    private JTextField getJTextFieldProductId() {
    	if (productIdField == null) {
    		productIdField = new JTextField();
    		productIdField.setBackground(Color.BLUE);
    		productIdField.setForeground(Color.WHITE);
    	}
    	return productIdField;
    }
    
    /**
     * close dialog
     *
     */
    public void done()
    {		
    	// take the default (1st on the list) if no selection made by user
    	if(skippedToProduct == null && products != null) {
    		if(products.size() > 0) skippedToProduct = products.get(0);
    		skippedToProductPointer = context.getProperty().isSaveNextProductAsExpectedProduct() ? 
    				skippedToProduct : getCurrentState().getExpectedProductId();
    		
    		skippedEngines = getCurrentState().getExpectedProductId();
    	}
		String productId = getCurrentState().getProductId();
		if(productId != null && !context.getDbManager().getExpectedProductManger().isInSequenceProduct(productId)) {
			// this is needed to update the expected product when the product id is actually a part id
			String previousProductId = context.getDbManager().getExpectedProductManger().findPreviousProductId(skippedToProductPointer);
			getCurrentState().getProduct().setProductId(previousProductId);
		}
    	
    	//Save skipped to/next expected to database
    	context.getDbManager().saveExpectedProductId(skippedToProductPointer,getCurrentState().getProductId());
    	
    	if(context.getProperty().isSaveNextProductAsExpectedProduct())
    		getCurrentState().setExpectedProductId(skippedToProduct);
    	else
    		getCurrentState().setExpectedProductId(null);
    		
    	skippedToProductPointer = null;

    	this.setVisible(false);
    	this.dispose();
    }

    /**
     * Cancel select operation
     *
     */
    public void cancel()
    {
    	skippedToProduct = null;
    	skippedEngines = null;
    	canceled = true;
    	this.setVisible(false);
    	this.dispose();
    }

    public String getSkippedToProduct() {
    	return skippedToProduct;
    }

    public void setSelectedEin(String selectedEin) {
    	this.skippedToProduct = selectedEin;
    }
    
    /**
     * This method is called by the action when the associate finishes scanning 
     * a product ID into the entry field and the carriage return triggers this call.
     * The product ID list is searched to return a valid product on the list and then
     * the OK button (done method) is called to finish the skip.
     */
	public void doneScan() {
		String productId = productIdField.getText().toUpperCase().trim();
		if (!StringUtils.isEmpty(productId) && products != null) {
			if (products.size() > 0) {
				for(int i = 0; i < products.size(); i++) {
					if(productId.equals(products.get(i))) {
						skippedToProductPointer = context.getProperty()
								.isSaveNextProductAsExpectedProduct() ? productId
								: (i==0 ? getCurrentState().getExpectedProductId() : products.get(i -1));
						
						skippedToProduct = getCurrentState().getExpectedProductId();
						if(i >  0) {
							getSkippedEngines(i);
						}
						done();
						return;
					}
				}
			}
		} 
		
		if(!StringUtils.isEmpty(productId) && productId.length() == context.getProperty().getMaxProductSnLength()) {
			try {
				//The Vin in not on the list but still not AF OFF
				//Make sure it's not AF OFF
				if (!isOffLinedProduct(productId) && isOnProduct(productId)) {
					skippedToProduct = productId;
					skippedToProductPointer = context.getProperty().isSaveNextProductAsExpectedProduct()
							? skippedToProduct
							: getPreviousProductIdFromInProcessProduct(productId);
					done();
					return;
				} 
			} catch (Exception e) {
				Logger.getLogger().info("Exception to scan expected product:", e.getMessage());
			}
		}
		
		noProductIdFoundError();
	}

	private void getSkippedEngines(int currentPosition) {
		skippedEngines = getCurrentState().getExpectedProductId();
		if(getTlProperty().getSkippedProductDisplayValue().equals(VinDisplayValue.PRODUCT_ID.name()))
			skippedEngines = skippedEngines + "-" + products.get(currentPosition -1).substring(context.getProperty().getMaxProductSnLength() - getTlProperty().getSkippedProductLastNumberOfDigits());
		else 
			skippedEngines = skippedEngines + "-" + products.get(currentPosition -1);
	}

	

	private String getPreviousProductIdFromInProcessProduct(String productId) {
		BaseProduct product = ProductTypeUtil.getProductDao(context.getProperty().getProductType()).findPreviousInprocessProduct(productId);
		return product == null ? "" : product.getProductId();
	}

	private boolean isOffLinedProduct(String productId) {
		ProductCheckUtil util = new ProductCheckUtil(ProductTypeUtil.findProduct(context.getProperty().getProductType(), productId), null);
		if(context.getProductType() == ProductType.FRAME)
			return util.productAlreadyProcessedCheck(getFrameLinePropertyBean().getAfOffProcessPointId());
		else
			return true; //fail the check - feature only available on Frame for now
	}
	
	private boolean isOnProduct(String productId) {
		ProductCheckUtil util = new ProductCheckUtil(ProductTypeUtil.findProduct(context.getProperty().getProductType(), productId), null);
		if(context.getProductType() == ProductType.FRAME)
			return util.productAlreadyProcessedCheck(getFrameLinePropertyBean().getAfOnProcessPointId());
		else
			return false; //fail the check - feature only available on Frame for now
	}

	private FrameLinePropertyBean getFrameLinePropertyBean() {
		if(frameLinePropertyBean == null)
			frameLinePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, context.getProcessPointId());
		return frameLinePropertyBean;
	}

	private void noProductIdFoundError() {
		getJTextFieldProductId().setBackground(Color.RED);
		getJTextFieldProductId().selectAll();
		getJTextFieldProductId().requestFocus();
		
	}
    
    /**
     * Return skipped products 
     * - Return current engine when single engine skipped
     * - Return ranges of engines when multiple engines skipped
     * @return
     */
    public String getSkippedToProducts() {
    	return skippedEngines == null ? skippedToProduct : skippedEngines;
    }
    
    public void showDialog(List<String> products){
    	updateDialog(products);
    	setLocation(200, 200);
		setVisible(true);
    }

    public void updateDialog(List<String> products){
    	List<String> cleanProducts = cleanList(products);
    	if (cleanProducts != null && cleanProducts.size() > 0)
    		getJButtonOk().setEnabled(true);
    	else
    		getJButtonOk().setEnabled(false);
    	this.products = cleanProducts;
    	this.skippedEngines = null;
    	this.skippedToProduct = null;
    	this.canceled = false;
    	DefaultTableModel tm = new DefaultTableModel(createTableData(), engineListTableCol);
    	getJTableProducts().setModel(tm);
		
		try {
			tableProducts.setRowSelectionInterval(0, 0);
		} catch (Exception e) {
			Logger.getLogger().warn("Exception to update dialog:", e.getMessage());
		}
    }
    
    private List<String> cleanList(List<String> list) {
    	if (list == null || list.isEmpty()) return null;
    	List<String> cleanList = new ArrayList<String>(list);
    	cleanList.removeAll(Arrays.asList(null,""));
    	return cleanList;
    }
    
    public DataCollectionState getCurrentState(){
    	return DataCollectionController.getInstance().getState();
    }

    public boolean isCanceled() {
    	return canceled;
    }

    public CommonTlPropertyBean getTlProperty() {
    	if(tlProperty == null)
    		tlProperty = PropertyService.getPropertyBean(CommonTlPropertyBean.class, context.getProcessPointId());
    	return tlProperty;
    }
    
    
}

