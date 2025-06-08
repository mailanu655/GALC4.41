package com.honda.galc.client.ui.component;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.product.BaseProduct;
/**
 * 
 * <h3>ProductSelectionDialog</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductSelectionDialog description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jun 21, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jun 21, 2017
 */
public class ProductSelectionDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	final String PRODUCT_SEARCH = "PRODUCT_SEARCH";
	private static final Font font = new Font("Default", Font.PLAIN, 16);
	private String productId = "";
	private int selectedIndex = -1;
	private LabeledTextField prodIdSearchField;
	private JButton selectButton;
	private List<BaseProduct> productIdList;
	private ObjectTablePane<BaseProduct> productTablePane;
	private List<ColumnMapping> columnMappings;
	protected boolean isValid;

	public ProductSelectionDialog(List<BaseProduct> productIds) {
		super();
		this.productIdList = productIds;
		init();
	}


	private void init() {
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setSize(650 + 16 * 17, 550);
		setTitle("Product Selection Panel");
		initComponents();
		initConnections(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { prodIdSearchField.getComponent().requestFocusInWindow(); }
		});
	}

	private void initConnections(final ProductSelectionDialog dialog) {
		getProductIdSeachField().getComponent().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				String prodId = getProductIdSeachField().getComponent().getText().trim();
				if (prodId.length() < 4){
					isValid =  false;
					MessageDialog.showError(dialog, "Search field must have at least 4 characters.");
					return;
				}
				
				searchProduct();
			}
		});
		getSelectButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if(productTablePane.getSelectedItem() == null) {
					MessageDialog.showError(dialog, "Please Select a Product");
					return;
				}
				productId = productTablePane.getSelectedItem().getProductId();
				BaseProduct selected = productTablePane.getSelectedItem();
				selectedIndex = productIdList.indexOf(selected); 
						
				dialog.dispose();
			}
		});
		
	}


	private void initComponents() {
		this.setLayout(new GridBagLayout());
		add(getProductIdSeachField(), getGridBagConstraint(0, 0));
		add(getProductTablePane(), getGridBagConstraint(0, 1));
		add(getSelectButton(), getGridBagConstraint(1, 1));

	}

	private JButton getSelectButton() {
		if(selectButton == null)
			selectButton = new JButton("Select");
		return selectButton;
	}


	private ObjectTablePane<BaseProduct> getProductTablePane() {
		if(productTablePane == null){
			productTablePane = new ObjectTablePane<BaseProduct>(getColumnMappings());
		}
		return productTablePane;
	}


	private List<ColumnMapping> getColumnMappings() {
		if(columnMappings == null){
			columnMappings = new ArrayList<ColumnMapping>();
			columnMappings.add(new ColumnMapping("#", "#"));
			columnMappings.add(new ColumnMapping("Product Id", "productId"));
		}
		return columnMappings;
	}


	private LabeledTextField getProductIdSeachField() {
		if(prodIdSearchField  == null){
			prodIdSearchField = new LabeledTextField("Product Id:", false);
			prodIdSearchField.setFont(font);
			prodIdSearchField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), PRODUCT_SEARCH);
			prodIdSearchField.getActionMap().put(PRODUCT_SEARCH, searchProductAction());
		}
		return prodIdSearchField;
	}


	@SuppressWarnings("serial")
	public Action searchProductAction() {
		return (new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent event) {
				searchProduct();
			}
		});
	}


	protected void searchProduct() {
		String prodId = getProductIdSeachField().getComponent().getText().trim();
		List<BaseProduct> selectedList = new ArrayList<BaseProduct>();
		for(BaseProduct bp : productIdList){
			if(bp.getProductId().endsWith(prodId))
				selectedList.add(bp);
		}
		
		if(selectedList.size() > 1)
			getProductTablePane().reloadData(selectedList);
		else if(selectedList.size() == 1){
			selectedIndex = productIdList.indexOf(selectedList.get(0));
			this.dispose();
		} 
	}
	
	private static GridBagConstraints getGridBagConstraint(int row, int column) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = row;
		c.gridx = column;
		c.anchor = GridBagConstraints.LINE_START;
		return c;
	}

	//==========getter && setter ===========
	public String getProductId() {
		return productId;
	}


	public int getSelectedIndex() {
		return selectedIndex;
	}

	
	
}
