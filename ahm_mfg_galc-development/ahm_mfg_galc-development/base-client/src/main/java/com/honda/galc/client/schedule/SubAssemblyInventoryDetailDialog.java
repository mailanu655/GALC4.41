package com.honda.galc.client.schedule;

import javax.swing.JDialog;

/**
 * 
 *   
 * @author Zack Chai
 * Nov 23, 2013
 *
 *
 */
public class SubAssemblyInventoryDetailDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	
	private String zoneName;
	private String productSpecCode;
	private String orderNo;
	
	
	public SubAssemblyInventoryDetailDialog(SubAssemblyInventoryPanel subAssemblyInventoryPanel,String zoneName, String productSpecCode, String orderNo) {
		super(subAssemblyInventoryPanel.getMainWindow(),true);
		setSize(800, 500);
		setLocationRelativeTo(subAssemblyInventoryPanel.getMainWindow());
		this.setZoneName(zoneName);
		this.setProductSpecCode(productSpecCode);
		this.setOrderNo(orderNo);
		initComponents();
	}

	public void initComponents() {
		
		setTitle(SubAssemblyInventoryDetailPanel.panel_title);
		SubAssemblyInventoryDetailPanel subAssemblyInventoryDetailPanel = new SubAssemblyInventoryDetailPanel(this.getZoneName(), this.getProductSpecCode(), this.getOrderNo());
		setContentPane(subAssemblyInventoryDetailPanel);
		pack();
		setVisible(true);
	}


	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
