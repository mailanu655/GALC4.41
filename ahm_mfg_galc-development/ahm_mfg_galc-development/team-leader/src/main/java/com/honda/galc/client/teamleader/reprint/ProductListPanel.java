package com.honda.galc.client.teamleader.reprint;

import java.util.List;

import javax.swing.JPanel;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.Engine;

/**
 * 
 * <h3>ProductListPanel Class description</h3>
 * <p> ProductListPanel description </p>
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
 * Apr 19, 2011
 *
 *
 */
public class ProductListPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	ObjectTablePane<? extends Object> productListTablePane;
	
	public ProductListPanel() {
		
		
	}
	
	public <T> void setItems(List<T> items) {
		
		productListTablePane = new ObjectTablePane<T>();
		
		
			
	}
	
	public void loadEngines(List<Engine> items) {
		
		this.remove(productListTablePane);
		
		ColumnMappings columnMappings = ColumnMappings.with("engine serial number","productId");
		ObjectTablePane<Engine> productListTablePane = new ObjectTablePane<Engine>(columnMappings.get());
		this.add(productListTablePane);
		productListTablePane.reloadData(items);
		
		this.productListTablePane = productListTablePane;
		
	}
	
	public ColumnMappings createColumnMappings(ProductType productType) {
		
		
		if(ProductType.ENGINE.equals(productType)){
			return ColumnMappings.with("engine serial number","productId");
		}else if (ProductType.KNUCKLE.equals(productType)) {
			return ColumnMappings.with("knuckle serial number","productId").put("sub_id","subId");
		}
		else return null;
		
	}
	
	
	
	

}
