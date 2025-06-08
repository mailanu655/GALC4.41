
package com.honda.galc.client.datacollection.view.action;
import java.awt.event.ActionEvent;


import com.honda.galc.client.datacollection.view.ClassicDataCollectionPanel;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;


public class ProductButtonAction  extends BaseDataCollectionAction{
	private static final long serialVersionUID = 4256139899025982427L;
	private String name = " ";
	public String productId = " ";
	private String productType;
	ClassicDataCollectionPanel view;
	private UpperCaseFieldBean textFieldProdId;
	private java.awt.Frame owner;
	
	public ProductButtonAction(java.awt.Frame owner, String productType, String name) {
		super(null, name);
		this.name = name;
		this.productType = productType;
		this.owner = owner;
	}
	
	public ProductButtonAction(java.awt.Frame owner, String productType, String name,  UpperCaseFieldBean text) {
		super(null, name);
		this.owner = owner;
		this.productType = productType;
		this.name = name;
		this.textFieldProdId=text;
	}
	
	public void actionPerformed(ActionEvent e) {
		Logger.getLogger().check(this.getClass().getSimpleName() + ": " + name + " action performed");
		ManualProductEntryDialog manualProductEntry = new ManualProductEntryDialog(owner,productType,name);
		manualProductEntry.setModal(true);
		manualProductEntry.setVisible(true);
		productId = manualProductEntry.getResultProductId();
		this.textFieldProdId.setText(productId);
		this.textFieldProdId.requestFocusInWindow();
	}
}